package mx.edu.utxj.pye.sgi.ejb.finanzas;

import com.github.adminfaces.starter.infra.security.LogonMB;
import com.google.zxing.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.xml.parsers.ParserConfigurationException;

import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.finanzas.TramitesDto;
import mx.edu.utxj.pye.sgi.dto.finanzas.TramitesRolSupervisor;
import mx.edu.utxj.pye.sgi.ejb.EjbFileWritterBean;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.finanzas.*;
import mx.edu.utxj.pye.sgi.entity.pye2.*;
import mx.edu.utxj.pye.sgi.enums.*;
import mx.edu.utxj.pye.sgi.enums.converter.ComisionOficioEstatusConverter;
import mx.edu.utxj.pye.sgi.enums.converter.GastoTipoConverter;
import mx.edu.utxj.pye.sgi.enums.converter.TramiteTipoConverter;
import mx.edu.utxj.pye.sgi.facade.Facade;
import org.xml.sax.SAXException;

import static java.time.temporal.ChronoUnit.DAYS;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.dto.Comision;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Actividades;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.funcional.Validador;
import mx.edu.utxj.pye.sgi.funcional.ValidadorFactura;
import mx.edu.utxj.pye.sgi.funcional.ValidadorFacturaPDF;
import mx.edu.utxj.pye.sgi.util.DateUtils;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.sgi.util.XMLReader;
import nl.lcs.qrscan.core.QrPdf;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author UTXJ
 */
@Stateless
@MultipartConfig()
public class ServicioFiscalizacion implements EjbFiscalizacion {

    @EJB Facade f;
    @EJB EjbPropiedades ep;
    @EJB EjbDocumentosInternos ejbDocumentosInternos;
    @EJB EjbFileWritterBean ejbFileWritter;
    @Inject Caster caster;
    @Inject LogonMB logonMB;
    @Resource ManagedExecutorService exe;
    @EJB EjbPersonalBean ejbPersonalBean;
    private EntityManager em;

    @PostConstruct
    public void init() {
        em = f.getEntityManager();
    }
    @Override
    public List<TramitesDto> getTramitesAcargo(Personal personal) {
        return em.createQuery("SELECT t FROM Tramites t LEFT JOIN t.comisionOficios oc where t.clave=:clave OR oc.comisionado = :clave ORDER BY oc.oficio DESC", Tramites.class)
                .setParameter("clave", personal.getClave())
                .getResultStream()
                .map(tramites -> packTramite(tramites))
                .collect(Collectors.toList());
    }
    
    /*@Override
    public List<TramitesDto> getTramitesAcargoDto(Personal personal) {
//        System.err.println("La persona de quien se obtiene el listado es : " + personal);
        List<TramitesDto> ldto;
        TypedQuery<Tramites> qt = em.createQuery("SELECT t FROM Tramites t LEFT JOIN t.comisionOficios oc where t.clave=:clave OR oc.comisionado = :clave ORDER BY oc.oficio DESC", Tramites.class);
        qt.setParameter("clave", personal.getClave());
        List<Tramites> lt = qt.getResultList();
        if (lt.isEmpty() || lt == null) {
//            System.err.println("no encuentra tramites regresa null");
            return null;
        } else {
            ldto = lt.stream().map(tramites -> caster.tramiteToListaTramitesDto(tramites)).collect(Collectors.toList());
        }
        return ldto;
    }*/

    @Override
    public List<TramitesDto> getTramitesAreaDto(Short area) {
        List<TramitesDto> ldto;
        List<Integer> claves = em.createQuery("SELECT p FROM Personal p WHERE p.areaOperativa=:area", Personal.class)
                .setParameter("area", area)
                .getResultList()
                .stream()
                .map(p -> p.getClave())
                .collect(Collectors.toList());

        TypedQuery<Tramites> qt = em.createQuery("SELECT t FROM Tramites t LEFT JOIN t.comisionOficios oc where t.clave IN :claves OR oc.comisionado in :claves ORDER BY oc.oficio DESC", Tramites.class);
        qt.setParameter("claves", claves);
        List<Tramites> lt = qt.getResultList();
        if (lt.isEmpty() || lt == null) {
            return null;
        } else {
            ldto = lt.stream().map(tramites -> caster.tramiteToListaTramitesDto(tramites)).collect(Collectors.toList());
        }
        return ldto;
    }

