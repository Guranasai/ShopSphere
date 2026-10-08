package shopsphere.service;

import java.util.List;

import org.springframework.stereotype.Service;

import shopsphere.entity.Order;
import shopsphere.entity.OrderItem;
import shopsphere.entity.Product;
import shopsphere.entity.User;
import shopsphere.exception.InsufficientStockException;
import shopsphere.exception.InvalidQuantityException;
import shopsphere.exception.OrderNotFoundException;
import shopsphere.exception.ProductNotFoundException;
import shopsphere.exception.UserNotFoundException;
import shopsphere.repository.OrderItemRepository;
import shopsphere.repository.OrderRepository;
import shopsphere.repository.ProductRepository;
import shopsphere.repository.UserRepository;

@Service
public class OrderItemService {

	private OrderItemRepository orderItemRepository;
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;

	public OrderItemService(OrderItemRepository orderItemRepository, OrderRepository orderRepository,
			ProductRepository productRepository,UserRepository userRepository) {
		this.orderItemRepository = orderItemRepository;
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.userRepository=userRepository;
	}

	public OrderItem addOrderItem(Long orderId, Long productId, int quantity, String email) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order not found"));

		if (order.getUser() == null || !order.getUser().getEmail().equalsIgnoreCase(email)) {

			throw new RuntimeException("You cannot add items to another user's order");
		}

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product not found"));

		if (quantity <= 0) {
			throw new InvalidQuantityException("Quantity must be greater than zero");
		}

		if (product.getQuantity() < quantity) {
			throw new InsufficientStockException("Not enough stock");
		}

		OrderItem orderItem = new OrderItem();

		orderItem.setOrder(order);
		orderItem.setProduct(product);
		orderItem.setQuantity(quantity);
		orderItem.setPrice(product.getPrice());

		double subtotal = product.getPrice() * quantity;

		double newTotal = order.getTotalAmount() + subtotal;

		order.setTotalAmount(newTotal);

		product.setQuantity(product.getQuantity() - quantity);

		productRepository.save(product);
		orderRepository.save(order);

		return orderItemRepository.save(orderItem);
	}

	public List<OrderItem> getAllOrderItems() {
	    return orderItemRepository.findAll();
	}

	public List<OrderItem> getOrderItemsByOrderId(
	        Long id,
	        String email) {

	    Order order = orderRepository.findById(id)
	            .orElseThrow(() ->
	                    new OrderNotFoundException("Order not found"));

	    if (order.getUser() == null ||
	        !order.getUser().getEmail().equalsIgnoreCase(email)) {

	        throw new RuntimeException(
	                "You cannot access another user's order items");
	    }

	    return orderItemRepository.findByOrder(order);
	}

}
