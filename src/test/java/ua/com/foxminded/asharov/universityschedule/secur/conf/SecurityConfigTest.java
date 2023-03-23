package ua.com.foxminded.asharov.universityschedule.secur.conf;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import ua.com.foxminded.asharov.universityschedule.config.Role;

@Configuration
@Primary
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfigTest {

    public SecurityConfigTest() {
        super();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails student = User.builder().username("testStudent").password(passwordEncoder().encode("11111"))
                .roles(Role.VIEWER.name()).build();
        UserDetails staff = User.builder().username("testStaff").password(passwordEncoder().encode("22222"))
                .roles(Role.VIEWER.name(), Role.MODERATOR.name()).build();
        UserDetails admin = User.builder().username(
                "testAdmin").password(passwordEncoder().encode("33333"))
                .roles(Role.VIEWER.name(), Role.MODERATOR.name(), Role.CREATOR.name()).build();
    
        return new InMemoryUserDetailsManager(student, staff, admin);

    
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}
