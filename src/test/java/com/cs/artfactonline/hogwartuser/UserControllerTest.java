package com.cs.artfactonline.hogwartuser;

import com.cs.artfactonline.hogwartuser.dto.UserDto;
import com.cs.artfactonline.system.StatusCode;
import com.cs.artfactonline.system.exception.ObjectNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc(addFilters = false)//ceci desactive la sécurite filter de spring
@SpringBootTest
//@AutoConfigureMockMvc
@ActiveProfiles(value = "dev")
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;


    @Value("${api.endpoint.base-url}")
    String baseUrl;

    List<HogwartUser> users = new ArrayList<>();

    @BeforeEach
    void setUp() {
        //Create the newest users
        HogwartUser user1 = new HogwartUser();
        user1.setId(1);
        user1.setUsername("Massire");
        user1.setEnable(true);
        user1.setPassword("massire");
        user1.setRoles("admin user");
        this.users.add(user1);

        HogwartUser user2 = new HogwartUser();
        user2.setId(2);
        user2.setUsername("BingYang");
        user2.setPassword("bingyang");
        user2.setEnable(true);
        user2.setRoles("user");
        this.users.add(user2);

        HogwartUser user3 = new HogwartUser();
        user3.setId(3);
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
    void testfindAllUsersSuccess() throws Exception {
        //Given
        given(this.userService.findAll()).willReturn(this.users);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/users")
                                                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].username").value("Massire"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].username").value("BingYang"));
    }

    @Test
    void testFinUserByIdSuccess() throws Exception {
        //Given
    given(userService.findById(1)).willReturn(this.users.get(0));

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/users/1")
                                                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find one success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("Massire"));
    }

    @Test
    void testFindByUserNotFound() throws Exception {
        //Given
        given(userService.findById(1)).willThrow(new ObjectNotFoundException("user",1));

        //When and then
        this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not Find user with Id:1:("))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testSaveUserSuccess() throws Exception {
        //Given
       /* UserDto userDto = new UserDto(
                null,
                "massire",
                true,
                "admin user"
        );*/
        HogwartUser hogwartUser = new HogwartUser();
        hogwartUser.setUsername("massire");
        hogwartUser.setPassword("123456");
        hogwartUser.setRoles("admin user");
        hogwartUser.setEnable(true);

        //On recupère les données
        String jsonUser = this.mapper.writeValueAsString(hogwartUser);
        HogwartUser savedUser = new HogwartUser();
        savedUser.setId(1);
        savedUser.setUsername("massire");
        savedUser.setPassword("123456");
        savedUser.setRoles("admin user");

        given(userService.save(Mockito.any(HogwartUser.class))).willReturn(savedUser);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrl+"/users")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Create user success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("massire"));
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        //Given
        UserDto userDto = new UserDto(
                1,
                "massire",
                true,
                "user"
        );
        String jsonUser = this.mapper.writeValueAsString(userDto);
        HogwartUser updatedUser = new HogwartUser();
        updatedUser.setId(1);
        updatedUser.setUsername("massire");
        updatedUser.setEnable(true);
        updatedUser.setRoles("user");

        given(this.userService.update(eq(1) , Mockito.any(HogwartUser.class))).willReturn(updatedUser);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/users/1")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update user success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("massire"));
    }

    @Test
    void testUpdateUserWithNonExistantId() throws Exception {
        //Given
      given(userService.update(eq(5),Mockito.any(HogwartUser.class))).willThrow(new ObjectNotFoundException("user",5));
        UserDto userDto = new UserDto(5,"massire",true,"admin user");
        String jasonUser = this.mapper.writeValueAsString(userDto);
        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/users/5")
                        .content(jasonUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not Find user with Id:5:("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void deleteUserSuccess() throws Exception {
        //Given
        doNothing().when(this.userService).delete(1);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl+"/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete user success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void deleteUserWithNonExistentId() throws Exception {
        //Given
        doThrow(new ObjectNotFoundException("user", 1)).when(this.userService).delete(1);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl+"/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not Find user with Id:1:("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}