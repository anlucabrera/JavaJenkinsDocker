/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.ejb.pye;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistroEspecifico;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistroEspecificoPK;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.siip.dto.pye.DtoModulosRegistroUsuario;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbGestionModulosRegistro")
public class EjbGestionModulosRegistro {
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
     * Permite validar si el usuario autenticado es personal con permiso de administrador para el módulo correspondiente
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarAdministrador(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            if (p.getPersonal().getAreaOperativa()== 6 && p.getPersonal().getCategoriaOperativa().getCategoria()==18) {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("planeacionAreaOperativa").orElse(6)));
                filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorCategoriaOperativa").orElse(18)));
            }
            else if (p.getPersonal().getAreaOperativa()== 6 && p.getPersonal().getCategoriaOperativa().getCategoria()==48) {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("planeacionAreaOperativa").orElse(6)));
                filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorEncargadoCategoriaOperativa").orElse(48)));
            }
            else if (p.getPersonal().getAreaOperativa() == 6 && p.getPersonal().getAreaSuperior()== 6 && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            else if ((p.getPersonal().getClave()== 349 || p.getPersonal().getClave()== 511) && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbGestionModulosRegistro.validarAdministrador)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de ejes de registro
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EjesRegistro>> getEjesRegistro(){
        try{
            List<EjesRegistro> ejesRegistro = em.createQuery("SELECT e FROM EjesRegistro e", EjesRegistro.class)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(ejesRegistro, "Lista de ejes de registro activos.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de ejes de registro activos. (EjbGestionModulosRegistro.getEjesRegistro)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de tipos de módulo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<String>> getTiposModulo(){
        try{
            List<String> tiposModulo = new ArrayList<>();
            tiposModulo.add("Registro");
            tiposModulo.add("Seguimiento");
            
            return ResultadoEJB.crearCorrecto(tiposModulo, "Lista de tipos de módulo.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de tipos de módulo. (EjbGestionModulosRegistro.getTiposModulo)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de módulos de registro activos del eje de registro seleccionado
     * @param ejeRegistro
     * @param tipoModulo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<ModulosRegistro>> getModulosRegistroEje(EjesRegistro ejeRegistro, String tipoModulo){
        try{
            List<ModulosRegistro> modulosRegistro = new ArrayList<>();
            
            if(tipoModulo.equals("Registro")){
                modulosRegistro = em.createQuery("SELECT m FROM ModulosRegistro m WHERE m.estado=:estado AND m.eje.eje=:eje AND m.link not like concat(:pista,'%')", ModulosRegistro.class)
                    .setParameter("estado", "1")
                    .setParameter("eje", ejeRegistro.getEje())
                    .setParameter("pista", "pye")
                    .getResultStream()
                    .collect(Collectors.toList());
            }else{
                modulosRegistro = em.createQuery("SELECT m FROM ModulosRegistro m WHERE m.estado=:estado AND m.eje.eje=:eje AND m.link like concat(:pista,'%')", ModulosRegistro.class)
                    .setParameter("estado", "1")
                    .setParameter("eje", ejeRegistro.getEje())
                    .setParameter("pista",  "pye")    
                    .getResultStream()
                    .collect(Collectors.toList());
            }
            
            return ResultadoEJB.crearCorrecto(modulosRegistro.stream().sorted(Comparator.comparing(ModulosRegistro::getTituloPrincipal)).collect(Collectors.toList()), "Lista de módulos de registro activos del eje de registro seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de módulos de registro activos del eje de registro seleccionado. (EjbGestionModulosRegistro.getModulosRegistroEje)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de módulos de registro asignados
     * @param moduloRegistro
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoModulosRegistroUsuario>> getModulosRegistroAsignados(ModulosRegistro moduloRegistro){
        try{
           
            List<DtoModulosRegistroUsuario> listaModulosAsignados = em.createQuery("SELECT m FROM ModulosRegistroEspecifico m WHERE m.modulosRegistro.clave=:modulo", ModulosRegistroEspecifico.class)
                    .setParameter("modulo", moduloRegistro.getClave())
                    .getResultStream()
                    .map(moduloReg -> packModuloAsignado(moduloReg).getValor())
                    .sorted(DtoModulosRegistroUsuario::compareTo)
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaModulosAsignados, "Lista de módulos de registro asignados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de módulos de registro asignados. (EjbGestionModulosRegistro.getModulosRegistroAsignados)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de módulos de registro asignados
     * @param personal
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoModulosRegistroUsuario>> getModulosAsignadosPersonal(Personal personal){
        try{
            List<DtoModulosRegistroUsuario> listaModulosAsignados = em.createQuery("SELECT m FROM ModulosRegistroEspecifico m WHERE m.modulosRegistroEspecificoPK.personal=:personal", ModulosRegistroEspecifico.class)
                    .setParameter("personal", personal.getClave())
                    .getResultStream()
                    .map(moduloReg -> packModuloAsignado(moduloReg).getValor())
                    .sorted(DtoModulosRegistroUsuario::compareTo)
                    .collect(Collectors.toList());
           
            return ResultadoEJB.crearCorrecto(listaModulosAsignados, "Lista de módulos de registro asignados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de módulos de registro asignados. (EjbGestionModulosRegistro.getModulosRegistroAsignados)", e, null);
        }
    }
    
    /**
     * Empaqueta un evento escolar del proceso en su DTO Wrapper
     * @param moduloRegistroEspecifico
     * @return Dto del documento empaquetado
     */
    public ResultadoEJB<DtoModulosRegistroUsuario> packModuloAsignado(ModulosRegistroEspecifico moduloRegistroEspecifico){
        try{
            ModulosRegistroEspecifico modulosRegistroEspecificoBD = em.find(ModulosRegistroEspecifico.class, moduloRegistroEspecifico.getModulosRegistroEspecificoPK());
            
            Personal personal = em.find(Personal.class, modulosRegistroEspecificoBD.getModulosRegistroEspecificoPK().getPersonal());
            
            AreasUniversidad areaRegistro = new AreasUniversidad();
            
            if(modulosRegistroEspecificoBD.getAreaRegistro()== null){
                areaRegistro = em.find(AreasUniversidad.class, personal.getAreaOperativa());
            }else{
                areaRegistro = em.find(AreasUniversidad.class, modulosRegistroEspecificoBD.getAreaRegistro());
            }
            
            DtoModulosRegistroUsuario dto = new DtoModulosRegistroUsuario(modulosRegistroEspecificoBD, personal, areaRegistro);
            return ResultadoEJB.crearCorrecto(dto, "Módulo registro empaquetado asignado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el módulo de registro asignado (EjbGestionModulosRegistro.packModuloAsignado).", e, DtoModulosRegistroUsuario.class);
        }
    }
    
    /**
     * Permite obtener la lista de áreas disponibles para asignar módulos de registro
     * @param tipoModulo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AreasUniversidad>> getAreasDepartamentos(String tipoModulo){
        try{
            List<AreasUniversidad> listaAreasCoordinacionesDepartamentos = new ArrayList<>();
            
            if(tipoModulo.equals("Registro")){
                List<Short> areasExcluir = new ArrayList<>();
                areasExcluir.add((short)60); areasExcluir.add((short)71); 
                
                listaAreasCoordinacionesDepartamentos = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.tienePoa=:valor AND a.area NOT IN :areas", AreasUniversidad.class)
                    .setParameter("valor", true)
                    .setParameter("areas", areasExcluir)
                    .getResultStream()
                    .collect(Collectors.toList());
                
            }else{
                List<Short> areas = new ArrayList<>();
                areas.add((short)6); areas.add((short)9);
                
                listaAreasCoordinacionesDepartamentos = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.area IN :areas", AreasUniversidad.class)
                    .setParameter("areas", areas)
                    .getResultStream()
                    .collect(Collectors.toList());
            }
            
            return ResultadoEJB.crearCorrecto(listaAreasCoordinacionesDepartamentos.stream().sorted(Comparator.comparing(AreasUniversidad::getNombre)).collect(Collectors.toList()), "Lista de áreas y departamentos disponibles para asignar módulos de registro.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de de áreas y departamentos disponibles para asignar módulos de registro. (EjbGestionModulosRegistro.getAreasDepartamentos)", e, null);
        }
    }
    
    /**
     * Permite guardar la asignación del usuario al módulo seleccionado
     * @param moduloRegistro
     * @param personal
     * @param areaRegistro
     * @return Resultado del proceso
     */
    public ResultadoEJB<ModulosRegistroEspecifico> guardarAsignacionModulo(ModulosRegistro moduloRegistro, Personal personal, AreasUniversidad areaRegistro){
        try{
            
            ModulosRegistroEspecificoPK modulosRegistroEspecificoPK = new ModulosRegistroEspecificoPK(moduloRegistro.getClave(), personal.getClave());
            
            ModulosRegistroEspecifico moduloRegistroBD = em.find(ModulosRegistroEspecifico.class, modulosRegistroEspecificoPK);
            
            if(moduloRegistroBD != null) return ResultadoEJB.crearErroneo(2, "No se puede duplicar la asignación del módulo al usuario seleccionado.", ModulosRegistroEspecifico.class);
            
            ModulosRegistroEspecifico modulosRegistroEspecifico = new ModulosRegistroEspecifico();
            modulosRegistroEspecifico.setModulosRegistroEspecificoPK(modulosRegistroEspecificoPK);
            if(areaRegistro != null){
                modulosRegistroEspecifico.setAreaRegistro(areaRegistro.getArea()); 
            }else{
                modulosRegistroEspecifico.setAreaRegistro(null); 
            }
            em.persist(modulosRegistroEspecifico);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(modulosRegistroEspecifico, "Se registró correctamente la asignación del usuario al módulo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente la asignación del usuario al módulo seleccionado. (EjbGestionModulosRegistro.guardarAperturaPersonal)", e, null);
        }
    }
    
     /**
     * Permite eliminar la asignación del usuario al módulo seleccionado
     * @param modulosRegistroEspecifico
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarAsignacionModulo(ModulosRegistroEspecifico modulosRegistroEspecifico){
        try{
           
            Integer delete = em.createQuery("DELETE FROM ModulosRegistroEspecifico m WHERE m.modulosRegistroEspecificoPK=:moduloPK ", ModulosRegistroEspecifico.class)
                .setParameter("moduloPK", modulosRegistroEspecifico.getModulosRegistroEspecificoPK())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente la asignación del usuario al módulo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar la asignación del usuario al módulo seleccionado. (EjbGestionModulosRegistro.eliminarAsignacionModulo)", e, null);
        }
    }
    
     /**
     * Permite actualizar la asignación del usuario al módulo seleccionado
     * @param dtoModulosRegistroUsuario
     * @return Resultado del proceso
     */
    public ResultadoEJB<ModulosRegistroEspecifico> actualizarAsignacionModulo(DtoModulosRegistroUsuario dtoModulosRegistroUsuario){
        try{
            
            ModulosRegistroEspecificoPK pK= new ModulosRegistroEspecificoPK(dtoModulosRegistroUsuario.getModulosRegistroEspecifico().getModulosRegistroEspecificoPK().getClave(), dtoModulosRegistroUsuario.getModulosRegistroEspecifico().getModulosRegistroEspecificoPK().getPersonal());
            ModulosRegistroEspecifico mre = new ModulosRegistroEspecifico();
            mre.setModulosRegistroEspecificoPK(pK);
            
            if(!dtoModulosRegistroUsuario.getAreaRegistro().getArea().equals(dtoModulosRegistroUsuario.getPersonal().getAreaOperativa())){
                mre.setAreaRegistro(dtoModulosRegistroUsuario.getAreaRegistro().getArea());
                em.merge(mre);
                f.flush();
            }
           
            return ResultadoEJB.crearCorrecto(mre, "Se ha actualizado la asignación del usuario al módulo seleccionado.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la asignación del usuario al módulo seleccionado. (EjbGestionModulosRegistro.actualizarAsignacionModulo)", e, null);
        }
    }
    
    
}
