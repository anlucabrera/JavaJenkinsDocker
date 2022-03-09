package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CuestionarioPsicopedagogicoRolEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCuestionarioPsicopedagogico;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CuestionarioPsicopedagogicoResultados;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorCuestionarioPsicopedagogicoEstudiante;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Named
@ViewScoped
public class CuestionarioPsicopedagogicoEstudiante extends ViewScopedRol  implements Desarrollable {
    
    @EJB private EjbPropiedades ep;
    @Inject LogonMB logonMB;
    @EJB EjbCuestionarioPsicopedagogico ejbCuestionario;
    @Getter Boolean tieneAcceso = false;
    @Getter  Boolean finalizo =false;
    @Getter Boolean cargada = false;
    @Getter @Setter CuestionarioPsicopedagogicoRolEstudiante rol = new CuestionarioPsicopedagogicoRolEstudiante();
    @Getter @Setter String valor;

    @PostConstruct
    public void init(){
        try{
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)) return;
                cargada = Boolean.TRUE;
                setVistaControlador(ControlEscolarVistaControlador.CUESTIONARIO_PSICOPEDAGOGICO);
                ResultadoEJB<Evaluaciones> resEvento = ejbCuestionario.getcuestionarioActiva();
                    if(!resEvento.getCorrecto()) return;
                    Evaluaciones evaluacion = resEvento.getValor();
                    ResultadoEJB<Estudiante> resAcceso = ejbCuestionario.validaEstudiante(Integer.parseInt(logonMB.getCurrentUser()), evaluacion.getPeriodo());
                        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
                            ResultadoEJB<Estudiante> resValidacion = ejbCuestionario.validaEstudiante(Integer.parseInt(logonMB.getCurrentUser()), evaluacion.getPeriodo());
                            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
                                rol.setEstudiante(resValidacion.getValor());
                                tieneAcceso = rol.tieneAcceso(rol.getEstudiante(), UsuarioTipo.ESTUDIANTE19);
                                if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
                                ResultadoEJB<Boolean> resultadoEJB = ejbCuestionario.verificarCuestionario(rol.getEstudiante().getMatricula());
                                    finalizo = resultadoEJB.getValor();
                                    if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
                                    if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
                                    if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
                                    rol.setNivelRol(NivelRol.OPERATIVO);
                                    
                                    rol.setApartados(ejbCuestionario.getApartados2());
                                    rol.setSino(ejbCuestionario.getSiNo());
                                    rol.setGruposVunerabilidad(ejbCuestionario.getGruposVunerabilidad());
                                    rol.setTecnicasEstudio(ejbCuestionario.getTecnicasEstudio());
                                    rol.setTipoProblemaFam(ejbCuestionario.getTipoProblemaFam());
                                    rol.setEstadoCivilPadres(ejbCuestionario.getEstadoCivilPadres());
                                    rol.setFamFinado(ejbCuestionario.getFamFinado());
                                    //Busca los resultados del estudiante
                                    ResultadoEJB<CuestionarioPsicopedagogicoResultados> resCuestionario = ejbCuestionario.getResultadosEstudiante(rol.getEstudiante(), evaluacion);
                                    rol.setResultados(resCuestionario.getValor());
                                    
//                                    getResultadosEstudiante();
                                    comprobar();
                                    rol.getInstrucciones().add("Lee con atención las preguntas y responde con sinceridad.");
                                    rol.getInstrucciones().add("En caso de ser una pregunta abierta, debes cumplir con el número de caracteres mínimo que te pide al costado de la pregunta.");
                                    rol.getInstrucciones().add("En las preguntas de opción múltiple, sólo debes seleccionar una de las posibles respuestas de tu preferencia.");
                                    rol.getInstrucciones().add("El sistema guarda tus respuestas automáticamente.");
                                    rol.getInstrucciones().add("En la parte superior derecha, el sistema te indicará si haz terminado el cuestionario, caso contrario te lo mostrará como pendiente.");
                                    rol.getInstrucciones().add("Una vez terminado el cuestionario, toma evidencia.");
                                    rol.getInstrucciones().add("Cualquier error o duda, hazlo saber a tu tutor/a de grupo para que él o ella la canalice a la Coordinación de Sistemas. En caso de ser error del sistema, adjunta evidencia.");
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    
    public void getResultadosEstudiante(){
        ResultadoEJB<CuestionarioPsicopedagogicoResultados> resResultados = ejbCuestionario.getResultadosEstudiante(rol.getEstudiante(),rol.getEvaluacion());
        if(resResultados.getCorrecto()==true){
            rol.setResultados(resResultados.getValor());
        }else { mostrarMensajeResultadoEJB(resResultados); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "cuestionarioPsicopedagogico";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }

    /**
     * Guarda la respuesta
     * @param e
     * @throws ELException
     */
    public void guardar(ValueChangeEvent e) throws ELException{

        UIComponent id = (UIComponent)e.getSource();

        if(e.getNewValue() != null){
            valor = e.getNewValue().toString();
        }else{
            valor = e.getOldValue().toString();
        }
        // System.out.println("id " + id.getId());
        //System.out.println("valor " + valor);
        ResultadoEJB<CuestionarioPsicopedagogicoResultados> refrescar=ejbCuestionario.cargaResultadosCuestionarioPsicopedagogico(id.getId(), valor, rol.getResultados(), Operacion.REFRESCAR);
        ResultadoEJB<CuestionarioPsicopedagogicoResultados> save=ejbCuestionario.cargaResultadosCuestionarioPsicopedagogico(id.getId(), valor, rol.getResultados(), Operacion.PERSISTIR);
        comprobar();

    }

    /**
     * Comprueba si el cuestionario ha sido terminado por el estudiante
     */
    public void comprobar(){
        //System.out.println("COMPROBAR");
        Comparador<CuestionarioPsicopedagogicoResultados> comparador = new ComparadorCuestionarioPsicopedagogicoEstudiante();
        rol.setFinalizado(comparador.isCompleto(rol.getResultados()));
//        finalizo =comparador.isCompleto(rol.getResultados());
        if(rol.isFinalizado()==true){
            ResultadoEJB<CuestionarioPsicopedagogicoResultados> resActualiza = ejbCuestionario.actualizarCompleto(rol.getResultados());
            if(resActualiza.getCorrecto()==true){rol.setResultados(resActualiza.getValor());}//tieneAcceso=false;}
            else {mostrarMensajeResultadoEJB(resActualiza);}
        }
        //Ajax.update("tb2");

       // System.out.println(comparador.isCompleto(rol.getResultados()));
        //System.out.println("CuestionarioPsicopedagogicoEstudiante.comprobar"+ rol.isFinalizado() );
    }
}

