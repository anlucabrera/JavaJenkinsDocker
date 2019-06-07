/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.util.ArrayList;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.facade.controlEscolar.FacadeCE;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Login;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.util.Encrypted;

/**
 *
 * @author UTXJ
 */
@Stateless
public class ServicioUtilToolAcademicas implements EjbUtilToolAcademicas {

    @EJB FacadeCE facadeCE;

    @Override
    public void guardaGrupo(Grupo grupo,Integer noGrupos,Integer periodo,PlanEstudio planEstudio) {
        Integer noGruposRegistrados = 0;
        
        noGruposRegistrados =
                facadeCE.getEntityManager().createQuery("SELECT g FROM Grupo g WHERE g.idPe = :id_Pe AND g.periodo = :idPeriodo AND g.grado = :grado")
                .setParameter("id_Pe", grupo.getIdPe())
                .setParameter("idPeriodo", periodo)
                .setParameter("grado", grupo.getGrado())
                .getResultList().size();
                
        Integer noAcumulado = noGruposRegistrados + noGrupos;

        Character[] abecedario = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','U','V','W','X','Y','Z'};
        for(int i = noGruposRegistrados; i < noAcumulado;i ++) {
            Grupo grupoNew = new Grupo();
            List<CargaAcademica> cas = new ArrayList<>();
            grupoNew.setIdPe(grupo.getIdPe());
            grupoNew.setGrado(grupo.getGrado());
            grupoNew.setPeriodo(periodo);
            grupoNew.setCapMaxima(grupo.getCapMaxima());
            grupoNew.setIdSistema(grupo.getIdSistema());
            grupoNew.setLiteral((abecedario[i]));
            facadeCE.create(grupoNew);
            facadeCE.flush();
            facadeCE.setEntityClass(Grupo.class);
            facadeCE.edit(grupoNew);
            facadeCE.flush();
        }
    }

    @Override
    public List<Grupo> listaByPeriodo(Integer cve_periodo) {
        return facadeCE.getEntityManager().createQuery("SELECT g FROM Grupo g WHERE g.periodo = :periodo",Grupo.class)
                .setParameter("periodo", cve_periodo)
                .getResultList();
    }

    @Override
    public void actualizaGrupo(Grupo grupo) {
        facadeCE.setEntityClass(Grupo.class);
        facadeCE.edit(grupo);
        facadeCE.flush();
    }

    @Override
    public void eliminaGrupo(Grupo grupo) {
        facadeCE.remove(grupo);
        facadeCE.flush();
    }

    @Override
    public Login autenticarUser(String usuario, String password) {
        String encripta = "";
        String key = "92AE31A79FEEB2A3"; 
        String iv = "0123456789ABCDEF";
        try {
            encripta = Encrypted.encrypt(key, iv, password);
        } catch (Exception ex) {
            Logger.getLogger(ServicioUtilToolAcademicas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return facadeCE.getEntityManager().createQuery("SELECT l FROM Login l WHERE l.usuario = :user AND l.password = :pass", Login.class)
                .setParameter("user", usuario)
                .setParameter("pass", encripta)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<Grupo> listaByPeriodoCarrera(Short carrera, Integer periodo) {
        return facadeCE.getEntityManager().createQuery("SELECT g FROM Grupo g WHERE g.periodo = :idPeriodo AND g.idPe = :carrera", Grupo.class)
                .setParameter("idPeriodo", periodo)
                .setParameter("carrera", carrera)
                .getResultList();
    }

    @Override
    public List<PlanEstudio> listarPlanesXCarrera(Short carrera) {
        return facadeCE.getEntityManager().createQuery("SELECT plan FROM PlanEstudio plan WHERE plan.idPe = :area AND plan.estatus = 1", PlanEstudio.class)
                .setParameter("area", carrera)
                .getResultList();
    }

    @Override
    public List<Estudiante> getEstudianteXMatricula(String matricula) {
        return facadeCE.getEntityManager().createQuery("SELECT e FROM Estudiante e WHERE e.matricula LIKE CONCAT('%',:stringMatricula ,'%')", Estudiante.class)
                .setParameter("stringMatricula", matricula)
                .getResultList();
    }

    @Override
    public List<Estudiante> getEstudianteXAP(String apellidoPaterno) {
        return facadeCE.getEntityManager().createQuery("SELECT e FROM Estudiante e WHERE e.aspirante.idPersona.apellidoPaterno LIKE CONCAT('%',:ap ,'%')", Estudiante.class)
                .setParameter("ap", apellidoPaterno)
                .getResultList();
    }

    @Override
    public AreasUniversidad buscaAreaByClave(Short area) {
        return facadeCE.getEntityManager().createNamedQuery("AreasUniversidad.findByArea", AreasUniversidad.class)
                .setParameter("area", area)
                .getResultList().stream().findFirst().orElse(null);
    }
}
