package pl.wojciechgunia.wgapi.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class LoginResponse {
    private final String timestamp;
    private final boolean access;
    private final Code code;

    public LoginResponse(boolean access) {
        this.timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.access = access;
        this.code = Code.SUCCESS;
    }
}
