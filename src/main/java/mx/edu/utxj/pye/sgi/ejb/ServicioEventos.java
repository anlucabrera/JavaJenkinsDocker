/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.StoredProcedureQuery;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.Permisos;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless
public class ServicioEventos implements EjbEvento {

    @EJB
    Facade f;

    @Override
    public List<Evaluaciones> getEvaluaciones() {
        StoredProcedureQuery q = f.getEntityManager().createStoredProcedureQuery("obtener_lista_evaluaciones", Evaluaciones.class);
        List<Evaluaciones> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    @Override
    public List<Evaluaciones360> getEvaluaciones360() {
        StoredProcedureQuery q = f.getEntityManager().createStoredProcedureQuery("obtener_lista_evaluaciones_360", Evaluaciones360.class);
        List<Evaluaciones360> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    @Override
    public List<DesempenioEvaluaciones> getEvaluacionesDesempenio() {
        StoredProcedureQuery q = f.getEntityManager().createStoredProcedureQuery("obtener_lista_evaluaciones_desempenio", DesempenioEvaluaciones.class);
        List<DesempenioEvaluaciones> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    @Override
    public List<Eventos> getEventos() {
        StoredProcedureQuery q = f.getEntityManager().createStoredProcedureQuery("obtener_lista_eventos", Eventos.class);
        List<Eventos> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    @Override
    public Permisos editarPermisos(Permisos permiso) {
        permiso = f.getEntityManager().find(Permisos.class, permiso.getPermiso());
        if (permiso.getActivo()) {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEventos.editarPermisos() EJB se desactiva");
            permiso.setActivo(false);
        } else {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEventos.editarPermisos() EJB se activa");
            permiso.setActivo(true);
        }
        f.edit(permiso);
        f.getEntityManager().flush();
        return permiso;
    }
}
