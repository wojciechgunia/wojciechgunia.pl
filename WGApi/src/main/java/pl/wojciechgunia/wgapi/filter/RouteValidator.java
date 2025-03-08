package pl.wojciechgunia.wgapi.filter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import pl.wojciechgunia.wgapi.entity.Endpoint;
import pl.wojciechgunia.wgapi.entity.HttpMethod;
import pl.wojciechgunia.wgapi.entity.Role;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public Set<Endpoint> openApiEndpoints = new HashSet<>(List.of(
            new Endpoint("/api/v1/auth/login", HttpMethod.POST, Role.GUEST),
            new Endpoint("/api/v1/auth/activate", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/auth/auto-login", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/content", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/content/language", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/file", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/parameters/type-list", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/parameters/technologyList", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/parameters/it-field-list", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/timeline/list", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/timeline/element", HttpMethod.GET, Role.GUEST)
    )
    );

    private final Set<Endpoint> adminEndpoints = new HashSet<>(List.of(
            new Endpoint("/api/v1/auth/register", HttpMethod.POST, Role.ADMIN),
            new Endpoint("/api/v1/auth/set-role", HttpMethod.PATCH, Role.ADMIN),
            new Endpoint("/api/v1/auth/set-lock", HttpMethod.PATCH, Role.ADMIN),
            new Endpoint("/api/v1/auth/admin-get", HttpMethod.GET, Role.ADMIN),
            new Endpoint("/api/v1/content", HttpMethod.PUT, Role.ADMIN),
            new Endpoint("/api/v1/content/language", HttpMethod.POST, Role.ADMIN),
            new Endpoint("/api/v1/content/language", HttpMethod.DELETE, Role.ADMIN),
            new Endpoint("/api/v1/file", HttpMethod.POST, Role.ADMIN),
            new Endpoint("/api/v1/file", HttpMethod.DELETE, Role.ADMIN),
            new Endpoint("/api/v1/file", HttpMethod.PATCH, Role.ADMIN),
            new Endpoint("/api/v1/get-all", HttpMethod.GET, Role.ADMIN),
            new Endpoint("/api/v1/parameters/technologyList", HttpMethod.POST, Role.ADMIN),
            new Endpoint("/api/v1/parameters/it-field-list", HttpMethod.POST, Role.ADMIN),
            new Endpoint("/api/v1/parameters/technologyList", HttpMethod.PUT, Role.ADMIN),
            new Endpoint("/api/v1/parameters/it-field-list", HttpMethod.PUT, Role.ADMIN),
            new Endpoint("/api/v1/parameters/technologyList", HttpMethod.DELETE, Role.ADMIN),
            new Endpoint("/api/v1/parameters/it-field-list", HttpMethod.DELETE, Role.ADMIN),
            new Endpoint("/api/v1/timeline/element", HttpMethod.POST, Role.ADMIN),
            new Endpoint("/api/v1/timeline/element", HttpMethod.PUT, Role.ADMIN),
            new Endpoint("/api/v1/timeline/element", HttpMethod.DELETE, Role.ADMIN)
    )
    );

    private final Set<Endpoint> userEndpoints = new HashSet<>(List.of(
            new Endpoint("/api/v1/auth/logout", HttpMethod.GET, Role.USER)
    )
    );

    public Predicate<HttpServletRequest> isAdmin =
            request -> adminEndpoints
                    .stream().anyMatch(value -> request.getRequestURI().contains(value.getUrl())
                            && request.getMethod().equalsIgnoreCase(value.getHttpMethod().name()));

    public Predicate<HttpServletRequest> isUser =
            request -> userEndpoints
                    .stream().anyMatch(value -> request.getRequestURI().contains(value.getUrl())
                            && request.getMethod().equalsIgnoreCase(value.getHttpMethod().name()));

    public Predicate<HttpServletRequest> isSecure =
            request -> openApiEndpoints
                    .stream().noneMatch(value -> request.getRequestURI().contains(value.getUrl())
                            && request.getMethod().equalsIgnoreCase(value.getHttpMethod().name()));

//    private boolean matchesEndpoint(HttpServletRequest request, Endpoint endpoint) {
//        return request.getRequestURI().contains(endpoint.getUrl())
//                && request.getMethod().equalsIgnoreCase(endpoint.getHttpMethod().name());
//    }

    public boolean hasRole(HttpServletRequest request, Collection<? extends GrantedAuthority> authorities) {
        if (isAdmin.test(request)) {
            return authorities.stream().anyMatch(auth -> auth.getAuthority().equals(Role.ADMIN.name()));
        }
        if (isUser.test(request)) {
            return authorities.stream().anyMatch(auth -> auth.getAuthority().equals(Role.USER.name()));
        }
        return true;
    }
}