/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.eb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.enums.EvidenciaCategoria;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbEvidenciasAlineacion;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioEvidenciasAlineacion implements EjbEvidenciasAlineacion{
  
    @EJB EjbModulos ejbModulos;
    @EJB EjbAdministracionEncuestas eJBAdministracionEncuestas;
    @EJB Facade f;
    @EJB EjbPropiedades ep;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @Inject Caster caster; 
    @Inject ControladorEmpleado controladorEmpleado;
   
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    @Override
    public List<Integer> buscaRegistroEvidenciasRegistro(Integer registro) throws Throwable {
        List<Integer> registros = new ArrayList<>();
        try {
            registros = em.createQuery("SELECT e FROM Evidencias e INNER JOIN e.registrosList r WHERE r.registro = :registro",  Evidencias.class)
                    .setParameter("registro", registro)
                    .getResultStream()
                    .map(s -> s.getEvidencia())
                    .collect(Collectors.toList());
                               
            return registros;
            
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<EvidenciasDetalle> getListaEvidenciasPorRegistro(Integer registro) {
                
        if(registro == null){
            return Collections.EMPTY_LIST;
        }
        List<EvidenciasDetalle> l = f.getEntityManager().createQuery("SELECT e FROM Evidencias e INNER JOIN e.registrosList r INNER JOIN e.evidenciasDetalleList ed WHERE r.registro=:registro ORDER BY ed.mime, ed.ruta", Evidencias.class)
                .setParameter("registro", registro)
                .getResultStream()
                .map(e -> e.getEvidenciasDetalleList())
                .flatMap(ed -> ed.stream())
                .distinct()
                .collect(Collectors.toList());
        return l;
    }

    @Override
    public Map.Entry<Boolean, Integer> registrarEvidenciasARegistro(Integer registro, List<Part> archivos, EventosRegistros eventosRegistros, RegistrosTipo registrosTipo) {
         Map<Boolean, Integer> map = new HashMap<>();

        if(registro == null || archivos == null || archivos.isEmpty() || eventosRegistros == null || registrosTipo == null){
            map.put(Boolean.FALSE, 0);
            return map.entrySet().iterator().next();
        }
        final List<Boolean> res = new ArrayList<>();
        Evidencias evidencias = new Evidencias(0, archivos.size() > 1?EvidenciaCategoria.MULTIPLE.getLabel():EvidenciaCategoria.UNICA.getLabel());
        f.create(evidencias);
        f.flush();
        f.refresh(evidencias);

        AreasUniversidad areaPOA = ejbFiscalizacion.getAreaConPOA((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
        RegistrosTipo tipo = f.getEntityManager().find(RegistrosTipo.class, registrosTipo.getRegistroTipo());
        f.refresh(tipo);
        archivos.forEach(archivo -> {
            try{
                String rutaAbsoluta = ServicioArchivos.almacenarEvidenciaRegistroSII(areaPOA, registro, archivo, eventosRegistros, tipo);
                EvidenciasDetalle ed = new EvidenciasDetalle(0, rutaAbsoluta, archivo.getContentType(), archivo.getSize(), eventosRegistros.getMes());
                evidencias.getEvidenciasDetalleList().add(ed);
                ed.setEvidencia(evidencias);
                f.create(ed);
                f.flush();
                f.refresh(ed);
                res.add(true);
            }catch(Exception e){
                res.add(Boolean.FALSE);
                LOG.log(Level.SEVERE, "No se guardó el archivo: " + archivo.getSubmittedFileName(), e);
            }
        });

        Long correctos = res.stream().filter(r -> r).count();
        Long incorrectos = res.stream().filter(r -> !r).count();

        if(correctos == 0){
            f.remove(evidencias);
            f.flush();
        }else{
            Registros r = f.getEntityManager().createQuery("SELECT r FROM Registros r WHERE r.registro = :registro", Registros.class)
            .setParameter("registro", registro)
            .getSingleResult();
            r.getEvidenciasList().add(evidencias);
            evidencias.getRegistrosList().add(r);
            f.edit(r);
            f.edit(evidencias);
            f.flush();
            f.refresh(r);
        }

        map.put(incorrectos == 0, correctos.intValue());
        return map.entrySet().iterator().next();
    }
    private static final Logger LOG = Logger.getLogger(ServicioEvidenciasAlineacion.class.getName());

    @Override
    public Boolean eliminarEvidenciaEnRegistro(Integer registro, EvidenciasDetalle evidenciasDetalle) {
         if (registro == null || evidenciasDetalle == null) {
            return false;
        }

        Integer id = evidenciasDetalle.getDetalleEvidencia();

        try {
            ServicioArchivos.eliminarArchivo(evidenciasDetalle.getRuta());

            Evidencias evidencias = evidenciasDetalle.getEvidencia();
            Integer total = evidencias.getEvidenciasDetalleList().size();
            evidencias.getEvidenciasDetalleList().remove(evidenciasDetalle);
            f.remove(evidenciasDetalle);
            f.edit(evidencias);
            f.flush();

            if(total == 1){
                f.remove(evidencias);
                f.flush();
            }else if(total == 2){
                evidencias.setCategoria(EvidenciaCategoria.UNICA.getLabel());
                f.edit(evidencias);
                f.flush();
                f.getEntityManager().detach(evidencias);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se eliminó la evidencia: " + evidenciasDetalle.getRuta(), e);
            return false;
        }

        return f.getEntityManager().find(EvidenciasDetalle.class, id) == null;
    }

    @Override
    public ActividadesPoa getActividadAlineada(Integer registro) {
       List<ActividadesPoa> l =f.getEntityManager().createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.registrosList r INNER JOIN a.cuadroMandoInt cm INNER JOIN FETCH cm.lineaAccion INNER JOIN FETCH cm.estrategia INNER JOIN FETCH cm.eje WHERE r.registro = :registro", ActividadesPoa.class)
                .setParameter("registro", registro)
                .getResultList();

        if(!l.isEmpty()) return l.get(0);
        else return null;
    }

    @Override
    public Boolean alinearRegistroActividad(ActividadesPoa actividad, Integer registro) {
        try{
            ActividadesPoa a = f.getEntityManager().find(ActividadesPoa.class, actividad.getActividadPoa());
            Registros r = f.getEntityManager().find(Registros.class, registro);

            eliminarAlineacion(registro);

            a.getRegistrosList().add(r);
            r.getActividadesPoaList().add(actividad);
            f.flush();

            return true;
        }catch(Exception e){
            LOG.log(Level.SEVERE, "No se pudo alinear el registro con la actividad.", e);
            return false;
        }
    }

    @Override
    public Boolean eliminarAlineacion(Integer registro) {
         try{
            Registros r = f.getEntityManager().find(Registros.class, registro);

            if(!r.getActividadesPoaList().isEmpty()){
                ActividadesPoa a2 = f.getEntityManager().find(ActividadesPoa.class, r.getActividadesPoaList().get(0).getActividadPoa());
                a2.getRegistrosList().remove(r);
                r.getActividadesPoaList().remove(a2);
                f.flush();
//                registro.setActividadAlineada(null);
            }
            
            return true;
        }catch(Exception e){
            LOG.log(Level.SEVERE, "No se pudo alinear el registro con la actividad.", e);
            return false;
        }
    }
}
