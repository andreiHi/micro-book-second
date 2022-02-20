package com.optimagrowth.license.model.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapper {

    private Object data;
    private Object metadata;
    private List<ErrorMessage> errors;
}
