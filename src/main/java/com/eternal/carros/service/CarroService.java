package com.eternal.carros.service;

import com.eternal.carros.model.Carro;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CarroService {

    private final List<Carro> carros = new ArrayList<>();
    private final AtomicLong contador = new AtomicLong(1);

    public List<Carro> listar() {
        return new ArrayList<>(carros);
    }

    public Carro registrar(Carro carro) {
        carro.setId(contador.getAndIncrement());
        carros.add(carro);
        return carro;
    }
}
  
  