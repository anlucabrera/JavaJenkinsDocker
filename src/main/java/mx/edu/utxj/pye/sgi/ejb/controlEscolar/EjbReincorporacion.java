package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CalificacionesSaiiutDto;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.QrNssDto;
import static mx.edu.utxj.pye.sgi.ejb.controlEscolar.ServicioFichaAdmision.ucFirst;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.CalificacionesAlumno;
import mx.edu.utxj.pye.sgi.saiiut.entity.PlanesEstudio;
import mx.edu.utxj.pye.sgi.saiiut.entity.Materias1;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;
import nl.lcs.qrscan.core.QrPdf;

@Stateless(name = "EjbReincorporacion")
public class EjbReincorporacion {
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbEstudianteBean ejbEstudianteBean;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    @EJB Facade2 f2;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    Integer claveMateria;
    
     /**
     * Permite validar si el usuario autenticado es personal adscrito al departamento de servicios escolares
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarServiciosEscolares(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalAreaOperativa").orElse(10)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbReincorporacion.validarServiciosEscolares)", e, null);
        }
    }
   
    /**
     * Permite verificar si hay un periodo abierto para reincoporaciones
     * @return Evento escolar detectado o null de lo contrario
     */
    public ResultadoEJB<EventoEscolar> verificarEvento(){
        try{
            return ejbEventoEscolar.verificarEventoAperturado(EventoEscolarTipo.REINCORPORACIONES);
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para reinscripción autónoma (EjbReinscripcionAutonoma.).", e, EventoEscolar.class);
        }
    }
    
     /**
     * Permite identificar si existen datos personales del estudiante que se reincorporará
     * @param curp CURP del estudiante que se va a reincorporar
     * @return Resultado del proceso
     */
    public ResultadoEJB<Persona> buscarDatosPersonalesEstudiante(String curp){
        try{
            //buscar registro de persona por medio de la curp
            Persona persona = f.getEntityManager().createQuery("select p from Persona p where p.curp =:curp", Persona.class)
                    .setParameter("curp", curp)
                    .getSingleResult();
            return ResultadoEJB.crearCorrecto(persona, "Datos personales del estudiante");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se encontraron datos personales del estudiante. (EjbReincorporacion.buscarDatosPersonalesEstudiante)", e, null);
        }
    }
    
     /**
     * Permite leer el código Qr de la CURP para la obtención de datos personales del estudiantes como: nombre completo, curp, genero, fecha y estado de nacimiento 
     * @param file Documento que se va a leer
     * @return Resultado del proceso
     */
    public ResultadoEJB<Persona> leerQrCURP(Part file){
        Persona p = new Persona();
        SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");
        String rutaRelativa = "";
        try{
            QrPdf pdf = new QrPdf(Paths.get(file.getSubmittedFileName()));
            String qrCode = pdf.getQRCode(1, true, true);
            String fecha_nacimiento = "";
            String[] parts = qrCode.split("\\|");
          
            if ((parts != null && (parts.length == 8 || parts.length == 9))) {
                if (parts.length == 9) {
                    p.setCurp(parts[0]);
                    p.setApellidoPaterno(ucFirst(parts[2]).trim());
                    p.setApellidoMaterno(ucFirst(parts[3]).trim());
                    p.setNombre(ucFirst(parts[4]));
                    if (parts[5].equals("HOMBRE")) {
                        p.setGenero((short) 2);
                    }
                    if (parts[5].equals("MUJER")) {
                        p.setGenero((short) 1);
                    }
                    fecha_nacimiento = parts[6].replace("/", "-");
                    p.setFechaNacimiento(sm.parse(fecha_nacimiento));
                    String claveEstado = parts[0].substring(11, 13);

                    Estado estado = f.getEntityManager().createNamedQuery("Estado.findByClave", Estado.class)
                            .setParameter("clave", claveEstado)
                            .getResultList()
                            .stream().findFirst().orElse(null);

                    p.setEstado(estado.getIdestado());
                    p.setUrlCurp(rutaRelativa);
                } else if (parts.length == 8) {
                    p.setCurp(parts[0]);
                    p.setApellidoPaterno(ucFirst(parts[2]));
                    p.setApellidoMaterno(ucFirst(parts[3]));
                    p.setNombre(ucFirst(parts[4]));
                    p.setGenero((short) 1);
                    if (parts[5].equals("HOMBRE")) {
                        p.setGenero((short) 2);
                    }
                    if (parts[5].equals("MUJER")) {
                        p.setGenero((short) 1);
                    }
                    fecha_nacimiento = parts[6].replace("/", "-");
                    p.setFechaNacimiento(sm.parse(fecha_nacimiento));
                    p.setUrlCurp(rutaRelativa);
                } else {
                    p = new Persona();
                }
            }
            
            return ResultadoEJB.crearCorrecto(p, "Datos personales del estudiante");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo el código qr de la curp. (EjbReinscripcionAutonoma.leerQrCURP)", e, null);
        }
    }
   
