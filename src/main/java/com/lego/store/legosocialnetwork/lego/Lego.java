package com.lego.store.legosocialnetwork.lego;

import com.lego.store.legosocialnetwork.feedback.Feedback;
import com.lego.store.legosocialnetwork.general.BaseEntity;
import com.lego.store.legosocialnetwork.history.LegoTransactionHistory;
import com.lego.store.legosocialnetwork.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Lego extends BaseEntity {

    private String title;
    private String creatorName;
    private String legoItemNumber;
    private String setDescription;
    private String legoThemeCover;
    private boolean archived;
    private boolean shareable;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false) // by default hibernate uses owner_id as the name
    private User owner;

    @OneToMany(mappedBy = "lego")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "lego")
    private List<LegoTransactionHistory> transactionHistories;
    @Getter
    @Setter
    @Id
    private Long id;

    @Transient
    public double getRate() {

        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        var rate = this.feedbacks.stream().mapToDouble(Feedback::getNote).average().orElse(0); // 3.23 -> 3.0 || 3.65 -> 4.0
        double roundedRate = Math.round(rate * 10.0) / 10.0;
        return roundedRate;
    }
}
