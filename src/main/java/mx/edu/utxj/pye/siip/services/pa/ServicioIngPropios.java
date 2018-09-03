/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.pa;

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
import mx.edu.utxj.pye.sgi.entity.pye2.IngresosPropiosCaptados;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.finanzas.DTOIngPropios;
import mx.edu.utxj.pye.siip.entity.finanzas.list.ListaIngPropios;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.pa.EjbIngPropios;
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
public class ServicioIngPropios implements EjbIngPropios{
    
    @EJB
    Facade facdepye;
    @EJB
    EjbModulos ejbModulos;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    @Override
    public  ListaIngPropios getListaIngPropios(String rutaArchivo) throws Throwable {
       
        ListaIngPropios listaIngPropios = new ListaIngPropios();

        List<DTOIngPropios> listaDtoIngPropios = new ArrayList<>();
        IngresosPropiosCaptados ingresosPropiosCaptados;
        DTOIngPropios dTOIngPropios;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        if (primeraHoja.getSheetName().equals("Ingresos Propios Captados")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

            if ((!"".equals(fila.getCell(4).getNumericCellValue()))) {
                ingresosPropiosCaptados = new IngresosPropiosCaptados();
                dTOIngPropios = new  DTOIngPropios();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        dTOIngPropios.setCicloEscolar(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                   case STRING:
                       dTOIngPropios.setPeriodoEscolar(fila.getCell(3).getStringCellValue());
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case FORMULA:
                        ingresosPropiosCaptados.setPeriodoEscolar((int)fila.getCell(4).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case FORMULA: 
                        ingresosPropiosCaptados.setConceptoIngresosCaptados(fila.getCell(6).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(7))) {
                        ingresosPropiosCaptados.setFechaIngreso(fila.getCell(7).getDateCellValue());
                        }
                    default:
                        break;
                }
                switch (fila.getCell(9).getCellTypeEnum()) {
                    case FORMULA:
                        ingresosPropiosCaptados.setMonto((double)fila.getCell(9).getNumericCellValue());
                        break;
                    default:
                        break;
                }
               
                switch (fila.getCell(11).getCellTypeEnum()) {
                    case FORMULA:
                        ingresosPropiosCaptados.setDescripcion(fila.getCell(11).getStringCellValue());
                        break;
                    default:
                        break;
                }
                    
                    dTOIngPropios.setIngresosPropiosCaptados(ingresosPropiosCaptados);
                    
                    listaDtoIngPropios.add(dTOIngPropios);
                }
            }
            listaIngPropios.setIngPropios(listaDtoIngPropios);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaIngPropios;

    }

    @Override
    public void guardaIngPropios(ListaIngPropios listaIngPropios, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//            listaIngPropios.getIngPropios().forEach((ingPropios) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//            
//            facdepye.setEntityClass(IngresosPropiosCaptados.class);
//            ingPropios.getIngresosPropiosCaptados().setRegistro(registro.getRegistro());
//            facdepye.create(ingPropios.getIngresosPropiosCaptados());
//            facdepye.flush();
//        });
        List<String> validaciones = new SerializableArrayList<>();
        List<String> listaCondicional = new ArrayList<>();
        listaIngPropios.getIngPropios().forEach((ingPropios) -> {
            
                facdepye.setEntityClass(IngresosPropiosCaptados.class);
                IngresosPropiosCaptados ingProEncontrado = getRegistroIngresosPropiosCaptados(ingPropios.getIngresosPropiosCaptados());
                Boolean registroAlmacenado = false;
                if (ingProEncontrado != null) {
                    listaCondicional.add(ingPropios.getPeriodoEscolar()+" - "+ingPropios.getIngresosPropiosCaptados().getConceptoIngresosCaptados());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    ingPropios.getIngresosPropiosCaptados().setRegistro(ingProEncontrado.getRegistro());
                    facdepye.edit(ingPropios.getIngresosPropiosCaptados());
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    ingPropios.getIngresosPropiosCaptados().setRegistro(registro.getRegistro());
                    facdepye.create(ingPropios.getIngresosPropiosCaptados());
                }
                facdepye.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public IngresosPropiosCaptados getRegistroIngresosPropiosCaptados(IngresosPropiosCaptados ingresosPropiosCaptados) {
        TypedQuery<IngresosPropiosCaptados> query = em.createQuery("SELECT i FROM IngresosPropiosCaptados i WHERE i.periodoEscolar = :periodoEscolar AND i.conceptoIngresosCaptados = :conceptoIngresosCaptados AND i.fechaIngreso = :fechaIngreso", IngresosPropiosCaptados.class);
        query.setParameter("periodoEscolar", ingresosPropiosCaptados.getPeriodoEscolar());
        query.setParameter("conceptoIngresosCaptados", ingresosPropiosCaptados.getConceptoIngresosCaptados());
        query.setParameter("fechaIngreso", ingresosPropiosCaptados.getFechaIngreso());
        try {
            ingresosPropiosCaptados = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            ingresosPropiosCaptados = null;
            ex.toString();
        }
        return ingresosPropiosCaptados;
    }
    
}
