package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReporteGeneralFichas;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReporteProyeccionFichas;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProyeccionAreas;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.TipoRegistroEstudiante;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProyeccionAreasPK;

/**
 * Consulta de registro de fichas de admisión  por carrera
 * Solo tienen acceso:
 * Vinculacion(Direccion),Servicios Escolares (Jefe),Planeación (Director),Estadistica (Jefe),Secretaria Académica, Rector, Prensa (Toda la coordinación),Directores de carrera
 * @author Taatisz :)
 */
@Stateless(name = "EjbFichaAdmisionReporte")
public class EjbFichaAdmisionReporte {
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    @EJB EjbValidacionRol ejbValidacionRol;
    private EntityManager em;

    @PostConstruct
    public void init(){em = f.getEntityManager();}

    /**
     * Valida que el usuario logueado tenga acceso . Tienen acceso al reporte:
     * - Rector
     * - Secretaría Académica
     * - Director de Vinculacion
     * - Director de Planeacion
     * - Jefes de Estadistica y Servicios Escolares
     * - Personal de Prensa y Difusión
     * @param clave Clave de personal logueado
     * @return Resultado del proceso
     */
    public ResultadoEJB<PersonalActivo> validarAcceso(Integer clave){
        try{
            if(clave ==null){return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"La clave no debe ser nula");}
            else {
                PersonalActivo personalActivo = ejbPersonalBean.pack(clave);

                ResultadoEJB<PersonalActivo> resRector = validarRector(personalActivo);
                if(resRector.getCorrecto()==true){
                    //System.out.println("Rector" );
                    return ResultadoEJB.crearCorrecto(resRector.getValor(),"Validado como Rector");}
                else {
                    //System.out.println("----" );
                    ResultadoEJB<PersonalActivo> resSA = validarSA(personalActivo);
                    if(resSA.getCorrecto()==true){
                        //System.out.println("Academica" +resSA.getValor() );
                        return ResultadoEJB.crearCorrecto(resSA.getValor(),"Validado como Secretario/a Académica");}
                    else {
                        ResultadoEJB<PersonalActivo> resDirPlaneacion= validarDirPlaneacion(personalActivo);
                        if(resDirPlaneacion.getCorrecto()==true){
                            // System.out.println("Director de planeación" +resDirPlaneacion.getValor() );
                            return ResultadoEJB.crearCorrecto(resDirPlaneacion.getValor(),"Validado como Director/a de Planeacion");
                        }
                        else {
                            ResultadoEJB<PersonalActivo> resDirVinculacion = validarDirVinculacion(personalActivo);
                            if(resDirVinculacion.getCorrecto()==true){
                                //System.out.println("Director de vinculacion" +resDirVinculacion.getValor() );
                                return ResultadoEJB.crearCorrecto(resDirVinculacion.getValor(),"Validado como Director /a de Vinculción");
                            }
                            else {
                                ResultadoEJB<PersonalActivo> resDirectorA = validarDirAc(personalActivo);
                                if(resDirectorA.getCorrecto()==true){
                                    //System.out.println("Director de area" +resDirVinculacion.getValor() );
                                    return ResultadoEJB.crearCorrecto(resDirectorA.getValor(),"Validado como director ");
                                }else {
                                    ResultadoEJB<PersonalActivo> resJefeEscolares = validarJefeEscolares(personalActivo);
                                    if(resJefeEscolares.getCorrecto()==true){
                                        //System.out.println("Jefe de Servicios Escolares" +resDirVinculacion.getValor() );
                                        return ResultadoEJB.crearCorrecto(resJefeEscolares.getValor(),"Validado como jefe de servicios esoolares");
                                    }else {
                                        ResultadoEJB<PersonalActivo> resJefeEstadistica = validarJefeEtadistica(personalActivo);
                                        if(resJefeEstadistica.getCorrecto()== true){
                                            //System.out.println("Jefe de IyE" +resJefeEstadistica.getValor() );
                                            return ResultadoEJB.crearCorrecto(resJefeEstadistica.getValor(),"Validado como jefe de IyE");
                                        }
                                        else {
                                            ResultadoEJB<PersonalActivo> resPrensa = validarPrensa(personalActivo);
                                            if(resPrensa.getCorrecto()==true){
                                                // System.out.println("Prensa" +resPrensa.getValor() );
                                                return ResultadoEJB.crearCorrecto(resPrensa.getValor(),"Validado como personal de prensa");
                                            }else {
                                                return ResultadoEJB.crearErroneo(5,resPrensa.getValor(),"No tiene acceso");
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }

            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbFichaAdmisionReporte.validarAcceso)", e, null);

        }
    }

    /**
     * Valida que el usuario logueado sea rector
     * @param personalActivo
     * @return Resultado del proceso
     */
    public ResultadoEJB<PersonalActivo> validarRector (PersonalActivo personalActivo){
        try {
            //System.out.println("Val Rector");
            //PersonalActivo personalActivo = new PersonalActivo();
            if(personalActivo ==null){return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"El personal no debe ser nulo");}
            //System.out.println("Personal " + personalActivo.getAreaOperativa().getArea()+ " Cat" + personalActivo.getPersonal().getCategoriaOperativa().getCategoria());
            if(personalActivo.getAreaOperativa().getArea()==1 & personalActivo.getPersonal().getCategoriaOperativa().getCategoria() ==33){
                //Es rector
                //System.out.println("Rector");
                return ResultadoEJB.crearCorrecto(personalActivo,"Usuario autenticado como rector");
            }
            else {
                //System.out.println("No...");
                return ResultadoEJB.crearErroneo(2,personalActivo,"No es rector");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbFichaAdmisionReporte.validarRector)", e, null);

        }
    }

    /**
     * Valida que el usuario logueado sea Secretario Acedemico
     * @param personalActivo Clave del personal
     * @return Resultado del proceso
     *
     */

    public ResultadoEJB<PersonalActivo> validarSA (PersonalActivo personalActivo){
        try {
            //System.out.println("Sec Ac--->" );
            //PersonalActivo personalActivo = new PersonalActivo();
            if(personalActivo ==null){return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"La clave no debe ser nula");}
            if(personalActivo.getAreaOperativa().getArea()== 2 & personalActivo.getPersonal().getCategoriaOperativa().getCategoria()==38 & personalActivo.getAreaSuperior().getArea()==1){
                //Es SA
                //System.out.println("Sec Ac" );
                return ResultadoEJB.crearCorrecto(personalActivo,"Usuario autenticado como Secretario /a Académico");
            }
            else {
                //System.out.println("No es SA" );
                return ResultadoEJB.crearErroneo(2,personalActivo,"No es SA");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbFichaAdmisionReporte.validarSA)", e, null);

        }
    }


    /**
     * Valida que el usuario logueado sea Director de Planeación
     * @param personalActivo
     * @return
     */
    public ResultadoEJB<PersonalActivo> validarDirPlaneacion (PersonalActivo personalActivo){
        try {
            //Personal personalActivo = new PersonalActivo();
            if(personalActivo ==null){return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"La clave no debe ser nula");}
            if(personalActivo.getAreaOperativa().getArea()==6  &personalActivo.getAreaSuperior().getArea()==1 & personalActivo.getPersonal().getCategoriaOperativa().getCategoria()==18 || personalActivo.getPersonal().getCategoriaOperativa().getCategoria()==48){
                //Es Director de Planeación
                return ResultadoEJB.crearCorrecto(personalActivo,"Usuario autenticado como director de planeación");
            }
            else {
                return ResultadoEJB.crearErroneo(2,personalActivo,"No es Director de Planeación");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbFichaAdmisionReporte.validarDirPlaneacion)", e, null);

        }
    }

    /**
     * Valida que el usuario logueado sea Director de Vinculación
     * @param personalActivo
     * @return
     */
    public ResultadoEJB<PersonalActivo> validarDirVinculacion (PersonalActivo personalActivo){
        try {
            //PersonalActivo personalActivo = new PersonalActivo();
            if(personalActivo ==null){return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"La clave no debe ser nula");}

            if(personalActivo.getAreaOperativa().getArea()==5  &personalActivo.getAreaSuperior().getArea()==1 & personalActivo.getPersonal().getCategoriaOperativa().getCategoria()==18 || personalActivo.getPersonal().getCategoriaOperativa().getCategoria()==48){
                //Es Director de Planeación
                return ResultadoEJB.crearCorrecto(personalActivo,"Usuario autenticado como director de vinculación");
            }
            else {
                return ResultadoEJB.crearErroneo(2,personalActivo,"No es Director de Vinculación");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbFichaAdmisionReporte.validarDirVinculacion)", e, null);

        }
    }

    /**
     * Valida que el usuario logueado sea director de algún area académica
     * @param personalActivo Clave del personal logueado
     * @return Resultado del proceso
     */
    public ResultadoEJB<PersonalActivo> validarDirAc (PersonalActivo personalActivo){
        try {
            if(personalActivo ==null){return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"La clave no debe ser nula");}
            if(personalActivo.getAreaSuperior().getArea()==2 & personalActivo.getPersonal().getCategoriaOperativa().getCategoria()==18 || personalActivo.getPersonal().getCategoriaOperativa().getCategoria()==48){
                //Es Director de Planeación
                return ResultadoEJB.crearCorrecto(personalActivo,"Usuario autenticado como director de algun area académica");
            }
            else {
                return ResultadoEJB.crearErroneo(2,personalActivo,"No es Director de Area");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbFichaAdmisionReporte.validarDirAc)", e, null);

        }
    }

    /**
     * Valida que el usuario logueado sea Jefe de Servicios Escolares
     * @param personalActivo Clave de personal logueado
     * @return Resultado del proceso
     */
    public ResultadoEJB<PersonalActivo> validarJefeEscolares (PersonalActivo personalActivo){
        try {
            if(personalActivo ==null){return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"La clave no debe ser nula");}

            if(personalActivo.getAreaOperativa().getArea()==10  &personalActivo.getAreaSuperior().getArea()==2 & personalActivo.getPersonal().getCategoriaOperativa().getCategoria()==24 || personalActivo.getPersonal().getCategoriaOperativa().getCategoria()==115){
                //Es Director de Planeación
                return ResultadoEJB.crearCorrecto(personalActivo,"Usuario autenticado como jefe de Servicios Escolares");
            }
            else {
                return ResultadoEJB.crearErroneo(2,personalActivo,"No es Jefe de Escolares");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbFichaAdmisionReporte.validarJefeEscolares)", e, null);

        }
    }

