package com.tejko.yamb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tejko.yamb.business.interfaces.PlayerService;
import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.security.AuthEntryPoint;
import com.tejko.yamb.security.AuthTokenFilter;
import com.tejko.yamb.security.RecaptchaFilter;
import com.tejko.yamb.util.JwtUtil;
import com.tejko.yamb.util.RecaptchaClient;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final PlayerService playerService;
	private final AuthEntryPoint unauthorizedHandler;
	private final JwtUtil jwtUtil;
	private final PlayerRepository playerRepository;
	private final RecaptchaClient recaptchaClient;

	@Autowired
	public WebSecurityConfig(PlayerService playerService, AuthEntryPoint unauthorizedHandler, JwtUtil jwtUtil, PlayerRepository playerRepository, RecaptchaClient recaptchaClient) {
		this.playerService = playerService;
		this.unauthorizedHandler = unauthorizedHandler;
		this.jwtUtil = jwtUtil;
		this.playerRepository = playerRepository;
		this.recaptchaClient = recaptchaClient;
	}

	@Bean
	public AuthTokenFilter authTokenFilter() {
		return new AuthTokenFilter(jwtUtil, playerRepository);
	}

	@Bean
	public RecaptchaFilter recaptchaFilter() {
		return new RecaptchaFilter(recaptchaClient);
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(playerService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests()
				.antMatchers("/**").permitAll()
				.antMatchers("/api/**").authenticated()
				.anyRequest().permitAll();
		
		http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(recaptchaFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Value("${spring.profiles.active:default}")
	private String activeProfile;

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		
		return new WebMvcConfigurer() {
			
			private final String[] DEV_ORIGINS = {
				"http://localhost:3000",
			};

			private final String[] PROD_ORIGINS = {
				"https://jamb.com.hr"
			};

			private final String[] ALLOWED_METHODS = {
				"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
			};

			@Override
			public void addCorsMappings(@NonNull CorsRegistry registry) {
				String[] allowedOrigins = "dev".equalsIgnoreCase(activeProfile) ? DEV_ORIGINS : PROD_ORIGINS;

				registry.addMapping("/**")
					.allowedOrigins(allowedOrigins)
					.allowedMethods(ALLOWED_METHODS)
					.allowCredentials(true);
			}
			
		};
	}


}