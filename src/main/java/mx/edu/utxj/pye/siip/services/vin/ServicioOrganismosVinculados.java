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
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EmpresasTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.GirosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.MunicipioPK;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.Pais;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.SectoresTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
//import mx.edu.utxj.pye.sgi.facade.FacadePye;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.entity.prontuario.list.ListaOrganismosVinculados;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
//import mx.edu.utxj.pye.siip.entity.prontuario.EmpresasTipo;
//import mx.edu.utxj.pye.siip.entity.prontuario.Estado;
//import mx.edu.utxj.pye.siip.entity.prontuario.GirosTipo;
//import mx.edu.utxj.pye.siip.entity.prontuario.Municipio;
//import mx.edu.utxj.pye.siip.entity.prontuario.MunicipioPK;
//import mx.edu.utxj.pye.siip.entity.prontuario.OrganismosTipo;
//import mx.edu.utxj.pye.siip.entity.prontuario.OrganismosVinculados;
//import mx.edu.utxj.pye.siip.entity.prontuario.Pais;
//import mx.edu.utxj.pye.siip.entity.prontuario.SectoresTipo;
//import mx.edu.utxj.pye.siip.entity.prontuario.list.ListaOrganismosVinculados;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioOrganismosVinculados implements EjbOrganismosVinculados {

    @EJB
    Facade facadeProntuario;
    @EJB
    EjbModulos ejbModulos;

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

