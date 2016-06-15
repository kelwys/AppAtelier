package controllers;

import java.util.List;
import models.Cliente;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Clientes extends Controller {
	
private final Form<Cliente> formCliente = Form.form(Cliente.class);
	
	public Result lista()
	{
		List<Cliente> clientes = Cliente.find.all();
		return ok(views.html.clientes.lista.render(clientes));
	}
	
	public Result novoCliente()
	{
		List<Cliente> cliente = Cliente.find.all();
		return ok(views.html.clientes.detalhes.render(formCliente,new Long(0)));
	}
	
	public Result detalhes(long id)
	{
		Cliente cliente = Cliente.find.byId(id);

		if (cliente == null) {
		 return notFound(String.format("Cliente %s não existe.", id));
		}
		Form<Cliente> formPreenchido = formCliente.fill(cliente);

		return ok(views.html.clientes.detalhes.render(formPreenchido, cliente.id));
	}
	
	public Result salvar(Long id){

		Form<Cliente> formEnviado = formCliente.bindFromRequest();
		Cliente cliente = formEnviado.get();
		Cliente clienteOld = Cliente.find.byId(id);
		if(clienteOld != null){
			cliente.update();
		} else {
			cliente.save();
		}
		
		flash("success", String.format("Salvo com sucesso!!!"));
		return redirect(routes.Clientes.lista());
	}
	
	public Result remover(long id)
	{
		Cliente cliente = new Cliente();
		
		if (cliente == null) {
			 return notFound(String.format("Cliente %s não existe.", id));
			}
		else
		{
			
			cliente.find.ref(id).delete();
			flash("success", String.format("Cliente %s removido", cliente));
		}
		
		return redirect(routes.Clientes.lista());
		
	}


}
