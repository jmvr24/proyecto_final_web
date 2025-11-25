package com.proyectoUltimo.Proyecto.controller;

import com.proyectoUltimo.Proyecto.model.Product;
import com.proyectoUltimo.Proyecto.repository.ProductsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador encargado de gestionar el inventario de productos.
 * Permite aumentar o disminuir el stock de un producto específico.
 */
@Controller
@RequestMapping("/admin_producto/inventario")  // Ruta base para las operaciones de inventario
public class InventoryController {

    @Autowired
    private ProductsRepository repo;  // Repositorio para acceder y modificar productos en la base de datos

    /**
     * Aumenta el stock de un producto específico según su ID.
     *
     * @param id        ID del producto al que se le aumentará el stock
     * @param cantidad  Cantidad de unidades a añadir al inventario
     * @return Redirección a la página principal de productos
     */
    @PostMapping("/aumentar")
    public String aumentarStock(@RequestParam int id, @RequestParam int cantidad) {
        try {
            // Busca el producto por ID
            Product product = repo.findById(id).get();

            // Aumenta la cantidad actual con la nueva cantidad
            product.setQuantity(product.getQuantity() + cantidad);

            // Guarda los cambios en la base de datos
            repo.save(product);
        } catch (Exception ex) {
            // Muestra cualquier error que ocurra
            System.out.println("Exception: " + ex.getMessage());
        }

        // Redirige a la página principal de productos
        return "redirect:/admin_producto";
    }

    /**
     * Disminuye el stock de un producto específico según su ID.
     * Si el resultado es menor a 0, se establece en 0.
     *
     * @param id        ID del producto al que se le disminuirá el stock
     * @param cantidad  Cantidad de unidades a restar del inventario
     * @return Redirección a la página principal de productos
     */
    @PostMapping("/disminuir")
    public String disminuirStock(@RequestParam int id, @RequestParam int cantidad) {
        try {
            // Busca el producto por ID
            Product product = repo.findById(id).get();

            // Calcula la nueva cantidad asegurándose de que no sea menor que 0
            int nuevaCantidad = product.getQuantity() - cantidad;
            if (nuevaCantidad < 0) {
                nuevaCantidad = 0;
            }

            // Establece la nueva cantidad
            product.setQuantity(nuevaCantidad);

            // Guarda los cambios en la base de datos
            repo.save(product);
        } catch (Exception ex) {
            // Muestra cualquier error que ocurra
            System.out.println("Exception: " + ex.getMessage());
        }

        // Redirige a la página principal de productos
        return "redirect:/admin_producto";
    }
}
