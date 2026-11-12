package com.eternal.carros;

import jakarta.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carros")
public class CarroController {
    private final ConcurrentHashMap<Long, Carro> carros = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong();

    // GET: consulta los carros guardados, ordenados por id.
    @GetMapping
    public List<Carro> listar() {
        return carros.values().stream()
            .sorted(Comparator.comparing(Carro::id)).toList();
    }

    // POST: valida los cuatro datos, asigna id y guarda el carro en memoria.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Carro registrar(@Valid @RequestBody CarroRequest datos) {
        long id = secuencia.incrementAndGet();
        Carro carro = new Carro(id, datos.marca().trim(), datos.modelo().trim(),
            datos.anio(), datos.color().trim());
        carros.put(id, carro);
        return carro;
    }

    // PUT: reemplaza los cuatro datos del carro sin cambiar su id.
    @PutMapping("/{id}")
    public Carro actualizar(@PathVariable Long id, @Valid @RequestBody CarroRequest datos) {
        Carro actualizado = carros.computeIfPresent(id, (clave, anterior) ->
            new Carro(clave, datos.marca().trim(), datos.modelo().trim(),
                datos.anio(), datos.color().trim()));
        if (actualizado == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                HttpStatus.NOT_FOUND, "No existe el carro con id " + id);
        }
        return actualizado;
    }

    // DELETE: elimina el carro indicado; 204 significa exito sin cuerpo.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        if (carros.remove(id) == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                HttpStatus.NOT_FOUND, "No existe el carro con id " + id);
        }
    }
}
