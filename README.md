# GlobalInvoice Backend

Este proyecto es el backend de una solución de facturación y gestión documental desarrollada con Java y Spring Boot para Grupo Bolivar. Su objetivo principal es administrar usuarios, autenticación, facturas y cálculos de impuestos, además de integrar un servicio externo SOAP para convertir valores numéricos a texto con el proveedor DataFlex/NumberConversion.

## ¿Por qué se hizo con Java?

Java fue la elección natural para este tipo de aplicación por varias razones:

- Es una plataforma madura y ampliamente utilizada en entornos empresariales.
- Tiene un excelente ecosistema para APIs REST, seguridad, persistencia y validaciones.
- Spring Boot simplifica la configuración de servicios web, JPA, seguridad, OpenAPI y despliegue.
- El manejo de tipos, clases y estructuras de dominio hace más seguro y mantenible el desarrollo de procesos de negocio complejos como facturación.
- Java es ideal para aplicaciones backend con alta estabilidad, escalabilidad y mantenimiento a largo plazo.

En este proyecto se usa Java 25 con Spring Boot 4.1.1, junto con Spring Security, Spring Data JPA, validación de DTOs y consumo SOAP mediante HttpClient.

## Visión general del sistema

La aplicación expone una API REST para:

- Registrar y gestionar usuarios.
- Autenticarse con JWT.
- Gestionar facturas con tipos de impuesto:
  - NACIONAL
  - EXPORTACION
  - GUBERNAMENTAL
- Calcular subtotal, IVA, retención y total.
- Convertir el total en palabras a través de un servicio externo SOAP.
- Exponer documentación con Swagger/OpenAPI.

## Arquitectura y diseño

El proyecto sigue una arquitectura basada en capas, típica del ecosistema Spring:

- Controller: expone los endpoints REST.
- Service: encapsula la lógica de negocio.
- Repository: acceso a datos con Spring Data JPA.
- Entity: modelos de dominio mapeados a la base de datos.
- DTO: transporte de información entre capas.
- Mapper: conversión entre entidades y DTOs.
- Security: autenticación JWT y protección de rutas.
- Config: configuración de CORS, seguridad y SOAP.

Esto permite separar claramente responsabilidades, hacer pruebas unitarias efectivas y mantener la lógica de negocio aislada de la infraestructura.

## Tecnologías utilizadas

- Java 25
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Security
- Spring Data JPA
- MySQL
- JWT (jjwt)
- Lombok
- OpenAPI / Swagger
- Mockito + JUnit 5 para pruebas unitarias
- JaCoCo para cobertura
- SOAP con HttpClient y XML / XPath

## Estructura principal del proyecto

```text
src/
  main/
    java/com/jssv/globalinvoice/
      config/
      controller/
      dto/
      entity/
      enums/
      exception/
      mapper/
      repository/
      security/
      service/
      validator/
  resources/
    application.properties
  test/
    java/com/jssv/globalinvoice/service/
```

### Carpetas clave

- config/: configuración de seguridad, CORS y SOAP.
- controller/: endpoints HTTP de la API.
- dto/: objetos que transportan datos entre cliente y backend.
- entity/: entidades JPA.
- service/: servicios del negocio.
- service/tax/: estrategias de cálculo fiscal.
- service/integration/: conexión con servicios externos (SOAP).
- security/: lógica de JWT y filtros.
- exception/: manejo de errores y excepciones funcionales.

## Funcionalidades principales

### 1. Autenticación y seguridad

El backend usa Spring Security para proteger rutas sensibles. La autenticación se basa en credenciales de usuario y JWT.

Aspectos importantes:

- El filtro JWT valida el token en cada solicitud.
- El servicio de autenticación genera un token al validar email y contraseña.
- Los endpoints públicos suelen ser los de login y registro de acceso inicial.
- CORS debe estar correctamente configurado para que un frontend Angular pueda consumir la API desde localhost:4200.

### 2. Gestión de usuarios

El usuario cuenta con datos como email, contraseña, estado activo y roles. Los roles se manejan como entidades relacionadas, y la aplicación usa autenticación basada en permisos por autoridad.

### 3. Facturación

La lógica de facturación incluye:

- generación de consecutivos
- cálculo de impuestos por tipo de factura
- validación del subtotal
- cálculo del total
- conversión del total a texto mediante servicio externo

Los tipos de factura se definen en un enum y son procesados por estrategias específicas, lo que permite extensibilidad sin duplicar lógica.

