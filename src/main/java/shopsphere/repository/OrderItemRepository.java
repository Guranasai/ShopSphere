package shopsphere.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import shopsphere.entity.Order;
import shopsphere.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
	
//	"JpaRepository provides built-in CRUD and JPA-related operations, so we don't need to write SQL queries manually for basic database operations."

	List<OrderItem> findByOrder(Order order);
}
