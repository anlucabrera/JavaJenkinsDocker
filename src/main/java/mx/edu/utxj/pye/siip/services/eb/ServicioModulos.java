/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.eb;

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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.servlet.http.Part;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.Meses;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistrosUsuarios;
//import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistroUsuario;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.enums.EvidenciaCategoria;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.sgi.exception.EvidenciaRegistroExtensionNoValidaException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.omnifaces.util.Messages;

@Stateful
/**
 * Servicio para mostrar a los usuario los ejes que tienen habilitados
 */
public class ServicioModulos implements EjbModulos {

    @EJB    Facade f;
    @EJB EjbPropiedades ep;
    
    private     EntityManager               em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
//
//    /*Administrador de entidades*/
//    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
//    private EntityManager em;

    /**
     *
     * @param clavepersonal
     * @return
     * @throws Throwable
     */
    @Override
    public List<ModulosRegistrosUsuarios> getEjesRectores(@NonNull Integer clavepersonal) throws Throwable {
        try {
            TypedQuery<ModulosRegistrosUsuarios> query = em.createQuery("SELECT mru FROM ModulosRegistrosUsuarios mru WHERE mru.clavePersonal = :clavepersonal AND mru.estado = :estado GROUP BY mru.claveEje ORDER BY mru.claveEje", ModulosRegistrosUsuarios.class);
            query.setParameter("clavepersonal", clavepersonal);
            query.setParameter("estado", "1");
            List<ModulosRegistrosUsuarios> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioModulos.getEjesRectores() " + e.getMessage());
            return Collections.EMPTY_LIST;
        }
    }

