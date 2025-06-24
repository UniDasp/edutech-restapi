# EduTech REST API

## Quienes lo hicimos

Este proyecto lo hicimos entre:

- **Diego S**
- **Diego C**
- **Benjamin F**

## De qué va esto

Es una API REST para una plataforma educativa. Tienes usuarios, cursos, evaluaciones, pagos y evaluaciones de usuarios.

## Cómo está organizado

```
Controllers -> Services -> Repositories -> Database
```

Los controllers manejan las peticiones HTTP, los services tienen la lógica, los repositories acceden a la base de datos y los models son las tablas.

## Endpoints

### Usuarios
- GET /api/v1/usuarios - Lista todos los usuarios
- GET /api/v1/usuarios/{id} - Obtiene un usuario
- POST /api/v1/usuarios - Crea un usuario
- PUT /api/v1/usuarios/{id} - Actualiza un usuario
- DELETE /api/v1/usuarios/{id} - Borra un usuario

### Cursos  
- GET /api/v1/cursos - Lista todos los cursos
- GET /api/v1/cursos/{id} - Obtiene un curso
- POST /api/v1/cursos - Crea un curso
- PUT /api/v1/cursos/{id} - Actualiza un curso
- DELETE /api/v1/cursos/{id} - Borra un curso

### Evaluaciones
- GET /api/v1/evaluaciones - Lista todas las evaluaciones
- GET /api/v1/evaluaciones/{id} - Obtiene una evaluación
- POST /api/v1/evaluaciones/{cursoId} - Crea una evaluación
- PUT /api/v1/evaluaciones/{id} - Actualiza una evaluación
- DELETE /api/v1/evaluaciones/{id} - Borra una evaluación

### Pagos
- GET /api/v1/pagos - Lista todos los pagos
- GET /api/v1/pagos/{id} - Obtiene un pago
- POST /api/v1/pagos - Crea un pago
- PUT /api/v1/pagos/{id} - Actualiza un pago
- DELETE /api/v1/pagos/{id} - Borra un pago

### Evaluaciones de Usuarios
- GET /api/v1/evaluaciones-usuarios - Lista todas las evaluaciones de usuarios
- GET /api/v1/evaluaciones-usuarios/{id} - Obtiene una evaluación de usuario
- POST /api/v1/evaluaciones-usuarios - Crea una evaluación de usuario
- PUT /api/v1/evaluaciones-usuarios/{id} - Actualiza una evaluación de usuario
- DELETE /api/v1/evaluaciones-usuarios/{id} - Borra una evaluación de usuario

## Ejemplos para probar en Postman

### 1. Crear Usuario
**URL:** `POST http://localhost:8080/api/v1/usuarios`
```json
{
  "username": "juan.perez",
  "password": "password123",
  "rol": "USER"
}
```

### 2. Crear Curso
**URL:** `POST http://localhost:8080/api/v1/cursos`
```json
{
  "nombre": "Java Programming",
  "descripcion": "Curso completo de programación en Java"
}
```

### 3. Crear Evaluación
**URL:** `POST http://localhost:8080/api/v1/evaluaciones/{cursoId}`
```json
{
  "titulo": "Examen Parcial Java",
  "fecha": "2024-12-25",
  "puntajeMaximo": 100
}
```

### 4. Crear Pago
**URL:** `POST http://localhost:8080/api/v1/pagos`
```json
{
  "monto": 50000.0,
  "fecha": "2024-12-25",
  "metodo": "tarjeta",
  "estado": "PENDIENTE",
  "usuarioId": 1,
  "cursoId": 1
}
```

### 5. Crear Evaluación de Usuario
**URL:** `POST http://localhost:8080/api/v1/evaluaciones-usuarios`
```json
{
  "usuarioId": 1,
  "evaluacionId": 1,
  "puntajeObtenido": 85,
  "fechaEntrega": "2024-12-25"
}
```

### Orden para probar:
1. Usuario → Obtienes ID del usuario
2. Curso → Obtienes ID del curso  
3. Evaluación → Usas el ID del curso en la URL
4. Pago → Usas los IDs de usuario y curso
5. Evaluación de Usuario → Usas los IDs de usuario y evaluación

### Para verificar datos existentes:
- `GET http://localhost:8080/api/v1/usuarios` - Ver usuarios
- `GET http://localhost:8080/api/v1/cursos` - Ver cursos
- `GET http://localhost:8080/api/v1/evaluaciones` - Ver evaluaciones

## Solución de problemas

### Error de clave foránea en EvaluacionUsuario
Si obtienes un error de clave foránea al crear una EvaluacionUsuario:

1. **Ejecuta el script SQL** `database-setup.sql` en tu base de datos MySQL
2. **O cambia la configuración** en `application.properties`:
   ```
   spring.jpa.hibernate.ddl-auto=create-drop
   ```
3. **Reinicia la aplicación** para que se recree la base de datos
4. **Vuelve a crear** usuarios, cursos y evaluaciones
5. **Luego prueba** el endpoint de EvaluacionUsuario

### Verificar que los IDs existen
Antes de crear una EvaluacionUsuario, verifica que:
- El usuario con ese ID existe
- La evaluación con ese ID existe
- Usa los endpoints GET para verificar

### Problemas de serialización JSON
Si tienes problemas con los POST de Cursos o Evaluaciones:
- Los modelos ya están simplificados sin anotaciones JsonManagedReference
- Los controladores usan DTOs específicos para evitar problemas de serialización
- Asegúrate de usar el formato JSON correcto como se muestra en los ejemplos

### Referencias circulares infinitas
Si ves un JSON que se repite infinitamente:
- Los modelos tienen anotaciones `@JsonBackReference` y `@JsonManagedReference` para evitar bucles
- `@JsonBackReference` se usa en el lado "muchos" de las relaciones
- `@JsonManagedReference` se usa en el lado "uno" de las relaciones
- Esto evita que el JSON se vuelva infinito cuando hay relaciones bidireccionales

## Tecnologías

- Spring Boot 3.2.5
- Spring Data JPA
- MySQL
- HATEOAS
- SpringDoc OpenAPI (Swagger)
- JUnit 5
- Lombok

## Estructura

```
src/main/java/com/edutech/cl/main/
├── assembler/     # Convierte entre DTOs y entidades
├── config/        # Configuraciones
├── controller/    # Endpoints REST
├── dto/          # Objetos de transferencia
├── model/        # Entidades JPA
├── repository/   # Acceso a datos
└── service/      # Lógica de negocio
```

## Cosas que sabemos

- Los tests a veces fallan pero funciona
- Si algo no funciona, revisa la base de datos
- La base de datos se llama 'db_edutech_vm'
- Swagger está en http://localhost:8080/swagger-ui.html

## Contacto

Si hay problemas o preguntas, habla con cualquiera de nosotros.

---

Hecho con paciencia, algunas ayuditas de ChatGPT y mucho café ☕ 