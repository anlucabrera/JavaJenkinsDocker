/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.facade.controlEscolar.FacadeCE;

/**
 *
 * @author UTXJ
 */
@Stateless
public class ServiceProcesoInscripcion implements EjbProcesoInscripcion {

    @EJB
    FacadeCE facadeCE;

    @Override
    public List<Aspirante> listaAspirantesTSU(Integer procesoInscripcion) {
        return  facadeCE.getEntityManager().createQuery("SELECT a FROM Aspirante a WHERE a.idProcesoInscripcion.idProcesosInscripcion = :idpi AND a.tipoAspirante.idTipoAspirante = 1 ORDER BY a.folioAspirante ",Aspirante.class)
                .setParameter("idpi", procesoInscripcion)
                .getResultList();
    }

    @Override
    public List<Aspirante> lisAspirantesByPE(String pe,Integer procesoInscripcion) {
        return facadeCE.getEntityManager().createQuery("SELECT a FROM Aspirante a WHERE a.idProcesoInscripcion.idProcesosInscripcion = :idpi AND a.tipoAspirante.idTipoAspirante = 1 AND a.datosAcademicos.primeraOpcion = :po ORDER BY a.folioAspirante ",Aspirante.class)
                .setParameter("idpi", procesoInscripcion)
                .setParameter("po", pe)
                .getResultList();
    }

    @Override
    public Aspirante buscaAspiranteByFolio(Integer folio) {
        return facadeCE.getEntityManager().createNamedQuery("Aspirante.findByFolioAspirante", Aspirante.class)
                .setParameter("folioAspirante", folio)
                .getResultList()
                .stream().findFirst().orElse(null);
    }
}
