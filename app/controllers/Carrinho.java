package controllers;

import java.util.List;
import java.util.ArrayList;
import play.mvc.Result;
import models.Categoria;
import models.Pedido;
import models.ItemPedido;
import models.Produto;
import play.mvc.Controller;
import play.api.libs.json.Reads;
import org.json.*;
import org.json.JSONException;
import play.data.Form;
import play.mvc.Result;



public class Carrinho extends Controller{
	public  Result listaCarrinho() {
    	String carrinho = session("carrinho");
    	List<ItemPedido> listaItens = new ArrayList<ItemPedido>();
    	List<Categoria> categoria = Categoria.find.all();
    	try
    	{
			if(carrinho != null) {
		    	JSONArray jsa = new JSONArray(carrinho);
	    		for(int i = 0; i < jsa.length(); i++) {
		    		JSONObject jsoItem = jsa.getJSONObject(i);
		    		ItemPedido item = new ItemPedido();
		    		item.produto =  Produto.find.byId(jsoItem.getLong("idProduto"));
		    		item.quantidade =  jsoItem.getInt("quantidade");
		    		item.precoTotal = (item.produto.preco * item.quantidade);
		    		listaItens.add(item);
		    	}
		   }
		  return ok(views.html.site.checkout.render(categoria, "", listaItens));
	 	} catch (JSONException e) {
	 		return ok("404");
	    }  	
    }
    public Result addItemAoCarrinho(Long idProduto) {
    	String carrinho = session("carrinho");
    	try
    	{
		    	if(carrinho == null) {
		    		carrinho = "[]"; //Recebe string JSON de array vazio
		    	}
		    	boolean aux = false;
		    	JSONArray jsa = new JSONArray(carrinho);
		    	loop : for(int i = 0; i < jsa.length(); i++) {
		    		if (jsa.getJSONObject(i).getLong("idProduto") == idProduto){
		    			JSONObject obj =  jsa.getJSONObject(i);
		    			Long qty = obj.getLong("quantidade") + 1;	
		    			obj.put("quantidade",qty);
		    			session("carrinho",jsa.toString());
		    			return listaCarrinho();
		    		}
		    	}
		    	if (aux != true){
		    		JSONObject jsoNovoProduto = new JSONObject();
		    		jsoNovoProduto.put("idProduto", idProduto);
		        	jsoNovoProduto.put("quantidade", 1);
		    		jsa.put(jsoNovoProduto);
		    	}
		    	session("carrinho", jsa.toString());
		    	return listaCarrinho();
    	} catch (JSONException e) {
    		return ok("404");
    	}
    }
    public Result remItemDoCarrinho(Long idProduto) {
    	String carrinho = session("carrinho");
    	try
    	{
	    	JSONArray jsa = new JSONArray(carrinho);
		    	
		    for(int i = 0; i < jsa.length(); i++) {
		    	JSONObject jsoItem = jsa.getJSONObject(i);
		    		
		    	if(jsoItem.getLong("idProduto") == idProduto){
		    		jsa.remove(i);
		    	}
		    }
		    session("carrinho", jsa.toString());
		    return listaCarrinho();
    	} catch (JSONException e) {
    		return ok("404");
    	}
    }
}