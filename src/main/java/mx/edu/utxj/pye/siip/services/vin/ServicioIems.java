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
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.entity.pye2.IemsServedu;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.LocalidadPK;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.MunicipioPK;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.siip.dto.prontuario.DTOIems;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.entity.prontuario.list.ListaIemsPrevia;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
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
public class ServicioIems implements EjbIems {

    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
   
    @Override
    public List<Iems> getIems() {
        TypedQuery<Iems> q = f.getEntityManager().createQuery("SELECT i FROM Iems i ORDER BY i.iems DESC", Iems.class);
        q.setMaxResults(10);
        q.getResultList().forEach(System.err::println);
        return q.getResultList();
    }

    @Override
    public List<Iems> getIemsByFechas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cambiarStatusIEMS(Integer iemsID) throws Throwable{
        Iems iems = f.getEntityManager().find(Iems.class, iemsID);
        if (iems.getStatus() == 0) {

            TypedQuery<Iems> q = f.getEntityManager().createQuery("UPDATE Iems i SET i.status =1 WHERE i.iems = :iems", Iems.class);
            q.setParameter("iems", iems.getIems());
            q.executeUpdate();
            addDetailMessage("Se activó correctamente el IEMS seleccionado");
            
        } else {

            TypedQuery<Iems> q = f.getEntityManager().createQuery("UPDATE Iems i SET i.status =0 WHERE i.iems = :iems", Iems.class);
            q.setParameter("iems", iems.getIems());
            q.executeUpdate();
            addDetailMessage("Se desactivó correctamente el IEMS seleccionado");

        }
    }
    
    @Override
    public List<Iems> filtroIems(Integer estado, Integer municipio){
        TypedQuery<Iems> q = f.getEntityManager()
                .createQuery("SELECT i from Iems i WHERE i.localidad.localidadPK.claveEstado = :estado AND i.localidad.municipio.municipioPK.claveMunicipio =:municipio ORDER BY i.iems DESC", Iems.class);
        q.setParameter("estado", estado);
        q.setParameter("municipio", municipio);
        
        List<Iems> li = q.getResultList();
        if(li.isEmpty() || li == null){
            return null;
        }else{
            return li;
        }
    }

