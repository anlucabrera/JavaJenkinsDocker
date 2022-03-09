package mx.edu.utxj.pye.sgi.controlador.evaluaciones;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoEstudianteMateria;
import mx.edu.utxj.pye.sgi.dto.dtoEstudiantesEvalauciones;
import mx.edu.utxj.pye.sgi.ejb.EJBAdimEstudianteBase;
import mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDocente2;
import mx.edu.utxj.pye.sgi.entity.ch.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.el.ELException;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluaciones;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;

/**
 * @author  Taatisz :P
 */
@Named
@ViewScoped
public class EvaluacionDocenteEstudiante extends ViewScopedRol implements Desarrollable{
    @Getter @Setter private DtoEvaluaciones rol = new DtoEvaluaciones();
    @Getter private Boolean cargada = false;
    @Getter private Boolean finalizado = false;
    @Getter @Setter private Boolean tieneAcceso = false;
    @Getter @Setter Boolean estudianteSauiit, estudianteCE;
    
    @Getter private PeriodosEscolares periodoEscolar;
    @Getter @Setter int totalDocentes, evaluados2;
    @Getter @Setter double porcentaje;
    @Getter @Setter dtoEstudianteMateria dtoDocenteEvaluando;
    
    @Inject LogonMB logonMB;
    @EJB EjbEvaluacionDocente2 ejbEvaluacionDocente2;
    @EJB EJBAdimEstudianteBase ejbAdminEstudiante;
    @EJB EjbPropiedades ep;

