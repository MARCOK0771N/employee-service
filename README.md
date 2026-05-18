# Employee Service
backend para servicios de empleados

## Objetivo
Desarrollar un servicio backend resiliente, seguro y escalable que gestione operaciones sobre una
entidad de "Empleado", siguiendo prácticas modernas de desarrollo, arquitectura de microservicios y
aplicando los principios de DevSecOps.

## Tecnologías
- Java 11 o Java 17  
- Spring Boot 2.7.x o Quarkus  
- Spring Data JPA / Hibernate  
- Base de datos Oracle o MySQL (o H2 para pruebas)  
- JSON request/response  - Excepciones customizadas  
- Swagger / OpenAPI  
- Pruebas unitarias con JUnit 5 y Mockito  
- Configuración de logs via YAML o prop

## Instalacion 
- git clone https://github.com/MARCOK0771N/employee-service.git
- cd employee-service
- mvn clean install
- docker build -t exam/invex .

## Etiquetar y subir a ECR
- docker tag exam/invex:latest 654654552067.dkr.ecr.us-west-2.amazonaws.com/exam/invex:latest
- docker push 654654552067.dkr.ecr.us-west-2.amazonaws.com/exam/invex:latest

## AWS EC2
- docker pull 654654552067.dkr.ecr.us-west-2.amazonaws.com/exam/invex:latest

## Ejecutar el contenedor
- docker run -d --name employee-service -p 8080:8080 654654552067.dkr.ecr.us-west-2.amazonaws.com/exam/invex:latest

## Pruebas 
- mvn test

## Postman
## File -> postman/employee-service.postman_collection.json
## File -> postman/aws.postman_environment.json

NOTA: usar para probar --> base_url = http://54.191.21.96:8080

- GET /employees → listar empleados
- GET /employees/{id} → obtener empleado por ID
- POST /employees → crear empleado
    - se implementa  solucion que permite registrar multiples empleados con un mecanismo de validacion de duplicados 
    - usando el nombre completo y la fecha de nacimiento en caso de existir y estar inactivo se actualiza y se activa nuevamnete
- PUT /employees/{id} → actualizar empleado
    - en este servicio se implemento un mecanismo de validacion por campo al no ser opcionales se realizan 
    - validaciones solo de los que tienen informacion, ademas se hace tratado por espacios vacios y se valida que exista el id del empleado a actualizar
- DELETE /employees/{id} → eliminar empleado
  -  se valida existencia previa

## Swagger UI
- http://54.191.21.96:8080/swagger-ui.html

Actuator
- http://54.191.21.96:8080/actuator
