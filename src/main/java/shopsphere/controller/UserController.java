package shopsphere.controller;

import java.util.List;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import shopsphere.dto.LoginRequest;
import shopsphere.entity.User;

import shopsphere.service.UserService;
import shopsphere.dto.LoginResponse;
import org.springframework.security.core.Authentication;
@RestController /*
				 * It tells Spring: This class will handle REST API requests.
				 */
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public User createUser(@Valid @RequestBody User user) {
		return userService.createUser(user);
	}
	// "@Valid is a Jakarta Bean Validation annotation used with request objects to
	// trigger validation based on constraints such as @NotBlank, @Email, @Positive,
	// and @Size. If validation fails, Spring throws
	// MethodArgumentNotValidException, which can be handled using a global
	// exception handler."

	@GetMapping
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/{id}")
	public User getUserById(
	        @PathVariable Long id,
	        Authentication authentication) {

	    String email = authentication.getName();

	    userService.verifyUserOwnership(id, email);

	    return userService.getUserById(id);
	}

	@PutMapping("/{id}")
	public User updateUser(
	        @PathVariable Long id,
	        @Valid @RequestBody User user,
	        Authentication authentication) {

	    String email = authentication.getName();

	    userService.verifyUserOwnership(id, email);

	    return userService.updateUser(id, user);
	}

	@DeleteMapping("/{id}")
	public String deleteUser(
	        @PathVariable Long id,
	        Authentication authentication) {

	    String email = authentication.getName();

	    userService.verifyUserOwnership(id, email);

	    userService.deleteUser(id);

	    return "User deleted";
	}

	@PostMapping("/login")
	public LoginResponse login(@Valid @RequestBody LoginRequest request) {

		return userService.login(request.getEmail(), request.getPassword());
	}

}
