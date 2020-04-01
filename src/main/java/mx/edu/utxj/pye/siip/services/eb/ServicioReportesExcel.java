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
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import net.sf.jxls.transformer.XLSTransformer;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbReportesExcel;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbAcervoBibliografico;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbActFormacionIntegral;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbBecasPeriodo;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbDesercionPeriodos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbDesercionReprobacion;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbEgetsuResultados;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbExaniResultados;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbComisionesAcademicas;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbPersonalCapacitado;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbProgramasEstimulos;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbReconocimientoProdep;
import mx.edu.utxj.pye.siip.interfaces.pa.EjbIngPropios;
import mx.edu.utxj.pye.siip.interfaces.pa.EjbPresupuestos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbBolsaTrabajo;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbDifusionIems;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbFeriasProfesiograficas;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbFeriasParticipantes;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbRegistroMovilidad;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbVisitasIndustriales;


/**
 *
 * @author UTXJ
 */
@Stateless
@MultipartConfig()
public class ServicioReportesExcel implements EjbReportesExcel{
    
    @EJB EjbCarga ejbCarga;
    @EJB EjbActFormacionIntegral ejbActFormacionIntegral;
    @EJB EjbAcervoBibliografico ejbAcervoBibliografico;
    @EJB EjbBecasPeriodo ejbBecasPeriodo;
    @EJB EjbDesercionPeriodos ejbDesercionPeriodos;
    @EJB EjbDesercionReprobacion ejbDesercionReprobacion;
    @EJB EjbEgetsuResultados ejbEgetsuResultados;
    @EJB EjbExaniResultados ejbExaniResultados;
    @EJB EjbIngPropios ejbIngPropios;
    @EJB EjbComisionesAcademicas ejbComisionesAcademicas;
    @EJB EjbPersonalCapacitado ejbPersonalCapacitado;
    @EJB EjbProgramasEstimulos ejbProgramasEstimulos;
    @EJB EjbReconocimientoProdep ejbReconocimientoProdep;
    @EJB EjbPresupuestos ejbPresupuestos;
    @EJB EjbBolsaTrabajo ejbBolsaTrabajo;
    @EJB EjbDifusionIems ejbDifusionIems;
    @EJB EjbFeriasProfesiograficas ejbFeriasProfesiograficas;
    @EJB EjbFeriasParticipantes ejbFeriasParticipantes;
    @EJB EjbRegistroMovilidad ejbRegistroMovilidad;
    @EJB EjbVisitasIndustriales ejbVisitasIndustriales;
    
    public static final String INGPROCAP_PLANTILLA = "ingresosPropiosCaptados.xlsx";
    public static final String INGPROCAP_ACTUALIZADO = "ingresosPropiosCaptados_reporte.xlsx";
    
    public static final String PRESUPUESTOS_PLANTILLA = "presupuestos.xlsx";
    public static final String PRESUPUESTOS_ACTUALIZADO = "presupuestos_reporte.xlsx";
    
    public static final String DIFIEMS_PLANTILLA = "difusionIems.xlsx";
    public static final String DIFIEMS_ACTUALIZADO = "difusionIems_reporte.xlsx";
    
    public static final String VISIND_PLANTILLA = "visitasIndustriales.xlsx";
    public static final String VISIND_ACTUALIZADO = "visitasIndustriales_reporte.xlsx";
    
    public static final String BECAS_PLANTILLA = "becas.xlsx";
    public static final String BECAS_ACTUALIZADO = "becas_reporte.xlsx";
    
    public static final String ACTFORMINT_PLANTILLA = "actividadFormacionIntegral.xlsx";
    public static final String ACTFORMINT_ACTUALIZADO = "actividadFormacionIntegral_reporte.xlsx";
    
    public static final String DESERCION_PLANTILLA = "desercionAcademica.xlsx";
    public static final String DESERCION_ACTUALIZADO = "desercionAcademica_reporte.xlsx";
    
    public static final String PERSCAPACITADO_PLANTILLA = "personalCapacitado.xlsx";
    public static final String PERSCAPACITADO_ACTUALIZADO = "personalCapacitado_reporte.xlsx";
    
