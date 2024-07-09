package com.lego.store.legosocialnetwork.lego;


import com.lego.store.legosocialnetwork.file.FileUtils;
import com.lego.store.legosocialnetwork.history.LegoTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class LegoMapper {


    public Lego toLego(LegoRequest request) {
        return Lego.builder().id(Long.valueOf(request.id())).title(request.title()).creatorName(request.creatorName()).setDescription(request.setDescription()).archived(false).shareable(request.shareable()).build();
    }

    public LegoResponse toLegoResponse(Lego lego) {
        return LegoResponse.builder()
                .id(lego.getId()
                .intValue())
                .title(lego.getTitle())
                .creatorName(lego.getCreatorName())
                .legoItemNumber(lego.getLegoItemNumber())
                .setDescription(lego.getSetDescription())
                .owner(lego.getOwner().fullName())
                // TODO: implement a bit later
                .cover(FileUtils.readFileFromLocation(lego.getLegoThemeCover()))
                .rate(lego.getRate())
                .archived(lego.isArchived())
                .shareable(lego.isShareable())
                .build();
    }

    public BorrowedLegoResponse toBorrowedLegoResponse(LegoTransactionHistory history) {
        return  BorrowedLegoResponse.builder()
                .id(history.getLego().getId()
                .intValue())
                .title(history.getLego().getTitle())
                .creatorName(history.getLego().getCreatorName())
                .legoItemNumber(history.getLego().getLegoItemNumber())
                .owner(history.getLego().getOwner().fullName())
                // TODO: implement a bit later
//                .cover(lego.getCover())
                .rate(history.getLego().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}
