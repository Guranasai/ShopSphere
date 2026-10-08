package shopsphere.service;

import java.util.List;

import org.springframework.stereotype.Service;

import shopsphere.dto.OrderDetailsDTO;
import shopsphere.dto.OrderItemDTO;
import shopsphere.dto.OrderRequest;
import shopsphere.entity.Order;
import shopsphere.entity.User;
import shopsphere.exception.OrderNotFoundException;
import shopsphere.exception.UserNotFoundException;
import shopsphere.repository.OrderRepository;
import shopsphere.repository.UserRepository;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final UserRepository userRepository;

	public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
	}
	
	
	public Order getOrderById(Long orderId, String email) {

	    Order order = orderRepository.findById(orderId)
	            .orElseThrow(() ->
	                    new OrderNotFoundException("Order not found"));

	    if (order.getUser() == null ||
	        !order.getUser().getEmail().equalsIgnoreCase(email)) {

	        throw new RuntimeException(
	                "You cannot access another user's order");
	    }

	    return order;
	}

	public List<Order> getAllOrders() {
	    return orderRepository.findAll();
	}

	public Order createOrder(OrderRequest request,String email) {

		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		
		  if (!user.getEmail().equalsIgnoreCase(email)) {
		        throw new RuntimeException(
		                "You cannot create an order for another user");
		    }

		Order order = new Order();

		order.setUser(user);

		order.setStatus(request.getStatus());
		  order.setTotalAmount(0);

		return orderRepository.save(order);
	}

	public OrderDetailsDTO getOrderDetails(Long orderId, String email) {

	    Order order = orderRepository.findById(orderId)
	            .orElseThrow(() ->
	                    new OrderNotFoundException("Order not found"));

	    if (order.getUser() == null ||
	        !order.getUser().getEmail().equalsIgnoreCase(email)) {

	        throw new RuntimeException(
	                "You cannot access another user's order");
	    }

	    OrderDetailsDTO dto = new OrderDetailsDTO();

	    dto.setOrderId(order.getId());
	    dto.setStatus(order.getStatus());
	    dto.setTotalAmount(order.getTotalAmount());

	    dto.setUserName(order.getUser().getName());

	    List<OrderItemDTO> itemDTOs = order.getOrderItems()
	            .stream()
	            .map(item -> {

	                OrderItemDTO itemDTO = new OrderItemDTO();

	                itemDTO.setProductId(item.getProduct().getId());
	                itemDTO.setProductName(item.getProduct().getName());
	                itemDTO.setPrice(item.getPrice());
	                itemDTO.setQuantity(item.getQuantity());

	                double subtotal =
	                        item.getPrice() * item.getQuantity();

	                itemDTO.setSubtotal(subtotal);

	                return itemDTO;
	            })
	            .toList();

	    dto.setItems(itemDTOs);

	    return dto;
	}
	
	public List<Order> getMyOrders(String email) {

	    return orderRepository.findByUserEmail(email);
	}

}
