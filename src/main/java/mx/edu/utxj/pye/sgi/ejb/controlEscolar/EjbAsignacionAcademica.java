package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless(name = "EjbAsignacionAcademica")
public class EjbAsignacionAcademica {
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    /**
     * Permite validar si el usuario autenticado es un director de área académica
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDirector(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_SUPERIOR.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorAreaSuperior").orElse(2)));
            filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorCategoriaOperativa").orElse(18)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como un director.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El director no se pudo validar.", e, null);
        }
    }

    /**
     * COnvierte el area operativa del director en programa para mostrar los docentes y grupos correspondientes a ese programa.
     * @param area Area operativa del directos
     * @return Resultado del proceso
     */
    /*public ResultadoEJB<ProgramasEducativos> areaAPrograma(AreasUniversidad area){
        try{
            ProgramasEducativos programa = new ProgramasEducativos();
            //:TODO hacer la conversion de area a programa
            return ResultadoEJB.crearCorrecto(programa, "Programa convertido.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "ENo se pudo convertir el area a programa.", e, ProgramasEducativos.class);
        }
    }*/

    /**
     * Permite identificar a una lista de posibles docentes para asignar la materia
     * @param pista Contenido que la vista que puede incluir parte del nombre, nùmero de nómina o área operativa del docente que se busca
     * @return Resultado del proceso con docentes ordenador por nombre
     */
    public ResultadoEJB<List<PersonalActivo>> buscarDocente(String pista){
        try{
            //buscar lista de docentes operativos por nombre, nùmero de nómina o área  operativa segun la pista y ordener por nombre del docente
            List<PersonalActivo> docentes = f.getEntityManager().createQuery("select p from Personal p where p.estado <> 'B' and concat(p.nombre, p.clave) like concat('%',:pista,'%')  ", Personal.class)
                    .setParameter("pista", pista)
                    .getResultStream()
                    .map(p -> ejbPersonalBean.pack(p))
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(docentes, "Lista para mostrar en autocomplete");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo localizar la lista de docentes activos. (EjbAsignacionAcademica)", e, null);
        }
    }

    /**
     * Permite obtener la lista de periodos escolares a elegir al realizar la asignación docente
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosDescendentes(){
        try{
            //buscar lista de periodos escolares ordenados de forma descendente para que al elegir un periodo en la asignación docente aparezca primero el mas actual
            final List<PeriodosEscolares> periodos = f.getEntityManager().createQuery("select p from PeriodosEscolares p order by p.periodo desc", PeriodosEscolares.class)
                    .getResultList();
            return ResultadoEJB.crearCorrecto(periodos, "Periodos ordenados de forma descendente");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares. (EjbAsignacionAcademica)", e, null);
        }
    }

    /**
     * Permite obtener la lista de programas educativos que tienen planes de estudio vigentes, los programas deben ordenarse por
     * área, nivel y nombre y los grupos por grado y letra
     * @return Resultado del proceso
     */
    public ResultadoEJB<Map<AreasUniversidad, List<Grupo>>> getProgramasActivos(PersonalActivo director, PeriodosEscolares periodo){
        try{
            //Map<AreasUniversidad, List<Grupo>> programasMap = Collections.EMPTY_MAP;
            // buscar lista de programas educativos con plan de estudios vigentes y despues mapear cada programa con su lista de grupos
            Integer programaEducativoCategoria = ep.leerPropiedadEntera("programaEducativoCategoria").orElse(9);
//            System.out.println("programaEducativoCategoria = " + programaEducativoCategoria);
            List<AreasUniversidad> programas = f.getEntityManager().createQuery("select a from AreasUniversidad  a where a.areaSuperior=:areaPoa and a.categoria.categoria=:categoria and a.vigente = '1' order by a.nombre", AreasUniversidad.class)
                    .setParameter("areaPoa", director.getAreaPOA().getArea())
                    .setParameter("categoria", programaEducativoCategoria)
                    .getResultList();
//            System.out.println("programas = " + programas);
            Map<AreasUniversidad, List<Grupo>> programasMap = programas.stream()
                    .collect(Collectors.toMap(programa -> programa, programa -> generarGrupos(programa, periodo)));
//            System.out.println("programasMap = " + programasMap);
            return ResultadoEJB.crearCorrecto(programasMap, "Mapa de programas y grupos");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo mapear los programas y sus grupos. (EjbAsignacionAcademica)", e, null);
        }
    }

