package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CedulaIdentificacionRolPsicopedagogia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCalificacionEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCedulaIdentificacion;
import mx.edu.utxj.pye.sgi.dto.vista.DtoAlerta;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCedulaIdentificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaCalificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCuestionarioPsicopedagogico;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CuestionarioPsicopedagogicoResultados;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorCuestionarioPsicopedagogicoPersonal;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
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



@Named(value = "cedulaIdentificacionPsicopedagogia")
@ViewScoped
public class cedulaIdentificacionPsicopedagogia extends ViewScopedRol implements Desarrollable {

    @Getter @Setter
    CedulaIdentificacionRolPsicopedagogia rol;
    @Inject LogonMB logonMB;
    @EJB EjbCedulaIdentificacion ejbCedulaIdentificacion;
    @EJB EjbCuestionarioPsicopedagogico ejbCuestionarioPsicopedagogico;
    @EJB private EjbPropiedades ep;
    @EJB EjbConsultaCalificacion ejbCalificaiones;
    @Getter Boolean tieneAcceso = false;
    @Getter @Setter String valor;

    


@Getter private Boolean cargado = false;

@PostConstruct
    public void init(){
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.CEDULA_IDENTIFICACION);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbCedulaIdentificacion.validarPsicopedagogia(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejbCedulaIdentificacion.validarPsicopedagogia(logonMB.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personalPsicopedagogia = filtro.getEntity();//            ejbPersonalBean.pack(logonMB.getPersonal());
            rol = new CedulaIdentificacionRolPsicopedagogia(filtro, personalPsicopedagogia, personalPsicopedagogia.getAreaOperativa());
            tieneAcceso = rol.tieneAcceso(personalPsicopedagogia);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setPersonalPsicopedagogia(personalPsicopedagogia);
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
            //TODO:Apartados para cuestionario
            rol.setApartadoCuestionario(ejbCuestionarioPsicopedagogico.getApartados());
            //TODO:Respuestas posibles
            rol.setSino(ejbCuestionarioPsicopedagogico.getSiNo());
            rol.setGruposVunerabilidad(ejbCuestionarioPsicopedagogico.getGruposVunerabilidad());
            rol.setEstadoCivilPadres(ejbCuestionarioPsicopedagogico.getEstadoCivilPadres());
            //TODO: Instrucciones
            rol.getInstrucciones().add("Ingrese el nombre o la matricula del estudiante que desea buscar");
            rol.getInstrucciones().add("Los datos del estudiante se cargarán automaticamente.");
            rol.getInstrucciones().add("NOTA IMPORTANTE: La coordinación de desarrollo de software sigue trabajando en algunos datos que son necesarios en la cédula de identificación, los cuales se estarán liberando en las próximos días.");
            rol.getInstrucciones().add("NOTA IMPORNTATE: La mayoría de los datos recabados forman parte del proceso de inscripción del estudiante y se tienen almacenados en nuestras bases de datos, si existen campos vacios, es porque no tenemos dicha información.");



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
            rol.setEstudiante(resEstudiante.getValor());
            //TODO: Se ejecuta el metodo de busqueda
            ResultadoEJB<DtoCedulaIdentificacion> resCedula = ejbCedulaIdentificacion.getCedulaIdentificacion(rol.getEstudiante().getMatricula());
            if(resCedula.getCorrecto()==true){
                //System.out.println("Entro a genera cedula");
                rol.setCedulaIdentificacion(resCedula.getValor());
                getPeriodoEstudiante();
                getResultadosCuestionario();
                alertas();
            }else{ mostrarMensajeResultadoEJB(resCedula); }
        } else {mostrarMensajeResultadoEJB(resEstudiante);}
    }
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "cedulaIdentificacion";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));

    }

    public void alertas(){
        setAlertas(Collections.EMPTY_LIST);
        ResultadoEJB<List<DtoAlerta>> resMensajes = ejbCuestionarioPsicopedagogico.identificaMensajes(rol);
        if (resMensajes.getCorrecto()) {
            setAlertas(resMensajes.getValor());
        } else {
            mostrarMensajeResultadoEJB(resMensajes);
        }
        repetirUltimoMensaje();
    }
    //TODO: Obtiene los resultados del cuestionario psicopedagogico
    public void getResultadosCuestionario(){
        CuestionarioPsicopedagogicoResultados resultados = new CuestionarioPsicopedagogicoResultados();
        ResultadoEJB<CuestionarioPsicopedagogicoResultados> resResultados = ejbCuestionarioPsicopedagogico.getResultadosCuestionarioPersonal(rol.getEstudiante(),rol.getPersonalPsicopedagogia().getPersonal());
        if(resResultados.getCorrecto()==true){resultados = resResultados.getValor();
        rol.setResultados(resultados);
        }else { rol.setResultados(resultados);}
    }
    public void getPeriodoEstudiante(){
        PeriodosEscolares periodo = new PeriodosEscolares();
        ResultadoEJB<PeriodosEscolares> resPeriodo= ejbCedulaIdentificacion.getPeriodoEstudiante(rol.getEstudiante());
        if(resPeriodo.getCorrecto()==true){rol.setPeriodoEstudiante(resPeriodo.getValor());rol.setPeriodoE(rol.getPeriodoEstudiante().getPeriodo());}
        else {mostrarMensajeResultadoEJB(resPeriodo);}
    }

    /**
     * Guarda la respuesta del personal
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
        ResultadoEJB<CuestionarioPsicopedagogicoResultados> refrescar=ejbCuestionarioPsicopedagogico.cargaResultadosCuestionarioPsicopedagogicoPersonal(id.getId(), valor, rol.getResultados(), Operacion.REFRESCAR);
        ResultadoEJB<CuestionarioPsicopedagogicoResultados> save=ejbCuestionarioPsicopedagogico.cargaResultadosCuestionarioPsicopedagogicoPersonal(id.getId(), valor, rol.getResultados(), Operacion.PERSISTIR);
        comprobar();

    }

    public void comprobar(){
        //System.out.println("COMPROBAR");
        Comparador<CuestionarioPsicopedagogicoResultados> comparador = new ComparadorCuestionarioPsicopedagogicoPersonal();
        rol.setFinalizadoPersonal(comparador.isCompleto(rol.getResultados()));
        if(rol.isFinalizadoPersonal()==true){
            ResultadoEJB<CuestionarioPsicopedagogicoResultados> resActualiza = ejbCuestionarioPsicopedagogico.actualizaRevisado(rol.getResultados());
            if(resActualiza.getCorrecto()==true){rol.setResultados(resActualiza.getValor());
            //System.out.println("Temino ----> " + rol.isFinalizadoPersonal());
               // System.out.println("Actualizo ----> " + rol.getResultados().getReviso());
            }
            else {mostrarMensajeResultadoEJB(resActualiza);}
        }
        //System.out.println(comparador.isCompleto(rol.getResultados()));
    }
    //----------------------- Calificaciones del estudiante(Marce)---------------------

    public void obtenerMateriasPorEstudiante() {
        ResultadoEJB<List<DtoCalificacionEstudiante.MateriasPorEstudiante>> resMaterias = ejbCalificaiones.packMaterias(rol.getEstudiante());
        rol.setMateriasPorEstudiante(resMaterias.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
        Map<Integer, Long> group = rol.getMateriasPorEstudiante().stream().collect(Collectors.groupingBy(DtoCalificacionEstudiante.MateriasPorEstudiante::getPeriodo, Collectors.counting()));
        group.forEach((k, v) -> {
            rol.setPeriodo(k);
        });
    }
    public void obtenerUnidadesPorMateria() {
        /*List<DtoCalificacionEstudiante.MapUnidadesTematicas> resMap = new ArrayList<>();
        ResultadoEJB<List<DtoCalificacionEstudiante.UnidadesPorMateria>> resUnidadesPorMateria = ejbCalificaiones.packUnidadesmateria(rol.getEstudiante());
        rol.setUnidadesPorMateria(resUnidadesPorMateria.getValor());
        rol.getUnidadesPorMateria().forEach(x -> {
            x.getUnidadMateriaConfiguracion().forEach(y -> {
                resMap.add(new DtoCalificacionEstudiante.MapUnidadesTematicas(y.getIdUnidadMateria().getNoUnidad(), y.getIdUnidadMateria().getNoUnidad()));
            });
        });
        rol.setMapUnidadesTematicas(new ArrayList<>(new HashSet<>(resMap)));
        rol.getMapUnidadesTematicas().sort(Comparator.comparingInt(DtoCalificacionEstudiante.MapUnidadesTematicas::getNoUnidad));*/
    }
    public void obtenerCalificaciones() {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorUnidad>> resCalificaciones = ejbCalificaiones.packCalificacionesPorUnidadyMateria1(rol.getEstudiante());
        rol.setCalificacionePorUnidad(resCalificaciones.getValor().stream().filter(a -> a.getEstudiante().getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
    }
    public void obtenerPromedioMateria() {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorMateria>> resultadoEJB = ejbCalificaiones.packPromedioMateria(rol.getEstudiante());
        rol.setCalificacionePorMateria(resultadoEJB.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
    }
    public void obtenerPromediosFinales() {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorMateria>> resultadoEJB = ejbCalificaiones.packCalificacionesFinales(rol.getEstudiante());
        rol.setCalificacionesFinalesPorMateria(resultadoEJB.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
    }
    public void obtenerPromedioCuatrimestral() {
        ResultadoEJB<BigDecimal> promedio = ejbCalificaiones.obtenerPromedioCuatrimestral( rol.getEstudiante(), rol.getPeriodo());
        BigDecimal valor = promedio.getValor();
        rol.setMateriasPorEstudiante(ejbCalificaiones.packMaterias(rol.getEstudiante()).getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
        BigDecimal numeroMaterias = new BigDecimal(rol.getMateriasPorEstudiante().size());
        BigDecimal promedioCuatrimestral = valor.divide(numeroMaterias, RoundingMode.HALF_UP);
        rol.setPromedio(promedioCuatrimestral.setScale(1, RoundingMode.HALF_UP));
    }
    public void obtenerPromedioAcumulado() {
        ResultadoEJB<List<BigDecimal>> promedios = ejbCalificaiones.obtenerPromedioAcumulado(rol.getEstudiante());
        BigDecimal numeroPromedios = new BigDecimal(promedios.getValor ().size());
        BigDecimal suma = promedios.getValor().stream().map(BigDecimal::plus).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal promedio = suma.divide(numeroPromedios, RoundingMode.HALF_UP);
        rol.setPromedioAcumluado(promedio.setScale(1, RoundingMode.HALF_UP));
    }

    public void obtenerTareaIntegradoraPorMateria(){
        ResultadoEJB<List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion>> resultadoEJB = ejbCalificaiones.tareaIntegradoraPresentacion(rol.getEstudiante());
        rol.setTareaIntegradoraPresentacion(resultadoEJB.getValor());
    }

    public void obtenerNivelacionesPorMateria(){
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria>> resultadoEJB = ejbCalificaiones.packPromedioNivelacionPorMateria(rol.getEstudiante());
        rol.setCalificacionesNivelacionPorMateria(resultadoEJB.getValor());
    }
}
