package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoInformeIntegralDocente;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.InformeIntegralDocenteRolMultiple;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbInformeIntegralDocente;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Named
@ViewScoped
public class InformeIntegralDocente extends ViewScopedRol implements Desarrollable {
    @EJB EjbPropiedades ep;
    @EJB EjbInformeIntegralDocente ejb;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;
    @Getter private Boolean cargado = false;
    @Getter @Setter private InformeIntegralDocenteRolMultiple rol;

    @PostConstruct
    public void init(){
        try{
            if(!logon.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.EVALUACION_INTEGRAL_DOCENTE);
            ResultadoEJB<PersonalActivo> resAcceso = ejb.validarAcceso(logon.getPersonal().getClave());//validar si es personal de servicios escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<PersonalActivo> resValidacion = ejb.validarAcceso(logon.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            PersonalActivo filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personalActivo = filtro;//ejbPersonalBean.pack(logon.getPersonal());

            rol = new InformeIntegralDocenteRolMultiple ();
            tieneAcceso = resAcceso.getCorrecto();
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            rol.setPersonalActivo(personalActivo);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            rol.setNivelRol(NivelRol.CONSULTA);
            rol.getInstrucciones().add("Ingrese nombre o curp del o de la estudiante del que desea consultar el historial de movimientos.");
            rol.setEsDocente(Boolean.FALSE);
            rol.setEsDirector(Boolean.FALSE);
            rol.setEsFDA(Boolean.FALSE);
            rol.setEsSA(Boolean.FALSE);
            DtoInformeIntegralDocente.Docente dtoDocente = new DtoInformeIntegralDocente.Docente(new Evaluaciones(),new ArrayList<>(),new Double(0.0),new String(),Boolean.FALSE);
            DtoInformeIntegralDocente.Tutor dtoTutor = new DtoInformeIntegralDocente.Tutor(new Evaluaciones(),Boolean.FALSE,new ArrayList<>(),new Double(0),new String());
            DtoInformeIntegralDocente.Desempeño dtoDes = new DtoInformeIntegralDocente.Desempeño(new DesempenioEvaluaciones(),new ArrayList<>(),new Double(0),new String());
            DtoInformeIntegralDocente.Pares dtoPares = new DtoInformeIntegralDocente.Pares(new Evaluaciones(),new ArrayList<>(),new Double(0),new String());
            List<DtoInformeIntegralDocente.EvaluacionIntegral > dtoEvIntegral= new ArrayList<>();
            DtoInformeIntegralDocente.InformeIntegral dto = new DtoInformeIntegralDocente.InformeIntegral(new Personal(),new AreasUniversidad(),new AreasUniversidad(),dtoDes,dtoPares,dtoTutor,dtoDocente,dtoEvIntegral,new Double(0.0));
            validarAcceso();

        }catch (Exception e){
           // mostrarExcepcion(e);
        }
    }
    public void validarAcceso(){
        try{
            if(rol.getPersonalActivo().getPersonal().getActividad().getActividad()==3);{
                rol.setEsDocente(Boolean.TRUE);
                rol.setAreaSeleccionada(rol.getPersonalActivo().getAreaSuperior());
                rol.setDocente(rol.getPersonalActivo().getPersonal());
                getPeriodosEvaluacionbyDocente();
                //getInforme();
            }
            if(rol.getPersonalActivo().getAreaOperativa().getArea()==12){
                rol.setEsFDA(Boolean.TRUE);
                getAreasAcademicas();
            }
            if(rol.getPersonalActivo().getAreaSuperior().getArea()==2 & rol.getPersonalActivo().getPersonal().getCategoriaOperativa().getCategoria()==18 || rol.getPersonalActivo().getPersonal().getCategoriaOperativa().getCategoria()==48){
                rol.setEsDirector(Boolean.TRUE);
                rol.setAreaSeleccionada(rol.getPersonalActivo().getAreaOperativa());
                getDocentesbyArea();
            }
            if(rol.getPersonalActivo().getAreaSuperior().getArea()==2 & rol.getPersonalActivo().getPersonal().getCategoriaOperativa().getCategoria()==14 & rol.getPersonalActivo().getAreaOperativa().getArea()==23){
                rol.setEsDirector(Boolean.TRUE);
                rol.setAreaSeleccionada(rol.getPersonalActivo().getAreaOperativa());
                getDocentesbyArea();
            }

            if(rol.getPersonalActivo().getAreaOperativa().getArea()==2 & rol.getPersonalActivo().getPersonal().getCategoriaOperativa().getCategoria()==38){
                rol.setEsSA(Boolean.TRUE);
                getAreasAcademicas();
            }
        }catch (Exception e){
           // System.out.println("InformeIntegralDocente.validarAcceso "+ e.getMessage());
           // mostrarExcepcion(e);
        }
    }

