package controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import models.Categoria;
import models.Produto;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;



public class Produtos extends Controller{
	
	
	private final Form<Produto> formProduto = Form.form(Produto.class);
	public Login loginStatus = new Login();
	List<Categoria> categorias = Categoria.find.all();
	
	public Result lista()
	{
		if(loginStatus.validaAcesso().equals("logado")){
			List<Produto> produtos = Produto.find.all();
			return ok(views.html.produtos.lista.render(produtos));
		}
		else{
			return unauthorized(views.html.site.account.render(categorias, "","/loginAdm"));
		}	
	}
	
	public Result novoProduto()
	{
		if(loginStatus.validaAcesso().equals("logado")){
			List<Categoria> categoria = Categoria.find.all();
			return ok(views.html.produtos.detalhes.render(formProduto,new Long(0), categoria));
		}
		else{
			return unauthorized(views.html.site.account.render(categorias, "","/loginAdm"));
		}	
	}
	
	public Result detalhes(long id)
	{
		Produto produto = Produto.find.byId(id);
		
		if(loginStatus.validaAcesso().equals("logado")){

			if (produto == null) {
			 return notFound(String.format("Produto %s não existe.", id));
			}
			Form<Produto> formPreenchido = formProduto.fill(produto);
			List<Categoria> categoria = Categoria.find.all();
	
			return ok(views.html.produtos.detalhes.render(formPreenchido, produto.id, categoria));
		}
		else{
			return unauthorized(views.html.site.account.render(categorias, "","/loginAdm"));
		}	
	}
	
	public Result detalhesProdutoSite(long id)
	{
			Produto produto = Produto.find.byId(id);
			List<Produto> produtos = new Produto().find.findPagedList(0, 3).getList();
			List<Categoria> categorias = Categoria.find.all();
			Form<Produto> formPreenchido = formProduto.fill(produto);
			return ok(views.html.site.single.render(categorias, "", produtos, produto.id, formPreenchido));
	}

	
	public Result salvar(Long id){

		if(loginStatus.validaAcesso().equals("logado")){
			Form<Produto> formEnviado = formProduto.bindFromRequest();
			Produto produto = formEnviado.get();
			Produto produtoOld = Produto.find.byId(id);
			produto.foto = imageUpload("imagem");
			if(produtoOld != null){
				produto.update();
			} else {
				produto.save();
			}
			
			flash("success", String.format("Salvo com sucesso!!!"));
			return redirect(routes.Produtos.lista());
		}
		else{
			return unauthorized(views.html.site.account.render(categorias, "","/loginAdm"));
		}	
	}
	
	public Result remover(long id)
	{
		Produto produto = new Produto();
		
		if(loginStatus.validaAcesso().equals("logado")){
		
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
		else{
			return unauthorized(views.html.site.account.render(categorias, "","/loginAdm"));
		}	
		
	}
	
	public String imageUpload(String param) {
    	MultipartFormData body = request().body().asMultipartFormData();
    	FilePart picture = body.getFile("foto");
    	String ret = "";
    	if(picture != null) {

    		String fileName = picture.getFilename();
    		String contentType = picture.getContentType();
    		File file = picture.getFile();
    		
    		String appDir = System.getProperty("user.dir");
    		
    		
    		String newPath = appDir + File.separator + "public" + File.separator + "images" + File.separator + "produtos" + File.separator + fileName;
    		ret = "images" + "\\" + "produtos" + "\\" + fileName;
    		File newFile = new File(newPath);
    		
    		try {
  	          Files.move(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
  	      } catch(IOException e) {
  	       e.printStackTrace();
  	      }
  	    }
  	     return ret;
  }
	
//	public Result mostraProdutoCategoria()
//	 {
//	  DynamicForm dynamicForm = Form.form().bindFromRequest();
//	  
//	  String input = dynamicForm.get("search");
//	  List<Categoria> categorias = Categoria.find.all();
//	  List<Produto> produtos = Produto.find
//	          .where()
//	          .like("descricao","%"+input+"%")
//	          .findList();
//	  return ok(views.html.site.products.render("Produto", categorias, produtos)); 
//	 }
	
	public Result FiltraPorCategoria(Long id)
	 {
	  List<Categoria> categorias = Categoria.find.all();
	  List<Produto> produtos = Produto.find.where().eq("Categoria_ID",id).findList();
	  return ok(views.html.site.products.render(categorias, "", produtos));
	 }


}
