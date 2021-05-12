package mx.edu.utxj.pye.sgi.controlador.evaluaciones;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.omnifaces.cdi.ViewScoped;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

@Named(value = "censoCovid")
@ViewScoped
public class CensoCovid implements Serializable {
    @Inject private LogonMB logonMB;
    //@Getter @Setter List<> res = new ArrayList();
    @Getter  @Setter boolean terminado;
    private static final long serialVersionUID = 6285845014865471015L;


    /**
     * Comprueba si ya ha concluido el censo
     * @param clave Clave del trabajdor
     * @return
     */
    public Boolean comprobarCensoTrabajador (Integer clave){
        //System.out.println("CensoCovid.comprobarCensoTrabajador");
        Client client = ClientBuilder.newClient().register(new JacksonFeature());
        //System.out.println("CensoCovid.comprobarCensoTrabajador");
        ArrayList res = client
                .target("http://siip.utxj.edu.mx/sii2-ws-contingencia-censos/validarCensoColaboradorPorClave/"+clave)
                .request(MediaType.APPLICATION_JSON).get(new GenericType<ArrayList>() {
                });
        //System.out.println("CensoCovid.comprobarCensoTrabajador" + res);
        if(res.isEmpty()|| res ==null){
            //System.out.println("CensoCovid.comprobarCensoTrabajador false");
            return true;}
        else {
            //System.out.println("CensoCovid.comprobarCensoTrabajador true");
            return false;}
    }

    /**
     * Comprueba si el estudiante ha terminado el censo
     * @param matricula Matricula del estudiante
     * @return
     */
    public Boolean comprobarCensoEstudiante(String matricula){
        //System.out.println("CensoCovid.comprobarCensoEstudiante");
        Client client = ClientBuilder.newClient().register(new JacksonFeature());
        //System.out.println("CensoCovid.comprobarCensoEstudiante");
        ArrayList res = client
                .target("http://siip.utxj.edu.mx/sii2-ws-contingencia-censos/validarCensoEstudiantePorClave/"+matricula)
                .request(MediaType.APPLICATION_JSON).get(new GenericType<ArrayList>() {
                });
        //System.out.println("CensoCovid.comprobarCensoEstudiante" + res);
        if(res.isEmpty()|| res ==null){
           // System.out.println("CensoCovid.comprobarCensoEstudiante true");
            return true;}
        else {
           //System.out.println("CensoCovid.comprobarCensoEstudiante false");
            return false;}
    }



}
