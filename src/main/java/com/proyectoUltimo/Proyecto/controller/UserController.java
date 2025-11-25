package com.proyectoUltimo.Proyecto.controller;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.proyectoUltimo.Proyecto.dto.ProductDto;
import com.proyectoUltimo.Proyecto.dto.UserDto;
import com.proyectoUltimo.Proyecto.model.Product;
import com.proyectoUltimo.Proyecto.model.User;
import com.proyectoUltimo.Proyecto.repository.ProductsRepository;
import com.proyectoUltimo.Proyecto.repository.UserRepository;
import com.proyectoUltimo.Proyecto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

/**
 * Controlador encargado de manejar las peticiones relacionadas con los usuarios,
 * como el registro, login y acceso a las páginas del usuario y del administrador.
 */
@Controller
public class UserController {

    // Inyección de dependencia del servicio que proporciona detalles del usuario
    @Autowired
    private UserDetailsService userDetailsService;

    // Inyección de dependencia del servicio personalizado de usuarios
    @Autowired
    private UserService userService;

    /**
     * Muestra la página de registro.
     *
     * @param userDto Objeto que representa los datos del usuario que se está registrando.
     * @return Nombre de la vista HTML del formulario de registro.
     */
    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("user") UserDto userDto) {
        return "register";
    }

    /**
     * Guarda los datos del usuario enviado desde el formulario de registro.
     *
     * @param userDto Objeto con los datos ingresados por el usuario.
     * @param model Objeto para enviar mensajes a la vista.
     * @return La vista del formulario de registro con un mensaje.
     */
    @PostMapping("/registration")
    public String saveUsers(@ModelAttribute("user") UserDto userDto, Model model) {
        // Llama al servicio para guardar el usuario (en la base de datos)
        userService.save(userDto);

        // Agrega un mensaje al modelo que se mostrará en la vista
        model.addAttribute("message", "¡Registro exitoso, ahora puedes iniciar sesión y hacer tu pedido!");
        return "index";
    }

    /**
     * Muestra la página de inicio de sesión (login).
     *
     * @return Nombre de la vista del login.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Muestra la página para usuarios autenticados con rol USER.
     *
     * @param model Objeto para pasar datos a la vista.
     * @param principal Objeto que contiene información del usuario autenticado.
     * @return Vista "user.html" con los datos del usuario.
     */
    @GetMapping("/user")
    public String userPage(Model model, Principal principal) {
        // Obtiene el nombre del usuario autenticado desde el principal
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());

        // Agrega el objeto user al modelo para mostrarlo en la vista
        model.addAttribute("user", userDetails);

        // Devuelve la vista user.html
        return "user";
    }

    /**
     * Muestra la página para usuarios con rol ADMIN.
     *
     * @param model Objeto para pasar datos a la vista.
     * @param principal Objeto con información del usuario autenticado.
     * @return Vista "admin.html" con los datos del usuario.
     */
    @GetMapping("/admin")
    public String adminPage(Model model, Principal principal) {
        // Obtiene los detalles del usuario usando su nombre de usuario
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());

        // Agrega el usuario autenticado al modelo
        model.addAttribute("user", userDetails);

        // Devuelve la vista admin.html
        return "admin";
    }
}