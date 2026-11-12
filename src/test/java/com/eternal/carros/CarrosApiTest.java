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
}
