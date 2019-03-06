/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.DesercionPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.DesercionReprobacionMaterias;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.MateriasProgramaEducativo;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.escolar.DTOReprobacion;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaDesercionReprobacion;
import mx.edu.utxj.pye.siip.entity.pye.list.ListaDtoReprobacion;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbDesercionPeriodos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbDesercionReprobacion;
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
public class ServicioDesercionReprobacion implements EjbDesercionReprobacion{
    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
    @EJB EjbDesercionPeriodos ejbDesercionPeriodos;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @Inject ControladorEmpleado controladorEmpleado;
  
    @Override
    public  ListaDesercionReprobacion getListaDesercionReprobacion(String rutaArchivo) throws Throwable {
       
        ListaDesercionReprobacion listaDesercionReprobacion = new ListaDesercionReprobacion();

        List<DTOReprobacion> listaDtoReprobacion = new ArrayList<>();
        MateriasProgramaEducativo materias;
        Personal personal;
        DesercionPeriodosEscolares desercionPeriodosEscolares;
        DesercionReprobacionMaterias desercionReprobacionMaterias;
        DTOReprobacion dTOReprobacion;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(1);
        XSSFRow fila;
        
        String mat="";
      
        if (primeraHoja.getSheetName().equals("Deserción por Reprobación")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                
            if ((!"".equals(fila.getCell(0).getStringCellValue()))) {
                materias = new MateriasProgramaEducativo();
                personal = new Personal();
                desercionPeriodosEscolares = new DesercionPeriodosEscolares();
                desercionReprobacionMaterias = new DesercionReprobacionMaterias();
                dTOReprobacion = new DTOReprobacion();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        desercionPeriodosEscolares.setDpe(fila.getCell(0).getStringCellValue());
                        desercionReprobacionMaterias.setDpe(desercionPeriodosEscolares);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(1).getCellTypeEnum()) {
                     case FORMULA:
                        int m = (int)fila.getCell(1).getNumericCellValue();
                        mat =Integer.toString(m);
                        dTOReprobacion.setMatricula(mat);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                    case STRING:
                        materias.setNombre(fila.getCell(7).getStringCellValue());
                        break;
                    default:
                        break;
                }
                 
                switch (fila.getCell(8).getCellTypeEnum()) {
                    case FORMULA:
                        materias.setCveMateria(fila.getCell(8).getStringCellValue());
                        desercionReprobacionMaterias.setAsignatura(materias);
                        break;
                    default:
                        break;
                }
               
                switch (fila.getCell(9).getCellTypeEnum()) {
                    case STRING:
                        personal.setNombre(fila.getCell(9).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(10).getCellTypeEnum()) {
                    case FORMULA:
                        personal.setClave((int) fila.getCell(10).getNumericCellValue());
                        desercionReprobacionMaterias.setDocente(personal.getClave());
                        break;
                    default:
                        break;
                }
                    dTOReprobacion.setMaterias(materias);
                    dTOReprobacion.setPersonal(personal);
                    dTOReprobacion.setDesercionReprobacionMaterias(desercionReprobacionMaterias);
                    listaDtoReprobacion.add(dTOReprobacion);
                    
                }
            }
            listaDesercionReprobacion.setReprobacion(listaDtoReprobacion);
            libroRegistro.close();
            Messages.addGlobalInfo("<b>Hoja de Reprobación por Materia Validada favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaDesercionReprobacion;
    }
    
    
    @Override
    public void guardaDesercionReprobacion(ListaDesercionReprobacion listaDesercionReprobacion, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {

            listaDesercionReprobacion.getReprobacion().forEach((reprobacion) -> {

            DesercionPeriodosEscolares desercionPeriodosEscolares = ejbDesercionPeriodos.getRegistroDesercionClave(reprobacion.getDesercionReprobacionMaterias().getDpe());
            if (desercionPeriodosEscolares == null || desercionPeriodosEscolares.getDpe().isEmpty()) {
               Messages.addGlobalWarn("<b>No existe la Clave de Deserción Académica</b>");

            } else {
                List<String> listaCondicional = new ArrayList<>();
                f.setEntityClass(DesercionReprobacionMaterias.class);
                DesercionReprobacionMaterias desRepMatEncontrada = getRegistroDesercionReprobacionMaterias(reprobacion.getDesercionReprobacionMaterias());
                Boolean registroAlmacenado = false;

                if (desRepMatEncontrada != null) {
                    listaCondicional.add(reprobacion.getDesercionReprobacionMaterias().getDpe().getDpe() + " " + reprobacion.getMaterias().getNombre());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {

                    if (ejbModulos.validaPeriodoRegistro(ejbModulos.getPeriodoEscolarActual(), desercionPeriodosEscolares.getMatriculaPeriodosEscolares().getPeriodo())) {

                        reprobacion.getDesercionReprobacionMaterias().setRegistro(desRepMatEncontrada.getRegistro());
                        reprobacion.getDesercionReprobacionMaterias().getDpe().setRegistro(ejbDesercionPeriodos.getRegistroDesercionPeriodosEspecifico(reprobacion.getDesercionReprobacionMaterias().getDpe().getDpe()));
                        f.edit(reprobacion.getDesercionReprobacionMaterias());
                        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());

                    } else {
                        Messages.addGlobalWarn("<b>No se pueden actualizar los registros con los siguientes datos: </b> " + listaCondicional.toString() + " porque no son del periodo actual");
                    }
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    reprobacion.getDesercionReprobacionMaterias().getDpe().setRegistro(ejbDesercionPeriodos.getRegistroDesercionPeriodosEspecifico(reprobacion.getDesercionReprobacionMaterias().getDpe().getDpe()));
                    reprobacion.getDesercionReprobacionMaterias().setRegistro(registro.getRegistro());
                    f.create(reprobacion.getDesercionReprobacionMaterias());
                    Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b> ");
                }
                f.flush();
            }
        });
    }

    @Override
    public DesercionReprobacionMaterias getRegistroDesercionReprobacionMaterias(DesercionReprobacionMaterias desercionReprobacionMaterias) {
        TypedQuery<DesercionReprobacionMaterias> query = f.getEntityManager().createQuery("SELECT r FROM DesercionReprobacionMaterias r JOIN r.dpe d WHERE d.dpe = :dpe AND r.asignatura = :asignatura AND r.docente = :docente",  DesercionReprobacionMaterias.class);
        query.setParameter("dpe", desercionReprobacionMaterias.getDpe().getDpe());
        query.setParameter("asignatura", desercionReprobacionMaterias.getAsignatura());
        query.setParameter("docente", desercionReprobacionMaterias.getDocente());
        try {
            desercionReprobacionMaterias = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            desercionReprobacionMaterias = null;
//            System.out.println(ex.toString());
        }
        return desercionReprobacionMaterias;
    }

    @Override
    public List<DesercionReprobacionMaterias> getListaReprobacionDTO(String mes, Short ejercicio) {
        Short area = controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
        List<DesercionReprobacionMaterias> q = new ArrayList<>();
        
        if (area == 6 || area == 9) {
            q = f.getEntityManager().createQuery("SELECT v FROM DesercionReprobacionMaterias v WHERE v.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND v.registros.eventoRegistro.mes = :mes", DesercionReprobacionMaterias.class)
            .setParameter("mes", mes)
            .setParameter("ejercicio", ejercicio)
            .getResultList();
            
        } else {
           q = f.getEntityManager().createQuery("SELECT v FROM DesercionReprobacionMaterias v WHERE v.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND v.registros.eventoRegistro.mes = :mes AND v.registros.area = :area", DesercionReprobacionMaterias.class)
           .setParameter("mes", mes)
           .setParameter("ejercicio", ejercicio)
           .setParameter("area", area)
           .getResultList();
        }
        if (q.isEmpty() || q == null) {
            return null;
        } else {            
            return q;
        }
    }

  
    @Override
    public List<DTOReprobacion> getListaDtoReprobacion(String mes, Short ejercicio) {
        Short area = controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
        List<DesercionReprobacionMaterias> q = new ArrayList<>();
        
        if (area == 6 || area == 9) {
            q = f.getEntityManager().createQuery("SELECT v FROM DesercionReprobacionMaterias v WHERE v.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND v.registros.eventoRegistro.mes = :mes", DesercionReprobacionMaterias.class)
            .setParameter("mes", mes)
            .setParameter("ejercicio", ejercicio)
            .getResultList();
            
        } else {
           q = f.getEntityManager().createQuery("SELECT v FROM DesercionReprobacionMaterias v WHERE v.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND v.registros.eventoRegistro.mes = :mes AND v.registros.area = :area", DesercionReprobacionMaterias.class)
           .setParameter("mes", mes)
           .setParameter("ejercicio", ejercicio)
           .setParameter("area", area)
           .getResultList();
        }
        if (q.isEmpty() || q == null) {
            return null;
        } else {
            List<DTOReprobacion> ldto = new ArrayList<>();
            q.forEach(x->{
                MateriasProgramaEducativo m = f.getEntityManager().find(MateriasProgramaEducativo.class, x.getAsignatura().getCveMateria());
                Personal p = f.getEntityManager().find(Personal.class, x.getDocente());
                MatriculaPeriodosEscolares mat = f.getEntityManager().find(MatriculaPeriodosEscolares.class, x.getDpe().getMatriculaPeriodosEscolares().getRegistro());
                AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, mat.getProgramaEducativo());
                DTOReprobacion dto = new DTOReprobacion(mat.getMatricula(),
                    m,
                    p,
                    x,
                    au,
                    mat);
                ldto.add(dto);
            });            
            return ldto;
        }        
    }

    @Override
    public List<ListaDtoReprobacion> getListaRegistrosReprobacionDto(String mes, Short ejercicio) {
        Short area = controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
        List<DesercionReprobacionMaterias> q = new ArrayList<>();
        
        if (area == 6 || area == 9) {
            q = f.getEntityManager().createQuery("SELECT v FROM DesercionReprobacionMaterias v WHERE v.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND v.registros.eventoRegistro.mes = :mes", DesercionReprobacionMaterias.class)
            .setParameter("mes", mes)
            .setParameter("ejercicio", ejercicio)
            .getResultList();
            
        } else {
           q = f.getEntityManager().createQuery("SELECT v FROM DesercionReprobacionMaterias v WHERE v.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND v.registros.eventoRegistro.mes = :mes AND v.registros.area = :area", DesercionReprobacionMaterias.class)
           .setParameter("mes", mes)
           .setParameter("ejercicio", ejercicio)
           .setParameter("area", area)
           .getResultList();
        }
        if (q.isEmpty() || q == null) {
            return null;
        } else {
             TypedQuery<EventosRegistros> query = f.getEntityManager().createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            List<ListaDtoReprobacion> ldto = new ArrayList<>();
            q.forEach(x->{
                MateriasProgramaEducativo m = f.getEntityManager().find(MateriasProgramaEducativo.class, x.getAsignatura().getCveMateria());
                Personal p = f.getEntityManager().find(Personal.class, x.getDocente());
                Registros registro = f.getEntityManager().find(Registros.class, x.getRegistro());
                AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, registro.getArea());
                ActividadesPoa a =registro.getActividadesPoaList().isEmpty()?null:registro.getActividadesPoaList().get(0);
                if (eventoRegistro.equals(registro.getEventoRegistro())) {
                    ListaDtoReprobacion dto = new ListaDtoReprobacion(true, x.getDpe().getMatriculaPeriodosEscolares().getMatricula(), m, p, x, au, a);
                    ldto.add(dto);
                } else {
                    ListaDtoReprobacion dto = new ListaDtoReprobacion(false, x.getDpe().getMatriculaPeriodosEscolares().getMatricula(), m, p, x, au, a);
                    ldto.add(dto);
                } 
            });            
            return ldto;
        }
    }
    
