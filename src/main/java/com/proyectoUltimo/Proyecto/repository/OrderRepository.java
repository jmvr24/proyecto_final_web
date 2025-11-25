package com.proyectoUltimo.Proyecto.repository;

import com.proyectoUltimo.Proyecto.model.Order;
import com.proyectoUltimo.Proyecto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de la entidad Order.
 * Esta interfaz permite realizar operaciones CRUD sobre la tabla de pedidos (Order)
 * utilizando JPA de manera automática sin necesidad de escribir SQL manual.
 */
@Repository // Indica que esta interfaz es un componente de repositorio de Spring
public interface OrderRepository extends JpaRepository<Order, Integer> {

    /**
     * Busca un pedido según su ID de transacción.
     * Este método será implementado automáticamente por Spring Data JPA,
     * gracias al nombre del método siguiendo la convención.
     *
     * @param transactionId ID de la transacción del pedido.
     * @return El objeto Order que contiene ese ID de transacción, o null si no existe.
     */
    Order findByTransactionId(String transactionId);

    /*
     * También podrías tener métodos como:
     * List<Order> findByUser(User user);
     * Para buscar todos los pedidos de un usuario específico, si descomentas esa línea.
     * Spring generará automáticamente la consulta SQL si el campo "user" existe en Order.
     */
}