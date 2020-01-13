package mx.edu.utxj.pye.sgi.funcional;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import mx.edu.utxj.pye.sgi.entity.finanzas.Facturas;

/**
 *
 * @author UTXJ
 */
public class ValidadorFacturaPDF implements Validador<Facturas>, Callable<Boolean> {
    private final Facturas t;
    private final String link;
    private final String rfcReceptor;

    public ValidadorFacturaPDF(Facturas t, String rfcReceptor) {
        this.t = t;
        this.link = t.getLinkVerificacion();
        this.rfcReceptor = rfcReceptor;
    }

    private static String decode(final String encoded) {
        try {
            return encoded == null ? null : URLDecoder.decode(encoded, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("Impossible: UTF-8 is a required encoding", e);
        }
    }

    @Override
    public boolean esCorrecta(Facturas t) {
        boolean error = false;
        List<String> mensajes = new ArrayList<>();

        try {
            t.setComentariosPdf("");
            t.setValidacionPdf(false);

            URL url = new URL(link);

            Map<String, List<String>> map = Pattern.compile("&").splitAsStream(url.getQuery())
                    .map(s -> Arrays.copyOf(s.split("="), 2))
                    .collect(Collectors.groupingBy(s -> decode(s[0]), Collectors.mapping(s -> decode(s[1]), Collectors.toList())));

            //validar número de serie de timbre fiscal
            System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorFacturaPDF.esCorrecta() id 1: " + map.get("id").get(0));
            System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorFacturaPDF.esCorrecta() id 2: " + t.getTimbreFiscalUUID());
            if(map.containsKey("id")){
                if(!map.get("id").get(0).equals(t.getTimbreFiscalUUID())){
                    mensajes.add("El PDF ingresado no coincide con el XML de la factura.");
                    error=true;
                }
            }else{
                mensajes.add("El PDF ingresado no contiene el ID del timbre fiscal digital.");
                error = true;
            }
            
            //validar receptor
            if(map.containsKey("rr")){
                if(!map.get("rr").get(0).equals(rfcReceptor)){
                    mensajes.add("El RFC del receptor especificado en el PDF no coincide con : " + rfcReceptor + ".");
                    error=true;
                }
            }else{
                mensajes.add("El PDF ingresado no contiene el RFC del receptor.");
                error = true;
            }
            
            //validar emisor
            if(map.containsKey("re")){
                if(!map.get("re").get(0).equals(t.getRfcEmisor())){
                    mensajes.add("El RFC del emisor especificado en el PDF no coincide con : " + t.getRfcEmisor() + ".");
                    error=true;
                }
            }else{
                mensajes.add("El PDF ingresado no contiene el RFC del emisor.");
                error = true;
            }

            //validar monto
            if(map.containsKey("tt")){
                if(Double.parseDouble(map.get("tt").get(0)) != t.getMontoTotal()){
                    mensajes.add("El monto especificado en el PDF(" + Double.parseDouble(map.get("tt").get(0)) + ") no coincide con el monto de la factura(" + t.getMontoTotal() + ")." );
                    error=true;
                }
            }else{
                mensajes.add("El PDF ingresado no contiene el monto de la factura.");
                error = true;
            }
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            Logger.getLogger(ValidadorFactura.class.getName()).log(Level.SEVERE, null, ex);
            mensajes.add("ocurrió un error al intentar leer el monto de la factura, " + ex.getClass().getName() + ", comunique al administrador " + (new Date()));
            error = true;
        }catch (Exception ex) {
            Logger.getLogger(ValidadorFactura.class.getName()).log(Level.SEVERE, null, ex);
            mensajes.add("Ocurrió un error interno del sistema, " + ex.getClass().getName() + ", comunique al administrador. " + (new Date()));
            error = true;
        }

        System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorFacturaPDF.esCorrecta() mensajes: " + mensajes);
        t.setComentariosPdf(mensajes.stream().collect(Collectors.joining("\n")));
        t.setValidacionPdf(!error);
        return !error;
    }

    @Override
    public Boolean call() throws Exception {
        return esCorrecta(t);
    }

}
