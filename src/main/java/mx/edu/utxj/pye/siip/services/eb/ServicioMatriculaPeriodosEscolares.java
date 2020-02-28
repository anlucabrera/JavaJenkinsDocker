/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.eb;

import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.util.ServicioCURP;
import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.enums.EvidenciaCategoria;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.eb.DTOMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.apache.poi.ss.usermodel.CellType;
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
public class ServicioMatriculaPeriodosEscolares implements EjbMatriculaPeriodosEscolares {
    
    @EJB    EjbModulos          ejbModulos;
    @EJB    Facade              f;
    @EJB    EjbPropiedades      ep;
    @EJB    EjbFiscalizacion    ejbFiscalizacion;

    @Inject Caster      caster;
    @Inject LogonMB     logonMB;
    

    @Override
    public List<DTOMatriculaPeriodosEscolares> getListaMatriculaPeriodosEscolares(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();

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

            try{
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
                    if ((fila.getCell(1).getNumericCellValue() > 0)) {
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

                            if (fila.getCell(4).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(4).getCellTypeEnum()) {
                                    case FORMULA:
                                        matriculaPeriodoEscolar.setPeriodo((int) fila.getCell(4).getNumericCellValue());
                                        dtoMatriculaPeriodoEscolar.setPeriodo(fila.getCell(5).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Periodo Escolar en la columna: " + (5 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(7).getCellTypeEnum() == CellType.FORMULA) {
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
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Programa Educativo en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(9).getCellTypeEnum() == CellType.NUMERIC || fila.getCell(9).getCellTypeEnum() == CellType.STRING) {
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
                                    case STRING:
                                        String maS = (String) fila.getCell(9).getStringCellValue();
                                        if (Integer.parseInt(maS) > 20000 && Integer.parseInt(maS) < 99999) {
                                            matriculaNueva = "0" + String.valueOf(maS);
                                        } else {
                                            matriculaNueva = String.valueOf(maS);
                                        }
                                        matriculaPeriodoEscolar.setMatricula(matriculaNueva);
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Matricula en la columna: " + (9 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(11).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(11).getCellTypeEnum()) {
                                    case FORMULA:
                                        matriculaPeriodoEscolar.setCuatrimestre(String.valueOf((int) fila.getCell(11).getNumericCellValue()));
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Cuatrimestre en la columna: " + (11 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(13).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(13).getCellTypeEnum()) {
                                    case FORMULA:
                                        matriculaPeriodoEscolar.setGrupo(fila.getCell(13).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Grupo en la columna: " + (13 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(14).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(14).getCellTypeEnum()) {
                                    case STRING:
                                        matriculaPeriodoEscolar.setCurp(fila.getCell(14).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: CURP en la columna: " + (14 + 1) + " y fila: " + (i + 1));
                            }
                            
                            if (fila.getCell(16).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(16).getCellTypeEnum()) {
                                    case FORMULA:
                                        matriculaPeriodoEscolar.setLenguaIndigena(fila.getCell(16).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Lengua Indígena en la columna: " + (15 + 1) + " y fila: " + (i + 1));
                            }
                            
                            if (fila.getCell(18).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(18).getCellTypeEnum()) {
                                    case FORMULA:
                                        matriculaPeriodoEscolar.setDiscapacidad(fila.getCell(18).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Discapacidad en la columna: " + (17 + 1) + " y fila: " + (i + 1));
                            }
                            
                            if (fila.getCell(20).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(20).getCellTypeEnum()) {
                                    case FORMULA:
                                        matriculaPeriodoEscolar.setComunidadIndigena(fila.getCell(20).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Comunidad Indígena en la columna: " + (19 + 1) + " y fila: " + (i + 1));
                            }

                            dtoMatriculaPeriodoEscolar.setMatricula(matriculaPeriodoEscolar);

                            dtoMatriculaPeriodosEscolares.add(dtoMatriculaPeriodoEscolar);
                        }
                    }
                    libroRegistro.close();

                    if (validarCelda.contains(false)) {
                        Messages.addGlobalWarn("<b>El archivo cargado contiene datos que no son validos, verifique los datos de la plantilla</b>");
                        Messages.addGlobalWarn(datosInvalidos.toString());

                        excel.delete();
                        ServicioArchivos.eliminarArchivo(rutaArchivo);
                        return Collections.EMPTY_LIST;
                    } else {
                        Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
                        return dtoMatriculaPeriodosEscolares;
                    }

                } else {
                    libroRegistro.close();
                    excel.delete();
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                    Messages.addGlobalWarn("<b>Al menos una CURP no es valida: </b> " + listaCondicional.toString());
                    return Collections.EMPTY_LIST;
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
        } else {
            Messages.addGlobalError("<b>Ocurrio un error en la lectura del archivo</b>");
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public void guardaMatriculaPeriodosEscolares(List<DTOMatriculaPeriodosEscolares> listaMatriculaPeriodosEscolares, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
        List<String> validaciones = new SerializableArrayList<>();
        List<String> listaCondicional = new ArrayList<>();
        listaMatriculaPeriodosEscolares.forEach((matriculaPeriodoEscolar) -> {
            if (ejbModulos.validaPeriodoRegistro(ejbModulos.getPeriodoEscolarActivo(), matriculaPeriodoEscolar.getMatricula().getPeriodo())) {
                f.setEntityClass(MatriculaPeriodosEscolares.class);
                MatriculaPeriodosEscolares matriculaPE = getRegistroMatriculaPeriodoEscolar(matriculaPeriodoEscolar.getMatricula().getMatricula(), matriculaPeriodoEscolar.getMatricula().getPeriodo());
                Boolean registroAlmacenado = false;
                if (matriculaPE != null) {
                    listaCondicional.add(matriculaPeriodoEscolar.getMatricula().getMatricula());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    if (ejbModulos.getPeriodoEscolarActivo().getPeriodo().equals(matriculaPE.getPeriodo())) {
                        matriculaPeriodoEscolar.getMatricula().setRegistro(matriculaPE.getRegistro());
                        f.edit(matriculaPeriodoEscolar.getMatricula());
                    } else {
                        listaCondicional.remove(matriculaPeriodoEscolar.getMatricula().getMatricula());
                    }
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    matriculaPeriodoEscolar.getMatricula().setRegistro(registro.getRegistro());
                    f.create(matriculaPeriodoEscolar.getMatricula());
                }
                f.flush();

            }
        });
        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datosSe actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public Integer getRegistroMatriculaEspecifico(String matricula, Integer periodo) {
        TypedQuery<MatriculaPeriodosEscolares> query = f.getEntityManager().createQuery("SELECT m FROM MatriculaPeriodosEscolares m WHERE m.matricula = :matricula AND m.periodo = :periodo", MatriculaPeriodosEscolares.class);
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
        TypedQuery<MatriculaPeriodosEscolares> query = f.getEntityManager().createQuery("SELECT m FROM MatriculaPeriodosEscolares m WHERE m.matricula = :matricula AND m.periodo = :periodo", MatriculaPeriodosEscolares.class);
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
        TypedQuery<MatriculaPeriodosEscolares> query = f.getEntityManager().createQuery("SELECT m FROM MatriculaPeriodosEscolares m WHERE m.periodo = :periodo ORDER BY m.matricula ASC", MatriculaPeriodosEscolares.class);
        query.setParameter("periodo", ejbModulos.getPeriodoEscolarActivo().getPeriodo());
        try {
            matPerEscLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            matPerEscLst = null;

        }
          return matPerEscLst;
    }
    
    @Override
    public List<DTOMatriculaPeriodosEscolares> getDtoMatriculasVigentes() {
        List<MatriculaPeriodosEscolares> matPerEscLst = new ArrayList<>();
        List<DTOMatriculaPeriodosEscolares> dtoList = new ArrayList<>();
        TypedQuery<MatriculaPeriodosEscolares> query = f.getEntityManager().createQuery("SELECT m FROM MatriculaPeriodosEscolares m WHERE m.periodo = :periodo ORDER BY m.matricula ASC", MatriculaPeriodosEscolares.class);
        query.setParameter("periodo", ejbModulos.getPeriodoEscolarActivo().getPeriodo());
        try {
            matPerEscLst = query.getResultList();
            matPerEscLst.stream().forEach((mpe) -> {
                dtoList.add(new DTOMatriculaPeriodosEscolares(
                        mpe,
                        caster.periodoToString(f.getEntityManager().find(PeriodosEscolares.class, mpe.getPeriodo())),
                        f.getEntityManager().find(AreasUniversidad.class, mpe.getProgramaEducativo())
                ));
            });
            return dtoList;
        } catch (NoResultException | NonUniqueResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    
    
    @Override
    public List<PeriodosEscolares> getPeriodosConregistro(RegistrosTipo registroTipo, EventosRegistros eventoRegistro) {
        List<Integer> claves = f.getEntityManager().createQuery("SELECT mpe.periodo FROM MatriculaPeriodosEscolares mpe INNER JOIN mpe.registros r WHERE r.tipo.registroTipo=:tipo GROUP BY mpe.periodo", Integer.class)
                .setParameter("tipo", registroTipo.getRegistroTipo())
                .getResultList();

        List<PeriodosEscolares> l = new ArrayList<>();
        if (claves.isEmpty()) {
            
            l = f.getEntityManager().createQuery("SELECT p FROM PeriodosEscolares p WHERE (:mes BETWEEN p.mesInicio.numero AND p.mesFin.numero) AND (p.anio = :anio) GROUP BY p.periodo",PeriodosEscolares.class)
                    .setParameter("mes", ejbModulos.getNumeroMes(eventoRegistro.getMes()))
                    .setParameter("anio", eventoRegistro.getEjercicioFiscal().getAnio())
                    .getResultList();
        } else {
            l = f.getEntityManager().createQuery("SELECT periodo FROM PeriodosEscolares periodo WHERE periodo.periodo IN :claves GROUP BY periodo.periodo ORDER BY periodo.periodo DESC", PeriodosEscolares.class)
                    .setParameter("claves", claves)
                    .getResultList();
        }
        return l;
    }
    
    @Override
    public Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> comprobarEventoActual(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoActual, RegistrosTipo registrosTipo) throws PeriodoEscolarNecesarioNoRegistradoException{
        if(periodos==null || periodos.isEmpty()) periodos = getPeriodosConregistro(registrosTipo,eventoActual);
        if(periodos==null || periodos.isEmpty()) return null;
        if(eventoActual == null) eventoActual = ejbModulos.getEventoRegistro();
        if(eventoActual == null) return null;

        PeriodosEscolares reciente = periodos.get(0);
        Boolean existe = eventos.contains(eventoActual);
        if(!existe){//si el evento no existe en la lista de eventos del periodo mas reciente
            if(eventos.size() <3){//si el evento deberia pertenecer al periodo mas reciente
                eventos = new ArrayList<>(Stream.concat(Stream.of(eventoActual), eventos.stream()).collect(Collectors.toList())); //.add(eventoActual);
            }else{//si el evento debería pertenecer al periodo inmediato al mas reciente detectado
                PeriodosEscolares periodo = f.getEntityManager().find(PeriodosEscolares.class, reciente.getPeriodo() + 1);
                if(periodo == null) throw new PeriodoEscolarNecesarioNoRegistradoException(reciente.getPeriodo() + 1, caster.periodoToString(reciente));
                periodos = new ArrayList<>(Stream.concat(Stream.of(periodo), periodos.stream()).collect(Collectors.toList()));
                eventos.clear();
                eventos.add(eventoActual);
            }
        }
        Map<List<PeriodosEscolares>,List<EventosRegistros>> map = new HashMap<>();
        map.put(periodos, eventos);
        return map.entrySet().iterator().next();
    }
    
    @Override
    public List<DTOMatriculaPeriodosEscolares> getListaRegistrosPorEventoAreaPeriodo(Short claveArea, PeriodosEscolares periodo, RegistrosTipo registrosTipo) {
        //verificar que los parametros no sean nulos
        if(claveArea == null || periodo == null){
            return null;
        }
        
        AreasUniversidad area = f.getEntityManager().find(AreasUniversidad.class, claveArea);
        
        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        List<DTOMatriculaPeriodosEscolares> l = new ArrayList<>();
        List<MatriculaPeriodosEscolares> entities = f.getEntityManager().createQuery("SELECT mpe FROM MatriculaPeriodosEscolares mpe INNER JOIN mpe.registros r INNER JOIN r.tipo t WHERE (mpe.periodo=:periodo) AND (t.registroTipo=:tipo) AND (r.area = :area) ORDER BY mpe.programaEducativo,mpe.cuatrimestre,mpe.grupo,mpe.matricula", MatriculaPeriodosEscolares.class)
                .setParameter("periodo", periodo.getPeriodo())
                .setParameter("tipo", registrosTipo.getRegistroTipo())
                .setParameter("area", area.getArea())
                .setFirstResult(0)
                .setMaxResults(100)
                .getResultList();

        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            f.getEntityManager().refresh(e);
            ActividadesPoa a = e.getRegistros().getActividadesPoaList().isEmpty() ? null : e.getRegistros().getActividadesPoaList().get(0);
                if(a != null){
                    l.add(new DTOMatriculaPeriodosEscolares(
                        e,
                        caster.periodoToString(f.getEntityManager().find(PeriodosEscolares.class, e.getPeriodo())),
                        f.getEntityManager().find(AreasUniversidad.class, e.getProgramaEducativo()),
                        a
                    ));
                }else{
                    l.add(new DTOMatriculaPeriodosEscolares(
                        e,
                        caster.periodoToString(f.getEntityManager().find(PeriodosEscolares.class, e.getPeriodo())),
                        f.getEntityManager().find(AreasUniversidad.class, e.getProgramaEducativo())
                    ));
                } 
        });
        return l;
    }
    
    @Override
    public Boolean eliminarRegistro(DTOMatriculaPeriodosEscolares registro) {
        Integer clave = registro.getMatricula().getRegistro();
        f.remove(registro.getMatricula().getRegistros());
        f.getEntityManager().detach(registro.getMatricula());
        f.flush();
        return f.getEntityManager().find(MatriculaPeriodosEscolares.class, clave) == null;
    }
    
    @Override
    public List<EvidenciasDetalle> getListaEvidenciasPorRegistro(DTOMatriculaPeriodosEscolares registro) {
        if(registro == null){
            return Collections.EMPTY_LIST;
        }
        List<EvidenciasDetalle> l = f.getEntityManager().createQuery("SELECT e FROM Evidencias e INNER JOIN e.registrosList r INNER JOIN e.evidenciasDetalleList ed WHERE r.registro=:registro ORDER BY ed.mime, ed.ruta", Evidencias.class)
                .setParameter("registro", registro.getMatricula().getRegistros().getRegistro())
                .getResultStream()
                .map(e -> e.getEvidenciasDetalleList())
                .flatMap(ed -> ed.stream())
                .distinct()
                .collect(Collectors.toList());
        return l;
    }
    
    @Override
    public Boolean eliminarEvidenciaEnRegistro(DTOMatriculaPeriodosEscolares registro, EvidenciasDetalle evidenciasDetalle) {
        if (registro == null || evidenciasDetalle == null) {
            return false;
        }

        Integer id = evidenciasDetalle.getDetalleEvidencia();

        try {
            ServicioArchivos.eliminarArchivo(evidenciasDetalle.getRuta());

            Evidencias evidencias = evidenciasDetalle.getEvidencia();
            Integer total = evidencias.getEvidenciasDetalleList().size();
            evidencias.getEvidenciasDetalleList().remove(evidenciasDetalle);
            f.remove(evidenciasDetalle);
            f.edit(evidencias);
            f.flush();

            if(total == 1){
                f.remove(evidencias);
                f.flush();
            }else if(total == 2){
                evidencias.setCategoria(EvidenciaCategoria.UNICA.getLabel());
                f.edit(evidencias);
                f.flush();
                f.getEntityManager().detach(evidencias);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se eliminó la evidencia: " + evidenciasDetalle.getRuta(), e);
            return false;
        }

        return f.getEntityManager().find(EvidenciasDetalle.class, id) == null;
    }
    
    @Override
    public Map.Entry<Boolean, Integer> registrarEvidenciasARegistro(DTOMatriculaPeriodosEscolares registro, List<Part> archivos) {
        Map<Boolean, Integer> map = new HashMap<>();

        if(registro == null || archivos == null || archivos.isEmpty()){
            map.put(Boolean.FALSE, 0);
            return map.entrySet().iterator().next();
        }
        final List<Boolean> res = new ArrayList<>();
        Evidencias evidencias = new Evidencias(0, archivos.size() > 1?EvidenciaCategoria.MULTIPLE.getLabel():EvidenciaCategoria.UNICA.getLabel());
        f.create(evidencias);
        f.flush();
        f.refresh(evidencias);
        AreasUniversidad areaPOA = f.getEntityManager().find(AreasUniversidad.class, registro.getMatricula().getRegistros().getArea());
        archivos.forEach(archivo -> {
            try{
                String rutaAbsoluta = ServicioArchivos.almacenarEvidenciaRegistroGeneral(areaPOA, registro.getMatricula().getRegistros(), archivo);
                EvidenciasDetalle ed = new EvidenciasDetalle(0, rutaAbsoluta, archivo.getContentType(), archivo.getSize(), registro.getMatricula().getRegistros().getEventoRegistro().getMes());
                evidencias.getEvidenciasDetalleList().add(ed);
                ed.setEvidencia(evidencias);
                f.create(ed);
                f.flush();
                res.add(true);
            }catch(Exception e){
                res.add(Boolean.FALSE);
                LOG.log(Level.SEVERE, "No se guardó el archivo: " + archivo.getSubmittedFileName(), e);
            }
        });

        Long correctos = res.stream().filter(r -> r).count();
        Long incorrectos = res.stream().filter(r -> !r).count();

        if(correctos == 0){
            f.remove(evidencias);
            f.flush();
        }else{
            registro.getMatricula().getRegistros().getEvidenciasList().add(evidencias);
            evidencias.getRegistrosList().add(registro.getMatricula().getRegistros());
            f.edit(registro.getMatricula().getRegistros());
            f.edit(evidencias);
            f.flush();
        }

        map.put(incorrectos == 0, correctos.intValue());
        return map.entrySet().iterator().next();
    }
    
    @Override
    public ActividadesPoa getActividadAlineada(DTOMatriculaPeriodosEscolares registro) {
        List<ActividadesPoa> l =f.getEntityManager().createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.registrosList r INNER JOIN a.cuadroMandoInt cm INNER JOIN FETCH cm.lineaAccion INNER JOIN FETCH cm.estrategia INNER JOIN FETCH cm.eje WHERE r.registro = :registro", ActividadesPoa.class)
                .setParameter("registro", registro.getMatricula().getRegistros().getRegistro())
                .getResultList();
        if(!l.isEmpty()) return l.get(0);
        else return null;
    }
    
    @Override
    public Boolean alinearRegistroActividad(ActividadesPoa actividad, DTOMatriculaPeriodosEscolares registro) {
        try{
            ActividadesPoa a = f.getEntityManager().find(ActividadesPoa.class, actividad.getActividadPoa());
            Registros r = f.getEntityManager().find(Registros.class, registro.getMatricula().getRegistro());

            eliminarAlineacion(registro);

            a.getRegistrosList().add(r);
            r.getActividadesPoaList().add(actividad);
            f.flush();
            return true;
        }catch(Exception e){
            LOG.log(Level.SEVERE, "No se pudo alinear el registro con la actividad.", e);
            return false;
        }
    }
    
    @Override
    public Boolean eliminarAlineacion(DTOMatriculaPeriodosEscolares registro) {
        try{
            Registros r = f.getEntityManager().find(Registros.class, registro.getMatricula().getRegistro());

            if(!r.getActividadesPoaList().isEmpty()){
                ActividadesPoa a2 = f.getEntityManager().find(ActividadesPoa.class, r.getActividadesPoaList().get(0).getActividadPoa());
                a2.getRegistrosList().remove(r);
                r.getActividadesPoaList().remove(a2);
                f.flush();
                registro.setActividadAlineada(null);
            }
            return true;
        }catch(Exception e){
            LOG.log(Level.SEVERE, "No se pudo alinear el registro con la actividad.", e);
            return false;
        }
    }
    
    private static final Logger LOG = Logger.getLogger(ServicioMatriculaPeriodosEscolares.class.getName());

    @Override
    public MatriculaPeriodosEscolares editaMatriculaPeriodoEscolar(MatriculaPeriodosEscolares mpe) {
        try {
            f.setEntityClass(MatriculaPeriodosEscolares.class);
            f.edit(mpe);
            f.flush();
            return mpe;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudo actualizar el registro de matricula: " + mpe.getMatricula(), e);
            return null;
        }
    }

    @Override
    public Boolean buscaMatriculaPeriodoEscolarExistente(MatriculaPeriodosEscolares matriculaPeriodoEscolar) {
        try {
            MatriculaPeriodosEscolares mpe = new MatriculaPeriodosEscolares();
            mpe = f.getEntityManager().createQuery("SELECT m FROM MatriculaPeriodosEscolares m WHERE m.matricula = :matricula AND m.periodo = :periodo AND m.registro <> :registro", MatriculaPeriodosEscolares.class)
                    .setParameter("matricula", matriculaPeriodoEscolar.getMatricula())
                    .setParameter("periodo", matriculaPeriodoEscolar.getPeriodo())
                    .setParameter("registro", matriculaPeriodoEscolar.getRegistro())
                    .getSingleResult();
            if (mpe != null) {
                return true;
            } else {
                return false;
            }
        } catch (NoResultException | NonUniqueResultException ex) {
            return false;
        }
    }

    @Override
    public List<MatriculaPeriodosEscolares> getReporteMatriculaPorEjercicio() {
        try {
            return f.getEntityManager().createQuery("SELECT m FROM MatriculaPeriodosEscolares m INNER JOIN m.registros r WHERE r.eventoRegistro.ejercicioFiscal.anio = :ejercicioFiscal ORDER BY m.periodo,m.programaEducativo,m.cuatrimestre,m.grupo ASC",MatriculaPeriodosEscolares.class)
                    .setParameter("ejercicioFiscal", ejbModulos.getEventoRegistro().getEjercicioFiscal().getAnio())
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Long getConteoMatriculaInicialPorPeriodo(PeriodosEscolares periodoEscolar) {
        try {
            Query numero = f.getEntityManager().createQuery("SELECT COUNT(m.registro) FROM MatriculaPeriodosEscolares m WHERE m.periodo = :periodo")
                    .setParameter("periodo", periodoEscolar.getPeriodo());
            Object count = numero.getSingleResult();
            Long numeroMatricula = (Long) count;
            return numeroMatricula;
        } catch (NonUniqueResultException nure){
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioMatriculaPeriodosEscolares.getConteoMatriculaInicialPorPeriodo() NonUniqueResultException" + nure);
            return null;
        } catch (NoResultException nre) {
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioMatriculaPeriodosEscolares.getConteoMatriculaInicialPorPeriodo() NoResultException" + nre);
            return null;
        }catch (Exception e){
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioMatriculaPeriodosEscolares.getConteoMatriculaInicialPorPeriodo() Exception" + e);
            return null;
        }
    }
    
}
