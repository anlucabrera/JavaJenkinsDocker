/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.ArrayList;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ConsultaListasAsistenciaDirector;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ConsultaReporteAsistenciaTutor;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPaseListaReportes;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReportePaseLista;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsistencias;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Asistenciasacademicas;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaalumnosca;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
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
public class ReporteAsistenciasTutor extends ViewScopedRol implements Desarrollable{
    @Getter @Setter ConsultaReporteAsistenciaTutor rol;
    
    @EJB EjbPropiedades ep;
    @EJB EjbValidacionRol evr;
    @EJB EjbRegistroPlanEstudio ejb;
    @EJB EjbAsistencias ea;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    private List<UnidadMateriaConfiguracion> umcs = new ArrayList<>();
    private List<DtoCargaAcademica> academicas = new ArrayList<>();
    private Integer tasis=0;
    private UnidadMateriaConfiguracion umc = new UnidadMateriaConfiguracion();
    @Inject LogonMB logon;
    
    @Getter Boolean tieneAcceso = false;
            
    @PostConstruct
    public void init(){
        try {
            setVistaControlador(ControlEscolarVistaControlador.REPORTE_ASISTENCIA_TUTOR);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = evr.validarTutor(logon.getPersonal().getClave());//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            rol = new ConsultaReporteAsistenciaTutor(resAcceso.getValor());
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
            rol.setNewCompetencia(false);
        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "Reporte Asistencias";
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
        rol.setUms(new ArrayList<>());
        rol.setDocentes(new ArrayList<>());
        ResultadoEJB<List<DtoCargaAcademica>> rejb = ea.getCargaAcademicasPorTutor(rol.getTutor().getPersonal().getClave(), rol.getPeriodo());
        academicas = new ArrayList<>();
        academicas = rejb.getValor().stream().filter(a -> Objects.equals(a.getGrupo().getIdGrupo(), rol.getGrupoSelec().getIdGrupo())).collect(Collectors.toList());
        academicas.forEach((t) -> {
            rol.getDocentes().add(t.getDocente());
            rol.setPrograma(t.getPrograma());
            rol.setPlanEstudio(t.getPlanEstudio());
            ResultadoEJB<List<UnidadMateriaConfiguracion>> resultadoEJB = ea.buscarConfiguracionUnidadMateria(t);
            List<UnidadMateriaConfiguracion> configuracions = new ArrayList<>();
            Date d = new Date();
            configuracions = resultadoEJB.getValor().stream().filter(um -> (d.after(um.getFechaInicio()) || d.equals(um.getFechaInicio())) && (d.before(um.getFechaFin()) || d.equals(um.getFechaFin()))).collect(Collectors.toList());
            UnidadMateriaConfiguracion configuracion = new UnidadMateriaConfiguracion();
            if (!configuracions.isEmpty()) {
                configuracion = configuracions.get(0);
                rol.getUms().add(configuracion.getIdUnidadMateria());
            } else {
                rol.getUms().add(new UnidadMateria(0, "Nombre Unidad", "", 0, 0, 0, false));
            }
        });
        rol.getListaalumnoscas().forEach((e) -> {
            rol.setDrpls(new ArrayList<>());
            academicas.forEach((a) -> {
                ResultadoEJB<List<UnidadMateriaConfiguracion>> resultadoEJB = ea.buscarConfiguracionUnidadMateria(a);
                umcs = new ArrayList<>();
                Date d = new Date();
                umcs = resultadoEJB.getValor().stream().filter(um -> (d.after(um.getFechaInicio()) || d.equals(um.getFechaInicio())) && (d.before(um.getFechaFin()) || d.equals(um.getFechaFin()))).collect(Collectors.toList());
                umc = new UnidadMateriaConfiguracion();
                if (!umcs.isEmpty()) {
                    umc = umcs.get(0);
                    Date fI = umc.getFechaInicio();
                    Date fF = umc.getFechaFin();

                    ResultadoEJB<List<Asistenciasacademicas>> res = ea.buscarAsistenciasacademicas(a.getCargaAcademica(), e.getMatricula());
                    List<Asistenciasacademicas> asFilter = new ArrayList<>();
                    if (!res.getValor().isEmpty()) {
                        asFilter = res.getValor().stream().filter(t -> (t.getAsistencia().getFechaHora().after(fI) || t.getAsistencia().getFechaHora().equals(fI)) && (t.getAsistencia().getFechaHora().before(fF) || t.getAsistencia().getFechaHora().equals(fF))).collect(Collectors.toList());
                    }
                    tasis = 0;
                    if (asFilter.size() > 0 && !asFilter.isEmpty()) {
                        asFilter.forEach((t) -> {
                            if (!t.getTipoAsistenciaA().equals("Falta")) {
                                tasis = tasis + 1;
                            }
                        });
                        Double porC = (tasis * 100.0) / asFilter.size();
                        Boolean b = (porC < 80.0);
                        rol.getDrpls().add(new DtoReportePaseLista(umc.getIdUnidadMateria(), asFilter.size(), tasis, porC, b));
                    } else {
                        rol.getDrpls().add(new DtoReportePaseLista(umc.getIdUnidadMateria(), asFilter.size(), tasis, 100D, Boolean.FALSE));
                    }
                }else{
                    rol.getDrpls().add(new DtoReportePaseLista(new UnidadMateria(0, "Nombre Unidad", "", 0, 0, 0, false), 0, tasis, 100D, Boolean.FALSE));
                }
            });
            rol.getDplrs().add(new DtoPaseListaReportes(e, rol.getDrpls()));
        });
    }




    public void metodoBase() {}
}
