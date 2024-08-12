package com.cs.artfactonline.wizard;

import com.cs.artfactonline.artifact.Artifact;
import com.cs.artfactonline.artifact.ArtifactRepository;
import com.cs.artfactonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WizardService {

    public WizardRepository wizardRepository;
    public ArtifactRepository artifactRepository;

    public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository) {
        this.wizardRepository = wizardRepository;
        this.artifactRepository = artifactRepository;
    }

    public List<Wizard> findAll()
    {
        return this.wizardRepository.findAll();
    }

    public Wizard save(Wizard wizard)
    {
        return this.wizardRepository.save(wizard);
    }
    public void delete(Integer idWizard)
    {
       Wizard wisardToBeDeleted=  this.wizardRepository.findById(idWizard).
                orElseThrow(()->new ObjectNotFoundException("name",idWizard));

         //Avnat de supprimer on doit supprimer les lien entre ces artifacts
        wisardToBeDeleted.removeAllArtifacts();
         this.wizardRepository.deleteById(idWizard);
    }


    public Wizard findById(int i) {
       return wizardRepository.findById(i).orElseThrow(()-> new ObjectNotFoundException("name",i));
    }

    public Wizard update(Integer i,Wizard wizard)
    {
        return this.wizardRepository.findById(i)
                .map(oldWizard->{
                    oldWizard.setName(wizard.getName());
                    return this.wizardRepository.save(oldWizard);
                })
                .orElseThrow(()->new ObjectNotFoundException("",i));
    }

    public void assignArtifact(Integer wizardId, String artifactId)
    {
        //Find artifact by id From DB
        Artifact artifactTobeAssigned = this.artifactRepository.findById(artifactId).orElseThrow(
                () -> new ObjectNotFoundException("artifact", artifactId)
        );

        //Find wizard by id from DB
        Wizard wizard = this.wizardRepository.findById(wizardId).orElseThrow(
                () -> new ObjectNotFoundException("wizard", wizardId)
        );


        //Artifact assignment
            //We need to see if the artifact is already owned by some wizard
            if(artifactTobeAssigned.getOwner() != null){
                artifactTobeAssigned.getOwner().removeArtifact(artifactTobeAssigned);
            }
        wizard.addArtifact(artifactTobeAssigned);

    }




}
