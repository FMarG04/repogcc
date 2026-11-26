package com.eternal.consolas.service;

import com.eternal.consolas.model.Consola;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

@Service
public class ConsolaService {
    private final JdbcClient jdbc;

    public ConsolaService(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public List<Consola> listar() {
        return jdbc.sql("SELECT id, marca, modelo, precio FROM consolas ORDER BY id")
                .query(Consola.class)
                .list();
    }

    public Consola crear(String marca, String modelo, BigDecimal precio) {
        return jdbc.sql("""
                INSERT INTO consolas (marca, modelo, precio)
                VALUES (:marca, :modelo, :precio)
                RETURNING id, marca, modelo, precio
                """)
                .param("marca", marca)
                .param("modelo", modelo)
                .param("precio", precio)
                .query(Consola.class)
                .single();
    }

    public Optional<Consola> editar(int id, String marca, String modelo, BigDecimal precio) {
        return jdbc.sql("""
                UPDATE consolas SET marca = :marca, modelo = :modelo, precio = :precio
                WHERE id = :id
                RETURNING id, marca, modelo, precio
                """)
                .param("id", id)
                .param("marca", marca)
                .param("modelo", modelo)
                .param("precio", precio)
                .query(Consola.class)
                .optional();
    }

    public boolean eliminar(int id) {
        return jdbc.sql("DELETE FROM consolas WHERE id = :id").param("id", id).update() == 1;
    }
}
