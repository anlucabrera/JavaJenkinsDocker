package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.List;
import javax.annotation.PostConstruct;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CordinadoresTutores;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateless
public class EjbValidacionRol {
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    
    @EJB Facade f;
    private EntityManager em;

    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    /**
     * Permite validar si el usuario logueado es un docente
     * @param clave Número de nómina del usuario logueado
     * @return Regresa la instancia del personal si es que cumple con ser docente o codigo de error de lo contrario
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDocente(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
                filtro.setEntity(p);
//            filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.ACTIIVIDAD.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalDocenteActividad").orElse(3)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como un docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El docente no se pudo validar. (EjbValidacionRol.validarDocente)", e, null);
        }
    }

    /**
     * Permite crear el filtro para validar si el usuario autenticado es un director de área académica
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDirector(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_SUPERIOR.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorAreaSuperior").orElse(2)));
            filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorCategoriaOperativa").orElse(18)));
            return ResultadoEJB.crearCorrecto(filtro, "El filtro del usuario ha sido preparado como un director.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El director no se pudo validar. (EjbValidacionRol.validarDirector)", e, null);
        }
    }

    /**
     * Permite crear el filtro para validar si el usuario autenticado es un director de área académica
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarSecretariaAcademica(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("secretarioAcademicoAreaOperativa").orElse(2)));
            filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("secretarioAcademicoCategoriaOperativa").orElse(18)));
            return ResultadoEJB.crearCorrecto(filtro, "El filtro del usuario ha sido preparado como Secretario/a Academico/a.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El director no se pudo validar. (EjbValidacionRol.validarSecretariaAcademica)", e, null);
        }
    }
    
    
    /**
     * Permite crear el filtro para validar si el usuario autenticado es un encarcado de dirección de área académica
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarEncargadoDireccion(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_SUPERIOR.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorAreaSuperior").orElse(2)));
            filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorEncargadoCategoriaOperativa").orElse(48)));
            return ResultadoEJB.crearCorrecto(filtro, "El filtro del usuario ha sido preparado como un encargado de dirección.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El encargado de dirección de área académica no se pudo validar. (EjbValidacionRol.validarDirector)", e, null);
        }
    }

    public ResultadoEJB<Filter<PersonalActivo>> validarTutor(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.TUTOR.getLabel(), "");

            return ResultadoEJB.crearCorrecto(filtro, "El filtro del usuario ha sido preparado como un encargado de dirección.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El encargado de dirección de área académica no se pudo validar. (EjbValidacionRol.validarDirector)", e, null);
        }
    }

    public ResultadoEJB<Filter<PersonalActivo>> validarJefeDepartamento(Integer clave, Integer areaOperativa){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalAreaOperativa").orElse(areaOperativa)));
            filtro.addParam(PersonalFiltro.AREA_SUPERIOR.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorAreaSuperior").orElse(2)));
            filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("jefePersonalCategoria").orElse(24)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbValidacionRol.validarServiciosEscolares)", e, null);
        }
    }

    public ResultadoEJB<Filter<PersonalActivo>> validarencargadoDepartamento(Integer clave, Integer areaOperativa){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalAreaOperativa").orElse(areaOperativa)));
            filtro.addParam(PersonalFiltro.AREA_SUPERIOR.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorAreaSuperior").orElse(2)));
            filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("encargadoPersonalCategoria").orElse(20)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbValidacionRol.validarencargadoDepartamento)", e, null);
        }
    }
    
    public ResultadoEJB<Estudiante> validarEstudiante(Integer matricula){        
        try {
            List<Estudiante> e = em.createQuery("SELECT e FROM Estudiante AS e WHERE e.matricula = :matricula ORDER BY e.periodo DESC", Estudiante.class)
                    .setParameter("matricula", matricula)
                    .getResultList();
            if (!e.isEmpty()) {
                return ResultadoEJB.crearCorrecto(e.get(0), "El usuario ha sido comprobado como estudiante.");
            } else {
                return ResultadoEJB.crearErroneo(2, "No se ha podido validar el estudiante", Estudiante.class);
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El estudiante no se pudo validar. (EjbValidacionRol.validarEstudiante)", e, null);
        }
    }

    public ResultadoEJB<Filter<PersonalActivo>> validarTienePOA(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filter = new Filter<>();
            filter.setEntity(p);
            filter.addParam(PersonalFiltro.TIENE_POA.getLabel(), clave.toString());
            return ResultadoEJB.crearCorrecto(filter, "El usuario ha sido preparado para validar si tiene POA");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar como directivo con POA.", e, null);
        }
    }
    
    public ResultadoEJB<Filter<PersonalActivo>> validarCoordinadorTutor(Integer clave){
        try{
            List<CordinadoresTutores> ct = em.createQuery("SELECT c FROM CordinadoresTutores c WHERE c.personal = :clave", CordinadoresTutores.class)
                    .setParameter("clave", clave)
                    .getResultList();
            if(!ct.isEmpty()){
                PersonalActivo p = ejbPersonalBean.pack(clave);
                Filter<PersonalActivo> filtro = new Filter<>();
                filtro.setEntity(p);
                return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido identificado como Coordinador de Tutores");
            }else{
                return ResultadoEJB.crearErroneo(2, null,"El usuario no ha sido en ninguna ocasión Coordinador de Tutores");
            }
        }catch(Exception e){
            return ResultadoEJB.crearErroneo(1, "No se ha podido comprobar si el usuario es o ha sido Coordinador de Tutores. (EjbValidacionRol.validarCoordinadorTutor)", e, null);
        }
    }
    
    public ResultadoEJB<Filter<PersonalActivo>> validarPsicopedagogia(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalPsicopedagogia").orElse(18)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal del área de psicopedagogía.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal del área no se pudo validar. (EjbValidacionRol.validarPsicopedagogia)", e, null);
        }
    }
    
}
