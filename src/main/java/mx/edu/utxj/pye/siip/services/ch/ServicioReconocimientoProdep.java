/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ch;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
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

                if ((fila.getCell(1).getNumericCellValue()>0)) {
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
                        cuerposAcademicosRegistro = getRegistroCuerpoAcademicoRegistros(fila.getCell(2).getStringCellValue());
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
                    dTOReconocimientoProdep.setCuerposAcademicosRegistro(cuerposAcademicosRegistro);
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
            ReconocimientoProdepRegistros recProReEncontrado = getRegistroReconocimientoProdepRegistros(reconocimientos.getReconocimientoProdepRegistros(), eventosRegistros);
            Boolean registroAlmacenado = false;
            if (recProReEncontrado != null) {
                listaCondicional.add(reconocimientos.getReconocimientoProdepRegistros().getDocente()+ " " + reconocimientos.getReconocimientoProdepRegistros().getCuerpAcad().getCuerpoAcademico());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                if (ejbModulos.validaEventoRegistro(ejbModulos.getEventoRegistro(), recProReEncontrado.getRegistros().getEventoRegistro().getEventoRegistro())) {
                    reconocimientos.getReconocimientoProdepRegistros().setRegistro(recProReEncontrado.getRegistro());
                    if(reconocimientos.getCuerposAcademicosRegistro()!= null){
                         CuerposAcademicosRegistro cuerposAcademicosRegistro = getRegistroCuerpoAcademicoRegistros(recProReEncontrado.getCuerpAcad().getCuerpoAcademico());
                        reconocimientos.getReconocimientoProdepRegistros().getCuerpAcad().setRegistro(cuerposAcademicosRegistro.getRegistro());
                    }else{
                        reconocimientos.getReconocimientoProdepRegistros().setCuerpAcad(null);
                    }
                    
                    f.edit(reconocimientos.getReconocimientoProdepRegistros());
                    Messages.addGlobalInfo("<b>Se actualizaron los registros del o la Docente con Clave: </b> " + recProReEncontrado.getDocente());
                } else {
                    Messages.addGlobalWarn("<b>No se pueden actualizar los registros del o la Docente con Clave: </b> " + recProReEncontrado.getDocente() + "<b> porque pertenece a otro mes</b>");
                }
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                if(reconocimientos.getCuerposAcademicosRegistro() != null){
                    CuerposAcademicosRegistro cuerposAcademicosRegistro = getRegistroCuerpoAcademicoRegistros(reconocimientos.getCuerposAcademicosRegistro().getCuerpoAcademico());
                    reconocimientos.getReconocimientoProdepRegistros().getCuerpAcad().setRegistro(cuerposAcademicosRegistro.getRegistro());
                }else{
                    reconocimientos.getReconocimientoProdepRegistros().setCuerpAcad(null);
                }
                reconocimientos.getReconocimientoProdepRegistros().setRegistro(registro.getRegistro());
                f.create(reconocimientos.getReconocimientoProdepRegistros());
                Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b> ");
            }
            f.flush();
        });
    }

    @Override
    public ReconocimientoProdepRegistros getRegistroReconocimientoProdepRegistros(ReconocimientoProdepRegistros reconocimientoProdepRegistros, EventosRegistros eventosRegistros) {
        ReconocimientoProdepRegistros prodepRegistros = new ReconocimientoProdepRegistros();
        if(reconocimientoProdepRegistros.getCuerpAcad() != null){
             prodepRegistros = f.getEntityManager().createQuery("SELECT r FROM ReconocimientoProdepRegistros r WHERE r.docente =:docente AND r.cuerpAcad.cuerpoAcademico=:cuerpoAcademico AND r.registros.eventoRegistro.eventoRegistro =:evento AND r.tipoApoyo.tipo=:tipo", ReconocimientoProdepRegistros.class)
                     .setParameter("docente", reconocimientoProdepRegistros.getDocente())   
                     .setParameter("cuerpoAcademico", reconocimientoProdepRegistros.getCuerpAcad().getCuerpoAcademico())
                     .setParameter("evento", eventosRegistros.getEventoRegistro())
                     .setParameter("tipo", reconocimientoProdepRegistros.getTipoApoyo().getTipo())
                     .getResultStream()
                     .findFirst()
                     .orElse(null);
        }else{
             prodepRegistros = f.getEntityManager().createQuery("SELECT r FROM ReconocimientoProdepRegistros r WHERE r.docente =:docente AND r.registros.eventoRegistro.eventoRegistro=:evento AND r.tipoApoyo.tipo=:tipo", ReconocimientoProdepRegistros.class)
                     .setParameter("docente", reconocimientoProdepRegistros.getDocente())   
                     .setParameter("evento", reconocimientoProdepRegistros.getRegistros().getEventoRegistro().getEventoRegistro())
                     .setParameter("tipo", reconocimientoProdepRegistros.getTipoApoyo().getTipo())
                     .getResultStream()
                     .findFirst()
                     .orElse(null);
        
        }
        return prodepRegistros;
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
    public List<DTOReconocimientoProdep> getRegistroDTOReconocimientosProdep(String mes, Short ejercicio, Short area) {
        List<DTOReconocimientoProdep> ldto = new ArrayList<>();
        TypedQuery<ReconocimientoProdepRegistros> q = f.getEntityManager()
                .createQuery("SELECT r from ReconocimientoProdepRegistros r WHERE r.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND r.registros.eventoRegistro.mes = :mes AND r.registros.area = :area", ReconocimientoProdepRegistros.class);
//         AND r.registros.area = :area
        q.setParameter("mes", mes);
        q.setParameter("ejercicio", ejercicio);
        q.setParameter("area", area);
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
                if(x.getCuerpAcad() !=null){
                    CuerposAcademicosRegistro cuerposAcademicosRegistro = f.getEntityManager().createQuery("SELECT ca FROM CuerposAcademicosRegistro ca WHERE ca.cuerpoAcademico = :cuerpoAcademico ORDER BY ca.registros.eventoRegistro.eventoRegistro DESC", CuerposAcademicosRegistro.class)
                        .setParameter("cuerpoAcademico", x.getCuerpAcad().getCuerpoAcademico())
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
                    AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, registro.getArea());
                    ActividadesPoa a = registro.getActividadesPoaList().isEmpty() ? null : registro.getActividadesPoaList().get(0);
                    DTOReconocimientoProdep dto;
                    if (eventoRegistro.equals(registro.getEventoRegistro())) {
                        dto = new DTOReconocimientoProdep(x, personal, cuerposAcademicosRegistro, reconocimientoProdepTiposApoyo, a, eventos, reconocimientoProdepTiposApoyo.getDescripcion());
                    } else {
                        dto = new DTOReconocimientoProdep(x, personal, cuerposAcademicosRegistro, reconocimientoProdepTiposApoyo, a, eventos, reconocimientoProdepTiposApoyo.getDescripcion());
                    }
                    ldto.add(dto);
                } else{
                    CuerposAcademicosRegistro cuerposAcademicosRegistro = null;
                    AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, registro.getArea());
                    ActividadesPoa a = registro.getActividadesPoaList().isEmpty() ? null : registro.getActividadesPoaList().get(0);
                    DTOReconocimientoProdep dto;
                    if (eventoRegistro.equals(registro.getEventoRegistro())) {
                        dto = new DTOReconocimientoProdep(x, personal, cuerposAcademicosRegistro, reconocimientoProdepTiposApoyo, a, eventos, reconocimientoProdepTiposApoyo.getDescripcion());
                    } else {
                        dto = new DTOReconocimientoProdep(x, personal, cuerposAcademicosRegistro, reconocimientoProdepTiposApoyo, a, eventos, reconocimientoProdepTiposApoyo.getDescripcion());
                    }
                    ldto.add(dto);
                }
            });
            return ldto;
    }
    }

    @Override
    public List<DTOReconocimientoProdep> getRegistroRecProdep(Short ejercicio, Short area) {
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
                if (x.getCuerpAcad() != null) {
                    CuerposAcademicosRegistro cuerposAcademicosRegistro = f.getEntityManager().createQuery("SELECT ca FROM CuerposAcademicosRegistro ca WHERE ca.cuerpoAcademico = :cuerpoAcademico ORDER BY ca.registros.eventoRegistro.eventoRegistro DESC", CuerposAcademicosRegistro.class)
                        .setParameter("cuerpoAcademico", x.getCuerpAcad().getCuerpoAcademico())
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
                    AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, registro.getArea());
                    ActividadesPoa a = registro.getActividadesPoaList().isEmpty() ? null : registro.getActividadesPoaList().get(0);
                    DTOReconocimientoProdep dto;
                    if (eventoRegistro.equals(registro.getEventoRegistro())) {
                        dto = new DTOReconocimientoProdep(x, personal, cuerposAcademicosRegistro, reconocimientoProdepTiposApoyo, a, eventos, reconocimientoProdepTiposApoyo.getDescripcion());
                    } else {
                        dto = new DTOReconocimientoProdep(x, personal, cuerposAcademicosRegistro, reconocimientoProdepTiposApoyo, a, eventos, reconocimientoProdepTiposApoyo.getDescripcion());
                    }
                    ldto.add(dto);
                }else{
                    CuerposAcademicosRegistro cuerposAcademicosRegistro = null;
                    AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, registro.getArea());
                    ActividadesPoa a = registro.getActividadesPoaList().isEmpty() ? null : registro.getActividadesPoaList().get(0);
                    DTOReconocimientoProdep dto;
                    if (eventoRegistro.equals(registro.getEventoRegistro())) {
                        dto = new DTOReconocimientoProdep(x, personal, cuerposAcademicosRegistro, reconocimientoProdepTiposApoyo, a, eventos, reconocimientoProdepTiposApoyo.getDescripcion());
                    } else {
                        dto = new DTOReconocimientoProdep(x, personal, cuerposAcademicosRegistro, reconocimientoProdepTiposApoyo, a, eventos, reconocimientoProdepTiposApoyo.getDescripcion());
                    }
                    ldto.add(dto);
                }
            });
            return ldto;
        }
    }

    @Override
    public CuerposAcademicosRegistro getRegistroCuerpoAcademicoRegistros(String cuerpoAcademico) {
        
       CuerposAcademicosRegistro cuerposAcademicosRegistro = f.getEntityManager().createQuery("SELECT ca FROM CuerposAcademicosRegistro ca WHERE ca.cuerpoAcademico = :cuerpoAcademico ORDER BY ca.registros.eventoRegistro.eventoRegistro DESC", CuerposAcademicosRegistro.class)
                        .setParameter("cuerpoAcademico", cuerpoAcademico)
                        .getResultStream()
                        .findFirst()
                        .orElse(new CuerposAcademicosRegistro());
        
        return cuerposAcademicosRegistro;
    }
}
