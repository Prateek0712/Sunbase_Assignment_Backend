package com.sumbaseassignment.Config;

import com.sumbaseassignment.Security.JwtAuthenticationEntryPoint;
import com.sumbaseassignment.Security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {


    @Autowired
    private JwtAuthenticationEntryPoint point;
    @Autowired
    private JwtAuthenticationFilter filter;

    //filter chain with custom configuration
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())

                /* configuring permission , all logining endpoint without authentication and remaining all
                * starting  from "/home" will required strict authentication*/
                .authorizeRequests().
                requestMatchers("/home/**").authenticated().requestMatchers("/sunbase/portal/api/assignment_auth.jsp").permitAll()
                .anyRequest()
                .authenticated()
                // Configures exception handling, specifying the authentication entry point.
                .and().exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // adding jwt auth filter before username password authentication
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        // Builds and returns the configured security filter chain.
        return http.build();
    }


}