### 4. Integración SOAP

La aplicación integra con un servicio externo para convertir un número entero en palabras. La lógica se encapsula en `DataFlexNumberToWords`, y es importante porque:

- el proveedor responde en formato SOAP
- se debe validar el contenido XML y extraer el valor correcto
- se debe manejar errores HTTP y parseo XML
- el resultado debe ser robusto ante cambios en namespace o prefijo del XML

## Diseño de cálculo fiscal

La aplicación usa el patrón Strategy para calcular impuestos según el tipo de factura.

- Nacional: aplica IVA al subtotal.
- Gubernamental: aplica IVA y retención.
- Exportación: no aplica IVA ni retención.

Esto hace que el cálculo sea más mantenible y escalable, porque cada tipo de factura tiene su propia lógica concreta, aunque comparten una interfaz común.

## CORS y Angular

Uno de los puntos críticos del proyecto fue la compatibilidad entre el backend y un frontend Angular.

Se configuró CORS para aceptar peticiones desde:

- http://localhost:4200
- http://127.0.0.1:4200

Además, hubo que considerar:

- preflight OPTIONS
- headers permitidos
- métodos HTTP permitidos
- credenciales cuando se requieran
- evitar dobles barras en rutas o errores de URL en el cliente

Esto es vital para que frontend y backend puedan comunicarse en desarrollo local sin bloqueos por política del navegador.

## Base de datos y entorno local

El proyecto usa MySQL y se configura mediante `application.properties`.

Configuración relevante:

```properties
server.port=8090
spring.datasource.url=jdbc:mysql://localhost:3306/dbglobalinvoice?serverTimezone=GMT-5
spring.datasource.username=root
spring.datasource.password=
```

Importante:

- la DB debe existir antes de arrancar la aplicación
- si se usa otra base o credenciales, deben ajustarse esos valores
- la aplicación usa Hibernate para generar/actualizar el esquema según `spring.jpa.hibernate.ddl-auto=update`

## Swagger / OpenAPI

El proyecto expone documentación Swagger para facilitar pruebas manuales y consumo desde clientes externos.

Rutas típicas:

- /v3/api-docs
- /swagger-ui.html

Esto ayuda a revisar los endpoints sin necesidad de consumir la API únicamente desde Postman o Angular.

## Pruebas

El proyecto cuenta con pruebas unitarias con JUnit 5 y Mockito, enfocadas en servicios con lógica de negocio.

Objetivos de estas pruebas:

- validar cálculos de impuestos
- comprobar la autenticación y generación de tokens
- validar usuarios y permisos
- probar conversiones SOAP
- confirmar el comportamiento ante errores esperados

También se usa JaCoCo para medir la cobertura de código.

## Consideraciones importantes

1. Seguridad
   - nunca exponer secretos en repositorios públicos
   - usar variables de entorno para claves y credenciales en producción

2. CORS
   - revisar los orígenes permitidos según el entorno
   - comprobar headers y métodos antes de abrir la API pública

3. SOAP
   - validar que el endpoint externo esté disponible
   - manejar errores 4xx/5xx claramente
   - evitar asumir un namespace o prefijo fijo en el XML

4. Base de datos
   - verificar versionado de esquemas en entornos reales
   - usar migraciones en lugar de depender solo de `ddl-auto` en producción

5. Mantenibilidad
   - mantener la lógica de negocio en servicios y no en controladores
   - usar DTOs y mappers para mantener una API limpia
   - separar estrategias de negocio por responsabilidades

## Cómo ejecutar el proyecto

Requisitos:

- Java 25
- Maven
- MySQL en localhost
- Docker o local MySQL running

Pasos:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

La aplicación queda disponible en:

- http://localhost:8090

## Estado del proyecto

Este backend está orientado a una solución de facturación empresarial con enfoque en:

- autenticación segura
- gestión de usuarios y roles
- cálculos de impuestos
- integración con servicios externos
- consumo desde aplicaciones frontend como Angular

Es una base sólida para continuar evolucionando con más módulos, endpoints y mejoras de producción.

## Conclusión

La decisión de construir este backend en Java con Spring Boot responde a la necesidad de crear una solución robusta, mantenible y escalable para un sistema de facturación con autenticación, seguridad, persistencia y servicios externos. La separación de capas, la estrategia de cálculo de impuestos, la integración SOAP y la configuración de CORS son elementos clave para soportar un flujo real de negocio y una integración con clientes web modernos.
