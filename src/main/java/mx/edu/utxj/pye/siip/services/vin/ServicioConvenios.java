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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.pye2.Convenios;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbConvenios;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
import org.apache.poi.ss.usermodel.CellType;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioConvenios implements EjbConvenios {

    @EJB    Facade                  facadeVinculacion;
    @EJB    EjbModulos              ejbModulos;
    @EJB    EjbOrganismosVinculados ejbOrganismosVinculados;
    @EJB    EjbFiscalizacion        ejbFiscalizacion;        
    @EJB    Facade                  f;
    @Inject LogonMB                 logonMB;

    private static final Logger LOG = Logger.getLogger(ServicioConvenios.class.getName());
    
    @Override
    public List<Convenios> getListaConvenios(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();

            List<Convenios> convenios = new ArrayList<>();
            Convenios convenio;
            OrganismosVinculados organismoVinculado;

            File excelConvenios = new File(rutaArchivo);
            XSSFWorkbook workBookConvenios = new XSSFWorkbook();
            workBookConvenios = (XSSFWorkbook) WorkbookFactory.create(excelConvenios);
            XSSFSheet primeraHoja = workBookConvenios.getSheetAt(0);
            XSSFRow fila;

            try{
            if (primeraHoja.getSheetName().equals("Convenios")) {
                for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                    fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                    if (fila.getCell(2).getDateCellValue() != null) {
                        convenio = new Convenios();
                        organismoVinculado = new OrganismosVinculados();

                        if (fila.getCell(1).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(1).getCellTypeEnum()) {
                                case FORMULA:
                                    organismoVinculado.setEmpresa((int) fila.getCell(1).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Empresa en la columna: " + (1 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(2).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(2).getCellTypeEnum()) {
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(fila.getCell(2))) {
                                        convenio.setFechaFirma(fila.getCell(2).getDateCellValue());
                                    }
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Fecha de firma en la columna: " + (2 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(3).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(3).getCellTypeEnum()) {
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(fila.getCell(3))) {
                                        convenio.setVigencia(fila.getCell(3).getDateCellValue());
                                    }
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Fecha de vigencia en la columna: " + (3 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(4).getCellTypeEnum() == CellType.STRING) {
                            switch (fila.getCell(4).getCellTypeEnum()) {
                                case STRING:
                                    convenio.setObjetivo(fila.getCell(4).getStringCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Objetivo en la columna: " + (4 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(5).getCellTypeEnum() == CellType.STRING) {
                            switch (fila.getCell(5).getCellTypeEnum()) {
                                case STRING:
                                    convenio.setDescripcion(fila.getCell(5).getStringCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Descripción en la columna: " + (5 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(6).getCellTypeEnum() == CellType.STRING) {
                            switch (fila.getCell(6).getCellTypeEnum()) {
                                case STRING:
                                    convenio.setImpacto(fila.getCell(6).getStringCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Impacto en la columna: " + (6 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(7).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(7).getCellTypeEnum()) {
                                case NUMERIC:
                                    convenio.setEbh((short) fila.getCell(7).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Estudiantes Beneficiados Hombres en la columna: " + (7 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(8).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(8).getCellTypeEnum()) {
                                case NUMERIC:
                                    convenio.setEbm((short) fila.getCell(8).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Estudiantes Beneficiados Mujeres en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(9).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(9).getCellTypeEnum()) {
                                case NUMERIC:
                                    convenio.setDbh((short) fila.getCell(9).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Docentes Beneficiados Hombres en la columna: " + (9 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(10).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(10).getCellTypeEnum()) {
                                case NUMERIC:
                                    convenio.setDbm((short) fila.getCell(10).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Docentes Beneficiados Mujeres en la columna: " + (10 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(12).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(12).getCellTypeEnum()) {
                                case FORMULA:
                                    convenio.setRecursosObtenidos(fila.getCell(12).getNumericCellValue());
                                    organismoVinculado.setNombre(fila.getCell(13).getStringCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Recursos Obtenidos en la columna: " + (13 + 1) + " y fila: " + (i + 1));
                        }

                        if (!organismoVinculado.getNombre().isEmpty()) {
                            convenio.setEmpresa(organismoVinculado);
                            convenios.add(convenio);
                        }

                    }
                }
                workBookConvenios.close();

                if (validarCelda.contains(false)) {
                    Messages.addGlobalWarn("<b>El archivo cargado contiene datos que no son validos, verifique los datos de la plantilla</b>");
                    Messages.addGlobalWarn(datosInvalidos.toString());

                    excelConvenios.delete();
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                    return Collections.EMPTY_LIST;
                } else {
                    Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
                    return convenios;
                }

            } else {
                workBookConvenios.close();
                excelConvenios.delete();
                ServicioArchivos.eliminarArchivo(rutaArchivo);
                Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
                return Collections.EMPTY_LIST;
            }
            } catch (IOException e) {
                workBookConvenios.close();
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
    public void guardaConvenios(List<Convenios> listaConvenios, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaConvenios.forEach((convenios) -> {
            facadeVinculacion.setEntityClass(Convenios.class);
            Convenios convenioEncontrado = getConvenio(convenios);
            Boolean registroAlmacenado = false;
            if (convenioEncontrado != null) {
                listaCondicional.add(convenios.getEmpresa().getEmpresa() + " " + convenios.getFechaFirma());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                if(ejbModulos.getEventoRegistro().getEventoRegistro().equals(convenioEncontrado.getRegistros().getEventoRegistro().getEventoRegistro())){
                    convenios.setRegistro(convenioEncontrado.getRegistro());
                    convenios.getEmpresa().setRegistro(convenioEncontrado.getEmpresa().getRegistro());
                    facadeVinculacion.edit(convenios);
                }else{
                    listaCondicional.remove(convenios.getEmpresa().getEmpresa() + " " + convenios.getFechaFirma());
                }
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                convenios.getEmpresa().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(convenios.getEmpresa().getEmpresa()));
                convenios.setRegistro(registro.getRegistro());
                facadeVinculacion.create(convenios);
            }
            facadeVinculacion.flush();
        });
        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public Convenios getConvenio(Convenios convenio) {
        TypedQuery<Convenios> query = facadeVinculacion.getEntityManager().createQuery("SELECT c FROM Convenios c JOIN c.empresa e WHERE e.empresa = :empresa AND c.fechaFirma = :fechaFirma", Convenios.class);
        query.setParameter("empresa", convenio.getEmpresa().getEmpresa());
        query.setParameter("fechaFirma", convenio.getFechaFirma());
        try {
            convenio = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            convenio = null;
            ex.toString();
        }
        return convenio;
    }
    
    @Override
    public List<Convenios> getFiltroConveniosEjercicioMesArea(Short ejercicio, String mes, Short area) {
        try {
            List<Convenios> convenios = facadeVinculacion.getEntityManager().createQuery("SELECT s FROM Convenios s JOIN s.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND e.mes = :mes AND r.area = :area AND s.empresa.estatus = :estatus", Convenios.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("mes", mes)
                    .setParameter("area", area)
                    .setParameter("estatus", true)
                    .getResultList();
            
            convenios.forEach((c) -> {
                facadeVinculacion.getEntityManager().refresh(c);
                facadeVinculacion.getEntityManager().refresh(c.getEmpresa());
            });
            
            return convenios;
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }
    
    @Override
    public Boolean verificaConvenio(Integer empresa) {
        try {
            List<Convenios> convenios = facadeVinculacion.getEntityManager().createQuery("SELECT c FROM Convenios c INNER JOIN c.empresa e WHERE e.empresa = :empresa", Convenios.class)
                    .setParameter("empresa", empresa)
                    .getResultList();
            if(convenios.isEmpty()){
                return false;
            }else{
                return true;
            }
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Convenios editaConvenio(Convenios convenio) {
        try {
            facadeVinculacion.setEntityClass(Convenios.class);
            facadeVinculacion.edit(convenio);
            facadeVinculacion.flush();
            return convenio;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudo actualizar el registro: " + convenio.getDescripcion(), e);
            return null;
        }
    }

    @Override
    public Boolean buscaConvenioExistente(Convenios convenio) {
        try {
            Convenios cnv = new Convenios();
            cnv = facadeVinculacion.getEntityManager().createQuery("SELECT c FROM Convenios c JOIN c.empresa e WHERE e.empresa = :empresa AND c.fechaFirma = :fechaFirma AND c.registro <> :registro", Convenios.class)
                    .setParameter("empresa", convenio.getEmpresa().getEmpresa())
                    .setParameter("fechaFirma", convenio.getFechaFirma())
                    .setParameter("registro", convenio.getRegistro())
                    .getSingleResult();
            if(cnv != null){
                return true;
            }else{
                return false;
            }
        } catch (NoResultException | NonUniqueResultException e) {
            return false;
        }
    }

    @Override
    public List<Convenios> getReporteGeneralConvenios(Short ejercicioFiscal) {
        try {
            return f.getEntityManager().createQuery("SELECT c FROM Convenios c INNER JOIN c.registros r WHERE r.eventoRegistro.ejercicioFiscal.anio = :ejercicioFiscal ORDER BY c.registros.eventoRegistro.mes",Convenios.class)
                    .setParameter("ejercicioFiscal", ejercicioFiscal)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

}