    @Override
    public List<TramitesDto> getTramitesArea(Short area) {
        List<Integer> claves = em.createQuery("SELECT p FROM Personal p WHERE p.areaOperativa=:area", Personal.class)
                .setParameter("area", area)
                .getResultStream()
                .map(p -> p.getClave())
                .collect(Collectors.toList());

        return em.createQuery("SELECT t FROM Tramites t LEFT JOIN t.comisionOficios oc where t.clave IN :claves OR oc.comisionado in :claves ORDER BY oc.oficio DESC", Tramites.class)
                .setParameter("claves", claves)
                .getResultStream()
                .map(tramites -> {
                    PersonalActivo seguidor = ejbPersonalBean.pack(em.find(Personal.class, tramites.getClave()));
                    PersonalActivo comisionado = ejbPersonalBean.pack(em.find(Personal.class, tramites.getComisionOficios().getComisionado()));
                    return new TramitesDto(seguidor, comisionado, tramites);
                })
                .sorted(Comparator.comparing(t -> t.getTramite().getComisionOficios().getOficio(), Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public Comision inicializarComision(Personal generador) {
        Comision comision = new Comision(new Tramites());        
        comision.getTramite().setAlineacionTramites(new AlineacionTramites());
        comision.getTramite().setComisionOficios(new ComisionOficios());
        comision.getTramite().getComisionOficios().setComisionAvisos(new ComisionAvisos());
        comision.setTipo(TramiteTipo.COMISION);
        comision.setGastoTipo(GastoTipo.ANTICIPADO);
        comision.getTramite().getComisionOficios().setFechaGeneracion(new Date());
        comision.getTramite().getComisionOficios().getComisionAvisos().setZona((short)0);
        comision.getTramite().setFolio((short)0);
        comision.getTramite().getComisionOficios().getComisionAvisos().setComentarios("");
        comision.getTramite().setEstatus(TramiteEstatus.SOLICITADO.getLabel());
        comision.getTramite().setFechaInicio(new Date());
        comision.getTramite().getComisionOficios().setObservaciones("");
        comision.getTramite().getComisionOficios().setEstatus(ComisionOficioEstatus.SOLICITADO_POR_COMISIONADO.getLabel());
        comision.getTramite().getComisionOficios().setRuta("");
        comision.getTramite().setFechaLimite(DateUtils.asDate(LocalDate.now().plusDays(5)));
        comision.getTramite().setAnio((short)Year.now().getValue());
        comision.getTramite().setFolio((short)0);
        
        comision.setAlineacionArea(em.find(AreasUniversidad.class, (short)generador.getAreaOperativa()));
        comision.setAreas(getAreasConPOA(Short.valueOf(caster.getEjercicioFiscal())));
        comision.setAreaPOA(getAreaConPOA(generador.getAreaOperativa()));
        comision.setEjes(getEjes(Short.valueOf(caster.getEjercicioFiscal()), comision.getAreaPOA()));
        comision.setEstados(getEstados());
        comision.setComisionado(generador);
        comision.setPosiblesComisionados(getPosiblesComisionados((short)generador.getAreaOperativa(), (short)comision.getComisionado().getAreaOperativa()));
        comision.getTramite().getComisionOficios().setGenerador(generador.getClave());
        comision.getTramite().getComisionOficios().setOficio("");
        /*System.out.println("comision = " + comision.getTramite());
        System.out.println("oficio = " + comision.getTramite().getComisionOficios());
        System.out.println("superior = " + comision.getTramite().getComisionOficios().getSuperior());
        System.out.println("generador = " + generador);
        System.out.println("getSuperior() = " + getSuperior(generador));*/
        comision.getTramite().getComisionOficios().setSuperior(getSuperior(generador).getClave());
        comision.getTramite().getComisionOficios().setVistoBueno(null);
        comision.getTramite().setClave(generador.getClave()); //clave de la persona que da el seguimiento, puede o no ser la misma que el comisionado
        
        comision.setInicio(DateUtils.asDate(LocalDate.now().plusDays(1)));
        comision.setFin(comision.getInicio());
        comision.setPernoctando((short)0);
        comision.setSinPernoctar((short)1);
        comision.calcularDias();
        comision.setMunicipio(new Municipio(new MunicipioPK(21, 197)));
        comision.setEstado(new Estado(21));
        comision.setMunicipios(getMunicipiosPorEstado(comision.getEstado()));
        
        return comision;
    }

    @Override
    public Comision inicializarComision(Personal logueado, Integer tramiteid) {
        if(tramiteid == null) return null;

        Tramites tramite = em.find(Tramites.class, tramiteid);

        if(tramite != null){
            PersonalActivo comisionadoA = ejbPersonalBean.pack(tramite.getComisionOficios().getComisionado());
            AreasUniversidad areaPOA = comisionadoA.getAreaPOA();
            AreasUniversidad alineacionArea = em.find(AreasUniversidad.class, tramite.getAlineacionTramites().getArea());
            EjesRegistro alineacionEje = em.find(EjesRegistro.class, tramite.getAlineacionTramites().getEje());
            Estrategias alineacionEstrategia = em.find(Estrategias.class, tramite.getAlineacionTramites().getEstrategia());
            LineasAccion alineacionLinea = em.find(LineasAccion.class, tramite.getAlineacionTramites().getLineaAccion());
            ActividadesPoa alineacionActividad = em.find(ActividadesPoa.class, tramite.getAlineacionTramites().getActividad());
            Estado estado = em.find(Estado.class, tramite.getComisionOficios().getEstado());
            Municipio municipio = em.find(Municipio.class, new MunicipioPK(tramite.getComisionOficios().getEstado(), tramite.getComisionOficios().getMunicipio()));
            Personal comisionado = comisionadoA.getPersonal();
            Date inicio = tramite.getComisionOficios().getFechaComisionInicio();
            Date fin = tramite.getComisionOficios().getFechaComisionFin();
            Short pernoctando = tramite.getComisionOficios().getComisionAvisos().getPernoctando();
            Short sinPernoctar = tramite.getComisionOficios().getComisionAvisos().getSinPernoctar();
            GastoTipo gastoTipo = GastoTipoConverter.of(tramite.getGastoTipo());
            TramiteTipo tipo = TramiteTipoConverter.of(tramite.getTipo());
            List<ActividadesPoa> actividades = getActividadesPorLineaAccion(alineacionLinea, areaPOA, Short.valueOf(caster.getEjercicioFiscal()));
            List<AreasUniversidad> areas = tramite.getAlineacionTramites()!=null?getAreasConPOA(Short.valueOf(caster.getEjercicioFiscal())):Collections.EMPTY_LIST;
            List<EjesRegistro> ejes = getEjes(Short.valueOf(caster.getEjercicioFiscal()), areaPOA);
            List<Estrategias> estrategias = getEstrategiasPorEje(alineacionEje, areaPOA);
            List<LineasAccion> lineasAccion = getLineasAccionPorEstrategia(alineacionEstrategia, areaPOA);
            List<Estado> estados = getEstados();
            List<Municipio> municipios = getMunicipiosPorEstado(estado);
            List<Short> clavesAreasSubordinadas = getAreasSubordinadasSinPOA(comisionadoA.getAreaPOA())
                    .stream()
                    .map(a -> a.getArea())
                    .collect(Collectors.toList());//claves de areas subordinas que no tienes poa
            List<Personal> posiblesComisionados = getPosiblesComisionados(logueado.getAreaOperativa(), alineacionArea.getArea());

            Comision comision = new Comision(tramite, areaPOA, alineacionArea, alineacionEje, alineacionEstrategia, alineacionLinea, alineacionActividad,
                    estado, municipio, comisionado, inicio, fin, pernoctando, sinPernoctar, gastoTipo, tipo,
                    actividades, areas, ejes, estrategias, lineasAccion, estados, municipios,
                    clavesAreasSubordinadas, posiblesComisionados);

            return comision;
        }
        return null;
    }

    @Override
    public void guardarTramite(Tramites tramite, Double distancia) {
//        final Double tramitesTabuladorKilometrosMaximo = ep.leerPropiedadDecimal("tramitesTabuladorKilometrosMaximo").orElse(397);
//        Tarifas tarifaViatico = null;
//        if(distancia <= tramitesTabuladorKilometrosMaximo){
//            List<Tarifas> l = em.createQuery("SELECT t FROM Tarifas t INNER JOIN t.tarifasPorKilometro tk WHERE (:distancia BETWEEN tk.minimo AND tk.maximo) AND (t.fechaCancelacion IS NULL or t.fechaCancelacion <= :fechaActual)", Tarifas.class)
//                    .setParameter("distancia", distancia)
//                    .setParameter("fechaActual", (new Date()))
//                    .getResultList();
//            if(!l.isEmpty()) tarifaViatico = l.get(0);
//        }else{
//            final Integer rectorAreaOficial = ep.leerPropiedadEntera("rectorAreaOficial").orElse(1);
//            final Short directivoActividad = (short)ep.leerPropiedadEntera("directivoActividad").orElse(2);
//            final Integer tramitesNivelRestoPersonal = ep.leerPropiedadEntera("tramitesNivelRestoPersonal").orElse(3);
//
//            Personal comisionado = em.find(Personal.class, tramite.getComisionOficios().getComisionado());
//            Integer nivel = tramitesNivelRestoPersonal;
////            Boolean esLocal = false;
//
//            if(Objects.equals(comisionado.getActividad().getActividad(), directivoActividad)){//si es directivo
//                if(comisionado.getAreaOficial() == rectorAreaOficial){//si es el rector
//                    nivel = 1;
//                }else{
//                    nivel = 2;
//                }
//            }
//
////            if(oficio.getEstado() == 21){//si la comision es en puebla
////                esLocal = true;
////            }
//
//            List<Tarifas> l = em.createQuery("SELECT t FROM Tarifas t INNER JOIN t.tarifasPorZona tz WHERE tz.nivel=:nivel", Tarifas.class)
//                    .setParameter("nivel", nivel)
//                    .getResultList();
//
//            if(!l.isEmpty()){
//                tarifaViatico = l.get(0);
//            }
//        }
//
//        if(tarifaViatico != null){
//            tramite.getComisionOficios().getComisionAvisos().setTarifaViaticos(tarifaViatico);
//        }
        AlineacionTramites alineacion = tramite.getAlineacionTramites();
        ComisionOficios oficio = tramite.getComisionOficios();
        ComisionAvisos aviso = oficio.getComisionAvisos();

        if (buscarPorOficio(tramite.getComisionOficios().getOficio()) == null) {
            try {
                tramite.setAlineacionTramites(null);
                tramite.setComisionOficios(null);
                oficio.setComisionAvisos(null);
                oficio.setObservaciones("Ninguna");
                oficio.setRuta(ValorVacio.PENDIENTE.getLabel());

                f.create(tramite);
                f.refresh(tramite);
                alineacion.setTramite(tramite.getTramite());
                f.create(alineacion);
                oficio.setOficio(ejbDocumentosInternos.generarNumeroOficio(em.find(AreasUniversidad.class, alineacion.getArea()), ComisionOficios.class));
                oficio.setTramite(tramite.getTramite());
                f.create(oficio);
                aviso.setTramite(tramite.getTramite());
                f.create(aviso);

                oficio.setComisionAvisos(aviso);
                tramite.setAlineacionTramites(alineacion);
                tramite.setComisionOficios(oficio);
            } catch (Exception ex) {
                oficio.setComisionAvisos(aviso);
                tramite.setAlineacionTramites(alineacion);
                tramite.setComisionOficios(oficio);                
                LOG.log(Level.SEVERE, null, ex);
            }
        } else {
            f.edit(tramite);
        }
    }

    @Override
    public ResultadoEJB<Boolean> editarTramite(Tramites tramite) {
        try {
            f.edit(tramite.getComisionOficios().getComisionAvisos());
            f.edit(tramite.getComisionOficios());
            f.edit(tramite.getAlineacionTramites());
            f.edit(tramite);
            return ResultadoEJB.crearCorrecto(Boolean.TRUE, "El trámite se editó correctamente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al intentar editar el trámite.", e, Boolean.class);
        }
    }

    private static final Logger LOG = Logger.getLogger(ServicioFiscalizacion.class.getName());

    @Override
    public void transferirTramite(Tramites tramite, Personal personal) {
        tramite.setClave(personal.getClave());
        f.edit(tramite);
//        guardarTramite(tramite);
    }

    @Override
    public Tramites obtenerTramite(Integer id, Personal personal) {
        Tramites tramite = em.find(Tramites.class, id);
        if (tramite != null) {
            transferirTramite(tramite, personal);
        }

        return tramite;
    }

    @Override
    public void eliminarOCancelarTramite(Tramites tramite) {
        Predicate<Tramites> eliminable = (t) -> t.getEstatus().equals(TramiteEstatus.SOLICITADO.getLabel());

        f.setEntityClass(Tramites.class);
        if (eliminable.test(tramite)) {
            f.remove(tramite);
        } else {
            tramite.setEstatus(TramiteEstatus.CANCELADO.getLabel());
            f.edit(tramite);
        }
    }

    @Override
    public void registrarOficioComision(Tramites tramite, ComisionOficios comisionOficio) {
        tramite.setComisionOficios(comisionOficio);
        comisionOficio.setTramite(tramite.getTramite());

        f.setEntityClass(ComisionOficios.class);
        f.edit(comisionOficio);

        f.setEntityClass(Tramites.class);
        f.edit(tramite);
    }

    @Override
    public Tramites buscarPorOficio(String oficio) {
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.buscarPorAnioFolio() oficio: " + oficio);
        if(oficio.trim().isEmpty())
            return null;
        
        try {
            return em.createQuery("SELECT t FROM Tramites t INNER JOIN FETCH t.comisionOficios WHERE t.comisionOficios.oficio=:oficio", Tramites.class)
                    .setParameter("oficio", oficio)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<AreasUniversidad> getAreasConPOA(Short ejercicio) {
        List<Short> clavesAreas = em.createQuery("SELECT ap FROM ActividadesPoa ap INNER JOIN ap.cuadroMandoInt cm WHERE cm.ejercicioFiscal.anio=:ejercicio", ActividadesPoa.class)
                .setParameter("ejercicio", ejercicio)
                .getResultStream()
                .map(a -> a.getArea())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
//        System.out.println("clavesAreas = " + clavesAreas);

        return  em.createQuery("SELECT au FROM AreasUniversidad au INNER JOIN FETCH au.categoria WHERE au.area in :claves order by au.categoria.descripcion, au.nombre", AreasUniversidad.class)
                .setParameter("claves", clavesAreas)
                .getResultList();
    }

    @Override
    public List<EjesRegistro> getEjes(Short ejercicio, AreasUniversidad areaPOA) {
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.getEjes() ejercicio,areas: " + ejercicio + "," + areaPOA);
        return em
                .createQuery("SELECT e FROM EjesRegistro e INNER JOIN e.cuadroMandoIntegralList cmi INNER JOIN cmi.actividadesPoaList ac INNER JOIN cmi.ejercicioFiscal ef WHERE ac.area = :area AND ef.anio=:ejercicio ORDER BY e.nombre", EjesRegistro.class)
                .setParameter("area", areaPOA.getArea())
                .setParameter("ejercicio", ejercicio)
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Estrategias> getEstrategiasPorEje(EjesRegistro eje, AreasUniversidad areaPOA) {
        return em
                .createQuery("SELECT es FROM Estrategias es INNER JOIN es.cuadroMandoIntegralList cmi INNER JOIN cmi.actividadesPoaList ac INNER JOIN cmi.eje ej WHERE ac.area = :area AND ej.eje=:eje ORDER BY es.nombre", Estrategias.class)
                .setParameter("area", areaPOA.getArea())
                .setParameter("eje", eje.getEje())
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<LineasAccion> getLineasAccionPorEstrategia(Estrategias estrategia, AreasUniversidad areaPOA) {
        return em
                .createQuery("SELECT la FROM LineasAccion la INNER JOIN la.cuadroMandoIntegralList cmi INNER JOIN cmi.actividadesPoaList ac INNER JOIN cmi.estrategia es WHERE ac.area = :area AND es.estrategia=:estrategia ORDER BY la.nombre", LineasAccion.class)
                .setParameter("area", areaPOA.getArea())
                .setParameter("estrategia", estrategia.getEstrategia())
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<ActividadesPoa> getActividadesPorLineaAccion(LineasAccion lineaaccion, AreasUniversidad areaPOA, Short ejercicio) {
        try {
            return em
                    .createQuery("SELECT ac FROM ActividadesPoa ac INNER JOIN ac.cuadroMandoInt cmi INNER JOIN cmi.lineaAccion la WHERE ac.area = :area AND la.lineaAccion=:lineaAccion AND cmi.ejercicioFiscal.anio=:ejercicio ORDER BY ac.denominacion", ActividadesPoa.class)
                    .setParameter("area", areaPOA.getArea())
                    .setParameter("lineaAccion", lineaaccion.getLineaAccion())
                    .setParameter("ejercicio", ejercicio)
                    .getResultList()
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());
        }catch (NullPointerException e){
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public UsuariosRegistros getUsuarioSIIP(Integer claveArea) {
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.getUsuarioSIIP(): " + claveArea);
        try {
            return em.
                    createQuery("SELECT u FROM UsuariosRegistros u WHERE u.area=:area", UsuariosRegistros.class)
                    .setParameter("area", claveArea)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<ProductosAreas> getProductosPlaneadosPorActividad(Actividades actividad) {
        return em
                .createQuery("SELECT pa FROM ProductosAreas pa INNER JOIN pa.recursosActividadList ra INNER JOIN ra.actividadPoa ac INNER JOIN ac.cuadroMandoInt cmi INNER JOIN cmi.ejercicioFiscal ej WHERE ra.actividadPoa=:actividad AND ej.anio=:ejercicio", ProductosAreas.class)
//                .createNativeQuery("SELECT producto.clave, producto.nombre, producto.descripcion, recursoactividad.RP_Enero, recursoactividad.RP_Febero, recursoactividad.RP_Marzo, recursoactividad.RP_Abril, recursoactividad.RP_Mayo, recursoactividad.RP_Junio, recursoactividad.RP_Julio, recursoactividad.RP_Agosto, recursoactividad.RP_Septiembre, recursoactividad.RP_Octubre, recursoactividad.RP_Noviembre, recursoactividad.RP_Diciembre, recursoactividad.total, actividad.idactividad FROM producto INNER JOIN productoarea ON productoarea.idProducto = producto.clave AND productoarea.ejercicio = producto.ejercicio INNER JOIN recursoactividad ON recursoactividad.claveProducto = productoarea.idproductoArea INNER JOIN actividad ON recursoactividad.claveActividad = actividad.idactividad WHERE actividad.idactividad=:actividad AND producto.ejercicio = :ejercicio")
                .setParameter("actividad", actividad.getActividad())
                .setParameter("ejercicio", Year.now().getValue())
                .getResultList();
    }

    @Override
    public List<Facturas> actualizarFacturasEnTramite(Tramites tramite) {
        return em
                .createQuery("SELECT f FROM Facturas f INNER JOIN f.tramitesList t WHERE t.tramite=:tramite ORDER BY f.fechaAplicacion, f.fechaCoberturaInicio, f.fechaCoberturaFin, f.rfcEmisor", Facturas.class)
                .setParameter("tramite", tramite.getTramite())
                .getResultList();
    }

    @Override
    public XMLReader leerFacturaXML(Part file) {
        String ruta = ServicioArchivos.genRutaRelativa("facturas");
        String rutaArchivo;

        try {
            XMLReader xml = new XMLReader(file.getInputStream());
            List<XMLReader.Identificador> identificadores = new ArrayList<>();
            XMLReader.Identificador i01 = new XMLReader.Identificador("cfdi:Emisor", "Rfc");
            XMLReader.Identificador i02 = new XMLReader.Identificador("tfd:TimbreFiscalDigital", "UUID");
            XMLReader.Identificador i03 = new XMLReader.Identificador("cfdi:Comprobante", "Total");
            XMLReader.Identificador i04 = new XMLReader.Identificador("cfdi:Comprobante", "Fecha");
            XMLReader.Identificador i05 = new XMLReader.Identificador("cfdi:Emisor", "Nombre");
            XMLReader.Identificador i06 = new XMLReader.Identificador("cfdi:Concepto", "Descripcion", XMLReader.LecturaTipo.MULTIPLE);
            identificadores.add(i01);
            identificadores.add(i02);
            identificadores.add(i03);
            identificadores.add(i04);
            identificadores.add(i05);
            identificadores.add(i06);
            Map<XMLReader.Identificador, List<String>> valores = xml.leerValores(identificadores);
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.leerFacturaXML() map: " + valores);

            Facturas factura = new Facturas();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            factura.setRfcEmisor(valores.get(i01).get(0));
            factura.setTimbreFiscalUUID(valores.get(i02).get(0));
            factura.setMontoTotal(Double.parseDouble(valores.get(i03).get(0)));
            factura.setMontoAceptado(factura.getMontoTotal());
            factura.setFechaExpedicion(sdf.parse(valores.get(i04).get(0)));
            factura.setFechaCoberturaInicio(factura.getFechaExpedicion());
            factura.setFechaCoberturaFin(factura.getFechaExpedicion());
            factura.setFechaAplicacion(factura.getFechaExpedicion());
            factura.setRazonEmisor(valores.get(i05).get(0));
            factura.setConcepto(valores.get(i06).stream().collect(Collectors.joining(" ")));
            factura.setPartida("0000");
            factura.setXml("pendiente");
            factura.setViaticoDiario(true);
            factura.setComentariosPdf("Debe subir el archivo PDF de la representación del CDFI, el cual será validado que coincida con el XML.");
            xml.setFactura(factura);

            rutaArchivo = ruta.concat(factura.getTimbreFiscalUUID()).concat(".xml");
            ServicioArchivos.addCarpetaRelativa(ruta);
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            file.write(rutaArchivo);
//            file.write(factura.getTimbreFiscalUUID().concat(".xml"));

            ruta = ServicioArchivos.carpetaRaiz.concat(ServicioArchivos.genRutaRelativa("facturas"));
            factura.setXml(ruta.concat(factura.getTimbreFiscalUUID()).concat(".xml"));
            return xml;
        } catch (ParserConfigurationException | SAXException | IOException | ParseException ex) {
//            ServicioArchivos.eliminarArchivo(rutaArchivo);
            Logger.getLogger(ServicioFiscalizacion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public Map.Entry<Boolean, String> guardarFactura(Tramites tramite, Facturas factura, XMLReader reader) {
        Map.Entry<Boolean, String> validacion;

        f.setEntityClass(Facturas.class);
        if (!tramite.getFacturasList().contains(factura)) {
            tramite.getFacturasList().add(factura);
            f.create(factura);
            factura.getTramitesList().add(tramite);

            validacion = validarFactura(tramite, factura, reader);
        } else {
            validacion = validarFactura(tramite, factura, reader);
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.guardarFactura(" + validacion + ") validación sistema: " + factura.getValidacionSistema());
            validarMontoAceptado(factura);
            f.edit(factura);
        }

        f.setEntityClass(Tramites.class);
        if (factura.getTramitesList().contains(tramite)) {
            factura.getTramitesList().add(tramite);
        }
        f.edit(tramite);
//        f.flush();

        return validacion;
    }

    private void validarMontoAceptado(Facturas factura) {
        if (factura.getComentariosSistema() == null) {
            factura.setComentariosSistema("");
        }

        Map.Entry<Boolean, List<Tramites>> repetida = comprobarFacturaRepetida(factura);

        boolean ajustar = false;
        if (repetida.getKey()) {
            if (!factura.getComentariosSistema().isEmpty()) {
                factura.setComentariosSistema(factura.getComentariosSistema().concat("\n"));
            }

            if (repetida.getValue().size() > 1) {
                factura.setMontoAceptado(calcularMontoDisponible(repetida.getValue(), factura));
                String claves = repetida.getValue()
                        .stream()
                        .map(tr -> tr.getComisionOficios().getComisionado())
                        .map(clave -> clave.toString())
                        .collect(Collectors.joining(", "));
                factura.setComentariosSistema(factura.getComentariosSistema().concat("El monto aceptado de la factura se ajustó a " + factura.getMontoAceptado() + ", ya que el resto ya ha sido sido registrado por: " + claves + "."));
            } else {
                ajustar = true;
            }
        } else {
            ajustar = true;
        }

        if (ajustar) {
            if (factura.getMontoAceptado() > factura.getMontoTotal()) {
                factura.setMontoAceptado(factura.getMontoTotal());
            }
        }
    }

    @Override
    public Map.Entry<Boolean, String> validarFactura(Tramites tramite, Facturas factura, XMLReader reader) {
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.validarFactura()");
        Map<Boolean, String> map = new HashMap<>();

        final String facturasVersion = ep.leerPropiedadCadena("facturasVersion").orElse("");
        final String facturasRFCReceptor = ep.leerPropiedadCadena("facturasRFCReceptor").orElse("");
        final String facturasRazonReceptor = ep.leerPropiedadCadena("facturasRazonReceptor").orElse("");
        Validador<Facturas> validador = new ValidadorFactura(reader, factura, tramite.getComisionOficios(), facturasVersion, facturasRFCReceptor, facturasRazonReceptor);
        Future<Boolean> res = exe.submit((Callable<Boolean>) validador);
        try {
            boolean tengoElValor = res.isDone();
            while (!tengoElValor) {
                Thread.sleep(150);
                tengoElValor = res.isDone();
            }

            boolean esCorrecto = res.get();
            if (esCorrecto) {
                map.put(esCorrecto, "La validación de la factura es correcta.");
                return map.entrySet().stream().iterator().next();
            } else {
                map.put(esCorrecto, "La validación de la factura es incorrecta. " + factura.getComentariosSistema());
                return map.entrySet().stream().iterator().next();
            }
        } catch (InterruptedException | ExecutionException ex) {
            map.put(Boolean.FALSE, "Error interno en la ejecución de la tarea de validación, informe al administrador. (" + (new Date()) + ") " + ex.getClass().getName());
            return map.entrySet().stream().iterator().next();
        }
    }

    @Override
    public void eliminarFactura(Tramites tramite, Facturas factura) {
        f.setEntityClass(Facturas.class);
        factura = em.find(Facturas.class, factura.getFactura());
        if (tramite.getFacturasList().contains(factura)) {
            tramite.getFacturasList().remove(factura);
        }
        f.remove(factura);
        f.flush();
    }

    @Override
    public Map.Entry<Boolean, List<Tramites>> comprobarFacturaRepetida(Facturas factura) {
        List<Tramites> tramites = em.createQuery("SELECT t FROM Tramites t INNER JOIN t.facturasList f WHERE f.rfcEmisor=:rfc AND f.timbreFiscalUUID=:uuid", Tramites.class)
                .setParameter("rfc", factura.getRfcEmisor())
                .setParameter("uuid", factura.getTimbreFiscalUUID())
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());

        Map<Boolean, List<Tramites>> mapa = new HashMap<>();

        if (tramites.isEmpty()) {
            mapa.put(Boolean.FALSE, null);
        } else {
            mapa.put(Boolean.TRUE, tramites);
        }

        return mapa.entrySet().iterator().next();
    }

    @Override
    public Boolean comprobarFacturaRepetida(Tramites tramite, Facturas factura) {
        if (tramite.getFacturasList() == null || tramite.getFacturasList().isEmpty() || tramite.getFacturasList().size() == 1) {
            return Boolean.FALSE;
        }

        Long repetidos = tramite.getFacturasList()
                .stream()
                .filter(fa -> !fa.getFactura().equals(factura.getFactura()))
                .filter(fa -> fa.getTimbreFiscalUUID().equals(factura.getTimbreFiscalUUID()))
                .filter(fa -> fa.getRfcEmisor().equals(factura.getRfcEmisor()))
                .count();

        return repetidos > 0;
    }

    @Override
    public Double calcularMontoDisponible(List<Tramites> tramites, Facturas factura) {
        return tramites
                .stream()
                .map(t -> t.getFacturasList())
                .flatMap(l -> l.stream())
                .filter(fa -> fa.getTimbreFiscalUUID().equals(factura.getTimbreFiscalUUID()) && fa.getRfcEmisor().equals(factura.getRfcEmisor()))
                .distinct()
                .mapToDouble(fa -> fa.getMontoAceptado())
                .sum();
    }

    @Override
    public Double calcularSubtotalFacturasPorFecha(List<Facturas> facturas, Facturas factura, Boolean soloAcumulado) {
        List<Facturas> filtradas = facturas //que sean de la misma fecha y de viaticos diarios
                .stream()
                .filter(fa -> validarFecha(factura.getFechaAplicacion(), fa))
                .collect(Collectors.toList());

//        filtradas.stream().forEach(fi -> System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.calcularSubtotalFacturasPorFecha() filtrada: " + fi));
        Double suma = filtradas
                .stream()
                .mapToDouble(fa -> calcularMonto(fa, soloAcumulado))
                .sum();
        return suma;
    }

    private boolean validarFecha(Date fechaAplicacion, Facturas facturaCirculante) {
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.validarFecha() aplicación: " + fechaAplicacion + ", circulante: ->" + facturaCirculante.getFechaCoberturaInicio() + "," + facturaCirculante.getFechaCoberturaFin() + "<-");
        long inicio = facturaCirculante.getFechaCoberturaInicio().getTime();
        long fin = facturaCirculante.getFechaCoberturaFin().getTime();
        long aplicacion = facturaCirculante.getFechaAplicacion().getTime();
        long par = fechaAplicacion.getTime();
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.validarFecha() aplicación: " + par + ", circulante: ->" + inicio+ "," + fin + "<-");

        return facturaCirculante.getViaticoDiario() && (par >= inicio && par <= fin) && (aplicacion >= inicio && aplicacion <= fin);
    }

    private double calcularMonto(Facturas facturaCirculante, Boolean soloAcumulado) {
        if (facturaCirculante.getFechaCoberturaInicio().equals(facturaCirculante.getFechaCoberturaFin()) && soloAcumulado) {
            return 0d;
        }

        if (facturaCirculante.getFechaCoberturaInicio().equals(facturaCirculante.getFechaCoberturaFin())) {
            return facturaCirculante.getMontoAceptado();
        }

        LocalDate inicio = facturaCirculante.getFechaCoberturaInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fin = facturaCirculante.getFechaCoberturaFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Long diasEnFacturaCirculante = DAYS.between(inicio, fin) + 1;
//        if(diasEnFacturaCirculante > 1){
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.calcularMonto(" + diasEnFacturaCirculante + "): " + facturaCirculante);
//        }

        return facturaCirculante.getMontoAceptado() / diasEnFacturaCirculante;
    }

    @Override
    public void almacenarCFDI(Part file, Facturas factura) {
        try {
            String ruta = ServicioArchivos.genRutaRelativa("facturas");
            String rutaArchivo = ruta.concat(factura.getTimbreFiscalUUID()).concat(".").concat(FilenameUtils.getExtension(file.getSubmittedFileName()));
            ServicioArchivos.addCarpetaRelativa(ruta);
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            file.write(rutaArchivo);

            ruta = ServicioArchivos.carpetaRaiz.concat(ServicioArchivos.genRutaRelativa("facturas"));
            factura.setPdf(ruta.concat(factura.getTimbreFiscalUUID()).concat(".").concat(FilenameUtils.getExtension(file.getSubmittedFileName())));

            //verificar datos del QR
            QrPdf pdf = new QrPdf(Paths.get(factura.getPdf()));
            Stream.iterate(1, n -> n + 1).limit(pdf.getNumberOfPages()).forEach(n -> {
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.almacenarCFDI(1) page: " + 1);
                try {
                    String code = pdf.getQRCode(1, false, false);
                    if(code.startsWith("?")){
                        code = "https://verificacfdi.facturaelectronica.sat.gob.mx/default.aspx".concat(code);
                    }
//                    System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.almacenarCFDI() code: " + code);
                    factura.setLinkVerificacion(code);
                } catch (IOException | NotFoundException ex) {
                    Logger.getLogger(ServicioFiscalizacion.class.getName()).log(Level.SEVERE, null, ex);
                } catch(IllegalArgumentException ex){
//                    System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.almacenarCFDI(2) page: " + 1);
                    factura.setValidacionPdf(false);
                    factura.setLinkVerificacion(null);
                }
            });

            f.setEntityClass(Facturas.class);
            if (factura.getLinkVerificacion() != null && !factura.getLinkVerificacion().isEmpty()) {
                final String facturasRFCReceptor = ep.leerPropiedadCadena("facturasRFCReceptor").orElse("");
                Validador<Facturas> v = new ValidadorFacturaPDF(factura, facturasRFCReceptor);

                Future<Boolean> res = exe.submit((Callable<Boolean>) v);
                try {
                    boolean tengoElValor = res.isDone();
                    while (!tengoElValor) {
                        Thread.sleep(150);
                        tengoElValor = res.isDone();
                    }

                    boolean esCorrecto = res.get();
//                    System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.almacenarCFDI(" + esCorrecto + ") mensajes pdf: " + factura.getComentariosPdf());
                    factura.setValidacionPdf(esCorrecto);
                    f.edit(factura);
                } catch (InterruptedException | ExecutionException ex) {
                    factura.setComentariosPdf("Error interno en la ejecución de la tarea de validación, informe al administrador. (" + (new Date()) + ") " + ex.getClass().getName());
                }
            }else{
                factura.setComentariosPdf("No se pudo leer el código QR de la factura, intente convirtiendo el documento a escala de grises para impedir que otras franjas de colores impidan la lectura del código QR.");
            }
            f.edit(factura);
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.almacenarCFDI() mensajes pdf 2: " + factura.getComentariosPdf());
        } catch (IOException ex) {
            Logger.getLogger(ServicioFiscalizacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void almacenarTicket(Part file, Tramites tramite, Facturas factura) {
        try {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.almacenarTicket()");
            String ruta = ServicioArchivos.genRutaRelativa("facturas");
            String rutaArchivo = ruta.concat(factura.getTimbreFiscalUUID()).concat("_ticket.").concat(FilenameUtils.getExtension(file.getSubmittedFileName()));
            ServicioArchivos.addCarpetaRelativa(ruta);
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            file.write(rutaArchivo);

            ruta = ServicioArchivos.carpetaRaiz.concat(ServicioArchivos.genRutaRelativa("facturas"));
            factura.setTicket(ruta.concat(factura.getTimbreFiscalUUID()).concat("_ticket.").concat(FilenameUtils.getExtension(file.getSubmittedFileName())));
            f.setEntityClass(Facturas.class);
            f.edit(factura);
            f.flush();
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.almacenarTicket() ticket: " + factura.getTicket());
            validarFactura(tramite, factura, null);
            f.edit(factura);
        } catch (IOException ex) {
            Logger.getLogger(ServicioFiscalizacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ResultadoEJB<File> almacenarTramiteEvidencia(Part file, Tramites tramite) {
//        System.err.println("Ela archivo es : " + file.getName() + " y el tramite es : " + tramite);
        try {
            ResultadoEJB<File> resFile = ejbFileWritter.almacenarArchivo("comision_evidencia", caster.numeroOficioToPath(tramite.getComisionOficios()), file);
//            System.err.println("ServicioFiscalizacion.almacenarTramiteEvidencia resFile: " + resFile.getCorrecto());
            if (resFile.getCorrecto()) {
                tramite.getComisionOficios().setRuta(resFile.getValor().toString());
                f.edit(tramite.getComisionOficios());
                return resFile;
            }else{
                return  ResultadoEJB.crearErroneo(1,"No se pudo sincronizar la información de la evidencia de la comisión con la base de datos.",File.class);
            }
        } catch (Exception ex) {
            Logger.getLogger(ServicioFiscalizacion.class.getName()).log(Level.SEVERE, null, ex);
            return ResultadoEJB.crearErroneo(2, "El archivo no pudo almacenarse.", ex, File.class);//new ResultadoEJB("El archivo no puedo almecenarse: " + ex.getMessage(),ex, 1);
        }
    }

    @Override
    public List<Estado> getEstados() {
        return em.createQuery("SELECT e FROM Estado e WHERE e.idpais.idpais=:pais ORDER BY e.nombre", Estado.class)
                .setParameter("pais", 42)
                .getResultList();
    }

    @Override
    public List<Pais> getPaises() {
        return em.createQuery("SELECT p FROM Pais p ORDER BY p.nombre", Pais.class)
                .getResultList();
    }

    @Override
    public List<Estado> getEstadosPorPais(Pais pais) {
        if(pais == null)
            return Collections.EMPTY_LIST;
        return em.createQuery("SELECT e FROM Estado e WHERE e.idpais.idpais=:pais ORDER BY e.nombre", Estado.class)
                .setParameter("pais", pais.getIdpais())
                .getResultList();
    }

    @Override
    public List<Localidad> getLocalidadesPorMunicipio(Municipio municipio) {
        if(municipio == null)
            return Collections.EMPTY_LIST;
        return em.createQuery("SELECT l FROM Localidad l WHERE l.municipio.municipioPK.claveEstado =:estado AND l.municipio.municipioPK.claveMunicipio = :municipio ORDER BY l.nombre", Localidad.class)
                .setParameter("estado", municipio.getMunicipioPK().getClaveEstado())
                .setParameter("municipio", municipio.getMunicipioPK().getClaveMunicipio())
                .getResultList();
    }


    @Override
    public List<Municipio> getMunicipiosPorEstado(Estado estado) {
        if(estado == null)
            return Collections.EMPTY_LIST;

        return em.createQuery("SELECT m FROM Municipio m WHERE m.estado.idestado=:estado ORDER BY m.nombre", Municipio.class)
                .setParameter("estado", estado.getIdestado())
                .getResultList();
    }

    @Override
    public Personal getSuperior(Personal subordinado) {
        AreasUniversidad areaPOA = getAreaConPOA(subordinado.getAreaOperativa());

        if(areaPOA == null){
            return null;
        }

        /*System.out.println("subordinado.getAreaOperativa() = " + subordinado.getAreaOperativa());
//        System.out.println("areaPOA = " + areaPOA);*/
        List<Personal> l = em.createQuery("SELECT p FROM Personal p WHERE p.areaOperativa=:area AND p.actividad.actividad IN :actividades", Personal.class)
                .setParameter("area", areaPOA.getArea())
                .setParameter("actividades", Stream.of(2,4).collect(Collectors.toList()))
                .getResultList();

        if(l.isEmpty())
            return null;
        else
            return l.get(0);
    }

    @Override
    public List<Personal> getPosiblesComisionados(Short areaSeguimiento, Short areaAlineacion) {
        return em.createQuery("SELECT p FROM Personal p WHERE p.areaOperativa in :areas ORDER BY p.nombre", Personal.class)
                .setParameter("areas", Stream.of(areaAlineacion, areaSeguimiento).collect(Collectors.toList()))
                .getResultList();
    }
    
    @Override
    public AreasUniversidad getAreaConPOA(Short claveArea) {
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getAreaConPOA(1)");
        //obtener la referencia al area operativa del trabajador
        AreasUniversidad area = em.find(AreasUniversidad.class, claveArea);
        
        while(!area.getTienePoa()){
//            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getAreaConPOA() area: " + area);
            area = em.find(AreasUniversidad.class, area.getAreaSuperior());
        }
        
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getAreaConPOA(2)");
        return area;
    }

    /**
     * Obtiene la lista de areas con POA para filtrar los trámites por área
     *
     * @return Lista de áreas con POA
     */
    @Override
    public List<AreasUniversidad> getAreasConPOA() {
//        System.out.println("ServicioFiscalizacion.getAreasConPOA");
        return em.createQuery("select a from AreasUniversidad a where (a.areaSuperior is not null or a.area = 1) and a.tienePoa = true order by a.categoria.categoria, a.nombre", AreasUniversidad.class)
                .getResultList();
    }

    @Override
    public List<AreasUniversidad> getAreasSubordinadasSinPOA(AreasUniversidad areaPOA) {
        return em.createQuery("SELECT au FROM AreasUniversidad au WHERE au.areaSuperior=:areaSuperior AND au.vigente='1' AND au.tienePoa=:tienePoa", AreasUniversidad.class)
                .setParameter("areaSuperior", areaPOA.getArea())
                .setParameter("tienePoa", false)
                .getResultList()
                .stream()
                .collect(Collectors.toList());
//                .map(au -> au.getArea())
//                .collect(Collectors.toList());
    }
    
    @Override
    public Tramites seguirTramiteDeArea(Tramites tramite) {
        Tramites t = em.find(Tramites.class, tramite.getTramite());
        t.setClave(logonMB.getPersonal().getClave());
        f.edit(t);
        f.flush();
        return t;
    }

    /**
     * Permite que el comisionado o la persona que da seguimiento al trámite pueda liberarlo para que el superior o supervisor revisen
     *
     * @param tramite  Tramite a liberar
     * @param logueado Usuario logueado para verificar si se tiene permiso
     * @return Resultado del proceso
     */
    @Override
    public ResultadoEJB<TramitesDto> liberarTramitePorComisionado(TramitesDto tramite, Personal logueado) {
        try{
            if(Arrays.asList(tramite.getComisionado().getPersonal(), tramite.getPersonalSiguiendoTramite().getPersonal()).contains(logueado)) {
                f.edit(tramite.getTramite().getComisionOficios());
                f.edit(tramite.getTramite());
                tramite.setTramite(em.find(Tramites.class, tramite.getTramite().getTramite()));
                return  ResultadoEJB.crearCorrecto(tramite, "El trámite se liberó correctamente");
            }else{
                return ResultadoEJB.crearErroneo(1, "Usted no tiene permitido liberar el trámite.", TramitesDto.class);
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(2, "Ocurrió un error al intentar liberar el trámite.", e, TramitesDto.class);
        }


    }

    @Override
    public ResultadoEJB<Tramites> aprobarTramitePorSuperior(TramitesDto tramite) {
        try{
//            System.out.println("ServicioFiscalizacion.aprobarTramitePorSuperior");
            if(tramite.getComisionOficioEstatus().getLabel().equals(ComisionOficioEstatus.REGRESADO_PARA_REVISION_POR_SUPERIOR.getLabel()) && !hasObservaciones(tramite.getTramite().getComisionOficios())){
                ResultadoEJB<Tramites> res = ResultadoEJB.crearErroneo(2, "No puede rechazar la comisión sin indicar al comisionado la causa mediante las observaciones. La comisión será marcada como Solicitado por el comisionado para que usted repita la validación.", Tramites.class);
                res.setValor(tramite.getTramite());
                res.getValor().getComisionOficios().setEstatus(ComisionOficioEstatus.SOLICITADO_POR_COMISIONADO.getLabel());
                tramite.setTramite(res.getValor());
                f.edit(res.getValor());
//                System.out.println("res = " + res);
                return res;
            }

            Tramites t = tramite.getTramite();
            f.edit(t.getComisionOficios());
            t = em.find(Tramites.class, t.getTramite());
            ComisionOficioEstatus estatus = ComisionOficioEstatusConverter.of(t.getComisionOficios().getEstatus());
            String mensaje;
            switch (estatus){
                case APROBADO_POR_SUPERIOR: mensaje = "El trámite de aprobó correctamente."; break;
                case CAMBIOS_REALIZADOS_PARA_SUPERIOR: mensaje = "El trámite se marcó como si el comisionado hubiera solicitado revisión por usted."; break;
                case REGRESADO_PARA_REVISION_POR_SUPERIOR: mensaje = "El trámite se regresó al comisionado para que realicé los cambios solicitados en las observaciones."; break;
                default: mensaje = String.format("Se detectó el estado %s el cual no se esperaba en éste momento.", estatus.getLabel()); break;
            }
//            System.out.println("t.getComisionOficios().getEstatus() = " + t.getComisionOficios().getEstatus());
            return ResultadoEJB.crearCorrecto(t, mensaje);
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al intentar aprobar el trámite.", e, Tramites.class);
        }
    }

    /**
     * Consulta los tramites aprobados por los superiores y que el área supervisora pueda trabajar
     *
     * @param area Área con POA que sirve como filtro
     * @return Lista de trámites ordenados por fecha
     */
    @Override
    public List<TramitesDto> getTramites(AreasUniversidad area, TramitesSupervisorFiltro filtro) {
        List<TramitesDto> l = em.createQuery("select t from Tramites t inner join t.comisionOficios co inner join co.comisionAvisos ca left join t.alineacionTramites at where co.estatus in :estatus order by co.oficio desc ", Tramites.class)
                .setParameter("estatus", getEstadosParaSupervisor())
                .getResultStream()
                .map(tramites -> packTramite(tramites)).collect(Collectors.toList());
//        System.out.println("l = " + l);

        Predicate<TramitesDto> predicadoFiltro = t -> true;
        Predicate<TramitesDto> predicadoArea = t -> true;
        if(filtro != null){
            switch (filtro){
                case SIN_VALIDAR_COMISION: predicadoFiltro = t -> t.getComisionOficioEstatus().getId() < ComisionOficioEstatus.VALIDADO_POR_FISCALIZACION.getId(); break;
                case SIN_ASIGNAR_TARIFA: predicadoFiltro = t -> t.getTarifaViaje() == null || t.getTarifaViatico() == null; break;
                case SIN_VALIDAR_FACTURAS: predicadoFiltro = t -> t.getFacturas().stream().filter(f -> !f.getValidacionFiscalizacion()).count() > 0l; break;
                case SIN_FORMATOS_AUTORIZADOS: predicadoFiltro = t -> !t.getTramite().getComisionOficios().getComisionAvisos().getFormatosAutorizados(); break;
                case SIN_LIQUIDAR: predicadoFiltro = t -> t.getTramite().getComisionOficios().getComisionAvisos().getLiquidacionFecha() == null; break;
            }
        }

        if(area!=null){
            predicadoArea = t -> t.getTramite().getAlineacionTramites().getArea() == area.getArea();
        }

        return l.stream().filter(predicadoArea).filter(predicadoFiltro).collect(Collectors.toList());
    }

    private TramitesDto packTramite(Tramites tramites){
        PersonalActivo seguidor = ejbPersonalBean.pack(em.find(Personal.class, tramites.getClave()));
        PersonalActivo comisionado = ejbPersonalBean.pack(em.find(Personal.class, tramites.getComisionOficios().getComisionado()));
        TramitesDto t = new TramitesDto(seguidor, comisionado, tramites);
        t.setAlineacionArea(em.find(AreasUniversidad.class, tramites.getAlineacionTramites().getArea()));
        t.setAlineacionEje(em.find(EjesRegistro.class, tramites.getAlineacionTramites().getEje()));
        t.setAlineacionEstrategia(em.find(Estrategias.class, tramites.getAlineacionTramites().getEstrategia()));
        t.setAlineacionLinea(em.find(LineasAccion.class, tramites.getAlineacionTramites().getLineaAccion()));
        t.setAlineacionActividad(em.find(ActividadesPoa.class, tramites.getAlineacionTramites().getActividad()));
        return t;
    }

    private List<String> getEstadosParaSupervisor(){
        List<String> res = Stream
                .of(Arrays.asList(ComisionOficioEstatus.values()))
                .flatMap(l -> l.stream())
                .filter(e -> e.getId() >= ComisionOficioEstatus.APROBADO_POR_SUPERIOR.getId())
                .map(e -> e.getLabel())
                .collect(Collectors.toList());
//        System.out.println("ServicioFiscalizacion.getEstadosParaSupervisor");
//        System.out.println("res = " + res);
        return res;
    }

    /**
     * Valida la comisión de un trámite por el supervisor
     * @param rol Rol con Trámite a validar
     * @return Resultado del proceso
     */
    @Override
    public ResultadoEJB<Tramites> validarTramitePorSupervisor(TramitesRolSupervisor rol) {
        TramitesDto tramite = rol.getTramite();
        if(tramite.getComisionOficioEstatus().getLabel().equals(ComisionOficioEstatus.REGRESADO_PARA_REVISION_POR_FIZCALIZACION.getLabel()) && !hasObservaciones(tramite.getTramite().getComisionOficios())){
            ResultadoEJB<Tramites> res = ResultadoEJB.crearErroneo(2, "No puede rechazar la comisión sin indicar al comisionado la causa mediante las observaciones. La comisión será marcada como Aprobado por superior para que usted repita la validación.", Tramites.class);
            res.setValor(tramite.getTramite());
            res.getValor().getComisionOficios().setEstatus(ComisionOficioEstatus.APROBADO_POR_SUPERIOR.getLabel());
            tramite.setTramite(res.getValor());
            aprobarTramitePorSuperior(tramite);
            return res;
        }
        System.out.println("tramite.getTarifaViaje() = " + tramite.getTarifaViaje());
        System.out.println("tramite.getTarifaViatico() = " + tramite.getTarifaViatico());
        System.out.println("tramite.getTramite().getComisionOficios().getComisionAvisos().getTarifaViaje() = " + tramite.getTramite().getComisionOficios().getComisionAvisos().getTarifaViaje());
        tramite.setTarifaViatico(tramite.getTarifaViatico());
        tramite.setTarifaViaje(tramite.getTarifaViaje());
        if(tramite.getTramite().getComisionOficios().getComisionAvisos().getTarifaViaje().getTarifa() == null){
            System.out.println("rol.getOrigen() = " + rol.getOrigen());
            System.out.println("rol.getDestino() = " + rol.getDestino());
            Tarifas tarifa = tramite.getTramite().getComisionOficios().getComisionAvisos().getTarifaViaje();
            Tarifas tarifaBD = em.createQuery("select t from Tarifas t inner join t.tarifasViajes tv where tv.origen=:origen and tv.destino=:destino and t.fechaCancelacion is null", Tarifas.class)
                    .setParameter("origen", rol.getOrigen())
                    .setParameter("destino", rol.getDestino())
                    .getResultStream()
                    .findAny().orElse(null);
            System.out.println("tarifaBD = " + tarifaBD);
            if(tarifaBD == null){
                tarifa.setTipo(TarifaTipo.VIAJE.getLabel());
                tarifa.setFechaAplicacion(new Date());
                tarifa.setFechaCancelacion(null);
                tarifa.getTarifasViajes().setCostoCasetasViajeRedondo(0);
                tarifa.getTarifasViajes().setDestino(rol.getDestino());
                tarifa.getTarifasViajes().setLineasRecomendadas(null);
                tarifa.getTarifasViajes().setOrigen(rol.getDestino());
                TarifasViajes tarifasViajes = tarifa.getTarifasViajes();
                tarifa.setTarifasViajes(null);
                f.create(tarifa);
                tarifasViajes.setTarifa(tarifa.getTarifa());
                tarifa.setTarifasViajes(tarifasViajes);
                f.create(tarifasViajes);
                f.edit(tarifa);
            }else{
                rol.getTramite().setTarifaViaje(tarifaBD);
                f.edit(rol.getTramite().getTramite().getComisionOficios().getComisionAvisos());
            }

        }
        ResultadoEJB<Tramites> res = aprobarTramitePorSuperior(tramite);
        ComisionOficioEstatus estatus = ComisionOficioEstatusConverter.of(res.getValor().getComisionOficios().getEstatus());
        if(res.getCorrecto()){
            switch (estatus){
                case APROBADO_POR_SUPERIOR: res.setMensaje("Se marcó la validación de la comisión al estado que el superior del comisionado aprobó."); break;
                case REGRESADO_PARA_REVISION_POR_FIZCALIZACION: res.setMensaje("Se notificará al comisionado que realice los ajustes señalados en las observaciones."); break;
                case VALIDADO_POR_FISCALIZACION: res.setMensaje("La comisión se validó correctamente, ahora el comisionado podrá imprimir su oficio y pliego de comisión."); break;
            }
        }else{
            res.setMensaje("Ocurrió un error al intentar validar la comisión.");
        }

        return res;
    }

    private Boolean hasObservaciones(ComisionOficios oficio){
        if(oficio == null) return false;
        if(oficio.getObservaciones() == null) return false;
        if(oficio.getObservaciones().replaceAll("Ninguna", "").trim().isEmpty()) return false;

        return true;
    }

    /**
     * Permite sugerir la tarifa del viático en base a la distancia y al municipio que se comisionó
     *
     * @param rol DTO del rol del superior con los datos de entrada.
     * @return Resultado de tarifa sugerida.
     */
    @Override
    public ResultadoEJB<Tarifas> getTarifaViaticoSugerida(TramitesRolSupervisor rol) {
        Double distancia = rol.getDistancia();
//        System.out.println("distancia = " + distancia);
        MunicipioPK municipioPK = new MunicipioPK(rol.getTramite().getTramite().getComisionOficios().getEstado(), rol.getTramite().getTramite().getComisionOficios().getMunicipio());
//        System.out.println("municipioPK = " + municipioPK);
        Municipio municipio = em.find(Municipio.class, municipioPK);
        Double tramitesTabuladorKilometrosMaximo = ep.leerPropiedadDecimal("tramitesTabuladorKilometrosMaximo").orElse(397);

//        System.out.println("municipio = " + municipio);
//        System.out.println("tramitesTabuladorKilometrosMaximo = " + tramitesTabuladorKilometrosMaximo);
//        System.out.println("(municipio == null) = " + (municipio == null));
        if(municipio == null) {
            System.out.println("Error de municipio nulo");
            return ResultadoEJB.crearErroneo(1, String.format("No se reconoce el municipio: %s", municipioPK.toString()), Tarifas.class);
        }

        //verificar si el estado es puebla sugerir por kilometro o si la distancia no sobrepasa el limite de kilometros sugerir por kilometro
//        System.out.println("municipioPK.getClaveEstado() == 21 || distancia <= tramitesTabuladorKilometrosMaximo = " + (municipioPK.getClaveEstado() == 21 || distancia <= tramitesTabuladorKilometrosMaximo));
        if(municipioPK.getClaveEstado() == 21 || distancia <= tramitesTabuladorKilometrosMaximo){
            TarifasPorKilometro porKilometro = em.createQuery("select t from TarifasPorKilometro t where :distancia between t.minimo and t.maximo", TarifasPorKilometro.class)
                    .setParameter("distancia", distancia.shortValue())
                    .getResultStream().findAny().orElse(null);
//            System.out.println("porKilometro = " + porKilometro);

//            System.out.println("(porKilometro == null) = " + (porKilometro == null));
            if(porKilometro == null) {
//                System.out.println("Error tarifa por kilometro nulo.");
                return ResultadoEJB.crearErroneo(2, String.format("No hay una tarifa por kilómetro para la distancia %s.", distancia.toString()), Tarifas.class);
            }
            else {
//                System.out.println("Tarifa por kilometro encontrada:  " + porKilometro.getTarifas());
                return ResultadoEJB.crearCorrecto(porKilometro.getTarifas(), "Se localizó la tarifa por kilómetro adecuada.");
            }
        }

        //en caso de que la distancia sea mayor al limite de kilometros y no del estado de puebla determinar si el estado tiene aplicación total, sugerir la zonificación del estado
        rol.setZonificacionesEstado(em.find(ZonificacionesEstado.class, municipioPK.getClaveEstado()));
//        System.out.println("zonificacionesEstado = " + rol.getZonificacionesEstado());
        ZonificacionesMunicipioPK zonificacionesMunicipioPK = new ZonificacionesMunicipioPK(municipioPK.getClaveEstado(), municipioPK.getClaveMunicipio());
//        System.out.println("zonificacionesMunicipioPK = " + zonificacionesMunicipioPK);
        rol.setZonificacionesMunicipio(em.find(ZonificacionesMunicipio.class, zonificacionesMunicipioPK));
//        System.out.println("zonificacionesMunicipio = " + rol.getZonificacionesMunicipio());

        if(rol.getZonificacionesEstado() == null) {
//            System.out.println("Error por zonificacion de estado nula");
            return ResultadoEJB.crearErroneo(3, String.format("La zonificación del estado %s no ha sido localizada.", String.valueOf(municipioPK.getClaveEstado())), Tarifas.class);
        }

        rol.setNivelServidor(ejbPersonalBean.getNivelPersonal(rol.getTramite().getComisionado()));
//        System.out.println("nivelServidor = " + rol.getNivelServidor());
        TarifasPorZona porZona = em.createQuery("select t from TarifasPorZona t where  t.nivel.nivel=:nivel", TarifasPorZona.class)
                .setParameter("nivel", rol.getNivelServidor().getNivel())
                .getResultStream().findAny().orElse(null);
//        System.out.println("porZona = " + porZona);


        em.getEntityManagerFactory().getCache().evictAll();

//        System.out.println("(zonificacionesEstado.getAplicable().equals(ZonificacionEstadoAplicable.TOTAL) || zonificacionesMunicipio == null) = " + (rol.getZonificacionesEstado().getAplicable().equals(ZonificacionEstadoAplicable.TOTAL) || rol.getZonificacionesMunicipio() == null));
        if(rol.getZonificacionesEstado().getAplicable().equals(ZonificacionEstadoAplicable.TOTAL) || rol.getZonificacionesMunicipio() == null){
//            System.out.println("(porZona == null) = " + (porZona == null));
            if(porZona == null) {
//                System.out.println("Error por zona nula");
                return ResultadoEJB.crearErroneo(4, String.format("No se reconoce la tarifa para la zona: %s y nivel: %s.", String.valueOf(rol.getZonificacionesEstado().getZona()), rol.getNivelServidor().getNivel().toString()), Tarifas.class);
            }
            else {
                ResultadoEJB<Tarifas> res = ResultadoEJB.crearCorrecto(porZona.getTarifas(), "Se localizó la tarifa por estado y nivel.");
                res.setResultado((int)rol.getZonificacionesEstado().getZona());
//                System.out.println("res = " + res);
//                System.out.println("zonificacionesEstado.getZona() = " + rol.getZonificacionesEstado().getZona());
                rol.setZona(rol.getZonificacionesEstado().getZona());
                return res;
            }
        }else{//en caso que el estado sea aplicable de forma parcial, sugerir tarifa por municipio y estado
            if(rol.getZonificacionesMunicipio() == null) {
//                System.out.println("Error por zonificacion municipio nula");
                return ResultadoEJB.crearErroneo(5, String.format("No se reconoce la zonificación para el municipio %s.", zonificacionesMunicipioPK.toString()), Tarifas.class);
            }
            else{
                ResultadoEJB<Tarifas> res = ResultadoEJB.crearCorrecto(porZona.getTarifas(), "Se localizó la tarifa por municipio y nivel.");
                res.setResultado((int)rol.getZonificacionesMunicipio().getZona());
//                System.out.println("res = " + res);
//                System.out.println("zonificacionesMunicipio.getZona() = " + rol.getZonificacionesMunicipio().getZona());
                rol.setZona(rol.getZonificacionesMunicipio().getZona());
                return res;
            }
        }
    }

    @Override
    public ResultadoEJB<TramitesDto> asignarTarifas(TramitesRolSupervisor rol) {
        //asignar y o modificar tarifa de viatico ya sea por zona o por kilometro


        //asignar tarifa de viaje
        return null;
    }
}
