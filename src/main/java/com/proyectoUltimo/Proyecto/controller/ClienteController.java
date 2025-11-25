package com.proyectoUltimo.Proyecto.controller;

import com.proyectoUltimo.Proyecto.dto.ClientDto;
import com.proyectoUltimo.Proyecto.model.User;
import com.proyectoUltimo.Proyecto.repository.UserRepository;
import com.proyectoUltimo.Proyecto.service.UsReportService;
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

import javax.sql.DataSource;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador encargado de la gestión de usuarios del sistema por parte del
 * administrador. Permite crear, listar, eliminar, visualizar y generar reportes
 * de usuarios.
 */
@Controller
public class ClienteController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private UsReportService usReportService;

	/**
	 * Muestra la página de administración de usuarios con la lista completa.
	 *
	 * @param model Modelo que lleva los datos a la vista.
	 * @return Nombre de la vista "admin_user".
	 */
	@GetMapping("/admin_user")
	public String pageCliente(Model model) {
		var user = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id")); // Ordena por ID descendente
		model.addAttribute("users", user); // Añade lista de usuarios al modelo
		return "admin_user"; // Devuelve la vista correspondiente
	}

	/**
	 * Muestra el formulario para crear un nuevo usuario.
	 *
	 * @param model Modelo para pasar el DTO vacío.
	 * @return Nombre de la vista "create".
	 */
	@GetMapping("/create")
	public String createClient(Model model) {
		ClientDto clientDto = new ClientDto(); // Crea objeto DTO vacío
		model.addAttribute("clientDto", clientDto); // Lo agrega al modelo para el formulario
		return "create";
	}

	/**
	 * Procesa el formulario de creación de usuario. Valida duplicados y errores
	 * antes de guardar.
	 *
	 * @param clientDto Datos del formulario.
	 * @param result    Resultado de la validación.
	 * @return Redirección o recarga de la vista según validación.
	 */
	@PostMapping("/create")
	public String createClient(@Valid @ModelAttribute ClientDto clientDto, BindingResult result) {
		// Verifica si el email ya está en uso
		if (userRepository.findByEmail(clientDto.getEmail()) != null) {
			result.addError(new FieldError("clientDto", "email", clientDto.getEmail(), false, null, null,
					"El email ya esta usado"));
		}

		// Si hay errores, vuelve a mostrar el formulario
		if (result.hasErrors()) {
			return "/create";
		}

		// Crea un nuevo usuario a partir de los datos del DTO
		User user = new User();
		user.setEmail(clientDto.getEmail());
		user.setFullname(clientDto.getFullname());
		user.setRole(clientDto.getRole());

		userRepository.save(user); // Guarda el nuevo usuario en la base de datos

		return "redirect:/admin_user"; // Redirige a la lista de usuarios
	}

	/**
	 * Elimina un usuario por su ID.
	 *
	 * @param id ID del usuario a eliminar.
	 * @return Redirección a la lista de usuarios.
	 */
	@GetMapping("/admin_user/eliminar/{id}")
	public String eliminarUsuario(@PathVariable("id") Integer id) {
		userRepository.deleteById(id); // Elimina el usuario por ID
		return "redirect:/admin_user"; // Redirige de vuelta a la lista
	}

	/**
	 * Muestra el perfil de un usuario específico.
	 *
	 * @param id    ID del usuario.
	 * @param model Modelo para pasar los datos a la vista.
	 * @return Nombre de la vista "user".
	 */
	@GetMapping("/admin_user/perfil/{id}")
	public String verPerfilUsuario(@PathVariable Integer id, Model model) {
		// Busca el usuario o lanza excepción si no lo encuentra
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
		model.addAttribute("user", user); // Agrega al modelo
		return "user"; // Muestra la vista de perfil
	}

	/**
	 * Genera un reporte PDF con los datos de los usuarios.
	 *
	 * @param response Objeto HttpServletResponse para escribir el PDF.
	 * @throws Exception Si ocurre un error en el proceso del reporte.
	 */
	@GetMapping("/admin_user/reporte")
	public void generarReporte(HttpServletResponse response) throws Exception {
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=\"users.pdf\"");

		JasperReport report = JasperCompileManager
				.compileReport(resourceLoader.getResource("classpath:users.jrxml").getInputStream());

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("REPORT_TITLE", "Reporte de Usuarios");
		parameters.put("IMAGE_PATH", "src/main/resources/logo_avenidarivera.jpeg");

		try (Connection connection = dataSource.getConnection()) {
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, connection);

			OutputStream out = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al generar el reporte: " + e.getMessage()); // Lanza error si ocurre
		}
	}
}