    @PostConstruct
    public  void  init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19) && !logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE)) return;
            cargada = true;
            setVistaControlador(ControlEscolarVistaControlador.EVALUACION_DOCENTE2);
            //Se busca evaluacion activa
            ResultadoEJB<Evaluaciones> resEvaluacion = ejbEvaluacionDocente2.getEvDocenteActiva();
            if(!resEvaluacion.getCorrecto()) return;
            rol.evaluacion = resEvaluacion.getValor();
            ResultadoEJB<dtoEstudiantesEvalauciones> resValidacion = ejbAdminEstudiante.getClaveEstudiante(logonMB.getCurrentUser(), rol.evaluacion.getPeriodo());
            //System.out.println("Resultado EJB" + resValidacion.getValor().getMatricula());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
            rol.setDtoEstudiante(resValidacion.getValor());
            //System.out.println("Dto Estudiante:" + rol.getDtoEstudiante().getMatricula());
            if(rol.getDtoEstudiante().getGrado() == 6 || rol.getDtoEstudiante().getGrado() == 11) return;
            tieneAcceso = rol.tieneAcceso(rol.getDtoEstudiante());
            if(rol.getDtoEstudiante().getEstudianteCE() != null) {
                estudianteCE = true;
            }
            if (rol.getDtoEstudiante().getEstudianteSaiiut() != null) {
                estudianteSauiit = true;
            }
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            //if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            rol.setNivelRol(NivelRol.OPERATIVO);
            ResultadoEJB<List<dtoEstudianteMateria>> resMaterias = ejbEvaluacionDocente2.getMateriasbyEstudiante(rol.getDtoEstudiante(), rol.getEvaluacion());
            if(!resMaterias.getCorrecto()) return;
            rol.setListaMaterias(resMaterias.getValor());
            rol.setRespuestas(new HashMap<>());
            dtoDocenteEvaluando = new dtoEstudianteMateria();
            if(rol.evaluacion.getTipo().equals(EvaluacionesTipo.DOCENTE.getLabel())){
                rol.tipoEvaluacion = 1;
                rol.listaResultados = obtenerResultados(rol.tipoEvaluacion, rol.getDtoEstudiante(), rol.getEvaluacion(), rol.getRespuestas());
                porcentaje = obtenerAvance(rol.listaResultados);
                dtoDocenteEvaluando = rol.listaResultados.get(0);
                ejbEvaluacionDocente2.obtenerResultadoEvaluacionTipo1(rol.getDtoEstudiante(), dtoDocenteEvaluando, rol.getEvaluacion(), rol.getRespuestas());
            }
            if(rol.evaluacion.getTipo().equals(EvaluacionesTipo.DOCENTE_2.getLabel())){
                rol.tipoEvaluacion = 2;
                rol.listaResultados = obtenerResultados(rol.tipoEvaluacion, rol.getDtoEstudiante(), rol.getEvaluacion(), rol.getRespuestas());
                porcentaje = obtenerAvance(rol.listaResultados);
                dtoDocenteEvaluando = rol.listaResultados.get(0);
                ejbEvaluacionDocente2.obtenerResultadoEvaluacionTipo2(rol.getDtoEstudiante(), dtoDocenteEvaluando, rol.getEvaluacion(), rol.getRespuestas());
            }
            if(rol.evaluacion.getTipo().equals(EvaluacionesTipo.DOCENTE_3.getLabel())){
                rol.tipoEvaluacion = 3;
                rol.listaResultados = obtenerResultados(rol.tipoEvaluacion, rol.getDtoEstudiante(), rol.getEvaluacion(), rol.getRespuestas());
                porcentaje = obtenerAvance(rol.listaResultados);
                dtoDocenteEvaluando = rol.listaResultados.get(0);
                ejbEvaluacionDocente2.obtenerResultadoEvaluacionTipo3(rol.getDtoEstudiante(), dtoDocenteEvaluando, rol.getEvaluacion(), rol.getRespuestas());
            }
            if(rol.evaluacion.getTipo().equals(EvaluacionesTipo.DOCENTE_4.getLabel())){
                rol.tipoEvaluacion = 4;
                rol.listaResultados = obtenerResultados(rol.tipoEvaluacion, rol.getDtoEstudiante(), rol.getEvaluacion(), rol.getRespuestas());
                porcentaje = obtenerAvance(rol.listaResultados);
                dtoDocenteEvaluando = rol.listaResultados.get(0);
                ejbEvaluacionDocente2.obtenerResultadoEvaluacionTipo4(rol.getDtoEstudiante(), dtoDocenteEvaluando, rol.getEvaluacion(), rol.getRespuestas());
            }
            
        } catch (Exception e) {mostrarExcepcion(e);}
    }
    
    public List<dtoEstudianteMateria> obtenerResultadosTipo4(){
        dtoEstudiantesEvalauciones estudiante = rol.getDtoEstudiante();
        Evaluaciones evaluacion = rol.getEvaluacion();
        List<dtoEstudianteMateria> resultados = rol.getListaMaterias()
                .stream()
                .map(dto -> pack(dto, 4, estudiante, evaluacion, rol.getRespuestas()))
                .distinct()
                .collect(Collectors.toList());
        return resultados;
    }
    
    public List<dtoEstudianteMateria> obtenerResultados(Integer tipo, dtoEstudiantesEvalauciones estudiante, Evaluaciones evaluacion, Map<String, String> respuestas){
        List<dtoEstudianteMateria> resultados;
        resultados = rol.getListaMaterias()
                .stream()
                .map(dto -> pack(dto, tipo, estudiante, evaluacion, respuestas))
                .distinct()
                .collect(Collectors.toList());
        return resultados;
    }
    
    public dtoEstudianteMateria pack(dtoEstudianteMateria m, Integer tipo, dtoEstudiantesEvalauciones estudiante, Evaluaciones evaluacion, Map<String, String> respuestas){
        dtoEstudianteMateria dto = new dtoEstudianteMateria();
        if(tipo.equals(1)){
            EvaluacionDocentesMateriaResultados2 resultados2;
            ResultadoEJB<EvaluacionDocentesMateriaResultados2> resResultados = ejbEvaluacionDocente2.obtenerResultadoEvaluacionTipo1(estudiante, m, evaluacion, respuestas);
            if(!resResultados.getCorrecto()) return new dtoEstudianteMateria();
            resultados2 = resResultados.getValor();
            dto.setResultados2(resultados2);
        }
        if(tipo.equals(2)){
            EvaluacionDocentesMateriaResultados3 resultados2;
            ResultadoEJB<EvaluacionDocentesMateriaResultados3> resResultados = ejbEvaluacionDocente2.obtenerResultadoEvaluacionTipo2(estudiante, m, evaluacion, respuestas);
            if(!resResultados.getCorrecto()) return new dtoEstudianteMateria();
            resultados2 = resResultados.getValor();
            dto.setResultadosTipo2(resultados2);
        }
        if(tipo.equals(3)){
            EvaluacionDocentesMateriaResultados4 resultados3;
            ResultadoEJB<EvaluacionDocentesMateriaResultados4> resResultados = ejbEvaluacionDocente2.obtenerResultadoEvaluacionTipo3(estudiante, m, evaluacion, respuestas);
            if(!resResultados.getCorrecto()) return new dtoEstudianteMateria();
            resultados3 = resResultados.getValor();
            dto.setResultadosTipo4(resultados3);
        }
        if(tipo.equals(4)){
            EvaluacionDocentesMateriaResultados5 resultado5;
            ResultadoEJB<EvaluacionDocentesMateriaResultados5> resResultado = ejbEvaluacionDocente2.obtenerResultadoEvaluacionTipo4(estudiante, m, evaluacion, respuestas);
            if (!resResultado.getCorrecto()) return new dtoEstudianteMateria();
            resultado5 = resResultado.getValor();
            dto.setResultadosTipo5(resultado5);
        }
        
        dto.setNombreMateria(m.getNombreMateria());
        dto.setClaveMateria(m.getClaveMateria());
        dto.setDocenteImparte(m.getDocenteImparte());
        return dto;
    }
    
    public List<Apartado> obtenerApartados(){
        List<Apartado> apartados = new ArrayList<>();
        if(rol.tipoEvaluacion.equals(1)){
            apartados = ejbEvaluacionDocente2.getApartados();
        }
        if(rol.tipoEvaluacion.equals(2)){
            apartados = ejbEvaluacionDocente2.getApartadosContingencia();
        }
        if(rol.tipoEvaluacion.equals(3)){
            apartados = ejbEvaluacionDocente2.getApartadosContingenciaCuestionario2();
        }
        if(rol.tipoEvaluacion.equals(4)){
            apartados = ejbEvaluacionDocente2.getApartadosContingenciaCuestionario3();
        }
        return apartados;
    }
    
    public List<SelectItem> obtenerItems(){
        return ejbEvaluacionDocente2.getRespuestasPosibles();
    }
    
    public Boolean completo(dtoEstudianteMateria evaluado){
        Boolean completo = false;
        if(rol.tipoEvaluacion.equals(1)){
            completo = evaluado.getResultados2().getCompleto();
        }
        if(rol.tipoEvaluacion.equals(2)){
            completo = evaluado.getResultadosTipo2().getCompleto();
        }
        if(rol.tipoEvaluacion.equals(3)){
            completo = evaluado.getResultadosTipo4().getCompleto();
        }
        if(rol.tipoEvaluacion.equals(4)){
            completo = evaluado.getResultadosTipo5().getCompleto();
        }
        return completo;
    }
    
    public void guardarRespuesta(ValueChangeEvent cve) throws ELException{
        UIComponent origen = (UIComponent) cve.getSource();
        if(cve.getNewValue() != null){
            rol.setValor(cve.getNewValue().toString());
        }else{
            rol.setValor(null);
        }
        if(rol.getTipoEvaluacion().equals(1)){
            ejbEvaluacionDocente2.actualizarRespuestaPregunta2(dtoDocenteEvaluando.getResultados2(), origen.getId(), rol.getValor(), rol.getRespuestas());
            ejbEvaluacionDocente2.comprobarResultado(dtoDocenteEvaluando.getResultados2());
        }
        if(rol.getTipoEvaluacion().equals(2)){
            ejbEvaluacionDocente2.actualizarRespuestaPregunta3(dtoDocenteEvaluando.getResultadosTipo2(), origen.getId(), rol.getValor(), rol.getRespuestas());
            ejbEvaluacionDocente2.comprobarResultado2(dtoDocenteEvaluando.getResultadosTipo2());
        }
        if(rol.getTipoEvaluacion().equals(3)){
            ejbEvaluacionDocente2.actualizarRespuestaPregunta4(dtoDocenteEvaluando.getResultadosTipo4(), origen.getId(), rol.getValor(), rol.getRespuestas());
            ejbEvaluacionDocente2.comprobarResultado3(dtoDocenteEvaluando.getResultadosTipo4());
        }
        if(rol.getTipoEvaluacion().equals(4)){
            ejbEvaluacionDocente2.actualizarRespuestaPregunta5(dtoDocenteEvaluando.getResultadosTipo5(), origen.getId(), rol.getValor(), rol.getRespuestas());
            ejbEvaluacionDocente2.comprobarResultado4(dtoDocenteEvaluando.getResultadosTipo5());
        }
        porcentaje = obtenerAvance(rol.listaResultados);
    }
    
    public void  getdocenteEvaluando(dtoEstudianteMateria evaluando){
        rol.setRespuestas(new HashMap<>());
        dtoDocenteEvaluando = new dtoEstudianteMateria();
        dtoDocenteEvaluando = evaluando;
        if(rol.getTipoEvaluacion().equals(1)){
            ejbEvaluacionDocente2.obtenerResultadoEvaluacionTipo1(rol.getDtoEstudiante(), dtoDocenteEvaluando, rol.getEvaluacion(), rol.getRespuestas());
        }
        if(rol.getTipoEvaluacion().equals(2)){
            ejbEvaluacionDocente2.obtenerResultadoEvaluacionTipo2(rol.getDtoEstudiante(), dtoDocenteEvaluando, rol.getEvaluacion(), rol.getRespuestas());
        }
        if(rol.getTipoEvaluacion().equals(3)){
            ejbEvaluacionDocente2.obtenerResultadoEvaluacionTipo3(rol.getDtoEstudiante(), dtoDocenteEvaluando, rol.getEvaluacion(), rol.getRespuestas());
        }
        if(rol.getTipoEvaluacion().equals(4)){
            ejbEvaluacionDocente2.obtenerResultadoEvaluacionTipo4(rol.getDtoEstudiante(), dtoDocenteEvaluando, rol.getEvaluacion(), rol.getRespuestas());
        }
        
    }
    
    public Double obtenerAvance(List<dtoEstudianteMateria> lista){
        if(rol.tipoEvaluacion.equals(1)){
            rol.evaluados = lista.stream().filter(docente -> docente.getResultados2().getCompleto()).collect(Collectors.toList()).size();
        }
        if(rol.tipoEvaluacion.equals(2)){
            rol.evaluados = lista.stream().filter(docente -> docente.getResultadosTipo2().getCompleto()).collect(Collectors.toList()).size();
        }
        if(rol.tipoEvaluacion.equals(3)){
            rol.evaluados = lista.stream().filter(docente -> docente.getResultadosTipo4().getCompleto()).collect(Collectors.toList()).size();
        }
        if(rol.tipoEvaluacion.equals(4)){
            rol.evaluados = lista.stream().filter(docente -> docente.getResultadosTipo5().getCompleto()).collect(Collectors.toList()).size();
        }
        rol.totalDocentes = lista.size();
        Double dte = new Double(rol.totalDocentes);
        Double dc= new Double(rol.evaluados);
        if(rol.totalDocentes.equals(rol.evaluados)){finalizado =true;}
        else {finalizado =false;}
        return (dc * 100) / dte;
    }
    
    public void ObtenerElNumeroSlide() throws InterruptedException {
        rol.setSlideActivo(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("model"));
        //System.out.println("Slide activo:" + dto.slideActivo);
        try {
            //renderizarTabla(dto.getSlideActivo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "evaluacion docente materia";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
////        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
}
