package com.proyectoUltimo.Proyecto.service;

import com.proyectoUltimo.Proyecto.dto.UserDto;
import com.proyectoUltimo.Proyecto.model.User;
import com.proyectoUltimo.Proyecto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * Implementación concreta del servicio de usuarios que maneja la lógica de operaciones
 * relacionadas con la gestión de usuarios en el sistema.
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * Codificador de contraseñas utilizado para encriptar las credenciales de los usuarios.
     * Se inyecta automáticamente por Spring.
     */
    @Autowired
    private PasswordEncoder passwordEncoderasswordEcoder;

    /**
     * Repositorio para realizar operaciones CRUD de usuarios en la base de datos.
     * Se inyecta automáticamente por Spring.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Registra un nuevo usuario en el sistema después de realizar validaciones.
     * Asigna automáticamente el rol "ADMIN" si el email sigue el patrón adminXX@gmail.com,
     * de lo contrario asigna el rol "USER".
     *
     * @param userDto Objeto de transferencia de datos con la información del usuario a registrar
     * @return Entidad User guardada en la base de datos
     * @throws RuntimeException si el correo electrónico ya está registrado en el sistema
     */
    @Override
    public User save(UserDto userDto) {
        // Verificar si ya existe un usuario con ese email
        User existingUser = userRepository.findByEmail(userDto.getEmail());

        // Si el usuario ya existe, lanzar una excepción
        if (existingUser != null) {
            throw new RuntimeException("El correo electrónico ya está registrado");
        }

        // Convertir email a minúsculas para consistencia
        String email = userDto.getEmail().toLowerCase(); // pasar a minúsculas para evitar problemas

        // Variable para almacenar el rol del usuario
        String role;

        // Lógica para asignar rol ADMIN si cumple el patrón adminXX@gmail.com
        // De lo contrario, asignar rol USER por defecto
        if (email.startsWith("admin") && email.endsWith("@gmail.com")) {
            role = "ADMIN";
        } else {
            role = "USER";
        }

        // Crear nuevo objeto User con los datos proporcionados
        // La contraseña se codifica antes de almacenarla
        User user = new User(
                userDto.getEmail(),
                passwordEncoderasswordEcoder.encode(userDto.getPassword()),
                role,
                userDto.getFullname()
        );

        // Guardar el usuario en la base de datos y devolver el resultado
        return userRepository.save(user);
    }
}