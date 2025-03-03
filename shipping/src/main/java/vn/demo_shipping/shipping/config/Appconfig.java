package vn.demo_shipping.shipping.config;

// import java.text.SimpleDateFormat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

// import com.fasterxml.jackson.databind.DeserializationFeature;
// import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class Appconfig {
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
}
