/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.annotation.MultipartConfig;
import lombok.Getter;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativos;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosEnfermeriaTipo;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbActFormacionIntegral;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbBecasPeriodo;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbCuerposAcademicos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbDesercionReprobacion;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbPlantillasCAExcel;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbServiciosEnfermeriaCicloPeriodos;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbReconocimientoProdep;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbRegistroMovilidad;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal;

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
    
    @EJB    EjbServiciosEnfermeriaCicloPeriodos ejbServiciosEnfermeriaCicloPeriodos;
    
    @EJB  EjbBecasPeriodo ejbBecasPeriodo;
    
    @EJB  EjbDesercionReprobacion ejbDesercionReprobacion;

    @EJB
    EjbPersonal ejbSelectec;

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
    
    public static final String PARTACTFORMINT_PLANTILLA = "participantesActFormInt.xlsx";
    public static final String PARTACTFORMINT_ACTUALIZADO = "participantesActFormInt_actualizado.xlsx";

    public static final String MOVILIDAD_PLANTILLA = "registrosMovilidad.xlsx";
    public static final String MOVILIDAD_ACTUALIZADO = "registrosMovilidad_actualizado.xlsx";

    public static final String RECPRODEP_PLANTILLA = "reconocimientoProdep.xlsx";
    public static final String RECPRODEP_ACTUALIZADO = "reconocimientoProdep_actualizado.xlsx";

    public static final String CUERPACAD_PLANTILLA = "cuerpos_academicos.xlsx";
    public static final String CUERPACAD_ACTUALIZADO = "cuerpos_academicos_actualizado.xlsx";

    public static final String PROD_ACAD_PLANTILLA = "productos_academicos.xlsx";
    public static final String PROD_ACAD_ACTUALIZADO = "productos_academicos_completo.xlsx";

    public static final String ASESORIAS_TUTORIAS_PLANTILLA = "asesorias_tutorias.xlsx";
    public static final String ASESORIAS_TUTORIAS_COPIA = "asesorias_tutorias_copia.xlsx";
    public static final String ASESORIAS_TUTORIAS_ACTUALIZADO = "asesorias_tutorias_actualizado.xlsx";
    
    public static final String ACTIVIDADES_VARIAS = "actividades_varias.xlsx";

    public static final String ESTADIAS_ESTUDIANTE_PLANTILLA = "estadias_estudiante.xlsx";
    public static final String ESTADIAS_ESTUDIANTE_COPIA = "estadias_estudiante_copia.xlsx";
    public static final String ESTADIAS_ESTUDIANTE_ACTUALIZADO = "estadias_estudiante_actualizado.xlsx";
    
    public static final String SERVICIOS_ENFERMERIA_PLANTILLA = "servicios_enfermeria.xlsx";
    public static final String SERVICIOS_ENFERMERIA_COPIA = "servicios_enfermeria_copia.xlsx";
    public static final String SERVICIOS_ENFERMERIA_ACTUALIZADO = "servicios_enfermeria_actualizado.xlsx";
    
    public static final String BECAS_PLANTILLA = "becas.xlsx";
    public static final String BECAS_ACTUALIZADO = "becas_actualizado.xlsx";
    
    public static final String DESERCION_PLANTILLA = "desercionAcademica.xlsx";
    public static final String DESERCION_ACTUALIZADO = "desercionAcademica_actualizado.xlsx";
    
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
//        List<listaDTOCiclosEscolares> l = new ArrayList<>();
//        List<CiclosEscolares> ciclosEscolares = ejbCatalogos.getCiclosEscolaresAct().stream().sorted(Comparator.comparing(CiclosEscolares::getInicio).reversed()).collect(Collectors.toList());
//        ciclosEscolares.forEach(e -> {
//           
//            String strDateFormat = "yyyy";
//            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
//            String cicloe = sdf.format(e.getInicio()) + " - " + sdf.format(e.getFin());
//            Integer ciclo = e.getCiclo();
//            
//            l.add(new listaDTOCiclosEscolares(ciclo, cicloe));
//        });
//        
        beans.put("ciclosEscolares", ejbCatalogos.getCiclosEscolaresDTO());
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
        String plantilla = crearDirectorioPlantilla(ejes[1]).concat(PROD_ACAD_PLANTILLA);
        String plantillaC = crearDirectorioPlantillaCompleto(ejes[1]).concat(PROD_ACAD_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("listaPersonal", ejbSelectec.mostrarListaDeEmpleados());
        beans.put("areasUniversidad", ejbCatalogos.getAreasAcademicas());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);
        return plantillaC;
    }

    @Override
    public String getPlantillaAsesoriasTutorias(Short area) throws Throwable {
        String plantilla = crearDirectorioPlantilla(ejes[1]).concat(ASESORIAS_TUTORIAS_PLANTILLA);
        String plantillaCopia = crearDirectorioPlantillaCompleto(ejes[1]).concat(ASESORIAS_TUTORIAS_COPIA);
        String plantillaCompleto = crearDirectorioPlantillaCompleto(ejes[1]).concat(ASESORIAS_TUTORIAS_ACTUALIZADO);

        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();

            XSSFWorkbook libroAsesoriasTutorias = new XSSFWorkbook();
            libroAsesoriasTutorias = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);

            XSSFSheet catalogos1 = libroAsesoriasTutorias.getSheetAt(1);

            List<AreasUniversidad> programasEducativos = ejbCatalogos.getProgramasEducativoPorAreaAcademica(area);
            List<ProgramasEducativos> programasEducativosProntuario = ejbCatalogos.getProgramasEducativosProntuario();
            List<CiclosEscolares> ciclosEscolares = ejbCatalogos.getCiclosEscolaresAct().stream().sorted(Comparator.comparing(CiclosEscolares::getInicio).reversed()).collect(Collectors.toList());
            List<PeriodosEscolares> periodosEscolares = ejbCatalogos.getPeriodosEscolaresAct().stream().sorted(Comparator.comparing(PeriodosEscolares::getPeriodo).reversed()).collect(Collectors.toList());

            XSSFRow fila;
            XSSFCell celda;

