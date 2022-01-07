package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.event.ValueChangeEvent;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReincorporacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ReincorporacionCalificacionesRolServiciosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbFichaAdmision;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroFichaIngenieria;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.UniversidadesUT;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;



@Named
@ViewScoped
public class ReincorporacionCalificacionesServiciosEscolares extends ViewScopedRol implements Desarrollable {
    
    @Getter @Setter ReincorporacionCalificacionesRolServiciosEscolares rol;
    @Getter Boolean tieneAcceso = false;
    @Getter private Boolean cargado = false;
    
    @EJB EjbReincorporacion ejb;
    @EJB EjbValidacionRol evr;
    @EJB EjbFichaAdmision efa;
    @EJB EjbPropiedades ep;
    @EJB private EjbRegistroFichaIngenieria ejbRegistroIng;
    
    @Inject LogonMB logonMB;

    



@PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.REINCORPORACION);
            ResultadoEJB<Filter<PersonalActivo>> resAccesoEs = ejb.validarServiciosEscolares(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares
                                  
            if (!resAccesoEs.getCorrecto()){mostrarMensajeResultadoEJB(resAccesoEs);return;}
            
            Filter<PersonalActivo> filtroEs = resAccesoEs.getValor();//se obtiene el filtro resultado de la validación        
            
            PersonalActivo activoEs = filtroEs.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            
            rol = new ReincorporacionCalificacionesRolServiciosEscolares(filtroEs, activoEs, activoEs.getAreaOperativa());
            tieneAcceso = rol.tieneAcceso(activoEs);
            rol.setPersonalActivoSe(activoEs);
            
                        
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.OPERATIVO);
            
            rol.setCalificacionesR(new ArrayList<>());
            rol.setEstudiantesReincorporaciones(new ArrayList<>());
            rol.setTipoCal("Regulatoria");
            rol.setEstudiante(new Estudiante());
        
            rol.setUniversidades(new ArrayList<>());
            ResultadoEJB<List<Estudiante>> resAcceso = ejb.getEstudiantesReincorporaciones(); 
            if (!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}   
            rol.setEstudiantesReincorporaciones(resAcceso.getValor());
            rol.setPlanesDeEstudioConsultas(new ArrayList<>());
            rol.setHistorialCalificacionesSaiiuts(new ArrayList<>());
            rol.setHistorialCalificacionesTsus(new ArrayList<>());
            rol.setUniversidadActiva(0);
            rol.setPestaniaActiva(0);
            rol.setIdPlanEstudio("");
            rol.setHistorialTsu(new CalificacionesHistorialTsuOtrosPe());
            rol.setTipoRep(Boolean.TRUE);
            rol.setFiltaBaja(Boolean.TRUE);
            llenaCatalogos();
            getreporte();
            getreporteEstudiante();
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "reincorporaciones";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    public void llenaCatalogos() {
        rol.setMeses(new ArrayList<>());
        rol.setAnios(new ArrayList<>());
        rol.getMeses().add("Enero");
        rol.getMeses().add("Febrero");
        rol.getMeses().add("Marzo");
        rol.getMeses().add("Abril");
        rol.getMeses().add("Mayo");
        rol.getMeses().add("Junio");
        rol.getMeses().add("Julio");
        rol.getMeses().add("Agosto");
        rol.getMeses().add("Septiembre");
        rol.getMeses().add("Octubre");
        rol.getMeses().add("Noviembre");
        rol.getMeses().add("Diciembre");
        
        for (int i = LocalDate.now().getYear(); i >=1986 ; i-- ){
            rol.getAnios().add(i);
        }
        
        rol.setMesIni("Septiembre");
        rol.setMesFin("Agosto");
        
        rol.setAnioIni(LocalDate.now().getYear()-2);
        rol.setAnioFin(LocalDate.now().getYear());

    }
    
    public void estudianteConsultado() {
        buscarAlineacionCalificaciones();
    }
    
    public void getUniversidades (){
        ResultadoEJB<List<UniversidadesUT>> resUniversidades = ejbRegistroIng.getUniversidades();
        if(!resUniversidades.getCorrecto()){ mostrarMensajeResultadoEJB(resUniversidades);return;}
        rol.setUniversidades(resUniversidades.getValor());
    }
    
    public void getPlanesEstudio (){
        ResultadoEJB<List<DtoReincorporacion.PlanesDeEstudioConsulta>> resUniversidades = ejb.getProgramasEducativosConsulta();
        if(!resUniversidades.getCorrecto()){ mostrarMensajeResultadoEJB(resUniversidades);return;}
        rol.setPlanesDeEstudioConsultas(resUniversidades.getValor());
    }
    
    public void buscarAlineacionCalificaciones(){          
        ResultadoEJB<List<DtoReincorporacion.CalificacionesR>> resAccesoTSU = ejb.getCalificaciones(rol.getEstudiante().getAspirante(),rol.getEstudiante().getMatricula(),"TSU"); 
        if(!resAccesoTSU.getCorrecto()){ mostrarMensajeResultadoEJB(resAccesoTSU);return;}
        rol.setCalificacionesTSU(resAccesoTSU.getValor()); 
        
        if(rol.getCalificacionesTSU().isEmpty()){
            rol.setTsu(new DtoReincorporacion.HistorialTsu(new Persona(), new Aspirante(), new Estudiante(), new UniversidadEgresoAspirante(), Boolean.FALSE, new EstudianteHistorialTsu(), new PlanesEstudioExternos(), new ArrayList<>(), new ArrayList<>(),Boolean.FALSE)); 
            ResultadoEJB<DtoReincorporacion.HistorialTsu> resAccesoTSUHistorico = ejb.getCalificacionesHistoricas(rol.getEstudiante().getAspirante(),rol.getEstudiante().getMatricula()); 
            if(!resAccesoTSUHistorico.getCorrecto()){ mostrarMensajeResultadoEJB(resAccesoTSUHistorico);return;}
            rol.setTsu(resAccesoTSUHistorico.getValor()); 
            getUniversidades();
            
            if(rol.getTsu().getUniversidadEncontrada()){
                rol.setUniversidadActiva(rol.getTsu().getEgresoAspirante().getUniversidadEgresoAspirantePK().getUniversidadEgreso());
            }
            getPlanesEstudio();
            
            ResultadoEJB<List<CalificacionesHistorialTsuOtrosPe>> calTSU = ejb.getCalificacionesHistorialTsu(rol.getTsu()); 
            if(!calTSU.getCorrecto()){ mostrarMensajeResultadoEJB(calTSU);return;}
            rol.setHistorialCalificacionesTsus(calTSU.getValor()); 
            
            ResultadoEJB<List<CalificacionesHistorialTsu>> calSaiiut = ejb.getCalificacionestsuotrasaiiut(rol.getTsu()); 
            if(!calSaiiut.getCorrecto()){ mostrarMensajeResultadoEJB(calSaiiut);return;}
            rol.setHistorialCalificacionesSaiiuts(calSaiiut.getValor()); 
                        
            
            if (!rol.getHistorialCalificacionesSaiiuts().isEmpty()) {
                CalificacionesHistorialTsu c = rol.getTsu().getCalificacionesHistorialTsu().get(0);
                rol.setIdPlanEstudio(c.getIdPlanMateria().getIdPlan().getIdPlanEstudio() + "-I");
            } else if (!rol.getHistorialCalificacionesTsus().isEmpty()) {
                CalificacionesHistorialTsuOtrosPe c = rol.getTsu().getCalificacionesHistorialTsuOtrosPes().get(0);
                rol.setIdPlanEstudio(c.getIdplanEstudio().getIdplanEstudio() + "-E");
                rol.getTsu().setExternos(c.getIdplanEstudio());
            }
            
            if(rol.getTsu().getEcontrado()){
                if (!rol.getTsu().getHistorialTsu().getPeriodoCarrera().equals("")) {
                    String[] parts = rol.getTsu().getHistorialTsu().getPeriodoCarrera().split(" ");
                    List<String> textoSeparado = Arrays.asList(parts);
                    if (textoSeparado.size() == 5) {
                        rol.setMesIni(textoSeparado.get(0));
                        rol.setMesFin(textoSeparado.get(3));

                        rol.setAnioIni(Integer.parseInt(textoSeparado.get(1)));
                        rol.setAnioFin(Integer.parseInt(textoSeparado.get(4)));
                    }
                }
            }
        }
        
        ResultadoEJB<List<DtoReincorporacion.CalificacionesR>> resAccesoLIN = ejb.getCalificaciones(rol.getEstudiante().getAspirante(),rol.getEstudiante().getMatricula(),"LIN"); 
        if(!resAccesoLIN.getCorrecto()){ mostrarMensajeResultadoEJB(resAccesoLIN);return;}
        rol.setCalificacionesLIN(resAccesoLIN.getValor()); 
        
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.ReincorporacionCalificacionesServiciosEscolares.buscarAlineacionCalificaciones()");
    }
    
    public void onRowEditCalificacion(RowEditEvent event) {
        DtoReincorporacion.CalificacionesR cr = (DtoReincorporacion.CalificacionesR) event.getObject();
        ResultadoEJB<DtoReincorporacion.CalificacionesR> rejb = ejb.registrarCalificacionesPorPromedio(cr);
        if(!rejb.getCorrecto()){ mostrarMensajeResultadoEJB(rejb);return;}
        buscarAlineacionCalificaciones();
    }
    
    public void onRowEditCalificacionExterno(RowEditEvent event) {
        CalificacionesHistorialTsuOtrosPe cr = (CalificacionesHistorialTsuOtrosPe) event.getObject();
        ResultadoEJB<CalificacionesHistorialTsuOtrosPe> rejb = ejb.operacionesCalificacionesHistorialTsu(cr,"A");
        if(!rejb.getCorrecto()){ mostrarMensajeResultadoEJB(rejb);return;}
        buscarAlineacionCalificaciones();
    }
    
    public void onRowEditCalificacionInterno(RowEditEvent event) {
        CalificacionesHistorialTsu cr = (CalificacionesHistorialTsu) event.getObject();
        ResultadoEJB<CalificacionesHistorialTsu> rejb = ejb.operacionesCalificacionestsuotrasaiiut(cr);
        if(!rejb.getCorrecto()){ mostrarMensajeResultadoEJB(rejb);return;}
        buscarAlineacionCalificaciones();
    }

     public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalInfo("¡Operación cancelada!");
    }

     public void historialTSUEstudiante(){          
       List<DtoReincorporacion.PlanesDeEstudioConsulta> consultas=rol.getPlanesDeEstudioConsultas().stream().filter(t-> t.getIdeCompuesto().equals(rol.getIdPlanEstudio())).collect(Collectors.toList());
       rol.getTsu().getHistorialTsu().setGeneracion(rol.getAnioIni()+" - "+rol.getAnioFin());
       rol.getTsu().getHistorialTsu().setPeriodoCarrera(rol.getMesIni()+" "+rol.getAnioIni()+" - "+rol.getMesFin()+" "+rol.getAnioFin());
       
//         System.out.println("rol.getTsu().getHistorialTsu().setGeneracion()"+rol.getTsu().getHistorialTsu().getGeneracion());
//         System.out.println("rol.getTsu().getHistorialTsu().getPeriodoCarrera()"+rol.getTsu().getHistorialTsu().getPeriodoCarrera());
       
       if(!consultas.isEmpty()){
           ResultadoEJB<DtoReincorporacion.HistorialTsu> rejb = ejb.operacionesHistorialTsu(rol.getTsu(), consultas.get(0), rol.getIdPlanEstudio(), rol.getUniversidadActiva());
       if(!rejb.getCorrecto()){ mostrarMensajeResultadoEJB(rejb);return;}
       buscarAlineacionCalificaciones();
       }else{
           Messages.addGlobalInfo("Seleccione un programa educativo Valido");
       }
    }
     
    public void guardaCalificacion(){
        rol.getHistorialTsu().setEstudianteHistoricoTSU(new EstudianteHistorialTsu());
        rol.getHistorialTsu().setIdplanEstudio(new PlanesEstudioExternos());
        
        rol.getHistorialTsu().setEstudianteHistoricoTSU(rol.getTsu().getHistorialTsu());
        rol.getHistorialTsu().setIdplanEstudio(rol.getTsu().getExternos());
        
//        System.out.println("rol.getHistorialTsu()"+rol.getHistorialTsu().getMateria());
        ResultadoEJB<CalificacionesHistorialTsuOtrosPe> rejb = ejb.operacionesCalificacionesHistorialTsu(rol.getHistorialTsu(),"I");
        if(!rejb.getCorrecto()){ mostrarMensajeResultadoEJB(rejb);return;}
        
        rol.setHistorialTsu(new CalificacionesHistorialTsuOtrosPe());
        
        ResultadoEJB<List<CalificacionesHistorialTsuOtrosPe>> calTSU = ejb.getCalificacionesHistorialTsu(rol.getTsu()); 
        if(!calTSU.getCorrecto()){ mostrarMensajeResultadoEJB(calTSU);return;}
        rol.setHistorialCalificacionesTsus(calTSU.getValor()); 

        ResultadoEJB<List<CalificacionesHistorialTsu>> calSaiiut = ejb.getCalificacionestsuotrasaiiut(rol.getTsu()); 
        if(!calSaiiut.getCorrecto()){ mostrarMensajeResultadoEJB(calSaiiut);return;}
        rol.setHistorialCalificacionesSaiiuts(calSaiiut.getValor()); 
        
        Ajax.update("frmHistorialCalificaciones");
    }
    
     
    public void imprimirValores() {

    }
    
    public void getreporte (){
        ResultadoEJB<List<DtoReincorporacion.ReporteReincorporaciones>> resUniversidades = ejb.getReporteReincorporacionesPorEstudiante();
        if(!resUniversidades.getCorrecto()){ mostrarMensajeResultadoEJB(resUniversidades);return;}
        List<DtoReincorporacion.ReporteReincorporaciones> rrs= new ArrayList<>();
//        if (rol.getTipoRep()) {
//            rrs.addAll(resUniversidades.getValor().stream().filter(t -> t.getCompleto().equals(Boolean.FALSE)).collect(Collectors.toList()));
//        } else {
            rrs.addAll(resUniversidades.getValor());
//        }
//        if (rol.getFiltaBaja()) {
//            rol.setReincorporacioneses(rrs.stream().filter(t->t.getEstatusUltimoregistro().equals("Regular")).collect(Collectors.toList()));
//        } else {
            rol.setReincorporacioneses(rrs);
//        }
    }   
    
    public void getreporteEstudiante (){
        ResultadoEJB<List<DtoReincorporacion.ReporteReincorporaciones>> resUniversidades = ejb.getReporteReincorporacionesPorEstudiante();
        if(!resUniversidades.getCorrecto()){ mostrarMensajeResultadoEJB(resUniversidades);return;}
        rol.setReincorporacionesEstudiantes(resUniversidades.getValor());
    }   
    
    public String buscaPeriodoEscolar (Integer idP){
        PeriodosEscolares escolares =ejb.buscaPeriodo(idP);
        String per=escolares.getMesInicio().getMes()+" - "+escolares.getMesFin().getMes()+" - "+escolares.getAnio();
        return per;
    }  
    public String colocaEstilo (Short tipoE){
        if(null == tipoE){
            return "color: #000000;";
        }else switch (tipoE) {
            case 2:
            case 3:
                return "color: #ff2733;";
            case 5:
            case 6:
                return "color: #00FF00;";
            default:
                return "color: #000000;";
        }
    }  
}

