/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ConsultaReporteCalificacionesTutor;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCalificacionEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCalificacionesTutor;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoGrupoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPresentacionCalificacionesReporte;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoUnidadConfiguracion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoVistaCalificaciones;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoVistaCalificacionestitulosTabla;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsistencias;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaCalificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaalumnosca;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracion;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */

@Named
@ViewScoped
public class ConcentradoCalificaciones extends ViewScopedRol implements Desarrollable{
    @Getter @Setter ConsultaReporteCalificacionesTutor rol;
    
    @EJB EjbPropiedades ep;
    @EJB EjbValidacionRol evr;
    @EJB EjbRegistroPlanEstudio ejb;
    @EJB EjbConsultaCalificacion ecc;
    @EJB EjbAsistencias ea;
    @EJB EjbPacker packer;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    private List<UnidadMateriaConfiguracion> umcs = new ArrayList<>();
    private List<DtoCargaAcademica> academicas = new ArrayList<>();
    private List<BigDecimal> proUni = new ArrayList<>();
    private CargaAcademica academica = new CargaAcademica();
    private List<DtoVistaCalificaciones> dvcs = new ArrayList<>();
    private Integer tasis=0;
    private UnidadMateriaConfiguracion umc = new UnidadMateriaConfiguracion();
    @Inject LogonMB logon;
    
    @Getter Boolean tieneAcceso = false;
            
