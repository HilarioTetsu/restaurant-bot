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
    public Pedido ejecutar(String pedidoId, String telefonoCliente, Long platilloId) {
       
        Platillo platillo = platilloRepository.buscarPorId(platilloId)
                .orElseThrow(() -> new IllegalArgumentException("El platillo con ID " + platilloId + " no existe."));

        // 2. Obtener el pedido (sesión de bot) activo, o inicializar uno nuevo si es su primer mensaje
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseGet(() -> new Pedido(pedidoId, telefonoCliente));

        // 3. Delegar la mutación al modelo de Dominio (aplica las reglas de estado y totales)
        pedido.agregarPlatillo(platillo);

        // 4. Salvar el estado final mediante el puerto de salida
        pedidoRepository.guardar(pedido);

        return pedido;
    }
}
