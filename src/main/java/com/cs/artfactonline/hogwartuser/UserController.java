package com.cs.artfactonline.hogwartuser;

import com.cs.artfactonline.hogwartuser.converter.UserDtoToUserConverter;
import com.cs.artfactonline.hogwartuser.converter.UserToUserDtoConverter;
import com.cs.artfactonline.hogwartuser.dto.UserDto;
import com.cs.artfactonline.system.Result;
import com.cs.artfactonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final UserDtoToUserConverter userDtoToUserConverter;

    public UserController(UserService userService, UserToUserDtoConverter userToUserDtoConverter, UserDtoToUserConverter userDtoToUserConverter) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.userDtoToUserConverter = userDtoToUserConverter;
    }

    @GetMapping
    public Result findAllUsers()
    {

        return  new Result(true, StatusCode.SUCCESS,"Find all success",
                this.userService.findAll().stream()
                        .map(userToUserDtoConverter::convert).collect(Collectors.toList())
                );
    }

    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable Integer userId)
    {
        return  new Result(true, StatusCode.SUCCESS,"Find one success",
                userToUserDtoConverter.convert(this.userService.findById(userId)));
    }

    @PostMapping
    public Result addUser( @Valid @RequestBody HogwartUser hogwartUser)
    {
        //On utilise HogWartUser et non user Dto cart on fera le changement de password prochainement
        return  new Result(true, StatusCode.SUCCESS,"Create user success",
                this.userToUserDtoConverter.convert(this.userService.save(hogwartUser)));
    }

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable Integer userId,@Valid @RequestBody UserDto userDto)
    {
      HogwartUser updatedUser =  this.userService.update(userId, userDtoToUserConverter.convert(userDto));


        return  new Result(true, StatusCode.SUCCESS,"Update user success",
                this.userToUserDtoConverter.convert(updatedUser));
    }

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Integer userId)
    {
        this.userService.delete(userId);

        return new Result(true,StatusCode.SUCCESS,"Delete user success");
    }

}
