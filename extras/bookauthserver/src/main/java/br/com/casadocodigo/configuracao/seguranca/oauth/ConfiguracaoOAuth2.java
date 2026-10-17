package br.com.casadocodigo.configuracao.seguranca.oauth;

import br.com.casadocodigo.configuracao.seguranca.DadosDoUsuarioService;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
public class ConfiguracaoOAuth2 {
//    public static final String RESOURCE_ID = "books";

//    @EnableResourceServer
//    protected static class OAuth2ResourceServer extends ResourceServerConfigurerAdapter {
//
//        @Override
//        public void configure(ResourceServerSecurityConfigurer resources)
//            throws Exception {
//            resources.resourceId(RESOURCE_ID);
//        }
//
//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//            http
//                .authorizeRequests()
//                    .anyRequest().authenticated().and()
//                .requestMatchers()
//                    .antMatchers("/api/v2/**").and()
//                    .cors();
//        }
//    }

    @EnableAuthorizationServer
    protected static class OAuth2AuthorizationServer
        extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private ClientDetailsService clientDetailsService;

        @Autowired
        private DadosDoUsuarioService userDetailsService;
        
        @Autowired @Qualifier("dsOauth")
        private DataSource dataSource;
        
        @Bean
        public TokenStore tokenStore(){
            return new JdbcTokenStore(dataSource);
        }
        
        @Bean
        public ApprovalStore approvalStore(){
            return new JdbcApprovalStore(dataSource);
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            DefaultOAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);
            requestFactory.setCheckUserScopes(true);

            // @formatter:off
            endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .requestFactory(requestFactory)
                .tokenStore(tokenStore())
                .approvalStore(approvalStore());
            // @formatter:on
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.jdbc(dataSource);
//            clients.inMemory()
//                .withClient("cliente-app")
//                .secret("123456")
//                .authorizedGrantTypes(
//                        "password", 
//                        "authorization_code",
//                        "implicit",
//                        "refresh_token")
//                .scopes("read", "write")
//                .authorities("read", "write")
//                .accessTokenValiditySeconds(60 * 2)
//                .refreshTokenValiditySeconds(60 * 60)
//                .resourceIds(RESOURCE_ID)
//            .and()
//                .withClient("cliente-admin")
//                .secret("123abc")
//                .authorizedGrantTypes("client_credentials")
//                .scopes("read")
//                .accessTokenValiditySeconds(60 * 2)
//                .refreshTokenValiditySeconds(60 * 60)
//                .resourceIds(RESOURCE_ID)
//
//            .and()
//                .withClient("cliente-browser")
//                .secret("abc")
//                .authorizedGrantTypes("implicit")
//                .accessTokenValiditySeconds(60 * 2)
//                .refreshTokenValiditySeconds(60 * 60)
//                .scopes("read");
        }
    }
}
