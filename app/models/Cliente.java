package models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import play.data.validation.Constraints.Required;

@Entity
public class Cliente extends Model{

	@Id	
	@GeneratedValue
	public Long id;
	@Required
	public String nome;
	@Required
	public String cpf;
	@Required
	public String telefone;
	@Required
	public String endereco;
	@Required
	public int numero;
	@Required
	public String cep;
	@Required
	public String complemento;
	@Required
	public String email;
	@Required
	public String password;
	public LocalDate datanascimento;
	@Required
	public String sexo;
	
	public Integer perfil = 1; // 1 = Admin
	
	public static Finder<Long, Cliente> find = new Finder<Long,Cliente>(Cliente.class);
	
	@OneToMany(mappedBy="cliente")
	public List<Pedido> pedidos = new ArrayList<>();
}
