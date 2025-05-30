package com.metric.backend.common;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiRes<T> {
    private String status;   // "success" or "fail"
    private String message;
    private T data;
}
