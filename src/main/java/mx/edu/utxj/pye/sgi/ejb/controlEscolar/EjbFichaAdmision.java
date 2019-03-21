/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;




import javax.ejb.Local;
import javax.faces.model.SelectItem;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;

import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativos;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbFichaAdmision {

    /**
     * Método que guarda los datos personales de una persona
     * @param persona parámetro que envía  el usuario al registrar persona
     */
    public void GuardaPersona(Persona persona);

    /**
     * Método que actualiza los datos de los apirantes
     * @param persona parámetro que envía  el usuario para hacer la actualización
     * @return devuelve el objeto persona una vez realizada la accion con los datos actualizados
     */

    public Persona actualizaPersona(Persona persona);

    /**
     * Método que realiza la lectura de la CURP mediante el codigo QR para poder obtener algunos datos personales como lo son nombre,
     * apellido paterno, apellido materno, genero, fecha de nacimiento y clave de estado de nacimiento
     * @param file parámetro mediante el cual se envía la CURP
     * @return devuelve el objeto persona
     */
    public Persona leerCurp(Part file) throws IOException;

    /**
     *  Método que realiza la busqueda del preceso de inscripcion activo
     * @return devuelve el proceso de inscripción activo
     */
    public ProcesosInscripcion getProcesoIncripcionTSU();
    /**
     * Método que realiza la consulta de los tipos de generos
     * @return devuelve las lista de generos
     */
    public List<SelectItem> listaGeneros();

    /**
     * Método que realiza la busqueda de una persona mediante su CURP
     * @param curp parámetro de busqueda que envía el usuario
     * @return devuelve el registro encontrado en la BD
     */
    public Persona buscaPersonaByCurp(String curp);

    /**
     *  Método mediante el cual el usuario guarda sus datos medicos
     * @param datosMedicos parámetro mediante el cual el usuario envía sus datos para su registro
     */
    public void guardaDatosMedicos(DatosMedicos datosMedicos);

    /**
     *  Método mediante el cual el usuario actualiza los datos medicos
     * @param datosMedicos parámetro mediante el cual el usuario envía sus datos para su actualización
     */
    public void actualizaDatosMedicos(DatosMedicos datosMedicos);
    /**
     *  Método medial el cual el usuario guarda sus medios de comunicación
     * @param comunicacion parámetro mediante el cual el usuario envía sus datos para su registro
     */
    public void guardaComunicacion(MedioComunicacion comunicacion);

    /**
     * Método medial el cual el usuario actualiza sus medios de comunicación
     * @param comunicacion parámetro mediante el cual el usuario envía sus datos para su actualización
     */
    public void actualizaCamunicacion(MedioComunicacion comunicacion);
    /**
     * Método medial el cual el usuario guarda sus datos de aspirante
     * @param aspirante parámetro mediante el cual el usuario envía sus datos para su registro
     */
    public void guardaAspirante(Aspirante aspirante);

    /**
     *  Método mediante que realiza la busqueda de un aspirante mediante su clave primaria
     * @param id parámetro mediante el cual el sistema obtiene la lleve primaria de busqueda
     * @return devuelve el aspirante encontrado
     */
    public Aspirante buscaAspiranteByClave(Integer id);
    /**
     * Método mediante el cual el usuario actualiza los datos de aspirante
     * @param aspirante parámetro mediante el cual el usuario envía sus datos para su actualización
     */
    public void actualizaAspirante(Aspirante aspirante);
    /**
     * Método mediante el cual el sistema asigna el folio del aspirante
     * @param procesosInscripcion parámetro mediante el cual se realiza la busqueda del ultimo folio de ficha de admisión
     * @return devuelve el ultimo folio de ficha de admision registrado
     */
    public Integer verificarFolio(ProcesosInscripcion procesosInscripcion);
    /**
     * Método medial el cual el usuario guarda su domicilio
     * @param domicilio parámetro mediante el cual el usuario envía su domicilio para su registro
     */
    public void  guardaDomicilo(Domicilio domicilio);
    /**
     * Método medial el cual el usuario actualiza su domicilio
     * @param domicilio parámetro mediante el cual el usuario envía su domicilio para su actualización
     */
    public void actualizaDomicilio(Domicilio domicilio);
    /**
     * Método medial el cual el usuario guarda su tutor familiar
     * @param tutorFamiliar parámetro mediante el cual el usuario envía su tutor familiar para su registro
     */
    public void guardaTutorFamiliar(TutorFamiliar tutorFamiliar);
    /**
     * Método medial el cual el usuario actualiza su tutor familiar
     * @param tutorFamiliar parámetro mediante el cual el usuario envía su tutor familiar para su actualización
     */
    public void actualizaTutorFamiliar(TutorFamiliar tutorFamiliar);
    /**
     * Método medial el cual el usuario guarda su Datos Familiares
     * @param datosFamiliares parámetro mediante el cual el usuario envía sus datos familiares para su registro
     */
    public void guardaDatosFamiliares(DatosFamiliares datosFamiliares);
    /**
     * Método medial el cual el usuario actualiza su Datos Familiares
     * @param datosFamiliares parámetro mediante el cual el usuario envía sus datos familiares para su actualización
     */
    public void actualizaDatosFamiliares(DatosFamiliares datosFamiliares);
    /**
     * Método medial el cual el usuario guarda su Datos Académicos
     * @param datosAcademicos parámetro mediante el cual el usuario envía sus datos para su registro
     */
    public void guardaDatosAcademicos(DatosAcademicos datosAcademicos);
    /**
     * Método medial el cual el usuario actualiza su Datos Académicos
     * @param datosAcademicos parámetro mediante el cual el usuario envía sus datos para su actualización
     */
    public void actualizaDatosAcademicos(DatosAcademicos datosAcademicos);
    /**
     * Método mediante el cual el sistema realiza una busque de IEMS por llave primaria
     * @param id parámetro de busqueda del IEMS
     * @return devuelve el IEMS encontrado
     */
    public Iems buscaIemsByClave(Integer id);
    /**
     * Método mediante el cual el sistema realiza una busque de un programa educativo mediante su llave primaria
     * @param clave parámetro mediante el cual el sistema realiza la busquede del programa educativo
     * @return devuelve el programa educativo encontrado
     */
    public ProgramasEducativos buscaPEByClave(String clave);
    /**
     * Método mediante el cual el usuario guarda sus requisitos de ficha de admisión
     * @param file parámetro mediante el cual el usuario guarda sus requisitos de inscripción
     * @param aspirante parámetro mediante el cual se envian los datos del aspirante
     * @param tipoRequisito parámetro mediante el cual el usuario envía el tipo de requisito
     * @return devuelve los datos del registro actualizado
     * @throws IOException 
     */
    public DocumentoAspirante guardaRequiitos(Part file, Aspirante aspirante, String tipoRequisito) throws IOException;
    /**
     * Método mediante el cual el usuario actualiza sus requisitos de ficha de admisión
     * @param documentoAspirante parámetro mediante el cual se realiza la actualización del registro de requisitos
     */
    public void actualizaDocumentosAspirante(DocumentoAspirante documentoAspirante);

}
