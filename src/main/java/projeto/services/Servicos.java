package projeto.services;

import org.springframework.beans.factory.annotation.Autowired;

import projeto.classificados.model.Anuncios;
import projeto.classificados.repository.AnunciosRepository;

public class Servicos {

	@Autowired
	AnunciosRepository anunciosRepository;
	
	
	public boolean validarAnunciosUsuario(Long idUsuario, Long idAnuncios){
		
		Anuncios anuncio = anunciosRepository.getAnuncioUsuario(idAnuncios, idUsuario);
				
		if(anuncio != null) {
			System.out.println("Anuncio encontrado" + anuncio.getTitulo());
			
			return true;
		}
		
		System.out.println("Anuncio não encontrado");
		
		return false;		
	}
	
}
