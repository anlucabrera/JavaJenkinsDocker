package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import mx.edu.utxj.pye.sgi.controladores.ch.PersonalAdmin;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CedulaIdentificacionRolSE;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Login;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.Encrypted;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Este ejb servirá para funciones exclusivas de Servicios escolares (Carta compromiso, reseteo de contraseña, Comprobante de estudios)
 * @author Taatisz :)
 */

@Stateless(name = "EjbAdminEstudiantesSE")
public class EjbAdministracionEstudiantesSE {
    @EJB Facade f;
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @Inject PersonalAdmin admin;
    private EntityManager em;

    @PostConstruct
    public void init(){em = f.getEntityManager();}

    /**
     * Verifica que el personal logueado sea de Servicios Escolares
     * @param clave Clave del trabajador logueado
     * @return Resultado del Proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validaSE(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalAreaOperativa").orElse(10)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbAdministracionEstudiantesSE.validaSE)", e, null);
        }

    }

    /**
     * Actualiza la contraseña del estudiante(Se le asigna una contraseña aleatorea)
     * @param rol
     * @return
     */
    public ResultadoEJB<Login> updatePwdSE (CedulaIdentificacionRolSE rol){
        try{
            Login login = new Login();
            if(rol.getEstudiante()==null){return  ResultadoEJB.crearErroneo(2,login,"El estudiante no debe ser nulo");}
            if(rol.getPwdNueva()==null){return  ResultadoEJB.crearErroneo(3,login,"La nueva contraseña no debe ser nula");}
            login = rol.getEstudiante().getAspirante().getIdPersona().getLogin();
            //TODO: Encripta la contraseña generada
            String pdwEncript= encriptaPassword(rol.getPwdNueva());
            login.setPassword(pdwEncript);
            login.setModificado(false);
            em.merge(login);
            return  ResultadoEJB.crearCorrecto(login,"Se ha cambiado la contraseña la contraseña.");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo restaurar la contraseña del estudiante(EjbAdministracionEstudiantesSE.updatePwdSE)", e, null);
        }
    }

    public  String encriptaPassword(String password) throws Exception{
        String contraseñaEncriptada = "";
        String key = "92AE31A79FEEB2A3";
        String iv = "0123456789ABCDEF";
        contraseñaEncriptada = Encrypted.encrypt(key, iv, password);

        return contraseñaEncriptada;
    }





}
