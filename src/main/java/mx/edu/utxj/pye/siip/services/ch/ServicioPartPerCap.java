/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ch;

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
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.PersonalCapacitado;
import mx.edu.utxj.pye.sgi.entity.pye2.ParticipantesPersonalCapacitado;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.caphum.DTOPerCapParticipantes;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbPersonalCapacitado;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbPartPerCap;
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
public class ServicioPartPerCap implements EjbPartPerCap{
    
    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
    @EJB EjbPersonalCapacitado ejbPersonalCapacitado;
    @Inject Caster caster; 
    @Inject ControladorEmpleado controladorEmpleado;
   
    @Override
    public List<DTOPerCapParticipantes> getListaPartPerCap(String rutaArchivo) throws Throwable {
        
        List<DTOPerCapParticipantes> listaDtoPerCapPart = new ArrayList<>();
        Personal personal;
        PersonalCapacitado personalCapacitado;
        ParticipantesPersonalCapacitado participantesPersonalCapacitado;
        DTOPerCapParticipantes dTOPerCapParticipantes;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(1);
        XSSFRow fila;
        
        if (primeraHoja.getSheetName().equals("Personal Capacitado")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((!"".equals(fila.getCell(1).getStringCellValue()))) {
                personal = new Personal();
                personalCapacitado = new PersonalCapacitado();
                participantesPersonalCapacitado = new ParticipantesPersonalCapacitado();
                dTOPerCapParticipantes = new DTOPerCapParticipantes();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        personalCapacitado.setCurso(fila.getCell(0).getStringCellValue());
                        participantesPersonalCapacitado.setPercap(personalCapacitado);
                        break;
                    default:
                        break;
                }
//                switch (fila.getCell(1).getCellTypeEnum()) {
//                    case FORMULA:
//                        dTOPerCapParticipantes.setCursoFecha(fila.getCell(1).getStringCellValue());
//                        break;
//                    default:
//                        break;
//                }
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
                        participantesPersonalCapacitado.setPersonal(personal.getClave());
                        break;
                    default:
                        break;
                }
                    dTOPerCapParticipantes.setPersonal(personal);
                    dTOPerCapParticipantes.setParticipantesPersonalCapacitado(participantesPersonalCapacitado);
                    listaDtoPerCapPart.add(dTOPerCapParticipantes);
          
                }
            }
            libroRegistro.close();
            Messages.addGlobalInfo("<b>Hoja de Participantes de Personal Capacitado Validada favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaDtoPerCapPart;
    }

    @Override
    public void guardaPartPerCap(List<DTOPerCapParticipantes> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
            
            lista.forEach((participantes) -> {

            PersonalCapacitado personalCapacitado = ejbPersonalCapacitado.getRegistroPersonalCapacitado(participantes.getParticipantesPersonalCapacitado().getPercap().getCurso());
            if (personalCapacitado == null || personalCapacitado.getCurso().isEmpty()) {
                Messages.addGlobalWarn("<b>No existe la Clave de la Capacitación</b>");

            } else {
                if (ejbModulos.validaPeriodoRegistro(ejbModulos.getPeriodoEscolarActual(), personalCapacitado.getPeriodo())) {
                    List<String> listaCondicional = new ArrayList<>();
                    f.setEntityClass(ParticipantesPersonalCapacitado.class);
                    ParticipantesPersonalCapacitado partEncontrado = getRegistroParticipantesPersonalCapacitado(participantes.getParticipantesPersonalCapacitado());
                    Boolean registroAlmacenado = false;
                    if (partEncontrado != null) {
                        listaCondicional.add(partEncontrado.getPercap().getCurso() + " " + partEncontrado.getPersonal());
                        registroAlmacenado = true;
                    }
                    if (registroAlmacenado) {
                        participantes.getParticipantesPersonalCapacitado().setRegistro(partEncontrado.getRegistro());
                        participantes.getParticipantesPersonalCapacitado().getPercap().setRegistro(ejbPersonalCapacitado.getRegistroPersonalCapacitadoEspecifico(participantes.getParticipantesPersonalCapacitado().getPercap().getCurso()));
                        f.edit(participantes.getParticipantesPersonalCapacitado());
                        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
                    } else {
                        Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                        participantes.getParticipantesPersonalCapacitado().setRegistro(registro.getRegistro());
                        participantes.getParticipantesPersonalCapacitado().getPercap().setRegistro(ejbPersonalCapacitado.getRegistroPersonalCapacitadoEspecifico(participantes.getParticipantesPersonalCapacitado().getPercap().getCurso()));
                        f.create(participantes.getParticipantesPersonalCapacitado());
                        Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b>");
                    }
                    f.flush();
                } else {

                    Messages.addGlobalWarn("<b>No puede registrar información de periodos anteriores</b>");
                }
            }
        });
                
          
    }

    @Override
    public ParticipantesPersonalCapacitado getRegistroParticipantesPersonalCapacitado(ParticipantesPersonalCapacitado participantesPersonalCapacitado) {
        TypedQuery<ParticipantesPersonalCapacitado> query = f.getEntityManager().createQuery("SELECT p FROM ParticipantesPersonalCapacitado p INNER JOIN p.percap c WHERE c.curso=:percap AND p.personal=:personal", ParticipantesPersonalCapacitado.class);
        query.setParameter("percap", participantesPersonalCapacitado.getPercap().getCurso());
        query.setParameter("personal", participantesPersonalCapacitado.getPersonal());
        try {
            participantesPersonalCapacitado = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            participantesPersonalCapacitado = null;
            ex.toString();
        }
        return participantesPersonalCapacitado; 
    }
}
