package com.emm.entity.information;

import com.emm.util.json.Serialize;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@Setter
@Getter
public class StandardResponseBody<T> {
    private T data;
    private String message;
    private String code;

    public static <T> StandardResponseBody<T> customInfo(ResponseEnum responseEnum) {
        StandardResponseBody<T> message = new StandardResponseBody<T>();
        message.setCode(responseEnum.getCode());
        message.setMessage(responseEnum.getMessage());
        return message;
    }

    public static <T> StandardResponseBody<T> customInfo(ResponseEnum responseEnum, T data) {
        StandardResponseBody<T> message = new StandardResponseBody<T>();
        message.setCode(responseEnum.getCode());
        message.setMessage(responseEnum.getMessage());
        message.setData(data);
        return message;
    }

    public static <T> StandardResponseBody<T> success(T data) {
        return customInfo(ResponseEnum.SUCCESS, data);
    }

    public static <T> StandardResponseBody<T> success() {
        return customInfo(ResponseEnum.SUCCESS);
    }

    @SneakyThrows
    @Override
    public String toString() {
        return Serialize.toJson(this);
    }
}
