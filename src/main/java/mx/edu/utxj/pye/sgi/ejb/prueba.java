package mx.edu.utxj.pye.sgi.ejb;

import com.google.zxing.NotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.lcs.qrscan.core.QrPdf;


public class prueba {

    public static void main(String[] args) {
        try {
            QrPdf pdf = new QrPdf(Paths.get("C:\\Users\\UTXJ\\Documents\\Proyectos\\Jeimy Guzmán Jimenez.pdf"));
            System.out.println("edu.mx.utxj.pye.qrscan.Prueba.main() pdf: " + pdf);
            String qrCode = pdf.getQRCode(1, true, true);
            String[] parts = qrCode.split("\\|");
            System.out.println("largo"+parts.length);
            System.out.println("edu.mx.utxj.pye.qrscan.Prueba.main() code: " + qrCode);
        } catch (IOException ex) {
            Logger.getLogger(prueba.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotFoundException ex) {
            Logger.getLogger(prueba.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
