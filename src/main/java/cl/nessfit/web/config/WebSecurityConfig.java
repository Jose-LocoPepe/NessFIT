package cl.nessfit.web.config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Inyección de origen de datos (base de datos)
     */
    @Autowired
    private DataSource dataSource;


    /**
     * Configura el usuario y el rol para acceder al sistema
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	auth.jdbcAuthentication().dataSource(dataSource)
		// Busca al usuario por el parámetro rut en la base de datos
		.usersByUsernameQuery("select rut, contrasena, estado from usuarios where rut=?")
		// Busca el rol asociado al rut
		.authoritiesByUsernameQuery(
			"select u.rut, r.nombre from usuarios u inner join roles r on u.id_rol = r.id where u.rut=?");
    }
    /**
     * Configura el filter Chain para acceso a las rutas
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
	http.authorizeRequests()
		.antMatchers("/scss/**", "/css/**", "/img/**", "/js/**", "/vendors/**")
			.permitAll()
		.and().formLogin()
			.loginPage("/login").usernameParameter("rut").passwordParameter("contrasena");
    }
 
}
