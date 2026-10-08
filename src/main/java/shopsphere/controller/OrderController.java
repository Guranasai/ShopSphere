package shopsphere.controller;

import java.util.List;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import shopsphere.dto.OrderDetailsDTO;
import shopsphere.dto.OrderRequest;
import shopsphere.entity.Order;
import shopsphere.service.OrderService;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public Order createOrder(@Valid@RequestBody OrderRequest request, Authentication authentication) {
		String email = authentication.getName();
		return orderService.createOrder(request,email);
	}

	@GetMapping
	public List<Order> getAllOrders() {
	    return orderService.getAllOrders();
	}

	@GetMapping("/{orderId}")
	public OrderDetailsDTO getOrderDetails(@PathVariable("orderId") Long orderId,Authentication authentication) {
      
		String email = authentication.getName();
		return orderService.getOrderDetails(orderId,email);
	}
	
	@GetMapping("/{orderId}/basic")
	public Order getOrderById(
	        @PathVariable Long orderId,
	        Authentication authentication) {

	    String email = authentication.getName();

	    return orderService.getOrderById(orderId, email);
	}
	
	@GetMapping("/my-orders")
	public List<Order> getMyOrders(
	        Authentication authentication) {

	    String email = authentication.getName();

	    return orderService.getMyOrders(email);
	}

}
