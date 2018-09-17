package mx.edu.utxj.pye.sgi.ejb.finanzas;

import com.google.zxing.NotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.xml.parsers.ParserConfigurationException;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.finanzas.ComisionOficios;
import mx.edu.utxj.pye.sgi.entity.finanzas.Facturas;
import mx.edu.utxj.pye.sgi.entity.finanzas.Tramites;
import mx.edu.utxj.pye.sgi.enums.TramiteEstatus;
import mx.edu.utxj.pye.sgi.facade.Facade;
import org.xml.sax.SAXException;

import static java.time.temporal.ChronoUnit.DAYS;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Stream;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.dto.Comision;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Actividades;
import mx.edu.utxj.pye.sgi.entity.finanzas.AlineacionTramites;
import mx.edu.utxj.pye.sgi.entity.finanzas.ComisionAvisos;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.MunicipioPK;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAreas;
import mx.edu.utxj.pye.sgi.entity.pye2.UsuariosRegistros;
import mx.edu.utxj.pye.sgi.enums.ComisionOficioEstatus;
import mx.edu.utxj.pye.sgi.enums.GastoTipo;
import mx.edu.utxj.pye.sgi.enums.TramiteTipo;
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
    @Inject Caster caster;
    @Resource ManagedExecutorService exe;

    @Override
    public List<Tramites> getTramitesAcargo(Personal personal) {
        return f.getEntityManager().createQuery("SELECT t FROM Tramites t LEFT JOIN t.comisionOficios oc where t.clave=:clave OR oc.comisionado = :clave ORDER BY oc.oficio DESC", Tramites.class)
                .setParameter("clave", personal.getClave())
                .getResultList();
    }

    @Override
    public List<Tramites> getTramitesArea(Short area) {
        List<Integer> claves = f.getEntityManager().createQuery("SELECT p FROM Personal p WHERE p.areaOperativa=:area", Personal.class)
                .setParameter("area", area)
                .getResultList()
                .stream()
                .map(p -> p.getClave())
                .collect(Collectors.toList());

        return f.getEntityManager().createQuery("SELECT t FROM Tramites t LEFT JOIN t.comisionOficios oc where t.clave IN :claves OR oc.comisionado in :claves ORDER BY oc.oficio DESC", Tramites.class)
                .setParameter("claves", claves)
                .getResultList();
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
        
        comision.setAlineacionArea(f.getEntityManager().find(AreasUniversidad.class, (short)generador.getAreaOperativa()));
        comision.setAreas(getAreasConPOA(Short.valueOf(caster.getEjercicioFiscal())));
        comision.setEjes(getEjes(Short.valueOf(caster.getEjercicioFiscal()), comision.getAreaPOA()));
        comision.setEstados(getEstados());
        comision.setComisionado(generador);
        comision.setPosiblesComisionados(getPosiblesComisionados((short)generador.getAreaOperativa(), (short)comision.getComisionado().getAreaOperativa()));
        comision.getTramite().getComisionOficios().setGenerador(generador.getClave());
        comision.getTramite().getComisionOficios().setOficio("");
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
    public void guardarTramite(Tramites tramite, Double distancia) {
//        final Double tramitesTabuladorKilometrosMaximo = ep.leerPropiedadDecimal("tramitesTabuladorKilometrosMaximo").orElse(397);
//        Tarifas tarifaViatico = null;
//        if(distancia <= tramitesTabuladorKilometrosMaximo){
//            List<Tarifas> l = f.getEntityManager().createQuery("SELECT t FROM Tarifas t INNER JOIN t.tarifasPorKilometro tk WHERE (:distancia BETWEEN tk.minimo AND tk.maximo) AND (t.fechaCancelacion IS NULL or t.fechaCancelacion <= :fechaActual)", Tarifas.class)
//                    .setParameter("distancia", distancia)
//                    .setParameter("fechaActual", (new Date()))
//                    .getResultList();
//            if(!l.isEmpty()) tarifaViatico = l.get(0);
//        }else{
//            final Integer rectorAreaOficial = ep.leerPropiedadEntera("rectorAreaOficial").orElse(1);
//            final Short directivoActividad = (short)ep.leerPropiedadEntera("directivoActividad").orElse(2);
//            final Integer tramitesNivelRestoPersonal = ep.leerPropiedadEntera("tramitesNivelRestoPersonal").orElse(3);
//
//            Personal comisionado = f.getEntityManager().find(Personal.class, tramite.getComisionOficios().getComisionado());
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
//            List<Tarifas> l = f.getEntityManager().createQuery("SELECT t FROM Tarifas t INNER JOIN t.tarifasPorZona tz WHERE tz.nivel=:nivel", Tarifas.class)
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

                f.create(tramite);
                f.refresh(tramite);
                alineacion.setTramite(tramite.getTramite());
                f.create(alineacion);
                oficio.setOficio(ejbDocumentosInternos.generarNumeroOficio(f.getEntityManager().find(AreasUniversidad.class, alineacion.getArea()), ComisionOficios.class));
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
    private static final Logger LOG = Logger.getLogger(ServicioFiscalizacion.class.getName());

    @Override
    public void transferirTramite(Tramites tramite, Personal personal) {
        tramite.setClave(personal.getClave());
        f.edit(tramite);
//        guardarTramite(tramite);
    }

    @Override
    public Tramites obtenerTramite(Integer id, Personal personal) {
        Tramites tramite = f.getEntityManager().find(Tramites.class, id);
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
        System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.buscarPorAnioFolio() oficio: " + oficio);
        if(oficio.trim().isEmpty())
            return null;
        
        try {
            return f.getEntityManager().createQuery("SELECT t FROM Tramites t INNER JOIN FETCH t.comisionOficios WHERE t.comisionOficios.oficio=:oficio", Tramites.class)
                    .setParameter("oficio", oficio)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public static void main(String[] args) {
        String estado = "Solicitado";
        System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.main() solicitado: " + (estado.equals(TramiteEstatus.SOLICITADO.getLabel())));
    }

    @Override
    public List<AreasUniversidad> getAreasConPOA(Short ejercicio) {
        List<Short> clavesAreas = f.getEntityManager().createQuery("SELECT ap FROM ActividadesPoa ap INNER JOIN ap.cuadroMandoInt cm WHERE cm.ejercicioFiscal.anio=:ejercicio", ActividadesPoa.class)
                .setParameter("ejercicio", ejercicio)
                .getResultStream()
                .map(a -> a.getArea())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        return  f.getEntityManager().createQuery("SELECT au FROM AreasUniversidad au INNER JOIN FETCH au.categoria WHERE au.area in :claves order by au.categoria.descripcion, au.nombre", AreasUniversidad.class)
                .setParameter("claves", clavesAreas)
                .getResultList();
    }

    @Override
    public List<EjesRegistro> getEjes(Short ejercicio, AreasUniversidad areaPOA) {
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.getEjes() ejercicio,areas: " + ejercicio + "," + areaPOA);
        return f.getEntityManager()
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
        return f.getEntityManager()
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
        return f.getEntityManager()
                .createQuery("SELECT la FROM LineasAccion la INNER JOIN la.cuadroMandoIntegralList cmi INNER JOIN cmi.actividadesPoaList ac INNER JOIN cmi.estrategia es WHERE ac.area = :area AND es.estrategia=:estrategia ORDER BY la.nombre", LineasAccion.class)
                .setParameter("area", areaPOA.getArea())
                .setParameter("estrategia", estrategia.getEstrategia())
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<ActividadesPoa> getActividadesPorLineaAccion(LineasAccion lineaaccion, AreasUniversidad areaPOA) {
        return f.getEntityManager()
                .createQuery("SELECT ac FROM ActividadesPoa ac INNER JOIN ac.cuadroMandoInt cmi INNER JOIN cmi.lineaAccion la WHERE ac.area = :area AND la.lineaAccion=:lineaAccion ORDER BY ac.denominacion", ActividadesPoa.class)
                .setParameter("area", areaPOA.getArea())
                .setParameter("lineaAccion", lineaaccion.getLineaAccion())
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public UsuariosRegistros getUsuarioSIIP(Integer claveArea) {
        System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.getUsuarioSIIP(): " + claveArea);
        try {
            return f.getEntityManager().
                    createQuery("SELECT u FROM UsuariosRegistros u WHERE u.area=:area", UsuariosRegistros.class)
                    .setParameter("area", claveArea)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<ProductosAreas> getProductosPlaneadosPorActividad(Actividades actividad) {
        return f.getEntityManager()
                .createQuery("SELECT pa FROM ProductosAreas pa INNER JOIN pa.recursosActividadList ra INNER JOIN ra.actividadPoa ac INNER JOIN ac.cuadroMandoInt cmi INNER JOIN cmi.ejercicioFiscal ej WHERE ra.actividadPoa=:actividad AND ej.anio=:ejercicio", ProductosAreas.class)
//                .createNativeQuery("SELECT producto.clave, producto.nombre, producto.descripcion, recursoactividad.RP_Enero, recursoactividad.RP_Febero, recursoactividad.RP_Marzo, recursoactividad.RP_Abril, recursoactividad.RP_Mayo, recursoactividad.RP_Junio, recursoactividad.RP_Julio, recursoactividad.RP_Agosto, recursoactividad.RP_Septiembre, recursoactividad.RP_Octubre, recursoactividad.RP_Noviembre, recursoactividad.RP_Diciembre, recursoactividad.total, actividad.idactividad FROM producto INNER JOIN productoarea ON productoarea.idProducto = producto.clave AND productoarea.ejercicio = producto.ejercicio INNER JOIN recursoactividad ON recursoactividad.claveProducto = productoarea.idproductoArea INNER JOIN actividad ON recursoactividad.claveActividad = actividad.idactividad WHERE actividad.idactividad=:actividad AND producto.ejercicio = :ejercicio")
                .setParameter("actividad", actividad.getActividad())
                .setParameter("ejercicio", Year.now().getValue())
                .getResultList();
    }

    @Override
    public List<Facturas> actualizarFacturasEnTramite(Tramites tramite) {
        return f.getEntityManager()
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
//            Comparable<Facturas> comparable = new ComparadorFechas(factura.getFechaAplicacion());
//            int res = comparable.compareTo(factura);
//            if(res != 0){ //si la fecha de inicio es posterior a la de fin o si la fecha de aplicación no está entre la fecha de inicio y fin
//                factura.setFechaCoberturaInicio(factura.getFechaAplicacion());
//                factura.setFechaCoberturaFin(factura.getFechaAplicacion());
//            }

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
        f.flush();

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
        System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.validarFactura()");
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
        factura = f.getEntityManager().find(Facturas.class, factura.getFactura());
        if (tramite.getFacturasList().contains(factura)) {
            tramite.getFacturasList().remove(factura);
        }
        f.remove(factura);
        f.flush();
    }

    @Override
    public Map.Entry<Boolean, List<Tramites>> comprobarFacturaRepetida(Facturas factura) {
        List<Tramites> tramites = f.getEntityManager().createQuery("SELECT t FROM Tramites t INNER JOIN t.facturasList f WHERE f.rfcEmisor=:rfc AND f.timbreFiscalUUID=:uuid", Tramites.class)
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
                    System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.almacenarCFDI(" + esCorrecto + ") mensajes pdf: " + factura.getComentariosPdf());
                    factura.setValidacionPdf(esCorrecto);
                    f.edit(factura);
                } catch (InterruptedException | ExecutionException ex) {
                    factura.setComentariosPdf("Error interno en la ejecución de la tarea de validación, informe al administrador. (" + (new Date()) + ") " + ex.getClass().getName());
                }
            }else{
                factura.setComentariosPdf("No se pudo leer el código QR de la factura, intente convirtiendo el documento a escala de grises para impedir que otras franjas de colores impidan la lectura del código QR.");
            }
            f.edit(factura);
            System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioFiscalizacion.almacenarCFDI() mensajes pdf 2: " + factura.getComentariosPdf());
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
    public List<Estado> getEstados() {
        return f.getEntityManager().createQuery("SELECT e FROM Estado e WHERE e.idpais.idpais=:pais ORDER BY e.nombre", Estado.class)
                .setParameter("pais", 42)
                .getResultList();
    }

    @Override
    public List<Municipio> getMunicipiosPorEstado(Estado estado) {
        if(estado == null)
            return Collections.EMPTY_LIST;

        return f.getEntityManager().createQuery("SELECT m FROM Municipio m WHERE m.estado.idestado=:estado ORDER BY m.nombre", Municipio.class)
                .setParameter("estado", estado.getIdestado())
                .getResultList();
    }

    @Override
    public Personal getSuperior(Personal subordinado) {
        List<Personal> l = f.getEntityManager().createQuery("SELECT p FROM Personal p WHERE p.areaOperativa=:area AND p.actividad.actividad IN :actividades", Personal.class)
                .setParameter("area", subordinado.getAreaOperativa())
                .setParameter("actividades", Stream.of(2,4).collect(Collectors.toList()))
                .getResultList();

        if(l.isEmpty())
            return null;
        else
            return l.get(0);
    }

    @Override
    public List<Personal> getPosiblesComisionados(Short areaSeguimiento, Short areaAlineacion) {
        return f.getEntityManager().createQuery("SELECT p FROM Personal p WHERE p.areaOperativa in :areas ORDER BY p.nombre", Personal.class)
                .setParameter("areas", Stream.of(areaAlineacion, areaSeguimiento).collect(Collectors.toList()))
                .getResultList();
    }
    
    @Override
    public AreasUniversidad getAreaConPOA(Short claveArea) {
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getAreaConPOA(1)");
        //obtener la referencia al area operativa del trabajador
        AreasUniversidad area = f.getEntityManager().find(AreasUniversidad.class, claveArea);
        
        while(!area.getTienePoa()){
//            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getAreaConPOA() area: " + area);
            area = f.getEntityManager().find(AreasUniversidad.class, area.getAreaSuperior());
        }
        
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getAreaConPOA(2)");
        return area;
    }

    @Override
    public List<Short> getAreasSubordinadasSinPOA(AreasUniversidad areaPOA) {
        return f.getEntityManager().createQuery("SELECT au FROM AreasUniversidad au WHERE au.areaSuperior=:areaSuperior AND au.vigente='1' AND au.tienePoa=:tienePoa", AreasUniversidad.class)
                .setParameter("areaSuperior", areaPOA.getArea())
                .setParameter("tienePoa", false)
                .getResultStream()
                .map(au -> au.getArea())
                .collect(Collectors.toList());
    }

}
