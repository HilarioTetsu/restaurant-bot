package com.restaurante.pedidos.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurante.pedidos.infrastructure.persistence.entity.PedidoEntity;

@Repository
public interface IJpaPedidoRepository extends JpaRepository<PedidoEntity, String> {
}