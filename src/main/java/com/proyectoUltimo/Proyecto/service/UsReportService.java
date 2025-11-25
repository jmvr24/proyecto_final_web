package com.proyectoUltimo.Proyecto.service;

import com.proyectoUltimo.Proyecto.model.User;
import com.proyectoUltimo.Proyecto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsReportService {

    private final UserRepository userRepository;


    public UsReportService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Obtiene todos los usuarios ordenados por ID descendente
     * @return Lista de usuarios
     */
    public List<User> listarUsuarios() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    /**
     * Guarda un nuevo usuario en la base de datos
     * @param user El usuario a guardar
     * @return El usuario guardado
     */
    public User guardarUsuario(User user) {
        return userRepository.save(user);
    }

    /**
     * Obtiene un usuario por su ID
     * @param id El ID del usuario
     * @return El usuario encontrado o null si no existe
     */
    public User obtenerUsuario(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Elimina un usuario por su ID
     * @param id El ID del usuario a eliminar
     */
    public void eliminarUsuario(Integer id) {
        userRepository.deleteById(id);
    }

    /**
     * Busca usuarios por email
     * @param email El email a buscar
     * @return El usuario encontrado o null si no existe
     */
    public User buscarPorEmail(String email) {
        return userRepository.findByEmail(email);
    }
}