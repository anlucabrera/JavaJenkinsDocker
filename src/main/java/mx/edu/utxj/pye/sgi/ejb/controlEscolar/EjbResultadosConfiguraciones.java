package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.util.ArrayList;
import java.util.List;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.facade.Facade;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoResultadosCargaAcademica;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AccionesDeMejora;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Calificacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ObjetivoEducacional;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ObjetivoEducacionalPlanMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradoraPromedio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Informeplaneacioncuatrimestraldocenteprint;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

@Stateless(name = "EjbResultadosConfiguraciones")
public class EjbResultadosConfiguraciones {
   @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPersonal ejbPersonal;
    @EJB EjbEstudianteBean ejbEstudianteBean;
    @EJB EjbFichaAdmision ejbFA;
    @EJB EjbAreasLogeo ejbAreasLogeo;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    @EJB Facade2 f2;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    Double sumaC=0D;
    Integer esalcanzados=0;
    private EntityManager em;

    @PostConstruct
    public void init() {
        em = f.getEntityManager();
    }
    

    public ResultadoEJB<DtoResultadosCargaAcademica> getCalificacionesPorConfiguracionDetalle(Informeplaneacioncuatrimestraldocenteprint informe,Double mp) {
        try {
            sumaC = 0D;
            esalcanzados = 0;
            String semafoto= "semaforoRojo";
            DtoResultadosCargaAcademica drca = new DtoResultadosCargaAcademica(informe, 0, 0, 0D, 0D,"",semafoto);
            CargaAcademica ca = getcargaAcdemica(informe.getCarga());
            Integer esAc = ca.getCveGrupo().getEstudianteList().size();
            if (informe.getNombreUnidad().equals("T.I.")) {
                List<TareaIntegradoraPromedio> trp = em.createQuery("select t from TareaIntegradoraPromedio t INNER JOIN t.tareaIntegradora dt INNER JOIN dt.carga cg WHERE cg.carga=:carga", TareaIntegradoraPromedio.class)
                        .setParameter("carga", informe.getCarga())
                        .getResultList();
                if (!trp.isEmpty()) {
                    trp.forEach((k) -> {
                        if (k.getValor() != 0D) {
                            sumaC = sumaC + k.getValor();
                            if (k.getValor() >= 8D) {
                                esalcanzados = esalcanzados + 1;
                            }
                        }
                    });
                }
            } else {
                List<Calificacion> cal = em.createQuery("select t from Calificacion t INNER JOIN t.configuracionDetalle dt WHERE dt.configuracionDetalle=:configuracionDetalle", Calificacion.class)
                        .setParameter("configuracionDetalle", informe.getConfiguracionDetalle())
                        .getResultList();
                if (!cal.isEmpty()) {
                    cal.forEach((t) -> {
                        if (t.getValor() != null) {
                            if (t.getValor() != 0) {
                                sumaC = sumaC + t.getValor();
                                if (t.getValor() >= 8D) {
                                    esalcanzados = esalcanzados + 1;
                                }
                            }
                        }
                    });
                }
            }
            
            Double promedioin = 0D;
            Double promediounidad = 0D;
            if (sumaC != 0D) {
                promedioin = sumaC / Double.parseDouble(esAc.toString());
            }
            if (esalcanzados != 0) {
                promediounidad = (Double.parseDouble(esalcanzados.toString()) * 100) / Double.parseDouble(esAc.toString());
            }

            if (promediounidad >= mp) {
                semafoto = "semaforoVerde";
            } else {
                semafoto = "semaforoRojo";
            }

            String accionesDeMejora="";
            List<AccionesDeMejora> cal = em.createQuery("select t from AccionesDeMejora t INNER JOIN t.configuracion dt WHERE dt.configuracion=:configuracion", AccionesDeMejora.class)
                        .setParameter("configuracion", informe.getConfiguracion())
                        .getResultList();
                if (!cal.isEmpty()) {
                    accionesDeMejora=cal.get(0).getAcciones();
                }
            
            
            drca=new DtoResultadosCargaAcademica(informe, esAc, esalcanzados, promedioin, promediounidad,accionesDeMejora,semafoto);
            
            return ResultadoEJB.crearCorrecto(drca, "Califiaciones Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los Tipos de Especialidad Centro(EjbResultadosConfiguraciones.getCalificacionesPorConfiguracionDetalle).", e, null);
        }
    }
    
