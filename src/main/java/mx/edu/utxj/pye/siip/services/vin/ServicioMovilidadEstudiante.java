/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

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
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosMovilidad;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistroMovilidadEstudiante;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOMovilidadEstudiante;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbRegistroMovilidad;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbMovilidadEstudiante;
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
public class ServicioMovilidadEstudiante implements EjbMovilidadEstudiante{
    
    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
    @EJB EjbRegistroMovilidad ejbRegistroMovilidad;
    @EJB EjbMatriculaPeriodosEscolares ejbMatriculaPeriodosEscolares;
    @EJB EjbPropiedades ep;
    @Inject Caster caster;
    @Inject ControladorEmpleado controladorEmpleado;
  
    @Override
    public List<DTOMovilidadEstudiante> getListaMovilidadEstudiante(String rutaArchivo) throws Throwable {
       
       
        List<DTOMovilidadEstudiante> listaDtoMovilidadEstudiante = new ArrayList<>();
        MatriculaPeriodosEscolares matriculaPeriodosEscolares;
        RegistrosMovilidad registrosMovilidad;
        RegistroMovilidadEstudiante registroMovilidadEstudiante;
        DTOMovilidadEstudiante dTOMovilidadEstudiante;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(1);
        XSSFRow fila;
        
        Integer matricula = 0;
        String matriculaNueva = "000000";
               
        if (primeraHoja.getSheetName().equals("Movilidad Estudiante")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((!"".equals(fila.getCell(0).getStringCellValue()))) {
                matriculaPeriodosEscolares =  new MatriculaPeriodosEscolares();
                registrosMovilidad = new  RegistrosMovilidad();
                registroMovilidadEstudiante = new  RegistroMovilidadEstudiante();
                dTOMovilidadEstudiante = new DTOMovilidadEstudiante();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        registrosMovilidad.setRegistroMovilidad(fila.getCell(0).getStringCellValue());
                        registroMovilidadEstudiante.setRegistroMovilidad(registrosMovilidad);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(1).getCellTypeEnum()) {
                    case FORMULA: 
//                        dTOMovilidadEstudiante.setParticipanteProyecto(fila.getCell(1).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case NUMERIC:
                        matricula = (int) fila.getCell(4).getNumericCellValue();
                            if (matricula > 20000 && matricula < 99999) {
                                matriculaNueva = "0" + String.valueOf(matricula);
                            } else {
                                matriculaNueva = String.valueOf(matricula);
                            }
                            matriculaPeriodosEscolares.setMatricula(matriculaNueva);
                        break;
                    case STRING:
                        matriculaPeriodosEscolares.setMatricula(fila.getCell(4).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case FORMULA: 
                        matriculaPeriodosEscolares.setPeriodo((int)fila.getCell(5).getNumericCellValue());
                        registroMovilidadEstudiante.setMatriculaPeriodosEscolares(matriculaPeriodosEscolares);
                        break;
                    default:
                        break;
                }
                    dTOMovilidadEstudiante.setRegistroMovilidadEstudiante(registroMovilidadEstudiante);
                    listaDtoMovilidadEstudiante.add(dTOMovilidadEstudiante);
                    
                }
            }
            libroRegistro.close();
            Messages.addGlobalInfo("<b>Hoja de Movilidad Estudiantil Validada favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaDtoMovilidadEstudiante;
    }

    @Override
    public void guardaMovilidadEstudiante(List<DTOMovilidadEstudiante> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {

            lista.forEach((movEst) -> {

            RegistrosMovilidad registrosMovilidad = ejbRegistroMovilidad.getRegistrosMovilidad(movEst.getRegistroMovilidadEstudiante().getRegistroMovilidad());
            if (registrosMovilidad == null || registrosMovilidad.getRegistroMovilidad().isEmpty()) {
                Messages.addGlobalWarn("<b>No existe la Clave del Registro de Movilidad</b>");

            } else {
                if (ejbModulos.validaPeriodoRegistro(ejbModulos.getPeriodoEscolarActual(), registrosMovilidad.getPeriodoEscolarCursado())) {

                    List<String> listaCondicional = new ArrayList<>();
                    f.setEntityClass(RegistroMovilidadEstudiante.class);
                    RegistroMovilidadEstudiante movEstEncontrada = getRegistroMovilidadEstudiante(movEst.getRegistroMovilidadEstudiante());
                    Boolean registroAlmacenado = false;
                    if (movEstEncontrada != null) {
                        listaCondicional.add(movEstEncontrada.getRegistroMovilidad().getRegistroMovilidad() + " " + movEstEncontrada.getMatriculaPeriodosEscolares().getMatricula());
                        registroAlmacenado = true;
                    }
                    if (registroAlmacenado) {
                        movEst.getRegistroMovilidadEstudiante().setRegistro(movEstEncontrada.getRegistro());
                        movEst.getRegistroMovilidadEstudiante().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(movEst.getRegistroMovilidadEstudiante().getMatriculaPeriodosEscolares().getMatricula(), movEst.getRegistroMovilidadEstudiante().getMatriculaPeriodosEscolares().getPeriodo()));
                        movEst.getRegistroMovilidadEstudiante().getRegistroMovilidad().setRegistro(ejbRegistroMovilidad.getRegistroMovilidadEspecifico(movEst.getRegistroMovilidadEstudiante().getRegistroMovilidad().getRegistroMovilidad()));
                        f.edit(movEst.getRegistroMovilidadEstudiante());
                        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
                    } else {
                        Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                        movEst.getRegistroMovilidadEstudiante().setRegistro(registro.getRegistro());
                        movEst.getRegistroMovilidadEstudiante().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(movEst.getRegistroMovilidadEstudiante().getMatriculaPeriodosEscolares().getMatricula(), movEst.getRegistroMovilidadEstudiante().getMatriculaPeriodosEscolares().getPeriodo()));
                        movEst.getRegistroMovilidadEstudiante().getRegistroMovilidad().setRegistro(ejbRegistroMovilidad.getRegistroMovilidadEspecifico(movEst.getRegistroMovilidadEstudiante().getRegistroMovilidad().getRegistroMovilidad()));
                        f.create(movEst.getRegistroMovilidadEstudiante());
                        Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b> ");
                    }
                    f.flush();
                } else {

                    Messages.addGlobalWarn("<b>No puede registrar información de periodos anteriores</b>");
                }
            }
        });
    }

    @Override
    public RegistroMovilidadEstudiante getRegistroMovilidadEstudiante(RegistroMovilidadEstudiante registroMovilidadEstudiante) {
        TypedQuery<RegistroMovilidadEstudiante> query = f.getEntityManager().createQuery("SELECT r FROM RegistroMovilidadEstudiante r JOIN r.registroMovilidad mo JOIN r.matriculaPeriodosEscolares m WHERE mo.registroMovilidad = :registroMovilidad AND m.matricula = :matricula AND m.periodo = :periodoEscolar",  RegistroMovilidadEstudiante.class);
        query.setParameter("registroMovilidad", registroMovilidadEstudiante.getRegistroMovilidad().getRegistroMovilidad());
        query.setParameter("matricula", registroMovilidadEstudiante.getMatriculaPeriodosEscolares().getMatricula());
        query.setParameter("periodoEscolar", registroMovilidadEstudiante.getMatriculaPeriodosEscolares().getPeriodo());
        try {
            registroMovilidadEstudiante = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            registroMovilidadEstudiante = null;
//            System.out.println(ex.toString());
        }
        return registroMovilidadEstudiante;
    }
}
