package com.proyectoUltimo.Proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador principal que gestiona la página de inicio del sitio web.
 * Utiliza Spring MVC para devolver la vista "index" con un mensaje de bienvenida.
 */
@Controller
public class HomeController {

    /**
     * Maneja las solicitudes GET al path raíz ("/").
     * Agrega un atributo al modelo con un mensaje de bienvenida y devuelve la vista "index".
     *
     * @return El nombre de la vista "index".
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/sobreNosotros")
    public String sobreNosotrs(){
        return "sobreNosotros";
    }

    @GetMapping("/ubicacion")
    public String ubicacion(){
        return "ubicacion";
    }

}
