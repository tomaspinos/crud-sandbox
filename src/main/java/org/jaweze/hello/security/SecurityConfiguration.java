package org.jaweze.hello.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //@formatter:off
        auth
                .inMemoryAuthentication()
                .withUser("admin").password("p").roles(CustomRoles.ADMIN.name())
                .and()
                .withUser("manager").password("p").roles(CustomRoles.MANAGER.name())
                .and()
                .withUser("viewer").password("p").roles(CustomRoles.VIEWER.name());
        //@formatter:on
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationManager();
    }

    static {
        // Use a custom SecurityContextHolderStrategy
        SecurityContextHolder.setStrategyName(VaadinSessionSecurityContextHolderStrategy.class.getName());
    }
}
