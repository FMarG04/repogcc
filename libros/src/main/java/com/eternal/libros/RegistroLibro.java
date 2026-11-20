package com.eternal.libros;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroLibro(
        @NotBlank @Size(max = 255) String titulo,
        @NotBlank @Size(max = 255) String autor,
        @NotBlank @Size(max = 32) String isbn
) {}
