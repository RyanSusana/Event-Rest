package com.isa.arnhem.isarest.config;

import com.isa.arnhem.isarest.models.user.UserType;
import com.isa.arnhem.isarest.services.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailService userDetailsService;

    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<UserType> ranksWithAuthority = UserType.STUDENT.getRanksAbove();
        String[] rankArray = new String[ranksWithAuthority.size()];
        for (int i = 0; i < rankArray.length; i++) {
            rankArray[i] = ranksWithAuthority.get(i).name();
        }
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/api/**/secured/**").hasAnyRole(rankArray)
                .and()
                .httpBasic();
    }
}
