package com.eternal.consolas.controller;

import com.eternal.consolas.model.Consola;
import com.eternal.consolas.service.ConsolaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/consolas")
public class ConsolaController {
    private final ConsolaService servicio;

    public ConsolaController(ConsolaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Consola> listar() {
        return servicio.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Consola crear(@Valid @RequestBody Datos datos) {
        return servicio.crear(datos.marca().strip(), datos.modelo().strip(), datos.precio());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consola> editar(@PathVariable int id, @Valid @RequestBody Datos datos) {
        return ResponseEntity.of(servicio.editar(
                id, datos.marca().strip(), datos.modelo().strip(), datos.precio()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable int id) {
        if (!servicio.eliminar(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public record Datos(
            @NotBlank @Size(max = 80) String marca,
            @NotBlank @Size(max = 80) String modelo,
            @NotNull @PositiveOrZero @Digits(integer = 10, fraction = 2) BigDecimal precio
    ) {}
}
