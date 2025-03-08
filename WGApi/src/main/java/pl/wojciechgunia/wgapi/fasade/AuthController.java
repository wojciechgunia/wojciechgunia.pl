package pl.wojciechgunia.wgapi.fasade;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.wojciechgunia.wgapi.entity.*;
import pl.wojciechgunia.wgapi.exceptions.UserDontExistException;
import pl.wojciechgunia.wgapi.exceptions.UserExistingWithEmail;
import pl.wojciechgunia.wgapi.exceptions.UserExistingWithName;
import pl.wojciechgunia.wgapi.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    public final UserService userService;

    @RequestMapping(path="/register",method = RequestMethod.POST)
    public ResponseEntity<Response> addNewUser(@Valid @RequestBody UserRegisterDTO user) {
        try {
            log.info("--START Register user");
            userService.register(user);
            log.info("--STOP Register user");
            return ResponseEntity.ok(new Response(Code.SUCCESS));
        } catch (UserExistingWithEmail e) {
            log.info("User exist in database with email");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Code.A5));
        } catch (UserExistingWithName e) {
            log.info("User exist in database with name");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Code.A4));
        }
    }

    @RequestMapping(path="/login",method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response) {
        log.info("--TRY Login user");
        return userService.login(response, user);
    }

    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    public ResponseEntity<?> logout(HttpServletResponse response, HttpServletRequest request){
        log.info("--TRY Logout user");
        return userService.logout(request, response);
    }


    @RequestMapping(path="/auto-login",method = RequestMethod.GET)
    public ResponseEntity<?> autoLogin(HttpServletRequest request, HttpServletResponse response) {
        log.info("--TRY Auto-login user");
        return userService.loginByToken(request,response);
    }

    @RequestMapping(path="/logged-in",method = RequestMethod.GET)
    public ResponseEntity<?> loggedIn(HttpServletRequest request, HttpServletResponse response) {
        log.info("--CHECK User logged-in");
        return userService.loggedIn(request,response);
    }


    @RequestMapping(path="/activate",method = RequestMethod.GET)
    public ResponseEntity<Response> activateUser(@RequestParam String uuid) {
        try {
            log.info("--START Activate user");
            userService.activateUser(uuid);
            log.info("--STOP Activate user");
            return ResponseEntity.ok(new Response(Code.SUCCESS));
        } catch(UserDontExistException e) {
            log.info("User dont exist in database");
            return ResponseEntity.status(400).body(new Response(Code.A6));
        }
    }

    @RequestMapping(path="/set-role",method = RequestMethod.PATCH)
    public ResponseEntity<Response> setRole(@RequestBody SetRoleData setRoleData) {
        return userService.setRole(setRoleData);
    }

    @RequestMapping(path="/set-lock",method = RequestMethod.PATCH)
    public ResponseEntity<Response> setRole(@RequestBody SetLockData setLockData) {
        return userService.setLock(setLockData);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin-get")
    public ResponseEntity<?> getAdmin(@RequestParam(required = false) String name_like,
                                      @RequestParam(required = false,defaultValue = "1") int _page,
                                      @RequestParam(required = false,defaultValue = "10") int _limit,
                                      @RequestParam(required = false,defaultValue = "price") String _sort,
                                      @RequestParam(required = false,defaultValue = "asc") String _order) {
        return userService.getUsers(_page,_limit,name_like,_sort,_order);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationMessage handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new ValidationMessage(ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage());
    }
}