    @PostConstruct
    public void init(){
        try {
            setVistaControlador(ControlEscolarVistaControlador.CONCENTRADO_CALIFICACIONES_TUTOR);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = evr.validarTutor(logon.getPersonal().getClave());//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            rol = new ConsultaReporteCalificacionesTutor(resAcceso.getValor());
            tieneAcceso = rol.tieneAcceso(rol.getTutor());
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
           
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            rol.setNivelRol(NivelRol.CONSULTA);
            
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDescendentes();
            if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
            rol.setPeriodos(resPeriodos.getValor());
            rol.setPeriodo(ea.getPeriodoActual());
            
            ResultadoEJB<List<Grupo>> resgrupos = ejb.getListaGrupoPorTutor(rol.getTutor(),rol.getPeriodo());
            if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);
            rol.setGrupos(resgrupos.getValor());   
            rol.setGrupoSelec(rol.getGrupos().get(0));
        
            ResultadoEJB<List<Listaalumnosca>> rejb = ejb.getListaAlumnosPorGrupo(rol.getGrupoSelec());
            if(!rejb.getCorrecto()) mostrarMensajeResultadoEJB(rejb);
            rol.setListaalumnoscas(rejb.getValor());  
            
            creareporte();
            rol.setFechaInpresion(new Date());
        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "concentrado calificaciones tutor";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

    public String buscarPersonal(Integer clave) {
        try {
            Personal p = new Personal();
            if (clave != null) {
                p = ejbPersonal.mostrarPersonalLogeado(clave);
                return p.getNombre();
            } else {
                return "Nombre del tutor";
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(PaseListaDoc.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    public void consultarAlumnos(ValueChangeEvent event) {
        rol.setListaalumnoscas(new ArrayList<>());
        rol.setGrupoSelec((Grupo) event.getNewValue());
        ResultadoEJB<List<Listaalumnosca>> rejb = ejb.getListaAlumnosPorGrupo(rol.getGrupoSelec());
        if(!rejb.getCorrecto()) mostrarMensajeResultadoEJB(rejb);
        rol.setListaalumnoscas(rejb.getValor());  
        creareporte();
    }
    
    public void creareporte() {
        rol.setDrpls(new ArrayList<>());
        rol.setDplrs(new ArrayList<>());
        rol.setEstudiantes(new ArrayList<>());
        ResultadoEJB<List<DtoCargaAcademica>> rejb = ea.getCargaAcademicasPorTutor(rol.getTutor().getPersonal().getClave(), rol.getPeriodo());
        academicas = new ArrayList<>();
        academicas = rejb.getValor().stream().filter(a -> Objects.equals(a.getGrupo().getIdGrupo(), rol.getGrupoSelec().getIdGrupo())).collect(Collectors.toList());
        DtoCargaAcademica dca = academicas.get(0);
        UnidadMateriaConfiguracion umc = dca.getCargaAcademica().getUnidadMateriaConfiguracionList().get(0);
        ResultadoEJB<DtoUnidadConfiguracion> ducB = packer.packUnidadConfiguracion(umc, dca);
        ResultadoEJB<DtoGrupoEstudiante> resGrupo = packer.packGrupoEstudiante(dca, ducB.getValor());
        resGrupo.getValor().getEstudiantes().forEach((t) -> {
            rol.getEstudiantes().add(t.getDtoEstudiante().getInscripcionActiva().getInscripcion());
        });
        rol.setDcts(new ArrayList<>());
        rol.getDcts().clear();
        
        rol.setDvcs(new ArrayList<>());
        rol.getDvcs().clear();
        

        rol.getEstudiantes().forEach((t) -> {
            List<DtoCalificacionEstudiante.CalificacionePorUnidad> calificacionePorUnidad = obtenerCalificaciones(t);
            List<DtoCalificacionEstudiante.CalificacionePorMateria> calificacionePorMateria = obtenerPromedioMateria(t);
            List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion> tareaIntegradoraPresentacion = obtenerTareaIntegradoraPorMateria(t);
            List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria> calificacionesNivelacionPorMateria = obtenerNivelacionesPorMateria(t);
            BigDecimal promedioF = obtenerPromedioCuatrimestral(t);
            rol.getDcts().add(new DtoCalificacionesTutor(t, calificacionePorUnidad, calificacionePorMateria, calificacionePorMateria, tareaIntegradoraPresentacion, calificacionesNivelacionPorMateria, promedioF));

        });
        rol.getDcts().forEach((t) -> {
            dvcs=new ArrayList<>();
            dvcs.clear();
            Collections.sort(t.getCalificacionePorMateria(),  (x, y) -> Integer.compare(x.getMateria().getIdMateria(), y.getMateria().getIdMateria()));                    
            t.getCalificacionePorMateria().forEach((cm) -> {
                proUni=new ArrayList<>();
                proUni.clear();
                List<DtoCalificacionEstudiante.CalificacionePorUnidad> cpus=t.getCalificacionePorUnidad().stream().filter(c -> Objects.equals(c.getMateria().getIdMateria(), cm.getMateria().getIdMateria())).collect(Collectors.toList());
                Collections.sort(cpus,  (x, y) -> Integer.compare(x.getUnidadMateria().getNoUnidad(), y.getUnidadMateria().getNoUnidad()));    
                
                List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion> tips=t.getTareaIntegradoraPresentacion().stream().filter(c -> Objects.equals(c.getCargaAcademica().getIdPlanMateria().getIdMateria().getIdMateria(), cm.getMateria().getIdMateria())).collect(Collectors.toList());
                List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria> cnpms=t.getCalificacionesNivelacionPorMateria().stream().filter(c -> Objects.equals(c.getMateria().getIdMateria(), cm.getMateria().getIdMateria())).collect(Collectors.toList());
                
                DtoCalificacionEstudiante.TareaIntegradoraPresentacion tip=tips.get(0);
                DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria cnpm=cnpms.get(0);
                
                cpus.forEach((p) -> {
                    proUni.add(p.getPromedioUnidad());
                });
                dvcs.add(new DtoVistaCalificaciones(cm.getMateria(), proUni,tip.getPromedio(),cnpm.getPromedio(), cm.getPromedio()));
            });   
            rol.getDvcs().add(new DtoPresentacionCalificacionesReporte(
                    t.getEstudiante().getMatricula(), 
                    t.getEstudiante().getAspirante().getIdPersona().getApellidoPaterno() + " " + t.getEstudiante().getAspirante().getIdPersona().getApellidoMaterno() + " " + t.getEstudiante().getAspirante().getIdPersona().getNombre(), 
                    dvcs,
                    t.getPromedioF()));
        });
        rol.setTitulos(new ArrayList<>());
        rol.getTitulos().clear();
        DtoPresentacionCalificacionesReporte dpcr=rol.getDvcs().get(0);
        dpcr.getMaterias().forEach((t) -> {
            List<DtoCargaAcademica> dcas = academicas.stream().filter(c -> Objects.equals(c.getMateria().getIdMateria(), t.getMateria().getIdMateria())).collect(Collectors.toList());
            if (!dcas.isEmpty()) {
                DtoCargaAcademica dc = dcas.get(0);
                Boolean b = Boolean.FALSE;
                if (dc.getCargaAcademica().getTareaIntegradora() != null) {
                    b = Boolean.TRUE;
                }                
                rol.getTitulos().add(new DtoVistaCalificacionestitulosTabla(dc.getDocente().getPersonal().getNombre(), dc.getMateria().getNombre(), dc.getMateria().getUnidadMateriaList().size(), b));
                rol.setPrograma(dc.getPrograma());
                rol.setPlanEstudio(dc.getPlanEstudio());
            }else{
                t.getMateria().getPlanEstudioMateriaList().forEach((pem) -> {
                    pem.getCargaAcademicaList().forEach((ca) -> {
                        academica=ca;
                    });
                });
                Boolean b = Boolean.FALSE;
                if (academica.getTareaIntegradora() != null) {
                    b = Boolean.TRUE;
                }
                rol.getTitulos().add(new DtoVistaCalificacionestitulosTabla(buscarPersonal(academica.getDocente()), t.getMateria().getNombre(), t.getMateria().getUnidadMateriaList().size(),b));
            }
        });
    }

    public List<DtoCalificacionEstudiante.CalificacionePorUnidad> obtenerCalificaciones(Estudiante e) {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorUnidad>> resCalificaciones = ecc.packCalificacionesPorUnidadyMateria1(e);
        return resCalificaciones.getValor().stream().filter(a -> a.getEstudiante().getGrupo().getPeriodo() == rol.getPeriodo().getPeriodo()).collect(Collectors.toList());
    }

    public List<DtoCalificacionEstudiante.CalificacionePorMateria> obtenerPromedioMateria(Estudiante e) {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorMateria>> resultadoEJB = ecc.packPromedioMateria(e);
        return resultadoEJB.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodo().getPeriodo()).collect(Collectors.toList());
    }

    public List<DtoCalificacionEstudiante.CalificacionePorMateria> obtenerPromediosFinales(Estudiante e) {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorMateria>> resultadoEJB = ecc.packCalificacionesFinales(e);
        return resultadoEJB.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodo().getPeriodo()).collect(Collectors.toList());
    }

    public BigDecimal obtenerPromedioCuatrimestral(Estudiante e) {
        ResultadoEJB<BigDecimal> promedio = ecc.obtenerPromedioCuatrimestral(e, rol.getPeriodo().getPeriodo());
        BigDecimal valor = promedio.getValor();
        rol.setMateriasPorEstudiante(new ArrayList<>());
        rol.setMateriasPorEstudiante(ecc.packMaterias(e).getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodo().getPeriodo()).collect(Collectors.toList()));
        BigDecimal numeroMaterias = new BigDecimal(rol.getMateriasPorEstudiante().size());
        BigDecimal promedioCuatrimestral = valor.divide(numeroMaterias, RoundingMode.HALF_UP);
        return promedioCuatrimestral.setScale(1, RoundingMode.HALF_UP);
    }

    public List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion> obtenerTareaIntegradoraPorMateria(Estudiante e) {
        ResultadoEJB<List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion>> resultadoEJB = ecc.tareaIntegradoraPresentacion(e);
        return resultadoEJB.getValor();
    }

    public List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria> obtenerNivelacionesPorMateria(Estudiante e) {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria>> resultadoEJB = ecc.packPromedioNivelacionPorMateria(e);
        return resultadoEJB.getValor();
    }


    public void metodoBase() {}
}
