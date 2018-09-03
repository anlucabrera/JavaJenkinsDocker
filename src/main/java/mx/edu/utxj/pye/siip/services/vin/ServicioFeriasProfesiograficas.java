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
import mx.edu.utxj.pye.sgi.entity.pye2.BolsaTrabajo;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.FeriasProfesiograficas;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.MunicipioPK;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.LocalidadPK;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOFerias;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaFeriasProfesiograficas;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbFeriasProfesiograficas;
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
public class ServicioFeriasProfesiograficas implements EjbFeriasProfesiograficas{
    @EJB
    Facade facdepye;
    @EJB
    EjbModulos ejbModulos;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    @Override
    public ListaFeriasProfesiograficas getListaFeriasProfesiograficas(String rutaArchivo) throws Throwable {
       
        ListaFeriasProfesiograficas listaFeriasProfesiograficas = new  ListaFeriasProfesiograficas();

        List<DTOFerias> listaDtoFerias = new ArrayList<>();
        Estado estado;
        Municipio municipio;
        MunicipioPK municipioPK;
        Localidad localidad;
        LocalidadPK localidadPK;
        FeriasProfesiograficas feriasProfesiograficas;
        DTOFerias dTOFerias;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        if (primeraHoja.getSheetName().equals("Ferias Profesiográficas")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((!"".equals(fila.getCell(0).getStringCellValue()))) {
                estado = new Estado();
                municipio = new Municipio();
                municipioPK = new MunicipioPK();
                localidad = new  Localidad();
                localidadPK = new LocalidadPK();
                feriasProfesiograficas = new FeriasProfesiograficas();
                dTOFerias = new DTOFerias();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case FORMULA:
                        feriasProfesiograficas.setFeria(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case NUMERIC: 
                         if (DateUtil.isCellDateFormatted(fila.getCell(2))) {
                         feriasProfesiograficas.setFecha(fila.getCell(2).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case STRING:
                         feriasProfesiograficas.setEvento(fila.getCell(5).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(11).getCellTypeEnum()) {
                    case STRING: 
                        estado.setNombre(fila.getCell(11).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(12).getCellTypeEnum()) {
                    case FORMULA: 
                        estado.setIdestado((int) fila.getCell(12).getNumericCellValue());
                        municipio.setEstado(estado);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(16).getCellTypeEnum()) {
                    case STRING:
                        municipio.setNombre(fila.getCell(16).getStringCellValue());
                        municipio.setMunicipioPK(municipioPK);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(17).getCellTypeEnum()) {
                    case FORMULA:
                         municipioPK.setClaveMunicipio((int)fila.getCell(17).getNumericCellValue());
                         municipioPK.setClaveEstado((int) fila.getCell(12).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(22).getCellTypeEnum()) {
                    case STRING:
                        localidad.setNombre(fila.getCell(22).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(23).getCellTypeEnum()) {
                    case FORMULA:
                        localidadPK.setClaveLocalidad((int)fila.getCell(23).getNumericCellValue());
                        localidadPK.setClaveMunicipio((int)fila.getCell(17).getNumericCellValue());
                        localidadPK.setClaveEstado((int) fila.getCell(12).getNumericCellValue());
                        localidad.setLocalidadPK(localidadPK);
                        localidad.setMunicipio(municipio);
                        break;
                    default:
                        break;
                }
                    feriasProfesiograficas.setLocalidad(localidad);
                    dTOFerias.setFeriasProfesiograficas(feriasProfesiograficas);
                    listaDtoFerias.add(dTOFerias);
                }
            }
            listaFeriasProfesiograficas.setFerias(listaDtoFerias);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaFeriasProfesiograficas;
    }

    @Override
    public void guardaFeriasProfesiograficas(ListaFeriasProfesiograficas listaFeriasProfesiograficas, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//            listaFeriasProfesiograficas.getFerias().forEach((ferias) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//            
//            facdepye.setEntityClass(FeriasProfesiograficas.class);
//            ferias.getFeriasProfesiograficas().setRegistro(registro.getRegistro());
//            facdepye.create(ferias.getFeriasProfesiograficas());
//            facdepye.flush();
//        });
            List<String> listaCondicional = new ArrayList<>();
            listaFeriasProfesiograficas.getFerias().forEach((ferias) -> {
            facdepye.setEntityClass(FeriasProfesiograficas.class);
            FeriasProfesiograficas ferProEncontrada = getRegistroFeriasProfesiograficas(ferias.getFeriasProfesiograficas());
            Boolean registroAlmacenado = false;

            if (ferProEncontrada != null) {
                listaCondicional.add(ferias.getFeriasProfesiograficas().getFeria());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                ferias.getFeriasProfesiograficas().setRegistro(ferProEncontrada.getRegistro());
                facdepye.edit(ferias.getFeriasProfesiograficas());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                ferias.getFeriasProfesiograficas().setRegistro(registro.getRegistro());
                facdepye.create(ferias.getFeriasProfesiograficas());
            }
            facdepye.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
}
    @Override
    public Integer getRegistroFeriasProfesiograficasEspecifico(String feria) {
        TypedQuery<FeriasProfesiograficas> query = em.createNamedQuery("FeriasProfesiograficas.findByFeria", FeriasProfesiograficas.class);
        query.setParameter("feria", feria);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }

    @Override
    public FeriasProfesiograficas getRegistroFeriasProfesiograficas(FeriasProfesiograficas feriasProfesiograficas) {
        TypedQuery<FeriasProfesiograficas> query = em.createQuery("SELECT f FROM FeriasProfesiograficas f WHERE f.feria = :feria", FeriasProfesiograficas.class);
        query.setParameter("feria", feriasProfesiograficas.getFeria());
        try {
            feriasProfesiograficas = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            feriasProfesiograficas = null;
            System.out.println(ex.toString());
        }
        return feriasProfesiograficas;
    }
}
