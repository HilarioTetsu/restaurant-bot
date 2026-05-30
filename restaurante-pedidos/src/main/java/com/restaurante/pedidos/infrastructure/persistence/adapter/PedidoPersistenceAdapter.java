package com.restaurante.pedidos.infrastructure.persistence.adapter;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

import com.restaurante.pedidos.application.ports.out.IPedidoRepository;
import com.restaurante.pedidos.domain.model.Pedido;
import com.restaurante.pedidos.domain.model.PedidoItem;
import com.restaurante.pedidos.domain.model.Platillo;
import com.restaurante.pedidos.infrastructure.persistence.entity.PedidoEntity;
import com.restaurante.pedidos.infrastructure.persistence.entity.PedidoItemEntity;
import com.restaurante.pedidos.infrastructure.persistence.entity.PlatilloEntity;
import com.restaurante.pedidos.infrastructure.persistence.repository.IJpaPedidoRepository;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PedidoPersistenceAdapter implements IPedidoRepository {

    private final IJpaPedidoRepository jpaPedidoRepository;

    public PedidoPersistenceAdapter(IJpaPedidoRepository jpaPedidoRepository) {
        this.jpaPedidoRepository = jpaPedidoRepository;
    }

    @Override
    @Transactional
    public void guardar(Pedido pedido) {
        PedidoEntity entity = new PedidoEntity();
        entity.setId(pedido.getId());
        entity.setTelefonoCliente(pedido.getTelefonoCliente());
        entity.setEstado(pedido.getEstado());
        entity.setTotal(pedido.getTotal());
        entity.setFechaCreacion(pedido.getFechaCreacion());

        // Mapeo de objetos puros de Dominio hacia Entidades de Persistencia
        List<PedidoItemEntity> itemEntities = pedido.getItems().stream().map(item -> {
            PedidoItemEntity itemEntity = new PedidoItemEntity();
            itemEntity.setId(item.getId());
            itemEntity.setPedido(entity);

            // Optimización de rendimiento: Instanciamos un cascarón con el ID del platillo
            PlatilloEntity platilloEntity = new PlatilloEntity();
            platilloEntity.setId(item.getPlatillo().getId());

            itemEntity.setPlatillo(platilloEntity);
            itemEntity.setCantidad(item.getCantidad());
            itemEntity.setPrecioUnitario(item.getPrecioUnitario());
            return itemEntity;
        }).collect(Collectors.toList());

        entity.setItems(itemEntities);
        jpaPedidoRepository.saveAndFlush(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorId(String id) {
        return jpaPedidoRepository.findById(id)
                .map(entity -> {
                    // 1. Rehidratamos la raíz de la sesión del pedido
                    Pedido pedido = new Pedido(
                            entity.getId(),
                            entity.getTelefonoCliente(),
                            entity.getEstado(),
                            entity.getTotal(),
                            entity.getFechaCreacion()
                    );

                    // 2. Rehidratamos cada línea guardada de comida
                    List<PedidoItem> domainItems = entity.getItems().stream().map(itemEntity -> {
                        Platillo platillo = new Platillo(
                                itemEntity.getPlatillo().getId(),
                                itemEntity.getPlatillo().getNombre(),
                                itemEntity.getPlatillo().getPrecio(),
                                itemEntity.getPlatillo().isDisponible()
                        );
                        PedidoItem domainItem = new PedidoItem(platillo, itemEntity.getCantidad());
                        domainItem.setId(itemEntity.getId());
                        return domainItem;
                    }).collect(Collectors.toList());

                    // 3. Insertamos de forma segura la colección reconstruida al agregado
                    pedido.rehidratarItems(domainItems);
                    return pedido;
                });
    }
}