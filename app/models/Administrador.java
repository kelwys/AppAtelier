package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.avaje.ebean.Model;

import play.data.validation.Constraints.Required;

@Entity
public class Administrador extends Model{
	@Id	
	@GeneratedValue
	public long id;
	@Required
	public String nome;
	@Required
	public String email;
	@Required
	public String password;
}
