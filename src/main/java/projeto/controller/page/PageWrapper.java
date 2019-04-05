package projeto.controller.page;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

public class PageWrapper<T> {
	
	private Page<T> page;
	
	private UriComponentsBuilder uriBuilder;
	
		
	public PageWrapper (Page<T> page, HttpServletRequest httpServletRequest) {
		this.page = page;
		this.uriBuilder = ServletUriComponentsBuilder.fromRequest(httpServletRequest);
	}

	public List<T> getConteudo(){
		return page.getContent();	
	}
	
	public int getAtual() {
		return page.getNumber();
	}
	
	public boolean getPrimeira() {
	
		return page.isFirst();
		
	}
	
	public int getTotalPaginas() {
		return page.getTotalPages();
	}
	
	public boolean getUltima() {
		return page.isLast();
	}
	
	public String urlParaPagina(int pagina) {
		return uriBuilder.replaceQueryParam("page", pagina).toUriString();	
	}
}
