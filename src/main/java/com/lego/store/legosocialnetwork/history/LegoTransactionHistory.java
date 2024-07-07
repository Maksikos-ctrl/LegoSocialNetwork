package com.lego.store.legosocialnetwork.history;

import com.lego.store.legosocialnetwork.general.BaseEntity;
import com.lego.store.legosocialnetwork.lego.Lego;
import com.lego.store.legosocialnetwork.user.User;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
public class LegoTransactionHistory extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "lego_id", nullable = false)
    private Lego lego;

    //TODO: implement user relatioship
    //TODO: implement lego relatioship

    private boolean returned;
    private boolean returnApproved;
    @Getter
    @Setter
    @Id
    private Long id;


}
