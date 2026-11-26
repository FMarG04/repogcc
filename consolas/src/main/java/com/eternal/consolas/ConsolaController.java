package com.eternal.consolas;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
// import jakarta.validation.constraints.Digits; // PRECIO OPCIONAL
// import jakarta.validation.constraints.NotNull; // PRECIO OPCIONAL
// import jakarta.validation.constraints.PositiveOrZero; // PRECIO OPCIONAL
// import java.math.BigDecimal; // PRECIO OPCIONAL
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/consolas")
public class ConsolaController {
    private static final String COLUMNAS = "id, marca, modelo"
            // + ", precio" // PRECIO OPCIONAL
            ;

    private final JdbcClient jdbc;

    public ConsolaController(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping
    public List<Consola> listar() {
        return jdbc.sql("SELECT " + COLUMNAS + " FROM consolas ORDER BY id")
                .query(Consola.class)
                .list();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Consola crear(@Valid @RequestBody Datos datos) {
        return jdbc.sql("INSERT INTO consolas (marca, modelo"
                // + ", precio" // PRECIO OPCIONAL
                + ") VALUES (:marca, :modelo"
                // + ", :precio" // PRECIO OPCIONAL
                + ") RETURNING " + COLUMNAS)
                .param("marca", datos.marca().strip())
                .param("modelo", datos.modelo().strip())
                // .param("precio", datos.precio()) // PRECIO OPCIONAL
                .query(Consola.class)
                .single();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consola> editar(@PathVariable int id, @Valid @RequestBody Datos datos) {
        var consola = jdbc.sql("UPDATE consolas SET marca = :marca, modelo = :modelo"
                // + ", precio = :precio" // PRECIO OPCIONAL
                + " WHERE id = :id RETURNING " + COLUMNAS)
                .param("id", id)
                .param("marca", datos.marca().strip())
                .param("modelo", datos.modelo().strip())
                // .param("precio", datos.precio()) // PRECIO OPCIONAL
                .query(Consola.class)
                .optional();
        return ResponseEntity.of(consola);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable int id) {
        int eliminadas = jdbc.sql("DELETE FROM consolas WHERE id = :id").param("id", id).update();
        if (eliminadas == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public record Datos(
            @NotBlank @Size(max = 80) String marca,
            @NotBlank @Size(max = 80) String modelo
            // , @NotNull @PositiveOrZero @Digits(integer = 10, fraction = 2) BigDecimal precio // PRECIO OPCIONAL
    ) {}

    public record Consola(
            int id,
            String marca,
            String modelo
            // , BigDecimal precio // PRECIO OPCIONAL
    ) {}
}
