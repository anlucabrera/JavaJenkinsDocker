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
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.AcervoBibliograficoPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.escolar.DTOAcervoBibliograficoPeriodosEscolares;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaAcervoBibliografico;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbAcervoBibliografico;
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
public class ServicioAcervoBibliografico implements EjbAcervoBibliografico{
    @EJB
    Facade facdepye;
    
    @EJB
    EjbModulos ejbModulos;
    
    @EJB
    EjbAdministracionEncuestas eJBAdministracionEncuestas;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
     
    @Override
    public  ListaAcervoBibliografico getListaAcervoBibliografico(String rutaArchivo) throws Throwable {
       
        ListaAcervoBibliografico listaAcervoBibliografico = new  ListaAcervoBibliografico();

        List<DTOAcervoBibliograficoPeriodosEscolares> listaDtoAcervoBibliografico = new ArrayList<>();
        AreasUniversidad areaUniversidad;
        AcervoBibliograficoPeriodosEscolares acervoBibliograficoPeriodosEscolares;
        DTOAcervoBibliograficoPeriodosEscolares  dTOAcervoBibliograficoPeriodosEscolares;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        if (primeraHoja.getSheetName().equals("Acervo Bibliográfico")) {
        for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

            if ((fila.getCell(3).getNumericCellValue()!= 0)) {
                areaUniversidad = new AreasUniversidad();
                acervoBibliograficoPeriodosEscolares = new AcervoBibliograficoPeriodosEscolares();
                dTOAcervoBibliograficoPeriodosEscolares = new DTOAcervoBibliograficoPeriodosEscolares();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        dTOAcervoBibliograficoPeriodosEscolares.setCicloEscolar(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(1).getCellTypeEnum()) {
                    case FORMULA:
                        acervoBibliograficoPeriodosEscolares.setCicloEscolar((int) fila.getCell(1).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case STRING:
                        dTOAcervoBibliograficoPeriodosEscolares.setPeriodoEscolar(fila.getCell(2).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                   case FORMULA:
                        acervoBibliograficoPeriodosEscolares.setPeriodoEscolar((int) fila.getCell(3).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case FORMULA:
                        areaUniversidad.setArea((short) ((int) fila.getCell(5).getNumericCellValue()));
                        areaUniversidad.setNombre(fila.getCell(4).getStringCellValue());
                        acervoBibliograficoPeriodosEscolares.setProgramaEducativo(areaUniversidad.getArea());
                        break;
                    default:
                        break;

                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setEduTit((int) fila.getCell(6).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setEduVol((int) fila.getCell(7).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(8).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setArthumTit((int) fila.getCell(8).getNumericCellValue());
                        break;
                    default:
                        break;
                }

                switch (fila.getCell(9).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setArthumVol((int) fila.getCell(9).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(10).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setCsaydTit((int) fila.getCell(10).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(11).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setCsaydVol((int) fila.getCell(11).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(12).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setCneycTit((int) fila.getCell(12).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(13).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setCneycVol((int) fila.getCell(13).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(14).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setImycTit((int) fila.getCell(14).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(15).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setImycVol((int) fila.getCell(15).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(16).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setAyvTit((int) fila.getCell(16).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(17).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setAyvVol((int) fila.getCell(17).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(18).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setSalTit((int) fila.getCell(18).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(19).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setSalVol((int) fila.getCell(19).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(20).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setSerTit((int) fila.getCell(20).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(21).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setSerVol((int) fila.getCell(21).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(22).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setRevTit((int) fila.getCell(22).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(23).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setRevVol((int) fila.getCell(23).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(24).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setFollTit((int) fila.getCell(24).getNumericCellValue());
                    default:
                        break;
                }
                switch (fila.getCell(25).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setFollVol((int) fila.getCell(25).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(26).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setVidTit((int) fila.getCell(26).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(27).getCellTypeEnum()) {
                    case NUMERIC:
                        acervoBibliograficoPeriodosEscolares.setVidVol((int) fila.getCell(27).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                
               dTOAcervoBibliograficoPeriodosEscolares.setAreasUniversidad(areaUniversidad);
               dTOAcervoBibliograficoPeriodosEscolares.setAcervoBibliograficoPeriodosEscolares(acervoBibliograficoPeriodosEscolares);
                    
               listaDtoAcervoBibliografico.add(dTOAcervoBibliograficoPeriodosEscolares);
            }
            }
            listaAcervoBibliografico.setAcervoBibliograficoPeriodosEscolares(listaDtoAcervoBibliografico);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaAcervoBibliografico;
        
    }
    
   @Override
    public void guardaAcervoBibliografico(ListaAcervoBibliografico listaAcervoBibliografico, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {

//        listaAcervoBibliografico.getAcervoBibliograficoPeriodosEscolares().forEach((acervoBibliograficoPeriodosEscolares) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//           
//            facdepye.setEntityClass(AcervoBibliograficoPeriodosEscolares.class);
//            acervoBibliograficoPeriodosEscolares.getAcervoBibliograficoPeriodosEscolares().setRegistro(registro.getRegistro());
//            facdepye.create(acervoBibliograficoPeriodosEscolares.getAcervoBibliograficoPeriodosEscolares());
//            facdepye.flush();
//        });

        List<String> validaciones = new SerializableArrayList<>();
        List<String> listaCondicional = new ArrayList<>();
        listaAcervoBibliografico.getAcervoBibliograficoPeriodosEscolares().forEach((acervoBibliograficoPeriodosEscolares) -> {
            if (ejbModulos.validaPeriodoRegistro(eJBAdministracionEncuestas.getPeriodoActual(), acervoBibliograficoPeriodosEscolares.getAcervoBibliograficoPeriodosEscolares().getPeriodoEscolar())) {
                facdepye.setEntityClass(AcervoBibliograficoPeriodosEscolares.class);
                AcervoBibliograficoPeriodosEscolares abpe = getRegistroAcervoBibliograficoPeriodosEscolares(acervoBibliograficoPeriodosEscolares.getAcervoBibliograficoPeriodosEscolares().getCicloEscolar(), acervoBibliograficoPeriodosEscolares.getAcervoBibliograficoPeriodosEscolares().getPeriodoEscolar(), acervoBibliograficoPeriodosEscolares.getAcervoBibliograficoPeriodosEscolares().getProgramaEducativo());
                Boolean registroAlmacenado = false;
                if (abpe != null) {
                    listaCondicional.add(acervoBibliograficoPeriodosEscolares.getPeriodoEscolar()+" - "+acervoBibliograficoPeriodosEscolares.getAreasUniversidad().getNombre());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    acervoBibliograficoPeriodosEscolares.getAcervoBibliograficoPeriodosEscolares().setRegistro(abpe.getRegistro());
                    facdepye.edit(acervoBibliograficoPeriodosEscolares.getAcervoBibliograficoPeriodosEscolares());
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    acervoBibliograficoPeriodosEscolares.getAcervoBibliograficoPeriodosEscolares().setRegistro(registro.getRegistro());
                    facdepye.create(acervoBibliograficoPeriodosEscolares.getAcervoBibliograficoPeriodosEscolares());
                }
                facdepye.flush();
            }
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }
    
    @Override
    public AcervoBibliograficoPeriodosEscolares getRegistroAcervoBibliograficoPeriodosEscolares(Integer cicloEscolar, Integer periodoEscolar, Short programaEducativo) {
        AcervoBibliograficoPeriodosEscolares acervoBibliograficoPeriodosEscolares = new AcervoBibliograficoPeriodosEscolares();
        TypedQuery<AcervoBibliograficoPeriodosEscolares> query = em.createQuery("SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.cicloEscolar = :cicloEscolar AND a.periodoEscolar = :periodoEscolar AND a.programaEducativo = :programaEducativo", AcervoBibliograficoPeriodosEscolares.class);
        query.setParameter("cicloEscolar", cicloEscolar);
        query.setParameter("periodoEscolar", periodoEscolar);
        query.setParameter("programaEducativo", programaEducativo);
        try {
            acervoBibliograficoPeriodosEscolares = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            acervoBibliograficoPeriodosEscolares = null;
            ex.toString();
        }
        return acervoBibliograficoPeriodosEscolares;
    }

    @Override
    public List<AcervoBibliograficoPeriodosEscolares> getAcervo() {
        System.err.println("entra al ejb");
        TypedQuery<AcervoBibliograficoPeriodosEscolares> q = facdepye.getEntityManager().createQuery("SELECT a FROM AcervoBibliograficoPeriodosEscolares a ORDER BY a.registro DESC", AcervoBibliograficoPeriodosEscolares.class);
        q.setMaxResults(10);
        System.err.println("la lista de acervo es : ");
        q.getResultList().forEach(System.err::println);
        return q.getResultList();
    }

    @Override
    public List<AcervoBibliograficoPeriodosEscolares> getAcervoByFechas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AcervoBibliograficoPeriodosEscolares eliminaAcervo(Integer acervo) {
        System.err.println("Entra a el acervo : " + acervo);
        if (acervo == null) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioAcervoBibliografico.eliminaAcervo() valor null de acervo");
            return null;
        } else {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioAcervoBibliografico.eliminaAcervo() el acervo que se elimina : " + acervo);
            facdepye.setEntityClass(AcervoBibliograficoPeriodosEscolares.class);
            AcervoBibliograficoPeriodosEscolares a = facdepye.getEntityManager().find(AcervoBibliograficoPeriodosEscolares.class, acervo);
            System.err.println("encuentra la iem : " + a);
            facdepye.remove(a);
            return a;
        }
    }

    @Override
    public List<AcervoBibliograficoPeriodosEscolares> filtroAcervo(Integer ciclo, Integer periodo) {
        System.err.println("el ciclo seleccionado es : " + ciclo + " y el periodo es : " + periodo);
        TypedQuery<AcervoBibliograficoPeriodosEscolares> q = facdepye.getEntityManager()
                .createQuery("SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.cicloEscolar = :cicloEscolar AND a.periodoEscolar = :periodoEscolar ORDER BY a.registro DESC", AcervoBibliograficoPeriodosEscolares.class);
        q.setParameter("cicloEscolar", ciclo);
        q.setParameter("periodoEscolar", periodo);
        
        List<AcervoBibliograficoPeriodosEscolares> li = q.getResultList();
        if(li.isEmpty() || li == null){
            System.err.println("no se encontraron registro de este periodo : " + periodo);
            return null;
        }else{
            System.err.println("El tamaño de la lista es : " + li.size());
            return li;
        }
    }

}
