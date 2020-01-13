package mx.edu.utxj.pye.sgi.controlador;

import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoReporteBecas;
import mx.edu.utxj.pye.sgi.ejb.EjbReporteBecas;
import mx.edu.utxj.pye.sgi.entity.prontuario.BecaTipos;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.BecasPeriodosEscolares;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.EstudiantesPye;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaAlumnosPye;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



@Named
@ViewScoped
public class reporteBecas extends ViewScopedRol{

    @EJB EjbReporteBecas ejbReporteBecas;
    @EJB private EjbPersonal ejbPersonal;
    @Inject private LogonMB logonMB;
    @Getter @Setter PeriodosEscolares periodo;
    @Getter @Setter List<BecasPeriodosEscolares> listRegistroBecas;
    @Getter @Setter dtoReporteBecas estudiante;
    @Getter @Setter List<dtoReporteBecas> reporteBecas,reporteBecasFilter;
    @Getter @Setter List<PeriodosEscolares> periodos ;
    @Getter @Setter Personal director;


@Getter private Boolean cargado = false;



    @PostConstruct
    public void init(){
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
        setVistaControlador(ControlEscolarVistaControlador.REPORTE_BECAS);
        getPeriodosBecas() ;
        getPersona();
    }
    
    //TODO: Obr
    public void getPersona(){
        try {
            //TODO: Obtiene el obejeto de la paersona logueada
            Integer claveNomina = logonMB.getPersonal().getClave();
            director = ejbPersonal.mostrarPersonalLogeado(claveNomina);
        }catch (Throwable e){
        }
    }

