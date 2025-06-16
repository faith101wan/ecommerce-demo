package com.cw.ecommerce.config;

import lombok.Data;
import lombok.AllArgsConstructor;

/**
 * 统一API响应封装
 */
@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;      // 状态码（200=成功，其他为错误码）
    private String message;// 提示信息
    private T data;        // 响应数据

    // 成功响应（无数据）
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "success", null);
    }

    // 成功响应（带数据）
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }

    // 错误响应（基础版）
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    // 错误响应（带数据）
    public static <T> ApiResponse<T> error(int code, String message, T errorData) {
        return new ApiResponse<>(code, message, errorData);
    }

    // ---------- 预定义常用错误 ----------
    public static <T> ApiResponse<T> badRequest(String message) {
        return error(400, message);
    }

    public static <T> ApiResponse<T> unauthorized(String message) {
        return error(401, message);
    }

    public static <T> ApiResponse<T> forbidden(String message) {
        return error(403, message);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return error(404, message);
    }

    public static <T> ApiResponse<T> internalError(String message) {
        return error(500, message);
    }
}