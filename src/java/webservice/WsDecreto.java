package webservice;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dominio.Decreto;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import repository.DecretoRepository;
import service.DecretoService;
import util.HtmlToText;

/**
 *
 * @author rosivaldo.adm
 */
@Path("/v1")
@RequestScoped
public class WsDecreto implements Serializable {

    @Inject
    private DecretoRepository initRep;

    @Inject
    private DecretoService decretoService;
    
    Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

    //Recebe a notificação de um decreto do Boleto fácil
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("/notificacao")
    public void wsServerPostBoleto(@FormParam("paymentToken") String paymentToken) {
        System.out.println("paymentToken: " + paymentToken);
    }
    
    //Lista decretos
    @GET
    @Path("/decretos")
    @Produces({"application/json; charset=UTF-8"})
    public String resposta() {
        List<Decreto> decretos = decretoService.buscaUltimosDecretos();
        return gson.toJson(decretos);
    }

    //Busca decreto pelo número
    @GET
    @Path("/decreto/{numero}")
    @Produces({"application/json; charset=UTF-8"})
    public String confirmaDecreto(@PathParam("numero") String numero) throws Exception {
        HtmlToText htmlToText = new HtmlToText();
        Decreto decreto = decretoService.buscaDecretoPeloNumero(numero);
        decreto.setPessoa(null);
        htmlToText.parse(gson.toJson(decreto));
        return htmlToText.getText();
    }



}
