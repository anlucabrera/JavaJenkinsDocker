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
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
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

    @EJB
    Facade f;
    @EJB EjbPropiedades ep;
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
    public List<ModulosRegistrosUsuarios> getEjesRectores(Integer clavepersonal) throws Throwable {
        TypedQuery<ModulosRegistrosUsuarios> query = f.getEntityManager().createQuery("SELECT mru FROM ModulosRegistrosUsuarios mru WHERE mru.clavePersonal = :clavepersonal AND mru.estado = :estado GROUP BY mru.claveEje ORDER BY mru.claveEje", ModulosRegistrosUsuarios.class);
        query.setParameter("clavepersonal", clavepersonal);
        query.setParameter("estado", "1");
        List<ModulosRegistrosUsuarios> lista = query.getResultList();
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
    public List<ModulosRegistrosUsuarios> getModulosRegistroUsuario(Integer eje, Integer clavepersonal) throws Throwable {
        TypedQuery<ModulosRegistrosUsuarios> query = f.getEntityManager().createQuery("SELECT mru FROM ModulosRegistrosUsuarios mru WHERE mru.claveEje = :eje AND mru.clavePersonal = :clavepersonal AND mru.estado = :estado ORDER BY mru.claveEje", ModulosRegistrosUsuarios.class);
        query.setParameter("eje", eje);
        query.setParameter("clavepersonal", clavepersonal);
        query.setParameter("estado", "1");
        List<ModulosRegistrosUsuarios> lista = query.getResultList();
        return lista;
    }

    @Override
    public Registros getRegistro(RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
        Registros registro = new Registros();
        f.setEntityClass(Registros.class);
        registro.setTipo(registrosTipo);
        registro.setEje(ejesRegistro);
        registro.setArea(area);
        registro.setFechaRegistro(new Date());
        registro.setEventoRegistro(eventosRegistros);
        f.create(registro);
        f.flush();
        return registro;
    }

    @Override
    public EventosRegistros getEventoRegistro() throws EventoRegistroNoExistenteException {
        try {
            TypedQuery<EventosRegistros> query = f.getEntityManager().createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            return eventoRegistro;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new EventoRegistroNoExistenteException(new Date(), ex);
        }
    }

    @Override
    public Boolean validaPeriodoRegistro(PeriodosEscolares periodoEscolar, Integer periodoRegistro) {
        Integer periodo = periodoEscolar.getPeriodo();
        if (Objects.equals(periodo, periodoRegistro)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean eliminarRegistro(Integer registro) {
        f.setEntityClass(Registros.class);
        Registros r = f.getEntityManager().find(Registros.class, registro);
        f.remove(r);
        f.flush();
        return f.getEntityManager().find(Registros.class, registro) == null;
    }

    @Override
    public List<Short> getEjercicioRegistros(List<Short> registroTipo, AreasUniversidad area) {
        List<Short> ejerciciosRegistros = new ArrayList<>();
        try {
            if(area.getArea() == 6){
                return ejerciciosRegistros = f.getEntityManager().createQuery("SELECT DISTINCT er.ejercicioFiscal.anio FROM Registros r JOIN r.eventoRegistro er WHERE r.tipo.registroTipo IN :registroTipo ORDER BY er.fechaInicio")
                    .setParameter("registroTipo", registroTipo)
                    .getResultList();
            }else{
                return ejerciciosRegistros = f.getEntityManager().createQuery("SELECT DISTINCT er.ejercicioFiscal.anio FROM Registros r JOIN r.eventoRegistro er WHERE r.tipo.registroTipo IN :registroTipo AND r.area = :area ORDER BY er.fechaInicio")
                    .setParameter("registroTipo", registroTipo)
                    .setParameter("area", area.getArea())
                    .getResultList();
            }
            
        } catch (NoResultException ex) {
            return ejerciciosRegistros = null;
        }
    }

    @Override
    public List<String> getMesesRegistros(Short ejercicio, List<Short> registroTipo, AreasUniversidad area) {
        List<String> mesesRegistros = new ArrayList<>();
        try {
            if(area.getArea() == 6){
                return mesesRegistros = f.getEntityManager().createQuery("SELECT DISTINCT er.mes FROM Registros r JOIN r.eventoRegistro er WHERE er.ejercicioFiscal.anio = :anio AND r.tipo.registroTipo IN :registroTipo ORDER BY er.fechaInicio")
                    .setParameter("anio", ejercicio)
                    .setParameter("registroTipo", registroTipo)
                    .getResultList();
            }else{
                return mesesRegistros = f.getEntityManager().createQuery("SELECT DISTINCT er.mes FROM Registros r JOIN r.eventoRegistro er WHERE er.ejercicioFiscal.anio = :anio AND r.tipo.registroTipo IN :registroTipo AND r.area = :area ORDER BY er.fechaInicio")
                    .setParameter("anio", ejercicio)
                    .setParameter("registroTipo", registroTipo)
                    .setParameter("area", area.getArea())
                    .getResultList();
            }
            
        } catch (NoResultException e) {
            return mesesRegistros = null;
        }
    }

    @Override
    public AreasUniversidad getAreaUniversidadPrincipalRegistro(Short areaUsuario) {
        AreasUniversidad areasUniversidadValidar = new AreasUniversidad();
        AreasUniversidad areasUniversidad = new AreasUniversidad();
        areasUniversidadValidar = f.getEntityManager().find(AreasUniversidad.class, areaUsuario);
        if (areasUniversidadValidar.getTienePoa()) {
            return areasUniversidad = f.getEntityManager().find(AreasUniversidad.class, areaUsuario);
        } else {
            return areasUniversidad = f.getEntityManager().createQuery("SELECT a FROM AreasUniversidad a WHERE a.area =:areaUsuario AND a.tienePoa = :tienePoa", AreasUniversidad.class)
                    .setParameter("areaUsuario", areasUniversidadValidar.getAreaSuperior())
                    .setParameter("tienePoa", true)
                    .getSingleResult();
        }
    }

    @Override
    public Boolean eliminarRegistroParticipantes(List<Integer> registrosParticipantes) {
        Query query = f.getEntityManager().createQuery("DELETE FROM Registros r WHERE r.registro IN :registrosParticipantes")
                .setParameter("registrosParticipantes", registrosParticipantes);
        try {
            query.executeUpdate();
        } catch (Exception e) {
            Messages.addGlobalError("<b>¡No se pudieron eliminar los participantes del registro seleccionado!</b>");
        }
        return f.getEntityManager().createQuery("SELECT r FROM Registros r WHERE r.registro IN :registrosParticipantes")
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
            l = f.getEntityManager().createQuery("SELECT e FROM Evidencias e INNER JOIN e.registrosList r INNER JOIN e.evidenciasDetalleList ed WHERE r.registro=:registro ORDER BY ed.mime, ed.ruta", Evidencias.class)
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
            l = f.getEntityManager().createQuery("SELECT e FROM Evidencias e INNER JOIN e.registrosList r INNER JOIN e.evidenciasDetalleList ed WHERE r.registro IN :registros ORDER BY ed.mime, ed.ruta", Evidencias.class)
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
        f.create(evidencias);
        f.flush();
        f.refresh(evidencias);
        AreasUniversidad areaPOA = getAreaUniversidadPrincipalRegistro(registro.getArea());
        archivos.forEach(archivo -> {
            try {
                String rutaAbsoluta = ServicioArchivos.almacenarEvidenciaRegistroGeneral(areaPOA, registro, archivo);
                EvidenciasDetalle ed = new EvidenciasDetalle(0, rutaAbsoluta, archivo.getContentType(), archivo.getSize(), registro.getEventoRegistro().getMes());
                evidencias.getEvidenciasDetalleList().add(ed);
                ed.setEvidencia(evidencias);
                f.create(ed);
                f.flush();
                res.add(true);
            } catch (IOException | EvidenciaRegistroExtensionNoValidaException e) {
                res.add(Boolean.FALSE);
                LOG.log(Level.SEVERE, "No se guardó el archivo: " + archivo.getSubmittedFileName(), e);
            }
        });

        Long correctos = res.stream().filter(r -> r).count();
        Long incorrectos = res.stream().filter(r -> !r).count();

        if (correctos == 0) {
            f.remove(evidencias);
            f.flush();
        } else {
            registro.getEvidenciasList().add(evidencias);
            evidencias.getRegistrosList().add(registro);
            f.edit(registro);
            f.edit(evidencias);
            f.flush();
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
            f.remove(evidenciasDetalle);
            f.edit(evidencias);
            f.flush();

//            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.eliminarEvidenciaEnRegistro() total: " + total);
            if (total == 1) {
                f.remove(evidencias);
                f.flush();
            } else if (total == 2) {
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
    public void eliminarEvidenciasEnRegistroGeneral(Integer registro, List<EvidenciasDetalle> evidenciasDetalle) {
        if (registro == null || evidenciasDetalle == null) {
        } else {
            evidenciasDetalle.forEach((t) -> {
                ServicioArchivos.eliminarArchivo(t.getRuta());

                Evidencias evidencias = t.getEvidencia();
                Integer total = evidencias.getEvidenciasDetalleList().size();
                evidencias.getEvidenciasDetalleList().remove(t);
                f.remove(t);
                f.edit(evidencias);
                f.flush();

//            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.eliminarEvidenciaEnRegistro() total: " + total);
                if (total == 1) {
                    f.remove(evidencias);
                    f.flush();
                } else if (total == 2) {
                    evidencias.setCategoria(EvidenciaCategoria.UNICA.getLabel());
                    f.edit(evidencias);
                    f.flush();
                    f.getEntityManager().detach(evidencias);
                }
            });
        }
    }

    @Override
    public ActividadesPoa getActividadAlineadaGeneral(Integer registro) throws Throwable {
        List<ActividadesPoa> l = f.getEntityManager().createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.registrosList r INNER JOIN a.cuadroMandoInt cm INNER JOIN FETCH cm.lineaAccion INNER JOIN FETCH cm.estrategia INNER JOIN FETCH cm.eje WHERE r.registro = :registro", ActividadesPoa.class)
                .setParameter("registro", registro)
                .getResultList();
        if(!l.isEmpty()) return l.get(0);
        else return null;
    }
    
    @Override
    public Boolean verificaActividadAlineadaGeneral(Integer registro) throws Throwable {
        List<ActividadesPoa> l = f.getEntityManager().createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.registrosList r INNER JOIN a.cuadroMandoInt cm INNER JOIN FETCH cm.lineaAccion INNER JOIN FETCH cm.estrategia INNER JOIN FETCH cm.eje WHERE r.registro = :registro", ActividadesPoa.class)
                .setParameter("registro", registro)
                .getResultList();
        if(!l.isEmpty()) return true;
        else return false;
    }
 

    @Override
    public Boolean eliminarAlineacion(Integer registro) {
        try{
            Registros r = f.getEntityManager().find(Registros.class, registro);

            if(!r.getActividadesPoaList().isEmpty()){
                ActividadesPoa a2 = f.getEntityManager().find(ActividadesPoa.class, r.getActividadesPoaList().get(0).getActividadPoa());
//                Integer clave = a2.getActividadPoa();
                a2.getRegistrosList().remove(r);
                r.getActividadesPoaList().remove(a2);
                f.getEntityManager().flush();
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
            ActividadesPoa a = f.getEntityManager().find(ActividadesPoa.class, actividad.getActividadPoa());
            Registros r = f.getEntityManager().find(Registros.class, registro);
            eliminarAlineacion(registro);
            a.getRegistrosList().add(r);
            r.getActividadesPoaList().add(actividad);
            f.getEntityManager().flush();
            return true;
        }catch(Exception e){
            LOG.log(Level.SEVERE, "No se pudo alinear el registro con la actividad.", e);
            return false;
        }
    }
    
    @Override
    public Boolean eliminarRegistroEvidencias(List<Integer> registrosEvidencias) {
        
        eliminarArchivoEvidencias(registrosEvidencias);
        
        Query query = f.getEntityManager().createQuery("DELETE FROM Evidencias e WHERE e.evidencia IN :registrosEvidencias")
                .setParameter("registrosEvidencias", registrosEvidencias);
    
        try {
            query.executeUpdate();
        } catch (Exception e) {
            Messages.addGlobalError("<b>¡No se pudieron eliminar los participantes del registro seleccionado!</b>");
        }
        if (f.getEntityManager().createQuery("SELECT e FROM Evidencias e WHERE e.evidencia IN :registrosEvidencias")
                .setParameter("registrosEvidencias", registrosEvidencias)
                .getResultList().isEmpty()) 
        {
            return true;
        } else {
            return false;
        }

    }
    
    @Override
    public List<ModulosRegistrosUsuarios> getListaPermisoPorRegistro(Integer clavePersonal,Short claveRegistro) {
        TypedQuery<ModulosRegistrosUsuarios> q = f.getEntityManager()
                .createQuery("SELECT m FROM ModulosRegistrosUsuarios m WHERE m.clavePersonal = :clavePersonal AND m.clave = :claveRegistro", ModulosRegistrosUsuarios.class);
        q.setParameter("clavePersonal", clavePersonal);
        q.setParameter("claveRegistro", claveRegistro);
        List<ModulosRegistrosUsuarios> li = q.getResultList();
        if(li.isEmpty() || li == null){
            return null;
        }else{
            return li;
        }
    }

    @Override
    public List<ModulosRegistrosUsuarios> getListaPermisoPorRegistroEjesDistintos(Integer clavePersonal, Short claveRegistro1, Short claveRegistro2) {
         TypedQuery<ModulosRegistrosUsuarios> q = f.getEntityManager()
                .createQuery("SELECT m FROM ModulosRegistrosUsuarios m WHERE m.clavePersonal = :clavePersonal AND (m.clave = :claveRegistro1 OR m.clave = :claveRegistro2)", ModulosRegistrosUsuarios.class);
        q.setParameter("clavePersonal", clavePersonal);
        q.setParameter("claveRegistro1", claveRegistro1);
        q.setParameter("claveRegistro2", claveRegistro2);
        List<ModulosRegistrosUsuarios> li = q.getResultList();
        if(li.isEmpty() || li == null){
            return null;
        }else{
            return li;
        }
    }

    @Override
    public void eliminarArchivoEvidencias(List<Integer> registrosEvidencias) {
         if (registrosEvidencias == null) {
        } else {
             TypedQuery<EvidenciasDetalle> q = f.getEntityManager()
                     .createQuery("SELECT ed FROM EvidenciasDetalle ed WHERE ed.evidencia.evidencia IN :registrosEvidencias", EvidenciasDetalle.class);
             q.setParameter("registrosEvidencias", registrosEvidencias);
             List<EvidenciasDetalle> led = q.getResultList();

             led.forEach((t) -> {
                 ServicioArchivos.eliminarArchivo(t.getRuta());
             });
        }
    }
    @Override
    public Registros buscaRegistroPorClave(Integer registro) {
        try {
            return f.getEntityManager().createQuery("SELECT r FROM Registros r WHERE r.registro = :registro",Registros.class)
                    .setParameter("registro", registro)
                    .getSingleResult();
        } catch (NonUniqueResultException | NoResultException e) {
            return null;
        }
    }

    @Override
    public PeriodosEscolares getPeriodoEscolarActual() {
        StoredProcedureQuery spq = f.getEntityManager().createStoredProcedureQuery("pye2.periodoEscolarActual", PeriodosEscolares.class);
        List<PeriodosEscolares> l = spq.getResultList();

        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l.get(0);
        }
    }

    @Override
    public Boolean validaEventoRegistro(EventosRegistros eventosRegistros, Integer eventoRegistro) {
        Integer evento = eventosRegistros.getEventoRegistro();
        if (Objects.equals(evento, eventoRegistro)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean comparaPeriodoRegistro(Integer periodoAnt, Integer periodoAct) {
       
        if (Objects.equals(periodoAnt, periodoAct)) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public List<EventosRegistros> getEventosPorPeriodo(PeriodosEscolares periodo) {
        if(periodo == null){
            return null;
        }
        List<String> meses = f.getEntityManager().createQuery("SELECT m FROM Meses m where m.numero BETWEEN :inicio AND :fin ORDER BY m.numero", Meses.class)
                .setParameter("inicio", periodo.getMesInicio().getNumero())
                .setParameter("fin", periodo.getMesFin().getNumero())
                .getResultList()
                .stream()
                .map(m -> m.getMes())
                .collect(Collectors.toList());

        List<EventosRegistros> l = f.getEntityManager().createQuery("SELECT er from EventosRegistros er INNER JOIN er.ejercicioFiscal ef WHERE ef.anio=:anio AND er.mes in :meses AND er.fechaInicio <= :fecha ORDER BY er.fechaInicio DESC, er.fechaFin DESC", EventosRegistros.class)
                .setParameter("fecha", new Date())
                .setParameter("anio", periodo.getAnio())
                .setParameter("meses", meses)
                .getResultList();
        return l;
    }
    
    @Override
    public PeriodosEscolares getPeriodoEscolarActivo() {
        try {
            EventosRegistros eventoRegistro = getEventoRegistro();
            return f.getEntityManager().createQuery("SELECT p FROM PeriodosEscolares p WHERE (p.anio = :anio) AND (:mes BETWEEN p.mesInicio.numero AND p.mesFin.numero)", PeriodosEscolares.class)
                    .setParameter("anio", eventoRegistro.getEjercicioFiscal().getAnio())
                    .setParameter("mes", getNumeroMes(eventoRegistro.getMes()))
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    @Override
    public Integer getNumeroMes(String mes) {
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
    public List<Short> getAreasDependientes(Short areaOperativa) {
        //verificar que el parametro no sea nulo
        if(areaOperativa == null){
            return null;
        }
        
        List<Short> areas = new ArrayList<>();
        
        //obtener la referencia al area operativa del trabajador
        AreasUniversidad area = f.getEntityManager().find(AreasUniversidad.class, areaOperativa);
        //comprobar si el area operativa es un programa educativo referenciar a su area superior para obtener la referencia al area academica
        Short programaCategoria = (short)ep.leerPropiedadEntera("modulosRegistroProgramaEducativoCategoria").orElse(9);
        if (Objects.equals(area.getCategoria().getCategoria(), programaCategoria)) {            
            area = f.getEntityManager().find(AreasUniversidad.class, area.getAreaSuperior());

            //Obtener las claves de todas las areas que dependan de área academicoa
            areas = f.getEntityManager().createQuery("SELECT au FROM AreasUniversidad au WHERE au.areaSuperior=:areaSuperior AND au.vigente='1'", AreasUniversidad.class)
                    .setParameter("areaSuperior", area.getArea())
                    .getResultStream()
                    .map(au -> au.getArea())
                    .collect(Collectors.toList());

        }else{//si no es Área Académica

            //Obtener las claves de todas las Áreas que dependan del Área del Usuario Logueado
            areas = f.getEntityManager().createQuery("SELECT au FROM AreasUniversidad au WHERE au.areaSuperior=:areaSuperior AND au.vigente='1'", AreasUniversidad.class)
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
    }
}
