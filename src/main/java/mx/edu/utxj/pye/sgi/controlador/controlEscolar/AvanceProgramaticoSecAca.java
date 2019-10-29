/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.time.LocalDate;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ConsultaAvanceProgramaticoSecAca;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAvanceProgramatico;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoGrupoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoUnidadConfiguracion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsistencias;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCapturaCalificaciones;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaCalificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import mx.edu.utxj.pye.sgi.util.DateUtils;
import static mx.edu.utxj.pye.sgi.util.DateUtils.asLocalDate;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */

@Named
@ViewScoped
public class AvanceProgramaticoSecAca extends ViewScopedRol implements Desarrollable{
    @Getter @Setter ConsultaAvanceProgramaticoSecAca rol;
    
    @EJB EjbPropiedades ep;
    @EJB EjbValidacionRol evr;
    @EJB EjbRegistroPlanEstudio ejb;
    @EJB EjbConsultaCalificacion ecc;
    @EJB EjbAsistencias ea;
    @EJB EjbPacker packer;    
    @EJB EjbCapturaCalificaciones calificaciones;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    
    private ResultadoEJB<DtoGrupoEstudiante> resGrupo;
    private ResultadoEJB<DtoUnidadConfiguracion> ducB;
    private Integer tasis = 0;
    private Integer totalEs =0;
    private Integer totalD =0;
    private List<DtoCargaAcademica> academicas = new ArrayList<>();
    private List<String> unidadd = new ArrayList<>();
    private List<Double> avanceU = new ArrayList<>();
    private List<String> comentario = new ArrayList<>();
    private List<String> clase = new ArrayList<>();
    private List<String> notas = new ArrayList<>();
    private String c = "";
    private Double p = 0D;
    
    
    @Inject LogonMB logon;
    @Inject UtilidadesCH cH;
    
    @Getter Boolean tieneAcceso = false;
            
    @PostConstruct
    public void init(){
        try {
            setVistaControlador(ControlEscolarVistaControlador.AVANCE_PROGRAMATICO_SECACA);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = evr.validarSecretariaAcademica(logon.getPersonal().getClave());//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            rol = new ConsultaAvanceProgramaticoSecAca(resAcceso.getValor());
            tieneAcceso = rol.tieneAcceso(rol.getSecAca());
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu
            rol.setNivelRol(NivelRol.CONSULTA);
            
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDescendentes();
            if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
            rol.setPeriodos(resPeriodos.getValor());
            rol.setPeriodo(ea.getPeriodoActual());
                        
            ResultadoEJB<Map<AreasUniversidad, List<PlanEstudio>>> resProgramaPlan = ejb.getProgramasEducativostotal();
            if(!resProgramaPlan.getCorrecto()) mostrarMensajeResultadoEJB(resProgramaPlan);
            rol.setAreaPlanEstudioMap(resProgramaPlan.getValor());           

            rol.setPlanEstudio(rol.getPlanesEstudios().get(0));

            ResultadoEJB<List<Grupo>> resgrupos = ejb.getListaGrupoPlanEstudio(rol.getPlanEstudio(),rol.getPeriodo());
            if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);
            rol.setGrupos(resgrupos.getValor()); 

            rol.setGrupoSelec(rol.getGrupos().get(0));
            creareporte();
            rol.setFechaInpresion(new Date());
        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "avance programatico S";
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
    public void cambiarPlanestudio(ValueChangeEvent event) {
        rol.setGrupos(new ArrayList<>());
        rol.setGrupoSelec(new Grupo());
        rol.setDvcs(new ArrayList<>());
        rol.setPlanEstudio((PlanEstudio) event.getNewValue());
        ResultadoEJB<List<Grupo>> resgrupos = ejb.getListaGrupoPlanEstudio(rol.getPlanEstudio(),rol.getPeriodo());
        if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);
        rol.setGrupos(resgrupos.getValor());          
//        rol.setGrupoSelec(rol.getGrupos().get(0));
//        creareporte();     
    }
    
    public void consultarAlumnos(ValueChangeEvent event) {
        rol.setDvcs(new ArrayList<>());
        rol.setDccs(new ArrayList<>());
        rol.setGrupoSelec((Grupo) event.getNewValue());
        creareporte();    
    }
    
