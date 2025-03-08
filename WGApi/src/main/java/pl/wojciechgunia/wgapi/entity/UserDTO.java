package pl.wojciechgunia.wgapi.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {
    private String uuid;
    private String login;
    private String email;
    private  String password;
    private Role role;
    private boolean isLock;
    private boolean isEnabled;

    public UserDTO(String uuid, String login, String email, String password, Role role, boolean isLock, boolean isEnabled) {
        this.uuid = uuid;
        this.login = login;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isLock = isLock;
        this.isEnabled = isEnabled;
    }

}
