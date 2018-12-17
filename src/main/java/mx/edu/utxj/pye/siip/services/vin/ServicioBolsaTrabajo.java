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
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.BolsaTrabajo;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOBolsa;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaBolsaTrabajo;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbBolsaTrabajo;
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
public class ServicioBolsaTrabajo implements EjbBolsaTrabajo{
    
    @EJB
    Facade facadepye;
    @EJB
    EjbModulos ejbModulos;
    @EJB
    EjbOrganismosVinculados ejbOrganismosVinculados;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    @Override
    public  ListaBolsaTrabajo getListaBolsaTrabajo(String rutaArchivo) throws Throwable {
       
        ListaBolsaTrabajo listaBolsaTrabajo = new  ListaBolsaTrabajo();

        List<DTOBolsa> listaDtoBolsa = new ArrayList<>();
        OrganismosVinculados organismosVinculados;
        BolsaTrabajo bolsaTrabajo;
        DTOBolsa dTOBolsa;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        if (primeraHoja.getSheetName().equals("Bolsa de Trabajo")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((!"".equals(fila.getCell(1).getNumericCellValue()))) {
                organismosVinculados = new OrganismosVinculados();
                bolsaTrabajo = new BolsaTrabajo();
                dTOBolsa = new DTOBolsa();
                
                switch (fila.getCell(1).getCellTypeEnum()) {
                    case FORMULA:
//                        bolsaTrabajo.setBolsatrab((int)fila.getCell(1).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case NUMERIC: 
                        if (DateUtil.isCellDateFormatted(fila.getCell(2))) {
                         bolsaTrabajo.setFecha(fila.getCell(2).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case STRING:
                        organismosVinculados.setNombre(fila.getCell(4).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case FORMULA: 
                        organismosVinculados.setEmpresa((int) fila.getCell(5).getNumericCellValue());
                        bolsaTrabajo.setEmpresa(organismosVinculados);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case STRING:
                        bolsaTrabajo.setPuestoOfertado(fila.getCell(6).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                    case NUMERIC:
                        bolsaTrabajo.setPlazas((short)fila.getCell(7).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                    dTOBolsa.setBolsaTrabajo(bolsaTrabajo);
                    listaDtoBolsa.add(dTOBolsa);
                }
            }
            listaBolsaTrabajo.setBolsa(listaDtoBolsa);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaBolsaTrabajo;
        
    }

    @Override
    public void guardaBolsaTrabajo(ListaBolsaTrabajo listaBolsaTrabajo, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//            listaBolsaTrabajo.getBolsa().forEach((bolsa) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//            
//            bolsa.getBolsaTrabajo().getEmpresa().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(bolsa.getBolsaTrabajo().getEmpresa().getEmpresa()));
//            
//            facadepye.setEntityClass(BolsaTrabajo.class);
//            bolsa.getBolsaTrabajo().setRegistro(registro.getRegistro());
//            facadepye.create(bolsa.getBolsaTrabajo());
//            facadepye.flush();
//        });
            List<Integer> listaCondicional = new ArrayList<>();
            listaBolsaTrabajo.getBolsa().forEach((bolsa) -> {
            facadepye.setEntityClass(BolsaTrabajo.class);
            BolsaTrabajo bolTrabEncontrada = getRegistroBolsaTrabajo(bolsa.getBolsaTrabajo());
            Boolean registroAlmacenado = false;

            if (bolTrabEncontrada != null) {
//                listaCondicional.add(bolsa.getBolsaTrabajo().getBolsatrab());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                bolsa.getBolsaTrabajo().setRegistro(bolTrabEncontrada.getRegistro());
                bolsa.getBolsaTrabajo().getEmpresa().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(bolsa.getBolsaTrabajo().getEmpresa().getEmpresa()));
                facadepye.edit(bolsa.getBolsaTrabajo());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                bolsa.getBolsaTrabajo().getEmpresa().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(bolsa.getBolsaTrabajo().getEmpresa().getEmpresa()));
                bolsa.getBolsaTrabajo().setRegistro(registro.getRegistro());
                facadepye.create(bolsa.getBolsaTrabajo());
            }
            facadepye.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
 }
    
    @Override
    public Integer getRegistroBolsaTrabajoEspecifico(int bolsatrab) {
        TypedQuery<BolsaTrabajo> query = em.createNamedQuery("BolsaTrabajo.findByBolsatrab", BolsaTrabajo.class);
        query.setParameter("bolsatrab", bolsatrab);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }

    @Override
    public BolsaTrabajo getRegistroBolsaTrabajo(BolsaTrabajo bolsaTrabajo) {
        TypedQuery<BolsaTrabajo> query = em.createQuery("SELECT b FROM BolsaTrabajo b WHERE b.bolsatrab = :bolsatrab", BolsaTrabajo.class);
        query.setParameter("bolsatrab", bolsaTrabajo.getBolsatrab());
        try {
            bolsaTrabajo = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            bolsaTrabajo = null;
            System.out.println(ex.toString());
        }
        return bolsaTrabajo;
    }
}