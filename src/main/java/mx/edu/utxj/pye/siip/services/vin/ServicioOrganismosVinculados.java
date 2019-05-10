/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.servlet.annotation.MultipartConfig;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesVinculacion;
import mx.edu.utxj.pye.sgi.entity.pye2.ContactosEmpresa;
import mx.edu.utxj.pye.sgi.entity.pye2.CorreosEmpresa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EmpresasTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.GirosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.LocalidadPK;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.Pais;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasBeneficiadosVinculacion;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.SectoresTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.TelefonosEmpresa;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.sgi.util.StringUtils;
import mx.edu.utxj.pye.siip.dto.vin.DTOActividadesVinculacion;
import mx.edu.utxj.pye.siip.dto.vin.DTOProgramasBeneficiadosVinculacion;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
import org.apache.poi.ss.usermodel.CellType;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Stateless
@MultipartConfig
public class ServicioOrganismosVinculados implements EjbOrganismosVinculados {

    @EJB        EjbModulos  ejbModulos;
    @EJB        Facade      facadeProntuario;
    @Inject     LogonMB     logonMB;
    
    @EJB
    EjbFiscalizacion ejbFiscalizacion;
    
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<OrganismosVinculados> getListaOrganismosVinculados(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();
            //Creacion de una lista que almacenará todas los nuevos orgnanismos vinculados del archivo de excel
            List<OrganismosVinculados> organismosVinculados = new ArrayList<>();
            //Declaración del objeto que será ocupado para almanar cada registro y almacenarlo como nuevo en la lista de organismos vinculados.
            OrganismosVinculados organismoVinculado;
            OrganismosTipo organismosTipo;
            EmpresasTipo empresasTipo;
            GirosTipo girosTipo;
            SectoresTipo sectoresTipo;

            Pais pais = new Pais(42);
            LocalidadPK localidadPK = new LocalidadPK(1, 1, 1);
            Localidad localidad = new Localidad(localidadPK);

            File excelOrgVin = new File(rutaArchivo);
            XSSFWorkbook workBookOrgVin = new XSSFWorkbook();
            workBookOrgVin = (XSSFWorkbook) WorkbookFactory.create(excelOrgVin);
            XSSFSheet primeraHoja = workBookOrgVin.getSheetAt(0);
            XSSFRow fila;

            try {
                if (primeraHoja.getSheetName().equals("Organismos Vinculados")) {
                    for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                        if ((fila.getCell(0).getDateCellValue() != null)) {
                            organismoVinculado = new OrganismosVinculados();
                            organismosTipo = new OrganismosTipo();
                            empresasTipo = new EmpresasTipo();
                            girosTipo = new GirosTipo();
                            sectoresTipo = new SectoresTipo();

                            if (fila.getCell(0).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(0).getCellTypeEnum()) {
                                    case NUMERIC:
                                        if (DateUtil.isCellDateFormatted(fila.getCell(0))) {
                                            organismoVinculado.setFecha(fila.getCell(0).getDateCellValue());
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Fecha en la columna: " + (0 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(1).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(1).getCellTypeEnum()) {
                                    case STRING:
                                        organismoVinculado.setNombre(fila.getCell(1).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Nombre en la columna: " + (1 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(3).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(3).getCellTypeEnum()) {
                                    case FORMULA:
                                        organismosTipo.setOrgtipo((short) fila.getCell(3).getNumericCellValue());
                                        organismosTipo.setDescripcion(fila.getCell(2).getStringCellValue());
                                        organismoVinculado.setOrgTip(organismosTipo);
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Tipo de Organismo en la columna: " + (2 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(5).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(5).getCellTypeEnum()) {
                                    case FORMULA:
                                        empresasTipo.setEmptipo((short) fila.getCell(5).getNumericCellValue());
                                        empresasTipo.setDescripcion(fila.getCell(4).getStringCellValue());
                                        organismoVinculado.setEmpTip(empresasTipo);
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Tipo en la columna: " + (4 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(7).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(7).getCellTypeEnum()) {
                                    case FORMULA:
                                        girosTipo.setGiro((short) fila.getCell(7).getNumericCellValue());
                                        girosTipo.setDescripcion(fila.getCell(6).getStringCellValue());
                                        organismoVinculado.setGiro(girosTipo);
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Tipo de giro en la columna: " + (6 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(8).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(8).getCellTypeEnum()) {
                                    case STRING:
                                        organismoVinculado.setGiroEspecifico(fila.getCell(8).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Giro específico en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(10).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(10).getCellTypeEnum()) {
                                    case FORMULA:
                                        sectoresTipo.setSector((short) fila.getCell(10).getNumericCellValue());
                                        sectoresTipo.setDescripcion(fila.getCell(9).getStringCellValue());
                                        organismoVinculado.setSector(sectoresTipo);
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Sector en la columna: " + (9 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(11).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(11).getCellTypeEnum()) {
                                    case STRING:
                                        organismoVinculado.setDireccion(fila.getCell(11).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Dirección en la columna: " + (11 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(12).getCellTypeEnum() == CellType.STRING || fila.getCell(12).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(12).getCellTypeEnum()) {
                                    case STRING:
                                        organismoVinculado.setCp(fila.getCell(12).getStringCellValue());
                                        break;
                                    case NUMERIC:
                                        Integer empresInt = (int) fila.getCell(12).getNumericCellValue();
                                        organismoVinculado.setCp(String.valueOf(empresInt));
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Codigo Postal en la columna: " + (12 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(13).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(13).getCellTypeEnum()) {
                                    case STRING:
                                        organismoVinculado.setRepresentantePrincipal(fila.getCell(13).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Representante Principal en la columna: " + (13 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(14).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(14).getCellTypeEnum()) {
                                    case STRING:
                                        organismoVinculado.setCargoRepresentantePrincipal(fila.getCell(14).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Cargo Representante Principal en la columna: " + (14 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(16).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(16).getCellTypeEnum()) {
                                    case FORMULA:
                                        String telefono = fila.getCell(16).getStringCellValue();
                                        String telefonoConvertido = StringUtils.quitarGuionesEspacios(telefono);
                                        organismoVinculado.setTelefonoPrincipal(String.valueOf(telefonoConvertido));
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Teléfono Principal en la columna: " + (16 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(17).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(17).getCellTypeEnum()) {
                                    case STRING:
                                        organismoVinculado.setEmailPrincipal(fila.getCell(17).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Email Principal en la columna: " + (17 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(18).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(18).getCellTypeEnum()) {
                                    case STRING:
                                        organismoVinculado.setConvenio(fila.getCell(18).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Convenio en la columna: " + (18 + 1) + " y fila: " + (i + 1));
                            }

                            organismoVinculado.setPais(pais);
                            organismoVinculado.setLocalidad(localidad);

                            organismosVinculados.add(organismoVinculado);
                        }
                    }
                    workBookOrgVin.close();

                    if (validarCelda.contains(false)) {
                        Messages.addGlobalWarn("<b>El archivo cargado contiene datos que no son validos, verifique los datos de la plantilla</b>");
                        Messages.addGlobalWarn(datosInvalidos.toString());

                        excelOrgVin.delete();
                        ServicioArchivos.eliminarArchivo(rutaArchivo);
                        return Collections.EMPTY_LIST;
                    } else {
                        Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
                        return organismosVinculados;
                    }
                } else {
                    workBookOrgVin.close();
                    excelOrgVin.delete();
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                    Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
                    return Collections.EMPTY_LIST;
                }
            } catch (IOException e) {
                workBookOrgVin.close();
                ServicioArchivos.eliminarArchivo(rutaArchivo);
                Messages.addGlobalError("<b>Ocurrió un error durante la lectura del archivo, asegurese de haber registrado correctamente su información</b>");
                return Collections.EMPTY_LIST;
            }
        } else {
            Messages.addGlobalError("<b>Ocurrio un error en la lectura del archivo</b>");
            return Collections.EMPTY_LIST;
        }
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
                    organismoVinculado.setEstatus(true);
                    organismoVinculado.setPais(organismosVEncontrado.getPais());
                    organismoVinculado.setLocalidad(organismosVEncontrado.getLocalidad());
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
        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public Integer getRegistroOrganismoEspecifico(Integer empresa) {
        TypedQuery<OrganismosVinculados> query = facadeProntuario.getEntityManager().createNamedQuery("OrganismosVinculados.findByEmpresa", OrganismosVinculados.class);
        query.setParameter("empresa", empresa);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }
    
    @Override
    public OrganismosVinculados getOrganismoVinculadoPorEmpresa(Integer empresa) {
        try {
            return facadeProntuario.getEntityManager().createNamedQuery("OrganismosVinculados.findByEmpresa", OrganismosVinculados.class)
                    .setParameter("empresa", empresa)
                    .getSingleResult();
        } catch (NonUniqueResultException e) {
            return null;
        }
    }

    @Override
    public OrganismosVinculados getOrganismosVinculado(OrganismosVinculados organismoVinculado) {
        OrganismosVinculados organismoEncontrado = new OrganismosVinculados();
        try {
            return organismoEncontrado = facadeProntuario.getEntityManager().createQuery("SELECT o FROM OrganismosVinculados o WHERE o.nombre = :nombre", OrganismosVinculados.class)
                    .setParameter("nombre", organismoVinculado.getNombre())
                    .getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            return organismoEncontrado = null;
        }
    }

    @Override
    public List<OrganismosVinculados> getOrganismosVinculadoVigentes() {
        List<OrganismosVinculados> organismosVinculadosLst = new ArrayList<>();
        try {
            return organismosVinculadosLst = facadeProntuario.getEntityManager().createQuery("SELECT o FROM OrganismosVinculados o WHERE o.estatus = :estatus ORDER BY o.nombre", OrganismosVinculados.class)
                    .setParameter("estatus", true)
                    .getResultList();
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<OrganismosTipo> getOrganismosTipo() throws Throwable {
        try {
            return facadeProntuario.getEntityManager().createQuery("SELECT o FROM OrganismosTipo o ORDER BY o.descripcion ASC", OrganismosTipo.class)
                    .getResultList();
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<EmpresasTipo> getEmpresasTipos() throws Throwable {
        try {
            return facadeProntuario.getEntityManager().createQuery("SELECT e FROM EmpresasTipo e ORDER BY e.descripcion ASC", EmpresasTipo.class)
                    .getResultList();
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<GirosTipo> getGirosTipo() throws Throwable {
        try {
            return facadeProntuario.getEntityManager().createQuery("SELECT g FROM GirosTipo g ORDER BY g.descripcion ASC", GirosTipo.class)
                    .getResultList();
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<SectoresTipo> getSectoresTipo() throws Throwable {
        try {
            return facadeProntuario.getEntityManager().createQuery("SELECT s FROM SectoresTipo s ORDER BY s.descripcion ASC", SectoresTipo.class)
                    .getResultList();
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<OrganismosVinculados> getFiltroOrganismoVinculadoEjercicioMesArea(Short ejercicio, String mes, Short area){
        try {
            return facadeProntuario.getEntityManager().createQuery("SELECT o FROM OrganismosVinculados o JOIN o.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND r.area = :area AND o.estatus = :estatus", OrganismosVinculados.class)
                    .setParameter("anio", ejercicio)
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
            List<ActividadesVinculacion> listaActividadesVinculacion = facadeProntuario.getEntityManager().createQuery("SELECT a FROM ActividadesVinculacion a ORDER BY a.nombre", ActividadesVinculacion.class)
                    .getResultList();
            listaActividadesVinculacion.forEach((d) -> {
                facadeProntuario.getEntityManager().refresh(d);
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
            OrganismosVinculados ov = facadeProntuario.getEntityManager().find(OrganismosVinculados.class, empresa.getRegistro());
            facadeProntuario.getEntityManager().refresh(ov);
            
            ActividadesVinculacion actVin = facadeProntuario.getEntityManager().createQuery("SELECT a FROM ActividadesVinculacion a INNER JOIN a.organismosVinculadosList o WHERE o.empresa = :empresa AND a.actividadVinculacion = :actividadVinculacion", ActividadesVinculacion.class)
                    .setParameter("empresa", ov.getEmpresa())
                    .setParameter("actividadVinculacion", actividadVinculacion.getActividadVinculacion())
                    .getSingleResult();
            
            facadeProntuario.getEntityManager().refresh(actVin);
            
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
            OrganismosVinculados ov = facadeProntuario.getEntityManager().find(OrganismosVinculados.class, empresa.getRegistro());
//            OrganismosVinculados ov = em.createQuery("SELECT o FROM OrganismosVinculados o JOIN o.actividadesVinculacionList av JOIN av.organismosVinculadosList aov WHERE o.registro = :registro", OrganismosVinculados.class)
//                    .setParameter("registro", empresa.getRegistro())
//                    .getSingleResult();
            ActividadesVinculacion av = facadeProntuario.getEntityManager().find(ActividadesVinculacion.class, actividadVinculacion.getActividadVinculacion().getActividadVinculacion());
//            ActividadesVinculacion av = em.createQuery("SELECT a FROM ActividadesVinculacion a JOIN a.organismosVinculadosList ov JOIN ov.actividadesVinculacionList oav  WHERE a.actividadVinculacion = :actividad", ActividadesVinculacion.class)
//                    .setParameter("actividad", actividadVinculacion.getActividadVinculacion().getActividadVinculacion())
//                    .getSingleResult();

            facadeProntuario.getEntityManager().refresh(ov);
            facadeProntuario.getEntityManager().refresh(av);
            
            if (verificaActividadVinculacion(ov, av)) {
                eliminarActividadVinculacionEmpresa(empresa, actividadVinculacion);
            } else {
                ov.getActividadesVinculacionList().add(av);
                av.getOrganismosVinculadosList().add(ov);
                facadeProntuario.getEntityManager().flush();
            }
            
            facadeProntuario.getEntityManager().flush();
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudo asignar la actividad con la empresa.", e);
            return false;
        }
    }

    @Override
    public Boolean eliminarActividadVinculacionEmpresa(OrganismosVinculados empresa, DTOActividadesVinculacion actividadVinculacion) {
        try {
            OrganismosVinculados ov = facadeProntuario.getEntityManager().find(OrganismosVinculados.class, empresa.getRegistro());
            ActividadesVinculacion av = facadeProntuario.getEntityManager().find(ActividadesVinculacion.class, actividadVinculacion.getActividadVinculacion().getActividadVinculacion());
            facadeProntuario.getEntityManager().refresh(ov);
            facadeProntuario.getEntityManager().refresh(av);
            if (verificaActividadVinculacion(ov, av)) {
                ov.getActividadesVinculacionList().remove(av);
                av.getOrganismosVinculadosList().remove(ov);
                facadeProntuario.getEntityManager().flush();
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
            Integer comprueba = facadeProntuario.getEntityManager().createQuery("UPDATE OrganismosVinculados o SET o.estatus = false WHERE o.registro = :registro")
                    .setParameter("registro", organismoVinculado.getRegistro())
                    .executeUpdate();
            if(comprueba != 0){
                Messages.addGlobalInfo("<b>Se ha dado de baja el siguiente Organismo Vinculado: </b> " + organismoVinculado.getNombre());
            }else{
                Messages.addGlobalInfo("<b>No se ha podido dar de baja el siguiente Organismo Vinculado: </b> " + organismoVinculado.getNombre());
            }
//            facadeProntuario.setEntityClass(OrganismosVinculados.class);
//            organismoVinculado.setEstatus(false);
//            facadeProntuario.edit(organismoVinculado);
//            facadeProntuario.flush();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    @Override
    public List<ContactosEmpresa> consultaContactosEmpresa(OrganismosVinculados organismoVinculado) {
        try {
            return facadeProntuario.getEntityManager().createQuery("SELECT c FROM ContactosEmpresa c WHERE c.empresa.empresa = :empresa", ContactosEmpresa.class)
                    .setParameter("empresa", organismoVinculado.getEmpresa())
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Boolean guardarContactoEmpresa(ContactosEmpresa contactoEmpresa) {
        try {
            facadeProntuario.setEntityClass(ContactosEmpresa.class);
            facadeProntuario.create(contactoEmpresa);
            facadeProntuario.flush();
            Integer verifica = facadeProntuario.getEntityManager().find(ContactosEmpresa.class, contactoEmpresa.getContactoEmpresa()).getContactoEmpresa();
            if(verifica != 0){
                return true;
            }else{
                return false;
            }
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Boolean eliminarContactoEmpresa(ContactosEmpresa contactosEmpresa) {
        try {
            facadeProntuario.setEntityClass(ContactosEmpresa.class);
            facadeProntuario.remove(contactosEmpresa);
            facadeProntuario.getEntityManager().detach(contactosEmpresa);
            facadeProntuario.flush();
            contactosEmpresa = facadeProntuario.getEntityManager().find(ContactosEmpresa.class, contactosEmpresa.getContactoEmpresa());
            if (contactosEmpresa == null) {
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean editaContactoEmpresa(ContactosEmpresa contactoEmpresa) {
        try {
            facadeProntuario.setEntityClass(ContactosEmpresa.class);
            facadeProntuario.edit(contactoEmpresa);
            facadeProntuario.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<CorreosEmpresa> consultaCorreosEmpresa(OrganismosVinculados organismoVinculado) {
        try {
            return facadeProntuario.getEntityManager().createQuery("SELECT c FROM CorreosEmpresa c WHERE c.empresa.empresa = :empresa", CorreosEmpresa.class)
                    .setParameter("empresa", organismoVinculado.getEmpresa())
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Boolean guardarCorreoEmpresa(CorreosEmpresa correoEmpresa) {
        try {
            facadeProntuario.setEntityClass(CorreosEmpresa.class);
            facadeProntuario.create(correoEmpresa);
            facadeProntuario.flush();
            Integer verifica = facadeProntuario.getEntityManager().find(CorreosEmpresa.class, correoEmpresa.getCorreoEmpresa()).getCorreoEmpresa();
            if(verifica != 0){
                return true;
            }else{
                return false;
            }
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Boolean eliminarCorreoEmpresa(CorreosEmpresa correoEmpresa) {
        try {
            facadeProntuario.setEntityClass(CorreosEmpresa.class);
            facadeProntuario.remove(correoEmpresa);
            facadeProntuario.getEntityManager().detach(correoEmpresa);
            facadeProntuario.flush();
            correoEmpresa = facadeProntuario.getEntityManager().find(CorreosEmpresa.class, correoEmpresa.getCorreoEmpresa());
            if (correoEmpresa == null) {
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean editaCorreoEmpresa(CorreosEmpresa correoEmpresa) {
        try {
            facadeProntuario.setEntityClass(CorreosEmpresa.class);
            facadeProntuario.edit(correoEmpresa);
            facadeProntuario.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<TelefonosEmpresa> consultaTelefonosEmpresa(OrganismosVinculados organismoVinculado) {
        try {
            return facadeProntuario.getEntityManager().createQuery("SELECT c FROM TelefonosEmpresa c WHERE c.empresa.empresa = :empresa", TelefonosEmpresa.class)
                    .setParameter("empresa", organismoVinculado.getEmpresa())
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Boolean guardaTelefonoEmpresa(TelefonosEmpresa telefonoEmpresa) {
        try {
            facadeProntuario.setEntityClass(TelefonosEmpresa.class);
            facadeProntuario.create(telefonoEmpresa);
            facadeProntuario.flush();
            Integer verifica = facadeProntuario.getEntityManager().find(TelefonosEmpresa.class, telefonoEmpresa.getTelefonoEmpresa()).getTelefonoEmpresa();
            if(verifica != 0){
                return true;
            }else{
                return false;
            }
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Boolean eliminarTelefonoEmpresa(TelefonosEmpresa telefonoEmpresa) {
        try {
            facadeProntuario.setEntityClass(TelefonosEmpresa.class);
            facadeProntuario.remove(telefonoEmpresa);
            facadeProntuario.getEntityManager().detach(telefonoEmpresa);
            facadeProntuario.flush();
            telefonoEmpresa = facadeProntuario.getEntityManager().find(TelefonosEmpresa.class, telefonoEmpresa.getTelefonoEmpresa());
            if (telefonoEmpresa == null) {
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean editaTelefonoEmpresa(TelefonosEmpresa telefonosEmpresa) {
        try {
            facadeProntuario.setEntityClass(TelefonosEmpresa.class);
            facadeProntuario.edit(telefonosEmpresa);
            facadeProntuario.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Localidad getLocalidadOrganismoVinculado(OrganismosVinculados organismoVinculado) {
        try {
            if(organismoVinculado == null)
                return null;
            return facadeProntuario.getEntityManager().createQuery("SELECT l FROM OrganismosVinculados o JOIN o.localidad l WHERE o.registro = :registro",Localidad.class)
                    .setParameter("registro", organismoVinculado.getRegistro())
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Pais getPaisOrganismoVinculado(OrganismosVinculados organismoVinculado) {
        try {
            if(organismoVinculado == null)
                return null;
            return facadeProntuario.getEntityManager().createQuery("SELECT p FROM OrganismosVinculados o JOIN o.pais p WHERE o.registro = :registro",Pais.class)
                .setParameter("registro", organismoVinculado.getRegistro())
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Boolean guardaUbicacion(OrganismosVinculados organismoVinculado, Pais pais, Estado estado, Municipio municipio, Localidad localidad) {
        try {
//            System.out.println("OrganismoVinculado: " + organismoVinculado.getNombre());
//            System.out.println("Pais: " + pais.getIdpais() + " " + pais.getNombre());
//            System.out.println("Localidad: " + localidad.getLocalidadPK().getClaveEstado() + " " + localidad.getLocalidadPK().getClaveMunicipio() + " " + localidad.getLocalidadPK().getClaveLocalidad() + " " + localidad.getNombre());
            OrganismosVinculados orgVin = new OrganismosVinculados();
            orgVin = facadeProntuario.getEntityManager().find(OrganismosVinculados.class, organismoVinculado.getRegistro());
            orgVin.setPais(pais);
            localidad.getLocalidadPK().setClaveEstado(estado.getIdestado());
            localidad.getLocalidadPK().setClaveMunicipio(municipio.getMunicipioPK().getClaveMunicipio());
            localidad.setMunicipio(municipio);
            orgVin.setLocalidad(localidad);
            facadeProntuario.edit(orgVin);
            facadeProntuario.flush();
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudo guardar la ubicación en el organismo vinculado.", e);
            return false;
        }
    }

    @Override
    public Boolean eliminaUbicacion(OrganismosVinculados organismosVinculados) {
        try {
        facadeProntuario.setEntityClass(OrganismosVinculados.class);
        OrganismosVinculados orgVin = new OrganismosVinculados();
        orgVin = facadeProntuario.getEntityManager().find(OrganismosVinculados.class, organismosVinculados.getRegistro());
        orgVin.setPais(new Pais());
        orgVin.setLocalidad(new Localidad());
        facadeProntuario.edit(orgVin);
        facadeProntuario.flush();
        return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudo eliminar la ubicación en el organismo vinculado.", e);
            return false;
        }
    }
    
    @Override
    public List<DTOProgramasBeneficiadosVinculacion> getProgramasBeneficiadosVinculacion() throws Throwable {
        try {
            List<DTOProgramasBeneficiadosVinculacion> dtoProgBenVin = new ArrayList<>();
            List<AreasUniversidad> areasUniversidad = facadeProntuario.getEntityManager().createQuery("SELECT a FROM AreasUniversidad a JOIN a.categoria c WHERE c.categoria = :categoria AND a.vigente = :vigente ORDER BY a.nombre ASC", AreasUniversidad.class)
                    .setParameter("categoria", 9)
                    .setParameter("vigente", "1")
                    .getResultList();
            areasUniversidad.stream().forEach((a) -> {
                dtoProgBenVin.add(new DTOProgramasBeneficiadosVinculacion(
                        a
                ));
            });
            return dtoProgBenVin;
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Boolean verificaProgramaBeneficiadoVinculacion(Integer empresa, AreasUniversidad areaUniversidad) {
        try {
            ProgramasBeneficiadosVinculacion pbv = facadeProntuario.getEntityManager().createQuery("SELECT p FROM ProgramasBeneficiadosVinculacion p INNER JOIN p.organismosVinculados c WHERE c.empresa = :empresa AND p.programasBeneficiadosVinculacionPK.programaEducativo = :programaEducativo",ProgramasBeneficiadosVinculacion.class)
                    .setParameter("empresa",empresa)
                    .setParameter("programaEducativo",areaUniversidad.getArea())
                    .getSingleResult();
            if(pbv != null) return true;
            else return false;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Boolean guardarProgramaBeneficiadoVinculacion(ProgramasBeneficiadosVinculacion programaBeneficiadosVinculacion) {
//        TODO: Verificar que guarde cuando existe un registro nuevo
        try {
            if (verificaProgramaBeneficiadoVinculacion(programaBeneficiadosVinculacion.getProgramasBeneficiadosVinculacionPK().getEmpresa(), facadeProntuario.getEntityManager().find(AreasUniversidad.class, programaBeneficiadosVinculacion.getProgramasBeneficiadosVinculacionPK().getProgramaEducativo()))) {
                eliminarProgramaBeneficiadoVinculacion(programaBeneficiadosVinculacion);
            } else {
                facadeProntuario.setEntityClass(ProgramasBeneficiadosVinculacion.class);
                facadeProntuario.create(programaBeneficiadosVinculacion);
                facadeProntuario.flush();
            }
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudo asignar el programa al convenio.", e);
            return false;
        }
    }

    @Override
    public Boolean eliminarProgramaBeneficiadoVinculacion(ProgramasBeneficiadosVinculacion programaBeneficiadosVinculacion) {
        try {
            if (verificaProgramaBeneficiadoVinculacion(programaBeneficiadosVinculacion.getProgramasBeneficiadosVinculacionPK().getEmpresa(), facadeProntuario.getEntityManager().find(AreasUniversidad.class, programaBeneficiadosVinculacion.getProgramasBeneficiadosVinculacionPK().getProgramaEducativo()))) {
                facadeProntuario.setEntityClass(ProgramasBeneficiadosVinculacion.class);
                facadeProntuario.remove(programaBeneficiadosVinculacion);
                facadeProntuario.flush();
            }
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudo eliminar el programa del convenio.", e);
            return false;
        }
    }

    @Override
    public OrganismosVinculados editaOrganismoVinculado(OrganismosVinculados organismoVinculado) {
        try {
            facadeProntuario.setEntityClass(OrganismosVinculados.class);
            facadeProntuario.edit(organismoVinculado);
            facadeProntuario.flush();
            return organismoVinculado;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudo actualizar el registro: " + organismoVinculado.getNombre(), e);
            return null;
        }
    }

    @Override
    public List<OrganismosVinculados> buscaCoincidenciasOrganismosVinculados(String parametro) {
        try {
            return facadeProntuario.getEntityManager().createQuery("SELECT o FROM OrganismosVinculados o WHERE o.nombre like concat('%',:parametro,'%') AND o.estatus = :estatus ORDER BY o.nombre", OrganismosVinculados.class)
                    .setParameter("parametro", parametro)
                    .setParameter("estatus", true)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Boolean buscaOrganismoVinculadoExistente(OrganismosVinculados organismoVinculado) {
        try {
            OrganismosVinculados orgVin = new OrganismosVinculados();
            orgVin = facadeProntuario.getEntityManager().createQuery("SELECT o FROM OrganismosVinculados o WHERE o.nombre = :nombre AND o.registro <> :registro",OrganismosVinculados.class)
                .setParameter("nombre", organismoVinculado.getNombre())
                .setParameter("registro", organismoVinculado.getRegistro())
                .getSingleResult();
            if(orgVin != null){
                return true;
            }else{
                return false;
            }
        } catch (NoResultException | NonUniqueResultException ex) {
            return false;
        }
    }

}
