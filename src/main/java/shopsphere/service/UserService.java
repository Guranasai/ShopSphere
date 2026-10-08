package shopsphere.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import shopsphere.entity.User;
import shopsphere.exception.UserNotFoundException;
import shopsphere.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import shopsphere.dto.LoginResponse;
@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	
	public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder,AuthenticationManager authenticationManager,JwtService jwtService)
	{
		this.userRepository=userRepository;
		this.passwordEncoder=passwordEncoder;
		this.authenticationManager=authenticationManager;
		this.jwtService=jwtService;
	}
	
	public User createUser(User user) {

	    if (userRepository.existsByEmail(user.getEmail())) {
	        throw new RuntimeException(
	                "Email already registered"
	        );
	    }

	    user.setRole("USER");

	    user.setPassword(
	            passwordEncoder.encode(user.getPassword())
	    );

	    return userRepository.save(user);
	}
	
	public List<User> getAllUsers()
	{
		return userRepository.findAll();
	}
	
	public User getUserById(Long id)
	{
		return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User not found"));
	}
	
	public User updateUser(Long id, User user) {

	    User existingUser = userRepository.findById(id)
	            .orElseThrow(() ->
	                    new UserNotFoundException("User not found"));

	    existingUser.setName(user.getName());
	    existingUser.setEmail(user.getEmail());

	    existingUser.setPassword(
	            passwordEncoder.encode(user.getPassword())
	    );

	    return userRepository.save(existingUser);
	}
	
	public void deleteUser(Long id)
	{
		 userRepository.deleteById(id);
	}
	
	public LoginResponse login(String email, String password) {

	    Authentication authentication =
	            authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        email,
	                        password
	                )
	            );

	    UserDetails userDetails =
	            (UserDetails) authentication.getPrincipal();

	    String token =
	            jwtService.generateToken(userDetails);

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() ->
	                    new UserNotFoundException("User not found"));

	    return new LoginResponse(
	            token,
	            user.getId(),
	            user.getEmail(),
	            user.getRole()
	    );
	}
	
	public void verifyUserOwnership(Long id, String email) {

	    User user = userRepository.findById(id)
	            .orElseThrow(() ->
	                    new UserNotFoundException("User not found"));

	    if (!user.getEmail().equals(email)) {
	        throw new RuntimeException(
	                "You cannot access another user's account"
	        );
	    }
	}

}
