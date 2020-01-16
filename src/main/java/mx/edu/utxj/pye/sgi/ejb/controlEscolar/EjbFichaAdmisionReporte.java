package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.entity.logueo.Areas;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.enums.TramiteTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

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


}