     /**
     * Permite el registro de los datos personales del estudiante
     * @param persona Datos personales que se registrarán
     * @param operacion Operación a realizar, se permite persistir y actualizar
     * @return Resultado del proceso
     */
    public ResultadoEJB<Persona> guardarDatosPersonales(Persona persona, Operacion operacion){
        try{
            if(persona == null) return ResultadoEJB.crearErroneo(2, "La persona no debe ser nula.", Persona.class);
            if(operacion == null) return ResultadoEJB.crearErroneo(3, "La operación no debe ser nula.", Persona.class);

            Persona datosPer = f.getEntityManager().createQuery("select p from Persona p where p.curp =:curp", Persona.class)
                    .setParameter("curp", persona.getCurp())
                    .getSingleResult();

            switch (operacion){
                case PERSISTIR:
                    //registrar datos personales
                    if(datosPer == null){//comprobar si la asignación existe para impedir  duplicar
                        datosPer = new Persona();
                        f.create(datosPer);
                        return ResultadoEJB.crearCorrecto(datosPer, "Los datos personales se han registrado correctamente.");
                    }else {//si ya existe se informa
                        return ResultadoEJB.crearErroneo(4, "Los datos personales ya existen", Persona.class);
                    }
                case ACTUALIZAR:
                    //actualizar datos personales
                        f.edit(datosPer);
                        return ResultadoEJB.crearCorrecto(null, "Los datos personales se han actualizado correctamente.");
                   
                    default:
                        return ResultadoEJB.crearErroneo(5, "Operación no autorizada.", Persona.class);
           
        }
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar los datos personales. (EjbReincorporacion.guardarDatosPersonales)", e, null);
        }
    }
    
    /**
     * Permite obtener el periodo escolar disponible para realizar el proceso de reincorporación
     * @param eventoEscolar Evento Escolar que se encuentra activo
     * @return Resultado del proceso
     */
        public ResultadoEJB<PeriodosEscolares> getPeriodoReincorporacion(EventoEscolar eventoEscolar){
        try{
            Integer periodoEvento = eventoEscolar.getPeriodo();
            //buscar periodo escolar para realizar reincorporación
            final PeriodosEscolares periodo = f.getEntityManager().createQuery("select p from PeriodosEscolares p where p.periodo =:periodo", PeriodosEscolares.class)
                    .setParameter("periodo", periodoEvento)
                    .getSingleResult();
            return ResultadoEJB.crearCorrecto(periodo, "Periodo para reinscripción");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el periodo para reinscripción. (EjbReinscripcionAutonoma.getPeriodoActivo)", e, null);
        }
    }

