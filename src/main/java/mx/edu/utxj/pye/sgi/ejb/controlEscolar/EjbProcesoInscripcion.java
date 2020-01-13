/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;

import javax.ejb.Local;
import java.util.List;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbProcesoInscripcion {
    public List<Aspirante> listaAspirantesTSU(Integer procesoInscripcion);
    public List<Aspirante> lisAspirantesByPE(Short pe,Integer procesoInscripcion);
    public Aspirante buscaAspiranteByFolio(Integer folio);
    public Aspirante buscaAspiranteByFolioValido(Integer folio);
    public AreasUniversidad buscaAreaByClave(Short area);
    public Estudiante guardaEstudiante(Estudiante estudiante, Documentosentregadosestudiante documentosentregadosestudiante, Boolean opcionIns);
    public Estudiante findByIdAspirante(Integer idAspirante);
    public void generaComprobanteInscripcion(Estudiante estudiante);
    public void generaCartaCompromiso(Estudiante estudiante);
    public List<Grupo> listaGruposXPeriodoByCarrera(Short periodo, Short carrera, Short sistema, Integer grado);
    public List<Estudiante> listaEstudiantesXPeriodo(Integer perido);
    public void actualizaEstudiante(Estudiante estudiante);
    public Iems buscaIemsByClave(Integer id);
    public ResultadoEJB<EventoEscolar> verificarEvento();
}
