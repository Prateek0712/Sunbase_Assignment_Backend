package com.sumbaseassignment.Model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class JwtRequest {
    // email use as username for authenticating rqst
    private String email;

    // password for authenting requesting
    private String password;
}
