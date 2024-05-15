package es.jaimelozanodiegotorres.backapp.rest.evaluation.controller;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.category.dto.CategoryResponseDto;
import es.jaimelozanodiegotorres.backapp.rest.category.dto.CategorySaveDto;
import es.jaimelozanodiegotorres.backapp.rest.category.dto.CategoryUpdateDto;
import es.jaimelozanodiegotorres.backapp.rest.category.mappers.CategoryMapper;
import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;
import es.jaimelozanodiegotorres.backapp.rest.category.services.CategoryServiceImp;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationResponseDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationSaveDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationUpdateDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.mapper.EvaluationMapper;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.models.Evaluation;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.services.EvaluationServiceImp;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
//@PreAuthorize("hasRole('USER')")
@RequestMapping(value = "/valoraciones")
@Tag(name = "Valoraciones", description = "Endpoint de Valoraciones de nuestros productos")
public class EvaluationController {
    private final EvaluationServiceImp evaluationService;

    @Autowired
    public EvaluationController(EvaluationServiceImp evaluationService) {
        this.evaluationService = evaluationService;
    }

    @GetMapping()
    public ResponseEntity<PageResponse<Evaluation>> findAll(
            @RequestParam(required = false) Optional<Integer> ValorMin,
            @RequestParam(required = false) Optional<Integer> ValorMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "valoracion") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ){
        log.info("Buscando Valoraciones");
        Sort sort= direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<Evaluation> pageResult = evaluationService.findAll(ValorMin, ValorMax, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    @GetMapping("/producto/{id}")
    public ResponseEntity<PageResponse<Evaluation>> findAllByProductId(@PathVariable Long id,
            @RequestParam(required = false) Optional<Integer> ValorMax,
            @RequestParam(required = false) Optional<Integer> ValorMin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "valoracion") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ){
        log.info("Buscando Valoraciones por producto");
        Sort sort= direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<Evaluation> pageResult = evaluationService.findByProductId(id,ValorMin, ValorMax, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evaluation> findById(@PathVariable Long id){
        log.info("Buscando Valoracion con id: " + id);
        return ResponseEntity.ok(evaluationService.findById(id));
    }

    @PostMapping("")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity <EvaluationResponseDto> createPosition(@Valid @RequestBody EvaluationSaveDto evaluation){
        log.info("Guardando Valoracion");
        return ResponseEntity.ok(EvaluationMapper.toEvaluationResponseDto(evaluationService.save(evaluation)));
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EvaluationResponseDto> updatePosition(@PathVariable Long id, @Valid @RequestBody EvaluationUpdateDto evaluation){
        log.info("Actualizando Valoracion con id: " + id);
        return ResponseEntity.ok(EvaluationMapper.toEvaluationResponseDto(evaluationService.update(id, evaluation)));
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public void deletePosition(@PathVariable Long id){
        log.info("Eliminando Valoracion con Id: " + id);
        evaluationService.deleteById(id);
    }

    /**
     * Maneja las excepciones de validación
     * @param ex Excepción de validación
     * @return Mapa con los errores de validación
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
