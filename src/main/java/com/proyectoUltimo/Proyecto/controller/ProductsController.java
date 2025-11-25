package com.proyectoUltimo.Proyecto.controller;

import com.proyectoUltimo.Proyecto.dto.ProductDto;
import com.proyectoUltimo.Proyecto.model.Product;
import com.proyectoUltimo.Proyecto.repository.ProductsRepository;
import com.proyectoUltimo.Proyecto.service.ImageService;
import com.proyectoUltimo.Proyecto.service.PrReportService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para administrar productos desde el panel de administrador. Ruta
 * base: /admin_producto
 */
@Controller
@RequestMapping("/admin_producto")
public class ProductsController {

	@Autowired
	private ProductsRepository repo;

	@Autowired
	private ResourceLoader resourceLoader; // Para cargar recursos como archivos .jrxml (JasperReports)

	@Autowired
	private DataSource dataSource; // Fuente de datos (conexión a la base de datos)

	@Autowired
	private PrReportService prReportService; // Servicio personalizado para reportes (no usado aquí directamente)

	@Autowired
	private ImageService imageService;

	/**
	 * Muestra la lista de productos en el panel de administración.
	 */
	@GetMapping
	public String showProductList(Model model) {
		List<Product> products = repo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		model.addAttribute("products", products); // Agrega la lista al modelo
		return "admin_producto"; // Vista: admin_producto.html
	}

	/**
	 * Muestra la página de creación de productos.
	 */
	@GetMapping("/CreateProduct")
	public String showCreatePage(Model model) {
		ProductDto productDto = new ProductDto();
		model.addAttribute("productDto", productDto);
		return "CreateProduct";
	}

	/**
	 * Procesa el formulario de creación de productos.
	 */
	@PostMapping("/CreateProduct")
	public String createPage(@Valid @ModelAttribute ProductDto productDto, BindingResult result,
			@RequestParam("imagen") MultipartFile imagen) {

		if (result.hasErrors()) {
			return "CreateProduct";
		}

		if (!imagen.isEmpty()) {
			String url = imageService.save(imagen);
			productDto.setImageFile(url);
		}

		Product product = new Product();
		product.setName(productDto.getName());
		product.setBrand(productDto.getBrand());
		product.setCategory(productDto.getCategory());
		product.setPrice(productDto.getPrice());
		product.setDescription(productDto.getDescription());
		product.setCreatedAt(LocalDateTime.now());
		product.setImageFileName(productDto.getImageFile());
		product.setQuantity(productDto.getQuantity());

		repo.save(product);

		return "redirect:/admin_producto";
	}

	/**
	 * Muestra el formulario para editar un producto existente.
	 */
	@GetMapping("/EditProduct")
	public String ShowEditPage(Model model, @RequestParam int id) {
		try {
			Product product = repo.findById(id).get();
			model.addAttribute("product", product);

			ProductDto productDto = new ProductDto();
			productDto.setName(product.getName());
			productDto.setBrand(product.getBrand());
			productDto.setCategory(product.getCategory());
			productDto.setPrice(product.getPrice());
			productDto.setDescription(product.getDescription());
			productDto.setQuantity(product.getQuantity());

			model.addAttribute("productDto", productDto);

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/admin_producto";
		}
		return "EditProduct";
	}

	/**
	 * Procesa la edición de un producto.
	 */
	@PostMapping("/EditProduct")
	public String updateProduct(Model model, @RequestParam("productId") int id,
			@Valid @ModelAttribute ProductDto productDto, @RequestParam("imagen") MultipartFile imagen,
			BindingResult result) {
		try {
			Product product = repo.findById(id).get();
			model.addAttribute("product", product);

			if (result.hasErrors()) {
				return "redirect: /admin_producto/EditProduct (id=" + id + ")";
			}

			if (!imagen.isEmpty()) {
				String url = imageService.save(imagen);
				productDto.setImageFile(url);
			}

			product.setName(productDto.getName());
			product.setBrand(productDto.getBrand());
			product.setCategory(productDto.getCategory());
			product.setPrice(productDto.getPrice());
			product.setDescription(productDto.getDescription());
			product.setCreatedAt(LocalDateTime.now());
			product.setImageFileName(productDto.getImageFile());
			product.setQuantity(productDto.getQuantity());

			repo.save(product);

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}

		return "redirect:/admin_producto";
	}

	/**
	 * Elimina un producto y su imagen asociada.
	 */
	@GetMapping("/ProductDelete")
	public String deleteProduct(@RequestParam int id) {
		try {
			Product product = repo.findById(id).get();

			// Borra imagen del sistema de archivos
			Path imagePath = Paths.get("public/images/" + product.getImageFileName());
			try {
				Files.delete(imagePath);
			} catch (Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
			}

			// Borra el producto de la base de datos
			repo.delete(product);

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}

		return "redirect:/admin_producto";
	}

}
