/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.ExaniResultadosCiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.escolar.DTOExani;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaExaniResultados;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbExaniResultados;
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
public class ServicioExaniResultados implements EjbExaniResultados{
    @EJB
    Facade facdepye;
    @EJB
    EjbModulos ejbModulos;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    @Override
    public ListaExaniResultados getListaExaniResultados(String rutaArchivo) throws Throwable {
      
        ListaExaniResultados listaExaniResultados = new  ListaExaniResultados();

        List<DTOExani> listaDtoExani = new ArrayList<>();
        AreasUniversidad areaUniversidad;
        ExaniResultadosCiclosEscolares exaniResultadosCiclosEscolares;
        DTOExani dTOExani;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        if (primeraHoja.getSheetName().equals("EXANI")) {
        for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

            if ((!"".equals(fila.getCell(0).getStringCellValue()))) {
                areaUniversidad = new AreasUniversidad();
                exaniResultadosCiclosEscolares = new ExaniResultadosCiclosEscolares();
                dTOExani = new  DTOExani();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        dTOExani.setCicloEscolar(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }

               switch (fila.getCell(1).getCellTypeEnum()) {
                    case FORMULA:
                        exaniResultadosCiclosEscolares.setCicloEscolar((int)fila.getCell(1).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case STRING:
                        areaUniversidad.setNombre(fila.getCell(2).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case FORMULA:
                        areaUniversidad.setArea((short)fila.getCell(3).getNumericCellValue());
                        exaniResultadosCiclosEscolares.setProgramaEducativo(areaUniversidad.getArea());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case FORMULA:
                        exaniResultadosCiclosEscolares.setSustentantes((int) fila.getCell(4).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case NUMERIC:
                        exaniResultadosCiclosEscolares.setICNEalto((int) fila.getCell(6).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(8).getCellTypeEnum()) {
                    case NUMERIC:
                        exaniResultadosCiclosEscolares.setICNEmedio((int) fila.getCell(8).getNumericCellValue());
                        break;
                    default:
                        break;
                }

                switch (fila.getCell(10).getCellTypeEnum()) {
                    case NUMERIC:
                         exaniResultadosCiclosEscolares.setICNEbajo((int) fila.getCell(10).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(12).getCellTypeEnum()) {
                    case NUMERIC:
                        exaniResultadosCiclosEscolares.setSusInscritos((int) fila.getCell(12).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(13).getCellTypeEnum()) {
                    case FORMULA:
                         exaniResultadosCiclosEscolares.setNosusInscritos((int) fila.getCell(13).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                
                 
               dTOExani.setAreasUniversidad(areaUniversidad);
               dTOExani.setExaniResultadosCiclosEscolares(exaniResultadosCiclosEscolares);
                    
               listaDtoExani.add(dTOExani);
            }
            }
            listaExaniResultados.setExanis(listaDtoExani);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaExaniResultados;
        
    }
    
    
    @Override
    public void guardaExaniResultados(ListaExaniResultados listaExaniResultados, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {

//            listaExaniResultados.getExanis().forEach((exanis) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//           
//            facdepye.setEntityClass(ExaniResultadosCiclosEscolares.class);
//            exanis.getExaniResultadosCiclosEscolares().setRegistro(registro.getRegistro());
//            facdepye.create(exanis.getExaniResultadosCiclosEscolares());
//            facdepye.flush();
//        });
        List<String> validaciones = new SerializableArrayList<>();
        List<String> listaCondicional = new ArrayList<>();
        listaExaniResultados.getExanis().forEach((exanis) -> {
            
                facdepye.setEntityClass(ExaniResultadosCiclosEscolares.class);
                ExaniResultadosCiclosEscolares erce = getRegistroExaniResultadosCiclosEscolares(exanis.getExaniResultadosCiclosEscolares().getCicloEscolar(), exanis.getExaniResultadosCiclosEscolares().getProgramaEducativo());
                Boolean registroAlmacenado = false;
                if (erce != null) {
                    listaCondicional.add(exanis.getCicloEscolar());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    exanis.getExaniResultadosCiclosEscolares().setRegistro(erce.getRegistro());
                    facdepye.edit(exanis.getExaniResultadosCiclosEscolares());
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    exanis.getExaniResultadosCiclosEscolares().setRegistro(registro.getRegistro());
                    facdepye.create(exanis.getExaniResultadosCiclosEscolares());
                }
                facdepye.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public ExaniResultadosCiclosEscolares getRegistroExaniResultadosCiclosEscolares(Integer cicloEscolar, Short programaEducativo) {
        ExaniResultadosCiclosEscolares exaniResultadosCiclosEscolares = new ExaniResultadosCiclosEscolares();
        TypedQuery<ExaniResultadosCiclosEscolares> query = em.createQuery("SELECT e FROM ExaniResultadosCiclosEscolares e WHERE e.cicloEscolar = :cicloEscolar AND e.programaEducativo = :programaEducativo", ExaniResultadosCiclosEscolares.class);
        query.setParameter("cicloEscolar", cicloEscolar);
        query.setParameter("programaEducativo", programaEducativo);
        try {
            exaniResultadosCiclosEscolares = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            exaniResultadosCiclosEscolares = null;
            ex.toString();
        }
        return exaniResultadosCiclosEscolares;   
    }
}
