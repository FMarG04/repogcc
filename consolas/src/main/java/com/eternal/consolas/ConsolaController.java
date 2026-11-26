package com.eternal.consolas;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
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
    private final ConsolaRepository repositorio;

    public ConsolaController(ConsolaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @GetMapping
    public List<Consola> listar() {
        return repositorio.findAll(Sort.by("id"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Consola crear(@Valid @RequestBody Datos datos) {
        return repositorio.save(new Consola(datos.marca(), datos.modelo()));
    }

    @PutMapping("/{id}")
    @Transactional
    public Consola editar(@PathVariable int id, @Valid @RequestBody Datos datos) {
        Consola consola = buscar(id);
        consola.actualizar(datos.marca(), datos.modelo());
        return consola;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void eliminar(@PathVariable int id) {
        repositorio.delete(buscar(id));
    }

    private Consola buscar(int id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consola no encontrada"));
    }

    public record Datos(
            @NotBlank @Size(max = 80) String marca,
            @NotBlank @Size(max = 80) String modelo
    ) {}
}
