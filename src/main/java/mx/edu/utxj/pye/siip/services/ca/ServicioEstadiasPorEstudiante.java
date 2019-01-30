/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.servlet.annotation.MultipartConfig;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EstadiasPorEstudiante;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.ca.DTOEstadiasPorEstudiante;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbEstadiasPorEstudiante;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
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
@MultipartConfig
public class ServicioEstadiasPorEstudiante implements EjbEstadiasPorEstudiante {

    @EJB    Facade facadeCA;
    @EJB    EjbModulos ejbModulos;
    @EJB    EjbPropiedades ep;
    @EJB    EjbFiscalizacion ejbFiscalizacion;
    @EJB    EjbMatriculaPeriodosEscolares ejbMatriculaPeriodosEscolares;
    @EJB    EjbOrganismosVinculados ejbOrganismosVinculados;
    
    @Inject Caster caster;

    @Override
    public List<DTOEstadiasPorEstudiante> getListaEstadiasPorEstudiante(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();

            List<DTOEstadiasPorEstudiante> listaDtoEstadiasPorEstudiante = new ArrayList<>();
            AreasUniversidad areaUniversidad;
            MatriculaPeriodosEscolares matriculaPeriodosEscolar;
            OrganismosVinculados organismoVinculado;
            EstadiasPorEstudiante estadiaPorEstudiante;
            DTOEstadiasPorEstudiante dtoEstadiaPorEstudiante;

            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFRow fila;

            try {
                Integer matricula = 0;
                String matriculaNueva = "000000";
                if ((primeraHoja.getSheetName().equals("Estadías"))) {
                    for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                        if (fila.getCell(1).getNumericCellValue() != 0) {
                            areaUniversidad = new AreasUniversidad();
                            organismoVinculado = new OrganismosVinculados();
                            matriculaPeriodosEscolar = new MatriculaPeriodosEscolares();
                            estadiaPorEstudiante = new EstadiasPorEstudiante();
                            dtoEstadiaPorEstudiante = new DTOEstadiasPorEstudiante();

                            if (fila.getCell(1).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(1).getCellTypeEnum()) {
                                    case FORMULA:
                                        estadiaPorEstudiante.setCicloEscolar((int) fila.getCell(1).getNumericCellValue());
                                        dtoEstadiaPorEstudiante.setCicloEscolar(fila.getCell(2).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Ciclo escolar en la columna: " + (2 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(4).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(4).getCellTypeEnum()) {
                                    case FORMULA:
                                        estadiaPorEstudiante.setGeneracion((short) ((int) fila.getCell(4).getNumericCellValue()));
                                        dtoEstadiaPorEstudiante.setGeneracion(fila.getCell(5).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Generación en la columna: " + (5 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(7).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(7).getCellTypeEnum()) {
                                    case FORMULA:
                                        matriculaPeriodosEscolar.setPeriodo((int) fila.getCell(7).getNumericCellValue());
                                        dtoEstadiaPorEstudiante.setPeriodoEscolar(fila.getCell(8).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Periodo Escolar en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                            }

//                    Matricula
                            if (fila.getCell(9).getCellTypeEnum() == CellType.NUMERIC || fila.getCell(9).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(9).getCellTypeEnum()) {
                                    case NUMERIC:
                                        matricula = (int) fila.getCell(9).getNumericCellValue();
                                        if (matricula > 20000 && matricula < 99999) {
                                            matriculaNueva = "0" + String.valueOf(matricula);
                                        } else {
                                            matriculaNueva = String.valueOf(matricula);
                                        }
                                        matriculaPeriodosEscolar.setMatricula(matriculaNueva);
                                        break;
                                    case STRING:
                                        String maS = (String) fila.getCell(9).getStringCellValue();
                                        if (Integer.parseInt(maS) > 20000 && Integer.parseInt(maS) < 99999) {
                                            matriculaNueva = "0" + String.valueOf(maS);
                                        } else {
                                            matriculaNueva = String.valueOf(maS);
                                        }
                                        matriculaPeriodosEscolar.setMatricula(matriculaNueva);
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
                                        areaUniversidad.setArea((short) ((int) fila.getCell(11).getNumericCellValue()));
                                        areaUniversidad.setNombre(fila.getCell(12).getStringCellValue());
                                        estadiaPorEstudiante.setProgramaEducativo(areaUniversidad.getArea());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Programa Educativo en la columna: " + (12 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(14).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(14).getCellTypeEnum()) {
                                    case FORMULA:
                                        organismoVinculado.setEmpresa((int) fila.getCell(14).getNumericCellValue());
                                        organismoVinculado.setNombre(fila.getCell(15).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Empresa en la columna: " + (15 + 1) + " y fila: " + (i + 1));
                            }

                            estadiaPorEstudiante.setMatriculaPeriodosEscolares(matriculaPeriodosEscolar);
                            estadiaPorEstudiante.setEmpresa(organismoVinculado);
                            dtoEstadiaPorEstudiante.setAreasUniversidad(areaUniversidad);
                            dtoEstadiaPorEstudiante.setEstadiasPorEstudiante(estadiaPorEstudiante);

                            listaDtoEstadiasPorEstudiante.add(dtoEstadiaPorEstudiante);
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
                        return listaDtoEstadiasPorEstudiante;
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
    public void guardaEstadiasPorEstudiante(List<DTOEstadiasPorEstudiante> listaEstadiasPorEstudiante, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
        List<String> listaCondicional = new ArrayList<>();
        listaEstadiasPorEstudiante.forEach((estadiaPorEstudiante) -> {
            facadeCA.setEntityClass(EstadiasPorEstudiante.class);
            EstadiasPorEstudiante estadiaEncontrada = getEstadiaPorEstudiante(estadiaPorEstudiante.getEstadiasPorEstudiante());
            Boolean registroAlmacenado = false;

            if (estadiaEncontrada != null) {
                listaCondicional.add(estadiaPorEstudiante.getPeriodoEscolar() + " " + estadiaPorEstudiante.getEstadiasPorEstudiante().getMatriculaPeriodosEscolares().getMatricula());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                if(ejbModulos.getEventoRegistro().equals(estadiaEncontrada.getRegistros().getEventoRegistro())){
                    estadiaPorEstudiante.getEstadiasPorEstudiante().setRegistro(estadiaEncontrada.getRegistro());
                    estadiaPorEstudiante.getEstadiasPorEstudiante().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(estadiaPorEstudiante.getEstadiasPorEstudiante().getMatriculaPeriodosEscolares().getMatricula(), estadiaPorEstudiante.getEstadiasPorEstudiante().getMatriculaPeriodosEscolares().getPeriodo()));
                    estadiaPorEstudiante.getEstadiasPorEstudiante().getEmpresa().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(estadiaPorEstudiante.getEstadiasPorEstudiante().getEmpresa().getEmpresa()));
                    facadeCA.edit(estadiaPorEstudiante.getEstadiasPorEstudiante());
                } else {
                    listaCondicional.remove(estadiaPorEstudiante.getPeriodoEscolar() + " " + estadiaPorEstudiante.getEstadiasPorEstudiante().getMatriculaPeriodosEscolares().getMatricula());
                }
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                estadiaPorEstudiante.getEstadiasPorEstudiante().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(estadiaPorEstudiante.getEstadiasPorEstudiante().getMatriculaPeriodosEscolares().getMatricula(), estadiaPorEstudiante.getEstadiasPorEstudiante().getMatriculaPeriodosEscolares().getPeriodo()));
                estadiaPorEstudiante.getEstadiasPorEstudiante().getEmpresa().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(estadiaPorEstudiante.getEstadiasPorEstudiante().getEmpresa().getEmpresa()));
                estadiaPorEstudiante.getEstadiasPorEstudiante().setRegistro(registro.getRegistro());
                facadeCA.create(estadiaPorEstudiante.getEstadiasPorEstudiante());
            }
            facadeCA.flush();
        });
        Messages.addGlobalInfo("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public EstadiasPorEstudiante getEstadiaPorEstudiante(EstadiasPorEstudiante estadiasPorEstudiante) {
        EstadiasPorEstudiante estadiaEncontrada = new EstadiasPorEstudiante();
        TypedQuery<EstadiasPorEstudiante> query = facadeCA.getEntityManager().createQuery("SELECT e FROM EstadiasPorEstudiante e JOIN e.matriculaPeriodosEscolares m WHERE e.cicloEscolar = :cicloEscolar AND m.periodo = :periodoEscolar AND m.matricula = :matricula", EstadiasPorEstudiante.class);
        query.setParameter("cicloEscolar", estadiasPorEstudiante.getCicloEscolar());
        query.setParameter("periodoEscolar", estadiasPorEstudiante.getMatriculaPeriodosEscolares().getPeriodo());
        query.setParameter("matricula", estadiasPorEstudiante.getMatriculaPeriodosEscolares().getMatricula());
        try {
            estadiaEncontrada = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            estadiaEncontrada = null;
            System.out.println(ex.toString());
        }
        return estadiaEncontrada;
    }

    @Override
    public List<PeriodosEscolares> getPeriodosConregistro(RegistrosTipo registrosTipo, EventosRegistros eventoRegistro, AreasUniversidad area) {
        List<Integer> claves = facadeCA.getEntityManager().createQuery("SELECT epe.matriculaPeriodosEscolares.periodo FROM EstadiasPorEstudiante epe INNER JOIN epe.registros r WHERE r.tipo.registroTipo=:tipo AND r.area = :area", Integer.class)
                .setParameter("tipo", registrosTipo.getRegistroTipo())
                .setParameter("area", area.getArea())
                .getResultList();
        
        List<PeriodosEscolares> l = new ArrayList<>();
        if(claves.isEmpty()){
            l = facadeCA.getEntityManager().createQuery("SELECT p FROM PeriodosEscolares p WHERE (:mes BETWEEN p.mesInicio.numero AND p.mesFin.numero) AND (p.anio = :anio)",PeriodosEscolares.class)
                    .setParameter("mes", ejbModulos.getNumeroMes(eventoRegistro.getMes()))
                    .setParameter("anio", eventoRegistro.getEjercicioFiscal().getAnio())
                    .getResultList();
        }else{
            l = facadeCA.getEntityManager().createQuery("SELECT periodo FROM PeriodosEscolares periodo WHERE periodo.periodo IN :claves ORDER BY periodo.periodo desc", PeriodosEscolares.class)
                    .setParameter("claves", claves)
                    .getResultList();
        }
        return l;
    }

    @Override
    public List<DTOEstadiasPorEstudiante> getListaRegistrosPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo, RegistrosTipo registrosTipo) {
        if(evento == null || claveArea == null || periodo == null){
            return null;
        }
        List<Short> areas = new ArrayList<>();
        AreasUniversidad area = facadeCA.getEntityManager().find(AreasUniversidad.class, claveArea);

        Short programaCategoria = (short)ep.leerPropiedadEntera("modulosRegistroProgramaEducativoCategoria").orElse(9);
        if (Objects.equals(area.getCategoria().getCategoria(), programaCategoria)) {
            areas.add(claveArea);
        }else{
            areas = facadeCA.getEntityManager().createQuery("SELECT au FROM AreasUniversidad au WHERE au.areaSuperior=:areaSuperior AND au.vigente='1'", AreasUniversidad.class)
                    .setParameter("areaSuperior", area.getArea())
                    .getResultStream()
                    .map(au -> au.getArea())
                    .collect(Collectors.toList());
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioEstadiasPorEstudiante.getListaRegistrosPorEventoAreaPeriodo() Claves areas: " + areas.toString());
        }
        List<DTOEstadiasPorEstudiante> l = new ArrayList<>();
        List<EstadiasPorEstudiante> entities = facadeCA.getEntityManager().createQuery("SELECT epe FROM EstadiasPorEstudiante epe INNER JOIN epe.registros r INNER JOIN r.tipo t INNER JOIN r.eventoRegistro er WHERE epe.programaEducativo in :areas AND epe.matriculaPeriodosEscolares.periodo = :periodo AND t.registroTipo=:tipo ORDER BY epe.programaEducativo, epe.matriculaPeriodosEscolares.matricula", EstadiasPorEstudiante.class)
                .setParameter("areas", areas)
                .setParameter("periodo", periodo.getPeriodo())
                .setParameter("tipo", registrosTipo.getRegistroTipo())
                .getResultList();
        entities.forEach(e -> {
            facadeCA.getEntityManager().refresh(e);
            ActividadesPoa a = e.getRegistros().getActividadesPoaList().isEmpty() ? null : e.getRegistros().getActividadesPoaList().get(0);
                if(a != null){
                    l.add(new DTOEstadiasPorEstudiante(
                        e,
                        caster.cicloEscolarToString(facadeCA.getEntityManager().find(CiclosEscolares.class, e.getCicloEscolar())),
                        caster.periodoToString(facadeCA.getEntityManager().find(PeriodosEscolares.class, e.getMatriculaPeriodosEscolares().getPeriodo())),
                        caster.generacionToString(facadeCA.getEntityManager().find(Generaciones.class, e.getGeneracion())),
                        facadeCA.getEntityManager().find(AreasUniversidad.class, e.getProgramaEducativo()),
                        a
                    ));
                }else{
                    l.add(new DTOEstadiasPorEstudiante(
                        e,
                        caster.cicloEscolarToString(facadeCA.getEntityManager().find(CiclosEscolares.class, e.getCicloEscolar())),
                        caster.periodoToString(facadeCA.getEntityManager().find(PeriodosEscolares.class, e.getMatriculaPeriodosEscolares().getPeriodo())),
                        caster.generacionToString(facadeCA.getEntityManager().find(Generaciones.class, e.getGeneracion())),
                        facadeCA.getEntityManager().find(AreasUniversidad.class, e.getProgramaEducativo())
                    ));
                }
        });
        return l;
    }

    @Override
    public Map.Entry<List<PeriodosEscolares>, List<EventosRegistros>> comprobarEventoActual(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoActual, RegistrosTipo registrosTipo,AreasUniversidad area) throws PeriodoEscolarNecesarioNoRegistradoException {
        if(periodos==null || periodos.isEmpty()) periodos = getPeriodosConregistro(registrosTipo,eventoActual,area);
        if(periodos==null || periodos.isEmpty()) return null;
        if(eventoActual == null) eventoActual = ejbModulos.getEventoRegistro();
        if(eventoActual == null) return null;

        PeriodosEscolares reciente = periodos.get(0);
        Boolean existe = eventos.contains(eventoActual);
        if(!existe){
            if(eventos.size() <3){
                eventos = new ArrayList<>(Stream.concat(Stream.of(eventoActual), eventos.stream()).collect(Collectors.toList()));
            }else{
                PeriodosEscolares periodo = facadeCA.getEntityManager().find(PeriodosEscolares.class, reciente.getPeriodo() + 1);
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
    
}
