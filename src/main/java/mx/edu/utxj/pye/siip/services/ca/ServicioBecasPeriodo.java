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
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.BecaTipos;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.BecasPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.escolar.DTOBecasPeriodosEscolares;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaBecasDto;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaBecasPeriodo;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbBecasPeriodo;
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
public class ServicioBecasPeriodo implements EjbBecasPeriodo{
    
 
    @EJB EjbModulos ejbModulos;
    @EJB EjbMatriculaPeriodosEscolares ejbMatriculaPeriodosEscolares;
    @EJB Facade f;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @Inject ControladorEmpleado controladorEmpleado;
   
    @Getter @Setter private List<Short> areas;
    
    String genero;
    
    @Override
    public ListaBecasPeriodo getListaBecasPeriodo(String rutaArchivo) throws Throwable {
    
        ListaBecasPeriodo listaBecasPeriodo = new ListaBecasPeriodo();

        List<DTOBecasPeriodosEscolares> listaDtoBecasPeriodo = new ArrayList<>();
        BecaTipos becaTipos;
        MatriculaPeriodosEscolares matriculaPeriodosEscolar;
        BecasPeriodosEscolares becasPeriodosEscolares;
        DTOBecasPeriodosEscolares dTOBecasPeriodosEscolares;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
       
        Integer matricula = 0;
        String matriculaNueva = "000000";
        
        if (primeraHoja.getSheetName().equals("Becas")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

            if ((!"".equals(fila.getCell(0).getStringCellValue()))) {
                becaTipos = new BecaTipos();
                matriculaPeriodosEscolar = new MatriculaPeriodosEscolares();
                becasPeriodosEscolares = new BecasPeriodosEscolares();
                dTOBecasPeriodosEscolares = new DTOBecasPeriodosEscolares();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case STRING:
                        dTOBecasPeriodosEscolares.setCicloEscolar(fila.getCell(0).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case STRING:
                        dTOBecasPeriodosEscolares.setPeriodoAsignacion(fila.getCell(2).getStringCellValue());
                        break;
                    default:
                        break;
                }
              
                switch (fila.getCell(3).getCellTypeEnum()) {
                    case FORMULA:
                        matriculaPeriodosEscolar.setPeriodo((int) fila.getCell(3).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                     case STRING:
                            matriculaPeriodosEscolar.setMatricula(fila.getCell(4).getStringCellValue());
                            break;
                        default:
                            break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case STRING:
                        becaTipos.setNombre(fila.getCell(5).getStringCellValue());
                        break;
                    default:
                        break;
                }

                switch (fila.getCell(6).getCellTypeEnum()) {
                    case FORMULA:
                        becaTipos.setBecaTipo((short) fila.getCell(6).getNumericCellValue());
                        becasPeriodosEscolares.setBeca(becaTipos.getBecaTipo());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(7).getCellTypeEnum()) {
                    case STRING:
                        becasPeriodosEscolares.setSolicitud(fila.getCell(7).getStringCellValue());
                       
                        break;
                    default:
                        break;
                }
               
                becasPeriodosEscolares.setMatriculaPeriodosEscolares(matriculaPeriodosEscolar);
                dTOBecasPeriodosEscolares.setBecaTipos(becaTipos);
                dTOBecasPeriodosEscolares.setBecasPeriodosEscolares(becasPeriodosEscolares);

                listaDtoBecasPeriodo.add(dTOBecasPeriodosEscolares);
                
            }
         }
            listaBecasPeriodo.setBecasPeriodosEscolares(listaDtoBecasPeriodo);

            libroRegistro.close();
            Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaBecasPeriodo;
        
    }
    
    
   @Override
    public void guardaBecasPeriodo(ListaBecasPeriodo listaBecasPeriodo, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {

        List<String> listaCondicional = new ArrayList<>();
        listaBecasPeriodo.getBecasPeriodosEscolares().forEach((becasPeriodosEscolares) -> {
            if (ejbModulos.validaPeriodoRegistro(ejbModulos.getPeriodoEscolarActivo(), becasPeriodosEscolares.getBecasPeriodosEscolares().getMatriculaPeriodosEscolares().getPeriodo())) {
            f.setEntityClass(BecasPeriodosEscolares.class);
            BecasPeriodosEscolares becasperescEncontrada = getRegistroBecasPeriodosEscolares(becasPeriodosEscolares.getBecasPeriodosEscolares());
            Boolean registroAlmacenado = false;

            if (becasperescEncontrada != null) {
                listaCondicional.add(becasPeriodosEscolares.getPeriodoAsignacion() + " " + becasPeriodosEscolares.getBecasPeriodosEscolares().getMatriculaPeriodosEscolares().getMatricula());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                becasPeriodosEscolares.getBecasPeriodosEscolares().setRegistro(becasperescEncontrada.getRegistro());
                becasPeriodosEscolares.getBecasPeriodosEscolares().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(becasPeriodosEscolares.getBecasPeriodosEscolares().getMatriculaPeriodosEscolares().getMatricula(), becasPeriodosEscolares.getBecasPeriodosEscolares().getMatriculaPeriodosEscolares().getPeriodo()));
                f.edit(becasPeriodosEscolares.getBecasPeriodosEscolares());
                Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                becasPeriodosEscolares.getBecasPeriodosEscolares().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(becasPeriodosEscolares.getBecasPeriodosEscolares().getMatriculaPeriodosEscolares().getMatricula(), becasPeriodosEscolares.getBecasPeriodosEscolares().getMatriculaPeriodosEscolares().getPeriodo()));
                becasPeriodosEscolares.getBecasPeriodosEscolares().setRegistro(registro.getRegistro());
                f.create(becasPeriodosEscolares.getBecasPeriodosEscolares());
                Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b> ");
            }
            f.flush();
            }else{
                Messages.addGlobalWarn("<b>No puede registrar información de periodos anteriores </b> ");
            }
        });
    }