//            Programas Educativos
            for (Integer listaPE = 0; listaPE < programasEducativos.size(); listaPE++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = listaPE + 2;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos1.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos1.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Programa Educativo
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(programasEducativos.get(listaPE).getNombre());

//                Area
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.NUMERIC);
                }
                fila.getCell(2).setCellValue(programasEducativos.get(listaPE).getArea());

//                Siglas
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(programasEducativos.get(listaPE).getSiglas());
            }

//            Siglas 2 - Programas Educativos 2
            for (Integer listaPP = 0; listaPP < programasEducativosProntuario.size(); listaPP++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = listaPP + 2;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos1.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos1.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Siglas
                celda = fila.getCell(11);
                if (null == celda) {
                    fila.createCell(11, CellType.STRING);
                }
                fila.getCell(11).setCellValue(programasEducativosProntuario.get(listaPP).getSiglas());

//                Nivel
                celda = fila.getCell(12);
                if (null == celda) {
                    fila.createCell(12, CellType.STRING);
                }
                fila.getCell(12).setCellValue(programasEducativosProntuario.get(listaPP).getNivel().getNivel());
//                TODO: Actualizar metodo de guardado y terminar plantilla de asesorias y tutorias y actualizar el formato
            }

            XSSFSheet catalogos2 = libroAsesoriasTutorias.getSheetAt(2);

//            Ciclos Escolares
            for (Integer listaCE = 0; listaCE < ciclosEscolares.size(); listaCE++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = listaCE + 3;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos2.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos2.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Ciclo escolar
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                LocalDate inicioGeneracion = ciclosEscolares.get(listaCE).getInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate finGeneracion = ciclosEscolares.get(listaCE).getFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String cicloCompuesto = inicioGeneracion.getYear() + "-" + finGeneracion.getYear();
                fila.getCell(1).setCellValue(cicloCompuesto);

//                Clave
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.NUMERIC);
                }
                fila.getCell(2).setCellValue(ciclosEscolares.get(listaCE).getCiclo());
            }

//            Periodos Escolares
            for (Integer listaPE = 0; listaPE < periodosEscolares.size(); listaPE++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = listaPE + 3;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos2.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos2.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Ciclo
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.NUMERIC);
                }
                fila.getCell(4).setCellValue(periodosEscolares.get(listaPE).getCiclo().getCiclo());

//                Cuatrimestre
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.STRING);
                }
                String compuesto = periodosEscolares.get(listaPE).getMesInicio().getMes() + "-" + periodosEscolares.get(listaPE).getMesFin().getMes();
                fila.getCell(5).setCellValue(compuesto);

