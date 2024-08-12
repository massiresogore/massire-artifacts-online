package com.cs.artfactonline.security;

import com.cs.artfactonline.system.Result;
import com.cs.artfactonline.system.StatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class AuthController {
    private final AuthService authService;
    private static final Logger LOGGER= LoggerFactory.getLogger(AuthController.class);
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result getLoginInfo(Authentication authentication)
    {
        //ceci affichera le nom d'utilisatteur
        LOGGER.debug("Autheticated user: $'{}'", authentication.getName());


        return new Result(true,
                StatusCode.SUCCESS,
                "user info and json web token",
                this.authService.createLoginInfo(authentication)
                //le controller va sérealiser le Map en Json String
        );
    }
}
