package com.proyectoUltimo.Proyecto.controller;

import com.proyectoUltimo.Proyecto.model.Order;
import com.proyectoUltimo.Proyecto.model.Product;
import com.proyectoUltimo.Proyecto.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Controlador para gestionar los pedidos desde el panel de administrador.
 * Permite visualizar todos los pedidos y eliminarlos por ID.
 */
@Controller
public class AdminPedidos {

    // Inyección del repositorio para acceder a la base de datos de pedidos
    @Autowired
    private OrderRepository orderRepository;

    /**
     * Muestra la lista de todos los pedidos registrados en la base de datos.
     *
     * @param model Objeto que permite pasar datos a la vista.
     * @return Nombre del archivo HTML que renderiza la lista de pedidos (sin .html).
     */
    @GetMapping("/admin_pedidos") // Ruta para acceder a la página de pedidos desde el navegador
    public String Pedidos(Model model) {
        // Obtiene todos los pedidos desde la base de datos
        List<Order> orders = orderRepository.findAll();

        // Agrega la lista de pedidos al modelo para mostrar en la vista HTML
        model.addAttribute("orders", orders);

        // Devuelve el nombre de la vista (ej. admin_pedidos.html)
        return "admin_pedidos";
    }

    /**
     * Elimina un pedido específico de la base de datos según su ID.
     *
     * @param id ID del pedido a eliminar.
     * @return Redirecciona nuevamente a la lista de pedidos.
     */
    @GetMapping("/admin_pedidos/eliminar/{id}") // Ruta para eliminar un pedido por su ID
    public String eliminarPedido(@PathVariable("id") Integer id) {
        // Elimina el pedido del repositorio (y por tanto de la base de datos)
        orderRepository.deleteById(id);

        // Redirige de nuevo a la lista de pedidos después de eliminar
        return "redirect:/admin_pedidos";
    }
}