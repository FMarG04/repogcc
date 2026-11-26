# CRUD de consolas

La página de Google Apps Script usa las rutas REST de Spring Boot. Spring Data JPA guarda los registros en la tabla `consolas` de PostgreSQL. Sus tres columnas son `id`, `marca` y `modelo`. PostgreSQL asigna el ID al crear un registro.

## Arrancar el backend en Codespaces

Desde la raíz del repositorio ejecuta:

```bash
cd consolas
docker compose up --wait
```

Si ya tienes una clave configurada en Apps Script, usa la misma al arrancar. El comando pide la clave de forma oculta:

```bash
read -rsp 'Clave de consolas: ' CONSOLAS_API_KEY && \
printf '\n' && export CONSOLAS_API_KEY && mvn spring-boot:run
```

Para una primera configuración, puedes generar una clave con `openssl rand -hex 32`. Usa ese mismo valor al arrancar Spring Boot y al configurar Apps Script.

En **Puertos**, reenvía el puerto `8082` y ponlo en **Público** durante la demostración para que Apps Script pueda llegar a la API. Copia su dirección HTTPS. La conexión JDBC a PostgreSQL se realiza dentro del Codespace.

## Implementar la página de Apps Script

1. Abre [Google Apps Script](https://script.google.com/) y pega [Code.gs](apps-script/Code.gs) en el archivo de código. Crea un archivo HTML llamado `Index` y pega [Index.html](apps-script/Index.html).
2. En **Configuración del proyecto → Propiedades del script** agrega `API_BASE_URL` con la dirección HTTPS del puerto `8082`. Agrega `CONSOLAS_API_KEY` con la clave del backend.
3. En **Implementar → Nueva implementación → Aplicación web**, elige **Ejecutar como yo** y **Solo yo**. Si ya tienes una aplicación web, puedes actualizarla desde **Administrar las implementaciones** con una versión nueva.
4. Abre la URL de Apps Script. Crea una consola, comprueba que aparece en el listado, edítala y elimínala. Al terminar la demo, vuelve a poner el puerto en **Privado**.

| Operación | Petición REST |
| --- | --- |
| Crear | `POST /api/consolas` |
| Listar | `GET /api/consolas` |
| Actualizar | `PUT /api/consolas/{id}` |
| Eliminar | `DELETE /api/consolas/{id}` |