    @Override
    public ListaIemsPrevia getListaIemsPrevia (String rutaArchivo) throws Throwable {
        List<Boolean> validarCelda = new ArrayList<>();
        List<String> datosInvalidos = new ArrayList<>();

        ListaIemsPrevia listaIemsPrevia = new ListaIemsPrevia();

        List<DTOIems> listaDtoIems = new ArrayList<>();
        
        Iems iems;
        IemsServedu iemsServedu;
        DTOIems dTOIems;
        Localidad localidad;
        LocalidadPK localidadPK;
        Municipio municipio;
        MunicipioPK municipioPK;
        Estado estado;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;

        String lada = "";
        String tel = "";
        int iem = 0;
        int col =0;
        int call = 0;
        int app= 0;
        int apm =0;
        int nomb = 0;
        
        try{
        if (primeraHoja.getSheetName().equals("IEMS")) {
        for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((!"".equals(fila.getCell(1).getStringCellValue()))) {
                    iems = new Iems();
                    iemsServedu = new IemsServedu();
                    dTOIems = new DTOIems();
                    localidad = new Localidad();
                    localidadPK = new LocalidadPK();
                    municipio = new Municipio();
                    municipioPK = new MunicipioPK();
                    estado = new Estado();

                    switch (fila.getCell(0).getCellTypeEnum()) {
                        case STRING:
                            iems.setNombre(fila.getCell(0).getStringCellValue());
                            break;
                        case NUMERIC:
                            String nom = Integer.toString((int) fila.getCell(0).getNumericCellValue());
                            iems.setNombre(nom);
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(1).getCellTypeEnum()) {
                        case STRING:
                            iems.setClave(fila.getCell(1).getStringCellValue());
                            break;
                        case NUMERIC:
                            String cla = Integer.toString((int) fila.getCell(1).getNumericCellValue());
                            iems.setClave(cla);
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(2).getCellTypeEnum()) {
                        case STRING:
                            iems.setTurno(fila.getCell(2).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(3).getCellTypeEnum()) {
                        case STRING:
                            iems.setTipo(fila.getCell(3).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(4).getCellTypeEnum()) {
                        case STRING:
                            iems.setAmbito(fila.getCell(4).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(5).getCellTypeEnum()) {
                        case STRING:
                            iemsServedu.setDescripcion(fila.getCell(5).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(6).getCellTypeEnum()) {
                        case FORMULA:
                            iemsServedu.setServeducativo((int) fila.getCell(6).getNumericCellValue());
                            iems.setServedu(iemsServedu);
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
                    switch (fila.getCell(28).getCellTypeEnum()) {
                        case FORMULA:
                            iems.setDomicilio(fila.getCell(28).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(29).getCellTypeEnum()) {
                        case NUMERIC:
                            int l = (int) fila.getCell(29).getNumericCellValue();
                            lada = Integer.toString(l);
                            iems.setLada(lada);
                            break;
                        case STRING:
                            iems.setLada(fila.getCell(29).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(30).getCellTypeEnum()) {
                        case NUMERIC:
                            int t = (int) fila.getCell(30).getNumericCellValue();
                            tel = Integer.toString(t);
                            iems.setTelefono(tel);
                            break;
                        case STRING:
                            iems.setTelefono(fila.getCell(30).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(34).getCellTypeEnum()) {
                        case FORMULA:
                            iems.setResponsable(fila.getCell(34).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(35).getCellTypeEnum()) {
                        case STRING:
                            iems.setCorreo(fila.getCell(35).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(42).getCellTypeEnum()) {
                        case FORMULA:
                            iem = (int) fila.getCell(42).getNumericCellValue();
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(43).getCellTypeEnum()) {
                        case FORMULA:
                            col = (int) fila.getCell(43).getNumericCellValue();
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(44).getCellTypeEnum()) {
                        case FORMULA:
                            call = (int) fila.getCell(44).getNumericCellValue();
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(45).getCellTypeEnum()) {
                        case FORMULA:
                            app = (int) fila.getCell(45).getNumericCellValue();
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(46).getCellTypeEnum()) {
                        case FORMULA:
                            apm = (int) fila.getCell(46).getNumericCellValue();
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(47).getCellTypeEnum()) {
                        case FORMULA:
                            nomb = (int) fila.getCell(47).getNumericCellValue();
                            break;
                        default:
                            break;
                    }
                    

                    if (iem == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Nombre del IEMS en la columna: " + (0 + 1) + " y fila: " + (i + 1) + " \n ");
                    }
                    if (col == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Colonia - Ubicación en la columna: " + (9 + 1) + " y fila: " + (i + 1) + " \n ");
                    }
                    if (call == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Calle - Ubicación en la columna: " + (10 + 1) + " y fila: " + (i + 1) + " \n ");
                    }
                    if (app == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Apellido Paterno - Responsable del ingreso en la columna: " + (15 + 1) + " y fila: " + (i + 1) + " \n ");
                    }
                    if (apm == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Apellido Materno - Responsable del ingreso en la columna: " + (16 + 1) + " y fila: " + (i + 1) + " \n ");
                    }
                    if (nomb == 1) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Nombre - Responsable en la columna: " + (17 + 1) + " y fila: " + (i + 1) + " \n ");
                    }
                    iems.setStatus((short)1);
                    iems.setLocalidad(localidad);
                    dTOIems.setIems(iems);
                    listaDtoIems.add(dTOIems);
                }
            }
           listaIemsPrevia.setDTOIems(listaDtoIems);
           libroRegistro.close();
            if (validarCelda.contains(false)) {
                    Messages.addGlobalError("<b>El archivo cargado contiene datos que no son válidos, verifique los datos de la plantilla</b>");
                    Messages.addGlobalError(datosInvalidos.toString());
                    excel.delete();
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                  
                    return null;
                } else {
                    Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
                    return listaIemsPrevia;
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
    public void guardaIems(ListaIemsPrevia listaIemsPrevia) throws Throwable {
            List<String> listaCondicional = new ArrayList<>();
            listaIemsPrevia.getDTOIems().forEach((dTOIems) -> {
            f.setEntityClass(Iems.class);
            Iems iemsEncontrado = getRegistroIems(dTOIems.getIems());
            Boolean registroAlmacenado = false;

            if (iemsEncontrado != null) {
                listaCondicional.add(dTOIems.getIems().getClave());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                dTOIems.getIems().setIems(iemsEncontrado.getIems());
                f.edit(dTOIems.getIems());
                Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
            } else {
                f.create(dTOIems.getIems());
                Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b> ");
            }
            f.flush();
        });
        
    }

    @Override
    public Iems getRegistroIems(Iems iems) {
        TypedQuery<Iems> query = f.getEntityManager().createQuery("SELECT i FROM Iems i WHERE i.clave =:clave", Iems.class);
        query.setParameter("clave", iems.getClave());
        try {
            iems = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            iems = null;
            System.out.println(ex.toString());
        }
        return iems;
    }

    @Override
    public List<Iems> getIemsVigentes() {
 
        TypedQuery<Iems> q = f.getEntityManager().createQuery("SELECT i FROM Iems i", Iems.class);
        q.getResultList().forEach(System.err::println);
        return q.getResultList();
      
    }

    @Override
    public Integer getRegistroIemsEspecifico(Integer iems) {
        TypedQuery<Iems> query = f.getEntityManager().createNamedQuery("Iems.findByIems",  Iems.class);
        query.setParameter("iems", iems);
        Integer registro = query.getSingleResult().getIems();
        return registro;
    }

    @Override
    public List<IemsServedu> getServEducativosAct() {
        List<IemsServedu> genLst = new ArrayList<>();
        TypedQuery<IemsServedu> query = f.getEntityManager().createQuery("SELECT i FROM IemsServedu i", IemsServedu.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }

    @Override
    public List<Iems> getListaStatusPorRegistro(Integer iemsID) {
        if (iemsID == null) {
            return Collections.EMPTY_LIST;
        }
        List<Iems> l = f.getEntityManager().createQuery("SELECT i FROM Iems i WHERE i.status=1 AND i.iems = :iems", Iems.class)
                .setParameter("iems", iemsID)
                .getResultList();
        return l;
    }

    @Override
    public Iems actualizarIems(Iems nuevoIems) throws Throwable {
        f.setEntityClass(Iems.class);
        f.edit(nuevoIems);
        f.flush();
        Messages.addGlobalInfo("El registro se ha actualizado correctamente");
        return nuevoIems;
    }

}
