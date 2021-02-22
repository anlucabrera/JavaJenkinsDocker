package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReporteFichaAdmision;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReporteProyeccionFichas;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.FichaAdmisionReporteRol;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ProcesoInscripcionRolServiciosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbFichaAdmisionReporte;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named(value = "fichaAdmisionReporte")
@ViewScoped
public class FichaAdmisionReporte extends ViewScopedRol implements Desarrollable {


    @EJB private EjbPropiedades ep;
    @EJB private EjbFichaAdmisionReporte ejb;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;
    @Getter @Setter FichaAdmisionReporteRol rol;
    @Getter private Boolean cargado = false;

    @Getter @Setter DtoReporteFichaAdmision dtoConcentrado;
    @Getter @Setter private List<Aspirante> listaAspirantesTSUXPE;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "fichaAdmisionReporte";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }

    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.FICHA_ADMISION_REPORTE);
            ResultadoEJB<PersonalActivo> resAcceso = ejb.validarAcceso(logonMB.getPersonal().getClave());
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
            ResultadoEJB<PersonalActivo> resValidacion = ejb.validarAcceso(logonMB.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
            rol = new FichaAdmisionReporteRol();
            rol.setPersonalActivo(resValidacion.getValor());
            tieneAcceso = rol.tieneAcceso(resValidacion.getValor(),UsuarioTipo.TRABAJADOR);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            rol.setPersonalActivo(resValidacion.getValor());
            ResultadoEJB<ProcesosInscripcion> resPreceso = ejb.getUltimoProcesoInscripcion();
            mostrarMensajeResultadoEJB(resAcceso);
            if(!resPreceso.getCorrecto()) tieneAcceso = false;//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
            rol.setProcesosInscripcion(resPreceso.getValor());
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!resPreceso.getCorrecto()) mostrarMensajeResultadoEJB(resPreceso);
            rol.setNivelRol(NivelRol.CONSULTA);
            //Obtiene PE activos
            getPE();
            rol.setProcesoSelect(rol.getProcesosInscripcion());
            getConcentrado();
            getReporte();
            getProcesos();
        }catch (Exception e){
            mostrarExcepcion(e);
        }

    }
    //Obtiene la lista de procesos de inscripcion para nuevo ingreso
    public void getProcesos(){
        try{
            ResultadoEJB<List<ProcesosInscripcion>> resProc= ejb.getProcesosInscripcion();
            if(resProc.getCorrecto()){
                rol.setListProcesosInsc(resProc.getValor());
            }else {mostrarMensajeResultadoEJB(resProc);}
        }catch (Exception e){mostrarExcepcion(e);}
    }



    /**
     * Obtiene los planes educativos activos
     */
    public void getPE(){
        try{
            ResultadoEJB<List<AreasUniversidad>> resPE = ejb.getAreasVigentes();
            if(resPE.getCorrecto()==true){
                rol.setPeActivas(resPE.getValor());
                //System.out.println("Areas " + rol.getPeActivas().size());

            }else {
                mostrarMensajeResultadoEJB(resPE);
            }
        }catch (Exception e){
            mostrarExcepcion(e);
        }

    }

    public void getConcentrado(){
        try{
            //System.out.println("Proceso ->" + rol.getProcesosInscripcion() + "Proceso select" + rol.getProcesoSelect());
            rol.setProcesosInscripcion(rol.getProcesoSelect());
            List<DtoReporteFichaAdmision> concentrado = new ArrayList<>();
            rol.getPeActivas().forEach(p->{
                dtoConcentrado = new DtoReporteFichaAdmision();
                dtoConcentrado = getTotales(p);
                //System.out.println("dto -------" +dtoConcentrado);
                concentrado.add(dtoConcentrado);
            });
            rol.setConcentradoFichas(concentrado);
            calculaTotalesIns();


            //System.out.println("Total " + rol.getConcentradoFichas().size());

        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    public void getReporte(){
        try{
            ResultadoEJB<List<DtoReporteProyeccionFichas>> resReporte = ejb.getReporte(rol.getProcesosInscripcion());
            if(resReporte.getCorrecto()==true){
                rol.setReporte(resReporte.getValor());
            }else {mostrarMensajeResultadoEJB(resReporte);}
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    public void calculaTotalesIns(){
        //total proyeccion institucional
        rol.setTotalfichasProyectadasInstitucional(0);
        rol.setTotalInscritosProyectadasInstitucional(0);
        //Fichas registradas institucional
        rol.setTotalFichasRegistradasSemanalInstitucional(0);
        rol.setTotalFichasRegistradasSabatinoInstitucional(0);
        rol.setTotalFichasRegistradasInstitucional(0);
        //Fichas validadas institucional
        rol.setTotalFichasValidadasSemanalInstitucional(0);
        rol.setTotalFichasValidadasSabatinoInstitucional(0);
        rol.setTotalFichasValidadasInstitucional(0);
        // Inscripción institucional
        rol.setTotalInscripcionSemanalInstitucional(0);
        rol.setTotalInscripcionSabatinoInstitucioanal(0);
        rol.setTotalInscripcionInstitucional(0);
        //Porcentajes de avance
        rol.setPorcentajeFichasValidadasInstitucional(new Double(0));
        rol.setPorcentajeInscritosInstitucional(new Double(0));

        rol.getConcentradoFichas().stream().forEach(c->{
            //Total de proyeccion institucional
            rol.setTotalfichasProyectadasInstitucional(rol.getTotalfichasProyectadasInstitucional()+c.getProyeccionFichasValidadas());
            rol.setTotalInscritosProyectadasInstitucional(rol.getTotalInscritosProyectadasInstitucional()+c.getProyeccionMatricula());
            // Total fichas registradas institucional
            rol.setTotalFichasRegistradasSemanalInstitucional(rol.getTotalFichasRegistradasSemanalInstitucional()+c.getTotalRegistroSemanal());
            rol.setTotalFichasRegistradasSabatinoInstitucional(rol.getTotalFichasRegistradasSabatinoInstitucional()+c.getTotalRegistroSabatino());
            rol.setTotalFichasRegistradasInstitucional(rol.getTotalFichasRegistradasInstitucional()+c.getTotalRegistros());
            //Total fichas validadas institucional
            rol.setTotalFichasValidadasSemanalInstitucional(rol.getTotalFichasValidadasSemanalInstitucional()+c.getTotalRegistroSemanalValido());
            rol.setTotalFichasValidadasSabatinoInstitucional(rol.getTotalFichasValidadasSabatinoInstitucional()+c.getTotalRegistroSabatinoValido());
            rol.setTotalFichasValidadasInstitucional(rol.getTotalFichasValidadasInstitucional()+c.getTotalRegistrosValidados());
            //Total inscritos validados
            rol.setTotalInscripcionSemanalInstitucional(rol.getTotalInscripcionSemanalInstitucional()+c.getTotalInscritosSemanal());
            rol.setTotalInscripcionSabatinoInstitucioanal(rol.getTotalInscripcionSabatinoInstitucioanal()+c.getTotalInscritosSabatino());
            rol.setTotalInscripcionInstitucional(rol.getTotalInscripcionInstitucional()+c.getTotalInscritos());

        }
        );
        if(rol.getTotalFichasValidadasInstitucional()!=0){
            //Porcentaje de fichas institucional
            Double pFichas = new Double(rol.getTotalfichasProyectadasInstitucional());
            Double totalFichas= new Double(rol.getTotalFichasValidadasInstitucional());
            Double porcentajeFichas = new Double(0);
            porcentajeFichas= (totalFichas*100)/pFichas;
            BigDecimal big= new BigDecimal(porcentajeFichas).setScale(2, RoundingMode.UP);
            rol.setPorcentajeFichasValidadasInstitucional(big.doubleValue());
            //System.out.println("Porcentaje fichas "+ rol.getPorcentajeFichasValidadasInstitucional());
        }else {rol.setPorcentajeFichasValidadasInstitucional(new Double(0));}
        if(rol.getTotalInscripcionInstitucional()!=0){
            //Porcentaje de inscripcion institucional
            Double pInscripcion = new Double(rol.getTotalInscritosProyectadasInstitucional());
            Double totalInscritos= new Double(rol.getTotalInscripcionInstitucional());
            Double porcentajeInscritos = new Double(0);
            porcentajeInscritos= (totalInscritos*100)/pInscripcion;
            BigDecimal big2= new BigDecimal(porcentajeInscritos).setScale(2, RoundingMode.UP);
            rol.setPorcentajeInscritosInstitucional(big2.doubleValue());
           // System.out.println("Porcentaje insc "+ rol.getPorcentajeInscritosInstitucional());
        }else {rol.setPorcentajeInscritosInstitucional(new Double(0));}

    }
    /*
    Calcula el total de aspirantes registrados y validados por programa educativo
     */
    public DtoReporteFichaAdmision getTotales(AreasUniversidad pe){
        try{
            DtoReporteFichaAdmision dtoReporteFichaAdmision = new DtoReporteFichaAdmision();
            dtoReporteFichaAdmision.setPe(pe);
            List<Aspirante> aspirantesbyPe= new ArrayList<>();
            //Obtienes aspirantes por plan educativo
            ResultadoEJB<List<Aspirante>> res = ejb.getAspirantesbyPE(pe,rol.getProcesosInscripcion());
            //Obtiene la lista de estuduantes inscritos
            List<Estudiante> inscritosbyPE= new ArrayList<>();
            ResultadoEJB<List<Estudiante>> resEstudiantes= ejb.getInscritosbyPE(pe,rol.getProcesosInscripcion());
            ProyeccionAreas proyeccionAreas =new ProyeccionAreas();
            //Obtiene la proyeccion de fichas y matricula por programa educativo
            ResultadoEJB<ProyeccionAreas> resProyeccion= ejb.getProyeccionbyArea(rol.getProcesosInscripcion(),pe);
           // System.out.println(res.getValor().size()+" Estudiantes -> " +resEstudiantes.getValor().size()+ "Proyeccion "+ resProyeccion.getValor());
            ////////////REGISTROS DE FICHAS
            //System.out.println("Total de Apirantes " +res.getValor().size());
            if(res.getCorrecto()== true){
                //La carrera tiene registros de fichas

                aspirantesbyPe = res.getValor();
                dtoReporteFichaAdmision.setTotalRegistroSemanal(aspirantesbyPe.stream()
                        .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Semanal"))
                        .count());

                dtoReporteFichaAdmision.setTotalRegistroSabatino(aspirantesbyPe.stream()
                        .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Sabatino"))
                        .count());

                dtoReporteFichaAdmision.setTotalRegistroSemanalValido(aspirantesbyPe.stream()
                        .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Semanal") && x.getEstatus() == true)
                        .count());

                dtoReporteFichaAdmision.setTotalRegistroSabatinoValido( aspirantesbyPe.stream()
                        .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Sabatino") && x.getEstatus() == true)
                        .count());
                dtoReporteFichaAdmision.setTotalRegistros(dtoReporteFichaAdmision.getTotalRegistroSemanal()+dtoReporteFichaAdmision.getTotalRegistroSabatino());
                dtoReporteFichaAdmision.setTotalRegistrosValidados(dtoReporteFichaAdmision.getTotalRegistroSemanalValido() + dtoReporteFichaAdmision.getTotalRegistroSabatinoValido());

            }else {
                //mostrarMensajeResultadoEJB(resEstudiantes);
                //La carrera no tiene registros
                dtoReporteFichaAdmision.setTotalRegistroSemanal(0);
                dtoReporteFichaAdmision.setTotalRegistroSabatino(0);
                dtoReporteFichaAdmision.setTotalRegistrosValidados(0);
                dtoReporteFichaAdmision.setTotalRegistroSabatinoValido(0);
                dtoReporteFichaAdmision.setTotalRegistros(0);
                dtoReporteFichaAdmision.setTotalRegistros(dtoReporteFichaAdmision.getTotalRegistroSemanal()+dtoReporteFichaAdmision.getTotalRegistroSabatino());
                dtoReporteFichaAdmision.setTotalRegistrosValidados(dtoReporteFichaAdmision.getTotalRegistroSemanalValido()+dtoReporteFichaAdmision.getTotalRegistroSabatinoValido());

               // return dtoReporteFichaAdmision;
            }
            ///// ESTUDIANTES INSCRITOS
            if(resEstudiantes.getCorrecto()==true){
                inscritosbyPE= resEstudiantes.getValor();
                dtoReporteFichaAdmision.setTotalInscritos(resEstudiantes.getValor().size());
                dtoReporteFichaAdmision.setTotalInscritosSemanal(inscritosbyPE.stream()
                        .filter(x -> x.getGrupo().getIdSistema().getNombre().equals("Semanal"))
                        .count());
                dtoReporteFichaAdmision.setTotalInscritosSabatino(inscritosbyPE.stream()
                        .filter(x -> x.getGrupo().getIdSistema().getNombre().equals("Sabatino"))
                        .count());

            }else {
                //No tiene estudiantes inscritos
                dtoReporteFichaAdmision.setTotalInscritos(0);
                dtoReporteFichaAdmision.setTotalInscritosSabatino(0);
                dtoReporteFichaAdmision.setTotalInscritosSemanal(0);

            }
            // PROYECCION
            if(resProyeccion.getCorrecto()==true){
                proyeccionAreas= resProyeccion.getValor();
                dtoReporteFichaAdmision.setProyeccionMatricula(proyeccionAreas.getProyeccionMatricula());
                dtoReporteFichaAdmision.setProyeccionFichasValidadas(proyeccionAreas.getProyeccionValidadas());
            }else {
                //No existe proyeccion registrada para el proceso de inscripcion seleccionada
                dtoReporteFichaAdmision.setProyeccionMatricula(0);
                dtoReporteFichaAdmision.setProyeccionFichasValidadas(0);

            }
            //Porcentaje de fichas
            Double dte = new Double(dtoReporteFichaAdmision.getProyeccionFichasValidadas());
            Double dc= new Double(dtoReporteFichaAdmision.getTotalRegistrosValidados());
            Double porcentajeFichas = new Double(0);
            porcentajeFichas= (dc*100)/dte;
            dtoReporteFichaAdmision.setPorcentajeAlcanceFichas(porcentajeFichas);
            //Porcentaje de matricula
            Double mP = new Double(dtoReporteFichaAdmision.getProyeccionMatricula());
            Double mR= new Double(dtoReporteFichaAdmision.getTotalInscritos());
            Double porcentajeMatricula = new Double(0);
            porcentajeFichas= (mR*100)/mP;
            dtoReporteFichaAdmision.setPorcentajeAlcanceMatricula(porcentajeMatricula);

            return dtoReporteFichaAdmision;

        }catch (Exception e){
            mostrarExcepcion(e);
            return new DtoReporteFichaAdmision();
        }
    }

    /*
    Obtiene el total de matricula inscrita por programa educativo
     */
    public DtoReporteFichaAdmision getTotalMatriculabyPe(AreasUniversidad pe){
        try{
            DtoReporteFichaAdmision dtoReporteFichaAdmision = new DtoReporteFichaAdmision();
            List<Estudiante> inscritosbyPE= new ArrayList<>();
            ResultadoEJB<List<Estudiante>> resEstudiantes= ejb.getInscritosbyPE(pe,rol.getProcesosInscripcion());
            if(resEstudiantes.getCorrecto()){
                // El programa educativo tiene esrudiantes inscritos
                inscritosbyPE= resEstudiantes.getValor();
                dtoReporteFichaAdmision.setTotalInscritos(resEstudiantes.getValor().size());
                dtoReporteFichaAdmision.setTotalInscritosSemanal(inscritosbyPE.stream()
                        .filter(x -> x.getGrupo().getIdSistema().getNombre().equals("Semanal"))
                        .count());
                dtoReporteFichaAdmision.setTotalInscritosSabatino(inscritosbyPE.stream()
                        .filter(x -> x.getGrupo().getIdSistema().getNombre().equals("Sabatino"))
                        .count());
                return dtoReporteFichaAdmision;

            }else {
                //No tiene estudiantes inscritos
                dtoReporteFichaAdmision.setTotalInscritos(0);
                dtoReporteFichaAdmision.setTotalInscritosSabatino(0);
                dtoReporteFichaAdmision.setTotalInscritosSemanal(0);
                return dtoReporteFichaAdmision;
            }
        }catch (Exception e){mostrarExcepcion(e);
            return new DtoReporteFichaAdmision();}
    }
    /*
    Obtiene la proyeccion de fichas y matricula por programa educativo
    */
    public DtoReporteFichaAdmision getProyeccion(AreasUniversidad pe){
        try{
            DtoReporteFichaAdmision dto=new DtoReporteFichaAdmision();
            ProyeccionAreas proyeccionAreas =new ProyeccionAreas();
            //Obtiene la proyeccion de fichas y matricula por programa educativo
            ResultadoEJB<ProyeccionAreas> resProyeccion= ejb.getProyeccionbyArea(rol.getProcesosInscripcion(),pe);
            if(resProyeccion.getCorrecto()){
                //Existe proyeccion registrada para el proceso de inscripcion seleccionado
                proyeccionAreas= resProyeccion.getValor();
                dto.setProyeccionMatricula(proyeccionAreas.getProyeccionMatricula());
                dto.setProyeccionFichasValidadas(proyeccionAreas.getProyeccionValidadas());
                return dto;
            }else {
                //No existe proyeccion registrada para el proceso de inscripcion seleccionada
                dto.setProyeccionMatricula(0);
                dto.setProyeccionFichasValidadas(0);
                return dto;
            }
        }catch (Exception e){mostrarExcepcion(e);
            return  new DtoReporteFichaAdmision();
        }
    }

}
