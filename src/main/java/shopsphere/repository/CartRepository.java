package shopsphere.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import shopsphere.entity.Cart;
import shopsphere.entity.User;

public interface CartRepository extends JpaRepository<Cart, Long>{
	
	//Spring Data JPA generates a query to find the cart belonging to that user.
	Optional<Cart> findByUser(User user);

}
