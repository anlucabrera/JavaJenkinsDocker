package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.controladores.ch.PersonalAdmin;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.*;
import mx.edu.utxj.pye.sgi.dto.dtoEstudianteMateria;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCapturaCalificaciones;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCapturaTareaIntegradora;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaCalificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReinscripcionAutonoma;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.Asentamiento;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.AssertTrue;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Named(value = "reinscripcionAutonomaEstudiante")
@ViewScoped
public class ReinscripcionAutonomaEstudiante extends ViewScopedRol implements Desarrollable {

    @EJB private EjbPropiedades ep;
    @EJB EjbReinscripcionAutonoma ejbReinscripcionAutonoma;
    @EJB EjbConsultaCalificacion ejbConsultaCalificacion;
    @EJB EjbCapturaCalificaciones ejbCapturaCalificaciones;
    @EJB EjbCapturaTareaIntegradora ejbCapturaTareaIntegradora;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;
    @Getter @Setter ReinscripcionAutonomaRolEstudiante rol = new ReinscripcionAutonomaRolEstudiante();
    @Getter @Setter String step;
    @Getter @Setter Boolean tab1,tab2,tab3,stepA =false;

    @Getter private Boolean cargado = false;

    @PostConstruct
    public void init(){
        try{
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19))return;
            cargado=true;
                setVistaControlador(ControlEscolarVistaControlador.REINSCRIPCION_AUTONOMA);

                ResultadoEJB<Estudiante> resAcceso = ejbReinscripcionAutonoma.validaEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
                //System.out.println(resAcceso.getValor());
                if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
                ResultadoEJB<Estudiante> resValidacion = ejbReinscripcionAutonoma.validaEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
                if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
                Estudiante estudiante = resValidacion.getValor();
                tieneAcceso = rol.tieneAcceso(estudiante,UsuarioTipo.ESTUDIANTE19);
                if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
                rol.setEstudiante(estudiante);
                ResultadoEJB<EventoEscolar> resEvento = ejbReinscripcionAutonoma.verificarEvento();
                if(!resEvento.getCorrecto())  tieneAcceso = false; //Debe negarle el acceso en caso de no haber un cuestionario activo
                else {rol.setEventoReinscripcion(resEvento.getValor()); }
                //----------------------------------------------------------------------------------------------------------------------------
                //if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
                if(!tieneAcceso){mostrarMensajeNoAcceso();return;}

