package com.sumbaseassignment.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.client.RestTemplate;

@Configuration
class MyConfig {

    //defined bean of user details
    @Bean
    public UserDetailsService userDetailsService() {
        // this is for login and it will be only  one admin
        //creting user with role admin
        UserDetails userDetails = User.builder().
                username("test@sunbasedata.com")
                .password(passwordEncoder().encode("Test@123")).roles("ADMIN").
                build();

        return new InMemoryUserDetailsManager(userDetails);
    }


    // bean for password encoding (in this case it will be Bcryptpassword encoding).
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // bean for providing the authentication manager.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
