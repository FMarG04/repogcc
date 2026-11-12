# Carros API

Aplicacion REST de Spring Boot para registrar y consultar carros desde GitHub Codespaces y Postman.

## Ejecutar en Codespaces

1. Abrir Code > Codespaces y entrar al Codespace.
2. En la terminal, desde la carpeta que contiene pom.xml: 

```bash
mvn test
mvn spring-boot:run
```

Esperar el mensaje Started CarrosApplication. Maven descarga Spring Boot y las dependencias automaticamente la primera vez; no hay que instalar Spring Boot por separado.

3. Abrir la pestana Ports/Puertos. El puerto es 8080.
4. Para Postman de escritorio se puede usar la URL del puerto publico (Port Visibility > Public). Esto expone esta API de demostracion a quien tenga la URL. Otra opcion es mantenerlo privado y autenticarse con X-Github-Token. No incluir tokens en evidencias o commits.
5. Copiar la direccion reenviada, por ejemplo https://NOMBRE-8080.app.github.dev. Mantener la terminal y Codespace encendidos durante la demostracion.

## Probar en Postman

1. Importar postman/Carros-API.postman_collection.json (se puede copiar su contenido y pegarlo en Import > Raw text).
2. En las variables de la coleccion, cambiar baseUrl por la direccion del puerto 8080, sin barra final. Guardar.
3. Ejecutar las peticiones 1, 2 y 3, en ese orden. Opcional: mostrar la peticion 4 de validacion.

| Metodo | Ruta | Resultado |
|---|---|---|
| GET | /api/carros | 200 OK y lista de carros |
| POST | /api/carros | 201 Created y carro registrado |

Body del POST, en Body > raw > JSON:

```json
{
  "marca": "Toyota",
  "modelo": "Corolla",
  "anio": 2024,
  "color": "Blanco"
}
```

El id se genera automaticamente. La lista comienza vacia. Los datos se guardan en memoria y se pierden al reiniciar el servidor. No requiere base de datos.

## Explicacion del codigo

- CarrosApplication: inicia Spring Boot y el servidor web.
- CarroRequest: los cuatro campos recibidos y sus validaciones.
- Carro: objeto JSON devuelto, incluido el id.
- CarroController: recibe las peticiones GET y POST y guarda los carros en memoria.
- ApiExceptionHandler: responde 400 si faltan datos o son invalidos.

## Guion breve para exponer

Esta aplicacion REST registra carros. Fue ejecutada desde GitHub Codespaces usando Spring Boot y Maven. Cada carro tiene marca, modelo, anio y color, ademas de un identificador generado automaticamente. Primero ejecuto GET y consulto la lista. Despues envio un carro como JSON mediante POST y obtengo 201 Created. Finalmente ejecuto GET nuevamente y compruebo que devuelve el carro registrado. Los datos se mantienen en memoria mientras el servidor esta encendido.

## Si algo falla

- 404 en la raiz: utilizar /api/carros al final de la URL.
- Pagina de login o HTML en Postman: revisar la visibilidad/autenticacion del puerto.
- 400: enviar los cuatro campos y anio como numero.
- 415: seleccionar Body > raw > JSON y Content-Type application/json.
- No responde: comprobar que Codespaces y mvn spring-boot:run siguen activos.

## CRUD completo: actualizar y eliminar

Coleccion: Carros API - CRUD completo. Ejecutar las peticiones 1 a 7 en orden. POST guarda el id automaticamente en carroId.

| Metodo | Ruta | Resultado |
|---|---|---|
| PUT | /api/carros/{id} | 200 OK y carro actualizado |
| DELETE | /api/carros/{id} | 204 No Content, sin cuerpo |

PUT requiere marca, modelo, anio y color; conserva el id. Ambos devuelven 404 si el carro no existe. PUT con datos invalidos devuelve 400 sin modificar el registro.

Para exponer: GET consulta; POST crea; PUT actualiza; DELETE elimina. Consultar con GET despues de PUT y DELETE para comprobar los cambios.
