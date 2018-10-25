/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.annotation.MultipartConfig;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesVinculacion;
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
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOActividadesVinculacion;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Stateless
@MultipartConfig
public class ServicioOrganismosVinculados implements EjbOrganismosVinculados {

    @EJB
    Facade facadeProntuario;
    @EJB
    EjbModulos ejbModulos;
    @EJB Facade f;
    @Inject LogonMB logonMB;
    
    @EJB
    EjbFiscalizacion ejbFiscalizacion;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<OrganismosVinculados> getListaOrganismosVinculados(String rutaArchivo) throws Throwable {
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
            workBookOrgVin.close();
            Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            workBookOrgVin.close();
            excelOrgVin.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            Messages.addGlobalError("<b>El archivo cargado no corresponde al registro</b>");
        }
        return organismosVinculados;
    }

    @Override
    public void guardaOrganismosVinculados(List<OrganismosVinculados> listaOrganismosVinculados, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaOrganismosVinculados.forEach((organismoVinculado) -> {
            facadeProntuario.setEntityClass(OrganismosVinculados.class);
            OrganismosVinculados organismosVEncontrado = getOrganismosVinculado(organismoVinculado);
            Boolean registroAlmacenado = false;
            if (organismosVEncontrado != null) {
                listaCondicional.add(sdf.format(organismoVinculado.getFecha()) + " " + organismoVinculado.getNombre());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                if (organismosVEncontrado.getEstatus() == true) {
                    organismoVinculado.setRegistro(organismosVEncontrado.getRegistro());
                    organismoVinculado.setEmpresa(organismosVEncontrado.getEmpresa());
                    facadeProntuario.edit(organismoVinculado);
                }else{
                    listaCondicional.remove(sdf.format(organismoVinculado.getFecha()) + " " + organismoVinculado.getNombre());
                }
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                organismoVinculado.setRegistro(registro.getRegistro());
                organismoVinculado.setEstatus(true);
                facadeProntuario.create(organismoVinculado);
            }
            facadeProntuario.flush();
        });
        Messages.addGlobalInfo("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public Integer getRegistroOrganismoEspecifico(Integer empresa) {
        TypedQuery<OrganismosVinculados> query = em.createNamedQuery("OrganismosVinculados.findByEmpresa", OrganismosVinculados.class);
        query.setParameter("empresa", empresa);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }
    
    @Override
    public OrganismosVinculados getOrganismoVinculadoPorEmpresa(Integer empresa) {
        try {
            return em.createNamedQuery("OrganismosVinculados.findByEmpresa", OrganismosVinculados.class)
                    .setParameter("empresa", empresa)
                    .getSingleResult();
        } catch (NonUniqueResultException e) {
            return null;
        }
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
        TypedQuery<OrganismosVinculados> query = em.createQuery("SELECT o FROM OrganismosVinculados o WHERE o.estatus = :estatus ORDER BY o.nombre", OrganismosVinculados.class);
        query.setParameter("estatus", true);
        try {
            organismosVinculadosLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            organismosVinculadosLst = null;
//            System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioOrganismosVinculados.getOrganismosVinculadoVigentes()" + ex.toString());
        }
        return organismosVinculadosLst;
    }

    @Override
    public List<OrganismosTipo> getOrganismosTipo() throws Throwable {
        try {
            return em.createQuery("SELECT o FROM OrganismosTipo o ORDER BY o.descripcion ASC", OrganismosTipo.class)
                    .getResultList();
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<EmpresasTipo> getEmpresasTipos() throws Throwable {
        try {
            return em.createQuery("SELECT e FROM EmpresasTipo e ORDER BY e.descripcion ASC", EmpresasTipo.class)
                    .getResultList();
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<GirosTipo> getGirosTipo() throws Throwable {
        try {
            return em.createQuery("SELECT g FROM GirosTipo g ORDER BY g.descripcion ASC", GirosTipo.class)
                    .getResultList();
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<SectoresTipo> getSectoresTipo() throws Throwable {
        try {
            return em.createQuery("SELECT s FROM SectoresTipo s ORDER BY s.descripcion ASC", SectoresTipo.class)
                    .getResultList();
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<OrganismosVinculados> getFiltroOrganismoVinculadoEjercicioMesArea(Short ejercicio, String mes, Short area){
        try {
            return em.createQuery("SELECT o FROM OrganismosVinculados o JOIN o.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND e.mes = :mes AND r.area = :area AND o.estatus = :estatus", OrganismosVinculados.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("mes", mes)
                    .setParameter("area", area)
                    .setParameter("estatus", true)
                    .getResultList();
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }
    
    private static final Logger LOG = Logger.getLogger(ServicioOrganismosVinculados.class.getName());

    @Override
    public List<DTOActividadesVinculacion> getActividadesVinculacion() throws Throwable {
        try {
            List<DTOActividadesVinculacion> dtoActividadesVinculacion = new ArrayList<>();
            List<ActividadesVinculacion> listaActividadesVinculacion = em.createQuery("SELECT a FROM ActividadesVinculacion a ORDER BY a.nombre", ActividadesVinculacion.class)
                    .getResultList();
            listaActividadesVinculacion.forEach((d) -> {
                em.refresh(d);
                dtoActividadesVinculacion.add(new DTOActividadesVinculacion(
                        d
                ));
            });
            return dtoActividadesVinculacion;
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Boolean verificaActividadVinculacion(OrganismosVinculados empresa, ActividadesVinculacion actividadVinculacion) {
        try {
            OrganismosVinculados ov = em.find(OrganismosVinculados.class, empresa.getRegistro());
            em.refresh(ov);
            
            ActividadesVinculacion actVin = em.createQuery("SELECT a FROM ActividadesVinculacion a INNER JOIN a.organismosVinculadosList o WHERE o.empresa = :empresa AND a.actividadVinculacion = :actividadVinculacion", ActividadesVinculacion.class)
                    .setParameter("empresa", ov.getEmpresa())
                    .setParameter("actividadVinculacion", actividadVinculacion.getActividadVinculacion())
                    .getSingleResult();
            
            em.refresh(actVin);
            
            if (!actVin.getOrganismosVinculadosList().isEmpty()) {
                return true;
            } else {
                return false;
            }
        } catch (NoResultException | NonUniqueResultException ex) {
            return false;
        }
    }

    @Override
    public Boolean guardarActividadVinculacionEmpresa(OrganismosVinculados empresa, DTOActividadesVinculacion actividadVinculacion) {
//        TODO: Corregir cuando el registro es nuevo
        try {
            OrganismosVinculados ov = em.find(OrganismosVinculados.class, empresa.getRegistro());
//            OrganismosVinculados ov = em.createQuery("SELECT o FROM OrganismosVinculados o JOIN o.actividadesVinculacionList av JOIN av.organismosVinculadosList aov WHERE o.registro = :registro", OrganismosVinculados.class)
//                    .setParameter("registro", empresa.getRegistro())
//                    .getSingleResult();
            ActividadesVinculacion av = em.find(ActividadesVinculacion.class, actividadVinculacion.getActividadVinculacion().getActividadVinculacion());
//            ActividadesVinculacion av = em.createQuery("SELECT a FROM ActividadesVinculacion a JOIN a.organismosVinculadosList ov JOIN ov.actividadesVinculacionList oav  WHERE a.actividadVinculacion = :actividad", ActividadesVinculacion.class)
//                    .setParameter("actividad", actividadVinculacion.getActividadVinculacion().getActividadVinculacion())
//                    .getSingleResult();

            em.refresh(ov);
            em.refresh(av);
            
            if (verificaActividadVinculacion(ov, av)) {
                eliminarActividadVinculacionEmpresa(empresa, actividadVinculacion);
            } else {
                ov.getActividadesVinculacionList().add(av);
                av.getOrganismosVinculadosList().add(ov);
                em.flush();
            }
            
            em.flush();
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudo asignar la actividad con la empresa.", e);
            return false;
        }
    }

    @Override
    public Boolean eliminarActividadVinculacionEmpresa(OrganismosVinculados empresa, DTOActividadesVinculacion actividadVinculacion) {
        try {
            OrganismosVinculados ov = em.find(OrganismosVinculados.class, empresa.getRegistro());
            ActividadesVinculacion av = em.find(ActividadesVinculacion.class, actividadVinculacion.getActividadVinculacion().getActividadVinculacion());
            em.refresh(ov);
            em.refresh(av);
            if (verificaActividadVinculacion(ov, av)) {
                ov.getActividadesVinculacionList().remove(av);
                av.getOrganismosVinculadosList().remove(ov);
                em.flush();
            }
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudo asignar la actividad con la empresa.", e);
            return false;
        }
    }

    @Override
    public void bajaOrganismoVinculado(OrganismosVinculados organismoVinculado) {
        try {
            facadeProntuario.setEntityClass(OrganismosVinculados.class);
            organismoVinculado.setEstatus(false);
            facadeProntuario.edit(organismoVinculado);
            facadeProntuario.flush();
            Messages.addGlobalInfo("<b>Se ha dado de baja el siguiente Organismo Vinculado: </b> " + organismoVinculado.getNombre());
        } catch (Exception e) {
        }
    }

}
