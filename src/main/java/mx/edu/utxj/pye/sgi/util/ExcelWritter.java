/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.util;

import edu.mx.utxj.pye.seut.util.preguntas.Pregunta;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonalDesempenioEvaluacion;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonalEvaluacion360Promedios;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonalEvaluacion360Reporte;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author UTXJ
 */
public class ExcelWritter implements Serializable {

    private static final long serialVersionUID = -2412349197367329490L;
    @Getter @Setter private XSSFWorkbook libro;
    @Getter private String nombre, salida, base;
    @Getter private Integer id;
    @Getter private final Integer filaDatosMin = 10;
    @Getter private final Integer filaDatosMax = 59;

    @Getter ListaPersonal directivo;
    @Getter Integer evaluacion;
    @Getter PeriodosEscolares periodoEscolar;
    @Getter EvaluacionesTipo tipo;
    @Getter DesempenioEvaluaciones desempenioEvaluacion;
    @Getter List<ListaPersonalDesempenioEvaluacion> resultadosDesempenio;
    @Getter Evaluaciones360 evaluaciones360;
    @Getter List<ListaPersonalEvaluacion360Promedios> listaPromedios;
    @Getter List<ListaPersonalEvaluacion360Reporte> listaResportes;
    @Getter Map<Short,Apartado> mapaHabilidades;

    InputStream in;
    OutputStream out;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private final DecimalFormat df = new DecimalFormat("#.#");
    private final DecimalFormat df2 = new DecimalFormat("#.##");

    public ExcelWritter(ListaPersonal directivo, DesempenioEvaluaciones desempenioEvaluacion, List<ListaPersonalDesempenioEvaluacion> resultadosDesempenio, PeriodosEscolares periodoEscolar) {
        this.directivo = directivo;
        this.desempenioEvaluacion = desempenioEvaluacion;
        this.resultadosDesempenio = resultadosDesempenio;
        this.periodoEscolar = periodoEscolar;
        this.evaluacion = desempenioEvaluacion.getEvaluacion();
        tipo = EvaluacionesTipo.EVALUACION_DESEMPENIO;
    }

    public ExcelWritter(Evaluaciones360 evaluaciones360, List<ListaPersonalEvaluacion360Promedios> listaPromedios, List<ListaPersonalEvaluacion360Reporte> listaResportes, PeriodosEscolares periodoEscolar, Map<Short,Apartado> mapaHabilidades) {
        this.periodoEscolar = periodoEscolar;
        this.evaluaciones360 = evaluaciones360;
        this.listaPromedios = listaPromedios;
        this.listaResportes = listaResportes;
        this.evaluacion = evaluaciones360.getEvaluacion();
        this.mapaHabilidades = mapaHabilidades;
        tipo = EvaluacionesTipo.EVALUACION_360;
    }

