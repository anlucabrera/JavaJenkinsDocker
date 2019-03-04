/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.pa;


import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.annotation.MultipartConfig;
import lombok.Getter;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.interfaces.pa.EjbPresupuestos;
import mx.edu.utxj.pye.siip.interfaces.pa.EjbPlantillasPAExcel;
import net.sf.jxls.transformer.XLSTransformer;

/**
 *
 * @author UTXJ
 */
@Stateless
@MultipartConfig()
public class ServicioPlantillaPAExcel implements EjbPlantillasPAExcel{
    
    @EJB EjbCarga ejbCarga;

    @EJB EjbPresupuestos ejbPresupuestos;
    
    @EJB EjbCatalogos ejbCatalogos;
    
    public static final String PRESUPUESTOS_PLANTILLA = "presupuestos.xlsx";
    public static final String PRESUPUESTOS_ACTUALIZADO = "presupuestos_actualizado.xlsx";
    
    public static final String INGPROCAP_PLANTILLA = "ingresosPropiosCaptados.xlsx";
    public static final String INGPROCAP_ACTUALIZADO = "ingresosPropiosCaptados_actualizado.xlsx";
  
    @Getter
    private final String[] ejes = ServicioArchivos.EJES;
    
     public String crearDirectorioPlantilla(String eje) {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(eje);
        return rutaPlantilla;
    }

    public String crearDirectorioPlantillaCompleto(String eje) {
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(eje);
        return rutaPlantillaC;
    }
    
    @Override
    public String getPlantillaPresupuestos() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[3]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[3]);
        String plantilla = rutaPlantilla.concat(PRESUPUESTOS_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(PRESUPUESTOS_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("capituloTipos", ejbPresupuestos.getCapitulosTiposAct());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }
    
    @Override
    public String getPlantillaIngPropios() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[3]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[3]);
        String plantilla = rutaPlantilla.concat(INGPROCAP_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(INGPROCAP_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("periodosEscolares", ejbCatalogos.getPeriodosEscolaresAct());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }
    
}
