/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasMovilidad;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosMovilidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Pais;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTORegistroMovilidad;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaRegistroMovilidad;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbRegistroMovilidad;
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
public class ServicioRegistroMovilidad implements EjbRegistroMovilidad{
    @EJB
    Facade facdepye;
    @EJB
    EjbModulos ejbModulos;
    @EJB
    EjbOrganismosVinculados ejbOrganismosVinculados;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
        
    @Override
    public ListaRegistroMovilidad getListaRegistroMovilidad(String rutaArchivo) throws Throwable {
       
        ListaRegistroMovilidad listaRegistroMovilidad = new  ListaRegistroMovilidad();

        List<DTORegistroMovilidad> listaDtoMovilidad = new ArrayList<>();
        Pais pais;
        Estado estado;
        OrganismosVinculados organismosVinculados;
        AreasUniversidad areasUniversidad;
        ProgramasMovilidad programasMovilidad;
        RegistrosMovilidad registrosMovilidad;
        DTORegistroMovilidad dTORegistroMovilidad;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        if (primeraHoja.getSheetName().equals("Registro Movilidad")) {
        for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((!"".equals(fila.getCell(0).getStringCellValue()))) {
                pais = new Pais();
                estado = new Estado();
                organismosVinculados = new OrganismosVinculados();
                areasUniversidad =  new AreasUniversidad();
                programasMovilidad = new ProgramasMovilidad();
                registrosMovilidad = new  RegistrosMovilidad();
                dTORegistroMovilidad = new DTORegistroMovilidad();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case FORMULA:
                        registrosMovilidad.setRegistroMovilidad(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case STRING: 
                        programasMovilidad.setNombre(fila.getCell(2).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case FORMULA:
                        programasMovilidad.setPrograma((short)fila.getCell(3).getNumericCellValue());
                        registrosMovilidad.setProgramaMovilidad(programasMovilidad);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case STRING: 
                        registrosMovilidad.setTipoParticipante(fila.getCell(4).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case STRING: 
                        registrosMovilidad.setTipoMovilidad(fila.getCell(5).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                     case NUMERIC: 
                         if (DateUtil.isCellDateFormatted(fila.getCell(6))) {
                         registrosMovilidad.setFechaInicio(fila.getCell(6).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                     case NUMERIC: 
                         if (DateUtil.isCellDateFormatted(fila.getCell(7))) {
                         registrosMovilidad.setFechaFin(fila.getCell(7).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                 switch (fila.getCell(8).getCellTypeEnum()) {
                     case STRING: 
                         registrosMovilidad.setParticipante(fila.getCell(8).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(9).getCellTypeEnum()) {
                    case STRING:
                        areasUniversidad.setNombre(fila.getCell(9).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(10).getCellTypeEnum()) {
                    case FORMULA:
                        areasUniversidad.setArea((short)fila.getCell(10).getNumericCellValue());
                        registrosMovilidad.setProgramaEducativo(areasUniversidad.getArea());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(12).getCellTypeEnum()) {
                    case STRING:
                         dTORegistroMovilidad.setCicloEscolar(fila.getCell(12).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(14).getCellTypeEnum()) {
                    case STRING:
                         dTORegistroMovilidad.setPeriodoEscolarCursado(fila.getCell(14).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(15).getCellTypeEnum()) {
                    case FORMULA:
                         registrosMovilidad.setPeriodoEscolarCursado((int)fila.getCell(15).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(16).getCellTypeEnum()) {
                    case NUMERIC:
                        int cuat= (int)fila.getCell(16).getNumericCellValue();
                        String c = Integer.toString(cuat);
                        registrosMovilidad.setCuatrimestreCursado(c);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(17).getCellTypeEnum()) {
                    case STRING:
                        organismosVinculados.setNombre(fila.getCell(17).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(18).getCellTypeEnum()) {
                    case FORMULA:
                        organismosVinculados.setEmpresa((int)fila.getCell(18).getNumericCellValue());
                        registrosMovilidad.setInstitucionOrganizacion(organismosVinculados);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(19).getCellTypeEnum()) {
                    case STRING:
                        pais.setNombre(fila.getCell(19).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(20).getCellTypeEnum()) {
                    case FORMULA:
                        pais.setIdpais((int)fila.getCell(20).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                 switch (fila.getCell(23).getCellTypeEnum()) {
                    case STRING:
                        estado.setNombre(fila.getCell(23).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(24).getCellTypeEnum()) {
                    case FORMULA:
                        estado.setIdestado((int)fila.getCell(24).getNumericCellValue());
                        estado.setIdpais(pais);
                        registrosMovilidad.setEstado(estado);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(25).getCellTypeEnum()) {
                    case STRING:
                        registrosMovilidad.setProyecto(fila.getCell(25).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(26).getCellTypeEnum()) {
                    case STRING:
                        registrosMovilidad.setDescripcion(fila.getCell(26).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(28).getCellTypeEnum()) {
                    case FORMULA:
                        BigDecimal est= new BigDecimal((int)fila.getCell(28).getNumericCellValue());
                        registrosMovilidad.setPresEst(est);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(30).getCellTypeEnum()) {
                    case FORMULA:
                        BigDecimal fed= new BigDecimal((int)fila.getCell(30).getNumericCellValue());
                        registrosMovilidad.setPresFed(fed);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(32).getCellTypeEnum()) {
                    case FORMULA:
                        BigDecimal cap= new BigDecimal((int)fila.getCell(32).getNumericCellValue());
                        registrosMovilidad.setCapDer(cap);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(34).getCellTypeEnum()) {
                    case FORMULA:
                        BigDecimal ing= new BigDecimal((int)fila.getCell(34).getNumericCellValue());
                        registrosMovilidad.setIngPropios(ing);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(36).getCellTypeEnum()) {
                    case FORMULA:
                        BigDecimal ext= new BigDecimal((int)fila.getCell(36).getNumericCellValue());
                        registrosMovilidad.setIngExtra(ext);
                        break;
                    default:
                        break;
                }
                
                switch (fila.getCell(37).getCellTypeEnum()) {
                    case STRING:
                        registrosMovilidad.setDescripcionIngExt(fila.getCell(37).getStringCellValue());
                        break;
                    default:
                        break;
                }
                    dTORegistroMovilidad.setAreasUniversidad(areasUniversidad);
                    dTORegistroMovilidad.setRegistrosMovilidad(registrosMovilidad);
                    listaDtoMovilidad.add(dTORegistroMovilidad);
                }
            }
            listaRegistroMovilidad.setMovilidad(listaDtoMovilidad);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaRegistroMovilidad;
    }

    @Override
    public void guardaRegistroMovilidad(ListaRegistroMovilidad listaRegistroMovilidad, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//            listaRegistroMovilidad.getMovilidad().forEach((movilidad) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//            
//            movilidad.getRegistrosMovilidad().getInstitucionOrganizacion().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(movilidad.getRegistrosMovilidad().getInstitucionOrganizacion().getEmpresa()));
//            
//            facdepye.setEntityClass(RegistrosMovilidad.class);
//            movilidad.getRegistrosMovilidad().setRegistro(registro.getRegistro());
//            facdepye.create(movilidad.getRegistrosMovilidad());
//            facdepye.flush();
//        });
        List<String> validaciones = new SerializableArrayList<>();
        List<String> listaCondicional = new ArrayList<>();
        listaRegistroMovilidad.getMovilidad().forEach((movilidad) -> {
               
                facdepye.setEntityClass(RegistrosMovilidad.class);
                RegistrosMovilidad regMovEncontrada = getRegistrosMovilidad(movilidad.getRegistrosMovilidad());
                Boolean registroAlmacenado = false;
                if (regMovEncontrada != null) {
                    listaCondicional.add(movilidad.getRegistrosMovilidad().getRegistroMovilidad());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    movilidad.getRegistrosMovilidad().setRegistro(regMovEncontrada.getRegistro());
                    movilidad.getRegistrosMovilidad().getInstitucionOrganizacion().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(movilidad.getRegistrosMovilidad().getInstitucionOrganizacion().getEmpresa()));
                    facdepye.edit(movilidad.getRegistrosMovilidad());
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    movilidad.getRegistrosMovilidad().setRegistro(registro.getRegistro());
                    movilidad.getRegistrosMovilidad().getInstitucionOrganizacion().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(movilidad.getRegistrosMovilidad().getInstitucionOrganizacion().getEmpresa()));
                    facdepye.create(movilidad.getRegistrosMovilidad());
                }
                facdepye.flush();
                
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
}
    @Override
    public Integer getRegistroMovilidadEspecifico(String registroMovilidad) {
        TypedQuery<RegistrosMovilidad> query = em.createNamedQuery("RegistrosMovilidad.findByRegistroMovilidad", RegistrosMovilidad.class);
        query.setParameter("registroMovilidad", registroMovilidad);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }

    @Override
    public RegistrosMovilidad getRegistrosMovilidad(RegistrosMovilidad registrosMovilidad) {
        TypedQuery<RegistrosMovilidad> query = em.createQuery("SELECT r FROM RegistrosMovilidad r WHERE r.registroMovilidad = :registroMovilidad", RegistrosMovilidad.class);
        query.setParameter("registroMovilidad", registrosMovilidad.getRegistroMovilidad());
        try {
            registrosMovilidad = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            registrosMovilidad = null;
            ex.toString();
        }
        return  registrosMovilidad;
    }

    @Override
    public List<ProgramasMovilidad> getProgramasMovilidadAct() {
        List<ProgramasMovilidad> genLst = new ArrayList<>();
        TypedQuery<ProgramasMovilidad> query = em.createQuery("SELECT p FROM ProgramasMovilidad p", ProgramasMovilidad.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }
}
