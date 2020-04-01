/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ch;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
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
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbReconocimientoProdep;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbCuerposAcademicos;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioReconocimientoProdep implements EjbReconocimientoProdep{
    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
    @EJB EjbCuerposAcademicos ejbCuerposAcademicos;
    @Inject ControladorEmpleado controladorEmpleado;
   
    @Override
    public List<DTOReconocimientoProdep> getListaReconocimientoProdep(String rutaArchivo) throws Throwable {
       
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
                        dTOReconocimientoProdep.setTipo(fila.getCell(4).getStringCellValue());
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
            libroRegistro.close();
            Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaDtoRecProdep;
    }

    @Override
    public void guardaReconocimientoProdep(List<DTOReconocimientoProdep> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {

            List<String> listaCondicional = new ArrayList<>();
            lista.forEach((reconocimientos) -> {
            f.setEntityClass(ReconocimientoProdepRegistros.class);
            ReconocimientoProdepRegistros recProReEncontrado = getRegistroReconocimientoProdepRegistros(reconocimientos.getReconocimientoProdepRegistros());
            Boolean registroAlmacenado = false;
            if (recProReEncontrado != null) {
                listaCondicional.add(reconocimientos.getReconocimientoProdepRegistros().getDocente()+ " " + reconocimientos.getReconocimientoProdepRegistros().getCuerpAcad().getCuerpoAcademico());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                
                if (ejbModulos.validaEventoRegistro(ejbModulos.getEventoRegistro(), recProReEncontrado.getRegistros().getEventoRegistro().getEventoRegistro())) {
                    reconocimientos.getReconocimientoProdepRegistros().setRegistro(recProReEncontrado.getRegistro());
                    reconocimientos.getReconocimientoProdepRegistros().getCuerpAcad().setRegistro(recProReEncontrado.getCuerpAcad().getRegistro());
                    f.edit(reconocimientos.getReconocimientoProdepRegistros());
                    Messages.addGlobalInfo("<b>Se actualizaron los registros del o la Docente con Clave: </b> " + recProReEncontrado.getDocente());
                } else {
                    Messages.addGlobalWarn("<b>No se pueden actualizar los registros del o la Docente con Clave: </b> " + recProReEncontrado.getDocente() + "<b> porque pertenece a otro mes</b>");
                }
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                reconocimientos.getReconocimientoProdepRegistros().getCuerpAcad().setRegistro(ejbCuerposAcademicos.getRegistroCuerpoAcademicoEspecifico(reconocimientos.getReconocimientoProdepRegistros().getCuerpAcad().getCuerpoAcademico()));
                reconocimientos.getReconocimientoProdepRegistros().setRegistro(registro.getRegistro());
                f.create(reconocimientos.getReconocimientoProdepRegistros());
                Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b> ");
            }
            f.flush();
        });
    }

    @Override
    public ReconocimientoProdepRegistros getRegistroReconocimientoProdepRegistros(ReconocimientoProdepRegistros reconocimientoProdepRegistros) {
        TypedQuery<ReconocimientoProdepRegistros> query = f.getEntityManager().createQuery("SELECT r FROM ReconocimientoProdepRegistros r JOIN r.cuerpAcad c JOIN r.tipoApoyo t WHERE r.docente = :docente AND c.cuerpoAcademico = :cuerpAcad AND t.tipo = :tipoApoyo", ReconocimientoProdepRegistros.class);
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
        TypedQuery<ReconocimientoProdepTiposApoyo> query = f.getEntityManager().createQuery("SELECT r FROM ReconocimientoProdepTiposApoyo r", ReconocimientoProdepTiposApoyo.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }

    @Override
    public List<DTOReconocimientoProdep> getRegistroDTOReconocimientosProdep(String mes, Short ejercicio) {
        List<DTOReconocimientoProdep> ldto = new ArrayList<>();
        TypedQuery<ReconocimientoProdepRegistros> q = f.getEntityManager()
                .createQuery("SELECT r from ReconocimientoProdepRegistros r WHERE r.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND r.registros.eventoRegistro.mes = :mes AND r.registros.area = :area", ReconocimientoProdepRegistros.class);
//         AND r.registros.area = :area
        q.setParameter("mes", mes);
        q.setParameter("ejercicio", ejercicio);
        q.setParameter("area", controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
        List<ReconocimientoProdepRegistros> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            TypedQuery<EventosRegistros> query = f.getEntityManager().createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            l.forEach(x -> {
                Registros registro = f.getEntityManager().find(Registros.class, x.getRegistro());
                EventosRegistros eventos = f.getEntityManager().find(EventosRegistros.class,registro.getEventoRegistro().getEventoRegistro());
                Personal personal = f.getEntityManager().find(Personal.class, x.getDocente());
                ReconocimientoProdepTiposApoyo reconocimientoProdepTiposApoyo = f.getEntityManager().find(ReconocimientoProdepTiposApoyo.class, x.getTipoApoyo().getTipo());
                TypedQuery<CuerposAcademicosRegistro> cuerposAcad = f.getEntityManager().createQuery("SELECT ca FROM CuerposAcademicosRegistro ca WHERE ca.cuerpoAcademico = :cuerpoAcademico", CuerposAcademicosRegistro.class);
                cuerposAcad.setParameter("cuerpoAcademico", x.getCuerpAcad().getCuerpoAcademico());
                CuerposAcademicosRegistro cuerposAcademicosRegistro = f.getEntityManager().find(CuerposAcademicosRegistro.class, cuerposAcad.getSingleResult().getRegistro());
                AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, registro.getArea());
                ActividadesPoa a = registro.getActividadesPoaList().isEmpty() ? null :registro.getActividadesPoaList().get(0);
                DTOReconocimientoProdep dto;
                if (eventoRegistro.equals(registro.getEventoRegistro())) {
                    dto = new DTOReconocimientoProdep(x, personal, cuerposAcademicosRegistro, reconocimientoProdepTiposApoyo, a, eventos, reconocimientoProdepTiposApoyo.getDescripcion());
                } else {
                    dto = new DTOReconocimientoProdep(x, personal, cuerposAcademicosRegistro, reconocimientoProdepTiposApoyo, a, eventos, reconocimientoProdepTiposApoyo.getDescripcion());
                }
                ldto.add(dto);
            });
            System.err.println("ldto " + ldto.toString());
            return ldto;
    }
    }

    @Override
    public List<DTOReconocimientoProdep> getRegistroRecProdep(Short ejercicio) {
        List<DTOReconocimientoProdep> ldto = new ArrayList<>();
        TypedQuery<ReconocimientoProdepRegistros> q = f.getEntityManager()
                .createQuery("SELECT r from ReconocimientoProdepRegistros r INNER JOIN r.registros reg INNER JOIN reg.eventoRegistro er WHERE er.ejercicioFiscal.ejercicioFiscal = :ejercicio", ReconocimientoProdepRegistros.class);
        List<ReconocimientoProdepRegistros> l = q
                .setParameter("ejercicio", ejercicio)
                .getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            TypedQuery<EventosRegistros> query = f.getEntityManager().createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            l.forEach(x -> {
                Registros registro = f.getEntityManager().find(Registros.class, x.getRegistro());
                EventosRegistros eventos = f.getEntityManager().find(EventosRegistros.class, registro.getEventoRegistro().getEventoRegistro());
                Personal personal = f.getEntityManager().find(Personal.class, x.getDocente());
                ReconocimientoProdepTiposApoyo reconocimientoProdepTiposApoyo = f.getEntityManager().find(ReconocimientoProdepTiposApoyo.class, x.getTipoApoyo().getTipo());
                TypedQuery<CuerposAcademicosRegistro> cuerposAcad = f.getEntityManager().createQuery("SELECT ca FROM CuerposAcademicosRegistro ca WHERE ca.cuerpoAcademico = :cuerpoAcademico", CuerposAcademicosRegistro.class);
                cuerposAcad.setParameter("cuerpoAcademico", x.getCuerpAcad().getCuerpoAcademico());
                CuerposAcademicosRegistro cuerposAcademicosRegistro = f.getEntityManager().find(CuerposAcademicosRegistro.class, cuerposAcad.getSingleResult().getRegistro());
                AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, registro.getArea());
                ActividadesPoa a = registro.getActividadesPoaList().isEmpty() ? null : registro.getActividadesPoaList().get(0);
                DTOReconocimientoProdep dto;
                if (eventoRegistro.equals(registro.getEventoRegistro())) {
                    dto = new DTOReconocimientoProdep(x, personal, cuerposAcademicosRegistro, reconocimientoProdepTiposApoyo, a, eventos, reconocimientoProdepTiposApoyo.getDescripcion());
                } else {
                    dto = new DTOReconocimientoProdep(x, personal, cuerposAcademicosRegistro, reconocimientoProdepTiposApoyo, a, eventos, reconocimientoProdepTiposApoyo.getDescripcion());
                }
                ldto.add(dto);
            });
            return ldto;
        }
    }
}
