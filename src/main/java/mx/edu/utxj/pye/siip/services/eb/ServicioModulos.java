/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.eb;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistroUsuario;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;

@Stateful
/**
 * Servicio para mostrar a los usuario los ejes que tienen habilitados
 */
public class ServicioModulos implements EjbModulos {

    @EJB
    Facade facadeEscolar;
    
    /*Administrador de entidades*/
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    /**
     *
     * @param clavepersonal
     * @return
     * @throws Throwable
     */
    @Override
    public List<ModulosRegistroUsuario> getEjesRectores(Integer clavepersonal) throws Throwable {
        TypedQuery<ModulosRegistroUsuario> query = em.createQuery("SELECT mru FROM ModulosRegistroUsuario mru WHERE mru.clavePersonal = :clavepersonal AND mru.estado = :estado GROUP BY mru.claveEje ORDER BY mru.claveEje", ModulosRegistroUsuario.class);
        query.setParameter("clavepersonal", clavepersonal);
        query.setParameter("estado", "1");
        List<ModulosRegistroUsuario> lista = query.getResultList();
        return lista;
    }

    /**
     *
     * @param eje
     * @param clavepersonal
     * @return
     * @throws Throwable
     */
    @Override
    public List<ModulosRegistroUsuario> getModulosRegistroUsuario(Integer eje, Integer clavepersonal) throws Throwable {
        TypedQuery<ModulosRegistroUsuario> query = em.createQuery("SELECT mru FROM ModulosRegistroUsuario mru WHERE mru.claveEje = :eje AND mru.clavePersonal = :clavepersonal AND mru.estado = :estado ORDER BY mru.claveEje", ModulosRegistroUsuario.class);
        query.setParameter("eje", eje);
        query.setParameter("clavepersonal", clavepersonal);
        query.setParameter("estado", "1");
        List<ModulosRegistroUsuario> lista = query.getResultList();
        return lista;
    }

    @Override
    public Registros getRegistro(RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
        Registros registro = new Registros();
        facadeEscolar.setEntityClass(Registros.class);
        registro.setTipo(registrosTipo);
        registro.setEje(ejesRegistro);
        registro.setArea(area);
        registro.setFechaRegistro(new Date());
        registro.setEventoRegistro(eventosRegistros);
        facadeEscolar.create(registro);
        facadeEscolar.flush();
        return registro;
    }

    @Override
    public EventosRegistros getEventoRegistro() {
        TypedQuery<EventosRegistros> query = em.createQuery("SELECT er FROM EventosRegistros er WHERE (er.fechaInicio <= :fechaInicio AND er.fechaFin >= :fechaFin)", EventosRegistros.class);
        query.setParameter("fechaInicio", new Date());
        query.setParameter("fechaFin", new Date());
        EventosRegistros eventoRegistro = query.getSingleResult();
        return eventoRegistro;
    }

    @Override
    public Boolean validaPeriodoRegistro(PeriodosEscolares periodoEscolar, Integer periodoRegistro) {
        if(!Objects.equals(periodoEscolar.getPeriodo(), periodoRegistro)){
            return true;
        }else{    
            return false;
        }
    }

}
