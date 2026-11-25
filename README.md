# RepoGCC

- [carros/](carros/) contiene el proyecto Maven de la API REST de carros.
- [libros/](libros/) es la carpeta para desarrollar la API REST de libros.

## Ejecutar la API de carros

Con Java 21, desde la raíz del repositorio, ejecuta en la terminal de Codespaces:

```bash
cd carros
./mvnw spring-boot:run
```

En otra terminal puedes consultar los carros con:

```bash
curl -i http://localhost:8080/api/carros
```
