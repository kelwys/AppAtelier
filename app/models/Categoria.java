package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import play.data.validation.Constraints.Required;

@Entity
public class Categoria extends Model {
	
	@Id
	@GeneratedValue
	public Long id;

	@Required
	public String descricao;
	
	@OneToMany(mappedBy="categoria")
	public List<Produto> produtos = new ArrayList<>();

	public String toString(){
		return descricao;
	}
	
	public static Finder<Long, Categoria> find = new Finder<Long,Categoria>(Categoria.class);

}
