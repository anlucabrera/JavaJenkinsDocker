/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
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
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbActFormacionIntegral;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbPartFormInt;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbMatriculaPeriodosEscolares;
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
public class ServicioPartActFormInt implements EjbPartFormInt{
    @EJB EjbModulos ejbModulos;
    @EJB EjbMatriculaPeriodosEscolares ejbMatriculaPeriodosEscolares;
    @EJB EjbActFormacionIntegral ejbActFormacionIntegral;
    @EJB Facade f;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @Inject Caster caster; 
    @Inject ControladorEmpleado controladorEmpleado;
    
    @Override
    public List<DTOParticipantesActFormInt> getListaPartActFormInt(String rutaArchivo) throws Throwable {
       
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

                if (fila.getCell(4).getNumericCellValue()==1) {
                actividadesFormacionIntegral = new ActividadesFormacionIntegral();
                matriculaPeriodosEscolares = new  MatriculaPeriodosEscolares();
                participantesActividadesFormacionIntegral = new ParticipantesActividadesFormacionIntegral();
                dTOParticipantesActFormInt = new DTOParticipantesActFormInt();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        actividadesFormacionIntegral.setActividadFormacionIntegral(fila.getCell(0).getStringCellValue());
                        participantesActividadesFormacionIntegral.setActividadFormacionIntegral(actividadesFormacionIntegral);
                        break;
                    case FORMULA: 
                        actividadesFormacionIntegral.setActividadFormacionIntegral(fila.getCell(0).getStringCellValue());
                        participantesActividadesFormacionIntegral.setActividadFormacionIntegral(actividadesFormacionIntegral);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case FORMULA:
                        matriculaPeriodosEscolares.setPeriodo((int) fila.getCell(2).getNumericCellValue());
                        break;
                    case NUMERIC:
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
            libroRegistro.close();
            Messages.addGlobalInfo("<b>Hoja de Participantes de Actividades de Formación Integral Validada favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaDtoParticipantesActFormInt;
    }

    @Override
    public void  guardaPartActFormInt(List<DTOParticipantesActFormInt> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
            
            lista.forEach((partActFormInt) -> {
            ActividadesFormacionIntegral actividadesFormacionIntegral = ejbActFormacionIntegral.getRegistroActividadesFormacionIntegral(partActFormInt.getParticipantesActividadesFormacionIntegral().getActividadFormacionIntegral().getActividadFormacionIntegral());
            if (actividadesFormacionIntegral == null || actividadesFormacionIntegral.getActividadFormacionIntegral().isEmpty()) {
                Messages.addGlobalWarn("<b>No existe la Clave de Actividad de Formación Integral</b>");

            } else {
                if (ejbModulos.validaPeriodoRegistro(ejbModulos.getPeriodoEscolarActual(), actividadesFormacionIntegral.getPeriodo())) {
                    List<String> listaCondicional = new ArrayList<>();
                    if (ejbModulos.validaPeriodoRegistro(ejbModulos.getPeriodoEscolarActual(), partActFormInt.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().getPeriodo())) {
                        f.setEntityClass(ParticipantesActividadesFormacionIntegral.class);
                        ParticipantesActividadesFormacionIntegral partAFIEncontrada = getRegistroParticipantesActividadesFormacionIntegral(partActFormInt.getParticipantesActividadesFormacionIntegral());
                        Boolean registroAlmacenado = false;
                        if (partAFIEncontrada != null) {
                            listaCondicional.add(partAFIEncontrada.getActividadFormacionIntegral().getActividadFormacionIntegral() + " " + partAFIEncontrada.getMatriculaPeriodosEscolares().getMatricula());
                            registroAlmacenado = true;
                        }
                        if (registroAlmacenado) {
                            partActFormInt.getParticipantesActividadesFormacionIntegral().setRegistro(partAFIEncontrada.getRegistro());
                            partActFormInt.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(partActFormInt.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().getMatricula(), partActFormInt.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().getPeriodo()));
                            partActFormInt.getParticipantesActividadesFormacionIntegral().getActividadFormacionIntegral().setRegistro(ejbActFormacionIntegral.getRegistroActFormacionIntegralEspecifico(partActFormInt.getParticipantesActividadesFormacionIntegral().getActividadFormacionIntegral().getActividadFormacionIntegral()));
                            f.edit(partActFormInt.getParticipantesActividadesFormacionIntegral());
                            Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
                        } else {
                            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                            partActFormInt.getParticipantesActividadesFormacionIntegral().setRegistro(registro.getRegistro());
                            partActFormInt.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(partActFormInt.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().getMatricula(), partActFormInt.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().getPeriodo()));
                            partActFormInt.getParticipantesActividadesFormacionIntegral().getActividadFormacionIntegral().setRegistro(ejbActFormacionIntegral.getRegistroActFormacionIntegralEspecifico(partActFormInt.getParticipantesActividadesFormacionIntegral().getActividadFormacionIntegral().getActividadFormacionIntegral()));
                            f.create(partActFormInt.getParticipantesActividadesFormacionIntegral());
                            Messages.addGlobalInfo("<b>Se guardaron los registros correctamente</b> ");
                        }
                        f.flush();
                    }
                } else {

                    Messages.addGlobalWarn("<b>No puede registrar información de periodos anteriores</b>");
                }
            }
        });
        
    }
    
    @Override
    public ParticipantesActividadesFormacionIntegral getRegistroParticipantesActividadesFormacionIntegral(ParticipantesActividadesFormacionIntegral participantesActividadesFormacionIntegral) {
        
        TypedQuery<ParticipantesActividadesFormacionIntegral> query = f.getEntityManager().createQuery("SELECT p FROM ParticipantesActividadesFormacionIntegral p JOIN p.actividadFormacionIntegral a JOIN p.matriculaPeriodosEscolares m WHERE m.matricula = :matricula AND m.periodo = :periodoEscolar AND a.actividadFormacionIntegral = :actividadFormacionIntegral",  ParticipantesActividadesFormacionIntegral.class);
        query.setParameter("matricula", participantesActividadesFormacionIntegral.getMatriculaPeriodosEscolares().getMatricula());
        query.setParameter("periodoEscolar", participantesActividadesFormacionIntegral.getMatriculaPeriodosEscolares().getPeriodo());
        query.setParameter("actividadFormacionIntegral", participantesActividadesFormacionIntegral.getActividadFormacionIntegral().getActividadFormacionIntegral());
        try {
            participantesActividadesFormacionIntegral = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            participantesActividadesFormacionIntegral = null;
//            System.out.println(ex.toString());
        }
        return participantesActividadesFormacionIntegral;
    }

    @Override
    public List<ParticipantesActividadesFormacionIntegral> getListaParticipantesPorActividad(String actividad) {
         if(actividad == null){
            return Collections.EMPTY_LIST;
        }
        List<ParticipantesActividadesFormacionIntegral> l = f.getEntityManager().createQuery("SELECT p FROM ParticipantesActividadesFormacionIntegral p JOIN p.actividadFormacionIntegral a WHERE a.actividadFormacionIntegral = :actividadFormacionIntegral", ParticipantesActividadesFormacionIntegral.class)
                .setParameter("actividadFormacionIntegral", actividad)
                .getResultList();
        return l;
    }
}
