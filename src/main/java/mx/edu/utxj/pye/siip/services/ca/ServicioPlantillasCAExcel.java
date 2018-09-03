/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

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
import mx.edu.utxj.pye.siip.interfaces.ca.EjbActFormacionIntegral;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbCuerposAcademicos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbPlantillasCAExcel;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbReconocimientoProdep;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbRegistroMovilidad;
//import org.jxls.common.Context;
//import org.jxls.util.JxlsHelper;
import net.sf.jxls.transformer.XLSTransformer;

/**
 *
 * @author UTXJ
 */
@Stateless
@MultipartConfig()
public class ServicioPlantillasCAExcel implements EjbPlantillasCAExcel {

    @EJB
    EjbCarga ejbCarga;

    @EJB
    EjbCatalogos ejbCatalogos;

    @EJB
    EjbMatriculaPeriodosEscolares ejbMatriculaPeriodosEscolares;

    @EJB
    EjbActFormacionIntegral ejbActFormacionIntegral;

    @EJB
    EjbOrganismosVinculados ejbOrganismosVinculados;

    @EJB
    EjbRegistroMovilidad ejbRegistroMovilidad;

    @EJB
    EjbCuerposAcademicos ejbCuerposAcademicos;

    @EJB
    EjbReconocimientoProdep ejbReconocimientoProdep;

    @EJB
    EjbSelectec ejbSelectec;

    public static final String PROGPERTCALIDAD_PLANTILLA = "programasPertCalidad.xlsx";
    public static final String PROGPERTCALIDAD_ACTUALIZADO = "programasPertCalidad_actualizado.xlsx";

    public static final String EGETSU_PLANTILLA = "egetsu.xlsx";
    public static final String EGETSU_ACTUALIZADO = "egetsu_actualizado.xlsx";

    public static final String EXANI_PLANTILLA = "exani.xlsx";
    public static final String EXANI_ACTUALIZADO = "exani_actualizado.xlsx";

    public static final String ACERVO_PLANTILLA = "acervoBibliografico.xlsx";
    public static final String ACERVO_ACTUALIZADO = "acervoBibliografico_actualizado.xlsx";

    public static final String ACTFORMINT_PLANTILLA = "actividadFormacionIntegral.xlsx";
    public static final String ACTFORMINT_ACTUALIZADO = "actividadFormacionIntegral_actualizado.xlsx";

    public static final String MOVILIDAD_PLANTILLA = "registrosMovilidad.xlsx";
    public static final String MOVILIDAD_ACTUALIZADO = "registrosMovilidad_actualizado.xlsx";

    public static final String RECPRODEP_PLANTILLA = "reconocimientoProdep.xlsx";
    public static final String RECPRODEP_ACTUALIZADO = "reconocimientoProdep_actualizado.xlsx";

    public static final String CUERPACAD_PLANTILLA = "cuerpos_academicos.xlsx";
    public static final String CUERPACAD_ACTUALIZADO = "cuerpos_academicos_actualizado.xlsx";

    public static final String PROD_ACAD_PLANTILLA = "productos_academicos_plantilla";
    public static final String PROD_ACAD_ACTUALIZADO = "productos_academicos_completo";

    @Getter
    private final String[] ejes = ServicioArchivos.EJES;
    
    public String crearDirectorioPlantilla(String eje){
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(eje);
        return rutaPlantilla;
    }
    public String crearDirectorioPlantillaCompleto(String eje){
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(eje);
        return rutaPlantillaC;
    }

