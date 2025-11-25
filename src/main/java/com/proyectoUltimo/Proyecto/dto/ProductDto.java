package com.proyectoUltimo.Proyecto.dto;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Clase ProductDto (Data Transfer Object) que representa los datos necesarios
 * para crear o actualizar un producto dentro del sistema.
 * Contiene validaciones para garantizar que los datos cumplan con las reglas del negocio.

 * Usada para transferir datos entre el front-end y el back-end.
 *
 * @author Juan Manuel Villalba Rincón
 * @version 1.0
 */
public class ProductDto {

    // Nombre del producto (obligatorio)
    @NotEmpty(message = "The name is required")
    private String name;

    // Marca del producto (obligatoria)
    @NotEmpty(message = "The brand is required")
    private String brand;

    // Categoría del producto (obligatoria)
    @NotEmpty(message = "The category is required")
    private String category;

    // Precio del producto (mínimo 0)
    @Min(0)
    private double price;

    // Descripción del producto (mínimo 10 y máximo 2000 caracteres)
    @Size(min = 10, message = "The description should be at least 10 characters")
    @Size(max = 2000, message = "The description cannot exceed 2000 characters")
    private String description;

    // Imagen del producto enviada desde el formulario (archivo)
    private String imageFile;

    // Cantidad disponible del producto (mínimo 0, valor por defecto 0)
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer quantity = 0;

    // ===================== Getters y Setters ========================

    /**
     * Obtiene el nombre del producto.
     * @return Nombre del producto
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del producto.
     * @param name Nombre nuevo del producto
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene la marca del producto.
     * @return Marca del producto
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Establece la marca del producto.
     * @param brand Nueva marca del producto
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Obtiene la categoría del producto.
     * @return Categoría actual
     */
    public String getCategory() {
        return category;
    }

    /**
     * Establece la categoría del producto.
     * @param category Nueva categoría
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Obtiene el precio del producto.
     * @return Precio actual
     */
    public double getPrice() {
        return price;
    }

    /**
     * Establece un nuevo precio para el producto.
     * @param price Precio nuevo
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Obtiene la descripción del producto.
     * @return Descripción
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece una nueva descripción para el producto.
     * @param description Nueva descripción
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtiene el archivo de imagen del producto.
     * @return Imagen como MultipartFile
     */
    public String getImageFile() {
        return imageFile;
    }

    /**
     * Establece un nuevo archivo de imagen para el producto.
     * @param imageFile Archivo de imagen
     */
    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * Obtiene la cantidad disponible del producto.
     * @return Cantidad actual
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Establece la cantidad disponible del producto.
     * @param quantity Nueva cantidad
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}