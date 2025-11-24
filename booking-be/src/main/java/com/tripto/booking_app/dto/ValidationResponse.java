package com.tripto.booking_app.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValidationResponse {
    private boolean valid;

    public ValidationResponse(boolean valid) {
        this.valid = valid;
    }

}
