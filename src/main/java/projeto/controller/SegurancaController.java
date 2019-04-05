package projeto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import projeto.classificados.model.Usuario;
import projeto.classificados.repository.AnunciosRepository;
import projeto.classificados.repository.UsuariosRepository;

@Controller
public class SegurancaController {

	@Autowired
	UsuariosRepository usuariosRepository;
	
	@Autowired
	AnunciosRepository anunciosRepository;
	
	@GetMapping("/login")
	public ModelAndView login (@AuthenticationPrincipal User user) {//anotação @Authentication recebe usuario que logou
		
		ModelAndView modelAndView = new ModelAndView("/");
		
		if(user != null) {
			
			Usuario usuario = usuariosRepository.consultarPorEmail(user.getUsername());
			modelAndView.addObject("usuarioLogado", usuario);
			return modelAndView;
		}else{
			modelAndView = new ModelAndView("/login");
			
		}
		
		return modelAndView;
	}
	
	@GetMapping("/loginError")
	public ModelAndView loginErro(@AuthenticationPrincipal User user) {
		
		ModelAndView modelAndView = new ModelAndView("/login");
		
		modelAndView.addObject("msgRecuperar", "Usuario ou senha inválidos. ");
		
		return modelAndView;
		
	}
	
	@GetMapping("/recuperarsenha")
	public ModelAndView recuperarSenha() {
		
		ModelAndView modelAndView = new ModelAndView("cadastro/recuperarsenha");
				
		return modelAndView;
	}
	
	@PostMapping("/recuperar")
	public ModelAndView recuperar(String email) {
		
		ModelAndView modelAndView = new ModelAndView("cadastro/recuperarsenha");
		Usuario usuario = usuariosRepository.consultarPorEmail(email);
		String msgSuccess, msg;
		
		if(usuario != null) {
			msgSuccess = "Os dados de recuperação foram enviados para o e-mail informado. ";
			modelAndView.addObject("msgSuccess", msgSuccess);
		}else {
			msg = "E-mail não cadastrado. ";
			modelAndView.addObject("msg", msg);
		}
		
		return modelAndView;
	}	
	
}