    /**
     * Valida que el usuario logueado sea Jefe de IyE
     * @param personalActivo Clave del personal logueado
     * @return Resultado del Procesoo
     */
    public ResultadoEJB<PersonalActivo> validarJefeEtadistica (PersonalActivo personalActivo){
        try {
            if(personalActivo ==null){return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"La clave no debe ser nula");}

            if(personalActivo.getAreaOperativa().getArea()==9 &personalActivo.getAreaSuperior().getArea()==6 & personalActivo.getPersonal().getCategoriaOperativa().getCategoria()==24 || personalActivo.getPersonal().getCategoriaOperativa().getCategoria()==115){
                //Es Director de Planeación
                return ResultadoEJB.crearCorrecto(personalActivo,"Usuario autenticado como jefe de Servicios de IyE");
            }
            else {
                return ResultadoEJB.crearErroneo(2,personalActivo,"No es Jefe de Estadística");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbFichaAdmisionReporte.validarJefeEtadistica)", e, null);

        }
    }

    /**
     * Valida que el usuario logueado sea personal de Prensa y Difusión
     * @param personalActivo Clave del personal logueado
     * @return Resultado del prceso
     */
    public ResultadoEJB<PersonalActivo> validarPrensa (PersonalActivo personalActivo){
        try {
            if(personalActivo ==null){return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"La clave no debe ser nula");}

            if(personalActivo.getAreaOperativa().getArea()==16 ){
                //Es Director de Planeación
                return ResultadoEJB.crearCorrecto(personalActivo,"Usuario autenticado como personal de Prensa");
            }
            else {
                return ResultadoEJB.crearErroneo(2,personalActivo,"No es de Prensa");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbFichaAdmisionReporte.validarPrensa)", e, null);

        }
    }
    /**
     * Obtiene la lista de procesos de inscripcion, segun los proceso de inscripcion para nuevo ingreso
     * @return
     */
    public ResultadoEJB<List<ProcesosInscripcion>> getProcesosInscripcion(){
        try{
            List<ProcesosInscripcion> procesosInscripcions = new ArrayList<>();
            procesosInscripcions = em.createQuery("select p from ProcesosInscripcion p where p.activoNi=true",ProcesosInscripcion.class)
                    .getResultList()
            ;
            if(procesosInscripcions.isEmpty()|| procesosInscripcions==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"No existen procesos de inscripción");}
            else {return ResultadoEJB.crearCorrecto(procesosInscripcions,"Lista de procesos de inscripcion");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el último proceso de inscripción. (EjbFichaAdmisionReporte.getUltimoProcesoInscripcion)", e, null);

        }
    }



    /**
     * Obtiene el ultimo periodo de inscripción
     * @return Resultado del proceso
     */
    public ResultadoEJB<ProcesosInscripcion> getUltimoProcesoInscripcion (){
        try {
            ProcesosInscripcion procesosInscripcion = new ProcesosInscripcion();
            procesosInscripcion = em.createQuery("select p from ProcesosInscripcion p where p.activoNi=:ni order by p.idProcesosInscripcion desc",ProcesosInscripcion.class)
                    .setParameter("ni",true)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(procesosInscripcion ==null){return ResultadoEJB.crearErroneo(2,procesosInscripcion,"No se ecnontro proceso de inscripcion");}
            else {
                return ResultadoEJB.crearCorrecto(procesosInscripcion,"Ultimo proceso de inscripción");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el último proceso de inscripción. (EjbFichaAdmisionReporte.getUltimoProcesoInscripcion)", e, null);
        }
    }

    /**
     * Obtiene laas areas de TSU Vigentes
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AreasUniversidad>> getAreasVigentes (){
        try{
            List<AreasUniversidad> areas = new ArrayList<>();
            areas = em.createQuery("select a from AreasUniversidad  a where a.nivelEducativo.nivel=:nivel and a.vigente=:vigente", AreasUniversidad.class)
                    .setParameter("nivel","TSU")
                    .setParameter("vigente","1")
                    .getResultList()
            ;
            if(areas==null || areas.isEmpty()){return ResultadoEJB.crearErroneo(2,areas,"No se encontraron areas vigentes");}
            else {return ResultadoEJB.crearCorrecto(areas,"Areas vigentes");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los programas educativos nivel TSU vigentes. (EjbFichaAdmisionReporte.getAreasVigentes)", e, null);
        }
    }

    /**
     * Obtien la lista de programas educativos nivel 5A y 5B
     * @return Resultado del proveso
     */
    public ResultadoEJB<List<AreasUniversidad>> getPEIng(){
        try{
            List<AreasUniversidad> pe = new ArrayList<>();
            pe = em.createQuery("select a from AreasUniversidad a where a.vigente=:vigente and (a.nivelEducativo.nivel=:nivel or a.nivelEducativo.nivel=:nivel2)", AreasUniversidad.class)
                    .setParameter("vigente","1")
                    .setParameter("nivel","5A")
                    .setParameter("nivel2","5B")
                    .getResultList()
            ;
            //System.out.println("EjbRegistroFichaIngenieria.getPEIngbyArea Areas 5A y 5B" + pe);
            if(pe.isEmpty()||pe ==null ){return ResultadoEJB.crearErroneo(3,pe,"No exiten ingenirías");}
            else {return ResultadoEJB.crearCorrecto(pe,"Programas educativos nivel ingeniería");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los PE de la Ing (EjbRegistroFichaIngenieria.getPEIngbyArea).", e, null);
        }
    }

    /**
     * Obtiene una lista de aspirantes por programa educativo
     * @param pe Programa Educativo
     * @param procesoInscripcion Ultimo proceso de Inscripción
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Aspirante>> getAspirantesbyPE (AreasUniversidad pe, ProcesosInscripcion procesoInscripcion){
        try{
            List<Aspirante> aspirantes = new ArrayList<>();
            if(pe == null ){return ResultadoEJB.crearErroneo(2, aspirantes,"El programa educativo no debe ser nulo");}
            if(procesoInscripcion==null){return ResultadoEJB.crearErroneo(3,aspirantes,"El proceso de inscripción no debe ser nulo");}
            aspirantes = em.createQuery("SELECT a FROM Aspirante a WHERE a.idProcesoInscripcion.idProcesosInscripcion = :idpi AND a.tipoAspirante.idTipoAspirante = 1 AND a.datosAcademicos.primeraOpcion = :po AND a.datosAcademicos <> null and a.folioAspirante <> null ORDER BY a.folioAspirante",Aspirante.class)
                    .setParameter("idpi",procesoInscripcion.getIdProcesosInscripcion())
                    .setParameter("po",pe.getArea())
                    .getResultList()
            ;
            if(aspirantes ==null || aspirantes.isEmpty()){return ResultadoEJB.crearErroneo(4,aspirantes,"No se encontraron aspirantes");}
            else {return ResultadoEJB.crearCorrecto(aspirantes,"Lista de aspirantes");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la lista de aspirantes. (EjbFichaAdmisionReporte.getAspirantesbyPE)", e, null);

        }

    }
    /**
     * Obtiene una lista de aspirantes ING/LIC por programa educativo
     * @param pe Programa Educativo
     * @param procesoInscripcion Ultimo proceso de Inscripción
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Aspirante>> getAspirantesIngbyPE (AreasUniversidad pe, ProcesosInscripcion procesoInscripcion){
        try{
            List<Aspirante> aspirantes = new ArrayList<>();
            if(pe == null ){return ResultadoEJB.crearErroneo(2, aspirantes,"El programa educativo no debe ser nulo");}
            if(procesoInscripcion==null){return ResultadoEJB.crearErroneo(3,aspirantes,"El proceso de inscripción no debe ser nulo");}
            aspirantes = em.createQuery("SELECT a FROM Aspirante a WHERE a.idProcesoInscripcion.idProcesosInscripcion = :idpi AND a.tipoAspirante.idTipoAspirante = 3 AND a.datosAcademicos.primeraOpcion = :po AND a.datosAcademicos <> null and a.folioAspirante <> null ORDER BY a.folioAspirante",Aspirante.class)
                    .setParameter("idpi",procesoInscripcion.getIdProcesosInscripcion())
                    .setParameter("po",pe.getArea())
                    .getResultList()
            ;
            if(aspirantes ==null || aspirantes.isEmpty()){return ResultadoEJB.crearErroneo(4,aspirantes,"No se encontraron aspirantes");}
            else {return ResultadoEJB.crearCorrecto(aspirantes,"Lista de aspirantes");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la lista de aspirantes. (EjbFichaAdmisionReporte.getAspirantesbyPE)", e, null);

        }

    }

    /**
     * Obtiene una lista de estudiantes incritos por programa educativo
     * @param pe Programa educativo
     * @param procesosInscripcion Proceso de inscripcion seleccionado
     * @return Resultado del proceso
     */

    public ResultadoEJB<List<Estudiante>> getInscritosbyPE(@NonNull AreasUniversidad pe,@NonNull ProcesosInscripcion procesosInscripcion){
        try{
            if(procesosInscripcion==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El proceso de inscripci+on no debe ser nulo");}
            if(pe==null){return  ResultadoEJB.crearErroneo(3,new ArrayList<>(),"El programa educativo no debe ser nulo");}
            List<Estudiante> estudiantesInscritos = em.createQuery("select e from Estudiante e where e.aspirante.idProcesoInscripcion.idProcesosInscripcion=:proceso and e.tipoRegistro=:tipo and e.carrera=:pe",Estudiante.class)
                    .setParameter("proceso",procesosInscripcion.getIdProcesosInscripcion())
                    .setParameter("tipo","Inscripción")
                    .setParameter("pe",pe.getArea())
                    .getResultList()
                    ;
            if(estudiantesInscritos.isEmpty() || estudiantesInscritos==null){return ResultadoEJB.crearErroneo(4,new ArrayList<>(),"No hay estudiantes inscritos");}
            else {return  ResultadoEJB.crearCorrecto(estudiantesInscritos,"Estudiantes incritos por programa educativo");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los estudiantes inscritos por pe. (EjbFichaAdmisionReporte.getInscritosbyPE)", e, null);

        }
    }
    /**
     * Obtiene una lista de estudiantes incritos por programa educativo
     * @param pe Programa educativo
     * @param procesosInscripcion Proceso de inscripcion seleccionado
     * @return Resultado del proceso
     */

    public ResultadoEJB<List<Estudiante>> getInscritoIngsbyPE(@NonNull AreasUniversidad pe,@NonNull ProcesosInscripcion procesosInscripcion){
        try{
            if(procesosInscripcion==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El proceso de inscripci+on no debe ser nulo");}
            if(pe==null){return  ResultadoEJB.crearErroneo(3,new ArrayList<>(),"El programa educativo no debe ser nulo");}
            List<Estudiante> estudiantesInscritos = em.createQuery("select e from Estudiante e where e.aspirante.idProcesoInscripcion.idProcesosInscripcion=:proceso and( e.tipoRegistro=:tipo or e.tipoRegistro=:tipo2 or e.tipoRegistro=:tipo3)and e.carrera=:pe",Estudiante.class)
                    .setParameter("proceso",procesosInscripcion.getIdProcesosInscripcion())
                    .setParameter("tipo", TipoRegistroEstudiante.INSCRIPCION_ING_LIC.getLabel())
                    .setParameter("tipo", TipoRegistroEstudiante.INSCRIPCION_ING_LIC_OTRA_UT.getLabel())
                    .setParameter("tipo", TipoRegistroEstudiante.INSCRIPCION_ING_lIC_OTRA_GENERACION.getLabel())
                    .setParameter("pe",pe.getArea())
                    .getResultList()
                    ;
            if(estudiantesInscritos.isEmpty() || estudiantesInscritos==null){return ResultadoEJB.crearErroneo(4,new ArrayList<>(),"No hay estudiantes inscritos");}
            else {return  ResultadoEJB.crearCorrecto(estudiantesInscritos,"Estudiantes incritos por programa educativo");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los estudiantes inscritos por pe. (EjbFichaAdmisionReporte.getInscritosbyPE)", e, null);

        }
    }

    /**
     * Obtiene la proyeccion por area
     * @param procesosInscripcion Proceso de inscripcion seleccionado
     * @param area Programa educativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<ProyeccionAreas> getProyeccionbyArea(@NonNull ProcesosInscripcion procesosInscripcion,@NonNull AreasUniversidad area){
        try{
            if(procesosInscripcion==null){return ResultadoEJB.crearErroneo(2,new ProyeccionAreas(),"El proceso de inscripción no debe ser nulo");}
            if(area==null){return  ResultadoEJB.crearErroneo(3,new ProyeccionAreas(),"El programa educativo no debe ser nulo");}
            ProyeccionAreas pa= new ProyeccionAreas();
            pa= em.createQuery("select p from ProyeccionAreas p where p.proyeccionAreasPK.procesoInscripcion=:proceso and p.proyeccionAreasPK.pe=:pe",ProyeccionAreas.class)
                    .setParameter("proceso",procesosInscripcion.getIdProcesosInscripcion())
                    .setParameter("pe",area.getArea())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(pa==null){return  ResultadoEJB.crearErroneo(4,new ProyeccionAreas(),"No se encontró proyección del área en el proceso de inscripción seleccionado");}
            else {return  ResultadoEJB.crearCorrecto(pa,"Proyección encontrada");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la proyeccion por area. (EjbFichaAdmisionReporte.getProyeccionbyArea)", e, null);

        }
    }

    /**
     * Obtiene la proyecccion de fichas
     * Este dato se obtiene de Configuración propiedades en Prontuario el cual debe ser proporcionado por Vinculacion
     * fichasProyectadasSemanal --> Fichas proyectadas Semanal
     * fichasProyectadasSabatino --> Fichas proyectadas Sabatino
     * total --> Suma de las dos anteriores
     * @return Resultado del proceso (dtoReporteGeneralFichas)
     */
    public ResultadoEJB<DtoReporteGeneralFichas> getProyeccionFichas (){
        try{
            DtoReporteGeneralFichas dto = new DtoReporteGeneralFichas();
            dto.setTipo("Fichas Proyectadas");
            //Obtiene el numero fichas proyectadas semanal
            dto.setTotalSemanal(ep.leerPropiedadEntera("fichasProyectadasSemanal").orElse(0));
            dto.setTotalSabatino(ep.leerPropiedadEntera("fichasProyectadasSabatino").orElse(0));
            dto.setTotal(dto.getTotalSemanal()+dto.getTotalSabatino());
            return ResultadoEJB.crearCorrecto(dto,"Fichas proyectadas");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la proyeccion de fichas. (EjbFichaAdmisionReporte.getProyeccionFichas)", e, null);
        }
    }

    /**
     * Obtiene la matricula proyectada
     * Este dato se obtiene de Configuración propiedades en Prontuario el cual debe ser proporcionado por Planeación (Los valores se deben cambiar anualmente)
     * matriculaProyectadaSemanal --> Proyecccion de estudiantes inscritos en ultimo periodo de proceso de inscripcion
     * matriculaProyectadaSabatino -> Proyeccion de estudiantes inscritos en el ultimo periodo de proceso de inscripcion
     * total -> suma de los dos anteriores
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoReporteGeneralFichas> getProyeccionMatricula (){
        try{
            DtoReporteGeneralFichas dto = new DtoReporteGeneralFichas();
            dto.setTipo("Matricula Proyectada");
            dto.setTotalSemanal(ep.leerPropiedadEntera("matriculaProyectadaSemanal").orElse(0));
            dto.setTotalSabatino(ep.leerPropiedadEntera("matriculaProyectadaSabatino").orElse(0));
            dto.setTotal(dto.getTotalSemanal()+dto.getTotalSabatino());
            return ResultadoEJB.crearCorrecto(dto,"Matricula Proyectada");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la proyeccion de matricula. (EjbFichaAdmisionReporte.getProyeccionMatricula)", e, null);
        }
    }


    /**
     * Obtiene la matricula registrada
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoReporteGeneralFichas> getMatriculaInscrita(@NonNull ProcesosInscripcion procesosInscripcion){
        try{
            DtoReporteGeneralFichas dto = new DtoReporteGeneralFichas();
            if(procesosInscripcion==null){return ResultadoEJB.crearErroneo(2,dto,"El proceso de inscripción no debe ser nulo");}
            dto.setTipo("Matricula Inscrita");
            //Se obtiene el total de la matricula inscrita
            ResultadoEJB<List<Estudiante>> resInscritos =getEstudiantesInscritos(procesosInscripcion);
            // System.out.println("Lista estudiantes inscritos" + resInscritos.getValor().size());
            if(resInscritos.getCorrecto()==true){
                // System.out.println("Correcto" + resInscritos.getValor().size());
                dto.setTotal(resInscritos.getValor().size());
                dto.setTotalSemanal((int) resInscritos.getValor().stream()
                        .filter(x -> x.getGrupo().getIdSistema().getNombre().equals("Semanal"))
                        .count());
                dto.setTotalSemanal((int) resInscritos.getValor().stream()
                        .filter(x -> x.getGrupo().getIdSistema().getNombre().equals("Sabatino"))
                        .count());
                return ResultadoEJB.crearCorrecto(dto,"Matricula Registrada");
            }
            else {
                //La lista viene vacia o nula
                dto.setTotal(0);
                dto.setTotalSemanal(0);
                dto.setTotalSabatino(0);
                // System.out.println("Error-> getListaEstudiantesInscritos");
                return ResultadoEJB.crearCorrecto(dto,"Lista vacia");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la matricula registrada. (EjbFichaAdmisionReporte.getMatriculaInscrita)", e, null);
        }
    }

    /**
     * Obtiene el total de Fichas registradas segun el ultimo proceso de inscripcion (Registro de Fichas)
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoReporteGeneralFichas> getFichasRegistradas (List<Aspirante> aspirantes){
        try{
            if(aspirantes ==null){return ResultadoEJB.crearErroneo(2,new DtoReporteGeneralFichas(),"La lista de estudiantes no debe ser nula");}
            DtoReporteGeneralFichas dto = new DtoReporteGeneralFichas();
            dto.setTipo("Registro de Fichas");
            dto.setTotal(aspirantes.size());
            dto.setTotalSemanal((int)aspirantes.stream()
                    .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Semanal"))
                    .count());
            dto.setTotalSabatino((int)aspirantes.stream()
                    .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Sabatino"))
                    .count());
            return ResultadoEJB.crearCorrecto(dto,"Registro de Fichas");


        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener las fichas registradas. (EjbFichaAdmisionReporte.getProyeccionMatricula)", e, null);
        }
    }

    /**
     * Obtiene las fichas que han sido validadas
     * @param aspirantes  Lista de aspirantes
     * @return RE
     */
    public ResultadoEJB<DtoReporteGeneralFichas> getFichasValidadas(List<Aspirante> aspirantes){
        try{
            if(aspirantes ==null){return ResultadoEJB.crearErroneo(2,new DtoReporteGeneralFichas(),"La la lista de aspirantes no debe ser nula");}
            if(aspirantes ==null){return ResultadoEJB.crearErroneo(2,new DtoReporteGeneralFichas(),"La lista de estudiantes no debe ser nula");}
            DtoReporteGeneralFichas dto = new DtoReporteGeneralFichas();
            dto.setTipo("Fichas validadas");
            dto.setTotalSemanal((int)aspirantes.stream()
                    .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Semanal")&& x.getEstatus() == true)
                    .count());
            dto.setTotalSabatino((int)aspirantes.stream()
                    .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Sabatino")&& x.getEstatus() == true)
                    .count());
            dto.setTotal(dto.getTotalSabatino() +dto.getTotalSemanal());
            return ResultadoEJB.crearCorrecto(dto,"Registro de Fichas");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener las fichas validadas. (EjbFichaAdmisionReporte.getFichasValidadas)", e, null);
        }
    }

    /**
     * Obtiene la lista de estudiantes que se han inscrito en el ultimo periodo de inscripcion
     * periodoProcesoInscripcion --> Se obtiene de confriguracion propiedades (Cambiar anualmente)
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Estudiante>> getEstudiantesInscritos (@NonNull ProcesosInscripcion procesosInscripcion){
        try {
            if(procesosInscripcion==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El proceso de inscripción no debe ser nulo");}
            //Periodo del proceso de inscripcion programado

            List<Estudiante> estudiantesInscritos = em.createQuery("select e from Estudiante e where e.aspirante.idProcesoInscripcion.idProcesosInscripcion=:proceso and e.tipoRegistro=:tipo",Estudiante.class)
                    .setParameter("proceso",procesosInscripcion.getIdProcesosInscripcion())
                    .setParameter("tipo","Inscripción")
                    .getResultList()
                    ;
            //System.out.println("Lista --->" +estudiantesInscritos);
            if(estudiantesInscritos ==null || estudiantesInscritos.isEmpty()){
                //System.out.println("Entro  error en lista estudiantes");
                return ResultadoEJB.crearErroneo(2,estudiantesInscritos,"No existen estudiantes inscritos");}
            else {
                //System.out.println("Correcto lista de estudiantes");

                return ResultadoEJB.crearCorrecto(estudiantesInscritos,"Estudiantes inscritos");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los estudiantes inscritos. (EjbFichaAdmisionReporte.getEstudiantesInscritos)", e, null);
        }
    }

    /**
     * Obtiene la lista de aspirantes de nuevo ingreso del ultimo periodo de proceso de registro de fichas
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Aspirante>> getAspirantes(@NonNull ProcesosInscripcion procesosInscripcion){
        try{
            List<Aspirante> aspirantes = new ArrayList<>() ;
            if(procesosInscripcion==null){return ResultadoEJB.crearErroneo(2,aspirantes,"El proceso de inscripción no debe ser nulo");}
            else {
                aspirantes = em.createQuery("select a from Aspirante a where a.idProcesoInscripcion.idProcesosInscripcion=:procesoInscripcion and a.tipoAspirante.idTipoAspirante=:tipo and a.datosAcademicos<> null and a.folioAspirante <> null order by a.folioAspirante",Aspirante.class)
                        .setParameter("procesoInscripcion",procesosInscripcion.getIdProcesosInscripcion())
                        .setParameter("tipo",1)
                        .getResultList()
                ;
                if(aspirantes ==null || aspirantes.isEmpty()){return ResultadoEJB.crearErroneo(3,aspirantes,"No se encontraron aspirantes");}
                else {return ResultadoEJB.crearCorrecto(aspirantes,"Lista de aspirantes");}
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los aspirantes. (EjbFichaAdmisionReporte.getAspirantes)", e, null);
        }
    }


    /**
     * Obtiene la lista de aspirantes de nuevo ingreso del ultimo periodo de proceso de registro de fichas
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Aspirante>> getAspirantesIng(@NonNull ProcesosInscripcion procesosInscripcion){
        try{
            List<Aspirante> aspirantes = new ArrayList<>() ;
            if(procesosInscripcion==null){return ResultadoEJB.crearErroneo(2,aspirantes,"El proceso de inscripción no debe ser nulo");}
            else {
                aspirantes = em.createQuery("select a from Aspirante a where a.idProcesoInscripcion.idProcesosInscripcion=:procesoInscripcion and a.tipoAspirante.idTipoAspirante=:tipo and a.datosAcademicos<> null and a.folioAspirante <> null order by a.folioAspirante",Aspirante.class)
                        .setParameter("procesoInscripcion",procesosInscripcion.getIdProcesosInscripcion())
                        .setParameter("tipo",3)
                        .getResultList()
                ;
                if(aspirantes ==null || aspirantes.isEmpty()){return ResultadoEJB.crearErroneo(3,aspirantes,"No se encontraron aspirantes");}
                else {return ResultadoEJB.crearCorrecto(aspirantes,"Lista de aspirantes");}
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los aspirantes. (EjbFichaAdmisionReporte.getAspirantes)", e, null);
        }
    }

    /**
     * Obtiene las fichas registradas y las fichas proyectadas
     * @param aspirantes Lista de aspirantes registrados
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoReporteProyeccionFichas> getFichasR(List<Aspirante> aspirantes){
        try{
            if(aspirantes==null){return ResultadoEJB.crearErroneo(2,new DtoReporteProyeccionFichas(),"La lista de aspirantes no debe ser nula");}
            DtoReporteProyeccionFichas  dto = new DtoReporteProyeccionFichas();
            dto.setNombre("Fichas registradas");
            //Se obtienen las fichas registradas (TE)
            ResultadoEJB<DtoReporteGeneralFichas> resFichas = getFichasRegistradas(aspirantes);
            if(resFichas.getCorrecto()==true){
                dto.setRegistradas(resFichas.getValor());
                //Se obtienen las fichas proyectadas
                ResultadoEJB<DtoReporteGeneralFichas> resFichasProyectadas = getProyeccionFichas();
                if(resFichasProyectadas.getCorrecto()==true){
                    dto.setProyectadas(resFichasProyectadas.getValor());
                    return ResultadoEJB.crearCorrecto(dto,"Fichas Registradas");
                }else {
                    return ResultadoEJB.crearErroneo(4,dto,"Error aal obtener la proyeccion de fichas");
                }
            }
            else {
                return ResultadoEJB.crearErroneo(3,dto,"Ocurrio un error al obtener las fichas registradas");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener las fichas registradas. (EjbFichaAdmisionReporte.getFichasR)", e, null);
        }
    }

    /**
     * Obtiene las fichas validadas y las proyectadas
     * @param aspirantes Lista de aspirantes registradas
     * @return Resultado del proceso
     *
     */
    public ResultadoEJB<DtoReporteProyeccionFichas> getFichasValidadasR(List<Aspirante> aspirantes){
        try{
            if(aspirantes ==null){return ResultadoEJB.crearErroneo(2,new DtoReporteProyeccionFichas(),"La lista de aspirantes no debe ser nula");}
            DtoReporteProyeccionFichas dto = new DtoReporteProyeccionFichas();
            dto.setNombre("Fichas validadas");
            //Se obtienen las fichas validadas
            ResultadoEJB<DtoReporteGeneralFichas> resFichasVal = getFichasValidadas(aspirantes);
            if(resFichasVal.getCorrecto()==true){
                dto.setRegistradas(resFichasVal.getValor());
                //Se obtienen las fichas proyectadas
                ResultadoEJB<DtoReporteGeneralFichas> resProyectadas = getProyeccionFichas();
                if(resProyectadas.getCorrecto()==true){
                    dto.setProyectadas(resProyectadas.getValor());
                    return ResultadoEJB.crearCorrecto(dto,"Fichas validadas");
                }else {return ResultadoEJB.crearErroneo(5,dto,"Error al obtener las fichas proyectadas");}
            }else {return ResultadoEJB.crearErroneo(3,dto,"Error al obtener las fichas validadas");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener las fichas validadas. (EjbFichaAdmisionReporte.getFichasValidadasR)", e, null);

        }
    }

    /**
     * Obtiene la matricula inscritaa y la proyectada
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoReporteProyeccionFichas> getMatricula (@NonNull ProcesosInscripcion procesosInscripcion){
        try{
            DtoReporteProyeccionFichas dto = new DtoReporteProyeccionFichas();
            if(procesosInscripcion==null){return ResultadoEJB.crearErroneo(2,dto, "El proceso de inscripción no debe ser nulo");}
            dto.setNombre("Matricula");
            //Obtiene la lista estudiantes inscritos en el ultimo periodo de inscripcion
            ResultadoEJB<DtoReporteGeneralFichas> resMatricula =getMatriculaInscrita(procesosInscripcion);
            //System.out.println("Matricula Inscrita -> " + resMatricula.getValor());
            if(resMatricula.getCorrecto()==true){
                dto.setRegistradas(resMatricula.getValor());
                //Se obtiene la matricula proyectada
                ResultadoEJB<DtoReporteGeneralFichas>resMatriculaProyectada =getProyeccionMatricula();
                if(resMatriculaProyectada.getCorrecto()==true){
                    dto.setProyectadas(resMatriculaProyectada.getValor());
                    // System.out.println("------->"+dto);
                    return ResultadoEJB.crearCorrecto(dto,"Matricula");
                }
                else {return ResultadoEJB.crearErroneo(3,dto,"Error al obtener la matricula proyectada");}
            }else {return ResultadoEJB.crearErroneo(2,dto,"Error al obtener la matricula inscrita");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la matricula. (EjbFichaAdmisionReporte.getMatricula)", e, null);
        }
    }

    /**
     * Obtiene el reporte general
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoReporteProyeccionFichas>> getReporte(@NonNull ProcesosInscripcion procesosInscripcion) {
        try {
            if(procesosInscripcion== null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El proceso de inscripción no debe ser nulo");}
            List<DtoReporteProyeccionFichas> reporte = new ArrayList<>();
            //Obtiene la lista de aspirantes registrados
            ResultadoEJB<List<Aspirante>> resAspirantes = getAspirantes(procesosInscripcion);
            if(resAspirantes.getCorrecto()==true){
                //Obtiene el registro de fichas
                ResultadoEJB<DtoReporteProyeccionFichas> resFichasR = getFichasR(resAspirantes.getValor());
                if(resFichasR.getCorrecto()==true){
                    reporte.add(resFichasR.getValor());
                    //Obtiene las fichas validadas
                    ResultadoEJB<DtoReporteProyeccionFichas> resFichasVal = getFichasValidadasR(resAspirantes.getValor());
                    if (resFichasVal.getCorrecto()==true){
                        reporte.add(resFichasVal.getValor());
                        //Obtiene la matricula
                        ResultadoEJB<DtoReporteProyeccionFichas> resMatricula = getMatricula(procesosInscripcion);
                        if(resMatricula.getCorrecto()==true){
                            reporte.add(resMatricula.getValor());
                            return ResultadoEJB.crearCorrecto(reporte,"Reporte");
                        }else { return ResultadoEJB.crearErroneo(5,reporte,"Error al obtener la matricula"); }
                    }else { return ResultadoEJB.crearErroneo(4,reporte,"Error al obtener las fichas validadas");}
                }else {return ResultadoEJB.crearErroneo(3,reporte,"Error al obtener las fichas registradas");}
            }else {return ResultadoEJB.crearErroneo(2,reporte,"Error al obtener la lista de aspirantes");}
        }catch (Exception e){ return ResultadoEJB.crearErroneo(1, "Error al obtener el reporte (EjbFichaAdmisionReporte.getReporte)", e, null); }
    }
    /**
     * Obtiene el reporte general
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoReporteProyeccionFichas>> getReporteIng(@NonNull ProcesosInscripcion procesosInscripcion) {
        try {
            if(procesosInscripcion== null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El proceso de inscripción no debe ser nulo");}
            List<DtoReporteProyeccionFichas> reporte = new ArrayList<>();
            //Obtiene la lista de aspirantes registrados
            ResultadoEJB<List<Aspirante>> resAspirantes = getAspirantes(procesosInscripcion);
            if(resAspirantes.getCorrecto()==true){
                //Obtiene el registro de fichas
                ResultadoEJB<DtoReporteProyeccionFichas> resFichasR = getFichasR(resAspirantes.getValor());
                if(resFichasR.getCorrecto()==true){
                    reporte.add(resFichasR.getValor());
                    //Obtiene las fichas validadas
                    ResultadoEJB<DtoReporteProyeccionFichas> resFichasVal = getFichasValidadasR(resAspirantes.getValor());
                    if (resFichasVal.getCorrecto()==true){
                        reporte.add(resFichasVal.getValor());
                        //Obtiene la matricula
                        ResultadoEJB<DtoReporteProyeccionFichas> resMatricula = getMatricula(procesosInscripcion);
                        if(resMatricula.getCorrecto()==true){
                            reporte.add(resMatricula.getValor());
                            return ResultadoEJB.crearCorrecto(reporte,"Reporte");
                        }else { return ResultadoEJB.crearErroneo(5,reporte,"Error al obtener la matricula"); }
                    }else { return ResultadoEJB.crearErroneo(4,reporte,"Error al obtener las fichas validadas");}
                }else {return ResultadoEJB.crearErroneo(3,reporte,"Error al obtener las fichas registradas");}
            }else {return ResultadoEJB.crearErroneo(2,reporte,"Error al obtener la lista de aspirantes");}
        }catch (Exception e){ return ResultadoEJB.crearErroneo(1, "Error al obtener el reporte (EjbFichaAdmisionReporte.getReporte)", e, null); }
    }
    
    public ResultadoEJB<List<Persona>> consultarUsuariosContrasenias(String pista){
        try {
            List<Persona> aspirantes = em.createQuery("SELECT p FROM Persona p WHERE CONCAT(p.nombre, p.apellidoPaterno, p.apellidoMaterno, p.curp) LIKE CONCAT('%',:pista,'%') ORDER BY p.idpersona DESC", Persona.class)
                    .setParameter("pista", pista)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();
            if(!aspirantes.isEmpty()){
                return ResultadoEJB.crearCorrecto(aspirantes, "Primeros dies resultados coincidentes con la búsqueda");
            }else{
                return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, "No se han encontrado coincidencias con los resultados ingresados.");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se han podido encontrar aspirantes con los datos ingresados (EjbFichaAdmisionReporte.consultarUsuariosContrasenias)", e, null);
        }
    }
    
    public ResultadoEJB<Boolean> registroProyeccionMatricula(Integer procesoInscripcion, Short programaEducativo, Long proyeccionValidadas, Long proyeccionMatricula){
        try {
            ProyeccionAreas proyeccionAreaBD = em.createQuery("SELECT p FROM ProyeccionAreas p WHERE p.proyeccionAreasPK.procesoInscripcion = :procesoInscripcion AND p.proyeccionAreasPK.pe = :programaEducativo", ProyeccionAreas.class)
                    .setParameter("procesoInscripcion", procesoInscripcion)
                    .setParameter("programaEducativo", programaEducativo)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(proyeccionAreaBD == null){
                ProyeccionAreasPK claveProyeccion = new ProyeccionAreasPK(procesoInscripcion, programaEducativo);
                ProyeccionAreas proyeccion = new ProyeccionAreas(claveProyeccion);
                proyeccion.setProcesosInscripcion(em.find(ProcesosInscripcion.class, procesoInscripcion));
                proyeccion.setProyeccionValidadas(proyeccionValidadas.intValue());
                proyeccion.setProyeccionMatricula(proyeccionMatricula.intValue());
                em.persist(proyeccion);
                proyeccionAreaBD = em.createQuery("SELECT p FROM ProyeccionAreas p WHERE p.proyeccionAreasPK.procesoInscripcion = :procesoInscripcion AND p.proyeccionAreasPK.pe = :programaEducativo", ProyeccionAreas.class)
                    .setParameter("procesoInscripcion", procesoInscripcion)
                    .setParameter("programaEducativo", programaEducativo)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            }else{
                proyeccionAreaBD.setProyeccionMatricula(proyeccionMatricula.intValue());
                proyeccionAreaBD.setProyeccionValidadas(proyeccionValidadas.intValue());
                em.merge(proyeccionAreaBD);
            }
            
            if(proyeccionAreaBD != null){
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "El regisro se ha guardado correctamente");
            }else{
                return ResultadoEJB.crearErroneo(2, null, "No se ha podido guardar ó actualizar la proyección del área seleccionada");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido crear o modificar el registro de la proyeccion de matricula (EjbFichaAdmisionReporte.registroProyeccionMatricula)", e, null);
        }
    }
}
