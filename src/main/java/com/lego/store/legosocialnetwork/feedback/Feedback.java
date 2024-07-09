package com.lego.store.legosocialnetwork.feedback;

import com.lego.store.legosocialnetwork.general.BaseEntity;
import com.lego.store.legosocialnetwork.lego.Lego;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Feedback extends BaseEntity {

    @Column
    private Double note;
    private String comment;
    @ManyToOne
    @JoinColumn(name = "lego_id")
    private Lego lego;
    @Getter
    @Setter
    @Id
    private Long id;


}
