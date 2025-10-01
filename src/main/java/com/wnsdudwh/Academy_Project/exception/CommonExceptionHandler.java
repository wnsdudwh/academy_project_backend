package com.wnsdudwh.Academy_Project.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex)
    {
        // 로그 찍고
        log.error(ex.getMessage());

        // 클라이언트에게 응답
        return new ResponseEntity<>("서버 오류 발생 : "+ ex.getMessage() ,HttpStatus.BAD_REQUEST);
    }
}
