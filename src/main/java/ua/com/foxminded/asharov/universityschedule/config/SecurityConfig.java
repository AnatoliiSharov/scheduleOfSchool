package ua.com.foxminded.asharov.universityschedule.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    public SecurityConfig() {
        super();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails student = User.builder().username("userStudent").password(passwordEncoder().encode("11111"))
                .roles(Role.VIEWER.name()).build();
        UserDetails staff = User.builder().username("userStaff").password(passwordEncoder().encode("22222"))
                .roles(Role.VIEWER.name(), Role.MODERATOR.name()).build();
        UserDetails admin = User.builder().username("userAdmin").password(passwordEncoder().encode("33333"))
                .roles(Role.VIEWER.name(), Role.MODERATOR.name(), Role.CREATOR.name()).build();
    
        return new InMemoryUserDetailsManager(student, staff, admin);

    
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .authorizeRequests()
        .antMatchers("/index", "/").permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .formLogin(form -> form.loginPage("/login").permitAll()).logout(LogoutConfigurer::permitAll);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}
