/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.annotation.MultipartConfig;
import lombok.Getter;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbPlantillasVINExcel;
//import org.jxls.common.Context;
//import org.jxls.util.JxlsHelper;
import net.sf.jxls.transformer.XLSTransformer;

/**
 *
 * @author UTXJ
 */
@Stateless
@MultipartConfig()
public class ServicioPlantillasVINExcel implements EjbPlantillasVINExcel {

    @EJB
    EjbCarga ejbCarga;
    @EJB
    EjbOrganismosVinculados ejbOrganismosVinculados;
    @EJB
    EjbMatriculaPeriodosEscolares ejbMatriculaPeriodosEscolares;
    @EJB
    EjbCatalogos ejbCatalogos;

    public static final String CONVENIOS_PLANTILLA = "convenios.xlsx";
    public static final String CONVENIOS_ACTUALIZADO = "convenios_actualizado.xlsx";
    
    public static final String BOLSATRABAJO_PLANTILLA = "bolsaTrabajo.xlsx";
    public static final String BOLSATRABAJO_ACTUALIZADO = "bolsaTrabajo_actualizado.xlsx";
    
    @Getter
    private final String[] ejes = ServicioArchivos.EJES;

    @Override
    public String getPlantillaConvenios() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[2]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[2]);
        String plantilla = rutaPlantilla.concat(CONVENIOS_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(CONVENIOS_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("organismosVinculados", ejbOrganismosVinculados.getOrganismosVinculadoVigentes());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

//        try (InputStream is = ServicioPlantillasVINExcel.class.getResourceAsStream(plantilla)) {
//            System.out.println("Archivo leido");
//            try (OutputStream os = new FileOutputStream(plantillaC)) {
//                Context context = new Context();
//                context.putVar("organismosVinculados", ejbOrganismosVinculados.getOrganismosVinculadoVigentes());
//                JxlsHelper.getInstance().processTemplate(is, os, context);
//            }
//        }
        return plantillaC;
    }

    @Override
    public String getPlantillaBolsaTrabajo() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[2]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[2]);
        String plantilla = rutaPlantilla.concat(BOLSATRABAJO_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(BOLSATRABAJO_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("organismosVinculados", ejbOrganismosVinculados.getOrganismosVinculadoVigentes());
        beans.put("matriculaPeriodosEscolares", ejbMatriculaPeriodosEscolares.getMatriculasVigentes());
        beans.put("generaciones", ejbCatalogos.getGeneracionesAct());
        beans.put("programasEducativos", ejbCatalogos.getProgramasEducativos());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

//        try (InputStream is = ServicioPlantillasVINExcel.class.getResourceAsStream(plantilla)) {
//            System.out.println("Archivo leido");
//            try (OutputStream os = new FileOutputStream(plantillaC)) {
//                Context context = new Context();
//                context.putVar("organismosVinculados", ejbOrganismosVinculados.getOrganismosVinculadoVigentes());
//                JxlsHelper.getInstance().processTemplate(is, os, context);
//            }
//        }
        return plantillaC;
    }

}
