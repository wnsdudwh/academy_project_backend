package com.wnsdudwh.Academy_Project.exception;

// HTTP 상태 코드 404 (Not Found)와 함께 사용될 예외 클래스
public class ResourceNotFoundException extends RuntimeException
{
    public ResourceNotFoundException(String message)
    {
        super(message);
    }
}
