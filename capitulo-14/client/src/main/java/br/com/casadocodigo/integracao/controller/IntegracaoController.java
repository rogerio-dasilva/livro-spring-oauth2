package br.com.casadocodigo.integracao.controller;

import br.com.casadocodigo.configuracao.seguranca.UsuarioLogado;
import br.com.casadocodigo.integracao.bookserver.AuthorizationCodeTokenService;
import br.com.casadocodigo.integracao.bookserver.ImplicitTokenService;
import br.com.casadocodigo.integracao.bookserver.OAuth2Token;
import br.com.casadocodigo.integracao.bookserver.PasswordTokenService;
import br.com.casadocodigo.usuarios.AcessoBookserver;
import br.com.casadocodigo.usuarios.Usuario;
import br.com.casadocodigo.usuarios.UsuariosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/integracao")
public class IntegracaoController {
    
    private static final Logger logger = LoggerFactory.getLogger(IntegracaoController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/callback")
//    public ModelAndView callback() {
    public ModelAndView callback() {

        logger.info("### 6");

        return new ModelAndView("forward:/minhaconta/principal");
    }
    
//    @RequestMapping(value = "/implicit" , method = RequestMethod.GET)
//    public ModelAndView implicit(){
//        return new ModelAndView("minhaconta/bookserver");
//    }
//
//    private Usuario usuarioLogado() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UsuarioLogado usuarioLogado = (UsuarioLogado) authentication.getPrincipal();
//        return usuarios.findById(usuarioLogado.getId());
//    }

//    @Autowired
//    private UsuariosRepository usuarios;
//    
//    @Autowired
//    private PasswordTokenService passwordTokenService;
//    
//    @Autowired
//    private ImplicitTokenService implicitTokenService;
//
//    @Autowired
//    private AuthorizationCodeTokenService authorizationCodeTokenService;
//
//    @RequestMapping(method = RequestMethod.GET)
//    public ModelAndView integracao() {
//        String endpointDeAutorizacao = authorizationCodeTokenService.getAuthorizationEndpoint();
////        String endpointDeAutorizacao = implicitTokenService.getAuthorizationEndpoint();
//        return new ModelAndView("redirect:" + endpointDeAutorizacao);
//    }


}
