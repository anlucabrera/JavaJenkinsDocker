/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.ejb.pye;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesFormacionIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.BolsaTrabajo;
import mx.edu.utxj.pye.sgi.entity.pye2.ComisionesAcademicas;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerposAcademicosRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.DesercionPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistrosPeriodosPK;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistrosPeriodos;
import mx.edu.utxj.pye.sgi.entity.pye2.FeriasProfesiograficas;
import mx.edu.utxj.pye.sgi.entity.pye2.PersonalCapacitado;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAcademicos;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosMovilidad;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosAnioMes;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.siip.dto.pye.DtoRegistroEvento;
import mx.edu.utxj.pye.siip.dto.pye.DtoTipoInformacionRegistro;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbGestionModulosEvento")
public class EjbGestionModulosEvento {
    
    @EJB EjbReportesEvidencias ejbReportesEvidencias;
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbPacker ejbPacker;
    @EJB Facade f;
    private EntityManager em;
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
    /**
     * Permite obtener la lista de ejes de registro
     * @return Resultado del proceso
     */
    public ResultadoEJB<EventosRegistros> getEventoActivo(){
        try{
             EventosRegistros eventoActivo = em.createQuery("select e from EventosRegistros e where :fecha BETWEEN e.fechaInicio and e.fechaFin", EventosRegistros.class)
                        .setParameter("fecha", new Date())
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
            
            if(eventoActivo == null){
                return ResultadoEJB.crearErroneo(2,eventoActivo, "No existe evento de registro aperturado.");
            }else{
                return ResultadoEJB.crearCorrecto(eventoActivo, "Evento de registro aperturado.");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar la apertura de evento de registro (EjbGestionModulosEvento.getEventoActivo).", e, EventosRegistros.class);
        }
    }
    
     /**
     * Permite obtener la lista de ejes de registro
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EventosRegistros>> getEventosRegistro(){
        try{
            List<EventosRegistros> eventosRegistro = em.createQuery("SELECT e FROM EventosRegistros e ORDER BY e.eventoRegistro DESC", EventosRegistros.class)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(eventosRegistro, "Lista de eventos de registro ordenados de forma descendente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de eventos de registro ordenados de forma descendente. (EjbGestionModulosEvento.getEventosRegistro)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de áreas con registros en el evento seleccionado
     * @param eventoRegistro
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AreasUniversidad>> getAreasEventoRegistro(EventosRegistros eventoRegistro){
        try{
            List<AreasUniversidad> areas = new ArrayList<>();
            
            List<Short> clavesAreas = em.createQuery("SELECT r FROM Registros r WHERE r.eventoRegistro.eventoRegistro=:evento", Registros.class)
                    .setParameter("evento", eventoRegistro.getEventoRegistro())
                    .getResultStream()
                    .map(p->p.getArea())
                    .distinct()
                    .collect(Collectors.toList());
            
            clavesAreas.forEach(claveArea -> {
                AreasUniversidad area = em.find(AreasUniversidad.class, claveArea);
                areas.add(area);
            });
            
            return ResultadoEJB.crearCorrecto(areas.stream().sorted(Comparator.comparing(AreasUniversidad::getArea)).collect(Collectors.toList()), "Lista de áreas con registros en el evento seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de áreas con registros en el evento seleccionado. (EjbGestionModulosEvento.getAreasEventoRegistro)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de ejes de registro de evento y área seleccionada
     * @param eventoRegistro
     * @param area
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EjesRegistro>> getEjesRegistroArea(EventosRegistros eventoRegistro, AreasUniversidad area){
        try{
            List<EjesRegistro> ejesRegistro = em.createQuery("SELECT r FROM Registros r WHERE r.eventoRegistro.eventoRegistro=:evento AND r.area=:area", Registros.class)
                    .setParameter("evento", eventoRegistro.getEventoRegistro())
                    .setParameter("area", area.getArea())
                    .getResultStream()
                    .map(p->p.getEje())
                    .distinct()
                    .sorted(Comparator.comparing(EjesRegistro::getEje))
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(ejesRegistro, "Lista de ejes con registros del área seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de ejes con registros del área seleccionada. (EjbGestionModulosEvento.getEjesRegistroArea)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de tipos registros del área y eje seleccionado
     * @param eventoRegistro
     * @param area
     * @param eje
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<RegistrosTipo>> getTiposRegistroAreaEje(EventosRegistros eventoRegistro, AreasUniversidad area, EjesRegistro eje){
        try{
            List<RegistrosTipo> ejesRegistro = em.createQuery("SELECT r FROM Registros r WHERE r.eventoRegistro.eventoRegistro=:evento AND r.area=:area AND r.eje.eje=:eje", Registros.class)
                    .setParameter("evento", eventoRegistro.getEventoRegistro())
                    .setParameter("area", area.getArea())
                    .setParameter("eje", eje.getEje())
                    .getResultStream()
                    .map(p->p.getTipo())
                    .distinct()
                    .sorted(Comparator.comparing(RegistrosTipo::getRegistroTipo))
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(ejesRegistro, "Lista de tipos de registro del área y eje seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de tipos registros del área y eje seleccionado. (EjbGestionModulosEvento.getTiposRegistroAreaEje)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de registros del evento, área, eje y tipo seleccionado
     * @param eventoRegistro
     * @param area
     * @param eje
     * @param tipo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoRegistroEvento>> getRegistrosEvento(EventosRegistros eventoRegistro, AreasUniversidad area, EjesRegistro eje, RegistrosTipo tipo){
        try{
            List<DtoRegistroEvento> listaRegistros = new ArrayList<>();
            
            List<Registros> registros = em.createQuery("SELECT r FROM Registros r WHERE r.eventoRegistro.eventoRegistro=:evento AND r.area=:area AND r.eje.eje=:eje AND r.tipo.registroTipo=:tipo", Registros.class)
                    .setParameter("evento", eventoRegistro.getEventoRegistro())
                    .setParameter("area", area.getArea())
                    .setParameter("eje", eje.getEje())
                    .setParameter("tipo", tipo.getRegistroTipo())
                    .getResultStream()
                    .collect(Collectors.toList());
            
            registros.forEach(registro -> {
                DtoTipoInformacionRegistro dtoTipoInformacionRegistro = ejbReportesEvidencias.getObtenerInformacionRegistro(registro).getValor();
                Boolean seleccionado = false;
                DtoRegistroEvento dto = new DtoRegistroEvento(registro, dtoTipoInformacionRegistro.getInformacionRegistro(), dtoTipoInformacionRegistro.getPeriodoEscolar(), dtoTipoInformacionRegistro.getInformacionSubregistros(), seleccionado);
                listaRegistros.add(dto);
            });
            
            return ResultadoEJB.crearCorrecto(listaRegistros, "Lista de registros del evento, área, eje y tipo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de registros del evento, área, eje y tipo seleccionado. (EjbGestionModulosEvento.getRegistrosEvento)", e, null);
        }
    }
    
     /**
     * Permite cambiar de mes los registros seleccionados
     * @param eventoRegistro
     * @param listaRegistros
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoRegistroEvento>> cambiarEventoRegistros(EventosRegistros eventoRegistro, List<DtoRegistroEvento> listaRegistros){
        try{
            List<Boolean> listaValores = new ArrayList<>();
            
            String mensaje = "", mensajeSubregistros = "";
            
            RegistrosTipo tipoRegistro = listaRegistros.stream().map(p->p.getRegistro().getTipo()).distinct().findFirst().orElse(null);
            
            List<RegistrosTipo> subRegistros = obtenerSubregistros(tipoRegistro).getValor();
            
            List<DtoRegistroEvento> listaRegistrosActualizados = new ArrayList<>();
           
            listaRegistros.forEach(registro -> {
                Boolean valor = verificarPeriodoEscolar(eventoRegistro, registro.getPeriodoEscolar()).getValor();
                if(registro.getPeriodoEscolar()== 0 || valor){
                    registro.getRegistro().setEventoRegistro(eventoRegistro);
                    em.merge(registro.getRegistro());
                    em.flush();
                    listaRegistrosActualizados.add(registro);
                }
                listaValores.add(valor);
            });
            
            if(subRegistros.size()!= 0 && !listaValores.stream().filter(p->p).collect(Collectors.toList()).isEmpty()){
                mensajeSubregistros = cambiarEventoSubregistros(subRegistros, listaRegistrosActualizados).getMensaje();
                mensaje = "Se han cambiado de mes: ".concat(String.valueOf(listaRegistrosActualizados.size())).concat(" registros ").concat(mensajeSubregistros);
            }else{
                mensaje = "No se pudo cambiar de mes la información seleccionada";
            }
            
            return ResultadoEJB.crearCorrecto(listaRegistrosActualizados, mensaje);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de registros que se han cambiado de mes. (EjbGestionModulosEvento.cambiarEventoRegistros)", e, null);
        }
    }
    
    /**
     * Permite obtener lista de subregistros del registro superior seleccionado
     * @param registrosTipo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<RegistrosTipo>> obtenerSubregistros(RegistrosTipo registrosTipo){
        try{
            
            List<RegistrosTipo> subRegistros = em.createQuery("SELECT r FROM RegistrosTipo r WHERE r.registroSuperior=:registro", RegistrosTipo.class)
                    .setParameter("registro", registrosTipo.getRegistroTipo())
                    .getResultStream()
                    .collect(Collectors.toList());
            
            
            return ResultadoEJB.crearCorrecto(subRegistros, "Se obtuvo correctamente la lista de subregistros del registro superior seleccionado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de subregistros del registro superior seleccionado. (EjbGestionModulosEvento.obtenerSubregistros)", e, null);
        }
    }
    
    /**
     * Permite cambiar de mes los subregistros del registro superior seleccionados
     * @param subRegistros
     * @param listaRegistros
     * @return Resultado del proceso
     */
    public ResultadoEJB<String> cambiarEventoSubregistros(List<RegistrosTipo> subRegistros, List<DtoRegistroEvento> listaRegistros){
        try{
            List<String> actualizaciones = new ArrayList<>();
            
            List<Short> tipos = subRegistros.stream().map(p->p.getRegistroTipo()).collect(Collectors.toList());
            
            tipos.forEach(tipo -> {
                Integer act = 0;
                switch(tipo){
                    case 2:
                         ResultadoEJB<Integer> resEntBT = actualizarRegistrosBolsaTrabajoEnt(listaRegistros);
                         if(resEntBT.getCorrecto()){
                            act = resEntBT.getValor();
                            actualizaciones.add(resEntBT.getMensaje());
                         }
                        break;
                    case 17:
                         ResultadoEJB<Integer> resPartAFI = actualizarRegistrosPartActFormInt(listaRegistros);
                         if(resPartAFI.getCorrecto()){
                            act = resPartAFI.getValor();
                            actualizaciones.add(resPartAFI.getMensaje());
                         }
                        break;
                    case 26:
                         ResultadoEJB<Integer> resDesRep =  actualizarRegistrosDesercionRep(listaRegistros);
                         if(resDesRep.getCorrecto()){
                            act = resDesRep.getValor();
                            actualizaciones.add(resDesRep.getMensaje());
                         }
                        break;
                    case 32:
                         ResultadoEJB<Integer> resMovEst =  actualizarRegistrosMovilidadEst(listaRegistros);
                         if(resMovEst.getCorrecto()){
                            act = resMovEst.getValor();
                            actualizaciones.add(resMovEst.getMensaje());
                         }
                        break;
                    case 33:
                         ResultadoEJB<Integer> resMovDoc =  actualizarRegistrosMovilidadDoc(listaRegistros);
                         if(resMovDoc.getCorrecto()){
                            act = resMovDoc.getValor();
                            actualizaciones.add(resMovDoc.getMensaje());
                         }
                        break;
                    case 35:
                         ResultadoEJB<Integer> resPartFP =  actualizarRegistrosPartFerias(listaRegistros);
                         if(resPartFP.getCorrecto()){
                            act = resPartFP.getValor();
                            actualizaciones.add(resPartFP.getMensaje());
                         }
                        break;
                    case 37:
                         ResultadoEJB<Integer> resPartST =  actualizarRegistrosPartServTec(listaRegistros);
                         if(resPartST.getCorrecto()){
                            act = resPartST.getValor();
                            actualizaciones.add(resPartST.getMensaje());
                         }
                        break;
                    case 39:
                         ResultadoEJB<Integer> resIntCA =  actualizarRegistrosCuerpAcadInt(listaRegistros);
                         if(resIntCA.getCorrecto()){
                            act = resIntCA.getValor();
                            actualizaciones.add(resIntCA.getMensaje());
                         }
                        break;
                    case 40:
                         ResultadoEJB<Integer> resLineasCA =  actualizarRegistrosCuerpAcadLineas(listaRegistros);
                         if(resLineasCA.getCorrecto()){
                            act = resLineasCA.getValor();
                            actualizaciones.add(resLineasCA.getMensaje());
                         }
                        break;
                    case 42:
                         ResultadoEJB<Integer> resPersPA =  actualizarRegistrosPersProdAcad(listaRegistros);
                         if(resPersPA.getCorrecto()){
                            act = resPersPA.getValor();
                            actualizaciones.add(resPersPA.getMensaje());
                         }
                        break;
                    case 46:
                         ResultadoEJB<Integer> resPartPC =  actualizarRegistrosPartPersonalCap(listaRegistros);
                         if(resPartPC.getCorrecto()){
                            act = resPartPC.getValor();
                            actualizaciones.add(resPartPC.getMensaje());
                         }
                        break;
                    case 49:
                         ResultadoEJB<Integer> resPartCA =  actualizarRegistrosPartComAcad(listaRegistros);
                         if(resPartCA.getCorrecto()){
                            act = resPartCA.getValor();
                            actualizaciones.add(resPartCA.getMensaje());
                         }
                        break;
                    default:
                        break;
                }
                
            });
            
            String mensaje = "Se cambiaron de mes los ".concat(String.valueOf(actualizaciones.size())).concat(" subregistros correspondientes.");
            
            return ResultadoEJB.crearCorrecto(actualizaciones.toString(),mensaje);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo cambiar el mes de los subregistros correspondientes. (EjbGestionModulosEvento.cambiarEventoSubregistros)", e, null);
        }
    }
    
     /**
     * Permite cambiar de mes los registros de entrevistas de bolsa de trabajo
     * @param listaRegistros
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> actualizarRegistrosBolsaTrabajoEnt(List<DtoRegistroEvento> listaRegistros){
        try{
            List<Integer> actualizacionesRealizadas = new ArrayList<>();
            
            List<BolsaTrabajo> listaBolsaTrabajo = listaRegistros.stream().map(p->p.getRegistro().getBolsaTrabajo()).collect(Collectors.toList());
            
            listaBolsaTrabajo.forEach(bolsaTrabajo -> {
                List<Integer> regEntrevistas = bolsaTrabajo.getBolsaTrabajoEntrevistasList().stream().map(p->p.getRegistros().getRegistro()).collect(Collectors.toList());
                
                regEntrevistas.forEach(regEntrevista -> {
                    
                    Integer entrevistasActualizadas = em.createQuery("UPDATE Registros r SET r.eventoRegistro=:evento WHERE r.registro=:registro")
                        .setParameter("evento", bolsaTrabajo.getRegistros().getEventoRegistro())
                        .setParameter("registro", regEntrevista)
                        .executeUpdate();
                });
                actualizacionesRealizadas.add(regEntrevistas.size());
            });
            
            
            String mensaje = "Se han cambiado de mes: ".concat(String.valueOf((int) actualizacionesRealizadas.stream().count())).concat(" entrevistas de bolsa de trabajo.");
            
            return ResultadoEJB.crearCorrecto((int) actualizacionesRealizadas.stream().count(), mensaje);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el mes de los registros de entrevistas de bolsa de trabajo. (EjbGestionModulosEvento.actualizarRegistrosBolsaTrabajoEnt)", e, null);
        }
    }
    
     /**
     * Permite cambiar de mes los registros de participantes de actividades de formación integral
     * @param listaRegistros
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> actualizarRegistrosPartActFormInt(List<DtoRegistroEvento> listaRegistros){
        try{
            List<Integer> actualizacionesRealizadas = new ArrayList<>();
            
            List<ActividadesFormacionIntegral> listaActFormInt = listaRegistros.stream().map(p->p.getRegistro().getActividadesFormacionIntegral()).collect(Collectors.toList());
            
            listaActFormInt.forEach(actFormInt -> {
                List<Integer> regActividades = actFormInt.getParticipantesActividadesFormacionIntegralList().stream().map(p->p.getRegistros().getRegistro()).collect(Collectors.toList());
                
                regActividades.forEach(regActividad -> {
                    Integer actividadesActualizadas = em.createQuery("UPDATE Registros r SET r.eventoRegistro=:evento WHERE r.registro=:registro")
                            .setParameter("evento", actFormInt.getRegistros().getEventoRegistro())
                            .setParameter("registro", regActividad)
                            .executeUpdate();
                });
                actualizacionesRealizadas.add(regActividades.size());
            });
            
            
            String mensaje = "Se han cambiado de mes: ".concat(String.valueOf((int) actualizacionesRealizadas.stream().count())).concat(" participantes de actividades de formación integral.");
            
            return ResultadoEJB.crearCorrecto((int) actualizacionesRealizadas.stream().count(), mensaje);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el mes de los registros de participantes de actividades de formación integral. (EjbGestionModulosEvento.actualizarRegistrosPartActFormInt)", e, null);
        }
    }
    
    /**
     * Permite cambiar de mes los registros de deserción por reprobación 
     * @param listaRegistros
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> actualizarRegistrosDesercionRep(List<DtoRegistroEvento> listaRegistros){
        try{
            List<Integer> actualizacionesRealizadas = new ArrayList<>();
            
            List<DesercionPeriodosEscolares> listaDesercion = listaRegistros.stream().map(p->p.getRegistro().getDesercionPeriodosEscolares()).collect(Collectors.toList());
            
            listaDesercion.forEach(desercion -> {
                List<Integer> regDeserciones = desercion.getDesercionReprobacionMateriasList().stream().map(p->p.getRegistros().getRegistro()).collect(Collectors.toList());
                
                regDeserciones.forEach(regDesercion -> {
                    
                    Integer desercionesActualizadas = em.createQuery("UPDATE Registros r SET r.eventoRegistro=:evento WHERE r.registro=:registro")
                        .setParameter("evento", desercion.getRegistros().getEventoRegistro())
                        .setParameter("registro", regDesercion)
                        .executeUpdate();
                });
                actualizacionesRealizadas.add(regDeserciones.size());
            });
            
            
            String mensaje = "Se han cambiado de mes: ".concat(String.valueOf((int) actualizacionesRealizadas.stream().count())).concat(" deserciones por reprobación.");
            
            return ResultadoEJB.crearCorrecto((int) actualizacionesRealizadas.stream().count(), mensaje);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el mes de los registros de deserción por reprobación. (EjbGestionModulosEvento.actualizarRegistrosDesercionRep)", e, null);
        }
    }
    
    /**
     * Permite cambiar de mes los registros de movilidad estudiantil 
     * @param listaRegistros
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> actualizarRegistrosMovilidadEst(List<DtoRegistroEvento> listaRegistros){
        try{
            List<Integer> actualizacionesRealizadas = new ArrayList<>();
            
            List<RegistrosMovilidad> listaMovilidad = listaRegistros.stream().map(p->p.getRegistro().getRegistrosMovilidad()).collect(Collectors.toList());
            
            listaMovilidad.forEach(movEst -> {
                List<Integer> regMovilidadesEst = movEst.getRegistroMovilidadEstudianteList().stream().map(p->p.getRegistros().getRegistro()).collect(Collectors.toList());
                
                regMovilidadesEst.forEach(regMovilidadEst -> {
                    
                    Integer desercionesActualizadas = em.createQuery("UPDATE Registros r SET r.eventoRegistro=:evento WHERE r.registro=:registro")
                        .setParameter("evento", movEst.getRegistros().getEventoRegistro())
                        .setParameter("registro", regMovilidadEst)
                        .executeUpdate();
                });
                actualizacionesRealizadas.add(regMovilidadesEst.size());
            });
            
            
            String mensaje = "Se han cambiado de mes: ".concat(String.valueOf((int) actualizacionesRealizadas.stream().count())).concat(" movilidades estudiante.");
            
            return ResultadoEJB.crearCorrecto((int) actualizacionesRealizadas.stream().count(), mensaje);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el mes de los registros de movilidad estudiante. (EjbGestionModulosEvento.actualizarRegistrosMovilidadEst)", e, null);
        }
    }
    
     /**
     * Permite cambiar de mes los registros de movilidad docente
     * @param listaRegistros
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> actualizarRegistrosMovilidadDoc(List<DtoRegistroEvento> listaRegistros){
        try{
            List<Integer> actualizacionesRealizadas = new ArrayList<>();
            
            List<RegistrosMovilidad> listaMovilidad = listaRegistros.stream().map(p->p.getRegistro().getRegistrosMovilidad()).collect(Collectors.toList());
            
            listaMovilidad.forEach(movDoc -> {
                List<Integer> regMovilidadesDoc = movDoc.getRegistroMovilidadDocenteList().stream().map(p->p.getRegistros().getRegistro()).collect(Collectors.toList());
                
                regMovilidadesDoc.forEach(regMovilidadDoc -> {
                    
                    Integer desercionesActualizadas = em.createQuery("UPDATE Registros r SET r.eventoRegistro=:evento WHERE r.registro=:registro")
                        .setParameter("evento", movDoc.getRegistros().getEventoRegistro())
                        .setParameter("registro", regMovilidadDoc)
                        .executeUpdate();
                });
                actualizacionesRealizadas.add(regMovilidadesDoc.size());
            });
            
            
            String mensaje = "Se han cambiado de mes: ".concat(String.valueOf((int) actualizacionesRealizadas.stream().count())).concat(" movilidades docente.");
            
            return ResultadoEJB.crearCorrecto((int) actualizacionesRealizadas.stream().count(), mensaje);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el mes de los registros de movilidad docente. (EjbGestionModulosEvento.actualizarRegistrosMovilidadDoc)", e, null);
        }
    }
    
     /**
     * Permite cambiar de mes los registros de participantes de ferias profesiográficas
     * @param listaRegistros
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> actualizarRegistrosPartFerias(List<DtoRegistroEvento> listaRegistros){
        try{
            List<Integer> actualizacionesRealizadas = new ArrayList<>();
            
            List<FeriasProfesiograficas> listaFerias = listaRegistros.stream().map(p->p.getRegistro().getFeriasProfesiograficas()).collect(Collectors.toList());
            
            listaFerias.forEach(feria -> {
                List<Integer> regPartFerias = feria.getFeriasParticipantesList().stream().map(p->p.getRegistros().getRegistro()).collect(Collectors.toList());
                
                regPartFerias.forEach(regPartFeria -> {
                    
                    Integer partFeriasActualizadas = em.createQuery("UPDATE Registros r SET r.eventoRegistro=:evento WHERE r.registro=:registro")
                        .setParameter("evento", feria.getRegistros().getEventoRegistro())
                        .setParameter("registro", regPartFeria)
                        .executeUpdate();
                });
                actualizacionesRealizadas.add(regPartFerias.size());
            });
            
            
            String mensaje = "Se han cambiado de mes: ".concat(String.valueOf((int) actualizacionesRealizadas.stream().count())).concat(" participantes de ferias profesiográficas.");
            
            return ResultadoEJB.crearCorrecto((int) actualizacionesRealizadas.stream().count(), mensaje);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el mes de los registros de participantes de ferias profesiográficas. (EjbGestionModulosEvento.actualizarRegistrosPartFerias)", e, null);
        }
    }
    
     /**
     * Permite cambiar de mes los registros de participantes de servicios tecnológicos
     * @param listaRegistros
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> actualizarRegistrosPartServTec(List<DtoRegistroEvento> listaRegistros){
        try{
            List<Integer> actualizacionesRealizadas = new ArrayList<>();
            
            List<ServiciosTecnologicosAnioMes> listaSevTec = listaRegistros.stream().map(p->p.getRegistro().getServiciosTecnologicosAnioMes()).collect(Collectors.toList());
            
            listaSevTec.forEach(servTect -> {
                List<Integer> regPartServiciosTec = servTect.getServiciosTecnologicosParticipantesList().stream().map(p->p.getRegistros().getRegistro()).collect(Collectors.toList());
                
                regPartServiciosTec.forEach(regPartServTec -> {
                    
                    Integer partFeriasActualizadas = em.createQuery("UPDATE Registros r SET r.eventoRegistro=:evento WHERE r.registro=:registro")
                        .setParameter("evento", servTect.getRegistros().getEventoRegistro())
                        .setParameter("registro", regPartServTec)
                        .executeUpdate();
                });
                actualizacionesRealizadas.add(regPartServiciosTec.size());
            });
            
            
            String mensaje = "Se han cambiado de mes: ".concat(String.valueOf((int) actualizacionesRealizadas.stream().count())).concat(" participantes de servicios tecnológicos.");
            
            return ResultadoEJB.crearCorrecto((int) actualizacionesRealizadas.stream().count(), mensaje);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el mes de los registros de participantes de servicios tecnológicos. (EjbGestionModulosEvento.actualizarRegistrosPartServTec)", e, null);
        }
    }
    
     /**
     * Permite cambiar de mes los registros de integrantes de cuerpos académicos
     * @param listaRegistros
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> actualizarRegistrosCuerpAcadInt(List<DtoRegistroEvento> listaRegistros){
        try{
            List<Integer> actualizacionesRealizadas = new ArrayList<>();
            
            List<CuerposAcademicosRegistro> listaCuerpAcad = listaRegistros.stream().map(p->p.getRegistro().getCuerposAcademicosRegistro()).collect(Collectors.toList());
            
            listaCuerpAcad.forEach(cuerpAcad -> {
                List<Integer> regIntCuerposAcad = cuerpAcad.getCuerpacadIntegrantesList().stream().map(p->p.getRegistros().getRegistro()).collect(Collectors.toList());
                
                regIntCuerposAcad.forEach(regIntCuerpAcad -> {
                    
                    Integer intCuerpAcadActualizadas = em.createQuery("UPDATE Registros r SET r.eventoRegistro=:evento WHERE r.registro=:registro")
                        .setParameter("evento", cuerpAcad.getRegistros().getEventoRegistro())
                        .setParameter("registro", regIntCuerpAcad)
                        .executeUpdate();
                });
                actualizacionesRealizadas.add(regIntCuerposAcad.size());
            });
            
            
            String mensaje = "Se han cambiado de mes: ".concat(String.valueOf((int) actualizacionesRealizadas.stream().count())).concat(" integrantes de cuerpos académicos.");
            
            return ResultadoEJB.crearCorrecto((int) actualizacionesRealizadas.stream().count(), mensaje);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el mes de los registros de integrantes de cuerpos académicos. (EjbGestionModulosEvento.actualizarRegistrosCuerpAcadInt)", e, null);
        }
    }
    
     /**
     * Permite cambiar de mes los registros de líneas de estudio de cuerpos académicos
     * @param listaRegistros
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> actualizarRegistrosCuerpAcadLineas(List<DtoRegistroEvento> listaRegistros){
        try{
            List<Integer> actualizacionesRealizadas = new ArrayList<>();
            
            List<CuerposAcademicosRegistro> listaCuerpAcad = listaRegistros.stream().map(p->p.getRegistro().getCuerposAcademicosRegistro()).collect(Collectors.toList());
            
            listaCuerpAcad.forEach(cuerpAcad -> {
                List<Integer> regLineasCuerposAcad = cuerpAcad.getCuerpacadLineasList().stream().map(p->p.getRegistros().getRegistro()).collect(Collectors.toList());
                
                regLineasCuerposAcad.forEach(regLineaCuerposAcad -> {
                    
                    Integer lineasCuerpAcadActualizadas = em.createQuery("UPDATE Registros r SET r.eventoRegistro=:evento WHERE r.registro=:registro")
                        .setParameter("evento", cuerpAcad.getRegistros().getEventoRegistro())
                        .setParameter("registro", regLineaCuerposAcad)
                        .executeUpdate();
                });
                actualizacionesRealizadas.add(regLineasCuerposAcad.size());
            });
            
            
            String mensaje = "Se han cambiado de mes: ".concat(String.valueOf((int) actualizacionesRealizadas.stream().count())).concat(" líneas de estudio de cuerpos académicos.");
            
            return ResultadoEJB.crearCorrecto((int) actualizacionesRealizadas.stream().count(), mensaje);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el mes de los registros de líneas de estudio de cuerpos académicos. (EjbGestionModulosEvento.actualizarRegistrosCuerpAcadLineas)", e, null);
        }
    }
    
     /**
     * Permite cambiar de mes los registros de personal de productos académicos
     * @param listaRegistros
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> actualizarRegistrosPersProdAcad(List<DtoRegistroEvento> listaRegistros){
        try{
            List<Integer> actualizacionesRealizadas = new ArrayList<>();
            
            List<ProductosAcademicos> listaProdAcad = listaRegistros.stream().map(p->p.getRegistro().getProductosAcademicos()).collect(Collectors.toList());
            
            listaProdAcad.forEach(prodAcad -> {
                List<Integer> regPersonalProdAcad = prodAcad.getProductosAcademicosPersonalList().stream().map(p->p.getRegistros().getRegistro()).collect(Collectors.toList());
                
                regPersonalProdAcad.forEach(regPersProdAcad -> {
                    
                    Integer personalProdAcadActualizadas = em.createQuery("UPDATE Registros r SET r.eventoRegistro=:evento WHERE r.registro=:registro")
                        .setParameter("evento", prodAcad.getRegistros().getEventoRegistro())
                        .setParameter("registro", regPersProdAcad)
                        .executeUpdate();
                });
                actualizacionesRealizadas.add(regPersonalProdAcad.size());
            });
            
            
            String mensaje = "Se han cambiado de mes: ".concat(String.valueOf((int) actualizacionesRealizadas.stream().count())).concat(" personal de productos académicos.");
            
            return ResultadoEJB.crearCorrecto((int) actualizacionesRealizadas.stream().count(), mensaje);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el mes de los registros de personal de productos académicos. (EjbGestionModulosEvento.actualizarRegistrosPersProdAcad)", e, null);
        }
    }
    
     /**
     * Permite cambiar de mes los registros de participantes de personal capacitado
     * @param listaRegistros
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> actualizarRegistrosPartPersonalCap(List<DtoRegistroEvento> listaRegistros){
        try{
            List<Integer> actualizacionesRealizadas = new ArrayList<>();
            
            List<PersonalCapacitado> listaPersCap = listaRegistros.stream().map(p->p.getRegistro().getPersonalCapacitado()).collect(Collectors.toList());
            
            listaPersCap.forEach(persCap -> {
                List<Integer> regParticipantesPersCap = persCap.getParticipantesPersonalCapacitadoList().stream().map(p->p.getRegistros().getRegistro()).collect(Collectors.toList());
                
                regParticipantesPersCap.forEach(regPartPersCap -> {
                    
                    Integer personalProdAcadActualizadas = em.createQuery("UPDATE Registros r SET r.eventoRegistro=:evento WHERE r.registro=:registro")
                        .setParameter("evento", persCap.getRegistros().getEventoRegistro())
                        .setParameter("registro", regPartPersCap)
                        .executeUpdate();
                });
                actualizacionesRealizadas.add(regParticipantesPersCap.size());
            });
            
            
            String mensaje = "Se han cambiado de mes: ".concat(String.valueOf((int) actualizacionesRealizadas.stream().count())).concat(" participantes de personal capacitado.");
            
            return ResultadoEJB.crearCorrecto((int) actualizacionesRealizadas.stream().count(), mensaje);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el mes de los registros de participantes de personal capacitado. (EjbGestionModulosEvento.actualizarRegistrosPartPersonalCap)", e, null);
        }
    }
    
     /**
     * Permite cambiar de mes los registros de participantes de personal capacitado
     * @param listaRegistros
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> actualizarRegistrosPartComAcad(List<DtoRegistroEvento> listaRegistros){
        try{
            List<Integer> actualizacionesRealizadas = new ArrayList<>();
            
            List<ComisionesAcademicas> listaComAcad = listaRegistros.stream().map(p->p.getRegistro().getComisionesAcademicas()).collect(Collectors.toList());
            
            listaComAcad.forEach(comAcad -> {
                List<Integer> regParticipantesComAcad = comAcad.getComisionesAcademicasParticipantesList().stream().map(p->p.getRegistros().getRegistro()).collect(Collectors.toList());
                
                regParticipantesComAcad.forEach(regPartComAcad -> {
                    
                    Integer personalProdAcadActualizadas = em.createQuery("UPDATE Registros r SET r.eventoRegistro=:evento WHERE r.registro=:registro")
                        .setParameter("evento", comAcad.getRegistros().getEventoRegistro())
                        .setParameter("registro", regPartComAcad)
                        .executeUpdate();
                });
                actualizacionesRealizadas.add(regParticipantesComAcad.size());
            });
            
            
            String mensaje = "Se han cambiado de mes: ".concat(String.valueOf((int) actualizacionesRealizadas.stream().count())).concat(" participantes de comisiones académicas.");
            
            return ResultadoEJB.crearCorrecto((int) actualizacionesRealizadas.stream().count(), mensaje);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el mes de los registros de participantes de comisiones académicas. (EjbGestionModulosEvento.actualizarRegistrosPartComAcad)", e, null);
        }
    }
    
     /**
     * Permite verificar el periodo escolar del evento escolar al que se cambiarán los registros con el que tenían registrado
     * @param eventoRegistro
     * @param periodoEscolar
     * @return Resultado del proceso
     */
    public ResultadoEJB<Boolean> verificarPeriodoEscolar(EventosRegistros eventoRegistro, Integer periodoEscolar){
        try{
            Boolean valor = false;
            
            EventosRegistrosPeriodos eventoRegPeriodo = em.createQuery("SELECT e FROM EventosRegistrosPeriodos e WHERE e.eventosRegistrosPeriodosPK.eventoRegistro=:evento", EventosRegistrosPeriodos.class)
                    .setParameter("evento", eventoRegistro.getEventoRegistro())
                    .getResultStream()
                    .findFirst().orElse(null);
            
            if(eventoRegPeriodo != null){
                if(eventoRegPeriodo.getEventosRegistrosPeriodosPK().getPeriodoEscolar() == periodoEscolar){
                    valor = true;
                }
            }
            
            return ResultadoEJB.crearCorrecto(valor, "Se ha verificado si es el mismo periodo escolar del evento al que se cambiará con el que ya tenía registrado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si es el mismo periodo escolar del evento al que se cambiará con el que ya tenía registrado. (EjbGestionModulosEvento.verificarPeriodoEscolar)", e, null);
        }
    }
    
}
