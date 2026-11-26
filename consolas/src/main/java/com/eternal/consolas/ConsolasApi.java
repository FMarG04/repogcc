package com.eternal.consolas;

import jakarta.servlet.ServletException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
// import jakarta.validation.constraints.Digits; // PRECIO OPCIONAL
// import jakarta.validation.constraints.NotNull; // PRECIO OPCIONAL
// import jakarta.validation.constraints.PositiveOrZero; // PRECIO OPCIONAL
// import java.math.BigDecimal; // PRECIO OPCIONAL
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Configuration(proxyBeanMethods = false)
class ConsolasApi {
    private static final String COLUMNAS = "id, marca, modelo"
            // + ", precio" // PRECIO OPCIONAL
            ;

    private final JdbcClient jdbc;
    private final Validator validator;

    ConsolasApi(JdbcClient jdbc, Validator validator) {
        this.jdbc = jdbc;
        this.validator = validator;
    }

    @Bean
    RouterFunction<ServerResponse> rutasConsolas() {
        return RouterFunctions.route()
                .GET("/api/consolas", this::listar)
                .POST("/api/consolas", this::crear)
                .PUT("/api/consolas/{id}", this::editar)
                .DELETE("/api/consolas/{id}", this::eliminar)
                .build();
    }

    private ServerResponse listar(ServerRequest request) {
        var consolas = jdbc.sql("SELECT " + COLUMNAS + " FROM consolas ORDER BY id")
                .query(Consola.class)
                .list();
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(consolas);
    }

    private ServerResponse crear(ServerRequest request) throws ServletException, IOException {
        Datos datos = leerDatos(request);
        Consola consola = jdbc.sql("INSERT INTO consolas (marca, modelo"
                // + ", precio" // PRECIO OPCIONAL
                + ") VALUES (:marca, :modelo"
                // + ", :precio" // PRECIO OPCIONAL
                + ") RETURNING " + COLUMNAS)
                .param("marca", datos.marca().strip())
                .param("modelo", datos.modelo().strip())
                // .param("precio", datos.precio()) // PRECIO OPCIONAL
                .query(Consola.class)
                .single();
        return ServerResponse.status(HttpStatus.CREATED).contentType(APPLICATION_JSON).body(consola);
    }

    private ServerResponse editar(ServerRequest request) throws ServletException, IOException {
        int id = leerId(request);
        Datos datos = leerDatos(request);
        var consola = jdbc.sql("UPDATE consolas SET marca = :marca, modelo = :modelo"
                // + ", precio = :precio" // PRECIO OPCIONAL
                + " WHERE id = :id RETURNING " + COLUMNAS)
                .param("id", id)
                .param("marca", datos.marca().strip())
                .param("modelo", datos.modelo().strip())
                // .param("precio", datos.precio()) // PRECIO OPCIONAL
                .query(Consola.class)
                .optional();
        return consola.map(registro -> ServerResponse.ok().contentType(APPLICATION_JSON).body(registro))
                .orElseGet(() -> ServerResponse.notFound().build());
    }

    private ServerResponse eliminar(ServerRequest request) {
        int eliminadas = jdbc.sql("DELETE FROM consolas WHERE id = :id")
                .param("id", leerId(request))
                .update();
        return eliminadas == 0 ? ServerResponse.notFound().build() : ServerResponse.noContent().build();
    }

    private Datos leerDatos(ServerRequest request) throws ServletException, IOException {
        Datos datos = request.body(Datos.class);
        if (datos == null || !validator.validate(datos).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Revisa los datos de la consola");
        }
        return datos;
    }

    private int leerId(ServerRequest request) {
        try {
            return Integer.parseInt(request.pathVariable("id"));
        } catch (NumberFormatException error) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de consola inválido", error);
        }
    }

    private record Datos(
            @NotBlank @Size(max = 80) String marca,
            @NotBlank @Size(max = 80) String modelo
            // , @NotNull @PositiveOrZero @Digits(integer = 10, fraction = 2) BigDecimal precio // PRECIO OPCIONAL
    ) {}

    private record Consola(
            int id,
            String marca,
            String modelo
            // , BigDecimal precio // PRECIO OPCIONAL
    ) {}
}
