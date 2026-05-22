package com.aitest.common.result;

import lombok.Data;

import java.util.Map;

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    /**
     * 创建一个成功的Result对象
     *
     * @param data 要返回的数据
     * @param <T>  数据类型
     * @return 返回一个包含成功状态码、成功消息和数据的Result对象
     */
    public static <T> Result<T> success(T data) {
        // 创建一个新的Result对象实例
        Result<T> result = new Result<>();
        // 设置成功状态码为200
        result.setCode(200);
        // 设置成功消息为"success"
        result.setMessage("success");
        // 将传入的数据设置到Result对象中
        result.setData(data);
        // 返回设置完成的Result对象
        return result;
    }

    /**
     * 通用成功返回结果方法
     *
     * @param message 返回的消息内容
     * @param map     额外的数据映射，虽然在此方法中未使用，但保留参数以便后续扩展
     * @param <T>     泛型类型，表示返回结果中数据的类型
     * @return 返回一个成功的Result对象，状态码为200，包含指定的消息
     */
    public static <T> Result<T> success(String message, Map<String, String> map) {
        // 创建一个新的Result对象
        Result<T> result = new Result<>();
        // 设置成功状态码为200
        result.setCode(200);
        // 设置返回的消息内容
        result.setMessage(message);
        // 返回处理后的结果对象
        return result;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return error(code, message);
    }

    public static <T> Result<T> fail(String message) {
        return error(500, message);
    }
}