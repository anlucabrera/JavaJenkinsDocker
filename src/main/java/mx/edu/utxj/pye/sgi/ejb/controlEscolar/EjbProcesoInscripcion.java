/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoGrupo;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.enums.Operacion;

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
    ///////////////////////////Nuevos ///////////////////////
    public ResultadoEJB<EventoEscolar> verificarEventoIncipcion();
    public ResultadoEJB<EventoEscolar> verificarEventoRegistroFichas();
    public  ResultadoEJB<Personal> getTutor (@NonNull Integer clave);
    public ResultadoEJB<List<Grupo>> getGruposbyPe(@NonNull EventoEscolar eventoEscolar, @NonNull AreasUniversidad pe, @NonNull Sistema sistema);
    public ResultadoEJB<List<Estudiante>> getEstudiantesbyGrupo(@NonNull Grupo grupo);
    public  ResultadoEJB<DtoGrupo> packGrupo(@NonNull Grupo grupo);
    public ResultadoEJB<List<DtoGrupo>> getGruposbyOpcion(@NonNull EventoEscolar eventoEscolar,@NonNull Aspirante aspirante,@NonNull AreasUniversidad pe,@NonNull DatosAcademicos datosAcademicos);
    public ResultadoEJB<Estudiante> saveEstudiante(@NonNull Estudiante estudiante, @NonNull Boolean opcionIn, @NonNull DtoGrupo grupo, @NonNull Documentosentregadosestudiante documentos, @NonNull Operacion operacion, @NonNull EventoEscolar eventoEscolar);
    public ResultadoEJB<Documentosentregadosestudiante> getDocEstudiante (@NonNull Estudiante estudiante);
    public ResultadoEJB<Login> getLoginbyPersona(@NonNull Persona persona);
    public ResultadoEJB<String> actualizarConfiguracionPropiedadCorreo(@NonNull String clave, @NonNull String valorNuevo);
}
