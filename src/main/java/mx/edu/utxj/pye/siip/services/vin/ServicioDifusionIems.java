/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.DifusionIems;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTODifusion;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaDifusionIems;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaDifusionIemsDTO;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbDifusionIems;
import org.apache.poi.ss.usermodel.DateUtil;
//import mx.edu.utxj.pye.siip.interfaces.vin.EjbIems;
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
public class ServicioDifusionIems implements EjbDifusionIems{
 
    @EJB EjbModulos ejbModulos;
    @EJB Facade f;
    @Inject ControladorEmpleado controladorEmpleado;
   
    @Override
    public ListaDifusionIems getListaDifusionIems(String rutaArchivo) throws Throwable {
        
        ListaDifusionIems listaDifusionIems = new  ListaDifusionIems();

        List<DTODifusion> listaDtoDifusion = new ArrayList<>();
        Iems iems;
        DifusionIems difusionIems;
        DTODifusion dTODifusion;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;

        if (primeraHoja.getSheetName().equals("Difusión")) {
        for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

             if ((!"".equals(fila.getCell(22).getStringCellValue()))) {
                iems = new Iems();
                difusionIems = new DifusionIems();
                dTODifusion = new DTODifusion();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(0))) {
                        difusionIems.setFechaInicio(fila.getCell(0).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(1).getCellTypeEnum()) {
                   case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(1))) {
                        difusionIems.setFechaFin(fila.getCell(1).getDateCellValue());
                        }
                    default:
                        break;
                }
                switch (fila.getCell(22).getCellTypeEnum()) {
                    case STRING:
                        iems.setNombre(fila.getCell(22).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(23).getCellTypeEnum()) {
                    case FORMULA:
                        iems.setIems((int) fila.getCell(23).getNumericCellValue());
                        difusionIems.setIems(iems);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(26).getCellTypeEnum()) {
                    case FORMULA:
                        difusionIems.setDuracion(fila.getCell(26).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(27).getCellTypeEnum()) {
                    case STRING:
                        difusionIems.setTipo(fila.getCell(27).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(28).getCellTypeEnum()) {
                    case NUMERIC:
                        difusionIems.setHombres((int) fila.getCell(28).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(29).getCellTypeEnum()) {
                    case NUMERIC:
                        difusionIems.setMujeres((int) fila.getCell(29).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                   
                    dTODifusion.setDifusionIems(difusionIems);
                    listaDtoDifusion.add(dTODifusion);
                }
            }
            listaDifusionIems.setDifusion(listaDtoDifusion);

            libroRegistro.close();
            Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaDifusionIems;
        
    }

    @Override
    public void guardaDifusionIems(ListaDifusionIems listaDifusionIems, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
            
            listaDifusionIems.getDifusion().forEach((difusion) -> {
            f.setEntityClass(DifusionIems.class);
            DifusionIems difIemsEncontrada = getRegistroDifusionIems(difusion.getDifusionIems());
            Boolean registroAlmacenado = false;

            if (difIemsEncontrada != null) {
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                if (ejbModulos.validaEventoRegistro(ejbModulos.getEventoRegistro(), difIemsEncontrada.getRegistros().getEventoRegistro().getEventoRegistro())) {
                difusion.getDifusionIems().setRegistro(difIemsEncontrada.getRegistro());
                f.edit(difusion.getDifusionIems());
                Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + difIemsEncontrada.getIems().getNombre()+" - "+ difIemsEncontrada.getTipo());
                } else{
                    Messages.addGlobalWarn("<b>No se pueden actualizar los registros con los siguientes datos: </b> " + difIemsEncontrada.getIems().getNombre()+" - "+ difIemsEncontrada.getTipo());
                }
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//                difusion.getDifusionIems().getIems().setRegistro(ejbDifusionIems.getRegistroIemsEspecifico(difusion.getDifusionIems().getIems().getIems()));
                difusion.getDifusionIems().setRegistro(registro.getRegistro());
                f.create(difusion.getDifusionIems());
                Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b> ");
            }
            f.flush();
        });
    }

    @Override
    public DifusionIems getRegistroDifusionIems(DifusionIems difusionIems) {
        TypedQuery<DifusionIems> query = f.getEntityManager().createQuery("SELECT d FROM DifusionIems d JOIN d.iems i WHERE i.iems = :iems AND d.tipo = :tipo AND d.fechaInicio = :fechaInicio", DifusionIems.class);
        query.setParameter("iems", difusionIems.getIems().getIems());
        query.setParameter("tipo", difusionIems.getTipo());
        query.setParameter("fechaInicio", difusionIems.getFechaInicio());
        try {
            difusionIems = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            difusionIems = null;
            System.out.println(ex.toString());
        }
        return difusionIems;
    }
    
    @Override
    public List<ListaDifusionIemsDTO> getRegistroDifusionIemsDTO(String mes, Short ejercicio) {
        List<ListaDifusionIemsDTO> ldto = new ArrayList<>();
        TypedQuery<DifusionIems> q = f.getEntityManager()
                .createQuery("SELECT a from DifusionIems a WHERE a.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND a.registros.eventoRegistro.mes = :mes AND a.registros.area = :area", DifusionIems.class);
        q.setParameter("mes", mes);
        q.setParameter("ejercicio", ejercicio);
        q.setParameter("area", controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
        List<DifusionIems> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
//            l.forEach(System.err::println);
            TypedQuery<EventosRegistros> query = f.getEntityManager().createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            l.forEach(x -> {

                ListaDifusionIemsDTO dto;
                Registros registro = f.getEntityManager().find(Registros.class, x.getRegistro());
                AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, registro.getArea());
                ActividadesPoa a = registro.getActividadesPoaList().isEmpty()?null:registro.getActividadesPoaList().get(0);
                if (eventoRegistro.equals(registro.getEventoRegistro())) {
                    dto = new ListaDifusionIemsDTO(Boolean.TRUE, x, au, a);
                } else {
                    dto = new ListaDifusionIemsDTO(Boolean.FALSE, x, au, a);
                }
                ldto.add(dto);
            });
            return ldto;
        }
    }
}
