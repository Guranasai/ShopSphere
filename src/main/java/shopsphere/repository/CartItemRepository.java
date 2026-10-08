package shopsphere.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import shopsphere.entity.Cart;
import shopsphere.entity.CartItem;
import shopsphere.entity.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	
	 List<CartItem> findByCart(Cart cart);
	 
	 Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

}
