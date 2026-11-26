package com.eternal.consolas.service;

import com.eternal.consolas.model.Consola;
// import java.math.BigDecimal; // PRECIO OPCIONAL
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

@Service
public class ConsolaService {
    private static final String COLUMNAS = "id, marca, modelo"
            // + ", precio" // PRECIO OPCIONAL
            ;

    private final JdbcClient jdbc;

    public ConsolaService(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public List<Consola> listar() {
        return jdbc.sql("SELECT " + COLUMNAS + " FROM consolas ORDER BY id")
                .query(Consola.class)
                .list();
    }

    public Consola crear(
            String marca, String modelo
            // , BigDecimal precio // PRECIO OPCIONAL
    ) {
        return jdbc.sql("INSERT INTO consolas (marca, modelo"
                // + ", precio" // PRECIO OPCIONAL
                + ") VALUES (:marca, :modelo"
                // + ", :precio" // PRECIO OPCIONAL
                + ") RETURNING " + COLUMNAS)
                .param("marca", marca)
                .param("modelo", modelo)
                // .param("precio", precio) // PRECIO OPCIONAL
                .query(Consola.class)
                .single();
    }

    public Optional<Consola> editar(
            int id, String marca, String modelo
            // , BigDecimal precio // PRECIO OPCIONAL
    ) {
        return jdbc.sql("UPDATE consolas SET marca = :marca, modelo = :modelo"
                // + ", precio = :precio" // PRECIO OPCIONAL
                + " WHERE id = :id RETURNING " + COLUMNAS)
                .param("id", id)
                .param("marca", marca)
                .param("modelo", modelo)
                // .param("precio", precio) // PRECIO OPCIONAL
                .query(Consola.class)
                .optional();
    }

    public boolean eliminar(int id) {
        return jdbc.sql("DELETE FROM consolas WHERE id = :id").param("id", id).update() == 1;
    }
}
