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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadAreasEstudio;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadDisciplinas;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadIntegrantes;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadLineas;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerposAcademicosRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.caphum.DTOCuerpAcadIntegrantes;
import mx.edu.utxj.pye.siip.dto.caphum.DTOCuerposAcademicosR;
import mx.edu.utxj.pye.siip.entity.caphum.list.ListaCuerposAcademicos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbCuerposAcademicos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.apache.poi.ss.usermodel.DateUtil;
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
public class ServicioCuerposAcademicos implements EjbCuerposAcademicos {

    @EJB
    Facade facadeCapitalHumano;
    @EJB
    EjbModulos ejbModulos;

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @Override
    public ListaCuerposAcademicos getListaCuerposAcademicos(String rutaArchivo) throws Throwable {
        ListaCuerposAcademicos listaCuerposAcademicos = new ListaCuerposAcademicos();

        List<DTOCuerposAcademicosR> listaDtoCuerposAcademicosR = new ArrayList<>();
        CuerposAcademicosRegistro cuerpoAcademicoRegistro;
        CuerpacadDisciplinas cuerpacadDisciplina;
        CuerpacadAreasEstudio cuerpacadAreasEstudio;
        AreasUniversidad areasUniversidad;
        DTOCuerposAcademicosR dtoCuerpoAcademicoR;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
        XSSFSheet tercerHoja = libroRegistro.getSheetAt(2);
        XSSFRow fila;

        if ((primeraHoja.getSheetName().equals("Cuerpos_Académicos")) || (segundaHoja.getSheetName().equals("Miembros_Cuerpos_Académicos")) || (tercerHoja.getSheetName().equals("Lineas_Investigación"))) {
            for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                if ((fila.getCell(0).getNumericCellValue() > 0)) {
                    cuerpoAcademicoRegistro = new CuerposAcademicosRegistro();
                    cuerpacadDisciplina = new CuerpacadDisciplinas();
                    cuerpacadAreasEstudio = new CuerpacadAreasEstudio();
                    areasUniversidad = new AreasUniversidad();
                    dtoCuerpoAcademicoR = new DTOCuerposAcademicosR();

//                    Cuerpo Académico
                    switch (fila.getCell(1).getCellTypeEnum()) {
                        case FORMULA:
                            cuerpoAcademicoRegistro.setCuerpoAcademico(fila.getCell(1).getStringCellValue());
                            break;
                        default:
                            break;
                    }
//                    Fecha de inicio
                    switch (fila.getCell(2).getCellTypeEnum()) {
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(fila.getCell(2))) {
                                cuerpoAcademicoRegistro.setFechaInicio(fila.getCell(2).getDateCellValue());
                            }
                            break;
                        default:
                            break;
                    }
//                    Fecha de termino
                    switch (fila.getCell(3).getCellTypeEnum()) {
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(fila.getCell(3))) {
                                cuerpoAcademicoRegistro.setFechaTermino(fila.getCell(3).getDateCellValue());
                            }
                            break;
                        default:
                            break;
                    }
//                    Nombre cuerpo académico
                    switch (fila.getCell(4).getCellTypeEnum()) {
                        case STRING:
                            cuerpoAcademicoRegistro.setNombre(fila.getCell(4).getStringCellValue());
                            break;
                        default:
                            break;
                    }
//                    Nivel de reconocimiento
                    switch (fila.getCell(5).getCellTypeEnum()) {
                        case STRING:
                            cuerpoAcademicoRegistro.setNivelProdep(fila.getCell(5).getStringCellValue());
                            break;
                        default:
                            break;
                    }
//                    Área académica
                    switch (fila.getCell(7).getCellTypeEnum()) {
                        case FORMULA:
                            areasUniversidad.setNombre(fila.getCell(6).getStringCellValue());
                            areasUniversidad.setArea((short) ((int) fila.getCell(7).getNumericCellValue()));
                            cuerpoAcademicoRegistro.setArea(areasUniversidad.getArea());
                            break;
                        default:
                            break;
                    }
//                    Área de estudio
                    switch (fila.getCell(9).getCellTypeEnum()) {
                        case FORMULA:
                            cuerpacadAreasEstudio.setNombre(fila.getCell(8).getStringCellValue());
                            cuerpacadAreasEstudio.setAreaEstudio((short) ((int) fila.getCell(9).getNumericCellValue()));
                            cuerpoAcademicoRegistro.setAreaEstudio(cuerpacadAreasEstudio);
                            break;
                        default:
                            break;
                    }
//                    Disciplina
                    switch (fila.getCell(11).getCellTypeEnum()) {
                        case FORMULA:
                            cuerpacadDisciplina.setNombre(fila.getCell(10).getStringCellValue());
                            cuerpacadDisciplina.setDisciplina((short) ((int) fila.getCell(11).getNumericCellValue()));
                            cuerpoAcademicoRegistro.setDisciplina(cuerpacadDisciplina);
                            break;
                        default:
                            break;
                    }
                    dtoCuerpoAcademicoR.setArea(areasUniversidad);
                    dtoCuerpoAcademicoR.setCuerposAcademicosRegistro(cuerpoAcademicoRegistro);

                    listaDtoCuerposAcademicosR.add(dtoCuerpoAcademicoR);
                }
            }
            listaCuerposAcademicos.setDtoCuerposAcademicosR(listaDtoCuerposAcademicosR);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaCuerposAcademicos;
    }

    @Override
    public ListaCuerposAcademicos getListaCuerpAcadIntegrantes(String rutaArchivo) throws Throwable {
        ListaCuerposAcademicos listaCuerposAcademicos = new ListaCuerposAcademicos();

        List<DTOCuerpAcadIntegrantes> listaDTOCuerpAcadIntegrantes = new ArrayList<>();
        CuerpacadIntegrantes cuerpoacadIntegrante;
        CuerposAcademicosRegistro cuerposAcademicosRegistro;
        Personal personal;
        DTOCuerpAcadIntegrantes dtoCuerpAcadIntegrante;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
        XSSFSheet tercerHoja = libroRegistro.getSheetAt(2);
        XSSFRow fila;

        if ((primeraHoja.getSheetName().equals("Cuerpos_Académicos")) || (segundaHoja.getSheetName().equals("Miembros_Cuerpos_Académicos")) || (tercerHoja.getSheetName().equals("Lineas_Investigación"))) {
            for (int i = 2; i <= segundaHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) segundaHoja.getRow(i);
                if (fila.getCell(2).getNumericCellValue() > 0) {
                    cuerpoacadIntegrante = new CuerpacadIntegrantes();
                    cuerposAcademicosRegistro = new CuerposAcademicosRegistro();
                    personal = new Personal();
                    dtoCuerpAcadIntegrante = new DTOCuerpAcadIntegrantes();

//                    Cuerpo Académico
                    switch (fila.getCell(0).getCellTypeEnum()) {
                        case STRING:
                            cuerposAcademicosRegistro.setCuerpoAcademico(fila.getCell(0).getStringCellValue());
                            break;
                        default:
                            break;
                    }
//                    Personal
                    switch (fila.getCell(1).getCellTypeEnum()) {
                        case STRING:
                            personal.setNombre(fila.getCell(1).getStringCellValue());
                            break;
                        default:
                            break;
                    }
//                    Clave Personal
                    switch (fila.getCell(2).getCellTypeEnum()) {
                        case FORMULA:
                            personal.setClave((int) fila.getCell(2).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    cuerpoacadIntegrante.setCuerpoAcademico(cuerposAcademicosRegistro);
                    cuerpoacadIntegrante.setPersonal(personal.getClave());
                    dtoCuerpAcadIntegrante.setPersonal(personal);
                    dtoCuerpAcadIntegrante.setCuerpoAcademicoIntegrantes(cuerpoacadIntegrante);

                    listaDTOCuerpAcadIntegrantes.add(dtoCuerpAcadIntegrante);
                }
            }
            listaCuerposAcademicos.setDtoCuerpAcadIntegrantes(listaDTOCuerpAcadIntegrantes);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaCuerposAcademicos;
    }

    @Override
    public ListaCuerposAcademicos getListaCuerpAcadLineas(String rutaArchivo) throws Throwable {
        ListaCuerposAcademicos listaCuerposAcademicos = new ListaCuerposAcademicos();

        List<CuerpacadLineas> listaCuerpacadLineas = new ArrayList<>();
        CuerpacadLineas cuerpacadLinea;
        CuerposAcademicosRegistro cuerpoAcademicoRegistro;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
        XSSFSheet tercerHoja = libroRegistro.getSheetAt(2);
        XSSFRow fila;

        if ((primeraHoja.getSheetName().equals("Cuerpos_Académicos")) || (segundaHoja.getSheetName().equals("Miembros_Cuerpos_Académicos")) || (tercerHoja.getSheetName().equals("Lineas_Investigación"))) {
            for (int i = 2; i <= tercerHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) tercerHoja.getRow(i);
                if ((!"".equals(fila.getCell(1).getStringCellValue()))) {
                    cuerpacadLinea = new CuerpacadLineas();
                    cuerpoAcademicoRegistro = new CuerposAcademicosRegistro();

//                    Cuerpo Académico
                    switch (fila.getCell(0).getCellTypeEnum()) {
                        case STRING:
                            cuerpoAcademicoRegistro.setCuerpoAcademico(fila.getCell(0).getStringCellValue());
                            break;
                        default:
                            break;
                    }
//                    Líneas de investigación
                    switch (fila.getCell(1).getCellTypeEnum()) {
                        case STRING:
                            cuerpacadLinea.setNombre(fila.getCell(1).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cuerpacadLinea.setCuerpoAcademico(cuerpoAcademicoRegistro);

                    listaCuerpacadLineas.add(cuerpacadLinea);
                }
            }
            listaCuerposAcademicos.setCuerpAcadLineas(listaCuerpacadLineas);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaCuerposAcademicos;
    }

    @Override
    public void guardaCuerposAcademicos(ListaCuerposAcademicos listaCuerposAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaCuerposAcademicos.getDtoCuerposAcademicosR().forEach((cuerpoAcademicos) -> {
            facadeCapitalHumano.setEntityClass(CuerposAcademicosRegistro.class);
            CuerposAcademicosRegistro cuerpAcadEncontrado = getCuerpoAcademico(cuerpoAcademicos.getCuerposAcademicosRegistro());
            Boolean registroAlmacenado = false;
            if (cuerpAcadEncontrado != null) {
                listaCondicional.add(cuerpoAcademicos.getCuerposAcademicosRegistro().getCuerpoAcademico());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                cuerpoAcademicos.getCuerposAcademicosRegistro().setRegistro(cuerpAcadEncontrado.getRegistro());
                facadeCapitalHumano.edit(cuerpoAcademicos.getCuerposAcademicosRegistro());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                cuerpoAcademicos.getCuerposAcademicosRegistro().setRegistro(registro.getRegistro());
                facadeCapitalHumano.create(cuerpoAcademicos.getCuerposAcademicosRegistro());
            }
            facadeCapitalHumano.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public void guardaCuerpAcadIntegrantes(ListaCuerposAcademicos listaCuerposAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaCuerposAcademicos.getDtoCuerpAcadIntegrantes().forEach((cuerpacadIntegrante) -> {
            try {
                facadeCapitalHumano.setEntityClass(CuerpacadIntegrantes.class);
                CuerpacadIntegrantes cuerpacadIntEncontrado = getCuerpacadIntegrantes(cuerpacadIntegrante.getCuerpoAcademicoIntegrantes());
                Boolean registroAlmacenado = false;
                if (cuerpacadIntEncontrado != null) {
                    listaCondicional.add(cuerpacadIntegrante.getCuerpoAcademicoIntegrantes().getCuerpoAcademico().getCuerpoAcademico() + " " + cuerpacadIntegrante.getPersonal().getNombre());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    cuerpacadIntegrante.getCuerpoAcademicoIntegrantes().setRegistro(cuerpacadIntEncontrado.getRegistro());
                    cuerpacadIntegrante.getCuerpoAcademicoIntegrantes().getCuerpoAcademico().setRegistro(cuerpacadIntEncontrado.getCuerpoAcademico().getRegistro());
                    facadeCapitalHumano.edit(cuerpacadIntegrante.getCuerpoAcademicoIntegrantes());
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    cuerpacadIntegrante.getCuerpoAcademicoIntegrantes().getCuerpoAcademico().setRegistro(getRegistroCuerpoAcademicoEspecifico(cuerpacadIntegrante.getCuerpoAcademicoIntegrantes().getCuerpoAcademico().getCuerpoAcademico()));
                    cuerpacadIntegrante.getCuerpoAcademicoIntegrantes().setRegistro(registro.getRegistro());
                    facadeCapitalHumano.create(cuerpacadIntegrante.getCuerpoAcademicoIntegrantes());
                }
                facadeCapitalHumano.flush();
            } catch (Throwable ex) {
                Logger.getLogger(ServicioCuerposAcademicos.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public void guardaCuerpAcadLineas(ListaCuerposAcademicos listaCuerposAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaCuerposAcademicos.getCuerpAcadLineas().forEach((cuerpoacadLinea) -> {
            try {
                facadeCapitalHumano.setEntityClass(CuerpacadLineas.class);
                CuerpacadLineas cuerpacadLEncontrado = getCuerpacadLineas(cuerpoacadLinea);
                Boolean registroAlmacenado = false;
                if (cuerpacadLEncontrado != null) {
                    listaCondicional.add(cuerpoacadLinea.getCuerpoAcademico().getCuerpoAcademico() + " " + cuerpacadLEncontrado.getNombre());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    cuerpoacadLinea.setRegistro(cuerpacadLEncontrado.getRegistro());
                    cuerpoacadLinea.getCuerpoAcademico().setRegistro(cuerpacadLEncontrado.getCuerpoAcademico().getRegistro());
                    facadeCapitalHumano.edit(cuerpoacadLinea);
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    cuerpoacadLinea.getCuerpoAcademico().setRegistro(getRegistroCuerpoAcademicoEspecifico(cuerpoacadLinea.getCuerpoAcademico().getCuerpoAcademico()));
                    cuerpoacadLinea.setRegistro(registro.getRegistro());
                    facadeCapitalHumano.create(cuerpoacadLinea);
                }
                facadeCapitalHumano.flush();
            } catch (Throwable ex) {
                Logger.getLogger(ServicioCuerposAcademicos.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public Integer getRegistroCuerpoAcademicoEspecifico(String cuerpoAcademico) {
//        TypedQuery<CuerposAcademicosRegistro> query = em.createNamedQuery("CuerposAcademicosRegistro.findByCuerpoAcademico", CuerposAcademicosRegistro.class);
//        query.setParameter("cuerpoAcademico", cuerpoAcademico);
//        Integer registro = query.getSingleResult().getRegistro();
//        return registro;
        TypedQuery<CuerposAcademicosRegistro> query = em.createQuery("SELECT c FROM CuerposAcademicosRegistro c WHERE c.cuerpoAcademico = :cuerpoAcademico", CuerposAcademicosRegistro.class);
        query.setParameter("cuerpoAcademico", cuerpoAcademico);
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
    public CuerposAcademicosRegistro getCuerpoAcademico(CuerposAcademicosRegistro cuerpoAcademico) {
        TypedQuery<CuerposAcademicosRegistro> query = em.createNamedQuery("CuerposAcademicosRegistro.findByCuerpoAcademico", CuerposAcademicosRegistro.class);
        query.setParameter("cuerpoAcademico", cuerpoAcademico.getCuerpoAcademico());
        try {
            cuerpoAcademico = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            cuerpoAcademico = null;
            ex.toString();
        }
        return cuerpoAcademico;
    }

    @Override
    public CuerpacadIntegrantes getCuerpacadIntegrantes(CuerpacadIntegrantes cuerpacadIntegrante) {
        TypedQuery<CuerpacadIntegrantes> query = em.createQuery("SELECT c FROM CuerpacadIntegrantes c JOIN c.cuerpoAcademico ca WHERE ca.cuerpoAcademico = :cuerpoAcademico AND c.personal = :personal", CuerpacadIntegrantes.class);
        query.setParameter("cuerpoAcademico", cuerpacadIntegrante.getCuerpoAcademico().getCuerpoAcademico());
        query.setParameter("personal", cuerpacadIntegrante.getPersonal());
        try {
            cuerpacadIntegrante = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            cuerpacadIntegrante = null;
            ex.toString();
        }
        return cuerpacadIntegrante;
    }

    @Override
    public CuerpacadLineas getCuerpacadLineas(CuerpacadLineas cuerpacadLinea) {
        TypedQuery<CuerpacadLineas> query = em.createQuery("SELECT c FROM CuerpacadLineas c JOIN c.cuerpoAcademico ca WHERE ca.cuerpoAcademico = :cuerpoAcademico AND c.nombre = :nombre", CuerpacadLineas.class);
        query.setParameter("cuerpoAcademico", cuerpacadLinea.getCuerpoAcademico().getCuerpoAcademico());
        query.setParameter("nombre", cuerpacadLinea.getNombre());
        try {
            cuerpacadLinea = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            cuerpacadLinea = null;
            ex.toString();
        }
        return cuerpacadLinea;
    }

    @Override
    public List<CuerpacadDisciplinas> getCuerpacadDisciplinas() {
        List<CuerpacadDisciplinas> cuerpacadDisciplinas = new ArrayList<>();
        TypedQuery<CuerpacadDisciplinas> query = em.createQuery("SELECT c FROM CuerpacadDisciplinas c ORDER BY c.nombre", CuerpacadDisciplinas.class);
        try {
            cuerpacadDisciplinas = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            cuerpacadDisciplinas = null;
        }
        return cuerpacadDisciplinas;
    }

    @Override
    public List<CuerpacadAreasEstudio> getCuerpacadAreasEstudio() {
        List<CuerpacadAreasEstudio> cuerpacadAreasEstudios = new ArrayList<>();
        TypedQuery<CuerpacadAreasEstudio> query = em.createQuery("SELECT c FROM CuerpacadAreasEstudio c ORDER BY c.nombre" ,CuerpacadAreasEstudio.class);
        try {
            cuerpacadAreasEstudios = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            cuerpacadAreasEstudios = null;
        }
        return cuerpacadAreasEstudios;
    }
    
    @Override
    public List<CuerposAcademicosRegistro> getCuerposAcademicosAct() {
        List<CuerposAcademicosRegistro> genLst = new ArrayList<>();
        TypedQuery<CuerposAcademicosRegistro> query = em.createQuery("SELECT c FROM CuerposAcademicosRegistro c", CuerposAcademicosRegistro.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }
}
