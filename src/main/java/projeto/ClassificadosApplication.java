package projeto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import projeto.classificados.model.Anuncios;

@SpringBootApplication
@EntityScan(basePackages = { "projeto.classificados.model" })
@ComponentScan(basePackages = { "projeto.*" })
@EnableJpaRepositories(basePackages = { "projeto.classificados.repository" })
@EnableTransactionManagement
public class ClassificadosApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(ClassificadosApplication.class, args);
	}
	
	@Bean
	public Anuncios getAnuncios() {
		return new Anuncios();
	}

}

