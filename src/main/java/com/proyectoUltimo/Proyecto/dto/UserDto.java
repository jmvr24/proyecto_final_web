package com.proyectoUltimo.Proyecto.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 * Clase UserDto (Data Transfer Object) que representa los datos de un usuario
 * necesarios para operaciones como registro, inicio de sesión o procesamiento en la aplicación.
 * Incluye email, contraseña, rol y nombre completo.
 *
 * @author Juan Manuel Villaba Rincón
 * @version 1.0
 */
public class UserDto {

    // Dirección de correo electrónico del usuario
    private String email;

    // Contraseña del usuario
    private String password;

    // Rol asignado al usuario (por ejemplo, ADMIN o USER)
    private String role;

    // Nombre completo del usuario
    private String fullname;

    /**
     * Constructor para crear un nuevo objeto UserDto con todos los atributos.
     *
     * @param email     Correo electrónico del usuario
     * @param password  Contraseña del usuario
     * @param role      Rol del usuario (ADMIN, USER, etc.)
     * @param fullname  Nombre completo del usuario
     */
    public UserDto(String email, String password, String role, String fullname) {
        super(); // Llama al constructor de la clase padre
        this.email = email;
        this.password = password;
        this.role = role;
        this.fullname = fullname;
    }

    // ======== Métodos Getters y Setters =========

    /**
     * Obtiene el email del usuario.
     * @return Email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece un nuevo email para el usuario.
     * @param email Nuevo email del usuario
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña del usuario.
     * @return Contraseña actual del usuario
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece una nueva contraseña para el usuario.
     * @param password Nueva contraseña del usuario
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el rol del usuario.
     * @return Rol del usuario (por ejemplo, ADMIN, USER)
     */
    public String getRole() {
        return role;
    }

    /**
     * Establece un nuevo rol para el usuario.
     * @param role Nuevo rol del usuario
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Obtiene el nombre completo del usuario.
     * @return Nombre completo
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * Establece un nuevo nombre completo para el usuario.
     * @param fullname Nombre completo nuevo
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
