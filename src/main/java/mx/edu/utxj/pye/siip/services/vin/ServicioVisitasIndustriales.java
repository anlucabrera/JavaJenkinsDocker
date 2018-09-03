/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.VisitasIndustriales;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOVisitas;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaVisitasIndustriales;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbVisitasIndustriales;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
import org.apache.poi.ss.usermodel.DateUtil;
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
public class ServicioVisitasIndustriales implements EjbVisitasIndustriales{
    @EJB
    Facade facdepye;
    @EJB
    EjbModulos ejbModulos;
    @EJB
    EjbOrganismosVinculados ejbOrganismosVinculados;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    @Override
    public ListaVisitasIndustriales getListaVisitasIndustriales(String rutaArchivo) throws Throwable {
       
        ListaVisitasIndustriales listaVisitasIndustriales = new ListaVisitasIndustriales();

        List<DTOVisitas> listaDtoVisitas = new ArrayList<>();
        AreasUniversidad areaUniversidad;
        OrganismosVinculados organismosVinculados;
        VisitasIndustriales visitasIndustriales;
        DTOVisitas dTOVisitas;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;

       Character grup = null;
       
       if (primeraHoja.getSheetName().equals("Visitas Industriales")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

            if ((!"".equals(fila.getCell(0).getStringCellValue()))) {
                areaUniversidad = new AreasUniversidad();
                organismosVinculados = new OrganismosVinculados();
                visitasIndustriales = new VisitasIndustriales();
                dTOVisitas = new DTOVisitas();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        dTOVisitas.setCicloEscolar(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(1).getCellTypeEnum()) {
                    case FORMULA: 
                        visitasIndustriales.setCicloEscolar((int)fila.getCell(1).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case STRING:
                        dTOVisitas.setPeriodoEscolar(fila.getCell(2).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case FORMULA: 
                        visitasIndustriales.setPeriodoEscolar((int)fila.getCell(3).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(4))) {
                         visitasIndustriales.setFecha(fila.getCell(4).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case STRING:
                        areaUniversidad.setNombre(fila.getCell(5).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case FORMULA:
                        areaUniversidad.setArea((short) fila.getCell(6).getNumericCellValue());
                        visitasIndustriales.setProgramaEducativo(areaUniversidad.getArea());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                    case NUMERIC:
                        visitasIndustriales.setCuatrimestre((int) fila.getCell(7).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(8).getCellTypeEnum()) {
                    case STRING:
                         String g1 = fila.getCell(8).getStringCellValue();
                         grup = g1.charAt(0);
                         visitasIndustriales.setGrupo(grup);
                         break;
                    default:
                        break;
                }
                switch (fila.getCell(9).getCellTypeEnum()) {
                    case STRING:
                        visitasIndustriales.setSistema(fila.getCell(9).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(10).getCellTypeEnum()) {
                    case STRING:
                        organismosVinculados.setNombre(fila.getCell(10).getStringCellValue());
                    default:
                        break;
                }
                switch (fila.getCell(11).getCellTypeEnum()) {
                    case FORMULA:
                        organismosVinculados.setEmpresa((int) fila.getCell(11).getNumericCellValue());
                        visitasIndustriales.setEmpresa(organismosVinculados);
                    default:
                        break;
                }
                switch (fila.getCell(12).getCellTypeEnum()) {
                    case STRING:
                         visitasIndustriales.setObjetivo(fila.getCell(12).getStringCellValue());
                    default:
                        break;
                }
                switch (fila.getCell(13).getCellTypeEnum()) {
                    case NUMERIC:
                        visitasIndustriales.setHombres((int) fila.getCell(13).getNumericCellValue());
                        break;
                    default:
                        break;
                }

                switch (fila.getCell(14).getCellTypeEnum()) {
                    case NUMERIC:
                         visitasIndustriales.setMujeres((int) fila.getCell(14).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                
               dTOVisitas.setAreasUniversidad(areaUniversidad);
               dTOVisitas.setVisitasIndustriales(visitasIndustriales);
                    
               listaDtoVisitas.add(dTOVisitas);
            }
            }
            listaVisitasIndustriales.setVisitas(listaDtoVisitas);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaVisitasIndustriales;
        
    }

    @Override
    public void guardaVisitasIndustriales(ListaVisitasIndustriales listaVisitasIndustriales, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//            listaVisitasIndustriales.getVisitas().forEach((visitas) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//           
//            visitas.getVisitasIndustriales().getEmpresa().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(visitas.getVisitasIndustriales().getEmpresa().getEmpresa()));
//            
//            facdepye.setEntityClass(VisitasIndustriales.class);
//            visitas.getVisitasIndustriales().setRegistro(registro.getRegistro());
//            facdepye.create(visitas.getVisitasIndustriales());
//            facdepye.flush();
//        });
            List<String> listaCondicional = new ArrayList<>();
            listaVisitasIndustriales.getVisitas().forEach((visitas) -> {
            facdepye.setEntityClass(VisitasIndustriales.class);
            VisitasIndustriales visIndEncontrada = getRegistroVisitasIndustriales(visitas.getVisitasIndustriales());
            Boolean registroAlmacenado = false;

            if (visIndEncontrada != null) {
                listaCondicional.add(visitas.getPeriodoEscolar()+" - "+ visitas.getVisitasIndustriales().getFecha());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                visitas.getVisitasIndustriales().setRegistro(visIndEncontrada.getRegistro());
                visitas.getVisitasIndustriales().getEmpresa().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(visitas.getVisitasIndustriales().getEmpresa().getEmpresa()));
                facdepye.edit(visitas.getVisitasIndustriales());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                visitas.getVisitasIndustriales().getEmpresa().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(visitas.getVisitasIndustriales().getEmpresa().getEmpresa()));
                visitas.getVisitasIndustriales().setRegistro(registro.getRegistro());
                facdepye.create(visitas.getVisitasIndustriales());
            }
            facdepye.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public VisitasIndustriales getRegistroVisitasIndustriales(VisitasIndustriales visitasIndustriales) {
        TypedQuery<VisitasIndustriales> query = em.createQuery("SELECT v FROM VisitasIndustriales v JOIN v.empresa e WHERE v.cicloEscolar = :cicloEscolar AND v.periodoEscolar = :periodoEscolar AND v.fecha = :fecha AND v.programaEducativo = :programaEducativo AND v.cuatrimestre = :cuatrimestre AND v.grupo = :grupo AND e.empresa = :empresa", VisitasIndustriales.class);
        query.setParameter("cicloEscolar", visitasIndustriales.getCicloEscolar());
        query.setParameter("periodoEscolar", visitasIndustriales.getPeriodoEscolar());
        query.setParameter("fecha", visitasIndustriales.getFecha());
        query.setParameter("programaEducativo", visitasIndustriales.getProgramaEducativo());
        query.setParameter("cuatrimestre", visitasIndustriales.getCuatrimestre());
        query.setParameter("grupo", visitasIndustriales.getGrupo());
        query.setParameter("empresa", visitasIndustriales.getEmpresa().getEmpresa());
        try {
            visitasIndustriales = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            visitasIndustriales = null;
            System.out.println(ex.toString());
        }
        return visitasIndustriales;
    }
}
