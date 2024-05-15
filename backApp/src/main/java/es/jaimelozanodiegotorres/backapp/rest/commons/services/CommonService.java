package es.jaimelozanodiegotorres.backapp.rest.commons.services;

import es.jaimelozanodiegotorres.backapp.rest.commons.exceptions.ExceptionService;
import es.jaimelozanodiegotorres.backapp.rest.commons.mapper.CommonMapper;
import es.jaimelozanodiegotorres.backapp.rest.commons.models.CommonModel;
import es.jaimelozanodiegotorres.backapp.rest.commons.repository.CommonRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

@Slf4j
public abstract class CommonService <T , ID extends Serializable>{

    protected final CommonRepository <T,ID> repository;
    protected final ExceptionService exceptionService;
    public final String entityName;

    protected CommonService(CommonRepository<T, ID> repository) {
        this.repository = repository;
        this.entityName = repository.getTableName();
        this.exceptionService = new ExceptionService(entityName);
    }

    public List<T> listAll(){
        log.info("Devolviendo listado completo de " + entityName);

        return repository.findByDeletedAtIsNull();
    }

    public T findById(ID id) throws RuntimeException {
        log.info("Buscando " + entityName + " por id");

        return repository.findById(id).orElseThrow(() -> exceptionService.notFoundException(id.toString()));
    }

    public T save(T entity){
        log.info("Guardando " + entityName + ": {}", entity);

        return repository.save(entity);
    }

    public T update(T entity){
        log.info("Actualizando " + entityName + ": {}", entity);

        return repository.save(entity);
    }

    public boolean deleteById(ID id) throws RuntimeException {
        log.info("Borrando " + entityName + " con id: "+id);

        findById(id);

        repository.deleteById(id);

        return true;
    }
}
