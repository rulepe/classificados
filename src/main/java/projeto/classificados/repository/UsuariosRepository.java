package projeto.classificados.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import projeto.classificados.model.Usuario;

@Repository
public interface UsuariosRepository extends CrudRepository<Usuario, Long>{

	@Query("select u from Usuario u where lower(u.email) = ?1")
	public Usuario consultarPorEmail (String email);
	
}
