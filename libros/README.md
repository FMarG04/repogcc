# API REST de libros con Neon

Postman llama a Spring Boot en Codespaces. Spring Boot usa su `DataSource` JDBC para leer y guardar libros en PostgreSQL de Neon. La API escucha en el puerto `8081`.

## 1. Conectar Neon

1. Abre [la consola de Neon](https://console.neon.tech/) y entra al proyecto de práctica donde guardarás los libros. Si todavía no tienes uno, crea un proyecto para esta actividad.
2. Pulsa **Connect** y copia el host, el nombre de la base, el usuario y la contraseña. Usa una conexión directa para esta práctica. En la URL de PostgreSQL, el usuario va antes de la contraseña, el host después de `@` y la base después de `/`. [Guía JDBC de Neon](https://neon.com/docs/guides/java)
3. En la terminal del Codespace, desde la raíz del repositorio, actualiza `main` y carga las credenciales:

```bash
git pull --ff-only
cd libros
read -rp 'URL JDBC de Neon: ' LIBROS_DB_URL
read -rp 'Usuario de Neon: ' LIBROS_DB_USER
read -rsp 'Contraseña de Neon: ' LIBROS_DB_PASSWORD
printf '\n'
export LIBROS_DB_URL LIBROS_DB_USER LIBROS_DB_PASSWORD
mvn spring-boot:run
```

En el primer prompt, usa este formato y reemplaza `HOST` y `BASE` con tus datos. Por ejemplo, la base predeterminada suele llamarse `neondb`.

```text
jdbc:postgresql://HOST/BASE?sslmode=require&channelBinding=require
```

El usuario y la contraseña se ingresan por separado. La contraseña queda oculta mientras la escribes. Si prefieres conservar las variables entre sesiones, puedes guardarlas como [secretos de Codespaces](https://github.com/settings/codespaces) asociados a `FMarG04/repogcc` y abrir de nuevo el Codespace. Las credenciales se quedan fuera del repositorio y de Postman.

El perfil predeterminado, `neon`, usa esas tres variables. Al iniciar, Spring ejecuta `schema.sql` para crear la tabla `libros` si hace falta. Para este perfil, la base se ejecuta en Neon y puedes arrancar Spring sin Docker.

## 2. Demostrarlo en Postman

1. En Postman, pulsa **Import** y carga [Libros.postman_collection.json](Libros.postman_collection.json).
2. En **Variables** de la colección, ajusta `baseUrl`. Si abriste el Codespace en **VS Code de escritorio**, puedes usar la dirección local del puerto `8081` que aparece en **Ports**, por ejemplo `http://localhost:8081`. Si usas **VS Code en el navegador**, copia la dirección reenviada HTTPS del puerto `8081` y usa el encabezado privado `X-Github-Token` en ambas peticiones, siguiendo el [procedimiento de GitHub para Postman](https://docs.github.com/en/codespaces/developing-in-a-codespace/forwarding-ports-in-your-codespace#using-postman-to-access-a-forwarded-port). Conserva el token en una variable local o en el Vault de Postman y fuera de la colección compartida.
3. Envía **Registrar libro**. Debe responder `201 Created` y devolver el ID que generó PostgreSQL. Después envía **Listar libros**. Debe responder `200 OK` y mostrar el libro con ese mismo ID.
4. Para mostrar el registro directamente en la base, abre el **SQL Editor** de Neon en la misma rama y base y ejecuta:

```sql
SELECT id, titulo, autor, isbn FROM libros ORDER BY id;
```

## PostgreSQL local

La configuración anterior con Docker sigue disponible para desarrollo local:

```bash
cd libros
docker compose up --wait
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Spring Boot conecta cada perfil a su propia base de datos. Los libros guardados en Docker siguen en su volumen local.
