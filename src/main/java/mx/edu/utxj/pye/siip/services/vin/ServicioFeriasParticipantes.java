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
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.FeriasParticipantes;
import mx.edu.utxj.pye.sgi.entity.pye2.FeriasProfesiograficas;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOFeriasParticipantes;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaFeriasParticipantes;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbFeriasParticipantes;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbFeriasProfesiograficas;
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
public class ServicioFeriasParticipantes implements EjbFeriasParticipantes{
    
    @EJB
    Facade facdepye;
    @EJB
    EjbModulos ejbModulos;
    @EJB
    EjbFeriasProfesiograficas ejbFeriasProfesiograficas;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    @Override
    public  ListaFeriasParticipantes getListaFeriasParticipantes(String rutaArchivo) throws Throwable {
       
        ListaFeriasParticipantes listaFeriasParticipantes = new  ListaFeriasParticipantes();

        List<DTOFeriasParticipantes> listaDtoFeriasParticipantes = new ArrayList<>();
        Iems iems;
        FeriasProfesiograficas feriasProfesiograficas;
        FeriasParticipantes feriasParticipantes;
        DTOFeriasParticipantes dTOFeriasParticipantes;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(1);
        XSSFRow fila;
        
        if (primeraHoja.getSheetName().equals("Participantes Ferias")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((!"".equals(fila.getCell(23).getNumericCellValue()))) {
                iems = new Iems();
                feriasProfesiograficas = new FeriasProfesiograficas();
                feriasParticipantes = new FeriasParticipantes();
                dTOFeriasParticipantes = new DTOFeriasParticipantes();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        feriasProfesiograficas.setFeria(fila.getCell(0).getStringCellValue());
                        feriasParticipantes.setFeria(feriasProfesiograficas);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(21).getCellTypeEnum()) {
                    case STRING: 
                        iems.setNombre(fila.getCell(21).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(22).getCellTypeEnum()) {
                    case FORMULA:
                        iems.setIems((int)fila.getCell(22).getNumericCellValue());
                        feriasParticipantes.setIems(iems);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(23).getCellTypeEnum()) {
                    case NUMERIC: 
                       feriasParticipantes.setParticipantes((int) fila.getCell(23).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                    dTOFeriasParticipantes.setFeriasParticipantes(feriasParticipantes);
                    listaDtoFeriasParticipantes.add(dTOFeriasParticipantes);
                    
                }
            }
            listaFeriasParticipantes.setFeriasParticipantes(listaDtoFeriasParticipantes);
            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaFeriasParticipantes;
        
    }

    @Override
    public void guardaFeriasParticipantes(ListaFeriasParticipantes listaFeriasParticipantes, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//            listaFeriasParticipantes.getFeriasParticipantes().forEach((feriasParticipantes) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//            
//            feriasParticipantes.getFeriasParticipantes().getFeria().setRegistro(ejbFeriasProfesiograficas.getRegistroFeriasProfesiograficasEspecifico(feriasParticipantes.getFeriasParticipantes().getFeria().getFeria()));
//                        
//            facdepye.setEntityClass(FeriasParticipantes.class);
//            feriasParticipantes.getFeriasParticipantes().setRegistro(registro.getRegistro());
//            facdepye.create(feriasParticipantes.getFeriasParticipantes());
//            facdepye.flush();
//        });
            List<String> listaCondicional = new ArrayList<>();
            listaFeriasParticipantes.getFeriasParticipantes().forEach((feriasParticipantes) -> {
            try {
                facdepye.setEntityClass(FeriasParticipantes.class);
                FeriasParticipantes ferPartEncontrado = getRegistroFeriasParticipantes(feriasParticipantes.getFeriasParticipantes());
                Boolean registroAlmacenado = false;
                if (ferPartEncontrado != null) {
                    listaCondicional.add(feriasParticipantes.getFeriasParticipantes().getFeria().getFeria()+ " - " + feriasParticipantes.getFeriasParticipantes().getIems().getNombre());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    feriasParticipantes.getFeriasParticipantes().setRegistro(ferPartEncontrado.getRegistro());
                    feriasParticipantes.getFeriasParticipantes().getFeria().setRegistro(ferPartEncontrado.getFeria().getRegistro());
                    facdepye.edit(feriasParticipantes.getFeriasParticipantes());
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    feriasParticipantes.getFeriasParticipantes().getFeria().setRegistro(ejbFeriasProfesiograficas.getRegistroFeriasProfesiograficasEspecifico(feriasParticipantes.getFeriasParticipantes().getFeria().getFeria()));
                    feriasParticipantes.getFeriasParticipantes().setRegistro(registro.getRegistro());
                    facdepye.create(feriasParticipantes.getFeriasParticipantes());
                }
                facdepye.flush();
            } catch (Throwable ex) {
                Logger.getLogger(ServicioFeriasProfesiograficas.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public FeriasParticipantes getRegistroFeriasParticipantes(FeriasParticipantes feriasParticipantes) {
        TypedQuery<FeriasParticipantes> query = em.createQuery("SELECT p FROM FeriasParticipantes p JOIN p.feria f JOIN p.iems i WHERE f.feria =:feria AND i.iems = :iems", FeriasParticipantes.class);
        query.setParameter("feria", feriasParticipantes.getFeria().getFeria());
        query.setParameter("iems", feriasParticipantes.getIems().getIems());
        try {
            feriasParticipantes = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            feriasParticipantes = null;
            ex.toString();
        }
        return feriasParticipantes; 
    }
    
}
