package com.cs.artfactonline.wizard.converter;

import com.cs.artfactonline.wizard.Wizard;
import com.cs.artfactonline.wizard.dto.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizartToWizardDtoConverter implements Converter<Wizard, WizardDto> {
    /**
     * Convert the source object of type {@code S} to target type {@code T}.
     *
     * @param source the source object to convert, which must be an instance of {@code S} (never {@code null})
     * @return the converted object, which must be an instance of {@code T} (potentially {@code null})
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */
    @Override
    public WizardDto convert(Wizard source) {

        return new WizardDto(
                source.getId(),
                source.getName(),
               source.getNumberOfArtifacts()
               //si source.getArtifacts().size()//il fau appliquer le principe de moindre connaissance, éviter de lui(cette classe) permettre d'en savoir trop
        );
    }

}