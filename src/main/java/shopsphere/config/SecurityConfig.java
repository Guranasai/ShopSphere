package shopsphere.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    // Password encryption
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    // Used during login authentication
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http

            // REST API - disable CSRF
            .csrf(csrf -> csrf.disable())

            // JWT authentication is stateless
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                )
            )
            
            .exceptionHandling(exception -> exception

            	    // No JWT / user is not authenticated
            	    .authenticationEntryPoint((request, response, authException) -> {

            	        response.setStatus(401);
            	        response.setContentType("application/json");

            	        response.getWriter().write(
            	            "{\"status\":401,\"message\":\"Authentication required\"}"
            	        );
            	    })

            	    // JWT is valid, but user does not have required role
            	    .accessDeniedHandler((request, response, accessDeniedException) -> {

            	        response.setStatus(403);
            	        response.setContentType("application/json");

            	        response.getWriter().write(
            	            "{\"status\":403,\"message\":\"Access denied\"}"
            	        );
            	    })
            	)
            

            .authorizeHttpRequests(auth -> auth
            		.requestMatchers(
            		        "/",
            		        "/index.html",
            		        "/login.html",
            		        "/products.html",
            		        "/cart.html",
            		        "/checkout.html",
            		        "/orders.html",
            		        "/order-details.html",
            		        "/register.html",
            		        "/admin.html",
            		        "/css/**",
            		        "/js/**"
            		)
            		.permitAll()
                // -------------------------
                // PUBLIC USER APIs
                // -------------------------

                // Registration
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/users"
                )
                .permitAll()

                // Login
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/users/login"
                )
                .permitAll()


                // -------------------------
                // PUBLIC PRODUCT APIs
                // -------------------------

                // Anyone can view/search products
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/products/**"
                )
                .permitAll()


                // -------------------------
                // ADMIN PRODUCT APIs
                // -------------------------

                // Create product
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/products/**"
                )
                .hasRole("ADMIN")

                // Update product
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/products/**"
                )
                .hasRole("ADMIN")

                // Delete product
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/products/**"
                )
                .hasRole("ADMIN")


                // -------------------------
                // ADMIN ORDER APIs
                // -------------------------

                // View all orders
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/orders"
                )
                .hasRole("ADMIN")

                // View all order items
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/order-items"
                )
                .hasRole("ADMIN")
                
             // Only ADMIN can view all users
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/users"
                )
                .hasRole("ADMIN")


                // -------------------------
                // ALL OTHER APIs
                // -------------------------

                // Cart, user's orders, adding order items, etc.
                .anyRequest()
                .authenticated()
            )

            // Check JWT before normal Spring authentication
            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