     /**
     * Permite obtener la lista de programas educativos que tienen planes de estudio vigentes, los programas deben ordenarse por
     * área, nivel y nombre y los grupos por grado y letra
     * @param periodo Periodo activo
     * @return Resultado del proceso
     */
    public ResultadoEJB<Map<AreasUniversidad, List<Grupo>>> getProgramasActivos(PeriodosEscolares periodo){
        try{
            // buscar lista de programas educativos con plan de estudios vigentes y despues mapear cada programa con su lista de grupos
            Integer programaEducativoCategoria = ep.leerPropiedadEntera("programaEducativoCategoria").orElse(9);
            List<AreasUniversidad> programas = f.getEntityManager().createQuery("select a from AreasUniversidad  a where a.areaSuperior=:areaPoa and a.categoria.categoria=:categoria and a.vigente = '1' order by a.nombre", AreasUniversidad.class)
                    .setParameter("categoria", programaEducativoCategoria)
                    .getResultList();
            Map<AreasUniversidad, List<Grupo>> programasMap = programas.stream()
                    .collect(Collectors.toMap(programa -> programa, programa -> buscarGrupos(programa, periodo)));
//            System.out.println("programasMap = " + programasMap);
            return ResultadoEJB.crearCorrecto(programasMap, "Mapa de programas y grupos");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo mapear los programas y sus grupos. (EjbReinscripcionAutonoma.getProgramasActivos)", e, null);
        }
    }
     private List<Grupo> buscarGrupos(AreasUniversidad programa, PeriodosEscolares periodo) {
        return f.getEntityManager().createQuery("select g from Grupo g where g.idPe=:programa and g.periodo=:periodo", Grupo.class)
                .setParameter("programa", programa.getArea())
                .setParameter("periodo", periodo.getPeriodo())
                .getResultList();
    }
    
    /**
     * Permite la asignación de grupo al estudiante
     * @param grupo Grupo que se asignará al estudiante
     * @param estudiante Estudiante al que se le asignará el grupo correspondiente
     * @return Resultado del proceso
     */
    public ResultadoEJB<Inscripcion> asignarGrupo(Grupo grupo, Inscripcion estudiante){
        /*try{
            if(grupo == null) return ResultadoEJB.crearErroneo(2, "El grupo no debe ser nulo.", Estudiante.class);
            if(estudiante == null) return ResultadoEJB.crearErroneo(3, "El estudiante no debe ser nulo.", Estudiante.class);

            TypedQuery<Estudiante> q1 = f.getEntityManager().createQuery("update Estudiante e SET e.grupo =:grupo AND e.periodo =:periodo WHERE e.idEstudiante =:estudiante", Estudiante.class);
            q1.setParameter("grupo", grupo.getIdGrupo());
            q1.setParameter("periodo", grupo.getPeriodo());
            q1.setParameter("estudiante", estudiante.getIdEstudiante());
            q1.executeUpdate();

            Estudiante asignaGrupo = f.getEntityManager().find(Estudiante.class, estudiante.getIdEstudiante());
            return ResultadoEJB.crearCorrecto(asignaGrupo, "Grupo asignado");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo asignar el grupo al estudiante. (EjbReinscripcionAutonoma.asignarGrupo)", e, null);
        }*/
        return null;
    }