    /*
    Obtiene la lista de las áreas académicas
     */
    public void getAreasAcademicas (){
        try{
            ResultadoEJB<List<AreasUniversidad>> resAreas= ejb.getAreasAcademicas();
            if(resAreas.getCorrecto()){
                rol.setAreasAcademicas(resAreas.getValor());
                rol.setAreaSeleccionada(resAreas.getValor().get(0));
                getDocentesbyArea();
            }else {
               // mostrarMensajeResultadoEJB(resAreas);
            }
        }catch (Exception e){
          //  mostrarExcepcion(e);
        }
    }

    /*
    Obtiene la lista de docentes por area seleccionada
     */
    public void getDocentesbyArea(){
        try{
            ResultadoEJB<List<Personal>> resDocentes= ejb.getDocentesbyArea(rol.getAreaSeleccionada());
            if(resDocentes.getCorrecto()){
                rol.setDocentes(resDocentes.getValor());
                rol.setDocente(resDocentes.getValor().get(0));
                getPeriodosEvaluacionbyDocente();
            }else {
                //mostrarMensajeResultadoEJB(resDocentes);
            }

        }catch (Exception e){
           // mostrarExcepcion(e);
        }
    }

    public void getPeriodosEvaluacionbyDocente(){
        try{
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosEvaluacionPersonal(rol.getDocente());
            if(resPeriodos.getCorrecto()){
                rol.setPeriodosEscolares(resPeriodos.getValor());
                rol.setPeriodoSeleccionado(resPeriodos.getValor().get(0));
                getInforme();
            }else {
                DtoInformeIntegralDocente.Docente dtoDocente = new DtoInformeIntegralDocente.Docente(new Evaluaciones(),new ArrayList<>(),new Double(0.0),new String(),Boolean.FALSE);
                DtoInformeIntegralDocente.Tutor dtoTutor = new DtoInformeIntegralDocente.Tutor(new Evaluaciones(),Boolean.FALSE,new ArrayList<>(),new Double(0),new String());
                DtoInformeIntegralDocente.Desempeño dtoDes = new DtoInformeIntegralDocente.Desempeño(new DesempenioEvaluaciones(),new ArrayList<>(),new Double(0),new String());
                DtoInformeIntegralDocente.Pares dtoPares = new DtoInformeIntegralDocente.Pares(new Evaluaciones(),new ArrayList<>(),new Double(0),new String());
                List<DtoInformeIntegralDocente.EvaluacionIntegral > dtoEvIntegral= new ArrayList<>();
                DtoInformeIntegralDocente.InformeIntegral dto = new DtoInformeIntegralDocente.InformeIntegral(new Personal(),new AreasUniversidad(),new AreasUniversidad(),dtoDes,dtoPares,dtoTutor,dtoDocente,dtoEvIntegral,new Double(0.0));
                rol.setEvDocente(dtoDocente);
                rol.setEvDesempeño(dtoDes);
                rol.setEvTutor(dtoTutor);
                rol.setEvPares(dtoPares);
                rol.setResultadosTutor(new ArrayList<>());
                rol.setResultadosPares(new ArrayList<>());
                rol.setPeriodoSeleccionado(new PeriodosEscolares());
                rol.setPeriodosEscolares(new ArrayList<>());
                rol.getInformeIntegral().setEvIntegral(dtoEvIntegral);
                rol.setInformeIntegral(dto);
                rol.setMappedList(new HashMap<>());
              // mostrarMensajeResultadoEJB(resPeriodos);
                }
        }catch (Exception e){
          //  mostrarExcepcion(e);
        }
    }

