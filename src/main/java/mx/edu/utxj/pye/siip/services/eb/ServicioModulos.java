/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.eb;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistroUsuario;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.enums.EvidenciaCategoria;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.sgi.exception.EvidenciaRegistroExtensionNoValidaException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
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
    public EventosRegistros getEventoRegistro() throws EventoRegistroNoExistenteException {
        try {
            TypedQuery<EventosRegistros> query = em.createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            return eventoRegistro;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new EventoRegistroNoExistenteException(new Date(), ex);
        }
    }

    @Override
    public Boolean validaPeriodoRegistro(PeriodosEscolares periodoEscolar, Integer periodoRegistro) {
        if (!Objects.equals(periodoEscolar.getPeriodo(), periodoRegistro)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean eliminarRegistro(Integer registro) {
        facadeEscolar.setEntityClass(Registros.class);
        Registros r = facadeEscolar.getEntityManager().find(Registros.class, registro);
        facadeEscolar.remove(r);
        facadeEscolar.flush();
        return facadeEscolar.getEntityManager().find(Registros.class, registro) == null;
    }
    
    

    @Override
    public List<Short> getEjercicioRegistros(List<Short> registroTipo, AreasUniversidad area) {
        List<Short> ejerciciosRegistros = new ArrayList<>();
        try {
            return ejerciciosRegistros = em.createQuery("SELECT DISTINCT er.ejercicioFiscal.anio FROM Registros r JOIN r.eventoRegistro er WHERE r.tipo.registroTipo IN :registroTipo AND r.area = :area ORDER BY er.fechaInicio")
                    .setParameter("registroTipo", registroTipo)
                    .setParameter("area", area.getArea())
                    .getResultList();
        } catch (NoResultException ex) {
            return ejerciciosRegistros = null;
        }
    }

    @Override
    public List<String> getMesesRegistros(Short ejercicio, List<Short> registroTipo, AreasUniversidad area) {
        List<String> mesesRegistros = new ArrayList<>();
        try {
            return mesesRegistros = em.createQuery("SELECT DISTINCT er.mes FROM Registros r JOIN r.eventoRegistro er WHERE er.ejercicioFiscal.anio = :anio AND r.tipo.registroTipo IN :registroTipo AND r.area = :area ORDER BY er.fechaInicio")
                    .setParameter("anio", ejercicio)
                    .setParameter("registroTipo", registroTipo)
                    .setParameter("area", area.getArea())
                    .getResultList();
        } catch (NoResultException e) {
            return mesesRegistros = null;
        }
    }

    @Override
    public AreasUniversidad getAreaUniversidadPrincipalRegistro(Short areaUsuario) {
        AreasUniversidad areasUniversidadValidar = new AreasUniversidad();
        AreasUniversidad areasUniversidad = new AreasUniversidad();
        areasUniversidadValidar = em.find(AreasUniversidad.class, areaUsuario);
        if (areasUniversidadValidar.getTienePoa()) {
            return areasUniversidad = em.find(AreasUniversidad.class, areaUsuario);
        } else {
            return areasUniversidad = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.area =:areaUsuario AND a.tienePoa = :tienePoa", AreasUniversidad.class)
                    .setParameter("areaUsuario", areasUniversidadValidar.getAreaSuperior())
                    .setParameter("tienePoa", true)
                    .getSingleResult();
        }
    }

    @Override
    public Boolean eliminarRegistroParticipantes(List<Integer> registrosParticipantes) {
        Query query = em.createQuery("DELETE FROM Registros r WHERE r.registro IN :registrosParticipantes")
                .setParameter("registrosParticipantes", registrosParticipantes);
        try {
            query.executeUpdate();
        } catch (Exception e) {
            addDetailMessage("<b>¡No se pudieron eliminar los participantes del registro seleccionado!</b>");
        }
        return em.createQuery("SELECT r FROM Registros r WHERE r.registro IN :registrosParticipantes")
                .setParameter("registrosParticipantes", registrosParticipantes)
                .getResultList().isEmpty();

    }

    @Override
    public List<EvidenciasDetalle> getListaEvidenciasPorRegistro(Integer registro) throws Throwable {
        if (registro == null) {
            return Collections.EMPTY_LIST;
        }
        List<EvidenciasDetalle> l = new ArrayList<>();
        try {
            l = em.createQuery("SELECT e FROM Evidencias e INNER JOIN e.registrosList r INNER JOIN e.evidenciasDetalleList ed WHERE r.registro=:registro ORDER BY ed.mime, ed.ruta", Evidencias.class)
                    .setParameter("registro", registro)
                    .getResultStream()
                    .map(e -> e.getEvidenciasDetalleList())
                    .flatMap(ed -> ed.stream())
                    .distinct()
                    .collect(Collectors.toList());
            return l;
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }
    
    @Override
    public List<EvidenciasDetalle> getListaEvidenciasPorRegistrosParticipantes(List<Integer> registros) throws Throwable {
        if (registros.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<EvidenciasDetalle> l = new ArrayList<>();
        try {
            l = em.createQuery("SELECT e FROM Evidencias e INNER JOIN e.registrosList r INNER JOIN e.evidenciasDetalleList ed WHERE r.registro IN :registros ORDER BY ed.mime, ed.ruta", Evidencias.class)
                    .setParameter("registros", registros)
                    .getResultStream()
                    .map(e -> e.getEvidenciasDetalleList())
                    .flatMap(ed -> ed.stream())
                    .distinct()
                    .collect(Collectors.toList());
            return l;
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Map.Entry<Boolean, Integer> registrarEvidenciasARegistro(Registros registro, List<Part> archivos) throws Throwable {
        Map<Boolean, Integer> map = new HashMap<>();

        if (registro == null || archivos == null || archivos.isEmpty()) {
            map.put(Boolean.FALSE, 0);
            return map.entrySet().iterator().next();
        }
        final List<Boolean> res = new ArrayList<>();
        Evidencias evidencias = new Evidencias(0, archivos.size() > 1 ? EvidenciaCategoria.MULTIPLE.getLabel() : EvidenciaCategoria.UNICA.getLabel());
        facadeEscolar.create(evidencias);
        facadeEscolar.flush();
        facadeEscolar.refresh(evidencias);
        AreasUniversidad areaPOA = getAreaUniversidadPrincipalRegistro(registro.getArea());
        archivos.forEach(archivo -> {
            try {
                String rutaAbsoluta = ServicioArchivos.almacenarEvidenciaRegistroGeneral(areaPOA, registro, archivo);
                EvidenciasDetalle ed = new EvidenciasDetalle(0, rutaAbsoluta, archivo.getContentType(), archivo.getSize(), registro.getEventoRegistro().getMes());
                evidencias.getEvidenciasDetalleList().add(ed);
                ed.setEvidencia(evidencias);
                facadeEscolar.create(ed);
                facadeEscolar.flush();
                facadeEscolar.refresh(ed);
                res.add(true);
            } catch (IOException | EvidenciaRegistroExtensionNoValidaException e) {
                res.add(Boolean.FALSE);
                LOG.log(Level.SEVERE, "No se guardó el archivo: " + archivo.getSubmittedFileName(), e);
            }
        });

        Long correctos = res.stream().filter(r -> r).count();
        Long incorrectos = res.stream().filter(r -> !r).count();

        if (correctos == 0) {
            facadeEscolar.remove(evidencias);
            facadeEscolar.flush();
        } else {
            registro.getEvidenciasList().add(evidencias);
            evidencias.getRegistrosList().add(registro);
            facadeEscolar.edit(registro);
            facadeEscolar.edit(evidencias);
            facadeEscolar.flush();
        }

        map.put(incorrectos == 0, correctos.intValue());
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.registrarEvidenciasARegistro(2)");
        return map.entrySet().iterator().next();
    }
    private static final Logger LOG = Logger.getLogger(ServicioModulos.class.getName());

    @Override
    public Boolean eliminarEvidenciaEnRegistro(Registros registro, EvidenciasDetalle evidenciasDetalle) {
        if (registro == null || evidenciasDetalle == null) {
            return false;
        }

        Integer id = evidenciasDetalle.getDetalleEvidencia();
        try {
            ServicioArchivos.eliminarArchivo(evidenciasDetalle.getRuta());

            Evidencias evidencias = evidenciasDetalle.getEvidencia();
            Integer total = evidencias.getEvidenciasDetalleList().size();
            evidencias.getEvidenciasDetalleList().remove(evidenciasDetalle);
            facadeEscolar.remove(evidenciasDetalle);
            facadeEscolar.edit(evidencias);
            facadeEscolar.flush();

//            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.eliminarEvidenciaEnRegistro() total: " + total);
            if (total == 1) {
                facadeEscolar.remove(evidencias);
                facadeEscolar.flush();
            } else if (total == 2) {
                evidencias.setCategoria(EvidenciaCategoria.UNICA.getLabel());
                facadeEscolar.edit(evidencias);
                facadeEscolar.flush();
                facadeEscolar.getEntityManager().detach(evidencias);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se eliminó la evidencia: " + evidenciasDetalle.getRuta(), e);
            return false;
        }
        return facadeEscolar.getEntityManager().find(EvidenciasDetalle.class, id) == null;
    }

    @Override
    public void eliminarEvidenciasEnRegistroGeneral(Integer registro, List<EvidenciasDetalle> evidenciasDetalle) {
        if (registro == null || evidenciasDetalle == null) {
        } else {
            evidenciasDetalle.forEach((t) -> {
                ServicioArchivos.eliminarArchivo(t.getRuta());

                Evidencias evidencias = t.getEvidencia();
                Integer total = evidencias.getEvidenciasDetalleList().size();
                evidencias.getEvidenciasDetalleList().remove(t);
                facadeEscolar.remove(t);
                facadeEscolar.edit(evidencias);
                facadeEscolar.flush();

//            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.eliminarEvidenciaEnRegistro() total: " + total);
                if (total == 1) {
                    facadeEscolar.remove(evidencias);
                    facadeEscolar.flush();
                } else if (total == 2) {
                    evidencias.setCategoria(EvidenciaCategoria.UNICA.getLabel());
                    facadeEscolar.edit(evidencias);
                    facadeEscolar.flush();
                    facadeEscolar.getEntityManager().detach(evidencias);
                }
            });
        }
    }

    @Override
    public ActividadesPoa getActividadAlineadaGeneral(Integer registro) throws Throwable {
        List<ActividadesPoa> l = em.createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.registrosList r INNER JOIN a.cuadroMandoInt cm INNER JOIN FETCH cm.lineaAccion INNER JOIN FETCH cm.estrategia INNER JOIN FETCH cm.eje WHERE r.registro = :registro", ActividadesPoa.class)
                .setParameter("registro", registro)
                .getResultList();
        if(!l.isEmpty()) return l.get(0);
        else return null;
    }
    
    @Override
    public Boolean verificaActividadAlineadaGeneral(Integer registro) throws Throwable {
        List<ActividadesPoa> l = em.createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.registrosList r INNER JOIN a.cuadroMandoInt cm INNER JOIN FETCH cm.lineaAccion INNER JOIN FETCH cm.estrategia INNER JOIN FETCH cm.eje WHERE r.registro = :registro", ActividadesPoa.class)
                .setParameter("registro", registro)
                .getResultList();
        if(!l.isEmpty()) return true;
        else return false;
    }
 

    @Override
    public Boolean eliminarAlineacion(Integer registro) {
        try{
            Registros r = em.find(Registros.class, registro);

            if(!r.getActividadesPoaList().isEmpty()){
                ActividadesPoa a2 = em.find(ActividadesPoa.class, r.getActividadesPoaList().get(0).getActividadPoa());
//                Integer clave = a2.getActividadPoa();
                a2.getRegistrosList().remove(r);
                r.getActividadesPoaList().remove(a2);
                em.flush();
            }
            return true;
        }catch(Exception e){
            LOG.log(Level.SEVERE, "No se pudo alinear el registro con la actividad.", e);
            return false;
        }
    }

    @Override
    public Boolean alinearRegistroActividad(ActividadesPoa actividad, Integer registro) {
         try{
            ActividadesPoa a = em.find(ActividadesPoa.class, actividad.getActividadPoa());
            Registros r = em.find(Registros.class, registro);
            eliminarAlineacion(registro);
            a.getRegistrosList().add(r);
            r.getActividadesPoaList().add(actividad);
            em.flush();
            return true;
        }catch(Exception e){
            LOG.log(Level.SEVERE, "No se pudo alinear el registro con la actividad.", e);
            return false;
        }
    }

    @Override
    public Registros buscaRegistroPorClave(Integer registro) {
        try {
            return em.createQuery("SELECT r FROM Registros r WHERE r.registro = :registro",Registros.class)
                    .setParameter("registro", registro)
                    .getSingleResult();
        } catch (NonUniqueResultException | NoResultException e) {
            return null;
        }
    }
}
