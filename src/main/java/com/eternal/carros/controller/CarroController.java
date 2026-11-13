package com.eternal.carros.controller;

import com.eternal.carros.model.Carro;
import com.eternal.carros.service.CarroService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/carros")

public class CarroController{

    private final CarroService carroService;

    public CarroController(CarroService carroService){
        this.carroService = carroService;
    }
    
    @GetMapping
    public List<Carro> listar() {
    return carroService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Carro registrar(@RequestBody Carro carro) {
        return carroService.registrar(carro);
    }

}