               //Obtiene la lista de estados, municipios y asentamientos
                step ="0";
                 getGrupo();
                //Obtiene la lista de cargas acaademicas del grupo al que se encontraba inscrito
                getCargasAcademicas();
                rol.setAprobo(compruebaAprobo());
                if(rol.getAprobo()!=true){tieneAcceso=false;}
                comprubaReinscrito();
                rol.setDomicilio(rol.getEstudiante().getAspirante().getDomicilio());
                rol.setDatosContacto(rol.getEstudiante().getAspirante().getIdPersona().getMedioComunicacion());
                ResultadoEJB<List<Estado>> resEstados = ejbReinscripcionAutonoma.getEstados();
                if(resEstados.getCorrecto()==true){rol.setEstados(resEstados.getValor());}
                getMunicipioByEstado();
                getAsentamientoByMunicipioEstado();
                rol.setNivelRol(NivelRol.OPERATIVO);
                //TODO: Instrucciones
                rol.getInstrucciones().add("Ingrese los datos que desea actualizar. Los campos marcados con * son obligatorios.");
                rol.getInstrucciones().add("Verifique que los datos sean correctos");
                rol.getInstrucciones().add("Presione el botón 'Actualizar datos'.");
                rol.getInstrucciones().add("A continuación podrás visualizar las materias que cursarás en el próximo cuatrimestre, al igual que el docente que la impartirá.");
                rol.getInstrucciones().add("En caso de que la lista aparezca vacía, es debido a que aún no han asignado a los docentes que te impartirán clases.");
                rol.getInstrucciones().add("Presione el botón 'Reinscribirse'");
                rol.getInstrucciones().add("Una vez hecho los pasos anteriores, el sistema te confirmará tu inscripción en el nuevo periodo cuatrimestral.");
                rol.getInstrucciones().add("Verifica que tus datos sean los correctos, principalmente nombre y CURP, en caso de que detectes algún error, acude a Servicios Escolares para realizar la corrección.");




        }catch (Exception e){
            mostrarExcepcion( e);
        }
    }
    public void getCargasAcademicas(){
        try{
            ResultadoEJB<List<DtoCargaAcademica>> resCargas = ejbReinscripcionAutonoma.getCargaAdemicabyGrupo(rol.getGrupoAnterior());
            if(resCargas.getCorrecto()==true){rol.setCargasEstudiante(resCargas.getValor());}
            else {mostrarMensajeResultadoEJB(resCargas);}

        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    //Desactiva todos los tabs en caso de que el estudiante ya esté reinscrito
    public void bloqTabReins(){
        try{
            stepA =true;
            tab1 =true;
            tab2=true;
            tab3=true;
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    //Activa los tabs y los pasos, segun el paso en el que vaya el estudiante para su reinscripcion
    public void activaTab(){
        try{
            stepA=false;
            if(step.equals("0")){ tab1=false;tab2=true;tab3=true; }
            if(step.equals("1")){tab2=false;tab1=true;tab3=true;}
            if(step.equals("2")){tab3=false; tab1=true;tab2=true;}

        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    /**
     * Obtiene los municipios según el Estado
     */
    public void getMunicipioByEstado(){
        try{
            ResultadoEJB<List<Municipio>> resMunicipios= ejbReinscripcionAutonoma.getMunicipioByEstado(rol.getDomicilio());
            if(resMunicipios.getCorrecto()==true){  rol.setMunicipios(resMunicipios.getValor());}
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    /**
     * Obtiene los asentamientos por municipio
      */
    public void getAsentamientoByMunicipioEstado(){
        try {
            ResultadoEJB<List<Asentamiento>> resAsentamiento = ejbReinscripcionAutonoma.getAsentamientobyMunicipio(rol.getDomicilio());
            if(resAsentamiento.getCorrecto()==true){
                rol.setAsentamientos(resAsentamiento.getValor());
            }
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    /**
     * Comprueba si el estudiante ya se encuentra inscrito en el periodo programado del evento
     */
    public void comprubaReinscrito(){
        try{
            ResultadoEJB<Boolean> resReinscrito = ejbReinscripcionAutonoma.compruebaReinscrito(rol.getEstudiante(),rol.getEventoReinscripcion());
            if(resReinscrito.getCorrecto()==true){rol.setReinscrito(resReinscrito.getValor()); bloqTabReins();}
            else {rol.setReinscrito(resReinscrito.getValor());activaTab();}

        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    /**
     * Comprueba si el estudiante logueado aprobó sus materias
     */
    public boolean compruebaAprobo(){
        try{
            List<Boolean> listAproboMateria = new ArrayList<>();
            List<Boolean> materiasReprobadas = new ArrayList<>();
            Boolean aproboMateria;
            BigDecimal promFinalMateria ;
            Boolean aprobo =false;
           for (DtoCargaAcademica c: rol.getCargasEstudiante()){
                promFinalMateria=getPromedioFinal(c);
                //System.out.println("Promedio de la materia " +promFinalMateria);
               if(promFinalMateria.compareTo(BigDecimal.valueOf(8)) >= 0){
                  aproboMateria =true;
                   //System.out.println("Aprobo la materiaa " +aproboMateria);
                   listAproboMateria.add(aproboMateria);
               }else {
                   aproboMateria=false;
                   materiasReprobadas.add(aproboMateria);
               }
           }
           //System.out.println("Lista ---> "+listAproboMateria.size());
           // System.out.println("Lista filtrada por materias que reprobo---> "+materiasReprobadas.size());
           if(materiasReprobadas.size()>0){
               aprobo =false;
           }else {aprobo= true;}
           //System.out.println("Aprobo -->" +aprobo);
           return aprobo;
        }catch (Exception e){
            mostrarExcepcion(e);
            return false;

        }
    }

    /**
     * Busca el grupo al que se va a reinscribir el estudiante
     * - Primero busca el grupo imediato en caso de que no exista un grupo inmediato, busca uno grupo que coincida con su grado, plan educativo y el sistema
     */
    public void getGrupo(){
        try{
            //Da el valor del grupo en que se encuentra inscrito el estudiante
            rol.setGrupoAnterior(rol.getEstudiante().getGrupo());
            //Obtiene el nuevo grupo
            ResultadoEJB<Grupo> resGrupo = ejbReinscripcionAutonoma.getGrupoInmediatoSuperior(rol.getEstudiante(),rol.getEventoReinscripcion());
            if(resGrupo.getCorrecto()==true){
                rol.setNuevoGrupo(resGrupo.getValor());
            }
            //Si no hay un grupo inmediato, busca uno alterno
            else {
                //mostrarMensajeResultadoEJB(resGrupo);
                ResultadoEJB<Grupo> resGrupoAl = ejbReinscripcionAutonoma.getGrupoAlterno(rol.getEstudiante(),rol.getEventoReinscripcion());
                if(resGrupoAl.getCorrecto()==true){
                    rol.setNuevoGrupo(resGrupoAl.getValor());
                }
                else {mostrarMensajeResultadoEJB(resGrupoAl);}
            }
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    /**
     * Obtiene la carga de materias que tiene el plan educativo del grupo al que se va a reinscribir el estudiante
     */
    public void getNuevasMaterias(){
        try{
            ResultadoEJB<List<dtoEstudianteMateria>> resMaterias = ejbReinscripcionAutonoma.getNuevasMaterias(rol.getNuevoGrupo());
            if(resMaterias.getCorrecto()==true){
                rol.setNuevasMaterias(resMaterias.getValor());
            }else {mostrarMensajeResultadoEJB(resMaterias);}

        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    /**
     * Actualiza los datos del estudiante (Medio comunicacion - Domicilio)
     */
    public void actualizaDatos(){
        try{
            ResultadoEJB<Boolean> resActualiza= ejbReinscripcionAutonoma.updateDatosPersonales(rol.getDatosContacto(),rol.getDomicilio());
            if(resActualiza.getCorrecto()==true){
                mostrarMensajeResultadoEJB(resActualiza);
                step ="1";
                getNuevasMaterias();
                //System.out.println("Lista de materias" + rol.getNuevoGrupo());
                activaTab();

            }else {
                mostrarMensajeResultadoEJB(resActualiza);
            }

        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    /**
     * Inscribe al estudiante en el periodo programado con su nuevo grupo
     */
    public void reinscribirEstudiante(){
        try{
           ResultadoEJB<Estudiante> resRescripcion = ejbReinscripcionAutonoma.reinscribirEstudiante(rol.getEstudiante(),rol.getNuevoGrupo());
            if(resRescripcion.getCorrecto()==true){
                rol.setEstudiante(resRescripcion.getValor());
                step="2";
                activaTab();
                mostrarMensajeResultadoEJB(resRescripcion);
            }else {mostrarMensajeResultadoEJB(resRescripcion);}

           // step="2";
            //activaTab();
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    //---------------------------------------- Calificaciones
    public void obtenerUnidadesPorMateria() {
        List<DtoCalificacionEstudiante.MapUnidadesTematicas> resMap = new ArrayList<>();
        ResultadoEJB<List<DtoCalificacionEstudiante.UnidadesPorMateria>> resUnidadesPorMateria = ejbConsultaCalificacion.packUnidadesmateria(rol.getEstudiante());
        rol.setUnidadesPorMateria(resUnidadesPorMateria.getValor());
        rol.getUnidadesPorMateria().forEach(x -> {
            x.getUnidadMateriaConfiguracion().forEach(y -> {
                resMap.add(new DtoCalificacionEstudiante.MapUnidadesTematicas(y.getIdUnidadMateria().getNoUnidad(), y.getIdUnidadMateria().getNoUnidad()));
            });
        });
        rol.setMapUnidadesTematicas(new ArrayList<>(new HashSet<>(resMap)));
        rol.getMapUnidadesTematicas().sort(Comparator.comparingInt(DtoCalificacionEstudiante.MapUnidadesTematicas::getNoUnidad));
    }
    public List<DtoUnidadConfiguracion> getUnidades(@NonNull DtoCargaAcademica dtoCargaAcademica){
        if(rol.getDtoUnidadConfiguracionesMap().containsKey(dtoCargaAcademica)) return  rol.getDtoUnidadConfiguracionesMap().get(dtoCargaAcademica);

        ResultadoEJB<List<DtoUnidadConfiguracion>> resConfiguraciones = ejbCapturaCalificaciones.getConfiguraciones(dtoCargaAcademica);
        if(!resConfiguraciones.getCorrecto()){
            mostrarMensaje("No se detectaron configuraciones de unidades en la materia de la carga académica seleccionada. " + resConfiguraciones.getMensaje());
            return Collections.EMPTY_LIST;
        }
        rol.getDtoUnidadConfiguracionesMap().put(dtoCargaAcademica, resConfiguraciones.getValor());

        return  rol.getDtoUnidadConfiguracionesMap().get(dtoCargaAcademica);
    }
    public DtoUnidadesCalificacionEstudiante getContenedor(@NonNull DtoCargaAcademica dtoCargaAcademica){
        if(rol.getDtoUnidadesCalificacionMap().containsKey(dtoCargaAcademica)) return rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica);
        ResultadoEJB<DtoUnidadesCalificacionEstudiante> resDtoUnidadesCalificacion = ejbConsultaCalificacion.packDtoUnidadesCalificacion(rol.getEstudiante(), getUnidades(dtoCargaAcademica));
        if(!resDtoUnidadesCalificacion.getCorrecto()){
            mostrarMensaje("No se detectaron registros de calificaciones de la carga seleccionada. " + resDtoUnidadesCalificacion.getMensaje());
            return null;
        }

        rol.getDtoUnidadesCalificacionMap().put(dtoCargaAcademica, resDtoUnidadesCalificacion.getValor());
        return rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica);
    }
    public  Boolean tieneIntegradora(@NonNull DtoCargaAcademica dtoCargaAcademica){
        if(rol.getTieneIntegradoraMap().containsKey(dtoCargaAcademica)) return rol.getTieneIntegradoraMap().get(dtoCargaAcademica);

        ResultadoEJB<TareaIntegradora> tareaIntegradoraResultadoEJB = ejbCapturaTareaIntegradora.verificarTareaIntegradora(dtoCargaAcademica);
        if(tareaIntegradoraResultadoEJB.getCorrecto()) {
            rol.getTieneIntegradoraMap().put(dtoCargaAcademica, true);
            rol.getTareaIntegradoraMap().put(dtoCargaAcademica, tareaIntegradoraResultadoEJB.getValor());
            rol.setTieneIntegradora(Boolean.TRUE);

            ResultadoEJB<Map<DtoCargaAcademica, TareaIntegradoraPromedio>> generarContenedorCalificaciones = ejbConsultaCalificacion.generarContenedorCalificaciones(getContenedor(dtoCargaAcademica).getDtoCargaAcademicas(), rol.getEstudiante(), tareaIntegradoraResultadoEJB.getValor());
            if(!generarContenedorCalificaciones.getCorrecto()){
                mostrarMensajeResultadoEJB(generarContenedorCalificaciones);
            }
            rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica).setTareaIntegradoraPromedioMap(generarContenedorCalificaciones.getValor());
        }
        else{
            rol.getTieneIntegradoraMap().put(dtoCargaAcademica, false);
        }

        return rol.getTieneIntegradoraMap().get(dtoCargaAcademica);
    }
    public DtoCalificacionNivelacion getNivelacion(@NonNull DtoCargaAcademica dtoCargaAcademica){
        DtoUnidadesCalificacionEstudiante.DtoNivelacionPK pk = new DtoUnidadesCalificacionEstudiante.DtoNivelacionPK(dtoCargaAcademica, rol.getEstudiante());
        if(getContenedor(dtoCargaAcademica).getNivelacionMap().containsKey(pk)) return getContenedor(dtoCargaAcademica).getNivelacionMap().get(pk);
        ResultadoEJB<DtoCalificacionNivelacion> packDtoCalificacionNivelacion = ejbConsultaCalificacion.packDtoCalificacionNivelacion(dtoCargaAcademica, rol.getEstudiante());
        if(packDtoCalificacionNivelacion.getCorrecto()){
            @NonNull DtoCalificacionNivelacion dtoCalificacionNivelacion = packDtoCalificacionNivelacion.getValor();
            getContenedor(dtoCargaAcademica).getNivelacionMap().put(pk, dtoCalificacionNivelacion);
            return getContenedor(dtoCargaAcademica).getNivelacionMap().get(pk);
        }else {
            mostrarMensajeResultadoEJB(packDtoCalificacionNivelacion);
            return null;
        }
    }
    public BigDecimal getPromedioAsignaturaEstudiante(@NonNull DtoCargaAcademica dtoCargaAcademica){
        ResultadoEJB<BigDecimal> res = ejbConsultaCalificacion.promediarAsignatura(getContenedor(dtoCargaAcademica), dtoCargaAcademica, rol.getEstudiante());
        if(res.getCorrecto()){
            return res.getValor();
        }else{
            mostrarMensaje(
                    String.format("El promedio del estudiante %s %s %s con matrícula %s, no se pudo calcular.", rol.getEstudiante().getAspirante().getIdPersona().getApellidoPaterno(),
                            rol.getEstudiante().getAspirante().getIdPersona().getApellidoMaterno(),
                            rol.getEstudiante().getAspirante().getIdPersona().getNombre(), rol.getEstudiante().getMatricula()));
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getPromedioFinal(@NonNull DtoCargaAcademica dtoCargaAcademica){
        BigDecimal promedioOrdinario = getPromedioAsignaturaEstudiante(dtoCargaAcademica);
        BigDecimal nivelacion = new BigDecimal(getNivelacion(dtoCargaAcademica).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal promedioFinal = BigDecimal.ZERO;
        if(nivelacion.compareTo(BigDecimal.valueOf(0)) == 0){
            promedioFinal = promedioFinal.add(promedioOrdinario);
        }else {
            promedioFinal = promedioFinal.add(nivelacion);
        }
        return promedioFinal;
    }



    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "reinscripcionAutonoma";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
}