    public void creareporte() {
        rol.setDvcs(new ArrayList<>());
        rol.setDccs(new ArrayList<>());
        ResultadoEJB<List<DtoCargaAcademica>> rejb = ea.getCargaAcademicasPorTutor(rol.getGrupoSelec().getTutor(), rol.getPeriodo());
        academicas = new ArrayList<>();
        academicas = rejb.getValor().stream().filter(a -> Objects.equals(a.getGrupo().getIdGrupo(), rol.getGrupoSelec().getIdGrupo())).collect(Collectors.toList());
        academicas.forEach((a) -> {
            unidadd = new ArrayList<>();
            avanceU = new ArrayList<>();
            comentario = new ArrayList<>();
            clase = new ArrayList<>();
            notas = new ArrayList<>();
            List<UnidadMateriaConfiguracion> umcs = a.getCargaAcademica().getUnidadMateriaConfiguracionList().stream().filter(t -> Objects.equals(t.getCarga().getIdPlanMateria().getIdMateria().getIdMateria(), a.getMateria().getIdMateria())).collect(Collectors.toList());
            umcs.forEach((t) -> {
                Boolean activaPorFecha = DateUtils.isBetweenWithRange(new Date(), t.getFechaInicio(), t.getFechaFin(), calificaciones.leerDiasRangoParaCapturarUnidad());
                if (activaPorFecha || (t.getFechaFin().before(new Date()))) {
                    ducB = packer.packUnidadConfiguracion(t, a);
                    resGrupo = packer.packGrupoEstudiante(a, ducB.getValor());
                    totalEs = resGrupo.getValor().getEstudiantes().size();
                    tasis = 0;
                    p=0D;
                    resGrupo.getValor().getEstudiantes().forEach((e) -> {
                        ResultadoEJB<Boolean> rejb1 = calificaciones.verificarCapturaCompleta(e);
                        if (rejb1.getCorrecto()) {
                            Boolean finalizado = rejb1.getValor();
                            if (finalizado) {
                                tasis = tasis + 1;
                            }
                        }
                    });

                    if (tasis != 0) {
                        p = ((100.0 * tasis) / totalEs);
                    }
                    if (activaPorFecha) {
                        c = "Evaluando";
                        LocalDate fin = asLocalDate(t.getFechaFin()).plusDays(calificaciones.leerDiasRangoParaCapturarUnidad());
                        totalD = ((int) ((cH.castearLDaD(fin).getTime() - new Date().getTime()) / 86400000));
                    } else {
                        c = "Evaluada";
                    }
                } else {
                    c="Sin evaluar";
                }                             
                if("Sin evaluar".equals(c)){
                    clase.add("bg-blue");
                    notas.add("No evaluable aun");
                }
                if(p==0D){
                    switch(c){
                        case "Evaluada": c="No se evaluó";clase.add("bg-red");notas.add("Periodo finalizado");break;
                        case "Evaluando":clase.add("bg-yellow");notas.add("Restan "+totalD+" días");break;
                    }
                }else if(p==100D){
                    switch(c){
                        case "Evaluada":clase.add("bg-green");notas.add("Periodo finalizado");break;
                        case "Evaluando":clase.add("bg-green");notas.add("Restan "+totalD+" días");break;
                    }
                }else{
                    switch(c){
                        case "Evaluada":clase.add("bg-red");notas.add("Faltaron "+(totalEs-tasis)+" de "+totalEs+" alumnos");break;
                        case "Evaluando":clase.add("bg-yellow");notas.add("Faltan "+(totalEs-tasis)+" de "+totalEs+" alumnos, restan "+totalD+" días");break;
                    }
                }                
                avanceU.add(p);
                unidadd.add(t.getIdUnidadMateria().getNoUnidad() + " - " + t.getIdUnidadMateria().getNombre());                
                comentario.add(c);                
            });
            rol.getDvcs().add(new DtoAvanceProgramatico(buscarPersonal(a.getDocente().getPersonal().getClave()), a.getMateria().getNombre(), unidadd, avanceU, comentario,clase,notas));
        });
    }

    public void metodoBase() {}
}
