package mx.edu.utxj.pye.siip.interfaces.vin;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.Convenios;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasBeneficiadosVinculacion;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.dto.vin.DTOProgramasBeneficiadosVinculacion;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbConvenios {
    
    public List<Convenios> getListaConvenios(String rutaArchivo) throws Throwable;
    
    public void guardaConvenios(List<Convenios> listaConvenios, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    
    public Convenios getConvenio(Convenios convenio);
    
    public Boolean verificaConvenio(Integer empresa);
    
    /**
     * Método que se ocupa para el filtrado de Convenios por Ejercicio, Mes y área el cual es mostrado para consulta y eliminación
     * @param ejercicio Ejercicio actual que deberá venir de la tabla eventos_registro
     * @param mes   Mes actual que deberá venir de la tabla eventos_registro
     * @param area  Área superior de cada usuario logueado
     * @return Regresa una lista de registros de Convenios que serán ocupados para consulta o eliminación
     */
    public List<Convenios> getFiltroConveniosEjercicioMesArea(Short ejercicio, String mes, Short area);

    /**
     * Devuelve una lista completa de DTOProgramasBeneficiadosVinculacion para asignar si aplica o no el beneficio en el programaEducativo
     * @return  Lista de DTO de ProgramasBeneficiadosVinculacion
     * @throws Throwable 
     */
    public List<DTOProgramasBeneficiadosVinculacion> getProgramasBeneficiadosVinculacion() throws Throwable;
    
    /**
     * Verifica que el ProgramasBeneficiadosVinculacion este ligado con la empresa o si existente dicha relación
     * @param empresa   Empresa o convenio seleccionado
     * @param areaUniversidad   Programa educativo consultado
     * @return  Devuelve True si existe o False si no hay dicha vinculación
     */
    public Boolean verificaProgramaBeneficiadoVinculacion(Integer empresa, AreasUniversidad areaUniversidad);
    
    /**
     * Guarda la asignación del programa educativo con el convenio
     * @param programaBeneficiadosVinculacion   Entidad para guardar en base de datos
     * @return 
     */
    public Boolean guardarProgramaBeneficiadoVinculacion(ProgramasBeneficiadosVinculacion programaBeneficiadosVinculacion);
    
    /**
     * Guarda la asignación del programa educativo con el convenio
     * @param programaBeneficiadosVinculacion   Entidad para guardar en base de datos
     * @return 
     */
    public Boolean eliminarProgramaBeneficiadoVinculacion(ProgramasBeneficiadosVinculacion programaBeneficiadosVinculacion);
}