    private List<Grupo> generarGrupos(AreasUniversidad programa, PeriodosEscolares periodo) {
        System.out.println("programa = [" + programa + "], periodo = [" + periodo + "]");
        return f.getEntityManager().createQuery("select g from Grupo g where g.idPe=:programa and g.periodo=:periodo", Grupo.class)
                .setParameter("programa", programa.getArea())
                .setParameter("periodo", periodo.getPeriodo())
                .getResultList();
    }

    /**
     * Permite obtener la lista de materias por asignar en un grupo correspondiente
     * @param programa Programa al que deben pertenecer las materias
     * @param grupo Grupo al que deben pertenecer las materias
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Materia>> getMateriasPorAsignar(AreasUniversidad programa, Grupo grupo){
        try{
            //TODO: buscar lista de materias sin asignar que pertenecen al programa y grupo seleccionado
            List<Materia> materiasSinAsignar = f.getEntityManager().createQuery("select m from Materia m inner join m.idPlan p left join m.cargaAcademicaList ca where p.idPe=:programaEducativo and ca.cargaAcademicaPK is null", Materia.class)
                    .getResultList();
            return ResultadoEJB.crearCorrecto(materiasSinAsignar, "Lista de materias sin asignar por grupo y programa");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de materias sin asignar por grupo y programa. (EjbAsignacionAcademica)", e, null);
        }
    }

    /**
     * Permite la asignación de carga académica a un docente para que imparta una materia a un grupo de un cierto periodo escolar
     * @param materia Materia que se va a asignar
     * @param docente Docente que impartirá la materia
     * @param grupo Grupo a quien se impartirá la materia
     * @param periodo Periodo en el que se impartirá la materia, si bien el grupo ya tiene la referencia necesaria, se utililizará como un regla de
     *                validación para asegurar que esta referencia y la del grupo coinciden
     * @param operacion Operación a realizar, se permite persistir y eliminar
     * @return Resultado del proceso generando la instancia de carga académica obtenida
     */
    public ResultadoEJB<CargaAcademica> asignarMateriaDocente(Materia materia, PersonalActivo docente, Grupo grupo, PeriodosEscolares periodo, Operacion operacion){
        try{
            if(materia == null) return ResultadoEJB.crearErroneo(3, "La materia no debe ser nula.", CargaAcademica.class);
            if(grupo == null) return ResultadoEJB.crearErroneo(4, "El grupo no debe ser nulo.", CargaAcademica.class);
            if(periodo == null) return ResultadoEJB.crearErroneo(5, "El periodo no debe ser nulo.", CargaAcademica.class);
            if(docente == null) return ResultadoEJB.crearErroneo(6, "El docente no debe ser nulo.", CargaAcademica.class);

            switch (operacion){
                case PERSISTIR:
                    CargaAcademica cargaAcademica = new CargaAcademica();
                    //TODO: crear la carga académica y persistirla
                    return ResultadoEJB.crearCorrecto(cargaAcademica, "Carga académica");
                case ELIMINAR:
                    //TODO: eliminar la carga académica
                    return ResultadoEJB.crearCorrecto(null, "Eliminación de carga académica realizada correctamente.");
                    default:
                        return ResultadoEJB.crearErroneo(2, "Operación no autorizada.", CargaAcademica.class);
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la carga académica. (EjbAsignacionAcademica)", e, null);
        }
    }

    /**
     * Permite obtener la lista de cargas académicas de un docente, en todos los programas educativos que participe
     * @param docente Docente de quien se quiere obtener la lista
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<CargaAcademica>> getCargaAcademicaPorDocente(PersonalActivo docente){
        try{
            List<CargaAcademica> cargas = Collections.EMPTY_LIST;
            //TODO: buscar lista de materias sin asignar que pertenecen al programa y grupo seleccionado
            return ResultadoEJB.crearCorrecto(cargas, "Lista de cargas académicas por docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de cargas académicas por docente. (EjbAsignacionAcademica)", e, null);
        }
    }

    /**
     * Permite obtener el total de horas frente de a grupo de una lista de cargas académicas
     * @param cargas Lista de cargas académicas a utilizar para realizar el cálculo
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> getTotalHorasFrenteAGrupo(List<CargaAcademica> cargas){
        try{
            Integer horas = 0;
            //TODO: calcular el total de horas frente a grupo asignadas
            return ResultadoEJB.crearCorrecto(horas, "Horas frente a grupo.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el total de horas frente a grupo. (EjbAsignacionAcademica)", e, Integer.class);
        }
    }
}