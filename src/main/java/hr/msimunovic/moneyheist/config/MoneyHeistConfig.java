package hr.msimunovic.moneyheist.config;

import hr.msimunovic.moneyheist.common.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MoneyHeistConfig implements WebMvcConfigurer {

    @Value("${allowed.origins}")
    private String[] allowedOrigins;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        // set up cors mapping
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .exposedHeaders(Constants.HTTP_HEADER_LOCATION);

    }
}
