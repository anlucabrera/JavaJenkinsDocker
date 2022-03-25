/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.evaluaciones;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.DtoAdministracionEvaluacionesRolPersonal;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluacion;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacion360Promedios;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacionDesempenioPromedios;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacionDocenteMateria;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacionTutorPromedios;
import mx.edu.utxj.pye.sgi.dto.ListadoGeneralEvaluacionesPersonal;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas;
import mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacion3601;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultados;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados3;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados4;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados5;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados2;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados3;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360Resultados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Calculable;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionDesempenio;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import mx.edu.utxj.pye.sgi.funcional.PromediarDesempenio;
import mx.edu.utxj.pye.sgi.funcional.PromediarTutor;
import mx.edu.utxj.pye.sgi.util.ExcelWritter;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

/**
 *
 * @author Planeacion
 */
@Named
@ViewScoped
public class AdministracionEvaluacionesPersonal extends ViewScopedRol implements Desarrollable{
    @Getter @Setter DtoAdministracionEvaluacionesRolPersonal rol;
    @Getter @Setter private Boolean cargado;
    @Getter @Setter private Boolean tieneAcceso;
    @EJB EjbPropiedades ep;
    @EJB EjbAdministracionEncuestas ejbAdmin;
    @EJB private EjbEvaluacion3601 ejbEvaluacion360;
    @EJB EJBSelectItems ejbSI;
    
    @Inject LogonMB logonMB;
    
    @PostConstruct
    public void init(){
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.ADMINISTRACION_EVALUACIONES);
            
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbAdmin.validarPersonal(logonMB.getPersonal().getClave());
            
            if(!resAcceso.getCorrecto()){mostrarMensajeResultadoEJB(resAcceso);return;}
            
            Filter<PersonalActivo> filtro = resAcceso.getValor();
            PersonalActivo psicopedagogia = filtro.getEntity();
            rol = new DtoAdministracionEvaluacionesRolPersonal(filtro, psicopedagogia);
            tieneAcceso = rol.tieneAcceso(psicopedagogia);
            
            if(!tieneAcceso){return;}
            rol.setPersonalActivo(psicopedagogia);
                        
