package mx.edu.utxj.pye.sgi.ejb.finanzas;

import java.io.Serializable;
import java.time.Year;
import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 * Provee servicios para generar o consultar datos de documentos internos como oficios de comisión, memorándum, oficios, etc.
 * @author UTXJ
 */
@Local
public interface EjbDocumentosInternos  extends Serializable{
    /**
     * Genera un nuevo número de oficio en el formato "UTXJ-AREA/0000/AA"
     * @param <T> Tipo de clase del documento.
     * @param area Area de la institución que solicita el número de oficio y que sus siglas serán incluídas en el número a generar
     * @param clase Tipo de clase del documento.
     * @return Número de oficio en el formato prestablecido.
     */
    public <T> String generarNumeroOficio(AreasUniversidad area, Class<T> clase);
    
    /**
     * Verifica si un número de oficio ya existe.
     * @param <T> Tipo de clase del documento.
     * @param area Área que solicita la verificación de su número de oficio.
     * @param anio Año de generación del oficio.
     * @param numero Número del Oficio.
     * @param clase Tipo de clase del documento.
     * @return Devuelve la instancia del oficio si existe o NULL de lo contrario.
     */
    public <T> T verificarNumeroOficio(AreasUniversidad area, Year anio, Short numero, Class<T> clase);
    
    /**
     * Verifica si un número de oficio ya existe.
     * @param <T> Tipo de clase del documento.
     * @param numeroOficio Número de oficio a comprobar.
     * @param clase Clase del documento.
     * @return Devuelve la instancia del oficio si existe o NULL de lo contrario.
     */
    public <T> T verificarNumeroOficio(String numeroOficio, Class<T> clase);
    
    /**
     * Obtiene el Área a partir de un tipo de documento y número de oficio.
     * @param <T> Tipo de clase del documento.
     * @param numeroOficio Número del oficio.
     * @param clase Clase del documento.
     * @return Devuelve la instancia del Área que generó el oficio o nulo si no se cumple el formato.
     */
    public <T> AreasUniversidad getAreaEnOficio(String numeroOficio, Class<T> clase);
    
    /**
     * Obtiene el Año en que fue generado un oficio según su número.
     * @param <T> Tipo de clase del documento.
     * @param numeroOficio Número del oficio.
     * @param clase Clase del documento.
     * @return Devuelve el año de generación del oficio o nulo si los dos últimos dígitos de número de oficio no se pueden convertir en Año.
     */
    public <T> Year getAnioEnOficio(String numeroOficio, Class<T> clase);
    
    /**
     * Obtiene el valor númerico consecutivo a partir de la cadena de un número de oficio.
     * @param <T> Tipo de clase del documento.
     * @param numeroOficio Cadena del número de oficio.
     * @param clase Clase del documento.
     * @return Devuelve el número consecutivo del oficio o nulo si no se cumple el formato establecido para el número de oficio.
     */
    public <T> Short getNumeroEnOficio(String numeroOficio, Class<T> clase);
    
    /**
     * Registra en la base de datos la información y crea el archivo correspondiente al oficio segun su tipo, 
     * las plantillas deben estar guardadas en la ruta "/archivos/plantillas/"
     * con el prefijo según su tipo.
     * @param <T> Tipo de clase del documento.
     * @param oficio Datos a imprimir en el documento.
     * @return Devuelve la ruta de descarga del archivo.
     */
    public <T> String construirOficio(T oficio);
    
    /**
     * Consulta la lista de oficios según el tipo especificado
     * @param <T> Tipo de clase del documento
     * @param clase
     * @param anio
     * @return 
     */
    public <T> List<T> getListaOficios(Class<T> clase, Year anio);
}
