package projeto.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import projeto.classificados.model.Anuncios;
import projeto.classificados.model.Usuario;
import projeto.classificados.repository.AnunciosRepository;
import projeto.classificados.repository.CategoriasRepository;
import projeto.classificados.repository.UsuariosRepository;
import projeto.classificados.repository.filter.ClassificadosFilter;
import projeto.controller.page.PageWrapper;

@Controller
public class IndexController {

	@Autowired
	AnunciosRepository anunciosRepository;
	
	@Autowired
	UsuariosRepository usuariosRepository;
	
	@Autowired
	CategoriasRepository categoriasRepository;
	
//	@RequestMapping("/")
//	public ModelAndView index() {
//		ModelAndView modelAndView = new ModelAndView("/index");
//		
//		return modelAndView;
//	}
	
	@GetMapping("/")
	public ModelAndView init(@AuthenticationPrincipal User user, 
			@PageableDefault(size=2) Pageable pageable, HttpServletRequest httpServletRequest
			) {
				
 		ModelAndView modelAndView = new ModelAndView("/index");
				
		Long totalAnuncios = anunciosRepository.count();
		Long totalUsuarios = usuariosRepository.count();
		PageWrapper<Anuncios> paginaWrapper = new PageWrapper<>(anunciosRepository.getAnunciosOrdemInversa(pageable), httpServletRequest);
			
		modelAndView.addObject("totalAnuncios", totalAnuncios);
		modelAndView.addObject("totalUsuarios", totalUsuarios);
		modelAndView.addObject("paginas", paginaWrapper);
		modelAndView.addObject("categorias", categoriasRepository.findAll());
								
		if(user != null) {
			Usuario usuario = usuariosRepository.consultarPorEmail(user.getUsername());
			modelAndView.addObject("usuarioLogado", usuario);
		}
		return modelAndView;
	}
	
