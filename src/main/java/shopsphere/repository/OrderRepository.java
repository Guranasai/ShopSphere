package shopsphere.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import shopsphere.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	
	List<Order> findByUserEmail(String email);
}
