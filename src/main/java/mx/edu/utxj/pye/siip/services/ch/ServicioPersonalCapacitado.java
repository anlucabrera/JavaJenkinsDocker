/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ch;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Meses;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.PersonalCapacitado;
import mx.edu.utxj.pye.sgi.entity.pye2.ParticipantesPersonalCapacitado;
import mx.edu.utxj.pye.sgi.entity.pye2.PercapTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.PercapModalidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.caphum.DTOPerCapParticipantes;
import mx.edu.utxj.pye.siip.dto.caphum.DTOPersonalCapacitado;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbPersonalCapacitado;
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
public class ServicioPersonalCapacitado implements EjbPersonalCapacitado{
    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
    @EJB EjbPropiedades ep;
    @Inject Caster caster; 
    @Inject ControladorEmpleado controladorEmpleado;
    
    @Getter @Setter private List<Short> areas;
   
    @Override
    public List<DTOPersonalCapacitado> getListaPersonalCapacitado(String rutaArchivo) throws Throwable {
        List<Boolean> validarCelda = new ArrayList<>();
        List<String> datosInvalidos = new ArrayList<>();
               
        List<DTOPersonalCapacitado> listaDtoPersonalCapacitado = new ArrayList<>();
        PercapTipo percapTipo;
        PercapModalidad percapModalidad;
        PersonalCapacitado personalCapacitado;
        PeriodosEscolares periodosEscolares;
        DTOPersonalCapacitado dTOPersonalCapacitado;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        int cur=0;
        int emp=0;
        int obj=0;
        int lug=0;
        
        try {
        if (primeraHoja.getSheetName().equals("Registro Cursos")) {
        for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((fila.getCell(1).getNumericCellValue()!= 0)) {
                percapTipo = new PercapTipo();
                percapModalidad = new PercapModalidad();
                personalCapacitado = new PersonalCapacitado();
                periodosEscolares = new PeriodosEscolares();
                dTOPersonalCapacitado = new DTOPersonalCapacitado();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case FORMULA:
                        personalCapacitado.setCurso(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case STRING: 
                        personalCapacitado.setNombre(fila.getCell(2).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(3))) {
                         personalCapacitado.setFechaInicial(fila.getCell(3).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(4))) {
                         personalCapacitado.setFechaFinal(fila.getCell(4).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                    case FORMULA: 
                        personalCapacitado.setDuracion(fila.getCell(7).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(8).getCellTypeEnum()) {
                    case STRING:
                        percapTipo.setTipo(fila.getCell(8).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(9).getCellTypeEnum()) {
                    case FORMULA:
                        percapTipo.setPercapTipo((short) fila.getCell(9).getNumericCellValue());
                        personalCapacitado.setTipo(percapTipo);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(10).getCellTypeEnum()) {
                    case STRING:
                        percapModalidad.setModalidad(fila.getCell(10).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(11).getCellTypeEnum()) {
                    case FORMULA:
                        percapModalidad.setPercapMod((short) fila.getCell(11).getNumericCellValue());
                        personalCapacitado.setModalidad(percapModalidad);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(12).getCellTypeEnum()) {
                    case STRING:
                        personalCapacitado.setEmpresaImpartidora(fila.getCell(12).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(13).getCellTypeEnum()) {
                    case STRING:
                        personalCapacitado.setObjetivo(fila.getCell(13).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(14).getCellTypeEnum()) {
                    case STRING:
                        personalCapacitado.setLugarImparticion(fila.getCell(14).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(16).getCellTypeEnum()) {
                    case FORMULA:
                        BigDecimal inv= new BigDecimal((int)fila.getCell(16).getNumericCellValue());
                        personalCapacitado.setMontoInvertido(inv);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(20).getCellTypeEnum()) {
                    case FORMULA: 
                        periodosEscolares.setPeriodo((int)fila.getCell(20).getNumericCellValue());
                        personalCapacitado.setPeriodo(periodosEscolares.getPeriodo());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(25).getCellTypeEnum()) {
                    case FORMULA:
                        cur= (int) fila.getCell(25).getNumericCellValue();
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(26).getCellTypeEnum()) {
                   case FORMULA:
                        emp= (int) fila.getCell(26).getNumericCellValue();
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(27).getCellTypeEnum()) {
                    case FORMULA:
                        obj= (int) fila.getCell(27).getNumericCellValue();
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(28).getCellTypeEnum()) {
                   case FORMULA:
                        lug= (int) fila.getCell(28).getNumericCellValue();
                        break;
                    default:
                        break;
                }
                    if (cur == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Nombre del Curso en la columna: <b>" + (2 + 1) + " y fila: " + (i + 1) + "</b> \n");
                    }
                    if (emp == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Nombre empresa en la columna: <b>" + (10 + 1) + " y fila: " + (i + 1) + "</b> \n");
                    }
                    if (obj == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Objetivo en la columna: <b>" + (11 + 1) + " y fila: " + (i + 1) + "</b> \n");
                    }
                    if (lug == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Lugar en la columna: <b>" + (12 + 1) + " y fila: " + (i + 1) + "</b> \n");
                    }
                
                    dTOPersonalCapacitado.setPersonalCapacitado(personalCapacitado);
                    listaDtoPersonalCapacitado.add(dTOPersonalCapacitado);
                
                }
            }
            libroRegistro.close();
            if (validarCelda.contains(false)) {
                    Messages.addGlobalError("<b>La hoja de registros de Comisiones Académicas contiene datos que no son válidos, verifique los datos de la plantilla</b>");
                    Messages.addGlobalError(datosInvalidos.toString());
                    return Collections.EMPTY_LIST;
                } else {
                    Messages.addGlobalInfo("<b>Hoja de Comisiones Académicas Validada favor de verificar sus datos antes de guardar su información</b>");
                    return listaDtoPersonalCapacitado;
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
    public void guardaPersonalCapacitado(List<DTOPersonalCapacitado> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
        List<String> listaCondicional = new ArrayList<>();
        lista.forEach((personalCapacitado) -> {
           
            if (ejbModulos.validaPeriodoRegistro(ejbModulos.getPeriodoEscolarActual(), personalCapacitado.getPersonalCapacitado().getPeriodo())) {
                f.setEntityClass(PersonalCapacitado.class);
                PersonalCapacitado pc = getRegistroPersonalCapacitado(personalCapacitado.getPersonalCapacitado().getCurso());
                Boolean registroAlmacenado = false;
                if (pc != null) {
                    listaCondicional.add(personalCapacitado.getPersonalCapacitado().getCurso());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    if(ejbModulos.comparaPeriodoRegistro(pc.getPeriodo(), personalCapacitado.getPersonalCapacitado().getPeriodo())){
                        personalCapacitado.getPersonalCapacitado().setRegistro(pc.getRegistro());
                        f.edit(personalCapacitado.getPersonalCapacitado());
                        f.flush();
                        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + pc.getCurso());
                    } else{
                        Messages.addGlobalWarn("<b>No se pueden actualizar los registros con los siguientes datos: </b> " + pc.getCurso());
                    }
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    personalCapacitado.getPersonalCapacitado().setRegistro(registro.getRegistro());
                    f.create(personalCapacitado.getPersonalCapacitado());
                    f.flush();
                    Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b>");
                }
                f.flush();
            } else{
            
             Messages.addGlobalWarn("<b>No puede registrar información de periodos anteriores</b>");
            }   
        });
 }
   
    @Override
    public Integer getRegistroPersonalCapacitadoEspecifico(String curso) {
        TypedQuery<PersonalCapacitado> query = f.getEntityManager().createNamedQuery("PersonalCapacitado.findByCurso", PersonalCapacitado.class);
        query.setParameter("curso", curso);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
        
    }

    @Override
    public PersonalCapacitado getRegistroPersonalCapacitado(String personalCapacitado) {
        
        PersonalCapacitado personalCapacitado1 = new PersonalCapacitado();
        TypedQuery<PersonalCapacitado> query = f.getEntityManager().createNamedQuery("PersonalCapacitado.findByCurso", PersonalCapacitado.class);
        query.setParameter("curso", personalCapacitado);
        try {
            personalCapacitado1 = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            personalCapacitado1 = null;
            ex.toString();
        }
        return  personalCapacitado1;
    }
    
  
    @Override
    public List<PercapTipo> getPerCapTipoAct() {
        List<PercapTipo> genLst = new ArrayList<>();
        TypedQuery<PercapTipo> query = f.getEntityManager().createQuery("SELECT p FROM PercapTipo p", PercapTipo.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }

    @Override
    public List<PercapModalidad> getPerCapModalidadAct() {
        List<PercapModalidad> genLst = new ArrayList<>();
        TypedQuery<PercapModalidad> query = f.getEntityManager().createQuery("SELECT p FROM PercapModalidad p", PercapModalidad.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }

     @Override
    public List<PeriodosEscolares> getPeriodosConregistro() {
        List<Integer> claves = f.getEntityManager().createQuery("SELECT p FROM PersonalCapacitado p", PersonalCapacitado.class)
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
    public List<DTOPersonalCapacitado> getListaRegistrosPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo, Short secretariaAcademica) {
        //verificar que los parametros no sean nulos
        if(evento == null || claveArea == null || periodo == null){
           
             return null;
        }
        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        List<DTOPersonalCapacitado> l = new ArrayList<>();
        List<PersonalCapacitado> entities = new ArrayList<>();
        
        areas = ejbModulos.getAreasDependientes(claveArea);
        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        if (secretariaAcademica == (short)2) {
            entities = f.getEntityManager().createQuery("SELECT p FROM PersonalCapacitado p INNER JOIN p.registros reg INNER JOIN reg.eventoRegistro er WHERE er.eventoRegistro=:evento AND p.periodo =:periodo ", PersonalCapacitado.class)
                    .setParameter("evento", evento.getEventoRegistro())
                    .setParameter("periodo", periodo.getPeriodo())
                    
                 
                    .getResultList();
        } else {
            entities = f.getEntityManager().createQuery("SELECT p FROM PersonalCapacitado p INNER JOIN p.registros reg INNER JOIN reg.eventoRegistro er WHERE er.eventoRegistro=:evento AND p.periodo =:periodo AND reg.area IN :areas", PersonalCapacitado.class)
                    .setParameter("evento", evento.getEventoRegistro())
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("areas", areas)
                    .getResultList();
        }

        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            
            Registros reg = f.getEntityManager().find(Registros.class, e.getRegistro());
            ActividadesPoa a = reg.getActividadesPoaList().isEmpty()?null:reg.getActividadesPoaList().get(0);
            PeriodosEscolares p = f.getEntityManager().find(PeriodosEscolares.class, e.getPeriodo());
            String strDateFormat = "yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            String cicloe = sdf.format(p.getCiclo().getInicio()) + " - " + sdf.format(p.getCiclo().getFin());
            l.add(new DTOPersonalCapacitado(
                    e,
                    f.getEntityManager().find(PeriodosEscolares.class, e.getPeriodo()),
                    a,
                    cicloe));
        });
        
        return l;
    }

    @Override
    public List<DTOPerCapParticipantes> getListaRegistrosPorEventoAreaPeriodoPart(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo, Short secretariaAcademica) {
        //verificar que los parametros no sean nulos
        if(evento == null || claveArea == null || periodo == null){
            return null;
        }
        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        List<DTOPerCapParticipantes> l = new ArrayList<>();
        List<ParticipantesPersonalCapacitado> entities = new ArrayList<>();
        
        areas = ejbModulos.getAreasDependientes(claveArea);
        
        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        if (secretariaAcademica == (short)2) {
            entities = f.getEntityManager().createQuery("SELECT p FROM ParticipantesPersonalCapacitado p INNER JOIN p.percap a INNER JOIN p.registros reg INNER JOIN reg.eventoRegistro er WHERE er.eventoRegistro=:evento AND a.periodo=:periodo", ParticipantesPersonalCapacitado.class)
                    .setParameter("evento", evento.getEventoRegistro())
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultList();
        } else {
            entities = f.getEntityManager().createQuery("SELECT p FROM ParticipantesPersonalCapacitado p INNER JOIN p.percap a INNER JOIN p.registros reg INNER JOIN reg.eventoRegistro er WHERE er.eventoRegistro=:evento AND a.periodo=:periodo AND reg.area IN :areas", ParticipantesPersonalCapacitado.class)
                    .setParameter("evento", evento.getEventoRegistro())
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("areas", areas)
                    .getResultList();
        }

        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            Registros reg = f.getEntityManager().find(Registros.class, e.getRegistro());
            ActividadesPoa a = reg.getActividadesPoaList().isEmpty()?null:reg.getActividadesPoaList().get(0);
            l.add(new DTOPerCapParticipantes(
                    e,
                    f.getEntityManager().find(Personal.class, e.getPersonal()),
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
//     
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
    public List<Integer> buscaRegistroParticipantesPersonalCapacitado(String clave) throws Throwable {
        
       List<Integer> registros = new ArrayList<>();
        try {
            registros = f.getEntityManager().createQuery("SELECT p FROM ParticipantesPersonalCapacitado p WHERE p.percap.curso = :clave", ParticipantesPersonalCapacitado.class)
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
    public List<Integer> buscaRegistroEvidenciasPartPersonalCap(String clave) throws Throwable {
        List<Integer> registros = new ArrayList<>();
        List<Integer> evidencias = new ArrayList<>();
        try {
            registros = f.getEntityManager().createQuery("SELECT p FROM ParticipantesPersonalCapacitado p WHERE p.percap.curso= :clave", ParticipantesPersonalCapacitado.class)
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
    public PersonalCapacitado actualizarPerCap(PersonalCapacitado nuevoPerCap) throws Throwable {
        f.setEntityClass(PersonalCapacitado.class);
        f.edit(nuevoPerCap);
        f.flush();
        Messages.addGlobalInfo("El registro se ha actualizado correctamente");
        return nuevoPerCap;
    }

    @Override
    public List<DTOPersonalCapacitado> getRegistroPerCap(Short ejercicio) {
        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        List<DTOPersonalCapacitado> l = new ArrayList<>();
        List<PersonalCapacitado> entities = new ArrayList<>();
        
        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        entities = f.getEntityManager().createQuery("SELECT p FROM PersonalCapacitado p INNER JOIN p.registros reg INNER JOIN reg.eventoRegistro er WHERE er.ejercicioFiscal.anio = :ejercicio",  PersonalCapacitado.class)
                .setParameter("ejercicio", ejercicio)
                .getResultList();
        
        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            
            Registros reg = f.getEntityManager().find(Registros.class, e.getRegistro());
            ActividadesPoa a = reg.getActividadesPoaList().isEmpty()?null:reg.getActividadesPoaList().get(0);
            PeriodosEscolares p = f.getEntityManager().find(PeriodosEscolares.class, e.getPeriodo());
            String strDateFormat = "yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            String cicloe = sdf.format(p.getCiclo().getInicio()) + " - " + sdf.format(p.getCiclo().getFin());
            l.add(new DTOPersonalCapacitado(
                    e,
                    f.getEntityManager().find(PeriodosEscolares.class, e.getPeriodo()),
                    a,
                    cicloe));
        });
        
        return l;
    }

    @Override
    public List<DTOPerCapParticipantes> getRegistroPartCap(Short ejercicio) {
        //obtener la lista de registros mensuales
        List<DTOPerCapParticipantes> l = new ArrayList<>();
        List<ParticipantesPersonalCapacitado> entities = new ArrayList<>();
        
        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        entities = f.getEntityManager().createQuery("SELECT p FROM ParticipantesPersonalCapacitado p INNER JOIN p.percap a INNER JOIN p.registros reg INNER JOIN reg.eventoRegistro er WHERE er.ejercicioFiscal.anio = :ejercicio", ParticipantesPersonalCapacitado.class)
                .setParameter("ejercicio", ejercicio)
                .getResultList();
     

        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            Registros reg = f.getEntityManager().find(Registros.class, e.getRegistro());
            ActividadesPoa a = reg.getActividadesPoaList().isEmpty()?null:reg.getActividadesPoaList().get(0);
            l.add(new DTOPerCapParticipantes(
                    e,
                    f.getEntityManager().find(Personal.class, e.getPersonal()),
                    a));
        });
        
        return l;
    }
}
