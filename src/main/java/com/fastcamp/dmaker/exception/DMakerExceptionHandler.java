package com.fastcamp.dmaker.exception;

import com.fastcamp.dmaker.dto.DMakerErrorResponse;
import com.fastcamp.dmaker.exception.type.DMakerErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class DMakerExceptionHandler {

    @ExceptionHandler(DMakerException.class)
    public DMakerErrorResponse handleException(
            DMakerException e,
            HttpServletRequest request
    ) {
        log.error("ErrorCode : {}, url : {}, message : {}",
                e.getDmakerErrorCode(), request.getRequestURI(), e.getDetailMessage());

        return DMakerErrorResponse.builder()
                .errorCode(e.getDmakerErrorCode())
                .errorMessage(e.getDetailMessage())
                .build();
    }

    // Http Method 매칭 에러
    // Parameter Invalidate
    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentNotValidException.class
    })
    public DMakerErrorResponse handleBadRequest(
            Exception e,
            HttpServletRequest request
    ) {
        log.error("url : {}, message : {}",
                request.getRequestURI(), e.getMessage());

        return DMakerErrorResponse.builder()
                .errorCode(DMakerErrorCode.INVALID_REQUEST)
                .errorMessage(DMakerErrorCode.INVALID_REQUEST.getMassage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public DMakerErrorResponse handleException(
            Exception e,
            HttpServletRequest request
    ) {
        log.error("url : {}, message : {}",
                request.getRequestURI(), e.getMessage());

        return DMakerErrorResponse.builder()
                .errorCode(DMakerErrorCode.INTERNAL_SERVER_ERROR)
                .errorMessage(DMakerErrorCode.INTERNAL_SERVER_ERROR.getMassage())
                .build();
    }
}
