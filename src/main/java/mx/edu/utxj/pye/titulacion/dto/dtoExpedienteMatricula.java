/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.entity.titulacion.Egresados;
import mx.edu.utxj.pye.sgi.entity.titulacion.ExpedientesTitulacion;
import mx.edu.utxj.pye.sgi.entity.titulacion.DatosContacto;
import mx.edu.utxj.pye.sgi.entity.titulacion.DatosTitulacion;
import mx.edu.utxj.pye.sgi.entity.titulacion.DomiciliosExpediente;
import mx.edu.utxj.pye.sgi.entity.titulacion.DocumentosExpediente;
import mx.edu.utxj.pye.sgi.entity.titulacion.AntecedentesAcademicos;
import mx.edu.utxj.pye.sgi.entity.titulacion.FechasDocumentos;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "dtoExpedienteMatricula")
@EqualsAndHashCode
public class dtoExpedienteMatricula implements Serializable{
    
    private static final long serialVersionUID = 2850497036094890250L;
    @Getter @Setter private Egresados egresados;
    @Getter @Setter private ExpedientesTitulacion expedientesTitulacion;
    @Getter @Setter private DatosContacto datosContacto;
    @Getter @Setter private DomiciliosExpediente domiciliosExpediente;
    @Getter @Setter private List<DocumentosExpediente> documentosExpediente;
    @Getter @Setter private AntecedentesAcademicos antecedentesAcademicos;
    @Getter @Setter private String localidad;
    @Getter @Setter private String municipio;
    @Getter @Setter private String estado;
    @Getter @Setter private Iems iems;
    @Getter @Setter private String estadoIems;
    @Getter @Setter private DatosTitulacion datosTitulacion;
    @Getter @Setter private String gradoAcademico;
    @Getter @Setter private String programaAcademico;
    @Getter @Setter private FechasDocumentos fechasDocumentos;
    @Getter @Setter private String generacion;
    @Getter @Setter private String personalValido;
    @Getter @Setter private dtoPagosFinanzas pagosFinanzas;
    @Getter @Setter private String situacionAcademica;
}
