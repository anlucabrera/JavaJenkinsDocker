/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ch;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
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
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasEstimulos;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasEstimulosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.caphum.DTOProgramasEstimulos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbProgramasEstimulos;
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
public class ServicioProgramasEstimulos implements EjbProgramasEstimulos{
    
    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
    @Inject ControladorEmpleado controladorEmpleado;
   
    @Override
    public List<DTOProgramasEstimulos> getListaProgramaEstimulos(String rutaArchivo) throws Throwable {
        List<Boolean> validarCelda = new ArrayList<>();
        List<String> datosInvalidos = new ArrayList<>();
       
        List<DTOProgramasEstimulos> listaDtoProgEst = new ArrayList<>();
        Personal personal;
        ProgramasEstimulos programasEstimulos;
        ProgramasEstimulosTipos programasEstimulosTipos;
        DTOProgramasEstimulos dTOProgramasEstimulos;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        int des=0;
        
        try{
        if (primeraHoja.getSheetName().equals("Programas de Estímulos")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((fila.getCell(1).getNumericCellValue()!= 0)) {
                personal = new Personal();
                programasEstimulos = new ProgramasEstimulos();
                programasEstimulosTipos = new ProgramasEstimulosTipos();
                dTOProgramasEstimulos = new DTOProgramasEstimulos();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        personal.setNombre(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(1).getCellTypeEnum()) {
                    case FORMULA:
                        personal.setClave((int) fila.getCell(1).getNumericCellValue());
                        programasEstimulos.setTrabajador(personal.getClave());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case FORMULA:
                        dTOProgramasEstimulos.setPuesto(fila.getCell(2).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case FORMULA:
                        dTOProgramasEstimulos.setArea(fila.getCell(3).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case STRING:
                        programasEstimulosTipos.setDescripcion(fila.getCell(4).getStringCellValue());
                        dTOProgramasEstimulos.setPuesto(fila.getCell(4).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case FORMULA: 
                        programasEstimulosTipos.setTipoPrograma((short)fila.getCell(5).getNumericCellValue());
                        programasEstimulos.setTipoPrograma(programasEstimulosTipos);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case STRING: 
                        programasEstimulos.setDescripcion(fila.getCell(6).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(8).getCellTypeEnum()) {
                    case FORMULA: 
                        programasEstimulos.setMonto((double)fila.getCell(8).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(9).getCellTypeEnum()) {
                    case NUMERIC: 
                        if (DateUtil.isCellDateFormatted(fila.getCell(9))) {
                         programasEstimulos.setFechaInicio(fila.getCell(9).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(10).getCellTypeEnum()) {
                    case NUMERIC: 
                        if (DateUtil.isCellDateFormatted(fila.getCell(10))) {
                         programasEstimulos.setFechaTermino(fila.getCell(10).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(12).getCellTypeEnum()) {
                    case FORMULA:
                        des= (int) fila.getCell(12).getNumericCellValue();
                        break;
                    default:
                        break;
                }
                    if (des == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Descripción en la columna: <b>" + (5 + 1) + " y fila: " + (i + 1) + "</b> \n");
                    }
                    dTOProgramasEstimulos.setPersonal(personal);
                    dTOProgramasEstimulos.setProgramasEstimulos(programasEstimulos);
                    listaDtoProgEst.add(dTOProgramasEstimulos);
                    
                }
            }
             libroRegistro.close();
            if (validarCelda.contains(false)) {
                    Messages.addGlobalError("<b>La hoja de registros de Programas de Estímulos contiene datos que no son válidos, verifique los datos de la plantilla</b>");
                    Messages.addGlobalError(datosInvalidos.toString());
                    excel.delete();
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                    return Collections.EMPTY_LIST;
                } else {
                    Messages.addGlobalInfo("<b>Hoja de Programas de Estímulos Validada favor de verificar sus datos antes de guardar su información</b>");
                    return listaDtoProgEst;
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
    public void guardaProgramasEstimulos(List<DTOProgramasEstimulos> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            
            List<String> listaCondicional = new ArrayList<>();
            lista.forEach((estimulos) -> {
            f.setEntityClass(ProgramasEstimulos.class);
            ProgramasEstimulos progEstEncontrado = getRegistroProgramasEstimulos(estimulos.getProgramasEstimulos());
            Boolean registroAlmacenado = false;
            if (progEstEncontrado != null) {
                listaCondicional.add(estimulos.getPersonal().getNombre()+ " - " + estimulos.getProgramasEstimulos().getTipoPrograma().getDescripcion());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                    
                Date fI = progEstEncontrado.getFechaInicio();
                LocalDate fecIni = fI.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String fecha = fecIni.format(formatter);
                
                if (ejbModulos.validaEventoRegistro(ejbModulos.getEventoRegistro(), progEstEncontrado.getRegistros().getEventoRegistro().getEventoRegistro())) {
                    estimulos.getProgramasEstimulos().setRegistro(progEstEncontrado.getRegistro());
                    f.edit(estimulos.getProgramasEstimulos());
                    Messages.addGlobalInfo("<b>Se actualizaron los registros del trabajador con clave </b> " + progEstEncontrado.getTrabajador() + "<b> y fecha: </b>" + fecha);
                    } else {
                        Messages.addGlobalWarn("<b>No se pueden actualizar los registros del trabajador con clave </b> " + progEstEncontrado.getTrabajador()+ "<b> y fecha: </b>" + fecha);
                    }
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                estimulos.getProgramasEstimulos().setRegistro(registro.getRegistro());
                f.create(estimulos.getProgramasEstimulos());
                Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b> ");
            }
            f.flush();
        });
    }

    @Override
    public ProgramasEstimulos getRegistroProgramasEstimulos(ProgramasEstimulos programasEstimulos) {
        TypedQuery<ProgramasEstimulos> query = f.getEntityManager().createQuery("SELECT p FROM ProgramasEstimulos p JOIN p.tipoPrograma t WHERE p.trabajador = :trabajador AND t.tipoPrograma = :tipoPrograma AND p.fechaInicio = :fechaInicio",  ProgramasEstimulos.class);
        query.setParameter("trabajador",programasEstimulos.getTrabajador());
        query.setParameter("tipoPrograma", programasEstimulos.getTipoPrograma().getTipoPrograma());
        query.setParameter("fechaInicio", programasEstimulos.getFechaInicio());
        try {
            programasEstimulos = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            programasEstimulos = null;
        }
        return programasEstimulos;
    }

    @Override
    public List<ProgramasEstimulosTipos> getProgramasEstimulosTiposAct() {
        List<ProgramasEstimulosTipos> genLst = new ArrayList<>();
        TypedQuery<ProgramasEstimulosTipos> query = f.getEntityManager().createQuery("SELECT p FROM ProgramasEstimulosTipos p", ProgramasEstimulosTipos.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }

    @Override
    public List<DTOProgramasEstimulos> getRegistroDTOProgramasEstimulos(String mes, Short ejercicio) {
        List<DTOProgramasEstimulos> ldto = new ArrayList<>();
        TypedQuery<ProgramasEstimulos> q = f.getEntityManager()
                .createQuery("SELECT p from ProgramasEstimulos p WHERE p.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND p.registros.eventoRegistro.mes = :mes AND p.registros.area = :area", ProgramasEstimulos.class);
        q.setParameter("mes", mes);
        q.setParameter("ejercicio", ejercicio);
        q.setParameter("area", controladorEmpleado.getNuevaAreasUniversidad().getArea());
        List<ProgramasEstimulos> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            TypedQuery<EventosRegistros> query = f.getEntityManager().createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            l.forEach(x -> {
                Registros registro = f.getEntityManager().find(Registros.class, x.getRegistro());
                EventosRegistros eventos = f.getEntityManager().find(EventosRegistros.class,registro.getEventoRegistro().getEventoRegistro());
                Personal personal = f.getEntityManager().find(Personal.class, x.getTrabajador());
                ProgramasEstimulosTipos programasEstimulosTipos = f.getEntityManager().find(ProgramasEstimulosTipos.class, x.getTipoPrograma().getTipoPrograma());
                AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, personal.getAreaOperativa());
                ActividadesPoa a = registro.getActividadesPoaList().isEmpty() ? null :registro.getActividadesPoaList().get(0);
                DTOProgramasEstimulos dto;
                if (eventoRegistro.equals(registro.getEventoRegistro())) {
                    dto = new DTOProgramasEstimulos(x, personal, programasEstimulosTipos, au, a, eventos, personal.getActividad().getNombre(), au.getNombre());
                } else {
                    dto = new DTOProgramasEstimulos(x, personal, programasEstimulosTipos, au, a, eventos, personal.getActividad().getNombre(), au.getNombre());
                }
                ldto.add(dto);
            });
            return ldto;
    }
    }

    @Override
    public List<DTOProgramasEstimulos> getRegistroReporteProgEst() {
         List<DTOProgramasEstimulos> ldto = new ArrayList<>();
        TypedQuery<ProgramasEstimulos> q = f.getEntityManager()
                .createQuery("SELECT p from ProgramasEstimulos p", ProgramasEstimulos.class);
        List<ProgramasEstimulos> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            TypedQuery<EventosRegistros> query = f.getEntityManager().createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            l.forEach(x -> {
                Registros registro = f.getEntityManager().find(Registros.class, x.getRegistro());
                EventosRegistros eventos = f.getEntityManager().find(EventosRegistros.class,registro.getEventoRegistro().getEventoRegistro());
                Personal personal = f.getEntityManager().find(Personal.class, x.getTrabajador());
                ProgramasEstimulosTipos programasEstimulosTipos = f.getEntityManager().find(ProgramasEstimulosTipos.class, x.getTipoPrograma().getTipoPrograma());
                AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, personal.getAreaOperativa());
                ActividadesPoa a = registro.getActividadesPoaList().isEmpty() ? null :registro.getActividadesPoaList().get(0);
                DTOProgramasEstimulos dto;
                if (eventoRegistro.equals(registro.getEventoRegistro())) {
                    dto = new DTOProgramasEstimulos(x, personal, programasEstimulosTipos, au, a, eventos, personal.getActividad().getNombre(), au.getNombre());
                } else {
                    dto = new DTOProgramasEstimulos(x, personal, programasEstimulosTipos, au, a, eventos, personal.getActividad().getNombre(), au.getNombre());
                }
                ldto.add(dto);
            });
            return ldto;
    }
    }
}
