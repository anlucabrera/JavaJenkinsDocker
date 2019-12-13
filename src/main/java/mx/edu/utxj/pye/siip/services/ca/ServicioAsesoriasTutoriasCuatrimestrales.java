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
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.servlet.annotation.MultipartConfig;
import mx.edu.utxj.pye.sgi.controlador.Caster;
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
    
    @Override
    public List<DatosAsesoriasTutorias> getDatosAsesoriasTutorias() {
        try {
            return f.getEntityManager().createNamedQuery("DatosAsesoriasTutorias.findAll", DatosAsesoriasTutorias.class).getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        } catch (Exception ex){
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<PeriodosEscolares> getPeriodosConregistro(RegistrosTipo registrosTipo, EventosRegistros eventoRegistro, AreasUniversidad area) {
        List<Integer> periodos = f.getEntityManager().createQuery("SELECT atc.periodoEscolar FROM AsesoriasTutoriasCuatrimestrales atc INNER JOIN atc.registros r WHERE r.tipo.registroTipo =:tipo AND r.area =:area", Integer.class)
                    .setParameter("tipo", registrosTipo.getRegistroTipo())
                    .setParameter("area", area.getArea())
                    .getResultList();
            List<Integer> claves = new ArrayList<>();
            periodos.stream().forEach((p) -> {
                claves.add(p);
            });
            List<PeriodosEscolares> listaPeriodoEscolares = new ArrayList<>();
        if(claves.isEmpty()){
            listaPeriodoEscolares = f.getEntityManager().createQuery("SELECT p FROM PeriodosEscolares p WHERE (:mes BETWEEN p.mesInicio.numero AND p.mesFin.numero) AND (p.anio = :anio)",PeriodosEscolares.class)
                        .setParameter("mes", ejbModulos.getNumeroMes(eventoRegistro.getMes()))
                        .setParameter("anio", eventoRegistro.getEjercicioFiscal().getAnio())
                        .getResultList();
        }else{
                listaPeriodoEscolares = f.getEntityManager().createQuery("SELECT periodo FROM PeriodosEscolares periodo WHERE periodo.periodo IN :claves ORDER BY periodo.periodo desc", PeriodosEscolares.class)
                        .setParameter("claves", claves)
                        .getResultList();
            }
            return listaPeriodoEscolares;
        }

    @Override
    public Map.Entry<List<PeriodosEscolares>, List<EventosRegistros>> comprobarEventoActual(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoActual, RegistrosTipo registrosTipo, AreasUniversidad area) throws PeriodoEscolarNecesarioNoRegistradoException {
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
    public List<AsesoriasTutoriasCuatrimestrales> buscaAsesoriaTutoriaCuatrimestralEspecifico(RegistrosTipo registroTipo, AreasUniversidad areaUniversidad, PeriodosEscolares periodoEscolar, DatosAsesoriasTutorias datoAsesoriaTutoria) {
        List<AsesoriasTutoriasCuatrimestrales> listaATC = new ArrayList<>();
        try {
            listaATC = f.getEntityManager().createQuery("SELECT atc FROM AsesoriasTutoriasCuatrimestrales atc INNER JOIN atc.registros r INNER JOIN r.tipo t INNER JOIN r.eventoRegistro er WHERE t.registroTipo=:tipoRegistro AND r.area = :areaRegistro AND atc.periodoEscolar = :periodo AND atc.datoAsesoriaTutoria.datoAsesoriaTutoria = :datoAsesoriaTutoria AND atc.area = :area", AsesoriasTutoriasCuatrimestrales.class)
                    .setParameter("tipoRegistro", registroTipo.getRegistroTipo())
                    .setParameter("areaRegistro", areaUniversidad.getArea())
                    .setParameter("periodo", periodoEscolar.getPeriodo())
                    .setParameter("datoAsesoriaTutoria", datoAsesoriaTutoria.getDatoAsesoriaTutoria())
                    .setParameter("area", areaUniversidad.getArea())
                    .getResultList();
            listaATC.stream().forEach((t) -> {
//                System.err.println(t.getRegistro());
            });
            return listaATC;
        } catch (NoResultException e) {
//            LOG.log(Level.SEVERE, "No se ha encontrado ninguna coincidencia: SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (NonUniqueResultException e) {
//            LOG.log(Level.SEVERE, "Se ha encontrado más de un resultado durante la consulta del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (Exception e) {
//            LOG.log(Level.SEVERE, "Ha ocurrido una excepción durante la busqueda del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        }
    }
    
    @Override
    public List<DTOAsesoriasTutoriasCuatrimestrales> getListaAsesoriaTutoriaCuatrimestralPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo, RegistrosTipo registrosTipo) {
            if(evento == null || claveArea == null || periodo == null){
                return null;
            }
        AreasUniversidad area = f.getEntityManager().find(AreasUniversidad.class, claveArea);

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
                    f.getEntityManager().find(PeriodosEscolares.class, periodo.getPeriodo()),  
                        promedio, 
                    f.getEntityManager().find(AreasUniversidad.class, area.getArea()))
                );
            });
            return listaDtos;
        }

    @Override
    public void guardaAsesoriaTutoriaCuatrimestral(DTOAsesoriasTutoriasCuatrimestrales asesoriaTutoriaCuatrimestral, RegistrosTipo registroTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
        f.setEntityClass(AsesoriasTutoriasCuatrimestrales.class);
            Registros registro = ejbModulos.getRegistro(registroTipo, ejesRegistro, area, eventosRegistros);
            asesoriaTutoriaCuatrimestral.getAsesoriaTutoriaCuatrimestral().setRegistro(registro.getRegistro());
        f.create(asesoriaTutoriaCuatrimestral.getAsesoriaTutoriaCuatrimestral());
        f.flush();
            Messages.addGlobalInfo("<b>Se ha dado de alta el registro de Asesoría ó Tutoría Cuatrimestral correctamente.");
        }
    
    @Override
    public String editaAsesoriaTutoriaCuatrimestralPeriodoEscolar(DTOAsesoriasTutoriasCuatrimestrales asesoriaTutoriaMensual) {
        try {
            f.setEntityClass(AsesoriasTutoriasCuatrimestrales.class);
            f.edit(asesoriaTutoriaMensual.getAsesoriaTutoriaCuatrimestral());
            f.flush();
            return "Los datos de la: " + asesoriaTutoriaMensual.getAsesoriaTutoriaCuatrimestral().getTipo() + " se han actualizado correctamente";
        } catch (Exception e) {
            return "Ha ocurrido un error durante la operación, verifique su información";
        }
    }

    @Override
    public List<AsesoriasTutoriasCuatrimestrales> buscaAsesoriaTutoriaCuatrimestralParaGuardado(AsesoriasTutoriasCuatrimestrales asesoriaTutoriaCuatrimestral) {
        List<AsesoriasTutoriasCuatrimestrales> listaATC = new ArrayList<>();
        try {
            listaATC = f.getEntityManager().createQuery("SELECT atc FROM AsesoriasTutoriasCuatrimestrales atc WHERE atc.periodoEscolar = :periodoEscolar AND atc.tipo = :tipo AND atc.datoAsesoriaTutoria.datoAsesoriaTutoria = :datoAsesoriaTutoria AND atc.area = :area", AsesoriasTutoriasCuatrimestrales.class)
                    .setParameter("periodoEscolar", asesoriaTutoriaCuatrimestral.getPeriodoEscolar())
                    .setParameter("tipo", asesoriaTutoriaCuatrimestral.getTipo())
                    .setParameter("datoAsesoriaTutoria", asesoriaTutoriaCuatrimestral.getDatoAsesoriaTutoria().getDatoAsesoriaTutoria())
                    .setParameter("area", asesoriaTutoriaCuatrimestral.getArea())
                    .getResultList();
            listaATC.stream().forEach((t) -> {
//                System.err.println(t.getRegistro());
            });
            return listaATC;
        } catch (NoResultException e) {
//            LOG.log(Level.SEVERE, "No se ha encontrado ninguna coincidencia: SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (NonUniqueResultException e) {
//            LOG.log(Level.SEVERE, "Se ha encontrado más de un resultado durante la consulta del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (Exception e) {
//            LOG.log(Level.SEVERE, "Ha ocurrido una excepción durante la busqueda del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<AsesoriasTutoriasCuatrimestrales> buscaAsesoriaTutoriaCuatrimestralParaEdicion(AsesoriasTutoriasCuatrimestrales asesoriaTutoriaCuatrimestral) {
        List<AsesoriasTutoriasCuatrimestrales> listaATC = new ArrayList<>();
        try {
            listaATC = f.getEntityManager().createQuery("SELECT atc FROM AsesoriasTutoriasCuatrimestrales atc WHERE atc.periodoEscolar = :periodoEscolar AND atc.tipo = :tipo AND atc.datoAsesoriaTutoria.datoAsesoriaTutoria = :datoAsesoriaTutoria AND atc.area = :area AND atc.registro <> :registro", AsesoriasTutoriasCuatrimestrales.class)
                    .setParameter("periodoEscolar", asesoriaTutoriaCuatrimestral.getPeriodoEscolar())
                    .setParameter("tipo", asesoriaTutoriaCuatrimestral.getTipo())
                    .setParameter("datoAsesoriaTutoria", asesoriaTutoriaCuatrimestral.getDatoAsesoriaTutoria().getDatoAsesoriaTutoria())
                    .setParameter("area", asesoriaTutoriaCuatrimestral.getArea())
                    .setParameter("registro", asesoriaTutoriaCuatrimestral.getRegistro())
                    .getResultList();
            listaATC.stream().forEach((t) -> {
//                System.err.println(t.getRegistro());
            });
            return listaATC;
        } catch (NoResultException e) {
//            LOG.log(Level.SEVERE, "No se ha encontrado ninguna coincidencia: SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (NonUniqueResultException e) {
//            LOG.log(Level.SEVERE, "Se ha encontrado más de un resultado durante la consulta del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        } catch (Exception e) {
//            LOG.log(Level.SEVERE, "Ha ocurrido una excepción durante la busqueda del registro SesionIndividualMensualPsicopedogia.", e);
            return Collections.EMPTY_LIST;
        }
    }

}
