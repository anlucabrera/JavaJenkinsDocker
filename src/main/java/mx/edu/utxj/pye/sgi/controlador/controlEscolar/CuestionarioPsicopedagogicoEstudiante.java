package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Evaluacion;
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

@Named(value = "cuestionarioPsicopedagogicoEstudiante")
@ViewScoped
public class CuestionarioPsicopedagogicoEstudiante extends ViewScopedRol  implements Desarrollable {
    private static final long serialVersionUID = -7745875703360648941L;


    @EJB private EjbPropiedades ep;
    @Inject LogonMB logonMB;
    @EJB EjbCuestionarioPsicopedagogico ejbCuestionario;
    @Getter Boolean tieneAcceso = false;
    @Getter Boolean cargada2 =false;
    @Getter @Setter CuestionarioPsicopedagogicoRolEstudiante rol = new CuestionarioPsicopedagogicoRolEstudiante();
    @Getter @Setter String valor;

    @PostConstruct
    public void init(){
        try{
            if(logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)){
                setVistaControlador(ControlEscolarVistaControlador.CUESTIONARIO_PSICOPEDAGOGICO);

                ResultadoEJB<Estudiante> resAcceso = ejbCuestionario.validaEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
                if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
                ResultadoEJB<Estudiante> resValidacion = ejbCuestionario.validaEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
                if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
                Estudiante estudiante = resValidacion.getValor();
                tieneAcceso = rol.tieneAcceso(estudiante,UsuarioTipo.ESTUDIANTE19);
                if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
                rol.setEstudiante(estudiante);
                ResultadoEJB<Evaluaciones> resEvaluacion = ejbCuestionario.getcuestionarioActiva();
                if(!resEvaluacion.getCorrecto()) tieneAcceso = false; //Debe negarle el acceso en caso de no haber un cuestionario activo
                //----------------------------------------------------------------------------------------------------------------------------
                if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
                if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
                rol.setNivelRol(NivelRol.OPERATIVO);
                if(resEvaluacion.getCorrecto()==true){
                    rol.setEvaluacion(resEvaluacion.getValor());
                    rol.setCargada(true);
                    //TODO: Instrucciones
                    rol.getInstrucciones().add("Lee con atención las preguntas y responde con sinceridad.");
                    rol.getInstrucciones().add("En caso de ser una pregunta abierta, debes cumplir con el número de caracteres mínimo que te pide al costado de la pregunta.");
                    rol.getInstrucciones().add("En las preguntas de opción múltiple, sólo debes seleccionar una de las posibles respuestas de tu preferencia.");
                    rol.getInstrucciones().add("El sistema guarda tus respuestas automáticamente.");
                    rol.getInstrucciones().add("En la parte superior derecha, el sistema te indicará si haz terminado el cuestionario, caso contrario te lo mostrará como pendiente.");
                    rol.getInstrucciones().add("Una vez terminado el cuestionario, toma evidencia.");
                    rol.getInstrucciones().add("Cualquier error o duda, hazlo saber a tu tutor/a de grupo para que él o ella la canalice a la Coordinación de Sistemas. En caso de ser error del sistema, adjunta evidencia.");
                    //TODO: Carga los apartados y las posibles respuestas

                    rol.setApartados(ejbCuestionario.getApartados());
                    rol.setSino(ejbCuestionario.getSiNo());
                    rol.setGruposVunerabilidad(ejbCuestionario.getGruposVunerabilidad());
                    rol.setTecnicasEstudio(ejbCuestionario.getTecnicasEstudio());
                    rol.setTipoProblemaFam(ejbCuestionario.getTipoProblemaFam());
                    rol.setEstadoCivilPadres(ejbCuestionario.getEstadoCivilPadres());
                    rol.setFamFinado(ejbCuestionario.getFamFinado());
                    //TODO:Busca los resultados del estudiante
                    getResultadosEstudiante();
                    //TODO: Comprueba si el cuestionario ya esta terminado
                    comprobar();
                }

            }
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    public boolean getCuestionarioActivo(){
        cargada2  =false;
        //TODO: Verifica que haya un cuestionario activo
        //System.out.println("Entra metodo");
        ResultadoEJB<Evaluaciones> resEva = ejbCuestionario.getcuestionarioActiva();
        if(resEva.getCorrecto()==true){rol.setCargada(true);cargada2=true; rol.setEvaluacion(resEva.getValor());
        System.out.println("Cargada -->" + rol.isCargada() + "Cargada 2 --> " + cargada2);}
        else {rol.setCargada(false);mostrarMensajeResultadoEJB(resEva);}
        return cargada2;
    }
    public void getResultadosEstudiante(){
        ResultadoEJB<CuestionarioPsicopedagogicoResultados> resResultados= ejbCuestionario.getResultadosEstudiante(rol.getEstudiante(),rol.getEvaluacion());
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
    public void comprobar(){
        //System.out.println("COMPROBAR");
        Comparador<CuestionarioPsicopedagogicoResultados> comparador = new ComparadorCuestionarioPsicopedagogicoEstudiante();
        rol.setFinalizado(comparador.isCompleto(rol.getResultados()));
        //System.out.println(comparador.isCompleto(rol.getResultados()));
    }
}

