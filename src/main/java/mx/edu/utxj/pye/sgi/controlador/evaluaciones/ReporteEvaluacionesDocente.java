package mx.edu.utxj.pye.sgi.controlador.evaluaciones;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.DtoReporteEvaluaciones;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ReporteEvaluacionesRolDocente;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AsignacionIndicadoresCriteriosRolDocente;
import mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbReporteResultadosEvaluaciones;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@Named
@RequestScoped
public class ReporteEvaluacionesDocente extends ViewScopedRol implements Desarrollable {
    @Getter @Setter ReporteEvaluacionesRolDocente rol;

    @EJB EjbPropiedades ep;
    @EJB EjbReporteResultadosEvaluaciones ejbReporte;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;
    @Getter private Boolean cargado = false;
    private String servidor = "localhost";
    @PostConstruct
    public void init(){
        try{
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.REPORTE_EVALUACIONES);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbReporte.validarDocente(logonMB.getPersonal().getClave());//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejbReporte.validarDocente(logonMB.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo docente = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new ReporteEvaluacionesRolDocente(filtro, docente);
            tieneAcceso = rol.tieneAcceso(docente);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            rol.setDocente(docente);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            getEvaluaciones();
        }catch (Exception e){mostrarExcepcion(e); }
    }

    public void getEvaluaciones(){
        try{
            ResultadoEJB<List<DtoReporteEvaluaciones>> resEv= ejbReporte.getEvaluacionesPack();
            if(resEv.getCorrecto()){
                rol.setListReporte(resEv.getValor());
                rol.getListReporte().forEach(l->{
                    //System.out.println("Tipo " + l.getEvaluacion().getTipo());
                });
            }else {mostrarMensajeResultadoEJB(resEv);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    public void getDto(DtoReporteEvaluaciones dto){
        rol.setDto(dto);
    }
    public StreamedContent getPdf() throws FileNotFoundException {
        String tipoEv= new String();
        tipoEv = Caster.tipoEvaluacionconverter(rol.getDto().getEvaluacion().getTipo());
        String requestServerName = FacesContext.getCurrentInstance().getExternalContext().getRequestServerName();
        if(requestServerName.equals(servidor)) servidor = "150.140.1.26";

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target( String.format("http://%s:8090/reporteEvaluacion/generar?idEvaluado=%s&idPeriodo=%s&idEvaluacion=%s", servidor, rol.getIdEvaluado().toString(), rol.getIdPeriodo().toString(),rol.getIdEvaluacion().toString()));

        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_OCTET_STREAM_TYPE);
        File entity = invocationBuilder.get(File.class);
        //return new DefaultStreamedContent(new FileInputStream(entity), "application/pdf", "evaluacion.pdf");

        return new DefaultStreamedContent(new FileInputStream(entity), "application/pdf", "Informe_Ev"+tipoEv+"_"+rol.getDto().getPeriodoEscolar().getMesInicio().getMes()+"_"+rol.getDto().getPeriodoEscolar().getMesFin().getMes()+"_"+rol.getDto().getPeriodoEscolar().getAnio()+".pdf");
    }

    public StreamedContent getPdf2() throws FileNotFoundException {
        String tipoEv= new String();
        tipoEv = Caster.tipoEvaluacionconverter(rol.getDto().getEvaluacion().getTipo());
        String requestServerName = FacesContext.getCurrentInstance().getExternalContext().getRequestServerName();
        if(requestServerName.equals(servidor)) servidor = "siip.utxj.edu.mx";

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target( String.format("http://%s:8090/reporteEvaluacion/generar?idEvaluado=%s&idPeriodo=%s&idEvaluacion=%s", servidor, rol.getIdEvaluado().toString(), rol.getIdPeriodo().toString(),rol.getIdEvaluacion().toString()));

        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_OCTET_STREAM_TYPE);
        File entity = invocationBuilder.get(File.class);
        //return new DefaultStreamedContent(new FileInputStream(entity), "application/pdf", "evaluacion.pdf");

        return new DefaultStreamedContent(new FileInputStream(entity), "application/pdf", "Informe_Ev"+tipoEv+"_"+rol.getDto().getPeriodoEscolar().getMesInicio().getMes()+"_"+rol.getDto().getPeriodoEscolar().getMesFin().getMes()+"_"+rol.getDto().getPeriodoEscolar().getAnio()+".pdf");
    }


    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "reporteEvaluaciones";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
}
