package com.proyectoUltimo.Proyecto.controller;

import com.proyectoUltimo.Proyecto.model.Product;
import com.proyectoUltimo.Proyecto.repository.ProductsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST que expone endpoints para gestionar productos en formato JSON.
 * Permite listar y eliminar productos. Utiliza Swagger para documentar la API.
 */
@RestController // Indica que este controlador devuelve datos (JSON) en vez de vistas HTML
@RequestMapping("/api/products") // Ruta base de la API REST para productos
public class ProductsApiController {

    // Inyecta automáticamente el repositorio de productos
    @Autowired
    private ProductsRepository repo;

    /**
     * Endpoint para obtener todos los productos registrados en la base de datos.
     * Devuelve los productos ordenados por ID de forma descendente.
     *
     * @return Lista de productos.
     */
    @GetMapping // Maneja solicitudes GET a "/api/products"
    @Operation(summary = "Obtener todos los productos", description = "Devuelve la lista de todos los productos en orden descendente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<Product> getAllProducts() {
        // Retorna todos los productos ordenados por ID descendente
        return repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    /**
     * Endpoint para eliminar un producto específico por su ID.
     * También elimina la imagen asociada del sistema de archivos.
     *
     * @param id ID del producto a eliminar.
     * @return ResponseEntity indicando el resultado de la operación.
     */
    @DeleteMapping("/{id}") // Maneja solicitudes DELETE a "/api/products/{id}"
    @Operation(summary = "Eliminar producto", description = "Elimina un producto específico según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        // Busca el producto en la base de datos
        Optional<Product> optionalProduct = repo.findById(id);

        // Verifica si el producto existe
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            // Ruta de la imagen asociada al producto
            Path imagePath = Paths.get("public/images/" + product.getImageFileName());

            // Intenta eliminar la imagen del sistema de archivos
            try {
                Files.deleteIfExists(imagePath); // Borra la imagen si existe
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage()); // Muestra error en consola si ocurre
            }

            // Elimina el producto de la base de datos
            repo.delete(product);

            // Retorna una respuesta HTTP 200 (OK)
            return ResponseEntity.ok().build();
        } else {
            // Si el producto no existe, retorna 404 (Not Found)
            return ResponseEntity.notFound().build();
        }
    }

    // Aquí podrías agregar más endpoints REST (crear, editar, buscar por nombre, etc.)
    // Ejemplos:
    // @PostMapping -> Para agregar un nuevo producto
    // @PutMapping("/{id}") -> Para actualizar un producto existente
    // @GetMapping("/{id}") -> Para obtener un producto por ID

}

