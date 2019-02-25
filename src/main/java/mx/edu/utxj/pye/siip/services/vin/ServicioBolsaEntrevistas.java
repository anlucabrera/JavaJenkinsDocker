/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.BolsaTrabajo;
import mx.edu.utxj.pye.sgi.entity.pye2.BolsaTrabajoEntrevistas;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOBolsaEntrevistas;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbBolsaTrabajo;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbBolsaEntrevistas;
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
public class ServicioBolsaEntrevistas implements EjbBolsaEntrevistas{
     
    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
    @EJB EjbBolsaTrabajo ejbBolsaTrabajo;
    @Inject Caster caster; 
    @Inject ControladorEmpleado controladorEmpleado;
    
    @Override
    public List<DTOBolsaEntrevistas> getListaBolsaEntrevistas(String rutaArchivo) throws Throwable {
        List<Boolean> validarCelda = new ArrayList<>();
        List<String> datosInvalidos = new ArrayList<>();
     
        List<DTOBolsaEntrevistas> listaDtoBolsaEntrevistas = new ArrayList<>();
        AreasUniversidad areasUniversidad;
        Generaciones generaciones;
        BolsaTrabajo bolsaTrabajo;
        BolsaTrabajoEntrevistas bolsaTrabajoEntrevistas;
        DTOBolsaEntrevistas dTOBolsaEntrevistas;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(1);
        XSSFRow fila;
        
        String mat="";
        int obs =0;
        
        try{
        if (primeraHoja.getSheetName().equals("Entrevistas Bolsa de Trabajo")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((!"".equals(fila.getCell(6).getStringCellValue()))) {
                areasUniversidad = new AreasUniversidad();
                generaciones = new Generaciones();
                bolsaTrabajo = new BolsaTrabajo();
                bolsaTrabajoEntrevistas = new BolsaTrabajoEntrevistas();
                dTOBolsaEntrevistas = new DTOBolsaEntrevistas();
               
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        bolsaTrabajo.setBolsatrab(fila.getCell(0).getStringCellValue());
                        bolsaTrabajoEntrevistas.setBolsatrabent(bolsaTrabajo);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(1).getCellTypeEnum()) {
                    case STRING:
                        dTOBolsaEntrevistas.setGeneracion(fila.getCell(1).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case FORMULA: 
                        generaciones.setGeneracion((short) fila.getCell(2).getNumericCellValue());
                        bolsaTrabajoEntrevistas.setGeneracion(generaciones.getGeneracion());
                        break;
                    default:
                        break;
                }
              
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case STRING: 
                        bolsaTrabajoEntrevistas.setMatricula(fila.getCell(3).getStringCellValue());
                        break;
                    case NUMERIC: 
                        int m = (int)fila.getCell(3).getNumericCellValue();
                        mat= Integer.toString(m);
                        bolsaTrabajoEntrevistas.setMatricula(mat);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case STRING: 
                        areasUniversidad.setNombre(fila.getCell(4).getStringCellValue());
                        dTOBolsaEntrevistas.setProgramaEducativo(fila.getCell(4).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case FORMULA: 
                        areasUniversidad.setArea((short) fila.getCell(5).getNumericCellValue());
                        bolsaTrabajoEntrevistas.setProgramaEducativo(areasUniversidad.getArea());
                        break;
                    default:
                        break;
                }
                
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case STRING: 
                        bolsaTrabajoEntrevistas.setContratado(fila.getCell(6).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                    case STRING: 
                        bolsaTrabajoEntrevistas.setObservaciones(fila.getCell(7).getStringCellValue());
                        break;
                    default:
                        break;

                }
                    switch (fila.getCell(9).getCellTypeEnum()) {
                        case FORMULA:
                            obs = (int) fila.getCell(9).getNumericCellValue();
                            break;
                        default:
                            break;
                    }
                    if (obs == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Observaciones de la Entrevista en la columna: <b>" + (5 + 1) + " y fila: " + (i + 1) + "</b> \n");
                    }
                   
                    dTOBolsaEntrevistas.setBolsaTrabajoEntrevistas(bolsaTrabajoEntrevistas);
                    listaDtoBolsaEntrevistas.add(dTOBolsaEntrevistas);
                    
                }
            }
            libroRegistro.close();
            if (validarCelda.contains(false)) {
                    Messages.addGlobalError("<b>Hoja de Entrevistas Bolsa de Trabajo contiene datos que no son válidos, verifique los datos de la plantilla</b>");
                    Messages.addGlobalError(datosInvalidos.toString());
                    return Collections.EMPTY_LIST;
                } else {
                    Messages.addGlobalInfo("<b>Hoja de Entrevistas Bolsa de Trabajo Validada favor de verificar sus datos antes de guardar su información</b>");
                    return listaDtoBolsaEntrevistas;
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
    public void guardaBolsaEntrevistas(List<DTOBolsaEntrevistas> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
        
        lista.forEach((bolsaEntrevistas) -> {
            
            BolsaTrabajo bolsaTrabajo = ejbBolsaTrabajo.getRegistroBolsaTrabajo(bolsaEntrevistas.getBolsaTrabajoEntrevistas().getBolsatrabent());
            if (bolsaTrabajo == null || bolsaTrabajo.getBolsatrab().isEmpty()) {
                Messages.addGlobalWarn("<b>No existe la Clave de Bolsa de Trabajo</b>");

            } else {
                
                if (ejbModulos.validaPeriodoRegistro(ejbModulos.getPeriodoEscolarActual(), bolsaTrabajo.getPeriodo())) {

                    f.setEntityClass(BolsaTrabajoEntrevistas.class);
                    BolsaTrabajoEntrevistas bolTrabEntEncontrado = getRegistroBolsaTrabajoEntrevistas(bolsaEntrevistas.getBolsaTrabajoEntrevistas());
                    Boolean registroAlmacenado = false;
                    if (bolTrabEntEncontrado != null) {
                        registroAlmacenado = true;
                    }
                    if (registroAlmacenado) {
                        bolsaEntrevistas.getBolsaTrabajoEntrevistas().setRegistro(bolTrabEntEncontrado.getRegistro());
                        bolsaEntrevistas.getBolsaTrabajoEntrevistas().getBolsatrabent().setRegistro(bolTrabEntEncontrado.getBolsatrabent().getRegistro());
                        f.edit(bolsaEntrevistas.getBolsaTrabajoEntrevistas());
                        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + bolTrabEntEncontrado.getBolsatrabent().getBolsatrab() + " - " + bolTrabEntEncontrado.getMatricula());
                    } else {
                        Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                        bolsaEntrevistas.getBolsaTrabajoEntrevistas().getBolsatrabent().setRegistro(ejbBolsaTrabajo.getRegistroBolsaTrabajoEspecifico(bolsaEntrevistas.getBolsaTrabajoEntrevistas().getBolsatrabent().getBolsatrab()));
                        bolsaEntrevistas.getBolsaTrabajoEntrevistas().setRegistro(registro.getRegistro());
                        f.create(bolsaEntrevistas.getBolsaTrabajoEntrevistas());
                        Messages.addGlobalInfo("<b>Se guardaron los registros correctamente</b> ");
                    }
                    f.flush();
                } else {

                    Messages.addGlobalWarn("<b>No puede registrar información de periodos anteriores</b>");
                }
            }
        });
   
    }

    @Override
    public BolsaTrabajoEntrevistas getRegistroBolsaTrabajoEntrevistas(BolsaTrabajoEntrevistas bolsaTrabajoEntrevistas) {
        TypedQuery<BolsaTrabajoEntrevistas> query = f.getEntityManager().createQuery("SELECT e FROM BolsaTrabajoEntrevistas e JOIN e.bolsatrabent b WHERE b.bolsatrab = :bolsatrabent AND e.generacion = :generacion AND e.matricula = :matricula", BolsaTrabajoEntrevistas.class);
        query.setParameter("bolsatrabent", bolsaTrabajoEntrevistas.getBolsatrabent().getBolsatrab());
        query.setParameter("generacion", bolsaTrabajoEntrevistas.getGeneracion());
        query.setParameter("matricula", bolsaTrabajoEntrevistas.getMatricula());
        try {
            bolsaTrabajoEntrevistas = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            bolsaTrabajoEntrevistas = null;
            ex.toString();
        }
        return bolsaTrabajoEntrevistas; 
    }

    @Override
    public List<DTOBolsaEntrevistas> getListaEntrevistaBolsaTrabajo(String bolsaTrab) {
        if(bolsaTrab == null){
            return Collections.EMPTY_LIST;
        }
         
        List<BolsaTrabajoEntrevistas> entities = new ArrayList<>();
        List<DTOBolsaEntrevistas> l = new ArrayList<>();
         
        entities = f.getEntityManager().createQuery("SELECT e FROM BolsaTrabajoEntrevistas e JOIN e.bolsatrabent b WHERE b.bolsatrab = :bolsaTrab", BolsaTrabajoEntrevistas.class)
                .setParameter("bolsaTrab", bolsaTrab)
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
}
