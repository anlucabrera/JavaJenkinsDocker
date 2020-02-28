/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.prontuario;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.dto.listaDTOCiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausa;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasTipo;
import mx.edu.utxj.pye.sgi.entity.prontuario.BecaTipos;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.OrganismosEvaluadores;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativos;



/**
 *
 * @author UTXJ
 */
@Local
public interface EjbCatalogos {
    
    public List<Generaciones> getGeneracionesAct();
    
    public List<AreasUniversidad> getProgramasEducativos();
    
    /**
     * Método que permite la consulta de programas educativo que no se encuentran actualmente vigentes en la base de datos
     * Utilidad en módulos de registro: ServiciosTecnoligicosParticipantes
     * @return Devuelve una lista de tipo AreasUniversidad
     */
    public List<AreasUniversidad> getProgramasEducativosGeneral();
    
    /**
     * Obtiene el catalogo de programas educativos del área académica que le corresponde
     * @param area
     * @return  Lista de entidades de AreasUniversidad
     */
    public List<AreasUniversidad> getProgramasEducativoPorAreaAcademica(Short area);
    
    public List<CiclosEscolares> getCiclosEscolaresAct();
    
    public List<PeriodosEscolares> getPeriodosEscolaresAct();
    
    public List<OrganismosEvaluadores> getOrganismosEvaluadoresAct();
    
    /**
     * Método que devuelve todas las áreas académicas vigentes
     * @return 
     */
    public List<AreasUniversidad> getAreasAcademicas();
    
    /**
     * Método que permite la búsqueda de todas las áreas académicas incluyendo el área de idiomas
     * @return 
     */
    public List<AreasUniversidad> getAreasAcademicasAsesoriasTutorias();
    
    /**
     * Obtiene los niveles de los programas educativos
     * @return  Lista de entidades de ProgramasEducativos
     */
    public List<ProgramasEducativos> getProgramasEducativosProntuario();
    
    /**
     * Devuelve una lista parcial de AreasUniversidad para el llenado de la plantilla de distribución de instalaciones
     * @return  Lista de entidades de AreasUniversidad
     */
    public List<AreasUniversidad> getAreasAcademicasDistribucionAulas();
    
     /**
     * Devuelve una lista de los tipos de Becas para el llenado de la plantilla de registro de becas
     * @return  Lista de entidades de BecaTipos
     */
    public List<BecaTipos> getBecaTiposAct();
     /**
     * Devuelve una lista de las causa de baja para el llenado de la plantilla de registro de deserción
     * @return  Lista de entidades de BecaTipos
     */
    public List<BajasCausa> getCausasBajaAct();
     /**
     * Devuelve una lista de los tipos de baja para el llenado de la plantilla de registro de desercion
     * @return  Lista de entidades de BecaTipos
     */
    public List<BajasTipo> getTipoBajaAct();
    
    public List<listaDTOCiclosEscolares> getCiclosEscolaresDTO();
    
    /**
     * Método que devuelve una lista de Categorías para el filtrado de áreas que cuenten con al menos una actividad en POA
     * @return 
     */
    public List<Categorias> getCategoriaAreasConPoa();
    
    /**
     * Método que devuelve una lista de áreas universidad filtradas por categoría y que cuenta con al menos una actividad en POA
     * @param categoria
     * @return 
     */
    public List<AreasUniversidad> getAreasUniversidadPorCategoriaConPoa(Categorias categoria);
    
    public List<AreasUniversidad> getAreasUniversidadDepartamentosConPoa();
    
    public List<AreasUniversidad> getAreasUniversidadCoordinacionesConPoa();
    
}
