package projeto.classificados.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import projeto.classificados.model.Anuncios;

@Repository
public interface AnunciosRepository extends CrudRepository<Anuncios, Long>, JpaRepository<Anuncios, Long>{

	@Query ("select a from Anuncios a where a.usuario.id = ?1")
	public List<Anuncios> getAnuncios (Long usuarioid);
	
	@Query("select a from Anuncios a where a.id = ?1 and a.usuario.id = ?2")
	public Anuncios getAnuncioUsuario(Long anuncioId, Long usuarioId);
	
	@Query ("select a from Anuncios a order by a.id desc")
	public Page<Anuncios> getAnunciosOrdemInversa(Pageable pageable);
	
	//Todos os filtros válidos
	@Query ("select a from Anuncios a where a.categoria.id = ?1 and a.valor between ?2 and ?3 and a.conservacao like %?4%")
	public Page<Anuncios> getFiltroTodos(Long categoria, Long precoIncial, Long precoFinal, String estado, Pageable pageable);
	
	//Filtro de Categoria e preço
	@Query("select a from Anuncios a where a.categoria.id = ?1 and a.valor between ?2 and ?3")
	public Page<Anuncios> getFiltroCategoriaPreco(Long categoria, Long precoInicial, Long precoFinal, Pageable pageable);
	
	//Filtro de Categoria e estado
	@Query("select a from Anuncios a where a.categoria.id = ?1 and a.conservacao like %?2%")
	public Page<Anuncios> getFiltroCategoriaEstado(Long categoria, String estado, Pageable pageable);
	
	//Filtro de Preço e estado
	@Query("select a from Anuncios a where a.valor between ?1 and ?2 and a.conservacao like %?3%")
	public Page<Anuncios> getFiltroPrecoEstado(Long valor1, Long valor2, String estado, Pageable pageable);
	
	//Filtro para somente categoria
	@Query ("select a from Anuncios a where a.categoria.id = ?1")
	public Page<Anuncios> getFiltroPorCategoria(Long categoria, Pageable pageable);
	
	//Filtro para somente estado
	@Query ("select a from Anuncios a where a.conservacao like %?1%")
	public Page<Anuncios> getFiltroPorEstado(String estado, Pageable pageable);
	
	//Filtro para somente preco
	@Query ("select a from Anuncios a where a.valor between ?1 and ?2")
	public Page<Anuncios> getFiltroPorPreco(Long valor1, Long valor2, Pageable pageable);
	
	
	
}
