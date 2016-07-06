package controllers;

import models.Administrador;
import models.Categoria;
import models.Cliente;
import models.Produto;
import java.util.List;

import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import play.data.Form;

public class Login  extends Controller {
	
	List<Categoria> categorias = Categoria.find.all();
	List<Administrador> administradors = Administrador.find.all();
	
	public Result index(){	
		if(session("nomeSessao") == null){
			return ok(views.html.site.account.render(categorias, "", "/loginCliente"));	
		}else{
			return ok(views.html.administradores.lista.render(administradors));
		}		
	}
	
	public Result loginCliente (){
		return ok(views.html.site.account.render(categorias, "", "/loginCliente"));
	}
	
	public Result validaLoginCliente(){
		DynamicForm formEnviado = Form.form().bindFromRequest();
		String emailForm = formEnviado.get("email");
		String senhaForm = formEnviado.get("senha");
		List<Cliente> listaCliente = Cliente.find.where().eq("email", emailForm).eq("password", senhaForm).findList();

		if(listaCliente != null && listaCliente.size() > 0){	
			session("nomeSessao", listaCliente.get(0).nome.toString());
			String nome = session("nomeSessao");
			session("nome", listaCliente.get(0).nome.toString());
			session("email", listaCliente.get(0).email.toString());	
			session("perfil", listaCliente.get(0).perfil.toString());
			List<Produto> listaProdutos = new Produto().find.findPagedList(0, 9).getList();
			return ok (views.html.index.render(categorias,session("nomeSessao"), listaProdutos));		
		}
		else{
			return unauthorized(views.html.site.account.render(categorias, "", "/loginCliente"));
		}
	}
	
	public Result validaLoginAdm(){
		DynamicForm formEnviado = Form.form().bindFromRequest();
		String email = formEnviado.get("email");
		String senha = formEnviado.get("senha");
		List<Administrador> listaAdm = Administrador.find.where().eq("email", email).eq("password", senha).findList();
		
		if(listaAdm != null && listaAdm.size() > 0){			
			session("nomeSessao", listaAdm.get(0).nome.toString());
			String nome = session("nomeSessao");
			session("nome", listaAdm.get(0).nome.toString());
			session("email", listaAdm.get(0).email.toString());	
			session("perfil", listaAdm.get(0).perfil.toString());
			List<Produto> listaProdutos = new Produto().find.findPagedList(0, 9).getList();
			return ok (views.html.administradores.admin.render(session("nomeSessao")));	
		}
		else{
			return unauthorized(views.html.site.account.render(categorias, "", "/loginAdm"));
		}
	}
	
	public String validaAcesso(){
		String perfil = session("perfil");
		
		if (session("nomeSessao") != null && perfil.toString().equals("0")){
			return "logado";
		}
		else{
			return "deslogado";
		}
	}
	
	public Result logout(){
		session().remove("nomeSessao");
		session().remove("nome");
		session().remove("email");
		session().remove("perfil");
		return ok(views.html.site.account.render(categorias, "", "/loginCliente"));
	}
	
	

}
