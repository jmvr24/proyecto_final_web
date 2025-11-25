package com.proyectoUltimo.Proyecto.controller;

import com.proyectoUltimo.Proyecto.dto.ClientDto;
import com.proyectoUltimo.Proyecto.model.User;
import com.proyectoUltimo.Proyecto.repository.UserRepository;
import com.proyectoUltimo.Proyecto.service.UsReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para gestión de usuarios por parte del administrador.
 * Proporciona APIs para CRUD de usuarios y generación de reportes.
 */
@RestController
@RequestMapping("/api/admin_user")
@Tag(name = "Administración de Usuarios", description = "API para crear, listar, eliminar, ver y reportar usuarios")
public class ClientApiContrller {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UsReportService usReportService;

    /**
     * Obtiene la lista de todos los usuarios ordenados por ID descendente.
     *
     * @return Lista de usuarios.
     */
    @Operation(summary = "Listar usuarios",
            description = "Devuelve la lista completa de usuarios ordenados por ID descendente")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class)))
    @GetMapping
    public Iterable<User> listarUsuarios() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    /**
     * Obtiene un usuario específico por su ID.
     *
     * @param id ID del usuario.
     * @return Usuario encontrado.
     */
    @Operation(summary = "Ver perfil de usuario",
            description = "Obtiene los datos de un usuario específico por su ID")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class)))
    @ApiResponse(responseCode = "400", description = "ID inválido")
    @GetMapping("/{id}")
    public User verPerfilUsuario(@PathVariable Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param clientDto DTO con datos del usuario a crear.
     * @return Usuario creado.
     */
    @Operation(summary = "Crear nuevo usuario",
            description = "Crea un usuario nuevo, validando que el email no esté duplicado")
    @ApiResponse(responseCode = "201", description = "Usuario creado correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos o email duplicado",
            content = @Content)
    @PostMapping
    public User crearUsuario(@Valid @RequestBody ClientDto clientDto) {
        if (userRepository.findByEmail(clientDto.getEmail()) != null) {
            throw new IllegalArgumentException("El email ya está usado");
        }

        User user = new User();
        user.setEmail(clientDto.getEmail());
        user.setFullname(clientDto.getFullname());
        user.setRole(clientDto.getRole());

        return userRepository.save(user);
    }

    /**
     * Elimina un usuario por ID.
     *
     * @param id ID del usuario a eliminar.
     */
    @Operation(summary = "Eliminar usuario",
            description = "Elimina un usuario dado su ID")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente")
    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Integer id) {
        userRepository.deleteById(id);
    }

    /**
     * Genera un reporte PDF con todos los usuarios.
     *
     * @param response HttpServletResponse para escribir el PDF.
     * @throws Exception si ocurre un error al generar el reporte.
     */
    @Operation(summary = "Generar reporte PDF de usuarios",
            description = "Genera y descarga un reporte en PDF con la información de todos los usuarios")
    @ApiResponse(responseCode = "200", description = "Reporte PDF generado correctamente",
            content = @Content(mediaType = "application/pdf"))
    @GetMapping("/reporte")
    public void generarReporte(HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"users.pdf\"");

        JasperReport report = JasperCompileManager.compileReport(
                resourceLoader.getResource("classpath:users.jrxml").getInputStream()
        );

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", "Reporte de Usuarios");
        parameters.put("IMAGE_PATH", "src/main/resources/leaf_banner_green.png");

        try (Connection connection = dataSource.getConnection()) {
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, connection);

            try (OutputStream out = response.getOutputStream()) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al generar el reporte: " + e.getMessage());
        }
    }
}
