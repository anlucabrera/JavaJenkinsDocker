/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDatosEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.LetreroFotografiasRolEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbLetreroFotografias;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionRolesEstadia;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class LetreroFotografiasEstudiante extends ViewScopedRol implements Desarrollable{
    @Getter @Setter private LetreroFotografiasRolEstudiante rol = new LetreroFotografiasRolEstudiante();
    
    @EJB EjbLetreroFotografias ejb;
    @EJB EjbSeguimientoEstadia ejbSeguimientoEstadia;
    @EJB EjbAsignacionRolesEstadia ejbAsignacionRolesEstadia;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por dirección de carrera<br/>
     *      El DTO del rol<br/>
     *      El periodo activo<br/>
     *      Las instrucciones de operación de la herramienta<br/>
     */
    


@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;

@PostConstruct
    public void init(){
        try {
            if(logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)){
                cargado = true;
                setVistaControlador(ControlEscolarVistaControlador.LETRERO_FOTOGRAFIAS);
                ResultadoEJB<Estudiante> resAcceso = ejb.validarEstudianteFotografias(Integer.parseInt(logonMB.getCurrentUser()));
                if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
                ResultadoEJB<Estudiante> resValidacion = ejb.validarEstudianteFotografias(Integer.parseInt(logonMB.getCurrentUser()));
                if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
                
                Estudiante estudiante = resValidacion.getValor();
                tieneAcceso = rol.tieneAcceso(estudiante, UsuarioTipo.ESTUDIANTE19);
                if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

                rol.setEstudiante(estudiante);
                //////////////////////////////////////////////////////////////////////////////////////////////////////////
                if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
                if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu
                if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

                rol.setNivelRol(NivelRol.OPERATIVO);
                
                rol.getInstrucciones().add("Seleccione generación.");
                rol.getInstrucciones().add("Seleccione nivel educativo.");
                rol.getInstrucciones().add("Por cuestión de diseño de los formatos, se recomienda utilizar el navegador Google Chrome");
                rol.getInstrucciones().add("Dar clic en el botón Imprimir, para visualizar la ventana con las opciones para guardar o imprimir el documento.");
                rol.getInstrucciones().add("A continuación, deberás verificar que el tamaño de la hoja sea Carta o Letter, la orientación o diseño Vertical y en Opciones Avanzadas deberá estar seleccionada la opción Gráficos de fondo");
                
                rol.setEventosEstadia(ejb.listaEventosEntregaFotografias().getValor());
                listaGeneracionesEventosRegistrados();

            }else{
                return;
            }
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "letrero fotografias";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de generaciones en los que existen eventos de estadía registrados
     */
    public void listaGeneracionesEventosRegistrados(){
        ResultadoEJB<List<Generaciones>> res = ejb.listaGeneracionesFotografias(rol.getEstudiante().getMatricula(), rol.getEventosEstadia());
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setGeneraciones(res.getValor());
                rol.setGeneracion(rol.getGeneraciones().get(0));
                obtenerNivelGeneracionEventoRegistrado();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de bajas registradas en el periodo seleccionado
     */
    public void obtenerNivelGeneracionEventoRegistrado(){
        if(rol.getGeneracion()== null) return;
        ResultadoEJB<ProgramasEducativosNiveles> res = ejb.getNivelGeneracionesFotografias(rol.getEstudiante().getMatricula(),rol.getGeneracion(), rol.getEventosEstadia());
        if(res.getCorrecto()){
            rol.setNivelEducativo(res.getValor());
            List<ProgramasEducativosNiveles> listaNiveles = new ArrayList<>();
            listaNiveles.add(rol.getNivelEducativo());
            rol.setNivelesEducativos(listaNiveles);
            informacionEstudiante();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de estudiantes asignados al asesor academico y evento seleccionado
     */
    public void informacionEstudiante(){
        ResultadoEJB<DtoDatosEstudiante> res = ejb.getInformacionEstudiante(rol.getGeneracion(), rol.getNivelEducativo(), rol.getEstudiante());
        if(res.getCorrecto()){
            rol.setDtoDatosEstudiante(res.getValor());
            rol.setEventoEstadia(ejbAsignacionRolesEstadia.buscarEventoSeleccionado(rol.getGeneracion(), rol.getNivelEducativo(), "Entrega de fotografías").getValor());
            rol.setEventoActivo(ejb.buscarEventoActivo(rol.getEventoEstadia()).getValor());
            rol.setCodigoQr(String.valueOf(rol.getEstudiante().getMatricula()));
            rol.setGeneracionTexto(Short.toString(rol.getGeneracion().getInicio()).concat("-").concat(Short.toString(rol.getGeneracion().getFin())));
            convertirFechasTexto();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite que al cambiar o seleccionar un periodo escolar se pueda actualizar la lista de bajas del periodo
     * @param e Evento del cambio de valor
     */
    public void cambiarGeneracion(ValueChangeEvent e){
        if(e.getNewValue() instanceof Generaciones){
            Generaciones generacion = (Generaciones)e.getNewValue();
            rol.setGeneracion(generacion);
            obtenerNivelGeneracionEventoRegistrado();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
   
      /**
     * Permite convertir la fecha de emisión del documento en texto
     */
    public void convertirFechasTexto(){
        SimpleDateFormat sinTexto = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat completa = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("ES", "MX"));
        SimpleDateFormat sinAnio = new SimpleDateFormat("d 'de' MMMM ", new Locale("ES", "MX"));
        
        rol.setFechaEmision(sinTexto.format(new Date()));
        rol.setFechaInicio(sinAnio.format(rol.getEventoEstadia().getFechaInicio()));
        rol.setFechaFin(completa.format(rol.getEventoEstadia().getFechaFin()));
    }
}
