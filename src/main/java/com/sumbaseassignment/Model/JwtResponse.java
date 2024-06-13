package com.sumbaseassignment.Model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class JwtResponse {

    // this token will be generated after successful authentication rqst of incoming user and will send to client-side
    private String jwtToken;

    // user name for which this token is generated
    private String userName;
}
