package client.config;

import web.converter.ClientConverter;
import web.converter.OrderConverter;
import web.converter.OrderProductConverter;
import web.converter.ProductConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan({"client.ui"})
public class ClientConfig {
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    ExecutorService executorService(){
        return Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
    }

    @Bean
    ClientConverter clientConverter() {
        return new ClientConverter();
    }

    @Bean
    OrderConverter orderConverter() {
        return new OrderConverter();
    }

    @Bean
    ProductConverter productConverter() {
        return new ProductConverter();
    }

    @Bean
    OrderProductConverter orderProductConverter() {
        return new OrderProductConverter();
    }
}
