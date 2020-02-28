package mx.edu.utxj.pye.sgi.funcional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.parsers.ParserConfigurationException;
import mx.edu.utxj.pye.sgi.dto.ConteoDias;
import mx.edu.utxj.pye.sgi.entity.finanzas.ComisionOficios;
import mx.edu.utxj.pye.sgi.entity.finanzas.Facturas;
import mx.edu.utxj.pye.sgi.util.XMLReader;
import org.xml.sax.SAXException;

/**
 *
 * @author UTXJ
 */
public class ValidadorFactura extends AbstractFunctional implements Validador<Facturas>, Callable<Boolean>{
    private final XMLReader reader;
    private final Facturas t;
    private final Boolean permitirSabado;
    private final Boolean permitirDomingo;
    private final String version;
    private final String rfcReceptor;
    private final String razonReceptor;

    public ValidadorFactura(XMLReader reader, Facturas t, ComisionOficios comisionOficio, String version, String rfcReceptor, String razonReceptor) {
        System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorFactura.<init>() ticket: " + t.getTicket());                
        this.reader = reader;
        this.t = t;
        this.permitirSabado = comisionOficio.getSabadosSolicitados() && comisionOficio.getSabadosAutorizados();
        this.permitirDomingo = comisionOficio.getDomingosSolicitados() && comisionOficio.getDomingosAutorizados();
        this.version = version;
        this.rfcReceptor = rfcReceptor;
        this.razonReceptor = razonReceptor;
    }


    @Override
    public boolean esCorrecta(Facturas t) {
        System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorFactura.esCorrecta()");
        boolean error = false;
        List<String> mensajes = new ArrayList<>();
        
        try {
//            if(t.getComentariosSistema() != null)
//                mensajes.add(t.getComentariosSistema());
            
            t.setComentariosSistema("");
            t.setValidacionSistema(false);
            
            if(reader != null){//cuando la factura no se ha persistido, se deben validar datos extras del xml
                XMLReader.Identificador i01 = new XMLReader.Identificador("cfdi:Comprobante", "Version");
                XMLReader.Identificador i02 = new XMLReader.Identificador("cfdi:Receptor", "Rfc");
                XMLReader.Identificador i03 = new XMLReader.Identificador("cfdi:Receptor", "Nombre");
                List<XMLReader.Identificador> identificadores = Arrays.asList(i01,i02,i03);
                Map<XMLReader.Identificador, List<String>> valores = reader.leerValores(identificadores);
                
                //validar versión 3.3
                String versionXML = quitarEspacios(valores.get(i01).get(0));
                if (!versionXML.equals(version)) {
                    mensajes.add("La versión de la factura no es correcta, se acepta sólo versión 3.3, informe al proveedor para que haga el cambio.");
                    error = true;
                }

                //validar datos del receptor rfc y razón
                String rfcXML = quitarEspacios(valores.get(i02).get(0));
                String razonXML = quitarEspacios(valores.get(i03).get(0));
                if (!rfcXML.equalsIgnoreCase(rfcReceptor)) {
                    mensajes.add("El RFC de la institución no es el correcto, solicite al proveedor el cambio al valor correcto: " + rfcReceptor + ".");
                    error = true;
                }
                
                if (!equalsIgnoreAccents(razonXML, razonReceptor)) {
                    mensajes.add("La razón de la institución no es la correcta, solicite al proveedor el cambio al valor correcto: " + razonReceptor + ".");
                    error = true;
                }
            }
            
            System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorFactura.esCorrecta() antes de comprobar fechas, mensajes: " + mensajes);
            //validar que la fecha de aplicación coincida con la expedición
            if(!t.getFechaAplicacion().equals(t.getFechaExpedicion()) && quitarEspacios(t.getTicket()).isEmpty()){
                System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorFactura.esCorrecta() Fechas no coinciden y no hay ticket.");
                mensajes.add("La fecha de aplicación no coincide con la fecha de expedición de la factura, favor de evidenciar el consumo en la fecha de aplicación mediante un ticket.");
                error=true;
            }
            
            Comparable<Facturas> cf = new ComparadorFechas(t.getFechaAplicacion());
            int res = cf.compareTo(t);
//            System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorFactura.esCorrecta() res: " + res);
            if(res != 0){//si la fecha de inicio es posterior a la de fin o si la fecha de aplicación no está entre la fecha de inicio y fin
                t.setFechaCoberturaInicio(t.getFechaAplicacion());
                t.setFechaCoberturaFin(t.getFechaAplicacion());
//                if(res == -1){//fecha de inicio posteior a fecha de fin
//                    mensajes.add("La fecha de cobertura de fin deber ser igual o posterior a la fecha de inicio.");
//                }else if(res == 1){
//                    mensajes.add("La fecha de aplicación debe estar entre la fecha de cobertura de inicio y fin.");
//                }
            }
            
            //validar monto aceptado

            ConteoDias cd = new ConteoDias(generarRangoFechas(t.getFechaCoberturaInicio(), t.getFechaCoberturaFin()));
            t.setLaborales((short)cd.getLaborales());
            t.setSabados((short)cd.getSabados());
            t.setDomingos((short)cd.getDomingos());
            //validar si se aceptan facturas en fin de semana
            if(!permitirSabado && !permitirDomingo && (cd.getSabados() + cd.getDomingos()) > 0){
                if(cd.getLaborales() > 0){
                    mensajes.add("Para éste trámite no se han autorizado sábados y domingos como fecha de cobertura, se contemplarán solo los días laborales.");
                }else{
                    mensajes.add("Para éste trámite no se han autorizado sábados y domingos como fecha de cobertura, solicite a fiscalización la autorización o utilice otra factura.");
                    error = true;
                }
            }else if(permitirSabado && !permitirDomingo){
                if(cd.getLaborales() + cd.getSabados() > 0){
                    mensajes.add("Para éste trámite no se han autorizado domingos como fecha de cobertura, se contemplarán solo los días laborales y los sábados.");
                }else{
                    mensajes.add("Para éste trámite no se han autorizado domingos como fecha de cobertura, solicite a fiscalización la autorización o utilice otra factura.");
                    error = true;
                }
            }else if(!permitirSabado && permitirDomingo){
                if(cd.getLaborales() + cd.getDomingos() > 0){
                    mensajes.add("Para éste trámite no se han autorizado sábados como fecha de cobertura, se contemplarán solo los días laborales y domingos.");
                }else{
                    mensajes.add("Para éste trámite no se han autorizado sábados como fecha de cobertura, solicite a fiscalización la autorización o utilice otra factura.");
                    error = true;
                }
            }else if(permitirSabado && permitirDomingo){
                //sin reglas de validación contempladas
            }

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ValidadorFactura.class.getName()).log(Level.SEVERE, null, ex);
            mensajes.add("El archivo XML no se pudo leer, comunique al administrador. " + (new Date()));
            error = true;
        } catch (Exception ex) {
            Logger.getLogger(ValidadorFactura.class.getName()).log(Level.SEVERE, null, ex);
            mensajes.add("Ocurrió un error interno del sistema, " + ex.getClass().getName() + ", comunique al administrador. " + (new Date()));
            error = true;
        }

//        System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorFactura.esCorrecta(error: " + error + ") mensajes: " + mensajes);
        t.setComentariosSistema(mensajes.stream().collect(Collectors.joining("\n")));
        t.setValidacionSistema(!error);
//        System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorFactura.esCorrecta() validación sistema: " + t.getValidacionSistema());
        return true;
    }

    @Override
    public Boolean call() throws Exception {
        return esCorrecta(t);
    }
}
