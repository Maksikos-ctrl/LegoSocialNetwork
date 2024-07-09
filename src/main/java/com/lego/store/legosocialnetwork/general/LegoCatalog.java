package com.lego.store.legosocialnetwork.general;


import com.lego.store.legosocialnetwork.feedback.FeedbackResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LegoCatalog<T> {

    private List<T> content;
    private int num;
    private int size;
    private long totalElems;
    private int totalCatalogs;
    private boolean first;
    private boolean last;



}
