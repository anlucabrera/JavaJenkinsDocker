/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
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

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoInscripcion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionNivelacion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Ajax;



/**
 *
 * @author UTXJ
 */

@Named
@ViewScoped
public class ConcentradoCalificacionesTutor extends ViewScopedRol implements Desarrollable{
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
    private Boolean niv=Boolean.FALSE;
    private UnidadMateriaConfiguracion umc = new UnidadMateriaConfiguracion();
    @Inject LogonMB logon;
    
    @Getter Boolean tieneAcceso = false;
            


@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;


    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.CONCENTRADO_CALIFICACIONES_TUTOR);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = evr.validarTutor(logon.getPersonal().getClave());//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            rol = new ConsultaReporteCalificacionesTutor(resAcceso.getValor());
            tieneAcceso = rol.tieneAcceso(rol.getTutor());
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu
            rol.setNivelRol(NivelRol.CONSULTA);
            
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosRegistros(rol.getTutor());
            if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
            if(resPeriodos.getValor().isEmpty()) return;
            
            rol.setPeriodos(resPeriodos.getValor());
            rol.setPeriodo(rol.getPeriodos().get(0));
            ResultadoEJB<List<Grupo>> resgrupos = ejb.getListaGrupoPorTutor(rol.getTutor(),rol.getPeriodo());
            if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);
            if(resgrupos.getValor().isEmpty())return;
            rol.setGrupos(resgrupos.getValor());   
            rol.setGrupoSelec(rol.getGrupos().get(0));
        
            ResultadoEJB<List<Listaalumnosca>> rejb = ejb.getListaAlumnosPorGrupo(rol.getGrupoSelec());
            if(!rejb.getCorrecto()) mostrarMensajeResultadoEJB(rejb);
            if(rejb.getValor().isEmpty())return;
            rol.setListaalumnoscas(rejb.getValor());  
            
            creareporte();
            rol.setFechaInpresion(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            mostrarExcepcion(e);
        }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "concentrado calificaciones tutor";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

    public void cambiarPeriodo() {
        rol.setDrpls(new ArrayList<>());
        rol.setDplrs(new ArrayList<>());
        rol.setTitulos(new ArrayList<>());
        rol.setDvcs(new ArrayList<>());
        rol.setEstudiantes(new ArrayList<>());
        academicas = new ArrayList<>();
        rol.setGrupoSelec(new Grupo());
        if (rol.getPeriodo() == null) {
            mostrarMensaje("No hay periodo escolar seleccionado.");
            return;
        }
        ResultadoEJB<List<Grupo>> resgrupos = ejb.getListaGrupoPorTutor(rol.getTutor(),rol.getPeriodo());
        if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);
        if(resgrupos.getValor().isEmpty())return;
        rol.setGrupos(resgrupos.getValor());   
        Ajax.update("frm");
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
        ResultadoEJB<List<DtoCargaAcademica>> rejb = ea.getCargaAcademicasPorGrupo(rol.getGrupoSelec().getIdGrupo(), rol.getPeriodo());        
        academicas = new ArrayList<>();
       
        if(!rejb.getCorrecto()) mostrarMensajeResultadoEJB(rejb);
        if(rejb.getValor().isEmpty())return;
        academicas = rejb.getValor().stream().filter(a -> Objects.equals(a.getGrupo().getIdGrupo(), rol.getGrupoSelec().getIdGrupo())).collect(Collectors.toList());
        DtoCargaAcademica dca = academicas.get(0);
        if(dca.getCargaAcademica().getUnidadMateriaConfiguracionList().isEmpty())return;
        UnidadMateriaConfiguracion umc = dca.getCargaAcademica().getUnidadMateriaConfiguracionList().get(0);
        ResultadoEJB<DtoUnidadConfiguracion> ducB = packer.packUnidadConfiguracion(umc, dca);
        ResultadoEJB<DtoGrupoEstudiante> resGrupo = packer.packGrupoEstudiante(dca, ducB.getValor());
        resGrupo.getValor().getEstudiantes().forEach((t) -> {
            List<DtoInscripcion> di = t.getDtoEstudiante().getInscripciones().stream().filter(a -> Objects.equals(a.getGrupo().getIdGrupo(), rol.getGrupoSelec().getIdGrupo())).collect(Collectors.toList());
            if (!di.isEmpty()) {
                DtoInscripcion inscripcion=di.get(0);
                rol.getEstudiantes().add(inscripcion.getInscripcion());
            }
        });
        rol.setDcts(new ArrayList<>());
        rol.getDcts().clear();

        rol.setDvcs(new ArrayList<>());
        rol.getDvcs().clear();

        rol.getEstudiantes().forEach((t) -> {
            if (!obtenerCalificaciones(t).isEmpty()) {
                List<DtoCalificacionEstudiante.CalificacionePorUnidad> calificacionePorUnidad = obtenerCalificaciones(t);
                List<DtoCalificacionEstudiante.CalificacionePorMateria> calificacionePorMateria = obtenerPromedioMateria(t);
                List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion> tareaIntegradoraPresentacion = obtenerTareaIntegradoraPorMateria(t);
                List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria> calificacionesNivelacionPorMateria = obtenerNivelacionesPorMateria(t);
                BigDecimal promedioF = obtenerPromedioCuatrimestral(t);
                rol.getDcts().add(new DtoCalificacionesTutor(t, calificacionePorUnidad, calificacionePorMateria, calificacionePorMateria, tareaIntegradoraPresentacion, calificacionesNivelacionPorMateria, promedioF));
            } else {
                rol.getDcts().add(new DtoCalificacionesTutor(t, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), BigDecimal.ZERO));
            }
        });
        rol.getDcts().forEach((t) -> {
            dvcs = new ArrayList<>();
            dvcs.clear();
            Collections.sort(t.getCalificacionePorMateria(), (x, y) -> Integer.compare(x.getMateria().getIdMateria(), y.getMateria().getIdMateria()));
            if (!t.getCalificacionePorMateria().isEmpty()) {
                t.getCalificacionePorMateria().forEach((cm) -> {
                    proUni = new ArrayList<>();
                    proUni.clear();
                    List<DtoCalificacionEstudiante.CalificacionePorUnidad> cpus = t.getCalificacionePorUnidad().stream().filter(c -> Objects.equals(c.getMateria().getIdMateria(), cm.getMateria().getIdMateria())).collect(Collectors.toList());
                    Collections.sort(cpus, (x, y) -> Integer.compare(x.getUnidadMateria().getNoUnidad(), y.getUnidadMateria().getNoUnidad()));
                    List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion> tips = t.getTareaIntegradoraPresentacion().stream().filter(c -> Objects.equals(c.getCargaAcademica().getIdPlanMateria().getIdMateria().getIdMateria(), cm.getMateria().getIdMateria())).collect(Collectors.toList());
                    List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria> cnpms = t.getCalificacionesNivelacionPorMateria().stream().filter(c -> Objects.equals(c.getMateria().getIdMateria(), cm.getMateria().getIdMateria())).collect(Collectors.toList());
                    DtoCalificacionEstudiante.TareaIntegradoraPresentacion tip;
                    DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria cnpm;
                    if (cpus.size() == cm.getMateria().getUnidadMateriaList().size()) {
                        cpus.forEach((p) -> {
                            proUni.add(p.getPromedioUnidad());
                        });
                    } else {
                        cm.getMateria().getUnidadMateriaList().forEach((p) -> {
                            List<DtoCalificacionEstudiante.CalificacionePorUnidad> l = cpus.stream().filter(u -> Objects.equals(u.getUnidadMateria().getIdUnidadMateria(), p.getIdUnidadMateria())).collect(Collectors.toList());
                            if (l.isEmpty()) {
                                proUni.add(BigDecimal.ZERO);
                            } else {
                                DtoCalificacionEstudiante.CalificacionePorUnidad cpu = l.get(0);
                                proUni.add(cpu.getPromedioUnidad());
                            }
                        });
                    }                    
                    if (!tips.isEmpty() && !cnpms.isEmpty()) {
                        tip = tips.get(0);
                        cnpm = cnpms.get(0);
                        niv = Boolean.TRUE;
                        if (cnpm.getPromedio() != BigDecimal.ZERO) {
                            dvcs.add(new DtoVistaCalificaciones(cm.getMateria(), proUni, tip.getPromedio(), cm.getPromedio(), cnpm.getPromedio(), cnpm.getPromedio()));
                        } else {
                            dvcs.add(new DtoVistaCalificaciones(cm.getMateria(), proUni, tip.getPromedio(), cm.getPromedio(), BigDecimal.ZERO, cm.getPromedio()));
                        }
                    }else if(!tips.isEmpty() && cnpms.isEmpty()){   
                        tip = tips.get(0);                     
                        niv = Boolean.FALSE;
                        dvcs.add(new DtoVistaCalificaciones(cm.getMateria(), proUni, tip.getPromedio(), cm.getPromedio(), BigDecimal.ZERO, cm.getPromedio()));
                    }else{
                        niv = Boolean.FALSE;
                        dvcs.add(new DtoVistaCalificaciones(cm.getMateria(), proUni, "0", cm.getPromedio(), BigDecimal.ZERO, cm.getPromedio()));
                    }                                                                                                                        
                    niv = Boolean.FALSE;
                });
                rol.getDvcs().add(new DtoPresentacionCalificacionesReporte(
                    t.getEstudiante().getMatricula(),
                    t.getEstudiante().getTipoEstudiante().getIdTipoEstudiante(),
                    t.getEstudiante().getAspirante().getIdPersona().getApellidoPaterno() + " " + t.getEstudiante().getAspirante().getIdPersona().getApellidoMaterno() + " " + t.getEstudiante().getAspirante().getIdPersona().getNombre(),
                    dvcs,
                    t.getPromedioF()));
            }
        });
        rol.setTitulos(new ArrayList<>());
        rol.getTitulos().clear();
        DtoPresentacionCalificacionesReporte dpcr = rol.getDvcs().get(0);
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
            } else {
                t.getMateria().getPlanEstudioMateriaList().forEach((pem) -> {
                    pem.getCargaAcademicaList().forEach((ca) -> {
                        academica = ca;
                    });
                });
                Boolean b = Boolean.FALSE;
                if (academica.getTareaIntegradora() != null) {
                    b = Boolean.TRUE;
                }                
                rol.getTitulos().add(new DtoVistaCalificacionestitulosTabla(buscarPersonal(academica.getDocente()), t.getMateria().getNombre(), t.getMateria().getUnidadMateriaList().size(), b));
            }
        });        
    }

    public List<DtoCalificacionEstudiante.CalificacionePorUnidad> obtenerCalificaciones(Estudiante e) {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorUnidad>> resCalificaciones = ecc.packCalificacionesPorUnidadyMateria1(e);
        if (resCalificaciones.getCorrecto()) {
            return resCalificaciones.getValor().stream().filter(a -> a.getEstudiante().getGrupo().getPeriodo() == rol.getPeriodo().getPeriodo()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public List<DtoCalificacionEstudiante.CalificacionePorMateria> obtenerPromedioMateria(Estudiante e) {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorMateria>> resultadoEJB = ecc.packPromedioMateria(e);
        if (resultadoEJB.getCorrecto()) {
            return resultadoEJB.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodo().getPeriodo()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public List<DtoCalificacionEstudiante.CalificacionePorMateria> obtenerPromediosFinales(Estudiante e) {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorMateria>> resultadoEJB = ecc.packCalificacionesFinales(e);
        if (resultadoEJB.getCorrecto()) {
            return resultadoEJB.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodo().getPeriodo()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public BigDecimal obtenerPromedioCuatrimestral(Estudiante e) {
        ResultadoEJB<BigDecimal> promedio = ecc.obtenerPromedioCuatrimestral(e, rol.getPeriodo().getPeriodo());
        if (promedio.getCorrecto()) {
            BigDecimal valor = promedio.getValor();
            rol.setMateriasPorEstudiante(new ArrayList<>());
            rol.setMateriasPorEstudiante(ecc.packMaterias(e).getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodo().getPeriodo()).collect(Collectors.toList()));
            BigDecimal numeroMaterias = new BigDecimal(rol.getMateriasPorEstudiante().size());
            BigDecimal promedioCuatrimestral = valor.divide(numeroMaterias, RoundingMode.HALF_UP);

            return promedioCuatrimestral.setScale(1, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion> obtenerTareaIntegradoraPorMateria(Estudiante e) {
        ResultadoEJB<List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion>> resultadoEJB = ecc.tareaIntegradoraPresentacion(e);
        if (resultadoEJB.getCorrecto()) {
            return resultadoEJB.getValor();
        } else {
            return new ArrayList<>();
        }
    }

    public List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria> obtenerNivelacionesPorMateria(Estudiante e) {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria>> resultadoEJB = ecc.packPromedioNivelacionPorMateria(e);
        if (resultadoEJB.getCorrecto()) {
            return resultadoEJB.getValor();
        } else {
            return new ArrayList<>();
        }
    }


    public void metodoBase() {}
}