    public static final String FERPROF_PLANTILLA = "feriasProfesiograficas.xlsx";
    public static final String FERPROF_ACTUALIZADO = "feriasProfesiograficas_actualizado.xlsx";
    
    public static final String MOVILIDAD_PLANTILLA = "registrosMovilidad.xlsx";
    public static final String MOVILIDAD_ACTUALIZADO = "registrosMovilidad_reporte.xlsx";
    
    public static final String BOLSATRABAJO_PLANTILLA = "bolsaTrabajo.xlsx";
    public static final String BOLSATRABAJO_ACTUALIZADO = "bolsaTrabajo_reporte.xlsx";
    
    public static final String ACERVO_PLANTILLA = "acervoBibliografico.xlsx";
    public static final String ACERVO_ACTUALIZADO = "acervoBibliografico_reporte.xlsx";
    
    public static final String RECPRODEP_PLANTILLA = "reconocimientoProdep.xlsx";
    public static final String RECPRODEP_ACTUALIZADO = "reconocimientoProdep_reporte.xlsx";
    
    public static final String COMACAD_PLANTILLA = "comisionesAcademicas.xlsx";
    public static final String COMACAD_ACTUALIZADO = "comisionesAcademicas_reporte.xlsx";
    
    public static final String PROGESTIMULOS_PLANTILLA = "programasEstimulos.xlsx";
    public static final String PROGESTIMULOS_ACTUALIZADO = "programasEstimulos_reporte.xlsx";
    
    public static final String EGETSU_PLANTILLA = "egetsu.xlsx";
    public static final String EGETSU_ACTUALIZADO = "egetsu_reporte.xlsx";

    public static final String EXANI_PLANTILLA = "exani.xlsx";
    public static final String EXANI_ACTUALIZADO = "exani_reporte.xlsx";
    
    @Getter
    private final String[] ejes = ServicioArchivos.EJES;
    
    public String crearDirectorioReporte(String eje) {
        String rutaPlantilla = ejbCarga.crearDirectorioReporte(eje);
        return rutaPlantilla;
    }

    public String crearDirectorioReporteCompleto(String eje) {
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(eje);
        return rutaPlantillaC;
    }

