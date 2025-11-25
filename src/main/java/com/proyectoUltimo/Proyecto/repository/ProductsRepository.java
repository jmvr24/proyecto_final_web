package com.proyectoUltimo.Proyecto.repository;

import com.proyectoUltimo.Proyecto.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repositorio para la entidad Product.
 * Proporciona métodos CRUD automáticos para acceder y modificar los productos en la base de datos.
 */
@Repository
public interface ProductsRepository extends JpaRepository<Product, Integer> {

}
