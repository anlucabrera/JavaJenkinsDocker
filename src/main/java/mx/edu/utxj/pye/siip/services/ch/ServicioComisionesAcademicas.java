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
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.ComisionesAcademicas;
import mx.edu.utxj.pye.sgi.entity.pye2.ComisionesAcademicasParticipantes;
import mx.edu.utxj.pye.sgi.entity.pye2.ComisionesAcademicasTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.caphum.DTOComAcadParticipantes;
import mx.edu.utxj.pye.siip.dto.caphum.DTOComisionesAcademicas;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbComisionesAcademicas;
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
public class ServicioComisionesAcademicas implements EjbComisionesAcademicas{
    
    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
    @Inject ControladorEmpleado controladorEmpleado;
  
    @Override
    public List<DTOComisionesAcademicas> getListaComisionesAcademicas(String rutaArchivo) throws Throwable{
        List<Boolean> validarCelda = new ArrayList<>();
        List<String> datosInvalidos = new ArrayList<>();
       
        List<DTOComisionesAcademicas> listaDtoComisionesAcademicas = new ArrayList<>();
        
        ComisionesAcademicas comisionesAcademicas;
        ComisionesAcademicasTipos comisionesAcademicasTipos;
        DTOComisionesAcademicas dTOComisionesAcademicas;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
      
        int l=0;
        int o=0;
        int a=0;
        
        try {
        if (primeraHoja.getSheetName().equals("Comisiones Académicas")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((fila.getCell(1).getNumericCellValue()!= 0)) {
                comisionesAcademicas = new ComisionesAcademicas();
                comisionesAcademicasTipos = new ComisionesAcademicasTipos();
                dTOComisionesAcademicas = new DTOComisionesAcademicas();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case FORMULA:
                        comisionesAcademicas.setComisionAcademica(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case STRING: 
                        comisionesAcademicasTipos.setDescripcion(fila.getCell(2).getStringCellValue());
                        dTOComisionesAcademicas.setTipoComision(comisionesAcademicasTipos.getDescripcion());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case FORMULA: 
                        comisionesAcademicasTipos.setTipoComision((short)fila.getCell(3).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(4))) {
                         comisionesAcademicas.setFechaInicio(fila.getCell(4).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(5))) {
                         comisionesAcademicas.setFechaFin(fila.getCell(5).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case STRING: 
                        comisionesAcademicas.setLugar(fila.getCell(6).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                    case STRING:
                        comisionesAcademicas.setObjetivo(fila.getCell(7).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(8).getCellTypeEnum()) {
                    case STRING:
                        comisionesAcademicas.setAcuerdos(fila.getCell(8).getStringCellValue());
                        break;
                    default:
                        break;
                }
                
                switch (fila.getCell(10).getCellTypeEnum()){
                    case FORMULA:
                        l= (int) fila.getCell(10).getNumericCellValue();
                        break;
                    default:
                        break;
                }
                
                switch (fila.getCell(12).getCellTypeEnum()){
                    case FORMULA:
                        o= (int) fila.getCell(12).getNumericCellValue();
                        break;
                    default:
                        break;
                }
                
                switch (fila.getCell(14).getCellTypeEnum()){
                    case FORMULA:
                        a= (int) fila.getCell(14).getNumericCellValue();
                        break;
                    default:
                        break;
                }
                
                    if (l == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Lugar de la comisión en la columna: <b>" + (5 + 1) + " y fila: " + (i + 1) + "</b> \n");
                    }
                    if (o == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Objetivo de la comisión en la columna: <b>" + (6 + 1) + " y fila: " + (i + 1) + "</b> \n");
                    }
                    if (a == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Acuerdos de la comisión en la columna: <b>" + (7 + 1) + " y fila: " + (i + 1) + "</b> \n");
                    }
              
                    comisionesAcademicas.setTipoComision(comisionesAcademicasTipos);
                    dTOComisionesAcademicas.setComisionesAcademicas(comisionesAcademicas);
                    listaDtoComisionesAcademicas.add(dTOComisionesAcademicas);
                  }
            }
            libroRegistro.close();
            if (validarCelda.contains(false)) {
                    Messages.addGlobalError("<b>La hoja de registros de Comisiones Académicas contiene datos que no son válidos, verifique los datos de la plantilla</b>");
                    Messages.addGlobalError(datosInvalidos.toString());
                    
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\archivos\\log\\output.txt"), "utf-8"));
                    out.write(datosInvalidos.toString());
                    out.close();
                    
                    return Collections.EMPTY_LIST;
                } else {
                    Messages.addGlobalInfo("<b>Hoja de Comisiones Académicas Validada favor de verificar sus datos antes de guardar su información</b>");
                    return listaDtoComisionesAcademicas;
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
    public void guardaComisionesAcademicas(List<DTOComisionesAcademicas> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
            
            List<String> listaCondicional = new ArrayList<>();
            lista.forEach((comisiones) -> {
                
            f.setEntityClass(ComisionesAcademicas.class);
            ComisionesAcademicas comAcadEncontrado = getRegistroComisionesAcademicas(comisiones.getComisionesAcademicas());
            Boolean registroAlmacenado = false;
            if (comAcadEncontrado != null) {
                
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                
                if (ejbModulos.validaEventoRegistro(ejbModulos.getEventoRegistro(), comAcadEncontrado.getRegistros().getEventoRegistro().getEventoRegistro())) {
                    comisiones.getComisionesAcademicas().setRegistro(comAcadEncontrado.getRegistro());
                    listaCondicional.add(comAcadEncontrado.getComisionAcademica());
                    f.edit(comisiones.getComisionesAcademicas());
                    Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + comAcadEncontrado.getComisionAcademica());
                } else{
                    Messages.addGlobalWarn("<b>No se pueden actualizar los registros con los siguientes datos: </b> " + comAcadEncontrado.getComisionAcademica());
                }
                
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                comisiones.getComisionesAcademicas().setRegistro(registro.getRegistro());
                f.create(comisiones.getComisionesAcademicas());
                Messages.addGlobalInfo("<b>Se guardaron los registros correctamente</b>");
            }
            f.flush();
        });
 }
    
    @Override
    public Integer getRegistroComisionesAcademicasEspecifico(String comisionAcademica) {
        TypedQuery<ComisionesAcademicas> query = f.getEntityManager().createNamedQuery("ComisionesAcademicas.findByComisionAcademica", ComisionesAcademicas.class);
        query.setParameter("comisionAcademica", comisionAcademica);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }

    @Override
    public ComisionesAcademicas getRegistroComisionesAcademicas(ComisionesAcademicas comisionesAcademicas) {
        TypedQuery<ComisionesAcademicas> query = f.getEntityManager().createNamedQuery("ComisionesAcademicas.findByComisionAcademica", ComisionesAcademicas.class);
        query.setParameter("comisionAcademica", comisionesAcademicas.getComisionAcademica());
        try {
             comisionesAcademicas = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            comisionesAcademicas = null;
            ex.toString();
        }
        return comisionesAcademicas;
    }

    @Override
    public List<ComisionesAcademicasTipos> getComisionesAcademicasTiposAct() {
        List<ComisionesAcademicasTipos> genLst = new ArrayList<>();
        TypedQuery<ComisionesAcademicasTipos> query = f.getEntityManager().createQuery("SELECT c FROM ComisionesAcademicasTipos c", ComisionesAcademicasTipos.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }

    @Override
    public List<DTOComisionesAcademicas> getRegistroDTOComAcad(String mes, Short ejercicio) {
         List<DTOComisionesAcademicas> ldto = new ArrayList<>();
        TypedQuery<ComisionesAcademicas> q = f.getEntityManager()
                .createQuery("SELECT c from ComisionesAcademicas c WHERE c.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND c.registros.eventoRegistro.mes = :mes AND c.registros.area = :area", ComisionesAcademicas.class);
        q.setParameter("mes", mes);
        q.setParameter("ejercicio", ejercicio);
        q.setParameter("area", controladorEmpleado.getNuevaAreasUniversidad().getArea());
        List<ComisionesAcademicas> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            TypedQuery<EventosRegistros> query = f.getEntityManager().createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            l.forEach(x -> {
                Registros registro = f.getEntityManager().find(Registros.class, x.getRegistro());
                EventosRegistros eventos = f.getEntityManager().find(EventosRegistros.class,registro.getEventoRegistro().getEventoRegistro());
                ComisionesAcademicasTipos comisionesAcademicasTipos = f.getEntityManager().find(ComisionesAcademicasTipos.class, x.getTipoComision().getTipoComision());
                AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, registro.getArea());
                ActividadesPoa a = registro.getActividadesPoaList().isEmpty() ? null :registro.getActividadesPoaList().get(0);
                DTOComisionesAcademicas dto;
                if (eventoRegistro.equals(registro.getEventoRegistro())) {
                    dto = new DTOComisionesAcademicas(x, comisionesAcademicasTipos, a, eventos, comisionesAcademicasTipos.getDescripcion());
                } else {
                    dto = new DTOComisionesAcademicas(x, comisionesAcademicasTipos, a, eventos, comisionesAcademicasTipos.getDescripcion());
                }
                ldto.add(dto);
            });
            return ldto;
    }
    }
    
    @Override
    public List<DTOComAcadParticipantes> getRegistroDTOPartComAcad(String mes, Short ejercicio) {
        List<DTOComAcadParticipantes> ldto = new ArrayList<>();
        TypedQuery<ComisionesAcademicasParticipantes> q = f.getEntityManager()
                .createQuery("SELECT c from ComisionesAcademicasParticipantes c WHERE c.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND c.registros.eventoRegistro.mes = :mes AND c.registros.area = :area", ComisionesAcademicasParticipantes.class);
        q.setParameter("mes", mes);
        q.setParameter("ejercicio", ejercicio);
        q.setParameter("area", controladorEmpleado.getNuevaAreasUniversidad().getArea());
        List<ComisionesAcademicasParticipantes> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            TypedQuery<EventosRegistros> query = f.getEntityManager().createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            l.forEach(x -> {
                Registros registro = f.getEntityManager().find(Registros.class, x.getRegistro());
                EventosRegistros eventos = f.getEntityManager().find(EventosRegistros.class,registro.getEventoRegistro().getEventoRegistro());
                Personal personal = f.getEntityManager().find(Personal.class, x.getParticipante());
                AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, personal.getAreaOperativa());
                ActividadesPoa a = registro.getActividadesPoaList().isEmpty() ? null :registro.getActividadesPoaList().get(0);
                DTOComAcadParticipantes dto;
                if (eventoRegistro.equals(registro.getEventoRegistro())) {
                    dto = new DTOComAcadParticipantes(x, personal, au, a, eventos, au.getNombre());
                } else {
                    dto = new DTOComAcadParticipantes(x, personal, au, a, eventos, au.getNombre());
                }
                ldto.add(dto);
            });
            return ldto;
    }
    }


    @Override
    public List<Integer> buscaRegistroParticipantesComAcad(String clave) throws Throwable {
        List<Integer> registros = new ArrayList<>();
        try {
            registros = f.getEntityManager().createQuery("SELECT p FROM ComisionesAcademicasParticipantes p WHERE p.comisionAcademica.comisionAcademica = :clave", ComisionesAcademicasParticipantes.class)
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
    public List<Integer> buscaRegistroEvidenciasPartComAcad(String clave) throws Throwable {
        List<Integer> registros = new ArrayList<>();
        List<Integer> evidencias = new ArrayList<>();
        try {
            registros = f.getEntityManager().createQuery("SELECT p FROM ComisionesAcademicasParticipantes p WHERE p.comisionAcademica.comisionAcademica = :clave", ComisionesAcademicasParticipantes.class)
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
