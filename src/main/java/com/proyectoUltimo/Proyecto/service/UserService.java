package com.proyectoUltimo.Proyecto.service;

import com.proyectoUltimo.Proyecto.dto.UserDto;
import com.proyectoUltimo.Proyecto.model.User;
import com.proyectoUltimo.Proyecto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Interfaz que define el contrato para las operaciones de servicio relacionadas con usuarios.
 * Proporciona los métodos que deben ser implementados por cualquier clase de servicio de usuarios.
 */
public interface UserService {

    /**
     * Registra un nuevo usuario en el sistema basado en los datos proporcionados en el DTO.
     * La implementación concreta debe manejar la validación, procesamiento y persistencia del usuario.
     *
     * @param userDto Objeto de Transferencia de Datos (DTO) que contiene la información
     *               necesaria para crear un nuevo usuario
     * @return La entidad User persistida en la base de datos
     * @throws RuntimeException si ocurre algún error durante el proceso de registro,
     *         como un email ya existente en el sistema
     */
    User save(UserDto userDto);
}

