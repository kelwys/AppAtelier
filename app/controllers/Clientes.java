package controllers;

import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import models.Categoria;
import models.Cliente;
import play.data.Form;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

public class Clientes extends Controller {
	
private final Form<Cliente> formCliente = Form.form(Cliente.class);

	@Inject WSClient ws;
	
	public Result lista()
	{
		List<Cliente> clientes = Cliente.find.all();
		return ok(views.html.clientes.lista.render(clientes));
	}
	
	public Result novoCliente()
	{
		List<Categoria> categorias = Categoria.find.all();
		List<Cliente> cliente = Cliente.find.all();
		return ok(views.html.site.register.render(formCliente,new Long(0), categorias));
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
		AddClienteWS(cliente.nome, cliente.cpf, cliente.telefone, cliente.endereco, cliente.numero, cliente.cep, cliente.complemento, cliente.email, cliente.password, cliente.dataNascimento, cliente.sexo);
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
	
	public String baseSoap(String conteudo) {
    	String base = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
    			+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
    			+ "<soap:Body>"
    			+ conteudo
    			+ "</soap:Body>"
    			+ "</soap:Envelope>";
    	
    	return base;
    }
	
    public String xmlAddClientes(String nome, String cpf, String telefone, String endereco, int numero, String cep, String complemento, String email, String password, Date data_nascimento, String sexo) {
    	String requestCliente = "<incluirCliente xmlns=\"http://tempuri.org/\">"
					+ "<nome>"
					+ nome
					+ "</nome>"
					+"<cpf>"
    				+cpf
    				+"</cpf>"
				    +"<telefone>"
				    +telefone
				    +"</telefone>"
				    +"<endereco>"
				    +endereco
				    +"</endereco>"
				    +"<numero>"
				    +numero
				    +"</numero>"
				    +"<cep>"
				    +cep
				    +"</cep>"
				    +"<complemento>"
				    +complemento
				    +"</complemento>"
					+ "<email>"
					+ email
					+"</email>"
					+ "<password>"
					+ password
					+ "</password>"
					+ "<data_nascimento>"
					+ data_nascimento
			    	+ "</data_nascimento>"
			        + "<sexo>"
			        + sexo
			        + " </sexo>"
					+ "</incluirCliente>";
    	
    	return this.baseSoap(requestCliente);
    }
    
    public Promise<Result> AddClienteWS(String nome, String cpf, String telefone, String endereco, int numero, String cep, String complemento, String email, String password, Date data_nascimento, String sexo){
    	
    	WSRequest requisicao = ws.url("http://localhost:64647/WebService.asmx?op=incluirCliente");    	
    	
    	String xmlRequisicao = this.xmlAddClientes(nome, cpf, telefone, endereco, numero, cep, complemento, email, password, data_nascimento, sexo);
    	
    	Promise<WSResponse> promessaResposta = requisicao.setContentType("text/xml").post(xmlRequisicao); 
    	
    	Promise<Result> promessaResultado = promessaResposta.map(resposta -> {
    		System.out.println(resposta.getBody());
    		String str;
    		str ="";
    		processarXmlClientes(resposta.asXml());
    		return ok(str);
    	});
    	
    	return promessaResultado;
    }
    

	public String processarXmlClientes(Document documentoXml) {
    	
    	NodeList listaTables = documentoXml.getElementsByTagName("Table");
    	String consulta="";
    	for(int i = 0; i < listaTables.getLength(); i++) {
    		Element table = (Element) listaTables.item(i);
    		
    		//nome, cpf, telefone, endereco, numero, cep, complemento, email, password, data_nascimento, sexo
    		String nome = table.getElementsByTagName("nome").item(0).getTextContent();
    		String cpf = table.getElementsByTagName("cpf").item(0).getTextContent();
    		String telefone = table.getElementsByTagName("telefone").item(0).getTextContent();
    		String endereco = table.getElementsByTagName("endereco").item(0).getTextContent();
    		String numero = table.getElementsByTagName("numero").item(0).getTextContent();
    		String cep = table.getElementsByTagName("cep").item(0).getTextContent();
    		String complemento = table.getElementsByTagName("complemento").item(0).getTextContent();
    		String email = table.getElementsByTagName("email").item(0).getTextContent();
    		String password = table.getElementsByTagName("password").item(0).getTextContent();
    		String data_nascimento = table.getElementsByTagName("data_nascimento").item(0).getTextContent();
    		String sexo = table.getElementsByTagName("sexo").item(0).getTextContent();
    		
    		System.out.println(nome+"/"+"/"+cpf+"/"+telefone+"/"+endereco+"/"+numero+"/"+cep+"/"+complemento+"/"+email+"/"+password+"/"+data_nascimento+"/"+sexo);
    		
    		consulta = (nome+"/"+"/"+cpf+"/"+telefone+"/"+endereco+"/"+numero+"/"+cep+"/"+complemento+"/"+email+"/"+password+"/"+data_nascimento+"/"+sexo);
    		
    	}
    		return consulta;
    	
	}


}
