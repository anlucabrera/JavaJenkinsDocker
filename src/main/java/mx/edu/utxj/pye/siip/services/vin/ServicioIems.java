/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
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

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioIems implements EjbIems {

    @EJB
    Facade f;
    
    @EJB
    EjbModulos ejbModulos;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @Override
    public List<Iems> getIems() {
        System.err.println("entra al ejb");
        TypedQuery<Iems> q = f.getEntityManager().createQuery("SELECT i FROM Iems i ORDER BY i.iems DESC", Iems.class);
        q.setMaxResults(10);
        System.err.println("la lista de iem es : ");
        q.getResultList().forEach(System.err::println);
        return q.getResultList();
    }

    @Override
    public List<Iems> getIemsByFechas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iems eliminaIems(Integer iem) {
        System.err.println("Entra a la iem : " + iem);
        if (iem == null) {
            System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioIems.eliminaIems() valor null de la iem");
            return null;
        } else {
            System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioIems.eliminaIems() la iem que se elimina : " + iem);
            f.setEntityClass(Iems.class);
            Iems i = f.getEntityManager().find(Iems.class, iem);
            System.err.println("encuentra la iem : " + i);
            f.remove(i);
            return i;
        }
    }
    
    @Override
    public List<Iems> filtroIems(Integer estado, Integer municipio){
        System.err.println("el estado seleccionado es : " + estado+ " y el municipio es : " + municipio);
        TypedQuery<Iems> q = f.getEntityManager()
                .createQuery("SELECT i from Iems i WHERE i.localidad.localidadPK.claveEstado = :estado AND i.localidad.municipio.municipioPK.claveMunicipio =:municipio ORDER BY i.iems DESC", Iems.class);
        q.setParameter("estado", estado);
        q.setParameter("municipio", municipio);
        
        List<Iems> li = q.getResultList();
        if(li.isEmpty() || li == null){
            System.err.println("no se encontraron registro de este municipio : " + municipio);
            return null;
        }else{
            System.err.println("El tamaño de la lista es : " + li.size());
            return li;
        }
    }

    @Override
    public ListaIemsPrevia getListaIemsPrevia (String rutaArchivo) throws Throwable {

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
                        default:
                            break;
                    }
                    switch (fila.getCell(1).getCellTypeEnum()) {
                        case STRING:
                            iems.setClave(fila.getCell(1).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(3).getCellTypeEnum()) {
                        case FORMULA:
                            iems.setTurno(fila.getCell(3).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(5).getCellTypeEnum()) {
                        case FORMULA:
                            iems.setTipo(fila.getCell(5).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(7).getCellTypeEnum()) {
                        case FORMULA:
                            iems.setAmbito(fila.getCell(7).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(8).getCellTypeEnum()) {
                        case STRING:
                            iemsServedu.setDescripcion(fila.getCell(8).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(9).getCellTypeEnum()) {
                        case FORMULA:
                            iemsServedu.setServeducativo((int) fila.getCell(9).getNumericCellValue());
                            iems.setServedu(iemsServedu);
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(10).getCellTypeEnum()) {
                        case STRING:
                            estado.setNombre(fila.getCell(10).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(11).getCellTypeEnum()) {
                        case FORMULA:
                            estado.setIdestado((int)fila.getCell(11).getNumericCellValue());
                            localidadPK.setClaveEstado((int)fila.getCell(11).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(15).getCellTypeEnum()) {
                        case STRING:
                            municipio.setNombre(fila.getCell(15).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(16).getCellTypeEnum()) {
                        case FORMULA:
                            municipioPK.setClaveMunicipio((int)fila.getCell(16).getNumericCellValue());
                            municipio.setMunicipioPK(municipioPK);
                            municipio.setEstado(estado);
                            localidadPK.setClaveMunicipio((int) fila.getCell(16).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(20).getCellTypeEnum()) {
                        case STRING:
                            localidad.setNombre(fila.getCell(20).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(21).getCellTypeEnum()) {
                        case FORMULA:
                            localidadPK.setClaveLocalidad((int) fila.getCell(21).getNumericCellValue());
                            localidad.setLocalidadPK(localidadPK);
                            localidad.setMunicipio(municipio);
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(26).getCellTypeEnum()) {
                        case FORMULA:
                            iems.setDomicilio(fila.getCell(26).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(27).getCellTypeEnum()) {
                        case NUMERIC:
                            int l = (int) fila.getCell(27).getNumericCellValue();
                            lada = Integer.toString(l);
                            iems.setLada(lada);
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(28).getCellTypeEnum()) {
                        case NUMERIC:
                            int t = (int) fila.getCell(28).getNumericCellValue();
                            tel = Integer.toString(t);
                            iems.setTelefono(tel);
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(32).getCellTypeEnum()) {
                        case FORMULA:
                            iems.setResponsable(fila.getCell(32).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(33).getCellTypeEnum()) {
                        case STRING:
                            iems.setCorreo(fila.getCell(33).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    iems.setLocalidad(localidad);
                    dTOIems.setIems(iems);
                    listaDtoIems.add(dTOIems);
                }
            }
            listaIemsPrevia.setDTOIems(listaDtoIems);
            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaIemsPrevia;
    }

//    @Override
//    public Iems getIemsPorClave(Integer clave) {
//        facadeProntuario.setEntityClass(Iems.class);
//        Iems iem = facadeProntuario.getEntityManager().find(Iems.class, clave);
//        if (iem == null) {
//            return null;
//        } else {
//            return iem;
//        }
//    }
//
//    @Override
//    public Iems editaIEMS(Iems iems, Integer estado, Integer municipio, Integer localidad) {
//        facadeProntuario.setEntityClass(Iems.class);
//        Iems i = facadeProntuario.getEntityManager().find(Iems.class, iems.getIems());
//        i.setTurno(iems.getTurno());
//        i.setAmbito(iems.getAmbito());
//        i.setServedu(iems.getServedu());
//        if (estado == null || municipio == null || localidad == null) {
//            i.setLocalidad(iems.getLocalidad());
//        } else {
//            Localidad l = facadeProntuario.getEntityManager().find(Localidad.class , new Localidad(estado, municipio, localidad));
//            i.setLocalidad(l);
//        }
//        i.setDomicilio(iems.getDomicilio());
//        i.setLada(iems.getLada());
//        i.setResponsable(iems.getResponsable());
//        i.setCorreo(iems.getCorreo());
//        facadeProntuario.edit(i);
//        facadeProntuario.getEntityManager().flush();
//        return i;
//    }

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
            } else {
                System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioIems.guardaIems()" + dTOIems.getIems());
                f.create(dTOIems.getIems());
            }
            f.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public Iems getRegistroIems(Iems iems) {
        TypedQuery<Iems> query = em.createQuery("SELECT i FROM Iems i WHERE i.clave =:clave", Iems.class);
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
        List<Iems> iemsLst = new ArrayList<>();
        TypedQuery<Iems> query = em.createQuery("SELECT i FROM Iems i", Iems.class);
        try {
            iemsLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            iemsLst = null;
//            System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioOrganismosVinculados.getOrganismosVinculadoVigentes()" + ex.toString());
        }
        return iemsLst;
    }

}
