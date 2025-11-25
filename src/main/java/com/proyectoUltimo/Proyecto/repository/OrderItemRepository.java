package com.proyectoUltimo.Proyecto.repository;

import com.proyectoUltimo.Proyecto.model.Order;
import com.proyectoUltimo.Proyecto.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad OrderItem.
 * Permite realizar operaciones CRUD sobre los items que forman parte de una orden.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    /**
     * Método para obtener todos los items asociados a una orden específica.
     * Spring Data JPA genera automáticamente la consulta basada en el nombre del método.
     *
     * @param order La orden de la que se desean obtener los items.
     * @return Lista de items pertenecientes a la orden indicada.
     */
    // List<OrderItem> findByOrder(Order order);
}
