package com.proyectoUltimo.Proyecto.configuration;

import com.proyectoUltimo.Proyecto.service.CustomSuccesHandler;
import com.proyectoUltimo.Proyecto.service.CustomUserDetail;
import com.proyectoUltimo.Proyecto.service.CustomeUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Clase de configuración de seguridad para la aplicación Spring. Define cómo se
 * manejan la autenticación y autorización.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/**
	 * Inyección automática del manejador personalizado que se ejecuta cuando un
	 * usuario inicia sesión exitosamente.
	 */
	@Autowired
	CustomSuccesHandler customSuccesHandler;

	/**
	 * Inyección automática del servicio personalizado para cargar los detalles del
	 * usuario (usuario, contraseña, roles).
	 */
	@Autowired
	CustomeUserDetailService customeUserDetailService;

	/**
	 * Bean que define el codificador de contraseñas a usar en la aplicación. Aquí
	 * se usa BCrypt para encriptar las contraseñas.
	 *
	 * @return PasswordEncoder instancia de BCryptPasswordEncoder.
	 */
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Define la cadena de filtros de seguridad HTTP, configurando reglas de acceso,
	 * login y logout.
	 *
	 * @param http objeto para configurar seguridad HTTP.
	 * @return SecurityFilterChain cadena de filtros configurada.
	 * @throws Exception si ocurre un error en la configuración.
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(c -> c.disable())
		
				.authorizeHttpRequests(request -> request
						.requestMatchers("/admin").hasAuthority("ADMIN")
						
						.requestMatchers("/user").hasAuthority("USER")
						
						.requestMatchers("/pago").authenticated()
						
						.requestMatchers("/", "/producto", "/sobreNosotros", "/ubicacion", "/registration", "/login",
								"/css/*", "/images/**", "/uploads/**")
						.permitAll()

						.anyRequest().authenticated())

				.formLogin(form -> form
						.loginPage("/login")
						.loginProcessingUrl("/login")
						.successHandler(customSuccesHandler)
						.permitAll())

				
				.logout(form -> form
						.invalidateHttpSession(true)
						.clearAuthentication(true)
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/?logout=true")
						.permitAll());
		return http.build();
	}

	/**
	 * Configura el AuthenticationManagerBuilder para usar el servicio personalizado
	 * de detalles de usuario y el codificador de contraseñas definido.
	 *
	 * @param auth AuthenticationManagerBuilder para configurar autenticación.
	 * @throws Exception si ocurre un error en la configuración.
	 */
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Define el servicio que carga usuarios y cómo verificar contraseñas
		auth.userDetailsService(customeUserDetailService).passwordEncoder(passwordEncoder());
	}
}