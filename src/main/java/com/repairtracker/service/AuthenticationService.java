package com.repairtracker.service;

import com.repairtracker.dto.request.LoginRequest;
import com.repairtracker.dto.request.RegisterRequest;
import com.repairtracker.dto.response.AuthResponse;
import com.repairtracker.dto.response.UserDto;
import com.repairtracker.entity.User;
import com.repairtracker.exception.AuthenticationException;
import com.repairtracker.exception.BusinessException;
import com.repairtracker.repository.UserRepository;
import com.repairtracker.repository.StoreRepository;
import com.repairtracker.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {
    
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username is already taken!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email is already in use!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());

        if (request.getStoreId() != null) {
            user.setStore(storeRepository.findById(request.getStoreId())
                    .orElseThrow(() -> new BusinessException("Store not found")));
        }

        User savedUser = userRepository.save(user);
        UserDto userDto = convertToUserDto(savedUser);

        // 🔥 Generate token directly using username
        String token = jwtTokenProvider.generateToken(savedUser.getUsername());

        return new AuthResponse(token, userDto);
    }


    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            
            User user = (User) authentication.getPrincipal();
            UserDto userDto = convertToUserDto(user);
            
            String token = jwtTokenProvider.generateToken(authentication);
            
            return new AuthResponse(token, userDto);
        } catch (Exception e) {
            throw new AuthenticationException("Invalid username or password");
        }
    }
    
    private UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRole(user.getRole());
        userDto.setStatus(user.getStatus());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());
        
        if (user.getStore() != null) {
            userDto.setStoreId(user.getStore().getId());
            userDto.setStoreName(user.getStore().getName());
        }
        
        return userDto;
    }
}
