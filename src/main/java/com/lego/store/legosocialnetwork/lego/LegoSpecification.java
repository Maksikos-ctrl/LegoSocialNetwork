package com.lego.store.legosocialnetwork.lego;

import org.springframework.data.jpa.domain.Specification;

public class LegoSpecification {

    public static Specification<Lego> withOwnerId(Integer ownerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
    }

}
