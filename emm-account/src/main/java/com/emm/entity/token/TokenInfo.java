package com.emm.entity.token;

import com.emm.util.token.Operation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenInfo<T> {
    private T data;
    private Operation operation;

    public TokenInfo(T data, Operation operation) {
        this.data = data;
        this.operation = operation;
    }
}
