package cn.tvfan.elasticdemo.config;

import cn.tvfan.elasticdemo.entity.AdminEntity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/application/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/**").permitAll()
                .and()
                .formLogin();
                //.loginPage("/login")
                //.failureUrl("/login?error=true");
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//                UserDetails userDetails = new AdminEntity("admin", "secret");
//                if (userDetails != null) {
//                    return userDetails;
//                }
//                throw new UsernameNotFoundException("User '" + username + "' not found.");
//            }
//        });
        auth.userDetailsService((username) -> new AdminEntity("admin", "secret"));
    }
}
