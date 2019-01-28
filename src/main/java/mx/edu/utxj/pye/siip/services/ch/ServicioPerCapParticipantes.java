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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
import mx.edu.utxj.pye.siip.entity.caphum.list.ListaPerCapParticipantes;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbPersonalCapacitado;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbPerCapParticipantes;
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
public class ServicioPerCapParticipantes implements EjbPerCapParticipantes{
    
    @EJB
    Facade facadepye;
    @EJB
    EjbModulos ejbModulos;
    @EJB
    EjbPersonalCapacitado ejbPersonalCapacitado;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
   
    @Override
    public ListaPerCapParticipantes getListaPerCapParticipantes(String rutaArchivo) throws Throwable {
       
        ListaPerCapParticipantes listaPerCapParticipantes = new ListaPerCapParticipantes();

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
                switch (fila.getCell(1).getCellTypeEnum()) {
                    case FORMULA:
                        dTOPerCapParticipantes.setCursoFecha(fila.getCell(1).getStringCellValue());
                        break;
                    default:
                        break;
                }
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
            listaPerCapParticipantes.setParticipantes(listaDtoPerCapPart);
            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaPerCapParticipantes;
    }

    @Override
    public void guardaPerCapParticipantes(ListaPerCapParticipantes listaPerCapParticipantes, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//            listaPerCapParticipantes.getParticipantes().forEach((participantes) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//            
//            participantes.getParticipantesPersonalCapacitado().getPercap().setRegistro(ejbPersonalCapacitado.getRegistroPersonalCapacitadoEspecifico(participantes.getParticipantesPersonalCapacitado().getPercap().getCurso()));
//           
//            facadepye.setEntityClass(ParticipantesPersonalCapacitado.class);
//            participantes.getParticipantesPersonalCapacitado().setRegistro(registro.getRegistro());
//            facadepye.create(participantes.getParticipantesPersonalCapacitado());
//            facadepye.flush();
//        });
            List<String> listaCondicional = new ArrayList<>();
            listaPerCapParticipantes.getParticipantes().forEach((participantes) -> {
            try {
                facadepye.setEntityClass(ParticipantesPersonalCapacitado.class);
                ParticipantesPersonalCapacitado parPerCapEncontrado = getRegistroParticipantesPersonalCapacitado(participantes.getParticipantesPersonalCapacitado());
                Boolean registroAlmacenado = false;
                if (parPerCapEncontrado != null) {
                    listaCondicional.add(participantes.getParticipantesPersonalCapacitado().getPercap().getCurso()+ " " + participantes.getPersonal().getNombre());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    participantes.getParticipantesPersonalCapacitado().setRegistro(parPerCapEncontrado.getRegistro());
                    participantes.getParticipantesPersonalCapacitado().getPercap().setRegistro(parPerCapEncontrado.getPercap().getRegistro());
                    facadepye.edit(participantes.getParticipantesPersonalCapacitado());
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    participantes.getParticipantesPersonalCapacitado().getPercap().setRegistro(ejbPersonalCapacitado.getRegistroPersonalCapacitadoEspecifico(participantes.getParticipantesPersonalCapacitado().getPercap().getCurso()));
                    participantes.getParticipantesPersonalCapacitado().setRegistro(registro.getRegistro());
                    facadepye.create(participantes.getParticipantesPersonalCapacitado());
                }
                facadepye.flush();
            } catch (Throwable ex) {
                Logger.getLogger(ServicioPersonalCapacitado.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public ParticipantesPersonalCapacitado getRegistroParticipantesPersonalCapacitado(ParticipantesPersonalCapacitado participantesPersonalCapacitado) {
        TypedQuery<ParticipantesPersonalCapacitado> query = em.createQuery("SELECT p FROM ParticipantesPersonalCapacitado p JOIN p.percap c WHERE c.curso = :percap AND p.personal = :personal", ParticipantesPersonalCapacitado.class);
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
