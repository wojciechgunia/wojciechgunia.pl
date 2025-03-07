package pl.wojciechgunia.wgapi.entity;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class FileResponse {
    private final String timestamp;
    private final String message;

    public FileResponse(String message) {
        this.timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.message = message;
    }
}
