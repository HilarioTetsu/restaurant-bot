package com.restaurante.pagos.infrastructure.persistence.repository;

import com.restaurante.pagos.infrastructure.persistence.entity.PagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IJpaPagoRepository extends JpaRepository<PagoEntity, Long> {
}