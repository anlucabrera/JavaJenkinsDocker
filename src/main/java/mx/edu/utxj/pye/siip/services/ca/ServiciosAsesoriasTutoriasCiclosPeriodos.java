/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Meses;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.AsesoriasTutoriasCicloPeriodos;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.enums.EvidenciaCategoria;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.ca.DTOAsesoriasTutoriasCicloPeriodos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbAsesoriasTutoriasCiclosPeriodos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author UTXJ
 */
@Stateless
@MultipartConfig
public class ServiciosAsesoriasTutoriasCiclosPeriodos implements EjbAsesoriasTutoriasCiclosPeriodos {

    @EJB Facade facadeEscolar;
    @EJB EjbModulos ejbModulos;
    @EJB EjbPropiedades ep;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @Inject Caster caster;
    
    @Override
    public List<DTOAsesoriasTutoriasCicloPeriodos> getListaAsesoriasTutorias(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();

            List<DTOAsesoriasTutoriasCicloPeriodos> listaDtoAsesoriasTutoriasCicloPeriodos = new ArrayList<>();
            DTOAsesoriasTutoriasCicloPeriodos dtoAsesoriaTutoriaCicloPeriodo;
            AsesoriasTutoriasCicloPeriodos asesoriaTutoriaCicloPeriodo;
            AreasUniversidad areaUniversidad;

            File excelAsesoriaTutoria = new File(rutaArchivo);
            XSSFWorkbook workBookAsesoriaTutoria = new XSSFWorkbook();
            workBookAsesoriaTutoria = (XSSFWorkbook) WorkbookFactory.create(excelAsesoriaTutoria);
            XSSFSheet primeraHoja = workBookAsesoriaTutoria.getSheetAt(0);
            XSSFRow fila;

            if (primeraHoja.getSheetName().equals("Tutorías_Asesorías")) {
                for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                    fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                    if ((fila.getCell(1).getNumericCellValue() > 0)) {
                        dtoAsesoriaTutoriaCicloPeriodo = new DTOAsesoriasTutoriasCicloPeriodos();
                        asesoriaTutoriaCicloPeriodo = new AsesoriasTutoriasCicloPeriodos();
                        areaUniversidad = new AreasUniversidad();

                        if (fila.getCell(6).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(6).getCellTypeEnum()) {
                                case FORMULA:
                                    asesoriaTutoriaCicloPeriodo.setPeriodoEscolar((int) fila.getCell(6).getNumericCellValue());
                                    dtoAsesoriaTutoriaCicloPeriodo.setPeriodoEscolar(fila.getCell(5).getStringCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Periodo Escolar en la columna: " + (5 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(8).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(8).getCellTypeEnum()) {
                                case FORMULA:
                                    asesoriaTutoriaCicloPeriodo.setTipoActividad(fila.getCell(8).getStringCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Tipo de actividad en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(13).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(13).getCellTypeEnum()) {
                                case FORMULA:
                                    areaUniversidad.setArea((short) fila.getCell(13).getNumericCellValue());
                                    asesoriaTutoriaCicloPeriodo.setProgramaEducativo(areaUniversidad.getArea());
                                    areaUniversidad.setNombre(fila.getCell(10).getStringCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Programa educativo en la columna: " + (10 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(15).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(15).getCellTypeEnum()) {
                                case FORMULA:
                                    asesoriaTutoriaCicloPeriodo.setCuatrimestre(String.valueOf((int) fila.getCell(15).getNumericCellValue()));
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Cuatrimestre en la columna: " + (15 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(17).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(17).getCellTypeEnum()) {
                                case FORMULA:
                                    asesoriaTutoriaCicloPeriodo.setGrupo(fila.getCell(17).getStringCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Grupo en la columna: " + (17 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(18).getCellTypeEnum() == CellType.STRING) {
                            switch (fila.getCell(18).getCellTypeEnum()) {
                                case STRING:
                                    asesoriaTutoriaCicloPeriodo.setAsunto(fila.getCell(18).getStringCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Asunto en la columna: " + (18 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(20).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(20).getCellTypeEnum()) {
                                case FORMULA:
                                    asesoriaTutoriaCicloPeriodo.setTipo(fila.getCell(20).getStringCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Tipo en la columna: " + (20 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(21).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(21).getCellTypeEnum()) {
                                case NUMERIC:
                                    asesoriaTutoriaCicloPeriodo.setNoTutoriasAsesorias((short) fila.getCell(21).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Numero de asesorías y tutorías en la columna: " + (21 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(22).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(22).getCellTypeEnum()) {
                                case NUMERIC:
                                    asesoriaTutoriaCicloPeriodo.setAsistentes((short) fila.getCell(22).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Asistentes en la columna: " + (22 + 1) + " y fila: " + (i + 1));
                        }

                        dtoAsesoriaTutoriaCicloPeriodo.setAsesoriasTutoriasCicloPeriodos(asesoriaTutoriaCicloPeriodo);
                        dtoAsesoriaTutoriaCicloPeriodo.setAreasUniversidad(areaUniversidad);

                        listaDtoAsesoriasTutoriasCicloPeriodos.add(dtoAsesoriaTutoriaCicloPeriodo);
                    }
                }
                workBookAsesoriaTutoria.close();

                if (validarCelda.contains(false)) {
                    addDetailMessage("<b>El archivo cargado contiene datos que no son validos, verifique los datos de la plantilla</b>");
                    addDetailMessage(datosInvalidos.toString());

                    excelAsesoriaTutoria.delete();
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                    return Collections.EMPTY_LIST;
                } else {
                    addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
                    return listaDtoAsesoriasTutoriasCicloPeriodos;
                }

            } else {
                workBookAsesoriaTutoria.close();
                excelAsesoriaTutoria.delete();
                ServicioArchivos.eliminarArchivo(rutaArchivo);
                addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
                return Collections.EMPTY_LIST;
            }
        } else {
            addDetailMessage("<b>Ocurrio un error en la lectura del archivo</b>");
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public void guardaAsesoriasTutorias(List<DTOAsesoriasTutoriasCicloPeriodos> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        lista.forEach((asesTut) -> {
            if (ejbModulos.validaPeriodoRegistro(ejbModulos.getPeriodoEscolarActivo(), asesTut.getAsesoriasTutoriasCicloPeriodos().getPeriodoEscolar())) {
                facadeEscolar.setEntityClass(AsesoriasTutoriasCicloPeriodos.class);
                AsesoriasTutoriasCicloPeriodos asesTutEncontrada = getRegistroAsesoriaTutoriaCicloPeriodo(asesTut.getAsesoriasTutoriasCicloPeriodos());
                Boolean registroAlmacenado = false;
                if (asesTutEncontrada != null) {
                    listaCondicional.add(asesTutEncontrada.getAsunto() + " " + asesTutEncontrada.getCuatrimestre() + " " + asesTutEncontrada.getGrupo());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    if (ejbModulos.getEventoRegistro().equals(asesTutEncontrada.getRegistros().getEventoRegistro())) {
                        asesTut.getAsesoriasTutoriasCicloPeriodos().setRegistro(asesTutEncontrada.getRegistro());
                        facadeEscolar.edit(asesTut.getAsesoriasTutoriasCicloPeriodos());
                    } else {
                        listaCondicional.remove(asesTutEncontrada.getAsunto() + " " + asesTutEncontrada.getCuatrimestre() + " " + asesTutEncontrada.getGrupo());
                    }
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    asesTut.getAsesoriasTutoriasCicloPeriodos().setRegistro(registro.getRegistro());
                    facadeEscolar.create(asesTut.getAsesoriasTutoriasCicloPeriodos());
                }
                facadeEscolar.flush();
            }
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public AsesoriasTutoriasCicloPeriodos getRegistroAsesoriaTutoriaCicloPeriodo(AsesoriasTutoriasCicloPeriodos asesoriaTutoriaCicloPeriodo) {
        AsesoriasTutoriasCicloPeriodos asesTutCPEnviada = new AsesoriasTutoriasCicloPeriodos();
        TypedQuery<AsesoriasTutoriasCicloPeriodos> query = facadeEscolar.getEntityManager().createQuery("SELECT a FROM AsesoriasTutoriasCicloPeriodos AS a WHERE a.periodoEscolar = :periodoEscolar AND a.programaEducativo = :programaEducativo AND a.cuatrimestre = :cuatrimestre AND a.grupo = :grupo AND a.tipoActividad = :tipoActividad AND a.tipo = :tipo", AsesoriasTutoriasCicloPeriodos.class);
        query.setParameter("periodoEscolar", asesoriaTutoriaCicloPeriodo.getPeriodoEscolar());
        query.setParameter("programaEducativo", asesoriaTutoriaCicloPeriodo.getProgramaEducativo());
        query.setParameter("cuatrimestre", asesoriaTutoriaCicloPeriodo.getCuatrimestre());
        query.setParameter("grupo", asesoriaTutoriaCicloPeriodo.getGrupo());
        query.setParameter("tipoActividad", asesoriaTutoriaCicloPeriodo.getTipoActividad());
        query.setParameter("tipo", asesoriaTutoriaCicloPeriodo.getTipo());
        try {
            asesTutCPEnviada = query.getSingleResult();
            asesTutCPEnviada.setRegistros(ejbModulos.buscaRegistroPorClave(asesTutCPEnviada.getRegistro()));
        } catch (NoResultException | NonUniqueResultException ex) {
            asesTutCPEnviada = null;
            ex.toString();
        }
        return asesTutCPEnviada;
    }

    @Override
    public List<PeriodosEscolares> getPeriodosConregistro(RegistrosTipo registrosTipo, EventosRegistros eventoRegistro) {
        List<Integer> claves = facadeEscolar.getEntityManager().createQuery("SELECT atp.periodoEscolar FROM AsesoriasTutoriasCicloPeriodos atp INNER JOIN atp.registros r WHERE r.tipo.registroTipo=:tipo", Integer.class)
                .setParameter("tipo", registrosTipo.getRegistroTipo())
                .getResultList();
        
        List<PeriodosEscolares> l = new ArrayList<>();
        if(claves.isEmpty()){
            
            l = facadeEscolar.getEntityManager().createQuery("SELECT p FROM PeriodosEscolares p WHERE (:mes BETWEEN p.mesInicio.numero AND p.mesFin.numero) AND (p.anio = :anio)",PeriodosEscolares.class)
                    .setParameter("mes", ejbModulos.getNumeroMes(eventoRegistro.getMes()))
                    .setParameter("anio", eventoRegistro.getEjercicioFiscal().getAnio())
                    .getResultList();
        }else{
            l = facadeEscolar.getEntityManager().createQuery("SELECT periodo FROM PeriodosEscolares periodo WHERE periodo.periodo IN :claves ORDER BY periodo.periodo desc", PeriodosEscolares.class)
                    .setParameter("claves", claves)
                    .getResultList();
        }
        return l;
    }

    @Override
    public List<DTOAsesoriasTutoriasCicloPeriodos> getListaRegistrosPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo, RegistrosTipo registrosTipo) {
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaRegistrosPorEventoAreaPeriodo(inicio)");
        //verificar que los parametros no sean nulos
        if(evento == null || claveArea == null || periodo == null){
            return null;
        }
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaRegistrosPorEventoArea(1) evento: " + evento);
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaRegistrosPorEventoArea(2) area: " + claveArea);

        List<Short> areas = new ArrayList<>();

        //obtener la referencia al area operativa del trabajador
        AreasUniversidad area = facadeEscolar.getEntityManager().find(AreasUniversidad.class, claveArea);
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaRegistrosPorEventoArea(3) area: " + area);

        //comprobar si el area operativa es un programa educativo referenciar a su area superior para obtener la referencia al area academica
        Short programaCategoria = (short)ep.leerPropiedadEntera("modulosRegistroProgramaEducativoCategoria").orElse(9);
        if (Objects.equals(area.getCategoria().getCategoria(), programaCategoria)) {
            areas.add(claveArea);
//            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaRegistrosPorEventoArea(3b) areas: " + areas);
        }else{//si no es area academica solo filtrar los datos del area operativa del trabajador
            //Obtener las claves de todas las areas que dependan de área academicoa
            areas = facadeEscolar.getEntityManager().createQuery("SELECT au FROM AreasUniversidad au WHERE au.areaSuperior=:areaSuperior AND au.vigente='1'", AreasUniversidad.class)
                    .setParameter("areaSuperior", area.getArea())
                    .getResultStream()
                    .map(au -> au.getArea())
                    .collect(Collectors.toList());
//            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaRegistrosPorEventoArea(3a) areas: " + areas);
        }
        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        List<DTOAsesoriasTutoriasCicloPeriodos> l = new ArrayList<>();
        List<AsesoriasTutoriasCicloPeriodos> entities = facadeEscolar.getEntityManager().createQuery("SELECT atc FROM AsesoriasTutoriasCicloPeriodos atc INNER JOIN atc.registros r INNER JOIN r.tipo t INNER JOIN r.eventoRegistro er WHERE er.eventoRegistro=:evento AND atc.programaEducativo in :areas AND atc.periodoEscolar=:periodo AND t.registroTipo=:tipo ORDER BY atc.programaEducativo, atc.cuatrimestre, atc.grupo, atc.tipoActividad, atc.tipo", AsesoriasTutoriasCicloPeriodos.class)
                .setParameter("areas", areas)
                .setParameter("evento", evento.getEventoRegistro())
                .setParameter("periodo", periodo.getPeriodo())
                .setParameter("tipo", registrosTipo.getRegistroTipo())
                .getResultList();

//        entities.forEach(atc -> System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaRegistrosPorEventoArea(4) registro: " + atc));

        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            facadeEscolar.getEntityManager().refresh(e);
            ActividadesPoa a = e.getRegistros().getActividadesPoaList().isEmpty() ? null : e.getRegistros().getActividadesPoaList().get(0);
                if(a != null){
                    l.add(new DTOAsesoriasTutoriasCicloPeriodos(
                        e,
                        caster.periodoToString(facadeEscolar.getEntityManager().find(PeriodosEscolares.class, e.getPeriodoEscolar())),
                        facadeEscolar.getEntityManager().find(AreasUniversidad.class, e.getProgramaEducativo()),
                        a
                    ));
                }else{
                    l.add(new DTOAsesoriasTutoriasCicloPeriodos(
                        e,
                        caster.periodoToString(facadeEscolar.getEntityManager().find(PeriodosEscolares.class, e.getPeriodoEscolar())),
                        facadeEscolar.getEntityManager().find(AreasUniversidad.class, e.getProgramaEducativo())
                    ));
                }
                
        });

//        l.forEach(dto -> System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaRegistrosPorEventoArea() dto: " + dto));
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaRegistrosPorEventoAreaPeriodo(fin)");
        return l;
    }

    @Override
    public Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> comprobarEventoActual(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoActual, RegistrosTipo registrosTipo) throws PeriodoEscolarNecesarioNoRegistradoException{
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.comprobarEventoActual(1)");
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.comprobarEventoActual(1) actual: " + eventoActual);
        if(periodos==null || periodos.isEmpty()) periodos = getPeriodosConregistro(registrosTipo,eventoActual);
        if(periodos==null || periodos.isEmpty()) return null;
        if(eventoActual == null) eventoActual = ejbModulos.getEventoRegistro();
        if(eventoActual == null) return null;

        PeriodosEscolares reciente = periodos.get(0);
        Boolean existe = eventos.contains(eventoActual);
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.comprobarEventoActual(2) existe: " + existe);

        if(!existe){//si el evento no existe en la lista de eventos del periodo mas reciente
            if(eventos.size() <3){//si el evento deberia pertenecer al periodo mas reciente
                eventos = new ArrayList<>(Stream.concat(Stream.of(eventoActual), eventos.stream()).collect(Collectors.toList())); //.add(eventoActual);
//                System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.comprobarEventoActual(2a) dentro del periodo");
            }else{//si el evento debería pertenecer al periodo inmediato al mas reciente detectado
                PeriodosEscolares periodo = facadeEscolar.getEntityManager().find(PeriodosEscolares.class, reciente.getPeriodo() + 1);
                if(periodo == null) throw new PeriodoEscolarNecesarioNoRegistradoException(reciente.getPeriodo() + 1, caster.periodoToString(reciente));
//                System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.comprobarEventoActual(2b) en nuevo periodo: " + periodo);
                periodos = new ArrayList<>(Stream.concat(Stream.of(periodo), periodos.stream()).collect(Collectors.toList()));
                eventos.clear();
                eventos.add(eventoActual);
            }
        }
//        periodos.forEach(pe -> System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.comprobarEventoActual(3) periodo: " + pe));
//        eventos.forEach(er -> System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.comprobarEventoActual(3) evento: " + er));
        Map<List<PeriodosEscolares>,List<EventosRegistros>> map = new HashMap<>();
        map.put(periodos, eventos);
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.comprobarEventoActual(2)");
        return map.entrySet().iterator().next();
    }

    @Override
    public Boolean eliminarRegistro(DTOAsesoriasTutoriasCicloPeriodos registro) {
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.eliminarRegistro(1)");
        Integer clave = registro.getAsesoriasTutoriasCicloPeriodos().getRegistro();
        facadeEscolar.remove(registro.getAsesoriasTutoriasCicloPeriodos().getRegistros());
        facadeEscolar.getEntityManager().detach(registro.getAsesoriasTutoriasCicloPeriodos());
        facadeEscolar.flush();

//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.eliminarRegistro(2)");
        return facadeEscolar.getEntityManager().find(AsesoriasTutoriasCicloPeriodos.class, clave) == null;
    }

    @Override
    public List<EvidenciasDetalle> getListaEvidenciasPorRegistro(DTOAsesoriasTutoriasCicloPeriodos registro) {
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaEvidenciasPorRegistro(1)");
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaEvidenciasPorRegistro(): " + registro);
        if(registro == null){
            return Collections.EMPTY_LIST;
        }
        List<EvidenciasDetalle> l = facadeEscolar.getEntityManager().createQuery("SELECT e FROM Evidencias e INNER JOIN e.registrosList r INNER JOIN e.evidenciasDetalleList ed WHERE r.registro=:registro ORDER BY ed.mime, ed.ruta", Evidencias.class)
                .setParameter("registro", registro.getAsesoriasTutoriasCicloPeriodos().getRegistros().getRegistro())
                .getResultStream()
                .map(e -> e.getEvidenciasDetalleList())
                .flatMap(ed -> ed.stream())
                .distinct()
                .collect(Collectors.toList());
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaEvidenciasPorRegistro(2)");
        return l;
    }

    @Override
    public Map.Entry<Boolean, Integer> registrarEvidenciasARegistro(DTOAsesoriasTutoriasCicloPeriodos registro, List<Part> archivos) {
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.registrarEvidenciasARegistro(1)");
        Map<Boolean, Integer> map = new HashMap<>();

        if(registro == null || archivos == null || archivos.isEmpty()){
            map.put(Boolean.FALSE, 0);
            return map.entrySet().iterator().next();
        }
        final List<Boolean> res = new ArrayList<>();
        Evidencias evidencias = new Evidencias(0, archivos.size() > 1?EvidenciaCategoria.MULTIPLE.getLabel():EvidenciaCategoria.UNICA.getLabel());
        facadeEscolar.create(evidencias);
        facadeEscolar.flush();
        facadeEscolar.refresh(evidencias);
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.registrarEvidenciasARegistro() evidencias: " + evidencias);
        AreasUniversidad areaPOA = facadeEscolar.getEntityManager().find(AreasUniversidad.class, registro.getAsesoriasTutoriasCicloPeriodos().getRegistros().getArea());
        archivos.forEach(archivo -> {
            try{
                String rutaAbsoluta = ServicioArchivos.almacenarEvidenciaRegistroGeneral(areaPOA, registro.getAsesoriasTutoriasCicloPeriodos().getRegistros(), archivo);
                EvidenciasDetalle ed = new EvidenciasDetalle(0, rutaAbsoluta, archivo.getContentType(), archivo.getSize(), registro.getAsesoriasTutoriasCicloPeriodos().getRegistros().getEventoRegistro().getMes());
                evidencias.getEvidenciasDetalleList().add(ed);
                ed.setEvidencia(evidencias);
                facadeEscolar.create(ed);
                facadeEscolar.flush();
//                System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.registrarEvidenciasARegistro() ed: " + ed.getRuta());
                res.add(true);
            }catch(Exception e){
                res.add(Boolean.FALSE);
                LOG.log(Level.SEVERE, "No se guardó el archivo: " + archivo.getSubmittedFileName(), e);
            }
        });

        Long correctos = res.stream().filter(r -> r).count();
        Long incorrectos = res.stream().filter(r -> !r).count();

        if(correctos == 0){
            facadeEscolar.remove(evidencias);
            facadeEscolar.flush();
        }else{
            registro.getAsesoriasTutoriasCicloPeriodos().getRegistros().getEvidenciasList().add(evidencias);
            evidencias.getRegistrosList().add(registro.getAsesoriasTutoriasCicloPeriodos().getRegistros());
            facadeEscolar.edit(registro.getAsesoriasTutoriasCicloPeriodos().getRegistros());
            facadeEscolar.edit(evidencias);
            facadeEscolar.flush();
        }

        map.put(incorrectos == 0, correctos.intValue());
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.registrarEvidenciasARegistro(2)");
        return map.entrySet().iterator().next();
    }
    
    private static final Logger LOG = Logger.getLogger(ServiciosAsesoriasTutoriasCiclosPeriodos.class.getName());

    @Override
    public Boolean eliminarEvidenciaEnRegistro(DTOAsesoriasTutoriasCicloPeriodos registro, EvidenciasDetalle evidenciasDetalle) {
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.eliminarEvidenciaEnRegistro(1)");
        if (registro == null || evidenciasDetalle == null) {
            return false;
        }

        Integer id = evidenciasDetalle.getDetalleEvidencia();

        try {
            ServicioArchivos.eliminarArchivo(evidenciasDetalle.getRuta());

            Evidencias evidencias = evidenciasDetalle.getEvidencia();
            Integer total = evidencias.getEvidenciasDetalleList().size();
            evidencias.getEvidenciasDetalleList().remove(evidenciasDetalle);
            facadeEscolar.remove(evidenciasDetalle);
            facadeEscolar.edit(evidencias);
            facadeEscolar.flush();

//            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.eliminarEvidenciaEnRegistro() total: " + total);

            if(total == 1){
                facadeEscolar.remove(evidencias);
                facadeEscolar.flush();
            }else if(total == 2){
                evidencias.setCategoria(EvidenciaCategoria.UNICA.getLabel());
                facadeEscolar.edit(evidencias);
                facadeEscolar.flush();
                facadeEscolar.getEntityManager().detach(evidencias);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se eliminó la evidencia: " + evidenciasDetalle.getRuta(), e);
            return false;
        }

//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.eliminarEvidenciaEnRegistro(2)");
        return facadeEscolar.getEntityManager().find(EvidenciasDetalle.class, id) == null;
    }

    @Override
    public ActividadesPoa getActividadAlineada(DTOAsesoriasTutoriasCicloPeriodos registro) {
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getActividadAlineada(1)");
        List<ActividadesPoa> l = facadeEscolar.getEntityManager().createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.registrosList r INNER JOIN a.cuadroMandoInt cm INNER JOIN FETCH cm.lineaAccion INNER JOIN FETCH cm.estrategia INNER JOIN FETCH cm.eje WHERE r.registro = :registro", ActividadesPoa.class)
                .setParameter("registro", registro.getAsesoriasTutoriasCicloPeriodos().getRegistros().getRegistro())
                .getResultList();
        if(!l.isEmpty()) return l.get(0);
        else return null;
    }

    @Override
    public Boolean alinearRegistroActividad(ActividadesPoa actividad, DTOAsesoriasTutoriasCicloPeriodos registro) {
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.alinearRegistroActividad(1)");
        try{
            ActividadesPoa a = facadeEscolar.getEntityManager().find(ActividadesPoa.class, actividad.getActividadPoa());
            Registros r = facadeEscolar.getEntityManager().find(Registros.class, registro.getAsesoriasTutoriasCicloPeriodos().getRegistro());

//            if(!a.getRegistrosList().isEmpty() || !r.getActividadesPoaList().isEmpty()){
//                ActividadesPoa a2 = f.getEntityManager().find(ActividadesPoa.class, r.getActividadesPoaList().get(0).getActividadPoa());
//                a2.getRegistrosList().remove(r);
//                r.getActividadesPoaList().remove(a2);
//                f.flush();
//            }
            eliminarAlineacion(registro);

            a.getRegistrosList().add(r);
            r.getActividadesPoaList().add(actividad);
            facadeEscolar.flush();

//            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.alinearRegistroActividad(2)");
            return true;
        }catch(Exception e){
            LOG.log(Level.SEVERE, "No se pudo alinear el registro con la actividad.", e);
            return false;
        }
    }

    @Override
    public Boolean eliminarAlineacion(DTOAsesoriasTutoriasCicloPeriodos registro) {
        try{
            Registros r = facadeEscolar.getEntityManager().find(Registros.class, registro.getAsesoriasTutoriasCicloPeriodos().getRegistro());

            if(!r.getActividadesPoaList().isEmpty()){
                ActividadesPoa a2 = facadeEscolar.getEntityManager().find(ActividadesPoa.class, r.getActividadesPoaList().get(0).getActividadPoa());
//                Integer clave = a2.getActividadPoa();
                a2.getRegistrosList().remove(r);
                r.getActividadesPoaList().remove(a2);
                facadeEscolar.flush();
                registro.setActividadAlineada(null);
            }
            
            return true;
        }catch(Exception e){
            LOG.log(Level.SEVERE, "No se pudo alinear el registro con la actividad.", e);
            return false;
        }
    }
}
