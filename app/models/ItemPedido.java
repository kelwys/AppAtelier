package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import play.data.validation.Constraints.Required;

@Entity
public class ItemPedido extends Model{
	
	@Id	
	@GeneratedValue
	public long id;
	@ManyToOne
	public Pedido pedido;
	@ManyToOne
	public Produto produto;
	@Required
	public int quantidade;
	@Required
	public double precoUnitario;
	@Required
	public double precoTotal;
	
	public static Finder<Long, ItemPedido> find = new Finder<Long,ItemPedido>(ItemPedido.class);

}
