package controllers;

import java.util.List;

import javax.inject.Inject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import models.Administrador;
import play.data.Form;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

public class Administradores extends Controller{
private final Form<Administrador> formAdministrador = Form.form(Administrador.class);

	@Inject WSClient ws;
	
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
		AddAdministradoresWS(administrador.nome, administrador.email, administrador.password);
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
	
	public String baseSoap(String conteudo) {
    	String base = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
    			+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
    			+ "<soap:Body>"
    			+ conteudo
    			+ "</soap:Body>"
    			+ "</soap:Envelope>";
    	
    	return base;
    }
    
    public String xmlAddAdministradores(String nome, String email, String password) {
    	String requestAdministrador = "<incluirAdministrador xmlns=\"http://tempuri.org/\">"
					+ "<nome>"
					+ nome
					+ "</nome>"
					+ "<email>"
					+ email
					+"</email>"
					+ "<password>"
					+ password
					+ "</password>"
					+ "</incluirAdministrador>";
    	
    	return this.baseSoap(requestAdministrador);
    }
    
    public Promise<Result> AddAdministradoresWS(String nome, String email, String password){
    	
    	WSRequest requisicao = ws.url("http://localhost:64647/WebService.asmx?op=incluirAdministrador");    	
    	
    	String xmlRequisicao = this.xmlAddAdministradores(nome, email, password);
    	
    	Promise<WSResponse> promessaResposta = requisicao.setContentType("text/xml").post(xmlRequisicao); 
    	
    	Promise<Result> promessaResultado = promessaResposta.map(resposta -> {
    		System.out.println(resposta.getBody());
    		String str;
    		str ="";
    		processarXmlAdministradores(resposta.asXml());
    		return ok(str);
    	});
    	
    	return promessaResultado;
    }
    

	public String processarXmlAdministradores(Document documentoXml) {
    	
    	NodeList listaTables = documentoXml.getElementsByTagName("Table");
    	String consulta="";
    	for(int i = 0; i < listaTables.getLength(); i++) {
    		Element table = (Element) listaTables.item(i);
    		
    		String nome = table.getElementsByTagName("nome").item(0).getTextContent();
    		String email = table.getElementsByTagName("email").item(0).getTextContent();
    		String password = table.getElementsByTagName("password").item(0).getTextContent();
    		
    		
    		System.out.println(nome+"/"+email+"/"+"/"+password);
    		
    		consulta = (nome+"/"+email+"/"+"/"+password);
    		
    	}
    		return consulta;
    	
	}

}
