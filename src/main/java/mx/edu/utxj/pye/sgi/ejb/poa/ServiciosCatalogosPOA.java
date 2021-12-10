package mx.edu.utxj.pye.sgi.ejb.poa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.controladores.cmi.DtoCmi;
import mx.edu.utxj.pye.sgi.dto.poa.DTOreportePoa;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.EjerciciosFiscales;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.Proyectos;
import mx.edu.utxj.pye.sgi.entity.pye2.RecursosActividad;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.UnidadMedidas;
import net.sf.jxls.transformer.XLSTransformer;

@Stateful
public class ServiciosCatalogosPOA implements EjbCatalogosPoa {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb-pye2_ejb_1.0PU")
    private EntityManager em;

    @EJB EjbCarga ejbCarga;
    @EJB EjbRegistroActividades ejbRegistroActividades;
    
    public static final String REPORTE_PLANTILLA = "Concentrado.xlsx";
    public static final String REPORTE_ACTUALIZADO = "Concentrado.xlsx";
        
    @EJB
    FacadePoa facadePoa;

    //  -------------------------------------------- EjesRegistro --------------------------------------------------
    @Override
    public EjesRegistro mostrarEjeRegistro(Integer clave) {
        facadePoa.setEntityClass(EjesRegistro.class);
        EjesRegistro pr = facadePoa.getEntityManager().find(EjesRegistro.class, clave);
        if (pr == null) {
            return null;
        } else {
            return pr;
        }
    }

    @Override
    public List<EjesRegistro> mostrarEjesRegistros() {
        facadePoa.setEntityClass(EjesRegistro.class);
        return facadePoa.findAll();
    }

