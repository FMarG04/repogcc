package com.eternal.consolas.model;

// import java.math.BigDecimal; // PRECIO OPCIONAL

public record Consola(
        int id,
        String marca,
        String modelo
        // , BigDecimal precio // PRECIO OPCIONAL
) {}
