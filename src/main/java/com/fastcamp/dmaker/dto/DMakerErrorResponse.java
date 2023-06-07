package com.fastcamp.dmaker.dto;

import com.fastcamp.dmaker.exception.type.DMakerErrorCode;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DMakerErrorResponse {
    private DMakerErrorCode errorCode;
    private String errorMessage;
}
