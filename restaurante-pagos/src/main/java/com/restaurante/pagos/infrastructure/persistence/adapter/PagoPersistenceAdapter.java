package com.restaurante.pagos.infrastructure.persistence.adapter;

import com.restaurante.pagos.application.ports.out.IPagoRepository;
import com.restaurante.pagos.domain.model.TransaccionPago;
import com.restaurante.pagos.infrastructure.persistence.entity.PagoEntity;
import com.restaurante.pagos.infrastructure.persistence.repository.IJpaPagoRepository;
import org.springframework.stereotype.Component;

@Component
public class PagoPersistenceAdapter implements IPagoRepository {

    private final IJpaPagoRepository jpaPagoRepository;

    public PagoPersistenceAdapter(IJpaPagoRepository jpaPagoRepository) {
        this.jpaPagoRepository = jpaPagoRepository;
    }

    @Override
    public void guardar(TransaccionPago pago) {
        PagoEntity entity = new PagoEntity();
        entity.setId(pago.getId());
        entity.setPedidoId(pago.getPedidoId());
        entity.setMonto(pago.getMonto());
        entity.setEstado(pago.getEstado());
        entity.setFechaProcesamiento(pago.getFechaProcesamiento());
        entity.setDetallesValidacion(pago.getDetallesValidacion());

        PagoEntity savedEntity = jpaPagoRepository.save(entity);

        // Sincronizamos el ID generado por PostgreSQL de vuelta al Dominio
        pago.setId(savedEntity.getId());
    }
}