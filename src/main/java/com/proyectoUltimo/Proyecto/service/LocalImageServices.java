package com.proyectoUltimo.Proyecto.service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class LocalImageServices implements ImageService{

	
	private final String uploadDir = "uploads/images/";

    @Override
    public String save(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("El archivo está vacío");
        }

        // Crear directorio si no existe
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Generar nombre único
        String ext = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID() + ext;

        // Construir ruta final
        Path path = Paths.get(uploadDir + fileName);

        try {
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            throw new RuntimeException("Error guardando la imagen", e);
        }

        // URL expuesta al cliente
        return "/uploads/" + fileName;
    }

	    
	
}