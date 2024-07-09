package com.lego.store.legosocialnetwork.feedback;

import com.lego.store.legosocialnetwork.general.LegoCatalog;
import com.lego.store.legosocialnetwork.lego.Lego;
import com.lego.store.legosocialnetwork.lego.LegoManager;
import com.lego.store.legosocialnetwork.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final LegoManager legoManager;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackManager feedbackManager;


    public Integer save(FeedbackRequest request, Authentication connectedUser) {
        Lego lego = legoManager.findById(request.legoId())
                .orElseThrow(() -> new EntityNotFoundException("Lego not found"));

        if (lego.isArchived() || !lego.isShareable()) {
            throw new IllegalArgumentException("Lego is not shareable");
        }

        User user = (User) connectedUser.getPrincipal();
        if (lego.getOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You cannot give feedback on your own lego");
        }

        Feedback feedback = feedbackMapper.toFeedback(request);

        return Math.toIntExact(feedbackManager.save(feedback).getId());


    }


    public LegoCatalog<FeedbackResponse> findAllFeedbackByLegoId(Integer legoId, int catalog, int size, Authentication connectedUser) {
        Pageable catalogable = (Pageable) PageRequest.of(catalog, size);
        User user = (User) connectedUser.getPrincipal();
        Page<Feedback> feedbacks = feedbackManager.findAllByLegoId(legoId, catalogable);
        List<FeedbackResponse> feedbackResponse = feedbacks.stream()
                .map(f -> feedbackMapper.toFeedbackResponse(f, user.getId()))
                .toList();
        return new LegoCatalog<>(
                feedbackResponse,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }


}
