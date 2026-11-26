package com.eternal.libros.service;

import com.eternal.libros.model.Libro;
import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

@Service
public class LibroService {
    private final JdbcClient jdbc;

    public LibroService(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public List<Libro> listar() {
        return jdbc.sql("""
                SELECT id, titulo, autor, isbn
                FROM libros
                ORDER BY id
                """)
                .query(Libro.class)
                .list();
    }

    public Libro registrar(String titulo, String autor, String isbn) {
        return jdbc.sql("""
                INSERT INTO libros (titulo, autor, isbn)
                VALUES (:titulo, :autor, :isbn)
                RETURNING id, titulo, autor, isbn
                """)
                .param("titulo", titulo)
                .param("autor", autor)
                .param("isbn", isbn)
                .query(Libro.class)
                .single();
    }
}
