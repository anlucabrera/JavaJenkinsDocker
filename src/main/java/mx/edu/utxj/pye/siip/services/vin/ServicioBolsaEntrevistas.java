/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.BolsaTrabajo;
import mx.edu.utxj.pye.sgi.entity.pye2.BolsaTrabajoEntrevistas;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOBolsaEntrevistas;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaBolsaEntrevistas;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbBolsaTrabajo;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbBolsaEntrevistas;
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
public class ServicioBolsaEntrevistas implements EjbBolsaEntrevistas{
     
    @EJB
    Facade facdepye;
    @EJB
    EjbModulos ejbModulos;
    @EJB
    EjbBolsaTrabajo ejbBolsaTrabajo;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    @Override
    public ListaBolsaEntrevistas getListaBolsaEntrevistas(String rutaArchivo) throws Throwable {
       
       ListaBolsaEntrevistas listaBolsaEntrevistas = new ListaBolsaEntrevistas();

        List<DTOBolsaEntrevistas> listaDtoBolsaEntrevistas = new ArrayList<>();
        AreasUniversidad areasUniversidad;
        BolsaTrabajo bolsaTrabajo;
        BolsaTrabajoEntrevistas bolsaTrabajoEntrevistas;
        DTOBolsaEntrevistas dTOBolsaEntrevistas;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(1);
        XSSFRow fila;
        
        String mat="";
         
        if (primeraHoja.getSheetName().equals("Entrevistas Bolsa de Trabajo")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((!"".equals(fila.getCell(10).getStringCellValue()))) {
                areasUniversidad = new AreasUniversidad();
                bolsaTrabajo = new BolsaTrabajo();
                bolsaTrabajoEntrevistas = new BolsaTrabajoEntrevistas();
                dTOBolsaEntrevistas = new DTOBolsaEntrevistas();
               
                switch (fila.getCell(1).getCellTypeEnum()) {
                    case FORMULA:
                        bolsaTrabajo.setBolsatrab((int) fila.getCell(1).getNumericCellValue());
                        bolsaTrabajoEntrevistas.setBolsatrabent(bolsaTrabajo);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case STRING:
                         dTOBolsaEntrevistas.setGeneracion(fila.getCell(2).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case FORMULA: 
                        bolsaTrabajoEntrevistas.setGeneracion((short) fila.getCell(3).getNumericCellValue());
                        break;
                    default:
                        break;
                }
              
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case FORMULA: 
                        int m = (int)fila.getCell(5).getNumericCellValue();
                        mat= Integer.toString(m);
                        bolsaTrabajoEntrevistas.setMatricula(mat);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case STRING: 
                        areasUniversidad.setNombre(fila.getCell(6).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                    case FORMULA: 
                        areasUniversidad.setArea((short) fila.getCell(7).getNumericCellValue());
                        bolsaTrabajoEntrevistas.setProgramaEducativo(areasUniversidad.getArea());
                        break;
                    default:
                        break;
                }
                
                switch (fila.getCell(9).getCellTypeEnum()) {
                    case FORMULA: 
                        bolsaTrabajoEntrevistas.setContratado(fila.getCell(9).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(10).getCellTypeEnum()) {
                    case STRING: 
                        bolsaTrabajoEntrevistas.setObservaciones(fila.getCell(10).getStringCellValue());
                        break;
                    default:
                        break;

                }
                    dTOBolsaEntrevistas.setAreasUniversidad(areasUniversidad);
                    dTOBolsaEntrevistas.setBolsaTrabajoEntrevistas(bolsaTrabajoEntrevistas);
                    listaDtoBolsaEntrevistas.add(dTOBolsaEntrevistas);
                    
                }
            }
            listaBolsaEntrevistas.setBolsaEntrevistas(listaDtoBolsaEntrevistas);
            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaBolsaEntrevistas;
        
    }

    @Override
    public void guardaBolsaEntrevistas(ListaBolsaEntrevistas listaBolsaEntrevistas, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//            listaBolsaEntrevistas.getBolsaEntrevistas().forEach((bolsaEntrevistas) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//            
//            bolsaEntrevistas.getBolsaTrabajoEntrevistas().getBolsatrabent().setRegistro(ejbBolsaTrabajo.getRegistroBolsaTrabajoEspecifico(bolsaEntrevistas.getBolsaTrabajoEntrevistas().getBolsatrabent().getBolsatrab()));
//                        
//            facdepye.setEntityClass(BolsaTrabajoEntrevistas.class);
//            bolsaEntrevistas.getBolsaTrabajoEntrevistas().setRegistro(registro.getRegistro());
//            facdepye.create(bolsaEntrevistas.getBolsaTrabajoEntrevistas());
//            facdepye.flush();
//        });
            List<String> listaCondicional = new ArrayList<>();
            listaBolsaEntrevistas.getBolsaEntrevistas().forEach((bolsaEntrevistas) -> {
            try {
                facdepye.setEntityClass(BolsaTrabajoEntrevistas.class);
                BolsaTrabajoEntrevistas bolTrabEntEncontrado = getRegistroBolsaTrabajoEntrevistas(bolsaEntrevistas.getBolsaTrabajoEntrevistas());
                Boolean registroAlmacenado = false;
                if (bolTrabEntEncontrado != null) {
                    listaCondicional.add(bolsaEntrevistas.getBolsaTrabajoEntrevistas().getBolsatrabent().getBolsatrab()+ " - " + bolsaEntrevistas.getBolsaTrabajoEntrevistas().getMatricula());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    bolsaEntrevistas.getBolsaTrabajoEntrevistas().setRegistro(bolTrabEntEncontrado.getRegistro());
                    bolsaEntrevistas.getBolsaTrabajoEntrevistas().getBolsatrabent().setRegistro(bolTrabEntEncontrado.getBolsatrabent().getRegistro());
                    facdepye.edit(bolsaEntrevistas.getBolsaTrabajoEntrevistas());
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    bolsaEntrevistas.getBolsaTrabajoEntrevistas().getBolsatrabent().setRegistro(ejbBolsaTrabajo.getRegistroBolsaTrabajoEspecifico(bolsaEntrevistas.getBolsaTrabajoEntrevistas().getBolsatrabent().getBolsatrab()));
                    bolsaEntrevistas.getBolsaTrabajoEntrevistas().setRegistro(registro.getRegistro());
                    facdepye.create(bolsaEntrevistas.getBolsaTrabajoEntrevistas());
                }
                facdepye.flush();
            } catch (Throwable ex) {
                Logger.getLogger(ServicioBolsaTrabajo.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public BolsaTrabajoEntrevistas getRegistroBolsaTrabajoEntrevistas(BolsaTrabajoEntrevistas bolsaTrabajoEntrevistas) {
        TypedQuery<BolsaTrabajoEntrevistas> query = em.createQuery("SELECT e FROM BolsaTrabajoEntrevistas e JOIN e.bolsatrabent b WHERE b.bolsatrab = :bolsatrabent AND e.generacion = :generacion AND e.matricula = :matricula", BolsaTrabajoEntrevistas.class);
        query.setParameter("bolsatrabent", bolsaTrabajoEntrevistas.getBolsatrabent().getBolsatrab());
        query.setParameter("generacion", bolsaTrabajoEntrevistas.getGeneracion());
        query.setParameter("matricula", bolsaTrabajoEntrevistas.getMatricula());
        try {
            bolsaTrabajoEntrevistas = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            bolsaTrabajoEntrevistas = null;
            ex.toString();
        }
        return bolsaTrabajoEntrevistas; 
    }
}
