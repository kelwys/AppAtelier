package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;

import play.data.validation.Constraints.Required;

@Entity
public class Pedido extends Model{
	
	@Id	
	@GeneratedValue
	public long id;
	@ManyToOne
    public Cliente cliente;
	@Required
	public Date dataPedido;
	@Required
	public double valorTotal;
	@Required
	public int status;
	@OneToMany(mappedBy="pedido")
	public List<ItemPedido> itens = new ArrayList<>();
 
}
