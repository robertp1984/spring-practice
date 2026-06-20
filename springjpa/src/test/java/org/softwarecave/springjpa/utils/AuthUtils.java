package org.softwarecave.springjpa.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

public class AuthUtils {

    public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtAuth(String... authorityNames) {
        List<GrantedAuthority> authorities = Arrays.stream(authorityNames)
                .map(authorityName -> new SimpleGrantedAuthority(authorityName))
                .collect(Collectors.toList());

        return jwt().authorities(authorities);
    }
}
