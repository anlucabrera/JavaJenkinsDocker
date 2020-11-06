package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.controlador.Evaluacion;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCuestionarioPsicopedagogicoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoGrupo;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CuestionarioPsicopedagogicoResultados;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless (name = "EjbSeguimientoCuestionarioPsicopedagico")
public
class EjbSeguimientoCuestionarioPsicopedagico {
    @EJB Facade f;
    @EJB EjbPersonalBean ejbPersonalBean;
    private EntityManager em;
    @EJB EjbPropiedades ep;
    @EJB EjbValidacionRol ejbValidacionRol;
    @EJB   EjbPeriodoEventoRegistro   ejbPeriodoEventoRegistro;

    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }

    /**
     * Valida que el personal aurtenticado sea Director/ Psicopedagogia/ Tutores
     * @param clave
     * @return
     */
    public  ResultadoEJB<Filter<PersonalActivo>> validarAcceso(@NonNull  Integer clave){
        try {
            ResultadoEJB<Filter<PersonalActivo>> resPsicope = validarPsicopedagogia(clave);
            ResultadoEJB<Filter<PersonalActivo>> resDirector = validarDirector(clave);
            ResultadoEJB<Filter<PersonalActivo>> resEncargadoDir = validarEncargadoDirector(clave);
            ResultadoEJB<Filter<PersonalActivo>> resTutor = validarTutor(clave);
        if(resPsicope.getCorrecto()){
          return ResultadoEJB.crearCorrecto(resPsicope.getValor(), "El usuario ha sido comprobado como personal de Psicopedagogía");
        }else if(resDirector.getCorrecto()){
            return ResultadoEJB.crearCorrecto(resDirector.getValor(), "El usuario ha sido comprobado como personal de Psicopedagogía");
        }else if(resEncargadoDir.getCorrecto()){
            return ResultadoEJB.crearCorrecto(resEncargadoDir.getValor(), "El usuario ha sido comprobado como personal de Psicopedagogía");
        }else if(resTutor.getCorrecto()){
            return ResultadoEJB.crearCorrecto(resTutor.getValor(), "El usuario ha sido comprobado como personal de Psicopedagogía");
        }else {return ResultadoEJB.crearErroneo(3,resDirector.getValor(),"El personal autenticado no tiene acceso a esta información");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbCedulaIdentificacion.validarAcceso", e, null);
        }
    }

    /**
     * Valida que el personal autenticado sea del area de Psicopedagogía
     * @param clave
     * @return
     */

    public ResultadoEJB<Filter<PersonalActivo>> validarPsicopedagogia(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalPsicopedagogia").orElse(18)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de Psicopedagogía");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbCedulaIdentificacion.validarPsicopedagogia)", e, null);
        }

    }
    // Se valida el acceso del personal autorizado, así como se obtienen los catálogos necesarios para poder crear un plan de estudios
    /**
     * Permite validar el usuario autenticado si es como director de área
     * académica
     *
     * @param clave Número de Nomina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDirector(Integer clave) {
        try {
            return ejbValidacionRol.validarDirector(clave);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El director no se pudo validar. (EjbRegistroPlanEstudio.validarDirector)", e, null);
        }
    }

    public ResultadoEJB<Filter<PersonalActivo>> validarEncargadoDirector(Integer clave) {
        try {
            return ejbValidacionRol.validarEncargadoDireccion(clave);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El director no se pudo validar. (EjbRegistroPlanEstudio.validarDirector)", e, null);
        }
    }
    public ResultadoEJB<Filter<PersonalActivo>> validarTutor(Integer clave) {
        try {
            return ejbValidacionRol.validarTutor(clave);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El director no se pudo validar. (EjbRegistroPlanEstudio.validarDirector)", e, null);
        }
    }


    /**
     * Obtiene lel último cuestionario psicopedagogico aperturado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Evaluaciones> getUltimoCuestionarioActivo(){
        try{
            Evaluaciones cuestionario = em.createQuery("select e from Evaluaciones e where e.tipo=:tipo order by e.evaluacion desc", Evaluaciones.class)
                    .setParameter("tipo", EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getLabel())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(cuestionario ==null){return ResultadoEJB.crearErroneo(2,cuestionario,"No se encontro cuestionario");}
            else {return ResultadoEJB.crearCorrecto(cuestionario,"Se obtuvo el cuestionario");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el último cuestionario aperturado. (EjbSeguimientoCuestionarioPsicopedagico.getUltimoCuestionarioActivo", e, null);
        }
    }

    /**
     * Busca una apertura de visualizacion del cuestionario activo
     * @return Resultado del proceso
     */

    public ResultadoEJB<AperturaVisualizacionEncuestas> getAperturaActiva(){
        try{
            AperturaVisualizacionEncuestas visualizacionEncuestas = em.createQuery("select a from AperturaVisualizacionEncuestas  a where a.encuesta=:tipo and :fecha between a.fechaInicial AND a.fechaFinal order by a.apertura desc ",AperturaVisualizacionEncuestas.class)
                    .setParameter("fecha", new Date())
                    .setParameter("tipo","Cuestionario Psicopedagógico")
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(visualizacionEncuestas !=null){return ResultadoEJB.crearCorrecto(visualizacionEncuestas,"Apertura de visualización del cuestionario activa");}
            else {return ResultadoEJB.crearErroneo(3,visualizacionEncuestas,"No exite apertura de visualización del cuestionario activa");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el último cuestionario aperturado. (EjbSeguimientoCuestionarioPsicopedagico.getAperturaActiva", e, null);
        }
    }

    /**
     * Obtiene el periodo escolar activo
     * @return
     */
    public ResultadoEJB<PeriodosEscolares> getPeriodoEscolarActivo(){
        try{
            return ejbPeriodoEventoRegistro.getPeriodoEscolarActivo();
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el periodo escolar activo (EjbSeguimientoCuestionarioPsicopedagico.getPeriodoEscolarActivo).", e, PeriodosEscolares.class);
        }
    }

    /**
     * Obtiene la lista de estudiantes regulares o reincoporados por el periodo activo
     * @param periodoActivo Periodo activo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Estudiante>> getEstudiantesByPeriodoActivo (@NonNull PeriodosEscolares periodoActivo){
        try {
            if (periodoActivo == null) {
                return ResultadoEJB.crearErroneo(2, new ArrayList<>(), "El periodo no debe ser nulo");
            }
            List<Estudiante> estudiantes = new ArrayList<>();
            estudiantes = em.createQuery("select e from Estudiante e where e.periodo=:periodo and(e.tipoEstudiante.idTipoEstudiante=:tipo or e.tipoEstudiante.idTipoEstudiante=:tipo2)", Estudiante.class)
                    .setParameter("tipo", 1)
                    .setParameter("tipo2", 5)
                    .setParameter("periodo", periodoActivo.getPeriodo())
                    .getResultList();
            if (estudiantes == null || estudiantes.isEmpty()) {
                return ResultadoEJB.crearErroneo(2, estudiantes, "No se encontraron estudiantes en el periodo proporcionado");
            } else {return ResultadoEJB.crearCorrecto(estudiantes  ,"Estudiantes encontrados");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes activos del periodo actual(EjbSeguimientoCuestionarioPsicopedagico.getEstudiantesByPeriodoActivo", e, null);
        }
    }


    /**
     * Obtiene la lista de resultados generales
     * No se busca por evaluacion porque los estudiantes lo responden una sola vez
     * @return Resultado del proceso (Lista de resultados generales)
     */
    public ResultadoEJB<List<CuestionarioPsicopedagogicoResultados>> getResuladosGenerales (){
        try{
            List<CuestionarioPsicopedagogicoResultados> resultados = em.createQuery("select c from CuestionarioPsicopedagogicoResultados c",CuestionarioPsicopedagogicoResultados.class)
                    .getResultList();
            if(resultados==null || resultados.isEmpty()){return ResultadoEJB.crearErroneo(3,resultados,"No seencontraron resultados del cuestionario");}
            else {return ResultadoEJB.crearCorrecto(resultados,"Resultados encontrasos");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obneter la lista de los resultados del cuestionario. (EjbSeguimientoCuestionarioPsicopedagico.getResuladosGenerales", e, null);
        }
    }

    /**
     * Busca al estudiante por matricula
     * Obtiene la ultima inscripción
     * @param id Estudiante que respondio en el cuestionario
     * @return Resultado del proceso
     */
    public ResultadoEJB<Estudiante> getEstudiante (@NonNull Estudiante id){
        try{
            if (id == null) {return ResultadoEJB.crearErroneo(2,id,"El estudiante no debe ser nulo"); }
            Estudiante estudiante = em.createQuery("SELECT e FROM Estudiante e where e.matricula=:matricula ORDER BY e.periodo desc",Estudiante.class)
                    .setParameter("matricula",id.getMatricula())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(estudiante ==null){return ResultadoEJB.crearErroneo(3,estudiante,"No se encontro a un estudiante con la mtricula proporcionada");}
            else {return ResultadoEJB.crearCorrecto(estudiante,"Estudiante localizado con éxito");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener al estudiante. (EjbSeguimientoCuestionarioPsicopedagico.getEstudiante", e, null);
        }
    }

    /**
     * Obtiene al estudiante de primer grado
     * El cuestionario lo responden cuando van en primero
     * @param estudiante Estudiante regular del periodo activo
     * @return Estudiante de primer grado
     */
    public ResultadoEJB<Estudiante> getEstudiantePrimerGrado(@NonNull Estudiante estudiante){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,estudiante,"El estudiante no debe ser nulo");}
           // System.out.println("Matricula que ingresa -> " + estudiante.getMatricula()+ " "+estudiante.getIdEstudiante());
            Estudiante estudiante1 = em.createQuery("select e from Estudiante e where e.matricula=:matricula order by e.periodo asc ",Estudiante.class)
                    .setParameter("matricula",estudiante.getMatricula())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            //System.out.println("Estudiante 1ero " + estudiante1.getIdEstudiante()+" "+ estudiante1.getGrupo().getGrado());
            if(estudiante1==null){return ResultadoEJB.crearErroneo(3,estudiante1,"No se encontró registro");}
            else {return ResultadoEJB.crearCorrecto(estudiante1,"");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener al estudiante. (EjbSeguimientoCuestionarioPsicopedagico.getEstudiantePrimerGrado", e, null);
        }
    }

    /**
     * Busca resultados del cuestionario psicopedagogico por id del estudiante
     * @param estudiante Resgutro del estudiante en primer grado
     * @return Resultado del proceso (Resultados del cuestionario pscopedagogico)
     */
    public ResultadoEJB<CuestionarioPsicopedagogicoResultados> getResultadosbyEstudiante(@NonNull Estudiante estudiante){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new CuestionarioPsicopedagogicoResultados(),"El estudiante no debe ser nulo");}
            CuestionarioPsicopedagogicoResultados resultados = em.createQuery("select  c from CuestionarioPsicopedagogicoResultados c where c.estudiante.idEstudiante=:id",CuestionarioPsicopedagogicoResultados.class)
                    .setParameter("id",estudiante.getIdEstudiante())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(resultados==null){return  ResultadoEJB.crearErroneo(3,resultados,"No se encontraron resultados del estudiante");}
            else {return  ResultadoEJB.crearCorrecto(resultados,"Resultados encontrados");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener resultados del cuestionario. (EjbSeguimientoCuestionarioPsicopedagico.getResultadosbyEstudiante", e, null);
        }
    }

    /**
     * Busca personal por clave de trabajador
     * @param clave Clave del trabajador
     * @return Resultado del proceso
     */
    public ResultadoEJB<Personal> getPersonalbyClave (@NonNull int clave){
        try{
            Personal personal = new Personal();
            if(clave==0){return ResultadoEJB.crearErroneo(2,personal,"La clave del personal no debe ser nulo");}
            personal = em.createQuery("select p from Personal p where p.clave=:clave", Personal.class)
                    .setParameter("clave",clave)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(personal==null){return ResultadoEJB.crearErroneo(3,personal,"No se encontró al personal con la clave proporcionada");}
            else {return ResultadoEJB.crearCorrecto(personal,"Personal encontrado");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener al estudiante. (EjbSeguimientoCuestionarioPsicopedagico.getPersonalbyClave", e, null);
        }
    }

    /**
     * Busca area por clave
     * @param clave
     * @return Resultado del proceso
     */
    public ResultadoEJB<AreasUniversidad> getPebyClave (@NonNull Short clave){
        try{
            if(clave==null){ return ResultadoEJB.crearErroneo(2,new AreasUniversidad(),"La clave no debe ser nula");}
            AreasUniversidad pe = em.createQuery("SELECT a FROM AreasUniversidad  a where a.area=:area",AreasUniversidad.class)
                    .setParameter("area",clave)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(pe==null){return ResultadoEJB.crearErroneo(3,pe,"No se encontro el programa educativo");}
            else {return ResultadoEJB.crearCorrecto(pe, "Programa educativo encontrado");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener al estudiante. (EjbSeguimientoCuestionarioPsicopedagico.getPersonalbyClave", e, null);
        }
    }

    /**
     * Obtiene al personal encargado de un área
     * @param areaSuperior Clave de área superior de un PE
     * @return Resultado del proceso
     */
    public ResultadoEJB<Personal> getDirectorbyArea(@NonNull short areaSuperior){
        try{
            ResultadoEJB<AreasUniversidad> resAreaSup = getPebyClave(areaSuperior);
            if(resAreaSup.getCorrecto()){
                //Se busca al personal encargado del área
                ResultadoEJB<Personal> resDirec = getPersonalbyClave(resAreaSup.getValor().getResponsable());
                if(resDirec.getCorrecto()){return ResultadoEJB.crearCorrecto(resDirec.getValor(),"Responsable encontrado");}
                else {return ResultadoEJB.crearErroneo(3,new Personal(),"Error al obtener al responsable del área");}
            }else {return ResultadoEJB.crearErroneo(2, new Personal(),"Error al obtener el área superior");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener al director. (EjbSeguimientoCuestionarioPsicopedagico.getDirectorbyArea", e, null);
        }
    }

    /**
     * Empaqueta los resultados del cuestionario psicopedagogico del estudiante
     * @param e Estudiante (Registro de 1er cuatrimestre)
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoCuestionarioPsicopedagogicoEstudiante> packCuestionarioEstudiante(@NonNull Estudiante e){
        try{
            DtoCuestionarioPsicopedagogicoEstudiante dto = new DtoCuestionarioPsicopedagogicoEstudiante(new Estudiante(),new CuestionarioPsicopedagogicoResultados(),new String(),new AreasUniversidad(),new String(),new Personal(),new Personal(),false);
            if(e==null){return ResultadoEJB.crearErroneo(2,dto,"El estudiante  no debe ser nulos");}
            //System.out.println("Estudiante "+ e);
            dto.setEstudianteA(e);
            //Obtiene al tutor de grupo
            if(e.getGrupo().getTutor()!=null){
                ResultadoEJB<Personal> resTutor = getPersonalbyClave(e.getGrupo().getTutor());
                //System.out.println("Tutor" + resTutor.getValor());
                    if(resTutor.getCorrecto()){
                        dto.setTutor(resTutor.getValor().getNombre());
                        dto.setTutorP(resTutor.getValor());
                    }else {return ResultadoEJB.crearErroneo(5,dto,"Error al obtener al tutor del grupo");}
            }else {dto.setTutor("Sin asignar");}
            //Obtiene el programa educativo
            ResultadoEJB<AreasUniversidad> resPe = getPebyClave(dto.getEstudianteA().getCarrera());
            //System.out.println("Pe" + resPe.getValor());
            if(resPe.getCorrecto()){dto.setPe(resPe.getValor());}
            else {return ResultadoEJB.crearErroneo(6,dto,"Error al obtener el programa educativo");}
            //Su busca al director encargado del Programa eductivo
            ResultadoEJB<Personal> resDirector = getDirectorbyArea(resPe.getValor().getAreaSuperior());
            if(resDirector.getCorrecto()){
                dto.setDirector(resDirector.getValor());
            }else {return ResultadoEJB.crearErroneo(7,dto,"Error al obtener al director del área");}
            //Se busca el primer registro del estudiante
            ResultadoEJB<Estudiante> resEs = getEstudiantePrimerGrado(e);
            if(resEs.getCorrecto()==true){
                //Busca los resultados del cuestionario
                ResultadoEJB<CuestionarioPsicopedagogicoResultados> resEstudiante = getResultadosbyEstudiante(resEs.getValor());
                if(resEstudiante.getCorrecto()==true){
                    dto.setIngresoAlSistema(true);
                    dto.setCuestionario(resEstudiante.getValor());
                    //Obtiene al personal que examino el cuestionario del estuduiante
                    if(resEstudiante.getValor().getClave()!=null){
                        ResultadoEJB<Personal> resPersonal = getPersonalbyClave(resEstudiante.getValor().getClave());
                        if(resPersonal.getCorrecto()){dto.setPersonalEvaluador(resPersonal.getValor().getNombre()); }
                        else {return ResultadoEJB.crearErroneo(7,dto,"Error al obtener al personal que examinó el cuestionario del estudiante");}
                    }else {dto.setPersonalEvaluador("Sin revisión");}
                }else {dto.setIngresoAlSistema(false);}
            }else {return ResultadoEJB.crearErroneo(8,dto,"Error al obtener el primer registro del estudiante");}
            return ResultadoEJB.crearCorrecto(dto,"Empaquetado");
        }catch (Exception a){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar. (EjbSeguimientoCuestionarioPsicopedagico.packCuestionarioEstudiante", a, null);
        }
    }

    public ResultadoEJB<List<DtoCuestionarioPsicopedagogicoEstudiante>> getResultadosbyEstudiantes(@NonNull PeriodosEscolares periodoActivo){
        try{
            List<DtoCuestionarioPsicopedagogicoEstudiante> listDto= new ArrayList<>();
            if(periodoActivo==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El periodo no debe ser nulo");}
            //Obtiene la lista de estudiantes por el periodo activo
            ResultadoEJB<List<Estudiante>> resEstudiantes = getEstudiantesByPeriodoActivo(periodoActivo);
            //System.out.println("Lista estudiantes " + resEstudiantes.getValor().size());
            if(resEstudiantes.getCorrecto()==true){
                //Recorre la lista para obtener su registro de primer año y los resultados de su cuestionario
                resEstudiantes.getValor().forEach(e->{
                    ResultadoEJB<Estudiante> resEs = getEstudiantePrimerGrado(e);
                    if(resEs.getCorrecto()==true){
                        ResultadoEJB<DtoCuestionarioPsicopedagogicoEstudiante> resPack= packCuestionarioEstudiante(resEs.getValor());
                        //System.out.println("Pack "+resPack.getValor());
                        if(resPack.getCorrecto()==true){
                            resPack.getValor().setEstudianteA(e);
                            listDto.add(resPack.getValor());
                        }else {return;}
                    }else {return;}
                });
                //System.out.println("Lista Dto ->"+ listDto.size());
                return ResultadoEJB.crearCorrecto(listDto,"Lista de resultados generada");
            }else {return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"Error al obneter la lista de estudiantes por periodo activo");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar. (EjbSeguimientoCuestionarioPsicopedagico.packCuestionarioEstudiante", e, null);

        }
    }
}
