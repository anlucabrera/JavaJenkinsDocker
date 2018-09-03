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
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasEstimulos;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasEstimulosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.caphum.DTOProgramasEstimulos;
import mx.edu.utxj.pye.siip.entity.caphum.list.ListaProgramasEstimulos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbProgramasEstimulos;
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
public class ServicioProgramasEstimulos implements EjbProgramasEstimulos{
    
    @EJB
    Facade facadepye;
    @EJB
    EjbModulos ejbModulos;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
   
    @Override
    public ListaProgramasEstimulos getListaProgramaEstimulos(String rutaArchivo) throws Throwable {
       
        ListaProgramasEstimulos listaProgramasEstimulos = new ListaProgramasEstimulos();

        List<DTOProgramasEstimulos> listaDtoProgEst = new ArrayList<>();
        Personal personal;
        ProgramasEstimulos programasEstimulos;
        ProgramasEstimulosTipos programasEstimulosTipos;
        DTOProgramasEstimulos dTOProgramasEstimulos;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        if (primeraHoja.getSheetName().equals("Programas de Estímulos")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((fila.getCell(1).getNumericCellValue()!= 0)) {
                personal = new Personal();
                programasEstimulos = new ProgramasEstimulos();
                programasEstimulosTipos = new ProgramasEstimulosTipos();
                dTOProgramasEstimulos = new DTOProgramasEstimulos();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        personal.setNombre(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(1).getCellTypeEnum()) {
                    case FORMULA:
                        personal.setClave((int) fila.getCell(1).getNumericCellValue());
                        programasEstimulos.setTrabajador(personal.getClave());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case FORMULA:
                        dTOProgramasEstimulos.setPuesto(fila.getCell(2).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case FORMULA:
                        dTOProgramasEstimulos.setArea(fila.getCell(3).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case STRING:
                        programasEstimulosTipos.setDescripcion(fila.getCell(4).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case FORMULA: 
                        programasEstimulosTipos.setTipoPrograma((short)fila.getCell(5).getNumericCellValue());
                        programasEstimulos.setTipoPrograma(programasEstimulosTipos);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case STRING: 
                        programasEstimulos.setDescripcion(fila.getCell(6).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(8).getCellTypeEnum()) {
                    case FORMULA: 
                        programasEstimulos.setMonto((double)fila.getCell(8).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(9).getCellTypeEnum()) {
                    case NUMERIC: 
                        if (DateUtil.isCellDateFormatted(fila.getCell(9))) {
                         programasEstimulos.setFechaInicio(fila.getCell(9).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(10).getCellTypeEnum()) {
                    case NUMERIC: 
                        if (DateUtil.isCellDateFormatted(fila.getCell(10))) {
                         programasEstimulos.setFechaTermino(fila.getCell(10).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                    dTOProgramasEstimulos.setPersonal(personal);
                    dTOProgramasEstimulos.setProgramasEstimulos(programasEstimulos);
                    listaDtoProgEst.add(dTOProgramasEstimulos);
                    
                }
            }
            listaProgramasEstimulos.setEstimulos(listaDtoProgEst);
            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaProgramasEstimulos;
    }

    @Override
    public void guardaProgramasEstimulos(ListaProgramasEstimulos listaProgramasEstimulos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
 
//            listaProgramasEstimulos.getEstimulos().forEach((estimulos) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//            
//            facadepye.setEntityClass(ProgramasEstimulos.class);
//            estimulos.getProgramasEstimulos().setRegistro(registro.getRegistro());
//            facadepye.create(estimulos.getProgramasEstimulos());
//            facadepye.flush();
//        });
            List<String> listaCondicional = new ArrayList<>();
            listaProgramasEstimulos.getEstimulos().forEach((estimulos) -> {
            facadepye.setEntityClass(ProgramasEstimulos.class);
            ProgramasEstimulos progEstEncontrado = getRegistroProgramasEstimulos(estimulos.getProgramasEstimulos());
            Boolean registroAlmacenado = false;
            if (progEstEncontrado != null) {
                listaCondicional.add(estimulos.getPersonal().getNombre()+ " - " + estimulos.getProgramasEstimulos().getTipoPrograma().getDescripcion());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                estimulos.getProgramasEstimulos().setRegistro(progEstEncontrado.getRegistro());
                facadepye.edit(estimulos.getProgramasEstimulos());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                estimulos.getProgramasEstimulos().setRegistro(registro.getRegistro());
                facadepye.create(estimulos.getProgramasEstimulos());
            }
            facadepye.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public ProgramasEstimulos getRegistroProgramasEstimulos(ProgramasEstimulos programasEstimulos) {
        TypedQuery<ProgramasEstimulos> query = em.createQuery("SELECT p FROM ProgramasEstimulos p JOIN p.tipoPrograma t WHERE p.trabajador = :trabajador AND t.tipoPrograma = :tipoPrograma AND p.fechaInicio = :fechaInicio",  ProgramasEstimulos.class);
        query.setParameter("trabajador",programasEstimulos.getTrabajador());
        query.setParameter("tipoPrograma", programasEstimulos.getTipoPrograma().getTipoPrograma());
        query.setParameter("fechaInicio", programasEstimulos.getFechaInicio());
        try {
            programasEstimulos = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            programasEstimulos = null;
        }
        return programasEstimulos;
    }

    @Override
    public List<ProgramasEstimulosTipos> getProgramasEstimulosTiposAct() {
        List<ProgramasEstimulosTipos> genLst = new ArrayList<>();
        TypedQuery<ProgramasEstimulosTipos> query = em.createQuery("SELECT p FROM ProgramasEstimulosTipos p", ProgramasEstimulosTipos.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }
   
}
