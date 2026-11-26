# API REST de libros

En GitHub abre **Code → Codespaces** en la rama `main`. El Codespace abre todo el repositorio. Si ya tienes uno en `main`, actualízalo con `git pull --ff-only` desde la raíz.

## Arrancar el proyecto

Desde la raíz del repositorio, en la terminal del Codespace:

```bash
cd libros
docker compose up --wait
mvn spring-boot:run
```

Compose inicia PostgreSQL con un volumen para conservar los datos. Spring Boot conecta el `DataSource` definido en `application.properties` y ejecuta `schema.sql` al arrancar. El puerto `8081` permite ejecutar libros junto a carros, que usa el `8080`.

## Probar los endpoints

En una segunda terminal, consulta la lista. Una base recién creada devuelve `200 OK` y `[]`.

```bash
curl -i http://localhost:8081/api/libros
```

Registra un libro. El servidor devuelve `201 Created` con el libro y su `id`.

```bash
curl -i -X POST http://localhost:8081/api/libros \
  -H 'Content-Type: application/json' \
  -d '{"titulo":"El principito","autor":"Antoine de Saint-Exupéry","isbn":"9780156012195"}'
```

Repite el GET y comprueba que aparece el mismo libro. Si envías un campo obligatorio vacío, el POST devuelve `400 Bad Request`.

## Cómo se organiza

- `Libro` representa un libro guardado con sus cuatro campos.
- `LibroController` recibe las peticiones HTTP y valida los datos del POST.
- `LibroService` usa `JdbcClient` y el `DataSource` de Spring para ejecutar SQL. PostgreSQL genera el `id` con una columna `IDENTITY` y lo devuelve mediante `RETURNING`.

Para detener el servicio, pulsa `Ctrl+C` en la terminal de Maven y ejecuta `docker compose down` desde `libros/`. El volumen de PostgreSQL se conserva.