//                Auxiliar
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.STRING);
                }
                fila.getCell(6).setCellValue(periodosEscolares.get(listaPE).getCiclo().getCiclo() + compuesto);

//                Periodo
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.NUMERIC);
                }
                fila.getCell(7).setCellValue(periodosEscolares.get(listaPE).getPeriodo());

//                Auxiliar
                celda = fila.getCell(11);
                if (null == celda) {
                    fila.createCell(11, CellType.STRING);
                }
                fila.getCell(11).setCellValue(periodosEscolares.get(listaPE).getCiclo().getCiclo() + compuesto);

//                Periodo
                celda = fila.getCell(12);
                if (null == celda) {
                    fila.createCell(12, CellType.NUMERIC);
                }
                fila.getCell(12).setCellValue(periodosEscolares.get(listaPE).getPeriodo());

            }
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            libroAsesoriasTutorias.write(archivoSalida);
            libroAsesoriasTutorias.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioPlantillasCAExcel.getPlantillaAsesoriasTutorias() Archivo no escontrado");
        } catch (IOException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioPlantillasCAExcel.getPlantillaAsesoriasTutorias() Error de lectura o escritura (i/o)");
        }
        return plantillaCompleto;
    }

    @Override
    public String getPlantillaActividadesVarias() throws Throwable {
        return crearDirectorioPlantilla(ejes[1]).concat(ACTIVIDADES_VARIAS);     
    }

    @Override
    public String getPlantillaEstadiasPorEstudiante(Short area) throws Throwable {
        String plantilla = crearDirectorioPlantilla(ejes[1]).concat(ESTADIAS_ESTUDIANTE_PLANTILLA);
        String plantillaCopia = crearDirectorioPlantillaCompleto(ejes[1]).concat(ESTADIAS_ESTUDIANTE_COPIA);
        String plantillaCompleto = crearDirectorioPlantillaCompleto(ejes[1]).concat(ESTADIAS_ESTUDIANTE_ACTUALIZADO);
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();

            XSSFWorkbook libroEstadiasPorEstudiante = new XSSFWorkbook();
            libroEstadiasPorEstudiante = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);

            XSSFSheet catalogos = libroEstadiasPorEstudiante.getSheetAt(1);
            
            List<CiclosEscolares> ciclosEscolares = ejbCatalogos.getCiclosEscolaresAct().stream().sorted(Comparator.comparing(CiclosEscolares::getInicio).reversed()).collect(Collectors.toList());
            List<Generaciones> generaciones = ejbCatalogos.getGeneracionesAct().stream().sorted(Comparator.comparing(Generaciones::getGeneracion).reversed()).collect(Collectors.toList());
            List<PeriodosEscolares> periodosEscolares = ejbCatalogos.getPeriodosEscolaresAct().stream().sorted(Comparator.comparing(PeriodosEscolares::getPeriodo).reversed()).collect(Collectors.toList());
            List<AreasUniversidad> programasEducativos = ejbCatalogos.getProgramasEducativoPorAreaAcademica(area);
            List<OrganismosVinculados> organismosVinculados = ejbOrganismosVinculados.getOrganismosVinculadoVigentes();
            
            XSSFRow fila;
            XSSFCell celda;
            
//            Ciclos Escolares
            for (Integer ce = 0; ce < ciclosEscolares.size(); ce++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = ce + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Ciclos Escolar
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                LocalDate inicioCiclo = ciclosEscolares.get(ce).getInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate finCiclo = ciclosEscolares.get(ce).getFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String cicloCompuesto = inicioCiclo.getYear() + "-" + finCiclo.getYear();
                fila.getCell(1).setCellValue(cicloCompuesto);
                
//                Clave
                celda = fila.getCell(2);
                if(null == celda){
                    fila.createCell(2, CellType.NUMERIC);
                }
                fila.getCell(2).setCellValue(ciclosEscolares.get(ce).getCiclo());
            }
            
//            Generaciones
            for (Integer gen = 0; gen < generaciones.size(); gen++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = gen + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Generación
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.STRING);
                }
                String generacionCompuesto = generaciones.get(gen).getInicio() + "-" + generaciones.get(gen).getFin();
                fila.getCell(4).setCellValue(generacionCompuesto);
                
//                Clave
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.NUMERIC);
                }
                fila.getCell(5).setCellValue(generaciones.get(gen).getGeneracion());
            }
            
