package br.com.casadocodigo.configuracao.seguranca.openid;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Client
public class GoogleOpenIdConnectConfig {
    
    @Autowired
    DiscoveryDocument discovery;

    @Bean
    public OAuth2ProtectedResourceDetails protectedResourceDetails(){
        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
        
        details.setClientId(discovery.getClientId());
        details.setClientSecret(discovery.getClientSecret());
        details.setAccessTokenUri(discovery.getAccessTokenUri());
        details.setUserAuthorizationUri(discovery.getUserAuthorizationUri());
        details.setPreEstablishedRedirectUri(discovery.getRedirectUri());
        details.setScope(Arrays.asList("openid", "email", "profile"));
        details.setUseCurrentUri(false);
        
        return details;
    }
    
    @Bean
    public OAuth2RestTemplate googleOpenIdRestTemplate(OAuth2ClientContext clientContext) {
        OAuth2RestTemplate template = new OAuth2RestTemplate(
                protectedResourceDetails(), clientContext);
        
        template.setAccessTokenProvider(getAccessTokenProvider());
        return template;
    }
    
    private AccessTokenProviderChain getAccessTokenProvider(){
        AccessTokenProviderChain provider = new AccessTokenProviderChain(
            Arrays.asList(new AuthorizationCodeAccessTokenProvider()));
        
        return provider;
    } 
}
