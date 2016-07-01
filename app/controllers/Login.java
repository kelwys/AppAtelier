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
	
	public Result index(){	
		if(session("nomeSessao") == null){
			return ok(views.html.site.account.render(categorias,"/loginAdmin"));	
		}else{
			return ok(views.html.administradores.admin.render(session("nomeSessao")));
		}		
	}
	
	public Result loginCliente (){
		return ok(views.html.site.account.render(categorias,"/validaLoginCliente"));
	}
	
	public Result validaLoginCliente(){
		DynamicForm formEnviado = Form.form().bindFromRequest();
		String emailForm = formEnviado.get("email");
		String senhaForm = formEnviado.get("senha");
		List<Cliente> listaCliente = Cliente.find.where().eq("email", emailForm).eq("password", senhaForm).findList();

		if(listaCliente != null && listaCliente.size() > 0){			
			session("id", listaCliente.get(0).id.toString());
			session("nome", listaCliente.get(0).nome.toString());
			session("email", listaCliente.get(0).email.toString());	
			List<Produto> listaProdutos = new Produto().find.findPagedList(0, 9).getList();
			return ok (views.html.index.render(categorias, listaProdutos));		
		}
		else{
			return unauthorized(views.html.site.account.render(categorias,"/loginCliente"));
			
			//return notFound(String.format("Administrador %s não existe.", id));
			//flash("", String.format("Usuario ou senha incorreto"));
		}
	}
	
	public Result loginAdmin (){
		return ok(views.html.site.account.render(categorias,"/validaLoginAdmin"));
	}
	
	public Result validaLoginAdmin(){
		DynamicForm formEnviado = Form.form().bindFromRequest();
		String email = formEnviado.get("email");
		String senha = formEnviado.get("senha");
		List<Administrador> listaAdmin = Administrador.find.where().eq("email", email).eq("password", senha).findList();

		if(listaAdmin != null && listaAdmin.size() > 0){			
			session("id", listaAdmin.get(0).id.toString());
			session("nome", listaAdmin.get(0).nome.toString());
			session("email", listaAdmin.get(0).email.toString());			
			return ok (views.html.administradores.admin.render(""));		
		}
		else{
			return unauthorized(views.html.site.account.render(categorias,"/loginAdmin"));
		}
	}
	
	public String validaAcesso(){
		if (session("nomeSessao") != null){
			return "logado";
		}
		else{
			return "deslogado";
		}
	}

	
	public Result home(){
		String result = validaAcesso();
		if(session("nomeSessao") != null){
			//return ok(views.html.administradores.render("Administração", session("nomeSessao")));
		}
		return TODO;
	}
	
	public Result logout(){
		session().remove("nomeSessao");
		return ok(views.html.site.account.render(categorias,"/validaLoginAdmin"));
	}
	
	

}
