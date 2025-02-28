package com.intelliacademy.orizonroute.librarymanagmentsystem;

import com.intelliacademy.orizonroute.librarymanagmentsystem.model.User;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.enums.AdminRole;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.UserRepository;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole(AdminRole.ADMIN);
    }

    @Test
    void loadUserByUsername_whenUserExists_thenReturnUserDetails() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

        // Assert
        verify(userRepository).findByUsername("testuser");
        assert userDetails.getUsername().equals("testuser");
        assert userDetails.getPassword().equals("password");
        assert userDetails.getAuthorities().size() == 1;
        assert userDetails.getAuthorities().iterator().next().getAuthority().equals("ROLE_ADMIN");
    }

    @Test
    void loadUserByUsername_whenUserDoesNotExist_thenThrowUsernameNotFoundException() {
        // Arrange
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("nonexistentuser"));
        verify(userRepository).findByUsername("nonexistentuser");
    }
}
