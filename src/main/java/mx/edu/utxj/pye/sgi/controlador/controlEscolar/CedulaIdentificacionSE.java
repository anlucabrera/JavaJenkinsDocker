package mx.edu.utxj.pye.sgi.controlador.controlEscolar;


import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CedulaIdentificacionRolSE;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCalificacionEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCedulaIdentificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAdministracionEstudiantesSE;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCedulaIdentificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaCalificacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Login;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



@Named(value = "cedulaIdentificacionSE")
@ViewScoped

public class CedulaIdentificacionSE extends ViewScopedRol implements Desarrollable {
    @Getter @Setter CedulaIdentificacionRolSE rol;
    @Inject LogonMB logonMB;
    @EJB EjbCedulaIdentificacion ejbCedulaIdentificacion;
    @EJB EjbAdministracionEstudiantesSE ejbAdministracionEstudiantesSE;
    @EJB EjbConsultaCalificacion ejbCalificaiones;
    @Getter Boolean tieneAcceso = false;

    @EJB private EjbPropiedades ep;



@Getter private Boolean cargado = false;


    @PostConstruct
    public void init(){
        try{
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.CEDULA_IDENTIFICACION_SE);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbAdministracionEstudiantesSE.validaSE(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejbAdministracionEstudiantesSE.validaSE(logonMB.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personalSE = filtro.getEntity();//            ejbPersonalBean.pack(logonMB.getPersonal());
            rol = new CedulaIdentificacionRolSE(filtro, personalSE, personalSE.getAreaOperativa());
            tieneAcceso = rol.tieneAcceso(personalSE);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setServiciosEscolares(personalSE);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.OPERATIVO);

            //Todo: Se obtienen los datos de la cédula de identificación
            rol.setApartados(ejbCedulaIdentificacion.getApartados());
            //TODO: Obtiene la lista de estudiantes
            ResultadoEJB<List<Estudiante>> resEstudiantes= ejbCedulaIdentificacion.getEstudiantes();
            if(resEstudiantes.getCorrecto()==true){rol.setEstudiantes(resEstudiantes.getValor());}
            else {mostrarMensajeResultadoEJB(resEstudiantes);}
            //TODO: Instrucciones
            rol.getInstrucciones().add("Ingrese el nombre o la matricula del estudiante que desea buscar");
            rol.getInstrucciones().add("Los datos del estudiante se cargarán automaticamente.");
            rol.getInstrucciones().add("NOTA IMPORTANTE: La coordinación de desarrollo de software sigue trabajando en algunos datos que son necesarios en la cédula de identificación, los cuales se estarán liberando en las próximos días.");
            rol.getInstrucciones().add("NOTA IMPORNTATE: La mayoría de los datos recabados forman parte del proceso de inscripción del estudiante y se tienen almacenados en nuestras bases de datos, si existen campos vacios, es porque no tenemos dicha información.");
            rol.setAvisoPrivacidad(false);

        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    //TODO: Busca los datos del estudiante
    public void getEstudiante(){
        if(rol.getMatricula()== null) return;
        //TODO:Busca al estudiante
        ResultadoEJB<Estudiante> resEstudiante = ejbCedulaIdentificacion.validaEstudiante(Integer.parseInt(rol.getMatricula()));
        if(resEstudiante.getCorrecto()==true){
            rol.setEstudiante(resEstudiante.getValor());rol.setEstudiantePeriodo(resEstudiante.getValor());
            //TODO: Se ejecuta el metodo de busqueda
            ResultadoEJB<DtoCedulaIdentificacion> resCedula = ejbCedulaIdentificacion.getCedulaIdentificacion(rol.getEstudiante().getMatricula());
            if(resCedula.getCorrecto()==true){
                getPeriodosEstudiante();
                //System.out.println("Entro a genera cedula");
                rol.setCedulaIdentificacion(resCedula.getValor());
                getPeriodoEstudiante();
               // getCalificaciones();
            }else{ mostrarMensajeResultadoEJB(resCedula); }
        } else {mostrarMensajeResultadoEJB(resEstudiante);}
    }

    public void generarPwd(){
        String contrasena="";
        int generador;
        for(int i=0;i<6;i++){
            generador = (int)(Math.random()*10);
            contrasena+= generador;
        }
        rol.setPwdNueva(contrasena);
        //System.out.println("Contraseña: " + rol.getPwdNueva());

    }
    public void resetPwd(){
        //System.out.println("Entro");
        rol.setPwdNueva(new String());
        generarPwd();
       // System.out.println(rol.getPwdNueva());
        ResultadoEJB<Login> resReset = ejbAdministracionEstudiantesSE.updatePwdSE(rol);
        if(resReset.getCorrecto()==true){mostrarMensajeResultadoEJB(resReset);}
        else {mostrarMensajeResultadoEJB(resReset);}
    }

    public void getPeriodoEstudiante(){
        PeriodosEscolares periodo = new PeriodosEscolares();
        ResultadoEJB<PeriodosEscolares> resPeriodo= ejbCedulaIdentificacion.getPeriodoEstudiante(rol.getEstudiante());
        if(resPeriodo.getCorrecto()==true){rol.setPeriodoEstudiante(resPeriodo.getValor());rol.setPeriodoE(rol.getPeriodoEstudiante().getPeriodo());}
        else {mostrarMensajeResultadoEJB(resPeriodo);}
    }

    public void getPeriodosEstudiante(){
        List<PeriodosEscolares> periodosEscolares = new ArrayList<>();
        ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejbCedulaIdentificacion.getPeriodosEscolaresbyEstudiante(rol.getEstudiantePeriodo());
        if(resPeriodos.getCorrecto()==true){
            rol.setPeriodosEstudiante(resPeriodos.getValor());
        }else {mostrarMensajeResultadoEJB(resPeriodos);}
    }

    public void getCalificaciones(){
        obtenerMateriasPorEstudiante();
        obtenerUnidadesPorMateria();
        obtenerCalificaciones();
        obtenerPromedioMateria();
        obtenerTareaIntegradoraPorMateria();
        obtenerNivelacionesPorMateria();
        obtenerPromediosFinales();
        obtenerPromedioCuatrimestral();
        // obtenerPromedioAcumulado();
    }
    public void changePeriodo(){
        ResultadoEJB<Estudiante> resEstudiantePeriodo = ejbCedulaIdentificacion.getEstudiantebyPeriodo(rol.getPeriodoEstudiante(),rol.getEstudiantePeriodo());
        if(resEstudiantePeriodo.getCorrecto()==true){
            rol.setEstudiantePeriodo(resEstudiantePeriodo.getValor());
            getPeriodosEstudiante();
            rol.setPeriodoE(resEstudiantePeriodo.getValor().getPeriodo());
            rol.setEstudiantePeriodo(resEstudiantePeriodo.getValor());
            getCalificaciones();
        }else {mostrarMensajeResultadoEJB(resEstudiantePeriodo);}
    }
    public void addMessage() {
        System.out.println("---> "+rol.getAvisoPrivacidad());
        String summary = rol.getAvisoPrivacidad() ? "Aceptado":"Debes aceptar el aviso de privacidad";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
    }

    //----------------------- Calificaciones del estudiante(Marce)---------------------

    public void obtenerMateriasPorEstudiante() {
        ResultadoEJB<List<DtoCalificacionEstudiante.MateriasPorEstudiante>> resMaterias = ejbCalificaiones.packMaterias(rol.getEstudiantePeriodo());
        rol.setMateriasPorEstudiante(resMaterias.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
        Map<Integer, Long> group = rol.getMateriasPorEstudiante().stream().collect(Collectors.groupingBy(DtoCalificacionEstudiante.MateriasPorEstudiante::getPeriodo, Collectors.counting()));
        group.forEach((k, v) -> {
            rol.setPeriodo(k);
        });
    }
    public void obtenerUnidadesPorMateria() {
        List<DtoCalificacionEstudiante.MapUnidadesTematicas> resMap = new ArrayList<>();
        ResultadoEJB<List<DtoCalificacionEstudiante.UnidadesPorMateria>> resUnidadesPorMateria = ejbCalificaiones.packUnidadesmateria(rol.getEstudiantePeriodo());
        rol.setUnidadesPorMateria(resUnidadesPorMateria.getValor());
        rol.getUnidadesPorMateria().forEach(x -> {
            x.getUnidadMaterias().forEach(y -> {
                resMap.add(new DtoCalificacionEstudiante.MapUnidadesTematicas(y.getNoUnidad(), y.getNoUnidad()));
            });
        });
        rol.setMapUnidadesTematicas(new ArrayList<>(new HashSet<>(resMap)));
        rol.getMapUnidadesTematicas().sort(Comparator.comparingInt(DtoCalificacionEstudiante.MapUnidadesTematicas::getNoUnidad));
    }
    public void obtenerCalificaciones() {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorUnidad>> resCalificaciones = ejbCalificaiones.packCalificacionesPorUnidadyMateria1(rol.getEstudiantePeriodo());
        rol.setCalificacionePorUnidad(resCalificaciones.getValor().stream().filter(a -> a.getEstudiante().getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
    }
    public void obtenerPromedioMateria() {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorMateria>> resultadoEJB = ejbCalificaiones.packPromedioMateria(rol.getEstudiantePeriodo());
        rol.setCalificacionePorMateria(resultadoEJB.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
    }
    public void obtenerPromediosFinales() {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorMateria>> resultadoEJB = ejbCalificaiones.packCalificacionesFinales(rol.getEstudiantePeriodo());
        rol.setCalificacionesFinalesPorMateria(resultadoEJB.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
    }
    public void obtenerPromedioCuatrimestral() {
        ResultadoEJB<BigDecimal> promedio = ejbCalificaiones.obtenerPromedioCuatrimestral( rol.getEstudiantePeriodo(), rol.getPeriodoEstudiante().getPeriodo());
        BigDecimal valor = promedio.getValor();
        rol.setMateriasPorEstudiante(ejbCalificaiones.packMaterias(rol.getEstudiantePeriodo()).getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
        BigDecimal numeroMaterias = new BigDecimal(rol.getMateriasPorEstudiante().size());
        BigDecimal promedioCuatrimestral = valor.divide(numeroMaterias, RoundingMode.HALF_UP);
        rol.setPromedio(promedioCuatrimestral.setScale(1, RoundingMode.HALF_UP));
    }
    public void obtenerPromedioAcumulado() {
        ResultadoEJB<List<BigDecimal>> promedios = ejbCalificaiones.obtenerPromedioAcumulado(rol.getEstudiantePeriodo());
        BigDecimal numeroPromedios = new BigDecimal(promedios.getValor ().size());
        BigDecimal suma = promedios.getValor().stream().map(BigDecimal::plus).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal promedio = suma.divide(numeroPromedios, RoundingMode.HALF_UP);
        rol.setPromedioAcumluado(promedio.setScale(1, RoundingMode.HALF_UP));
    }

    public void obtenerTareaIntegradoraPorMateria(){
        ResultadoEJB<List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion>> resultadoEJB = ejbCalificaiones.tareaIntegradoraPresentacion(rol.getEstudiantePeriodo());
        rol.setTareaIntegradoraPresentacion(resultadoEJB.getValor());
    }

    public void obtenerNivelacionesPorMateria(){
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria>> resultadoEJB = ejbCalificaiones.packPromedioNivelacionPorMateria(rol.getEstudiantePeriodo());
        rol.setCalificacionesNivelacionPorMateria(resultadoEJB.getValor());
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "cedulaSE";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));

    }
}
