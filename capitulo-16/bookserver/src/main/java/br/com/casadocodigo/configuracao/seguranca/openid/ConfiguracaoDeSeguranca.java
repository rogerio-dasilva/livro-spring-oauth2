package br.com.casadocodigo.configuracao.seguranca.openid;

import br.com.casadocodigo.usuarios.RepositorioDeUsuarios;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@EnableWebSecurity
public class ConfiguracaoDeSeguranca extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private OAuth2RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper jsonMapper;
    
    @Autowired
    private RepositorioDeUsuarios repositorioDeUsuarios;
    
    @Autowired
    private OpenIdTokenServices tokenServices;
    
    @Autowired
    private OAuth2ProtectedResourceDetails resourcesDetailes;
    
    @Bean
    public OpenIdConnectFilter openIdConnectFilter(){
        OpenIdConnectFilter filter = new OpenIdConnectFilter("/livros/**", "/google/callback");
        
        filter.setRestTemplate(restTemplate);
        filter.setJsonMapper(jsonMapper);
        filter.setRepositorioDeUsuarios(repositorioDeUsuarios);
        filter.setTokenServices(tokenServices);
        filter.setResourceDetails(resourcesDetailes);
        return filter;
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		String[] caminhosPermitidos = new String[] {
				"/", "/home", "/usuarios", "/google/login",
				"/webjars/**", "/static/**", "/jquery*"
		};
                
                http
                    .addFilterAfter(filtroParaClienteOAuth2(), AbstractPreAuthenticatedProcessingFilter.class)
                    .addFilterAfter(openIdConnectFilter(), OAuth2ClientContextFilter.class)
                    .httpBasic()
                    .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/google/callback"))
                .and()
                    .authorizeRequests()
                    .antMatchers(caminhosPermitidos).permitAll()
                    .anyRequest().authenticated()
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                    .permitAll()
                .and()
                    .csrf().disable();

	}
        
        private Filter filtroParaClienteOAuth2(){
            return new OAuth2ClientContextFilter();
        }

}
