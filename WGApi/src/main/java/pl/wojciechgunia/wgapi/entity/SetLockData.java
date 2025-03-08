package pl.wojciechgunia.wgapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SetLockData {
    private String uuid;
    private String login;
    private boolean lock;
}