    @Override
    public BecasPeriodosEscolares getRegistroBecasPeriodosEscolares(BecasPeriodosEscolares becasPeriodosEscolares) {
        BecasPeriodosEscolares becasperescEncontrada = new BecasPeriodosEscolares();
        TypedQuery<BecasPeriodosEscolares> query = f.getEntityManager().createQuery("SELECT b FROM BecasPeriodosEscolares b JOIN b.matriculaPeriodosEscolares m WHERE m.periodo = :periodoAsignacion AND m.matricula = :matricula AND b.beca = :beca", BecasPeriodosEscolares.class);
        query.setParameter("periodoAsignacion", becasPeriodosEscolares.getMatriculaPeriodosEscolares().getPeriodo());
        query.setParameter("matricula", becasPeriodosEscolares.getMatriculaPeriodosEscolares().getMatricula());
        query.setParameter("beca", becasPeriodosEscolares.getBeca());
        try {
            becasperescEncontrada = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            becasperescEncontrada = null;
//            System.out.println(ex.toString());
        }
        return becasperescEncontrada;
    }
   
    @Override
    public List<ListaBecasDto> getRegistroBecas(String mes, Short ejercicio) {
        //verificar que los parametros no sean nulos
        if(mes == null || ejercicio == null){
            return null;
        }
        Short area = controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
        List<BecasPeriodosEscolares> q = new ArrayList<>();
        List<ListaBecasDto> ldto = new ArrayList<>();
        
        if (area == 6 || area == 9) {

             q = f.getEntityManager().createQuery("SELECT a from BecasPeriodosEscolares a WHERE a.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND a.registros.eventoRegistro.mes = :mes", BecasPeriodosEscolares.class)
                    .setParameter("mes", mes)
                    .setParameter("ejercicio", ejercicio)
                    .setFirstResult(0)
                    .setMaxResults(100)
                    .getResultList();

        } else {
            areas = ejbModulos.getAreasDependientes(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());

            q = f.getEntityManager().createQuery("SELECT a from BecasPeriodosEscolares a WHERE a.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND a.registros.eventoRegistro.mes = :mes AND a.registros.area IN :areas", BecasPeriodosEscolares.class)
                    .setParameter("mes", mes)
                    .setParameter("ejercicio", ejercicio)
                    .setParameter("areas", areas)
                    .setFirstResult(0)
                    .setMaxResults(100)
                    .getResultList();
        }
        
        if (q.isEmpty() || q == null) {
            return null;
        } else {
            TypedQuery<EventosRegistros> query = f.getEntityManager().createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            q.forEach(x -> {
                PeriodosEscolares p = f.getEntityManager().find(PeriodosEscolares.class, x.getMatriculaPeriodosEscolares().getPeriodo());
                Caster caster = new Caster();
                String periodoAsignacion = caster.periodoToString(p);
                String strDateFormat = "yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                String cicloEscolar = sdf.format(p.getCiclo().getInicio()) + " - " + sdf.format(p.getCiclo().getFin());
                BecaTipos becaTipos = f.getEntityManager().find(BecaTipos.class, x.getBeca());

                Registros registro = f.getEntityManager().find(Registros.class, x.getRegistro());
                MatriculaPeriodosEscolares mat = f.getEntityManager().find(MatriculaPeriodosEscolares.class, x.getMatriculaPeriodosEscolares().getRegistro());
                genero = mat.getCurp().substring(10, 11);
                
                AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, mat.getProgramaEducativo());
                ActividadesPoa a = registro.getActividadesPoaList().isEmpty() ? null :registro.getActividadesPoaList().get(0);
                ListaBecasDto dto;
                if (eventoRegistro.equals(registro.getEventoRegistro())) {
                    dto = new ListaBecasDto(Boolean.TRUE, cicloEscolar, periodoAsignacion, becaTipos, x, mat, genero, au, a);
                } else {
                    dto = new ListaBecasDto(Boolean.FALSE, cicloEscolar, periodoAsignacion, becaTipos, x, mat, genero, au, a);
                }
                ldto.add(dto);
            });
            return ldto;
        }
    }

    @Override
    public List<ListaBecasDto> getRegistroReporteBecas(Short ejercicioFiscal) {
//        TODO: Verificar el ejercicio
        List<BecasPeriodosEscolares> q = new ArrayList<>();
        List<ListaBecasDto> ldto = new ArrayList<>();
        
         q = f.getEntityManager().createQuery("SELECT a from BecasPeriodosEscolares a INNER JOIN a.registros r INNER JOIN r.eventoRegistro er WHERE er.ejercicioFiscal.ejercicioFiscal = :ejercicioFiscal", BecasPeriodosEscolares.class)
                .setParameter("ejercicioFiscal", ejercicioFiscal)
                 .getResultList();

        if (q.isEmpty() || q == null) {
            return null;
        } else {
            TypedQuery<EventosRegistros> query = f.getEntityManager().createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            q.forEach(x -> {
                PeriodosEscolares p = f.getEntityManager().find(PeriodosEscolares.class, x.getMatriculaPeriodosEscolares().getPeriodo());
                Caster caster = new Caster();
                String periodoAsignacion = caster.periodoToString(p);
                String strDateFormat = "yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                String cicloEscolar = sdf.format(p.getCiclo().getInicio()) + " - " + sdf.format(p.getCiclo().getFin());
                BecaTipos becaTipos = f.getEntityManager().find(BecaTipos.class, x.getBeca());

                Registros registro = f.getEntityManager().find(Registros.class, x.getRegistro());
                MatriculaPeriodosEscolares mat = f.getEntityManager().find(MatriculaPeriodosEscolares.class, x.getMatriculaPeriodosEscolares().getRegistro());
                genero = mat.getCurp().substring(10, 11);

                AreasUniversidad au = f.getEntityManager().find(AreasUniversidad.class, mat.getProgramaEducativo());
                ActividadesPoa a = registro.getActividadesPoaList().isEmpty() ? null :registro.getActividadesPoaList().get(0);
                ListaBecasDto dto;
                if (eventoRegistro.equals(registro.getEventoRegistro())) {
                    dto = new ListaBecasDto(Boolean.TRUE, cicloEscolar, periodoAsignacion, becaTipos, x, mat, genero, au, a);
                } else {
                    dto = new ListaBecasDto(Boolean.FALSE, cicloEscolar, periodoAsignacion, becaTipos, x, mat, genero, au, a);
                }
                ldto.add(dto);
            });
            return ldto;
        }
    }

}
