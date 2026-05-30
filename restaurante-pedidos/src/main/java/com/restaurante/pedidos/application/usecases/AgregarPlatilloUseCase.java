package com.restaurante.pedidos.application.usecases;

import com.restaurante.pedidos.application.ports.in.IAgregarPlatilloPort;
import com.restaurante.pedidos.application.ports.out.IPedidoRepository;
import com.restaurante.pedidos.application.ports.out.IPlatilloRepository;
import com.restaurante.pedidos.domain.model.Pedido;
import com.restaurante.pedidos.domain.model.Platillo;

public class AgregarPlatilloUseCase implements IAgregarPlatilloPort{

	private final IPedidoRepository pedidoRepository;
    private final IPlatilloRepository platilloRepository;
    
    
    
	
	
	public AgregarPlatilloUseCase(IPedidoRepository pedidoRepository, IPlatilloRepository platilloRepository) {
		this.pedidoRepository = pedidoRepository;
		this.platilloRepository = platilloRepository;
	}




    @Override
    public Pedido ejecutar(String pedidoId, String telefonoCliente, Long platilloId, int cantidad) {
        Platillo platillo = platilloRepository.buscarPorId(platilloId)
                .orElseThrow(() -> new IllegalArgumentException("El platillo con ID " + platilloId + " no existe."));

        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseGet(() -> new Pedido(pedidoId, telefonoCliente));

        // Delegamos la mutación pasando la cantidad correspondiente
        pedido.agregarPlatillo(platillo, cantidad);

        pedidoRepository.guardar(pedido);

        return pedido;
    }
}
