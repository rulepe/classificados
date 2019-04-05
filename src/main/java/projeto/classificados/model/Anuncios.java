package projeto.classificados.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Anuncios implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(columnDefinition="text")
	private String foto;
	
	/*gravar arquivos no banco de dados*/
	@Basic(fetch = FetchType.LAZY)/*configura pra carregar só quando for chamado*/
	private byte[] fotoBase64;
	
	@Column(name="content_type")
	private String contentType;
	
	@NotBlank (message = "Preencha o titulo.")
	private String titulo;
	
	@NotNull(message = "Preencha o valor.")
	private Long valor;
	
	@NotBlank (message = "Escolha um estado de conservação.")
	private String conservacao;
	
	@NotBlank (message = "Preencha a descrição.")
	private String descricao;

	private String tempFoto;
	
	public String getTempFoto() {
		
		tempFoto = "data:" + getContentType()+";base64," + foto;
		
		return tempFoto;
	}
	
	//identificar de quem é esse anuncio, amarrar a outra classe
	//Muitos anuncios para um usuario (ManyToOne)
		
	@ManyToOne
	@JoinColumn(name= "usuario_id")
	private Usuario usuario;	
	
	@NotNull (message= "Escolha uma categoria")
	@ManyToOne
	@JoinColumn(name= "categoria_id")
	private Categorias categoria;
		
	public byte[] getFotoBase64() {
		return fotoBase64;
	}

	public void setFotoBase64(byte[] fotoBase64) {
		this.fotoBase64 = fotoBase64;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Long getValor() {
		return valor;
	}

	public void setValor(Long valor) {
		this.valor = valor;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Categorias getCategoria() {
		return categoria;
	}

	public void setCategoria(Categorias categoria) {
		this.categoria = categoria;
	}

	public String getConservacao() {
		return conservacao;
	}

	public void setConservacao(String conservacao) {
		this.conservacao = conservacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Anuncios other = (Anuncios) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
