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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesFormacionIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ParticipantesActividadesFormacionIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.pye.DTOParticipantesActFormInt;
import mx.edu.utxj.pye.siip.entity.pye.list.ListaPartActFormInt;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbActFormacionIntegral;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbPartFormInt;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbMatriculaPeriodosEscolares;
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
public class ServicioPartActFormInt implements EjbPartFormInt{
    @EJB
    Facade facdepye;
    @EJB
    EjbModulos ejbModulos;
    @EJB
    EjbMatriculaPeriodosEscolares ejbMatriculaPeriodosEscolares;
    @EJB
    EjbActFormacionIntegral ejbActFormacionIntegral;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    @Override
    public  ListaPartActFormInt getListaPartActFormInt(String rutaArchivo) throws Throwable {
       
        ListaPartActFormInt listaPartActFormInt = new  ListaPartActFormInt();

        List<DTOParticipantesActFormInt> listaDtoParticipantesActFormInt = new ArrayList<>();
        ActividadesFormacionIntegral actividadesFormacionIntegral;
        MatriculaPeriodosEscolares matriculaPeriodosEscolares;
        ParticipantesActividadesFormacionIntegral participantesActividadesFormacionIntegral;
        DTOParticipantesActFormInt dTOParticipantesActFormInt;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(1);
        XSSFRow fila;
        
        String matricula ="";
       
        if (primeraHoja.getSheetName().equals("Participante Formación Integral")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((!"".equals(fila.getCell(2).getNumericCellValue()))) {
                actividadesFormacionIntegral = new ActividadesFormacionIntegral();
                matriculaPeriodosEscolares = new  MatriculaPeriodosEscolares();
                participantesActividadesFormacionIntegral = new ParticipantesActividadesFormacionIntegral();
                dTOParticipantesActFormInt = new DTOParticipantesActFormInt();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        actividadesFormacionIntegral.setActividadFormacionIntegral(fila.getCell(0).getStringCellValue());
                        participantesActividadesFormacionIntegral.setActividadFormacionIntegral(actividadesFormacionIntegral);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(1).getCellTypeEnum()) {
                    case FORMULA: 
                        dTOParticipantesActFormInt.setPeriodoEscolar(fila.getCell(1).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case FORMULA:
                        matriculaPeriodosEscolares.setPeriodo((int) fila.getCell(2).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case NUMERIC: 
                        int num = (int)fila.getCell(3).getNumericCellValue();
                        matricula = Integer.toString(num);
                        matriculaPeriodosEscolares.setMatricula(matricula);
                        participantesActividadesFormacionIntegral.setMatriculaPeriodosEscolares(matriculaPeriodosEscolares);
                        
                        break;
                    case STRING: 
                        matriculaPeriodosEscolares.setMatricula(fila.getCell(3).getStringCellValue());
                        participantesActividadesFormacionIntegral.setMatriculaPeriodosEscolares(matriculaPeriodosEscolares);
                        
                        break;
                    default:
                        break;
                }
                
                    dTOParticipantesActFormInt.setParticipantesActividadesFormacionIntegral(participantesActividadesFormacionIntegral);
                    listaDtoParticipantesActFormInt.add(dTOParticipantesActFormInt);
                }
            }
            listaPartActFormInt.setParticipantesActFormInt(listaDtoParticipantesActFormInt);
            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaPartActFormInt;
    }

    @Override
    public void  guardaPartActFormInt(ListaPartActFormInt listaPartActFormInt, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {

//            listaPartActFormInt.getParticipantesActFormInt().forEach((participantesActFormInt) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//            
//            participantesActFormInt.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(participantesActFormInt.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().getMatricula(), participantesActFormInt.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().getPeriodo()));
//            participantesActFormInt.getParticipantesActividadesFormacionIntegral().getActividadFormacionIntegral().setRegistro(ejbActFormacionIntegral.getRegistroActFormacionIntegralEspecifico(participantesActFormInt.getParticipantesActividadesFormacionIntegral().getActividadFormacionIntegral().getActividadFormacionIntegral()));
//            
//            facdepye.setEntityClass(ParticipantesActividadesFormacionIntegral.class);
//            participantesActFormInt.getParticipantesActividadesFormacionIntegral().setRegistro(registro.getRegistro());
//            facdepye.create(participantesActFormInt.getParticipantesActividadesFormacionIntegral());
//            facdepye.flush();
//        });

            List<String> listaCondicional = new ArrayList<>();
            listaPartActFormInt.getParticipantesActFormInt().forEach((participantesActFormInt) -> {
            facdepye.setEntityClass(ParticipantesActividadesFormacionIntegral.class);
            ParticipantesActividadesFormacionIntegral parActFormIntEncontrada = getRegistroParticipantesActividadesFormacionIntegral(participantesActFormInt.getParticipantesActividadesFormacionIntegral());
            Boolean registroAlmacenado = false;

            if (parActFormIntEncontrada != null) {
                listaCondicional.add(participantesActFormInt.getParticipantesActividadesFormacionIntegral().getActividadFormacionIntegral().getActividadFormacionIntegral()+ " " + participantesActFormInt.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().getMatricula());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                participantesActFormInt.getParticipantesActividadesFormacionIntegral().setRegistro(parActFormIntEncontrada.getRegistro());
                participantesActFormInt.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(participantesActFormInt.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().getMatricula(), participantesActFormInt.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().getPeriodo()));
                participantesActFormInt.getParticipantesActividadesFormacionIntegral().getActividadFormacionIntegral().setRegistro(ejbActFormacionIntegral.getRegistroActFormacionIntegralEspecifico(participantesActFormInt.getParticipantesActividadesFormacionIntegral().getActividadFormacionIntegral().getActividadFormacionIntegral()));
                facdepye.edit(participantesActFormInt.getParticipantesActividadesFormacionIntegral());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                participantesActFormInt.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(participantesActFormInt.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().getMatricula(), participantesActFormInt.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().getPeriodo()));
                participantesActFormInt.getParticipantesActividadesFormacionIntegral().getActividadFormacionIntegral().setRegistro(ejbActFormacionIntegral.getRegistroActFormacionIntegralEspecifico(participantesActFormInt.getParticipantesActividadesFormacionIntegral().getActividadFormacionIntegral().getActividadFormacionIntegral()));
                participantesActFormInt.getParticipantesActividadesFormacionIntegral().setRegistro(registro.getRegistro());
                facdepye.create(participantesActFormInt.getParticipantesActividadesFormacionIntegral());
            }
            facdepye.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }
    
//     @Override
//    public ParticipantesActividadesFormacionIntegral getPartActividadesFormacionIntegral(ParticipantesActividadesFormacionIntegral participantesActividadesFormacionIntegral) {
//        TypedQuery<ParticipantesActividadesFormacionIntegral> query = em.createQuery("SELECT p FROM ParticipantesActividadesFormacionIntegral p JOIN p.actividadFormacionIntegral a JOIN p.matriculaPeriodosEscolares m WHERE a.actividadFormacionIntegral = :actividadFormacionIntegral AND m.matricula = :matricula AND m.periodo = :periodoEscolar", ParticipantesActividadesFormacionIntegral.class);
//        query.setParameter("actividadFormacionIntegral", participantesActividadesFormacionIntegral.getActividadFormacionIntegral().getActividadFormacionIntegral());
//        query.setParameter("matricula", participantesActividadesFormacionIntegral.getMatriculaPeriodosEscolares().getMatricula());
//        query.setParameter("periodoEscolar", participantesActividadesFormacionIntegral.getMatriculaPeriodosEscolares().getPeriodo());
//        try {
//            participantesActividadesFormacionIntegral = query.getSingleResult();
//        } catch (NoResultException | NonUniqueResultException ex) {
//            participantesActividadesFormacionIntegral = null;
//            ex.toString();
//        }
//        return participantesActividadesFormacionIntegral;
//    }

    @Override
    public ParticipantesActividadesFormacionIntegral getRegistroParticipantesActividadesFormacionIntegral(ParticipantesActividadesFormacionIntegral participantesActividadesFormacionIntegral) {
        
        TypedQuery<ParticipantesActividadesFormacionIntegral> query = em.createQuery("SELECT p FROM ParticipantesActividadesFormacionIntegral p JOIN p.actividadFormacionIntegral a JOIN p.matriculaPeriodosEscolares m WHERE m.matricula = :matricula AND m.periodo = :periodoEscolar AND a.actividadFormacionIntegral = :actividadFormacionIntegral",  ParticipantesActividadesFormacionIntegral.class);
        query.setParameter("matricula", participantesActividadesFormacionIntegral.getMatriculaPeriodosEscolares().getMatricula());
        query.setParameter("periodoEscolar", participantesActividadesFormacionIntegral.getMatriculaPeriodosEscolares().getPeriodo());
        query.setParameter("actividadFormacionIntegral", participantesActividadesFormacionIntegral.getActividadFormacionIntegral().getActividadFormacionIntegral());
        try {
            participantesActividadesFormacionIntegral = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            participantesActividadesFormacionIntegral = null;
            System.out.println(ex.toString());
        }
        return participantesActividadesFormacionIntegral;
    }

   
}
