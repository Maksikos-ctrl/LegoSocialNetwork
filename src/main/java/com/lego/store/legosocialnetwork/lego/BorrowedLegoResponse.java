package com.lego.store.legosocialnetwork.lego;


import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedLegoResponse {

    private Integer id;
    private String title;
    private String creatorName;
    private String legoItemNumber;
    private String owner;
    private double rate;
    private boolean returned;
    private boolean returnApproved;


}

