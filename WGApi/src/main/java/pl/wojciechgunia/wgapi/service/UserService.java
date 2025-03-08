package pl.wojciechgunia.wgapi.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wojciechgunia.wgapi.configuration.CustomUserDetailsService;
import pl.wojciechgunia.wgapi.entity.*;
import pl.wojciechgunia.wgapi.exceptions.TokenExpiredException;
import pl.wojciechgunia.wgapi.exceptions.UserDontExistException;
import pl.wojciechgunia.wgapi.exceptions.UserExistingWithEmail;
import pl.wojciechgunia.wgapi.exceptions.UserExistingWithName;
import pl.wojciechgunia.wgapi.repository.UserRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CookieService cookieService;
    private final CustomUserDetailsService customUserDetailsService;
    //private final EmailService emailService;
    @Value("${jwt.exp}")
    private int exp;
    @Value("${jwt.refresh.exp}")
    private int refreshExp;

    @PersistenceContext
    EntityManager entityManager;

    private void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
    }

    public String generateToken(String username,int exp) {
        return jwtService.generateToken(username,exp);
    }

    public void validateToken(HttpServletRequest request, HttpServletResponse response) throws ExpiredJwtException, IllegalArgumentException {
        String token = null;
        String refresh = null;
        if (request.getCookies() != null) {
            for (Cookie value : Arrays.stream(request.getCookies()).toList()) {
                if (value.getName().equals("Authorization")) {
                    token = value.getValue();
                } else if (value.getName().equals("Refresh")) {
                    refresh = value.getValue();
                }
            }
        } else {
            log.info("Token can't be null, cant login");
            throw new IllegalArgumentException("Token can't be null");
        }
        if (token != null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtService.extractUsername(token));
            if(!jwtService.validateToken(token, userDetails) && refresh != null) {
                userDetails = customUserDetailsService.loadUserByUsername(jwtService.extractUsername(refresh));
                refreshToken(response, refresh, userDetails);
            }
        } else if(refresh != null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtService.extractUsername(refresh));
            refreshToken(response, refresh, userDetails);
        }
    }

    private void refreshToken(HttpServletResponse response, String refresh, UserDetails userDetails) {
        if(jwtService.validateToken(refresh, userDetails)) {
            Cookie refreshCookie = cookieService.generateCookie("Refresh", jwtService.refreshToken(refresh,refreshExp), refreshExp);
            Cookie cookie = cookieService.generateCookie("Authorization", jwtService.refreshToken(refresh,exp), exp);
            response.addCookie(cookie);
            response.addCookie(refreshCookie);
        } else {
            log.info("Token expired");
            throw new TokenExpiredException("Token expired");
        }
    }

    public void register(UserRegisterDTO userRegisterDTO) throws UserExistingWithEmail,UserExistingWithName {
        userRepository.findUserByLogin(userRegisterDTO.getLogin()).ifPresent(_ ->{
            log.info("User already exist with this name");
            throw new UserExistingWithName("User with name already exists");
        });
        userRepository.findUserByEmail(userRegisterDTO.getEmail()).ifPresent(_ ->{
            log.info("User already exist with this email");
            throw new UserExistingWithEmail("User with email already exists");
        });
        User user = new User();
        user.setLock(false);
        user.setEnabled(true);
        user.setLogin(userRegisterDTO.getLogin());
        user.setName(userRegisterDTO.getName());
        user.setSurname(userRegisterDTO.getSurname());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(userRegisterDTO.getPassword());
        user.setRole(Role.USER);
        saveUser(user);
        //emailService.sendActivation(user);
    }

    public ResponseEntity<?> login(HttpServletResponse response, User authRequest) {
        log.info("--START Login Service");
        User user = userRepository.findUserByLoginAndLockAndEnabled(authRequest.getUsername()).orElse(null);
        if (user != null) {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            log.info("--STOP Login Service");
            if (authenticate.isAuthenticated()) {
                Cookie refresh = cookieService.generateCookie("Refresh", generateToken(authRequest.getUsername(),refreshExp), refreshExp);
                Cookie cookie = cookieService.generateCookie("Authorization", generateToken(authRequest.getUsername(),exp), exp);
                response.addCookie(cookie);
                response.addCookie(refresh);
                return ResponseEntity.ok(
                        UserLoginDTO
                                .builder()
                                .login(user.getUsername())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .build());
            } else {
                return ResponseEntity.ok(new Response(Code.A1));
            }
        }
        log.info("User dont exist");
        log.info("--STOP Login Service");
        return ResponseEntity.ok(new Response(Code.A2));
    }

    public ResponseEntity<?> loginByToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            validateToken(request,response);
            String refresh=null;
            for (Cookie value : Arrays.stream(request.getCookies()).toList()) {
                if (value.getName().equals("Refresh")) {
                    refresh = value.getValue();
                }
            }
            String login = jwtService.extractUsername(refresh);
            User user = userRepository.findUserByLoginAndLockAndEnabled(login).orElse(null);
            if (user != null) {
                return ResponseEntity.ok(UserLoginDTO.builder().login(user.getUsername()).email(user.getEmail()).role(user.getRole()).build());
            }
            log.info("Cant login user dont exist");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response(Code.A1));
        } catch(ExpiredJwtException | IllegalArgumentException e) {
            log.info("Token expired or is null, cant login");
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response(Code.A3));
        }
    }

    public ResponseEntity<LoginResponse> loggedIn(HttpServletRequest request, HttpServletResponse response) {
        try {
            validateToken(request,response);
            return  ResponseEntity.ok(new LoginResponse(true));
        } catch(ExpiredJwtException | IllegalArgumentException e) {
            return  ResponseEntity.ok(new LoginResponse(false));
        }
    }

    public void activateUser(String uid) throws UserDontExistException {
        User user = userRepository.findUserByUuid(uid).orElse(null);
        if(user != null) {
            user.setLock(false);
            user.setEnabled(true);
            userRepository.save(user);
            return;
        }
        log.info("User don't exist");
        throw new UserDontExistException("User don't exist");

    }

    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Delete all cookies");
        Cookie cookie = cookieService.removeCookie(request.getCookies(),"Authorization");
        if (cookie != null){
            response.addCookie(cookie);
        }
        cookie = cookieService.removeCookie(request.getCookies(),"Refresh");
        if (cookie != null){
            response.addCookie(cookie);
        }
        return  ResponseEntity.ok(new Response(Code.SUCCESS));
    }

    public ResponseEntity<Response> setRole(SetRoleData setRoleData) {
        try {
            userRepository.findUserByLoginAndUuid(setRoleData.getLogin(),setRoleData.getUuid()).ifPresentOrElse(value->{
                value.setRole(Role.valueOf(setRoleData.getRole()));
                userRepository.save(value);
            },()->{throw new UserDontExistException("User not found");});
            return ResponseEntity.ok(new Response(Code.SUCCESS));
        } catch (UserDontExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Code.A2));
        }
    }

    public ResponseEntity<Response> setLock(SetLockData setLockData) {
        try {
            userRepository.findUserByLoginAndUuid(setLockData.getLogin(),setLockData.getUuid()).ifPresentOrElse(value->{
                value.setLock(setLockData.isLock());
                userRepository.save(value);
            },()->{throw new UserDontExistException("User not found");});
            return ResponseEntity.ok(new Response(Code.SUCCESS));
        } catch (UserDontExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Code.A2));
        }
    }

    public ResponseEntity<?> getUsers(int page, int limit, String name, String sort, String order) {
        if((name != null && !name.isEmpty())) {
            name = URLDecoder.decode(name, StandardCharsets.UTF_8);
        }
        List<UserDTO> userDTOS = new ArrayList<>();
        getAdminUsers(name, page, limit, sort, order).forEach(value -> userDTOS.add(new UserDTO(value.getUuid(), value.getLogin(), value.getEmail(), value.getPassword(), value.getRole(), value.isLock(), value.isEnabled())));
        long totalCount = userRepository.count();
        return ResponseEntity.ok().header("X-Total-Count",String.valueOf(totalCount)).body(userDTOS);
    }

    private List<User> getAdminUsers(String name, int page, int limit, String sort, String order) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        page=lowerThanOne(page);
        limit=lowerThanOne(limit);
        List<Predicate> predicates = prepareQuery(name,criteriaBuilder,root);

        if(!order.isEmpty() && !sort.isEmpty()) {
            String column = switch (sort) {
                case "email" -> "email";
                case "role" -> "role";
                default -> "login";
            };
            Order orderQuery;
            if(order.equals("desc")) {
                orderQuery = criteriaBuilder.desc(root.get(column));
            } else {
                orderQuery = criteriaBuilder.asc(root.get(column));
            }
            query.orderBy(orderQuery);
        }

        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).setFirstResult((page-1)*limit).setMaxResults(limit).getResultList();
    }

    private int lowerThanOne(int number) {
        return Math.max(number, 1);
    }

    private List<Predicate> prepareQuery(String name, CriteriaBuilder criteriaBuilder,Root<User> root) {
        List<Predicate> predicates = new ArrayList<>();
        if(name != null && !name.trim().isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("login")), "%" + name.toLowerCase() + "%"));
        }
        return predicates;
    }
}
