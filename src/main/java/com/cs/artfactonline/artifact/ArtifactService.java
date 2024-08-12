package com.cs.artfactonline.artifact;

import com.cs.artfactonline.artifact.utils.IdWorker;
import com.cs.artfactonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional // Pertmet de mettre toutes ses methodes transactional
public class ArtifactService {

    private final ArtifactRepository artifactRepository;
    private final IdWorker idWorker;

    public ArtifactService(ArtifactRepository artifactRepository, IdWorker idWorker) {
        this.artifactRepository = artifactRepository;
        this.idWorker = idWorker;
    }

    public Artifact findById(String artifactId)
    {

        return artifactRepository
                .findById(artifactId)
                .orElseThrow(()->new ObjectNotFoundException("",artifactId));
    }



    public List<Artifact> findAll()
    {
        return this.artifactRepository.findAll();
    }

    public Artifact save(Artifact newArtifact)
    {
        newArtifact.setId(idWorker.nextId() + "");
        return artifactRepository.save(newArtifact);
    }

    public Artifact update(String artifactId, Artifact update)
    {
        return this.artifactRepository.findById(artifactId)
                        .map(oldArtifact->{
                            oldArtifact.setName(update.getName());
                            oldArtifact.setDescription(update.getDescription());
                            oldArtifact.setImageUrl(update.getImageUrl());
                            return this.artifactRepository.save(oldArtifact);
                        })
                        .orElseThrow(()->new ObjectNotFoundException(update.getName(),artifactId));
    }

    public void delete(String artifactId)
    {
        //artifact exist or not
        this.artifactRepository.findById(artifactId)
               .orElseThrow(()->new ObjectNotFoundException("update",artifactId));
       this.artifactRepository.deleteById(artifactId);
    }

    /*  La source dans son repository
    *     Page<T> findAll(Pageable pageable);
     * */
    public Page<Artifact> findAll(Pageable pageable) {

        return this.artifactRepository.findAll(pageable);
    }
}
