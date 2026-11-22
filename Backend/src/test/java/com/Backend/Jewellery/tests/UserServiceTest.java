package com.Backend.Jewellery.tests;

import com.Backend.Jewellery.dto.UserRegistrationRequest;
import com.Backend.Jewellery.entity.Role;
import com.Backend.Jewellery.entity.User;
import com.Backend.Jewellery.repository.UserRepository;
import com.Backend.Jewellery.service.EmailService;
import com.Backend.Jewellery.service.OtpService;
import com.Backend.Jewellery.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    //create fake objects
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private OtpService otpService;
    @Mock private EmailService emailService;

    @InjectMocks private UserService userService; // what we test


    @Test
    void registerUser_throwConflict_whenEmailAlreadyExists(){
        UserRegistrationRequest request = new UserRegistrationRequest(
                "Lebo",
                "lebogang@gmail.com",
                "123pass"
        );
        when(userRepository.existsByEmail("lebogang@gmail.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Email already in use");

        verify(userRepository, never()).saveAndFlush(any());
    }

    @Test
    void registerUser_saveUnverifiedCustomer_withNormalizedEmailAndHashedPassword(){

        //Arrange
        UserRegistrationRequest request = new UserRegistrationRequest(
                "Lebogang",
                " Lebogang@gmail.COM",
                "secret123"
        );

        when(passwordEncoder.encode("secret123")).thenReturn("hashed-secret");


        //Act
        userService.registerUser(request);

        //Assert
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).saveAndFlush(captor.capture());

        User saved = captor.getValue();


        assertThat(saved.getName()).isEqualTo("Lebogang");
        assertThat(saved.getEmail()).isEqualTo("lebogang@gmail.com");
        assertThat(saved.getPassword()).isEqualTo("hashed-secret");
        assertThat(saved.getRole()).isEqualTo(Role.ROLE_CUSTOMER);
        assertThat(saved.getVerified()).isFalse();

    }
}
