/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.FeriasParticipantes;
import mx.edu.utxj.pye.sgi.entity.pye2.FeriasProfesiograficas;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOFeriasParticipantes;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaFeriasParticipantes;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaFeriasParticipantesDTO;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbFeriasParticipantes;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbFeriasProfesiograficas;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbIems;
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
public class ServicioFeriasParticipantes implements EjbFeriasParticipantes{
    
    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
    @EJB EjbFeriasProfesiograficas ejbFeriasProfesiograficas;
    @EJB EjbIems ejbIems;
    @Inject ControladorEmpleado controladorEmpleado;
    
    @Getter @Setter private List<Short> areas;
    
    @Override
    public  ListaFeriasParticipantes getListaFeriasParticipantes(String rutaArchivo) throws Throwable {
       
        ListaFeriasParticipantes listaFeriasParticipantes = new  ListaFeriasParticipantes();

        List<DTOFeriasParticipantes> listaDtoFeriasParticipantes = new ArrayList<>();
        Iems iems;
        FeriasProfesiograficas feriasProfesiograficas;
        FeriasParticipantes feriasParticipantes;
        DTOFeriasParticipantes dTOFeriasParticipantes;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(1);
        XSSFRow fila;
        
        if (primeraHoja.getSheetName().equals("Participantes Ferias")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((!"".equals(fila.getCell(0).getStringCellValue()))) {
                iems = new Iems();
                feriasProfesiograficas = new FeriasProfesiograficas();
                feriasParticipantes = new FeriasParticipantes();
                dTOFeriasParticipantes = new DTOFeriasParticipantes();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        feriasProfesiograficas.setFeria(fila.getCell(0).getStringCellValue());
                        feriasParticipantes.setFeria(feriasProfesiograficas);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(21).getCellTypeEnum()) {
                    case STRING: 
                        iems.setNombre(fila.getCell(21).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(22).getCellTypeEnum()) {
                    case FORMULA:
                        iems.setIems((int)fila.getCell(22).getNumericCellValue());
                        feriasParticipantes.setIems(iems);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(23).getCellTypeEnum()) {
                    case NUMERIC: 
                       feriasParticipantes.setParticipantes((int) fila.getCell(23).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                    dTOFeriasParticipantes.setFeriasParticipantes(feriasParticipantes);
                    listaDtoFeriasParticipantes.add(dTOFeriasParticipantes);
                    
                }
            }
            listaFeriasParticipantes.setFeriasParticipantes(listaDtoFeriasParticipantes);
            libroRegistro.close();
            Messages.addGlobalInfo("<b>Hoja de Participantes de Ferias Profesiográficas Validada favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
        }
        
        return listaFeriasParticipantes;
        
    }

    @Override
    public void guardaFeriasParticipantes(ListaFeriasParticipantes listaFeriasParticipantes, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
            
            listaFeriasParticipantes.getFeriasParticipantes().forEach((feriasParticipantes) -> {

            FeriasProfesiograficas feriasProfesiograficas = ejbFeriasProfesiograficas.getRegistroFeriasProfesiograficas(feriasParticipantes.getFeriasParticipantes().getFeria());
            if (feriasProfesiograficas == null || feriasProfesiograficas.getFeria().isEmpty()) {
                Messages.addGlobalWarn("<b>No existe la Clave de Feria Profesiográfica</b>");

            } else {
                if (ejbModulos.validaEventoRegistro(ejbModulos.getEventoRegistro(), feriasProfesiograficas.getRegistros().getEventoRegistro().getEventoRegistro())) {
                    List<String> listaCondicional = new ArrayList<>();
                    try {
                        f.setEntityClass(FeriasParticipantes.class);
                        FeriasParticipantes ferPartEncontrado = getRegistroFeriasParticipantes(feriasParticipantes.getFeriasParticipantes());
                        Boolean registroAlmacenado = false;
                        if (ferPartEncontrado != null) {
                            listaCondicional.add(feriasParticipantes.getFeriasParticipantes().getFeria().getFeria() + " - " + feriasParticipantes.getFeriasParticipantes().getIems().getNombre());
                            registroAlmacenado = true;
                        }
                        if (registroAlmacenado) {
                            feriasParticipantes.getFeriasParticipantes().setRegistro(ferPartEncontrado.getRegistro());
                            feriasParticipantes.getFeriasParticipantes().getFeria().setRegistro(ferPartEncontrado.getFeria().getRegistro());
                            f.edit(feriasParticipantes.getFeriasParticipantes());
                            Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
                        } else {
                            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                            feriasParticipantes.getFeriasParticipantes().getIems().setIems(ejbIems.getRegistroIemsEspecifico(feriasParticipantes.getFeriasParticipantes().getIems().getIems()));
                            feriasParticipantes.getFeriasParticipantes().getFeria().setRegistro(ejbFeriasProfesiograficas.getRegistroFeriasProfesiograficasEspecifico(feriasParticipantes.getFeriasParticipantes().getFeria().getFeria()));
                            feriasParticipantes.getFeriasParticipantes().setRegistro(registro.getRegistro());
                            f.create(feriasParticipantes.getFeriasParticipantes());
                            Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b> ");
                        }
                        f.flush();
                    } catch (Throwable ex) {
                        Logger.getLogger(ServicioFeriasProfesiograficas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {

                    Messages.addGlobalWarn("<b>No puede registrar información de periodos anteriores</b>");
                }
            }
        });
    }

    @Override
    public FeriasParticipantes getRegistroFeriasParticipantes(FeriasParticipantes feriasParticipantes) {
        TypedQuery<FeriasParticipantes> query = f.getEntityManager().createQuery("SELECT p FROM FeriasParticipantes p JOIN p.feria f JOIN p.iems i WHERE f.feria =:feria AND i.iems = :iems", FeriasParticipantes.class);
        query.setParameter("feria", feriasParticipantes.getFeria().getFeria());
        query.setParameter("iems", feriasParticipantes.getIems().getIems());
        try {
            feriasParticipantes = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            feriasParticipantes = null;
            ex.toString();
        }
        return feriasParticipantes; 
    }

    @Override
    public List<ListaFeriasParticipantesDTO> getRegistrosFParticipantes(String mes, Short ejercicio) {
        //verificar que los parametros no sean nulos
        if(mes == null || ejercicio == null){
            return null;
        }
        Short area = controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
        List<FeriasParticipantes> q = new ArrayList<>();
        List<ListaFeriasParticipantesDTO> ldto = new ArrayList<>();
        
        if (area == 6 || area == 9) {
            q = f.getEntityManager().createQuery("SELECT p from FeriasParticipantes p WHERE p.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND p.registros.eventoRegistro.mes = :mes", FeriasParticipantes.class)
                    .setParameter("mes", mes)
                    .setParameter("ejercicio", ejercicio)
                    .getResultList();
        } else {
            areas = ejbModulos.getAreasDependientes(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());

            q = f.getEntityManager().createQuery("SELECT p from FeriasParticipantes p WHERE p.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND p.registros.eventoRegistro.mes = :mes AND p.registros.area IN :areas", FeriasParticipantes.class)
                    .setParameter("mes", mes)
                    .setParameter("ejercicio", ejercicio)
                    .setParameter("areas", areas)
                    .getResultList();
        }
        if (q.isEmpty() || q == null) {
            return null;
        } else {
//            l.forEach(System.err::println);
            TypedQuery<EventosRegistros> query = f.getEntityManager().createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            q.forEach(x -> {

                ListaFeriasParticipantesDTO dto;
                Registros registro = f.getEntityManager().find(Registros.class, x.getRegistro());
                AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, registro.getArea());
                ActividadesPoa a = registro.getActividadesPoaList().isEmpty() ? null : registro.getActividadesPoaList().get(0);
                if (eventoRegistro.equals(registro.getEventoRegistro())) {
                    dto = new ListaFeriasParticipantesDTO(Boolean.TRUE, x, au, a);
                } else {
                    dto = new ListaFeriasParticipantesDTO(Boolean.FALSE, x, au, a);
                }
                ldto.add(dto);
            });
            return ldto;
        }
    }
}
