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
import java.util.List;
import java.util.stream.Collectors;
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
import mx.edu.utxj.pye.sgi.entity.pye2.FeriasProfesiograficas;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.FeriasParticipantes;
import mx.edu.utxj.pye.sgi.entity.pye2.MunicipioPK;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.LocalidadPK;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOFerias;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaFeriasDTO;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaFeriasProfesiograficas;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbFeriasProfesiograficas;
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
public class ServicioFeriasProfesiograficas implements EjbFeriasProfesiograficas{
    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
    @Inject ControladorEmpleado controladorEmpleado;
    
    @Getter @Setter private List<Short> areas;
   
    @Override
    public ListaFeriasProfesiograficas getListaFeriasProfesiograficas(String rutaArchivo) throws Throwable {
        List<Boolean> validarCelda = new ArrayList<>();
        List<String> datosInvalidos = new ArrayList<>();
       
        ListaFeriasProfesiograficas listaFeriasProfesiograficas = new  ListaFeriasProfesiograficas();

        List<DTOFerias> listaDtoFerias = new ArrayList<>();
        Estado estado;
        Municipio municipio;
        MunicipioPK municipioPK;
        Localidad localidad;
        LocalidadPK localidadPK;
        FeriasProfesiograficas feriasProfesiograficas;
        DTOFerias dTOFerias;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        int eve = 0;
        
        try {
        if (primeraHoja.getSheetName().equals("Ferias Profesiográficas")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((!"".equals(fila.getCell(5).getStringCellValue()))) {
                estado = new Estado();
                municipio = new Municipio();
                municipioPK = new MunicipioPK();
                localidad = new  Localidad();
                localidadPK = new LocalidadPK();
                feriasProfesiograficas = new FeriasProfesiograficas();
                dTOFerias = new DTOFerias();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case FORMULA:
                        feriasProfesiograficas.setFeria(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case NUMERIC: 
                         if (DateUtil.isCellDateFormatted(fila.getCell(2))) {
                         feriasProfesiograficas.setFecha(fila.getCell(2).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case STRING:
                         feriasProfesiograficas.setEvento(fila.getCell(5).getStringCellValue());
                        break;
                    case NUMERIC:
                         String even = Integer.toString((int) fila.getCell(5).getNumericCellValue());
                         feriasProfesiograficas.setEvento(even);
                         break;
                    default:
                        break;
                }
                 switch (fila.getCell(11).getCellTypeEnum()) {
                        case STRING:
                            estado.setNombre(fila.getCell(11).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(12).getCellTypeEnum()) {
                        case FORMULA:
                            estado.setIdestado((int)fila.getCell(12).getNumericCellValue());
                            localidadPK.setClaveEstado((int)fila.getCell(12).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(16).getCellTypeEnum()) {
                        case STRING:
                            municipio.setNombre(fila.getCell(16).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(17).getCellTypeEnum()) {
                        case FORMULA:
                            municipioPK.setClaveMunicipio((int)fila.getCell(17).getNumericCellValue());
                            municipio.setMunicipioPK(municipioPK);
                            municipio.setEstado(estado);
                            localidadPK.setClaveMunicipio((int) fila.getCell(17).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(22).getCellTypeEnum()) {
                        case STRING:
                            localidad.setNombre(fila.getCell(22).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(23).getCellTypeEnum()) {
                        case FORMULA:
                            localidadPK.setClaveLocalidad((int) fila.getCell(23).getNumericCellValue());
                            localidad.setLocalidadPK(localidadPK);
                            localidad.setMunicipio(municipio);
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(26).getCellTypeEnum()) {
                        case FORMULA:
                            eve = (int) fila.getCell(26).getNumericCellValue();
                            break;
                        default:
                            break;
                    }

                    if (eve == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Nombre del Evento en la columna: <b>" + (2 + 1) + " y fila: " + (i + 1) + "</b> \n");
                    }
                    
                    feriasProfesiograficas.setLocalidad(localidad);
                    dTOFerias.setFeriasProfesiograficas(feriasProfesiograficas);
                    listaDtoFerias.add(dTOFerias);
                    
                }
            }
            listaFeriasProfesiograficas.setFerias(listaDtoFerias);
            libroRegistro.close();
            if (validarCelda.contains(false)) {
                    Messages.addGlobalError("<b>Hoja de Ferias Profesiográficas contiene datos que no son válidos, verifique los datos de la plantilla</b>");
                    Messages.addGlobalError(datosInvalidos.toString());
                  
                    return null;
                } else {
                    Messages.addGlobalInfo("<b>Hoja de Ferias Profesiográficas Validada favor de verificar sus datos antes de guardar su información</b>");
                    return listaFeriasProfesiograficas;
                }
       } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
            return null;
        }
    } catch (IOException e) {
            libroRegistro.close();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            Messages.addGlobalError("<b>Ocurrió un error durante la lectura del archivo, asegurese de haber registrado correctamente su información</b>");
            return null;
    }

    }

    @Override
    public void guardaFeriasProfesiograficas(ListaFeriasProfesiograficas listaFeriasProfesiograficas, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
        List<String> listaCondicional = new ArrayList<>();
        listaFeriasProfesiograficas.getFerias().forEach((ferias) -> {
            f.setEntityClass(FeriasProfesiograficas.class);
            FeriasProfesiograficas ferProEncontrada = getRegistroFeriasProfesiograficas(ferias.getFeriasProfesiograficas());
            Boolean registroAlmacenado = false;

            if (ferProEncontrada != null) {
                listaCondicional.add(ferias.getFeriasProfesiograficas().getFeria());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                if (ejbModulos.validaEventoRegistro(ejbModulos.getEventoRegistro(), ferProEncontrada.getRegistros().getEventoRegistro().getEventoRegistro())) {
                    ferias.getFeriasProfesiograficas().setRegistro(ferProEncontrada.getRegistro());
                    f.edit(ferias.getFeriasProfesiograficas());
                    Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + ferProEncontrada.getFeria());
                } else {
                    Messages.addGlobalWarn("<b>No se pueden actualizar los registros con los siguientes datos: </b> " + ferProEncontrada.getFeria());
                }
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                ferias.getFeriasProfesiograficas().setRegistro(registro.getRegistro());
                f.create(ferias.getFeriasProfesiograficas());
                Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b> ");
            }
            f.flush();
        });
}
    @Override
    public Integer getRegistroFeriasProfesiograficasEspecifico(String feria) {
        TypedQuery<FeriasProfesiograficas> query = f.getEntityManager().createNamedQuery("FeriasProfesiograficas.findByFeria", FeriasProfesiograficas.class);
        query.setParameter("feria", feria);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }

    @Override
    public FeriasProfesiograficas getRegistroFeriasProfesiograficas(FeriasProfesiograficas feriasProfesiograficas) {
        TypedQuery<FeriasProfesiograficas> query = f.getEntityManager().createQuery("SELECT f FROM FeriasProfesiograficas f WHERE f.feria = :feria", FeriasProfesiograficas.class);
        query.setParameter("feria", feriasProfesiograficas.getFeria());
        try {
            feriasProfesiograficas = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            feriasProfesiograficas = null;
            System.out.println(ex.toString());
        }
        return feriasProfesiograficas;
    }

    @Override
    public List<ListaFeriasDTO> getRegistroFeriaProf(String mes, Short ejercicio) {
        //verificar que los parametros no sean nulos
        if(mes == null || ejercicio == null){
            return null;
        }
        Short area = controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
        List<FeriasProfesiograficas> q = new ArrayList<>();
        List<ListaFeriasDTO> ldto = new ArrayList<>();
        
        if (area == 6 || area == 9) {
            q = f.getEntityManager().createQuery("SELECT a from FeriasProfesiograficas a WHERE a.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND a.registros.eventoRegistro.mes = :mes", FeriasProfesiograficas.class)
                    .setParameter("mes", mes)
                    .setParameter("ejercicio", ejercicio)
                    .getResultList();

        } else {
            areas = ejbModulos.getAreasDependientes(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());

            q = f.getEntityManager().createQuery("SELECT a from FeriasProfesiograficas a WHERE a.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND a.registros.eventoRegistro.mes = :mes AND a.registros.area IN :areas", FeriasProfesiograficas.class)
                    .setParameter("mes", mes)
                    .setParameter("ejercicio", ejercicio)
                    .setParameter("areas", areas)
                    .getResultList();
        }
        
        if (q.isEmpty() || q == null) {
            return null;
        } else {
            TypedQuery<EventosRegistros> query = f.getEntityManager().createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            q.forEach(x -> {            
                Registros registro = f.getEntityManager().find(Registros.class, x.getRegistro());
                AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, registro.getArea());
                ActividadesPoa a = registro.getActividadesPoaList().isEmpty()?null:registro.getActividadesPoaList().get(0);
                ListaFeriasDTO dto;
                if (eventoRegistro.equals(registro.getEventoRegistro())) {
                    dto = new ListaFeriasDTO(Boolean.TRUE,  x, au, a);
                            } else {
                    dto = new ListaFeriasDTO(Boolean.FALSE, x, au, a);
                }
                ldto.add(dto);
            });
            return ldto;
        }
    }

    @Override
    public List<Integer> buscaRegistroParticipantesFeriasProf(String clave) throws Throwable {
         List<Integer> registros = new ArrayList<>();
        try {
            registros = f.getEntityManager().createQuery("SELECT p FROM FeriasParticipantes p WHERE p.feria.feria = :clave", FeriasParticipantes.class)
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
    public List<Integer> buscaRegistroEvidenciasPartFeriasProf(String clave) throws Throwable {
        List<Integer> registros = new ArrayList<>();
        List<Integer> evidencias = new ArrayList<>();
        try {
            registros = f.getEntityManager().createQuery("SELECT p FROM FeriasParticipantes p WHERE p.feria.feria= :clave", FeriasParticipantes.class)
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
