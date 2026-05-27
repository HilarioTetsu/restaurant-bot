package com.restaurante.interaccion.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/pruebas")
public class TenantTestController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/menu")
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<?>> obtenerMenuDelRestaurante() {
        // Hacemos una consulta nativa a "platillos". 
        // Como no le ponemos prefijo de esquema, PostgreSQL usará el search_path activo en el hilo.
        List<?> platillos = entityManager
                .createNativeQuery("SELECT id, nombre, precio FROM platillos")
                .getResultList();

        return ResponseEntity.ok(platillos);
    }
}