package com.cs.artfactonline.hogwartuser;

import com.cs.artfactonline.system.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration test for user API endpoint")
@Tag("integration")
@ActiveProfiles(value = "dev")
public class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Value("${api.endpoint.base-url}")
    String baseurl;

    @Mock
    PasswordEncoder passwordEncoder;

    String token;

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    void setUp() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .post(this.baseurl+"/users/login").with(httpBasic("massire","123456")));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        this.token = "Bearer "+ json.getJSONObject("data").getString("token");
    }

    @Test
        //@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testFindAllArtifactSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(baseurl+"/users")
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)));
    }

   @Test
   @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAddUserSuccess() throws Exception {
        //Given
       com.cs.artfactonline.hogwartuser.HogwartUser user = new HogwartUser();
        user.setUsername("malik");
        user.setEnable(true);
        user.setRoles("admin user");
        user.setPassword("123456");

       //UserDto userDto = new UserDto(null,"lolo",true,"admin user");

        String jsonUser = this.mapper.writeValueAsString(user);
        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.post(baseurl+"/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",this.token)
                        .content(jsonUser)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Create user success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                //.andExpect(jsonPath("$.data.name").value(hogwartUser.getUsername()))
                .andExpect(jsonPath("$.data.roles").value(user.getRoles()));

        this.mockMvc.perform(MockMvcRequestBuilders.get(baseurl+"/users")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)));
    }

    @Test
    void testFindUserByIdSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseurl + "/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find one success"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.username").value("BingYang"));
    }

    @Test
    void testFindUserByIdNotFound() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseurl + "/users/5").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not Find user with Id:5:("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddUserErrorWithInvalidInput() throws Exception {
        HogwartUser hogwartsUser = new HogwartUser();
        hogwartsUser.setUsername(""); // Username is not provided.
        hogwartsUser.setPassword(""); // Password is not provided.
        hogwartsUser.setEnable(true);
        hogwartsUser.setRoles(""); // Roles field is not provided.

        String json = this.mapper.writeValueAsString(hogwartsUser);

        this.mockMvc.perform(MockMvcRequestBuilders.post(this.baseurl + "/users").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided Arguments are invalid, see data for details. "))
                .andExpect(jsonPath("$.data.username").value("username is required"))
                .andExpect(jsonPath("$.data.password").value("password is required"))
                .andExpect(jsonPath("$.data.roles").value("roles ares required"));
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseurl + "/users").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)));
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        HogwartUser hogwartsUser = new HogwartUser();
        hogwartsUser.setUsername("tom123"); // Username is changed. It was tom.
        hogwartsUser.setEnable(false);
        hogwartsUser.setRoles("user");

        String json = this.mapper.writeValueAsString(hogwartsUser);

        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseurl + "/users/2").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update user success"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.username").value("tom123"))
                .andExpect(jsonPath("$.data.enable").value(false))
                .andExpect(jsonPath("$.data.roles").value("user"));
    }

    @Test
    void testUpdateUserErrorWithNonExistentId() throws Exception {
        HogwartUser hogwartsUser = new HogwartUser();
        hogwartsUser.setId(5); // This id does not exist in the database.
        hogwartsUser.setUsername("john123"); // Username is changed.
        hogwartsUser.setEnable(true);
        hogwartsUser.setRoles("admin user");

        String json = this.mapper.writeValueAsString(hogwartsUser);

        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseurl + "/users/5").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not Find user with Id:5:("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testUpdateUserErrorWithInvalidInput() throws Exception {
        HogwartUser hogwartsUser = new HogwartUser();
        hogwartsUser.setId(1); // Valid id
        hogwartsUser.setUsername(""); // Updated username is empty.
        hogwartsUser.setEnable(false);
        hogwartsUser.setRoles(""); // Updated roles field is empty.

        String json = this.mapper.writeValueAsString(hogwartsUser);

        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseurl + "/users/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided Arguments are invalid, see data for details. "))
                .andExpect(jsonPath("$.data.username").value("usernamee is required."))
                .andExpect(jsonPath("$.data.roles").value("roles are required."));
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseurl + "/users/1").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find one success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("massire"));
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.baseurl + "/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete user success"))
                .andExpect(jsonPath("$.data").isEmpty());
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseurl + "/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not Find user with Id:2:("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUserErrorWithNonExistentId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.baseurl + "/users/5").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not Find user with Id:5:("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUserNoAccessAsRoleUser() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post(this.baseurl + "/users/login").with(httpBasic("BingYang", "123456"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String ericToken = "Bearer " + json.getJSONObject("data").getString("token");

        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.baseurl + "/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, ericToken))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No perission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseurl + "/users").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].username").value("massire"));
    }


}
