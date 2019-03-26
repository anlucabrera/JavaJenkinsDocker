/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.VisitasIndustriales;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOVisitas;
import mx.edu.utxj.pye.siip.entity.pye.list.ListaDtoVisitasIndustriales;
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
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioVisitasIndustriales implements EjbVisitasIndustriales{
   
    @EJB EjbModulos ejbModulos;
    @EJB EjbOrganismosVinculados ejbOrganismosVinculados;
    @EJB Facade f;
    @Inject ControladorEmpleado controladorEmpleado;
    
    @Getter @Setter private List<Short> areas;
   
    @Override
    public ListaVisitasIndustriales getListaVisitasIndustriales(String rutaArchivo) throws Throwable {
        List<Boolean> validarCelda = new ArrayList<>();
        List<String> datosInvalidos = new ArrayList<>();
       
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
       Integer cuat = 0;
       int obj = 0;
       
       try{
       if (primeraHoja.getSheetName().equals("Visitas Industriales")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

            if ((!"".equals(fila.getCell(13).getStringCellValue()))) {
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
                switch (fila.getCell(8).getCellTypeEnum()) {
                    case STRING:
                        String c = fila.getCell(8).getStringCellValue();
                        cuat = Integer.parseInt(c);
                        visitasIndustriales.setCuatrimestre(cuat);
                        break;
                    case NUMERIC:
                        visitasIndustriales.setCuatrimestre((int) fila.getCell(8).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(9).getCellTypeEnum()) {
                    case STRING:
                         String g1 = fila.getCell(9).getStringCellValue();
                         grup = g1.charAt(0);
                         visitasIndustriales.setGrupo(grup);
                         break;
                    default:
                        break;
                }
                switch (fila.getCell(10).getCellTypeEnum()) {
                    case STRING:
                        visitasIndustriales.setSistema(fila.getCell(10).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(11).getCellTypeEnum()) {
                    case STRING:
                        organismosVinculados.setNombre(fila.getCell(11).getStringCellValue());
                    default:
                        break;
                }
                switch (fila.getCell(12).getCellTypeEnum()) {
                    case FORMULA:
                        organismosVinculados.setEmpresa((int) fila.getCell(12).getNumericCellValue());
                        visitasIndustriales.setEmpresa(organismosVinculados);
                    default:
                        break;
                }
                switch (fila.getCell(14).getCellTypeEnum()) {
                    case STRING:
                         visitasIndustriales.setObjetivo(fila.getCell(14).getStringCellValue());
                    default:
                        break;
                }
                switch (fila.getCell(15).getCellTypeEnum()) {
                    case NUMERIC:
                        visitasIndustriales.setHombres((int) fila.getCell(15).getNumericCellValue());
                        break;
                    default:
                        break;
                }

                switch (fila.getCell(16).getCellTypeEnum()) {
                    case NUMERIC:
                         visitasIndustriales.setMujeres((int) fila.getCell(16).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(19).getCellTypeEnum()) {
                    case FORMULA:
                        obj = (int) fila.getCell(19).getNumericCellValue();
                        break;
                    default:
                        break;
                }

                if (obj == 1) {
                    validarCelda.add(false);
                    datosInvalidos.add("Dato incorrecto: Objetivo en la columna: " + (8 + 1) + " y fila: " + (i + 1) + " \n ");
                }
                
               dTOVisitas.setAreasUniversidad(areaUniversidad);
               dTOVisitas.setVisitasIndustriales(visitasIndustriales);
                    
               listaDtoVisitas.add(dTOVisitas);
            }
            }
                listaVisitasIndustriales.setVisitas(listaDtoVisitas);
                libroRegistro.close();
                if (validarCelda.contains(false)) {
                    Messages.addGlobalError("<b>El archivo cargado contiene datos que no son válidos, verifique los datos de la plantilla</b>");
                    Messages.addGlobalError(datosInvalidos.toString());
                    excel.delete();
                    ServicioArchivos.eliminarArchivo(rutaArchivo);

                    return null;
                } else {
                    Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
                    return listaVisitasIndustriales;
                }
            } else {
                libroRegistro.close();
                excel.delete();
                ServicioArchivos.eliminarArchivo(rutaArchivo);
                Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
                return null;
            }
        } catch (IOException e) {
            libroRegistro.close();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            Messages.addGlobalError("<b>Ocurrió un error durante la lectura del archivo, asegurese de haber registrado correctamente su información</b>");
            return null;
        }

    }

    @Override
    public void guardaVisitasIndustriales(ListaVisitasIndustriales listaVisitasIndustriales, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            listaVisitasIndustriales.getVisitas().forEach((visitas) -> {
            f.setEntityClass(VisitasIndustriales.class);
            VisitasIndustriales visIndEncontrada = getRegistroVisitasIndustriales(visitas.getVisitasIndustriales());
            Boolean registroAlmacenado = false;

            if (visIndEncontrada != null) {
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                Date fec = visIndEncontrada.getFecha();
                LocalDate fecha = fec.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String fechaVis = fecha.format(formatter);
                
                if (ejbModulos.validaEventoRegistro(ejbModulos.getEventoRegistro(), visIndEncontrada.getRegistros().getEventoRegistro().getEventoRegistro())) {
                visitas.getVisitasIndustriales().setRegistro(visIndEncontrada.getRegistro());
                visitas.getVisitasIndustriales().getEmpresa().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(visitas.getVisitasIndustriales().getEmpresa().getEmpresa()));
                f.edit(visitas.getVisitasIndustriales());
                Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + visIndEncontrada.getEmpresa().getNombre()+" - "+ fechaVis);
                } else {
                    Messages.addGlobalWarn("<b>No se pueden actualizar los registros con los siguientes datos: </b> " + visIndEncontrada.getEmpresa().getNombre()+" - "+ fechaVis);
                }
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                visitas.getVisitasIndustriales().getEmpresa().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(visitas.getVisitasIndustriales().getEmpresa().getEmpresa()));
                visitas.getVisitasIndustriales().setRegistro(registro.getRegistro());
                f.create(visitas.getVisitasIndustriales());
                Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b> ");
            }
            f.flush();
        });
    }

    @Override
    public VisitasIndustriales getRegistroVisitasIndustriales(VisitasIndustriales visitasIndustriales) {
        TypedQuery<VisitasIndustriales> query = f.getEntityManager().createQuery("SELECT v FROM VisitasIndustriales v JOIN v.empresa e WHERE v.cicloEscolar = :cicloEscolar AND v.periodoEscolar = :periodoEscolar AND v.fecha = :fecha AND v.programaEducativo = :programaEducativo AND v.cuatrimestre = :cuatrimestre AND v.grupo = :grupo AND e.empresa = :empresa", VisitasIndustriales.class);
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
//            System.out.println(ex.toString());
        }
        return visitasIndustriales;
    }
    
    @Override
    public List<VisitasIndustriales> getVisitasIndustrialesRegistrosPorEjercicioMesArea(String mes, Short ejercicio) {
        //verificar que los parametros no sean nulos
        if(mes == null || ejercicio == null){
            return null;
        }
        areas = ejbModulos.getAreasDependientes(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
        
        TypedQuery<VisitasIndustriales> q = f.getEntityManager()
                .createQuery("SELECT v FROM VisitasIndustriales v WHERE v.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND v.registros.eventoRegistro.mes = :mes AND v.registros.area IN :areas", VisitasIndustriales.class);
        q.setParameter("mes", mes);
        q.setParameter("ejercicio", ejercicio);
        q.setParameter("areas", areas);
        System.err.println("getVisitasIndustrialesRegistrosPorEjercicioMesArea " + q.getResultList());
        List<VisitasIndustriales> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            getListaVisitasIndutrialesDTO(mes, ejercicio);
            return l;
        }
    }

    @Override
    public List<ListaDtoVisitasIndustriales> getListaVisitasIndutrialesDTO(String mes, Short ejercicio) {
        //verificar que los parametros no sean nulos
        if(mes == null || ejercicio == null){
            return null;
        }
        Short area = controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
        List<VisitasIndustriales> q = new ArrayList<>();
        List<ListaDtoVisitasIndustriales> ldto = new ArrayList<>();
        
        if (area == 6 || area == 9) {

            q = f.getEntityManager().createQuery("SELECT v FROM VisitasIndustriales v WHERE v.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND v.registros.eventoRegistro.mes = :mes", VisitasIndustriales.class)
                    .setParameter("mes", mes)
                    .setParameter("ejercicio", ejercicio)
                    .getResultList();

        } else {
            areas = ejbModulos.getAreasDependientes(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());

            q = f.getEntityManager().createQuery("SELECT v FROM VisitasIndustriales v WHERE v.registros.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio AND v.registros.eventoRegistro.mes = :mes AND v.registros.area IN :areas", VisitasIndustriales.class)
                    .setParameter("mes", mes)
                    .setParameter("ejercicio", ejercicio)
                    .setParameter("areas", areas)
                    .getResultList();

        }
        if (q.isEmpty() || q == null) {
            return null;
        } else {
            TypedQuery<EventosRegistros> query = f.getEntityManager().createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            q.forEach(x -> {
                PeriodosEscolares p = f.getEntityManager().find(PeriodosEscolares.class, x.getPeriodoEscolar());
                CiclosEscolares c = f.getEntityManager().find(CiclosEscolares.class, x.getCicloEscolar());
                Caster caster = new Caster();
                String periodo = caster.periodoToString(p);
                String strDateFormat = "yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                String cicloe = sdf.format(c.getInicio()) + " - " + sdf.format(c.getFin());
                Registros registro = f.getEntityManager().find(Registros.class, x.getRegistro());
                AreasUniversidad a = f.getEntityManager().find(AreasUniversidad.class, x.getProgramaEducativo());
                ActividadesPoa ap = registro.getActividadesPoaList().isEmpty()?null:registro.getActividadesPoaList().get(0);
                if (eventoRegistro.equals(registro.getEventoRegistro())) {
                    ListaDtoVisitasIndustriales dto = new ListaDtoVisitasIndustriales(true, cicloe, periodo, a, x, ap);
                    ldto.add(dto);
                } else {
                    ListaDtoVisitasIndustriales dto = new ListaDtoVisitasIndustriales(false, cicloe, periodo, a, x, ap);
                    ldto.add(dto);
                }

            });
//            ldto.forEach(System.err::println);
            return ldto;
        }
    }

    @Override
    public VisitasIndustriales actualizarVisita(VisitasIndustriales nuevaVisita) throws Throwable {
        f.setEntityClass(VisitasIndustriales.class);
        f.edit(nuevaVisita);
        f.flush();
        Messages.addGlobalInfo("El registro se ha actualizado correctamente");
        return nuevaVisita;
    }
}
