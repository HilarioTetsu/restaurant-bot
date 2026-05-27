package com.restaurante.pedidos.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.restaurante.pedidos.application.ports.out.IPlatilloRepository;
import com.restaurante.pedidos.domain.model.Platillo;
import com.restaurante.pedidos.infrastructure.persistence.repository.IJpaPlatilloRepository;

@Component
public class PlatilloPersistenceAdapter implements IPlatilloRepository {

	private final IJpaPlatilloRepository jpaPlatilloRepository;

	public PlatilloPersistenceAdapter(IJpaPlatilloRepository jpaPlatilloRepository) {
		this.jpaPlatilloRepository = jpaPlatilloRepository;
	}

	@Override
    public Optional<Platillo> buscarPorId(Long id) {
        return jpaPlatilloRepository.findById(id)
                .map(entity -> new Platillo(
                        entity.getId(),
                        entity.getNombre(),
                        entity.getPrecio(),
                        entity.isDisponible()
                ));
    }
	
	
	
}
