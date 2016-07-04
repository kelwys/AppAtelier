package controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import models.Categoria;
import models.Cliente;
import play.data.DynamicForm;
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
	
	public String cpf = "";
	
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

		//Form<Cliente> formEnviado = formCliente.bindFromRequest();
		
		DynamicForm formEnviado = Form.form().bindFromRequest();
		Cliente cliente = new Cliente();
		Cliente clienteOld = Cliente.find.byId(id);
		DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate data = LocalDate.parse(formEnviado.get("datanascimento"), formatoData);
		
		wsValidaCPF(cliente.cpf);
				
		cliente.datanascimento = data;
		cliente.nome = formEnviado.get("nome");
		cliente.cpf = formEnviado.get("cpf");
		cliente.email = formEnviado.get("email");
		
		cliente.nome = formEnviado.get("nome");
		cliente.cpf = formEnviado.get("cpf");
		cliente.telefone = formEnviado.get("telefone");
		cliente.endereco = formEnviado.get("endereco");
		cliente.numero = Integer.parseInt(formEnviado.get("numero"));
		cliente.cep = formEnviado.get("cep");
		cliente.complemento = formEnviado.get("complemento");
		cliente.email = formEnviado.get(" email");
		cliente.password = formEnviado.get("password");
		cliente.datanascimento = data;
		cliente.sexo = formEnviado.get("sexo");
		
		
		
		
        
		
		AddClienteWS(cliente.nome, cliente.cpf, cliente.telefone, cliente.endereco, cliente.numero, cliente.cep, cliente.complemento, cliente.email, cliente.password, cliente.datanascimento, cliente.sexo, cliente.perfil);
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
	
	public String xmlValidaCPF(String cpf) {
    	String requestCliente = "<ValidarCPF xmlns=\"http://tempuri.org/\">"   			
					+"<cpf>"
    				+cpf
    				+"</cpf>"
				    + "</ValidarCPF>";
    	
    	return this.baseSoap(requestCliente);
    }
	
	public String processarXmlClientes(Document documentoXml) {
    	
    	NodeList listaTables = documentoXml.getElementsByTagName("Table");
    	String consulta="";
    	for(int i = 0; i < listaTables.getLength(); i++) {
    		Element table = (Element) listaTables.item(i);
    		    		
    	}
    		return consulta;
    	
	}
	
	public String processarXmlCpf(Document documentoXml) {
		System.out.println("Passei aqui2"); 
		String str = "";
			
		NodeList listaTables = documentoXml.getElementsByTagName("cpf");
		
		/*for (int i = 0; i < listaTables.getLength(); i++) {
			Element table = (Element) listaTables.item(i);

			str = table.getElementsByTagName("cpf").item(0).getTextContent();

			System.out.println(str);

		}*/
		return cpf.toString(); 
		}
	
    public String xmlAddClientes(String nome, String cpf, String telefone, String endereco, int numero, String cep, String complemento, String email, String password, LocalDate datanascimento, String sexo, int perfil) {
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
					+ "<datanascimento>"
					+ datanascimento
			    	+ "</datanascimento>"
			        + "<sexo>"
			        + sexo
			        + " </sexo>"
			        + "<perfil>"
			        + perfil
			        + " </perfil>"
			       	+ "</incluirCliente>";
    	
    	return this.baseSoap(requestCliente);
    }
    
    
    public Promise<Result> AddClienteWS(String nome, String cpf, String telefone, String endereco, int numero, String cep, String complemento, String email, String password, LocalDate datanascimento, String sexo, int perfil){
    	
    	WSRequest requisicao = ws.url("http://localhost:64647/WebService.asmx?op=incluirCliente");    	
    	
    	String xmlRequisicao = this.xmlAddClientes(nome, cpf, telefone, endereco, numero, cep, complemento, email, password, datanascimento, sexo, perfil);
    	
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
    


	public Promise<Result> wsValidaCPF(String cpf) {
		WSRequest requisicao = ws.url("http://localhost:64647/WebService.asmx?op=ValidarCPF"); 
		
		String xmlRequisicao = this.xmlValidaCPF(cpf);
		
		System.out.println("Passei aqui");
		System.out.println(xmlRequisicao);

		Promise<WSResponse> promessaResposta = requisicao.setContentType("text/xml").post(xmlRequisicao);

		 Promise<Result> promessaResultado = promessaResposta.map(resposta -> {	
			System.out.println("Passei de novo aqui");
			String str;
			processarXmlCpf(resposta.asXml());
			str = processarXmlCpf(resposta.asXml());
			return ok(str);
		});
		
		//CompletionStage<Document> documentPromise = ws.url(url).get().thenApply(WSResponse::asXml);
		
		return promessaResultado;
	}
	

		
	
	
}
