/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

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
import mx.edu.utxj.pye.sgi.entity.prontuario.Materias;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.DesercionPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.DesercionReprobacionMaterias;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.escolar.DTOReprobacion;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaDesercionReprobacion;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbDesercionPeriodos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbDesercionReprobacion;
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
public class ServicioDesercionReprobacion implements EjbDesercionReprobacion{
    @EJB
    Facade facdepye;
    @EJB
    EjbModulos ejbModulos;
    @EJB
    EjbDesercionPeriodos ejbDesercionPeriodos;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    @Override
    public  ListaDesercionReprobacion getListaDesercionReprobacion(String rutaArchivo) throws Throwable {
       
        ListaDesercionReprobacion listaDesercionReprobacion = new ListaDesercionReprobacion();

        List<DTOReprobacion> listaDtoReprobacion = new ArrayList<>();
        Materias materias;
        Personal personal;
        DesercionPeriodosEscolares desercionPeriodosEscolares;
        DesercionReprobacionMaterias desercionReprobacionMaterias;
        DTOReprobacion dTOReprobacion;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(1);
        XSSFRow fila;
        
        String mat="";
      
        if (primeraHoja.getSheetName().equals("Deserción por Reprobación")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                
            if ((!"".equals(fila.getCell(0).getStringCellValue()))) {
                materias = new Materias();
                personal = new Personal();
                desercionPeriodosEscolares = new DesercionPeriodosEscolares();
                desercionReprobacionMaterias = new DesercionReprobacionMaterias();
                dTOReprobacion = new DTOReprobacion();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        desercionPeriodosEscolares.setDpe(fila.getCell(0).getStringCellValue());
                        desercionReprobacionMaterias.setDpe(desercionPeriodosEscolares);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(1).getCellTypeEnum()) {
                     case FORMULA:
                        int m = (int)fila.getCell(1).getNumericCellValue();
                        mat =Integer.toString(m);
                        dTOReprobacion.setMatricula(mat);
                        break;
                    default:
                        break;
                }
                
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case STRING:
                        materias.setNombre(fila.getCell(4).getStringCellValue());
                        break;
                    default:
                        break;
                }
              
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case FORMULA:
                        materias.setCveMateria(fila.getCell(5).getStringCellValue());
//                        desercionReprobacionMaterias.setAsignatura(materias.getCveMateria());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case STRING:
                        personal.setNombre(fila.getCell(6).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                    case FORMULA:
                        personal.setClave((int) fila.getCell(7).getNumericCellValue());
                        desercionReprobacionMaterias.setDocente(personal.getClave());
                        break;
                    default:
                        break;
                }
                    dTOReprobacion.setMaterias(materias);
                    dTOReprobacion.setPersonal(personal);
                    dTOReprobacion.setDesercionReprobacionMaterias(desercionReprobacionMaterias);
                    listaDtoReprobacion.add(dTOReprobacion);
                    
                }
            }
            listaDesercionReprobacion.setReprobacion(listaDtoReprobacion);
            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaDesercionReprobacion;
    }
    
    
    @Override
    public void guardaDesercionReprobacion(ListaDesercionReprobacion listaDesercionReprobacion, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//            listaDesercionReprobacion.getReprobacion().forEach((reprobacion) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//            
//            reprobacion.getDesercionReprobacionMaterias().getDpe().setRegistro(ejbDesercionPeriodos.getRegistroDesercionPeriodosEspecifico(reprobacion.getDesercionReprobacionMaterias().getDpe().getDpe()));
//                        
//            facdepye.setEntityClass(DesercionReprobacionMaterias.class);
//            reprobacion.getDesercionReprobacionMaterias().setRegistro(registro.getRegistro());
//            facdepye.create(reprobacion.getDesercionReprobacionMaterias());
//            facdepye.flush();
//        });
            List<String> listaCondicional = new ArrayList<>();
            listaDesercionReprobacion.getReprobacion().forEach((reprobacion) -> {
            facdepye.setEntityClass(DesercionReprobacionMaterias.class);
            DesercionReprobacionMaterias desRepMatEncontrada = getRegistroDesercionReprobacionMaterias(reprobacion.getDesercionReprobacionMaterias());
            Boolean registroAlmacenado = false;

            if (desRepMatEncontrada != null) {
                listaCondicional.add(reprobacion.getDesercionReprobacionMaterias().getDpe().getDpe()+ " " + reprobacion.getMaterias().getNombre());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                reprobacion.getDesercionReprobacionMaterias().setRegistro(desRepMatEncontrada.getRegistro());
                reprobacion.getDesercionReprobacionMaterias().getDpe().setRegistro(ejbDesercionPeriodos.getRegistroDesercionPeriodosEspecifico(reprobacion.getDesercionReprobacionMaterias().getDpe().getDpe()));
                facdepye.edit(reprobacion.getDesercionReprobacionMaterias());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                reprobacion.getDesercionReprobacionMaterias().getDpe().setRegistro(ejbDesercionPeriodos.getRegistroDesercionPeriodosEspecifico(reprobacion.getDesercionReprobacionMaterias().getDpe().getDpe()));
                reprobacion.getDesercionReprobacionMaterias().setRegistro(registro.getRegistro());
                facdepye.create(reprobacion.getDesercionReprobacionMaterias());
            }
            facdepye.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public DesercionReprobacionMaterias getRegistroDesercionReprobacionMaterias(DesercionReprobacionMaterias desercionReprobacionMaterias) {
        TypedQuery<DesercionReprobacionMaterias> query = em.createQuery("SELECT r FROM DesercionReprobacionMaterias r JOIN r.dpe d WHERE d.dpe = :dpe AND r.asignatura = :asignatura AND r.docente = :docente",  DesercionReprobacionMaterias.class);
        query.setParameter("dpe", desercionReprobacionMaterias.getDpe().getDpe());
        query.setParameter("asignatura", desercionReprobacionMaterias.getAsignatura());
        query.setParameter("docente", desercionReprobacionMaterias.getDocente());
        try {
            desercionReprobacionMaterias = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            desercionReprobacionMaterias = null;
            System.out.println(ex.toString());
        }
        return desercionReprobacionMaterias;
    }
}
