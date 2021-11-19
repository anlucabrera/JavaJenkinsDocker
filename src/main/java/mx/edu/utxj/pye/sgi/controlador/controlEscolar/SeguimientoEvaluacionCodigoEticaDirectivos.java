/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.SeguimientoEvaluacionEticaRolDirectivos;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoEvaluacionEticaDirectivos;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionConocimientoCodigoEticaResultados2;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import mx.edu.utxj.pye.sgi.util.GenerateExcel;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

/**
 *
 * @author Planeacion
 */
@Named
@ViewScoped
public class SeguimientoEvaluacionCodigoEticaDirectivos extends ViewScopedRol implements Desarrollable{
    @Getter @Setter SeguimientoEvaluacionEticaRolDirectivos rol;
    @Getter private Boolean cargado = false;
    @Getter private Boolean tieneAcceso = false;
    
    @EJB EjbPropiedades ep;
    @EJB EjbSeguimientoEvaluacionEticaDirectivos ejb;
    @Inject LogonMB logonMB;
    @Inject GenerateExcel gExcel;
    
    @PostConstruct
    public void init(){
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try {
            setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_EVALUACION_ETICA);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarDirectivo(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarDirectivo(logonMB.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo directivo = filtro.getEntity();//            ejbPersonalBean.pack(logonMB.getPersonal());
            rol = new SeguimientoEvaluacionEticaRolDirectivos(filtro, directivo, directivo.getAreaOperativa());
            tieneAcceso = rol.tieneAcceso(directivo);
            System.out.println("Tiene acceso:"+ tieneAcceso);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setDirectivo(directivo);
            System.out.println("Rol directivo asignado");
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.CONSULTA);
            obtenerEvaluacionCompleto();
            obtenerEvaluacionIncompleto();
            obtenerAvance();
            
            rol.setNombreExcel("plantilla_resultados.xlsx");
            rol.setCarpeta("resultados_evaluacion");
        }catch (Exception e){mostrarExcepcion(e); }
    }
    
    public void obtenerEvaluacionCompleto(){
        ResultadoEJB<List<EvaluacionConocimientoCodigoEticaResultados2>> resList = ejb.obtenerCompletos(rol.getDirectivo().getAreaOperativa().getArea());
        rol.setLista(resList.getValor());
        if(rol.getLista().isEmpty()){
            mostrarMensajeError("El personal a su cargo no ha realizado la evaluacion");
        }
    }
    
    public void obtenerEvaluacionIncompleto(){
        ResultadoEJB<List<Personal>> resList = ejb.obtenerIncompletos(rol.getDirectivo().getAreaOperativa().getArea());
        rol.setListaIn(resList.getValor());
    }
    
    public String obtenerAreaPersonal(Short area){
        ResultadoEJB<AreasUniversidad> resAU = ejb.obtenerAreaOp(area);
        if(!resAU.getCorrecto()) return "";
        return resAU.getValor().getNombre();
    }
    
    public void obtenerAvance(){
        Integer totalPart = rol.getLista().size() + rol.getListaIn().size();
        Double porcentaje = (double) (rol.getLista().size() * 100) / totalPart.doubleValue();
        rol.setPorcentaje(porcentaje);
    }
    
    public void generateResultadosTestCompleto()throws IOException{
        gExcel.obtenerLibro(rol.getNombreExcel());
        rol.setExcelSalida("resultados_evaluacion_ut.xlsx");
        rol.setSubCarpeta("resultados_evaluacion");
        ResultadoEJB<List<EvaluacionConocimientoCodigoEticaResultados2>> lista = ejb.obtenerResultados();
        rol.setListaResultados(lista.getValor());
            rol.setLibro(gExcel.getLibro().getSheetAt(0).getSheetName());
            for (int i = 0; i <= rol.getListaResultados().size() - 1; i++) {
            EvaluacionConocimientoCodigoEticaResultados2 dto = rol.getListaResultados().get(i);
            String[] datos = {
                    dto.getPersonal().getClave().toString(), dto.getPersonal().getNombre(), dto.getPersonal().getGenero().getAbreviacion().toString(),
                            ejb.obtenerAreaOp(dto.getPersonal().getAreaOperativa()).getValor().getNombre(), 
                            dto.getR1(), dto.getR2(), dto.getR3(), dto.getR4(), dto.getR5(), dto.getR6(), dto.getR7(), dto.getR8(), dto.getR9(), dto.getR10(), dto.getR11(),
                            dto.getR12()};
            gExcel.escribirDatosExcel(datos, i, 1, rol.getLibro());
            }
        
        //excel.eliminarLibros(dto.excel);
        //dto.dtoEXANI = ejbExani.getResultadosExani();
        
        
        gExcel.escribirLibro(rol.getCarpeta(), rol.getSubCarpeta(), rol.getExcelSalida());
        String ruta = gExcel.enviarLibro();
        Ajax.oncomplete("descargar('" + gExcel.enviarLibro() + "');");
    }
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "seguimiento evaluacion etica";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
}
