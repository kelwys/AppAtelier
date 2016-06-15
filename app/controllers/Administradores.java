package controllers;

import java.util.List;

import models.Administrador;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Administradores extends Controller{
private final Form<Administrador> formAdministrador = Form.form(Administrador.class);
	
	public Result lista()
	{
		List<Administrador> administradores = Administrador.find.all();
		return ok(views.html.administradores.lista.render(administradores));
	}
	
	public Result novoAdministrador()
	{
		List<Administrador> administrador = Administrador.find.all();
		return ok(views.html.administradores.detalhes.render(formAdministrador,new Long(0)));
	}
	
	public Result detalhes(long id)
	{
		Administrador administrador = Administrador.find.byId(id);

		if (administrador == null) {
		 return notFound(String.format("Administrador %s não existe.", id));
		}
		Form<Administrador> formPreenchido = formAdministrador.fill(administrador);

		return ok(views.html.administradores.detalhes.render(formPreenchido, administrador.id));
	}
	
	public Result salvar(Long id){

		Form<Administrador> formEnviado = formAdministrador.bindFromRequest();
		Administrador administrador = formEnviado.get();
		Administrador administradorOld = Administrador.find.byId(id);
		if(administradorOld != null){
			administrador.update();
		} else {
			administrador.save();
		}
		
		flash("success", String.format("Salvo com sucesso!!!"));
		return redirect(routes.Administradores.lista());
	}
	
	public Result remover(long id)
	{
		Administrador administrador = new Administrador();
		
		if (administrador == null) {
			 return notFound(String.format("Administrador %s não existe.", id));
			}
		else
		{
			
			administrador.find.ref(id).delete();
			flash("success", String.format("Administrador %s removido", administrador));
		}
		
		return redirect(routes.Administradores.lista());
		
	}
}
