package shopsphere.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import shopsphere.entity.Cart;
import shopsphere.entity.CartItem;
import shopsphere.entity.Product;
import shopsphere.entity.User;
import shopsphere.exception.CartItemNotFoundException;
import shopsphere.exception.CartItemOwnershipException;
import shopsphere.exception.CartNotFoundException;
import shopsphere.exception.InsufficientStockException;
import shopsphere.exception.InvalidQuantityException;
import shopsphere.exception.ProductNotFoundException;
import shopsphere.exception.UserNotFoundException;
import shopsphere.repository.CartItemRepository;
import shopsphere.repository.CartRepository;
import shopsphere.repository.ProductRepository;
import shopsphere.repository.UserRepository;

@Service
public class CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;

	public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
			UserRepository userRepository, ProductRepository productRepository) {

		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
	}

	// create cart for user

	public Cart createCart(Long userId, String email) {

	    User user = userRepository.findById(userId)
	            .orElseThrow(() ->
	                    new UserNotFoundException("User not found"));

	    if (!user.getEmail().equalsIgnoreCase(email)) {
	        throw new RuntimeException(
	                "You cannot create a cart for another user");
	    }

	    return cartRepository.findByUser(user)
	            .orElseGet(() -> {
	                Cart cart = new Cart();
	                cart.setUser(user);
	                return cartRepository.save(cart);
	            });
	}

	// Get user's cart
	public Cart getCart(Long userId, String email) {

		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

		if (!user.getEmail().equalsIgnoreCase(email)) {
			throw new RuntimeException("You cannot access another user's cart");
		}
		return cartRepository.findByUser(user).orElseThrow(() -> new CartNotFoundException("Cart not found"));
	}

	// add product to cart

	public CartItem addToCart(Long userId, Long productId, int quantity, String email) {

		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

		if (!user.getEmail().equalsIgnoreCase(email)) {
			throw new RuntimeException("You cannot access another user's cart");
		}

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product not found"));

		if (quantity <= 0) {
			throw new InvalidQuantityException("Quantity must be greater than zero");
		}

		Cart cart = createCart(userId, email);

		// Check whether product is already in cart
		Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

		if (existingItem.isPresent()) {

			CartItem cartItem = existingItem.get();

			int newQuantity = cartItem.getQuantity() + quantity;
			if (newQuantity > product.getQuantity()) {
				throw new InsufficientStockException("Not enough stock");
			}

			cartItem.setQuantity(newQuantity);

			return cartItemRepository.save(cartItem);
		}

		// check if quantity greater then the product quantity
		if (quantity > product.getQuantity()) {
			throw new InsufficientStockException("Not enough stock");
		}

		// Product is not in cart, create new CartItem
		CartItem cartItem = new CartItem();

		cartItem.setCart(cart);
		cartItem.setProduct(product);
		cartItem.setQuantity(quantity);

		return cartItemRepository.save(cartItem);

	}

	// Get all items in cart
	public List<CartItem> getCartItems(Long userId, String email) {

	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new UserNotFoundException("User not found"));

	    if (!user.getEmail().equalsIgnoreCase(email)) {
	        throw new RuntimeException(
	                "You cannot access another user's cart");
	    }

	    Cart cart = cartRepository.findByUser(user)
	            .orElseThrow(() ->
	                    new CartNotFoundException("Cart not found"));

	    return cartItemRepository.findByCart(cart);
	}

	// Remove
	public void removeFromCart(
	        Long userId,
	        Long cartItemId,
	        String email) {

	    User user = userRepository.findById(userId)
	            .orElseThrow(() ->
	                    new UserNotFoundException("User not found"));

	    if (!user.getEmail().equalsIgnoreCase(email)) {
	        throw new RuntimeException(
	                "You cannot access another user's cart");
	    }

	    Cart cart = cartRepository.findByUser(user)
	            .orElseThrow(() ->
	                    new CartNotFoundException("Cart not found"));

	    CartItem cartItem = cartItemRepository.findById(cartItemId)
	            .orElseThrow(() ->
	                    new CartItemNotFoundException("Cart item not found"));

	    if (!cartItem.getCart().getId().equals(cart.getId())) {
	        throw new CartItemOwnershipException(
	                "Cart item does not belong to this user");
	    }

	    cartItemRepository.delete(cartItem);
	}
	// Update
	public CartItem updateCartItem(
	        Long userId,
	        Long cartItemId,
	        int quantity,
	        String email) {

	    User user = userRepository.findById(userId)
	            .orElseThrow(() ->
	                    new UserNotFoundException("User not found"));

	    if (!user.getEmail().equalsIgnoreCase(email)) {
	        throw new RuntimeException(
	                "You cannot access another user's cart");
	    }

	    Cart cart = cartRepository.findByUser(user)
	            .orElseThrow(() ->
	                    new CartNotFoundException("Cart not found"));

	    CartItem cartItem = cartItemRepository.findById(cartItemId)
	            .orElseThrow(() ->
	                    new CartItemNotFoundException("Cart item not found"));

	    if (!cartItem.getCart().getId().equals(cart.getId())) {
	        throw new CartItemOwnershipException(
	                "Cart item does not belong to this user");
	    }

	    if (quantity <= 0) {
	        throw new InvalidQuantityException(
	                "Quantity must be greater than zero");
	    }

	    Product product = cartItem.getProduct();

	    if (quantity > product.getQuantity()) {
	        throw new InsufficientStockException("Not enough stock");
	    }

	    cartItem.setQuantity(quantity);

	    return cartItemRepository.save(cartItem);
	}

}
