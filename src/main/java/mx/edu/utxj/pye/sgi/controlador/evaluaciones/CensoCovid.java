package mx.edu.utxj.pye.sgi.controlador.evaluaciones;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.omnifaces.cdi.ViewScoped;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

@Named(value = "censoCovid")
@ViewScoped
public class CensoCovid extends ViewScopedRol {
    @Inject private LogonMB logonMB;
    //@Getter @Setter List<> res = new ArrayList();
    @Getter  @Setter boolean terminado;
    @EJB EjbPersonalBean ejbPersonalBean;
    @Getter @Setter Boolean tieneAccesoT, tieneAccesoEs;
    private static final long serialVersionUID = 6285845014865471015L;

    @PostConstruct
    public void init(){
        validaAccesoReporteTrabajadores();
        validaAccesoReporteEstudiantes();
    }
    /**
     * Comprueba si ya ha concluido el censo
     * @param clave Clave del trabajdor
     * @return
     */
    public Boolean comprobarCensoTrabajador (Integer clave){
        try{
            //System.out.println("CensoCovid.comprobarCensoTrabajador");
            Client client = ClientBuilder.newClient().register(new JacksonFeature());
            //System.out.println("CensoCovid.comprobarCensoTrabajador");
            ArrayList res = client
                    .target("http://150.140.1.26:8080/sii2-ws-contingencia-censos/validarCensoColaboradorPorClave/"+clave)
                    .request(MediaType.APPLICATION_JSON).get(new GenericType<ArrayList>() {
                    });
            //System.out.println("CensoCovid.comprobarCensoTrabajador" + res);
            if(res.isEmpty()|| res ==null){
                //System.out.println("CensoCovid.comprobarCensoTrabajador false");
                return true;}
            else {
                //System.out.println("CensoCovid.comprobarCensoTrabajador true");
                return false;}

        }catch (Exception e){
            System.out.println("CensoCovid.comprobarCensoTrabajador" + e.getMessage());
            return false ;
        }

    }

    /**
     * Comprueba si el estudiante ha terminado el censo
     * @param matricula Matricula del estudiante
     * @return
     */
    public Boolean comprobarCensoEstudiante(String matricula){
        try{
            //System.out.println("CensoCovid.comprobarCensoEstudiante");
            Client client = ClientBuilder.newClient().register(new JacksonFeature());
            //System.out.println("CensoCovid.comprobarCensoEstudiante");
            ArrayList res = client
                    .target("http://150.140.1.26:8080/sii2-ws-contingencia-censos/validarCensoEstudiantePorClave/"+matricula)
                    .request(MediaType.APPLICATION_JSON).get(new GenericType<ArrayList>() {
                    });
            //System.out.println("CensoCovid.comprobarCensoEstudiante" + res);
            if(res.isEmpty()|| res ==null){
                // System.out.println("CensoCovid.comprobarCensoEstudiante true");
                return true;}
            else {
                //System.out.println("CensoCovid.comprobarCensoEstudiante false");
                return false;}
        }catch (Exception e){
            System.out.println("CensoCovid.comprobarCensoEstudiante" + e.getMessage());
            return false;
        }

    }

    public void validaAccesoReporteTrabajadores(){
        try{
            tieneAccesoT = Boolean.FALSE;
            PersonalActivo pa = ejbPersonalBean.pack(logonMB.getPersonal().getClave());
            //Valida que sea directivo
            if(pa.getPersonal().getCategoriaOperativa().getCategoria()==48 || pa.getPersonal().getCategoriaOperativa().getCategoria()==18){
                tieneAccesoT =true;
            }else {tieneAccesoT=false;}


            }catch (Exception e){mostrarExcepcion(e);}
    }

    public void  validaAccesoReporteEstudiantes(){
        try{
            tieneAccesoEs = Boolean.FALSE;
            PersonalActivo pa = ejbPersonalBean.pack(logonMB.getPersonal().getClave());
            //Valida Director de Area academica
            if(pa.getAreaSuperior().getArea()==2 & pa.getPersonal().getCategoriaOperativa().getCategoria()==18 || pa.getPersonal().getCategoriaOperativa().getCategoria()==48){
                tieneAccesoEs = true; }
            //Valida que sea director de planeación
            else if(pa.getAreaOperativa().getArea()==6  &pa.getAreaSuperior().getArea()==1 & pa.getPersonal().getCategoriaOperativa().getCategoria()==18 || pa.getPersonal().getCategoriaOperativa().getCategoria()==48){
              tieneAccesoEs = true; }
            //Valida que sea Rector
            else if(pa.getAreaOperativa().getArea()==1 & pa.getPersonal().getCategoriaOperativa().getCategoria() ==33){
                tieneAccesoEs= true; }
            //Valida que sea SA
            else  if(pa.getAreaOperativa().getArea()== 2 & pa.getPersonal().getCategoriaOperativa().getCategoria()==38 & pa.getAreaSuperior().getArea()==1){
              tieneAccesoEs = true; }
            else {tieneAccesoEs= false;}
            }catch (Exception e){mostrarExcepcion(e);}
    }



}
