package es.jaimelozanodiegotorres.backapp.rest.commons.services;

import es.jaimelozanodiegotorres.backapp.rest.auth.service.jwt.JwtService;
import es.jaimelozanodiegotorres.backapp.rest.commons.exceptions.ExceptionService;
import es.jaimelozanodiegotorres.backapp.rest.commons.repository.CommonRepository;
import es.jaimelozanodiegotorres.backapp.rest.user.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Slf4j
public abstract class CommonService <T , ID extends Serializable>{

    @Autowired
    protected JwtService jwtService;

    protected final CommonRepository <T,ID> repository;
    protected final ExceptionService exceptionService;
    public final String entityName;

    protected CommonService(CommonRepository<T, ID> repository) {
        this.repository = repository;
        this.entityName = repository.getTableName();
        this.exceptionService = new ExceptionService(entityName);
    }

    public List<T> listAll(){
        log.info("Devolviendo listado completo de {}", entityName);

        return repository.findByDeletedAtIsNull();
    }

    public T findById(ID id) throws RuntimeException {
        log.info("Buscando {} por id", entityName);

        return repository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> exceptionService.notFoundException(id.toString()));
    }

    public T save(T entity){
        log.info("Guardando {}: {}", entityName, entity);

        return repository.save(entity);
    }

    public T update(T entity){
        log.info("Actualizando {}: {}", entityName, entity);

        return repository.save(entity);
    }

    public boolean deleteById(ID id) throws RuntimeException {
        log.info("Borrando {} con id: {}", entityName, id);

        findById(id);

        repository.deleteById(id);

        return true;
    }

    protected UUID getLoggedUserId() {
        String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();

        System.out.println(token); //todo

        if (token == null)
            return null;

        return UUID.fromString(jwtService.extractUserId(token).substring(7));
    }

    protected void verifyLogguedSameUser(User user){
        if(user.getId() != getLoggedUserId())
            throw exceptionService.badRequestException("El usuario que accede no es el mismo que al que pertenece la entidad");
    }

    protected void verifyLogguedSameUser(UUID userId){
        if(userId != getLoggedUserId())
            throw exceptionService.badRequestException("El usuario que accede no es el mismo que al que pertenece la entidad");
    }
}
