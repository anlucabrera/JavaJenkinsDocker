/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import mx.edu.utxj.pye.sgi.entity.titulacion.FechasDocumentos;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.titulacion.ExpedientesTitulacion;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.titulacion.dto.dtoFechasDocumentos;
import mx.edu.utxj.pye.titulacion.interfaces.EjbFechasDocumentos;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServiceFechasDocumentos implements EjbFechasDocumentos{
    
    @EJB Facade facade;

    @Override
    public List<dtoFechasDocumentos> getListaFechasDocumentosGeneracion(Generaciones generacionSeleccionada) {
        if (generacionSeleccionada == null) {
            return null;
        }
        //obtener la lista de expedientes de titulación filtrando por generación y programa educativo
        List<dtoFechasDocumentos> l = new ArrayList<>();
        List<FechasDocumentos> entities = new ArrayList<>();
      
        entities = facade.getEntityManager().createQuery("SELECT f FROM FechasDocumentos f WHERE f.generacion =:generacion", FechasDocumentos.class)
                .setParameter("generacion", generacionSeleccionada.getGeneracion())
                .getResultList();
        
        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            dtoFechasDocumentos dto = new dtoFechasDocumentos();
            try {
               
//                Generaciones gen = facadePYE2.getEntityManager().find(Generaciones.class, e.getGeneracion());
                AreasUniversidad prog = facade.getEntityManager().find(AreasUniversidad.class, e.getProgramaEducativo());
                
                l.add(new dtoFechasDocumentos(
                        e,
                        generacionSeleccionada,
                        prog
                ));
            } catch (Throwable ex) {
                Logger.getLogger(ServiceFechasDocumentos.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        });
        return l;
    }

    @Override
    public Boolean eliminarFecDocsGeneracion(dtoFechasDocumentos fecDocs) {
         if (fecDocs == null) {
            return false;
        }

        Integer id = fecDocs.getFechasDocumentos().getFechaDoc();

        List<ExpedientesTitulacion> listaExp = buscarExpConFecDocs(fecDocs);
        try {
            
            if (listaExp.size() > 0) {
                Messages.addGlobalInfo("No se puede eliminar el registro porque tiene expediente de titulación relacionados");
            } else {

                facade.remove(fecDocs.getFechasDocumentos());
                facade.flush();
            }

        } catch (Exception e) {
            return false;
        }

        return facade.getEntityManager().find(FechasDocumentos.class, id) == null;
    }

    @Override
    public List<ExpedientesTitulacion> buscarExpConFecDocs(dtoFechasDocumentos fecDocs) {
       
        List<ExpedientesTitulacion> l = facade.getEntityManager().createQuery("SELECT e FROM ExpedientesTitulacion e WHERE e.generacion =:generacion AND e.programaEducativo =:programaEducativo", ExpedientesTitulacion.class)
            .setParameter("generacion", fecDocs.getFechasDocumentos().getGeneracion())
            .setParameter("programaEducativo", fecDocs.getAreasUniversidad().getSiglas())
            .getResultList();
       
        return l;
    }

    @Override
    public dtoFechasDocumentos actualizarFecDocumentos(dtoFechasDocumentos dtoFecDocs) throws Throwable {
        FechasDocumentos fecDoc = dtoFecDocs.getFechasDocumentos();
        facade.setEntityClass(FechasDocumentos.class);
        facade.edit(fecDoc);
        facade.flush();
        
        Messages.addGlobalInfo("El registro se ha actualizado correctamente");
        return dtoFecDocs;
    }

    @Override
    public FechasDocumentos guardarFecDocumentos(FechasDocumentos fechasDocumentos) throws Throwable {
        facade.create(fechasDocumentos);
        Messages.addGlobalInfo("<b>Se guardó el registro correctamente.</b>");
            
        return fechasDocumentos;
    }

    @Override
    public List<Generaciones> getGeneracionesRegistradas() {
        List<Short> claves = facade.getEntityManager().createQuery("SELECT f FROM FechasDocumentos f", FechasDocumentos.class)
                    .getResultStream()
                    .map(e -> e.getGeneracion())
                    .collect(Collectors.toList());

        return facade.getEntityManager().createQuery("SELECT g FROM Generaciones g WHERE g.generacion IN :claves ORDER BY g.generacion desc", Generaciones.class)
                    .setParameter("claves", claves)
                    .getResultList();
    }
    
}
