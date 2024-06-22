package es.jaimelozanodiegotorres.backapp.config.cors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Value("${spring.profiles.active}")
    String profile;

    /**
     * CORS: Configuración más ajustada.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                if(profile==null)
                    throw new RuntimeException("No profile setUp");

                if(profile.contains("dev")||profile.equals("prod")){ //dev profiles and prod docker
                    registry.addMapping("/**")
                            .allowedOrigins("http://localhost:4200")
                            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                            .maxAge(3600);
                }
                if(profile.equals("prod-server")){
                    registry.addMapping("/**")
                            .allowedOrigins("https://tfg-jaime-lozano-diego-torres-git-master-jaime9lozanos-projects.vercel.app/")
                            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                            .maxAge(3600);
                    registry.addMapping("/**")
                            .allowedOrigins("https://bio-online.netlify.app/")
                            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                            .maxAge(3600);
                }
            }
        };
    }
}
