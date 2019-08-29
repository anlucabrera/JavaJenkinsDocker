/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativos;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.facade.controlEscolar.FacadeCE;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@Stateless
public class ServicioSelectItemCE implements EjbSelectItemCE {

    @EJB FacadeCE facadeCE;
    private EntityManager em;

    @PostConstruct
    public void init() {
        em = facadeCE.getEntityManager();
    }
    
    public List<TipoSangre> getTipoSangre(){
        return  em.createQuery("SELECT ts FROM  TipoSangre ts",TipoSangre.class)
                .getResultList();
    }

    @Override
    public List<SelectItem> itemTipoSangre() {
        List<SelectItem> slts = new ArrayList<>();
        getTipoSangre().stream()
                .map((ts)-> new SelectItem(ts, ts.getNombre()))
                .forEachOrdered((selectItem -> slts.add(selectItem)));
        return slts;
    }

    public List<TipoDiscapacidad> getTipoDiscapacidad() {
        return em.createNamedQuery("TipoDiscapacidad.findAll", TipoDiscapacidad.class)
                .getResultList();
    }

    @Override
    public List<SelectItem> itemDiscapcidad() {
        List<SelectItem> sltd = new ArrayList<>();
        getTipoDiscapacidad().stream()
                .map((td)-> new SelectItem(td,td.getNombre(),td.getDescripcion()))
                .forEachOrdered((selectItem -> sltd.add(selectItem)));

        return sltd;
    }


    @Override
    public List<Ocupacion> itemOcupacion() {
        return em.createNamedQuery("Ocupacion.findAll", Ocupacion.class)
                .getResultList();
    }

    @Override
    public List<Escolaridad> itemEscolaridad() {
        return em.createNamedQuery("Escolaridad.findAll",Escolaridad.class)
                .getResultList();
    }

    public List<Iems> getIemsByEstadoMunicipioLocalidad(Integer estado, Integer municipio, Integer localidad){
        return em.createQuery("SELECT i FROM Iems i WHERE i.localidad.localidadPK.claveEstado = :estado AND i.localidad.localidadPK.claveMunicipio = :municipio AND i.localidad.localidadPK.claveLocalidad = :localidad", Iems.class)
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

    @Override
    public List<EspecialidadCentro> itemEspecialidadCentro() {
        return em.createQuery("SELECT e FROM EspecialidadCentro e",EspecialidadCentro.class)
                .getResultList();
    }

    public List<AreasUniversidad> getProgramasEducativos(){
        return em.createQuery("SELECT au FROM AreasUniversidad au WHERE au.categoria.categoria = 8",AreasUniversidad.class)
                .getResultList();
    }

    @Override
    public List<SelectItem> itemAreaAcademica() {
        List<SelectItem> lsaa = new ArrayList<>();
        getProgramasEducativos().stream()
                .map(a -> new SelectItem(a.getArea(),a.getNombre()))
                .forEachOrdered(selectItem -> {
                    lsaa.add(selectItem);
                });
        return lsaa;
    }

    public List<AreasUniversidad> getProgramasEducativosByArea(Short area){
        return em.createQuery("SELECT au FROM AreasUniversidad au WHERE au.areaSuperior = :idArea AND au.nivelEducativo.nivel = 'TSU' AND au.vigente = 1",AreasUniversidad.class)
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

    public List<ProgramasEducativos> getProgramasEducativoAll(){
        return em.createQuery("SELECT pe FROM ProgramasEducativos pe WHERE pe.nivel.nivel = 'TSU' AND pe.activo = 1",ProgramasEducativos.class)
                .getResultList();
    }

    @Override
    public List<SelectItem> itemProgramaEducativoAll() {
        List<SelectItem> slpea = new ArrayList<>();
        getProgramasEducativoAll().stream()
                .map(programasEducativos -> new SelectItem(programasEducativos.getSiglas(),programasEducativos.getNivel().getNombre().concat(" en ").concat(programasEducativos.getNombre())))
                .forEachOrdered(selectItem -> {
                    slpea.add(selectItem);
                });

        return  slpea;
    }

    @Override
    public List<Turno> itemTurno() {
        return em.createQuery("SELECT t FROM Turno t",Turno.class)
                .getResultList();
    }

    @Override
    public List<AreasUniversidad> itemPEAll() {
        return em.createQuery("SELECT au FROM AreasUniversidad au WHERE au.nivelEducativo.nivel = 'TSU' AND au.vigente = 1",AreasUniversidad.class)
                .getResultList();
    }

    @Override
    public List<Sistema> itemSistema() {
        return em.createNamedQuery("Sistema.findAll", Sistema.class)
                .getResultList();
    }

    @Override
    public List<LenguaIndigena> itemLenguaIndigena() {
        return em.createNamedQuery("LenguaIndigena.findAll", LenguaIndigena.class)
                .getResultList();
    }

    @Override
    public List<MedioDifusion> itemMedioDifusion() {
        return em.createNamedQuery("MedioDifusion.findAll", MedioDifusion.class)
                .getResultList();
    }
}
