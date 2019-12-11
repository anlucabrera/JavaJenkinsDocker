/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.servlet.annotation.MultipartConfig;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.AsesoriasTutoriasCuatrimestrales;
import mx.edu.utxj.pye.sgi.entity.pye2.DatosAsesoriasTutorias;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.siip.dto.ca.DTOAsesoriasTutoriasCuatrimestrales;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbAsesoriasTutoriasCuatrimestrales;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Stateless
@MultipartConfig
public class ServicioAsesoriasTutoriasCuatrimestrales implements EjbAsesoriasTutoriasCuatrimestrales{

    @EJB    Facade                          f;
    @EJB    EjbFiscalizacion                ejbFiscalizacion;
    @EJB    EjbModulos                      ejbModulos;
    @EJB    EjbMatriculaPeriodosEscolares   ejbMatriculaPeriodosEscolares;
    @Inject Caster                          caster;
    private EntityManager em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    @Override
    public List<DatosAsesoriasTutorias> getDatosAsesoriasTutorias() {
        try {
            return em.createNamedQuery("DatosAsesoriasTutorias.findAll", DatosAsesoriasTutorias.class).getResultList();
        } catch (NoResultException e) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioAsesoriasTutoriasCuatrimestrales.getDatosAsesoriasTutorias() " + e.getMessage());
            return Collections.EMPTY_LIST;
        } catch (Exception ex){
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioAsesoriasTutoriasCuatrimestrales.getDatosAsesoriasTutorias() " + ex.getMessage());
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<PeriodosEscolares> getPeriodosConregistro(@NonNull RegistrosTipo registrosTipo, @NonNull EventosRegistros eventoRegistro, @NonNull AreasUniversidad area) {
        try {
            List<Integer> periodos = em.createQuery("SELECT atc.periodoEscolar FROM AsesoriasTutoriasCuatrimestrales atc INNER JOIN atc.registros r WHERE r.tipo.registroTipo =:tipo AND r.area =:area", Integer.class)
                    .setParameter("tipo", registrosTipo.getRegistroTipo())
                    .setParameter("area", area.getArea())
                    .getResultList();
            List<Integer> claves = new ArrayList<>();
            periodos.stream().forEach((p) -> {
                claves.add(p);
            });
            List<PeriodosEscolares> listaPeriodoEscolares = new ArrayList<>();
            if (claves.isEmpty()) {
                listaPeriodoEscolares = em.createQuery("SELECT p FROM PeriodosEscolares p WHERE (:mes BETWEEN p.mesInicio.numero AND p.mesFin.numero) AND (p.anio = :anio)", PeriodosEscolares.class)
                        .setParameter("mes", ejbModulos.getNumeroMes(eventoRegistro.getMes()))
                        .setParameter("anio", eventoRegistro.getEjercicioFiscal().getAnio())
                        .getResultList();
            } else {
                listaPeriodoEscolares = f.getEntityManager().createQuery("SELECT periodo FROM PeriodosEscolares periodo WHERE periodo.periodo IN :claves ORDER BY periodo.periodo desc", PeriodosEscolares.class)
                        .setParameter("claves", claves)
                        .getResultList();
            }
            return listaPeriodoEscolares;
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioAsesoriasTutoriasCuatrimestrales.getPeriodosConregistro() " + e.getMessage());
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Map.Entry<List<PeriodosEscolares>, List<EventosRegistros>> comprobarEventoActual(@NonNull List<PeriodosEscolares> periodos, @NonNull List<EventosRegistros> eventos, @NonNull EventosRegistros eventoActual, @NonNull RegistrosTipo registrosTipo, @NonNull AreasUniversidad area) throws PeriodoEscolarNecesarioNoRegistradoException {
        try {
            if(periodos==null || periodos.isEmpty()) periodos = getPeriodosConregistro(registrosTipo,eventoActual,area);
            if(periodos==null || periodos.isEmpty()) return null;
            if(eventoActual == null) eventoActual = ejbModulos.getEventoRegistro();
            if(eventoActual == null) return null;
            PeriodosEscolares reciente = periodos.get(0);
            Boolean existe = eventos.contains(eventoActual);
            if(!existe){
                if(eventos.size() <3){
                    eventos = new ArrayList<>(Stream.concat(Stream.of(eventoActual), eventos.stream()).collect(Collectors.toList()));
                }else{
                    PeriodosEscolares periodo = em.find(PeriodosEscolares.class, reciente.getPeriodo() + 1);
                    if(periodo == null) throw new PeriodoEscolarNecesarioNoRegistradoException(reciente.getPeriodo() + 1, caster.periodoToString(reciente));
                    periodos = new ArrayList<>(Stream.concat(Stream.of(periodo), periodos.stream()).collect(Collectors.toList()));
                    eventos.clear();
                    eventos.add(eventoActual);
                }
            }
            Map<List<PeriodosEscolares>,List<EventosRegistros>> map = new HashMap<>();
            map.put(periodos, eventos);
            return map.entrySet().iterator().next();
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioAsesoriasTutoriasCuatrimestrales.comprobarEventoActual() " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<AsesoriasTutoriasCuatrimestrales> buscaAsesoriaTutoriaCuatrimestralEspecifico(@NonNull RegistrosTipo registroTipo, @NonNull AreasUniversidad areaUniversidad, @NonNull PeriodosEscolares periodoEscolar, @NonNull DatosAsesoriasTutorias datoAsesoriaTutoria) {
        List<AsesoriasTutoriasCuatrimestrales> listaATC = new ArrayList<>();
        try {
            listaATC = em.createQuery("SELECT atc FROM AsesoriasTutoriasCuatrimestrales atc INNER JOIN atc.registros r INNER JOIN r.tipo t INNER JOIN r.eventoRegistro er WHERE t.registroTipo=:tipoRegistro AND r.area = :areaRegistro AND atc.periodoEscolar = :periodo AND atc.datoAsesoriaTutoria.datoAsesoriaTutoria = :datoAsesoriaTutoria AND atc.area = :area", AsesoriasTutoriasCuatrimestrales.class)
                    .setParameter("tipoRegistro", registroTipo.getRegistroTipo())
                    .setParameter("areaRegistro", areaUniversidad.getArea())
                    .setParameter("periodo", periodoEscolar.getPeriodo())
                    .setParameter("datoAsesoriaTutoria", datoAsesoriaTutoria.getDatoAsesoriaTutoria())
                    .setParameter("area", areaUniversidad.getArea())
                    .getResultList();
            return listaATC;
        } catch (NoResultException e) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioAsesoriasTutoriasCuatrimestrales.buscaAsesoriaTutoriaCuatrimestralEspecifico() " + e.getMessage());
//            LOG.log(Level.SEVERE, "No se ha encontrado ninguna coincidencia: SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (NonUniqueResultException e) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioAsesoriasTutoriasCuatrimestrales.buscaAsesoriaTutoriaCuatrimestralEspecifico() " + e.getMessage());
//            LOG.log(Level.SEVERE, "Se ha encontrado más de un resultado durante la consulta del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioAsesoriasTutoriasCuatrimestrales.buscaAsesoriaTutoriaCuatrimestralEspecifico() " + e.getMessage());
//            LOG.log(Level.SEVERE, "Ha ocurrido una excepción durante la busqueda del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        }
    }
    
    @Override
    public List<DTOAsesoriasTutoriasCuatrimestrales> getListaAsesoriaTutoriaCuatrimestralPorEventoAreaPeriodo(@NonNull EventosRegistros evento, @NonNull Short claveArea, @NonNull PeriodosEscolares periodo, @NonNull RegistrosTipo registrosTipo) {
        try {
            if(evento == null || claveArea == null || periodo == null){
                return null;
            }
            AreasUniversidad area = em.find(AreasUniversidad.class, claveArea);

            List<DTOAsesoriasTutoriasCuatrimestrales> listaDtos = new ArrayList<>();
            List<DatosAsesoriasTutorias> entidades = getDatosAsesoriasTutorias();
            entidades.stream().forEach(e -> {
                AsesoriasTutoriasCuatrimestrales asesoriaTutoria = new AsesoriasTutoriasCuatrimestrales();
                List<AsesoriasTutoriasCuatrimestrales> valoresBD = buscaAsesoriaTutoriaCuatrimestralEspecifico(registrosTipo, area, periodo, e);
                Integer suma = 0;
                Double promedio = 0.0D;
                if(!valoresBD.isEmpty()){
                    asesoriaTutoria = valoresBD.get(0);
                    suma = (int)asesoriaTutoria.getHombres() + (int)asesoriaTutoria.getMujeres();
                    promedio = (double)Math.round(((double)(suma)/ejbMatriculaPeriodosEscolares.getConteoMatriculaInicialPorPeriodo(periodo).intValue()*100) * 100d) / 100d;
                }else{
                    asesoriaTutoria.setHombres((short)0);
                    asesoriaTutoria.setMujeres((short)0);
                    asesoriaTutoria.setDatoAsesoriaTutoria(e);
                    asesoriaTutoria.setTipo(e.getTipo());
                    asesoriaTutoria.setArea(area.getArea());
                    asesoriaTutoria.setPeriodoEscolar(periodo.getPeriodo());
                }
                listaDtos.add(new DTOAsesoriasTutoriasCuatrimestrales(
                        asesoriaTutoria, 
                        em.find(PeriodosEscolares.class, periodo.getPeriodo()),  
                        promedio, 
                        em.find(AreasUniversidad.class, area.getArea()))
                );
            });
            return listaDtos;
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioAsesoriasTutoriasCuatrimestrales.getListaAsesoriaTutoriaCuatrimestralPorEventoAreaPeriodo() " + e.getMessage());
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public void guardaAsesoriaTutoriaCuatrimestral(@NonNull DTOAsesoriasTutoriasCuatrimestrales asesoriaTutoriaCuatrimestral, @NonNull RegistrosTipo registroTipo, @NonNull EjesRegistro ejesRegistro, @NonNull Short area, @NonNull EventosRegistros eventosRegistros) {
        try {
            Registros registro = ejbModulos.getRegistro(registroTipo, ejesRegistro, area, eventosRegistros);
            asesoriaTutoriaCuatrimestral.getAsesoriaTutoriaCuatrimestral().setRegistro(registro.getRegistro());
            guardaAsesoriaTutoriaCuatrimestralRE(asesoriaTutoriaCuatrimestral.getAsesoriaTutoriaCuatrimestral());
            Messages.addGlobalInfo("<b>Se ha dado de alta el registro de Asesoría ó Tutoría Cuatrimestral correctamente.");
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioAsesoriasTutoriasCuatrimestrales.guardaAsesoriaTutoriaCuatrimestral() " + e.getMessage());
        }
    }
    
    public ResultadoEJB<AsesoriasTutoriasCuatrimestrales> guardaAsesoriaTutoriaCuatrimestralRE(AsesoriasTutoriasCuatrimestrales atc){
        try {
            em.persist(atc);
            em.flush();
            return ResultadoEJB.crearCorrecto(atc, "Registro guardado correctamente AsesoriasTutoriasCuatrimestrales");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido guardar AsesoriasTutoriasCuatrimestrales", e, AsesoriasTutoriasCuatrimestrales.class);
        }
    }

    @Override
    public String editaAsesoriaTutoriaCuatrimestralPeriodoEscolar(@NonNull DTOAsesoriasTutoriasCuatrimestrales asesoriaTutoriaMensual) {
        try {
            editaTutoriaCuatrimestralPeriodoEscolarRE(asesoriaTutoriaMensual.getAsesoriaTutoriaCuatrimestral());
            return "Los datos de la: " + asesoriaTutoriaMensual.getAsesoriaTutoriaCuatrimestral().getTipo() + " se han actualizado correctamente";
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioAsesoriasTutoriasCuatrimestrales.editaAsesoriaTutoriaCuatrimestralPeriodoEscolar() " + e.getMessage());
            return "Ha ocurrido un error durante la operación, verifique su información";
        }
    }

    public ResultadoEJB<AsesoriasTutoriasCuatrimestrales> editaTutoriaCuatrimestralPeriodoEscolarRE(AsesoriasTutoriasCuatrimestrales atc){
        try {
            em.merge(atc);
            em.flush();
            return ResultadoEJB.crearCorrecto(atc, "Registro actualizado correctamente (AsesoriasTutoriasCuatrimestrales)");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido guardar (AsesoriasTutoriasCuatrimestrales)", e, AsesoriasTutoriasCuatrimestrales.class);
        }
    }
    
    @Override
    public List<AsesoriasTutoriasCuatrimestrales> buscaAsesoriaTutoriaCuatrimestralParaGuardado(@NonNull AsesoriasTutoriasCuatrimestrales asesoriaTutoriaCuatrimestral) {
        List<AsesoriasTutoriasCuatrimestrales> listaATC = new ArrayList<>();
        try {
            listaATC = em.createQuery("SELECT atc FROM AsesoriasTutoriasCuatrimestrales atc WHERE atc.periodoEscolar = :periodoEscolar AND atc.tipo = :tipo AND atc.datoAsesoriaTutoria.datoAsesoriaTutoria = :datoAsesoriaTutoria AND atc.area = :area", AsesoriasTutoriasCuatrimestrales.class)
                    .setParameter("periodoEscolar", asesoriaTutoriaCuatrimestral.getPeriodoEscolar())
                    .setParameter("tipo", asesoriaTutoriaCuatrimestral.getTipo())
                    .setParameter("datoAsesoriaTutoria", asesoriaTutoriaCuatrimestral.getDatoAsesoriaTutoria().getDatoAsesoriaTutoria())
                    .setParameter("area", asesoriaTutoriaCuatrimestral.getArea())
                    .getResultList();
            return listaATC;
        } catch (NoResultException e) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioAsesoriasTutoriasCuatrimestrales.buscaAsesoriaTutoriaCuatrimestralParaGuardado() " + e.getMessage());
//            LOG.log(Level.SEVERE, "No se ha encontrado ninguna coincidencia: SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (NonUniqueResultException e) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioAsesoriasTutoriasCuatrimestrales.buscaAsesoriaTutoriaCuatrimestralParaGuardado()" + e.getMessage());
//            LOG.log(Level.SEVERE, "Se ha encontrado más de un resultado durante la consulta del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioAsesoriasTutoriasCuatrimestrales.buscaAsesoriaTutoriaCuatrimestralParaGuardado()" + e.getMessage());
//            LOG.log(Level.SEVERE, "Ha ocurrido una excepción durante la busqueda del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<AsesoriasTutoriasCuatrimestrales> buscaAsesoriaTutoriaCuatrimestralParaEdicion(@NonNull AsesoriasTutoriasCuatrimestrales asesoriaTutoriaCuatrimestral) {
        List<AsesoriasTutoriasCuatrimestrales> listaATC = new ArrayList<>();
        try {
            listaATC = em.createQuery("SELECT atc FROM AsesoriasTutoriasCuatrimestrales atc WHERE atc.periodoEscolar = :periodoEscolar AND atc.tipo = :tipo AND atc.datoAsesoriaTutoria.datoAsesoriaTutoria = :datoAsesoriaTutoria AND atc.area = :area AND atc.registro <> :registro", AsesoriasTutoriasCuatrimestrales.class)
                    .setParameter("periodoEscolar", asesoriaTutoriaCuatrimestral.getPeriodoEscolar())
                    .setParameter("tipo", asesoriaTutoriaCuatrimestral.getTipo())
                    .setParameter("datoAsesoriaTutoria", asesoriaTutoriaCuatrimestral.getDatoAsesoriaTutoria().getDatoAsesoriaTutoria())
                    .setParameter("area", asesoriaTutoriaCuatrimestral.getArea())
                    .setParameter("registro", asesoriaTutoriaCuatrimestral.getRegistro())
                    .getResultList();
            return listaATC;
        } catch (NoResultException e) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioAsesoriasTutoriasCuatrimestrales.buscaAsesoriaTutoriaCuatrimestralParaEdicion() " + e.getMessage());
//            LOG.log(Level.SEVERE, "No se ha encontrado ninguna coincidencia: SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (NonUniqueResultException e) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioAsesoriasTutoriasCuatrimestrales.buscaAsesoriaTutoriaCuatrimestralParaEdicion() " + e.getMessage());
//            LOG.log(Level.SEVERE, "Se ha encontrado más de un resultado durante la consulta del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioAsesoriasTutoriasCuatrimestrales.buscaAsesoriaTutoriaCuatrimestralParaEdicion() " + e.getMessage());
//            LOG.log(Level.SEVERE, "Ha ocurrido una excepción durante la busqueda del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        }
    }

}
