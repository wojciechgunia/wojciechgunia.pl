package pl.wojciechgunia.wgapi.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
public class UserLoginDTO {
    @Length(min = 3, max = 50, message = "Login should be between 3 and 50 characters long")
    private String login;
    @Email
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Length(min = 5, max = 75, message = "The password should be between 5 and 75 characters long")
    private String password;
    private Role role;
}
