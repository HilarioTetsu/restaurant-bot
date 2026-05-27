package com.restaurante.pedidos.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurante.pedidos.infrastructure.persistence.entity.PlatilloEntity;

@Repository
public interface IJpaPlatilloRepository extends JpaRepository<PlatilloEntity, Long> {
}