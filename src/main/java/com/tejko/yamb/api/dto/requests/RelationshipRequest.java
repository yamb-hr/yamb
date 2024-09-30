package com.tejko.yamb.api.dto.requests;

import javax.validation.constraints.NotBlank;

import com.tejko.yamb.domain.enums.RelationshipType;

public class RelationshipRequest {

    @NotBlank
    private RelationshipType type;

    public RelationshipRequest() {}

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }

}
