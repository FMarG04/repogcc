package com.eternal.libros.controller;

import com.eternal.libros.model.Libro;
import com.eternal.libros.service.LibroService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/libros")
public class LibroController {
    private final LibroService servicio;

    public LibroController(LibroService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Libro> listar() {
        return servicio.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Libro registrar(@Valid @RequestBody NuevoLibro libro) {
        return servicio.registrar(libro.titulo(), libro.autor(), libro.isbn());
    }

    public record NuevoLibro(
            @NotBlank String titulo,
            @NotBlank String autor,
            @NotBlank String isbn
    ) {}
}
