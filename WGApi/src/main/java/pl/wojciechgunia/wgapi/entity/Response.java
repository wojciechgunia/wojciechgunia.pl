package pl.wojciechgunia.wgapi.entity;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class Response {
    private final String timestamp;
    private final String message;
    private final Code code;

    public Response(Code code) {
        this.timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.message = code.label;
        this.code = code;
    }
}
