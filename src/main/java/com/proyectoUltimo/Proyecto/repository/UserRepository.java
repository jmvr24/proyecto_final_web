package com.proyectoUltimo.Proyecto.repository;

import com.proyectoUltimo.Proyecto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la gestión de entidades User en la base de datos.
 * Extiende JpaRepository para heredar operaciones CRUD básicas.
 *
 * @param <"User"> El tipo de entidad que gestiona este repositorio (en este caso, User)
 * @param <"Integer"> El tipo del identificador de la entidad (en este caso, Integer)
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Busca un usuario por su dirección de email.
     *
     * @param email La dirección de email a buscar (no sensible a mayúsculas/minúsculas)
     * @return El usuario encontrado, o null si no existe ningún usuario con ese email
     */
    User findByEmail(String email);
}