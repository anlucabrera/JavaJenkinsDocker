/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Meses;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesFormacionIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.ParticipantesActividadesFormacionIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.pye.DTOActFormacionIntegral;
import mx.edu.utxj.pye.siip.dto.pye.DTOParticipantesActFormInt;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbActFormacionIntegral;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
import org.apache.poi.ss.usermodel.DateUtil;
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
public class ServicioActFormacionIntegral implements EjbActFormacionIntegral{
   
    @EJB EjbModulos ejbModulos;
    @EJB Facade f;
    @EJB EjbOrganismosVinculados ejbOrganismosVinculados;
    @EJB EjbPropiedades ep;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @Inject Caster caster; 
    
    @Getter @Setter private List<Short> areas;
    
    String genero;
    
    @Override
    public List<DTOActFormacionIntegral> getListaActFormacionIntegral(String rutaArchivo) throws Throwable {
        
        List<Boolean> validarCelda = new ArrayList<>();
        List<String> datosInvalidos = new ArrayList<>();
        
        List<DTOActFormacionIntegral> listaDtoActFormacionIntegral = new ArrayList<>();
        ActividadesTipos actividadesTipos;
        EventosTipos eventosTipos;
        PeriodosEscolares periodosEscolares;
        ActividadesFormacionIntegral actividadesFormacionIntegral;
        DTOActFormacionIntegral dTOActFormacionIntegral;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        int act=0;
        int obj=0;
        int lug=0;
        int fac=0;
        
        try{
        if (primeraHoja.getSheetName().equals("Actividades Formación Integral")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((!"".equals(fila.getCell(0).getStringCellValue()))) {
                actividadesTipos = new ActividadesTipos();
                eventosTipos = new EventosTipos();
                periodosEscolares = new PeriodosEscolares();
                actividadesFormacionIntegral = new ActividadesFormacionIntegral();
                dTOActFormacionIntegral = new DTOActFormacionIntegral();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case FORMULA:
                        actividadesFormacionIntegral.setActividadFormacionIntegral(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
//                switch (fila.getCell(2).getCellTypeEnum()) {
//                    case STRING: 
//                         dTOActFormacionIntegral.setCicloEscolar(fila.getCell(2).getStringCellValue());
//                        break;
//                    default:
//                        break;
//                }
//                switch (fila.getCell(4).getCellTypeEnum()) {
//                    case STRING: 
//                         dTOActFormacionIntegral.setPeriodoEscolar(fila.getCell(4).getStringCellValue());
//                        break;
//                    default:
//                        break;
//                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case FORMULA: 
                        periodosEscolares.setPeriodo((int)fila.getCell(5).getNumericCellValue());
                        actividadesFormacionIntegral.setPeriodo(periodosEscolares.getPeriodo());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case NUMERIC: 
                         if (DateUtil.isCellDateFormatted(fila.getCell(6))) {
                         actividadesFormacionIntegral.setFechaInicio(fila.getCell(6).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                    case NUMERIC: 
                         if (DateUtil.isCellDateFormatted(fila.getCell(7))) {
                         actividadesFormacionIntegral.setFechaFin(fila.getCell(7).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(10).getCellTypeEnum()) {
                    case STRING:
                         actividadesFormacionIntegral.setNombre(fila.getCell(10).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(12).getCellTypeEnum()) {
                    case STRING: 
                        actividadesTipos.setNombre(fila.getCell(12).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(13).getCellTypeEnum()) {
                    case FORMULA: 
                        actividadesTipos.setActividadTipo((short)fila.getCell(13).getNumericCellValue());
                        actividadesFormacionIntegral.setActividadTipo(actividadesTipos);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(14).getCellTypeEnum()) {
                    case STRING: 
                        eventosTipos.setNombre(fila.getCell(14).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(15).getCellTypeEnum()) {
                    case FORMULA: 
                        eventosTipos.setEventoTipo((short)fila.getCell(15).getNumericCellValue());
                        actividadesFormacionIntegral.setEventoTipo(eventosTipos);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(16).getCellTypeEnum()) {
                    case STRING: 
                        actividadesFormacionIntegral.setOrganizaParticipa(fila.getCell(16).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(17).getCellTypeEnum()) {
                    case STRING: 
                        actividadesFormacionIntegral.setObjetivo(fila.getCell(17).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(18).getCellTypeEnum()) {
                    case STRING: 
                        actividadesFormacionIntegral.setLugarRealizacion(fila.getCell(18).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(21).getCellTypeEnum()) {
                    case FORMULA: 
                        actividadesFormacionIntegral.setDuracion(fila.getCell(21).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(22).getCellTypeEnum()) {
                    case STRING: 
                        actividadesFormacionIntegral.setFacilitadorEmpresa(fila.getCell(22).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(23).getCellTypeEnum()) {
                    case STRING: 
                        actividadesFormacionIntegral.setEquiposParticipantes(fila.getCell(23).getStringCellValue());
                        break;
                    case NUMERIC: 
                        String eqPart = Integer.toString((int) fila.getCell(23).getNumericCellValue());
                        actividadesFormacionIntegral.setEquiposParticipantes(eqPart);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(24).getCellTypeEnum()) {
                    case STRING: 
                        actividadesFormacionIntegral.setEquiposGanadores(fila.getCell(24).getStringCellValue());
                        break;
                    case NUMERIC: 
                        String eqGan = Integer.toString((int) fila.getCell(24).getNumericCellValue());
                        actividadesFormacionIntegral.setEquiposGanadores(eqGan);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(29).getCellTypeEnum()) {
                   case FORMULA:
                        act= (int) fila.getCell(29).getNumericCellValue();
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(30).getCellTypeEnum()) {
                    case FORMULA:
                        obj= (int) fila.getCell(30).getNumericCellValue();
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(31).getCellTypeEnum()) {
                   case FORMULA:
                        lug= (int) fila.getCell(31).getNumericCellValue();
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(32).getCellTypeEnum()) {
                   case FORMULA:
                        fac= (int) fila.getCell(32).getNumericCellValue();
                        break;
                    default:
                        break;
                }
                
                    if (act == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Nombre de la Actividad en la columna: <b>" + (5 + 1) + " y fila: " + (i + 1) + "</b> \n");
                    }
                    if (obj == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Objetivo en la columna: <b>" + (9 + 1) + " y fila: " + (i + 1) + "</b> \n");
                    }
                    if (lug == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Lugar en la columna: <b>" + (10 + 1) + " y fila: " + (i + 1) + "</b> \n");
                    }
                    if (fac == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Facilitador/Facilitadora en la columna: <b>" + (14 + 1) + " y fila: " + (i + 1) + "</b> \n");
                    }
                    
                    dTOActFormacionIntegral.setActividadesFormacionIntegral(actividadesFormacionIntegral);
                    listaDtoActFormacionIntegral.add(dTOActFormacionIntegral);
                }
            }
            libroRegistro.close();
            if (validarCelda.contains(false)) {
                    Messages.addGlobalError("<b>La hoja de registros de Actividades de Formación Integral contiene datos que no son válidos, verifique los datos de la plantilla</b>");
                    Messages.addGlobalError(datosInvalidos.toString());
                    return Collections.EMPTY_LIST;
                } else {
                    Messages.addGlobalInfo("<b>Hoja de Actividades de Formación Integral Validada favor de verificar sus datos antes de guardar su información</b>");
                    return listaDtoActFormacionIntegral;
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

    }

    @Override
    public void guardaActFormacionIntegral(List<DTOActFormacionIntegral> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
       
        List<String> listaCondicional = new ArrayList<>();
        lista.forEach((actFormacionIntegral) -> {
           
            if (ejbModulos.validaPeriodoRegistro(ejbModulos.getPeriodoEscolarActual(), actFormacionIntegral.getActividadesFormacionIntegral().getPeriodo())) {
                f.setEntityClass(ActividadesFormacionIntegral.class);
                ActividadesFormacionIntegral afi = getRegistroActividadesFormacionIntegral(actFormacionIntegral.getActividadesFormacionIntegral().getActividadFormacionIntegral());
                Boolean registroAlmacenado = false;
                if (afi != null) {
                    listaCondicional.add(actFormacionIntegral.getActividadesFormacionIntegral().getActividadFormacionIntegral());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    if(ejbModulos.comparaPeriodoRegistro(afi.getPeriodo(), actFormacionIntegral.getActividadesFormacionIntegral().getPeriodo())){
                        actFormacionIntegral.getActividadesFormacionIntegral().setRegistro(afi.getRegistro());
                        f.edit(actFormacionIntegral.getActividadesFormacionIntegral());
                        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + afi.getActividadFormacionIntegral());
                    } else{
                        Messages.addGlobalWarn("<b>No se pueden actualizar los registros con los siguientes datos: </b> " + afi.getActividadFormacionIntegral());
                    }
                } else {
                   Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    actFormacionIntegral.getActividadesFormacionIntegral().setRegistro(registro.getRegistro());
                    f.create(actFormacionIntegral.getActividadesFormacionIntegral());
                    Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b>");
                }
                f.flush();
            } else{
               Messages.addGlobalWarn("<b>No puede registrar información de periodos anteriores</b>");
            }
                
        });
       
    }
    
    @Override
    public ActividadesFormacionIntegral getRegistroActividadesFormacionIntegral(String actividadFormacionIntegral) {
        ActividadesFormacionIntegral actividadesFormacionIntegral = new ActividadesFormacionIntegral();
        TypedQuery<ActividadesFormacionIntegral> query = f.getEntityManager().createQuery("SELECT a FROM ActividadesFormacionIntegral a WHERE a.actividadFormacionIntegral = :actividadFormacionIntegral", ActividadesFormacionIntegral.class);
        query.setParameter("actividadFormacionIntegral", actividadFormacionIntegral);
        try {
            actividadesFormacionIntegral = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            actividadesFormacionIntegral = null;
            ex.toString();
        }
        return  actividadesFormacionIntegral;
    }
    
    @Override
    public Integer getRegistroActFormacionIntegralEspecifico(String actividadFormacionIntegral) {
        TypedQuery<ActividadesFormacionIntegral> query = f.getEntityManager().createNamedQuery("ActividadesFormacionIntegral.findByActividadFormacionIntegral", ActividadesFormacionIntegral.class);
        query.setParameter("actividadFormacionIntegral", actividadFormacionIntegral);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }

    @Override
    public List<ActividadesTipos> getActividadesTiposAct() {
        List<ActividadesTipos> genLst = new ArrayList<>();
        TypedQuery<ActividadesTipos> query = f.getEntityManager().createQuery("SELECT a FROM ActividadesTipos a ORDER BY a.nombre ASC", ActividadesTipos.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }

    @Override
    public List<EventosTipos> getEventosTiposAct() {
        List<EventosTipos> genLst = new ArrayList<>();
        TypedQuery<EventosTipos> query = f.getEntityManager().createQuery("SELECT e FROM EventosTipos e ORDER BY e.nombre ASC", EventosTipos.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }

    @Override
    public List<PeriodosEscolares> getPeriodosConregistro() {
        List<Integer> claves = f.getEntityManager().createQuery("SELECT a FROM ActividadesFormacionIntegral a", ActividadesFormacionIntegral.class)
                .getResultStream()
                .map(a -> a.getPeriodo())
                .collect(Collectors.toList());
        
         if(claves.isEmpty())
       {
           claves.add(0, ejbModulos.getPeriodoEscolarActual().getPeriodo());
       
       }
         
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

        return f.getEntityManager().createQuery("SELECT er from EventosRegistros er INNER JOIN er.ejercicioFiscal ef WHERE ef.anio=:anio AND er.mes in :meses AND er.fechaInicio <= :fecha ORDER BY er.fechaInicio DESC, er.fechaFin DESC", EventosRegistros.class)
                .setParameter("fecha", new Date())
                .setParameter("anio", periodo.getAnio())
                .setParameter("meses", meses)
                .getResultList();
    }

    @Override
    public List<DTOActFormacionIntegral> getListaRegistrosPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo) {
           //verificar que los parametros no sean nulos
        if(evento == null || claveArea == null || periodo == null){
            return null;
        }
        
        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        List<DTOActFormacionIntegral> l = new ArrayList<>();
        List<ActividadesFormacionIntegral> entities = new ArrayList<>();
      
        areas = ejbModulos.getAreasDependientes(claveArea);
        entities = f.getEntityManager().createQuery("SELECT a FROM ActividadesFormacionIntegral a INNER JOIN a.registros reg INNER JOIN reg.eventoRegistro er WHERE er.eventoRegistro=:evento AND a.periodo =:periodo AND reg.area IN :areas", ActividadesFormacionIntegral.class)
                .setParameter("evento", evento.getEventoRegistro())
                .setParameter("periodo", periodo.getPeriodo())
                .setParameter("areas", areas)
                .getResultList();
        
        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            
            Registros reg = f.getEntityManager().find(Registros.class, e.getRegistro());
            //ActividadesPoa a = e.getRegistros().getActividadesPoaList().isEmpty()?null:e.getRegistros().getActividadesPoaList().get(0);
            ActividadesPoa a = reg.getActividadesPoaList().isEmpty()?null:reg.getActividadesPoaList().get(0);
            PeriodosEscolares p = f.getEntityManager().find(PeriodosEscolares.class, e.getPeriodo());
            String strDateFormat = "yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            String cicloEscolar = sdf.format(p.getCiclo().getInicio()) + " - " + sdf.format(p.getCiclo().getFin());
            l.add(new DTOActFormacionIntegral(
                    e,
                    f.getEntityManager().find(PeriodosEscolares.class, e.getPeriodo()),
                    a,
                    cicloEscolar));
        });
        return l;
    }

    @Override
    public List<DTOParticipantesActFormInt> getListaRegistrosPorEventoAreaPeriodoPart(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo) {
           //verificar que los parametros no sean nulos
        if(evento == null || claveArea == null || periodo == null){
            return null;
        }
        areas = ejbModulos.getAreasDependientes(claveArea);

        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        List<DTOParticipantesActFormInt> l = new ArrayList<>();
        List<ParticipantesActividadesFormacionIntegral> entities = f.getEntityManager().createQuery("SELECT p FROM ParticipantesActividadesFormacionIntegral p INNER JOIN p.actividadFormacionIntegral a INNER JOIN p.registros reg INNER JOIN reg.eventoRegistro er WHERE er.eventoRegistro=:evento AND a.periodo=:periodo AND reg.area IN :areas ORDER BY p.matriculaPeriodosEscolares.matricula ASC", ParticipantesActividadesFormacionIntegral.class)
                .setParameter("evento", evento.getEventoRegistro())
                .setParameter("periodo", periodo.getPeriodo())
                .setParameter("areas", areas)
                .getResultList();
     

        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            Registros reg = f.getEntityManager().find(Registros.class, e.getRegistro());
            MatriculaPeriodosEscolares mat = f.getEntityManager().find(MatriculaPeriodosEscolares.class, e.getMatriculaPeriodosEscolares().getRegistro());
            genero = mat.getCurp().substring(10, 11);
            AreasUniversidad progEdu = f.getEntityManager().find(AreasUniversidad.class, mat.getProgramaEducativo());
            ActividadesPoa a = reg.getActividadesPoaList().isEmpty()?null:reg.getActividadesPoaList().get(0);

            l.add(new DTOParticipantesActFormInt(
                    e,
                    mat,
                    genero,
                    progEdu,
                    a));
        });
        
        return l;
    }

    @Override
    public Map.Entry<List<PeriodosEscolares>, List<EventosRegistros>> comprobarEventoActual(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoActual) throws PeriodoEscolarNecesarioNoRegistradoException {
       
        if(periodos==null || periodos.isEmpty()) periodos = getPeriodosConregistro();
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
    public List<Integer> buscaRegistroParticipantesActFormInt(String clave) throws Throwable {
       List<Integer> registros = new ArrayList<>();
        try {
            registros = f.getEntityManager().createQuery("SELECT p FROM ParticipantesActividadesFormacionIntegral p WHERE p.actividadFormacionIntegral.actividadFormacionIntegral = :clave", ParticipantesActividadesFormacionIntegral.class)
                    .setParameter("clave", clave)
                    .getResultStream()
                    .map(s -> s.getRegistro())
                    .collect(Collectors.toList());
            return registros;
            
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    @Override
    public List<Integer> buscaRegistroEvidenciasPartActFormInt(String clave) throws Throwable {
        List<Integer> registros = new ArrayList<>();
        List<Integer> evidencias = new ArrayList<>();
        try {
            registros = f.getEntityManager().createQuery("SELECT p FROM ParticipantesActividadesFormacionIntegral p WHERE p.actividadFormacionIntegral.actividadFormacionIntegral= :clave", ParticipantesActividadesFormacionIntegral.class)
                    .setParameter("clave", clave)
                    .getResultStream()
                    .map(s -> s.getRegistro())
                    .collect(Collectors.toList());
           
            registros.stream().forEach((reg)-> {
		
                List<Integer> evidenciasReg = f.getEntityManager().createQuery("SELECT e FROM Evidencias e INNER JOIN e.registrosList r WHERE r.registro = :registro",  Evidencias.class)
                    .setParameter("registro", reg)
                    .getResultStream()
                    .map(s -> s.getEvidencia())
                    .collect(Collectors.toList());
              
                    evidenciasReg.stream().forEach((evidencia)-> {
                     
                     evidencias.add(evidencia);
                     
                 });
               
            });
           
                return evidencias;
            
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<AreasUniversidad> getAFIAreasConRegistroMensualGeneral(String mes) {
         try {
            List<Short> areas = new ArrayList<>();

            areas = f.getEntityManager().createQuery("SELECT r.area FROM ActividadesFormacionIntegral a INNER JOIN a.registros r INNER JOIN r.eventoRegistro e WHERE e.mes = :mes GROUP BY r.area", Short.class)
                    .setParameter("mes", mes)
                    .getResultList();

            if (!areas.isEmpty()) {
                return f.getEntityManager().createQuery("SELECT a FROM AreasUniversidad a WHERE a.area IN :areas", AreasUniversidad.class)
                        .setParameter("areas", areas)
                        .getResultList();
            } else {
                return Collections.EMPTY_LIST;
            }
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public ActividadesFormacionIntegral actualizarActFormInt(ActividadesFormacionIntegral nuevaActFormInt) throws Throwable {
        f.setEntityClass(ActividadesFormacionIntegral.class);
        f.edit(nuevaActFormInt);
        f.flush();
        Messages.addGlobalInfo("El registro se ha actualizado correctamente");
        return nuevaActFormInt;
    }

    @Override
    public List<DTOActFormacionIntegral> getListaRegistrosAFI(Short claveArea) {
        //verificar que el parametro no sea nulo
        if(claveArea == null){
            return null;
        }
        
        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        List<DTOActFormacionIntegral> l = new ArrayList<>();
        List<ActividadesFormacionIntegral> entities = new ArrayList<>();
      
        if(claveArea==6 || claveArea==9){
        entities = f.getEntityManager().createQuery("SELECT a FROM ActividadesFormacionIntegral a", ActividadesFormacionIntegral.class)
                .getResultList();
        }
        else{
            
        areas = ejbModulos.getAreasDependientes(claveArea);
        
        entities = f.getEntityManager().createQuery("SELECT a FROM ActividadesFormacionIntegral a INNER JOIN a.registros reg WHERE reg.area IN :areas", ActividadesFormacionIntegral.class)
                .setParameter("areas", areas)
                .getResultList();
        }
        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            
            Registros reg = f.getEntityManager().find(Registros.class, e.getRegistro());
            //ActividadesPoa a = e.getRegistros().getActividadesPoaList().isEmpty()?null:e.getRegistros().getActividadesPoaList().get(0);
            ActividadesPoa a = reg.getActividadesPoaList().isEmpty()?null:reg.getActividadesPoaList().get(0);
            PeriodosEscolares p = f.getEntityManager().find(PeriodosEscolares.class, e.getPeriodo());
            String strDateFormat = "yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            String cicloEscolar = sdf.format(p.getCiclo().getInicio()) + " - " + sdf.format(p.getCiclo().getFin());
            l.add(new DTOActFormacionIntegral(
                    e,
                    f.getEntityManager().find(PeriodosEscolares.class, e.getPeriodo()),
                    a,
                    cicloEscolar));
        });
        return l;
    }

    @Override
    public List<DTOParticipantesActFormInt> getListaRegistrosPAFI(Short claveArea) {
        //verificar que el parametro no sea nulo
        if(claveArea == null){
            return null;
        }
        areas = ejbModulos.getAreasDependientes(claveArea);

        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        List<DTOParticipantesActFormInt> l = new ArrayList<>();
        List<ParticipantesActividadesFormacionIntegral> entities = new ArrayList<>();
        
        if(claveArea==6 || claveArea==9){
        entities = f.getEntityManager().createQuery("SELECT p FROM ParticipantesActividadesFormacionIntegral p ORDER BY p.matriculaPeriodosEscolares.matricula ASC", ParticipantesActividadesFormacionIntegral.class)
                .getResultList();
        }
        else{
        
        entities = f.getEntityManager().createQuery("SELECT p FROM ParticipantesActividadesFormacionIntegral p INNER JOIN p.actividadFormacionIntegral a INNER JOIN p.registros reg INNER JOIN reg.eventoRegistro er WHERE reg.area IN :areas ORDER BY p.matriculaPeriodosEscolares.matricula ASC", ParticipantesActividadesFormacionIntegral.class)
                .setParameter("areas", areas)
                .getResultList();
     
         }
        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            Registros reg = f.getEntityManager().find(Registros.class, e.getRegistro());
            MatriculaPeriodosEscolares mat = f.getEntityManager().find(MatriculaPeriodosEscolares.class, e.getMatriculaPeriodosEscolares().getRegistro());
            genero = mat.getCurp().substring(10, 11);
            AreasUniversidad progEdu = f.getEntityManager().find(AreasUniversidad.class, mat.getProgramaEducativo());
            ActividadesPoa a = reg.getActividadesPoaList().isEmpty()?null:reg.getActividadesPoaList().get(0);

            l.add(new DTOParticipantesActFormInt(
                    e,
                    mat,
                    genero,
                    progEdu,
                    a));
        });
        
        return l;
    }
}
