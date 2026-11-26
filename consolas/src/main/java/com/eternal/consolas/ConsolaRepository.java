package com.eternal.consolas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

interface ConsolaRepository extends JpaRepository<Consola, Integer> {
    @Transactional
    long deleteConsolaById(Integer id);
}