    @Override
    public String getReporteIngPropios(Short ejercicioFiscal) throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioReporte(ejes[3]);
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(ejes[3]);
        String plantilla = rutaPlantilla.concat(INGPROCAP_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(INGPROCAP_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("ingP", ejbIngPropios.getListaRegistrosParaReporte(ejercicioFiscal));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getReportePresupuestos(Short ejercicioFiscal) throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioReporte(ejes[3]);
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(ejes[3]);
        String plantilla = rutaPlantilla.concat(PRESUPUESTOS_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(PRESUPUESTOS_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("presE", ejbPresupuestos.getRegistroPresupuestos(ejercicioFiscal));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getReporteDifusion(Short ejercicio) throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioReporte(ejes[2]);
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(ejes[2]);
        String plantilla = rutaPlantilla.concat(DIFIEMS_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(DIFIEMS_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("difI", ejbDifusionIems.getRegistroDifusion(ejercicio));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);
        
        return plantillaC;
    }

    @Override
    public String getReporteVisitas(Short ejercicio) throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioReporte(ejes[2]);
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(ejes[2]);
        String plantilla = rutaPlantilla.concat(VISIND_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(VISIND_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("visI", ejbVisitasIndustriales.getRegistroVisitas(ejercicio));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);
        
        return plantillaC;
    }

    @Override
    public String getReporteBecas(Short ejercicioFiscal) throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioReporte(ejes[1]);
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(ejes[1]);
        String plantilla = rutaPlantilla.concat(BECAS_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(BECAS_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("bpe", ejbBecasPeriodo.getRegistroReporteBecas(ejercicioFiscal));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);
        
        return plantillaC;
    }

    @Override
    public String getReporteAFI(Short claveArea, Short ejercicio) throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioReporte(ejes[1]);
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(ejes[1]);
        String plantilla = rutaPlantilla.concat(ACTFORMINT_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(ACTFORMINT_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("afi", ejbActFormacionIntegral.getListaRegistrosAFI(claveArea,ejercicio));
        beans.put("pafi", ejbActFormacionIntegral.getListaRegistrosPAFI(claveArea,ejercicio));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getReporteDesercion(Short ejercicio) throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioReporte(ejes[1]);
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(ejes[1]);
        String plantilla = rutaPlantilla.concat(DESERCION_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(DESERCION_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("desA", ejbDesercionPeriodos.getRegistroDesercion(ejercicio));
        beans.put("rep", ejbDesercionReprobacion.getRegistroReprobacion(ejercicio));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getReportePerCap(Short ejercicio) throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioReporte(ejes[4]);
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(ejes[4]);
        String plantilla = rutaPlantilla.concat(PERSCAPACITADO_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(PERSCAPACITADO_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("perC",  ejbPersonalCapacitado.getRegistroPerCap(ejercicio));
        beans.put("partC",  ejbPersonalCapacitado.getRegistroPartCap(ejercicio));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getReporteFerProf(Short ejercicio) throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioReporte(ejes[2]);
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(ejes[2]);
        String plantilla = rutaPlantilla.concat(FERPROF_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(FERPROF_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("ferP", ejbFeriasProfesiograficas.getRegistroReporteFerProf(ejercicio));
        beans.put("partFP", ejbFeriasParticipantes.getRegistroReportePartFerProf(ejercicio));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);
        
        return plantillaC;
    }

    @Override
    public String getReporteMov(Short claveArea, Short ejercicio) throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioReporte(ejes[1]);
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(ejes[1]);
        String plantilla = rutaPlantilla.concat(MOVILIDAD_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(MOVILIDAD_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("mov", ejbRegistroMovilidad.getRegistroReporteMov(claveArea, ejercicio));
        beans.put("doc", ejbRegistroMovilidad.getRegistroReporteMovDoc(claveArea, ejercicio));
        beans.put("est", ejbRegistroMovilidad.getRegistroReporteMovEst(claveArea, ejercicio));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getReporteBolTrab(Short ejercicio) throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioReporte(ejes[2]);
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(ejes[2]);
        String plantilla = rutaPlantilla.concat(BOLSATRABAJO_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(BOLSATRABAJO_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("bolT", ejbBolsaTrabajo.getRegistroReporteBolTrab(ejercicio));
        beans.put("entBT", ejbBolsaTrabajo.getRegistroReporteEntBolTrab(ejercicio));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getReporteAcervo(Short ejercicio) throws Throwable {
         String rutaPlantilla = ejbCarga.crearDirectorioReporte(ejes[1]);
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(ejes[1]);
        String plantilla = rutaPlantilla.concat(ACERVO_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(ACERVO_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("acerB", ejbAcervoBibliografico.getRegistroReporteAcervoBib(ejercicio));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getReporteRecProdep(Short ejercicio) throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioReporte(ejes[1]);
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(ejes[1]);
        String plantilla = rutaPlantilla.concat(RECPRODEP_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(RECPRODEP_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("recP", ejbReconocimientoProdep.getRegistroRecProdep(ejercicio));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getReporteComAcad() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioReporte(ejes[4]);
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(ejes[4]);
        String plantilla = rutaPlantilla.concat(COMACAD_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(COMACAD_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("comA", ejbComisionesAcademicas.getRegistroReporteComAcad());
        beans.put("partCA", ejbComisionesAcademicas.getRegistroReportePartCA());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getReporteProgEst() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioReporte(ejes[4]);
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(ejes[4]);
        String plantilla = rutaPlantilla.concat(PROGESTIMULOS_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(PROGESTIMULOS_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("progE", ejbProgramasEstimulos.getRegistroReporteProgEst());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getReporteEgetsu() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioReporte(ejes[1]);
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(ejes[1]);
        String plantilla = rutaPlantilla.concat(EGETSU_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(EGETSU_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("resE", ejbEgetsuResultados.getRegistroReporteEgetsu());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getReporteExani() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioReporte(ejes[1]);
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(ejes[1]);
        String plantilla = rutaPlantilla.concat(EXANI_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(EXANI_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("resEx", ejbExaniResultados.getRegistroReporteExani());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }
    
}
