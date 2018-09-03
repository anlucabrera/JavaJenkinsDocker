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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.OrganismosEvaluadores;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasPertcal;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.pye.DTOProgramasPertCalidad;
import mx.edu.utxj.pye.siip.entity.pye.list.ListaProgramasPertCalidad;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbProgramasPertCalidad;
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
public class ServicioProgramasPertCalidad implements EjbProgramasPertCalidad{
     
    @EJB
    Facade facadepye;
    @EJB
    EjbModulos ejbModulos;
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    @Override
    public  ListaProgramasPertCalidad getListaProgramasPertCalidad(String rutaArchivo) throws Throwable {
       
        ListaProgramasPertCalidad listaProgramasPertCalidad = new  ListaProgramasPertCalidad();

        List<DTOProgramasPertCalidad> listaDtoProgramasPertCalidad = new ArrayList<>();
        AreasUniversidad areaUniversidad;
        OrganismosEvaluadores organismosEvaluadores;
        ProgramasPertcal programasPertcal;
        DTOProgramasPertCalidad  dTOProgramasPertCalidad;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        if (primeraHoja.getSheetName().equals("PE's Pertinentes y de Calidad")) {
        for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

             if ((!"".equals(fila.getCell(0).getStringCellValue()))) {
                areaUniversidad = new AreasUniversidad();
                organismosEvaluadores = new OrganismosEvaluadores();
                programasPertcal = new ProgramasPertcal();
                dTOProgramasPertCalidad = new DTOProgramasPertCalidad();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        areaUniversidad.setNombre(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case FORMULA:
                        areaUniversidad.setArea((short) fila.getCell(2).getNumericCellValue());
                        programasPertcal.setProgramaEducativo(areaUniversidad.getArea());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                   case NUMERIC:
                        programasPertcal.setAnioInicio((short) fila.getCell(3).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case STRING:
                        programasPertcal.setEvaluable(fila.getCell(4).getStringCellValue());
                        break;
                    default:
                        break;

                }
                 switch (fila.getCell(5).getCellTypeEnum()) {
                    case STRING:
                        programasPertcal.setPertinente(fila.getCell(5).getStringCellValue());
                        break;
                    default:
                        break;

                }
               
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case STRING:
                        organismosEvaluadores.setSiglas(fila.getCell(6).getStringCellValue());
                        programasPertcal.setOrgAcreditador(organismosEvaluadores.getSiglas());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(7))) {
                         programasPertcal.setFeciniAcred(fila.getCell(7).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(8).getCellTypeEnum()) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(8))) {
                         programasPertcal.setFecfinAcred(fila.getCell(8).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(9).getCellTypeEnum()) {
                    case NUMERIC:
                        programasPertcal.setAnioEstfac((short) fila.getCell(9).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(10).getCellTypeEnum()) {
                    case NUMERIC:
                        programasPertcal.setAnioUltast((short) fila.getCell(10).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                 
               dTOProgramasPertCalidad.setAreasUniversidad(areaUniversidad);
               dTOProgramasPertCalidad.setProgramasPertcal(programasPertcal);
                    
               listaDtoProgramasPertCalidad.add(dTOProgramasPertCalidad);
                }
            }
            listaProgramasPertCalidad.setProgramasPertCalidad(listaDtoProgramasPertCalidad);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaProgramasPertCalidad;
        
    }
    
   @Override
    public void guardaProgramasPertCalidad(ListaProgramasPertCalidad listaProgramasPertCalidad, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//
//            listaProgramasPertCalidad.getProgramasPertCalidad().forEach((programasPertCalidad) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//           
//            facadepye.setEntityClass(ProgramasPertcal.class);
//            programasPertCalidad.getProgramasPertcal().setRegistro(registro.getRegistro());
//            facadepye.create(programasPertCalidad.getProgramasPertcal());
//            facadepye.flush();
//        });
        List<String> validaciones = new SerializableArrayList<>();
        List<Short> listaCondicional = new ArrayList<>();
        listaProgramasPertCalidad.getProgramasPertCalidad().forEach((programasPertCalidad) -> {
            
                facadepye.setEntityClass(ProgramasPertcal.class);
                ProgramasPertcal pp = getRegistroProgramasPertcal(programasPertCalidad.getProgramasPertcal().getProgramaEducativo(), programasPertCalidad.getProgramasPertcal().getAnioInicio());
                Boolean registroAlmacenado = false;
                if (pp != null) {
                    listaCondicional.add(programasPertCalidad.getProgramasPertcal().getProgramaEducativo());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    programasPertCalidad.getProgramasPertcal().setRegistro(pp.getRegistro());
                    facadepye.edit(programasPertCalidad.getProgramasPertcal());
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    programasPertCalidad.getProgramasPertcal().setRegistro(registro.getRegistro());
                    facadepye.create(programasPertCalidad.getProgramasPertcal());
                }
                facadepye.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public ProgramasPertcal getRegistroProgramasPertcal(Short programaEducativo, Short anioInicio) {
        ProgramasPertcal programasPertcal = new ProgramasPertcal();
        TypedQuery<ProgramasPertcal> query = em.createQuery("SELECT p FROM ProgramasPertcal p WHERE p.programaEducativo = :programaEducativo AND p.anioInicio = :anioInicio",ProgramasPertcal.class);
        query.setParameter("programaEducativo", programaEducativo);
        query.setParameter("anioInicio", anioInicio);
        try {
            programasPertcal = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            programasPertcal = null;
            ex.toString();
        }
        return programasPertcal;
    }
}
