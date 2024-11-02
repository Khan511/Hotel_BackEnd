package com.naji.gjensidige.sHotel.config;

import java.util.List;
import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@Configuration
public class CorsConfig {

        public static final String X_REQUESTED_WITH = "X-Requested-With";

        @Bean
        public CorsFilter corsFilter() {

                var urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
                var corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowCredentials(true);

                corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173"));

                corsConfiguration.setAllowedHeaders(List.of(
                                HttpHeaders.ORIGIN, HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, HttpHeaders.CONTENT_TYPE,
                                HttpHeaders.ACCEPT, HttpHeaders.AUTHORIZATION,
                                X_REQUESTED_WITH, HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD,
                                HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS,
                                HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS));

                corsConfiguration.setExposedHeaders(List.of(
                                HttpHeaders.ORIGIN, HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, HttpHeaders.CONTENT_TYPE,
                                HttpHeaders.ACCEPT, HttpHeaders.AUTHORIZATION,
                                X_REQUESTED_WITH, HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
                                HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS));

                corsConfiguration.setAllowedMethods(List.of(
                                HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
                                HttpMethod.PATCH.name(), HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name()));

                urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

                return new CorsFilter(urlBasedCorsConfigurationSource);

        }
}
