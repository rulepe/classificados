package projeto.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import projeto.classificados.model.Anuncios;
import projeto.classificados.model.Usuario;
import projeto.classificados.repository.AnunciosRepository;
import projeto.classificados.repository.CategoriasRepository;
import projeto.classificados.repository.UsuariosRepository;
import projeto.services.Servicos;

@Controller
@Transactional
public class AnunciosController {

	@Autowired
	UsuariosRepository usuariosRepository;
	
	@Autowired
	CategoriasRepository categoriasRepository;
	
	@Autowired
	AnunciosRepository anunciosRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private Servicos servico = new Servicos();
	
	@GetMapping("**/meus-anuncios")
	public ModelAndView init(@AuthenticationPrincipal User user) {
		
		ModelAndView modelAndView = new ModelAndView("/anuncios/meus-anuncios");
		
		try {
			if(user != null) {				
				
				Usuario usuarioLogado = usuariosRepository.consultarPorEmail(user.getUsername());
				
				modelAndView.addObject("anuncios" ,anunciosRepository.getAnuncios(usuarioLogado.getId()));
				
				modelAndView.addObject("usuarioLogado", usuarioLogado);
				
				
			}else {			
				modelAndView = new ModelAndView("/login");
			}
		}catch(Exception e ) {
			throw new RuntimeException("Erro ao iniciar o anuncios controlle", e);
		}
			
		return modelAndView;
	}
	@GetMapping("**/add-anuncio")
	public ModelAndView adicionar(Anuncios anuncios, @AuthenticationPrincipal User user){
				
		ModelAndView modelAndView = new ModelAndView("/anuncios/novo");
		Usuario usuarioLogado = usuariosRepository.consultarPorEmail(user.getUsername());
		
		modelAndView.addObject("categorias", categoriasRepository.findAll());
		modelAndView.addObject("anuncios", new Anuncios());
		modelAndView.addObject("usuarioLogado", usuarioLogado);
//				
		return modelAndView;
	}
	
	@PostMapping("**/salvaranuncio")
	public ModelAndView adicionarAnuncio(@Valid Anuncios anuncios, BindingResult result, @AuthenticationPrincipal User user,@RequestParam MultipartFile arquivo) throws FileUploadException, IOException {
		
		ModelAndView modelAndView = new ModelAndView("/anuncios/novo");
		Usuario usuarioLogado = usuariosRepository.consultarPorEmail(user.getUsername());
		List<String> msg = new ArrayList<String>();
		
		if(result.hasErrors()) {		
			for(ObjectError objError : result.getAllErrors()) {
				msg.add(objError.getDefaultMessage());
			}
			
			modelAndView.addObject("msgAlerta", msg);
			modelAndView.addObject("anuncios", anuncios);
					
		}else {
			anuncios.setUsuario(usuariosRepository.consultarPorEmail(user.getUsername()));
			
			if(anuncios.getId() != null) {
				modelAndView.addObject("msgSucesso", "Seu anúncio foi atualizado.");
			}else {
				modelAndView.addObject("msgSucesso", "Seu anúncio foi adicionado com sucesso.");
			}
			
						
			if(arquivo != null) {
				String foto = new Base64().encodeBase64String(arquivo.getBytes());
				String contentType = arquivo.getContentType();
				anuncios.setFotoBase64(arquivo.getBytes());
				anuncios.setFoto(foto);
				anuncios.setContentType(contentType);
				
			}
			
			anunciosRepository.save(anuncios);
			modelAndView.addObject("categorias", categoriasRepository.findAll());
			modelAndView.addObject("anuncios", new Anuncios());
		}
		
		modelAndView.addObject("categorias", categoriasRepository.findAll());
		modelAndView.addObject("usuarioLogado", usuarioLogado);
		modelAndView.addObject("idnovo", "idnovo");
				
		return modelAndView;
	}
	
	@GetMapping(value="**/editar/{id_anuncio}")
	public ModelAndView editarAnuncio(@PathVariable("id_anuncio") Long id_anuncio, @AuthenticationPrincipal User user) {
		
		ModelAndView modelAndView = new ModelAndView("/anuncios/novo");
		Usuario usuarioLogado = usuariosRepository.consultarPorEmail(user.getUsername());
		Optional<Anuncios> anuncios = anunciosRepository.findById(id_anuncio);		
		
		Long idUsuario = (Long)usuarioLogado.getId();
		Long idAnuncio = (Long)anuncios.get().getId();
		
		if(servico.validarAnunciosUsuario(idUsuario, idAnuncio)) {
			
			modelAndView.addObject("anuncios", anuncios.get());
			modelAndView.addObject("categorias", categoriasRepository.findAll());
			modelAndView.addObject("usuarioLogado" , usuarioLogado);
			modelAndView.addObject("condicao", "condicao");
			
		}else {
			modelAndView = new ModelAndView("/anuncios/naoAutorizado");
			return modelAndView;
		}
							
		long heapSize = Runtime.getRuntime().totalMemory();
		System.out.println(heapSize);
		return modelAndView;
	}
	
	@GetMapping( value="/excluir/{id_anuncio}")
	public ModelAndView excluirAnuncio(@PathVariable("id_anuncio") Long id_anuncio, @AuthenticationPrincipal User user) {
		
		ModelAndView modelAndView = new ModelAndView("/anuncios/meus-anuncios");
		Usuario usuarioLogado = usuariosRepository.consultarPorEmail(user.getUsername());
		
		anunciosRepository.deleteById(id_anuncio);	
		modelAndView.addObject("usuarioLogado", usuarioLogado);
		modelAndView.addObject("anuncios" ,anunciosRepository.getAnuncios(usuarioLogado.getId()));
		modelAndView.addObject("msgExcluido", "Anúncio excluído com sucesso");
		
		return modelAndView;
	}
	
	@GetMapping(value="/produto/{id_anuncio}")
	public ModelAndView produto (@AuthenticationPrincipal User user, @PathVariable("id_anuncio") Long id_anuncio) {
		
		ModelAndView modelAndView = new ModelAndView("/anuncios/produto");
		
		if(user != null) {
			Usuario usuarioLogado = usuariosRepository.consultarPorEmail(user.getUsername());
			modelAndView.addObject("usuarioLogado", usuarioLogado);
		}
		
		
		modelAndView.addObject("anuncios" , anunciosRepository.findById(id_anuncio).get());
		
		return modelAndView;
	}
	
	@GetMapping(value="/naoAutorizado")
	public ModelAndView naoAutorizado() {
		
		ModelAndView modelAndView = new ModelAndView("/anuncios/naoAutorizado");
		
		return modelAndView;
	}
}
