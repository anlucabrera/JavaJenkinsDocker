package mx.edu.utxj.pye.sgi.controlador.evaluaciones;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluacionParesAcademicos;
import mx.edu.utxj.pye.sgi.dto.EvaluacionParesAcademicosRolFortalecimiento;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionParesAcademicos;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionParesAcademicos;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Tatisz :P
 */
@Named
@ViewScoped
public class EvaluacionParesAcademicosFortalecimiento extends ViewScopedRol implements Desarrollable {
    @EJB
    EjbPropiedades ep;
    @EJB EjbEvaluacionParesAcademicos ejbEvaluacionParesAcademicos;
    @Inject
    LogonMB logonMB;
    @Getter private Boolean cargado = false;
    @Getter Boolean tieneAcceso = false;
    @Getter @Setter EvaluacionParesAcademicosRolFortalecimiento rol;

    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.COMBINACIONES_EVALUACION_PARES_ACADEMICOS);

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejbEvaluacionParesAcademicos.validaPersonalFortalecimiento(logonMB.getPersonal().getClave());
            if (!resValidacion.getCorrecto()) { mostrarMensajeResultadoEJB(resValidacion);return; }
            //cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personalFDA = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new EvaluacionParesAcademicosRolFortalecimiento(filtro);
            tieneAcceso = rol.tieneAcceso(personalFDA);
            if (!tieneAcceso) { rol.setFiltro(resValidacion.getValor());tieneAcceso = rol.tieneAcceso(personalFDA); }
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu
            ResultadoEJB<Evaluaciones> resEv= ejbEvaluacionParesAcademicos.getUltimaEvaluacionActiva();
            if(resEv.getCorrecto()){rol.setEvaluacion(resEv.getValor());}
           // System.out.println("Evaluacion "+ resEv.getValor());
            ResultadoEJB<Evaluaciones> resEvAct = ejbEvaluacionParesAcademicos.getEvaluacionActiva(rol.getEvaluacion());
            rol.setEvaluacionActiva(new Evaluaciones());
            rol.setEvaluacionActiva(resEvAct.getValor());
           // System.out.println("act "+ resEvAct.getValor());
           //if(resEv.getCorrecto()){rol.}
            //System.out.println("1");
            // Lista de evaluaciones
            ResultadoEJB<List<Evaluaciones>> resEvaluaciones= ejbEvaluacionParesAcademicos.getEvaluaciones();
            if(resEvaluaciones.getCorrecto()){rol.setEvaluaciones(resEvaluaciones.getValor());}
            else {rol.setEvaluaciones(new ArrayList<>());}
           // System.out.println("22" + resEvaluaciones.getValor().size());

            ResultadoEJB<List<AreasUniversidad>> resAreas = ejbEvaluacionParesAcademicos.getAreasAcademicas();
            if(resAreas.getCorrecto()){rol.setAreas(resAreas.getValor());rol.setAreaSeleccionada(resAreas.getValor().get(0));getCombinacionesbyArea();}
            else {mostrarMensajeResultadoEJB(resAreas);}
           // System.out.println("22" + resAreas.getValor().size());

            rol.getInstrucciones().add("Para generar nuevas combinaciones, es necesario que seleccione un área");
            rol.getInstrucciones().add("El sistema sólo permite editar, eliminar o validar las combinaciones que no han sido validadas anteriormente");
            rol.getInstrucciones().add("Sólo puede generar nuevas combinaciones si existe una evaluación activa");
        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }

    public void getCombinacionesbyEvaluacion(){
        try{
            rol.setAreaSeleccionada(rol.getAreas().get(0));
            getCombinacionesbyArea();
            Ajax.update("frmEvaluaciones");
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Obtiene las combinaciones por área y por evaluacion seleccionada
     */

    public void getCombinacionesbyArea(){
        try{
            rol.setCombinaciones(new ArrayList<>());
            //System.out.println("Area que recibe  "+ rol.getAreaSeleccionada().getArea());
            ResultadoEJB<List<DtoEvaluacionParesAcademicos>> resCombinaciones= ejbEvaluacionParesAcademicos.generaCombinacionesbyArea(rol.getAreaSeleccionada(),rol.getEvaluacion());
            if(resCombinaciones.getCorrecto()){
                rol.setCombinaciones(resCombinaciones.getValor());
            }else {mostrarMensajeResultadoEJB(resCombinaciones);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Actualiza combinación
     */

    public void  updateCombinacion(){
        try{
           // System.out.println("Clave combinación evaluado - >"+ rol.getDtoSeleccionado().getCombinacion().getEvaluacionParesAcademicosPK().getEvaluado()+ " Dto Evaluado "+ rol.getDocenteEvaluadoUpdate().getPersonal().getClave());
            ResultadoEJB<EvaluacionParesAcademicos> resUpdate = ejbEvaluacionParesAcademicos.operacionCombinacion(rol.getEvaluacion(),rol.getDtoSeleccionado(),rol.getDocenteEvaluadoUpdate(), Operacion.ACTUALIZAR);
            if(resUpdate.getCorrecto()){
                ResultadoEJB<List<DtoEvaluacionParesAcademicos>> resCombinaciones= ejbEvaluacionParesAcademicos.generaCombinacionesbyArea(rol.getDtoSeleccionado().getEvaluador().getAreaSuperior(),rol.getEvaluacion());
                if(resCombinaciones.getCorrecto()){
                    rol.setCombinaciones(resCombinaciones.getValor());
                }else {mostrarMensajeResultadoEJB(resCombinaciones);}
                mostrarMensajeResultadoEJB(resUpdate);

            }else {mostrarMensajeResultadoEJB(resUpdate);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Elimina una combinación
     */
    public void deleteCombinacion(DtoEvaluacionParesAcademicos combinacion){
        try{
            rol.setDtoSeleccionado(combinacion);
            ResultadoEJB<EvaluacionParesAcademicos> resDelete = ejbEvaluacionParesAcademicos.operacionCombinacion(rol.getEvaluacion(),rol.getDtoSeleccionado(),rol.getDtoSeleccionado().getEvaluado(), Operacion.ELIMINAR);
            if(resDelete.getCorrecto()){
                ResultadoEJB<List<DtoEvaluacionParesAcademicos>> resCombinaciones= ejbEvaluacionParesAcademicos.generaCombinacionesbyArea(rol.getDtoSeleccionado().getEvaluador().getAreaSuperior(),rol.getEvaluacion());
                if(resCombinaciones.getCorrecto()){
                    rol.setCombinaciones(resCombinaciones.getValor());
                }else {mostrarMensajeResultadoEJB(resCombinaciones);}
                mostrarMensajeResultadoEJB(resDelete);
            }
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Guarda una nueva combinación
     */
    public void saveCombinacion(){
        try{
            ResultadoEJB<EvaluacionParesAcademicos> resSave = ejbEvaluacionParesAcademicos.getCombinacionEvaluadorEvaluado(rol.getEvaluacion(),rol.getNuevaCombinacion().getEvaluador(),rol.getNuevaCombinacion().getEvaluado());
            if(resSave.getCorrecto()){
                ResultadoEJB<List<DtoEvaluacionParesAcademicos>> resCombinaciones= ejbEvaluacionParesAcademicos.generaCombinacionesbyArea(rol.getNuevaCombinacion().getEvaluador().getAreaSuperior(),rol.getEvaluacion());
                if(resCombinaciones.getCorrecto()){
                    rol.setCombinaciones(resCombinaciones.getValor());
                }else {mostrarMensajeResultadoEJB(resCombinaciones);}
                mostrarMensajeResultadoEJB(resSave);
            }else {mostrarMensajeResultadoEJB(resSave);}

        }catch (Exception e ){mostrarExcepcion(e);}
    }
    /*
    Validar combinaciones
     */
    public  void validaCombinacion(DtoEvaluacionParesAcademicos combinacion){
        try{
            rol.setDtoSeleccionado(combinacion);
            ResultadoEJB<EvaluacionParesAcademicos> resValida = ejbEvaluacionParesAcademicos.validarCombinaciones(rol.getEvaluacion(),rol.getDtoSeleccionado());
            if(resValida.getCorrecto()){
                ResultadoEJB<List<DtoEvaluacionParesAcademicos>> resCombinaciones= ejbEvaluacionParesAcademicos.generaCombinacionesbyArea(rol.getDtoSeleccionado().getEvaluador().getAreaSuperior(),rol.getEvaluacion());
                if(resCombinaciones.getCorrecto()){
                    rol.setCombinaciones(resCombinaciones.getValor());
                    Ajax.update("frmEvaluaciones");
                }else {mostrarMensajeResultadoEJB(resCombinaciones);}
                mostrarMensajeResultadoEJB(resValida);
            }else {mostrarMensajeResultadoEJB(resValida);}

        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Valida lista de combienaciones
     */
    public void validarListaCombinaciones(){
        try{
            rol.getCombinaciones().stream().forEach(c->{
                ResultadoEJB<EvaluacionParesAcademicos> resValida = ejbEvaluacionParesAcademicos.validarCombinaciones(rol.getEvaluacion(),c);
                rol.setDtoSeleccionado(c);
            });
            ResultadoEJB<List<DtoEvaluacionParesAcademicos>> resCombinaciones= ejbEvaluacionParesAcademicos.generaCombinacionesbyArea(rol.getDtoSeleccionado().getEvaluador().getAreaSuperior(),rol.getEvaluacion());
            if(resCombinaciones.getCorrecto()){
                rol.setCombinaciones(resCombinaciones.getValor());
                Ajax.update("frmEvaluaciones");
            }else {mostrarMensajeResultadoEJB(resCombinaciones);}
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Se han validado las combinaciones"));
        }catch (Exception e){}
    }
    /*
    Obtiene lista de docentes de la misma categoría y área del evaluador
     */
    public void getDocentesParesbyArea(@NonNull PersonalActivo evaluador){
        try{
            ResultadoEJB<List<PersonalActivo>> resDocentes= ejbEvaluacionParesAcademicos.buscarDocente(evaluador);
            if(resDocentes.getCorrecto()){
                rol.setDocentesPares(resDocentes.getValor());
                mostrarMensajeResultadoEJB(resDocentes);
            }else {mostrarMensajeResultadoEJB(resDocentes);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void  getCombinaciónEdit(DtoEvaluacionParesAcademicos combinacion){
        try{
            rol.setDtoSeleccionado(combinacion);
            rol.setDocenteEvaluadoUpdate(combinacion.getEvaluado());
            getDocentesParesbyArea(combinacion.getEvaluador());
            Ajax.update("frmEditCombinacion");
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    public void getCombinacionNueva(){
        try{
            rol.setAreaSelect(rol.getAreas().get(0));
            getPersonalbyAreaSeleccionada();


        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void  getPersonalbyAreaSeleccionada(){
        try{
            //System.out.println("Area seleccionada "+ rol.getAreaSelect().getArea());
            ResultadoEJB<List<PersonalActivo>> resPer = ejbEvaluacionParesAcademicos.getPersonalDocentebyArea(rol.getAreaSelect());
            if(resPer.getCorrecto()){
                DtoEvaluacionParesAcademicos dto = new DtoEvaluacionParesAcademicos();
               // System.out.println("Lista docentes "+ resPer.getValor().size());
                rol.setDocentesArea(resPer.getValor());
                dto.setEvaluador(resPer.getValor().get(0));
                rol.setNuevaCombinacion(dto);
                //System.out.println("Evaluador -->" +rol.getNuevaCombinacion().getEvaluador());
                getDocentesParesbyEvaluador();
                Ajax.update("frmNewCombinación");
            }else {mostrarMensajeResultadoEJB(resPer);}

        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void  getDocentesParesbyEvaluador(){
        try{
            ResultadoEJB<List<PersonalActivo>> resPares= ejbEvaluacionParesAcademicos.buscarDocente(rol.getNuevaCombinacion().getEvaluador());
            if(resPares.getCorrecto()){
                List<PersonalActivo> docentes= new ArrayList<>();
                docentes = resPares.getValor();
                rol.getNuevaCombinacion().setEvaluado(docentes.get(0));
                rol.setDocentesPares(docentes);
                rol.getNuevaCombinacion().setEvaluado(rol.getDocentesPares().get(0));
            }else {mostrarMensajeResultadoEJB(resPares);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "generacion combinaciones evaluacion pares academicos";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
}
