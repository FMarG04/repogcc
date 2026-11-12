package com.eternal.carros;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// El cliente envia estos cuatro campos; la aplicacion genera el id.
public record CarroRequest(
    @NotBlank(message = "La marca es obligatoria") @Size(max = 60) String marca,
    @NotBlank(message = "El modelo es obligatorio") @Size(max = 80) String modelo,
    @NotNull(message = "El anio es obligatorio") @Min(value = 1886, message = "El anio debe ser 1886 o posterior") Integer anio,
    @NotBlank(message = "El color es obligatorio") @Size(max = 40) String color
) {}
