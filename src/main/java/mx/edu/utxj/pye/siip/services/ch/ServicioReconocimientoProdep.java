/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ch;

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
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.ReconocimientoProdepRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.ReconocimientoProdepTiposApoyo;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerposAcademicosRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.caphum.DTOReconocimientoProdep;
import mx.edu.utxj.pye.siip.entity.caphum.list.ListaReconocimientoProdep;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbReconocimientoProdep;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbCuerposAcademicos;
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
public class ServicioReconocimientoProdep implements EjbReconocimientoProdep{
    @EJB
    Facade facadepye;
    @EJB
    EjbModulos ejbModulos;
    @EJB
    EjbCuerposAcademicos ejbCuerposAcademicos;
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
   
    @Override
    public ListaReconocimientoProdep getListaReconocimientoProdep(String rutaArchivo) throws Throwable {
       
        ListaReconocimientoProdep listaReconocimientoProdep = new  ListaReconocimientoProdep();

        List<DTOReconocimientoProdep> listaDtoRecProdep = new ArrayList<>();
        Personal personal;
        ReconocimientoProdepRegistros reconocimientoProdepRegistros;
        ReconocimientoProdepTiposApoyo reconocimientoProdepTiposApoyo;
        CuerposAcademicosRegistro cuerposAcademicosRegistro;
        DTOReconocimientoProdep dTOReconocimientoProdep;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        if (primeraHoja.getSheetName().equals("Reconocimiento PRODEP")) {
        for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if ((fila.getCell(7).getNumericCellValue()!= 0)) {
                personal = new Personal();
                reconocimientoProdepRegistros = new ReconocimientoProdepRegistros();
                reconocimientoProdepTiposApoyo = new ReconocimientoProdepTiposApoyo();
                cuerposAcademicosRegistro = new CuerposAcademicosRegistro();
                dTOReconocimientoProdep = new DTOReconocimientoProdep();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        personal.setNombre(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                 switch (fila.getCell(1).getCellTypeEnum()) {
                    case FORMULA:
                        personal.setClave((int)fila.getCell(1).getNumericCellValue());
                        reconocimientoProdepRegistros.setDocente(personal.getClave());
                        break;
                    default:
                        break;
                }
                  switch (fila.getCell(2).getCellTypeEnum()) {
                    case STRING:
                        cuerposAcademicosRegistro.setNombre(fila.getCell(2).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case FORMULA:
                        cuerposAcademicosRegistro.setCuerpoAcademico(fila.getCell(3).getStringCellValue());
                        reconocimientoProdepRegistros.setCuerpAcad(cuerposAcademicosRegistro);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case STRING: 
                        reconocimientoProdepTiposApoyo.setDescripcion(fila.getCell(4).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case FORMULA:
                        reconocimientoProdepTiposApoyo.setTipo((short)fila.getCell(5).getNumericCellValue());
                        reconocimientoProdepRegistros.setTipoApoyo(reconocimientoProdepTiposApoyo);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                    case FORMULA: 
                        reconocimientoProdepRegistros.setMonto((double)fila.getCell(7).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(8).getCellTypeEnum()) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(8))) {
                         reconocimientoProdepRegistros.setVigenciaInicial(fila.getCell(8).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(9).getCellTypeEnum()) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(fila.getCell(9))) {
                         reconocimientoProdepRegistros.setVigenciaFinal(fila.getCell(9).getDateCellValue());
                        }
                        break;
                    default:
                        break;
                }
                    dTOReconocimientoProdep.setPersonal(personal);
                    dTOReconocimientoProdep.setReconocimientoProdepRegistros(reconocimientoProdepRegistros);
                    listaDtoRecProdep.add(dTOReconocimientoProdep);
                }
            }
            listaReconocimientoProdep.setReconocimientos(listaDtoRecProdep);
            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaReconocimientoProdep;
    }

    @Override
    public void guardaReconocimientoProdep(ListaReconocimientoProdep listaReconocimientoProdep, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//
//            listaReconocimientoProdep.getReconocimientos().forEach((reconocimientos) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//            
////            reconocimientos.getReconocimientoProdepRegistros().getCuerpAcad().setRegistro(ejbCuerposAcademicos.getRegistroCuerposAcademicosEspecifico(reconocimientos.getReconocimientoProdepRegistros().getCuerpoAcademico().getCuerpAcad().getCuerpoAcademico()));
//            reconocimientos.getReconocimientoProdepRegistros().getCuerpAcad().setRegistro(328);
//             
//            facadepye.setEntityClass(ReconocimientoProdepRegistros.class);
//            reconocimientos.getReconocimientoProdepRegistros().setRegistro(registro.getRegistro());
//            facadepye.create(reconocimientos.getReconocimientoProdepRegistros());
//            facadepye.flush();
//        });
            List<String> listaCondicional = new ArrayList<>();
            listaReconocimientoProdep.getReconocimientos().forEach((reconocimientos) -> {
            facadepye.setEntityClass(ReconocimientoProdepRegistros.class);
            ReconocimientoProdepRegistros recProReEncontrado = getRegistroReconocimientoProdepRegistros(reconocimientos.getReconocimientoProdepRegistros());
            Boolean registroAlmacenado = false;
            if (recProReEncontrado != null) {
                listaCondicional.add(reconocimientos.getReconocimientoProdepRegistros().getDocente()+ " " + reconocimientos.getReconocimientoProdepRegistros().getCuerpAcad().getCuerpoAcademico());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                reconocimientos.getReconocimientoProdepRegistros().setRegistro(recProReEncontrado.getRegistro());
                reconocimientos.getReconocimientoProdepRegistros().getCuerpAcad().setRegistro(recProReEncontrado.getCuerpAcad().getRegistro());
                facadepye.edit(reconocimientos.getReconocimientoProdepRegistros());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                reconocimientos.getReconocimientoProdepRegistros().getCuerpAcad().setRegistro(ejbCuerposAcademicos.getRegistroCuerpoAcademicoEspecifico(reconocimientos.getReconocimientoProdepRegistros().getCuerpAcad().getCuerpoAcademico()));
                reconocimientos.getReconocimientoProdepRegistros().setRegistro(registro.getRegistro());
                facadepye.create(reconocimientos.getReconocimientoProdepRegistros());
            }
            facadepye.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public ReconocimientoProdepRegistros getRegistroReconocimientoProdepRegistros(ReconocimientoProdepRegistros reconocimientoProdepRegistros) {
        TypedQuery<ReconocimientoProdepRegistros> query = em.createQuery("SELECT r FROM ReconocimientoProdepRegistros r JOIN r.cuerpAcad c JOIN r.tipoApoyo t WHERE r.docente = :docente AND c.cuerpoAcademico = :cuerpAcad AND t.tipo = :tipoApoyo", ReconocimientoProdepRegistros.class);
        query.setParameter("docente",reconocimientoProdepRegistros.getDocente());
        query.setParameter("cuerpAcad", reconocimientoProdepRegistros.getCuerpAcad().getCuerpoAcademico());
        query.setParameter("tipoApoyo", reconocimientoProdepRegistros.getTipoApoyo().getTipo());
        try {
            reconocimientoProdepRegistros = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            reconocimientoProdepRegistros = null;
        }
        return reconocimientoProdepRegistros;
    }

    @Override
    public List<ReconocimientoProdepTiposApoyo> getReconocimientoProdepTiposApoyoAct() {
        List<ReconocimientoProdepTiposApoyo> genLst = new ArrayList<>();
        TypedQuery<ReconocimientoProdepTiposApoyo> query = em.createQuery("SELECT r FROM ReconocimientoProdepTiposApoyo r", ReconocimientoProdepTiposApoyo.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }
}
