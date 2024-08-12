package com.cs.artfactonline.artifact;

import com.cs.artfactonline.artifact.dto.ArtifactDto;
import com.cs.artfactonline.system.StatusCode;
import com.cs.artfactonline.system.exception.ObjectNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)//ceci desactive la sécurite filter de spring
//@AutoConfigureMockMvc
@ActiveProfiles(value = "dev")
class ArtifactControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ArtifactService artifactService;

    @Autowired
    ObjectMapper mapper;

    List<Artifact> artifacts;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        this.artifacts = new ArrayList<>();

        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");
        this.artifacts.add(a1);

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl2");
        this.artifacts.add(a2);

        Artifact a3 = new Artifact();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl3");
        this.artifacts.add(a3);

        Artifact a4 = new Artifact();
        a4.setId("1250808601744904194");
        a4.setName("The Marauder's Map");
        a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl4");
        this.artifacts.add(a4);

        Artifact a5 = new Artifact();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageUrl("ImageUrl5");
        this.artifacts.add(a5);

        Artifact a6 = new Artifact();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        a6.setImageUrl("ImageUrl6");
        this.artifacts.add(a6);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindArtifactByIdNotFound() throws Exception {
        /*
        {
            "flag": true,
            "code" :200,
            "message": "Find One Success",
            "data": {|
            "id": "1250808601744904191".
            "name": "Deluminator".
            "description":"A Deluminator is a device invented by...",
            "imageUr1" : "ImageUr1"
            “owner"：｛日
            "id" : 1,
            "name": "Albus Dumbledore",
            "numberofArtifacts" :2
        }
         */
        //Given
        given(artifactService.findById("1250808601744904191")).willThrow(new ObjectNotFoundException("name","1250808601744904191"));
        //When(86) and then
        /*Fake http GET Request*/
          this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/artifacts/1250808601744904191")
                          .accept(MediaType.APPLICATION_JSON))
                          .andExpect(jsonPath("$.flag").value(false))
                          .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                          .andExpect(jsonPath("$.message").value("Could not Find name with Id:1250808601744904191:("))
                          .andExpect(jsonPath("$.data").isEmpty());


    }

    @Test
    void testFindArtifactByIdSuccess() throws Exception {
        //Given
        given(artifactService.findById("1250808601744904191")).willReturn(this.artifacts.get(0));
        //When and then
        /*Fake http GET Request*/
        this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/artifacts/1250808601744904191") .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904191"))
                .andExpect(jsonPath("$.data.name").value("Deluminator"));
    }

    @Test
    void testAllArtifactsSuccess() throws Exception {
        //Given
    Pageable pageable = PageRequest.of(0,20);//valeur par défaut
        PageImpl<Artifact> artifactPage = new PageImpl<>(this.artifacts, pageable,this.artifacts.size());
    given(this.artifactService.findAll(Mockito.any(Pageable.class))).willReturn(artifactPage);
        MultiValueMap<String , String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("page", "0");
        requestParams.add("size", "20");
        //Wen and Then
        this.mockMvc.perform(MockMvcRequestBuilders.
                        get(baseUrl+"/artifacts").accept(MediaType.APPLICATION_JSON)
                        .params(requestParams)
                )
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data.content", Matchers.hasSize(this.artifacts.size())))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data.content", Matchers.hasSize(this.artifacts.size())))
                .andExpect(jsonPath("$.data.content[0].id").value("1250808601744904191"))
                .andExpect(jsonPath("$.data.content[0].name").value("Deluminator"))
                .andExpect(jsonPath("$.data.content[1].id").value("1250808601744904192"))
                .andExpect(jsonPath("$.data.content[1].name").value("Invisibility Cloak")
                );
    }

    @Test
    void testAddArtifactSuccess() throws Exception {
        //Given
        ArtifactDto artifactDto = new ArtifactDto(
                null,
                "Massire",
                "Etudiant 3em année Web",
                "massire.png",
                null
        );
        //fake input data to define output methode
       String jsonArt =  this.mapper.writeValueAsString(artifactDto);
       Artifact savedArtifact = new Artifact();
       savedArtifact.setId("1250808601744904197");
       savedArtifact.setName("Massire");
       savedArtifact.setDescription("Etudiant 3em année Web");
       savedArtifact.setImageUrl("massire.png");
       //Simulation de donnée qui provient du front-end
       given(this.artifactService.save(Mockito.any(Artifact.class))).willReturn(savedArtifact);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrl+"/artifacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonArt)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(savedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(savedArtifact.getImageUrl())
                );
    }

    @Test
    void testUpdateArtifactSuccess() throws Exception {
        //Given, fournit par le front-end
        ArtifactDto artifactDto = new ArtifactDto(
                "1250808601744904192",
                "Invisibility Cloak",
                "A new Description",
                "ImageUrl",
                null
        );
        String json = this.mapper.writeValueAsString(artifactDto);//Reçu depuis le fropnt-end

        Artifact updateArtifact = new Artifact();
        updateArtifact.setId("1250808601744904192");
        updateArtifact.setName("Invisibility Cloak");
        updateArtifact.setDescription("A new Description");
        updateArtifact.setImageUrl("ImageUrl");

        given(this.artifactService.update(eq("1250808601744904192"),Mockito.any(Artifact.class))).willReturn(updateArtifact);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/artifacts/1250808601744904192")
                                                        .content(json)
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(updateArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(updateArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(updateArtifact.getImageUrl()));
    }

    @Test
    void testUpdateArtifactErrorWithNonexistentiId() throws Exception {
        //Given, fournit par le front-end
        ArtifactDto artifactDto = new ArtifactDto(
                "1250808601744904192",
                "Invisibility Cloak",
                "A new Description",
                "ImageUrl",
                null
        );
        String json = this.mapper.writeValueAsString(artifactDto);//Reçu depuis le fropnt-end



        given(this.artifactService.update(eq("1250808601744904192"),Mockito.any(Artifact.class))).willThrow(new ObjectNotFoundException("name","1250808601744904192"));

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/artifacts/1250808601744904192")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not Find name with Id:1250808601744904192:("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteArtifactSuccess() throws Exception {
        //Given
        doNothing().when(this.artifactService).delete("1250808601744904192");

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl+"/artifacts/1250808601744904192")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteArtifactErrorWithNoExistId() throws Exception {
        //Given
        doThrow(new ObjectNotFoundException("name","1250808601744904192")).when(this.artifactService).delete("1250808601744904192");
        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl+"/artifacts/1250808601744904192")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not Find name with Id:1250808601744904192:("))
                .andExpect(jsonPath("$.data").isEmpty());
    }



}