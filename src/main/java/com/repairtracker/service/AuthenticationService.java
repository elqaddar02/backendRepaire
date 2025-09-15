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
import com.repairtracker.entity.Store;
import com.repairtracker.enums.UserRole;
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
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email is already in use!");
        }

        // Username may be omitted by FE; generate from email before validation
        String username = request.getUsername() != null && !request.getUsername().isBlank()
                ? request.getUsername().trim()
                : request.getEmail().substring(0, request.getEmail().indexOf('@'));

        if (userRepository.existsByUsername(username)) {
            // Add suffix if taken
            int suffix = 1;
            String base = username;
            while (userRepository.existsByUsername(username)) {
                username = base + suffix++;
            }
        }

        // Map FE role to backend enum
        String roleStr = request.getRole();
        UserRole role = "store".equalsIgnoreCase(roleStr) ? UserRole.MANAGER : UserRole.CUSTOMER;

        User user = new User();
        user.setUsername(username);
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        if (role == UserRole.CUSTOMER) {
            // Split fullName if present
            String fullName = request.getFullName() != null ? request.getFullName().trim() : "";
            if (!fullName.isEmpty()) {
                String[] parts = fullName.split(" ", 2);
                user.setFirstName(parts[0]);
                if (parts.length > 1) user.setLastName(parts[1]);
            }
            user.setPhoneNumber(request.getPhone());
        } else {
            // Create store then attach user as manager
            if (request.getShopName() == null || request.getCity() == null || request.getAddress() == null) {
                throw new BusinessException("Store registration requires shopName, city, and address");
            }
            Store store = new Store();
            store.setName(request.getShopName());
            store.setAddress(request.getAddress() + ", " + request.getCity());
            store.setEmail(request.getEmail());
            store.setManagerName(username);
            Store savedStore = storeRepository.save(store);
            user.setStore(savedStore);
        }

        User savedUser = userRepository.save(user);
        UserDto userDto = convertToUserDto(savedUser);

        String token = jwtTokenProvider.generateToken(savedUser.getUsername());
        return new AuthResponse(token, userDto);
    }


    public AuthResponse login(LoginRequest request) {
        try {
            String principal = request.getUsername();
            if ((principal == null || principal.isBlank()) && request.getEmail() != null) {
                // resolve username by email
                principal = userRepository.findByEmail(request.getEmail())
                        .map(User::getUsername)
                        .orElseThrow(() -> new AuthenticationException("Invalid email or password"));
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(principal, request.getPassword()));
            
            User user = (User) authentication.getPrincipal();
            UserDto userDto = convertToUserDto(user);
            
            String token = jwtTokenProvider.generateToken(authentication);
            
            return new AuthResponse(token, userDto);
        } catch (Exception e) {
            throw new AuthenticationException("Invalid email or password");
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
