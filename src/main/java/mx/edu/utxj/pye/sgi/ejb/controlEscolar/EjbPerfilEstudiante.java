package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import mx.edu.utxj.pye.sgi.controlador.controlEscolar.PerfilEstudiante;
import mx.edu.utxj.pye.sgi.controladores.ch.PersonalAdmin;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CambioPwdRolEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.PerfilRolEstudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.titulacion.DatosContacto;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.rmi.server.ExportException;
import java.security.cert.TrustAnchor;

@Stateless(name = "EjbPerfilEstudiante")
public class EjbPerfilEstudiante {

    @EJB Facade f;
    @EJB EjbEstudianteBean ejbEstudianteBean;
    private EntityManager em;
    @Inject PersonalAdmin admin;
    @PostConstruct
    public void init(){em = f.getEntityManager();}

    /**
     * Valida que el usuario logueado sea un estudiante activo
     * @param matricula
     * @return
     */
    public ResultadoEJB<Estudiante> validaEstudiante(Integer matricula){
        try{
            Estudiante e = em.createQuery("select e from Estudiante as e where e.matricula = :matricula", Estudiante.class).setParameter("matricula", matricula)
                    .getResultStream().findFirst().orElse(new Estudiante());
            return ResultadoEJB.crearCorrecto(e, "El usuario ha sido comprobado como estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El estudiante no se pudo validar. (EjbPerfilEstudiante.validadEstudiante)", e, null);
        }
    }

    /**
     * Cambia la contraseña del estudiante
     * @param rol
     * @return Resultado del proceso
     */
    public  ResultadoEJB<Login> cambiaPwd( CambioPwdRolEstudiante rol){
        try{
            Login login = new Login();
            if(rol.getEstudiante() ==null){return ResultadoEJB.crearErroneo(2,login,"El estudiante no debe ser nulo");}
            login = rol.getEstudiante().getAspirante().getIdPersona().getLogin();
            login.setPassword(rol.getPwdNuevaEncript());
            login.setModificado(true);
            em.merge(login);
            return ResultadoEJB.crearCorrecto(login,"Se ha cambiado la contraseña");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la contraseña. (EjbPerfilEstudiante.cambiaPwd)", e, null);
        }
    }

    public ResultadoEJB<CambioPwdRolEstudiante> comprobarPwd (CambioPwdRolEstudiante rol){
        try{
            if(rol.getPwdActual() ==null){return ResultadoEJB.crearErroneo(2,rol,"La contraseña actual no debe ser nula");}
            if(rol.getPwdNueva()==null){return ResultadoEJB.crearErroneo(3,rol,"La nueva contraseña no debe ser nula");}
            //TODO: Se desencripta la contraseña que tiene en el login y se compara con la que el estudiante ingreso
            //System.out.println("Contraseña actua ingresada: " + rol.getPwdActual());
            String pdwDes = admin.desencriptaPassword(rol.getEstudiante().getAspirante().getIdPersona().getLogin().getPassword());
            //System.out.println("Contraseña actual desenctiptada: " + pdwDes);
            //System.out.println("Contraseña actual encriptada: " + rol.getEstudiante().getAspirante().getIdPersona().getLogin().getPassword());
            if(pdwDes.equals(rol.getPwdActual())){
               // System.out.println("Son iguales" );
                //TODO: Ecncripta la contraseña nueva
                String pwdEncript = admin.encriptaPassword(rol.getPwdNueva());
                rol.setPwdNuevaEncript(pwdEncript);
                //System.out.println("Contraseña nueva descriptada: " + pwdEncript );
                return ResultadoEJB.crearCorrecto(rol, "La contraseña coincide");
            }else {return ResultadoEJB.crearErroneo(4,rol,"La contraseña actual no es la correcta");}
        }catch (Exception e ){
            return ResultadoEJB.crearErroneo(1, "No se pudo comprobar las contraseñas. (EjbPerfilEstudiante.comprobarPwd)", e, null);
        }
    }

