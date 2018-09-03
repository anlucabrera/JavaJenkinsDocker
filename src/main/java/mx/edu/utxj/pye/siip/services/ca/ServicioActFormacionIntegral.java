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
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesFormacionIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.pye.DTOActFormacionIntegral;
import mx.edu.utxj.pye.siip.entity.pye.list.ListaActFormacionIntegral;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbActFormacionIntegral;
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
public class ServicioActFormacionIntegral implements EjbActFormacionIntegral{
    
    @EJB
    Facade facdepye;
    @EJB
    EjbModulos ejbModulos;
    @EJB
    EjbAdministracionEncuestas eJBAdministracionEncuestas;
   
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    @Override
    public ListaActFormacionIntegral getListaActFormacionIntegral(String rutaArchivo) throws Throwable {
       
        ListaActFormacionIntegral  listaActFormacionIntegral = new ListaActFormacionIntegral();

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
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case STRING: 
                         dTOActFormacionIntegral.setPeriodoEscolar(fila.getCell(2).getStringCellValue());
                        break;
                    default:
                        break;
                }
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
                        String cadena = Integer.toString((int) fila.getCell(23).getNumericCellValue());
                        actividadesFormacionIntegral.setEquiposParticipantes(cadena);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(24).getCellTypeEnum()) {
                    case STRING: 
                        actividadesFormacionIntegral.setEquiposGanadores(fila.getCell(24).getStringCellValue());
                        break;
                    default:
                        break;
                }
                    
                    dTOActFormacionIntegral.setActividadesFormacionIntegral(actividadesFormacionIntegral);
                    listaDtoActFormacionIntegral.add(dTOActFormacionIntegral);
                }
            }
            listaActFormacionIntegral.setActFormacionIntegral(listaDtoActFormacionIntegral);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaActFormacionIntegral;
        
    }

    @Override
    public void guardaActFormacionIntegral(ListaActFormacionIntegral listaActFormacionIntegral, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//
//            listaActFormacionIntegral.getActFormacionIntegral().forEach((actFormacionIntegral) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//            
//            facdepye.setEntityClass(ActividadesFormacionIntegral.class);
//            actFormacionIntegral.getActividadesFormacionIntegral().setRegistro(registro.getRegistro());
//            facdepye.create(actFormacionIntegral.getActividadesFormacionIntegral());
//            facdepye.flush();
//        });
            
        List<String> validaciones = new SerializableArrayList<>();
        List<String> listaCondicional = new ArrayList<>();
        listaActFormacionIntegral.getActFormacionIntegral().forEach((actFormacionIntegral) -> {
                if (ejbModulos.validaPeriodoRegistro(eJBAdministracionEncuestas.getPeriodoActual(), actFormacionIntegral.getActividadesFormacionIntegral().getPeriodo())) {
                facdepye.setEntityClass(ActividadesFormacionIntegral.class);
                ActividadesFormacionIntegral afi = getRegistroActividadesFormacionIntegral(actFormacionIntegral.getActividadesFormacionIntegral().getActividadFormacionIntegral());
                Boolean registroAlmacenado = false;
                if (afi != null) {
                    listaCondicional.add(actFormacionIntegral.getActividadesFormacionIntegral().getActividadFormacionIntegral());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    actFormacionIntegral.getActividadesFormacionIntegral().setRegistro(afi.getRegistro());
                    facdepye.edit(actFormacionIntegral.getActividadesFormacionIntegral());
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    actFormacionIntegral.getActividadesFormacionIntegral().setRegistro(registro.getRegistro());
                    facdepye.create(actFormacionIntegral.getActividadesFormacionIntegral());
                }
                facdepye.flush();
                }
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }
    
    @Override
    public ActividadesFormacionIntegral getRegistroActividadesFormacionIntegral(String actividadFormacionIntegral) {
        ActividadesFormacionIntegral actividadesFormacionIntegral = new ActividadesFormacionIntegral();
        TypedQuery<ActividadesFormacionIntegral> query = em.createQuery("SELECT a FROM ActividadesFormacionIntegral a WHERE a.actividadFormacionIntegral = :actividadFormacionIntegral", ActividadesFormacionIntegral.class);
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
        TypedQuery<ActividadesFormacionIntegral> query = em.createNamedQuery("ActividadesFormacionIntegral.findByActividadFormacionIntegral", ActividadesFormacionIntegral.class);
        query.setParameter("actividadFormacionIntegral", actividadFormacionIntegral);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }

    @Override
    public List<ActividadesTipos> getActividadesTiposAct() {
        List<ActividadesTipos> genLst = new ArrayList<>();
        TypedQuery<ActividadesTipos> query = em.createQuery("SELECT a FROM ActividadesTipos a", ActividadesTipos.class);
        
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
        TypedQuery<EventosTipos> query = em.createQuery("SELECT e FROM EventosTipos e", EventosTipos.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }
}
