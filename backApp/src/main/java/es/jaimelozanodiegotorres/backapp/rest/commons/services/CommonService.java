package es.jaimelozanodiegotorres.backapp.rest.commons.services;

import es.jaimelozanodiegotorres.backapp.rest.commons.exceptions.ExceptionService;
import es.jaimelozanodiegotorres.backapp.rest.user.models.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public abstract class CommonService {

    protected final ExceptionService exceptionService;
    public final String entityName;

    protected CommonService(String entityName) {
        this.entityName = entityName;
        this.exceptionService = new ExceptionService(entityName);
    }

    protected UUID getLoggedUserId() {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null)
            return null;

        return user.getId();
    }

    protected void verifyLogguedSameUser(User user){
        if(user.getId().equals(getLoggedUserId()))
            throw exceptionService.badRequestException("El usuario que accede no es el mismo que al que pertenece la entidad");
    }

    protected void verifyLogguedSameUser(UUID userId){
        if(userId.equals(getLoggedUserId()))
            throw exceptionService.badRequestException("El usuario que accede no es el mismo que al que pertenece la entidad");
    }
}
