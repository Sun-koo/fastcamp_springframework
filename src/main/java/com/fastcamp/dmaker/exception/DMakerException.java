package com.fastcamp.dmaker.exception;

import com.fastcamp.dmaker.exception.type.DMakerErrorCode;
import lombok.Getter;

@Getter
public class DMakerException extends RuntimeException {
    private DMakerErrorCode dmakerErrorCode;
    private String detailMessage;

    public DMakerException(DMakerErrorCode errorCode) {
        super(errorCode.getMassage());
        this.dmakerErrorCode = errorCode;
        this.detailMessage = errorCode.getMassage();
    }

    public DMakerException(DMakerErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.dmakerErrorCode = errorCode;
        this.detailMessage = detailMessage;
    }
}
