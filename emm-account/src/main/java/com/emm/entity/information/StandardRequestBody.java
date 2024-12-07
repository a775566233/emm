package com.emm.entity.information;

import com.emm.util.json.Serialize;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@Setter
@Getter
public class StandardRequestBody<T> {
    private T data;
    private String offset;
    private String limit;

    @SneakyThrows
    @Override
    public String toString() {
        return Serialize.toJson(this);
    }
}
