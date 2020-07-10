package com.ilkengin.core.gateway.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.ilkengin.core.gateway.provider.JwtTokenProvider;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtTokenFilter extends GenericFilterBean {
	private static final String BEARER = "Bearer";

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {
		var headerValue = ((HttpServletRequest) req).getHeader("Authorization");
		
		var optToken = getBearerToken(headerValue);
		
		if (optToken.isPresent()) {
			var token = optToken.get();
			String username = null;
			try {
				username = jwtTokenProvider.getUsernameFromToken(token);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}
			
			if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {				
				var userDetails = userDetailsService.loadUserByUsername(username);
				if (jwtTokenProvider.isTokenValid(token, userDetails)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) req));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
		}
		filterChain.doFilter(req, res);
	}

	private Optional<String> getBearerToken(String headerVal) {
		if (headerVal != null && headerVal.startsWith(BEARER)) {
			return Optional.of(headerVal.replace(BEARER, "").trim());
		}
		return Optional.empty();
	}
}