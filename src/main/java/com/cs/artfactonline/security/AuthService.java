package com.cs.artfactonline.security;

import com.cs.artfactonline.hogwartuser.HogwartUser;
import com.cs.artfactonline.hogwartuser.MyUserPrincipal;
import com.cs.artfactonline.hogwartuser.converter.UserToUserDtoConverter;
import com.cs.artfactonline.hogwartuser.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final JwtProvider jwtProvider;
    private final UserToUserDtoConverter userToUserDtoConverter;

    public AuthService(JwtProvider jwtProvider, UserToUserDtoConverter userToUserDtoConverter) {
        this.jwtProvider = jwtProvider;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }


    public Map<String, Object> createLoginInfo(Authentication authentication) {
        //Create UserInfo.
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        HogwartUser user = principal.getHogwartUser();
        UserDto userDto = this.userToUserDtoConverter.convert(user);


        //Craete JWT
       String token =  this.jwtProvider.createToken(authentication);

       Map<String, Object> loginResultMap = new HashMap<>();

       loginResultMap.put("userInfo",userDto);
       loginResultMap.put("token",token);

        return loginResultMap;
    }
}