    /**
     *
     * @param eje
     * @param clavepersonal
     * @return
     * @throws Throwable
     */
    @Override
    public List<ModulosRegistrosUsuarios> getModulosRegistroUsuario(@NonNull Integer eje, @NonNull Integer clavepersonal) throws Throwable {
        try {
            TypedQuery<ModulosRegistrosUsuarios> query = em.createQuery("SELECT mru FROM ModulosRegistrosUsuarios mru WHERE mru.claveEje = :eje AND mru.clavePersonal = :clavepersonal AND mru.estado = :estado ORDER BY mru.claveEje", ModulosRegistrosUsuarios.class);
            query.setParameter("eje", eje);
            query.setParameter("clavepersonal", clavepersonal);
            query.setParameter("estado", "1");
            List<ModulosRegistrosUsuarios> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioModulos.getModulosRegistroUsuario() " + e.getMessage());
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Registros getRegistro(@NonNull RegistrosTipo registrosTipo, @NonNull EjesRegistro ejesRegistro, @NonNull Short area, @NonNull EventosRegistros eventosRegistros) {
        try {
            Registros registro = new Registros();
            registro.setTipo(registrosTipo);
            registro.setEje(ejesRegistro);
            registro.setArea(area);
            registro.setFechaRegistro(new Date());
            registro.setEventoRegistro(eventosRegistros);
            guardaRegistroREJB(registro);
            return registro;
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioModulos.getRegistro() " + e.getMessage());
            return null;
        }
    }
    
    public ResultadoEJB<Registros> guardaRegistroREJB(Registros registro){
        try {
            em.persist(registro);
            em.flush();
            return ResultadoEJB.crearCorrecto(registro, "Registro guardado correctamente (Registros)");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido guardar (Registros)", e, Registros.class);
        }
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
    public Boolean validaPeriodoRegistro(@NonNull PeriodosEscolares periodoEscolar, @NonNull Integer periodoRegistro) {
        Integer periodo = periodoEscolar.getPeriodo();
        if (Objects.equals(periodo, periodoRegistro)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean eliminarRegistro(@NonNull Integer registro) {
        try {
            Registros r = em.find(Registros.class, registro);
            em.remove(r);
            em.flush();
            return em.find(Registros.class, registro) == null;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public List<Short> getEjercicioRegistros(@NonNull List<Short> registroTipo, @NonNull AreasUniversidad area) {
        List<Short> ejerciciosRegistros = new ArrayList<>();
        try {
            return ejerciciosRegistros = em.createQuery("SELECT DISTINCT er.ejercicioFiscal.anio FROM Registros r JOIN r.eventoRegistro er WHERE r.tipo.registroTipo IN :registroTipo AND r.area = :area ORDER BY er.fechaInicio")
                    .setParameter("registroTipo", registroTipo)
                    .setParameter("area", area.getArea())
                    .getResultList();
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<Short> getEjercicioRegistroRepositorio(@NonNull AreasUniversidad area) {
        try {
            return em.createQuery("SELECT DISTINCT er.ejercicioFiscal.anio FROM Registros r JOIN r.eventoRegistro er WHERE r.area = :area ORDER BY er.fechaInicio")
                    .setParameter("area", area.getArea())
                    .getResultList();
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }
    
    @Override
    public List<String> getMesesRegistros(@NonNull Short ejercicio, @NonNull List<Short> registroTipo, @NonNull AreasUniversidad area) {
        List<String> mesesRegistros = new ArrayList<>();
        try {
            return mesesRegistros = em.createQuery("SELECT DISTINCT er.mes FROM Registros r JOIN r.eventoRegistro er WHERE er.ejercicioFiscal.anio = :anio AND r.tipo.registroTipo IN :registroTipo AND r.area = :area ORDER BY er.fechaInicio")
                    .setParameter("anio", ejercicio)
                    .setParameter("registroTipo", registroTipo)
                    .setParameter("area", area.getArea())
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }
    
    @Override
    public List<String> getMesesRegistroRepositorio(@NonNull Short ejercicio, @NonNull AreasUniversidad area) {
        try {
            return em.createQuery("SELECT DISTINCT er.mes FROM Registros r JOIN r.eventoRegistro er WHERE er.ejercicioFiscal.anio = :anio AND r.area = :area ORDER BY er.fechaInicio")
                    .setParameter("anio", ejercicio)
                    .setParameter("area", area.getArea())
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }
    

    @Override
    public AreasUniversidad getAreaUniversidadPrincipalRegistro(@NonNull Short areaUsuario) {
        try {
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
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Boolean eliminarRegistroParticipantes(@NonNull List<Integer> registrosParticipantes) {
        try {
            Query query = em.createQuery("DELETE FROM Registros r WHERE r.registro IN :registrosParticipantes")
                    .setParameter("registrosParticipantes", registrosParticipantes);
            try {
                query.executeUpdate();
            } catch (Exception e) {
                Messages.addGlobalError("<b>¡No se pudieron eliminar los participantes del registro seleccionado!</b>");
            }
            return em.createQuery("SELECT r FROM Registros r WHERE r.registro IN :registrosParticipantes")
                    .setParameter("registrosParticipantes", registrosParticipantes)
                    .getResultList().isEmpty();
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public List<EvidenciasDetalle> getListaEvidenciasPorRegistro(@NonNull Integer registro) throws Throwable {
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
    public List<EvidenciasDetalle> getListaEvidenciasPorRegistrosParticipantes(@NonNull List<Integer> registros) throws Throwable {
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
    public Map.Entry<Boolean, Integer> registrarEvidenciasARegistro(@NonNull Registros registro, @NonNull List<Part> archivos) throws Throwable {
        try {
            Map<Boolean, Integer> map = new HashMap<>();

            if (registro == null || archivos == null || archivos.isEmpty()) {
                map.put(Boolean.FALSE, 0);
                return map.entrySet().iterator().next();
            }
            final List<Boolean> res = new ArrayList<>();
            Evidencias evidencias = new Evidencias(0, archivos.size() > 1 ? EvidenciaCategoria.MULTIPLE.getLabel() : EvidenciaCategoria.UNICA.getLabel());
            em.persist(evidencias);
            em.flush();
            em.refresh(evidencias);
            AreasUniversidad areaPOA = getAreaUniversidadPrincipalRegistro(registro.getArea());
            archivos.forEach(archivo -> {
                try {
                    String rutaAbsoluta = ServicioArchivos.almacenarEvidenciaRegistroGeneral(areaPOA, registro, archivo);
                    EvidenciasDetalle ed = new EvidenciasDetalle(0, rutaAbsoluta, archivo.getContentType(), archivo.getSize(), registro.getEventoRegistro().getMes());
                    evidencias.getEvidenciasDetalleList().add(ed);
                    ed.setEvidencia(evidencias);
                    em.persist(ed);
                    em.flush();
                    res.add(true);
                } catch (IOException | EvidenciaRegistroExtensionNoValidaException e) {
                    res.add(Boolean.FALSE);
                    LOG.log(Level.SEVERE, "No se guardó el archivo: " + archivo.getSubmittedFileName(), e);
                }
            });

            Long correctos = res.stream().filter(r -> r).count();
            Long incorrectos = res.stream().filter(r -> !r).count();

            if (correctos == 0) {
                em.remove(evidencias);
                em.flush();
            } else {
                registro.getEvidenciasList().add(evidencias);
                evidencias.getRegistrosList().add(registro);
                em.merge(registro);
                em.merge(evidencias);
                em.flush();
            }

            map.put(incorrectos == 0, correctos.intValue());
    //        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.registrarEvidenciasARegistro(2)");
            return map.entrySet().iterator().next();
        } catch (Exception e) {
            Map<Boolean, Integer> map = new HashMap<>();
            map.put(Boolean.FALSE, 0);    
            return map.entrySet().iterator().next();
        }
    }
    private static final Logger LOG = Logger.getLogger(ServicioModulos.class.getName());

    @Override
    public Boolean eliminarEvidenciaEnRegistro(@NonNull Registros registro, @NonNull EvidenciasDetalle evidenciasDetalle) {
        try {
            if (registro == null || evidenciasDetalle == null) {
                return false;
            }

            Integer id = evidenciasDetalle.getDetalleEvidencia();
            try {
                ServicioArchivos.eliminarArchivo(evidenciasDetalle.getRuta());

                Evidencias evidencias = evidenciasDetalle.getEvidencia();
                Integer total = evidencias.getEvidenciasDetalleList().size();
                evidencias.getEvidenciasDetalleList().remove(evidenciasDetalle);
                em.remove(evidenciasDetalle);
                em.merge(evidencias);
                em.flush();
    //            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.eliminarEvidenciaEnRegistro() total: " + total);
                if (total == 1) {
                    em.remove(evidencias);
                    em.flush();
                } else if (total == 2) {
                    evidencias.setCategoria(EvidenciaCategoria.UNICA.getLabel());
                    em.merge(evidencias);
                    em.flush();
                    em.detach(evidencias);
                }
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "No se eliminó la evidencia: " + evidenciasDetalle.getRuta(), e);
                return false;
            }
            return em.find(EvidenciasDetalle.class, id) == null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void eliminarEvidenciasEnRegistroGeneral(@NonNull Integer registro, @NonNull List<EvidenciasDetalle> evidenciasDetalle) {
        try {
            if (registro == null || evidenciasDetalle == null) {
            } else {
                evidenciasDetalle.forEach((t) -> {
                    ServicioArchivos.eliminarArchivo(t.getRuta());

                    Evidencias evidencias = t.getEvidencia();
                    Integer total = evidencias.getEvidenciasDetalleList().size();
                    evidencias.getEvidenciasDetalleList().remove(t);
                    em.remove(t);
                    em.merge(evidencias);
                    em.flush();

    //            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.eliminarEvidenciaEnRegistro() total: " + total);
                    if (total == 1) {
                        em.remove(evidencias);
                        em.flush();
                    } else if (total == 2) {
                        evidencias.setCategoria(EvidenciaCategoria.UNICA.getLabel());
                        em.merge(evidencias);
                        em.flush();
                        em.detach(evidencias);
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioModulos.eliminarEvidenciasEnRegistroGeneral() " + e.getMessage());
        }
    }

    @Override
    public ActividadesPoa getActividadAlineadaGeneral(@NonNull Integer registro) throws Throwable {
        try {
            List<ActividadesPoa> l = em.createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.registrosList r INNER JOIN a.cuadroMandoInt cm INNER JOIN FETCH cm.lineaAccion INNER JOIN FETCH cm.estrategia INNER JOIN FETCH cm.eje WHERE r.registro = :registro", ActividadesPoa.class)
                    .setParameter("registro", registro)
                    .getResultList();
            if(!l.isEmpty()) return l.get(0);
            else return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public Boolean verificaActividadAlineadaGeneral(@NonNull Integer registro) throws Throwable {
        try {
            List<ActividadesPoa> l = em.createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.registrosList r INNER JOIN a.cuadroMandoInt cm INNER JOIN FETCH cm.lineaAccion INNER JOIN FETCH cm.estrategia INNER JOIN FETCH cm.eje WHERE r.registro = :registro", ActividadesPoa.class)
                .setParameter("registro", registro)
                .getResultList();
            if(!l.isEmpty()) return true;
            else return false;
        } catch (Exception e) {
            return false;
        }
    }
 

    @Override
    public Boolean eliminarAlineacion(@NonNull Integer registro) {
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
    public Boolean alinearRegistroActividad(@NonNull ActividadesPoa actividad, @NonNull Integer registro) {
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
    public Boolean eliminarRegistroEvidencias(@NonNull List<Integer> registrosEvidencias) {
        try {
            eliminarArchivoEvidencias(registrosEvidencias);
            Query query = em.createQuery("DELETE FROM Evidencias e WHERE e.evidencia IN :registrosEvidencias")
                    .setParameter("registrosEvidencias", registrosEvidencias);
            try {
                query.executeUpdate();
            } catch (Exception e) {
                Messages.addGlobalError("<b>¡No se pudieron eliminar los participantes del registro seleccionado!</b>");
            }
            if (em.createQuery("SELECT e FROM Evidencias e WHERE e.evidencia IN :registrosEvidencias")
                    .setParameter("registrosEvidencias", registrosEvidencias)
                    .getResultList().isEmpty()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<ModulosRegistrosUsuarios> getListaPermisoPorRegistro(@NonNull Integer clavePersonal,@NonNull Short claveRegistro) {
        try {
            TypedQuery<ModulosRegistrosUsuarios> q = em.createQuery("SELECT m FROM ModulosRegistrosUsuarios m WHERE m.clavePersonal = :clavePersonal AND m.clave = :claveRegistro", ModulosRegistrosUsuarios.class);
            q.setParameter("clavePersonal", clavePersonal);
            q.setParameter("claveRegistro", claveRegistro);
            List<ModulosRegistrosUsuarios> li = q.getResultList();
            if(li.isEmpty() || li == null){
                return Collections.EMPTY_LIST;
            }else{
                return li;
            }
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<ModulosRegistrosUsuarios> getListaPermisoPorRegistroEjesDistintos(@NonNull Integer clavePersonal, @NonNull Short claveRegistro1, @NonNull Short claveRegistro2) {
        try {
            TypedQuery<ModulosRegistrosUsuarios> q = em.createQuery("SELECT m FROM ModulosRegistrosUsuarios m WHERE m.clavePersonal = :clavePersonal AND (m.clave = :claveRegistro1 OR m.clave = :claveRegistro2)", ModulosRegistrosUsuarios.class);
            q.setParameter("clavePersonal", clavePersonal);
            q.setParameter("claveRegistro1", claveRegistro1);
            q.setParameter("claveRegistro2", claveRegistro2);
            List<ModulosRegistrosUsuarios> li = q.getResultList();
            if (li.isEmpty() || li == null) {
                return Collections.EMPTY_LIST;
            } else {
                return li;
            }
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public void eliminarArchivoEvidencias(@NonNull List<Integer> registrosEvidencias) {
        try {
            if (registrosEvidencias == null) {
            } else {
                TypedQuery<EvidenciasDetalle> q = em.createQuery("SELECT ed FROM EvidenciasDetalle ed WHERE ed.evidencia.evidencia IN :registrosEvidencias", EvidenciasDetalle.class);
                q.setParameter("registrosEvidencias", registrosEvidencias);
                List<EvidenciasDetalle> led = q.getResultList();

                led.forEach((t) -> {
                    ServicioArchivos.eliminarArchivo(t.getRuta());
                });
            }
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioModulos.eliminarArchivoEvidencias()" + e.getMessage());
        }
    }
    @Override
    public Registros buscaRegistroPorClave(@NonNull Integer registro) {
        try {
            return em.createQuery("SELECT r FROM Registros r WHERE r.registro = :registro",Registros.class)
                    .setParameter("registro", registro)
                    .getSingleResult();
        } catch (NonUniqueResultException | NoResultException e) {
            return null;
        }
    }

    @Override
    public PeriodosEscolares getPeriodoEscolarActual() {
        try {
            StoredProcedureQuery spq = em.createStoredProcedureQuery("pye2.periodoEscolarActual", PeriodosEscolares.class);
            List<PeriodosEscolares> l = spq.getResultList();
            if (l == null || l.isEmpty()) {
                return null;
            } else {
                return l.get(0);
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Boolean validaEventoRegistro(@NonNull EventosRegistros eventosRegistros, @NonNull Integer eventoRegistro) {
        Integer evento = eventosRegistros.getEventoRegistro();
        if (Objects.equals(evento, eventoRegistro)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean comparaPeriodoRegistro(@NonNull Integer periodoAnt, @NonNull Integer periodoAct) {
       
        if (Objects.equals(periodoAnt, periodoAct)) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public List<EventosRegistros> getEventosPorPeriodo(@NonNull PeriodosEscolares periodo) {
        try {
            if (periodo == null) {
                return Collections.EMPTY_LIST;
            }
            List<String> meses = em.createQuery("SELECT m FROM Meses m where m.numero BETWEEN :inicio AND :fin ORDER BY m.numero", Meses.class)
                    .setParameter("inicio", periodo.getMesInicio().getNumero())
                    .setParameter("fin", periodo.getMesFin().getNumero())
                    .getResultList()
                    .stream()
                    .map(m -> m.getMes())
                    .collect(Collectors.toList());
            List<EventosRegistros> l = em.createQuery("SELECT er from EventosRegistros er INNER JOIN er.ejercicioFiscal ef WHERE ef.anio=:anio AND er.mes in :meses AND er.fechaInicio <= :fecha ORDER BY er.fechaInicio DESC, er.fechaFin DESC", EventosRegistros.class)
                    .setParameter("fecha", new Date())
                    .setParameter("anio", periodo.getAnio())
                    .setParameter("meses", meses)
                    .getResultList();
            return l;
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }
    }
    
    @Override
    public PeriodosEscolares getPeriodoEscolarActivo() {
        try {
            EventosRegistros eventoRegistro = getEventoRegistro();
            return em.createQuery("SELECT p FROM PeriodosEscolares p WHERE (p.anio = :anio) AND (:mes BETWEEN p.mesInicio.numero AND p.mesFin.numero)", PeriodosEscolares.class)
                    .setParameter("anio", eventoRegistro.getEjercicioFiscal().getAnio())
                    .setParameter("mes", getNumeroMes(eventoRegistro.getMes()))
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    @Override
    public Integer getNumeroMes(@NonNull String mes) {
        Integer mesNumero = 0;
        String[] meses = {"0", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        for (int i = 0; i < meses.length; i++) {
            if (meses[i].equals(mes)) {
                mesNumero = i;
            }
        }
        return mesNumero;
    }
    
     @Override
    public List<Short> getAreasDependientes(@NonNull Short areaOperativa) {
        //verificar que el parametro no sea nulo
        if (areaOperativa == null) {
            return Collections.EMPTY_LIST;
        }
        try {
            List<Short> areas = new ArrayList<>();

            //obtener la referencia al area operativa del trabajador
            AreasUniversidad area = em.find(AreasUniversidad.class, areaOperativa);
            //comprobar si el area operativa es un programa educativo referenciar a su area superior para obtener la referencia al area academica
            Short programaCategoria = (short) ep.leerPropiedadEntera("modulosRegistroProgramaEducativoCategoria").orElse(9);
            if (Objects.equals(area.getCategoria().getCategoria(), programaCategoria)) {
                area = em.find(AreasUniversidad.class, area.getAreaSuperior());

                //Obtener las claves de todas las areas que dependan de área academicoa
                areas = em.createQuery("SELECT au FROM AreasUniversidad au WHERE au.areaSuperior=:areaSuperior AND au.vigente='1'", AreasUniversidad.class)
                        .setParameter("areaSuperior", area.getArea())
                        .getResultStream()
                        .map(au -> au.getArea())
                        .collect(Collectors.toList());

            } else {//si no es Área Académica

                //Obtener las claves de todas las Áreas que dependan del Área del Usuario Logueado
                areas = em.createQuery("SELECT au FROM AreasUniversidad au WHERE au.areaSuperior=:areaSuperior AND au.vigente='1'", AreasUniversidad.class)
                        .setParameter("areaSuperior", area.getArea())
                        .getResultStream()
                        .map(au -> au.getArea())
                        .collect(Collectors.toList());
                areas.add(areaOperativa);
                //Si no tiene Áreas inferiores es decir la lista es vacía, únicamente se muestran los datos de registro del Área del Usuario Logueado
                if (areas.isEmpty()) {
                    areas.add(areaOperativa);
                }
            }
            return areas;
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public CiclosEscolares buscaCicloEscolarEspecifico(@NonNull Integer generacion) {
        try {
            return em.find(CiclosEscolares.class, generacion);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public PeriodosEscolares buscaPeriodoEscolarEspecifico(@NonNull Integer periodo) {
        try {
            return em.find(PeriodosEscolares.class, periodo);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public AreasUniversidad buscaProgramaEducativoEspecifico(@NonNull Short programaEducativo) {
        try {
            return em.find(AreasUniversidad.class, programaEducativo);
        } catch (NoResultException e) {
            return null;
        }
    }
    
}
