package com.restaurante.interaccion.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/pruebas")
public class TenantTestController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/tenant")
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, String>> verificarSchemaActual() {
       
        String schemaActual = (String) entityManager
                .createNativeQuery("SHOW search_path")
                .getSingleResult();

        return ResponseEntity.ok(Map.of(
                "status", "OK",
                "schema_en_uso", schemaActual
        ));
    }
}