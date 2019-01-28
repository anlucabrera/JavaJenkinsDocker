/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Meses;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasMovilidad;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosMovilidad;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistroMovilidadDocente;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistroMovilidadEstudiante;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Pais;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOMovilidadDocente;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOMovilidadEstudiante;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTORegistroMovilidad;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbRegistroMovilidad;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
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
public class ServicioRegistroMovilidad implements EjbRegistroMovilidad{
    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
    @EJB EjbOrganismosVinculados ejbOrganismosVinculados;
    @EJB EjbPropiedades ep;
    @Inject Caster caster;
    @Inject ControladorEmpleado controladorEmpleado;
      
    @Override
    public List<DTORegistroMovilidad> getListaRegistroMovilidad(String rutaArchivo) throws Throwable {
        List<Boolean> validarCelda = new ArrayList<>();
        List<String> datosInvalidos = new ArrayList<>();
       
        List<DTORegistroMovilidad> listaDtoMovilidad = new ArrayList<>();
        Pais pais;
        Estado estado;
        OrganismosVinculados organismosVinculados;
        AreasUniversidad areasUniversidad;
        PeriodosEscolares periodosEscolares;
        ProgramasMovilidad programasMovilidad;
        RegistrosMovilidad registrosMovilidad;
        DTORegistroMovilidad dTORegistroMovilidad;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        int par =0;
        int pro =0;
        int des =0;
        int ie=0;
        
        try{
        if (primeraHoja.getSheetName().equals("Registro Movilidad")) {
        for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((!"".equals(fila.getCell(0).getStringCellValue()))) {
                pais = new Pais();
                estado = new Estado();
                organismosVinculados = new OrganismosVinculados();
                areasUniversidad =  new AreasUniversidad();
                periodosEscolares = new PeriodosEscolares();
                programasMovilidad = new ProgramasMovilidad();
                registrosMovilidad = new  RegistrosMovilidad();
                dTORegistroMovilidad = new DTORegistroMovilidad();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case FORMULA:
                        registrosMovilidad.setRegistroMovilidad(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case STRING: 
                        programasMovilidad.setNombre(fila.getCell(2).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case FORMULA:
                        programasMovilidad.setPrograma((short)fila.getCell(3).getNumericCellValue());
                        registrosMovilidad.setProgramaMovilidad(programasMovilidad);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case STRING: 
                        registrosMovilidad.setTipoParticipante(fila.getCell(4).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case STRING: 
                        registrosMovilidad.setTipoMovilidad(fila.getCell(5).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                     case NUMERIC: 
                         if (DateUtil.isCellDateFormatted(fila.getCell(6))) {
                         registrosMovilidad.setFechaInicio(fila.getCell(6).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                     case NUMERIC: 
                         if (DateUtil.isCellDateFormatted(fila.getCell(7))) {
                         registrosMovilidad.setFechaFin(fila.getCell(7).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                 switch (fila.getCell(8).getCellTypeEnum()) {
                     case STRING: 
                         registrosMovilidad.setParticipante(fila.getCell(8).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(9).getCellTypeEnum()) {
                    case STRING:
                        areasUniversidad.setNombre(fila.getCell(9).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(10).getCellTypeEnum()) {
                    case FORMULA:
                        areasUniversidad.setArea((short)fila.getCell(10).getNumericCellValue());
                        registrosMovilidad.setProgramaEducativo(areasUniversidad.getArea());
                        break;
                    default:
                        break;
                }
//                switch (fila.getCell(12).getCellTypeEnum()) {
//                    case STRING:
//                        dTORegistroMovilidad.setCicloEscolar(fila.getCell(12).getStringCellValue());
//                        break;
//                    default:
//                        break;
//                }
//                switch (fila.getCell(14).getCellTypeEnum()) {
//                    case STRING:
//                         registrosMovilidad.setPeriodoEscolarCursado(fila.getCell(14).getStringCellValue());
//                        break;
//                    default:
//                        break;
//                }
                switch (fila.getCell(15).getCellTypeEnum()) {
                    case FORMULA:
                        periodosEscolares.setPeriodo((int) fila.getCell(15).getNumericCellValue());
                        registrosMovilidad.setPeriodoEscolarCursado(periodosEscolares.getPeriodo());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(16).getCellTypeEnum()) {
                    case NUMERIC:
                        int cuat= (int)fila.getCell(16).getNumericCellValue();
                        String c = Integer.toString(cuat);
                        registrosMovilidad.setCuatrimestreCursado(c);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(17).getCellTypeEnum()) {
                    case STRING:
                        organismosVinculados.setNombre(fila.getCell(17).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(18).getCellTypeEnum()) {
                    case FORMULA:
                        organismosVinculados.setEmpresa((int)fila.getCell(18).getNumericCellValue());
                        registrosMovilidad.setInstitucionOrganizacion(organismosVinculados);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(19).getCellTypeEnum()) {
                    case STRING:
                        pais.setNombre(fila.getCell(19).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(20).getCellTypeEnum()) {
                    case FORMULA:
                        pais.setIdpais((int)fila.getCell(20).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                 switch (fila.getCell(23).getCellTypeEnum()) {
                    case STRING:
                        estado.setNombre(fila.getCell(23).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(24).getCellTypeEnum()) {
                    case FORMULA:
                        estado.setIdestado((int)fila.getCell(24).getNumericCellValue());
                        estado.setIdpais(pais);
                        registrosMovilidad.setEstado(estado);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(25).getCellTypeEnum()) {
                    case STRING:
                        registrosMovilidad.setProyecto(fila.getCell(25).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(26).getCellTypeEnum()) {
                    case STRING:
                        registrosMovilidad.setDescripcion(fila.getCell(26).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(28).getCellTypeEnum()) {
                    case FORMULA:
                        BigDecimal est= new BigDecimal((int)fila.getCell(28).getNumericCellValue());
                        registrosMovilidad.setPresEst(est);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(30).getCellTypeEnum()) {
                    case FORMULA:
                        BigDecimal fed= new BigDecimal((int)fila.getCell(30).getNumericCellValue());
                        registrosMovilidad.setPresFed(fed);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(32).getCellTypeEnum()) {
                    case FORMULA:
                        BigDecimal cap= new BigDecimal((int)fila.getCell(32).getNumericCellValue());
                        registrosMovilidad.setCapDer(cap);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(34).getCellTypeEnum()) {
                    case FORMULA:
                        BigDecimal ing= new BigDecimal((int)fila.getCell(34).getNumericCellValue());
                        registrosMovilidad.setIngPropios(ing);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(36).getCellTypeEnum()) {
                    case FORMULA:
                        BigDecimal ext= new BigDecimal((int)fila.getCell(36).getNumericCellValue());
                        registrosMovilidad.setIngExtra(ext);
                        break;
                    default:
                        break;
                }
                
                switch (fila.getCell(37).getCellTypeEnum()) {
                    case STRING:
                        registrosMovilidad.setDescripcionIngExt(fila.getCell(37).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(43).getCellTypeEnum()) {
                    case FORMULA:
                        par= (int) fila.getCell(43).getNumericCellValue();
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(44).getCellTypeEnum()) {
                   case FORMULA:
                        pro= (int) fila.getCell(44).getNumericCellValue();
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(45).getCellTypeEnum()) {
                    case FORMULA:
                        des= (int) fila.getCell(45).getNumericCellValue();
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(46).getCellTypeEnum()) {
                   case FORMULA:
                        ie= (int) fila.getCell(46).getNumericCellValue();
                        break;
                    default:
                        break;
                }
                    if (par == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Nombre de la o el participante en la columna: <b>" + (7 + 1) + " y fila: " + (i + 1) + "</b> \n");
                    }
                    if (pro == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Nombre proyecto en la columna: <b>" + (15 + 1) + " y fila: " + (i + 1) + "</b> \n");
                    }
                    if (des == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Descripción en la columna: <b>" + (16 + 1) + " y fila: " + (i + 1) + "</b> \n");
                    }
                    if (ie == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Descripción de Ing. Extraordinarios en la columna: <b>" + (22 + 1) + " y fila: " + (i + 1) + "</b> \n");
                    }
                    dTORegistroMovilidad.setAreasUniversidad(areasUniversidad);
                    dTORegistroMovilidad.setRegistrosMovilidad(registrosMovilidad);
                    listaDtoMovilidad.add(dTORegistroMovilidad);
                }
            }
            libroRegistro.close();
            if (validarCelda.contains(false)) {
                    addDetailMessage("<b>Hoja de Registros de Movilidad contiene datos que no son válidos, verifique los datos de la plantilla</b>");
                    addDetailMessage(datosInvalidos.toString());
                    return Collections.EMPTY_LIST;
                } else {
                    addDetailMessage("<b>Hoja de Registros de Movilidad Validada favor de verificar sus datos antes de guardar su información</b>");
                    return listaDtoMovilidad;
                }
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
            return Collections.EMPTY_LIST;
        }
    } catch (IOException e) {
            libroRegistro.close();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>Ocurrió un error durante la lectura del archivo, asegurese de haber registrado correctamente su información</b>");
            return Collections.EMPTY_LIST;
    }

    }

    @Override
    public void guardaRegistroMovilidad(List<DTORegistroMovilidad> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
        List<String> listaCondicional = new ArrayList<>();
        lista.forEach((movilidad) -> {
            
            if (ejbModulos.validaPeriodoRegistro(ejbModulos.getPeriodoEscolarActual(), movilidad.getRegistrosMovilidad().getPeriodoEscolarCursado())) {
                f.setEntityClass(RegistrosMovilidad.class);
                RegistrosMovilidad regMovEncontrada = getRegistrosMovilidad(movilidad.getRegistrosMovilidad());
                Boolean registroAlmacenado = false;
                if (regMovEncontrada != null) {
                    listaCondicional.add(regMovEncontrada.getRegistroMovilidad()+ " " + regMovEncontrada.getProyecto());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    if(ejbModulos.comparaPeriodoRegistro(regMovEncontrada.getPeriodoEscolarCursado(), movilidad.getRegistrosMovilidad().getPeriodoEscolarCursado())){
                        movilidad.getRegistrosMovilidad().setRegistro(regMovEncontrada.getRegistro());
                        movilidad.getRegistrosMovilidad().getInstitucionOrganizacion().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(movilidad.getRegistrosMovilidad().getInstitucionOrganizacion().getEmpresa()));
                        f.edit(movilidad.getRegistrosMovilidad());
                        f.flush();
                        addDetailMessage("<b>Se actualizaron los registros con los siguientes datos: </b> " + regMovEncontrada.getRegistroMovilidad());
                    } else{
                        addDetailMessage("<b>No se pueden actualizar los registros con los siguientes datos: </b> " + regMovEncontrada.getRegistroMovilidad());
                    }
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    movilidad.getRegistrosMovilidad().setRegistro(registro.getRegistro());
                    movilidad.getRegistrosMovilidad().getInstitucionOrganizacion().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(movilidad.getRegistrosMovilidad().getInstitucionOrganizacion().getEmpresa()));
                    f.create(movilidad.getRegistrosMovilidad());
                    addDetailMessage("<b>Se guardaron los registros correctamente </b> ");
                }
                f.flush();
            } else{
            
             addDetailMessage("<b>No puede registrar información de periodos anteriores</b>");
            }   
        });
}
    @Override
    public Integer getRegistroMovilidadEspecifico(String registroMovilidad) {
        TypedQuery<RegistrosMovilidad> query = f.getEntityManager().createNamedQuery("RegistrosMovilidad.findByRegistroMovilidad", RegistrosMovilidad.class);
        query.setParameter("registroMovilidad", registroMovilidad);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }

    @Override
    public RegistrosMovilidad getRegistrosMovilidad(RegistrosMovilidad registrosMovilidad) {
        TypedQuery<RegistrosMovilidad> query = f.getEntityManager().createQuery("SELECT r FROM RegistrosMovilidad r WHERE r.registroMovilidad = :registroMovilidad", RegistrosMovilidad.class);
        query.setParameter("registroMovilidad", registrosMovilidad.getRegistroMovilidad());
        try {
            registrosMovilidad = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            registrosMovilidad = null;
            ex.toString();
        }
        return  registrosMovilidad;
    }

    @Override
    public List<ProgramasMovilidad> getProgramasMovilidadAct() {
        List<ProgramasMovilidad> genLst = new ArrayList<>();
        TypedQuery<ProgramasMovilidad> query = f.getEntityManager().createQuery("SELECT p FROM ProgramasMovilidad p", ProgramasMovilidad.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }

   
    @Override
    public List<PeriodosEscolares> getPeriodosConregistro() {
        List<Integer> claves = f.getEntityManager().createQuery("SELECT r FROM RegistrosMovilidad r", RegistrosMovilidad.class)
                .getResultStream()
                .map(r -> r.getPeriodoEscolarCursado())
                .collect(Collectors.toList());
        
        if(claves.isEmpty())
       {
           claves.add(0,  ejbModulos.getPeriodoEscolarActual().getPeriodo());
       
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
    public List<DTORegistroMovilidad> getListaRegistrosPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo) {
          //verificar que los parametros no sean nulos
        if(evento == null || claveArea == null || periodo == null){
            return null;
        }
        List<Short> areas = new ArrayList<>();
        
        //obtener la referencia al area operativa del trabajador
        AreasUniversidad area = f.getEntityManager().find(AreasUniversidad.class, claveArea);
      
        //comprobar si el area operativa es un programa educativo referenciar a su area superior para obtener la referencia al area academica
        Short programaCategoria = (short)ep.leerPropiedadEntera("modulosRegistroProgramaEducativoCategoria").orElse(9);
        if (Objects.equals(area.getCategoria().getCategoria(), programaCategoria)) {            
            area = f.getEntityManager().find(AreasUniversidad.class, area.getAreaSuperior());

            //Obtener las claves de todas las areas que dependan de área academicoa
            areas = f.getEntityManager().createQuery("SELECT au FROM AreasUniversidad au WHERE au.areaSuperior=:areaSuperior AND au.vigente='1'", AreasUniversidad.class)
                    .setParameter("areaSuperior", area.getArea())
                    .getResultStream()
                    .map(au -> au.getArea())
                    .collect(Collectors.toList());

        }else{//si no es area academica solo filtrar los datos del area operativa del trabajador
            areas.add(claveArea);

        }
        
//        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
//        List<DTORegistroMovilidad> l = new ArrayList<>();
//        List<RegistrosMovilidad> entities = f.getEntityManager().createQuery("SELECT r FROM RegistrosMovilidad r INNER JOIN r.registros reg INNER JOIN reg.eventoRegistro er WHERE er.eventoRegistro=:evento AND r.programaEducativo in :areas AND r.periodoEscolarCursado=:periodo", RegistrosMovilidad.class)
//                .setParameter("areas", areas)
//                .setParameter("evento", evento.getEventoRegistro())
//                .setParameter("periodo", periodo.getPeriodo())
//                .getResultList();

        List<DTORegistroMovilidad> l = new ArrayList<>();
        List<RegistrosMovilidad> entities = f.getEntityManager().createQuery("SELECT r FROM RegistrosMovilidad r INNER JOIN r.registros reg INNER JOIN reg.eventoRegistro er WHERE er.eventoRegistro=:evento AND r.periodoEscolarCursado=:periodo", RegistrosMovilidad.class)
                .setParameter("evento", evento.getEventoRegistro())
                .setParameter("periodo", periodo.getPeriodo())
                .getResultList();
     

        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            Registros reg = f.getEntityManager().find(Registros.class, e.getRegistro());
            ActividadesPoa a = reg.getActividadesPoaList().isEmpty()?null:reg.getActividadesPoaList().get(0);
            l.add(new DTORegistroMovilidad(
                    e,
                    f.getEntityManager().find(AreasUniversidad.class, e.getProgramaEducativo()),
                    f.getEntityManager().find(PeriodosEscolares.class, e.getPeriodoEscolarCursado()),
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
        Map<List<PeriodosEscolares>,List<EventosRegistros>> map = new HashMap<>();
        map.put(periodos, eventos);
        return map.entrySet().iterator().next();
    }

    @Override
    public List<DTOMovilidadDocente> getListaRegistrosPorEventoAreaPeriodoDoc(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo) {
          //verificar que los parametros no sean nulos
        if(evento == null || claveArea == null || periodo == null){
            return null;
        }
        List<Short> areas = new ArrayList<>();
        
        //obtener la referencia al area operativa del trabajador
        AreasUniversidad area = f.getEntityManager().find(AreasUniversidad.class, claveArea);
      
        //comprobar si el area operativa es un programa educativo referenciar a su area superior para obtener la referencia al area academica
        Short programaCategoria = (short)ep.leerPropiedadEntera("modulosRegistroProgramaEducativoCategoria").orElse(9);
        if (Objects.equals(area.getCategoria().getCategoria(), programaCategoria)) {            
            area = f.getEntityManager().find(AreasUniversidad.class, area.getAreaSuperior());

            //Obtener las claves de todas las areas que dependan de área academicoa
            areas = f.getEntityManager().createQuery("SELECT au FROM AreasUniversidad au WHERE au.areaSuperior=:areaSuperior AND au.vigente='1'", AreasUniversidad.class)
                    .setParameter("areaSuperior", area.getArea())
                    .getResultStream()
                    .map(au -> au.getArea())
                    .collect(Collectors.toList());

        }else{//si no es area academica solo filtrar los datos del area operativa del trabajador
            areas.add(claveArea);

        }
        
//        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
//        List<DTOMovilidadDocente> l = new ArrayList<>();
//        List<RegistroMovilidadDocente> entities = f.getEntityManager().createQuery("SELECT d FROM RegistroMovilidadDocente d INNER JOIN d.registroMovilidad r INNER JOIN d.registros reg INNER JOIN reg.eventoRegistro er WHERE er.eventoRegistro=:evento AND r.programaEducativo in :areas AND r.periodoEscolarCursado=:periodo", RegistroMovilidadDocente.class)
//                .setParameter("areas", areas)
//                .setParameter("evento", evento.getEventoRegistro())
//                .setParameter("periodo", periodo.getPeriodo())
//                .getResultList();
        
         //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        List<DTOMovilidadDocente> l = new ArrayList<>();
        List<RegistroMovilidadDocente> entities = f.getEntityManager().createQuery("SELECT d FROM RegistroMovilidadDocente d INNER JOIN d.registroMovilidad r INNER JOIN d.registros reg INNER JOIN reg.eventoRegistro er WHERE er.eventoRegistro=:evento AND r.periodoEscolarCursado=:periodo", RegistroMovilidadDocente.class)
                .setParameter("evento", evento.getEventoRegistro())
                .setParameter("periodo", periodo.getPeriodo())
                .getResultList();
     

        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            Registros reg = f.getEntityManager().find(Registros.class, e.getRegistro());
            ActividadesPoa a = reg.getActividadesPoaList().isEmpty()?null:reg.getActividadesPoaList().get(0);
            l.add(new DTOMovilidadDocente(
                    e,
                    f.getEntityManager().find(Personal.class, e.getClavePersonal()),
                    a));
        });
        


        return l;
    }

    @Override
    public List<DTOMovilidadEstudiante> getListaRegistrosPorEventoAreaPeriodoEst(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo) {
         //verificar que los parametros no sean nulos
        if(evento == null || claveArea == null || periodo == null){
            return null;
        }
        List<Short> areas = new ArrayList<>();
        
        //obtener la referencia al area operativa del trabajador
        AreasUniversidad area = f.getEntityManager().find(AreasUniversidad.class, claveArea);
      
        //comprobar si el area operativa es un programa educativo referenciar a su area superior para obtener la referencia al area academica
        Short programaCategoria = (short)ep.leerPropiedadEntera("modulosRegistroProgramaEducativoCategoria").orElse(9);
        if (Objects.equals(area.getCategoria().getCategoria(), programaCategoria)) {            
            area = f.getEntityManager().find(AreasUniversidad.class, area.getAreaSuperior());

            //Obtener las claves de todas las areas que dependan de área academicoa
            areas = f.getEntityManager().createQuery("SELECT au FROM AreasUniversidad au WHERE au.areaSuperior=:areaSuperior AND au.vigente='1'", AreasUniversidad.class)
                    .setParameter("areaSuperior", area.getArea())
                    .getResultStream()
                    .map(au -> au.getArea())
                    .collect(Collectors.toList());

        }else{//si no es area academica solo filtrar los datos del area operativa del trabajador
            areas.add(claveArea);

        }
        
//        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
//        List<DTOMovilidadEstudiante> l = new ArrayList<>();
//        List<RegistroMovilidadEstudiante> entities = f.getEntityManager().createQuery("SELECT e FROM RegistroMovilidadEstudiante e INNER JOIN e.registroMovilidad r INNER JOIN e.registros reg INNER JOIN reg.eventoRegistro er WHERE er.eventoRegistro=:evento AND r.programaEducativo in :areas AND r.periodoEscolarCursado=:periodo", RegistroMovilidadEstudiante.class)
//                .setParameter("areas", areas)
//                .setParameter("evento", evento.getEventoRegistro())
//                .setParameter("periodo", periodo.getPeriodo())
//                .getResultList();
        
         //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        List<DTOMovilidadEstudiante> l = new ArrayList<>();
        List<RegistroMovilidadEstudiante> entities = f.getEntityManager().createQuery("SELECT e FROM RegistroMovilidadEstudiante e INNER JOIN e.registroMovilidad r INNER JOIN e.registros reg INNER JOIN reg.eventoRegistro er WHERE er.eventoRegistro=:evento AND r.periodoEscolarCursado=:periodo", RegistroMovilidadEstudiante.class)
                .setParameter("evento", evento.getEventoRegistro())
                .setParameter("periodo", periodo.getPeriodo())
                .getResultList();
     

        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            Registros reg = f.getEntityManager().find(Registros.class, e.getRegistro());
            ActividadesPoa a = reg.getActividadesPoaList().isEmpty()?null:reg.getActividadesPoaList().get(0);
            l.add(new DTOMovilidadEstudiante(
                    e,
                    a));
        });
        


        return l;
    }

    @Override
    public List<Integer> buscaRegistroDocentesRegMovilidad(String clave) throws Throwable {
       List<Integer> registros = new ArrayList<>();
        try {
            registros = f.getEntityManager().createQuery("SELECT d FROM RegistroMovilidadDocente d WHERE d.registroMovilidad.registroMovilidad = :clave", RegistroMovilidadDocente.class)
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
    public List<Integer> buscaRegistroEstudiantesRegMovilidad(String clave) throws Throwable {
        List<Integer> registros = new ArrayList<>();
        try {
            registros = f.getEntityManager().createQuery("SELECT e FROM RegistroMovilidadEstudiante e WHERE e.registroMovilidad.registroMovilidad = :clave", RegistroMovilidadEstudiante.class)
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
    public List<Integer> buscaRegistroEvidenciasDocente(String clave) throws Throwable {
        List<Integer> registros = new ArrayList<>();
        List<Integer> evidencias = new ArrayList<>();
        try {
            registros = f.getEntityManager().createQuery("SELECT d FROM RegistroMovilidadDocente d WHERE d.registroMovilidad.registroMovilidad = :clave", RegistroMovilidadDocente.class)
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
    public List<Integer> buscaRegistroEvidenciasEstudiante(String clave) throws Throwable {
         List<Integer> registros = new ArrayList<>();
        List<Integer> evidencias = new ArrayList<>();
        try {
            registros = f.getEntityManager().createQuery("SELECT e FROM RegistroMovilidadEstudiante e WHERE e.registroMovilidad.registroMovilidad = :clave", RegistroMovilidadEstudiante.class)
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

}