    @Override
    public List<EjesRegistro> mostrarEjesRegistrosAreas(Short area, Short ejerciciosFiscales) {
        TypedQuery<EjesRegistro> q = em.createQuery("SELECT ej FROM ActividadesPoa ap INNER JOIN ap.cuadroMandoInt cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef WHERE ap.area=:area AND ef.ejercicioFiscal=:ejercicioFiscal GROUP BY ej.eje", EjesRegistro.class);
        q.setParameter("area", area);
        q.setParameter("ejercicioFiscal", ejerciciosFiscales);
        List<EjesRegistro> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<EjesRegistro> mostrarEjesRegistrosAreasCMI(Short area, Short ejerciciosFiscales) {
        TypedQuery<EjesRegistro> q = em.createQuery("SELECT ej FROM ActividadesPoa ap INNER JOIN ap.cuadroMandoInt cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef WHERE ap.area=:area AND ef.ejercicioFiscal=:ejercicioFiscal AND cm.proyecto!=null GROUP BY ej.eje", EjesRegistro.class);
        q.setParameter("area", area);
        q.setParameter("ejercicioFiscal", ejerciciosFiscales);
        List<EjesRegistro> pr = q.getResultList();
        return pr;
    }
//  -------------------------------------------- Estrategias -------------------------------------------------

    @Override
    public Estrategias mostrarEstrategia(Short clave) {
        facadePoa.setEntityClass(Estrategias.class);
        Estrategias pr = facadePoa.getEntityManager().find(Estrategias.class, clave);
        if (pr == null) {
            return null;
        } else {
            return pr;
        }
    }

    @Override
    public List<Estrategias> mostrarEstrategiasRegistro(Short ejerciciosFiscales, EjesRegistro ejesRegistro) {
        TypedQuery<Estrategias> q = em.createQuery("SELECT e FROM CuadroMandoIntegral cm INNER JOIN cm.estrategia e INNER JOIN cm.ejercicioFiscal ef INNER JOIN cm.eje ej WHERE ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal GROUP BY e.estrategia", Estrategias.class);
        q.setParameter("eje", ejesRegistro.getEje());
        q.setParameter("ejercicioFiscal", ejerciciosFiscales);
        List<Estrategias> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Estrategias> getEstarategiasPorEje(EjesRegistro eje, Short ejercicio, Short area) {
        return facadePoa.getEntityManager().createQuery("SELECT e FROM Estrategias e INNER JOIN e.cuadroMandoIntegralList cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef INNER JOIN cm.actividadesPoaList ap WHERE ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal AND ap.area=:area ORDER BY e.estrategia", Estrategias.class)
                .setParameter("eje", eje.getEje())
                .setParameter("ejercicioFiscal", ejercicio)
                .setParameter("area", area)
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Estrategias> getEstrategiaPorProyectos(Proyectos proyectos, Short ejercicio, Short area) {
        return facadePoa.getEntityManager().createQuery("SELECT e FROM Estrategias e INNER JOIN e.cuadroMandoIntegralList cm INNER JOIN cm.proyecto p INNER JOIN cm.ejercicioFiscal ef INNER JOIN cm.actividadesPoaList ap WHERE p.proyecto=:proyecto AND ef.ejercicioFiscal=:ejercicioFiscal AND ap.area=:area ORDER BY e.estrategia", Estrategias.class)
                .setParameter("proyecto", proyectos.getProyecto())
                .setParameter("ejercicioFiscal", ejercicio)
                .setParameter("area", area)
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Estrategias> getEstrategiaProyectos(Proyectos proyectos, Short ejercicio) {
        return facadePoa.getEntityManager().createQuery("SELECT e FROM Estrategias e INNER JOIN e.cuadroMandoIntegralList cm INNER JOIN cm.proyecto p INNER JOIN cm.ejercicioFiscal ef WHERE p.proyecto=:proyecto AND ef.ejercicioFiscal=:ejercicioFiscal ORDER BY e.estrategia", Estrategias.class)
                .setParameter("proyecto", proyectos.getProyecto())
                .setParameter("ejercicioFiscal", ejercicio)
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    //  -------------------------------------------- Lineas Accion -------------------------------------------------
    @Override
    public LineasAccion mostrarLineaAccion(Short clave) {
        facadePoa.setEntityClass(LineasAccion.class);
        LineasAccion pr = facadePoa.getEntityManager().find(LineasAccion.class, clave);
        if (pr == null) {
            return null;
        } else {
            return pr;
        }
    }

    @Override
    public List<LineasAccion> mostrarLineasAccionRegistroParametros(Short area, Short ejerciciosFiscales, EjesRegistro ejesRegistro, Estrategias estrategias) {
        TypedQuery<LineasAccion> q = em.createQuery("SELECT l FROM ActividadesPoa ap INNER JOIN  ap.cuadroMandoInt cm  INNER JOIN cm.lineaAccion l INNER JOIN cm.estrategia e INNER JOIN cm.ejercicioFiscal ef INNER JOIN cm.eje ej WHERE ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal AND e.estrategia=:estrategia AND ap.area=:area GROUP BY l.lineaAccion", LineasAccion.class);
        q.setParameter("area", area);
        q.setParameter("eje", ejesRegistro.getEje());
        q.setParameter("ejercicioFiscal", ejerciciosFiscales);
        q.setParameter("estrategia", estrategias.getEstrategia());
        List<LineasAccion> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<LineasAccion> mostrarLineasAccionRegistro(Short ejerciciosFiscales, EjesRegistro ejesRegistro, Estrategias estrategias) {
        TypedQuery<LineasAccion> q = em.createQuery("SELECT l FROM CuadroMandoIntegral cm INNER JOIN cm.lineaAccion l INNER JOIN cm.estrategia e INNER JOIN cm.ejercicioFiscal ef INNER JOIN cm.eje ej WHERE ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal AND e.estrategia=:estrategia GROUP BY l.lineaAccion", LineasAccion.class);
        q.setParameter("eje", ejesRegistro.getEje());
        q.setParameter("ejercicioFiscal", ejerciciosFiscales);
        q.setParameter("estrategia", estrategias.getEstrategia());
        List<LineasAccion> pr = q.getResultList();
        return pr;
    }
//  -------------------------------------------- UnidadMedidas --------------------------------------------------

    @Override
    public List<UnidadMedidas> mostrarUnidadMedidases() {
        facadePoa.setEntityClass(UnidadMedidas.class);
        return facadePoa.findAll();
    }

    @Override
    public UnidadMedidas agregarUnidadMedidas(UnidadMedidas nuevaUnidadMedidas) {
        facadePoa.setEntityClass(UnidadMedidas.class);
        facadePoa.create(nuevaUnidadMedidas);
        facadePoa.flush();
        return nuevaUnidadMedidas;
    }

//  ------------------------------------------CuadroMandoIntegral -----------------------------------------------
    @Override
    public List<CuadroMandoIntegral> mostrarCuadroMandoIntegralRegistrpo(Short ejercicioFiscal, EjesRegistro ejesRegistro, Estrategias estrategias, LineasAccion lineasAccion) {
        TypedQuery<CuadroMandoIntegral> q = em.createQuery("SELECT c FROM CuadroMandoIntegral c INNER JOIN c.ejercicioFiscal ef INNER JOIN c.eje ej INNER JOIN c.estrategia es INNER JOIN c.lineaAccion la WHERE ef.ejercicioFiscal = :ejercicioFiscal AND ej.eje=:eje AND es.estrategia=:estrategia AND la.lineaAccion=:lineaAccion", CuadroMandoIntegral.class);
        q.setParameter("ejercicioFiscal", ejercicioFiscal);
        q.setParameter("eje", ejesRegistro.getEje());
        q.setParameter("estrategia", estrategias.getEstrategia());
        q.setParameter("lineaAccion", lineasAccion.getLineaAccion());
        List<CuadroMandoIntegral> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<CuadroMandoIntegral> mostrarCuadroMandoIntegrals(Short ejercicioFiscal) {
        TypedQuery<CuadroMandoIntegral> q = em.createQuery("SELECT c FROM CuadroMandoIntegral c INNER JOIN c.ejercicioFiscal e WHERE e.ejercicioFiscal = :ejercicioFiscal", CuadroMandoIntegral.class);
        q.setParameter("ejercicioFiscal", ejercicioFiscal);
        List<CuadroMandoIntegral> pr = q.getResultList();
        return pr;
    }

//  ------------------------------------------Proyectos -----------------------------------------------    
    @Override
    public List<Proyectos> getProyectosPorEje(EjesRegistro eje, Short ejercicio, Short area) {
        return facadePoa.getEntityManager().createQuery("SELECT p FROM Proyectos p INNER JOIN p.cuadroMandoIntegralList cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef INNER JOIN cm.actividadesPoaList ap WHERE ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal AND ap.area=:area ORDER BY p.proyecto", Proyectos.class)
                .setParameter("eje", eje.getEje())
                .setParameter("ejercicioFiscal", ejercicio)
                .setParameter("area", area)
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Proyectos> getProyectos(EjesRegistro eje, Short ejercicio) {
        return facadePoa.getEntityManager().createQuery("SELECT p FROM Proyectos p INNER JOIN p.cuadroMandoIntegralList cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef WHERE ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal ORDER BY p.proyecto", Proyectos.class)
                .setParameter("eje", eje.getEje())
                .setParameter("ejercicioFiscal", ejercicio)
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

//  ------------------------------------------Registros -----------------------------------------------
    @Override
    public List<Registros> mostrarRegistrosActividad(ActividadesPoa actividadesPoa) {
        TypedQuery<Registros> q = em.createQuery("SELECT r FROM Registros r INNER JOIN r.actividadesPoaList a WHERE a.actividadPoa=:actividadPoa", Registros.class);
        q.setParameter("actividadPoa", actividadesPoa.getActividadPoa());
        List<Registros> pr = q.getResultList();
        return pr;
    }
  
    //  ------------------------------------------EjerciciosFiscales -----------------------------------------------
    @Override
    public EjerciciosFiscales mostrarEjercicioFiscaleses(Short idE) {
        TypedQuery<EjerciciosFiscales> q = em.createQuery("SELECT r FROM EjerciciosFiscales r WHERE r.ejercicioFiscal=:ejercicioFiscal", EjerciciosFiscales.class);
        q.setParameter("ejercicioFiscal", idE);
        List<EjerciciosFiscales> pr = q.getResultList();
        if (pr.isEmpty()) {
            return new EjerciciosFiscales();
        } else {
            return pr.get(0);
        }
    }
    
    @Override
    public EjerciciosFiscales mostrarEjercicioFiscalAnio(Short anio) {
        TypedQuery<EjerciciosFiscales> q = em.createQuery("SELECT r FROM EjerciciosFiscales r WHERE r.anio=:anio", EjerciciosFiscales.class);
        q.setParameter("anio", anio);
        List<EjerciciosFiscales> pr = q.getResultList();
        if (pr.isEmpty()) {
            return new EjerciciosFiscales();
        } else {
            return pr.get(0);
        }
    }
    
    @Override
    public List<EjerciciosFiscales> mostrarEjercicioFiscalesesTotales() {
        TypedQuery<EjerciciosFiscales> q = em.createQuery("SELECT e FROM EjerciciosFiscales e", EjerciciosFiscales.class);
        List<EjerciciosFiscales> pr = q.getResultList();
        return pr;
    }
    
    public String getReporteCuadroMandoPOA(Short ejeFiscal,List<DtoCmi.ReporteCuatrimestralAreas> reporte) throws Throwable{
        String rutaPlantilla = ejbCarga.crearDirectorioReportePOA();
        String rutaPlantillaC = ejbCarga.crearDirectorioReportePOACompleto(ejeFiscal.toString());
        String plantilla = rutaPlantilla.concat(REPORTE_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(REPORTE_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("resumen", reporte);
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);
        return plantillaC;
    }
//  ------------------------------------------DTOreportePoa -----------------------------------------------    
    @Override
    public List<DTOreportePoa.RecursoActividad> getPresupuestacionPOA(ActividadesPoa ap){
        List<DTOreportePoa.RecursoActividad> pas= new ArrayList<>();
        
        TypedQuery<RecursosActividad> q = em.createQuery("SELECT ra FROM RecursosActividad ra INNER JOIN ra.actividadPoa ap WHERE ap.actividadPoa=:actividadPoa", RecursosActividad.class);
        q.setParameter("actividadPoa", ap.getActividadPoa());
        List<RecursosActividad> pr = q.getResultList();
        
        if(!pr.isEmpty()){
            pr.forEach((ra) -> {
                pas.add(new DTOreportePoa.RecursoActividad(ra, ra.getProductoArea(), ra.getProductoArea().getProductos(), ra.getProductoArea().getPartida(), ra.getProductoArea().getCapitulo()));
            });
        }else{
            return new ArrayList<>();
        }        
        return pas;
    }

}
