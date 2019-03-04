/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

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
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasPertcal;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.pye.DTOProgramasPertCalidad;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbProgramasPertCalidad;
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
public class ServicioProgramasPertCalidad implements EjbProgramasPertCalidad{
    
    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @Inject ControladorEmpleado controladorEmpleado;
    
    @Override
    public List<DTOProgramasPertCalidad> getListaProgramasPertCalidad(String rutaArchivo) throws Throwable {
        List<DTOProgramasPertCalidad> listaDtoProgPertCal = new ArrayList<>();
        ProgramasPertcal programasPertcal;
        AreasUniversidad areasUniversidad;
        DTOProgramasPertCalidad dTOProgramasPertCalidad;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        if (primeraHoja.getSheetName().equals("Programas Pertinentes y Calidad")) {
        for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((fila.getCell(3).getNumericCellValue()!= 0)) {
                programasPertcal = new ProgramasPertcal();
                areasUniversidad = new AreasUniversidad();
                dTOProgramasPertCalidad = new DTOProgramasPertCalidad();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        areasUniversidad.setNombre(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                 switch (fila.getCell(2).getCellTypeEnum()) {
                    case FORMULA:
                        areasUniversidad.setArea((short)fila.getCell(2).getNumericCellValue());
                        programasPertcal.setProgramaEducativo(areasUniversidad.getArea());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case NUMERIC:
                        programasPertcal.setAnioInicio((short)fila.getCell(3).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case STRING:
                        programasPertcal.setEvaluable(fila.getCell(4).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case STRING:
                        programasPertcal.setPertinente(fila.getCell(5).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case STRING:
                        programasPertcal.setOrgAcreditador(fila.getCell(6).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(7))) {
                         programasPertcal.setFeciniAcred(fila.getCell(7).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(8).getCellTypeEnum()) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(8))) {
                         programasPertcal.setFecfinAcred(fila.getCell(8).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(9).getCellTypeEnum()) {
                    case NUMERIC:
                        programasPertcal.setAnioEstfac((short)fila.getCell(9).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(10).getCellTypeEnum()) {
                    case NUMERIC:
                        programasPertcal.setAnioUltast((short)fila.getCell(10).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                    dTOProgramasPertCalidad.setAreasUniversidad(areasUniversidad);
                    dTOProgramasPertCalidad.setProgramasPertcal(programasPertcal);
                    listaDtoProgPertCal.add(dTOProgramasPertCalidad);
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
        return listaDtoProgPertCal;
    }

    @Override
    public void guardaProgramasPertCalidad(List<DTOProgramasPertCalidad> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
         List<String> listaCondicional = new ArrayList<>();
            lista.forEach((programas) -> {
            f.setEntityClass(ProgramasPertcal.class);
            ProgramasPertcal progPertCalEncontrado = getRegistroProgramasPertcal(programas.getProgramasPertcal());
            Boolean registroAlmacenado = false;
            if (progPertCalEncontrado != null) {
                listaCondicional.add(programas.getAreasUniversidad().getSiglas()+ " " + programas.getProgramasPertcal().getOrgAcreditador());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                AreasUniversidad programaEducativo = f.getEntityManager().find(AreasUniversidad.class, progPertCalEncontrado.getProgramaEducativo());
                
                if (ejbModulos.validaEventoRegistro(ejbModulos.getEventoRegistro(), progPertCalEncontrado.getRegistros().getEventoRegistro().getEventoRegistro())) {
                    programas.getProgramasPertcal().setRegistro(progPertCalEncontrado.getRegistro());
                    f.edit(programas.getProgramasPertcal());
                    Messages.addGlobalInfo("<b>Se actualizaron los registros del Programa Educativo: </b> " + programaEducativo.getSiglas());
                } else {
                    Messages.addGlobalWarn("<b>No se pueden actualizar los registros del Programa Educativo: </b> " + programaEducativo.getSiglas());
                }
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                programas.getProgramasPertcal().setRegistro(registro.getRegistro());
                f.create(programas.getProgramasPertcal());
                Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b> ");
            }
            f.flush();
        });
    }

    @Override
    public ProgramasPertcal getRegistroProgramasPertcal(ProgramasPertcal programasPertcal) {
        TypedQuery<ProgramasPertcal> query = f.getEntityManager().createQuery("SELECT p FROM ProgramasPertcal p WHERE p.programaEducativo = :programaEducativo AND p.anioInicio = :anioInicio", ProgramasPertcal.class);
        query.setParameter("programaEducativo",programasPertcal.getProgramaEducativo());
        query.setParameter("anioInicio", programasPertcal.getAnioInicio());
        try {
            programasPertcal = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            programasPertcal = null;
        }
        return programasPertcal;
    }

    @Override
    public List<DTOProgramasPertCalidad> getRegistroDTOProgramasPertCalidad(String mes, Short ejercicio) {
        List<DTOProgramasPertCalidad> ldto = new ArrayList<>();
        TypedQuery<ProgramasPertcal> q = f.getEntityManager()
                .createQuery("SELECT p FROM ProgramasPertcal p WHERE p.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND p.registros.eventoRegistro.mes = :mes AND p.registros.area = :area", ProgramasPertcal.class);
        q.setParameter("mes", mes);
        q.setParameter("ejercicio", ejercicio);
        q.setParameter("area", controladorEmpleado.getNuevaAreasUniversidad().getArea());
        List<ProgramasPertcal> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            TypedQuery<EventosRegistros> query = f.getEntityManager().createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            l.forEach(x -> {
                Registros registro = f.getEntityManager().find(Registros.class, x.getRegistro());
                EventosRegistros eventos = f.getEntityManager().find(EventosRegistros.class,registro.getEventoRegistro().getEventoRegistro());
                AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, x.getProgramaEducativo());
                ActividadesPoa a = registro.getActividadesPoaList().isEmpty() ? null :registro.getActividadesPoaList().get(0);
                DTOProgramasPertCalidad dto;
                if (eventoRegistro.equals(registro.getEventoRegistro())) {
                    dto = new DTOProgramasPertCalidad(x, au, a, eventos);
                } else {
                    dto = new DTOProgramasPertCalidad(x, au, a, eventos);
                }
                ldto.add(dto);
            });
            return ldto;
    }
    }
}
