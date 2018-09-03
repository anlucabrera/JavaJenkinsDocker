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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.ComisionesAcademicas;
import mx.edu.utxj.pye.sgi.entity.pye2.ComisionesAcademicasParticipantes;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.caphum.DTOComAcadParticipantes;
import mx.edu.utxj.pye.siip.entity.caphum.list.ListaComAcadParticipantes;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbComisionesAcademicas;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbComAcadParticipantes;
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
public class ServicioComAcadParticipantes implements EjbComAcadParticipantes{
    
    @EJB
    Facade facadepye;
    @EJB
    EjbModulos ejbModulos;
    @EJB
    EjbComisionesAcademicas ejbComisionesAcademicas;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
   
    @Override
    public ListaComAcadParticipantes getListaComAcadParticipantes(String rutaArchivo) throws Throwable {
       
        ListaComAcadParticipantes listaComAcadParticipantes = new ListaComAcadParticipantes();

        List<DTOComAcadParticipantes> listaDtoComAcadPart = new ArrayList<>();
        Personal personal;
        ComisionesAcademicas comisionesAcademicas;
        ComisionesAcademicasParticipantes comisionesAcademicasParticipantes;
        DTOComAcadParticipantes dTOComAcadParticipantes;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(1);
        XSSFRow fila;
        
        if (primeraHoja.getSheetName().equals("Participantes")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((!"".equals(fila.getCell(1).getStringCellValue()))) {
                personal = new Personal();
                comisionesAcademicas = new ComisionesAcademicas();
                comisionesAcademicasParticipantes = new ComisionesAcademicasParticipantes();
                dTOComAcadParticipantes = new DTOComAcadParticipantes();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        comisionesAcademicas.setComisionAcademica(fila.getCell(0).getStringCellValue());
                        comisionesAcademicasParticipantes.setComisionAcademica(comisionesAcademicas);
                        break;
                    default:
                        break;
                }
                 switch (fila.getCell(1).getCellTypeEnum()) {
                    case FORMULA:
                        dTOComAcadParticipantes.setTipoComision(fila.getCell(1).getStringCellValue());
                        break;
                    default:
                        break;
                }
                  switch (fila.getCell(2).getCellTypeEnum()) {
                    case FORMULA:
                        dTOComAcadParticipantes.setLugarComision(fila.getCell(2).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case STRING:
                        personal.setNombre(fila.getCell(4).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case FORMULA: 
                        personal.setClave((int) fila.getCell(5).getNumericCellValue());
                        comisionesAcademicasParticipantes.setParticipante(personal.getClave());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case FORMULA:
                        dTOComAcadParticipantes.setAreaPart(fila.getCell(6).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                    case FORMULA: 
                        comisionesAcademicasParticipantes.setArea((short)fila.getCell(7).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                    dTOComAcadParticipantes.setPersonal(personal);
                    dTOComAcadParticipantes.setComisionesAcademicasParticipantes(comisionesAcademicasParticipantes);
                    listaDtoComAcadPart.add(dTOComAcadParticipantes);
                    
                    System.out.println("mx.edu.utxj.pye.siip.services.ch.ServicioComAcadParticipantes.getListaComAcadParticipantes()" + listaDtoComAcadPart);
                    
                }
            }
            listaComAcadParticipantes.setParticipantes(listaDtoComAcadPart);
            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaComAcadParticipantes;
    }

    @Override
    public void guardaComAcadParticipantes(ListaComAcadParticipantes listaComAcadParticipantes, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {

//            listaComAcadParticipantes.getParticipantes().forEach((participantes) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//            
//            participantes.getComisionesAcademicasParticipantes().getComisionAcademica().setRegistro(ejbComisionesAcademicas.getRegistroComisionesAcademicasEspecifico(participantes.getComisionesAcademicasParticipantes().getComisionAcademica().getComisionAcademica()));
//                        
//            facadepye.setEntityClass(ComisionesAcademicasParticipantes.class);
//            participantes.getComisionesAcademicasParticipantes().setRegistro(registro.getRegistro());
//            facadepye.create(participantes.getComisionesAcademicasParticipantes());
//            facadepye.flush();
//        });

            List<String> listaCondicional = new ArrayList<>();
            listaComAcadParticipantes.getParticipantes().forEach((participantes) -> {
            try {
                facadepye.setEntityClass(ComisionesAcademicasParticipantes.class);
                ComisionesAcademicasParticipantes comAcadPartEncontrado = getRegistroComisionesAcademicasParticipantes(participantes.getComisionesAcademicasParticipantes());
                Boolean registroAlmacenado = false;
                if (comAcadPartEncontrado != null) {
                    listaCondicional.add(participantes.getComisionesAcademicasParticipantes().getComisionAcademica().getComisionAcademica()+ " " + participantes.getPersonal().getNombre());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    participantes.getComisionesAcademicasParticipantes().setRegistro(comAcadPartEncontrado.getRegistro());
                    participantes.getComisionesAcademicasParticipantes().getComisionAcademica().setRegistro(comAcadPartEncontrado.getComisionAcademica().getRegistro());
                    facadepye.edit(participantes.getComisionesAcademicasParticipantes());
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    participantes.getComisionesAcademicasParticipantes().getComisionAcademica().setRegistro(ejbComisionesAcademicas.getRegistroComisionesAcademicasEspecifico(participantes.getComisionesAcademicasParticipantes().getComisionAcademica().getComisionAcademica()));
                    participantes.getComisionesAcademicasParticipantes().setRegistro(registro.getRegistro());
                    facadepye.create(participantes.getComisionesAcademicasParticipantes());
                }
                facadepye.flush();
            } catch (Throwable ex) {
                Logger.getLogger(ServicioComisionesAcademicas.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public ComisionesAcademicasParticipantes getRegistroComisionesAcademicasParticipantes(ComisionesAcademicasParticipantes comisionesAcademicasParticipantes) {
        TypedQuery<ComisionesAcademicasParticipantes> query = em.createQuery("SELECT c FROM ComisionesAcademicasParticipantes c JOIN c.comisionAcademica ca WHERE ca.comisionAcademica = :comisionAcademica AND c.participante = :participante", ComisionesAcademicasParticipantes.class);
        query.setParameter("comisionAcademica", comisionesAcademicasParticipantes.getComisionAcademica().getComisionAcademica());
        query.setParameter("participante", comisionesAcademicasParticipantes.getParticipante());
        try {
            comisionesAcademicasParticipantes = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            comisionesAcademicasParticipantes = null;
            ex.toString();
        }
        return comisionesAcademicasParticipantes; 
    }
    
}
