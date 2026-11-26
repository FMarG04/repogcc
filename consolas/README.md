# CRUD de consolas

La página de Google Apps Script llama a Spring Boot por HTTPS. Spring Boot utiliza su `DataSource` para guardar las consolas en PostgreSQL. La tabla tiene `marca`, `modelo`, `precio` e `id`. PostgreSQL genera el ID y guarda el precio como decimal de hasta dos cifras fraccionarias.

## 1. Arranca Spring Boot en Codespaces

Actualiza `main` si ya tenías abierto el repositorio. Desde la raíz:

```bash
cd consolas
docker compose up --wait
export CONSOLAS_API_KEY="$(openssl rand -hex 32)"
mvn spring-boot:run
```

Antes de ejecutar Maven, puedes copiar la clave con `printf '%s\n' "$CONSOLAS_API_KEY"` en la misma terminal. La usarás en Apps Script. Genera una nueva si compartiste esa clave por error. Si vuelves a arrancar la API con otra clave, actualiza también la propiedad de Apps Script.

Con la API en marcha, abre **Ports** en Codespaces, reenvía el puerto `8082`, cambia su visibilidad a **Public** para que pueda acceder Apps Script y copia la dirección HTTPS. Solo se reenvía el puerto de Spring Boot. El puerto de PostgreSQL permanece local.

## 2. Crea la aplicación web en Google Apps Script

1. Entra a [Google Apps Script](https://script.google.com/) y crea un proyecto independiente.
2. Pega [Code.gs](apps-script/Code.gs) en el archivo de código inicial. Si el editor lo muestra como `Código.gs`, puedes usar ese archivo.
3. Crea un archivo HTML llamado `Index` y pega [Index.html](apps-script/Index.html).
4. En **Configuración del proyecto → Propiedades del script**, agrega `API_BASE_URL` con la dirección HTTPS del puerto `8082`, sin añadir `/api/consolas`. Agrega también `CONSOLAS_API_KEY` con la clave generada en la terminal.
5. Ve a **Implementar → Nueva implementación → Aplicación web**. Selecciona **Ejecutar como yo** y **Solo yo** para la prueba individual. Autoriza la aplicación y abre la URL que devuelve Google.

## 3. Prueba el CRUD

Con el Codespace activo y Spring Boot en ejecución, abre la aplicación web. Agrega una consola con marca `Nintendo`, modelo `Switch OLED` y precio `1299.90`. Comprueba que aparece en el listado, edita su precio y luego elimínala.

| Acción | Método y ruta | Respuesta |
| --- | --- | --- |
| Crear | `POST /api/consolas` | `201` y la consola creada |
| Listar | `GET /api/consolas` | `200` y la lista |
| Editar | `PUT /api/consolas/{id}` | `200` y la consola actualizada |
| Eliminar | `DELETE /api/consolas/{id}` | `204` |

`Index.html` contiene el formulario y la tabla. `Code.gs` hace las peticiones con `UrlFetchApp` y envía la clave en la cabecera `X-API-Key`. En Java, `Consola` es el modelo, `ConsolaController` recibe las peticiones y `ConsolaService` ejecuta el SQL.
