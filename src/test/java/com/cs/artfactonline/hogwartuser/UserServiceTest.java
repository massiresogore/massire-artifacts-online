package com.cs.artfactonline.hogwartuser;

import com.cs.artfactonline.artifact.utils.IdWorker;
import com.cs.artfactonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "dev")
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Mock
    IdWorker idWorker;
    List<HogwartUser> users = new ArrayList<>();

    @BeforeEach
    void setUp() {
        //Create the newest users
        HogwartUser user1 = new HogwartUser();
        user1.setUsername("Massire");
        user1.setEnable(true);
        user1.setPassword("massire");
        user1.setRoles("admin user");
        this.users.add(user1);

        HogwartUser user2 = new HogwartUser();
        user2.setUsername("BingYang");
        user2.setPassword("bingyang");
        user2.setEnable(true);
        user2.setRoles("user");
        this.users.add(user2);

        HogwartUser user3 = new HogwartUser();
        user3.setUsername("Binta");
        user3.setPassword("binta");
        user3.setEnable(false);
        user3.setRoles("user");
        this.users.add(user3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllSuccess() {
        //Given
        given(this.userRepository.findAll()).willReturn(this.users);

        //When
        List<HogwartUser> actualUsers = this.userService.findAll();

        //Then
        assertThat(actualUsers.size()).isEqualTo(this.users.size());
        verify(userRepository,times(1)).findAll();
    }

    @Test
    void testFindUserByIdSuccess()
    {
        HogwartUser user1 = new HogwartUser();
        user1.setId(1);
        user1.setUsername("Massire");
        user1.setEnable(true);
        user1.setPassword("massire");
        user1.setRoles("admin user");

        //Given
        given(userRepository.findById(1)).willReturn(Optional.of(user1));

        //When
        HogwartUser returnUser = userService.findById(1);

        //Then
        assertThat(returnUser.getId()).isEqualTo(user1.getId());
        assertThat(returnUser.getUsername()).isEqualTo(user1.getUsername());
        assertThat(returnUser.getRoles()).isEqualTo(user1.getRoles());
        verify(userRepository,times(1)).findById(1);

    }

    @Test
    void testFindUserByIdNotFound()
    {
        //Given
        given(userRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(()->{
            HogwartUser returnUser = userService.findById(1);
        });

        //Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not Find user with Id:1:(");
        verify(userRepository,times(1)).findById(1);
    }

    @Test
    void testAddUserSuccess()
    {

        //Given
        HogwartUser user = new HogwartUser();
        user.setId(1);
        user.setUsername("Massire");
        user.setEnable(true);
        user.setPassword("massire");
        user.setRoles("admin user");

        given(this.passwordEncoder.encode(user.getPassword())).willReturn("Encoded Password");
        given(userRepository.save(user)).willReturn(user);

        //When
       HogwartUser savedUser =userService.save(user);

        //Then
        assertThat(savedUser.getId()).isEqualTo(1);
        assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(savedUser.getRoles()).isEqualTo(user.getRoles());
        verify(userRepository,times(1)).save(user);
    }

    @Test
    void testUpdateUserSuccess()
    {

        //Given
        //envoyé au formulaire
        HogwartUser oldUser = new HogwartUser();
        oldUser.setId(1);
        oldUser.setUsername("Massire");
        oldUser.setEnable(true);
        oldUser.setPassword("massire");
        oldUser.setRoles("admin user");

        //crée de la base de donnée
        HogwartUser update = new HogwartUser();
        update.setId(1);
        update.setUsername("Massire");
        update.setEnable(true);
        update.setRoles("admin user");

        given(userRepository.findById(1)).willReturn(Optional.of(oldUser));
        given(userRepository.save(oldUser)).willReturn(oldUser);

        //When
        HogwartUser updatedUser = userService.update(1,update);

        //Then
        assertThat(updatedUser.getId()).isEqualTo(1);
        assertThat(updatedUser.getUsername()).isEqualTo("Massire");
        assertThat(updatedUser.getRoles()).isEqualTo("admin user");
        verify(userRepository,times(1)).findById(1);
        verify(userRepository,times(1)).save(oldUser);
    }

    @Test
    void testUpdateUserNotFound()
    {
        //Given
        HogwartUser update = new HogwartUser();
        update.setId(1);
        update.setUsername("massire");
        update.setRoles("user");
        update.setPassword("massire");

        given(userRepository.findById(1)).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class,()->{
           userService.update(1,update);
        });

        //Then
        verify(userRepository,times(1)).findById(1);
    }

    @Test
    void testDeleUserSuccess()
    {
        //Given
        HogwartUser user = new HogwartUser();
        user.setId(1);
        user.setUsername("massire");
        user.setEnable(true);
        user.setRoles("user");
        given(userRepository.findById(1)).willReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1);

        //When
        userService.delete(1);

        //Then
        verify(userRepository,times(1)).findById(1);

    }

    @Test
    void testDeleteUserNotFound()
    {
        //Given
        given(userRepository.findById(1)).willReturn(Optional.empty());

        //Then
        assertThrows(ObjectNotFoundException.class,()->{
            userService.delete(1);
        });

        //When
        verify(userRepository,times(1)).findById(1);
    }
}