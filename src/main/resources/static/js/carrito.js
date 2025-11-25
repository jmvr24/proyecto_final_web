document.addEventListener('DOMContentLoaded', function() {
    let carrito = JSON.parse(sessionStorage.getItem('carrito')) || [];

    const contadorCarrito = document.getElementById('contador-carrito');
    const listaCarrito = document.getElementById('lista-carrito');
    const carritoVacio = document.getElementById('carrito-vacio');
    const subtotalCarrito = document.getElementById('subtotal-carrito');
    const totalCarrito = document.getElementById('total-carrito');
    const btnPagar = document.getElementById('btn-pagar');
    const botonesAgregar = document.querySelectorAll('.agregar-carrito');

    function guardarCarritoEnSesion() {
        sessionStorage.setItem('carrito', JSON.stringify(carrito));
    }

    function actualizarCarrito() {
        const totalItems = carrito.reduce((total, item) => total + item.cantidad, 0);
        contadorCarrito.textContent = totalItems;

        if (carrito.length === 0) {
            carritoVacio.style.display = 'block';
            listaCarrito.innerHTML = '';
			btnPagar.disabled = true;
        } else {
            carritoVacio.style.display = 'none';
			btnPagar.disabled = false;
        }

        const subtotal = carrito.reduce((total, item) => total + (item.precio * item.cantidad), 0);
        subtotalCarrito.textContent = '$' + subtotal.toFixed(2);

        const total = subtotal + 5000;
        totalCarrito.textContent = '$' + total.toFixed(2);

        guardarCarritoEnSesion();
    }

    function renderizarCarrito() {
        listaCarrito.innerHTML = '';

        carrito.forEach(item => {
            const itemHTML = `
                <div class="card mb-2" data-id="${item.id}">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div class="d-flex">
                                <img src="${item.imagen}" alt="${item.nombre}" class="cart-item-img me-3">
                                <div>
                                    <h6 class="mb-1">${item.nombre}</h6>
                                    <p class="mb-1">$${item.precio.toFixed(2)} c/u</p>
                                    <small class="text-muted">Disponibles: ${item.disponibles}</small>
                                </div>
                            </div>
                            <div class="d-flex align-items-center">
                                <button class="btn btn-sm btn-outline-secondary quantity-control disminuir" data-id="${item.id}">-</button>
                                <input type="number" class="form-control quantity-input mx-1" value="${item.cantidad}" min="1" max="${item.disponibles}" data-id="${item.id}">
                                <button class="btn btn-sm btn-outline-secondary quantity-control aumentar" data-id="${item.id}">+</button>
                                <button class="btn btn-sm btn-outline-danger ms-2 eliminar" data-id="${item.id}">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </div>
                        </div>
                        <div class="text-end mt-2">
                            <strong>Total: $${(item.precio * item.cantidad).toFixed(2)}</strong>
                        </div>
                    </div>
                </div>
            `;
            listaCarrito.insertAdjacentHTML('beforeend', itemHTML);
        });

        document.querySelectorAll('.disminuir').forEach(btn => {
            btn.addEventListener('click', function() {
                const id = this.getAttribute('data-id');
                const item = carrito.find(item => item.id == id);
                if (item.cantidad > 1) {
                    item.cantidad--;
                } else {
                    carrito = carrito.filter(item => item.id != id);
                }
                renderizarCarrito();
                actualizarCarrito();
            });
        });

        document.querySelectorAll('.aumentar').forEach(btn => {
            btn.addEventListener('click', function() {
                const id = this.getAttribute('data-id');
                const item = carrito.find(item => item.id == id);
                if (item.cantidad < item.disponibles) {
                    item.cantidad++;
                } else {
                    alert('No hay suficiente stock disponible');
                }
                renderizarCarrito();
                actualizarCarrito();
            });
        });

        document.querySelectorAll('.eliminar').forEach(btn => {
            btn.addEventListener('click', function() {
                const id = this.getAttribute('data-id');
                carrito = carrito.filter(item => item.id != id);
                renderizarCarrito();
                actualizarCarrito();
            });
        });

        document.querySelectorAll('.quantity-input').forEach(input => {
            input.addEventListener('change', function() {
                const id = this.getAttribute('data-id');
                const cantidad = parseInt(this.value);
                const item = carrito.find(item => item.id == id);
                const maxDisponible = parseInt(this.getAttribute('max'));

                if (cantidad > 0 && cantidad <= maxDisponible) {
                    item.cantidad = cantidad;
                } else if (cantidad > maxDisponible) {
                    item.cantidad = maxDisponible;
                    this.value = maxDisponible;
                    alert('No hay suficiente stock disponible');
                } else {
                    carrito = carrito.filter(item => item.id != id);
                }

                renderizarCarrito();
                actualizarCarrito();
            });
        });
    }

    botonesAgregar.forEach(boton => {
        boton.addEventListener('click', function() {
            const id = this.getAttribute('data-id');
            const nombre = this.getAttribute('data-name');
            const precio = parseFloat(this.getAttribute('data-price'));
            const imagen = this.getAttribute('data-image');
            const disponibles = parseInt(this.getAttribute('data-quantity'));

            const itemExistente = carrito.find(item => item.id == id);

            if (itemExistente) {
                if (itemExistente.cantidad < disponibles) {
                    itemExistente.cantidad++;
                } else {
                    alert('No hay suficiente stock disponible');
                    return;
                }
            } else {
                carrito.push({
                    id,
                    nombre,
                    precio,
                    imagen,
                    disponibles,
                    cantidad: 1
                });
            }

            const offcanvasCart = new bootstrap.Offcanvas(document.getElementById('offcanvasCart'));
            offcanvasCart.show();

            renderizarCarrito();
            actualizarCarrito();
        });
    });

	btnPagar.addEventListener('click', function () {
	    if (carrito.length === 0) {
	        alert('No hay productos en el carrito');
	        return;
	    }

	    guardarCarritoEnSesion();
	    window.location.href = '/pago';
	});


    renderizarCarrito();
    actualizarCarrito();
});