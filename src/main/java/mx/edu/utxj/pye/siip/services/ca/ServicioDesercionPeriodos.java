/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausa;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasTipo;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.DesercionPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.DesercionReprobacionMaterias;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.escolar.DTODesercion;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaDesercionPeriodos;
import mx.edu.utxj.pye.siip.entity.pye.list.ListaDtoDesercion;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbDesercionPeriodos;
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
public class ServicioDesercionPeriodos implements EjbDesercionPeriodos{
    
    @EJB EjbModulos ejbModulos;
    @EJB EjbMatriculaPeriodosEscolares ejbMatriculaPeriodosEscolares;
    @EJB Facade f;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @Inject ControladorEmpleado controladorEmpleado;
    
    String genero;
   
    @Override
    public ListaDesercionPeriodos getListaDesercionPeriodos(String rutaArchivo) throws Throwable {
    
        ListaDesercionPeriodos listaDesercionPeriodos = new ListaDesercionPeriodos();

        List<DTODesercion> listaDtoDesercion = new ArrayList<>();
        BajasCausa bajasCausa;
        BajasTipo bajasTipo;
        Generaciones generaciones;
        MatriculaPeriodosEscolares matriculaPeriodosEscolares;
        DesercionPeriodosEscolares desercionPeriodosEscolares;
        DTODesercion dTODesercion;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        String mat = "";
       
        if (primeraHoja.getSheetName().equals("Deserción")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

            if ("".equals(fila.getCell(0).getStringCellValue())) {
            } else {
                bajasCausa = new BajasCausa();
                bajasTipo = new BajasTipo();
                generaciones = new Generaciones();
                matriculaPeriodosEscolares = new MatriculaPeriodosEscolares();
                desercionPeriodosEscolares = new DesercionPeriodosEscolares();
                dTODesercion = new DTODesercion();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case FORMULA:
                        desercionPeriodosEscolares.setDpe(fila.getCell(0).getStringCellValue());                  
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case STRING:
                        dTODesercion.setCicloEscolar(fila.getCell(2).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case STRING:
                        dTODesercion.setPeriodoEscolar(fila.getCell(4).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case FORMULA:
                        matriculaPeriodosEscolares.setPeriodo((int) fila.getCell(5).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case STRING:
                        dTODesercion.setGeneracion(fila.getCell(6).getStringCellValue());
                        break;
                    default:
                        break;
                }

                switch (fila.getCell(7).getCellTypeEnum()) {
                    case FORMULA:
                        generaciones.setGeneracion((short) fila.getCell(7).getNumericCellValue());
                        desercionPeriodosEscolares.setGeneracion(generaciones.getGeneracion());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(8).getCellTypeEnum()) {
                    case STRING:
                        matriculaPeriodosEscolares.setMatricula(fila.getCell(8).getStringCellValue());
                        desercionPeriodosEscolares.setMatriculaPeriodosEscolares(matriculaPeriodosEscolares);
                        break;
                    default:
                        break;
                }
                
                 switch (fila.getCell(9).getCellTypeEnum()) {
                    case STRING:
                       bajasCausa.setCausa(fila.getCell(9).getStringCellValue());
                        break;
                    default:
                        break;
                }
                  switch (fila.getCell(10).getCellTypeEnum()) {
                    case FORMULA:
                        bajasCausa.setCveCausa((int)fila.getCell(10).getNumericCellValue());
                        desercionPeriodosEscolares.setCausaBaja(bajasCausa.getCveCausa());
                        break;
                    default:
                        break;
                }
                   switch (fila.getCell(11).getCellTypeEnum()) {
                    case STRING:
                        bajasTipo.setDescripcion(fila.getCell(11).getStringCellValue());
                        break;
                    default:
                        break;
                }
                    switch (fila.getCell(12).getCellTypeEnum()) {
                    case FORMULA:
                        bajasTipo.setTipoBaja((int)fila.getCell(12).getNumericCellValue());
                        desercionPeriodosEscolares.setTipoBaja(bajasTipo.getTipoBaja());
                        break;
                    default:
                        break;
                }
                    dTODesercion.setBajasCausa(bajasCausa);
                    dTODesercion.setBajasTipo(bajasTipo);
                    dTODesercion.setDesercionPeriodosEscolares(desercionPeriodosEscolares);
                    listaDtoDesercion.add(dTODesercion);
                }
            }
            listaDesercionPeriodos.setDesercion(listaDtoDesercion);

            libroRegistro.close();
            Messages.addGlobalInfo("<b>Hoja de Deserción por Periodo Validada favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaDesercionPeriodos;
        
    }
    
    
    @Override
    public void guardaDesercionPeriodos (ListaDesercionPeriodos listaDesercionPeriodos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {

            List<String> listaCondicional = new ArrayList<>();
            listaDesercionPeriodos.getDesercion().forEach((desercion) -> {
            if (ejbModulos.validaPeriodoRegistro(ejbModulos.getPeriodoEscolarActual(), desercion.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().getPeriodo())) {
            f.setEntityClass(DesercionPeriodosEscolares.class);
            DesercionPeriodosEscolares derPerEscEncontrada = getRegistroDesercionPeriodosEscolares(desercion.getDesercionPeriodosEscolares());
            Boolean registroAlmacenado = false;

            if (derPerEscEncontrada != null) {
                listaCondicional.add(desercion.getPeriodoEscolar()+ " " + desercion.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().getMatricula());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                desercion.getDesercionPeriodosEscolares().setRegistro(derPerEscEncontrada.getRegistro());
                desercion.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(desercion.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().getMatricula(), desercion.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().getPeriodo()));
                f.edit(desercion.getDesercionPeriodosEscolares());
                Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                desercion.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(desercion.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().getMatricula(), desercion.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().getPeriodo()));
                desercion.getDesercionPeriodosEscolares().setRegistro(registro.getRegistro());
                f.create(desercion.getDesercionPeriodosEscolares());
                Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b> ");
            }
            f.flush();
            }else{
                Messages.addGlobalWarn("<b>No puede registrar información de periodos anteriores </b> ");
            }
        });
    }
    
    @Override
    public Integer getRegistroDesercionPeriodosEspecifico(String dpe) {
        TypedQuery<DesercionPeriodosEscolares> query = f.getEntityManager().createNamedQuery("DesercionPeriodosEscolares.findByDpe", DesercionPeriodosEscolares.class);
        query.setParameter("dpe", dpe);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }

    @Override
    public DesercionPeriodosEscolares getRegistroDesercionPeriodosEscolares(DesercionPeriodosEscolares desercionPeriodosEscolares) {
        TypedQuery<DesercionPeriodosEscolares> query = f.getEntityManager().createQuery("SELECT d FROM DesercionPeriodosEscolares d JOIN d.matriculaPeriodosEscolares m WHERE m.periodo = :periodoEscolar AND m.matricula = :matricula", DesercionPeriodosEscolares.class);
        query.setParameter("periodoEscolar", desercionPeriodosEscolares.getMatriculaPeriodosEscolares().getPeriodo());
        query.setParameter("matricula", desercionPeriodosEscolares.getMatriculaPeriodosEscolares().getMatricula());
        try {
            desercionPeriodosEscolares = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            desercionPeriodosEscolares = null;
//            System.out.println(ex.toString());
        }
        return desercionPeriodosEscolares;
    }
    
    @Override
    public DesercionPeriodosEscolares getRegistroDesercionClave(DesercionPeriodosEscolares desercionPeriodosEscolares) {
        TypedQuery<DesercionPeriodosEscolares> query = f.getEntityManager().createQuery("SELECT d FROM DesercionPeriodosEscolares d WHERE d.dpe = :dpe", DesercionPeriodosEscolares.class);
        query.setParameter("dpe", desercionPeriodosEscolares.getDpe());
        try {
            desercionPeriodosEscolares = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            desercionPeriodosEscolares = null;
//            System.out.println(ex.toString());
        }
        return desercionPeriodosEscolares;
    }
    
    @Override
    public List<DesercionPeriodosEscolares> getListaDesercionDTO(String mes, Short ejercicio) {
        Short area = controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
        List<DesercionPeriodosEscolares> entities = new ArrayList<>();
        
        if (area == 6 || area == 9) {
            entities = f.getEntityManager().createQuery("SELECT v FROM DesercionPeriodosEscolares v WHERE v.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND v.registros.eventoRegistro.mes = :mes", DesercionPeriodosEscolares.class)
                    .setParameter("mes", mes)
                    .setParameter("ejercicio", ejercicio)
                    .getResultList();
        } else {
            entities = f.getEntityManager().createQuery("SELECT v FROM DesercionPeriodosEscolares v WHERE v.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND v.registros.eventoRegistro.mes = :mes AND v.registros.area = :area", DesercionPeriodosEscolares.class)
                    .setParameter("mes", mes)
                    .setParameter("ejercicio", ejercicio)
                    .setParameter("area", area)
                    .getResultList();

        }
        if (entities.isEmpty() || entities == null) {
            return null;
        } else {
            return entities;
        }
    }

    @Override
    public List<DTODesercion> getListaDtoDesercion(String mes, Short ejercicio) {
        Short area = controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
        List<DesercionPeriodosEscolares> entities = new ArrayList<>();
        
        if (area == 6 || area == 9) {
            entities = f.getEntityManager().createQuery("SELECT v FROM DesercionPeriodosEscolares v WHERE v.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND v.registros.eventoRegistro.mes = :mes", DesercionPeriodosEscolares.class)
                    .setParameter("mes", mes)
                    .setParameter("ejercicio", ejercicio)
                    .getResultList();
        } else {
            entities = f.getEntityManager().createQuery("SELECT v FROM DesercionPeriodosEscolares v WHERE v.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND v.registros.eventoRegistro.mes = :mes AND v.registros.area = :area", DesercionPeriodosEscolares.class)
                    .setParameter("mes", mes)
                    .setParameter("ejercicio", ejercicio)
                    .setParameter("area", area)
                    .getResultList();

        }
        if (entities.isEmpty() || entities == null) {
            return null;
        } else {
            List<DTODesercion> ldto = new ArrayList<>();
//            l.forEach(System.err::println);
            entities.forEach(x -> {
                PeriodosEscolares p = f.getEntityManager().find(PeriodosEscolares.class, x.getMatriculaPeriodosEscolares().getPeriodo());
                BajasCausa bc = f.getEntityManager().find(BajasCausa.class, x.getCausaBaja());
                BajasTipo bt = f.getEntityManager().find(BajasTipo.class, x.getTipoBaja());
                Generaciones g = f.getEntityManager().find(Generaciones.class, x.getGeneracion());
                Short inicio = g.getInicio();
                Short fin = g.getFin();
                Caster caster = new Caster();
                String periodo = caster.periodoToString(p);
                String strDateFormat = "yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                String generacion = String.valueOf(inicio) + "-"+ String.valueOf(fin);
                String cicloe = sdf.format(p.getCiclo().getInicio()) + " - " + sdf.format(p.getCiclo().getFin());
                DTODesercion dto = new DTODesercion(cicloe, periodo, generacion, bc, bt, x);
                ldto.add(dto);
            });
            return ldto;
        }
    }

    @Override
    public List<ListaDtoDesercion> getListaRegistrosDesercionDto(String mes, Short ejercicio) {
        
        Short area = controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
        List<DesercionPeriodosEscolares> entities = new ArrayList<>();
        
        if (area == 6 || area == 9) {
            entities = f.getEntityManager().createQuery("SELECT v FROM DesercionPeriodosEscolares v WHERE v.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND v.registros.eventoRegistro.mes = :mes", DesercionPeriodosEscolares.class)
                    .setParameter("mes", mes)
                    .setParameter("ejercicio", ejercicio)
                    .setFirstResult(0)
                    .setMaxResults(100)
                    .getResultList();
        } else {
            entities = f.getEntityManager().createQuery("SELECT v FROM DesercionPeriodosEscolares v WHERE v.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND v.registros.eventoRegistro.mes = :mes AND v.registros.area = :area", DesercionPeriodosEscolares.class)
                    .setParameter("mes", mes)
                    .setParameter("ejercicio", ejercicio)
                    .setParameter("area", area)
                    .setFirstResult(0)
                    .setMaxResults(100)
                    .getResultList();

        }
        if (entities.isEmpty() || entities == null) {
            return null;
        } else {
            List<ListaDtoDesercion> ldto = new ArrayList<>();
             TypedQuery<EventosRegistros> query = f.getEntityManager().createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            
            entities.forEach(x -> {
                PeriodosEscolares p = f.getEntityManager().find(PeriodosEscolares.class, x.getMatriculaPeriodosEscolares().getPeriodo());
                BajasCausa bc = f.getEntityManager().find(BajasCausa.class, x.getCausaBaja());
                BajasTipo bt = f.getEntityManager().find(BajasTipo.class, x.getTipoBaja());
                Generaciones g = f.getEntityManager().find(Generaciones.class, x.getGeneracion());
                Short inicio = g.getInicio();
                Short fin = g.getFin();
                Caster caster = new Caster();
                String periodo = caster.periodoToString(p);
                String strDateFormat = "yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                String generacion = String.valueOf(inicio) + "-"+ String.valueOf(fin);
                String cicloe = sdf.format(p.getCiclo().getInicio()) + " - " + sdf.format(p.getCiclo().getFin());
                Registros registro = f.getEntityManager().find(Registros.class, x.getRegistro());
                MatriculaPeriodosEscolares mat = f.getEntityManager().find(MatriculaPeriodosEscolares.class, x.getMatriculaPeriodosEscolares().getRegistro());
                genero = mat.getCurp().substring(10, 11);
                AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class,  mat.getProgramaEducativo());
                ActividadesPoa a =  registro.getActividadesPoaList().isEmpty()?null: registro.getActividadesPoaList().get(0);
                if (eventoRegistro.equals(registro.getEventoRegistro())) {
                    ListaDtoDesercion dto = new ListaDtoDesercion(true, cicloe, periodo, generacion, bc, bt, x, mat, genero, au, a);
                    ldto.add(dto);
                } else {
                    ListaDtoDesercion dto = new ListaDtoDesercion(false, cicloe, periodo, generacion, bc, bt, x, mat, genero, au, a);
                    ldto.add(dto);
                }   
            });
            return ldto;
        }
    }
    
    @Override
    public List<Integer> buscaRegistroReprobacionDesercionAcademica(String clave) throws Throwable {
          
       List<Integer> registros = new ArrayList<>();
        try {
            registros = f.getEntityManager().createQuery("SELECT d FROM DesercionReprobacionMaterias d WHERE d.dpe.dpe= :clave", DesercionReprobacionMaterias.class)
                    .setParameter("clave", clave)
                    .getResultStream()
                    .map(s -> s.getRegistro())
                    .collect(Collectors.toList());
            return registros;
            
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<Integer> buscaRegistroEvidenciasDesercionRep(String clave) throws Throwable {
        List<Integer> registros = new ArrayList<>();
        List<Integer> evidencias = new ArrayList<>();
        try {
            registros = f.getEntityManager().createQuery("SELECT d FROM DesercionReprobacionMaterias d WHERE d.dpe.dpe= :clave", DesercionReprobacionMaterias.class)
                    .setParameter("clave", clave)
                    .getResultStream()
                    .map(s -> s.getRegistro())
                    .collect(Collectors.toList());
           
            registros.stream().forEach((reg)-> {
		
                List<Integer> evidenciasReg = f.getEntityManager().createQuery("SELECT e FROM Evidencias e INNER JOIN e.registrosList r WHERE r.registro = :registro",  Evidencias.class)
                    .setParameter("registro", reg)
                    .getResultStream()
                    .map(s -> s.getEvidencia())
                    .collect(Collectors.toList());
              
                    evidenciasReg.stream().forEach((evidencia)-> {
                     
                     evidencias.add(evidencia);
                     
                 });
               
            });
           
                return evidencias;
            
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<ListaDtoDesercion> getRegistroDesercion(Short ejercicio) {
        List<DesercionPeriodosEscolares> entities = new ArrayList<>();

        entities = f.getEntityManager().createQuery("SELECT v FROM DesercionPeriodosEscolares v INNER JOIN v.registros r INNER JOIN r.eventoRegistro er WHERE er.ejercicioFiscal.ejercicioFiscal = :ejercicio", DesercionPeriodosEscolares.class)
                .setParameter("ejercicio", ejercicio)
                .getResultList();

        if (entities.isEmpty() || entities == null) {
            return null;
        } else {
            List<ListaDtoDesercion> ldto = new ArrayList<>();
             TypedQuery<EventosRegistros> query = f.getEntityManager().createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            
            entities.forEach(x -> {
                PeriodosEscolares p = f.getEntityManager().find(PeriodosEscolares.class, x.getMatriculaPeriodosEscolares().getPeriodo());
                BajasCausa bc = f.getEntityManager().find(BajasCausa.class, x.getCausaBaja());
                BajasTipo bt = f.getEntityManager().find(BajasTipo.class, x.getTipoBaja());
                Generaciones g = f.getEntityManager().find(Generaciones.class, x.getGeneracion());
                Short inicio = g.getInicio();
                Short fin = g.getFin();
                Caster caster = new Caster();
                String periodo = caster.periodoToString(p);
                String strDateFormat = "yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                String generacion = String.valueOf(inicio) + "-"+ String.valueOf(fin);
                String cicloe = sdf.format(p.getCiclo().getInicio()) + " - " + sdf.format(p.getCiclo().getFin());
                Registros registro = f.getEntityManager().find(Registros.class, x.getRegistro());
                MatriculaPeriodosEscolares mat = f.getEntityManager().find(MatriculaPeriodosEscolares.class, x.getMatriculaPeriodosEscolares().getRegistro());
                genero = mat.getCurp().substring(10, 11);
                AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class,  mat.getProgramaEducativo());
                ActividadesPoa a =  registro.getActividadesPoaList().isEmpty()?null: registro.getActividadesPoaList().get(0);
                if (eventoRegistro.equals(registro.getEventoRegistro())) {
                    ListaDtoDesercion dto = new ListaDtoDesercion(true, cicloe, periodo, generacion, bc, bt, x, mat, genero, au, a);
                    ldto.add(dto);
                } else {
                    ListaDtoDesercion dto = new ListaDtoDesercion(false, cicloe, periodo, generacion, bc, bt, x, mat, genero, au, a);
                    ldto.add(dto);
                }   
            });
            return ldto;
        }
    }
}

