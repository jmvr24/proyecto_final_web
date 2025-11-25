package com.proyectoUltimo.Proyecto.repository;

import com.proyectoUltimo.Proyecto.model.Address;
import com.proyectoUltimo.Proyecto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Address.
 * Proporciona operaciones CRUD básicas sobre direcciones almacenadas en la base de datos.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    /**
     * Método para obtener todas las direcciones de un usuario específico.
     * Spring Data JPA generará automáticamente la consulta SQL.
     *
     * @param user El usuario al que pertenecen las direcciones.
     * @return Lista de direcciones asociadas al usuario.
     */
    // List<Address> findByUser(User user);

    /**
     * Método para obtener la dirección por defecto de un usuario.
     * Retorna un Optional porque puede que no exista una dirección marcada como por defecto.
     *
     * @param user Usuario al que pertenece la dirección.
     * @param isDefault Booleano que indica si la dirección es la predeterminada.
     * @return Optional con la dirección por defecto si existe.
     */
    // Optional<Address> findByUserAndIsDefault(User user, Boolean isDefault);
}