    /**
     * Actualiza los datos familiares del estudiante
     * @param rol
     * @return Resultado del proceso
     */
    public ResultadoEJB<DatosFamiliares> updateDatosFamiliares(PerfilRolEstudiante rol){
        try{
            DatosFamiliares datosFamiliares = new DatosFamiliares();
            if(rol.getEstudiante()==null){return ResultadoEJB.crearErroneo(2,datosFamiliares,"El estudiante no debe ser nulo");}
            else {
                datosFamiliares = rol.getDatosFamiliares();
                em.merge(datosFamiliares);
                return ResultadoEJB.crearCorrecto(datosFamiliares,"Datos familiares actualizados");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar los datos familiares del estudiante. (EjbPerfilEstudiante.updateDatosFamiliares)", e, null);
        }
    }

    /**
     * Actualiza los datos de contacto del estudinte
     * @param rol
     * @return
     */
    public ResultadoEJB<MedioComunicacion> updateDatosContacto(PerfilRolEstudiante rol){
        try{
            MedioComunicacion medioComunicacion = new MedioComunicacion();
            if(rol.getEstudiante()==null){return  ResultadoEJB.crearErroneo(2,medioComunicacion,"El estudiante no debe ser nulo");}
            else {
                medioComunicacion = rol.getMedioComunicacion();
                em.merge(medioComunicacion);
                return  ResultadoEJB.crearCorrecto(medioComunicacion,"Medios de comunicación del estudiante actualizados con éxito.");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar los medio de comunicación del estudiante. (EjbPerfilEstudiante.updateDatosContacto)", e, null);
        }
    }

    /**
     * Actualiza los datos del tutor familiar
     * @param rol
     * @return
     */
    public ResultadoEJB<TutorFamiliar> upadateTutorFam (PerfilRolEstudiante rol){
        try{
            TutorFamiliar tutorFamiliar = new TutorFamiliar();
            if(rol.getEstudiante()==null){return ResultadoEJB.crearErroneo(2,tutorFamiliar,"El estudiante no debe ser nulo");}
            else {
                tutorFamiliar = rol.getTutorFamiliar();
                em.merge(tutorFamiliar);
                return ResultadoEJB.crearCorrecto(tutorFamiliar,"Datos del tutor familiar del estudiante actualizados con éxito");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar los medio de comunicación del estudiante. (EjbPerfilEstudiante.upadateTutorFam)", e, null);
        }
    }

    public ResultadoEJB<PerfilRolEstudiante> upadateDatosEstudiante(PerfilRolEstudiante rol){
        try{
            if(rol.getEstudiante() ==null){return ResultadoEJB.crearErroneo(2,rol,"El estudiante no debe ser nulo");}
            //Todo: actualiza medios de comunicacion del estudiante
            ResultadoEJB<MedioComunicacion> resMedioComunicacion = updateDatosContacto(rol);
            //Todo: actualizar datos familiares del estudiante
            ResultadoEJB<DatosFamiliares> resDatosFam= updateDatosFamiliares(rol);
            //Todo: actualizar datos del tutor del estudiante
            ResultadoEJB<TutorFamiliar> resTutor= upadateTutorFam(rol);
            if(!resMedioComunicacion.getCorrecto()==true){return ResultadoEJB.crearErroneo(3,rol,"No se pudieron actualizar los datos de contacto del estudiante");}
            if(!resDatosFam.getCorrecto()== true){return ResultadoEJB.crearErroneo(4,rol,"No se pudieron actualizar los datos familiares del estudiante.");}
            if(!resTutor.getCorrecto()==true){return ResultadoEJB.crearErroneo(5,rol,"No se pudo actualizar los datos del tutor familiar del estudiante.");}
            return ResultadoEJB.crearCorrecto(rol,"Datos del estudiante actualizados corectamente");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar los medio de comunicación del estudiante. (EjbPerfilEstudiante.upadateTutorFam)", e, null);
        }


    }



}
