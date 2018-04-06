package com.krydtin.user.configurations;

import com.krydtin.user.configurations.jwt.JwtTokenFilterConfigurer;
import com.krydtin.user.configurations.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()
            .and()
                .csrf().disable()
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/signin")
                    .permitAll()
                .antMatchers(HttpMethod.POST, "/signup")
                    .permitAll()
                .anyRequest()
                    .authenticated()
            .and()
                .apply(new JwtTokenFilterConfigurer(jwtTokenProvider))
            .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
