package com.cs.artfactonline.artifact;

import com.cs.artfactonline.artifact.converter.ArtifactDtoToArtifactConverter;
import com.cs.artfactonline.artifact.converter.ArtifactToArtifactDtoConverter;
import com.cs.artfactonline.artifact.dto.ArtifactDto;
import com.cs.artfactonline.system.Result;
import com.cs.artfactonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("${api.endpoint.base-url}/artifacts")
public class ArtifactController {
    private final ArtifactService artifactService;
    public final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;
    public final ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;


    public ArtifactController(ArtifactService artifactService, ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter, ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter) {
        this.artifactService = artifactService;
        this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
        this.artifactDtoToArtifactConverter = artifactDtoToArtifactConverter;
    }

    @GetMapping("/{artifactId}")
    public Result findArtifactById(@PathVariable("artifactId") String artifactId)
    {
        Artifact foundArtifact = this.artifactService.findById(artifactId);

        return new Result(true, StatusCode.SUCCESS,"Find One Success",artifactToArtifactDtoConverter.convert(foundArtifact));
        //spring mvc s'occupe de transformer Result Object en json
    }

    /*
     * Ce qui se passe en coullise,
     * PageableHandlerMethodArgumentResolver. résout ce paramettre de
     * méthode pageable
     *
     * ********cette fonction qui provient de linstance PageableHandlerMethodArgumentResolver,
     * montre comment linstnace de pageable est créé
     *
     * public Pageable resolveArgument(MethodParameter methodParameter,
     *                                    @Nullable ModelAndViewContainer mavContainer,
     *                                    NativeWebRequest webRequest,
     *                                    @Nullable WebDataBinderFactory binderFactory) {
     * String page = webRequest.getParameter(this.getParameterNameToUse(this.getPageParameterName(), methodParameter));
     * String pageSize = webRequest.getParameter(this.getParameterNameToUse(this.getSizeParameterName(), methodParameter));
     * Sort sort = this.sortResolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
     * Pageable pageable = this.getPageable(methodParameter, page, pageSize);
     * return (Pageable)(sort.isSorted() ? PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort) : pageable);
     *}
     *
     *
     * ***** une autre fonction PageableHandlerMethodArgumentResolverSupport
     * montre les constante utilise par pageable, page et zise
     *
     * pour Personnaliser les noms des paramettre url on fait ceci dan yaml
     *
     * spring :
     *   data:
     *       web:
     *           pageable:
     *               page-parameter: yourCustomPageParam
     *               size-parameter: yourCustomSizeParam
     *           sort:
     *               sort-parameter: yourCustomSortParam
     * */
    @GetMapping
    public Result findAllArtifacts(Pageable pageable)
    {
        Page<Artifact> artifactPage = this.artifactService.findAll(pageable);

        //Convert foundArtifactPage to a page of foundArtifactDto
       Page<ArtifactDto> artifactDtoPage =  artifactPage
               .map(artifactToArtifactDtoConverter::convert);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Find All Success",
                artifactDtoPage
        );
    }

    @PostMapping
    public Result addArtifact(@Valid @RequestBody ArtifactDto artifactDto)
    {
            return new Result(
                    true,
                    StatusCode.SUCCESS,
                    "Add Success",
                    this.artifactToArtifactDtoConverter.convert(artifactService.save(Objects.requireNonNull(artifactDtoToArtifactConverter.convert(artifactDto))))) ;
    }

    @PutMapping("/{artifactId}")
    public Result updateArtifact(@PathVariable String artifactId,@Valid @RequestBody ArtifactDto artifactDto)
    {
        Artifact update = this.artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact updatedArtifact = this.artifactService.update(artifactId,update);
        ArtifactDto artifactDto1 = this.artifactToArtifactDtoConverter.convert(updatedArtifact);

        return new Result(true,StatusCode.SUCCESS,"Update Success",artifactDto1);
    }

    @DeleteMapping("/{artifactId}")
    public Result deleteArtifact(@PathVariable("artifactId") String artifactId)
    {
        this.artifactService.delete(artifactId);
        return new Result(true,StatusCode.SUCCESS,"Delete Success");
    }



}
