package shopsphere.controller;

import java.util.List;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import shopsphere.entity.Cart;
import shopsphere.entity.CartItem;
import shopsphere.service.CartService;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	 private final CartService cartService;

	    public CartController(CartService cartService) {
	        this.cartService = cartService;
	    }
	    
	    @PostMapping("/{userId}")
	    public Cart createCart(
	            @PathVariable Long userId,
	            Authentication authentication) {
	    	  String email = authentication.getName();

	        return cartService.createCart(userId, email);
	    }
	    @GetMapping("/{userId}")
	    public Cart getCart(
	            @PathVariable Long userId,
	            Authentication authentication) {

	        String email = authentication.getName();

	        return cartService.getCart(userId, email);
	    }

	    @PostMapping("/{userId}/add/{productId}/{quantity}")
	    public CartItem addToCart(
	            @PathVariable Long userId,
	            @PathVariable Long productId,
	            @PathVariable int quantity,
	            Authentication authentication) {

	        String email = authentication.getName();

	        return cartService.addToCart(
	                userId, productId, quantity, email);
	    }

	    @GetMapping("/{userId}/items")
	    public List<CartItem> getCartItems(
	            @PathVariable Long userId,
	            Authentication authentication) {

	        String email = authentication.getName();

	        return cartService.getCartItems(userId, email);
	    }
	    
	    @DeleteMapping("/{userId}/remove/{cartItemId}")
	    public String removeFromCart(
	            @PathVariable Long userId,
	            @PathVariable Long cartItemId,
	            Authentication authentication) {

	        String email = authentication.getName();

	        cartService.removeFromCart(userId, cartItemId, email);

	        return "Cart Item removed";
	    }
	    
	    @PutMapping("/{userId}/update/{cartItemId}/{quantity}")
	    public CartItem updateCartItem(
	            @PathVariable Long userId,
	            @PathVariable Long cartItemId,
	            @PathVariable int quantity,
	            Authentication authentication) {

	        String email = authentication.getName();

	        return cartService.updateCartItem(
	                userId,
	                cartItemId,
	                quantity,
	                email);
	    }

}
