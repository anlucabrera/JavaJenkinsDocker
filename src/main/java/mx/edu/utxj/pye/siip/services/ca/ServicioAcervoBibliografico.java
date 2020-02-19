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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.Meses;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.AcervoBibliograficoPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.escolar.DTOAcervoBibliograficoPeriodosEscolares;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbAcervoBibliografico;
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
public class ServicioAcervoBibliografico implements EjbAcervoBibliografico{
    
    @EJB EjbModulos ejbModulos;
    @EJB Facade f;
    @EJB EjbPropiedades ep;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @Inject Caster caster; 
    @Inject ControladorEmpleado controladorEmpleado;
   
    @Override
    public  List<DTOAcervoBibliograficoPeriodosEscolares> getListaAcervoBibliografico(String rutaArchivo) throws Throwable {
       
        List<DTOAcervoBibliograficoPeriodosEscolares> listaDtoAcervoBibliografico = new ArrayList<>();
        CiclosEscolares ciclosEscolares;
        PeriodosEscolares periodosEscolares;
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
                ciclosEscolares = new CiclosEscolares();
                periodosEscolares = new PeriodosEscolares();
                areaUniversidad = new AreasUniversidad();
                acervoBibliograficoPeriodosEscolares = new AcervoBibliograficoPeriodosEscolares();
                dTOAcervoBibliograficoPeriodosEscolares = new DTOAcervoBibliograficoPeriodosEscolares();
                
//                switch (fila.getCell(0).getCellTypeEnum()) {
//                    case STRING:
//                        dTOAcervoBibliograficoPeriodosEscolares.setCicloEsc(fila.getCell(0).getStringCellValue());
//                        break;
//                    default:
//                        break;
//                }
                switch (fila.getCell(1).getCellTypeEnum()) {
                    case FORMULA:
                        ciclosEscolares.setCiclo((int) fila.getCell(1).getNumericCellValue());
                        acervoBibliograficoPeriodosEscolares.setCicloEscolar(ciclosEscolares.getCiclo());
                        break;
                    default:
                        break;
                }
//                switch (fila.getCell(2).getCellTypeEnum()) {
//                    case STRING:
//                        dTOAcervoBibliograficoPeriodosEscolares.setPeriodoEsc(fila.getCell(2).getStringCellValue());
//                        break;
//                    default:
//                        break;
//                }
                switch (fila.getCell(3).getCellTypeEnum()) {
                   case FORMULA:
                        periodosEscolares.setPeriodo((int) fila.getCell(3).getNumericCellValue());
                        acervoBibliograficoPeriodosEscolares.setPeriodoEscolar(periodosEscolares.getPeriodo());
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
            libroRegistro.close();
            Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaDtoAcervoBibliografico;
        
    }
    
   @Override
    public void guardaAcervoBibliografico(List<DTOAcervoBibliograficoPeriodosEscolares> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
      
        List<String> listaCondicional = new ArrayList<>();
        lista.forEach((acervoBibliografico) -> {
           
        
           if (ejbModulos.validaPeriodoRegistro(ejbModulos.getPeriodoEscolarActual(), acervoBibliografico.getAcervoBibliograficoPeriodosEscolares().getPeriodoEscolar())) {
               f.setEntityClass(AcervoBibliograficoPeriodosEscolares.class);
               AcervoBibliograficoPeriodosEscolares abpe = getRegistroAcervoBibliografico(acervoBibliografico.getAcervoBibliograficoPeriodosEscolares().getCicloEscolar(), acervoBibliografico.getAcervoBibliograficoPeriodosEscolares().getPeriodoEscolar(), acervoBibliografico.getAcervoBibliograficoPeriodosEscolares().getProgramaEducativo());
               Boolean registroAlmacenado = false;
               if (abpe != null) {
                   listaCondicional.add(acervoBibliografico.getAreasUniversidad().getNombre());
                   registroAlmacenado = true;
               }
               if (registroAlmacenado) {
                   acervoBibliografico.getAcervoBibliograficoPeriodosEscolares().setRegistro(abpe.getRegistro());
                   f.edit(acervoBibliografico.getAcervoBibliograficoPeriodosEscolares());
                   f.flush();
                   Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
               } else {
                   Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                   acervoBibliografico.getAcervoBibliograficoPeriodosEscolares().setRegistro(registro.getRegistro());
                   f.create(acervoBibliografico.getAcervoBibliograficoPeriodosEscolares());
                   f.flush();
                   Messages.addGlobalInfo("<b>Se guardaron los registros correctamente </b>");
               }
               f.flush();
           } else {
               Messages.addGlobalWarn("<b>No se puede modificar información de periodos anteriores </b>");
           }
       });
    }
    
    @Override
    public AcervoBibliograficoPeriodosEscolares getRegistroAcervoBibliografico(Integer cicloEscolar, Integer periodoEscolar, Short programaEducativo) {
        AcervoBibliograficoPeriodosEscolares acervoBibliograficoPeriodosEscolares = new AcervoBibliograficoPeriodosEscolares();
        TypedQuery<AcervoBibliograficoPeriodosEscolares> query = f.getEntityManager().createQuery("SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.cicloEscolar = :cicloEscolar AND a.periodoEscolar = :periodoEscolar AND a.programaEducativo = :programaEducativo", AcervoBibliograficoPeriodosEscolares.class);
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
    public List<PeriodosEscolares> getPeriodosConregistro() {
       List<Integer> claves = f.getEntityManager().createQuery("SELECT a FROM AcervoBibliograficoPeriodosEscolares a", AcervoBibliograficoPeriodosEscolares.class)
                .getResultStream()
                .map(a -> a.getPeriodoEscolar())
                .collect(Collectors.toList());
        
         if(claves.isEmpty())
       {
           claves.add(0, ejbModulos.getPeriodoEscolarActual().getPeriodo());
       
       }
         
        return f.getEntityManager().createQuery("SELECT periodo FROM PeriodosEscolares periodo WHERE periodo.periodo IN :claves ORDER BY periodo.periodo desc", PeriodosEscolares.class)
                .setParameter("claves", claves)
                .getResultList();
    }

    @Override
    public List<EventosRegistros> getEventosPorPeriodo(PeriodosEscolares periodo) {
         if(periodo == null){
            return null;
        }

        List<String> meses = f.getEntityManager().createQuery("SELECT m FROM Meses m where m.numero BETWEEN :inicio AND :fin ORDER BY m.numero", Meses.class)
                .setParameter("inicio", periodo.getMesInicio().getNumero())
                .setParameter("fin", periodo.getMesFin().getNumero())
                .getResultList()
                .stream()
                .map(m -> m.getMes())
                .collect(Collectors.toList());
        
        return f.getEntityManager().createQuery("SELECT er from EventosRegistros er INNER JOIN er.ejercicioFiscal ef WHERE ef.anio=:anio AND er.mes in :meses AND er.fechaInicio <= :fecha ORDER BY er.fechaInicio DESC, er.fechaFin DESC", EventosRegistros.class)
                .setParameter("fecha", new Date())
                .setParameter("anio", periodo.getAnio())
                .setParameter("meses", meses)
                .getResultList();
    }

    @Override
    public List<DTOAcervoBibliograficoPeriodosEscolares> getListaRegistrosPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo) {
           //verificar que los parametros no sean nulos
        if(evento == null || claveArea == null || periodo == null){
            return null;
        }
        List<Short> areas = new ArrayList<>();
        
        //obtener la referencia al area operativa del trabajador
        AreasUniversidad area = f.getEntityManager().find(AreasUniversidad.class, claveArea);
      
        //comprobar si el area operativa es un programa educativo referenciar a su area superior para obtener la referencia al area academica
        Short programaCategoria = (short)ep.leerPropiedadEntera("modulosRegistroProgramaEducativoCategoria").orElse(9);
        
        if (Objects.equals(area.getCategoria().getCategoria(), programaCategoria)) {            
            area = f.getEntityManager().find(AreasUniversidad.class, area.getAreaSuperior());

            //Obtener las claves de todas las areas que dependan de área academicoa
            areas = f.getEntityManager().createQuery("SELECT au FROM AreasUniversidad au WHERE au.areaSuperior=:areaSuperior AND au.vigente='1'", AreasUniversidad.class)
                    .setParameter("areaSuperior", area.getArea())
                    .getResultStream()
                    .map(au -> au.getArea())
                    .collect(Collectors.toList());

        }else{//si no es area academica solo filtrar los datos del area operativa del trabajador
            areas.add(claveArea);

        }
        
        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        List<DTOAcervoBibliograficoPeriodosEscolares> l = new ArrayList<>();
        List<AcervoBibliograficoPeriodosEscolares> entities = f.getEntityManager().createQuery("SELECT a FROM AcervoBibliograficoPeriodosEscolares a INNER JOIN a.registros reg INNER JOIN reg.eventoRegistro er WHERE er.eventoRegistro=:evento AND a.periodoEscolar =:periodo", AcervoBibliograficoPeriodosEscolares.class)
                .setParameter("evento", evento.getEventoRegistro())
                .setParameter("periodo", periodo.getPeriodo())
                .getResultList();
      
        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            
            Registros reg = f.getEntityManager().find(Registros.class, e.getRegistro());
            ActividadesPoa a = reg.getActividadesPoaList().isEmpty()?null:reg.getActividadesPoaList().get(0);
            PeriodosEscolares p = f.getEntityManager().find(PeriodosEscolares.class, e.getPeriodoEscolar());
            String strDateFormat = "yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            String cicloe = sdf.format(p.getCiclo().getInicio()) + " - " + sdf.format(p.getCiclo().getFin());
            l.add(new DTOAcervoBibliograficoPeriodosEscolares(
                    e,
                    f.getEntityManager().find(CiclosEscolares.class, e.getCicloEscolar()),
                    f.getEntityManager().find(PeriodosEscolares.class, e.getPeriodoEscolar()),
                    f.getEntityManager().find(AreasUniversidad.class, e.getProgramaEducativo()),
                    a,
                    cicloe));
        });
        


