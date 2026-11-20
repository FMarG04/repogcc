package com.eternal.libros;

import java.util.List;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class LibroRepository {
    private final JdbcClient jdbc;

    public LibroRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public List<Libro> listar() {
        return jdbc.sql("SELECT id, titulo, autor, isbn FROM libros ORDER BY id")
                .query(Libro.class)
                .list();
    }

    public Libro registrar(RegistroLibro libro) {
        return jdbc.sql("""
                INSERT INTO libros (titulo, autor, isbn)
                VALUES (:titulo, :autor, :isbn)
                RETURNING id, titulo, autor, isbn
                """)
                .param("titulo", libro.titulo().strip())
                .param("autor", libro.autor().strip())
                .param("isbn", libro.isbn().strip())
                .query(Libro.class)
                .single();
    }
}
