package com.eternal.carros;

// Objeto devuelto por la API: cuatro datos del carro y su identificador.
public record Carro(Long id, String marca, String modelo, Integer anio, String color) {}
