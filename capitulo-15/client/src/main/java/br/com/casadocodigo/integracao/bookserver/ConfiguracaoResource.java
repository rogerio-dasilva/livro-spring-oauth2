package br.com.casadocodigo.integracao.bookserver;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Client
public class ConfiguracaoResource {

    private static final Logger logger = LoggerFactory.getLogger(ConfiguracaoResource.class);
    
    @Autowired
    private ClientTokenServices clientTokenServices;

    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    @Bean
    public OAuth2ProtectedResourceDetails bookserver() {
        logger.info("### 2");
        AuthorizationCodeResourceDetails detailsForBookserver = new AuthorizationCodeResourceDetails();

        detailsForBookserver.setId("bookserver");
        detailsForBookserver.setTokenName("oauth_token");
        detailsForBookserver.setClientId("cliente-app");
        detailsForBookserver.setClientSecret("123456");
        detailsForBookserver.setAccessTokenUri("http://localhost:8081/oauth/token");
        detailsForBookserver.setUserAuthorizationUri("http://localhost:8081/oauth/authorize");
        detailsForBookserver.setScope(Arrays.asList("read", "write"));

        detailsForBookserver.setPreEstablishedRedirectUri(("http://localhost:9000/integracao/callback"));
        detailsForBookserver.setUseCurrentUri(false);

        detailsForBookserver.setClientAuthenticationScheme(AuthenticationScheme.header);
        return detailsForBookserver;
    }

    @Autowired
    @Qualifier("accessTokenRequest")
    private AccessTokenRequest accessTokenRequest;

    @Bean
    public OAuth2RestTemplate oauth2RestTemplate() {
        logger.info("### 1");

        OAuth2ProtectedResourceDetails resourceDetails = bookserver();

        OAuth2RestTemplate template = new OAuth2RestTemplate(resourceDetails, oauth2ClientContext);

        AccessTokenProviderChain provider = new AccessTokenProviderChain(
            Arrays.asList(new AuthorizationCodeAccessTokenProvider()));

        provider.setClientTokenServices(clientTokenServices);
        template.setAccessTokenProvider(provider);

        return template;
    }

}
