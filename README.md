# RepoGCC

La raíz contiene la API REST de carros. La carpeta `libros/` contiene un segundo proyecto Maven con la API REST de libros y PostgreSQL.

## Ejecutar la API de libros en GitHub Codespaces

1. En GitHub, abre **Code → Codespaces → Create codespace** en la rama que contiene la actividad. Si ya tienes un Codespace en esa rama, actualiza el código y ejecuta **Codespaces: Rebuild Container**.
2. Codespaces prepara Java 21 y arranca PostgreSQL mediante Docker Compose. En la terminal, ejecuta:

   ```bash
   bash mvnw -f libros/pom.xml spring-boot:run
   ```

3. En otra terminal ejecuta los ejemplos de abajo. También puedes abrir el puerto privado `8081` desde la pestaña **Ports** y añadir `/api/libros` a la URL para ver el listado.

## Probar el servicio

Registra un libro con `POST /api/libros`. Los tres campos son obligatorios y el ISBN se envía como texto.

```bash
curl -i http://localhost:8081/api/libros \
  -H 'Content-Type: application/json' \
  -d '{"titulo":"Cien años de soledad","autor":"Gabriel García Márquez","isbn":"9780307474728"}'
```

La respuesta es `201 Created` e incluye el ID generado por PostgreSQL. Si falta un campo obligatorio o está en blanco, devuelve `400 Bad Request`.

```json
{"id":1,"titulo":"Cien años de soledad","autor":"Gabriel García Márquez","isbn":"9780307474728"}
```

Consulta todos los libros con `GET /api/libros`. Devuelve `200 OK` y un arreglo ordenado por ID. Una base vacía devuelve `[]`.

```bash
curl -i http://localhost:8081/api/libros
```

## Cómo se guardan los libros

Spring Boot configura un `DataSource` JDBC con HikariCP. `JdbcClient` usa ese DataSource para consultar e insertar filas y PostgreSQL genera el ID con una columna `IDENTITY`. Al iniciar, Spring ejecuta `schema.sql` para crear la tabla si hace falta. Docker guarda los datos en el volumen `libros-pgdata`, así que reiniciar la API conserva los libros.

Compose define credenciales de desarrollo para la base de datos dentro de la red del contenedor. Para conectar con otra instancia, configura `LIBROS_DB_URL`, `LIBROS_DB_USER` y `LIBROS_DB_PASSWORD` antes de iniciar la API.
