package com.proyectoUltimo.Proyecto.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoUltimo.Proyecto.model.*;
import com.proyectoUltimo.Proyecto.repository.AddressRepository;
import com.proyectoUltimo.Proyecto.repository.OrderItemRepository;
import com.proyectoUltimo.Proyecto.repository.OrderRepository;
import com.proyectoUltimo.Proyecto.repository.ProductsRepository;
import com.proyectoUltimo.Proyecto.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * Controlador para gestionar el proceso de pago, confirmación y guardado de pedidos.
 */
@Controller
public class ProductsPagoController {

    @Autowired
    private ProductsRepository productRepository; // Repositorio de productos

    @Autowired
    private UserRepository userRepository; // Repositorio de usuarios

    @Autowired
    private OrderRepository orderRepository; // Repositorio de órdenes (pedidos)

    @Autowired
    private OrderItemRepository orderItemRepository; // Repositorio de ítems de la orden

    @Autowired
    private AddressRepository addressRepository; // Repositorio de direcciones

    /**
     * Muestra el formulario de pago.
     * @param model Modelo para pasar datos a la vista.
     * @param authentication Información del usuario autenticado.
     * @return Vista del formulario de pago.
     */
    @GetMapping("/pago")
    public String showPaymentForm(Model model, Authentication authentication) {
        // Obtener el email del usuario autenticado
        String email = authentication.getName();
        User user = userRepository.findByEmail(email); // Buscar usuario por email

        model.addAttribute("paymentForm", new PaymentForm()); // Agregar formulario vacío
        model.addAttribute("user", user); // Pasar usuario a la vista

        return "pago"; // Retornar vista 'pago.html'
    }

    /**
     * Procesa el pago, guarda la orden, la dirección y limpia el carrito.
     * @param paymentForm Formulario de pago recibido.
     * @param authentication Usuario autenticado.
     * @param session Sesión HTTP (almacena el carrito).
     * @param model Modelo para pasar datos a la vista.
     * @return Redirección a la página de confirmación.
     */
    @PostMapping("/pago")
    public String processPayment(@ModelAttribute PaymentForm paymentForm,
                                 Authentication authentication,
                                 HttpSession session,
                                 Model model) {
        try {
            // 1. Obtener el usuario
            String email = authentication.getName();
            User user = userRepository.findByEmail(email);

            // 2. Obtener el carrito desde el formulario o sesión
            List<Map<String, Object>> carrito = obtenerCarrito(paymentForm, session);

            if (carrito == null || carrito.isEmpty()) {
                model.addAttribute("error", "No hay productos en el carrito");
                return "pago";
            }

            // 3. Crear y guardar la dirección de envío
            Address address = crearDireccion(paymentForm, user);
            addressRepository.save(address);

            // 4. Crear y guardar la orden
            Order order = crearOrden(user, address);
            procesarItemsCarrito(carrito, order); // Asignar productos y calcular total
            Order savedOrder = orderRepository.save(order); // Guardar orden en base de datos

            // 5. Limpiar carrito de sesión
            session.removeAttribute("carrito");

            // 6. Redirigir a página de confirmación con el ID de transacción
            return "redirect:/confirmacion?transactionId=" + savedOrder.getTransactionId();

        } catch (Exception e) {
            e.printStackTrace(); // Imprimir error en consola
            model.addAttribute("error", "Ocurrió un error al procesar el pago: " + e.getMessage());
            return "pago";
        }
    }

    /**
     * Muestra la página de confirmación de la compra.
     * @param transactionId ID de la transacción para buscar la orden.
     * @param model Modelo para mostrar los datos de la orden.
     * @return Vista de confirmación.
     */
    @GetMapping("/confirmacion")
    public String showConfirmation(@RequestParam String transactionId, Model model) {
        try {
            Order order = orderRepository.findByTransactionId(transactionId);

            if (order == null) {
                return "redirect:/producto?error=Orden no encontrada";
            }

            model.addAttribute("order", order);
            return "confirmacion";
        } catch (Exception e) {
            return "redirect:/producto?error=" + e.getMessage();
        }
    }

    /**
     * Obtiene el carrito desde el formulario o la sesión.
     */
    private List<Map<String, Object>> obtenerCarrito(PaymentForm paymentForm, HttpSession session) throws Exception {
        if (paymentForm.getCarritoData() != null && !paymentForm.getCarritoData().isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(paymentForm.getCarritoData(),
                    new TypeReference<List<Map<String, Object>>>(){});
        }
        return (List<Map<String, Object>>) session.getAttribute("carrito");
    }

    /**
     * Crea una dirección de envío con los datos del formulario.
     */
    private Address crearDireccion(PaymentForm paymentForm, User user) {
        Address address = new Address();
        address.setUser(user);
        address.setStreet(paymentForm.getAddress());
        address.setCity(paymentForm.getCity());
        address.setState(paymentForm.getState());
        address.setCountry(paymentForm.getCountry());
        address.setPostalCode(paymentForm.getPostalCode());
        address.setIsDefault(paymentForm.isSaveInfo());
        return address;
    }

    /**
     * Crea una nueva orden asociada al usuario y la dirección.
     */
    private Order crearOrden(User user, Address address) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus("PAGADO");
        order.setShippingAddress(address);
        order.setPaymentMethod("Tarjeta de Crédito");
        order.setTransactionId(UUID.randomUUID().toString()); // ID único de la transacción
        return order;
    }

    /**
     * Procesa los productos del carrito: crea ítems de orden, descuenta del stock y calcula el total.
     */
    private void procesarItemsCarrito(List<Map<String, Object>> carrito, Order order) {
        double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (Map<String, Object> item : carrito) {
            Integer productId = Integer.parseInt(item.get("id").toString());
            Product product = productRepository.findById(productId).orElse(null);

            if (product != null && product.getQuantity() > 0) {
                int cantidadSolicitada = Integer.parseInt(item.get("cantidad").toString());
                int cantidadDisponible = Math.min(cantidadSolicitada, product.getQuantity());

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setQuantity(cantidadDisponible);
                orderItem.setPrice(product.getPrice());

                // Calcular el subtotal
                totalAmount += product.getPrice() * orderItem.getQuantity();
                orderItems.add(orderItem);

                // Actualizar el stock del producto
                product.setQuantity(product.getQuantity() - cantidadDisponible);
                productRepository.save(product);
            }
        }

        // Guardar total y lista de ítems en la orden
        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);
    }
}