    private void cargarGraficoIntegral() {
        rol.setMappedList(new HashMap<>());
        HashMap<String,List<DtoInformeIntegralDocente.EvaluacionIntegral>> map= new HashMap();
        //Random r = new Random();
        map.put("Alcanzado", rol.getInformeIntegral().getEvIntegral());
        //System.out.println("InformeIntegralDocente.cargarGraficoIntegral "+ rol.getInformeIntegral());
        List<DtoInformeIntegralDocente.EvaluacionIntegral> faltante= new ArrayList<>();
        rol.getInformeIntegral().getEvIntegral().stream().forEach(i->{
            DtoInformeIntegralDocente.EvaluacionIntegral dtoIntegral = new DtoInformeIntegralDocente.EvaluacionIntegral(new Double(0.0),"");
            if(rol.getEvDocente().getFueDocente()==false){
                //System.out.println("InformeIntegralDocente.cargarGraficoIntegral 1");
                //No fue docente en el periodo seleccionado
                //Si fue tutor se le da 40% a la evaluacion del tutor, en caso de que no haya sido tutor se le da 30% a la evaluación de desempeño y 10% a la de pares
                //comprobar si fue tutor
                if(rol.getTutor()==true){
                    //System.out.println("InformeIntegralDocente.cargarGraficoIntegral");
                    if(i.getNombreApartado().equals("Estudiante/Tutor")){
                        dtoIntegral.setNombreApartado("Faltante Tutor");
                        dtoIntegral.setValor(40.00-i.getValor());
                    }
                    //Desempeño
                    if(i.getNombreApartado().equals("Desempeño")){
                        dtoIntegral.setNombreApartado("Falante Desempeño");
                        dtoIntegral.setValor(40.00 -i.getValor());
                    }
                    //Pares
                    if(i.getNombreApartado().equals("Pares")){
                        dtoIntegral.setNombreApartado("Falante Desempeño");
                        dtoIntegral.setValor(20.00 -i.getValor());
                    }
                }else {
                    //System.out.println("InformeIntegralDocente.cargarGraficoIntegral 2");
                    //Desempeño
                    if(i.getNombreApartado().equals("Desempeño")){
                        dtoIntegral.setNombreApartado("Falante Desempeño");
                        dtoIntegral.setValor(70.00 -i.getValor());
                    }
                    //Pares
                    if(i.getNombreApartado().equals("Pares")){
                        dtoIntegral.setNombreApartado("Falante Desempeño");
                        dtoIntegral.setValor(30.00 -i.getValor());
                    }
                }
            }else {
                //System.out.println("InformeIntegralDocente.cargarGraficoIntegral 2");
                //Fue docente en el periodo seleccionado
                if(rol.getTutor()==true){
                    if(i.getNombreApartado().equals("Estudiante/Docente")){
                        dtoIntegral.setNombreApartado("Falante Docente");
                        dtoIntegral.setValor(20.00 -i.getValor());
                    }
                    if(i.getNombreApartado().equals("Estudiante/Tutor")){
                        dtoIntegral.setNombreApartado("Falante Tutor");
                        dtoIntegral.setValor(20.00 -i.getValor());
                    }
                }else {
                    //No fue tutor
                    if(i.getNombreApartado().equals("Estudiante/Docente")){
                        dtoIntegral.setNombreApartado("Falante Docente");
                        dtoIntegral.setValor(40.00 -i.getValor());
                    }
                }
                //Desempeño
                if(i.getNombreApartado().equals("Desempeño")){
                    dtoIntegral.setNombreApartado("Falante Desempeño");
                    dtoIntegral.setValor(40.00 -i.getValor());
                }
                //Pares
                if(i.getNombreApartado().equals("Pares")){
                    dtoIntegral.setNombreApartado("Falante Desempeño");
                    dtoIntegral.setValor(20.00 -i.getValor());
                }
            }
            faltante.add(dtoIntegral);
        });
        map.put("Faltante", faltante);
        rol.setMappedList(map);

    }



    public void getInforme (){
        try{
          ResultadoEJB<DtoInformeIntegralDocente.InformeIntegral> resInforme = ejb.packInformeIntegral(rol.getPeriodoSeleccionado(),rol.getDocente());
          if(resInforme.getCorrecto()){
              mostrarMensajeResultadoEJB(resInforme);
              rol.setInformeIntegral(resInforme.getValor());
              rol.setTutor(resInforme.getValor().getEvTutor().getEsTutor());
              rol.setFueDocente(resInforme.getValor().getEvDocente().getFueDocente());
              if(resInforme.getValor().getEvTutor().getEsTutor()==true){
                  rol.setEvTutor(resInforme.getValor().getEvTutor());
                  rol.setTutor(resInforme.getValor().getEvTutor().getEsTutor());
                  rol.getEvTutor().getResultados().stream().forEach(p->rol.setResultadosTutor(p.getResultadoPreguntas()));

              }
              rol.setEvDocente(resInforme.getValor().getEvDocente());
              rol.setEvDesempeño(resInforme.getValor().getEvDesempeño());
              rol.setEvPares(resInforme.getValor().getEvPares());
              rol.setFechaImpresion(new Date());
              rol.getEvPares().getResultados().stream().forEach(p->rol.setResultadosPares(p.getResultadoPreguntas()));
              cargarGraficoIntegral();
          }else {
              DtoInformeIntegralDocente.Docente dtoDocente = new DtoInformeIntegralDocente.Docente(new Evaluaciones(),new ArrayList<>(),new Double(0.0),new String(),Boolean.FALSE);
              DtoInformeIntegralDocente.Tutor dtoTutor = new DtoInformeIntegralDocente.Tutor(new Evaluaciones(),Boolean.FALSE,new ArrayList<>(),new Double(0),new String());
              DtoInformeIntegralDocente.Desempeño dtoDes = new DtoInformeIntegralDocente.Desempeño(new DesempenioEvaluaciones(),new ArrayList<>(),new Double(0),new String());
              DtoInformeIntegralDocente.Pares dtoPares = new DtoInformeIntegralDocente.Pares(new Evaluaciones(),new ArrayList<>(),new Double(0),new String());
              List<DtoInformeIntegralDocente.EvaluacionIntegral > dtoEvIntegral= new ArrayList<>();
              DtoInformeIntegralDocente.InformeIntegral dto = new DtoInformeIntegralDocente.InformeIntegral(new Personal(),new AreasUniversidad(),new AreasUniversidad(),dtoDes,dtoPares,dtoTutor,dtoDocente,dtoEvIntegral,new Double(0.0));
              rol.setInformeIntegral(dto);
              rol.setMappedList(new HashMap<>());
             mostrarMensajeResultadoEJB(resInforme);
          }
        }catch (Exception e){
            //mostrarExcepcion(e);
        }
    }


    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "informeIntegralDocente";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
}
