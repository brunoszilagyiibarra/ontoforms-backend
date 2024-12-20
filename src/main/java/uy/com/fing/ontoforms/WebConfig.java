package uy.com.fing.ontoforms;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Allow all endpoints
                .allowedOrigins("*")     // Allow all origins (or specify specific origins like "http://example.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")  // Allow all HTTP methods
                .allowedHeaders("*");     // Allow all headers
    }
}
