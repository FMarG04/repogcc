package com.eternal.consolas;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
// import java.math.BigDecimal; // PRECIO OPCIONAL

@Entity
@Table(name = "consolas")
public class Consola {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 80)
    private String marca;

    @Column(nullable = false, length = 80)
    private String modelo;

    // @Column(precision = 12, scale = 2) // PRECIO OPCIONAL
    // private BigDecimal precio; // PRECIO OPCIONAL

    protected Consola() {}

    void actualizar(
            String marca, String modelo
            // , BigDecimal precio // PRECIO OPCIONAL
    ) {
        this.marca = marca;
        this.modelo = modelo;
        // this.precio = precio; // PRECIO OPCIONAL
    }

    public Integer getId() {
        return id;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    // public BigDecimal getPrecio() { return precio; } // PRECIO OPCIONAL
}
