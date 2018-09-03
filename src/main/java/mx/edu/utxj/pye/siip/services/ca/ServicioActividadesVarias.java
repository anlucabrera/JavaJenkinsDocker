/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesVariasRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.entity.pye.list.ListaActividadesVarias;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbActividadesVarias;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioActividadesVarias implements EjbActividadesVarias {

    @EJB
    Facade facadePye;
    @EJB
    EjbModulos ejbModulos;
//    public static void main(String args[]){
//        try {
//            System.out.println("mx.edu.utxj.pye.siip.services.ServicioActividadesVarias.main() Main");
//            getListaActividadesVarias();
//        } catch (Throwable ex) {
//            Logger.getLogger(ServicioActividadesVarias.class.getName()).log(Level.SEVERE, null, ex);
//        } 
//    }

//    @Override
//    public List<ActividadesVarias> getListaActividadesVariasMesActual(String area, Short poa, String mes) throws Throwable {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    @Override
    public ListaActividadesVarias getListaActividadesVarias(String rutaArchivo) throws Throwable {
       ListaActividadesVarias listaActividadesVarias = new ListaActividadesVarias();
        List<ActividadesVariasRegistro> actividadesVarias = new ArrayList<>();
        ActividadesVariasRegistro actividadVaria;

        File excelActividadadesVarias = new File(rutaArchivo);
        XSSFWorkbook workBookActividadVaria = new XSSFWorkbook();
        workBookActividadVaria = (XSSFWorkbook) WorkbookFactory.create(excelActividadadesVarias);
        XSSFSheet primeraHoja = workBookActividadVaria.getSheetAt(0);
        XSSFRow fila;

        try {
            if (primeraHoja.getSheetName().equals("Actividades Varias")) {
                for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
                    fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                    if ((fila.getCell(0).getDateCellValue() != null)) {
                        actividadVaria = new ActividadesVariasRegistro();

                        switch (fila.getCell(0).getCellTypeEnum()) {
                            case NUMERIC:
                                if (DateUtil.isCellDateFormatted(fila.getCell(0))) {
                                    actividadVaria.setFechaInicio(fila.getCell(0).getDateCellValue());
                                }
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(1).getCellTypeEnum()) {
                            case NUMERIC:
                                if (DateUtil.isCellDateFormatted(fila.getCell(1))) {
                                    actividadVaria.setFechaFin(fila.getCell(1).getDateCellValue());
                                }
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(2).getCellTypeEnum()) {
                            case STRING:
                                actividadVaria.setNombre(fila.getCell(2).getStringCellValue() + i);
                                break;
                            default:
                                break;

                        }
                        switch (fila.getCell(3).getCellTypeEnum()) {
                            case STRING:
                                actividadVaria.setObjetivo(fila.getCell(3).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(4).getCellTypeEnum()) {
                            case STRING:
                                actividadVaria.setLugar(fila.getCell(4).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(5).getCellTypeEnum()) {
                            case STRING:
                                actividadVaria.setImpactoBeneficio(fila.getCell(5).getStringCellValue());
                                break;
                            default:
                                break;
                        }

                        switch (fila.getCell(6).getCellTypeEnum()) {
                            case NUMERIC:
                                actividadVaria.setTotalMujeres((int) fila.getCell(6).getNumericCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(7).getCellTypeEnum()) {
                            case NUMERIC:
                                actividadVaria.setTotalHombres((int) fila.getCell(7).getNumericCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(8).getCellTypeEnum()) {
                            case STRING:
                                actividadVaria.setPersonalidades(fila.getCell(8).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        actividadesVarias.add(actividadVaria);
                    }
                }
                workBookActividadVaria.close();
                listaActividadesVarias.setActividadesVarias(actividadesVarias);
                addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
            } else {
                workBookActividadVaria.close();
                excelActividadadesVarias.delete();
                ServicioArchivos.eliminarArchivo(rutaArchivo);
                addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
            }
        } catch (IOException e) {
            workBookActividadVaria.close();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>Ocurrió un error durante la lectura del archivo, asegurese de haber registrado correctamente su información</b>");
        }
        
        return listaActividadesVarias; 
    }

    @Override
    public void guardaActividadesVarias(ListaActividadesVarias listaActividadesVarias, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        listaActividadesVarias.getActividadesVarias().forEach((actividadesVarias) -> {
            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
            facadePye.setEntityClass(ActividadesVariasRegistro.class);
            actividadesVarias.setRegistro(registro.getRegistro());
            facadePye.create(actividadesVarias);
            facadePye.flush();
        });
    }
}
