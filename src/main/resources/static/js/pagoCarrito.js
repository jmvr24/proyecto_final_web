document.addEventListener('DOMContentLoaded', function() {
	// ==================== OBTENER ELEMENTOS DEL DOM ====================
	const carrito = JSON.parse(sessionStorage.getItem('carrito')) || [];
	const listaProductos = document.getElementById('lista-productos');
	const subtotalPagar = document.getElementById('subtotal-pagar');
	const totalPagar = document.getElementById('total-pagar');
	const formularioPago = document.getElementById('formulario-pago');
	
	// Campos del formulario
	const cardNumber = document.getElementById('cardNumber');
	const cardName = document.getElementById('cardName');
	const expiryDate = document.getElementById('expiryDate');
	const cvv = document.getElementById('cvv');
	const postalCode = document.getElementById('postalCode');

	// ==================== FUNCIONES DE VALIDACIÓN ====================
	
	// Validar número de tarjeta (solo números, 13-19 dígitos)
	function validarNumeroTarjeta(numero) {
		const numeroLimpio = numero.replace(/\s/g, '');
		return /^\d{13,19}$/.test(numeroLimpio);
	}

	// Validar formato de fecha MM/AA
	function validarFechaExpiracion(fecha) {
		const regex = /^(0[1-9]|1[0-2])\/\d{2}$/;
		if (!regex.test(fecha)) {
			return false;
		}
		
		// Validar que la fecha no esté vencida
		const partes = fecha.split('/');
		const mes = partes[0];
		const anio = partes[1];
		const anioCompleto = 2000 + parseInt(anio);
		const fechaExpiracion = new Date(anioCompleto, parseInt(mes), 0);
		const hoy = new Date();
		
		return fechaExpiracion > hoy;
	}

	// Validar CVV (3 o 4 dígitos)
	function validarCVV(cvvValue) {
		return /^\d{3,4}$/.test(cvvValue);
	}

	// Validar código postal (solo números, 5-6 dígitos)
	function validarCodigoPostal(codigo) {
		return /^\d{5,6}$/.test(codigo);
	}

	// Validar nombre (solo letras y espacios)
	function validarNombre(nombre) {
		return /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]{3,}$/.test(nombre.trim());
	}

	// Mostrar error en el campo
	function mostrarError(campo, mensaje) {
		// Remover error previo si existe
		const errorPrevio = campo.parentElement.querySelector('.error-mensaje');
		if (errorPrevio) {
			errorPrevio.remove();
		}
		
		// Agregar clase de error
		campo.classList.add('is-invalid');
		
		// Crear mensaje de error
		const errorDiv = document.createElement('div');
		errorDiv.className = 'error-mensaje text-danger small mt-1';
		errorDiv.textContent = mensaje;
		campo.parentElement.appendChild(errorDiv);
	}

	// Limpiar error del campo
	function limpiarError(campo) {
		campo.classList.remove('is-invalid');
		const errorMensaje = campo.parentElement.querySelector('.error-mensaje');
		if (errorMensaje) {
			errorMensaje.remove();
		}
	}

	// ==================== FORMATEO AUTOMÁTICO DE CAMPOS ====================
	
	// Formatear número de tarjeta (agregar espacios cada 4 dígitos)
	cardNumber.addEventListener('input', function(e) {
		let valor = e.target.value.replace(/\s/g, '').replace(/\D/g, '');
		
		// Limitar a 19 dígitos
		if (valor.length > 19) {
			valor = valor.substring(0, 19);
		}
		
		// Agregar espacios cada 4 dígitos
		let formatoConEspacios = '';
		for (let i = 0; i < valor.length; i++) {
			if (i > 0 && i % 4 === 0) {
				formatoConEspacios += ' ';
			}
			formatoConEspacios += valor[i];
		}
		
		e.target.value = formatoConEspacios;
		
		// Validar en tiempo real
		if (valor.length > 0) {
			if (validarNumeroTarjeta(formatoConEspacios)) {
				limpiarError(cardNumber);
			} else if (valor.length >= 13) {
				mostrarError(cardNumber, 'Número de tarjeta inválido');
			}
		} else {
			limpiarError(cardNumber);
		}
	});

	// Formatear fecha de expiración (agregar / automáticamente)
	expiryDate.addEventListener('input', function(e) {
		let valor = e.target.value.replace(/\D/g, '');
		
		if (valor.length >= 2) {
			valor = valor.substring(0, 2) + '/' + valor.substring(2, 4);
		}
		
		e.target.value = valor.substring(0, 5);
		
		// Validar en tiempo real
		if (valor.length === 5) {
			if (validarFechaExpiracion(valor)) {
				limpiarError(expiryDate);
			} else {
				mostrarError(expiryDate, 'Fecha inválida o vencida (MM/AA)');
			}
		} else if (valor.length > 0) {
			limpiarError(expiryDate);
		}
	});

	// Formatear CVV (solo números, máximo 4 dígitos)
	cvv.addEventListener('input', function(e) {
		let valor = e.target.value.replace(/\D/g, '');
		e.target.value = valor.substring(0, 4);
		
		// Validar en tiempo real
		if (valor.length >= 3) {
			if (validarCVV(valor)) {
				limpiarError(cvv);
			} else {
				mostrarError(cvv, 'CVV debe tener 3 o 4 dígitos');
			}
		} else {
			limpiarError(cvv);
		}
	});

	// Validar nombre en la tarjeta (solo letras)
	cardName.addEventListener('input', function(e) {
		let valor = e.target.value;
		
		// Validar en tiempo real
		if (valor.length > 0) {
			if (validarNombre(valor)) {
				limpiarError(cardName);
			} else {
				mostrarError(cardName, 'El nombre solo debe contener letras');
			}
		} else {
			limpiarError(cardName);
		}
	});

	// Formatear código postal (solo números)
	postalCode.addEventListener('input', function(e) {
		let valor = e.target.value.replace(/\D/g, '');
		e.target.value = valor.substring(0, 6);
		
		if (valor.length > 0) {
			if (validarCodigoPostal(valor)) {
				limpiarError(postalCode);
			} else if (valor.length >= 5) {
				mostrarError(postalCode, 'Código postal inválido');
			}
		} else {
			limpiarError(postalCode);
		}
	});

	// ==================== MOSTRAR CARRITO ====================
	
	let subtotal = 0;
	carrito.forEach(function(item) {
		subtotal += item.precio * item.cantidad;
	});
	const total = subtotal + 5000;

	if (carrito.length === 0) {
		listaProductos.innerHTML = '<p class="text-center text-muted">Tu carrito está vacío</p>';
	} else {
		listaProductos.innerHTML = '';
		carrito.forEach(function(item) {
			const subtotalItem = item.precio * item.cantidad;
			const productoHTML = 
				'<div class="list-group-item">' +
					'<div class="d-flex justify-content-between align-items-center">' +
						'<div class="d-flex align-items-center">' +
							'<img src="' + item.imagen + '" alt="' + item.nombre + '" class="product-image">' +
							'<div>' +
								'<h6 class="mb-1">' + item.nombre + '</h6>' +
								'<small class="text-muted">Cantidad: ' + item.cantidad + '</small>' +
							'</div>' +
						'</div>' +
						'<span>$' + subtotalItem.toFixed(2) + '</span>' +
					'</div>' +
				'</div>';
			listaProductos.insertAdjacentHTML('beforeend', productoHTML);
		});
	}

	subtotalPagar.textContent = '$' + subtotal.toFixed(2);
	totalPagar.textContent = '$' + total.toFixed(2);

	// Pasar datos del carrito como hidden
	const carritoDataInput = document.createElement('input');
	carritoDataInput.type = 'hidden';
	carritoDataInput.name = 'carritoData';
	carritoDataInput.value = JSON.stringify(carrito);
	formularioPago.appendChild(carritoDataInput);

	// ==================== VALIDACIÓN AL ENVIAR FORMULARIO ====================
	
	formularioPago.addEventListener('submit', function(event) {
		event.preventDefault();
		
		let hayErrores = false;

		// Validar carrito no vacío
		if (carrito.length === 0) {
			alert('No hay productos en el carrito');
			return false;
		}

		// Validar número de tarjeta
		if (!validarNumeroTarjeta(cardNumber.value)) {
			mostrarError(cardNumber, 'Número de tarjeta inválido (13-19 dígitos)');
			hayErrores = true;
		}

		// Validar nombre en tarjeta
		if (!validarNombre(cardName.value)) {
			mostrarError(cardName, 'El nombre solo debe contener letras');
			hayErrores = true;
		}

		// Validar fecha de expiración
		if (!validarFechaExpiracion(expiryDate.value)) {
			mostrarError(expiryDate, 'Fecha inválida o vencida (formato MM/AA)');
			hayErrores = true;
		}

		// Validar CVV
		if (!validarCVV(cvv.value)) {
			mostrarError(cvv, 'CVV debe tener 3 o 4 dígitos');
			hayErrores = true;
		}

		// Validar código postal
		if (!validarCodigoPostal(postalCode.value)) {
			mostrarError(postalCode, 'Código postal inválido (5-6 dígitos)');
			hayErrores = true;
		}

		// Si hay errores, no enviar el formulario
		if (hayErrores) {
			alert('Por favor, corrige los errores en el formulario');
			return false;
		}

		// Si todo está bien, enviar el formulario
		formularioPago.submit();
	});
});