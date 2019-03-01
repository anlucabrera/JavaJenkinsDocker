/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.io.IOException;
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
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.Meses;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.BolsaTrabajo;
import mx.edu.utxj.pye.sgi.entity.pye2.BolsaTrabajoEntrevistas;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOBolsa;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOBolsaEntrevistas;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbBolsaTrabajo;
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
public class ServicioBolsaTrabajo implements EjbBolsaTrabajo{
    
    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
    @EJB EjbOrganismosVinculados ejbOrganismosVinculados;
    @EJB EjbPropiedades ep;
    @Inject Caster caster; 
    @Inject ControladorEmpleado controladorEmpleado;
    
    @Getter @Setter private List<Short> areas;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    
    @Override
    public List<DTOBolsa> getListaBolsaTrabajo(String rutaArchivo) throws Throwable {
        List<Boolean> validarCelda = new ArrayList<>();
        List<String> datosInvalidos = new ArrayList<>();
        
        List<DTOBolsa> listaDtoBolsa = new ArrayList<>();
        PeriodosEscolares periodosEscolares;
        OrganismosVinculados organismosVinculados;
        BolsaTrabajo bolsaTrabajo;
        DTOBolsa dTOBolsa;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        int puesto=0;
        
        try{
        if (primeraHoja.getSheetName().equals("Bolsa de Trabajo")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((!"".equals(fila.getCell(2).getStringCellValue()))) {
                periodosEscolares = new PeriodosEscolares();
                organismosVinculados = new OrganismosVinculados();
                bolsaTrabajo = new BolsaTrabajo();
                dTOBolsa = new DTOBolsa();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case FORMULA:
                        bolsaTrabajo.setBolsatrab(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case FORMULA: 
                        periodosEscolares.setPeriodo((int)fila.getCell(5).getNumericCellValue());
                        bolsaTrabajo.setPeriodo(periodosEscolares.getPeriodo());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case NUMERIC: 
                        if (DateUtil.isCellDateFormatted(fila.getCell(6))) {
                         bolsaTrabajo.setFecha(fila.getCell(6).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(10).getCellTypeEnum()) {
                    case STRING:
                        organismosVinculados.setNombre(fila.getCell(10).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(11).getCellTypeEnum()) {
                    case FORMULA: 
                        organismosVinculados.setEmpresa((int) fila.getCell(11).getNumericCellValue());
                        bolsaTrabajo.setEmpresa(organismosVinculados);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(12).getCellTypeEnum()) {
                    case STRING:
                        bolsaTrabajo.setPuestoOfertado(fila.getCell(12).getStringCellValue());
                        break;
                    case NUMERIC: 
                        String puestoOfer = Integer.toString((int) fila.getCell(12).getNumericCellValue());
                        bolsaTrabajo.setPuestoOfertado(puestoOfer);
                    break;
                    default:
                        break;
                }
                switch (fila.getCell(13).getCellTypeEnum()) {
                    case NUMERIC:
                        bolsaTrabajo.setPlazas((short)fila.getCell(13).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(15).getCellTypeEnum()) {
                   case FORMULA:
                        puesto= (int) fila.getCell(15).getNumericCellValue();
                        break;
                    default:
                        break;
                }
                    if (puesto == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Nombre del Puesto en la columna: <b>" + (5 + 1) + " y fila: " + (i + 1) + "</b> \n");
                    }
                    dTOBolsa.setBolsaTrabajo(bolsaTrabajo);
                    listaDtoBolsa.add(dTOBolsa);
                }
            }
            libroRegistro.close();
            if (validarCelda.contains(false)) {
                    Messages.addGlobalError("<b>Hoja de Bolsa de Trabajo contiene datos que no son válidos, verifique los datos de la plantilla</b>");
                    Messages.addGlobalError(datosInvalidos.toString());
                    return Collections.EMPTY_LIST;
                } else {
                    Messages.addGlobalInfo("<b>Hoja de Bolsa de Trabajo Validada favor de verificar sus datos antes de guardar su información</b>");
                    return listaDtoBolsa;
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
    public void guardaBolsaTrabajo(List<DTOBolsa> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
  
        List<String> listaCondicional = new ArrayList<>();
        lista.forEach((bolsa) -> {
            
            if (ejbModulos.validaPeriodoRegistro(ejbModulos.getPeriodoEscolarActual(), bolsa.getBolsaTrabajo().getPeriodo())) {
                f.setEntityClass(BolsaTrabajo.class);
                BolsaTrabajo bt = getRegistroBolsaTrabajo(bolsa.getBolsaTrabajo());
                Boolean registroAlmacenado = false;
                if (bt != null) {
                    listaCondicional.add(bolsa.getBolsaTrabajo().getBolsatrab());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    if (ejbModulos.comparaPeriodoRegistro(bt.getPeriodo(), bolsa.getBolsaTrabajo().getPeriodo())) {
                        bolsa.getBolsaTrabajo().setRegistro(bt.getRegistro());
                        bolsa.getBolsaTrabajo().getEmpresa().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(bolsa.getBolsaTrabajo().getEmpresa().getEmpresa()));
                        f.edit(bolsa.getBolsaTrabajo());
                        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + bt.getBolsatrab());
                    } else {
                        Messages.addGlobalWarn("<b>No se pueden actualizar los registros con los siguientes datos: </b> " + bt.getBolsatrab());
                    }
                } else {
                   Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                   bolsa.getBolsaTrabajo().getEmpresa().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(bolsa.getBolsaTrabajo().getEmpresa().getEmpresa()));
                    bolsa.getBolsaTrabajo().setRegistro(registro.getRegistro());
                    f.create(bolsa.getBolsaTrabajo());
                    Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b>");
                }
                f.flush();
            } 
               
        });
 }
    
    @Override
    public Integer getRegistroBolsaTrabajoEspecifico(String bolsatrab) {
        TypedQuery<BolsaTrabajo> query = em.createNamedQuery("BolsaTrabajo.findByBolsatrab", BolsaTrabajo.class);
        query.setParameter("bolsatrab", bolsatrab);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }

    @Override
    public BolsaTrabajo getRegistroBolsaTrabajo(BolsaTrabajo bolsaTrabajo) {
        TypedQuery<BolsaTrabajo> query = em.createQuery("SELECT b FROM BolsaTrabajo b WHERE b.bolsatrab = :bolsatrab", BolsaTrabajo.class);
        query.setParameter("bolsatrab", bolsaTrabajo.getBolsatrab());
        try {
            bolsaTrabajo = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            bolsaTrabajo = null;
            System.out.println(ex.toString());
        }
        return bolsaTrabajo;
    }

    @Override
    public List<PeriodosEscolares> getPeriodosConregistro() {
         List<Integer> claves = f.getEntityManager().createQuery("SELECT b FROM BolsaTrabajo b", BolsaTrabajo.class)
                .getResultStream()
                .map(b -> b.getPeriodo())
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
    public List<DTOBolsa> getListaRegistrosPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo) {
            //verificar que los parametros no sean nulos
        if(evento == null || claveArea == null || periodo == null){
            return null;
        }
        
        
        
        
        
        areas = ejbModulos.getAreasDependientes(claveArea);
        
        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        List<DTOBolsa> l = new ArrayList<>();
        List<BolsaTrabajo> entities = f.getEntityManager().createQuery("SELECT b FROM BolsaTrabajo b INNER JOIN b.registros reg INNER JOIN reg.eventoRegistro er WHERE er.eventoRegistro=:evento AND b.periodo =:periodo AND reg.area IN :areas", BolsaTrabajo.class)
                .setParameter("evento", evento.getEventoRegistro())
                .setParameter("periodo", periodo.getPeriodo())
                .setParameter("areas", areas)
                .getResultList();
      
        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            
            Registros reg = f.getEntityManager().find(Registros.class, e.getRegistro());
//            ActividadesPoa a = e.getRegistros().getActividadesPoaList().isEmpty()?null:e.getRegistros().getActividadesPoaList().get(0);
            ActividadesPoa a = reg.getActividadesPoaList().isEmpty()?null:reg.getActividadesPoaList().get(0);
            l.add(new DTOBolsa(
                    e,
                    f.getEntityManager().find(PeriodosEscolares.class, e.getPeriodo()),
                    a));
        });
        


        return l;
    }

    @Override
    public List<DTOBolsaEntrevistas> getListaRegistrosPorEventoAreaPeriodoPart(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo) {
           //verificar que los parametros no sean nulos
        if(evento == null || claveArea == null || periodo == null){
            return null;
        }
        areas = ejbModulos.getAreasDependientes(claveArea);
        
        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        List<DTOBolsaEntrevistas> l = new ArrayList<>();
        List<BolsaTrabajoEntrevistas> entities = f.getEntityManager().createQuery("SELECT e FROM BolsaTrabajoEntrevistas e INNER JOIN e.bolsatrabent b INNER JOIN e.registros reg INNER JOIN reg.eventoRegistro er WHERE er.eventoRegistro=:evento AND b.periodo=:periodo AND reg.area IN :areas", BolsaTrabajoEntrevistas.class)
                .setParameter("evento", evento.getEventoRegistro())
                .setParameter("periodo", periodo.getPeriodo())
                .setParameter("areas", areas)
                .getResultList();
     

        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            Registros reg = f.getEntityManager().find(Registros.class, e.getRegistro());
            ActividadesPoa a = reg.getActividadesPoaList().isEmpty()?null:reg.getActividadesPoaList().get(0);


            l.add(new DTOBolsaEntrevistas(
                    e,
                    f.getEntityManager().find(AreasUniversidad.class, e.getProgramaEducativo()),
                    f.getEntityManager().find(Generaciones.class, e.getGeneracion()),
                    a,
                    "",
                    ""));
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
    public List<Integer> buscaRegistroEntrevistasBolsaTrabajo(String clave) throws Throwable {
         List<Integer> registros = new ArrayList<>();
        try {
            registros = em.createQuery("SELECT e FROM BolsaTrabajoEntrevistas e WHERE e.bolsatrabent.bolsatrab = :clave", BolsaTrabajoEntrevistas.class)
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
    public List<Integer> buscaRegistroEvidenciasEntrevistasBolsaTrabajo(String clave) throws Throwable {
        List<Integer> registros = new ArrayList<>();
        List<Integer> evidencias = new ArrayList<>();
        try {
            registros = em.createQuery("SELECT e FROM BolsaTrabajoEntrevistas e WHERE e.bolsatrabent.bolsatrab = :clave", BolsaTrabajoEntrevistas.class)
                    .setParameter("clave", clave)
                    .getResultStream()
                    .map(s -> s.getRegistro())
                    .collect(Collectors.toList());
           
            registros.stream().forEach((reg)-> {
		
                List<Integer> evidenciasReg = em.createQuery("SELECT e FROM Evidencias e INNER JOIN e.registrosList r WHERE r.registro = :registro",  Evidencias.class)
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