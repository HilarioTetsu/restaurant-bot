package com.restaurante.pedidos.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.restaurante.pedidos.application.ports.out.IPedidoRepository;
import com.restaurante.pedidos.domain.model.Pedido;
import com.restaurante.pedidos.infrastructure.persistence.entity.PedidoEntity;
import com.restaurante.pedidos.infrastructure.persistence.repository.IJpaPedidoRepository;

@Component
public class PedidoPersistenceAdapter implements IPedidoRepository {

	private final IJpaPedidoRepository jpaPedidoRepository;
	
	
	
	
	public PedidoPersistenceAdapter(IJpaPedidoRepository jpaPedidoRepository) {
		this.jpaPedidoRepository = jpaPedidoRepository;
	}

	@Override
    public void guardar(Pedido pedido) {
        PedidoEntity entity = new PedidoEntity();
        entity.setId(pedido.getId());
        entity.setTelefonoCliente(pedido.getTelefonoCliente());
        entity.setEstado(pedido.getEstado());
        entity.setTotal(pedido.getTotal());
        entity.setFechaCreacion(pedido.getFechaCreacion());

        jpaPedidoRepository.save(entity);
    }
	

	@Override
    public Optional<Pedido> buscarPorId(String id) {
        return jpaPedidoRepository.findById(id)
                .map(entity -> new Pedido(
                        entity.getId(),
                        entity.getTelefonoCliente(),
                        entity.getEstado(),
                        entity.getTotal(),
                        entity.getFechaCreacion()
                ));
    }

}
