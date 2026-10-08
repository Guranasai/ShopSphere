package shopsphere.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderItemRequest {
	
	 @NotNull(message = "Order ID is required")
	    private Long orderId;

	    @NotNull(message = "Product ID is required")
	    private Long productId;

	    @Positive(message = "Quantity must be greater than zero")
	    private int quantity;

	    public OrderItemRequest() {
	    }

	    public Long getOrderId() {
	        return orderId;
	    }

	    public void setOrderId(Long orderId) {
	        this.orderId = orderId;
	    }

	    public Long getProductId() {
	        return productId;
	    }

	    public void setProductId(Long productId) {
	        this.productId = productId;
	    }

	    public int getQuantity() {
	        return quantity;
	    }

	    public void setQuantity(int quantity) {
	        this.quantity = quantity;
	    }

}
