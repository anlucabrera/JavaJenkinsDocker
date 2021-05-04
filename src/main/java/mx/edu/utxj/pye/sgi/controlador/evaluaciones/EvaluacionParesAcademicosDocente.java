package mx.edu.utxj.pye.sgi.controlador.evaluaciones;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluacionParesAcademicos;
import mx.edu.utxj.pye.sgi.dto.EvaluacionParesAcademicosRolDocente;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionParesAcademicos;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionParesAcademicos;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionParesPA;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionParesPTC;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tatisz :P
 */
@Named
@ViewScoped
public class EvaluacionParesAcademicosDocente extends ViewScopedRol implements Desarrollable {
    @EJB
    EjbPropiedades ep;
    @EJB EjbEvaluacionParesAcademicos ejbEvaluacionParesAcademicos;
    @Inject
    LogonMB logonMB;
    @Getter @Setter String valor;
    @Getter private Boolean cargado = false;
    @Getter Boolean tieneAcceso = false;
    @Getter @Setter EvaluacionParesAcademicosRolDocente rol;
    @Getter @Setter Boolean finalizado;


    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.EVALUACION_PARES_ACADEMICOS);
            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejbEvaluacionParesAcademicos.validaPersonalDocente(logonMB.getPersonal().getClave());
            //System.out.println("EvaluacionParesAcademicosDocente.init" + resValidacion.getValor());
            if(!resValidacion.getCorrecto()){tieneAcceso=false; mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo docente = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new EvaluacionParesAcademicosRolDocente(filtro, docente);
            tieneAcceso = rol.tieneAcceso(docente);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
           // if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            //if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu
            ResultadoEJB<Evaluaciones> resEv= ejbEvaluacionParesAcademicos.getEvaluacionActivaDoc();
            if(resEv.getCorrecto()){ rol.setEvaluacionActiva(resEv.getValor());}
            else {tieneAcceso= false;return;}
            //System.out.println("EV " + rol.getEvaluacionActiva());
            getApartados();
            rol.setEvaluados(new ArrayList<>());
            rol.setPocentaje(new Double(0));
            rol.setTotalDocentes(new Integer(0));
            rol.setTotalEvaluados(new Integer(0));
            resultados();
           // System.out.println("EvaluacionParesAcademicosDocente.init");
            rol.setDtoSeleccionado(rol.getEvaluados().get(0));
            if(rol.getEvaluados().isEmpty()|| rol.getEvaluados()==null){tieneAcceso=false;}

        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }
    /*
    Obtiene las preguntas según la categoria del docente autenticado
     */
    public  void  getApartados(){
        try{
            //Respuestas posibles
            rol.setRespuestasPosibles(ejbEvaluacionParesAcademicos.getRespuestasPosibles());
            //Preguntas segun su categoria
            if(rol.getPersonalDocente().getPersonal().getCategoriaOperativa().getCategoria()==30){
                //Carga apartados de PA
                rol.setPreguntas(ejbEvaluacionParesAcademicos.getApartadosPA());
               // System.out.println(rol.getPreguntas());
            }else if(rol.getPersonalDocente().getPersonal().getCategoriaOperativa().getCategoria()==32){
                //Cargar apartados de PTC
                rol.setPreguntas(ejbEvaluacionParesAcademicos.getApartadosPTC());
            }
        }catch (Exception e){ mostrarExcepcion(e); }
    }
    public void  getdocenteEvaluando(DtoEvaluacionParesAcademicos evaluando){
        rol.setDtoSeleccionado(new DtoEvaluacionParesAcademicos());
        rol.setDtoSeleccionado(evaluando);
        Ajax.update("frmEv");
        //System.out.println("Evaluado" + dtoDocenteEvaluando);
    }

    public void resultados(){
        try{
            ResultadoEJB<List<EvaluacionParesAcademicos>> resRes= ejbEvaluacionParesAcademicos.getResultadosbyEvaluador(rol.getEvaluacionActiva(),rol.getPersonalDocente());
            if(resRes.getCorrecto()){
                //Empaqueta los resultados
                ResultadoEJB<List<DtoEvaluacionParesAcademicos>> resPack= ejbEvaluacionParesAcademicos.empaquetaCombinacion(resRes.getValor());
                if(resPack.getCorrecto()){
                    rol.setEvaluados(new ArrayList<>());
                    rol.setEvaluados(resPack.getValor().stream().filter(d->d.getCombinacion().getCombinacionValidada()==true).collect(Collectors.toList()));
                    if(rol.getEvaluados().isEmpty()|| rol.getEvaluados()==null){
                        tieneAcceso=false;
                    }else {
                        tieneAcceso=true;
                        rol.setTotalDocentes(rol.getEvaluados().size());
                        rol.setTotalEvaluados((int) rol.getEvaluados().stream().filter(e->e.getCombinacion().getCompleto()==true).count());
                        System.out.println("Lista evaluados "+ rol.getEvaluados());
                        //rol.setDtoSeleccionado(rol.getEvaluados().get(0));
                        //System.out.println("Docente eva" +rol.getDtoSeleccionado().getEvaluado().getPersonal().getClave());
                        Double dte = new Double(rol.getTotalDocentes());
                        Double dc= new Double(rol.getTotalEvaluados());
                        rol.setPocentaje ((dc * 100)/dte);
                        if(rol.getTotalDocentes()==rol.getTotalEvaluados()){finalizado =true;}
                        else {finalizado =false;}
                    }
                }else {mostrarMensajeResultadoEJB(resPack);}
            }else {
                tieneAcceso=false;
                mostrarMensajeResultadoEJB(resRes);}

        }catch (Exception e){mostrarExcepcion(e);}
    }
    /**
     * Guarda resultado seleccionado
     * @param e
     * @throws ELException
     */
    public void saveRespuesta(ValueChangeEvent e) throws ELException {
       //
        // System.out.println("Entro");

        UIComponent id = (UIComponent)e.getSource();

        if(e.getNewValue() != null){
            valor = e.getNewValue().toString();
        }else{
            valor = e.getOldValue().toString();
        }
       // System.out.println("valor");
        ResultadoEJB<EvaluacionParesAcademicos> guardar=ejbEvaluacionParesAcademicos.cargarResultadosDocentes(id.getId(), valor, rol.getDtoSeleccionado().getCombinacion(), Operacion.REFRESCAR);
       // rol.getDtoSeleccionado().setCombinacion(guardar.getValor());
        ResultadoEJB<EvaluacionParesAcademicos> update=ejbEvaluacionParesAcademicos.cargarResultadosDocentes(id.getId(), valor, guardar.getValor(), Operacion.PERSISTIR);
        //rol.getDtoSeleccionado().setCombinacion(update.getValor());
        //System.out.println("2");
        if(rol.getPersonalDocente().getPersonal().getCategoriaOperativa().getCategoria()==30){
            rol.getDtoSeleccionado().setCombinacion(new EvaluacionParesAcademicos());
            rol.getDtoSeleccionado().setCombinacion(update.getValor());
            comprobarPA();
            resultados();
        }else if(rol.getPersonalDocente().getPersonal().getCategoriaOperativa().getCategoria()==32){
            rol.getDtoSeleccionado().setCombinacion(new EvaluacionParesAcademicos());
            rol.getDtoSeleccionado().setCombinacion(update.getValor());
            comprobarPTC();
            resultados();
        }
        //resultados();
    }

    /*
 Comprueba si ya termino la evaluacion (PA))
  */
    public void comprobarPA(){
        try{
            Comparador<EvaluacionParesAcademicos> comparador = new ComparadorEvaluacionParesPA();
            finalizado = comparador.isCompleto(rol.getDtoSeleccionado().getCombinacion());
            rol.getDtoSeleccionado().getCombinacion().setPromedio(ejbEvaluacionParesAcademicos.calculapromedioPA(rol.getDtoSeleccionado().getCombinacion()));
            ResultadoEJB<EvaluacionParesAcademicos> resUpdate = ejbEvaluacionParesAcademicos.updateCompleto(rol.getDtoSeleccionado().getCombinacion(),finalizado);
            rol.getDtoSeleccionado().setCombinacion(resUpdate.getValor());
            if(!resUpdate.getCorrecto()){mostrarMensajeResultadoEJB(resUpdate);}

        }catch (Exception e){mostrarExcepcion(e);}

    }
    /*
Comprueba si ya termino la evaluacion (PA))
*/
    public void comprobarPTC(){
        try{
            try{
                Comparador<EvaluacionParesAcademicos> comparador = new ComparadorEvaluacionParesPTC();
                finalizado = comparador.isCompleto(rol.getDtoSeleccionado().getCombinacion());
                rol.getDtoSeleccionado().getCombinacion().setPromedio(ejbEvaluacionParesAcademicos.calculapromedioPTC(rol.getDtoSeleccionado().getCombinacion()));
                ResultadoEJB<EvaluacionParesAcademicos> resUpdate = ejbEvaluacionParesAcademicos.updateCompleto(rol.getDtoSeleccionado().getCombinacion(),finalizado);
                rol.getDtoSeleccionado().setCombinacion(resUpdate.getValor());
                if(!resUpdate.getCorrecto()){mostrarMensajeResultadoEJB(resUpdate);}

            }catch (Exception e){mostrarExcepcion(e);}

        }catch (Exception e){mostrarExcepcion(e);}

    }


    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "evalucion pares academicos";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
}