    @Override
    public String getPlantillaProgPertCalidad() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[1]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[1]);
        String plantilla = rutaPlantilla.concat(PROGPERTCALIDAD_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(PROGPERTCALIDAD_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("programasEducativos", ejbCatalogos.getProgramasEducativos());
        beans.put("organismosEvaluadores", ejbCatalogos.getOrganismosEvaluadoresAct());
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
    public String getPlantillaEgetsu() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[1]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[1]);
        String plantilla = rutaPlantilla.concat(EGETSU_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(EGETSU_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("generaciones", ejbCatalogos.getGeneracionesAct());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getPlantillaExani() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[1]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[1]);
        String plantilla = rutaPlantilla.concat(EXANI_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(EXANI_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("ciclosEscolares", ejbCatalogos.getCiclosEscolaresAct());
        beans.put("programasEducativos", ejbCatalogos.getProgramasEducativos());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getPlantillaAcervo() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[1]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[1]);
        String plantilla = rutaPlantilla.concat(ACERVO_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(ACERVO_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("ciclosEscolares", ejbCatalogos.getCiclosEscolaresAct());
        beans.put("programasEducativos", ejbCatalogos.getProgramasEducativos());
        beans.put("periodosEscolares", ejbCatalogos.getPeriodosEscolaresAct());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getPlantillaActFormacionIntegral() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[1]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[1]);
        String plantilla = rutaPlantilla.concat(ACTFORMINT_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(ACTFORMINT_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("actividadesTipos", ejbActFormacionIntegral.getActividadesTiposAct());
        beans.put("eventosTipos", ejbActFormacionIntegral.getEventosTiposAct());
        beans.put("ciclosEscolares", ejbCatalogos.getCiclosEscolaresAct());
        beans.put("periodosEscolares", ejbCatalogos.getPeriodosEscolaresAct());
        beans.put("matriculaPeriodosEscolares", ejbMatriculaPeriodosEscolares.getMatriculasVigentes());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getPlantillaRegistroMovilidad() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[1]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[1]);
        String plantilla = rutaPlantilla.concat(MOVILIDAD_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(MOVILIDAD_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("programasMovilidad", ejbRegistroMovilidad.getProgramasMovilidadAct());
        beans.put("ciclosEscolares", ejbCatalogos.getCiclosEscolaresAct());
        beans.put("periodosEscolares", ejbCatalogos.getPeriodosEscolaresAct());
        beans.put("programasEducativos", ejbCatalogos.getProgramasEducativos());
        beans.put("organismosVinculados", ejbOrganismosVinculados.getOrganismosVinculadoVigentes());
        beans.put("listaPersonal", ejbSelectec.mostrarListaDeEmpleados());
        beans.put("matriculaPeriodosEscolares", ejbMatriculaPeriodosEscolares.getMatriculasVigentes());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getPlantillaReconomicientoProdep() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[1]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[1]);
        String plantilla = rutaPlantilla.concat(RECPRODEP_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(RECPRODEP_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("listaPersonal", ejbSelectec.mostrarListaDeEmpleados());
        beans.put("cuerposAcademicos", ejbCuerposAcademicos.getCuerposAcademicosAct());
        beans.put("tiposApoyo", ejbReconocimientoProdep.getReconocimientoProdepTiposApoyoAct());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getPlantillaCuerposAcademicos() throws Throwable {
        String plantilla = crearDirectorioPlantilla(ejes[1]).concat(CUERPACAD_PLANTILLA);
        String plantillaC = crearDirectorioPlantillaCompleto(ejes[1]).concat(CUERPACAD_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("listaPersonal", ejbSelectec.mostrarListaDeEmpleados());
        beans.put("cuerpacadDisciplinas", ejbCuerposAcademicos.getCuerpacadDisciplinas());
        beans.put("cuerpacadAreasEstudio", ejbCuerposAcademicos.getCuerpacadAreasEstudio());
        beans.put("areasUniversidad", ejbCatalogos.getAreasAcademicas());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);
        return plantillaC;
    }

    @Override
    public String getPlantillaProductosAcademicos() throws Throwable {
        String plantilla = crearDirectorioPlantilla(ejes[1].concat(PROD_ACAD_PLANTILLA));
        String plantillaC = crearDirectorioPlantillaCompleto(ejes[1]).concat(PROD_ACAD_ACTUALIZADO);
        Map modelos = new HashMap();
        
        return plantillaC;
    }
}
