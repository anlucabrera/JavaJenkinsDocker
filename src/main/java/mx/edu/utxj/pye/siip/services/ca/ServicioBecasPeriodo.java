/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.prontuario.BecaTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.BecasPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.escolar.DTOBecasPeriodosEscolares;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaBecasPeriodo;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbBecasPeriodo;
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
public class ServicioBecasPeriodo implements EjbBecasPeriodo{
    
    @EJB
    Facade facdepye;
    @EJB
    EjbModulos ejbModulos;
    @EJB
    EjbMatriculaPeriodosEscolares ejbMatriculaPeriodosEscolares;
    
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @Override
    public ListaBecasPeriodo getListaBecasPeriodo(String rutaArchivo) throws Throwable {
    
        ListaBecasPeriodo listaBecasPeriodo = new ListaBecasPeriodo();

        List<DTOBecasPeriodosEscolares> listaDtoBecasPeriodo = new ArrayList<>();
        BecaTipos becaTipos;
        MatriculaPeriodosEscolares matriculaPeriodosEscolar;
        BecasPeriodosEscolares becasPeriodosEscolares;
        DTOBecasPeriodosEscolares dTOBecasPeriodosEscolares;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
       
        Integer matricula = 0;
        String matriculaNueva = "000000";
        
        if (primeraHoja.getSheetName().equals("Becas")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

            if ((!"".equals(fila.getCell(0).getStringCellValue()))) {
                becaTipos = new BecaTipos();
                matriculaPeriodosEscolar = new MatriculaPeriodosEscolares();
                becasPeriodosEscolares = new BecasPeriodosEscolares();
                dTOBecasPeriodosEscolares = new DTOBecasPeriodosEscolares();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        dTOBecasPeriodosEscolares.setCicloEscolar(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case STRING:
                        dTOBecasPeriodosEscolares.setPeriodoAsignacion(fila.getCell(2).getStringCellValue());
                        break;
                    default:
                        break;
                }
              
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case FORMULA:
                        matriculaPeriodosEscolar.setPeriodo((int) fila.getCell(3).getNumericCellValue());
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
                            matriculaPeriodosEscolar.setMatricula(matriculaNueva);
                            break;
                        default:
                            break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case STRING:
                        becaTipos.setNombre(fila.getCell(5).getStringCellValue());
                        break;
                    default:
                        break;
                }

                switch (fila.getCell(6).getCellTypeEnum()) {
                    case FORMULA:
                        becaTipos.setBecaTipo((short) fila.getCell(6).getNumericCellValue());
                        becasPeriodosEscolares.setBeca(becaTipos.getBecaTipo());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                    case STRING:
                        becasPeriodosEscolares.setSolicitud(fila.getCell(7).getStringCellValue());
                       
                        break;
                    default:
                        break;
                }
               
                becasPeriodosEscolares.setMatriculaPeriodosEscolares(matriculaPeriodosEscolar);
                dTOBecasPeriodosEscolares.setBecaTipos(becaTipos);
                dTOBecasPeriodosEscolares.setBecasPeriodosEscolares(becasPeriodosEscolares);

                listaDtoBecasPeriodo.add(dTOBecasPeriodosEscolares);
                
            }
         }
            listaBecasPeriodo.setBecasPeriodosEscolares(listaDtoBecasPeriodo);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaBecasPeriodo;
        
    }
    
    
   @Override
    public void guardaBecasPeriodo(ListaBecasPeriodo listaBecasPeriodo, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//            listaBecasPeriodo.getBecasPeriodosEscolares().forEach((becasPeriodosEscolares) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//            
//            becasPeriodosEscolares.getBecasPeriodosEscolares().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(becasPeriodosEscolares.getBecasPeriodosEscolares().getMatriculaPeriodosEscolares().getMatricula(), becasPeriodosEscolares.getBecasPeriodosEscolares().getMatriculaPeriodosEscolares().getPeriodo()));
//            
//            facdepye.setEntityClass(BecasPeriodosEscolares.class);
//            becasPeriodosEscolares.getBecasPeriodosEscolares().setRegistro(registro.getRegistro());
//            facdepye.create(becasPeriodosEscolares.getBecasPeriodosEscolares());
//            facdepye.flush();
//        });
        List<String> listaCondicional = new ArrayList<>();
        listaBecasPeriodo.getBecasPeriodosEscolares().forEach((becasPeriodosEscolares) -> {
            facdepye.setEntityClass(BecasPeriodosEscolares.class);
            BecasPeriodosEscolares becasperescEncontrada = getRegistroBecasPeriodosEscolares(becasPeriodosEscolares.getBecasPeriodosEscolares());
            Boolean registroAlmacenado = false;

            if (becasperescEncontrada != null) {
                listaCondicional.add(becasPeriodosEscolares.getPeriodoAsignacion()+ " " + becasPeriodosEscolares.getBecasPeriodosEscolares().getMatriculaPeriodosEscolares().getMatricula());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                becasPeriodosEscolares.getBecasPeriodosEscolares().setRegistro(becasperescEncontrada.getRegistro());
                becasPeriodosEscolares.getBecasPeriodosEscolares().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(becasPeriodosEscolares.getBecasPeriodosEscolares().getMatriculaPeriodosEscolares().getMatricula(), becasPeriodosEscolares.getBecasPeriodosEscolares().getMatriculaPeriodosEscolares().getPeriodo()));
                facdepye.edit(becasPeriodosEscolares.getBecasPeriodosEscolares());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                becasPeriodosEscolares.getBecasPeriodosEscolares().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(becasPeriodosEscolares.getBecasPeriodosEscolares().getMatriculaPeriodosEscolares().getMatricula(), becasPeriodosEscolares.getBecasPeriodosEscolares().getMatriculaPeriodosEscolares().getPeriodo()));
                becasPeriodosEscolares.getBecasPeriodosEscolares().setRegistro(registro.getRegistro());
                facdepye.create(becasPeriodosEscolares.getBecasPeriodosEscolares());
            }
            facdepye.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public BecasPeriodosEscolares getRegistroBecasPeriodosEscolares(BecasPeriodosEscolares becasPeriodosEscolares) {
        BecasPeriodosEscolares becasperescEncontrada = new BecasPeriodosEscolares();
        TypedQuery<BecasPeriodosEscolares> query = em.createQuery("SELECT b FROM BecasPeriodosEscolares b JOIN b.matriculaPeriodosEscolares m WHERE m.periodo = :periodoAsignacion AND m.matricula = :matricula",  BecasPeriodosEscolares.class);
        query.setParameter("periodoAsignacion", becasPeriodosEscolares.getMatriculaPeriodosEscolares().getPeriodo());
        query.setParameter("matricula", becasPeriodosEscolares.getMatriculaPeriodosEscolares().getMatricula());
        try {
            becasperescEncontrada = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            becasperescEncontrada = null;
            System.out.println(ex.toString());
        }
        return becasperescEncontrada;
    }
}
