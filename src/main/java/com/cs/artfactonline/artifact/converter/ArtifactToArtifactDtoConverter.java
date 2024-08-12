package com.cs.artfactonline.artifact.converter;

import com.cs.artfactonline.artifact.Artifact;
import com.cs.artfactonline.artifact.dto.ArtifactDto;
import com.cs.artfactonline.wizard.converter.WizartToWizardDtoConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToArtifactDtoConverter implements Converter<Artifact, ArtifactDto> {
    private final WizartToWizardDtoConverter wizartToWizardDtoConverter;

    public ArtifactToArtifactDtoConverter(WizartToWizardDtoConverter wizartToWizardDtoConverter) {
        this.wizartToWizardDtoConverter = wizartToWizardDtoConverter;
    }

    /**
     * Convert the source object of type {@code S} to target type {@code T}.
     *
     * @param source the source object to convert, which must be an instance of {@code S} (never {@code null})
     * @return the converted object, which must be an instance of {@code T} (potentially {@code null})
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */
    @Override
    public ArtifactDto convert(Artifact source) {
   return new ArtifactDto(
            source.getId(),
            source.getName(),
            source.getDescription(),
            source.getImageUrl(),
            source.getOwner() != null ? this.wizartToWizardDtoConverter.convert(source.getOwner()): null
//            source.getOwner() != null ? this.wizartToWizardDtoConverter.convert(source.getOwner()): null
    );

    }
}
