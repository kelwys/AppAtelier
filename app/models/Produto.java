package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import play.data.validation.Constraints.Required;

@Entity
public class Produto extends Model{
	
	@Id	
	@GeneratedValue
	public long id;
	
	
	public String codigoBarras;
	
	public String nome;
	
	public String descricao;
	
	@ManyToOne
    public Categoria categoria;
	
	public byte[] foto;
	
	public double preco;
	
	public long quantidadeEstoque;
	
	@OneToMany(mappedBy="produto")
	public List<ItemPedido> itens = new ArrayList<>();

	public static Finder<Long, Produto> find = new Finder<Long,Produto>(Produto.class);
}
