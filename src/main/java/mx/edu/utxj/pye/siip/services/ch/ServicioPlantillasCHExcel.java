/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ch;


import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.annotation.MultipartConfig;
import lombok.Getter;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbSelectec;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbPlantillasCHExcel;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbComisionesAcademicas;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbProgramasEstimulos;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbPersonalCapacitado;
//import org.jxls.common.Context;
//import org.jxls.util.JxlsHelper;
import net.sf.jxls.transformer.XLSTransformer;

/**
 *
 * @author UTXJ
 */

@Stateless
@MultipartConfig()
public class ServicioPlantillasCHExcel implements EjbPlantillasCHExcel{
    
    @EJB
    EjbCarga ejbCarga;
    
    @EJB
    EjbCatalogos ejbCatalogos;
  
    @EJB
    EjbSelectec ejbSelectec;
    
    @EJB
    EjbComisionesAcademicas ejbComisionesAcademicas;
    
    @EJB
    EjbProgramasEstimulos ejbProgramasEstimulos;
    
    @EJB
    EjbPersonalCapacitado ejbPersonalCapacitado;
    
    public static final String COMACAD_PLANTILLA = "comisionesAcademicas.xlsx";
    public static final String COMACAD_ACTUALIZADO = "comisionesAcademicas_actualizado.xlsx";
    
    public static final String PROGESTIMULOS_PLANTILLA = "programasEstimulos.xlsx";
    public static final String PROGESTIMULOS_ACTUALIZADO = "programasEstimulos_actualizado.xlsx";
    
    public static final String PERSCAPACITADO_PLANTILLA = "personalCapacitado.xlsx";
    public static final String PERSCAPACITADO_ACTUALIZADO = "personalCapacitado_actualizado.xlsx";
        
    @Getter
    private final String[] ejes = ServicioArchivos.EJES;
    
    @Override
    public String getPlantillaComisionesAcademicas() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[4]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[4]);
        String plantilla = rutaPlantilla.concat(COMACAD_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(COMACAD_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("listaPersonal", ejbSelectec.mostrarListaDeEmpleados());
        beans.put("tiposComision", ejbComisionesAcademicas.getComisionesAcademicasTiposAct());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getPlantillaProgramasEstimulos() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[4]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[4]);
        String plantilla = rutaPlantilla.concat(PROGESTIMULOS_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(PROGESTIMULOS_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("listaPersonal", ejbSelectec.mostrarListaDeEmpleados());
        beans.put("programasEstimulos", ejbProgramasEstimulos.getProgramasEstimulosTiposAct());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getPlantillaPersonalCapacitado() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[4]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[4]);
        String plantilla = rutaPlantilla.concat(PERSCAPACITADO_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(PERSCAPACITADO_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("listaPersonal", ejbSelectec.mostrarListaDeEmpleados());
        beans.put("tipoPerCap",  ejbPersonalCapacitado.getPerCapTipoAct());
        beans.put("modalidadPerCap", ejbPersonalCapacitado.getPerCapModalidadAct());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }
    
}
