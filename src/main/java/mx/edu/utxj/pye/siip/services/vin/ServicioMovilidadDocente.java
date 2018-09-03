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
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosMovilidad;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistroMovilidadDocente;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOMovilidadDocente;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaMovilidadDocente;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbRegistroMovilidad;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbMovilidadDocente;
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
public class ServicioMovilidadDocente implements EjbMovilidadDocente{
    
    @EJB
    Facade facadepye;
    @EJB
    EjbModulos ejbModulos;
    @EJB
    EjbRegistroMovilidad ejbRegistroMovilidad;
   
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    @Override
    public ListaMovilidadDocente getListaMovilidadDocente(String rutaArchivo) throws Throwable {
       
        ListaMovilidadDocente listaMovilidadDocente = new ListaMovilidadDocente();

        List<DTOMovilidadDocente> listaDtoMovilidadDocente = new ArrayList<>();
        Personal personal;
        RegistrosMovilidad registrosMovilidad;
        RegistroMovilidadDocente registroMovilidadDocente;
        DTOMovilidadDocente dTOMovilidadDocente;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(2);
        XSSFRow fila;
        
        String clave ="";
        
        if (primeraHoja.getSheetName().equals("Movilidad Docente")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((!"".equals(fila.getCell(0).getStringCellValue()))) {
                personal = new Personal();
                registrosMovilidad = new RegistrosMovilidad();
                registroMovilidadDocente = new RegistroMovilidadDocente();
                dTOMovilidadDocente = new DTOMovilidadDocente();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        registrosMovilidad.setRegistroMovilidad(fila.getCell(0).getStringCellValue());
                        registroMovilidadDocente.setRegistroMovilidad(registrosMovilidad);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(1).getCellTypeEnum()) {
                    case FORMULA: 
                        dTOMovilidadDocente.setParticipanteProyecto(fila.getCell(1).getStringCellValue());
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
                        registroMovilidadDocente.setClavePersonal(personal.getClave());
                        break;
                    default:
                        break;
                }
                    dTOMovilidadDocente.setPersonal(personal);
                    dTOMovilidadDocente.setRegistroMovilidadDocente(registroMovilidadDocente);
                    listaDtoMovilidadDocente.add(dTOMovilidadDocente);
                    
                }
            }
            listaMovilidadDocente.setMovilidadDocente(listaDtoMovilidadDocente);
            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaMovilidadDocente;
    }

    @Override
    public void guardaMovilidadDocente(ListaMovilidadDocente listaMovilidadDocente, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//        listaPartActFormInt.getParticipantesActFormInt().forEach((t) -> {
//            System.out.println(t.getParticipantesActividadesFormacionIntegral().getRegistro()+ " " +t.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().getMatricula() + " " + t.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().getPeriodo() + " " + t.getParticipantesActividadesFormacionIntegral().getActividadFormacionIntegral().getActividadFormacionIntegral());
//            
//        });
            
            listaMovilidadDocente.getMovilidadDocente().forEach((movilidadDocente) -> {
            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
            
            movilidadDocente.getRegistroMovilidadDocente().getRegistroMovilidad().setRegistro(ejbRegistroMovilidad.getRegistroMovilidadEspecifico(movilidadDocente.getRegistroMovilidadDocente().getRegistroMovilidad().getRegistroMovilidad()));
                        
            facadepye.setEntityClass(RegistroMovilidadDocente.class);
            movilidadDocente.getRegistroMovilidadDocente().setRegistro(registro.getRegistro());
            facadepye.create(movilidadDocente.getRegistroMovilidadDocente());
            facadepye.flush();
        });
    }

    @Override
    public RegistroMovilidadDocente getRegistroMovilidadDocente(RegistroMovilidadDocente registroMovilidadDocente) {
        TypedQuery<RegistroMovilidadDocente> query = em.createQuery("SELECT r FROM RegistroMovilidadDocente r JOIN r.registroMovilidad m WHERE m.registroMovilidad = :registroMovilidad AND r.clavePersonal = :clavePersonal", RegistroMovilidadDocente.class);
        query.setParameter("registroMovilidad", registroMovilidadDocente.getRegistroMovilidad().getRegistroMovilidad());
        query.setParameter("clavePersonal", registroMovilidadDocente.getClavePersonal());
        try {
            registroMovilidadDocente = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            registroMovilidadDocente = null;
            ex.toString();
        }
        return registroMovilidadDocente; 
    }
}
