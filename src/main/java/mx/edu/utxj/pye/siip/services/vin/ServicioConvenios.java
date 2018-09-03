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
import mx.edu.utxj.pye.sgi.entity.pye2.Convenios;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaConvenios;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbConvenios;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioConvenios implements EjbConvenios {

    @EJB
    Facade facadeVinculacion;
    @EJB
    EjbModulos ejbModulos;
    @EJB
    EjbOrganismosVinculados ejbOrganismosVinculados;
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @Override
    public ListaConvenios getListaConvenios(String rutaArchivo) throws Throwable {
        ListaConvenios listaConvenios = new ListaConvenios();

        List<Convenios> convenios = new ArrayList<>();
        Convenios convenio;
        OrganismosVinculados organismoVinculado;

        File excelConvenios = new File(rutaArchivo);
        XSSFWorkbook workBookConvenios = new XSSFWorkbook();
        workBookConvenios = (XSSFWorkbook) WorkbookFactory.create(excelConvenios);
        XSSFSheet primeraHoja = workBookConvenios.getSheetAt(0);
        XSSFRow fila;

        if (primeraHoja.getSheetName().equals("Convenios")) {
            for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                if ((fila.getCell(1).getDateCellValue() != null)) {
                    convenio = new Convenios();
                    organismoVinculado = new OrganismosVinculados();
                    switch (fila.getCell(1).getCellTypeEnum()) {
                        case FORMULA:
                            organismoVinculado.setNombre(fila.getCell(0).getStringCellValue());
                            organismoVinculado.setEmpresa((int) fila.getCell(1).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(2).getCellTypeEnum()) {
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(fila.getCell(2))) {
                                convenio.setFechaFirma(fila.getCell(2).getDateCellValue());
                            }
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(3).getCellTypeEnum()) {
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(fila.getCell(3))) {
                                convenio.setVigencia(fila.getCell(3).getDateCellValue());
                            }
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(4).getCellTypeEnum()) {
                        case STRING:
                            convenio.setObjetivo(fila.getCell(4).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(5).getCellTypeEnum()) {
                        case STRING:
                            convenio.setDescripcion(fila.getCell(5).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(6).getCellTypeEnum()) {
                        case STRING:
                            convenio.setImpacto(fila.getCell(6).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(7).getCellTypeEnum()) {
                        case NUMERIC:
                            convenio.setEbh((short) fila.getCell(7).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(8).getCellTypeEnum()) {
                        case NUMERIC:
                            convenio.setEbm((short) fila.getCell(8).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(9).getCellTypeEnum()) {
                        case NUMERIC:
                            convenio.setDbh((short) fila.getCell(9).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(10).getCellTypeEnum()) {
                        case NUMERIC:
                            convenio.setDbm((short) fila.getCell(10).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(12).getCellTypeEnum()) {
                        case FORMULA:
                            convenio.setRecursosObtenidos(fila.getCell(12).getNumericCellValue());
                            break;
                        default:
                            break;
                    }

                    switch (fila.getCell(13).getCellTypeEnum()) {
                        case NUMERIC:
                            String cadena = Integer.toString((int) fila.getCell(13).getNumericCellValue());
                            Character aach = cadena.charAt(0);
                            convenio.setAach(aach);
                            break;
                        case BLANK:
                            convenio.setAach('0');
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(14).getCellTypeEnum()) {
                        case NUMERIC:
                            String cadena = Integer.toString((int) fila.getCell(14).getNumericCellValue());
                            Character aarh = cadena.charAt(0);
                            convenio.setAarh(aarh);
                            break;
                        case BLANK:
                            convenio.setAarh('0');
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(15).getCellTypeEnum()) {
                        case NUMERIC:
                            String cadena = Integer.toString((int) fila.getCell(15).getNumericCellValue());
                            Character idie = cadena.charAt(0);
                            convenio.setIdie(idie);
                            break;
                        case BLANK:
                            convenio.setIdie('0');
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(16).getCellTypeEnum()) {
                        case NUMERIC:
                            String cadena = Integer.toString((int) fila.getCell(16).getNumericCellValue());
                            Character pa = cadena.charAt(0);
                            convenio.setPa(pa);
                            break;
                        case BLANK:
                            convenio.setPa('0');
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(17).getCellTypeEnum()) {
                        case NUMERIC:
                            String cadena = Integer.toString((int) fila.getCell(17).getNumericCellValue());
                            Character qab = cadena.charAt(0);
                            convenio.setQab(qab);
                            break;
                        case BLANK:
                            convenio.setQab('0');
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(18).getCellTypeEnum()) {
                        case NUMERIC:
                            String cadena = Integer.toString((int) fila.getCell(18).getNumericCellValue());
                            Character gas = cadena.charAt(0);
                            convenio.setGas(gas);
                            break;
                        case BLANK:
                            convenio.setGas('0');
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(19).getCellTypeEnum()) {
                        case NUMERIC:
                            String cadena = Integer.toString((int) fila.getCell(19).getNumericCellValue());
                            Character asp = cadena.charAt(0);
                            convenio.setAsp(asp);
                            break;
                        case BLANK:
                            convenio.setAsp('0');
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(20).getCellTypeEnum()) {
                        case NUMERIC:
                            String cadena = Integer.toString((int) fila.getCell(20).getNumericCellValue());
                            Character ipa = cadena.charAt(0);
                            convenio.setIpa(ipa);
                            break;
                        case BLANK:
                            convenio.setIpa('0');
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(21).getCellTypeEnum()) {
                        case NUMERIC:
                            String cadena = Integer.toString((int) fila.getCell(21).getNumericCellValue());
                            Character ibio = cadena.charAt(0);
                            convenio.setIbio(ibio);
                            break;
                        case BLANK:
                            convenio.setIbio('0');
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(22).getCellTypeEnum()) {
                        case NUMERIC:
                            String cadena = Integer.toString((int) fila.getCell(22).getNumericCellValue());
                            Character mai = cadena.charAt(0);
                            convenio.setMai(mai);
                            break;
                        case BLANK:
                            convenio.setMai('0');
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(23).getCellTypeEnum()) {
                        case NUMERIC:
                            String cadena = Integer.toString((int) fila.getCell(23).getNumericCellValue());
                            Character miap = cadena.charAt(0);
                            convenio.setMiap(miap);
                            break;
                        case BLANK:
                            convenio.setMiap('0');
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(24).getCellTypeEnum()) {
                        case NUMERIC:
                            String cadena = Integer.toString((int) fila.getCell(24).getNumericCellValue());
                            Character imi = cadena.charAt(0);
                            convenio.setImi(imi);
                            break;
                        case BLANK:
                            convenio.setImi('0');
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(25).getCellTypeEnum()) {
                        case NUMERIC:
                            String cadena = Integer.toString((int) fila.getCell(25).getNumericCellValue());
                            Character mecaa = cadena.charAt(0);
                            convenio.setMecaa(mecaa);
                            break;
                        case BLANK:
                            convenio.setMecaa('0');
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(26).getCellTypeEnum()) {
                        case NUMERIC:
                            String cadena = Integer.toString((int) fila.getCell(26).getNumericCellValue());
                            Character imeca = cadena.charAt(0);
                            convenio.setImeca(imeca);
                            break;
                        case BLANK:
                            convenio.setImeca('0');
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(27).getCellTypeEnum()) {
                        case NUMERIC:
                            String cadena = Integer.toString((int) fila.getCell(27).getNumericCellValue());
                            Character ticasi = cadena.charAt(0);
                            convenio.setTicasi(ticasi);
                            break;
                        case BLANK:
                            convenio.setTicasi('0');
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(28).getCellTypeEnum()) {
                        case NUMERIC:
                            String cadena = Integer.toString((int) fila.getCell(28).getNumericCellValue());
                            Character ticamc = cadena.charAt(0);
                            convenio.setTicamc(ticamc);
                            break;
                        case BLANK:
                            convenio.setTicamc('0');
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(29).getCellTypeEnum()) {
                        case NUMERIC:
                            String cadena = Integer.toString((int) fila.getCell(29).getNumericCellValue());
                            Character itic = cadena.charAt(0);
                            convenio.setItic(itic);
                            break;
                        case BLANK:
                            convenio.setItic('0');
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(30).getCellTypeEnum()) {
                        case NUMERIC:
                            String cadena = Integer.toString((int) fila.getCell(30).getNumericCellValue());
                            Character tfar = cadena.charAt(0);
                            convenio.setTfar(tfar);
                            break;
                        case BLANK:
                            convenio.setTfar('0');
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(31).getCellTypeEnum()) {
                        case NUMERIC:
                            String cadena = Integer.toString((int) fila.getCell(31).getNumericCellValue());
                            Character ltefi = cadena.charAt(0);
                            convenio.setLtefi(ltefi);

                            break;
                        case BLANK:
                            convenio.setLtefi('0');
                            break;
                        default:
                            break;
                    }
                    convenio.setEmpresa(organismoVinculado);

                    convenios.add(convenio);
                }
            }
            listaConvenios.setConvenios(convenios);
            workBookConvenios.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            workBookConvenios.close();
            excelConvenios.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaConvenios;
    }

    @Override
    public void guardaConvenios(ListaConvenios listaConvenios, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaConvenios.getConvenios().forEach((convenios) -> {
            facadeVinculacion.setEntityClass(Convenios.class);
            Convenios convenioEncontrado = getConvenio(convenios);
            Boolean registroAlmacenado = false;
            if (convenioEncontrado != null) {
                listaCondicional.add(convenios.getEmpresa().getEmpresa() + " " + convenios.getFechaFirma());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                convenios.setRegistro(convenioEncontrado.getRegistro());
                convenios.getEmpresa().setRegistro(convenioEncontrado.getEmpresa().getRegistro());
                facadeVinculacion.edit(convenios);
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                convenios.getEmpresa().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(convenios.getEmpresa().getEmpresa()));
                convenios.setRegistro(registro.getRegistro());
                facadeVinculacion.create(convenios);
            }
            facadeVinculacion.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public Convenios getConvenio(Convenios convenio) {
        TypedQuery<Convenios> query = em.createQuery("SELECT c FROM Convenios c JOIN c.empresa e WHERE e.empresa = :empresa AND c.fechaFirma = :fechaFirma", Convenios.class);
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
}
