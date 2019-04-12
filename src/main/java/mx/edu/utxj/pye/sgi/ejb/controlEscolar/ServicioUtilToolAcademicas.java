/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.facade.controlEscolar.FacadeCE;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 *
 * @author UTXJ
 */
@Stateless
public class ServicioUtilToolAcademicas implements EjbUtilToolAcademicas {

    @EJB FacadeCE facadeCE;

    @Override
    public void guardaGrupo(Grupo grupo,Integer noGrupos) {
        Integer noGruposRegistrados = 0;

        noGruposRegistrados =
                facadeCE.getEntityManager().createQuery("SELECT g FROM Grupo g WHERE g.idPe = :id_Pe AND g.periodo = :idPeriodo AND g.grado = :grado")
                .setParameter("id_Pe", grupo.getIdPe())
                .setParameter("idPeriodo", 50)
                .setParameter("grado", grupo.getGrado())
                .getResultList().size();
                
        Integer noAcumulado = noGruposRegistrados + noGrupos;

        Character[] abecedario = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','U','V','W','X','Y','Z'};
        for(int i = noGruposRegistrados; i < noAcumulado;i ++) {
            Grupo grupoNew = new Grupo();
            grupoNew.setIdPe(grupo.getIdPe());
            grupoNew.setGrado(grupo.getGrado());
            grupoNew.setPeriodo(50);
            grupoNew.setCapMaxima(grupo.getCapMaxima());
            grupoNew.setIdSistema(grupo.getIdSistema());
            grupoNew.setLiteral((abecedario[i]));
            facadeCE.create(grupoNew);
        }
    }

    @Override
    public List<Grupo> listaByPeriodo(Integer cve_periodo) {
        return facadeCE.getEntityManager().createQuery("SELECT g FROM Grupo g WHERE g.periodo = :periodo",Grupo.class)
                .setParameter("periodo", cve_periodo)
                .getResultList();
    }
}
