package com.proyectoUltimo.Proyecto.service;

import com.proyectoUltimo.Proyecto.dto.ProductDto;
import com.proyectoUltimo.Proyecto.model.Product;
import com.proyectoUltimo.Proyecto.repository.ProductsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class PrReportService {

    private final ProductsRepository productsRepository;


    public PrReportService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    /**
     * Obtiene todos los productos ordenados por ID ascendente
     */
    public List<Product> listAllProducts() {
        return productsRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    /**
     * Guarda un nuevo producto con su imagen
     */
    public void saveProduct(ProductDto productDto) throws Exception {

        if (productDto.getImageFile().isEmpty()) {
            throw new Exception("La imagen es requerida");
        }

        MultipartFile image = productDto.getImageFile();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

        String uploadDir = "public/images/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = image.getInputStream()) {
            Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                    StandardCopyOption.REPLACE_EXISTING);
        }

        // Crear y guardar producto
        Product product = new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setImageFileName(storageFileName);
        product.setQuantity(productDto.getQuantity());

        productsRepository.save(product);
    }

    /**
     * Obtiene un producto por ID
     */
    public Product getProductById(int id) throws Exception {
        return productsRepository.findById(id)
                .orElseThrow(() -> new Exception("Producto no encontrado con ID: " + id));
    }

    /**
     * Actualiza un producto existente
     */
    public void updateProduct(int id, ProductDto productDto) throws Exception {
        Product product = getProductById(id);

        if (!productDto.getImageFile().isEmpty()) {
            // Eliminar imagen antigua
            String uploadDir = "public/images/";
            Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());
            try {
                Files.delete(oldImagePath);
            } catch (Exception ex) {
                System.out.println("Error al eliminar imagen antigua: " + ex.getMessage());
            }

            // Guardar nueva imagen
            MultipartFile image = productDto.getImageFile();
            String storageFileName = new Date().getTime() + "__" + image.getOriginalFilename();

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }

            product.setImageFileName(storageFileName);
        }

        // Actualizar campos
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setQuantity(productDto.getQuantity());

        productsRepository.save(product);
    }

    /**
     * Elimina un producto y su imagen asociada
     */
    public void deleteProduct(int id) throws Exception {
        Product product = getProductById(id);

        // Eliminar imagen
        Path imagePath = Paths.get("public/images/" + product.getImageFileName());
        try {
            Files.delete(imagePath);
        } catch (Exception ex) {
            System.out.println("Error al eliminar imagen: " + ex.getMessage());
        }

        // Eliminar producto
        productsRepository.delete(product);
    }
}

