package com.proyectoUltimo.Proyecto.service;

import com.proyectoUltimo.Proyecto.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Clase CustomUserDetail que implementa la interfaz UserDetails de Spring Security.
 * Proporciona los detalles necesarios para la autenticación y autorización del usuario.
 * Encapsula un objeto User y expone sus propiedades requeridas por Spring Security.
 *
 * @author Juan Manuel Villalba Rincón
 * @version 1.0
 */
public class CustomUserDetail implements UserDetails {

    // Objeto User que contiene la información real del usuario
    private User user;

    /**
     * Constructor que recibe un objeto User para inicializar CustomUserDetail.
     *
     * @param user Objeto User con datos del usuario
     */
    public CustomUserDetail(User user) {
        super(); // Llamada al constructor de la clase padre (Object)
        this.user = user;
    }

    /**
     * Retorna una colección con los roles o autoridades del usuario.
     * En este caso, devuelve una lista con un GrantedAuthority que obtiene el rol desde user.
     *
     * @return Colección de GrantedAuthority para Spring Security
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Usamos List.of con una expresión lambda para crear GrantedAuthority
        return List.of(() -> user.getRole());
    }

    /**
     * Obtiene el nombre completo del usuario.
     *
     * @return Nombre completo almacenado en user
     */
    public String getFullname() {
        return user.getFullname();
    }

    /**
     * Obtiene el email del usuario.
     * @return Email del usuario
     */
    public String getEmail() {
        return user.getEmail();
    }

    /**
     * Retorna la contraseña del usuario.
     * Usado por Spring Security para autenticación.
     *
     * @return Contraseña cifrada o en texto almacenada en user
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Retorna el nombre de usuario para el inicio de sesión.
     * En este caso, se usa el email como username.
     *
     * @return Email del usuario para login
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Indica si la cuenta no está expirada.
     * Siempre devuelve true para no manejar expiración aquí.
     *
     * @return true si la cuenta no está expirada
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta no está bloqueada.
     * Siempre devuelve true para no manejar bloqueo aquí.
     *
     * @return true si la cuenta no está bloqueada
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica si las credenciales (contraseña) no han expirado.
     * Siempre devuelve true para no manejar expiración aquí.
     *
     * @return true si las credenciales no han expirado
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si el usuario está habilitado.
     * Siempre devuelve true para considerar el usuario activo.
     *
     * @return true si el usuario está habilitado
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}