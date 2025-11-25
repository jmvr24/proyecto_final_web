package com.proyectoUltimo.Proyecto.controller;

import com.proyectoUltimo.Proyecto.dto.ProductDto;
import com.proyectoUltimo.Proyecto.model.Product;
import com.proyectoUltimo.Proyecto.repository.ProductsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controlador para mostrar productos al público (clientes).
 * Maneja las solicitudes a la ruta "/producto".
 */
@Controller
@RequestMapping("/producto")  // Ruta pública accesible para los clientes
public class PublicProductsController {

    @Autowired
    private ProductsRepository repo;  // Inyecta el repositorio de productos

    /**
     * Muestra todos los productos disponibles en orden descendente por ID.
     *
     * @param model el modelo que se usa para pasar datos a la vista HTML
     * @return el nombre del archivo HTML "producto.html"
     */
    @GetMapping  // Cuando se accede a /producto (GET), se ejecuta este método
    public String showPublicProducts(Model model) {
        // Obtiene todos los productos ordenados por ID de forma descendente (más recientes primero)
        List<Product> products = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));

        // Agrega la lista de productos al modelo para que estén disponibles en la vista HTML
        model.addAttribute("products", products);

        // Devuelve el nombre del archivo HTML que se debe renderizar (src/main/resources/templates/producto.html)
        return "producto";
    }
}