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

    private Double note; //TODO  1-5 stars implement
    private String comment;

    @ManyToOne
    @JoinColumn(name = "lego_id", nullable = false)
    private Lego lego;



}
