/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ch;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
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
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.ComisionesAcademicas;
import mx.edu.utxj.pye.sgi.entity.pye2.ComisionesAcademicasTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.caphum.DTOComisionesAcademicas;
import mx.edu.utxj.pye.siip.entity.caphum.list.ListaComisionesAcademicas;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbComisionesAcademicas;
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
public class ServicioComisionesAcademicas implements EjbComisionesAcademicas{
    
    @EJB
    Facade facadepye;
    @EJB
    EjbModulos ejbModulos;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    @Override
    public ListaComisionesAcademicas getListaComisionesAcademicas(String rutaArchivo) throws Throwable {
       
        ListaComisionesAcademicas listaComisionesAcademicas = new  ListaComisionesAcademicas();

        List<DTOComisionesAcademicas> listaDtoComisionesAcademicas = new ArrayList<>();
        
        ComisionesAcademicas comisionesAcademicas;
        ComisionesAcademicasTipos comisionesAcademicasTipos;
        DTOComisionesAcademicas dTOComisionesAcademicas;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        if (primeraHoja.getSheetName().equals("Comisiones Académicas")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((fila.getCell(1).getNumericCellValue()!= 0)) {
                comisionesAcademicas = new ComisionesAcademicas();
                comisionesAcademicasTipos = new ComisionesAcademicasTipos();
                dTOComisionesAcademicas = new DTOComisionesAcademicas();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case FORMULA:
                        comisionesAcademicas.setComisionAcademica(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case STRING: 
                        comisionesAcademicasTipos.setDescripcion(fila.getCell(2).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case FORMULA: 
                        comisionesAcademicasTipos.setTipoComision((short)fila.getCell(3).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(4))) {
                         comisionesAcademicas.setFechaInicio(fila.getCell(4).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(5))) {
                         comisionesAcademicas.setFechaFin(fila.getCell(5).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case STRING: 
                        comisionesAcademicas.setLugar(fila.getCell(6).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                    case STRING:
                        comisionesAcademicas.setObjetivo(fila.getCell(7).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(8).getCellTypeEnum()) {
                    case STRING:
                        comisionesAcademicas.setAcuerdos(fila.getCell(8).getStringCellValue());
                        break;
                    default:
                        break;
                }
                    comisionesAcademicas.setTipoComision(comisionesAcademicasTipos);
                    dTOComisionesAcademicas.setComisionesAcademicas(comisionesAcademicas);
                    listaDtoComisionesAcademicas.add(dTOComisionesAcademicas);
                }
            }
            listaComisionesAcademicas.setComisiones(listaDtoComisionesAcademicas);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaComisionesAcademicas;
        
    }

    @Override
    public void guardaComisionesAcademicas(ListaComisionesAcademicas listaComisionesAcademicas, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//        
//            listaComisionesAcademicas.getComisiones().forEach((comisiones) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//            
//            facadepye.setEntityClass(ComisionesAcademicas.class);
//            comisiones.getComisionesAcademicas().setRegistro(registro.getRegistro());
//            facadepye.create(comisiones.getComisionesAcademicas());
//            facadepye.flush();
//        });

            List<String> listaCondicional = new ArrayList<>();
            listaComisionesAcademicas.getComisiones().forEach((comisiones) -> {
            facadepye.setEntityClass(ComisionesAcademicas.class);
            ComisionesAcademicas comAcadEncontrado = getRegistroComisionesAcademicas(comisiones.getComisionesAcademicas());
            Boolean registroAlmacenado = false;
            if (comAcadEncontrado != null) {
                listaCondicional.add(comisiones.getComisionesAcademicas().getComisionAcademica());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                comisiones.getComisionesAcademicas().setRegistro(comAcadEncontrado.getRegistro());
                facadepye.edit(comisiones.getComisionesAcademicas());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                comisiones.getComisionesAcademicas().setRegistro(registro.getRegistro());
                facadepye.create(comisiones.getComisionesAcademicas());
            }
            facadepye.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
 }
    
    @Override
    public Integer getRegistroComisionesAcademicasEspecifico(String comisionAcademica) {
        TypedQuery<ComisionesAcademicas> query = em.createNamedQuery("ComisionesAcademicas.findByComisionAcademica", ComisionesAcademicas.class);
        query.setParameter("comisionAcademica", comisionAcademica);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }

    @Override
    public ComisionesAcademicas getRegistroComisionesAcademicas(ComisionesAcademicas comisionesAcademicas) {
        TypedQuery<ComisionesAcademicas> query = em.createNamedQuery("ComisionesAcademicas.findByComisionAcademica", ComisionesAcademicas.class);
        query.setParameter("comisionAcademica", comisionesAcademicas.getComisionAcademica());
        try {
             comisionesAcademicas = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            comisionesAcademicas = null;
            ex.toString();
        }
        return comisionesAcademicas;
    }

    @Override
    public List<ComisionesAcademicasTipos> getComisionesAcademicasTiposAct() {
        List<ComisionesAcademicasTipos> genLst = new ArrayList<>();
        TypedQuery<ComisionesAcademicasTipos> query = em.createQuery("SELECT c FROM ComisionesAcademicasTipos c", ComisionesAcademicasTipos.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }
}