    public ResultadoEJB<DtoResultadosCargaAcademica> getCalificacionesUnidad(Informeplaneacioncuatrimestraldocenteprint informe,Double mp) {
        try {
            sumaC = 0D;
            esalcanzados = 0;
            String semafoto= "semaforoRojo";
            DtoResultadosCargaAcademica drca = new DtoResultadosCargaAcademica(informe, 0, 0, 0D, 0D,"",semafoto);
            CargaAcademica ca = getcargaAcdemica(informe.getCarga());
            Integer esAc = ca.getCveGrupo().getEstudianteList().size();
            if (informe.getNombreUnidad().equals("T.I.")) {
                List<TareaIntegradoraPromedio> trp = em.createQuery("select t from TareaIntegradoraPromedio t INNER JOIN t.tareaIntegradora dt INNER JOIN dt.carga cg WHERE cg.carga=:carga", TareaIntegradoraPromedio.class)
                        .setParameter("carga", informe.getCarga())
                        .getResultList();
                if (!trp.isEmpty()) {
                    trp.forEach((k) -> {
                        if (k.getValor() != 0D) {
                            sumaC = sumaC + k.getValor();
                            if (k.getValor() >= 8D) {
                                esalcanzados = esalcanzados + 1;
                            }
                        }
                    });
                }
            } else {
                List<Calificacion> cal = em.createQuery("select t from CalificacionPromedio t INNER JOIN t.cargaAcademica dt WHERE dt.carga=:carga", Calificacion.class)
                        .setParameter("carga", informe.getCarga())
                        .getResultList();
                if (!cal.isEmpty()) {
                    cal.forEach((t) -> {
                        if (t.getValor() != null) {
                            if (t.getValor() != 0) {
                                sumaC = sumaC + t.getValor();
                                if (t.getValor() >= 8D) {
                                    esalcanzados = esalcanzados + 1;
                                }
                            }
                        }
                    });
                }
            }
            
            Double promedioin = 0D;
            Double promediounidad = 0D;
            if (sumaC != 0D) {
                promedioin = sumaC / Double.parseDouble(esAc.toString());
            }
            if (esalcanzados != 0) {
                promediounidad = (Double.parseDouble(esalcanzados.toString()) * 100) / Double.parseDouble(esAc.toString());
            }

            if (promediounidad >= mp) {
                semafoto = "semaforoVerde";
            } else {
                semafoto = "semaforoRojo";
            }

            String accionesDeMejora="";
            List<AccionesDeMejora> cal = em.createQuery("select t from AccionesDeMejora t INNER JOIN t.configuracion dt WHERE dt.configuracion=:configuracion", AccionesDeMejora.class)
                        .setParameter("configuracion", informe.getConfiguracion())
                        .getResultList();
                if (!cal.isEmpty()) {
                    accionesDeMejora=cal.get(0).getAcciones();
                }
            
            
            drca=new DtoResultadosCargaAcademica(informe, esAc, esalcanzados, promedioin, promediounidad,accionesDeMejora,semafoto);
            
            return ResultadoEJB.crearCorrecto(drca, "Califiaciones Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los Tipos de Especialidad Centro(EjbResultadosConfiguraciones.getCalificacionesPorConfiguracionDetalle).", e, null);
        }
    }
    
    public CargaAcademica getcargaAcdemica(Integer carga) {
        try {
            List<CargaAcademica> cal = em.createQuery("select t from CargaAcademica t WHERE t.carga=:carga", CargaAcademica.class)
                    .setParameter("carga", carga)
                    .getResultList();
            return cal.get(0);
        } catch (Exception e) {
            return new CargaAcademica();
        }
    } 
    
    
    public ResultadoEJB<AccionesDeMejora> registrarAccionMejora(AccionesDeMejora adm,Integer confi, Operacion operacion) {
        try {
            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbResultadosConfiguraciones.registrarAccionMejora(1)");
            f.setEntityClass(AccionesDeMejora.class);
            
            switch (operacion) {
                case PERSISTIR:
                    UnidadMateriaConfiguracion umc= getcargaconfiguracion(confi);
                    adm.setConfiguracion(new UnidadMateriaConfiguracion());
                    System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbResultadosConfiguraciones.registrarAccionMejora(2)");
                    adm.setConfiguracion(umc);
                    System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbResultadosConfiguraciones.registrarAccionMejora(3)");                    
                    adm.getConfiguracion().setConfiguracion(confi);
                    System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbResultadosConfiguraciones.registrarAccionMejora(4)");
                    em.persist(adm);
                    f.flush();
                    return ResultadoEJB.crearCorrecto(adm, "Se registró correctamente La Unidad Materia");
                case ACTUALIZAR:
                    em.merge(adm);
                    f.flush();
                    return ResultadoEJB.crearCorrecto(adm, "Se actualizo correctamente La Unidad Materia");
                case ELIMINAR:
                    f.remove(adm);
                    f.flush();
                    return ResultadoEJB.crearCorrecto(null, "Se elimino correctamente La Unidad Materia");
                default:
                    return ResultadoEJB.crearErroneo(2, "Operación no autorizada.", AccionesDeMejora.class);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar La Unidad Materia (EjbRegistroPlanEstudio)", e, null);
        }
    }
    
    public UnidadMateriaConfiguracion getcargaconfiguracion(Integer carga) {
        try {
            List<UnidadMateriaConfiguracion> cal = em.createQuery("select t from UnidadMateriaConfiguracion t WHERE t.configuracion=:configuracion", UnidadMateriaConfiguracion.class)
                    .setParameter("configuracion", carga)
                    .getResultList();
            return cal.get(0);
        } catch (Exception e) {
            return new UnidadMateriaConfiguracion();
        }
    } 

}
