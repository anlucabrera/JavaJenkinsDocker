/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.eb;

import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.annotation.MultipartConfig;
import lombok.Getter;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbPlantillasEBExcel;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.xssf.XLSBUnsupportedException;

/**
 *
 * @author UTXJ
 */
@Stateless
@MultipartConfig()
public class ServicioPlantillasEBExcel implements EjbPlantillasEBExcel{

    @EJB
    EjbCarga ejbCarga;
    @EJB
    EjbCatalogos ejbCatalogos;
    
    public static final String MATRICULA_PLANTILLA = "matricula_periodo_escolar.xlsx";
    public static final String MATRICULA_ACTUALIZADO = "matricula_periodo_escolar_actualizado.xlsx";
    
    @Getter private final String[] ejes = ServicioArchivos.EJES;
    
//    TODO: Verificar plantilla para que la interpretacion de los datos sea correctamente mostrada en el archivo de excel
    @Override
    public String getPlantillaMatriculaPeriodosEscolares() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[0]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[0]);
        String plantilla = rutaPlantilla.concat(MATRICULA_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(MATRICULA_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("ciclosEscolares", ejbCatalogos.getCiclosEscolaresAct());
        beans.put("periodosEscolares", ejbCatalogos.getPeriodosEscolaresAct());
        beans.put("areasUniversidad", ejbCatalogos.getProgramasEducativos());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);
        return plantillaC;
    }    
}