//            Periodos Escolares
            for (Integer pe = 0; pe < periodosEscolares.size(); pe++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = pe + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Ciclo
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.NUMERIC);
                }
                fila.getCell(7).setCellValue(periodosEscolares.get(pe).getCiclo().getCiclo());
                
//                Cuatrimestre
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.STRING);
                }
                fila.getCell(8).setCellValue(periodosEscolares.get(pe).getMesInicio().getMes()+"-"+periodosEscolares.get(pe).getMesFin().getMes());                
                
//                Auxiliar
                celda = fila.getCell(9);
                if (null == celda) {
                    fila.createCell(9, CellType.STRING);
                }
                fila.getCell(9).setCellValue(periodosEscolares.get(pe).getCiclo().getCiclo()+periodosEscolares.get(pe).getMesInicio().getMes()+"-"+periodosEscolares.get(pe).getMesFin().getMes());
                
//                Periodo
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.NUMERIC);
                }
                fila.getCell(10).setCellValue(periodosEscolares.get(pe).getPeriodo());
            }
            
//            Programas Educativos
            for (Integer au = 0; au < programasEducativos.size(); au++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = au + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Programa Educativo
                celda = fila.getCell(12);
                if (null == celda) {
                    fila.createCell(12, CellType.STRING);
                }
                fila.getCell(12).setCellValue(programasEducativos.get(au).getNombre());
                
//                Nivel (Siglas)
                celda = fila.getCell(13);
                if (null == celda) {
                    fila.createCell(13, CellType.STRING);
                }
                fila.getCell(13).setCellValue(programasEducativos.get(au).getSiglas());
                
//                Clave
                celda = fila.getCell(14);
                if (null == celda) {
                    fila.createCell(14, CellType.NUMERIC);
                }
                fila.getCell(14).setCellValue(programasEducativos.get(au).getArea());
            }
            
//            Empresas Actividad Estadia
            for (Integer eae = 0; eae < organismosVinculados.size(); eae++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = eae + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Empresa Nombre
                celda = fila.getCell(16);
                if (null == celda) {
                    fila.createCell(16, CellType.STRING);
                }
                fila.getCell(16).setCellValue(organismosVinculados.get(eae).getNombre());
                
//                Clave
                celda = fila.getCell(17);
                if (null == celda) {
                    fila.createCell(17, CellType.NUMERIC);
                }
                fila.getCell(17).setCellValue(organismosVinculados.get(eae).getEmpresa());
            }
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            libroEstadiasPorEstudiante.write(archivoSalida);
            libroEstadiasPorEstudiante.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioPlantillasCAExcel.getPlantillaEstadiasPorEstudiante() Archivo no escontrado");
        } catch (IOException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioPlantillasCAExcel.getPlantillaEstadiasPorEstudiante() Error de lectura o escritura (i/o)");
        }
        return plantillaCompleto;
    }

   @Override
    public String getPlantillaServiciosEnfermeria() throws Throwable {
        String plantilla = crearDirectorioPlantilla(ejes[1]).concat(SERVICIOS_ENFERMERIA_PLANTILLA);
        String plantillaCopia = crearDirectorioPlantillaCompleto(ejes[1]).concat(SERVICIOS_ENFERMERIA_COPIA);
        String plantillaCompleto = crearDirectorioPlantillaCompleto(ejes[1]).concat(SERVICIOS_ENFERMERIA_ACTUALIZADO);
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();

            XSSFWorkbook libroServiciosEnfermeria = new XSSFWorkbook();
            libroServiciosEnfermeria = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);

            XSSFSheet catalogos = libroServiciosEnfermeria.getSheetAt(1);
            
            List<CiclosEscolares> ciclosEscolares = ejbCatalogos.getCiclosEscolaresAct().stream().sorted(Comparator.comparing(CiclosEscolares::getInicio).reversed()).collect(Collectors.toList());
            List<PeriodosEscolares> periodosEscolares = ejbCatalogos.getPeriodosEscolaresAct().stream().sorted(Comparator.comparing(PeriodosEscolares::getPeriodo).reversed()).collect(Collectors.toList());
            List<ServiciosEnfermeriaTipo> serviciosEnfermeriaTipos = ejbServiciosEnfermeriaCicloPeriodos.getServiciosEnfermeriaTipos();
            
            XSSFRow fila;
            XSSFCell celda;
            