	@GetMapping("/pesquisa/**")
	public ModelAndView pesquisa(@Valid ClassificadosFilter classificadosFilter, 
			@PageableDefault(size=2) Pageable pageable, HttpServletRequest httpServletRequest) {
		
		ModelAndView modelAndView = new ModelAndView("/pesquisa");
		Page<Anuncios> anuncios = null;
		PageWrapper<Anuncios> pageWrapper = null;
		Long totalAnuncios = anunciosRepository.count();
		Long totalUsuarios = usuariosRepository.count();		
		
		/**Categoria vazia
		 * Estado vazio
		 * Preço vazio
		 * */
		if(!classificadosFilter.getCategoria().isEmpty()
				|| !classificadosFilter.getEstado().isEmpty()
				|| classificadosFilter.getPreco() != null) {
			
				
			Long valor1 = 0L, valor2 = 0L;
			
			if(classificadosFilter.getPreco() != null) {
				
				Integer valorRecebido = classificadosFilter.getPreco().intValue();
				
				switch (valorRecebido) {
				
					case 1:{
						valor1 = 0L;
						valor2 = 100L;
						break;
					}
					case 2:{
						valor1 = 101L;
						valor2 = 499L;
						break;
					}
					case 3:{
						valor1 = 500L;
						valor2 = 999L;
						break;
					}
					case 4:{
						valor1 = 1000L;
						valor2 = 10000000000L;
						break;
					}
					default:{
						
					}
				}
			}
			
				/*Entra se todos os filtros foram selecionados*/
				if(!classificadosFilter.getCategoria().isEmpty()
					&& classificadosFilter.getPreco()!= null
					&& !classificadosFilter.getEstado().isEmpty()) {
					
					System.out.println("Todos validos");
					anuncios = anunciosRepository.getFiltroTodos(Long.parseLong(classificadosFilter.getCategoria()), valor1, valor2, classificadosFilter.getEstado(), pageable);
					pageWrapper = new PageWrapper<>(anuncios, httpServletRequest);
					// anuncios = anunciosRepository.getFiltroTodos(Long.parseLong(classificadosFilter.getCategoria()), valor1, valor2, classificadosFilter.getEstado(), pageable);
					
					//colocar o que fazer na view
					
				/*Entra se categoria e preço foram selecionados*/	
				}else if(!classificadosFilter.getCategoria().isEmpty()
						&& classificadosFilter.getPreco()!= null
						&& classificadosFilter.getEstado().isEmpty()) {
					
					System.out.println("Categoria e preço validos");
					
					anuncios = anunciosRepository.getFiltroCategoriaPreco(Long.parseLong(classificadosFilter.getCategoria()), valor1, valor2, pageable);
					pageWrapper = new PageWrapper<>(anuncios, httpServletRequest);				
					//colocar o que fazer na view
					
				/*Entra se Categoria e estado foram selecionados*/					
				}else if(!classificadosFilter.getCategoria().isEmpty()
						&& classificadosFilter.getPreco()== null
						&& !classificadosFilter.getEstado().isEmpty()) {
				
					System.out.println("Categoria e estado validos");
					
					anuncios = anunciosRepository.getFiltroCategoriaEstado(Long.parseLong(classificadosFilter.getCategoria()), classificadosFilter.getEstado(), pageable);
					pageWrapper = new PageWrapper<>(anuncios, httpServletRequest);
					//colocar o que fazer na view
					
				/*Entra se preço e estados foram selecionados*/
				}else if(classificadosFilter.getCategoria().isEmpty()
						&& classificadosFilter.getPreco()!= null
						&& !classificadosFilter.getEstado().isEmpty()) {	
					
					System.out.println("Preço e estados validos");
					
					anuncios = anunciosRepository.getFiltroPrecoEstado(valor1, valor2, classificadosFilter.getEstado(), pageable);
					pageWrapper = new PageWrapper<>(anuncios, httpServletRequest);
					//colocar o que fazer na view
				
				/*Entra se somente categoria foi selecionada*/
				}else if(!classificadosFilter.getCategoria().isEmpty()
						&& classificadosFilter.getPreco()== null
						&& classificadosFilter.getEstado().isEmpty()) {
					
					System.out.println("Somente categoria");
					
					anuncios = anunciosRepository.getFiltroPorCategoria(Long.parseLong(classificadosFilter.getCategoria()), pageable);
					pageWrapper = new PageWrapper<>(anuncios, httpServletRequest);
//								anuncios = anunciosRepository.getFiltroPorCategoria(Long.parseLong(classificadosFilter.getCategoria()));
				
				/*Entra se somente o preço foi selecionado*/
				}else if(classificadosFilter.getCategoria().isEmpty()
						&& classificadosFilter.getPreco()!= null
						&& classificadosFilter.getEstado().isEmpty()) {
					
					System.out.println("Somente Preço");
					
					anuncios = anunciosRepository.getFiltroPorPreco(valor1, valor2, pageable);
					pageWrapper = new PageWrapper<>(anuncios, httpServletRequest);
				
				/*Entra se somente o estado foi selecionado*/
				}else if(classificadosFilter.getCategoria().isEmpty()
						&& classificadosFilter.getPreco()== null
						&& !classificadosFilter.getEstado().isEmpty()) {
					
					System.out.println("Somente estado");
					
					anuncios = anunciosRepository.getFiltroPorEstado(classificadosFilter.getEstado(), pageable);
					pageWrapper = new PageWrapper<>(anuncios, httpServletRequest);
				}
				
				/**/
				if(anuncios.isEmpty()) {
					
					System.out.println("Não foi encontrado nenhum anuncio com este filtro !");
					modelAndView.addObject("mensagemNenhum", "Não foi encontrado nenhum anuncio com este filtro !");
//										
				}else{
					
					for (Anuncios anuncios2 : anuncios) {
						System.out.println("Id: "+ anuncios2.getId() + "\nTitulo: "+anuncios2.getTitulo());
					}
				}
			
		}else {
			modelAndView.addObject("mensagemNenhum", "Nenhum filtro selecionado !");
			pageWrapper = new PageWrapper<>(anunciosRepository.getAnunciosOrdemInversa(pageable), httpServletRequest);
		}
			
		modelAndView.addObject("paginas", pageWrapper);
		modelAndView.addObject("totalAnuncios", totalAnuncios);
		modelAndView.addObject("totalUsuarios", totalUsuarios);
		modelAndView.addObject("categorias", categoriasRepository.findAll());
		modelAndView.addObject("classificadosFilter", classificadosFilter);	

		return modelAndView;
	}	
	
}
