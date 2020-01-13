package mx.edu.utxj.pye.sgi.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.finanzas.Facturas;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Permite la lectura de información en un XML
 * @author UTXJ
 */
public class XMLReader {
    private Document doc;
    @Getter @Setter private Facturas factura;
    
    public XMLReader(InputStream is) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(is);
        doc.getDocumentElement().normalize();
    }
    
    /**
     * Lee valores de un archivo XML con 
     * @param identificadores Lista de identificadores de los valores a leer.
     * @return Devuelve un mapa con los valores leídos almacenando en la clave el identificador del valores leído y 
     *          en el valor una lista con un elemento para valores único o con mas elementos para valores múltiples, 
     *          el valor del mapa será una lista vacia en caso de no encontrar valor alguno.
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    public Map<Identificador,List<String>> leerValores(List<Identificador> identificadores) throws ParserConfigurationException, SAXException, IOException{
        final Map<Identificador, List<String>> map = new HashMap<>();
        
        identificadores.parallelStream().forEach(i -> {
            try{
                System.out.println("mx.edu.utxj.pye.sgi.util.XMLReader.leerValores() i: " + i);
                if(i.getTipo().equals(IdentificadorTipo.ETIQUETA)){
                    if(i.getLecturaTipo().equals(LecturaTipo.UNICA)){
                        if(doc.getElementsByTagName(i.getEtiqueta()) != null && doc.getElementsByTagName(i.getEtiqueta()).getLength() > 0){
                            String valor = doc.getElementsByTagName(i.getEtiqueta()).item(0).getNodeValue().trim();
                            map.put(i, Stream.of(valor).collect(Collectors.toList()));
                        }
                    } else if(i.getLecturaTipo().equals(LecturaTipo.MULTIPLE)){
                        if(doc.getElementsByTagName(i.getEtiqueta()) != null && doc.getElementsByTagName(i.getEtiqueta()).getLength() > 0){
                            map.put(i, new ArrayList<>());
                            for(int x = 0; x < doc.getElementsByTagName(i.getEtiqueta()).getLength();x++){
                                map.get(i).add(doc.getElementsByTagName(i.getEtiqueta()).item(x).getNodeValue().trim());
                            }
                        }
                    }
                }else if(i.getTipo().equals(IdentificadorTipo.ATRIBUTO)){
                    if(i.getLecturaTipo().equals(LecturaTipo.UNICA)){
                        System.out.println("mx.edu.utxj.pye.sgi.util.XMLReader.leerValores() etiqueta: " + doc.getElementsByTagName(i.getEtiqueta()));
                        if(doc.getElementsByTagName(i.getEtiqueta()) != null && doc.getElementsByTagName(i.getEtiqueta()).getLength() > 0){
                            String valor = doc.getElementsByTagName(i.getEtiqueta()).item(0).getAttributes().getNamedItem(i.getAtributo()).getNodeValue().trim();
                            map.put(i, Stream.of(valor).collect(Collectors.toList()));
                            System.out.println("mx.edu.utxj.pye.sgi.util.XMLReader.leerValores(): " + map.get(i));
                        }
                    } else if(i.getLecturaTipo().equals(LecturaTipo.MULTIPLE)){
                        if(doc.getElementsByTagName(i.getEtiqueta()) != null && doc.getElementsByTagName(i.getEtiqueta()).getLength() > 0){
                            map.put(i, new ArrayList<>());
                            for(int x = 0; x < doc.getElementsByTagName(i.getEtiqueta()).getLength();x++){
                                map.get(i).add(doc.getElementsByTagName(i.getEtiqueta()).item(x).getAttributes().getNamedItem(i.getAtributo()).getNodeValue().trim());
                            }
                        }
                    }
                }

                if (map.get(i) == null) {
                    map.put(i, Collections.EMPTY_LIST);
                }
            }catch(DOMException e){
                map.put(i, Collections.EMPTY_LIST);
                LOG.log(Level.SEVERE, null, e);
            }
        });
        
        return map;
    }
    private static final Logger LOG = Logger.getLogger(XMLReader.class.getName());
    
    /**
     * Permite especificar el identificador para especificar el contenido exacto que se va a leer en el XML, ya sea por etiqueta o por etiqueta/atributo
     * y si el valor esperado es único o múltiple.
     */
    public static class Identificador{
        @Getter @NonNull private final String id;
        @Getter private final String etiqueta, atributo;
        @Setter @Getter private IdentificadorTipo tipo;
        @Setter @Getter private LecturaTipo lecturaTipo; 

        /**
         * Constructor para identificador de lectura por etiqueta con lectura única en el XML.
         * @param etiqueta Etiqueta a leer
         */
        public Identificador(String etiqueta) {
            this.etiqueta = etiqueta;
            this.atributo = null;
            this.id = etiqueta;
            tipo = IdentificadorTipo.ETIQUETA;
            lecturaTipo = LecturaTipo.UNICA;
        }

        /**
         * Constructor para identificador de lectura por etiqueta-atributo con lectura única en el XML.
         * @param etiqueta Etiqueta a leer
         * @param atributo Atributo a leer
         */
        public Identificador(String etiqueta, String atributo) {
            this.etiqueta = etiqueta;
            this.atributo = atributo;
            this.id = etiqueta.concat(":").concat(atributo);
            tipo = IdentificadorTipo.ATRIBUTO;
            lecturaTipo = LecturaTipo.UNICA;
        }

        /**
         * Constructor para identificador de lectura por etiqueta con lectura única o múltiple en el XML.
         * @param etiqueta Etiqueta a leer
         * @param lecturaTipo Tipo de lectura única o múltiple.
         */
        public Identificador(String etiqueta, LecturaTipo lecturaTipo) {
            this.etiqueta = etiqueta;
            this.lecturaTipo = lecturaTipo;
            this.atributo = null;
            this.id = etiqueta;
            tipo = IdentificadorTipo.ETIQUETA;
        }

        /**
         * Constructor para identificador de lectura por etiqueta-atributo con lectura única o múltiple en el XML.
         * @param etiqueta Etiqueta a leer
         * @param atributo Atributo a leer
         * @param lecturaTipo Tipo de lectura única o múltiple.
         */
        public Identificador(String etiqueta, String atributo, LecturaTipo lecturaTipo) {
            this.etiqueta = etiqueta;
            this.atributo = atributo;
            this.lecturaTipo = lecturaTipo;
            tipo = IdentificadorTipo.ATRIBUTO;
            this.id = etiqueta.concat(":").concat(atributo);
        }

        @Override
        public String toString() {
            return "Identificador{" + "id=" + id + ", etiqueta=" + etiqueta + ", atributo=" + atributo + ", tipo=" + tipo + ", lecturaTipo=" + lecturaTipo + '}';
        }
        
    }
    
    /**
     * Especifica el tipo de identificador para lectura a nivel etiqueta o de atributo de la etiqueta.
     */
    public enum IdentificadorTipo{
        ETIQUETA,ATRIBUTO;
    }
    
    /**
     * Especifica el tipo de lectura para el valor esperado, si es valor único o múltiple.
     */
    public enum LecturaTipo{
        UNICA, MULTIPLE;
    }
}
