package shopsphere.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import shopsphere.dto.OrderItemRequest;
import shopsphere.entity.OrderItem;
import shopsphere.service.OrderItemService;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

	private final OrderItemService orderItemService;

	public OrderItemController(OrderItemService orderItemService) {
		this.orderItemService = orderItemService;
	}

	@PostMapping
	public OrderItem addOrderItem(
	        @Valid @RequestBody OrderItemRequest request,
	        Authentication authentication) {

	    String email = authentication.getName();

	    return orderItemService.addOrderItem(
	            request.getOrderId(),
	            request.getProductId(),
	            request.getQuantity(),
	            email
	    );
	}
	
	@GetMapping
	public List<OrderItem> getAllOrderItems() {

	    return orderItemService.getAllOrderItems();
	}
	
	@GetMapping("/order/{id}")
	public List<OrderItem> getOrderItemsByOrderId(
	        @PathVariable Long id,
	        Authentication authentication) {

	    String email = authentication.getName();

	    return orderItemService.getOrderItemsByOrderId(id, email);
	}

}
