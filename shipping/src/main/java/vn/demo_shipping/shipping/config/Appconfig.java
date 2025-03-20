package vn.demo_shipping.shipping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class Appconfig implements WebMvcConfigurer {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // @Bean
    // public ObjectMapper objectMapper() {
    // ObjectMapper objectMapper = new ObjectMapper();
    // objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    // objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    // return objectMapper;
    // }

    // @Bean
    // public CorsFilter corsFilter() {
    // CorsConfiguration config = new CorsConfiguration();
    // // config.setAllowedOrigins(List.of("*"));
    // config.setAllowedOriginPatterns(List.of("*"));
    // config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    // config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    // config.setAllowCredentials(true);

    // UrlBasedCorsConfigurationSource source = new
    // UrlBasedCorsConfigurationSource();
    // source.registerCorsConfiguration("/**", config);
    // return new CorsFilter(source);
    // }

    @Override
    public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // Cho phép tất cả origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các phương thức HTTP
                .allowedHeaders("*") // Cho phép tất cả headers
                .allowCredentials(true); // Hỗ trợ credentials (cookies, token)
    }

}
