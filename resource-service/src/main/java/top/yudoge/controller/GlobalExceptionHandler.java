package top.yudoge.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import top.yudoge.constants.ResponseCodes;
import top.yudoge.exceptions.ResourceNotFoundException;
import top.yudoge.exceptions.UserAuthenticatException;
import top.yudoge.exceptions.UserNotFoundException;
import top.yudoge.pojos.ResponseObject;
import top.yudoge.pojos.ResponseObjectBuilder;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseObject faildWithCode(int code) {
        return ResponseObjectBuilder.faild(code).build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = UserAuthenticatException.class)
    public ResponseObject userAuthenticationException() {
        return faildWithCode(ResponseCodes.USER_AUTHENTICATION_FAILD);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseObject resourceNotFoundException() {
        return faildWithCode(ResponseCodes.RESOURCE_NOT_FOUND);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Throwable.class)
    public ResponseObject serverInternelException(Throwable e) {
        log.warn("ServerInternalException Catched!", e);
        return faildWithCode(ResponseCodes.SERVER_INTERNAL_ERROR);
    }

}