    public void obtenerLibro() {
        this.nombre = "cedulas.xlsx";
        try {
            in = new FileInputStream(ServicioArchivos.carpetaRaiz.concat(File.separator).concat(nombre));
            libro = new XSSFWorkbook(in);

            if (tipo == EvaluacionesTipo.EVALUACION_DESEMPENIO) {
                libro.removeSheetAt(0);
                libro.removeSheetAt(0);
                libro.removeSheetAt(1);
                libro.removeSheetAt(1);
                libro.removeSheetAt(1);
            } else if (tipo == EvaluacionesTipo.EVALUACION_360) {
                libro.removeSheetAt(1);
                libro.removeSheetAt(1);
                libro.removeSheetAt(1);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExcelWritter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExcelWritter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarLibro() {
        int index = 1;
        
        if (tipo == EvaluacionesTipo.EVALUACION_DESEMPENIO) {
            for (ListaPersonalDesempenioEvaluacion rd : resultadosDesempenio.stream().filter(item -> item.isCompleto()).collect(Collectors.toList())) {
                libro.cloneSheet(0, String.valueOf(rd.getPk().getEvaluado()));
                XSSFSheet hojaDesempenio = libro.getSheetAt(index);
                escribirCelda(hojaDesempenio.getRow(3), 2, Datos.getPeriodoEscolarNombre(periodoEscolar));
                escribirCelda(hojaDesempenio.getRow(5), 2, rd.getEvaluadoNombre());
                escribirCelda(hojaDesempenio.getRow(6), 2, String.valueOf(rd.getPk().getEvaluado()));
                escribirCelda(hojaDesempenio.getRow(6), 7, rd.getEvaluadoGeneroNombre());
                escribirCelda(hojaDesempenio.getRow(7), 2, rd.getEvaluadoAreaOperativaNombre());
                escribirCelda(hojaDesempenio.getRow(8), 2, rd.getEvaluadoCategoriaOperativaNombre());
                escribirCelda(hojaDesempenio.getRow(9), 2, String.valueOf(rd.getEvaluadoActividadNombre()));

                escribirCelda(hojaDesempenio.getRow(11), 3, rd.getEvaluadorNombre());
                escribirCelda(hojaDesempenio.getRow(12), 3, rd.getEvaluadorCategoriaOperativaNombre());

                escribirCelda(hojaDesempenio.getRow(14), 13, rd.getR1());
                escribirCelda(hojaDesempenio.getRow(16), 13, rd.getR2());
                escribirCelda(hojaDesempenio.getRow(17), 13, rd.getR3());
                escribirCelda(hojaDesempenio.getRow(19), 13, rd.getR4());
                escribirCelda(hojaDesempenio.getRow(20), 13, rd.getR5());
                escribirCelda(hojaDesempenio.getRow(22), 13, rd.getR6());
                escribirCelda(hojaDesempenio.getRow(24), 13, rd.getR7());
                escribirCelda(hojaDesempenio.getRow(26), 13, rd.getR8());
                escribirCelda(hojaDesempenio.getRow(27), 13, rd.getR9());
                escribirCelda(hojaDesempenio.getRow(29), 13, rd.getR10());
                escribirCelda(hojaDesempenio.getRow(30), 13, rd.getR11());
                escribirCelda(hojaDesempenio.getRow(32), 13, rd.getR12());
                escribirCelda(hojaDesempenio.getRow(33), 13, rd.getR13());
                escribirCelda(hojaDesempenio.getRow(34), 13, rd.getR14());
                escribirCelda(hojaDesempenio.getRow(36), 13, rd.getR15());
                escribirCelda(hojaDesempenio.getRow(38), 13, rd.getR16());
                escribirCelda(hojaDesempenio.getRow(40), 13, rd.getR17());
                escribirCelda(hojaDesempenio.getRow(42), 13, rd.getR18());
                escribirCelda(hojaDesempenio.getRow(44), 13, rd.getR19());
                escribirCelda(hojaDesempenio.getRow(45), 13, rd.getR20());
                escribirCelda(hojaDesempenio.getRow(47), 1, rd.getR21());

                escribirCelda(hojaDesempenio.getRow(48), 12, rd.getPromedio());

                index++;
            }
            libro.removeSheetAt(0);
        } else if (tipo == EvaluacionesTipo.EVALUACION_360) {
            Map<Integer, List<String>> completos = new HashMap();
            completos.put(0, Arrays.asList("0, 0, 0", "0, 0"));
            completos.put(1, Arrays.asList("1, 0, 0", "0, 1, 0", "0, 0, 1", "1, 0", "0, 1"));
            completos.put(2, Arrays.asList("1, 1, 0", "1, 0, 1", "0, 1, 1"));
            completos.put(3, Arrays.asList("1, 1, 1", "1, 1"));

            for (ListaPersonalEvaluacion360Promedios lpe : listaPromedios) {
//                System.out.println("mx.edu.utxj.pye.sgi.util.ExcelWritter.editarLibro() lpe: " + lpe);
                libro.cloneSheet(0, String.valueOf(lpe.getPk().getEvaluado()));
                XSSFSheet hoja = libro.getSheetAt(index + 2);
                if (completos.get(0).contains(lpe.getCompleto())) {
                    hoja.setTabColor(new XSSFColor(Color.red));
                } else if (completos.get(1).contains(lpe.getCompleto())) {
                    hoja.setTabColor(new XSSFColor(Color.yellow));
                } else if (completos.get(2).contains(lpe.getCompleto())) {
                    hoja.setTabColor(new XSSFColor(Color.orange));
                } else if (completos.get(3).contains(lpe.getCompleto())) {
                    hoja.setTabColor(new XSSFColor(Color.green));
                }

                escribirCelda(hoja.getRow(10), 2, (new Date()));
                escribirCelda(hoja.getRow(10), 7, Datos.getPeriodoEscolarNombre(periodoEscolar));
                escribirCelda(hoja.getRow(11), 4, lpe.getPk().getEvaluado());
                escribirCelda(hoja.getRow(12), 4, lpe.getEvaluadoNombre());
                escribirCelda(hoja.getRow(12), 14, lpe.getEvaluadoGeneroNombre());
                escribirCelda(hoja.getRow(13), 4, lpe.getEvaluadoAreaOperativaNombre());
//                escribirCelda(hoja.getRow(14), 4, lpe.getCategoriaNombre()); // se reescribio para la impresion de cedulas 31/05/18
                escribirCelda(hoja.getRow(14), 4, lpe.getEvaluadoCategoriaOperativaNombre());
                escribirCelda(hoja.getRow(15), 4, lpe.getEvaluadoFechaIngreso());
                escribirCelda(hoja.getRow(15), 10, lpe.getEvaluadoActividadNombre());

                escribirCelda(hoja.getRow(18), 7, redondear(lpe.getR1()));
                escribirCelda(hoja.getRow(19), 7, redondear(lpe.getR2()));
                escribirCelda(hoja.getRow(20), 7, redondear(lpe.getR3()));
                escribirCelda(hoja.getRow(21), 7, redondear(lpe.getR4()));
                escribirCelda(hoja.getRow(22), 7, redondear(lpe.getR5()));
                escribirCelda(hoja.getRow(23), 7, redondear(lpe.getR6()));
                escribirCelda(hoja.getRow(24), 7, redondear(lpe.getR7()));
                escribirCelda(hoja.getRow(25), 7, redondear(lpe.getR8()));
                escribirCelda(hoja.getRow(26), 7, redondear(lpe.getR9()));
                escribirCelda(hoja.getRow(27), 7, redondear(lpe.getR10()));
                escribirCelda(hoja.getRow(28), 7, redondear(lpe.getR11()));
                escribirCelda(hoja.getRow(29), 7, redondear(lpe.getR12()));
                escribirCelda(hoja.getRow(30), 7, redondear(lpe.getR13()));
                escribirCelda(hoja.getRow(31), 7, redondear(lpe.getR14()));
                escribirCelda(hoja.getRow(32), 7, redondear(lpe.getR15()));
                escribirCelda(hoja.getRow(33), 7, redondear(lpe.getR16()));
                
//                System.out.println("mx.edu.utxj.pye.sgi.util.ExcelWritter.editarLibro() lpe: " + lpe);
                int index2 =18;
                Apartado habilidades = mapaHabilidades.get(lpe.getCategoria());
//                System.out.println("mx.edu.utxj.pye.sgi.util.ExcelWritter.editarLibro(" + lpe.getCategoria() + ") habilidades: " + habilidades);
                for(Pregunta p : habilidades.getPreguntas()){
                    escribirCelda(hoja.getRow(index2), 9, df.format(p.getNumero()).concat(") ").concat(p.getTitulo()));
                    if (obtenerRespuestaPorPregunta(lpe, p.getNumero()) != null) {
                        escribirCelda(hoja.getRow(index2), 14, redondear(Double.valueOf(obtenerRespuestaPorPregunta(lpe, p.getNumero()))));
                    }
                    index2++;
                }
                
                escribirCelda(hoja.getRow(49), 12, lpe.getPromedio());

                index++;
            }
            libro.removeSheetAt(0);

            XSSFSheet hojaResumen = libro.getSheetAt(0);
            index = 1;
            for (ListaPersonalEvaluacion360Promedios lpe : listaPromedios) {
//                System.out.println("mx.edu.utxj.pye.sgi.util.ExcelWritter.editarLibro() index: " + index);
                crearFila(hojaResumen, index);
                escribirCelda(hojaResumen.getRow(index), 0, lpe.getPk().getEvaluado());
                escribirCelda(hojaResumen.getRow(index), 1, lpe.getEvaluadoNombre());
                escribirCelda(hojaResumen.getRow(index), 2, lpe.getEvaluadoAreaOperativaNombre());
                escribirCelda(hojaResumen.getRow(index), 3, lpe.getEvaluadores());
                escribirCelda(hojaResumen.getRow(index), 4, lpe.getCompleto());
                escribirCelda(hojaResumen.getRow(index), 5, lpe.getPromedio());
                escribirCelda(hojaResumen.getRow(index), 6, lpe.getR32());
                escribirCelda(hojaResumen.getRow(index), 7, lpe.getR33());
                index++;
            }
            
            XSSFSheet hojaReporte = libro.getSheetAt(1);
            index = 1;
            for(ListaPersonalEvaluacion360Reporte reporte : listaResportes){
                crearFila(hojaReporte, index);
                escribirCelda(hojaReporte.getRow(index), 0, reporte.getPk().getEvaluador());
                escribirCelda(hojaReporte.getRow(index), 1, reporte.getEvaluadorNombre());
                escribirCelda(hojaReporte.getRow(index), 2, reporte.getEvaluadorAreaOperativaNombre());
                escribirCelda(hojaReporte.getRow(index), 3, reporte.getCompleto());
                escribirCelda(hojaReporte.getRow(index), 4, reporte.getResultado());
                escribirCelda(hojaReporte.getRow(index), 5, reporte.getEvaluados());
                escribirCelda(hojaReporte.getRow(index), 6, reporte.getEvaluadosNombre());
                index++;
            }
        }
    }

    public void escribirLibro() {
        try {
            base = ServicioArchivos.carpetaRaiz
                    .concat("cedulas").concat(File.separator)
                    .concat(evaluacion.toString()).concat(File.separator)
                    .concat(tipo.name()).concat(File.separator);

            ServicioArchivos.addCarpetaRelativa(base);
            salida = nombre;

            ServicioArchivos.eliminarArchivo(base.concat(salida));

            out = new FileOutputStream(new File(base.concat(salida)));
            libro.write(out);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExcelWritter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExcelWritter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String enviarLibro() {
//        System.out.println("jvv.aldesa.sgot.util.ExcelWritter.enviarLibro() ruta: " + base.concat(salida));
        File file = new File(base.concat(salida));
        String ruta = "media".concat(file.toURI().toString().split("archivos")[1]);
//        System.out.println("jvv.aldesa.sgot.util.ExcelWritter.enviarLibro() ruta 2: " + ruta);
        return ruta;
//        return new File(base.concat(salida));
    }
    
    private Double redondear(Double valor){
        if(valor != null){
            return Double.valueOf(df2.format(valor));
        }else{
            return 0D;
        }
    }
    
    private void crearFila(XSSFSheet hoja, Integer index){
        if(hoja.getRow(index) == null){
            hoja.createRow(index);
        }
    }

    private void escribirCelda(XSSFRow fila, Integer indice, String valor) {
        if (valor != null) {
            XSSFCell celda = fila.getCell(indice);
            if (celda == null) {
                fila.createCell(indice, CellType.STRING);
            }
            fila.getCell(indice).setCellType(CellType.STRING);
            fila.getCell(indice).setCellValue(valor);
        }
    }

    private void escribirCelda(XSSFRow fila, Integer indice, Short valor) {
        if (valor != null) {
            XSSFCell celda = fila.getCell(indice);
            if (celda == null) {
                fila.createCell(indice, CellType.NUMERIC);
            }
            fila.getCell(indice).setCellType(CellType.NUMERIC);
            fila.getCell(indice).setCellValue(valor);
        }
    }

    @SneakyThrows(NullPointerException.class)
    private void escribirCelda(XSSFRow fila, Integer indice, Double valor) {
        if (valor != null) {
            XSSFCell celda = fila.getCell(indice);
            if (celda == null) {
                fila.createCell(indice, CellType.NUMERIC);
            }
            fila.getCell(indice).setCellType(CellType.NUMERIC);
            fila.getCell(indice).setCellValue(valor);
        }
    }

    private void escribirCelda(XSSFRow fila, Integer indice, Integer valor) {
        if (valor != null) {
//            System.out.println("mx.edu.utxj.pye.sgi.util.ExcelWritter.escribirCelda(" + indice + ") valor: " + valor);
//            System.out.println("mx.edu.utxj.pye.sgi.util.ExcelWritter.escribirCelda(" + indice + ") fila: " + fila);
//            System.out.println("mx.edu.utxj.pye.sgi.util.ExcelWritter.escribirCelda(" + indice + ") celda: " + fila.getCell(indice));
            XSSFCell celda = fila.getCell(indice);
            if (celda == null) {
                fila.createCell(indice, CellType.NUMERIC);
            }
            fila.getCell(indice).setCellType(CellType.NUMERIC);
            fila.getCell(indice).setCellValue(valor);
        }
    }

    private void escribirCelda(XSSFRow fila, Integer indice, Date valor) {
        escribirCelda(fila, indice, sdf.format(valor));
    }
    
    public String obtenerRespuestaPorPregunta(ListaPersonalEvaluacion360Promedios resultado, Float pregunta) {
        String res = null;
        switch (pregunta.toString()) {
            case "1.0": res = resultado.getR1()!=null ? resultado.getR1().toString() : null; break;
            case "2.0": res = resultado.getR2()!=null ? resultado.getR2().toString() : null; break;
            case "3.0": res = resultado.getR3()!=null ? resultado.getR3().toString() : null; break;
            case "4.0": res = resultado.getR4()!=null ? resultado.getR4().toString() : null; break;
            case "5.0": res = resultado.getR5()!=null ? resultado.getR5().toString() : null; break;
            case "6.0": res = resultado.getR6()!=null ? resultado.getR6().toString() : null; break;
            case "7.0": res = resultado.getR7()!=null ? resultado.getR7().toString() : null; break;
            case "8.0": res = resultado.getR8()!=null ? resultado.getR8().toString() : null; break;
            case "9.0": res = resultado.getR9()!=null ? resultado.getR9().toString() : null; break;
            case "10.0": res = resultado.getR10()!=null ? resultado.getR10().toString() : null; break;
            case "11.0": res = resultado.getR11()!=null ? resultado.getR11().toString() : null; break;
            case "12.0": res = resultado.getR12()!=null ? resultado.getR12().toString() : null; break;
            case "13.0": res = resultado.getR13()!=null ? resultado.getR13().toString() : null; break;
            case "14.0": res = resultado.getR14()!=null ? resultado.getR14().toString() : null; break;
            case "15.0": res = resultado.getR15()!=null ? resultado.getR15().toString() : null; break;
            case "16.0": res = resultado.getR16()!=null ? resultado.getR16().toString() : null; break;
            case "17.0": res = resultado.getR17()!=null ? resultado.getR17().toString() : null; break;
            case "18.0": res = resultado.getR18()!=null ? resultado.getR18().toString() : null; break;
            case "19.0": res = resultado.getR19()!=null ? resultado.getR19().toString() : null; break;
            case "20.0": res = resultado.getR20()!=null ? resultado.getR20().toString() : null; break;
            case "21.0": res = resultado.getR21()!=null ? resultado.getR21().toString() : null; break;
            case "22.0": res = resultado.getR22()!=null ? resultado.getR22().toString() : null; break;
            case "23.0": res = resultado.getR23()!=null ? resultado.getR23().toString() : null; break;
            case "24.0": res = resultado.getR24()!=null ? resultado.getR24().toString() : null; break;
            case "25.0": res = resultado.getR25()!=null ? resultado.getR25().toString() : null; break;
            case "26.0": res = resultado.getR26()!=null ? resultado.getR26().toString() : null; break;
            case "27.0": res = resultado.getR27()!=null ? resultado.getR27().toString() : null; break;
            case "28.0": res = resultado.getR28()!=null ? resultado.getR28().toString() : null; break;
            case "29.0": res = resultado.getR29()!=null ? resultado.getR29().toString() : null; break;
            case "30.0": res = resultado.getR30()!=null ? resultado.getR30().toString() : null; break;
            case "31.0": res = resultado.getR31()!=null ? resultado.getR31().toString() : null; break;
            case "32.0": res = resultado.getR32()!=null ? resultado.getR32() : null; break;
            case "33.0": res = resultado.getR33()!=null ? resultado.getR33() : null; break;
        }
        
        return res;
    }
}
