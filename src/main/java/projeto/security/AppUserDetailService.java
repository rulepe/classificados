package projeto.security;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import projeto.classificados.model.Usuario;
import projeto.classificados.repository.UsuariosRepository;

@Service
public class AppUserDetailService implements UserDetailsService{

	@Autowired
	UsuariosRepository usuariosRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Usuario usuario = usuariosRepository.consultarPorEmail(email);
		
		if(usuario != null) {
			return new User(usuario.getEmail(), usuario.getSenha(), new HashSet<>());
		}
		
		return null;
	}

}
