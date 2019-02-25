/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.pa;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import lombok.Getter;
import lombok.Setter;
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
import org.omnifaces.util.Messages;
/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioPresupuestos implements EjbPresupuestos{
    
    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
    @Inject ControladorEmpleado controladorEmpleado;
    
    @Getter @Setter private List<Short> areas;
    
    @Override
    public List<DTOPresupuestos> getListaPresupuestos(String rutaArchivo) throws Throwable {
        List<Boolean> validarCelda = new ArrayList<>();
        List<String> datosInvalidos = new ArrayList<>();
        
        List<DTOPresupuestos> listaDtoPresupuestos = new ArrayList<>();
        Presupuestos presupuestos;
        CapitulosTipos capitulosTipos;
        DTOPresupuestos dTOPresupuestos;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        int obs=0;
        
        try {
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
                
                switch (fila.getCell(9).getCellTypeEnum()) {
                    case STRING:
                        presupuestos.setObservaciones(fila.getCell(9).getStringCellValue());
                        break;
                    default:
                        break;
                }
                
                switch (fila.getCell(11).getCellTypeEnum()) {
                    case FORMULA:
                        obs = (int) fila.getCell(11).getNumericCellValue();
                        break;
                    default:
                        break;
                }
                if (obs == 1) {
                    validarCelda.add(false);
                    datosInvalidos.add("Dato incorrecto: Observaciones del presupuesto en la columna: " + (5 + 1) + " y fila: " + (i + 1) + " \n ");
                }
              
                    dTOPresupuestos.setPresupuestos(presupuestos);
                    listaDtoPresupuestos.add(dTOPresupuestos);
                }
            }
            libroRegistro.close();
            if (validarCelda.contains(false)) {
                    Messages.addGlobalError("<b>El archivo cargado contiene datos que no son válidos, verifique los datos de la plantilla</b>");
                    Messages.addGlobalError(datosInvalidos.toString());
                    excel.delete();
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                  
                    return Collections.EMPTY_LIST;
                } else {
                    Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
                    return listaDtoPresupuestos;
                }
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
            return Collections.EMPTY_LIST;
        }
    } catch (IOException e) {
            libroRegistro.close();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            Messages.addGlobalError("<b>Ocurrió un error durante la lectura del archivo, asegurese de haber registrado correctamente su información</b>");
            return Collections.EMPTY_LIST;
    }

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
                        Messages.addGlobalInfo("<b>Se actualizaron los registros con fecha de aplicación: </b> " + fecha);
                    } else {
                        Messages.addGlobalWarn("<b>No se pueden actualizar los registros con fecha de aplicación: </b> " + fecha);
                    }
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    presupuestos.getPresupuestos().setRegistro(registro.getRegistro());
                    f.create(presupuestos.getPresupuestos());
                    f.flush();
                    Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b>");
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
        //verificar que los parametros no sean nulos
        if(mes == null || ejercicio == null){
            return null;
        }
        Short area = controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
        
        List<DTOPresupuestos> ldto = new ArrayList<>();
        List<Presupuestos> l = new ArrayList<>();
        
         if (area == 6) {

            l = f.getEntityManager().createQuery("SELECT p from Presupuestos p WHERE p.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND p.registros.eventoRegistro.mes = :mes", Presupuestos.class)
                     .setParameter("mes", mes)
                     .setParameter("ejercicio", ejercicio)
                     .getResultList();

        } else {
            areas = ejbModulos.getAreasDependientes(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());

             l = f.getEntityManager().createQuery("SELECT p from Presupuestos p WHERE p.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND p.registros.eventoRegistro.mes = :mes AND p.registros.area IN :areas", Presupuestos.class)
                     .setParameter("mes", mes)
                     .setParameter("ejercicio", ejercicio)
                     .setParameter("areas", areas)
                     .getResultList();
        }
                
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