    //TODO: Obtiene lista de periodos de los registros de becas
    public void getPeriodosBecas(){
        ResultadoEJB<List<PeriodosEscolares>> resPeriodos= ejbReporteBecas.getPeriodosBecas();
        if(resPeriodos.getCorrecto()==true){
            periodos = resPeriodos.getValor();
        }else {mostrarMensajeResultadoEJB(resPeriodos);}
    }
    //TODO: Obtiene la lista general de registros de becas, segun el periodo que seleccionó
    public void obReporteGeneralBecas(){
        //System.err.println("--->Entro a reporte general Periodo ->" + periodo);
        listRegistroBecas= new ArrayList<>();
        reporteBecas = new ArrayList<>();
        ResultadoEJB<List<BecasPeriodosEscolares>> resRegistroBecas = ejbReporteBecas.getBecasbyPeriodo(periodo);
        if(resRegistroBecas.getCorrecto()==true){
            //System.err.println("Entro a correcto en becas!");
            listRegistroBecas = resRegistroBecas.getValor();
           // System.err.println("Registros -..>" + listRegistroBecas);
            //TODO: Recorre la lista para determinar si el estudiante esta registrado en Sauiit o en Control Escolar
            listRegistroBecas.forEach(e->{
                //TODO: Debe comprobar si el estudiante esta registrado en Sauiit o en Control escolar
                estudiante = new dtoReporteBecas();
                estudiante = getEstudiante(periodo, e.getMatriculaPeriodosEscolares().getMatricula());
                //System.err.println("Estudiante ->" + estudiante);
                //System.err.println("Matricula ->" + e.getMatriculaPeriodosEscolares().getMatricula());
                //TODO:Se busca el tipo de beca
                ResultadoEJB<BecaTipos> resTipoBeca = ejbReporteBecas.getTipoBecabyClave((int) e.getBeca());
                if(resTipoBeca.getCorrecto()==true){
                    //TODO:S
                    estudiante.setTipoBeca(resTipoBeca.getValor());
                }else {mostrarMensajeResultadoEJB(resTipoBeca);}
                reporteBecas.add(estudiante);  
            });
            //System.err.println("Total de registros " + reporteBecas.size());
        }else {
            
            mostrarMensajeResultadoEJB(resRegistroBecas);}

    }
    //TODO: Filtra los resultados generales por dirección
    public void obReporteBecasbyDireccion(){
        List<dtoReporteBecas> reporteDirector = new ArrayList<>();       
        obReporteGeneralBecas();
        reporteDirector = reporteBecas.stream().filter(b->director.getClave().equals(b.getDirector().getClave())).collect(Collectors.toList());
        reporteBecas = reporteDirector;
    }
    //TODO: Busca al estudiante en sauiit y en CE
    public dtoReporteBecas getEstudiante(PeriodosEscolares periodo, String matricula){
        dtoReporteBecas dto  = new dtoReporteBecas();
        VistaAlumnosPye estudianteSauiit = new VistaAlumnosPye();
        EstudiantesPye estudianteCE = new EstudiantesPye();
        //TODO: Busca al estudiante en Sauiit
        ResultadoEJB<VistaAlumnosPye> resSauiit = ejbReporteBecas.getEstudiantesSauiitbyPeriodo(matricula);
        if(resSauiit.getCorrecto()==true){
            estudianteSauiit = resSauiit.getValor();
            //TODO: Debe llenar el dto
            dto.setNombreC(estudianteSauiit.getNombre()+ " " + estudianteSauiit.getApellidoPat() + " " + estudianteSauiit.getApellidoMat());
            dto.setMatricula(estudianteSauiit.getMatricula());
            dto.setGrado(estudianteSauiit.getGrado());
            dto.setGrupo(estudianteSauiit.getIdGrupo());
            dto.setSexo(estudianteSauiit.getSexo().toString());
            dto.setStatus(estudianteSauiit.getDescripcion());
            //se obtiene la carrera por siglas
            ResultadoEJB<AreasUniversidad> resArea= ejbReporteBecas.getAreabySiglas(estudianteSauiit.getAbreviatura());
            if(resArea.getCorrecto()==true){
                dto.setCarrera(resArea.getValor());
               // System.err.println("Area " + resArea.getValor());
            }else{mostrarMensajeResultadoEJB(resArea);}
            //TODO: Se obtiene al director
            /*ResultadoEJB<Personal> resDirector = ejbReporteBecas.getDirectorArea(resArea.getValor());
            if(resDirector.getCorrecto()==true){
                    dto.setDirector(resDirector.getValor());
                    System.err.println("DIRECTOR "+ resArea.getValor());
            }else{mostrarMensajeResultadoEJB(resDirector);}
            */
            
        }else{
        //TODO: Busca al estudiante en CE
        ResultadoEJB<EstudiantesPye> resCE= ejbReporteBecas.getEstudiantesCEbyPeriodo(matricula);
        if(resCE.getCorrecto()==true){
            estudianteCE = resCE.getValor();
            //System.err.println("El estudiante está en CE");
            //TODO: Llenar dto
             dto.setNombreC(estudianteCE.getNombre()+ " " + estudianteCE.getAPaterno() + " " + estudianteCE.getAMaterno());
            dto.setMatricula(estudianteCE.getMatricula());
            dto.setGrado(estudianteCE.getGrado());
            dto.setGrupo(estudianteCE.getGrupo().toString());
            dto.setSexo(estudianteCE.getSexo());
            dto.setStatus(estudianteCE.getDescripcion());
            //se obtiene la carrera por clave
            ResultadoEJB<AreasUniversidad> resArea= ejbReporteBecas.getAreabyClave((int)estudianteCE.getCarrera());
            if(resArea.getCorrecto()==true){
                dto.setCarrera(resArea.getValor());
            }else{mostrarMensajeResultadoEJB(resArea);}
            /*
            //TODO: Se obtiene al director
            ResultadoEJB<Personal> resDirector = ejbReporteBecas.getDirectorArea(resArea.getValor());
            if(resDirector.getCorrecto()==true){
                    dto.setDirector(resDirector.getValor());
            }else{mostrarMensajeResultadoEJB(resDirector);}
            */
        }else{
        Messages.addGlobalWarn("El estudiante no esta registrado en ninguna base");    
            }
        }      
       return dto;  
    }
}
       
