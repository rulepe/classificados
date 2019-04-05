package projeto.classificados.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import projeto.classificados.model.Categorias;

@Repository
public interface CategoriasRepository extends CrudRepository<Categorias, Long>{

}
