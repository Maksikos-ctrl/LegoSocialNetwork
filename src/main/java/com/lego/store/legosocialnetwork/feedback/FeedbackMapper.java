package com.lego.store.legosocialnetwork.feedback;

import com.lego.store.legosocialnetwork.lego.Lego;
import org.springframework.stereotype.Service;

@Service
public class FeedbackMapper {
    public Feedback toFeedback(FeedbackRequest request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .lego(Lego.builder()
                        .id(Long.valueOf(request.legoId()))
                        .archived(false) //? Required only for primitive data types to satisfy lombok :)
                        .shareable(false)
                        .build())
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback f, Integer id) {
        return FeedbackResponse.builder()
                .note(f.getNote())
                .comment(f.getComment())
                .ownFeedback(f.getLego().getOwner().getId().equals(id))
                .build();
    }
}
