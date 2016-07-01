package controllers;

import java.util.List;

import models.Categoria;
import models.Produto;
import models.ItemPedido;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
	List<Categoria> categorias = Categoria.find.all();
	List<Produto> produtos = Produto.find.all();
	List<ItemPedido> itemPedidos = ItemPedido.find.all();
    public Result index() {
        return ok(index.render(categorias, produtos));
    }
    public Result account() {
        return ok(views.html.site.account.render(categorias));
        
    }
    public Result listaProdutos() {
        return ok(views.html.site.products.render(categorias, produtos));
        
    }
    public Result mostraCarrinho() {
        return ok(views.html.site.checkout.render(categorias, itemPedidos));
        
    }

}
