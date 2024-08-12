package com.cs.artfactonline.wizard;


import com.cs.artfactonline.system.Result;
import com.cs.artfactonline.system.StatusCode;
import com.cs.artfactonline.wizard.converter.WizardDtoToWizardConverter;
import com.cs.artfactonline.wizard.converter.WizartToWizardDtoConverter;
import com.cs.artfactonline.wizard.dto.WizardDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("${api.endpoint.base-url}/wizards")
public class WizardController {

    WizardService wizardService;
    WizartToWizardDtoConverter wizartToWizardDtoConverter;
    WizardDtoToWizardConverter wizardDtoToWizardConverter;

    public WizardController(WizardService wizardService,WizartToWizardDtoConverter wizartToWizardDtoConverter,WizardDtoToWizardConverter wizardDtoToWizardConverter) {
        this.wizardService = wizardService;
        this.wizartToWizardDtoConverter = wizartToWizardDtoConverter;
        this.wizardDtoToWizardConverter=wizardDtoToWizardConverter;
    }

    @GetMapping
    public Result findAllWizards()
    {
        List<Wizard>  wizardList = wizardService.findAll();
        List<WizardDto> foundWizardDto = wizardList.stream().map(wizard -> wizartToWizardDtoConverter.convert(wizard)).toList();
        return new Result(true, StatusCode.SUCCESS,"Find all wizards success",foundWizardDto);
    }

    @PostMapping
    public Result addWizard(@Valid @RequestBody WizardDto wizardDto)
    {
       Wizard savedWizard = this.wizardService.save(Objects.requireNonNull(this.wizardDtoToWizardConverter.convert(wizardDto)));

       WizardDto savedWizardDto = this.wizartToWizardDtoConverter.convert(savedWizard);
        return new Result(true,StatusCode.SUCCESS,"Add wizard success",savedWizardDto);
    }

    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable("wizardId") Integer wizardId)
    {
        return new Result(true,StatusCode.SUCCESS,"Find one wizard success",wizartToWizardDtoConverter.convert(wizardService.findById(wizardId)));
    }

    @PutMapping("/{wizardId}")
    public Result updatedWizard(@PathVariable("wizardId") Integer wizardId,@Valid @RequestBody WizardDto wizardDto)
    {
        //Convert  WizardDto to Wizard
        Wizard wizard = this.wizardDtoToWizardConverter.convert(wizardDto);

        //saveWizard
        Wizard savedWizard= this.wizardService.update(wizardId,wizard);

        //Convert saveWizard to savedWizardDto
        WizardDto updatedWizard = this.wizartToWizardDtoConverter.convert(savedWizard);

        return new Result(true,StatusCode.SUCCESS,"Update wizard success",updatedWizard);

    }

    @DeleteMapping("/{wizardId}")
    public Result deleteWizard(@PathVariable Integer wizardId)
    {
         this.wizardService.delete(wizardId);

         return new Result(true,StatusCode.SUCCESS,"Delete wizard success");
    }

    @PutMapping("/{wizardId}/artifacts/{artifactId}")
    public Result assignArtifact(@PathVariable Integer wizardId, @PathVariable String artifactId)
    {
        this.wizardService.assignArtifact(wizardId,artifactId);
        return new Result(true,StatusCode.SUCCESS,"Artifact assignment success");
    }


}
