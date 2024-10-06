package com.example.QLDA;

import com.example.QLDA.Service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())  // Tắt bảo vệ CSRF cho ứng dụng
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/", "/login", "/register", "/error")
                        .permitAll()  // Cho phép truy cập các URL công khai
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")  // Chỉ cho phép ADMIN truy cập URL bắt đầu bằng /admin
                        .requestMatchers("/user/**").hasAnyAuthority("USER", "ADMIN")  // Chỉ USER hoặc ADMIN mới được truy cập
                        .anyRequest().authenticated()  // Mọi yêu cầu khác đều phải đăng nhập
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")  // Đường dẫn đến trang đăng nhập
                        .loginProcessingUrl("/login")  // URL xử lý khi đăng nhập
                        .defaultSuccessUrl("/home", true)  // Đường dẫn sau khi đăng nhập thành công
                        .failureUrl("/login?error=true")  // Đường dẫn khi đăng nhập thất bại
                        .permitAll()  // Cho phép tất cả truy cập trang đăng nhập
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")  // Trang chuyển hướng sau khi đăng xuất
                        .invalidateHttpSession(true)  // Hủy session sau khi đăng xuất
                        .clearAuthentication(true)  // Xóa xác thực sau khi đăng xuất
                        .deleteCookies("JSESSIONID")  // Xóa cookie phiên sau khi đăng xuất
                        .permitAll()  // Cho phép truy cập trang đăng xuất
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("UniqueAndSecretKey")  // Khóa bảo mật cho remember-me
                        .tokenValiditySeconds(86400)  // Thời gian ghi nhớ đăng nhập trong 1 ngày
                        .userDetailsService(userDetailsService)  // Cấu hình dịch vụ UserDetails
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/403")  // Trang truy cập bị từ chối
                )
                .build();  // Hoàn thành cấu hình SecurityFilterChain
    }
}
