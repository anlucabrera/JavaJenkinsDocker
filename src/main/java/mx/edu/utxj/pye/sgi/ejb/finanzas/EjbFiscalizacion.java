package mx.edu.utxj.pye.sgi.ejb.finanzas;

import java.time.Year;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.dto.Comision;
import mx.edu.utxj.pye.sgi.entity.ch.Actividades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.finanzas.ComisionOficios;
import mx.edu.utxj.pye.sgi.entity.finanzas.Facturas;
import mx.edu.utxj.pye.sgi.entity.finanzas.Tramites;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAreas;
import mx.edu.utxj.pye.sgi.entity.pye2.UsuariosRegistros;
import mx.edu.utxj.pye.sgi.util.XMLReader;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbFiscalizacion {
    /**
     * Consulta la lista de tramites que tiene a cargo la persona, sin importar que sea o no el comisionado,
     * ésto permite que personas de la misma área puedan dar seguimiento a los trámites de sus compañeros.
     * Tambien se agregan a la lista los trámites en los que la persona indicada es el comisionado, para poder
     * consultar sus trámites pasados o en curso, para dar seguimiento personalmente.
     * @param personal Persona que está a cargo de los trámites o que es comisionado en los trámites.
     * @return Devuelve la lista de los trámites ordenados por el mas reciente
     */
    public List<Tramites> getTramitesAcargo(Personal personal);
    
    /**
     * Consulta la lista de trámites del area solicitada.
     * @param area Área de la que de necesta conocer sus trámites
     * @return Regresa la lista de trámites del area solicitada.
     */
    public List<Tramites> getTramitesArea(Short area);
    
    /**
     * Inicializa una instancia de comisión con entidades persitentes.
     * @param generador Persona que genera la comisión y que automáticamente se trata como comisionado.
     * @return Comisión inicializada.
     */
    public Comision inicializarComision(Personal generador);
    
    /**
     * Guarda en base de datos el tramite solicitado desde la interface gráfica.
     * @param tramite Trámite a generar.
     * @param distancia Distancia redonda entre el origen y destino de la comisón.
     */
    public void guardarTramite(Tramites tramite, Double distancia);
    
    /**
     * Permite tranferir a otra persona el seguimiento del trámite para que le den seguimiento.
     * @param tramite Trámite a tranferir
     * @param personal Persona a quien se le tranfiere el trámite.
     */
    public void transferirTramite(Tramites tramite, Personal personal);
    
    /**
     * Permite cancelar la tranferencia de un trámite o hacer el proceso inverso para obtener el control del seguimiento del trámite.
     * @param id Clave númerica del trámite
     * @param personal Persona que desea obtener el trámite
     * @return Devuelve el trámite si obtenido si existe con el id especificado o null de lo contrario.
     */
    public Tramites obtenerTramite(Integer id, Personal personal);
    
    /**
     * Intenta eliminar el trámite en caso no haber interferido ya otra área, de lo contrario solo lo marcará como cancelado.
     * @param tramite Trámite a operar
     */
    public void eliminarOCancelarTramite(Tramites tramite);
    
    /**
     * Agrega un oficio de comisión al trámite
     * @param tramite Trámite en operación.
     * @param comisionOficio Oficio de comisión a agregar al trámite
     */
    public void registrarOficioComision(Tramites tramite, ComisionOficios comisionOficio);
    
    /**
     * Busca un trámite número de oficio.
     * @param oficio Número de oficio
     * @return Regresa la instancia del trámite en caso de existir o NULL de lo contrario.
     */
    public Tramites buscarPorOficio(String oficio);
    
    /**
     * Consulta la lista de areas con POA del eejercicio fiscal especificado.
     * @param ejercicio Ejercicio fiscal
     * @return Devuelve la lista de áreas.
     */
    public List<AreasUniversidad> getAreasConPOA(Short ejercicio);
    
    /**
     * Consulta la lista de ejes para el ejercicio fiscal correspondiente.
     * @param ejercicio Ejercicio fiscal, se recomienda el activo.
     * @param area
     * @return Devuelve la lista de ejes.
     */
    public List<EjesRegistro> getEjes(Short ejercicio, Short area);
    
    /**
     * Consulta la lista de estrategias correspondientes a un eje determidado.
     * @param eje Eje a consultar.
     * @param area
     * @return Devuelve la lista de estrategias.
     */
    public List<Estrategias> getEstrategiasPorEje(EjesRegistro eje, Short area);
    
    /**
     * Consulta la lista de líneas de acción correspondientes a una estrategia determinada.
     * @param estrategia Estrategia a consultar
     * @param area
     * @return Devuelve la lista de líneas de acción.
     */
    public List<LineasAccion> getLineasAccionPorEstrategia(Estrategias estrategia, Short area);
    
    /**
     * Consulta la lista de actividades correspondientes a una línea de acción determinada.
     * @param lineaaccion Línea de acción a consultar.
     * @param area
     * @return Devuelve la lista de actividades.
     */
    public List<ActividadesPoa> getActividadesPorLineaAccion(LineasAccion lineaaccion, Short area);
    
    /**
     * Obtiene el usuario SIIP a partir de la clave de un area de la institución.
     * @param claveArea Clave del área que se requiere saber su usuario en POA
     * @return Devuelve el usuario SIIP con POA
     */
    public UsuariosRegistros getUsuarioSIIP(Integer claveArea);
    
    /**
     * Consulta la lista de productos con recurso programado por actividad.
     * @param actividad Actividad a consultar sobre sus productos.
     * @return Lista de objetos resultantes de la consulta nativa.
     */
    public List<ProductosAreas> getProductosPlaneadosPorActividad(Actividades actividad);
    
    /**
     * Actualiza la lista de facturas del trámite correspondiente
     * @param tramite Trámite al que se le va actualizar su lista de facturas.
     * @return Lista de facturas del trámite
     */
    public List<Facturas> actualizarFacturasEnTramite(Tramites tramite);
    
    /**
     * Lee una factura desde el XML y regresa la instancia creada.
     * @param file Archivo xml subido a la plataforma. 
     * @return Instancia de factura creada sin persistir
     */
    public XMLReader leerFacturaXML(Part file);
    
    /**
     * Asocia la factura sin persistir a un trámite persistido
     * @param tramite Trámite persistido
     * @param factura Factura sin persistir
     * @param reader Lector de xml utilizado para validar la factura antes de persistir
     * @return Devuelve los resultados de validación ver {@link #validarFactura(mx.edu.utxj.pye.sgi.entity.finanzas.Tramites, mx.edu.utxj.pye.sgi.entity.finanzas.Facturas, mx.edu.utxj.pye.sgi.util.XMLReader) }
     */
    public Map.Entry<Boolean, String> guardarFactura(Tramites tramite, Facturas factura, XMLReader reader);
    
    /**
     * Valida los datos de la factura, cuando es una factura sin persistir, valida datos extras incluídos en el XML, de lo contrario
     * solo valida los datos de la instancia de la factura.
     * @param tramite Trámite persistido.
     * @param factura Factura a validar persistida o no
     * @param reader Lector del XML en caso de que sea una factura sin persistir para validar datos.
     * @return Devuelve una entrada de mapa con la clave boleana indicando si la validación es correcta o no y el valor un mensaje del resultado de la validación.
     */
    public Map.Entry<Boolean, String> validarFactura(Tramites tramite, Facturas factura, XMLReader reader);
    
    /**
     * Eliminar una factura y elimina la asociación con el trámite.
     * @param tramite Trámite persistido al que s ele eliminala factura
     * @param factura Factura a eliminar
     */
    public void eliminarFactura(Tramites tramite, Facturas factura);
    
    /**
     * Comprueba si la factura subida ya fue registrada por algún otro trámite, es caso de repetición toma como modo aceptado lo que no haya aceptado el otro trámite.
     * @param factura Factura a comprobar.
     * @return Regresa una entrada de mapa con clave-valor <TRUE-Lista de tramites> en caso de haber repetición, o <FALSE,NULL> de lo contrario.
     */
    public Map.Entry<Boolean, List<Tramites>> comprobarFacturaRepetida(Facturas factura);
    
    /**
     * Comprueba si la factura que se ha subido ya está registrada en el trámite actual
     * @param tramite Trámite en el que se va a comprobar
     * @param factura Factura a comprobar que no esté repetida.
     * @return Regresa TRUE si hay repetición, FALSE de lo contrario
     */
    public Boolean comprobarFacturaRepetida(Tramites tramite, Facturas factura);
    
    /**
     * Calcula el monto disponible de una factura con respecto el monto que no se ha aceptado por otro trámites que registraron la misma factura
     * @param tramites Lista de trámites que contienen la misma factura.
     * @param factura Factura a razonar.
     * @return Monto total disponible a aplicar en la factura
     */
    public Double calcularMontoDisponible(List<Tramites> tramites, Facturas factura);
    
    /**
     * Calcula el subtotal del monto aceptado para las facturas por viáticos diarios de una fecha específica.
     * @param facturas Lista de facturas a sumar sus montos aceptados.
     * @param factura Instancia de factura que aporta la fecha de expedición a filtrar.
     * @param soloAcumulado Si es TRUE sumará solo el total del acumulado de montos de facturas que aplican varios dias, 
     *                      de lo contrario tambien suma el monto de las facturas del dia.
     * @return Monto del subtotal por la fecha de expedición de la factura especificada.
     */
    public Double calcularSubtotalFacturasPorFecha(List<Facturas> facturas, Facturas factura, Boolean soloAcumulado);
    
    /**
     * Permite almacenar a la representación del CDFI a través de un archivo PDF de una factura.
     * @param file Archivo PDF subido.
     * @param factura Factura a la que se le está estableciendo su PDF.
     */
    public void almacenarCFDI(Part file, Facturas factura);
    
    /**
     * Permite almacenar al ticket o comprobante de consumo de una factura determinada.
     * @param file Archivo PDF, PNG o JPG subido.
     * @param tramite Tramite al que pertenece la factura, necesario para volver a validar la factura.
     * @param factura Factura a la que se le está estableciendo su comprobante de consumo.
     */
    public void almacenarTicket(Part file, Tramites tramite, Facturas factura);
    
    /**
     * Obtiene la lista de estados ordenados alfabeticamente.
     * @return Lista de estados.
     */
    public List<Estado> getEstados();
    
    /**
     * Obtiene la lista de municipios ordenados alfabéticamente y que pertenecen al estado especificado.
     * @param estado Estado que sirve para filtrar los municipios.
     * @return Lista de municipios por estado.
     */
    public List<Municipio> getMunicipiosPorEstado(Estado estado);
    
    /**
     * Permite obtener la referencia al jefe inmediato de una persona
     * @param subordinado Persona de quien se desea conocer su jefe inmediato.
     * @return Referencia del jefe inmediato o NULL si no tiene jefe.
     */
    public Personal getSuperior(Personal subordinado);
    
    /**
     * Genera la lista de posibles comisionados tomando en cuenta el área de la persona que le dá seguimiento y el área de la alineación al POA del trámite.
     * @param areaSeguimiento
     * @param areaAlineacion
     * @return Lista de posibles comisionados.
     */
    public List<Personal> getPosiblesComisionados(Short areaSeguimiento, Short areaAlineacion);
}
