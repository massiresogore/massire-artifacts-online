package com.cs.artfactonline.wizard;

import com.cs.artfactonline.artifact.utils.IdWorker;
import com.cs.artfactonline.system.StatusCode;
import com.cs.artfactonline.system.exception.ObjectNotFoundException;
import com.cs.artfactonline.wizard.dto.WizardDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)//ceci desactive la sécurite filter de spring
//@AutoConfigureMockMvc
@ActiveProfiles(value = "dev")
class WizardControllerTest {

    @MockBean
    WizardService wizardService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Mock
    IdWorker idWorker;

    @Value("${api.endpoint.base-url}")
    String baseUrl;
    List<Wizard> wizardList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Wizard w1 = new Wizard();
        w1.setId(123456);
        w1.setName("Albus Dumbledore");
        this.wizardList.add(w1);

        Wizard w2 = new Wizard();
        w2.setId(123457);
        w2.setName("Harry Potter");
        this.wizardList.add(w2);

        Wizard w3 = new Wizard();
        w3.setId(123458);
        w3.setName("Harry Potter");
        this.wizardList.add(w3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testfindAllWizardsSuccess() throws Exception {
        //Given
        given(idWorker.nextId()).willReturn(123456L);
        given(idWorker.nextId()).willReturn(123457L);
        given(this.wizardService.findAll()).willReturn(this.wizardList);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/wizards")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find all wizards success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.wizardList.size())))
                .andExpect(jsonPath("$.data[0].id").value(123456))
                .andExpect(jsonPath("$.data[0].name").value("Albus Dumbledore"))
                .andExpect(jsonPath("$.data[1].id").value(123457))
                .andExpect(jsonPath("$.data[1].name").value("Harry Potter"));

    }

    @Test
    void testAddWizardSuccess() throws Exception {
        //Given
        WizardDto wizardDto = new WizardDto(null,"Massire", null);

        //Fausse donnée de formulaire
        String jsonWizard = this.mapper.writeValueAsString(wizardDto);
        //Nouvelle donnée sauvegarder qui renvoie ses valeur depuis la Base de donnée
        Wizard savesWizard = new Wizard();
        savesWizard.setId(1);
        savesWizard.setName("Massire");

        //Simullation de donnée qui provient du front-end
        //given(idWorker.nextId()).willReturn(123456L);
        given(this.wizardService.save(Mockito.any(Wizard.class))).willReturn(savesWizard);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrl+"/wizards")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonWizard)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add wizard success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savesWizard.getName()));


    }

    @Test
    void testFindWizardByIdSuccess() throws Exception {
        //Given
        given(wizardService.findById(123456)).willReturn(this.wizardList.get(0));

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/wizards/123456").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find one wizard success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(123456))
                .andExpect(jsonPath("$.data.name").value("Albus Dumbledore"));



    }

    @Test
    void testFindWizardByIdNotFound() throws Exception {
        //Given
        given(wizardService.findById(2)).willThrow(new ObjectNotFoundException("",2));
        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/wizards/2").accept(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.flag").value(false))
                        .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                        .andExpect(jsonPath("$.message").value("Could not Find  with Id:2:("))
                        .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void updateWizardSuccess() throws Exception {
        //Given
        WizardDto wizardDto = new WizardDto(1,"Massire",4);

        String jsonWizard = this.mapper.writeValueAsString(wizardDto);

        Wizard updatedWizard = new Wizard();
        updatedWizard.setId(1);
        updatedWizard.setName("new name");

        given(this.wizardService.update(eq(1),Mockito.any(Wizard.class))).willReturn(updatedWizard);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/wizards/1")
                        .content(jsonWizard)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update wizard success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(updatedWizard.getName()));
    }

    @Test
    void deleteWizardSuccess() throws Exception {
        //Given
        doNothing().when(this.wizardService).delete(1);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl+"/wizards/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete wizard success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void deleteWizardWithErrorWithNoExistsId() throws Exception {
        //Given
        doThrow(new ObjectNotFoundException("name",1)).when(this.wizardService).delete(1);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl+"/wizards/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not Find name with Id:1:("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignmentArtifactSuccess() throws Exception {
        //Given
        doNothing().when(this.wizardService).assignArtifact(2,"1250808601744904192");

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/wizards/2/artifacts/1250808601744904192")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact assignment success"))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testAssignmentArtifactErrorWithNonExistentWizard() throws Exception {
        //Given
        doThrow(new ObjectNotFoundException("wizard",5)).when(this.wizardService).assignArtifact(5,"1250808601744904192");
        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/wizards/5/artifacts/1250808601744904192")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not Find wizard with Id:5:("))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testAssignmentArtifactErrorWithNonExistentArtifact() throws Exception {
        //Given
        doThrow(new ObjectNotFoundException("artifact","1250808601744904192")).when(this.wizardService).assignArtifact(5,"1250808601744904192");
        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/wizards/5/artifacts/1250808601744904192")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not Find artifact with Id:1250808601744904192:("))
                .andExpect(jsonPath("$.data").isEmpty());

    }
}