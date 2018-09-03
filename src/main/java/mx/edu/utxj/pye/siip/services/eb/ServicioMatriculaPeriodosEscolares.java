/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.eb;

import mx.edu.utxj.pye.sgi.util.ServicioCURP;
import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.prontuario.DTOMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.siip.entity.prontuario.list.ListaMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
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
public class ServicioMatriculaPeriodosEscolares implements EjbMatriculaPeriodosEscolares {

    @EJB
    Facade facadeProntuario;

    @EJB
    EjbModulos ejbModulos;

    @EJB
    EjbAdministracionEncuestas eJBAdministracionEncuestas;

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @Override
    public ListaMatriculaPeriodosEscolares getListaMatriculaPeriodosEscolares(String rutaArchivo) throws Throwable {
        //        Lista General
        ListaMatriculaPeriodosEscolares listaMatriculaPeriodosEscolares = new ListaMatriculaPeriodosEscolares();

        //        Listas para muestra del usuario
        List<DTOMatriculaPeriodosEscolares> dtoMatriculaPeriodosEscolares = new ArrayList<>();
        MatriculaPeriodosEscolares matriculaPeriodoEscolar;
        AreasUniversidad areaUniversidad;
        DTOMatriculaPeriodosEscolares dtoMatriculaPeriodoEscolar;

        //        Utilización y apertura del archivo recibido
        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;

        //        Campos para formato correcto de registros
        Integer matricula = 0;
        String matriculaNueva = "000000";
        List<String> validaciones = new SerializableArrayList<>();
        List<String> listaCondicional = new ArrayList<>();
        if (primeraHoja.getSheetName().equals("Matricula_Periodo_Escolar")) {
            //            Validacion de CURP
            Boolean curpValidadas = true;
            for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                if ((fila.getCell(4).getNumericCellValue() > 0)) {
                    String curp = (String) fila.getCell(14).getStringCellValue();
                    if (ServicioCURP.validaCurp(curp)) {
                    } else {
                        listaCondicional.add(curp);
                        validaciones.add("false");
                    }
                }
            }
            Iterator<String> it = validaciones.iterator();
            while (it.hasNext()) {
                if (it.next().equals("false")) {
                    curpValidadas = false;
                }
            }
            if (curpValidadas) {
//                        Validacion de Campos
                for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                    fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                    if ((fila.getCell(4).getNumericCellValue() > 0)) {
                        matriculaPeriodoEscolar = new MatriculaPeriodosEscolares();
                        areaUniversidad = new AreasUniversidad();
                        dtoMatriculaPeriodoEscolar = new DTOMatriculaPeriodosEscolares();

                        switch (fila.getCell(4).getCellTypeEnum()) {
                            case FORMULA:
                                matriculaPeriodoEscolar.setPeriodo((int) fila.getCell(4).getNumericCellValue());
                                dtoMatriculaPeriodoEscolar.setPeriodo(fila.getCell(5).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(7).getCellTypeEnum()) {
                            case FORMULA:
                                areaUniversidad.setArea((short) fila.getCell(7).getNumericCellValue());
                                areaUniversidad.setNombre(fila.getCell(8).getStringCellValue());
                                matriculaPeriodoEscolar.setProgramaEducativo(areaUniversidad.getArea());
                                dtoMatriculaPeriodoEscolar.setProgramaEducativo(areaUniversidad);
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(9).getCellTypeEnum()) {
                            case NUMERIC:
                                matricula = (int) fila.getCell(9).getNumericCellValue();
                                if (matricula > 20000 && matricula < 99999) {
                                    matriculaNueva = "0" + String.valueOf(matricula);
                                } else {
                                    matriculaNueva = String.valueOf(matricula);
                                }
                                matriculaPeriodoEscolar.setMatricula(matriculaNueva);
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(11).getCellTypeEnum()) {
                            case FORMULA:
                                matriculaPeriodoEscolar.setCuatrimestre(String.valueOf((int) fila.getCell(11).getNumericCellValue()));
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(13).getCellTypeEnum()) {
                            case FORMULA:
                                matriculaPeriodoEscolar.setGrupo(fila.getCell(13).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(14).getCellTypeEnum()) {
                            case STRING:
                                matriculaPeriodoEscolar.setCurp(fila.getCell(14).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        dtoMatriculaPeriodoEscolar.setMatricula(matriculaPeriodoEscolar);

                        dtoMatriculaPeriodosEscolares.add(dtoMatriculaPeriodoEscolar);
                    }
                }
                libroRegistro.close();
                listaMatriculaPeriodosEscolares.setDtoMatriculaPeriodosEscolares(dtoMatriculaPeriodosEscolares);
                addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
            } else {
                libroRegistro.close();
                excel.delete();
                ServicioArchivos.eliminarArchivo(rutaArchivo);
                addDetailMessage("<b>Al menos una CURP no es valida: </b> " + listaCondicional.toString());
            }
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaMatriculaPeriodosEscolares;
    }

    @Override
    public void guardaMatriculaPeriodosEscolares(ListaMatriculaPeriodosEscolares listaMatriculaPeriodosEscolares, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//        List<String> validaciones = new SerializableArrayList<>();
//        List<String> listaCondicional = new ArrayList<>();
//        
//        listaMatriculaPeriodosEscolares.getDtoMatriculaPeriodosEscolares().forEach((matriculaPeriodoEscolar) -> {
//            MatriculaPeriodosEscolares matriculaPE = getRegistroMatriculaPeriodoEscolar(matriculaPeriodoEscolar.getMatricula().getMatricula(), matriculaPeriodoEscolar.getMatricula().getPeriodo());
//            if (matriculaPE != null) {
//                listaCondicional.add(matriculaPeriodoEscolar.getMatricula().getMatricula());
//                validaciones.add("false");
//            }
//        });
//        Boolean registroValido = true;
//        Iterator<String> it = validaciones.iterator();
//        while (it.hasNext()) {
//            if (it.next().equals("false")) {
//                registroValido = false;
//            }
//        }
//        if (registroValido) {
//            listaMatriculaPeriodosEscolares.getDtoMatriculaPeriodosEscolares().forEach((matriculaPeriodoEscolar) -> {
//                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//                facadeProntuario.setEntityClass(MatriculaPeriodosEscolares.class);
//                matriculaPeriodoEscolar.getMatricula().setRegistro(registro.getRegistro());
//                facadeProntuario.create(matriculaPeriodoEscolar.getMatricula());
//                facadeProntuario.flush();
//            });
//        } else {
//            listaMatriculaPeriodosEscolares.getDtoMatriculaPeriodosEscolares().forEach((matriculaPeriodoEscolar) -> {
//                facadeProntuario.setEntityClass(MatriculaPeriodosEscolares.class);
//                matriculaPeriodoEscolar.getMatricula().setRegistro(getRegistroMatriculaEspecifico(matriculaPeriodoEscolar.getMatricula().getMatricula(), matriculaPeriodoEscolar.getMatricula().getPeriodo()));
//                facadeProntuario.edit(matriculaPeriodoEscolar.getMatricula());
//                facadeProntuario.flush();
//            });
//            addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
//        }

        List<String> validaciones = new SerializableArrayList<>();
        List<String> listaCondicional = new ArrayList<>();
        listaMatriculaPeriodosEscolares.getDtoMatriculaPeriodosEscolares().forEach((matriculaPeriodoEscolar) -> {
            if (ejbModulos.validaPeriodoRegistro(eJBAdministracionEncuestas.getPeriodoActual(), matriculaPeriodoEscolar.getMatricula().getPeriodo())) {
                facadeProntuario.setEntityClass(MatriculaPeriodosEscolares.class);
                MatriculaPeriodosEscolares matriculaPE = getRegistroMatriculaPeriodoEscolar(matriculaPeriodoEscolar.getMatricula().getMatricula(), matriculaPeriodoEscolar.getMatricula().getPeriodo());
                Boolean registroAlmacenado = false;
                if (matriculaPE != null) {
                    listaCondicional.add(matriculaPeriodoEscolar.getMatricula().getMatricula());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    matriculaPeriodoEscolar.getMatricula().setRegistro(matriculaPE.getRegistro());
                    facadeProntuario.edit(matriculaPeriodoEscolar.getMatricula());
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    matriculaPeriodoEscolar.getMatricula().setRegistro(registro.getRegistro());
                    facadeProntuario.create(matriculaPeriodoEscolar.getMatricula());
                }
                facadeProntuario.flush();
            }
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public Integer getRegistroMatriculaEspecifico(String matricula, Integer periodo) {
        TypedQuery<MatriculaPeriodosEscolares> query = em.createQuery("SELECT m FROM MatriculaPeriodosEscolares m WHERE m.matricula = :matricula AND m.periodo = :periodo", MatriculaPeriodosEscolares.class);
        query.setParameter("matricula", matricula);
        query.setParameter("periodo", periodo);
        Integer registro = 0;
        try {
            registro = query.getSingleResult().getRegistro();
        } catch (NoResultException | NonUniqueResultException ex) {
            registro = null;
            ex.toString();
        }

        return registro;
    }

    @Override
    public MatriculaPeriodosEscolares getRegistroMatriculaPeriodoEscolar(String matricula, Integer periodo) {
        MatriculaPeriodosEscolares matriculaPeriodoEscolar = new MatriculaPeriodosEscolares();
        TypedQuery<MatriculaPeriodosEscolares> query = em.createQuery("SELECT m FROM MatriculaPeriodosEscolares m WHERE m.matricula = :matricula AND m.periodo = :periodo", MatriculaPeriodosEscolares.class);
        query.setParameter("matricula", matricula);
        query.setParameter("periodo", periodo);
        try {
            matriculaPeriodoEscolar = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            matriculaPeriodoEscolar = null;
            ex.toString();
        }
        return matriculaPeriodoEscolar;
    }
    
    @Override
    public List<MatriculaPeriodosEscolares> getMatriculasVigentes() {
        List<MatriculaPeriodosEscolares> matPerEscLst = new ArrayList<>();
        TypedQuery<MatriculaPeriodosEscolares> query = em.createQuery("SELECT m FROM MatriculaPeriodosEscolares m WHERE m.periodo = :periodo ORDER BY m.matricula ASC", MatriculaPeriodosEscolares.class);
        query.setParameter("periodo", 47);
        try {
            matPerEscLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            matPerEscLst = null;

        }
          return matPerEscLst;
    }

}
