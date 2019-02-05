/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMaterias;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
//import mx.edu.utxj.pye.sgi.entity.logueo.Areas;
import mx.edu.utxj.pye.sgi.entity.prontuario.Listaperiodosescolares;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioSelectItems implements EJBSelectItems {

    @EJB
    Facade f;
    
    @Inject
    LogonMB logonMB;

    @Override
    public List<SelectItem> itemsEvaluacionesDirectivos() {
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("1", "360", "Evaluación de 360°"));
        l.add(new SelectItem("2", "Desempeño", "Evaluación de desempeño"));

        return l;
    }

    @Override
    public List<SelectItem> itemsEvaluacionDirectores() {
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("1", "360", "Evaluación de 360°"));
        l.add(new SelectItem("2", "Desempeño", "Evaluación de desempeño"));
        l.add(new SelectItem("3", "Tutor", "Evaluación al tutor"));
        l.add(new SelectItem("4", "Docente", "Evaluación docente"));

        return l;
    }

    @Override
    public List<SelectItem> itemsEvaluacionPersonal() {
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("1", "360", "Evaluación de 360°"));
        l.add(new SelectItem("2", "Desempeño", "Evaluación de desempeño"));
//        l.add(new SelectItem("3", "Tutor", "Evaluación al tutor"));
        l.add(new SelectItem("4", "Docente", "Evaluación docente"));

        return l;
    }

    @Override
    public List<SelectItem> itemsEvaluacionSecretariaAcademica() {
        List<SelectItem> l = new ArrayList<>();
//        l.add(new SelectItem("1", "360", "Evaluación de 360°"));
//        l.add(new SelectItem("2", "Desempeño", "Evaluación de desempeño"));
//        l.add(new SelectItem("3", "Tutor", "Evaluación al tutor"));
        l.add(new SelectItem("4", "Docente", "Evaluación docente"));

        return l;
    }

    public List<Listaperiodosescolares> getItemsPeriodoEscolar() {
        TypedQuery<Listaperiodosescolares> q = f.getEntityManager().createQuery("SELECT l FROM Listaperiodosescolares l ", Listaperiodosescolares.class);
        return q.getResultList();
    }

    @Override
    public List<SelectItem> itemsPeriodos() {
        List<SelectItem> lip = new ArrayList<>();
        for (Listaperiodosescolares per : getItemsPeriodoEscolar()) {
            lip.add(new SelectItem(per.getPeriodo(), per.getMesInicio() + "-" + per.getMesFin() + " del año " + per.getAnio(), "Periodo " + per.getMesInicio() + "-" + per.getMesFin() + " de evaluacion en el ciclo escolar " + per.getAnio()));
        }
        return lip;
    }

    public List<Evaluaciones360> getItemsPeriodos360() {
        TypedQuery<Evaluaciones360> q = f.getEntityManager().createQuery("Select e from Evaluaciones360 as e", Evaluaciones360.class);
        return q.getResultList();
    }

    @Override
    public List<SelectItem> itemsPeriodos360() {
        List<SelectItem> lp360 = new ArrayList<>();
        for (Evaluaciones360 ev : getItemsPeriodos360()) {
//            Integer periodo = ev.getPeriodo();
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioSelectItems.itemsPeriodos360() periodo : " + periodo);
            TypedQuery<Listaperiodosescolares> q = f.getEntityManager().createQuery("SELECT p from Listaperiodosescolares as p WHERE p.periodo = :periodo ORDER BY p.periodo DESC", Listaperiodosescolares.class);
            q.setParameter("periodo", ev.getPeriodo());
//            System.out.println("resultado: " + q.getResultList());
            Listaperiodosescolares lp = q.getSingleResult();
            lp360.add(new SelectItem(ev.getPeriodo(), lp.getMesInicio() + "-" + lp.getMesFin() + " " + lp.getAnio(), "Periodo : " + lp.getMesInicio() + "-" + lp.getMesFin() + " " + lp.getAnio()));
        }
        System.out.println("select item 360: " + lp360);
        return lp360;
    }

    public List<DesempenioEvaluaciones> getItemsPeriodoDesempenio() {
        TypedQuery<DesempenioEvaluaciones> q = f.getEntityManager().createQuery("Select d from DesempenioEvaluaciones as d ", DesempenioEvaluaciones.class);
        return q.getResultList();
    }

    @Override
    public List<SelectItem> itemPeriodosDesempenio() {
        List<SelectItem> lpd = new ArrayList<>();
        for (DesempenioEvaluaciones des : getItemsPeriodoDesempenio()) {
            TypedQuery<Listaperiodosescolares> q = f.getEntityManager().createQuery("SELECT p from Listaperiodosescolares as p WHERE p.periodo = :periodo", Listaperiodosescolares.class);
            q.setParameter("periodo", des.getPeriodo());
            Listaperiodosescolares lp = q.getSingleResult();
            lpd.add(new SelectItem(des.getPeriodo(), lp.getMesInicio() + "-" + lp.getMesFin() + " " + lp.getAnio(), "Periodo : " + lp.getMesInicio() + "-" + lp.getMesFin() + " " + lp.getAnio()));
        }
        System.out.println("select item desempenio : " + lpd);
        return lpd;
    }

    public List<EvaluacionDocentesMaterias> getItemsDocenteMaterias() {
        TypedQuery<EvaluacionDocentesMaterias> q = f.getEntityManager().createQuery("SELECT e FROM EvaluacionDocentesMaterias e  ", EvaluacionDocentesMaterias.class);
        return q.getResultList();
    }

    @Override
    public List<SelectItem> itemPeriodosDocenteMateria() {
        List<SelectItem> ldmp = new ArrayList<>();
        for (EvaluacionDocentesMaterias ev : getItemsDocenteMaterias()) {
            TypedQuery<Listaperiodosescolares> q = f.getEntityManager().createQuery("SELECT p from Listaperiodosescolares as p WHERE p.periodo = :periodo", Listaperiodosescolares.class);
            q.setParameter("periodo", ev.getPeriodo());
            Listaperiodosescolares lp = q.getSingleResult();
            ldmp.add(new SelectItem(ev.getPeriodo(), lp.getMesInicio() + "-" + lp.getMesFin() + " " + lp.getAnio(), "Periodo : " + lp.getMesInicio() + "-" + lp.getMesFin() + " " + lp.getAnio()));
        }
        return ldmp;
    }

    public List<Evaluaciones> getItemPeriodosEvaluaciones(String tipo) {
        TypedQuery<Evaluaciones> q = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo = :tipo", Evaluaciones.class);
        q.setParameter("tipo", tipo);
//        System.out.println("las evaluaciones : " + q.getResultList());
        return q.getResultList();
    }

    @Override
    public List<SelectItem> itemPeriodosEvaluaciones(String tipo) {
        List<SelectItem> lpe = new ArrayList<>();
        for (Evaluaciones ev : getItemPeriodosEvaluaciones(tipo)) {
//            System.out.println("Tipo de evaluacion que se envia : " + tipo);
//            System.out.println("periodo de la evaluacion : " + ev.getPeriodo());
            TypedQuery<Listaperiodosescolares> q = f.getEntityManager().createQuery("SELECT p from Listaperiodosescolares as p WHERE p.periodo = :periodo", Listaperiodosescolares.class);
            q.setParameter("periodo", ev.getPeriodo());
            Listaperiodosescolares lp = q.getSingleResult();
            lpe.add(new SelectItem(ev.getPeriodo(), lp.getMesInicio() + "-" + lp.getMesFin() + " " + lp.getAnio(), "Periodo : " + lp.getMesInicio() + "-" + lp.getMesFin() + " " + lp.getAnio()));
//            System.out.println("selectItem periodos Genereal por evaluacion de tipo : " + tipo + " -> " + lpe);
        }
        return lpe;
    }

    public List<AreasUniversidad> getAreasAcademicas() {
        TypedQuery<AreasUniversidad> q = f.getEntityManager().createQuery("SELECT a from AreasUniversidad a WHERE a.categoria.categoria = :categoria", AreasUniversidad.class);
        q.setParameter("categoria", 8);
        System.out.println("Listado de areas academicas : " + q.getResultList());
        List<AreasUniversidad> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            return l;
        }
    }

    @Override
    public List<SelectItem> itemAreasAcademicas() {
        List<SelectItem> lac = new ArrayList<>();
        for(AreasUniversidad a: getAreasAcademicas()){
            lac.add(new SelectItem(a.getArea(), a.getSiglas(), a.getNombre()));
        }
        return lac;
    }
    
    public List<Estado> getEstados() {
        TypedQuery<Estado> q = f.getEntityManager().createQuery("SELECT e from Estado e WHERE e.idpais.idpais = :pais", Estado.class);
        q.setParameter("pais", 42);
        List<Estado> le = q.getResultList();
        if (le.isEmpty() || le == null) {
            System.err.println("no se encontraron estados ligados a este pais");
            return null;
        } else {
            return le;
        }
    }

    @Override
    public List<SelectItem> itemEstados() {
        List<SelectItem> lse = new ArrayList<>();
        for (Estado e : getEstados()) {
            lse.add(new SelectItem(e.getIdestado(), e.getNombre(), e.getNombre()));
        }
        return lse;
    }

    public List<Municipio> getMunicipiosPorEstado(Integer estado) {
        TypedQuery<Municipio> q = f.getEntityManager().createQuery("SELECT m FROM Municipio m  WHERE m.municipioPK.claveEstado = :estado", Municipio.class);
        q.setParameter("estado", estado);
        List<Municipio> lm = q.getResultList();
        if (lm.isEmpty() || lm == null) {
            System.err.println("no se encontraron estados ligados a este municipio");
            return null;
        }else{
            return lm;
        }
    }

    @Override
    public List<SelectItem> itemMunicipiosByClave(Integer estado) {
        List<SelectItem> lsm = new ArrayList<>();
        for(Municipio m : getMunicipiosPorEstado(estado)){
            lsm.add( new SelectItem(m.getMunicipioPK().getClaveMunicipio(), m.getNombre(), m.getNombre()));
        }
        return lsm;
    }

    
    @Override
    public List<SelectItem> itemLocalidadesByClave(Integer estado, Integer municipio) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<CiclosEscolares> getCiclos() {
        TypedQuery<CiclosEscolares> q = f.getEntityManager().createQuery("SELECT c FROM CiclosEscolares c ORDER BY c.ciclo DESC", CiclosEscolares.class);
        List<CiclosEscolares> le = q.getResultList();
        if (le.isEmpty() || le == null) {
            System.err.println("no existe el ciclo escolar");
            return null;
        } else {
            return le;
        }
    }

    @Override
    public List<SelectItem> itemCiclos() {
        List<SelectItem> lse = new ArrayList<>();
        for (CiclosEscolares pe : getCiclos()) {
            SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy");
            
            lse.add(new SelectItem(pe.getCiclo(), formatoDeFecha.format(pe.getInicio()) + "-" + formatoDeFecha.format(pe.getFin()), formatoDeFecha.format(pe.getInicio()) + "-" + formatoDeFecha.format(pe.getFin())));
        }
        return lse;
    }
    
    public List<Listaperiodosescolares> getPeriodosPorCiclo(Integer ciclo) {
        TypedQuery<Listaperiodosescolares> q = f.getEntityManager().createQuery("SELECT l FROM Listaperiodosescolares l WHERE l.ciclo = :ciclo",  Listaperiodosescolares.class);
        q.setParameter("ciclo", ciclo);
        List<Listaperiodosescolares> lp = q.getResultList();
        if (lp.isEmpty() || lp == null) {
            System.err.println("no se encontraron periodos ligados a este ciclo");
            return null;
        }else{
            return lp;
        }
    }

    @Override
    public List<SelectItem> itemPeriodosByClave(Integer ciclo) {
        List<SelectItem> lsp = new ArrayList<>();
        for(Listaperiodosescolares p : getPeriodosPorCiclo(ciclo)){
            lsp.add( new SelectItem(p.getPeriodo(), p.getMesInicio() + "-" + p.getMesFin(),  p.getMesInicio() + "-" + p.getMesFin()));
        }
        return lsp;
    }
    
     static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public List<Registros> getListaRegistros(Short tipo) {
        TypedQuery<Registros> qr = f.getEntityManager().createQuery("SELECT r FROM Registros r WHERE r.tipo.registroTipo = :tipo", Registros.class);
        qr.setParameter("tipo", tipo);
        List<Registros> lr = qr.getResultList();
        if (lr.isEmpty() || lr == null) {
            return null;
        } else {
            return lr;
        }
    }

    public List<Registros> getListaRegistrosAreaTipo(Short tipo) {
       TypedQuery<AreasUniversidad> qa = f.getEntityManager().createQuery("SELECT a from AreasUniversidad a", AreasUniversidad.class);
        List<AreasUniversidad> listaAreasConPoa = qa.getResultStream().filter(x -> (x.getTienePoa() && x.getArea().equals(logonMB.getPersonal().getAreaOperativa())
                || (x.getTienePoa() && x.getArea().equals(logonMB.getPersonal().getAreaOperativa())))).collect(Collectors.toList());

        if (listaAreasConPoa == null || listaAreasConPoa.isEmpty()) {
            return null;
        } else {

            listaAreasConPoa.forEach(x -> {
                //System.err.println("El personal es --->" + x);
            });
            Short areaSeleccionada = listaAreasConPoa.get(0).getArea();
            TypedQuery<Registros> qr = f.getEntityManager().createQuery("SELECT r FROM Registros r WHERE r.tipo.registroTipo = :tipo AND r.area = :area ORDER BY r.registro DESC", Registros.class);
            qr.setParameter("tipo", tipo);
            qr.setParameter("area", areaSeleccionada);
//            System.err.println("La lista de registros es : " + qr.getResultList() + " y su tamaño es : " + qr.getResultList().size());
            List<Registros> lr = qr.getResultList();
            if (lr == null || lr.isEmpty()) {
                return null;
            } else {
                return lr;
            }
        }
    }

    public List<Registros> getListaRegistrosAreaTipoEjercicio(Short tipo, Short ejercicio) {
        TypedQuery<AreasUniversidad> qa = f.getEntityManager().createQuery("SELECT a from AreasUniversidad a", AreasUniversidad.class);
        List<AreasUniversidad> listaAreasConPoa = qa.getResultStream().filter(x -> (x.getTienePoa() && x.getArea().equals(logonMB.getPersonal().getAreaOperativa())
                || (x.getTienePoa() && x.getArea().equals(logonMB.getPersonal().getAreaOperativa())))).collect(Collectors.toList());

        if (listaAreasConPoa == null || listaAreasConPoa.isEmpty()) {
            return null;
        } else {

            listaAreasConPoa.forEach(x -> {
                //System.err.println("El personal es --->" + x);
            });
            Short areaSeleccionada = listaAreasConPoa.get(0).getArea();
            TypedQuery<Registros> qr = f.getEntityManager().createQuery("SELECT r FROM Registros r WHERE r.tipo.registroTipo = :tipo AND r.area = :area AND r.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio ORDER BY r.registro DESC", Registros.class);
            qr.setParameter("tipo", tipo);
            qr.setParameter("area", areaSeleccionada);
            qr.setParameter("ejercicio", ejercicio);
            //System.err.println("La lista de registros es : " + qr.getResultList() + " y su tamaño es : " + qr.getResultList().size());
            List<Registros> lr = qr.getResultList();
            if (lr == null || lr.isEmpty()) {
                return null;
            } else {
                return lr;
            }
        }
    }

    @Override
    public List<SelectItem> itemMesesPorRegistro(Short tipo, Short ejercicio) {
        List<Registros> registrosArea = getListaRegistrosAreaTipoEjercicio(tipo, ejercicio);
        //System.err.println("lo registros del area son = : "+ registrosArea);
        List<SelectItem> ls = new ArrayList<>();
        if (registrosArea == null) {
//            ls.add(new SelectItem((short)1, "Aun no hay registros ligados a un mes"));
            return null;
        } else {
            for (Registros r : registrosArea) {
                ls.add(new SelectItem(r.getEventoRegistro().getMes(), r.getEventoRegistro().getMes()));
//                ls.sort(Comparator.comparing(SelectItem::getLabel));
            }
            
            return ls.stream().filter(distinctByKey(x -> x.getLabel())).collect(Collectors.toList());
        }
    }

    @Override
    public List<SelectItem> itemEjercicioFiscalPorRegistro(Short tipo) {
        List<Registros> registrosArea = getListaRegistrosAreaTipo(tipo);
//        System.err.println("lo registros del area son = : "+ registrosArea);
        List<SelectItem> ls = new ArrayList<>();
        if (registrosArea == null) {
//            ls.add(new SelectItem((short)1, "Aun no hay registros ligados a un ejercicio"));
            return null;
        } else {
            for (Registros r : registrosArea) {
                ls.add(new SelectItem(r.getEventoRegistro().getEjercicioFiscal().getEjercicioFiscal(), r.getEventoRegistro().getEjercicioFiscal().getAnio() + ""));
                
            }
            //System.err.println("se imprimen las areas del registro ");
            getListaRegistrosAreaTipo(tipo);
            
            return ls.stream().filter(distinctByKey(x -> x.getLabel())).collect(Collectors.toList());
             
        }
    }
    public List<Generaciones> getGeneraciones() {
        TypedQuery<Generaciones> q = f.getEntityManager().createQuery("SELECT g FROM Generaciones g ORDER BY g.generacion DESC", Generaciones.class);
        List<Generaciones> le = q.getResultList();
        if (le.isEmpty() || le == null) {
            //System.err.println("no existe el ciclo escolar");
            return null;
        } else {
            return le;
        }
    }

    @Override
    public List<SelectItem> itemGeneraciones() {
        List<SelectItem> lse = new ArrayList<>();
        for (Generaciones ge : getGeneraciones()) {
            lse.add(new SelectItem(ge.getGeneracion(), ge.getInicio() + "-" + ge.getFin(), ge.getInicio() + "-" + ge.getFin()));
        }
        return lse;
    }

}
