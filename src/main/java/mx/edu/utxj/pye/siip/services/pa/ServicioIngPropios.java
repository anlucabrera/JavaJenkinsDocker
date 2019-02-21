/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.pa;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.Meses;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.IngresosPropiosCaptados;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.finanzas.DTOIngPropios;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.pa.EjbIngPropios;
import org.apache.poi.ss.usermodel.CellType;
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
public class ServicioIngPropios implements EjbIngPropios{
    
    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
    @EJB EjbPropiedades ep;
    @Inject Caster caster; 
    @Inject ControladorEmpleado controladorEmpleado;
    
    @Getter @Setter private List<Short> areas;
   
    @Override
    public List<DTOIngPropios> getListaIngPropios(String rutaArchivo) throws Throwable, FileNotFoundException, ClassNotFoundException {
        List<Boolean> validarCelda = new ArrayList<>();
        List<String> datosInvalidos = new ArrayList<>();
        
        List<DTOIngPropios> listaDtoIngPropios = new ArrayList<>();
        IngresosPropiosCaptados ingresosPropiosCaptados;
        DTOIngPropios dTOIngPropios;
        PeriodosEscolares periodosEscolares;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        int v=0;
        
        try {
        if (primeraHoja.getSheetName().equals("Ingresos")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

            if ((!"".equals(fila.getCell(0).getStringCellValue()))) {
                ingresosPropiosCaptados = new IngresosPropiosCaptados();
                dTOIngPropios = new  DTOIngPropios();
                periodosEscolares = new PeriodosEscolares();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        dTOIngPropios.setCicloEscolar(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                   case STRING:
                       dTOIngPropios.setPeriodoEscolar(fila.getCell(2).getStringCellValue());
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                   case FORMULA:
                       periodosEscolares.setPeriodo((int)fila.getCell(3).getNumericCellValue());
                       ingresosPropiosCaptados.setPeriodoEscolar(periodosEscolares.getPeriodo());
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case FORMULA: 
                        ingresosPropiosCaptados.setConceptoIngresosCaptados(fila.getCell(5).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(6))) {
                        ingresosPropiosCaptados.setFechaIngreso(fila.getCell(6).getDateCellValue());
                        }
                    default:
                        break;
                }
                if (fila.getCell(7).getCellTypeEnum() != CellType.STRING) {
                switch (fila.getCell(8).getCellTypeEnum()) {
                    case FORMULA:
                        ingresosPropiosCaptados.setMonto((double)fila.getCell(8).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                } else {
                    validarCelda.add(false);
                    datosInvalidos.add("Dato incorrecto: Descripción del ingreso en la columna: " + (4 + 1) + " y fila: " + (i + 1) + " \n ");
                }
              
                switch (fila.getCell(9).getCellTypeEnum()) {
                    case STRING:
                        ingresosPropiosCaptados.setDescripcion(fila.getCell(9).getStringCellValue());
                        break;
                    default:
                        break;
                }
                
                switch (fila.getCell(11).getCellTypeEnum()){
                    case FORMULA:
                        v= (int) fila.getCell(11).getNumericCellValue();
                        break;
                    default:
                        break;
                }
                
                if(v == 1)
                {
                    validarCelda.add(false);
                    datosInvalidos.add("Dato incorrecto: Descripción del ingreso en la columna: " + (5 + 1) + " y fila: " + (i + 1) + " \n ");
                }
                
                    dTOIngPropios.setIngresosPropiosCaptados(ingresosPropiosCaptados);
                    listaDtoIngPropios.add(dTOIngPropios);
                }
            }
            libroRegistro.close();
            if (validarCelda.contains(false)) {
                    Messages.addGlobalError("<b>El archivo cargado contiene datos que no son validos, verifique los datos de la plantilla</b>");
                    Messages.addGlobalError(datosInvalidos.toString());
                    excel.delete();
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                  
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\archivos\\log\\output.txt"), "utf-8"));
                    out.write(datosInvalidos.toString());
                    out.close();
                    
                    return Collections.EMPTY_LIST;
                } else {
                    Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
                    return listaDtoIngPropios;
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
    public void guardaIngPropios(List<DTOIngPropios> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
        List<String> listaCondicional = new ArrayList<>();
        lista.forEach((ingPropios) -> {
            
                f.setEntityClass(IngresosPropiosCaptados.class);
                IngresosPropiosCaptados ingProEncontrado = getRegistroIngresosPropiosCaptados(ingPropios.getIngresosPropiosCaptados());
                Boolean registroAlmacenado = false;
                if (ingProEncontrado != null) {
                    listaCondicional.add(ingPropios.getPeriodoEscolar()+" - "+ingPropios.getIngresosPropiosCaptados().getConceptoIngresosCaptados());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    ingPropios.getIngresosPropiosCaptados().setRegistro(ingProEncontrado.getRegistro());
                    f.edit(ingPropios.getIngresosPropiosCaptados());
                    Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    ingPropios.getIngresosPropiosCaptados().setRegistro(registro.getRegistro());
                    f.create(ingPropios.getIngresosPropiosCaptados());
                    Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b>");
                }
                f.flush();
        });
    }

    @Override
    public IngresosPropiosCaptados getRegistroIngresosPropiosCaptados(IngresosPropiosCaptados ingresosPropiosCaptados) {
        TypedQuery<IngresosPropiosCaptados> query = f.getEntityManager().createQuery("SELECT i FROM IngresosPropiosCaptados i WHERE i.periodoEscolar = :periodoEscolar AND i.conceptoIngresosCaptados = :conceptoIngresosCaptados AND i.fechaIngreso = :fechaIngreso", IngresosPropiosCaptados.class);
        query.setParameter("periodoEscolar", ingresosPropiosCaptados.getPeriodoEscolar());
        query.setParameter("conceptoIngresosCaptados", ingresosPropiosCaptados.getConceptoIngresosCaptados());
        query.setParameter("fechaIngreso", ingresosPropiosCaptados.getFechaIngreso());
        try {
            ingresosPropiosCaptados = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            ingresosPropiosCaptados = null;
            ex.toString();
        }
        return ingresosPropiosCaptados;
    }

    @Override
    public List<PeriodosEscolares> getPeriodosConregistro() {
        List<Integer> claves = f.getEntityManager().createQuery("SELECT i FROM IngresosPropiosCaptados i", IngresosPropiosCaptados.class)
                .getResultStream()
                .map(a -> a.getPeriodoEscolar())
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
    public List<DTOIngPropios> getListaRegistrosPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo) {
            //verificar que los parametros no sean nulos
        if(evento == null || claveArea == null || periodo == null){
            return null;
        }
       //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        List<DTOIngPropios> l = new ArrayList<>();
        List<IngresosPropiosCaptados> entities = new ArrayList<>();
        
        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        if (claveArea == 6) {
            entities = f.getEntityManager().createQuery("SELECT i FROM IngresosPropiosCaptados i INNER JOIN i.registros reg INNER JOIN reg.eventoRegistro er WHERE er.eventoRegistro=:evento AND i.periodoEscolar =:periodoEscolar", IngresosPropiosCaptados.class)
                    .setParameter("evento", evento.getEventoRegistro())
                    .setParameter("periodoEscolar", periodo.getPeriodo())
                    .getResultList();
        } else {
            areas = ejbModulos.getAreasDependientes(claveArea);
            entities = f.getEntityManager().createQuery("SELECT i FROM IngresosPropiosCaptados i INNER JOIN i.registros reg INNER JOIN reg.eventoRegistro er WHERE er.eventoRegistro=:evento AND i.periodoEscolar =:periodoEscolar AND reg.area IN :areas", IngresosPropiosCaptados.class)
                    .setParameter("evento", evento.getEventoRegistro())
                    .setParameter("periodoEscolar", periodo.getPeriodo())
                    .setParameter("areas", areas)
                    .getResultList();
        }
        
        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            PeriodosEscolares p = f.getEntityManager().find(PeriodosEscolares.class, e.getPeriodoEscolar());
            CiclosEscolares c = f.getEntityManager().find(CiclosEscolares.class, p.getCiclo().getCiclo());
            String strDateFormat = "yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            String cicloe = sdf.format(c.getInicio()) + " - " + sdf.format(c.getFin());
            String periodoe = p.getMesInicio().getMes()+ " - " + p.getMesFin().getMes();
            
            Registros reg = f.getEntityManager().find(Registros.class, e.getRegistro());
//            ActividadesPoa a = e.getRegistros().getActividadesPoaList().isEmpty()?null:e.getRegistros().getActividadesPoaList().get(0);
            ActividadesPoa a = reg.getActividadesPoaList().isEmpty()?null:reg.getActividadesPoaList().get(0);
            l.add(new DTOIngPropios(
                    e,
                    f.getEntityManager().find(PeriodosEscolares.class, e.getPeriodoEscolar()),
                    a,
                    cicloe,
                    periodoe));
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

}
