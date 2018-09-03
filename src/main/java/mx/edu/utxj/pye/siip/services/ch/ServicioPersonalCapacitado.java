/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ch;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.math.BigDecimal;
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
import mx.edu.utxj.pye.sgi.entity.pye2.PersonalCapacitado;
import mx.edu.utxj.pye.sgi.entity.pye2.PercapTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.PercapModalidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.caphum.DTOPersonalCapacitado;
import mx.edu.utxj.pye.siip.entity.caphum.list.ListaPersonalCapacitado;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbPersonalCapacitado;
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
public class ServicioPersonalCapacitado implements EjbPersonalCapacitado{
    @EJB
    Facade facadepye;
    @EJB
    EjbModulos ejbModulos;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    @Override
    public ListaPersonalCapacitado getListaPersonalCapacitado(String rutaArchivo) throws Throwable {
       
        ListaPersonalCapacitado listaPersonalCapacitado = new  ListaPersonalCapacitado();

        List<DTOPersonalCapacitado> listaDtoPersonalCapacitado = new ArrayList<>();
        
        PercapTipo percapTipo;
        PercapModalidad percapModalidad;
        PersonalCapacitado personalCapacitado;
        DTOPersonalCapacitado dTOPersonalCapacitado;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        if (primeraHoja.getSheetName().equals("Registro Cursos")) {
        for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((fila.getCell(7).getNumericCellValue()!= 0)) {
                percapTipo = new PercapTipo();
                percapModalidad = new PercapModalidad();
                personalCapacitado = new PersonalCapacitado();
                dTOPersonalCapacitado = new DTOPersonalCapacitado();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case FORMULA:
                        personalCapacitado.setCurso(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case STRING: 
                        personalCapacitado.setNombre(fila.getCell(2).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(3))) {
                         personalCapacitado.setFechaInicial(fila.getCell(3).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(4))) {
                         personalCapacitado.setFechaFinal(fila.getCell(4).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                    case FORMULA: 
                        personalCapacitado.setDuracion(fila.getCell(7).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(8).getCellTypeEnum()) {
                    case STRING:
                        percapTipo.setTipo(fila.getCell(8).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(9).getCellTypeEnum()) {
                    case FORMULA:
                        percapTipo.setPercapTipo((short) fila.getCell(9).getNumericCellValue());
                        personalCapacitado.setTipo(percapTipo);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(10).getCellTypeEnum()) {
                    case STRING:
                        percapModalidad.setModalidad(fila.getCell(10).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(11).getCellTypeEnum()) {
                    case FORMULA:
                        percapModalidad.setPercapMod((short) fila.getCell(11).getNumericCellValue());
                        personalCapacitado.setModalidad(percapModalidad);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(12).getCellTypeEnum()) {
                    case STRING:
                        personalCapacitado.setEmpresaImpartidora(fila.getCell(12).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(13).getCellTypeEnum()) {
                    case STRING:
                        personalCapacitado.setObjetivo(fila.getCell(13).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(14).getCellTypeEnum()) {
                    case STRING:
                        personalCapacitado.setLugarImparticion(fila.getCell(14).getStringCellValue());
                        break;
                    default:
                        break;
                }
                 switch (fila.getCell(16).getCellTypeEnum()) {
                    case FORMULA:
                        BigDecimal inv= new BigDecimal((int)fila.getCell(16).getNumericCellValue());
                        personalCapacitado.setMontoInvertido(inv);
                        break;
                    default:
                        break;
                }
                    dTOPersonalCapacitado.setPersonalCapacitado(personalCapacitado);
                    listaDtoPersonalCapacitado.add(dTOPersonalCapacitado);
                
                    System.out.println(listaPersonalCapacitado);
                
                }
            }
            listaPersonalCapacitado.setCapacitaciones(listaDtoPersonalCapacitado);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaPersonalCapacitado;
        
    }

    @Override
    public void guardaPersonalCapacitado(ListaPersonalCapacitado listaPersonalCapacitado, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//            listaPersonalCapacitado.getCapacitaciones().forEach((capacitaciones) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//            
//            facadepye.setEntityClass(PersonalCapacitado.class);
//            capacitaciones.getPersonalCapacitado().setRegistro(registro.getRegistro());
//            facadepye.create(capacitaciones.getPersonalCapacitado());
//            facadepye.flush();
//        });
            List<String> listaCondicional = new ArrayList<>();
            listaPersonalCapacitado.getCapacitaciones().forEach((capacitaciones) -> {
            facadepye.setEntityClass(PersonalCapacitado.class);
            PersonalCapacitado perCapEncontrado = getRegistroPersonalCapacitado(capacitaciones.getPersonalCapacitado());
            Boolean registroAlmacenado = false;
            if (perCapEncontrado != null) {
                listaCondicional.add(capacitaciones.getPersonalCapacitado().getCurso());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                capacitaciones.getPersonalCapacitado().setRegistro(perCapEncontrado.getRegistro());
                facadepye.edit(capacitaciones.getPersonalCapacitado());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                capacitaciones.getPersonalCapacitado().setRegistro(registro.getRegistro());
                facadepye.create(capacitaciones.getPersonalCapacitado());
            }
            facadepye.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
 }
    
    @Override
    public Integer getRegistroPersonalCapacitadoEspecifico(String curso) {
        TypedQuery<PersonalCapacitado> query = em.createNamedQuery("PersonalCapacitado.findByCurso", PersonalCapacitado.class);
        query.setParameter("curso", curso);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }

    @Override
    public PersonalCapacitado getRegistroPersonalCapacitado(PersonalCapacitado personalCapacitado) {
        TypedQuery<PersonalCapacitado> query = em.createNamedQuery("PersonalCapacitado.findByCurso", PersonalCapacitado.class);
        query.setParameter("curso", personalCapacitado.getCurso());
        try {
             personalCapacitado = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            personalCapacitado = null;
            ex.toString();
        }
        return personalCapacitado;
    }

    @Override
    public List<PercapTipo> getPerCapTipoAct() {
        List<PercapTipo> genLst = new ArrayList<>();
        TypedQuery<PercapTipo> query = em.createQuery("SELECT p FROM PercapTipo p", PercapTipo.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }

    @Override
    public List<PercapModalidad> getPerCapModalidadAct() {
        List<PercapModalidad> genLst = new ArrayList<>();
        TypedQuery<PercapModalidad> query = em.createQuery("SELECT p FROM PercapModalidad p", PercapModalidad.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }

}
