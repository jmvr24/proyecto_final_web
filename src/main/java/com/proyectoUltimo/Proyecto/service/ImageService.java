package com.proyectoUltimo.Proyecto.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

	 /**
    * Guarda un archivo de imagen y retorna la URL pública.
    */
   String save(MultipartFile file);

}
