/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EgetsuResultadosGeneraciones;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.escolar.DTOEgetsu;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaEgetsuResultados;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbEgetsuResultados;
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
public class ServicioEgetsuResultados implements EjbEgetsuResultados{
    @EJB
    Facade facdepye;
    @EJB
    EjbModulos ejbModulos;
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @Override
    public ListaEgetsuResultados getListaEgetsuResultados(String rutaArchivo) throws Throwable {
       
        ListaEgetsuResultados listaEgetsuResultados = new ListaEgetsuResultados();

        List<DTOEgetsu> listaDtoEgetsu = new ArrayList<>();
        EgetsuResultadosGeneraciones egetsuResultadosGeneraciones;
        DTOEgetsu dTOEgetsu;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        if (primeraHoja.getSheetName().equals("EGETSU")) {
        for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

            if ((!"".equals(fila.getCell(0).getStringCellValue()))) {
                egetsuResultadosGeneraciones = new EgetsuResultadosGeneraciones();
                dTOEgetsu = new DTOEgetsu();
                
               switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        dTOEgetsu.setGeneracion(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
               switch (fila.getCell(1).getCellTypeEnum()) {
                    case FORMULA:
                        egetsuResultadosGeneraciones.setGeneracion((short)fila.getCell(1).getNumericCellValue());
                        break;
                    default:
                        break;
                }
              
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case NUMERIC:
                        egetsuResultadosGeneraciones.setEgreEgetsu((int) fila.getCell(2).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case NUMERIC:
                         egetsuResultadosGeneraciones.setTestSobre((int) fila.getCell(3).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case NUMERIC:
                        egetsuResultadosGeneraciones.setTestSatis((int) fila.getCell(4).getNumericCellValue());
                        break;
                    default:
                        break;
                }

                switch (fila.getCell(5).getCellTypeEnum()) {
                    case NUMERIC:
                         egetsuResultadosGeneraciones.setSinTest((int) fila.getCell(5).getNumericCellValue());
                        break;
                    default:
                        break;
                }
               
               dTOEgetsu.setEgetsuResultadosGeneraciones(egetsuResultadosGeneraciones);
                    
               listaDtoEgetsu.add(dTOEgetsu);
            }
            }
            listaEgetsuResultados.setEgetsus(listaDtoEgetsu);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaEgetsuResultados;
        
    }
    
   
   @Override
    public void guardaEgetsuResultados(ListaEgetsuResultados listaEgetsuResultados, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {

//            listaEgetsuResultados.getEgetsus().forEach((egetsus) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//
//            facdepye.setEntityClass(EgetsuResultadosGeneraciones.class);
//            egetsus.getEgetsuResultadosGeneraciones().setRegistro(registro.getRegistro());
//            facdepye.create(egetsus.getEgetsuResultadosGeneraciones());
//            facdepye.flush();
//        });
        List<String> validaciones = new SerializableArrayList<>();
        List<String> listaCondicional = new ArrayList<>();
        listaEgetsuResultados.getEgetsus().forEach((egetsus) -> {
            
                facdepye.setEntityClass(EgetsuResultadosGeneraciones.class);
                EgetsuResultadosGeneraciones erg = getRegistroEgetsuResultadosGeneraciones(egetsus.getEgetsuResultadosGeneraciones().getGeneracion());
                Boolean registroAlmacenado = false;
                if (erg != null) {
                    listaCondicional.add(egetsus.getGeneracion());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    egetsus.getEgetsuResultadosGeneraciones().setRegistro(erg.getRegistro());
                    facdepye.edit(egetsus.getEgetsuResultadosGeneraciones());
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    egetsus.getEgetsuResultadosGeneraciones().setRegistro(registro.getRegistro());
                    facdepye.create(egetsus.getEgetsuResultadosGeneraciones());
                }
                facdepye.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());

    }

    @Override
    public EgetsuResultadosGeneraciones getRegistroEgetsuResultadosGeneraciones(Short generacion) {
        EgetsuResultadosGeneraciones egetsuResultadosGeneraciones = new EgetsuResultadosGeneraciones();
        TypedQuery<EgetsuResultadosGeneraciones> query = em.createQuery("SELECT e FROM EgetsuResultadosGeneraciones e WHERE e.generacion = :generacion",EgetsuResultadosGeneraciones.class);
        query.setParameter("generacion", generacion);
        try {
            egetsuResultadosGeneraciones = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            egetsuResultadosGeneraciones = null;
            ex.toString();
        }
        return egetsuResultadosGeneraciones;
    }
}
