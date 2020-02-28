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
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMaterias;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
//import mx.edu.utxj.pye.sgi.entity.logueo.Areas;
import mx.edu.utxj.pye.sgi.entity.prontuario.Listaperiodosescolares;
import mx.edu.utxj.pye.sgi.entity.pye2.*;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;

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
    
    @EJB EjbModulos ejbModulos;
    
    @Getter @Setter List<Short> areas;

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
//        System.out.println("select item 360: " + lp360);
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
        TypedQuery<Estado> q = f.getEntityManager().createQuery("SELECT e from Estado e WHERE e.idpais.idpais = :pais ORDER BY e.nombre ASC", Estado.class);
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

    public List<Estado> getEstadoPorPais(Integer pais){
        TypedQuery<Estado> q = f.getEntityManager().createQuery("SELECT e from Estado e WHERE e.idpais.idpais = :pais ORDER BY e.nombre ASC", Estado.class);
        q.setParameter("pais", pais);
        List<Estado> le = q.getResultList();
        if (le.isEmpty() || le == null) {
            System.err.println("no se encontraron estados ligados a este pais");
            return null;
        } else {
            return le;
        }
    }

    @Override
    public List<SelectItem> itemEstadoByClave(Integer pais) {
        List<SelectItem> lse = new ArrayList<>();
        getEstadoPorPais(pais).stream()
                .map((e)-> new SelectItem(e.getIdestado(),e.getNombre()))
                .forEachOrdered((selectItem -> {
                    lse.add(selectItem);
                }));

        return  lse;
    }

    public List<Municipio> getMunicipiosPorEstado(Integer estado) {
        TypedQuery<Municipio> q = f.getEntityManager().createQuery("SELECT m FROM Municipio m  WHERE m.municipioPK.claveEstado = :estado ORDER BY m.nombre ASC", Municipio.class);
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

    public List<Localidad> getLocalidadPorMunicipio(Integer estado, Integer municipio){
        TypedQuery<Localidad> q = f.getEntityManager().createQuery("SELECT l FROM Localidad l WHERE l.localidadPK.claveEstado = :estado AND l.localidadPK.claveMunicipio = :municipio ORDER BY l.nombre ASC",Localidad.class);
        q.setParameter("estado",estado);
        q.setParameter("municipio",municipio);
        List<Localidad> ll = q.getResultList();
        if (ll.isEmpty() || ll == null) {
            System.err.println("no se encontraron localidades ligados a este municipio");
            return null;
        }else{
            return  ll;
        }
    }

    @Override
    public List<SelectItem> itemLocalidadesByClave(Integer estado, Integer municipio) {
        List<SelectItem> lsl = new ArrayList<>();
        getLocalidadPorMunicipio(estado,municipio).stream()
                .map((l)-> new SelectItem(l.getLocalidadPK().getClaveLocalidad(),l.getNombre()))
                .forEachOrdered((selectItem -> {
                    lsl.add(selectItem);
                }));

        return lsl;
    }

    @Override
    public List<Asentamiento> itemAsentamiento() {
        return f.getEntityManager().createQuery("SELECT a FROM Asentamiento a",Asentamiento.class)
                .getResultList();
    }

    public List<Asentamiento> getAsentamientoPorEstadoMunicipio(Integer Estado, Integer munipio){
        return f.getEntityManager().createQuery("SELECT a FROM Asentamiento a WHERE a.asentamientoPK.estado = :cveEstado AND a.asentamientoPK.municipio = :cveMunicipio ORDER BY a.nombreAsentamiento ASC")
                .setParameter("cveEstado", Estado)
                .setParameter("cveMunicipio", munipio)
                .getResultList();
    }

    @Override
    public List<SelectItem> itemAsentamientoByClave(Integer Estado, Integer munipio) {
        List<SelectItem> lsa = new ArrayList<>();
        getAsentamientoPorEstadoMunicipio(Estado,munipio).stream()
                .map(asentamiento -> new SelectItem(asentamiento.getAsentamientoPK().getAsentamiento(),asentamiento.getCodigoPostal().concat("-").concat(asentamiento.getNombreAsentamiento())))
                .forEachOrdered(selectItem -> lsa.add(selectItem));
        return lsa;
    }

    public List<Pais> getPaises(){
        TypedQuery<Pais> q = f.getEntityManager().createQuery("SELECT  p FROM Pais p  WHERE p.idpais <> 42 ORDER BY p.nombre ASC", Pais.class);
        List<Pais> lp = q.getResultList();
        if(lp.isEmpty() || lp == null){
            System.err.println("no se encontraron paises");
            return  null;
        }else{
            return  lp;
        }
    }

    @Override
    public List<SelectItem> itemPaises() {
        List<SelectItem> lsp = new ArrayList<>();
        getPaises().stream()
                .map((p)-> new SelectItem(p.getIdpais(), p.getNombre()))
                .forEachOrdered((selectItem -> {
                    lsp.add(selectItem);
                }));

        return lsp;
    }

    public List<Pais> getPaisMexico(){
        TypedQuery<Pais> q = f.getEntityManager().createQuery("SELECT  p FROM Pais p  WHERE p.idpais = 42", Pais.class);
        List<Pais> lpm = q.getResultList();
        if(lpm.isEmpty() || lpm == null){
            System.err.println("no se encontraron paises");
            return  null;
        }else{
            return lpm;
        }
    }

    @Override
    public List<SelectItem> itemPaisMexico() {
        List<SelectItem> lspm = new ArrayList<>();
        getPaisMexico().stream()
                .map((p)-> new SelectItem(p.getIdpais(), p.getNombre(),p.getNombre()))
                .forEachOrdered((selectItem -> {
                    lspm.add(selectItem);
                }));

        return lspm;
    }

    @Override
    public Integer itemCvePais(Integer idEstado) {
        Estado estado = new Estado();
        estado = f.getEntityManager().createQuery("SELECT e FROM Estado e WHERE e.idestado = :estado", Estado.class)
                .setParameter("estado", idEstado)
                .getSingleResult();
        
        return  estado.getIdpais().getIdpais();
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
            List<Registros> qr = new ArrayList<>();
            
            if (areaSeleccionada == 6 || areaSeleccionada == 9) {
                qr = f.getEntityManager().createQuery("SELECT r FROM Registros r WHERE r.tipo.registroTipo = :tipo ORDER BY r.eventoRegistro.eventoRegistro DESC", Registros.class)
                .setParameter("tipo", tipo)
                .getResultList();
                        
            } else {
                areas = ejbModulos.getAreasDependientes(areaSeleccionada);
                qr = f.getEntityManager().createQuery("SELECT r FROM Registros r WHERE r.tipo.registroTipo = :tipo AND r.area IN :areas ORDER BY r.eventoRegistro.eventoRegistro DESC", Registros.class)
                .setParameter("tipo", tipo)
                .setParameter("areas", areas)
                .getResultList();
            }
            if (qr == null || qr.isEmpty()) {
                return null;
            } else {
                return qr;
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
            List<Registros> qr = new ArrayList<>();
            
            if (areaSeleccionada == 6 || areaSeleccionada == 9) {
                qr = f.getEntityManager().createQuery("SELECT r FROM Registros r WHERE r.tipo.registroTipo = :tipo AND r.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio ORDER BY r.eventoRegistro.eventoRegistro DESC", Registros.class)
                        .setParameter("tipo", tipo)
                        .setParameter("ejercicio", ejercicio)
                        .getResultList();

            } else {
                areas = ejbModulos.getAreasDependientes(areaSeleccionada);

                qr = f.getEntityManager().createQuery("SELECT r FROM Registros r WHERE r.tipo.registroTipo = :tipo AND r.area IN :areas AND r.eventoRegistro.ejercicioFiscal.ejercicioFiscal = :ejercicio ORDER BY r.eventoRegistro.eventoRegistro DESC", Registros.class)
                        .setParameter("tipo", tipo)
                        .setParameter("areas", areas)
                        .setParameter("ejercicio", ejercicio)
                        .getResultList();
            }
            if (qr == null || qr.isEmpty()) {
                return null;
            } else {
                return qr;
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
    
    public List<AreasUniversidad> getProgramasEducativos() {
        TypedQuery<AreasUniversidad> q = f.getEntityManager().createQuery("SELECT a from AreasUniversidad a WHERE a.categoria.categoria = :categoria AND a.vigente = :vigente ORDER BY a.nivelEducativo.nivel DESC, a.nombre ASC", AreasUniversidad.class);
        q.setParameter("categoria", 9);
        q.setParameter("vigente", "1");
        List<AreasUniversidad> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            return l;
        }
    }

    @Override
    public List<SelectItem> itemProgramasEducativos() {
        List<SelectItem> lpe = new ArrayList<>();
        for(AreasUniversidad a: getProgramasEducativos()){
            lpe.add(new SelectItem(a.getArea(), a.getNombre(), a.getNombre()));
        }
        return lpe;
    }
    
    public List<Iems> getIemsByEstadoMunicipioLocalidad(Integer estado, Integer municipio, Integer localidad){
        return f.getEntityManager().createQuery("SELECT i FROM Iems i WHERE i.localidad.localidadPK.claveEstado = :estado AND i.localidad.localidadPK.claveMunicipio = :municipio AND i.localidad.localidadPK.claveLocalidad = :localidad ORDER BY i.nombre ASC", Iems.class)
                .setParameter("estado", estado)
                .setParameter("municipio", municipio)
                .setParameter("localidad", localidad)
                .getResultList();
    }
    
    @Override
    public List<SelectItem> itemIems(Integer estado, Integer municipio, Integer localidad) {
        List<SelectItem> lsi = new ArrayList<>();
        getIemsByEstadoMunicipioLocalidad(estado, municipio, localidad).stream()
                .map(iem -> new SelectItem(iem.getIems(), iem.getTurno().concat("-").concat(iem.getNombre())))
                .forEachOrdered(selectItem -> {
                    lsi.add(selectItem);
                });
        return lsi;
    }
    
     public List<AreasUniversidad> getProgramasEducativosByArea(Short area){
        return f.getEntityManager().createQuery("SELECT au FROM AreasUniversidad au WHERE au.areaSuperior = :idArea AND au.nivelEducativo.nivel = 'TSU' AND au.vigente = 1",AreasUniversidad.class)
                .setParameter("idArea", area)
                .getResultList();
    }

    @Override
    public List<SelectItem> itemProgramEducativoPorArea(Short area) {
        List<SelectItem> slpe = new ArrayList<>();
        getProgramasEducativosByArea(area).stream()
                .map(programasEducativos -> new SelectItem(programasEducativos.getArea(),programasEducativos.getNombre()))
                .forEachOrdered(selectItem -> {
                    slpe.add(selectItem);
                });
        return slpe;
    }
    
    @Override
    public List<SelectItem> itemAreaAcademica() {
        List<SelectItem> lsaa = new ArrayList<>();
        getProgramasEducativos2().stream()
                .map(a -> new SelectItem(a.getArea(),a.getNombre()))
                .forEachOrdered(selectItem -> {
                    lsaa.add(selectItem);
                });
        return lsaa;
    }
    
     public List<AreasUniversidad> getProgramasEducativos2(){
        return f.getEntityManager().createQuery("SELECT au FROM AreasUniversidad au WHERE au.categoria.categoria = 8",AreasUniversidad.class)
                .getResultList();
    }

}
