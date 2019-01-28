/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.pa;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
import mx.edu.utxj.pye.sgi.entity.pye2.Presupuestos;
import mx.edu.utxj.pye.sgi.entity.pye2.CapitulosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.finanzas.DTOPresupuestos;
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
    
    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
    @Inject ControladorEmpleado controladorEmpleado;
    
    @Override
    public List<DTOPresupuestos> getListaPresupuestos(String rutaArchivo) throws Throwable {
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
                        dTOPresupuestos.setCapitulo(capitulosTipos.getNombre());
                        break;
                    case NUMERIC:
                        Integer num = (int) fila.getCell(4).getNumericCellValue();
                        String cap = Integer.toString(num);
                        capitulosTipos.setNombre(cap);
                        dTOPresupuestos.setCapitulo(cap);
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
            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaDtoPresupuestos;
        
    }

    @Override
    public void guardaPresupuestos(List<DTOPresupuestos> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
       
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        List<String> listaCondicional = new ArrayList<>();
        lista.forEach((presupuestos) -> {
            
                f.setEntityClass(Presupuestos.class);
                Presupuestos presEncontrado = getRegistroPresupuestos(presupuestos.getPresupuestos());
                Boolean registroAlmacenado = false;
                if (presEncontrado != null) {
                    listaCondicional.add(presupuestos.getPresupuestos().getPresupuestoOperacion()+" - "+presupuestos.getPresupuestos().getPresupuestoTipo());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    
                    Date fA = presEncontrado.getFechaAplicacion();
                    LocalDate fecApl = fA.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    String fecha = fecApl.format(formatter);
                    
                    if (ejbModulos.validaEventoRegistro(ejbModulos.getEventoRegistro(), presEncontrado.getRegistros().getEventoRegistro().getEventoRegistro())) {
                        presupuestos.getPresupuestos().setRegistro(presEncontrado.getRegistro());
                        f.edit(presupuestos.getPresupuestos());
                        f.flush();
                        addDetailMessage("<b>Se actualizaron los registros con fecha de aplicación: </b> " + fecha);
                    } else {
                        addDetailMessage("<b>No se pueden actualizar los registros con fecha de aplicación: </b> " + fecha);
                    }
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    presupuestos.getPresupuestos().setRegistro(registro.getRegistro());
                    f.create(presupuestos.getPresupuestos());
                    f.flush();
                    addDetailMessage("<b>Se guardaron los registros correctamente </b>");
                }
                f.flush();
        });
    }

    @Override
    public Presupuestos getRegistroPresupuestos(Presupuestos presupuestos) {
        TypedQuery<Presupuestos> query = f.getEntityManager().createQuery("SELECT p FROM Presupuestos p JOIN p.capituloTipo c WHERE p.presupuestoOperacion = :presupuestoOperacion AND p.presupuestoTipo = :presupuestoTipo AND c.capituloTipo = :capituloTipo", Presupuestos.class);
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

    @Override
    public List<DTOPresupuestos> getRegistroDTOPresupuestos(String mes, Short ejercicio) {
        List<DTOPresupuestos> ldto = new ArrayList<>();
        TypedQuery<Presupuestos> q = f.getEntityManager()
                .createQuery("SELECT p from Presupuestos p WHERE p.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND p.registros.eventoRegistro.mes = :mes AND p.registros.area = :area", Presupuestos.class);
        q.setParameter("mes", mes);
        q.setParameter("ejercicio", ejercicio);
        q.setParameter("area", controladorEmpleado.getNuevaAreasUniversidad().getArea());
        List<Presupuestos> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            TypedQuery<EventosRegistros> query = f.getEntityManager().createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            l.forEach(x -> {
                Registros registro = f.getEntityManager().find(Registros.class, x.getRegistro());
                EventosRegistros eventos = f.getEntityManager().find(EventosRegistros.class,registro.getEventoRegistro().getEventoRegistro());
                CapitulosTipos capitulosTipos = f.getEntityManager().find(CapitulosTipos.class, x.getCapituloTipo().getCapituloTipo());
                AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, registro.getArea());
                ActividadesPoa a = registro.getActividadesPoaList().isEmpty() ? null :registro.getActividadesPoaList().get(0);
                DTOPresupuestos dto;
                if (eventoRegistro.equals(registro.getEventoRegistro())) {
                    dto = new DTOPresupuestos(x, capitulosTipos, a, eventos, capitulosTipos.getNombre());
                } else {
                    dto = new DTOPresupuestos(x, capitulosTipos, a, eventos, capitulosTipos.getNombre());
                }
                ldto.add(dto);
            });
            return ldto;
    }
        
    }

    @Override
    public List<CapitulosTipos> getCapitulosTiposAct() {
        List<CapitulosTipos> genLst = new ArrayList<>();
        TypedQuery<CapitulosTipos> query = f.getEntityManager().createQuery("SELECT c FROM CapitulosTipos c", CapitulosTipos.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }

}