    /**
     * Permite obtener la lista de materias por asignar al estudiante recien inscrito
     * @param programa Programa al que deben pertenecer las materias
     * @param grupo Grupo al que deben pertenecer las materias
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Materia>> getMateriasPorAsignar(AreasUniversidad programa, Grupo grupo){
        /*try{
            //TODO: buscar lista de materias por asignar que pertenecen al grupo seleccionado
            List<Materia> materiasPorAsignar = f.getEntityManager().createQuery("select m from Materia m inner join m.idPlan p where p.idPe=:programaEducativo and m.grado =:grado and m.estatus =:estatus", Materia.class)
                    .setParameter("programaEducativo", programa.getArea())
                    .setParameter("grado", grupo.getGrado())
                    .setParameter("estatus", true)
                    .getResultList();
            return ResultadoEJB.crearCorrecto(materiasPorAsignar, "Lista de materias para asignar al estudiante");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de materias por asignar. (EjbReinscripcionAutonoma.getMateriasPorAsignar)", e, null);
        }*/
        return null;
    }
   
    /**
     * Permite la asignación de materias que cursará el estudiante
     * @param estudiante Estudiante al que se le asignarán las materias
     * @param materias Lista de materias que se asignarán
     * @return Resultado del proceso
     */
    public ResultadoEJB<Calificacion> asignarMateriasEstudiante(Inscripcion estudiante, List<Materia> materias){
        /*try{
            if(estudiante == null) return ResultadoEJB.crearErroneo(2, "El estudiante no puede ser nulo.", Calificaciones.class);
            if(materias.isEmpty()) return ResultadoEJB.crearErroneo(3, "La lista de materias no puede ser vacia.", Calificaciones.class);
            
            //Obtener la clave de la materia
            materias.forEach(mat -> {claveMateria = mat.getIdMateria();});
            Materia materia = f.getEntityManager().find(Materia.class, claveMateria);
            
            CalificacionesPK pk = new CalificacionesPK(estudiante.getIdEstudiante(), estudiante.getGrupo().getIdGrupo(), claveMateria);
            Calificaciones calificaciones = f.getEntityManager().createQuery("select c from Calificaciones c where c.estudiante1.idEstudiante =:estudiante and c.estudiante1.grupo =:grupo and c.materia1.idMateria =:materia", Calificaciones.class)
                    .setParameter("estudiante", estudiante.getIdEstudiante())
                    .setParameter("grupo", estudiante.getGrupo().getIdGrupo())
                    .setParameter("materia", claveMateria)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (calificaciones == null) {//comprobar si la asignación existe para impedir  duplicar
                calificaciones = new Calificaciones();
                calificaciones.setCalificacionesPK(pk);
                calificaciones.setEstudiante1(estudiante);
                calificaciones.setMateria1(materia);
                f.create(calificaciones);
                return ResultadoEJB.crearCorrecto(calificaciones, "La asignación fue registrada correctamente.");
            }
            return ResultadoEJB.crearErroneo(2, "La asignación ya fue realizada. (EjbReinscripcionAutonoma.asignarMateriasEstudiante)", Calificaciones.class);
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo asignar las materias. (EjbReinscripcionAutonoma.asignarMateriasEstudiante)", e, null);
        }*/

        return null;
    }
    
     /**
     * Permite la lectura del folio de IMSS mediante el código QR
     * @param file Documento que se va a leer
     * @return Resultado del proceso
     */
    public ResultadoEJB<QrNssDto> leerQrNSS(Part file) {
        try{
            QrPdf pdf = new QrPdf(Paths.get(file.getSubmittedFileName()));
            String qrCode = pdf.getQRCode(1, true, true);
            String[] parts = qrCode.split(":|");
            
            String nss = parts[3].replace(" ", "");
            String curp = parts[7].replace(" ", "");
            
            Persona persona = f.getEntityManager().createQuery("select p from Persona p where p.curp =:curp", Persona.class)
                    .setParameter("curp", curp)
                    .getSingleResult();
            
            //comprobar si la curp esta relacionada con un estudiante
            if(persona != null){ 
                QrNssDto qrNssDto = new QrNssDto(persona, nss);
                return ResultadoEJB.crearCorrecto(qrNssDto, "La asignación fue registrada correctamente.");
            }
            
            return ResultadoEJB.crearErroneo(2, "El nss no puede ser nulo.", QrNssDto.class);
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo leer el código qr del nss. (EjbReinscripcionAutonoma.leerQrNSS)", e, null);
        }
    }
    
     /**
     * Permite la asignación del número de seguridad social al estudiante
     * @param datosQr Datos leídos del qr (persona y nss)
     * @return Resultado del proceso
     */
    public ResultadoEJB<DatosMedicos> asignarNSS(QrNssDto datosQr){
        try{
            if(datosQr == null) return ResultadoEJB.crearErroneo(3, "El nss no puede ser nulo.", DatosMedicos.class);
            
            DatosMedicos datosMedicos = f.getEntityManager().createQuery("select d from DatosMedicos d WHERE d.persona.idpersona =:persona and d.nss =:nss", DatosMedicos.class)
                    .setParameter("persona",  datosQr.getPersona().getIdpersona())
                    .setParameter("nss", datosQr.getNss())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (datosMedicos == null) {//comprobar si la asignación existe para impedir  duplicar
                datosMedicos = new DatosMedicos();
                datosMedicos.setCvePersona(datosQr.getPersona().getIdpersona());
                datosMedicos.setNss(datosQr.getNss());
                f.create(datosMedicos);
                return ResultadoEJB.crearCorrecto(datosMedicos, "La asignación fue registrada correctamente.");
            }
            return ResultadoEJB.crearErroneo(2, "La asignación ya fue realizada. (EjbReinscripcionAutonoma.asignarNSS)", DatosMedicos.class);
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo asignar el nss. (EjbReinscripcionAutonoma.asignarNSS)", e, null);
        }
    }
    
    /** PARA REINCORPORACIONES DE GENERACIONES ANTERIORES QUE SE ENCUENTRAN EN SAIIUT **/
    
     /**
     * Permite verificar si el estudiante tiene calificaciones de anteriores cuatrimestres registradas en SAIIUT
     * @param matricula Matricula del estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<CalificacionesSaiiutDto>> verificarCalificacionesSAIIUT(String matricula){
        /*try{
            List<CalificacionesSaiiutDto> listaCalificacionesSaiiutDto = new ArrayList<>();
            List<CalificacionesAlumno> listaCalificacionesAlumno = new ArrayList<>();
            
            //Identificar claves del alumno para posteriormente buscar las calificaciones
            List<Alumnos> clavesAlumno = f2.getEntityManager().createQuery("SELECT a FROM Alumnos a WHERE a.matricula =:matricula", Alumnos.class)
                    .setParameter("matricula", matricula)
                    .getResultList();

            //Buscar lista de calificaciones en saiiut del estudiante
            listaCalificacionesAlumno = f2.getEntityManager().createQuery("SELECT ca FROM CalificacionesAlumno ca WHERE ca.calificacionesAlumnoPK.cveAlumno IN :claves ", CalificacionesAlumno.class)
                    .setParameter("claves", clavesAlumno)
                    .setParameter("valida", true)
                    .getResultList();

            //Construir la lista de dto's para mostrar las calificaciones
            listaCalificacionesAlumno.forEach(calificacionesAlumno -> {
                
                Alumnos alumno = f2.getEntityManager().createQuery("SELECT a FROM Alumnos a WHERE a.alumnosPK.cveAlumno =:cveAlumno", Alumnos.class)
                        .setParameter("cveAlumno", calificacionesAlumno.getCalificacionesAlumnoPK().getCveAlumno())
                        .getSingleResult();
                        
                PlanesEstudio planEstudio = f2.getEntityManager().createQuery("SELECT p FROM PlanesEstudio p WHERE p.planesEstudioPK.cvePlan =:cvePlan", PlanesEstudio.class)
                        .setParameter("cvePlan", calificacionesAlumno.getCalificacionesAlumnoPK().getCvePlan())
                        .getSingleResult();
                
                Materias1 materia = f2.getEntityManager().createQuery("SELECT m FROM Materias1 m WHERE m.cveMateria =:cveMateria", Materias1.class)
                        .setParameter("cveMateria", calificacionesAlumno.getCalificacionesAlumnoPK().getCveMateria())
                        .getSingleResult();
                
                listaCalificacionesSaiiutDto.add(new CalificacionesSaiiutDto(
                        calificacionesAlumno,
                        alumno,
                        planEstudio,
                        materia));
            });
        
            return ResultadoEJB.crearCorrecto(listaCalificacionesSaiiutDto, "Existen registros de calificaciones de cuatrimestres anteriores.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No existen calificaciones de cuatrimestres anteriores registradas. (EjbReincorporacion.verificarCalificacionesSAIIUT)", e, null);
        }*/

        return null;
    }
    
     /**
     * Permite la asignación de calificaciones de SAIIUT al sistema de Control Escolar
     * @param listaCalificacionesSaiiutDto calificaciones de saiiut
     * @param estudiante Estudiante al que se le asignarán las calificaciones
     * @return Resultado del proceso
     */
    public ResultadoEJB<Calificacion> asignarCalificacionesSAIIUT(List<CalificacionesSaiiutDto> listaCalificacionesSaiiutDto, Inscripcion estudiante){
        /*try{
            if(listaCalificacionesSaiiutDto.isEmpty()) return ResultadoEJB.crearErroneo(3, "La calificación no puede ser nulo.", Calificaciones.class);
            if(estudiante == null) return ResultadoEJB.crearErroneo(4, "El estudiante no puede ser nulo.", Calificaciones.class);
           
            Calificaciones calificacionesAsignar = new Calificaciones();
            
            listaCalificacionesSaiiutDto.forEach((calificacionesAlumno) -> {
            
            Calificaciones calificaciones = f.getEntityManager().createQuery("select c from Calificaciones c INNER JOIN c.estudiante1 e INNER JOIN c.materia1 m WHERE e.matricula =:matricula AND m.claveMateria =:materia", Calificaciones.class)
                    .setParameter("matricula", calificacionesAlumno.getAlumno().getMatricula())
                    .setParameter("materia", calificacionesAlumno.getCalificacionesAlumno().getCalificacionesAlumnoPK().getCveMateria())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (calificaciones == null) {//comprobar si la asignación existe para impedir  duplicar
                
                Materia mat = f.getEntityManager().createQuery("SELECT m FROM Materia m WHERE m.claveMateria =:claveMateria", Materia.class)
                .setParameter("claveMateria", calificacionesAlumno.getMateria1().getCveMateria())
                .getSingleResult();
                                 
                CalificacionesPK pk = new CalificacionesPK(estudiante.getIdEstudiante(), estudiante.getGrupo().getIdGrupo(), mat.getIdMateria());

                calificacionesAsignar.setCalificacionesPK(pk);
                calificacionesAsignar.setEstudiante1(estudiante);
                calificacionesAsignar.setMateria1(mat);
                calificacionesAsignar.setCf(calificacionesAlumno.getCalificacionesAlumno().getCf().doubleValue());
                f.create(calificacionesAsignar);
                }
            });
            return ResultadoEJB.crearCorrecto(calificacionesAsignar, "La calificación ya fue asignada. (EjbReincorporacion.asignarCalificacionesSAIIUT)");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo asignar la calificación. (EjbReincorporacion.asignarCalificacionesSAIIUT)", e, null);
        }*/
        return null;
    }
    
    /** PARA REINCORPORACIONES DE OTRA UT **/
    
     /**
     * Permite buscar lista de materias previas que se asignarán al estudiante que no tiene historial académico en sistema
     * @param planEstudio Materias del plan de estudio que se asignarán
     * @param estudiante Estudiante al que se le asignarán las materias
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Materia>> getMateriasPorAsignar(PlanEstudio planEstudio, Inscripcion estudiante){
        /*try{
            //TODO: buscar lista de materias activas previas al grado actual para asignar que pertenecen al plan de estudios seleccionado del estudiante
            List<Materia> materiasPorAsignar = f.getEntityManager().createQuery("SELECT m FROM Materia m WHERE m.idPlan.idPlanEstudio =:planEstudio AND m.grado < :grado AND m.estatus =:estatus", Materia.class)
                    .setParameter("planEstudio", planEstudio.getIdPlanEstudio())
                    .setParameter("grados", estudiante.getGrupo().getGrado())
                    .setParameter("estatus", true)
                    .getResultList();
            return ResultadoEJB.crearCorrecto(materiasPorAsignar, "Lista de materias sin asignar por grupo y programa");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de materias sin asignar por grupo y programa. (EjbAsignacionAcademica.getMateriasPorAsignar)", e, null);
        }*/
        return null;
    }
    
     /**
     * Permite la asignación de calificaciones directas de cuatrimestres previos al estudiante que no tiene historial académico en sistema
     * @param materia Clave de la materia a la que se le asignará calificación
     * @param calificacion Calificación
     * @param estudiante Estudiante al que se le asignarán las calificaciones
     * @return Resultado del proceso
     */
    public ResultadoEJB<Calificacion> asignarCalificacionesDirectas(Materia materia, Double calificacion, Inscripcion estudiante){
        /*try{
            if(materia == null) return ResultadoEJB.crearErroneo(3, "La materia no puede ser nula.", Calificaciones.class);
            if(calificacion == null) return ResultadoEJB.crearErroneo(4, "La calificación no puede ser nula.", Calificaciones.class);
            if(estudiante == null) return ResultadoEJB.crearErroneo(5, "El estudiante no puede ser nulo.", Calificaciones.class);
           
            Calificaciones calificaciones = f.getEntityManager().createQuery("SELECT c FROM Calificaciones c WHERE c.calificacionesPK.estudiante =:estudiante AND c.calificacionesPK.materia =:materia AND c.cf !=:null", Calificaciones.class)
                    .setParameter("estudiante",  estudiante.getIdEstudiante())
                    .setParameter("materia", materia.getIdMateria())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (calificaciones == null) {//comprobar si la asignación existe para impedir  duplicar
                
                calificaciones = new Calificaciones();
                CalificacionesPK pk = new CalificacionesPK(estudiante.getIdEstudiante(), estudiante.getGrupo().getIdGrupo(), materia.getIdMateria());
               
                calificaciones.setCalificacionesPK(pk);
                calificaciones.setEstudiante1(estudiante);
                calificaciones.setMateria1(materia);
                calificaciones.setCf(calificacion);
                f.create(calificaciones);
                return ResultadoEJB.crearCorrecto(calificaciones, "La asignación fue registrada correctamente.");
            }
            return ResultadoEJB.crearErroneo(2, "La asignación ya fue realizada. (EjbReinscripcionAutonoma.asignarCalificacionesDirectas)", Calificaciones.class);
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo asignar la calificación. (EjbReincorporacion.asignarCalificacionesDirectas)", e, null);
        }*/
        return null;
    }
    
     /** PARA REINCORPORACIONES EN LAS CUALES EXISTE HISTORIAL ACADEMICO PREVIO **/
     
     /**
     * Permite buscar calificaciones previas del estudiante en el sistema
     * @param estudiante Estudiante del que se buscarán calificaciones
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Calificacion>> getCalificacionesPrevias(Inscripcion estudiante){
        /*try{
            //Identificar claves del alumno para posteriormente buscar las calificaciones
            List<Estudiante> clavesAlumno = f.getEntityManager().createQuery("SELECT e FROM Estudiante e WHERE e.matricula =:matricula AND e.matricula !=:claveActual", Estudiante.class)
                    .setParameter("matricula", estudiante.getMatricula())
                    .setParameter("claveActual", estudiante.getIdEstudiante())
                    .getResultList();
            
            //TODO: buscar calificaciones previas al grado actual para asignar que pertenecen al estudiante
            List<Calificaciones> calificaciones = f.getEntityManager().createQuery("SELECT c FROM Calificaciones c WHERE c.calificacionesPK.estudiante IN :clavesAlumno ORDER BY c.materia1.claveMateria ASC", Calificaciones.class)
                    .setParameter("clavesAlumno", clavesAlumno)
                    .getResultList();
             
            return ResultadoEJB.crearCorrecto(calificaciones, "Lista de materias sin asignar por grupo y programa");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de materias sin asignar por grupo y programa. (EjbAsignacionAcademica.getMateriasPorAsignar)", e, null);
        }*/

        return null;
    }
}