package projeto.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import projeto.classificados.model.Usuario;
import projeto.classificados.repository.AnunciosRepository;
import projeto.classificados.repository.UsuariosRepository;

@Controller
public class CadastroController {

	@Autowired
	UsuariosRepository usuariosRepository;
	
	@Autowired
	AnunciosRepository anunciosRepository;

	@GetMapping("/cadastre-se")
	public ModelAndView cadastro() {

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastre-se");
		modelAndView.addObject("usuarioobj", new Usuario());
				
		return modelAndView;
	}

	@PostMapping(value = "/cadastrar")
	public ModelAndView salvar(@Valid Usuario usuario, BindingResult result) {
		
		List<String> msg = new ArrayList<String>();
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastre-se");
		
		Usuario usuarioConsultado = usuariosRepository.consultarPorEmail(usuario.getEmail());
				
		
		if (usuarioConsultado != null) {
			modelAndView = new ModelAndView("cadastro/cadastre-se");
			modelAndView.addObject("msgRecuperar", "Este email já está cadastrado. ");
			modelAndView.addObject("usuarioobj", usuario);
			return modelAndView;		
		}
		
		if(result.hasErrors()) {
					
			modelAndView = new ModelAndView("cadastro/cadastre-se");	
			for(ObjectError objectError : result.getAllErrors()) {
				msg.add(objectError.getDefaultMessage());
			}
			
			modelAndView.addObject("msg", msg);
			modelAndView.addObject("usuarioobj", usuario);
			
			return modelAndView;
		}
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		usuario.setSenha(encoder.encode(usuario.getSenha()));
		
		System.out.println(usuario.getSenha());
		usuariosRepository.save(usuario);

		modelAndView.addObject("msgSuccess", "Cadastro realizado com sucesso! ");
		modelAndView.addObject("usuarioobj", new Usuario());
		
		return modelAndView;
	}
}