//    @Override
//    public ListaOrganismosVinculados getListaActualizadaPlantilla() throws Throwable {
//        ListaOrganismosVinculados listaOrganismosVinculados = new ListaOrganismosVinculados();
//        Query q = facadeProntuario.getEntityManager().createNativeQuery("SELECT empresa, nombre FROM prontuario.organismos_vinculados", OrganismosVinculados.class);
//        listaOrganismosVinculados.setOrganismosVinculadosLst(q.getResultList());
//        return listaOrganismosVinculados;
//    }
//
//    @Override
//    public void actualizarPlantillaConvenio(ListaOrganismosVinculados listaOrganismosVinculados) throws FileNotFoundException, IOException, ParsePropertyException, InvalidFormatException, Throwable{
//        Map<String, ListaOrganismosVinculados> beans = new HashMap<>();
//        XLSTransformer transformer = new XLSTransformer();
//        beans.put("listaOrganismosVinculados", listaOrganismosVinculados);
//        InputStream stream;
//        stream = ServicioConvenios.class.getClassLoader().getResourceAsStream("plantillas\\convenios.xlsx");
//        try (Workbook workbook = transformer.transformXLS(stream, beans)) {
//            OutputStream reportFile = new FileOutputStream("C:\\archivos\\modulos_registro\\plantillas\\vinculacion\\convenios.xlsx");
//            workbook.write(reportFile);
//        }
//    }
//    @Override
//    public ListaOrganismosVinculados getListaOrganismosVinculados(String rutaArchivo) throws Throwable {
//        
//    }
//
////    public static void main(String args[]){
////        try {
////            System.out.println("mx.edu.utxj.pye.siip.services.main() Main");
////            getListaEmpresas("C:\\archivos\\MODULOS_REGISTRO\\2018\\511\\VINCULACION\\empresas.xlsx");
////        } catch (Throwable ex) {
////            Logger.getLogger(ServicioEmpresas.class.getName()).log(Level.SEVERE, null, ex);
////        } 
////    }
//    @Override
//    public void guardaOrganismosVinculados(ListaOrganismosVinculados listaOrganismosVinculados) throws Throwable {
//    }
    @Override
    public ListaOrganismosVinculados getListaOrganismosVinculados(String rutaArchivo) throws Throwable {
//        Creación de un nuevo objeto contenedor de listas de organismos vinculados
        ListaOrganismosVinculados listaOrganismosVinculados = new ListaOrganismosVinculados();
        //Creacion de una lista que almacenará todas los nuevos orgnanismos vinculados del archivo de excel
        List<OrganismosVinculados> organismosVinculados = new ArrayList<>();
        //Declaración del objeto que será ocupado para almanar cada registro y almacenarlo como nuevo en la lista de organismos vinculados.
        OrganismosVinculados organismoVinculado;
        OrganismosTipo organismosTipo;
        EmpresasTipo empresasTipo;
        GirosTipo girosTipo;
        SectoresTipo sectoresTipo;
        Pais pais;
        Estado estado;
        Municipio municipio;
        MunicipioPK municipioPK;

        File excelOrgVin = new File(rutaArchivo);
        XSSFWorkbook workBookOrgVin = new XSSFWorkbook();
        workBookOrgVin = (XSSFWorkbook) WorkbookFactory.create(excelOrgVin);
        XSSFSheet primeraHoja = workBookOrgVin.getSheetAt(0);
        XSSFRow fila;

        if (primeraHoja.getSheetName().equals("Organismos Vinculados")) {
            for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                if ((fila.getCell(0).getDateCellValue() != null)) {
                    organismoVinculado = new OrganismosVinculados();
                    organismosTipo = new OrganismosTipo();
                    empresasTipo = new EmpresasTipo();
                    girosTipo = new GirosTipo();
                    sectoresTipo = new SectoresTipo();
                    pais = new Pais();
                    estado = new Estado();
                    municipio = new Municipio();
                    municipioPK = new MunicipioPK();
                    switch (fila.getCell(0).getCellTypeEnum()) {
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(fila.getCell(0))) {
                                organismoVinculado.setFecha(fila.getCell(0).getDateCellValue());
                            }
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(1).getCellTypeEnum()) {
                        case STRING:
                            organismoVinculado.setNombre(fila.getCell(1).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(3).getCellTypeEnum()) {
                        case FORMULA:
                            organismosTipo.setOrgtipo((short) fila.getCell(3).getNumericCellValue());
                            organismosTipo.setDescripcion(fila.getCell(4).getStringCellValue());
                            organismoVinculado.setOrgTip(organismosTipo);
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(6).getCellTypeEnum()) {
                        case FORMULA:
                            empresasTipo.setEmptipo((short) fila.getCell(6).getNumericCellValue());
                            empresasTipo.setDescripcion(fila.getCell(7).getStringCellValue());
                            organismoVinculado.setEmpTip(empresasTipo);
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(9).getCellTypeEnum()) {
                        case FORMULA:
                            girosTipo.setGiro((short) fila.getCell(9).getNumericCellValue());
                            girosTipo.setDescripcion(fila.getCell(10).getStringCellValue());
                            organismoVinculado.setGiro(girosTipo);
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(12).getCellTypeEnum()) {
                        case FORMULA:
                            sectoresTipo.setSector((short) fila.getCell(12).getNumericCellValue());
                            sectoresTipo.setDescripcion(fila.getCell(13).getStringCellValue());
                            organismoVinculado.setSector(sectoresTipo);
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(14).getCellTypeEnum()) {
                        case STRING:
                            organismoVinculado.setDireccion(fila.getCell(14).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(17).getCellTypeEnum()) {
                        case FORMULA:
                            pais.setNombre(fila.getCell(16).getStringCellValue());
                            pais.setIdpais((int) fila.getCell(17).getNumericCellValue());
                            organismoVinculado.setPais(pais);
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(22).getCellTypeEnum()) {
                        case FORMULA:
                            estado.setNombre(fila.getCell(21).getStringCellValue());
                            estado.setIdestado((int) fila.getCell(22).getNumericCellValue());
                            municipio.setEstado(estado);
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(28).getCellTypeEnum()) {
                        case FORMULA:
                            municipio.setNombre(fila.getCell(27).getStringCellValue());
                            municipioPK.setClaveEstado(estado.getIdestado());
                            municipioPK.setClaveMunicipio((int) fila.getCell(29).getNumericCellValue());
                            municipio.setMunicipioPK(municipioPK);
                            organismoVinculado.setMunicipio(municipio);
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(30).getCellTypeEnum()) {
                        case STRING:
                            organismoVinculado.setCp(fila.getCell(30).getStringCellValue());
                            break;
                        case NUMERIC:
                            Integer empresInt = (int) fila.getCell(30).getNumericCellValue();
                            organismoVinculado.setCp(String.valueOf(empresInt));
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(31).getCellTypeEnum()) {
                        case STRING:
                            organismoVinculado.setRepresentante(fila.getCell(31).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(33).getCellTypeEnum()) {
                        case FORMULA:
                            String telefono = fila.getCell(33).getStringCellValue();
                            organismoVinculado.setTelefono(String.valueOf(telefono));
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(35).getCellTypeEnum()) {
                        case FORMULA:
                            String telefonoOtro = fila.getCell(35).getStringCellValue();
                            organismoVinculado.setTelefonoOtro(String.valueOf(telefonoOtro));
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(36).getCellTypeEnum()) {
                        case STRING:
                            organismoVinculado.setEmail(fila.getCell(36).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(38).getCellTypeEnum()) {
                        case FORMULA:
                            organismoVinculado.setConvenio(fila.getCell(38).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    organismosVinculados.add(organismoVinculado);
                }
            }
            listaOrganismosVinculados.setOrganismosVinculadosLst(organismosVinculados);
            workBookOrgVin.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            workBookOrgVin.close();
            excelOrgVin.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaOrganismosVinculados;
    }

    @Override
    public void guardaOrganismosVinculados(ListaOrganismosVinculados listaOrganismosVinculados, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaOrganismosVinculados.getOrganismosVinculadosLst().forEach((organismoVinculado) -> {
            facadeProntuario.setEntityClass(OrganismosVinculados.class);
            OrganismosVinculados organismosVEncontrado = getOrganismosVinculado(organismoVinculado);
            Boolean registroAlmacenado = false;
            if (organismosVEncontrado != null) {
                listaCondicional.add(organismoVinculado.getFecha() + " " + organismoVinculado.getNombre());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                organismoVinculado.setRegistro(organismosVEncontrado.getRegistro());
                organismoVinculado.setEmpresa(organismosVEncontrado.getEmpresa());
                facadeProntuario.edit(organismoVinculado);
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                organismoVinculado.setRegistro(registro.getRegistro());
                facadeProntuario.create(organismoVinculado);
            }
            facadeProntuario.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public Integer getRegistroOrganismoEspecifico(Integer empresa) {
        TypedQuery<OrganismosVinculados> query = em.createNamedQuery("OrganismosVinculados.findByEmpresa", OrganismosVinculados.class);
        query.setParameter("empresa", empresa);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }

    @Override
    public OrganismosVinculados getOrganismosVinculado(OrganismosVinculados organismoVinculado) {
        TypedQuery<OrganismosVinculados> query = em.createQuery("SELECT o FROM OrganismosVinculados o WHERE o.fecha = :fecha AND o.nombre = :nombre", OrganismosVinculados.class);
        query.setParameter("fecha", organismoVinculado.getFecha());
        query.setParameter("nombre", organismoVinculado.getNombre());
        try {
            organismoVinculado = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            organismoVinculado = null;
            ex.toString();
        }
        return organismoVinculado;
    }

    @Override
    public List<OrganismosVinculados> getOrganismosVinculadoVigentes() {
        List<OrganismosVinculados> organismosVinculadosLst = new ArrayList<>();
        TypedQuery<OrganismosVinculados> query = em.createQuery("SELECT o FROM OrganismosVinculados o WHERE o.estatus = :estatus", OrganismosVinculados.class);
        query.setParameter("estatus", true);
        try {
            organismosVinculadosLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            organismosVinculadosLst = null;
//            System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioOrganismosVinculados.getOrganismosVinculadoVigentes()" + ex.toString());
        }
        return organismosVinculadosLst;
    }

}
