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
import mx.edu.utxj.pye.sgi.entity.pye2.Presupuestos;
import mx.edu.utxj.pye.sgi.entity.pye2.CapitulosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.finanzas.DTOPresupuestos;
import mx.edu.utxj.pye.siip.entity.finanzas.list.ListaPresupuestos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.pa.EjbPresupuestos;
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
public class ServicioPresupuestos implements EjbPresupuestos{
    
    @EJB
    Facade facdepye;
    @EJB
    EjbModulos ejbModulos;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    @Override
    public ListaPresupuestos getListaPresupuestos(String rutaArchivo) throws Throwable {
       
        ListaPresupuestos listaPresupuestos = new ListaPresupuestos();

        List<DTOPresupuestos> listaDtoPresupuestos = new ArrayList<>();
        Presupuestos presupuestos;
        CapitulosTipos capitulosTipos;
        DTOPresupuestos dTOPresupuestos;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        if (primeraHoja.getSheetName().equals("Presupuesto")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

            if ((!"".equals(fila.getCell(1).getStringCellValue()))) {
                presupuestos = new Presupuestos();
                capitulosTipos = new CapitulosTipos();
                dTOPresupuestos = new  DTOPresupuestos();
                
               
                switch (fila.getCell(1).getCellTypeEnum()) {
                   case FORMULA:
                       presupuestos.setPresupuestoOperacion(fila.getCell(1).getStringCellValue());
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case FORMULA: 
                         presupuestos.setPresupuestoTipo(fila.getCell(3).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case STRING:
                        capitulosTipos.setNombre(fila.getCell(4).getStringCellValue());
                        break;
                    case NUMERIC:
                        Integer num = (int) fila.getCell(4).getNumericCellValue();
                        String cap = Integer.toString(num);
                        capitulosTipos.setNombre(cap);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case FORMULA:
                        capitulosTipos.setCapituloTipo((short)fila.getCell(5).getNumericCellValue());
                        presupuestos.setCapituloTipo(capitulosTipos);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case NUMERIC:
                        presupuestos.setMonto((double) fila.getCell(6).getNumericCellValue());
                        break;
                    default:
                        break;
                }
               
                switch (fila.getCell(8).getCellTypeEnum()) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(8))) {
                        presupuestos.setFechaAplicacion(fila.getCell(8).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                   
                    dTOPresupuestos.setPresupuestos(presupuestos);
                    listaDtoPresupuestos.add(dTOPresupuestos);
                }
            }
            listaPresupuestos.setPresupuestos(listaDtoPresupuestos);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaPresupuestos;
        
    }

    @Override
    public void guardaPresupuestos(ListaPresupuestos listaPresupuestos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//
//            listaPresupuestos.getPresupuestos().forEach((presupuestos) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//            
//            facdepye.setEntityClass(Presupuestos.class);
//            presupuestos.getPresupuestos().setRegistro(registro.getRegistro());
//            facdepye.create(presupuestos.getPresupuestos());
//            facdepye.flush();
//        });
        List<String> validaciones = new SerializableArrayList<>();
        List<String> listaCondicional = new ArrayList<>();
        listaPresupuestos.getPresupuestos().forEach((presupuestos) -> {
            
                facdepye.setEntityClass(Presupuestos.class);
                Presupuestos presEncontrado = getRegistroPresupuestos(presupuestos.getPresupuestos());
                Boolean registroAlmacenado = false;
                if (presEncontrado != null) {
                    listaCondicional.add(presupuestos.getPresupuestos().getPresupuestoOperacion()+" - "+presupuestos.getPresupuestos().getPresupuestoTipo());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    presupuestos.getPresupuestos().setRegistro(presEncontrado.getRegistro());
                    facdepye.edit(presupuestos.getPresupuestos());
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    presupuestos.getPresupuestos().setRegistro(registro.getRegistro());
                    facdepye.create(presupuestos.getPresupuestos());
                }
                facdepye.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public Presupuestos getRegistroPresupuestos(Presupuestos presupuestos) {
        TypedQuery<Presupuestos> query = em.createQuery("SELECT p FROM Presupuestos p JOIN p.capituloTipo c WHERE p.presupuestoOperacion = :presupuestoOperacion AND p.presupuestoTipo = :presupuestoTipo AND c.capituloTipo = :capituloTipo", Presupuestos.class);
        query.setParameter("presupuestoOperacion", presupuestos.getPresupuestoOperacion());
        query.setParameter("presupuestoTipo", presupuestos.getPresupuestoTipo());
        query.setParameter("capituloTipo", presupuestos.getCapituloTipo().getCapituloTipo());
        try {
            presupuestos = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            presupuestos = null;
            ex.toString();
        }
        return presupuestos;
    }
}
