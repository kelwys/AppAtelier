package controllers;

import java.util.List;

import models.Categoria;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Categorias extends Controller{
private final Form<Categoria> formCategoria = Form.form(Categoria.class);
	
	public Result lista()
	{
		List<Categoria> categorias = Categoria.find.all();
		return ok(views.html.categorias.lista.render(categorias));
	}
	
	public Result novoCategoria()
	{
		List<Categoria> categoria = Categoria.find.all();
		return ok(views.html.categorias.detalhes.render(formCategoria,new Long(0)));
	}
	
	public Result detalhes(long id)
	{
		Categoria categoria = Categoria.find.byId(id);

		if (categoria == null) {
		 return notFound(String.format("Categoria %s não existe.", id));
		}
		Form<Categoria> formPreenchido = formCategoria.fill(categoria);

		return ok(views.html.categorias.detalhes.render(formPreenchido, categoria.id));
	}
	
	public Result salvar(Long id){

		Form<Categoria> formEnviado = formCategoria.bindFromRequest();
		Categoria categoria = formEnviado.get();
		Categoria categoriaOld = Categoria.find.byId(id);
		if(categoriaOld != null){
			categoria.update();
		} else {
			categoria.save();
		}
		
		flash("success", String.format("Salvo com sucesso!!!"));
		return redirect(routes.Categorias.lista());
	}
	
	public Result remover(long id)
	{
		Categoria categoria = new Categoria();
		
		if (categoria == null) {
			 return notFound(String.format("Categoria %s não existe.", id));
			}
		else
		{
			
			categoria.find.ref(id).delete();
			flash("success", String.format("Categoria %s removido", categoria));
		}
		
		return redirect(routes.Categorias.lista());
		
	}
}