        return l;
    }

    @Override
    public Map.Entry<List<PeriodosEscolares>, List<EventosRegistros>> comprobarEventoActual(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoActual) throws PeriodoEscolarNecesarioNoRegistradoException {
       
        if(periodos==null || periodos.isEmpty()) periodos = getPeriodosConregistro();
        if(periodos==null || periodos.isEmpty()) return null;
        if(eventoActual == null) eventoActual = ejbModulos.getEventoRegistro();
        if(eventoActual == null) return null;
        
        PeriodosEscolares reciente = periodos.get(0);
        Boolean existe = eventos.contains(eventoActual);        

        
        if(!existe){//si el evento no existe en la lista de eventos del periodo mas reciente
            if(eventos.size() <3){//si el evento deberia pertenecer al periodo mas reciente
                eventos = new ArrayList<>(Stream.concat(Stream.of(eventoActual), eventos.stream()).collect(Collectors.toList())); //.add(eventoActual);
            }else{//si el evento debería pertenecer al periodo inmediato al mas reciente detectado
                PeriodosEscolares periodo = f.getEntityManager().find(PeriodosEscolares.class, reciente.getPeriodo() + 1);
                if(periodo == null) throw new PeriodoEscolarNecesarioNoRegistradoException(reciente.getPeriodo() + 1, caster.periodoToString(reciente));

                periodos = new ArrayList<>(Stream.concat(Stream.of(periodo), periodos.stream()).collect(Collectors.toList()));
                eventos.clear();
                eventos.add(eventoActual);
            }
        }
        Map<List<PeriodosEscolares>,List<EventosRegistros>> map = new HashMap<>();
        map.put(periodos, eventos);
        return map.entrySet().iterator().next();
    }

    @Override
    public List<DTOAcervoBibliograficoPeriodosEscolares> getRegistroReporteAcervoBib() {
        //obtener la lista de registros mensuales filtrando por evento y por claves de areas
        List<DTOAcervoBibliograficoPeriodosEscolares> l = new ArrayList<>();
        List<AcervoBibliograficoPeriodosEscolares> entities = f.getEntityManager().createQuery("SELECT a FROM AcervoBibliograficoPeriodosEscolares a", AcervoBibliograficoPeriodosEscolares.class)
                .getResultList();
      
        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            
            Registros reg = f.getEntityManager().find(Registros.class, e.getRegistro());
            ActividadesPoa a = reg.getActividadesPoaList().isEmpty()?null:reg.getActividadesPoaList().get(0);
            PeriodosEscolares p = f.getEntityManager().find(PeriodosEscolares.class, e.getPeriodoEscolar());
            String strDateFormat = "yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            String cicloe = sdf.format(p.getCiclo().getInicio()) + " - " + sdf.format(p.getCiclo().getFin());
            l.add(new DTOAcervoBibliograficoPeriodosEscolares(
                    e,
                    f.getEntityManager().find(CiclosEscolares.class, e.getCicloEscolar()),
                    f.getEntityManager().find(PeriodosEscolares.class, e.getPeriodoEscolar()),
                    f.getEntityManager().find(AreasUniversidad.class, e.getProgramaEducativo()),
                    a,
                    cicloe));
        });

        return l;
    }
}
