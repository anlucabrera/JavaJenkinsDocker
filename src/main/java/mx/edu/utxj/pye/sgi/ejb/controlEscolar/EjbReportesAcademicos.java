/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

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
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbReportesAcademicos")
public class EjbReportesAcademicos {
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbPacker ejbPacker;
    @EJB Facade f;
    private EntityManager em;
    
    @EJB EjbCarga ejbCarga;
    public static final String ACTUALIZADO_ACADEMICO = "seguimientoEstadia.xlsx";
    public static final String ACTUALIZADO_ACADEMICO_DIRECCION = "seguimientoEstadiaAreaAcademica.xlsx";
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
      /**
     * Permite validar si el usuario autenticado es personal adscrito al departamento de servicios escolares
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarRolesReportesAcademicos(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            if (p.getPersonal().getAreaSuperior()== 2 && p.getPersonal().getCategoriaOperativa().getCategoria()==18) {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.AREA_SUPERIOR.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorAreaSuperior").orElse(2)));
                filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorCategoriaOperativa").orElse(18)));
            }
            else if (p.getPersonal().getAreaSuperior()== 2 && p.getPersonal().getCategoriaOperativa().getCategoria()==48) {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.AREA_SUPERIOR.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorAreaSuperior").orElse(2)));
                filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorEncargadoCategoriaOperativa").orElse(48)));
            }
            else if (p.getPersonal().getAreaOperativa() == 10 && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal con acceso a reportes académicos.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbReportesAcademicos.validarRolesReportesAcademicos)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de periodos con escolar en la que existen eventos escolares registrados
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosEscolaresRegistro(){
        try{
            List<PeriodosEscolares> listaPeriodosEscolares = new ArrayList<>();
            
            List<Integer> listaPeriodosEventos = em.createQuery("SELECT e FROM EventoEscolar e ORDER BY e.periodo DESC",  EventoEscolar.class)
                    .getResultStream()
                    .map(p->p.getPeriodo())
                    .distinct()
                    .collect(Collectors.toList());
          
            listaPeriodosEventos.forEach(periodo -> {
                PeriodosEscolares periodoEscolar = em.find(PeriodosEscolares.class, periodo);
                listaPeriodosEscolares.add(periodoEscolar);
            });
            
            
            return ResultadoEJB.crearCorrecto(listaPeriodosEscolares, "Lista de periodos escolares en que existen eventos escolares registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares en los que existen eventos escolares registrados. (EjbReportesAcademicos.getPeriodosEscolaresRegistro)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de niveles educativo en los que existen eventos escolares registrados del periodo seleccionado
     * @param periodo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<ProgramasEducativosNiveles>> getNivelesPeriodosEscolares(PeriodosEscolares periodo){
        try{
            List<ProgramasEducativosNiveles> listaNiveles = new ArrayList<>();
            
            List<Short> listaProgramasGrupo = em.createQuery("SELECT g FROM Grupo g WHERE g.periodo=:periodo",  Grupo.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream()
                    .map(p->p.getIdPe())
                    .distinct()
                    .collect(Collectors.toList());
          
            listaProgramasGrupo.forEach(programa -> {
                AreasUniversidad programaPK = em.find(AreasUniversidad.class, programa);
                ProgramasEducativosNiveles nivel = em.find(ProgramasEducativosNiveles.class, programaPK.getNivelEducativo().getNivel());
                listaNiveles.add(nivel);
            });
            
            return ResultadoEJB.crearCorrecto(listaNiveles.stream().distinct().sorted(Comparator.comparing(ProgramasEducativosNiveles::getNombre)).collect(Collectors.toList()), "Lista de niveles educativos del periodo escolar seleccionado en que existen eventos escolares registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de niveles educativos del periodo escolar seleccionado en los que existen eventos escolares registrados. (EjbReportesAcademicos.getNivelesPeriodosEscolares)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de programas educativos en que existen eventos escolares registrados del periodo y nivel educativo seleccionados
     * @param periodo
     * @param nivel
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AreasUniversidad>> getProgramasEducativosNivel(PeriodosEscolares periodo, ProgramasEducativosNiveles nivel){
        try{
            List<AreasUniversidad> listaProgramasEducativos = new ArrayList<>();
            
            List<Short> listaProgramasGrupo = em.createQuery("SELECT g FROM Grupo g WHERE g.periodo=:periodo",  Grupo.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream()
                    .map(p->p.getIdPe())
                    .distinct()
                    .collect(Collectors.toList());
          
            listaProgramasGrupo.forEach(programa -> {
                AreasUniversidad programaPK = em.find(AreasUniversidad.class, programa);
                listaProgramasEducativos.add(programaPK);
            });
            
            return ResultadoEJB.crearCorrecto(listaProgramasEducativos.stream().filter(p->p.getNivelEducativo().equals(nivel)).collect(Collectors.toList()), "Lista de programas educativos del periodo y nivel seleccionado en que existen eventos escolares registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos del periodo y nivel seleccionado en los que existen eventos escolares registrados. (EjbReportesAcademicos.getProgramasEducativosNivel)", e, null);
        }
    }
    
}
