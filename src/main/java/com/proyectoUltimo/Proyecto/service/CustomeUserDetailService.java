package com.proyectoUltimo.Proyecto.service;

import com.proyectoUltimo.Proyecto.model.User;
import com.proyectoUltimo.Proyecto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio personalizado que implementa UserDetailsService para cargar detalles
 * del usuario desde la base de datos por medio del email.
 */
@Service
public class CustomeUserDetailService implements UserDetailsService {

    /**
     * Repositorio para acceder a los datos del usuario.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Carga los detalles del usuario para autenticación a partir del nombre de usuario (email).
     *
     * @param username El email del usuario que intenta autenticarse.
     * @return Un objeto UserDetails que Spring Security usará para autenticación y autorización.
     * @throws UsernameNotFoundException Si no se encuentra un usuario con el email dado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca en la base de datos un usuario por su email (username)
        User user = userRepository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        return new CustomUserDetail(user);
    }
}

