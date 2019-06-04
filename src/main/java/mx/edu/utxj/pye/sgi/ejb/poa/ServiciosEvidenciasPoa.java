package mx.edu.utxj.pye.sgi.ejb.poa;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.Proyectos;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.UnidadMedidas;

@Stateful
public class ServiciosEvidenciasPoa implements EjbEvidenciasPoa {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb-pye2_ejb_1.0PU")
    private EntityManager em;

    @EJB
    FacadePoa facadePoa;

    @Override
    public Evidencias agregarEvidenciases(Evidencias evidencias, ActividadesPoa actividadesPoa) {
        facadePoa.setEntityClass(Evidencias.class);
        if (!actividadesPoa.getEvidenciasList().contains(evidencias)) {
            actividadesPoa.getEvidenciasList().add(evidencias);
            facadePoa.create(evidencias);
            evidencias.getActividadesPoaList().add(actividadesPoa);
            facadePoa.edit(actividadesPoa);
            facadePoa.flush();
        }
        return evidencias;
    }

    @Override
    public EvidenciasDetalle agregarEvidenciasesEvidenciasDetalle(EvidenciasDetalle evidenciasDetalle) {
        facadePoa.setEntityClass(Evidencias.class);
        facadePoa.create(evidenciasDetalle);
        facadePoa.flush();
        return evidenciasDetalle;
    }

    @Override
    public Evidencias actualizarEvidenciases(Evidencias evidencias) {
        facadePoa.setEntityClass(Evidencias.class);
        facadePoa.edit(evidencias);
        facadePoa.flush();
        return evidencias;
    }

    @Override
    public Evidencias eliminarEvidencias(Evidencias evidencias) {
        facadePoa.setEntityClass(Evidencias.class);
        facadePoa.remove(evidencias);
        facadePoa.flush();
        return evidencias;
    }

    @Override
    public EvidenciasDetalle eliminarEvidenciasDetalle(EvidenciasDetalle evidenciasDetalle) {
        facadePoa.setEntityClass(EvidenciasDetalle.class);
        facadePoa.remove(evidenciasDetalle);
        facadePoa.flush();
        return evidenciasDetalle;
    }

    @Override
    public List<Evidencias> mostrarEvidenciasesRegistros(ActividadesPoa actividad, List<Registros> registroses) {
        List<Evidencias> lev = new ArrayList<>();
        lev.clear();

        TypedQuery<Evidencias> q = em.createQuery("SELECT e FROM Evidencias e INNER JOIN e.actividadesPoaList a WHERE a.actividadPoa=:actividadPoa", Evidencias.class);
        q.setParameter("actividadPoa", actividad.getActividadPoa());
        List<Evidencias> pr = q.getResultList();
        if (!pr.isEmpty()) {
            lev.addAll(pr);
        }
        if (!registroses.isEmpty()) {
            registroses.forEach((t) -> {
                TypedQuery<Evidencias> p = em.createQuery("SELECT e FROM Evidencias e INNER JOIN e.registrosList r WHERE r.registro=:registro", Evidencias.class);
                p.setParameter("registro", t.getRegistro());
                List<Evidencias> prr = p.getResultList();
                if (!prr.isEmpty()) {
                    lev.addAll(prr);
                }
            });
        }
        return lev;
    }

    @Override
    public List<EvidenciasDetalle> mostrarEvidenciases(Evidencias evidencias) {
        TypedQuery<EvidenciasDetalle> q = em.createQuery("SELECT d FROM EvidenciasDetalle d INNER JOIN d.evidencia e WHERE e.evidencia = :evidencia", EvidenciasDetalle.class);
        q.setParameter("evidencia", evidencias.getEvidencia());
        List<EvidenciasDetalle> pr = q.getResultList();
        return pr;
    }

}
