package shopsphere.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import shopsphere.entity.Product;
import shopsphere.entity.User;
import shopsphere.exception.ProductNotFoundException;
import shopsphere.exception.UserNotFoundException;
import shopsphere.repository.ProductRepository;
import shopsphere.repository.UserRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final UserRepository userRepository;

	public ProductService(ProductRepository productRepository, UserRepository userRepository) {
		this.productRepository = productRepository;
		this.userRepository = userRepository;
	}

	public Product createProduct(Product product) {
	    return productRepository.save(product);
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Product getProductById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
	}

	public Product updateProduct(Long id, Product product) {

	    Product existingProduct = productRepository.findById(id)
	            .orElseThrow(() ->
	                    new ProductNotFoundException("Product not found"));

	    existingProduct.setName(product.getName());
	    existingProduct.setDescription(product.getDescription());
	    existingProduct.setPrice(product.getPrice());
	    existingProduct.setQuantity(product.getQuantity());
	    existingProduct.setCategory(product.getCategory());

	    return productRepository.save(existingProduct);
	}

	public String deleteProduct(Long id) {

		 Product product = productRepository.findById(id)
		            .orElseThrow(() ->
		                    new ProductNotFoundException("Product not found"));

		    productRepository.delete(product);

		    return "Product deleted";
	}

	public List<Product> searchProducts(String name) {
		return productRepository.findByNameContainingIgnoreCase(name);
	}

	public Page<Product> getProducts(Pageable pageable) {
		return productRepository.findAll(pageable);
	}
}
