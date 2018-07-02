package kz.asialife.banks.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String REALM_NAME = "asialife";

    @Value("${security.user.name.bcc}")
    private String bccUsername;

    @Value("${security.user.password.bcc}")
    private String bccPassword;

    @Value("${security.user.name.kaspi}")
    private String kaspiUsername;

    @Value("${security.user.password.kaspi}")
    private String kaspiPassword;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .realmName(REALM_NAME)
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser(bccUsername).password(bccPassword).roles("BCC")
                .and()
                .withUser(kaspiUsername).password(kaspiPassword).roles("KASPI");
    }

}