//            Ciclos Escolares
            for (Integer ce = 0; ce < ciclosEscolares.size(); ce++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = ce + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Ciclos Escolar
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                LocalDate inicioCiclo = ciclosEscolares.get(ce).getInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate finCiclo = ciclosEscolares.get(ce).getFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String cicloCompuesto = inicioCiclo.getYear() + "-" + finCiclo.getYear();
                fila.getCell(1).setCellValue(cicloCompuesto);
                
//                Clave
                celda = fila.getCell(2);
                if(null == celda){
                    fila.createCell(2, CellType.NUMERIC);
                }
                fila.getCell(2).setCellValue(ciclosEscolares.get(ce).getCiclo());
            }
            
//            Periodos Escolares
            for (Integer pe = 0; pe < periodosEscolares.size(); pe++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = pe + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Ciclo
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.NUMERIC);
                }
                fila.getCell(4).setCellValue(periodosEscolares.get(pe).getCiclo().getCiclo());
                
//                Cuatrimestre
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.STRING);
                }
                fila.getCell(5).setCellValue(periodosEscolares.get(pe).getMesInicio().getMes()+"-"+periodosEscolares.get(pe).getMesFin().getMes());                
                
//                Auxiliar
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.STRING);
                }
                fila.getCell(6).setCellValue(periodosEscolares.get(pe).getCiclo().getCiclo()+periodosEscolares.get(pe).getMesInicio().getMes()+"-"+periodosEscolares.get(pe).getMesFin().getMes());
                
//                Periodo
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.NUMERIC);
                }
                fila.getCell(7).setCellValue(periodosEscolares.get(pe).getPeriodo());
            }
            
//            Servicios Enfermeria
            for (Integer se = 0; se < serviciosEnfermeriaTipos.size(); se++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = se + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Nombre
                celda = fila.getCell(9);
                if (null == celda) {
                    fila.createCell(9, CellType.STRING);
                }
                fila.getCell(9).setCellValue(serviciosEnfermeriaTipos.get(se).getDescripcion());
                
//                Clave
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.NUMERIC);
                }
                fila.getCell(10).setCellValue(serviciosEnfermeriaTipos.get(se).getServicio());
            }
            
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            libroServiciosEnfermeria.write(archivoSalida);
            libroServiciosEnfermeria.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioPlantillasCAExcel.getPlantillaServiciosEnfermeria() Archivo no escontrado");
        } catch (IOException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioPlantillasCAExcel.getPlantillaServiciosEnfermeria() Error de lectura o escritura (i/o)");
        }
        return plantillaCompleto;       
    }
    
    @Override
    public String getPlantillaBecas() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[1]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[1]);
        String plantilla = rutaPlantilla.concat(BECAS_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(BECAS_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("becaTipos", ejbCatalogos.getBecaTiposAct());
        beans.put("ciclosEscolares", ejbCatalogos.getCiclosEscolaresAct());
        beans.put("periodosEscolares", ejbCatalogos.getPeriodosEscolaresAct());
        beans.put("matriculaPeriodosEscolares", ejbMatriculaPeriodosEscolares.getMatriculasVigentes());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getPlantillaDesercion() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[1]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[1]);
        String plantilla = rutaPlantilla.concat(DESERCION_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(DESERCION_ACTUALIZADO);
        Map beans = new HashMap();
//        beans.put("ciclosEscolares", ejbCatalogos.getCiclosEscolaresAct());
        beans.put("periodosEscolares", ejbCatalogos.getPeriodosEscolaresAct());
        beans.put("matriculaPeriodosEscolares", ejbMatriculaPeriodosEscolares.getMatriculasVigentes());
//        beans.put("generaciones", ejbCatalogos.getGeneracionesAct());
        beans.put("causasBaja", ejbCatalogos.getCausasBajaAct());
        beans.put("tiposBaja", ejbCatalogos.getTipoBajaAct());
        beans.put("materiasProgramaEducativo", ejbDesercionReprobacion.getMateriasProgramaEducativoAct());
        beans.put("listaPersonal", ejbSelectec.mostrarListaDeEmpleados());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }
    @Override
    public String getPlantillaPartActFormacionIntegral(String actForIntClave) throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[1]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[1]);
        String plantilla = rutaPlantilla.concat(PARTACTFORMINT_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(PARTACTFORMINT_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("claveActividad", actForIntClave);
        beans.put("matriculaPeriodosEscolares", ejbMatriculaPeriodosEscolares.getMatriculasVigentes());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getReporteCuerposAcademicos() throws Throwable {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
