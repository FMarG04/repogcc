package com.eternal.carros;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarrosApiTest {
    @Autowired TestRestTemplate cliente;

    @Test
    void registraYConsultaLosCuatroDatos() {
        var respuesta = cliente.postForEntity("/api/carros",
            new CarroRequest("Toyota", "Corolla", 2024, "Blanco"), Carro.class);
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        Carro creado = respuesta.getBody();
        assertNotNull(creado);
        assertNotNull(creado.id());
        var consulta = cliente.getForEntity("/api/carros", Carro[].class);
        assertEquals(HttpStatus.OK, consulta.getStatusCode());
        assertNotNull(consulta.getBody());
        assertTrue(java.util.Arrays.asList(consulta.getBody()).contains(creado));
        assertEquals("Toyota", creado.marca());
        assertEquals("Corolla", creado.modelo());
        assertEquals(2024, creado.anio());
        assertEquals("Blanco", creado.color());
    }

    @Test
    void rechazaDatosIncompletos() {
        var respuesta = cliente.postForEntity("/api/carros",
            Map.of("marca", "Toyota"), Map.class);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertTrue(respuesta.getBody().containsKey("errores"));
    }

    @Test
    void rechazaAnioAnteriorAlAutomovil() {
        var respuesta = cliente.postForEntity("/api/carros",
            new CarroRequest("Toyota", "Corolla", 1800, "Blanco"), Map.class);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
    }

    @Test
    void actualizaLosCuatroDatosConservandoId() {
        Carro creado = cliente.postForObject("/api/carros",
            new CarroRequest("Toyota", "Corolla", 2024, "Blanco"), Carro.class);
        var datos = new CarroRequest("Honda", "Civic", 2025, "Negro");
        var respuesta = cliente.exchange("/api/carros/" + creado.id(), HttpMethod.PUT,
            new HttpEntity<>(datos), Carro.class);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        Carro esperado = new Carro(creado.id(), "Honda", "Civic", 2025, "Negro");
        assertEquals(esperado, respuesta.getBody());
        assertTrue(java.util.Arrays.asList(cliente.getForObject("/api/carros", Carro[].class)).contains(esperado));
    }

    @Test
    void eliminaYNoPermiteEliminarDosVeces() {
        Carro creado = cliente.postForObject("/api/carros",
            new CarroRequest("Mazda", "3", 2023, "Rojo"), Carro.class);
        String ruta = "/api/carros/" + creado.id();
        var respuesta = cliente.exchange(ruta, HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        assertTrue(java.util.Arrays.stream(cliente.getForObject("/api/carros", Carro[].class))
            .noneMatch(c -> c.id().equals(creado.id())));
        assertEquals(HttpStatus.NOT_FOUND,
            cliente.exchange(ruta, HttpMethod.DELETE, HttpEntity.EMPTY, String.class).getStatusCode());
    }

    @Test
    void putInexistenteDevuelve404SinCrearCarro() {
        var respuesta = cliente.exchange("/api/carros/999999", HttpMethod.PUT,
            new HttpEntity<>(new CarroRequest("Honda", "Civic", 2025, "Negro")), String.class);
        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertTrue(java.util.Arrays.stream(cliente.getForObject("/api/carros", Carro[].class))
            .noneMatch(c -> c.id().equals(999999L)));
    }

    @Test
    void putInvalidoNoModificaElCarro() {
        Carro creado = cliente.postForObject("/api/carros",
            new CarroRequest("Kia", "Rio", 2022, "Azul"), Carro.class);
        var respuesta = cliente.exchange("/api/carros/" + creado.id(), HttpMethod.PUT,
            new HttpEntity<>(java.util.Map.of("marca", "Honda")), String.class);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertTrue(java.util.Arrays.asList(cliente.getForObject("/api/carros", Carro[].class)).contains(creado));
    }
}
