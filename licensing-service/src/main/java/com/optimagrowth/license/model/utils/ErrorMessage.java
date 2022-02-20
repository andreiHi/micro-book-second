package com.optimagrowth.license.model.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {
    private String message;
    private String code;
    private String detail;

    public ErrorMessage(String message, String detail) {
        this.message = message;
        this.detail = detail;
    }

    public ErrorMessage(String message) {
        this(message, "", "");
    }
}
