package com.proyectoUltimo.Proyecto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

/**
 * Clase ClientDto que representa un objeto de transferencia de datos para un cliente.
 * Contiene validaciones para los campos email, fullname y role usando anotaciones de javax.validation.
 *
 * @author Juan Manuel Villalba Rincón
 * @version 1.0
 */
public class ClientDto {

    // Email del cliente, obligatorio y con formato válido
    @NotEmpty(message = "El email es requerido")
    @Email
    private String email;

    // Nombre completo del cliente, obligatorio
    @NotEmpty(message = "El nombre completo es requerido")
    private String fullname;

    // Rol asignado al cliente, obligatorio
    @NotEmpty(message = "¿Qué rol tendrá el usuario?")
    private String role;

    // ======= Getters y Setters =======

    /**
     * Obtiene el email del cliente.
     * @return Email del cliente
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece un nuevo email para el cliente.
     * @param email Nuevo email del cliente
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el nombre completo del cliente.
     * @return Nombre completo del cliente
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * Establece un nuevo nombre completo para el cliente.
     * @param fullname Nuevo nombre completo
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * Obtiene el rol del cliente.
     * @return Rol del cliente
     */
    public String getRole() {
        return role;
    }

    /**
     * Establece un nuevo rol para el cliente.
     * @param role Nuevo rol asignado
     */
    public void setRole(String role) {
        this.role = role;
    }
}