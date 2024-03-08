package com.example.cloudflareassignment.controller;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

enum Status {
    OK,
    ERROR
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@lombok.Builder
public class ApiResponse<T> {
    Status status;
    String message;
    T data;

    public static <T> ApiResponse<T> of(Status status, String message, T data) {
        return new ApiResponse<T>(status, message, data);
    }
}
