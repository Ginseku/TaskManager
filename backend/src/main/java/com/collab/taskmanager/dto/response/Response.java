package com.collab.taskmanager.dto.response;

public record Response<T>(T data, String message, boolean success) {
    public static <T> Response<T> success(T data) {
        return new Response<>(data, "OK", true);
    }
}