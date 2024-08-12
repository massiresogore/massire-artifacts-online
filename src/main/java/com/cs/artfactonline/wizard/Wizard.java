package com.cs.artfactonline.wizard;

import com.cs.artfactonline.artifact.Artifact;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Wizard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty(message = "name is required.")
    @NonNull
    private String name;

    //one wizard are many artifact,
    // Le nom de mappedBy doit correspondre au nom donné dans @ManyToOne
        /*
         @ManyToOne
            private Wizard owner;
         */
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "owner")
    List<Artifact> artifacts = new ArrayList<>();

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addArtifact(Artifact artifact) {
        //We set the owner of the artifact to the current wizard
        artifact.setOwner(this);
        //we add the artifact to this wizard
        this.artifacts.add(artifact);
    }

    public Integer getNumberOfArtifacts() {
        return this.getArtifacts().size();
    }

    public void removeAllArtifacts()
    {
        this.artifacts.stream().forEach(artifact->artifact.setOwner(null));
        this.artifacts = null;
    }

    public void removeArtifact(Artifact artifactTobeAssigned) {
        //remove artifact owner
        artifactTobeAssigned.setOwner(null);
        this.artifacts.remove(artifactTobeAssigned);
    }
}
