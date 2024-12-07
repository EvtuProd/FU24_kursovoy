package ru.evtu.kursovoy_new;

// Импорт необходимых классов и аннотаций из Spring Security
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

/**
 * Конфигурация безопасности для Spring приложения.
 *
 * Этот класс настраивает безопасность HTTP запросов, включая аутентификацию, авторизацию,
 * обработку CORS (Cross-Origin Resource Sharing) и защиту от CSRF (Cross-Site Request Forgery).
 */
@Configuration // Указывает, что этот класс содержит конфигурацию Spring
@EnableWebSecurity // Включает поддержку безопасности в приложении
public class SecurityConfig {

    /**
     * Метод, создающий цепочку фильтров безопасности.
     *
     * @param http объект HttpSecurity, используемый для настройки безопасности
     * @return объект SecurityFilterChain, представляющий конфигурацию безопасности
     * @throws Exception если возникает ошибка при настройке безопасности
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Настройка безопасности для HTTP-запросов
        // Разрешить доступ к функции выхода без аутентификации
        http
                // Включаем защиту от CSRF
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/cars/api/**") // Игнорируем CSRF для указанных путей
                        .ignoringRequestMatchers("/rent")
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/**").permitAll() // Разрешаем доступ к API без аутентификации
                        .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                )
                // Настройка формы входа
                .formLogin(form -> form
                        .defaultSuccessUrl("/cars", true) // URL перенаправления после успешного входа
                        .permitAll() // Разрешаем доступ к странице входа без аутентификации
                )
                // Настройка выхода из системы
                .logout(LogoutConfigurer::permitAll)
                .cors(); // Включаем поддержку CORS

        // Построить и вернуть объект SecurityFilterChain
        return http.build();
    }

    /**
     * Метод для настройки конфигурации CORS (Cross-Origin Resource Sharing).
     *
     * @return источник конфигурации CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Указываем домен вашего фронтенда
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Разрешаем необходимые HTTP методы
        configuration.setAllowCredentials(true); // Разрешаем отправку куков

        // Регистрация конфигурации CORS для всех URL
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Применяем настройки ко всем запросам
        return source;
    }
}



