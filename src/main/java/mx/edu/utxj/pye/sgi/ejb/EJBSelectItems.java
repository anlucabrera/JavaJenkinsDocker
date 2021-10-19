/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import mx.edu.utxj.pye.sgi.entity.pye2.Asentamiento;

import java.util.List;
import javax.ejb.Local;
import javax.faces.model.SelectItem;

/**
 *
 * @author UTXJ
 */
@Local
public interface EJBSelectItems {
    
    public List<SelectItem> itemsEvaluacionesDirectivos();
    
    public List<SelectItem> itemsEvaluacionDirectores();
    
    public List<SelectItem> itemsEvaluacionPersonal();
    
    public List<SelectItem> itemsEvaluacionSecretariaAcademica();
    
    public List<SelectItem> itemsPeriodos();
    
    public List<SelectItem> itemsPeriodos360();
    
    public List<SelectItem> itemPeriodosDesempenio();
    
    public List<SelectItem> itemPeriodosEvaluaciones(String tipo);
    
    public List<SelectItem> itemPeriodosDocenteMateria();
    
    public List<SelectItem> itemAreasAcademicas();
    
    ////////////////////////////////////////// Codigo agregado para la selección de estados o municipios para el filtrado de los Módulos de registro ////////////////////////////////////////////////////////
    
    public List<SelectItem> itemEstados();

    public List<SelectItem> itemEstadoByClave(Integer pais);
        
    public List<SelectItem> itemMunicipiosByClave(Integer estado);
        
    public List<SelectItem> itemLocalidadesByClave(Integer estado, Integer municipio);

    public List<Asentamiento> itemAsentamiento();

    public List<SelectItem> itemAsentamientoByClave(Integer Estado, Integer munipio);
    
    public List<SelectItem> itemLocalidadesIEMSByClave(Integer estado, Integer municipio);

    public List<SelectItem> itemPaises();

    public List<SelectItem> itemPaisMexico();

    public Integer itemCvePais(Integer idEstado);
    
    public List<SelectItem> itemCiclos();
    
    public List<SelectItem> itemPeriodosByClave(Integer ciclo);

    
     /*Se agregan los select item que obtienen los datos para el filtrado de los ejercicios y meses */
    /**
     * Lena la lista de selectitems de meses por registro, busca dependiendo dl paramaetro tipo
     * para encontrar el tipo de registro y el parametro ejercicio indica que el ejercicio en el que se buscara para
     * obtener los meses que llenaran la lista
     * @param tipo
     * @param ejercicio
     * @return 
     */
    public List<SelectItem> itemMesesPorRegistro(Short tipo, Short ejercicio);
    
    /**
     * Lena la lista de selectitems de meses por registro, busca dependiendo dl paramaetro tipo
     * para encontrar el tipo de registro y el parametro ejercicio indica que el ejercicio en el que se buscara para
     * obtener los meses que llenaran la lista
     * @param tipo
     * @param ejercicio
     * @return 
     */
    public List<SelectItem> itemMesesPorRegistroUsuarioOtraArea(Short tipo, Short ejercicio, Short area);
    
    /**
     * Llena la lista de select items de ejercicios activos segun sea ale tipo de registro
     * definido con el parametro tipo
     * @param tipo
     * @return 
     */
    public List<SelectItem> itemEjercicioFiscalPorRegistro(Short tipo);
    
     /**
     * Llena la lista de select items de ejercicios activos segun sea ale tipo de registro
     * definido con el parametro tipo
     * @param tipo
     * @param area
     * @return 
     */
    public List<SelectItem> itemEjercicioFiscalPorRegistroUsuarioOtraArea(Short tipo, Short area);
    
    public List<SelectItem> itemGeneraciones();
    
    public List<SelectItem> itemProgramasEducativos();
    
    public List<SelectItem> itemProgramEducativoPorArea(Short area);
    
    public List<SelectItem> itemIems(Integer estado, Integer municipio, Integer localidad);
    
    public List<SelectItem> itemAreaAcademica();
    
}
