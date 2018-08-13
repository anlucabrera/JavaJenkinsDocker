
package mx.edu.utxj.pye.sgi.ejb.finanzas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.persistence.NoResultException;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.finanzas.ComisionOficios;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.exception.DocumentoInternoNoSoportadoException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.DocxReplacer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

/**
 *
 * @author UTXJ
 */
@Stateless
public class ServicioDocumentosInternos implements EjbDocumentosInternos {
    @EJB Facade f;
    @Resource ManagedExecutorService exe;
    SimpleDateFormat sdfWord = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public <T> String generarNumeroOficio(AreasUniversidad area, Class<T> clase) {
        switch(clase.getName()){
            case "mx.edu.utxj.pye.sgi.entity.finanzas.ComisionOficios":
                List<ComisionOficios> l = f.getEntityManager().createQuery("SELECT co FROM ComisionOficios co WHERE co.area=:area ORDER BY co.oficio DESC", ComisionOficios.class)
                        .setParameter("area", area.getArea())
                        .setMaxResults(1)
                        .getResultList();
                
                String numeroOficio = l.isEmpty()?"":l.get(0).getOficio();
                Short numero = getNumeroEnOficio(numeroOficio, ComisionOficios.class);
                return String.format("UTXJ-%s/%04d/%s", area.getSiglas(),numero,Year.now().format(DateTimeFormatter.ofPattern("yy")));
            default: 
                throw new DocumentoInternoNoSoportadoException(clase);
        }        
    }

    /**
     * Verifica si un número de oficio ya existe.
     * @param <T> Tipo de clase del documento.
     * @param area Área que solicita la verificación de su número de oficio.
     * @param anio Año de generación del oficio.
     * @param numero Número del Oficio.
     * @param clase Tipo de clase del documento.
     * @return Devuelve la instancia del oficio si existe o NULL de lo contrario.
     */
    @Override
    public <T> T verificarNumeroOficio(AreasUniversidad area, Year anio, Short numero, Class<T> clase) {
        String numeroOficio = generarNumeroOficio(area, clase);
        return verificarNumeroOficio(numeroOficio, clase);
    }

    @Override
    public <T> T verificarNumeroOficio(String numeroOficio, Class<T> clase) {
        switch(clase.getName()){
            case "mx.edu.utxj.pye.sgi.entity.finanzas.ComisionOficios":
                ComisionOficios comisionOficio;
                try{
                    comisionOficio = f.getEntityManager().createNamedQuery("ComisionOficios.findByOficio", ComisionOficios.class)
                            .setParameter("oficio", numeroOficio)
                            .getSingleResult();
                }catch(NoResultException e){
                    comisionOficio = null;
                }

                return (T)comisionOficio;
            default:
                throw new DocumentoInternoNoSoportadoException(clase);
        }
    }

    @Override
    public <T> AreasUniversidad getAreaEnOficio(String numeroOficio, Class<T> clase) {
        if(numeroOficio.indexOf('-') == -1 || numeroOficio.indexOf('/') == -1){
            return null;
        }
        String area = numeroOficio.substring(numeroOficio.indexOf('-') + 1, numeroOficio.indexOf('/'));
        try{
            return f.getEntityManager().createNamedQuery("AreasUniversidad.findByArea", AreasUniversidad.class)
                    .setParameter("area", area)
                    .getSingleResult();
        }catch(NoResultException ex){
            return null;
        }
    }

    @Override
    public <T> Year getAnioEnOficio(String numeroOficio, Class<T> clase) {
        try{
            return Year.parse(numeroOficio.subSequence(numeroOficio.length()-2, numeroOficio.length()), DateTimeFormatter.ofPattern("yy"));
        }catch(DateTimeParseException e){
            return null;
        }
    }

    @Override
    public <T> Short getNumeroEnOficio(String numeroOficio, Class<T> clase) {
        if(numeroOficio.split("/").length != 3){
            return null;
        }

        try{
            return Short.parseShort(numeroOficio.split("/")[1]);
        }catch(NumberFormatException e){
            return null;
        }
    }

