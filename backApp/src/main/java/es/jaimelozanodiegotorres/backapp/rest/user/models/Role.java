package es.jaimelozanodiegotorres.backapp.rest.user.models;

import java.util.Set; /**
 * Enumerado que representa los roles de los usuarios.
 */
public enum Role {
    USER, // Por defecto
    WORKER, // TRABAJADOR DEL RESTAURANTE
    ADMIN; // Administrador


    //la logica de la app puede cambiar. Cargos como camarero, etc o cambios en los clientes
    public static boolean isWorker(Set<Role> roles) {
        return roles.contains(Role.WORKER) || roles.contains(Role.ADMIN);
    }

    public static boolean isClient(Set<Role> roles) {
        return roles.contains(Role.USER);
    }
}