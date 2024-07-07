package com.lego.store.legosocialnetwork.lego;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LegoResponse {

    private Integer id;
    private String title;
    private String creatorName;
    private String legoItemNumber;
    private String setDescription;
    private String owner;
    private byte[] cover;
    private double rate;
    private boolean archived;
    private boolean shareable;


}
