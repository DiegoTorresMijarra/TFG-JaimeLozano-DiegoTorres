# TFG-JaimeLozano-DiegoTorres

# MacJava Project:

**Autor/es del Proyecto:**

- [Jaime Lozano](https://github.com/jaime9lozano)
- [Diego Torres Mijarra](https://github.com/DiegoTorresMijarra)

## Introducción

La API MacJava es una solución integral para la administración segura y eficiente de bases de datos, especialmente diseñada para tiendas online. Este proyecto se basa en un enfoque robusto y moderno, utilizando SpringBoot para la capa de back-end y Angular junto con Ionic para la capa de front-end. La combinación de estas tecnologías permite una configuración sencilla y un desarrollo ágil, facilitando la creación y gestión de aplicaciones web y móviles de alto rendimiento.

Para asegurar un flujo de trabajo ordenado y colaborativo, se adopta la metodología GitFlow para la gestión de ramas. Este enfoque organiza las ramas de características en una rama de desarrollo, la cual, tras pasar por un riguroso proceso de pruebas, se fusiona en la rama principal. Esta estructura no solo optimiza la integración y el despliegue continuo, sino que también minimiza los riesgos y mejora la calidad del software.

Con la API MacJava, las tiendas online pueden beneficiarse de una plataforma confiable y escalable, diseñada para manejar grandes volúmenes de datos y proporcionar una experiencia de usuario excepcional.

## Instalación:

### Desarrollo:

#### Requisitos:

- [Git](https://git-scm.com/downloads)
- [JDK 17 o superior](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Apache Maven 3.11.0 o superior](https://maven.apache.org/download.cgi)
- [Node.js y npm](https://nodejs.org/)
- [Angular CLI](https://angular.io/cli)
- [Ionic CLI](https://ionicframework.com/docs/cli)
- [Docker](https://www.docker.com/products/docker-desktop)


Descargar repositorio:
```
git clone https://github.com/DiegoTorresMijarra/TFG-JaimeLozano-DiegoTorres.git
```
Front:
```
cd TFG-JaimeLozano-DiegoTorres/frontApp 

npm install

npm start
```
Back:
``` 
cd TFG-JaimeLozano-DiegoTorres/backApp

docker-compose -f docker-compose-db.yaml up # levantar bbdd de desarrollo

mvn clean install

mvn spring-boot:run
```
### Despliegue:

#### Requisitos:

- [Git](https://git-scm.com/downloads)
- [Docker](https://www.docker.com/products/docker-desktop)

Descargar repositorio:
```
git clone https://github.com/DiegoTorresMijarra/TFG-JaimeLozano-DiegoTorres.git
```
Despliegue Dockers:
```
cd TFG-JaimeLozano-DiegoTorres

docker-compose -f docker-compose.yaml up
```


## Galeria
<div style="display: flex; flex-wrap: wrap; justify-content: center;">
    <img src="/documentacion/Imagenes/actualizarProducto.png" width="150" height="150" alt="actualizarProducto">
    <img src="/documentacion/Imagenes/carrito.png" width="150" height="150" alt="carrito">
    <img src="/documentacion/Imagenes/direccion.png" width="150" height="150" alt="direccion">
    <img src="/documentacion/Imagenes/Esquema-Common-Controller-Repo-Service.PNG" width="150" height="150" alt="Esquema-Common-Controller-Repo-Service">
    <img src="/documentacion/Imagenes/Esquema-Common-Controller-Repo-Service-module.PNG" width="150" height="150" alt="Esquema-Common-Controller-Repo-Service-module">
    <img src="/documentacion/Imagenes/Esquema-filters.PNG" width="150" height="150" alt="Esquema-filters">
    <img src="/documentacion/Imagenes/Esquema-Mappers.PNG" width="150" height="150" alt="Esquema-Mappers">
    <img src="/documentacion/Imagenes/estadoPedido.png" width="150" height="150" alt="estadoPedido">
    <img src="/documentacion/Imagenes/factura.png" width="150" height="150" alt="factura">
    <img src="/documentacion/Imagenes/imagenProd.png" width="150" height="150" alt="imagenProd">
    <img src="/documentacion/Imagenes/loginMovil.png" width="150" height="150" alt="loginMovil">
    <img src="/documentacion/Imagenes/me.png" width="150" height="150" alt="me">
    <img src="/documentacion/Imagenes/meADMIN.png" width="150" height="150" alt="meADMIN">
    <img src="/documentacion/Imagenes/Menu.png" width="150" height="150" alt="Menu">
    <img src="/documentacion/Imagenes/menuADMIN.png" width="150" height="150" alt="menuADMIN">
    <img src="/documentacion/Imagenes/MenuMovil.png" width="150" height="150" alt="MenuMovil">
    <img src="/documentacion/Imagenes/MenuOBSCURO.png" width="150" height="150" alt="MenuOBSCURO">
    <img src="/documentacion/Imagenes/menuOscuro.png" width="150" height="150" alt="menuOscuro">
    <img src="/documentacion/Imagenes/OfertaActiva.png" width="150" height="150" alt="OfertaActiva">
    <img src="/documentacion/Imagenes/ofertas.png" width="150" height="150" alt="ofertas">
    <img src="/documentacion/Imagenes/pedidoInfo.png" width="150" height="150" alt="pedidoInfo">
    <img src="/documentacion/Imagenes/pedidos.png" width="150" height="150" alt="pedidos">
    <img src="/documentacion/Imagenes/Principal.png" width="150" height="150" alt="Principal">
    <img src="/documentacion/Imagenes/prodcustoOscuro.png" width="150" height="150" alt="prodcustoOscuro">
    <img src="/documentacion/Imagenes/ProductId.png" width="150" height="150" alt="ProductId">
    <img src="/documentacion/Imagenes/productoADMIN.png" width="150" height="150" alt="productoADMIN">
    <img src="/documentacion/Imagenes/productosMovil.png" width="150" height="150" alt="productosMovil">
    <img src="/documentacion/Imagenes/Register.png" width="150" height="150" alt="Register">
    <img src="/documentacion/Imagenes/registroOscuro.png" width="150" height="150" alt="registroOscuro">
    <img src="/documentacion/Imagenes/Restaurantes.png" width="150" height="150" alt="Restaurantes">
    <img src="/documentacion/Imagenes/Valoracion.png" width="150" height="150" alt="Valoracion">
    <img src="/documentacion/Imagenes/valoracionesMovil.png" width="150" height="150" alt="valoracionesMovil">
    <img src="/documentacion/Imagenes/websocket.png" width="150" height="150" alt="websocket">
</div>

## Objetivo

El objetivo principal de este proyecto es diseñar, desarrollar y documentar una aplicación web y móvil utilizando la API MacJava, basada en las tecnologías SpringBoot, Angular e Ionic. Los objetivos específicos son:

1. Diseñar la arquitectura del sistema.
2. Desarrollar la aplicación.
3. Realizar pruebas y validaciones.
4. Documentar el proceso.

## Alcance

El proyecto abarca el diseño, desarrollo, pruebas y documentación de una aplicación web y móvil para la gestión de una tienda online. Incluye:

1. Diseño de la arquitectura.
2. Desarrollo de funcionalidades.
3. Diseño y desarrollo de interfaces de usuario.
4. Pruebas exhaustivas.
5. Documentación técnica y de usuario.

El proyecto no incluirá la integración con sistemas externos o de terceros, ni el desarrollo de funcionalidades avanzadas que no sean directamente relevantes para una tienda online básica.

## Justificación

Este proyecto se justifica por:

1. Demanda del mercado.
2. Eficiencia operativa.
3. Accesibilidad.
4. Seguridad.
5. Escalabilidad.

## Implementación

### Análisis de la Aplicación

La aplicación sigue una arquitectura de tres capas, compuesta por el back-end, el front-end y las bases de datos PostgreSQL y MongoDB, desplegadas en contenedores Docker.

- **Back-end**: Desarrollado con Spring Boot, gestiona los endpoints y servicios que manipulan los datos.
- **Front-end**: Desarrollado con Angular e Ionic, consume los endpoints proporcionados por el back-end.
- **Seguridad**: Implementación de autenticación y autorización utilizando JWT.
- **Escalabilidad**: Despliegue de las bases de datos en contenedores Docker.

### Diseño

Consulte el [Manual de Usuario](documentacion/ManualUsuario.pdf).

### Implementación

El back-end, se organiza en modelos, DTOs, mappers, repositorios, servicios y controladores. En el front-end, se organiza en un header, footer y body con vistas y servicios que se conectan al back-end mediante HTTP.

### Documentación

Consulte el [Manual de Tecnico](documentacion/ManualTecnico.pdf).


## Resultados y Discusión

### Objetivos Alcanzados

- Desarrollo integral de funcionalidades clave.
- Integración exitosa con bases de datos PostgreSQL y MongoDB.
- Despliegue en entorno de producción utilizando contenedores Docker.

### Dificultades y Estrategias de Superación

- Complejidad de integración tecnológica.
- Optimización de consultas a bases de datos.
- Gestión de errores y excepciones.

### Reflexiones y Lecciones Aprendidas

- Planificación adecuada y definición clara de requisitos.
- Comunicación y colaboración efectiva.
- Prácticas de desarrollo ágiles y pruebas continuas.
- Flexibilidad y capacidad de adaptación.

## Trabajo Futuro

1. Sistema de gestión de restaurantes.
2. Ofertas personalizadas y cupones de descuento.
3. Mejoras en el carrito de compra.
4. Roles de usuario más complejos.
5. Mejoras en la seguridad y autenticación.
6. Sistema de notificaciones.
7. Optimización del rendimiento y escalabilidad.
8. Internacionalización y localización.

## Conclusiones

El desarrollo de este proyecto ha permitido alcanzar importantes logros y superar diversos desafíos. La aplicación desarrollada proporciona una experiencia completa para los usuarios, con un énfasis especial en la seguridad, la escalabilidad y el rendimiento. El proyecto ha sido una experiencia valiosa, proporcionando una base sólida para futuras iteraciones y mejoras.

---

