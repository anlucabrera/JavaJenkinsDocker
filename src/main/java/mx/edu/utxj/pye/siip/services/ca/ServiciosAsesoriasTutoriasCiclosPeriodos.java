/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
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
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Meses;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.AsesoriasTutoriasCicloPeriodos;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaAsesoriasTutoriasCicloPeriodos;
import mx.edu.utxj.pye.siip.dto.escolar.DTOAsesoriasTutoriasCicloPeriodos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbAsesoriasTutoriasCiclosPeriodos;
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
public class ServiciosAsesoriasTutoriasCiclosPeriodos implements EjbAsesoriasTutoriasCiclosPeriodos {

    @EJB Facade facadeEscolar;
    @EJB EjbModulos ejbModulos;
    @EJB EjbAdministracionEncuestas eJBAdministracionEncuestas;
    @EJB Facade f;
    @EJB EjbPropiedades ep;
    @Inject Caster caster;

//    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
//    private EntityManager em;

    @Override
    public List<DTOAsesoriasTutoriasCicloPeriodos> getListaAsesoriasTutorias(String rutaArchivo) throws Throwable {
//        ListaAsesoriasTutoriasCicloPeriodos listaAsesoriasTutoriasCicloPeriodos = new ListaAsesoriasTutoriasCicloPeriodos();

        List<DTOAsesoriasTutoriasCicloPeriodos> listaDtoAsesoriasTutoriasCicloPeriodos = new ArrayList<>();
        DTOAsesoriasTutoriasCicloPeriodos dtoAsesoriaTutoriaCicloPeriodo;
        AsesoriasTutoriasCicloPeriodos asesoriaTutoriaCicloPeriodo;
        AreasUniversidad areaUniversidad;

        File excelAsesoriaTutoria = new File(rutaArchivo);
        XSSFWorkbook workBookAsesoriaTutoria;
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

                    switch (fila.getCell(6).getCellTypeEnum()) {
                        case FORMULA:
                            asesoriaTutoriaCicloPeriodo.setPeriodoEscolar((int) fila.getCell(6).getNumericCellValue());
                            dtoAsesoriaTutoriaCicloPeriodo.setPeriodoEscolar(fila.getCell(5).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(8).getCellTypeEnum()) {
                        case FORMULA:
                            asesoriaTutoriaCicloPeriodo.setTipoActividad(fila.getCell(8).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(12).getCellTypeEnum()) {
                        case FORMULA:
                            areaUniversidad.setArea((short) fila.getCell(12).getNumericCellValue());
                            asesoriaTutoriaCicloPeriodo.setProgramaEducativo(areaUniversidad.getArea());
                            areaUniversidad.setNombre(fila.getCell(10).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(14).getCellTypeEnum()) {
                        case FORMULA:
                            asesoriaTutoriaCicloPeriodo.setCuatrimestre(String.valueOf((int) fila.getCell(14).getNumericCellValue()));
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(16).getCellTypeEnum()) {
                        case FORMULA:
                            asesoriaTutoriaCicloPeriodo.setGrupo(fila.getCell(16).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(17).getCellTypeEnum()) {
                        case STRING:
                            asesoriaTutoriaCicloPeriodo.setAsunto(fila.getCell(17).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(19).getCellTypeEnum()) {
                        case FORMULA:
                            asesoriaTutoriaCicloPeriodo.setTipo(fila.getCell(19).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(20).getCellTypeEnum()) {
                        case NUMERIC:
                            asesoriaTutoriaCicloPeriodo.setNoTutoriasAsesorias((short) fila.getCell(20).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(21).getCellTypeEnum()) {
                        case NUMERIC:
                            asesoriaTutoriaCicloPeriodo.setAsistentes((short) fila.getCell(21).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    dtoAsesoriaTutoriaCicloPeriodo.setAsesoriasTutoriasCicloPeriodos(asesoriaTutoriaCicloPeriodo);
                    dtoAsesoriaTutoriaCicloPeriodo.setAreasUniversidad(areaUniversidad);

                    listaDtoAsesoriasTutoriasCicloPeriodos.add(dtoAsesoriaTutoriaCicloPeriodo);
                }
            }
            workBookAsesoriaTutoria.close();
//            listaAsesoriasTutoriasCicloPeriodos.setAsesoriasTutoriasCicloPeriodos(listaDtoAsesoriasTutoriasCicloPeriodos);
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            workBookAsesoriaTutoria.close();
            excelAsesoriaTutoria.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaDtoAsesoriasTutoriasCicloPeriodos;
    }

    @Override
    public void guardaAsesoriasTutorias(List<DTOAsesoriasTutoriasCicloPeriodos> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        lista.forEach((asesTut) -> {
            if (ejbModulos.validaPeriodoRegistro(eJBAdministracionEncuestas.getPeriodoActual(), asesTut.getAsesoriasTutoriasCicloPeriodos().getPeriodoEscolar())) {
                facadeEscolar.setEntityClass(AsesoriasTutoriasCicloPeriodos.class);
                AsesoriasTutoriasCicloPeriodos asesTutEncontrada = getRegistroAsesoriaTutoriaCicloPeriodo(asesTut.getAsesoriasTutoriasCicloPeriodos());
                Boolean registroAlmacenado = false;
                if (asesTutEncontrada != null) {
                    listaCondicional.add(asesTutEncontrada.getAsunto() + " " + asesTutEncontrada.getCuatrimestre() + " " + asesTutEncontrada.getGrupo());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    asesTut.getAsesoriasTutoriasCicloPeriodos().setRegistro(asesTutEncontrada.getRegistro());
                    facadeEscolar.edit(asesTut.getAsesoriasTutoriasCicloPeriodos());
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
        AsesoriasTutoriasCicloPeriodos asesTutCPEnviada;
        TypedQuery<AsesoriasTutoriasCicloPeriodos> query = f.getEntityManager().createQuery("SELECT a FROM AsesoriasTutoriasCicloPeriodos AS a WHERE a.periodoEscolar = :periodoEscolar AND a.programaEducativo = :programaEducativo AND a.cuatrimestre = :cuatrimestre AND a.grupo = :grupo AND a.tipoActividad = :tipoActividad AND a.tipo = :tipo", AsesoriasTutoriasCicloPeriodos.class);
        query.setParameter("periodoEscolar", asesoriaTutoriaCicloPeriodo.getPeriodoEscolar());
        query.setParameter("programaEducativo", asesoriaTutoriaCicloPeriodo.getProgramaEducativo());
        query.setParameter("cuatrimestre", asesoriaTutoriaCicloPeriodo.getCuatrimestre());
        query.setParameter("grupo", asesoriaTutoriaCicloPeriodo.getGrupo());
        query.setParameter("tipoActividad", asesoriaTutoriaCicloPeriodo.getTipoActividad());
        query.setParameter("tipo", asesoriaTutoriaCicloPeriodo.getTipo());
        try {
            asesTutCPEnviada = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            asesTutCPEnviada = null;
            ex.toString();
        }
        return asesTutCPEnviada;
    }

    @Override
    public List<PeriodosEscolares> getPeriodosConregistro() {
        List<Integer> claves = f.getEntityManager().createQuery("SELECT atp FROM AsesoriasTutoriasCicloPeriodos atp", AsesoriasTutoriasCicloPeriodos.class)
                .getResultStream()
                .map(atp -> atp.getPeriodoEscolar())
                .collect(Collectors.toList());


        return f.getEntityManager().createQuery("SELECT periodo FROM PeriodosEscolares periodo WHERE periodo.periodo IN :claves ORDER BY periodo.periodo desc", PeriodosEscolares.class)
                .setParameter("claves", claves)
                .getResultList();
    }

    @Override
    public List<EventosRegistros> getEventosPorPeriodo(PeriodosEscolares periodo) {
        if(periodo == null){
            return null;
        }

        List<String> meses = f.getEntityManager().createQuery("SELECT m FROM Meses m where m.numero BETWEEN :inicio AND :fin ORDER BY m.numero", Meses.class)
                .setParameter("inicio", periodo.getMesInicio().getNumero())
                .setParameter("fin", periodo.getMesFin().getNumero())
                .getResultList()
                .stream()
                .map(m -> m.getMes())
                .collect(Collectors.toList());

//        meses.forEach(m -> System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getEventosPorPeriodo() mes: " + m));

        return f.getEntityManager().createQuery("SELECT er from EventosRegistros er INNER JOIN er.ejercicioFiscal ef WHERE ef.anio=:anio AND er.mes in :meses AND er.fechaInicio < :fecha ORDER BY er.fechaInicio DESC, er.fechaFin DESC", EventosRegistros.class)
                .setParameter("fecha", new Date())
                .setParameter("anio", periodo.getAnio())
                .setParameter("meses", meses)
                .getResultList();

    }

    @Override
    public List<DTOAsesoriasTutoriasCicloPeriodos> getListaRegistrosPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo) {
        //verificar que los parametros no sean nulos
        if(evento == null || claveArea == null || periodo == null){
            return null;
        }
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaRegistrosPorEventoArea(1) evento: " + evento);
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaRegistrosPorEventoArea(2) area: " + claveArea);
        
        List<Short> areas = new ArrayList<>();
        
        //obtener la referencia al area operativa del trabajador
        AreasUniversidad area = f.getEntityManager().find(AreasUniversidad.class, claveArea);
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaRegistrosPorEventoArea(3) area: " + area);
        
        //comprobar si el area operativa es un programa educativo referenciar a su area superior para obtener la referencia al area academica
        Short programaCategoria = (short)ep.leerPropiedadEntera("modulosRegistroProgramaEducativoCategoria").orElse(9);
        if (Objects.equals(area.getCategoria().getCategoria(), programaCategoria)) {            
            area = f.getEntityManager().find(AreasUniversidad.class, area.getAreaSuperior());
//            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaRegistrosPorEventoArea(3a) area: " + area);
            
            //Obtener las claves de todas las areas que dependan de área academicoa
            areas = f.getEntityManager().createQuery("SELECT au FROM AreasUniversidad au WHERE au.areaSuperior=:areaSuperior AND au.vigente='1'", AreasUniversidad.class)
                    .setParameter("areaSuperior", area.getArea())
                    .getResultStream()
                    .map(au -> au.getArea())
                    .collect(Collectors.toList());
//            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaRegistrosPorEventoArea(3a) areas: " + areas);
        }else{//si no es area academica solo filtrar los datos del area operativa del trabajador
            areas.add(claveArea);
//            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaRegistrosPorEventoArea(3b) areas: " + areas);
        }
        
        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        List<DTOAsesoriasTutoriasCicloPeriodos> l = new ArrayList<>();
        List<AsesoriasTutoriasCicloPeriodos> entities = f.getEntityManager().createQuery("SELECT atc FROM AsesoriasTutoriasCicloPeriodos atc INNER JOIN atc.registros r INNER JOIN r.eventoRegistro er WHERE er.eventoRegistro=:evento AND atc.programaEducativo in :areas AND atc.periodoEscolar=:periodo ORDER BY atc.programaEducativo, atc.cuatrimestre, atc.grupo, atc.tipoActividad, atc.tipo", AsesoriasTutoriasCicloPeriodos.class)
                .setParameter("areas", areas)
                .setParameter("evento", evento.getEventoRegistro())
                .setParameter("periodo", periodo.getPeriodo())
                .getResultList();
        
//        entities.forEach(atc -> System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaRegistrosPorEventoArea(4) registro: " + atc));

        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            l.add(new DTOAsesoriasTutoriasCicloPeriodos(
                    e,
                    caster.periodoToString(f.getEntityManager().find(PeriodosEscolares.class, e.getPeriodoEscolar())),
                    f.getEntityManager().find(AreasUniversidad.class, e.getProgramaEducativo())));
        });
        
//        l.forEach(dto -> System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.getListaRegistrosPorEventoArea() dto: " + dto));

        return l;
    }

    @Override
    public Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> comprobarEventoActual(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoActual) throws PeriodoEscolarNecesarioNoRegistradoException{
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.comprobarEventoActual(1) actual: " + eventoActual);
        if(periodos==null || periodos.isEmpty()) periodos = getPeriodosConregistro();
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
                PeriodosEscolares periodo = f.getEntityManager().find(PeriodosEscolares.class, reciente.getPeriodo() + 1);
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
        return map.entrySet().iterator().next();
    }

    @Override
    public Boolean eliminarRegistro(DTOAsesoriasTutoriasCicloPeriodos registro) {
        Integer clave = registro.getAsesoriasTutoriasCicloPeriodos().getRegistro();
        f.remove(registro.getAsesoriasTutoriasCicloPeriodos().getRegistros());
        f.getEntityManager().detach(registro.getAsesoriasTutoriasCicloPeriodos());
        f.flush();
        
        return f.getEntityManager().find(AsesoriasTutoriasCicloPeriodos.class, clave) == null;
    }

}
