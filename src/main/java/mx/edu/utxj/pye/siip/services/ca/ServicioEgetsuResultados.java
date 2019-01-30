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
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EgetsuResultadosGeneraciones;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.escolar.DTOEgetsu;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbEgetsuResultados;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omnifaces.util.Messages;
/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioEgetsuResultados implements EjbEgetsuResultados{
    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @Inject ControladorEmpleado controladorEmpleado;
   
    @Override
    public List<DTOEgetsu> getListaEgetsuResultados(String rutaArchivo) throws Throwable {
       
        List<DTOEgetsu> listaDtoEgetsu = new ArrayList<>();
        EgetsuResultadosGeneraciones egetsuResultadosGeneraciones;
        DTOEgetsu dTOEgetsu;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        if (primeraHoja.getSheetName().equals("EGETSU")) {
        for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

            if ((!"".equals(fila.getCell(0).getStringCellValue()))) {
                egetsuResultadosGeneraciones = new EgetsuResultadosGeneraciones();
                dTOEgetsu = new DTOEgetsu();
                
               switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        dTOEgetsu.setGeneracion(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
               switch (fila.getCell(1).getCellTypeEnum()) {
                    case FORMULA:
                        egetsuResultadosGeneraciones.setGeneracion((short)fila.getCell(1).getNumericCellValue());
                        break;
                    default:
                        break;
                }
              
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case NUMERIC:
                        egetsuResultadosGeneraciones.setEgreEgetsu((int) fila.getCell(2).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case NUMERIC:
                         egetsuResultadosGeneraciones.setTestSobre((int) fila.getCell(3).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case NUMERIC:
                        egetsuResultadosGeneraciones.setTestSatis((int) fila.getCell(4).getNumericCellValue());
                        break;
                    default:
                        break;
                }

                switch (fila.getCell(5).getCellTypeEnum()) {
                    case NUMERIC:
                         egetsuResultadosGeneraciones.setSinTest((int) fila.getCell(5).getNumericCellValue());
                        break;
                    default:
                        break;
                }
               
               dTOEgetsu.setEgetsuResultadosGeneraciones(egetsuResultadosGeneraciones);
                    
               listaDtoEgetsu.add(dTOEgetsu);
            }
            }
            libroRegistro.close();
            Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaDtoEgetsu;
        
    }
    
   
   @Override
    public void guardaEgetsuResultados(List<DTOEgetsu> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
        List<String> listaCondicional = new ArrayList<>();
        lista.forEach((egetsus) -> {
            
                f.setEntityClass(EgetsuResultadosGeneraciones.class);
                EgetsuResultadosGeneraciones erg = getRegistroEgetsuResultadosGeneraciones(egetsus.getEgetsuResultadosGeneraciones().getGeneracion());
                Boolean registroAlmacenado = false;
                if (erg != null) {
                    listaCondicional.add(egetsus.getGeneracion());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    egetsus.getEgetsuResultadosGeneraciones().setRegistro(erg.getRegistro());
                    f.edit(egetsus.getEgetsuResultadosGeneraciones());
                    f.flush();
                    Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    egetsus.getEgetsuResultadosGeneraciones().setRegistro(registro.getRegistro());
                    f.create(egetsus.getEgetsuResultadosGeneraciones());
                    f.flush();
                    Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b>");
                }
                f.flush();
        });
    }

    @Override
    public EgetsuResultadosGeneraciones getRegistroEgetsuResultadosGeneraciones(Short generacion) {
        EgetsuResultadosGeneraciones egetsuResultadosGeneraciones = new EgetsuResultadosGeneraciones();
        TypedQuery<EgetsuResultadosGeneraciones> query = f.getEntityManager().createQuery("SELECT e FROM EgetsuResultadosGeneraciones e WHERE e.generacion = :generacion",EgetsuResultadosGeneraciones.class);
        query.setParameter("generacion", generacion);
        try {
            egetsuResultadosGeneraciones = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            egetsuResultadosGeneraciones = null;
            ex.toString();
        }
        return egetsuResultadosGeneraciones;
    }
   
    @Override
    public List<DTOEgetsu> filtroEgetsu(Short generacion) {
        if(generacion == null){
            return null;
        }
        
        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        List<DTOEgetsu> l = new ArrayList<>();
        List<EgetsuResultadosGeneraciones> entities = f.getEntityManager().createQuery("SELECT e FROM EgetsuResultadosGeneraciones e WHERE e.generacion =:generacion",  EgetsuResultadosGeneraciones.class)
                .setParameter("generacion", generacion)
                .getResultList();
      
        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            
            Generaciones g = f.getEntityManager().find(Generaciones.class, e.getGeneracion());
            String gen = g.getInicio() + " - " + g.getFin();
            
            Registros reg = f.getEntityManager().find(Registros.class, e.getRegistro());
            ActividadesPoa a = reg.getActividadesPoaList().isEmpty()?null:reg.getActividadesPoaList().get(0);
            l.add(new DTOEgetsu(
                    e,
                    f.getEntityManager().find(Generaciones.class, e.getGeneracion()),
                    a,
                    gen));
        });
        


        return l;
    }
}
