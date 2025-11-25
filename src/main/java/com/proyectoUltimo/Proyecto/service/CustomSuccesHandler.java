package com.proyectoUltimo.Proyecto.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Manejador personalizado para redireccionar usuarios según su rol después de
 * una autenticación exitosa.
 */
@Service
public class CustomSuccesHandler implements AuthenticationSuccessHandler {

	/**
	 * Método invocado después de una autenticación exitosa que redirige al usuario
	 * a diferentes URLs según el rol que tenga asignado.
	 *
	 * @param request        El objeto HttpServletRequest
	 * @param response       El objeto HttpServletResponse para redireccionar
	 * @param authentication Objeto que contiene los detalles de la autenticación
	 * @throws IOException      Si ocurre un error de entrada/salida durante la
	 *                          redirección
	 * @throws ServletException Si ocurre un error en el servlet
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		// Obtener las autoridades (roles) del usuario autenticado
		var authourities = authentication.getAuthorities();

		// Extraer el primer rol disponible
		var roles = authourities.stream().map(r -> r.getAuthority()).findFirst();

		// Redirigir según el rol del usuario
		if (roles.orElse("").equals("ADMIN")) {
			response.sendRedirect("/admin");

		} else if (roles.orElse("").equals("USER")) {
			response.sendRedirect("/user");
		} else {
			response.sendRedirect("/error");
		}
	}
}