    @Override
    public List<MateriasProgramaEducativo> getMateriasProgramaEducativoAct() {
        List<MateriasProgramaEducativo> genLst = new ArrayList<>();
        TypedQuery<MateriasProgramaEducativo> query = f.getEntityManager().createQuery("SELECT m FROM MateriasProgramaEducativo m WHERE m.activo = 1 ORDER BY m.cveMateria ASC", MateriasProgramaEducativo.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }

    @Override
    public List<DTOReprobacion> getListaMateriasReprobadas(String desercion) {
        if(desercion == null){
            return Collections.EMPTY_LIST;
        }
        List<DesercionReprobacionMaterias> entities = new ArrayList<>();
        List<DTOReprobacion> l = new ArrayList<>();
        
        entities = f.getEntityManager().createQuery("SELECT r FROM DesercionReprobacionMaterias r JOIN r.dpe d WHERE d.dpe = :desercion", DesercionReprobacionMaterias.class)
                .setParameter("desercion", desercion)
                .getResultList();
        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            Registros reg = f.getEntityManager().find(Registros.class, e.getRegistro());
            MateriasProgramaEducativo m = f.getEntityManager().find(MateriasProgramaEducativo.class, e.getAsignatura().getCveMateria());
            Personal p = f.getEntityManager().find(Personal.class, e.getDocente());
            MatriculaPeriodosEscolares mat = f.getEntityManager().find(MatriculaPeriodosEscolares.class, e.getDpe().getMatriculaPeriodosEscolares().getRegistro());
            AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, mat.getProgramaEducativo());

            l.add(new DTOReprobacion(
                    mat.getMatricula(),
                    m,
                    p,
                    e,
                    au,
                    mat));
        });
        
        return l;
    }

    
}