    @Override
    public <T> String construirOficio(T oficio) {
        switch (oficio.getClass().getName()) {
            case "mx.edu.utxj.pye.sgi.entity.finanzas.ComisionOficios":
                ComisionOficios comisionOficio = (ComisionOficios) oficio;
                Personal comisionado = f.getEntityManager().find(Personal.class, comisionOficio.getComisionado());
                Personal superior = f.getEntityManager().find(Personal.class, comisionOficio.getSuperior());
//                Localidad localidad = f.getEntityManager().find(Localidad.class, new LocalidadPK(comisionOficio.getEstado(), comisionOficio.getMunicipio(), comisionOficio.getLocalidad()));
                
                try {
                    XWPFDocument document = new XWPFDocument(OPCPackage.open(new File("C:\\archivos\\plantillas\\oficio_comision.docx")));
                    Map<String, String> reemplazos = new HashMap<>();
                    reemplazos.put("numeroOficio", comisionOficio.getOficio());
                    reemplazos.put("COMISIONADO", comisionado.getNombre().toUpperCase());
                    reemplazos.put("COMISIONADO_CATEGORIA", comisionado.getCategoriaOficial().getNombre().toUpperCase());
                    reemplazos.put("actividades", comisionOficio.getActividades());
                    reemplazos.put("dependencia", comisionOficio.getDependencia());
//                    reemplazos.put("municipio", localidad.getMunicipio().getNombre());
//                    reemplazos.put("estado", localidad.getMunicipio().getEstado().getNombre());
                    reemplazos.put("pais", "México");
                    reemplazos.put("fechaComision", sdfWord.format(comisionOficio.getFechaComisionInicio()));
                    reemplazos.put("fechaActual", sdfWord.format(comisionOficio.getFechaGeneracion()));
                    reemplazos.put("SUPERIOR", superior.getNombre().toUpperCase());
                    reemplazos.put("SUPERIOR_CATEGORIA", superior.getCategoriaOficial().getNombre().toUpperCase());
                    

                    exe.submit((Runnable) new DocxReplacer(reemplazos, document));
                    while (!exe.isTerminated()) {
                        Thread.sleep(50);
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmm");
                    String ruta = String.format("C:\\archivos\\plantillas\\oficio_comision%s.docx", sdf.format(new Date()));
                    File file = new File(ruta);
                    FileOutputStream fos = new FileOutputStream(file);
                    document.write(fos);
                    return URI.create(ruta).getPath();
                } catch (InvalidFormatException | IOException | InterruptedException ex) {
                    Logger.getLogger(ServicioDocumentosInternos.class.getName()).log(Level.SEVERE, null, ex);
                }

                return null;
            default:
                throw new DocumentoInternoNoSoportadoException(oficio.getClass());
        }
    }

    public static void main(String[] args) {
        String res = String.format("UTXJ-%s/%04d/%s", "PAL",1,Year.now().format(DateTimeFormatter.ofPattern("yy")));
        System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioDocumentosInternos.main() numeroOficio: " + res);

        System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioDocumentosInternos.main() clase: " + ComisionOficios.class.getName());

        String numeroOficio = "UTXJ-MECA/0001/17";
        String area = numeroOficio.substring(numeroOficio.indexOf('-') + 1, numeroOficio.indexOf('/'));
        System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioDocumentosInternos.main() area: " + area);

        Year anio = Year.parse(numeroOficio.subSequence(numeroOficio.length()-2, numeroOficio.length()), DateTimeFormatter.ofPattern("yy"));
        System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioDocumentosInternos.main() anio: " + anio);

        Short numero = Short.parseShort(numeroOficio.split("/")[1]);
        System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioDocumentosInternos.main() numero: " + numero);
        
        try {            
            Predicate<XWPFParagraph> filtro = (p) -> p.getText().contains("@");
            XWPFDocument document = new XWPFDocument(OPCPackage.open(new File("C:\\archivos\\plantillas\\oficio_comision.docx")));
            
            Map<String, String> reemplazos = new HashMap<>();
            reemplazos.put("numeroOficio", numeroOficio);
            
            Thread hilo = new Thread((Runnable)new DocxReplacer(reemplazos, document));
            hilo.start();
            
            document.getHeaderList().get(0).getParagraphs().stream().filter(filtro).forEach(parrafoH -> {
                parrafoH.getRuns().forEach(r -> {
                    String text = r.getText(0);
                    if (text != null && text.contains("@numeroOficio")) {
                        text = text.replace("@numeroOficio", numeroOficio);
                        r.setText(text, 0);
                    }
                });
//                if(parrafoH.getText().contains("@numeroOficio")){
//                    run.setText(run.getText(0).replace("@numeroOficio", numeroOficio),0);
//                }
                System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioDocumentosInternos.main() header: " + parrafoH.getText());
            });
            
            document.getTables().get(0).getRow(0).getTableCells().forEach(celda -> {
                celda.getParagraphs().stream().filter(filtro).forEach(parrafoC -> {
                    System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioDocumentosInternos.main() parrafoC: " + parrafoC.getText());
                });
            });
            
            document.getTables().get(1).getRows().forEach(fila -> {
                fila.getTableCells().get(1).getParagraphs().stream().filter(filtro).forEach(parrafoC -> {
                    System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioDocumentosInternos.main() parrafoC 2: " + parrafoC.getText());
                });
            });

            document.getParagraphs().stream().filter(filtro).forEach(parrafo -> {
                System.out.println("mx.edu.utxj.pye.sgi.ejb.finanzas.ServicioDocumentosInternos.main() parrafos: " + parrafo.getText());
            });
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmm");
            FileOutputStream fos = new FileOutputStream(new File(String.format("C:\\archivos\\plantillas\\oficio_comision%s.docx", sdf.format(new Date()))));
            document.write(fos);
        } catch (InvalidFormatException | IOException ex) {
            Logger.getLogger(ServicioDocumentosInternos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @Override
    public <T> List<T> getListaOficios(Class<T> clase, Year anio) {
        switch(clase.getName()){
            case "mx.edu.utxj.pye.sgi.entity.finanzas.ComisionOficios":
                return f.getEntityManager().createQuery("SELECT oc FROM ComisionOficios oc WHERE oc.oficio like :parOficio ORDER BY oc.oficio", ComisionOficios.class)
                        .setParameter("parOficio", "%".concat(DateTimeFormatter.ofPattern("yy").format(anio)))
                        .getResultList()
                        .stream()
                        .map(oc -> (T)oc)
                        .collect(Collectors.toList());
            default:
                throw new DocumentoInternoNoSoportadoException(clase);
        }
    }
    
}
