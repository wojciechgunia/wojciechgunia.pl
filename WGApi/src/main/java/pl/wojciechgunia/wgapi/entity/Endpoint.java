package pl.wojciechgunia.wgapi.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Endpoint {
    private String url;
    private HttpMethod httpMethod;

    private Role role;

    public Endpoint(String url, HttpMethod httpMethod, Role role) {
        super();
        this.url = url;
        this.httpMethod = httpMethod;
        this.role = role;
    }

}
