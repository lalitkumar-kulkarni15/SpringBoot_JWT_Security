package com.springboot.jwt.security.auth.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.jwt.security.implementation.JwtAuthenticationRequest;
import com.springboot.jwt.security.implementation.JwtTokenUtil;
import com.springboot.jwt.security.implementation.JwtUser;
import com.springboot.jwt.security.model.JwtAuthenticationResponse;

@RestController
public class AuthenticationRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    //@Autowired
    //private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {

        //authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
    	
    	
    	
    	if(authenticateUSer(authenticationRequest.getUsername(),authenticationRequest.getPassword())) {
    		
    	} else {
    		throw new AuthenticationException("Bad Credentials", null);
    		
    	}

        // Reload password post-security so we can generate the token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    /**
     * Authenticates the user. If something is wrong, an {@link AuthenticationException} will be thrown
     */
   /* private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
    }*/
    
    
    private boolean authenticateUSer(String username,String password) {
    	
    	try(Connection con=DriverManager.getConnection(  
        		"jdbc:mysql://zencon.co.in:3306/zencolv6_JNM","zencolv6_jnm","zencolv6_jnm")){  
    		Class.forName("com.mysql.jdbc.Driver");   
    	 
    		PreparedStatement psstmt=con.prepareStatement("select USERNAME,PASSWORD from user WHERE USERNAME=? AND PASSWORD=?"); 
    		psstmt.setString(1,username);
    		psstmt.setString(2,password);
    		ResultSet rs=psstmt.executeQuery();
    		
    		while(rs.next()) {
    			
    			if(null!=rs.getString(1) && null!=rs.getString(2)) {
    				return true;
    			} else {
    				return false;
    			}
    			
    		}
    		}catch(Exception e){ System.out.println(e);}
		return false;  
    		}  
    	
    }

