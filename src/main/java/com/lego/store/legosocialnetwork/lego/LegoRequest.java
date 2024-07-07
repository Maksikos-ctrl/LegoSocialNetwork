package com.lego.store.legosocialnetwork.lego;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LegoRequest(Integer id, @NotNull(message = "100") @NotEmpty(message = "100") String title, @NotNull(message = "101") @NotEmpty(message = "101") String creatorName,  @NotNull(message = "102") @NotEmpty(message = "102") String legoItemNumber,  @NotNull(message = "103") @NotEmpty(message = "103") String setDescription, boolean shareable) {


}
