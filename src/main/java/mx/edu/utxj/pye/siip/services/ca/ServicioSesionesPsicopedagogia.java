/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.AreasConflicto;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.OtrosTiposSesionesPsicopedagogia;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.SesionIndividualMensualPsicopedogia;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.siip.dto.ca.DTOSesionesPsicopedagogia;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbSesionesPsicopedagogia;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;

/**
 *
 * @author UTXJ
 */
@Stateless
public class ServicioSesionesPsicopedagogia implements EjbSesionesPsicopedagogia{

    @EJB    Facade      facade;
    @EJB    EjbModulos  ejbModulos;
    
    private static final Logger LOG = Logger.getLogger(ServicioSesionesPsicopedagogia.class.getName());
    
    @Override
    public List<AreasConflicto> getListaAreasDeConflicto() {
        try {
            return facade.getEntityManager().createQuery("SELECT a FROM AreasConflicto a ORDER BY a.descripcion",AreasConflicto.class)
                    .getResultList();
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        } catch (Exception e){
            LOG.log(Level.SEVERE, "No se pudieron cosultar los registros de AreasDeConflicto.", e);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<OtrosTiposSesionesPsicopedagogia> getListaOtrosTiposSesionesPsicopedagogia() {
        try {
            return facade.getEntityManager().createQuery("SELECT o FROM OtrosTiposSesionesPsicopedagogia o ORDER BY o.descripcion", OtrosTiposSesionesPsicopedagogia.class)
                    .getResultList();
        }catch  (NoResultException ex){
            return Collections.EMPTY_LIST;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudieron cosultar los registros de OtrosTiposSesionesPsicopedagogia.", e);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<SesionIndividualMensualPsicopedogia> buscaSesionIndividualMensualPsicopedagogia(SesionIndividualMensualPsicopedogia simPsicopedagogia) {
        List<SesionIndividualMensualPsicopedogia> listaSesionIndividual = new ArrayList<>();
        try {
            listaSesionIndividual = facade.getEntityManager().createQuery("SELECT s FROM SesionIndividualMensualPsicopedogia s WHERE s.areaConflicto.areaConflicto = :areaConflicto AND s.otroTipoSesion.otroTipoSesionPsicopedagogia = :otroTipoSesion AND s.programaEducativo = :programaEducativo AND s.mes = :mes", SesionIndividualMensualPsicopedogia.class)
                    .setParameter("areaConflicto", simPsicopedagogia.getAreaConflicto().getAreaConflicto())
                    .setParameter("otroTipoSesion", simPsicopedagogia.getOtroTipoSesion().getOtroTipoSesionPsicopedagogia())
                    .setParameter("programaEducativo", simPsicopedagogia.getProgramaEducativo())
                    .setParameter("mes", simPsicopedagogia.getMes())
                    .getResultList();
            listaSesionIndividual.stream().forEach((t) -> {
//                System.err.println(t.getRegistro());
            });
            return listaSesionIndividual;
        } catch (NoResultException e) {
//            LOG.log(Level.SEVERE, "No se ha encontrado ninguna coincidencia: SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (NonUniqueResultException e) {
//            LOG.log(Level.SEVERE, "Se ha encontrado más de un resultado durante la consulta del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (Exception e) {
//            LOG.log(Level.SEVERE, "Ha ocurrido una excepción durante la busqueda del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        }
    }
    
    @Override
    public List<SesionIndividualMensualPsicopedogia> buscaSesionIndividualMensualPsicopedagogiaSPE(SesionIndividualMensualPsicopedogia simPsicopedagogia) {
        List<SesionIndividualMensualPsicopedogia> listaSesionIndividual = new ArrayList<>();
        try {
            listaSesionIndividual = facade.getEntityManager().createQuery("SELECT s FROM SesionIndividualMensualPsicopedogia s WHERE s.areaConflicto.areaConflicto = :areaConflicto AND s.otroTipoSesion.otroTipoSesionPsicopedagogia = :otroTipoSesion AND s.mes = :mes AND s.programaEducativo IS NULL", SesionIndividualMensualPsicopedogia.class)
                    .setParameter("areaConflicto", simPsicopedagogia.getAreaConflicto().getAreaConflicto())
                    .setParameter("otroTipoSesion", simPsicopedagogia.getOtroTipoSesion().getOtroTipoSesionPsicopedagogia())
                    .setParameter("mes", simPsicopedagogia.getMes())    
                    .getResultList();
            listaSesionIndividual.stream().forEach((t) -> {
//                System.err.println(t.getRegistro());
            });
            return listaSesionIndividual;
        } catch (NoResultException e) {
//            LOG.log(Level.SEVERE, "No se ha encontrado ninguna coincidencia: SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (NonUniqueResultException e) {
//            LOG.log(Level.SEVERE, "Se ha encontrado más de un resultado durante la consulta del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (Exception e) {
//            LOG.log(Level.SEVERE, "Ha ocurrido una excepción durante la busqueda del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        }
    }
    
    @Override
    public List<SesionIndividualMensualPsicopedogia> buscaSesionIndividualMensualPsicopedagogiaParaEdicion(SesionIndividualMensualPsicopedogia simPsicopedagogia) {
        List<SesionIndividualMensualPsicopedogia> listaSesionIndividual = new ArrayList<>();
        try {
            listaSesionIndividual = facade.getEntityManager().createQuery("SELECT s FROM SesionIndividualMensualPsicopedogia s WHERE s.areaConflicto.areaConflicto = :areaConflicto AND s.otroTipoSesion.otroTipoSesionPsicopedagogia = :otroTipoSesion AND s.programaEducativo = :programaEducativo AND s.mes = :mes AND s.registro <> :registro", SesionIndividualMensualPsicopedogia.class)
                    .setParameter("areaConflicto", simPsicopedagogia.getAreaConflicto().getAreaConflicto())
                    .setParameter("otroTipoSesion", simPsicopedagogia.getOtroTipoSesion().getOtroTipoSesionPsicopedagogia())
                    .setParameter("programaEducativo", simPsicopedagogia.getProgramaEducativo())
                    .setParameter("mes", simPsicopedagogia.getMes())
                    .setParameter("registro", simPsicopedagogia.getRegistro())
                    .getResultList();
            listaSesionIndividual.stream().forEach((t) -> {
//                System.err.println(t.getRegistro());
            });
            return listaSesionIndividual;
        } catch (NoResultException e) {
//            LOG.log(Level.SEVERE, "No se ha encontrado ninguna coincidencia: SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (NonUniqueResultException e) {
//            LOG.log(Level.SEVERE, "Se ha encontrado más de un resultado durante la consulta del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (Exception e) {
//            LOG.log(Level.SEVERE, "Ha ocurrido una excepción durante la busqueda del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<SesionIndividualMensualPsicopedogia> buscaSesionIndividualMensualPsicopedagogiaSPEParaEdicion(SesionIndividualMensualPsicopedogia simPsicopedagogia) {
        List<SesionIndividualMensualPsicopedogia> listaSesionIndividual = new ArrayList<>();
        try {
            listaSesionIndividual = facade.getEntityManager().createQuery("SELECT s FROM SesionIndividualMensualPsicopedogia s WHERE s.areaConflicto.areaConflicto = :areaConflicto AND s.otroTipoSesion.otroTipoSesionPsicopedagogia = :otroTipoSesion AND s.mes = :mes AND s.programaEducativo IS NULL AND s.registro <> :registro", SesionIndividualMensualPsicopedogia.class)
                    .setParameter("areaConflicto", simPsicopedagogia.getAreaConflicto().getAreaConflicto())
                    .setParameter("otroTipoSesion", simPsicopedagogia.getOtroTipoSesion().getOtroTipoSesionPsicopedagogia())
                    .setParameter("mes", simPsicopedagogia.getMes())  
                    .setParameter("registro", simPsicopedagogia.getRegistro())
                    .getResultList();
            listaSesionIndividual.stream().forEach((t) -> {
//                System.err.println(t.getRegistro());
            });
            return listaSesionIndividual;
        } catch (NoResultException e) {
//            LOG.log(Level.SEVERE, "No se ha encontrado ninguna coincidencia: SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (NonUniqueResultException e) {
//            LOG.log(Level.SEVERE, "Se ha encontrado más de un resultado durante la consulta del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (Exception e) {
//            LOG.log(Level.SEVERE, "Ha ocurrido una excepción durante la busqueda del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public String guardaSesionIndividualMensualPsicopedagogia(SesionIndividualMensualPsicopedogia simPsicopedagogia, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
        Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
        facade.setEntityClass(SesionIndividualMensualPsicopedogia.class);
        if(simPsicopedagogia.getProgramaEducativo() == null){
            simPsicopedagogia.setRegistro(registro.getRegistro());
            simPsicopedagogia.setProgramaEducativo(null);
        }else{
            simPsicopedagogia.setRegistro(registro.getRegistro());
        }
        facade.create(simPsicopedagogia);
        facade.flush();
        if(simPsicopedagogia.getRegistro() == null){
            return "El registro se almacenó en la base de datos";
        }else{
            return "Ha ocurrido un error durante la operación";
        }
    }

    @Override
    public String editaSesionIndividualMensualPsicopedagogia(SesionIndividualMensualPsicopedogia simPsicopedagogia) {
        try {
            facade.setEntityClass(SesionIndividualMensualPsicopedogia.class);
            if(simPsicopedagogia.getProgramaEducativo() == null){
                simPsicopedagogia.setProgramaEducativo(null);
            }
            facade.edit(simPsicopedagogia);
            facade.flush();
            return "Los datos se han actualizado correctamente";
        } catch (Exception e) {
            return "Ha ocurrido un error durante la operación, verifique su información";
        }
    }

    @Override
    public List<DTOSesionesPsicopedagogia> getFiltroSesionesIndividualesPorAreaEjercicioMesArea(AreasUniversidad area, Short ejercicio, String mes) {
        try {
            List<DTOSesionesPsicopedagogia> listaDTOs = new ArrayList<>();
            List<SesionIndividualMensualPsicopedogia> listaSI = new ArrayList<>();
            listaSI = facade.getEntityManager().createQuery("SELECT s FROM SesionIndividualMensualPsicopedogia s JOIN s.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE r.area = :area AND f.anio = :ejercicio AND e.mes = :mes", SesionIndividualMensualPsicopedogia.class)
                    .setParameter("area", area.getArea())
                    .setParameter("ejercicio", ejercicio)
                    .setParameter("mes", mes)
                    .getResultList();
            listaSI.forEach((t) -> {
                facade.getEntityManager().refresh(t);
                ActividadesPoa a = t.getRegistros().getActividadesPoaList().isEmpty() ? null : t.getRegistros().getActividadesPoaList().get(0);
                if (a != null) {
                    listaDTOs.add(new DTOSesionesPsicopedagogia(
                            t,
                            validaAreaUniversidad(t.getProgramaEducativo()),
                            a
                    ));
                } else {
                    listaDTOs.add(new DTOSesionesPsicopedagogia(
                            t,
                            validaAreaUniversidad(t.getProgramaEducativo())
                    ));
                }
            });
            return listaDTOs;
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }
    
    public AreasUniversidad validaAreaUniversidad(Short programaEducativo) {
        AreasUniversidad areaUniversidad = null;
        if (programaEducativo != null) {
            areaUniversidad = facade.getEntityManager().find(AreasUniversidad.class, programaEducativo);
        }
        return areaUniversidad;
    }
    
}