            if(verificarInvocacionMenu()) return;
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.CONSULTA);
            rol.setRenderSelectPeGlobal(Boolean.FALSE);
            rol.setRenderDTB360(Boolean.FALSE);
            rol.setRenderDTBDesempenio(Boolean.FALSE);
            rol.setRenderDTBDocente(Boolean.FALSE);
            rol.setRenderBtnResultados(Boolean.FALSE);
            rol.setRenderDTBResGral(Boolean.FALSE);
            rol.setRenderBtnDownloadCedulas360(Boolean.FALSE);
            rol.setRenderBtnDownloadCedulasDes(Boolean.FALSE);
            rol.setRenderBtnDownloadRepGral(Boolean.FALSE);
            
    }
    
    public List<SelectItem> obtenerEvaluaciones(){
        List<SelectItem> lista = ejbSI.itemsEvaluacionPersonal();
        return lista;
    }
    
    public List<PeriodosEscolares> obtenerPeriodos(){
        ResultadoEJB<List<PeriodosEscolares>> lista = ejbAdmin.obtenerPeriodos();
        if(!lista.getCorrecto()) return Collections.EMPTY_LIST;
        return lista.getValor();
    } 
    
    public void obtenerPeriodosEvaluacion(){
        rol.setRenderBtnResultados(Boolean.FALSE);
        rol.setRenderBtnDownloadCedulas360(Boolean.FALSE);
        rol.setRenderBtnDownloadCedulasDes(Boolean.FALSE);
        rol.setRenderBtnDownloadRepGral(Boolean.FALSE);
        rol.setRenderDTB360(Boolean.FALSE);
        rol.setRenderDTBDesempenio(Boolean.FALSE);
        rol.setRenderDTBDocente(Boolean.FALSE);
        rol.setRenderDTBTutor(Boolean.FALSE);
        rol.setRenderDTBResGral(Boolean.FALSE);
        rol.setRenderSelectPeGlobal(Boolean.FALSE);
        rol.setDtoListaEv360(new ArrayList<>());
        rol.setDtoListaEvDes(new ArrayList<>());
        rol.setDtoListaEvDoc(new ArrayList<>());
        rol.setDtoListaEvTutor(new ArrayList<>());
        rol.setDtoListaGeneralResultados(new ArrayList<>());
        rol.setListaEvaluaciones(new ArrayList<>());
        rol.setPeSelect(0);
        switch(rol.getEvSelect()){
            case 1:
                rol.setTipoEvalaucion("360°");
                rol.setListaEvaluaciones(ejbAdmin.obtenerEvaluaciones360().getValor());
                rol.setRenderBtnResultados(Boolean.TRUE);
                //System.out.println("Evaluacion seleccionada: " + rol.getTipoEvalaucion());
                break;
            case 2:
                rol.setTipoEvalaucion("Desempenio");
                rol.setListaEvaluaciones(ejbAdmin.obtenerEvaluacionesDesempenio().getValor());
                rol.setRenderBtnResultados(Boolean.TRUE);
                //System.out.println("Evaluacion seleccionada: " + rol.getTipoEvalaucion());
                break;
            case 3:
                rol.setTipoEvalaucion("Tutor");
                rol.setListaEvaluaciones(ejbAdmin.obtenerEvaluaciones(rol.getTipoEvalaucion()).getValor());
                rol.getListaEvaluaciones().sort(Comparator.comparingInt(DtoEvaluacion::getPeriodo).reversed());
                rol.setRenderBtnResultados(Boolean.TRUE);
                //System.out.println("Evaluacion seleccionada: " + rol.getTipoEvalaucion());
                break;
            case 4:
                rol.setTipoEvalaucion("Docente");
                rol.setListaEvaluaciones(ejbAdmin.obtenerEvaluaciones(rol.getTipoEvalaucion()).getValor());
                rol.getListaEvaluaciones().sort(Comparator.comparingInt(DtoEvaluacion::getPeriodo).reversed());
                rol.setRenderBtnResultados(Boolean.TRUE);
                //System.out.println("Evaluacion seleccionada: " + rol.getTipoEvalaucion());
                break;
        }
    }
    
    public void generarResultados(){
        if(rol.getPeSelect().equals(0)) return;
        rol.setDtoListaEv360(new ArrayList<>());
        rol.setDtoListaEvDes(new ArrayList<>());
        rol.setDtoListaEvDoc(new ArrayList<>());
        rol.setDtoListaEvTutor(new ArrayList<>());
        rol.setDtoListaGeneralResultados(new ArrayList<>());
        switch(rol.getEvSelect()){
            case 1:
                rol.setRenderDTB360(Boolean.TRUE);
                rol.setRenderDTBDesempenio(Boolean.FALSE);
                rol.setRenderDTBTutor(Boolean.FALSE);
                rol.setRenderDTBDocente(Boolean.FALSE);
                rol.setRenderDTBResGral(Boolean.FALSE);
                rol.setRenderBtnDownloadCedulas360(Boolean.TRUE);
                rol.setRenderBtnDownloadCedulasDes(Boolean.FALSE);
                //System.out.println("Evaluacion seleccionada: " + rol.getTipoEvalaucion());
                resultadoEvaluacion360Personal();
                break;
            case 2:
                rol.setRenderDTB360(Boolean.FALSE);
                rol.setRenderDTBDesempenio(Boolean.TRUE);
                rol.setRenderDTBDocente(Boolean.FALSE);
                rol.setRenderDTBTutor(Boolean.FALSE);
                rol.setRenderDTBResGral(Boolean.FALSE);
                rol.setRenderBtnDownloadCedulas360(Boolean.FALSE);
                rol.setRenderBtnDownloadCedulasDes(Boolean.TRUE);
                //System.out.println("Evaluacion seleccionada: " + rol.getTipoEvalaucion());
                resultadoEvaluacionDesempenioPersonal();
                break;
            case 3:
                rol.setRenderDTB360(Boolean.FALSE);
                rol.setRenderDTBDesempenio(Boolean.FALSE);
                rol.setRenderDTBTutor(Boolean.TRUE);
                rol.setRenderDTBDocente(Boolean.FALSE);
                rol.setRenderDTBResGral(Boolean.FALSE);
                rol.setRenderBtnDownloadCedulas360(Boolean.FALSE);
                rol.setRenderBtnDownloadCedulasDes(Boolean.FALSE);
                //System.out.println("Evaluacion seleccionada: " + rol.getTipoEvalaucion());
                resultadoEvaluacionTutor();
                break;
            case 4:
                rol.setRenderDTB360(Boolean.FALSE);
                rol.setRenderDTBDesempenio(Boolean.FALSE);
                rol.setRenderDTBTutor(Boolean.FALSE);
                rol.setRenderDTBDocente(Boolean.TRUE);
                rol.setRenderDTBResGral(Boolean.FALSE);
                rol.setRenderBtnDownloadCedulas360(Boolean.FALSE);
                rol.setRenderBtnDownloadCedulasDes(Boolean.FALSE);
                //System.out.println("Evaluacion seleccionada: " + rol.getTipoEvalaucion());
                resultadoEvaluacionDocente();
                break;
        }
    }
    
    public void resultadoEvaluacion360Personal() {
        rol.setDtoListaEv360(new ArrayList<>());
        ResultadoEJB<Evaluaciones360> resEv = ejbAdmin.obtenerEv360(rol.getPeSelect());
        if(!resEv.getCorrecto()) return;
        Evaluaciones360 evaluacion = resEv.getValor();
        //System.out.println("Evaluación obtenida: " + evaluacion);
        List<ListaEvaluacion360Promedios> resDto = ejbAdmin.obtenerResultadosEv360(evaluacion).getValor();
        if(resDto.isEmpty()) return;
        rol.setDtoListaEv360(resDto);
    }
    
    public void resultadoEvaluacionDesempenioPersonal(){
        rol.setDtoListaEvDes(new ArrayList<>());
        ResultadoEJB<DesempenioEvaluaciones> resEv = ejbAdmin.obtenerEvDes(rol.getPeSelect());
        if(!resEv.getCorrecto()) return;
        DesempenioEvaluaciones evaluacion = resEv.getValor();
        List<ListaEvaluacionDesempenioPromedios> resDtoLista = ejbAdmin.obtenerResultadosEvDesempenio(evaluacion).getValor();
        if(resDtoLista.isEmpty()) return;
        rol.setDtoListaEvDes(resDtoLista);
    }
    
    public ListaEvaluacionDesempenioPromedios packDes(ListaPersonal persona, DesempenioEvaluaciones ev){
        if(persona.equals(new ListaPersonal())) return new ListaEvaluacionDesempenioPromedios();
        List<DesempenioEvaluacionResultados> l = ejbAdmin.getEvaluacionesDesempenioSubordinados(ev.getEvaluacion(), persona.getClave());
        Comparador<DesempenioEvaluacionResultados> comparador = new ComparadorEvaluacionDesempenio();
        Calculable<DesempenioEvaluacionResultados> obtener = new PromediarDesempenio();
        //System.out.println("Lista resultados = " + l);
        if(l.isEmpty()) return new ListaEvaluacionDesempenioPromedios(persona.getClave(), persona.getNombre(), persona.getAreaOperativaNombre(), persona.getCategoriaOperativaNombre(), 0.0);
        Double promedio = l.stream()
                .filter(encuesta -> comparador.isCompleto(encuesta))
                .mapToDouble(desempenio -> obtener.promediar(desempenio))
                .sum();
        //System.out.println("Promedio" + promedio);
        return new ListaEvaluacionDesempenioPromedios(persona.getClave(), persona.getNombre(), persona.getAreaOperativaNombre(), persona.getCategoriaOperativaNombre(), promedio);
    }
    
    public void resultadoEvaluacionDocente(){
        rol.setDtoListaEvDoc(Collections.EMPTY_LIST);
        List<ListaEvaluacionDocenteMateria> resDtoLista = new ArrayList<>();
        ResultadoEJB<Evaluaciones> resEv = ejbAdmin.obtenerEvaluacion(rol.getPeSelect());
        if(!resEv.getCorrecto()) return;
        Evaluaciones evaluacion = resEv.getValor();
        ResultadoEJB<List<EvaluacionDocentesMateriaResultados>> resEv1 = ejbAdmin.obtenerResultados1(evaluacion.getEvaluacion());
        if(resEv1.getCorrecto()){
            resDtoLista = resEv1.getValor()
                    .stream()
                    .map(ev -> packDoc1(ev))
                    .collect(Collectors.toList());
        }
        
        ResultadoEJB<List<EvaluacionDocentesMateriaResultados3>> resEv2 = ejbAdmin.obtenerResultados2(evaluacion.getEvaluacion());
        if(resEv2.getCorrecto()){
            resDtoLista = resEv2.getValor()
                    .stream()
                    .map(ev -> packDoc2(ev))
                    .collect(Collectors.toList());
        }
        
        ResultadoEJB<List<EvaluacionDocentesMateriaResultados4>> resEv3 = ejbAdmin.obtenerResultados3(evaluacion.getEvaluacion());
        if(resEv3.getCorrecto()){
            resDtoLista = resEv3.getValor()
                    .stream()
                    .map(ev -> packDoc3(ev))
                    .collect(Collectors.toList());
        }
        
        ResultadoEJB<List<EvaluacionDocentesMateriaResultados5>> resEv4 = ejbAdmin.obtenerResultados4(evaluacion.getEvaluacion());
        if(resEv4.getCorrecto()){
            resDtoLista = resEv4.getValor()
                    .stream()
                    .map(ev -> packDoc4(ev))
                    .collect(Collectors.toList());
        }
        
        if(resDtoLista.isEmpty()) return;
        rol.setDtoListaEvDoc(resDtoLista);
    }
    
    public ListaEvaluacionDocenteMateria packDoc1(EvaluacionDocentesMateriaResultados evaluacion){
        if(evaluacion.equals(new EvaluacionDocentesMateriaResultados())) return new ListaEvaluacionDocenteMateria();
            Personal persona = ejbAdmin.obtenerPersona(evaluacion.getEvaluacionDocentesMateriaResultadosPK().getEvaluado()).getValor();
            AreasUniversidad area = ejbAdmin.obtenerArea(persona.getAreaOperativa()).getValor();
        return new ListaEvaluacionDocenteMateria(persona.getClave(), persona.getNombre(), area.getNombre(), 
                persona.getCategoriaOperativa().getNombre(), evaluacion.getEvaluacionDocentesMateriaResultadosPK().getCveMateria(), "", evaluacion.getPromedio());
    }
    
    public ListaEvaluacionDocenteMateria packDoc2(EvaluacionDocentesMateriaResultados3 evaluacion){
        if(evaluacion.equals(new EvaluacionDocentesMateriaResultados3())) return new ListaEvaluacionDocenteMateria();
            Personal persona = ejbAdmin.obtenerPersona(evaluacion.getEvaluacionDocentesMateriaResultados3PK().getEvaluado()).getValor();
            AreasUniversidad area = ejbAdmin.obtenerArea(persona.getAreaOperativa()).getValor();
        return new ListaEvaluacionDocenteMateria(persona.getClave(), persona.getNombre(), area.getNombre(), 
                persona.getCategoriaOperativa().getNombre(), evaluacion.getEvaluacionDocentesMateriaResultados3PK().getCveMateria(), "", evaluacion.getPromedio());
    }
    
    public ListaEvaluacionDocenteMateria packDoc3(EvaluacionDocentesMateriaResultados4 evaluacion){
        if(evaluacion.equals(new EvaluacionDocentesMateriaResultados4())) return new ListaEvaluacionDocenteMateria();
            Personal persona = ejbAdmin.obtenerPersona(evaluacion.getEvaluacionDocentesMateriaResultados4PK().getEvaluado()).getValor();
            AreasUniversidad area = ejbAdmin.obtenerArea(persona.getAreaOperativa()).getValor();
        return new ListaEvaluacionDocenteMateria(persona.getClave(), persona.getNombre(), area.getNombre(), 
                persona.getCategoriaOperativa().getNombre(), evaluacion.getEvaluacionDocentesMateriaResultados4PK().getCveMateria(), "", evaluacion.getPromedio());
    }
    
    public ListaEvaluacionDocenteMateria packDoc4(EvaluacionDocentesMateriaResultados5 evaluacion){
        if(evaluacion.equals(new EvaluacionDocentesMateriaResultados5())) return new ListaEvaluacionDocenteMateria();
            Personal persona = ejbAdmin.obtenerPersona(evaluacion.getEvaluacionDocentesMateriaResultados5PK().getEvaluado()).getValor();
            AreasUniversidad area = ejbAdmin.obtenerArea(persona.getAreaOperativa()).getValor();
        return new ListaEvaluacionDocenteMateria(persona.getClave(), persona.getNombre(), area.getNombre(), 
                persona.getCategoriaOperativa().getNombre(), evaluacion.getEvaluacionDocentesMateriaResultados5PK().getCveMateria(), "", evaluacion.getPromedio());
    }
    
    public void resultadoEvaluacionTutor(){
        rol.setDtoListaEvTutor(Collections.EMPTY_LIST);
        List<ListaEvaluacionTutorPromedios> resDtoLista = new ArrayList<>();
        ResultadoEJB<Evaluaciones> resEv = ejbAdmin.obtenerEvaluacion(rol.getPeSelect());
        if(!resEv.getCorrecto()) return;
        Evaluaciones evaluacion = resEv.getValor();
        
        List<EvaluacionTutoresResultados>  resTbl1  = ejbAdmin.obtenerResultadosTutor1(evaluacion.getEvaluacion()).getValor();
        if(!resTbl1.isEmpty()){
            resDtoLista = resTbl1.stream().map(ev -> packTutor1(ev)).distinct().collect(Collectors.toList());
            rol.setIdEvTutor(1);
        }
        List<EvaluacionTutoresResultados2> resTbl2  = ejbAdmin.obtenerResultadosTutor2(evaluacion.getEvaluacion()).getValor();
        if(!resTbl2.isEmpty()){
            resDtoLista = resTbl2.stream().map(ev -> packTutor2(ev)).distinct().collect(Collectors.toList());
            rol.setIdEvTutor(2);
        }
        List<EvaluacionTutoresResultados3> resTbl3  = ejbAdmin.obtenerResultadosTutor3(evaluacion.getEvaluacion()).getValor();
        if(!resTbl3.isEmpty()){
            resDtoLista = resTbl3.stream().map(ev -> packTutor3(ev)).distinct().collect(Collectors.toList());
            rol.setIdEvTutor(3);
        }
        
        if(resDtoLista.isEmpty()) return;
        rol.setDtoListaEvTutor(resDtoLista.stream().filter(x -> x.getPromedio() > 0d).collect(Collectors.toList()));
    }
    
    public ListaEvaluacionTutorPromedios packTutor1(EvaluacionTutoresResultados evaluacion){
        if(evaluacion.equals(new EvaluacionTutoresResultados())) return new ListaEvaluacionTutorPromedios();
        Calculable<EvaluacionTutoresResultados>  obtener  = new PromediarTutor();
        Personal persona = ejbAdmin.obtenerPersona(evaluacion.getEvaluacionTutoresResultadosPK().getEvaluado()).getValor();
        AreasUniversidad area = ejbAdmin.obtenerArea(persona.getAreaOperativa()).getValor();
        List<EvaluacionTutoresResultados>  resTbl1  = ejbAdmin.obtenerResultadosTutor1(evaluacion.getEvaluaciones().getEvaluacion(), persona.getClave()).getValor();
        Double promedio = 0d;
        if(!resTbl1.isEmpty()){
            promedio = resTbl1.stream()
                .mapToDouble(tutor -> obtener.promediar(tutor))
                .average().orElse(0d);
        }
        return new ListaEvaluacionTutorPromedios(persona.getClave(), persona.getNombre(), area.getNombre(), persona.getCategoriaOperativa().getNombre(), promedio);
    }
    
    public ListaEvaluacionTutorPromedios packTutor2(EvaluacionTutoresResultados2 evaluacion){
        if(evaluacion.equals(new EvaluacionTutoresResultados2())) return new ListaEvaluacionTutorPromedios();
        Personal persona = ejbAdmin.obtenerPersona(evaluacion.getEvaluacionTutoresResultados2PK().getEvaluado()).getValor();
        //System.out.println("Persona" + persona.getClave());
        AreasUniversidad area = ejbAdmin.obtenerArea(persona.getAreaOperativa()).getValor();
        List<EvaluacionTutoresResultados2>  resTbl1  = ejbAdmin.obtenerResultadosTutor2(evaluacion.getEvaluaciones().getEvaluacion(), persona.getClave()).getValor();
        Double promedio = 0d;
        if(!resTbl1.isEmpty()){
            Double promedioR1  = resTbl1.stream().map(ev -> ev.getR1().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
            Double promedioR2  = resTbl1.stream().map(ev -> ev.getR2().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
            Double promedioR3  = resTbl1.stream().map(ev -> ev.getR3().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
            Double promedioR4  = resTbl1.stream().map(ev -> ev.getR4().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
            Double promedioR5  = resTbl1.stream().map(ev -> ev.getR5().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
            Double promedioR6  = resTbl1.stream().map(ev -> ev.getR6().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
            Double promedioR7  = resTbl1.stream().map(ev -> ev.getR7().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
            Double promedioR8  = resTbl1.stream().map(ev -> ev.getR8().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();

            List<Double> promedios = new ArrayList<>();
            promedios.add(promedioR1); promedios.add(promedioR2); promedios.add(promedioR3); promedios.add(promedioR4); promedios.add(promedioR5); promedios.add(promedioR6);
            promedios.add(promedioR7); promedios.add(promedioR8);
            promedio = promedios.stream().filter(x -> !x.equals(0d)).mapToDouble(Double::doubleValue).average().orElse(0d);
        }
        return new ListaEvaluacionTutorPromedios(persona.getClave(), persona.getNombre(), area.getNombre(), persona.getCategoriaOperativa().getNombre(), promedio);
    }
    
    public ListaEvaluacionTutorPromedios packTutor3(EvaluacionTutoresResultados3 evaluacion){
        if(evaluacion.equals(new EvaluacionTutoresResultados3())) return new ListaEvaluacionTutorPromedios();
        Personal persona = ejbAdmin.obtenerPersona(evaluacion.getEvaluacionTutoresResultados3PK().getEvaluado()).getValor();
        AreasUniversidad area = ejbAdmin.obtenerArea(persona.getAreaOperativa()).getValor();
        List<EvaluacionTutoresResultados3>  resTbl1  = ejbAdmin.obtenerResultadosTutor3(evaluacion.getEvaluaciones().getEvaluacion(), persona.getClave()).getValor();
        Double promedio = 0d;
        if(!resTbl1.isEmpty()){
            Double promedioR1  = resTbl1.stream().map(ev -> ev.getR1().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
            Double promedioR2  = resTbl1.stream().map(ev -> ev.getR2().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
            Double promedioR3  = resTbl1.stream().map(ev -> ev.getR3().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
            Double promedioR4  = resTbl1.stream().map(ev -> ev.getR4().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
            Double promedioR5  = resTbl1.stream().map(ev -> ev.getR5().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
            Double promedioR6  = resTbl1.stream().map(ev -> ev.getR6().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
            Double promedioR7  = resTbl1.stream().map(ev -> ev.getR7().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
            Double promedioR8  = resTbl1.stream().map(ev -> ev.getR8().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
            Double promedioR9  = resTbl1.stream().map(ev -> ev.getR9().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
            Double promedioR10 = resTbl1.stream().map(ev -> ev.getR10().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();

            List<Double> promedios = new ArrayList<>();
            promedios.add(promedioR1); promedios.add(promedioR2); promedios.add(promedioR3); promedios.add(promedioR4); promedios.add(promedioR5); promedios.add(promedioR6);
            promedios.add(promedioR7); promedios.add(promedioR8); promedios.add(promedioR9); promedios.add(promedioR10);
            promedio = promedios.stream().filter(x -> !x.equals(0d)).mapToDouble(Double::doubleValue).average().orElse(0d);
        }
        return new ListaEvaluacionTutorPromedios(persona.getClave(), persona.getNombre(), area.getNombre(), persona.getCategoriaOperativa().getNombre(), promedio);
    }
    
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_DOWN);
        return bd.doubleValue();
    }
    
    public void generarResultadosGenerales(){
        rol.setRenderDTB360(Boolean.FALSE);
        rol.setRenderDTBDesempenio(Boolean.FALSE);
        rol.setRenderDTBTutor(Boolean.FALSE);
        rol.setRenderDTBDocente(Boolean.FALSE);
        rol.setRenderDTBResGral(Boolean.TRUE);
        rol.setRenderBtnDownloadRepGral(Boolean.TRUE);
        rol.setDtoListaEv360(new ArrayList<>());
        rol.setDtoListaEvDes(new ArrayList<>());
        rol.setDtoListaEvDoc(new ArrayList<>());
        rol.setDtoListaEvTutor(new ArrayList<>());
        rol.setDtoListaGeneralResultados(new ArrayList<>());
        ResultadoEJB<List<Personal>> resList =ejbAdmin.obtenerListaPersonas();
        if(!resList.getCorrecto()) return;
        rol.setListaPersonas(resList.getValor());
        rol.getPeriodoEvaluacion();
        List<ListadoGeneralEvaluacionesPersonal> listaResultados = rol.getListaPersonas()
                .stream()
                .map(persona -> packResultados(persona, rol.getPeriodoEvaluacion()))
                .distinct()
                .filter(x -> x.getPromedio360() > 0d || x.getPromedioDesempenio() > 0d || x.getPromedioDocente() > 0d || x.getPromediogeneral() > 0d)
                .collect(Collectors.toList());
        if(listaResultados.isEmpty()) return;
        rol.setDtoListaGeneralResultados(listaResultados);
    }
    
    public ListadoGeneralEvaluacionesPersonal packResultados(Personal persona, Integer periodo){
        if(periodo == null) return new ListadoGeneralEvaluacionesPersonal();
        if(persona.equals(new Personal())) return new ListadoGeneralEvaluacionesPersonal();
        Personal p = ejbAdmin.obtenerPersona(persona.getClave()).getValor();
        AreasUniversidad area = ejbAdmin.obtenerArea(p.getAreaOperativa()).getValor();
        Evaluaciones360 ev360 = ejbAdmin.getEvaluacion360Administracion(periodo);
        DesempenioEvaluaciones evDes = ejbAdmin.getEvaluacionDesempenioAdministracion(periodo);
        Evaluaciones evDoc = ejbAdmin.getEvaluaciones(periodo, "Docente");
        Evaluaciones evTutor = ejbAdmin.getEvaluaciones(periodo, "Tutor");
        Double promedioEvDes = 0d, promedioEv360 = 0d, promedioEvDoc = 0d, promedioEvTutor = 0d;
        if(!evDes.equals(new DesempenioEvaluaciones())){
            List<DesempenioEvaluacionResultados> l = ejbAdmin.getEvaluacionesDesempenioSubordinados(evDes.getEvaluacion(), p.getClave());
            Comparador<DesempenioEvaluacionResultados> comparador = new ComparadorEvaluacionDesempenio();
            Calculable<DesempenioEvaluacionResultados> obtener = new PromediarDesempenio();
            promedioEvDes = l.stream()
                    .filter(encuesta -> comparador.isCompleto(encuesta))
                    .mapToDouble(desempenio -> obtener.promediar(desempenio))
                    .sum();
        }
        if(!ev360.equals(new Evaluaciones360())){
            List<Evaluaciones360Resultados> l = ejbAdmin.getEvaluaciones360ResultadosSubordinados(ev360, persona.getClave());
            promedioEv360 = l.stream().mapToDouble(Evaluaciones360Resultados::getPromedio).average().orElse(0d);
        }
        if(!evTutor.equals(new Evaluaciones())){
            List<EvaluacionTutoresResultados>  resTbl1  = ejbAdmin.obtenerResultadosTutor1(evTutor.getEvaluacion(), p.getClave()).getValor();
            if(!resTbl1.isEmpty()){
                Calculable<EvaluacionTutoresResultados>  obtener  = new PromediarTutor();
                promedioEvTutor = resTbl1.stream()
                    .mapToDouble(tutor -> obtener.promediar(tutor))
                    .average().orElse(0d);
            }
            List<EvaluacionTutoresResultados2>  resTbl2  = ejbAdmin.obtenerResultadosTutor2(evTutor.getEvaluacion(), p.getClave()).getValor();
            if(!resTbl2.isEmpty()){
                Double promedioR1  = resTbl2.stream().map(ev -> ev.getR1().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
                Double promedioR2  = resTbl2.stream().map(ev -> ev.getR2().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
                Double promedioR3  = resTbl2.stream().map(ev -> ev.getR3().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
                Double promedioR4  = resTbl2.stream().map(ev -> ev.getR4().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
                Double promedioR5  = resTbl2.stream().map(ev -> ev.getR5().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
                Double promedioR6  = resTbl2.stream().map(ev -> ev.getR6().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
                Double promedioR7  = resTbl2.stream().map(ev -> ev.getR7().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
                Double promedioR8  = resTbl2.stream().map(ev -> ev.getR8().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
                List<Double> promedios = new ArrayList<>();
                promedios.add(promedioR1); promedios.add(promedioR2); promedios.add(promedioR3); promedios.add(promedioR4); promedios.add(promedioR5); promedios.add(promedioR6);
                promedios.add(promedioR7); promedios.add(promedioR8);
                promedioEvTutor = promedios.stream().filter(x -> !x.equals(0d)).mapToDouble(Double::doubleValue).average().orElse(0d);
            }
            List<EvaluacionTutoresResultados3>  resTbl3  = ejbAdmin.obtenerResultadosTutor3(evTutor.getEvaluacion(), p.getClave()).getValor();
            if(!resTbl3.isEmpty()){
                Double promedioR1  = resTbl3.stream().map(ev -> ev.getR1().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
                Double promedioR2  = resTbl3.stream().map(ev -> ev.getR2().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
                Double promedioR3  = resTbl3.stream().map(ev -> ev.getR3().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
                Double promedioR4  = resTbl3.stream().map(ev -> ev.getR4().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
                Double promedioR5  = resTbl3.stream().map(ev -> ev.getR5().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
                Double promedioR6  = resTbl3.stream().map(ev -> ev.getR6().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
                Double promedioR7  = resTbl3.stream().map(ev -> ev.getR7().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
                Double promedioR8  = resTbl3.stream().map(ev -> ev.getR8().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
                Double promedioR9  = resTbl3.stream().map(ev -> ev.getR9().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
                Double promedioR10 = resTbl3.stream().map(ev -> ev.getR10().intValue()).filter(x -> !x.equals(0)).mapToInt(Integer::intValue).average().getAsDouble();
                List<Double> promedios = new ArrayList<>();
                promedios.add(promedioR1); promedios.add(promedioR2); promedios.add(promedioR3); promedios.add(promedioR4); promedios.add(promedioR5); promedios.add(promedioR6);
                promedios.add(promedioR7); promedios.add(promedioR8); promedios.add(promedioR9); promedios.add(promedioR10);
                promedioEvTutor = promedios.stream().filter(x -> !x.equals(0d)).mapToDouble(Double::doubleValue).average().orElse(0d);
            }
        }
        
        if(!evDoc.equals(new Evaluaciones())){
            List<EvaluacionDocentesMateriaResultados> resEvDoc1 = ejbAdmin.obtenerResultados1(evDoc.getEvaluacion(), p.getClave()).getValor();
            if(!resEvDoc1.isEmpty()){
                promedioEvDoc = resEvDoc1.stream().mapToDouble(EvaluacionDocentesMateriaResultados::getPromedio).average().orElse(0d);
            }
            List<EvaluacionDocentesMateriaResultados3> resEvDoc2 = ejbAdmin.obtenerResultados2(evDoc.getEvaluacion(), p.getClave()).getValor();
            if(!resEvDoc2.isEmpty()){
                promedioEvDoc = resEvDoc2.stream().mapToDouble(EvaluacionDocentesMateriaResultados3::getPromedio).average().orElse(0d);
            }
            List<EvaluacionDocentesMateriaResultados4> resEvDoc3 = ejbAdmin.obtenerResultados3(evDoc.getEvaluacion(), p.getClave()).getValor();
            if(!resEvDoc3.isEmpty()){
                promedioEvDoc = resEvDoc3.stream().mapToDouble(EvaluacionDocentesMateriaResultados4::getPromedio).average().orElse(0d);
            }
            List<EvaluacionDocentesMateriaResultados5> resEvDoc4 = ejbAdmin.obtenerResultados4(evDoc.getEvaluacion(), p.getClave()).getValor();
            if(!resEvDoc4.isEmpty()){
                promedioEvDoc = resEvDoc4.stream().mapToDouble(EvaluacionDocentesMateriaResultados5::getPromedio).average().orElse(0d);
            }
        }
        
        return new ListadoGeneralEvaluacionesPersonal(p.getClave(), p.getNombre(), area.getNombre(), p.getCategoriaOperativa().getNombre(), 
                promedioEv360, promedioEvDes, promedioEvDoc, promedioEvTutor);
    }
    
    public void activarSelectOneMenu(){
        rol.setRenderSelectPeGlobal(Boolean.TRUE);
        rol.setRenderBtnResultados(Boolean.FALSE);
        rol.setRenderBtnDownloadCedulas360(Boolean.FALSE);
        rol.setRenderBtnDownloadCedulasDes(Boolean.FALSE);
        rol.setRenderBtnDownloadRepGral(Boolean.FALSE);
        rol.setRenderDTB360(Boolean.FALSE);
        rol.setRenderDTBDesempenio(Boolean.FALSE);
        rol.setRenderDTBDocente(Boolean.FALSE);
        rol.setRenderDTBTutor(Boolean.FALSE);
        rol.setDtoListaEv360(new ArrayList<>());
        rol.setDtoListaEvDes(new ArrayList<>());
        rol.setDtoListaEvDoc(new ArrayList<>());
        rol.setDtoListaEvTutor(new ArrayList<>());
        rol.setListaEvaluaciones(new ArrayList<>());
    }
    
    public void descargarCedulas() {
        ResultadoEJB<Evaluaciones360> resEv = ejbAdmin.obtenerEv360(rol.getPeSelect());
        if(!resEv.getCorrecto()) return;
        Evaluaciones360 evaluacion = resEv.getValor();
        //System.out.println("Evaluacion" + evaluacion);
        PeriodosEscolares pe = ejbAdmin.obtenerPeriodo(evaluacion.getPeriodo()).getValor();
        //System.out.println("Periodo" + pe);
        ResultadoEJB<List<ListaEvaluacion360Promedios.DtoListaResultadosEvaluacion360>> resLista = ejbAdmin.obtenerResultadosCedulas(evaluacion.getEvaluacion());
        if(!resLista.getCorrecto()) return;
        List<ListaEvaluacion360Promedios.DtoListaResultadosEvaluacion360> listaPromedios = resLista.getValor();
        Map<Short, Apartado> mapaHabilidades = new HashMap<>();
        listaPromedios.forEach(lpe -> {
            if (!mapaHabilidades.containsKey(lpe.getPersonalCategoria().getCategoria())) {
                mapaHabilidades.put(lpe.getPersonalCategoria().getCategoria(), ejbEvaluacion360.getApartadoHabilidades(lpe.getPersonalCategoria().getCategoria(),evaluacion.getEvaluacion()));
            }
        });

        List<ListaEvaluacion360Promedios.DtoListaReporteEvaluacion360> listaResportes = ejbAdmin.obtenerReporteCedulas(evaluacion.getEvaluacion()).getValor();

        ExcelWritter ew = new ExcelWritter(pe, evaluacion, listaPromedios, listaResportes, mapaHabilidades);
        ew.obtenerLibro();
        ew.editarLibro1();
        ew.escribirLibro();
//        String ruta = ew.enviarLibro();
        Ajax.oncomplete("descargar('" + ew.enviarLibro() + "');");

//        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.descargarCedulas() ruta: " + ruta);
    }
    
    public void descargarCedulasDesempenio() {
        ResultadoEJB<DesempenioEvaluaciones> resEv = ejbAdmin.obtenerEvDes(rol.getPeSelect());
        if(!resEv.getCorrecto()) return;
        DesempenioEvaluaciones evaluacion = resEv.getValor();
        //System.out.println("Evaluacion" + evaluacion);
        PeriodosEscolares pe = ejbAdmin.obtenerPeriodo(evaluacion.getPeriodo()).getValor();
        //System.out.println("Periodo" + pe);
        ResultadoEJB<List<ListaEvaluacionDesempenioPromedios.DtoListaResultadosEvaluacionDesempenio>> resLista = ejbAdmin.obtenerReultadosCedulas(evaluacion.getEvaluacion());
        if(!resLista.getCorrecto()) return;
        List<ListaEvaluacionDesempenioPromedios.DtoListaResultadosEvaluacionDesempenio> listaResultados = resLista.getValor();
        
        ExcelWritter ew = new ExcelWritter(evaluacion, listaResultados, pe);
        ew.obtenerLibro();
        ew.editarLibro1();
        ew.escribirLibro();
//        String ruta = ew.enviarLibro();
        Ajax.oncomplete("descargar('" + ew.enviarLibro() + "');");

//        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.descargarCedulas() ruta: " + ruta);
    }
    
    public void descargarReporteGral(){
        ResultadoEJB<List<Personal>> resList =ejbAdmin.obtenerListaPersonas();
        if(!resList.getCorrecto()) return;
        rol.setListaPersonas(resList.getValor());
        rol.getPeriodoEvaluacion();
        PeriodosEscolares pe = ejbAdmin.obtenerPeriodo(rol.getPeriodoEvaluacion()).getValor();
        List<ListadoGeneralEvaluacionesPersonal> listaResultados = rol.getListaPersonas()
                .stream()
                .map(persona -> packResultados(persona, rol.getPeriodoEvaluacion()))
                .distinct()
                .filter(x -> x.getPromedio360() > 0d || x.getPromedioDesempenio() > 0d || x.getPromedioDocente() > 0d || x.getPromediogeneral() > 0d)
                .collect(Collectors.toList());
        if(listaResultados.isEmpty()) return;
        //System.out.println("Periodo" + pe);
        
        ExcelWritter ew = new ExcelWritter(listaResultados, pe);
        ew.obtenerExcel();
        ew.editarExcel();
        ew.escribirExcel();
//        String ruta = ew.enviarLibro();
        Ajax.oncomplete("descargar('" + ew.enviarLibro() + "');");
    }
    
    public Boolean habilitarPeriodo(Integer periodo){
        ResultadoEJB<Boolean> resBol = ejbAdmin.comprobarEvaluacionesPeriodo(periodo);
        if(!resBol.getCorrecto()) return Boolean.FALSE;
        return resBol.getValor();
    }
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "administracion evaluaciones personal";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
////        map.entrySet().forEach(//System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
    
}
