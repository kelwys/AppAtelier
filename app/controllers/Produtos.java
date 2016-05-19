package controllers;

import java.util.List;

import models.Categoria;
import models.Produto;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;



public class Produtos extends Controller{
	
	
	private final Form<Produto> formProduto = Form.form(Produto.class);
	
	public Result lista()
	{
		List<Produto> produtos = Produto.find.all();
		return ok(views.html.produtos.lista.render(produtos));
	}
	
	public Result novoProduto()
	{
		List<Categoria> categoria = Categoria.find.all();
		return ok(views.html.produtos.detalhes.render(formProduto,new Long(0), categoria));
	}
	
	public Result detalhes(long id)
	{
		Produto produto = Produto.find.byId(id);

		if (produto == null) {
		 return notFound(String.format("Produto %s não existe.", id));
		}
		Form<Produto> formPreenchido = formProduto.fill(produto);
		List<Categoria> categoria = Categoria.find.all();

		return ok(views.html.produtos.detalhes.render(formPreenchido, produto.id, categoria));
	}

	
	public Result salvar(Long id){

		Form<Produto> formEnviado = formProduto.bindFromRequest();
		System.out.println(formEnviado);
		Produto produto = formEnviado.get();
		System.out.println(produto);
		Produto produtoOld = Produto.find.byId(id);
		if(produtoOld != null){
			produto.update();
		} else {
			produto.save();
		}
		
		flash("success", String.format("Salvo com sucesso!!!"));
		return redirect(routes.Produtos.lista());
	}
	
	public Result remover(long id)
	{
		Produto produto = new Produto();
		
		if (produto == null) {
			 return notFound(String.format("Produto %s não existe.", id));
			}
		else
		{
			
			produto.find.ref(id).delete();
			flash("success", String.format("Produto %s removido", produto));
		}
		
		return redirect(routes.Produtos.lista());
		
	}
	
//	public Result updateList(Long id)
//	{
//		Produto produto = Produto.find.byId(id);
//
//		if (produto == null) {
//		 return notFound(String.format("Produto %s não existe.", id));
//		}
//
//		Form<Produto> formPreenchido = formProduto.fill(produto);
//
//		return ok(views.html.produtos.update.render(id,formPreenchido));
//	}
//	
//	@Transactional
//	public Result update(long id)
//	
//	{
//		Produto produto = Produto.find.byId(id);
//		
//		DynamicForm reqData = Form.form().bindFromRequest();
//		produto.codigoBarras = reqData.get("codigoBarras");
//		produto.nome = reqData.get("nome");
//		produto.descricao = reqData.get("descricao");
//		
//		produto.save();
//		return redirect(routes.Produtos.lista());
//	}
	
//public Result teste()
//	
//	{
//		Produto produto = Produto.find.byId(1L);
//		
//		
//		produto.setCodigoBarras("0000000");
//		
//		produto.save();
//		return ok("ok");
//	}

}
