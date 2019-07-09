//package mx.edu.utxj.pye.sgi.controlador.controlEscolar;
//
//import com.github.adminfaces.starter.infra.model.Filter;
//import com.github.adminfaces.starter.infra.security.LogonMB;
//import lombok.Getter;
//import lombok.Setter;
//import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
//import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
//import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
//import mx.edu.utxj.pye.sgi.dto.controlEscolar.ValidacionConfiguracionMateriaRolDirector;
//import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
//import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateria;
//import mx.edu.utxj.pye.sgi.dto.vista.DtoAlerta;
//import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionAcademica;
//import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
//import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
//import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
//import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
//import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
//import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
//import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativos;
//import mx.edu.utxj.pye.sgi.enums.Operacion;
//import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
//import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
//import org.omnifaces.cdi.ViewScoped;
//import org.omnifaces.util.Ajax;
//
//import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
//import javax.faces.event.ValueChangeEvent;
//import javax.inject.Inject;
//import javax.inject.Named;
//import javax.servlet.http.HttpServletRequest;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
///**
// * La selección del grupo, docente y del periodo deben ser directos de un control de entrada
// */
//@Named
//@ViewScoped
//public class ValidacionConfiguracionMateriaDirector extends ViewScopedRol implements Desarrollable {
//    @Getter @Setter ValidacionConfiguracionMateriaRolDirector rol;
//
//    @EJB EjbAsignacionAcademica ejb;
//    @EJB EjbPropiedades ep;
//    @Inject LogonMB logon;
//    @Getter Boolean tieneAcceso = false;
//
//    /**
//     * Inicializa:<br/>
//     *      El filtro de rol por area superior y categiría operativa<br/>
//     *      La referencia al director si es que el usuario logueado es efectivamente un director por medio del filtro de rol<br/>
//     *      El programa educativo al que pertenece el director por medio de operación segura antierror<br/>
//     *      El DTO del rol<br/>
//     *      La lista de periodos escolares en forma descendente por medio de operación segura antierror<br/>
//     *      EL mapa de programas con grupos por medio de operación segura antierror ordenando programas por areas, niveles y nombre del programa y los grupos por grado y letra
//     */
//    @PostConstruct
//    public void init(){
//        try{
//            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarDirector(logon.getPersonal().getClave());//validar si es director
//            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
//
//            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarDirector(logon.getPersonal().getClave());
//            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
//
//            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
//            PersonalActivo director = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
//            rol = new ValidacionConfiguracionMateriaRolDirector(filtro, director, director.getAreaOficial());
//            tieneAcceso = rol.tieneAcceso(director);
//            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
//
//            rol.setDirector(director);
//            ResultadoEJB<EventoEscolar> resEvento = ejb.verificarEvento(rol.getDirector());
//            if(!resEvento.getCorrecto()) tieneAcceso = false;//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
//            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
//            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
//            if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
//            rol.setNivelRol(NivelRol.OPERATIVO);
////            rol.setSoloLectura(true);
//            rol.setPeriodoActivo(resEvento.getValor().getPeriodo());
//
//            rol.setEventoActivo(resEvento.getValor());
//
//            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDescendentes();
//            if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
//            rol.setPeriodos(resPeriodos.getValor());
//
//            ResultadoEJB<Map<AreasUniversidad, List<Grupo>>> resProgramas = ejb.getProgramasActivos(rol.getDirector(), rol.getPeriodo());
//            if(!resProgramas.getCorrecto()) mostrarMensajeResultadoEJB(resProgramas);
//            rol.setProgramasGruposMap(resProgramas.getValor());
//
//            rol.getInstrucciones().add("Seleccionar periodo escolar activo, de lo contrario solo podrá consultar asignaciones anteriores.");
//            rol.getInstrucciones().add("Seleccionar el programa educativo del cual se va a asignar materias.");
//            rol.getInstrucciones().add("Seleccionar el grupo deseado.");
//            rol.getInstrucciones().add("Para seleccionar el docente puede escribir parte del nombre o número de nómina y elegir al docente en la lista desplegable que aparecerá después de una pausa de un segundo.");
//            rol.getInstrucciones().add("Usted podrá consultar dos tablas de datos correspondientes a las materias por grupos (disponible al seleccionar un periodo, programa y grupo) y materias asignadas al docente (sólo disponible si seleccionó un docente en la parte superior).");
//            rol.getInstrucciones().add("En la tabla de datos MATERIAS DEL GRUPO usted tendrá 3 tipo de botones en la columna derecha para crear una nueva asignación, eliminar una asignación existente o cambiar el docente de una asignación al docente seleccionado en la parte superior.");
//            rol.getInstrucciones().add("Usted puede eliminar asignaciones sin elegir un docente en la parte superior.");
//            rol.getInstrucciones().add("En la tabla de datos MATERIAS ASIGNADAS AL DOCENTE usted podrá consultar las asignaciones académicas del docente seleccionado en la parte superior y eliminar asignaciones en caso de ser necesario.");
//            rol.getInstrucciones().add("Al eliminar una asignación desde la tabla de datos MATERIAS ASIGNADAS AL DOCENTE se cargarán las materias del grupo correspondientes en la tabla de datos MATERIAS DEL GRUPO para poder realizar la asignación de materia eliminada a otro docente.");
//
//            actualizarMaterias();
//        }catch (Exception e){mostrarExcepcion(e); }
//    }
//
//    @Override
//    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
//        String valor = "asignación académica";
//        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
////        map.entrySet().forEach(System.out::println);
//        return mostrar(request, map.containsValue(valor));
//    }
//
//    /**
//     * Método para proporcionar lista de docentes sugeridos en un autocomplete donde se puede ingresar el número de nómina, nombre o área del docente
//     * @param pista
//     * @return Lista de sugerencias
//     */
//    public List<PersonalActivo> completeDocentes(String pista){
//        ResultadoEJB<List<PersonalActivo>> res = ejb.buscarDocente(pista);
//        if(res.getCorrecto()){
//            return res.getValor();
//        }else{
//            mostrarMensajeResultadoEJB(res);
//            return Collections.emptyList();
//        }
//    }
//
//    /**
//     * Permite seleccionar un programa desde un evento de selección única, se recomienda actualizar al control de grupos ya que será sincronizado
//     * de forma inmediata y se seleccionará el primer grupo de esa lista para que también se actualicen los controles que visualicen al grupo
//     * @param e Evento de selección única, se recomienda un selectOneMenu
//     */
//    public void seleccionarPrograma(ValueChangeEvent e){
//        if(e.getNewValue() instanceof ProgramasEducativos){
//            rol.setPrograma((AreasUniversidad) e.getNewValue());
//            Ajax.update("grupo");//actualizaría al selector de grupos si lleva el id especificado
//        }
//    }
//
//    /**
//     * Permite invocar la asignaciòn de la materia, para que se lleve acabo, se debió haber seleccionado un docente, una materia, un periodo y un grupo
//     * @param materia
//     */
//    public void asignarMateria(DtoMateria materia){
//        rol.setMateria(materia);
//        ResultadoEJB<CargaAcademica> resAsignacion = ejb.asignarMateriaDocente(rol.getMateria().getMateria(), rol.getDocente(), rol.getGrupo(), rol.getPeriodo(), rol.getPrograma(), rol.getEventoActivo(), Operacion.PERSISTIR);
//        mostrarMensajeResultadoEJB(resAsignacion);
//    }
//
//    /**
//     * Permite invocar la eliminaciòn de una asignaciòn
//     * @param materia
//     */
//    public void eliminarAsignacion(DtoMateria materia){
//        if(materia.getDtoCargaAcademica() == null) mostrarMensaje("No se puede eliminar una asignación que no existe.");
//        rol.setMateria(materia);
//        ResultadoEJB<CargaAcademica> resAsignacion = ejb.asignarMateriaDocente(rol.getMateria().getMateria(), materia.getDtoCargaAcademica().getDocente(), rol.getGrupo(), rol.getPeriodo(), rol.getPrograma(), rol.getEventoActivo(), Operacion.ELIMINAR);
//        mostrarMensajeResultadoEJB(resAsignacion);
//    }
//
//    /**
//     * Permite la eliminación de una carga académica de un docente en específico
//     * @param carga DTO de la carga académica a eliminar
//     */
//    public void eliminarAsignacionPorDocente(DtoCargaAcademica carga){
//        ResultadoEJB<DtoMateria> res = ejb.packMateria(carga.getGrupo(), carga.getMateria(), carga.getPrograma());
//        if(!res.getCorrecto()){
//            mostrarMensajeResultadoEJB(res);
//            return;
//        }
//        rol.setPrograma(carga.getPrograma());
//        rol.setGrupo(carga.getGrupo());
//        eliminarAsignacion(res.getValor());
//    }
//
//    public void actualizarAsignacion(DtoMateria dtoMateria){
//        ResultadoEJB<CargaAcademica> res = ejb.actualizarDocenteEnAsignacion(dtoMateria, rol.getDocente());//ejb.asignarMateriaDocente(dtoMateria.getMateria(), rol.getDocente(), dtoMateria.getDtoCargaAcademica().getGrupo(), dtoMateria.getDtoCargaAcademica().getPeriodo(), rol.getEventoActivo(), Operacion.ACTUALIZAR);
//        mostrarMensajeResultadoEJB(res);
//    }
//
//    /**
//     * Permite que al cambiar o seleccionar un docente se puedan actualizar las materias asignadas a este docente
//     * @param e Evento del cambio de calor
//     */
//    public void  cambiarDocente(ValueChangeEvent e){
//        if(e.getNewValue() instanceof PersonalActivo){
//            PersonalActivo docente = (PersonalActivo)e.getNewValue();
//            final ResultadoEJB<List<DtoCargaAcademica>> res = ejb.getCargaAcademicaPorDocente(docente, rol.getPeriodo());
//            if(!res.getCorrecto()) {
//                mostrarMensajeResultadoEJB(res);
//                return;
//            }
//
//            rol.setCargas(res.getValor());
//        }else mostrarMensaje("El valor seleccionado como docente no es del tipo necesario.");
//    }
//
//    /**
//     * Permite actualizar las materias del del programa y grado seleccionado
//     */
//    public void actualizarMaterias(){
//        rol.setMateriasPorGrupo(Collections.EMPTY_LIST);
//        setAlertas(Collections.EMPTY_LIST);
//        if(rol.getPeriodo() == null) return;
//        if(rol.getPrograma() == null) return;
//        if(rol.getGrupo() == null) return;
//
//        ResultadoEJB<List<DtoMateria>> res = ejb.getMaterias(rol.getPrograma(), rol.getGrupo(), rol.getPeriodo(), rol.getPeriodoActivo());
//        if(res.getCorrecto()){
//            rol.setMateriasPorGrupo(res.getValor());
//            /*rol.getMateriasPorGrupo().forEach(dtoMateria -> {
//                System.out.println("dtoMateria.getDtoCargaAcademica() = " + dtoMateria.getDtoCargaAcademica());
//            });*/
//        }else mostrarMensajeResultadoEJB(res);
//
//        if(rol.getDocente() != null){
//            ResultadoEJB<List<DtoCargaAcademica>> resDocente = ejb.getCargaAcademicaPorDocente(rol.getDocente(), rol.getPeriodo());
//            if(resDocente.getCorrecto()) rol.setCargas(resDocente.getValor());
//            else mostrarMensajeResultadoEJB(resDocente);
//        }
//
//        ResultadoEJB<List<DtoAlerta>> resMensajes = ejb.identificarMensajes(rol);
////        System.out.println("resMensajes = " + resMensajes);
//        if(resMensajes.getCorrecto()){
//            setAlertas(resMensajes.getValor());
//        }else {
//            mostrarMensajeResultadoEJB(resMensajes);
//        }
//
//        repetirUltimoMensaje();
//    }
//
//    /**
//     * Permite decidir si se msotrará el botón de asignacion para una materia asignada o no
//     * @param dtoMateria DTO referencia de la materia a tratar
//     * @return Regresa TRUE si debe mostrarse, FALSE de lo contrario
//     */
//    public Boolean mostrarBotonAsignacion(DtoMateria dtoMateria){
//        if(dtoMateria == null) return false;
//        return rol.getDocente() != null //el docente no debe ser nulo
//                && dtoMateria.getDtoCargaAcademica() == null; //la materia no debe haber sido asignada aun
//    }
//
//    /**
//     * Permite decidir si se mostrará el botón de reasignación para una materia asignada o no
//     * @param dtoMateria DTO referenci de la materia a reasignar
//     * @return Regresa TRUE si debe mostrarse, FALSE de lo contrario
//     */
//    public Boolean mostrarBotonReasignacion(DtoMateria dtoMateria){
//        if(dtoMateria == null) return false;
//        return rol.getDocente() != null // el docente no debe ser nulo
//                && dtoMateria.getDtoCargaAcademica() != null  //la asignación debe existir
//                && !dtoMateria.getDtoCargaAcademica().getDocente().getPersonal().equals(rol.getDocente().getPersonal()); //se debió haber seleccionado un docente diferente a quien ya se le asignó la materia
//    }
//
//    public Boolean mostrarBotonHoras(DtoMateria dtoMateria){
//        return dtoMateria.getDtoCargaAcademica() != null;
//    }
//
//    /**
//     * Permite actualizar el valor de horas por semana
//     */
//    public void actualizarHorasPorSemana(){
//        /*if(e.getNewValue() instanceof Integer){
//            Integer horas = (Integer) e.getNewValue();
//        }else {
//            System.out.println("e.getNewValue().getClass() = " + e.getNewValue().getClass());
//        }*/
//        ResultadoEJB<Integer> res = ejb.actualizarHorasPorSemana(rol.getMateria());
////        System.out.println("res = " + res);
//        mostrarMensajeResultadoEJB(res);
//    }
//
//    public void seleccionarMateria(DtoMateria dtoMateria){
//        rol.setMateria(dtoMateria);
//    }
//}
