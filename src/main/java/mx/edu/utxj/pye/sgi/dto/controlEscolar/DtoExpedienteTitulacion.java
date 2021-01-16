/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosAcademicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Domicilio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.FechaTerminacionTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioComunicacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.pye2.Asentamiento;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoExpedienteTitulacion implements Serializable{
   
    @Getter @Setter @NonNull ExpedienteTitulacion expediente;
    @Getter @Setter String situacionAcademica;
    @Getter @Setter String personal;
    @Getter @Setter @NonNull String fechaIntExp;
    @Getter @Setter @NonNull String fechaNacimiento;
    @Getter @Setter @NonNull AreasUniversidad programaEducativo;
    @Getter @Setter @NonNull String nivelEducativo;
    @Getter @Setter @NonNull Generaciones generacion;
    @Getter @Setter @NonNull Generos genero;
    @Getter @Setter Domicilio domicilio;
    @Getter @Setter Estado estadoDom;
    @Getter @Setter Municipio munDom;
    @Getter @Setter Asentamiento asentDom;
    @Getter @Setter MedioComunicacion medioComunicacion;
    @Getter @Setter DatosAcademicos datosAcademicos;
    @Getter @Setter @NonNull Iems iems;
    @Getter @Setter @NonNull Localidad locIems;
    @Getter @Setter DtoPagosEstudianteFinanzas pagEstFinanzas;
    @Getter @Setter Integer docsExp;
    @Getter @Setter Integer docsTotalExp;
    @Getter @Setter FechaTerminacionTitulacion fechaTerminacionTitulacion;

    public DtoExpedienteTitulacion() {
    }
    
    public DtoExpedienteTitulacion(ExpedienteTitulacion expediente, String situacionAcademica, String personal, String fechaIntExp, String fechaNacimiento, AreasUniversidad programaEducativo, String nivelEducativo, Generaciones generacion, Generos genero, Domicilio domicilio, Estado estadoDom, Municipio munDom, Asentamiento asentDom, MedioComunicacion medioComunicacion, DatosAcademicos datosAcademicos, Iems iems, Localidad locIems, DtoPagosEstudianteFinanzas pagEstFinanzas, Integer docsExp, Integer docsTotalExp, FechaTerminacionTitulacion fechaTerminacionTitulacion) {
        this.expediente = expediente;
        this.situacionAcademica = situacionAcademica;
        this.personal = personal;
        this.fechaIntExp = fechaIntExp;
        this.fechaNacimiento = fechaNacimiento;
        this.programaEducativo = programaEducativo;
        this.nivelEducativo = nivelEducativo;
        this.generacion = generacion;
        this.genero = genero;
        this.domicilio = domicilio;
        this.estadoDom = estadoDom;
        this.munDom = munDom;
        this.asentDom = asentDom;
        this.medioComunicacion = medioComunicacion;
        this.datosAcademicos = datosAcademicos;
        this.iems = iems;
        this.locIems = locIems;
        this.pagEstFinanzas = pagEstFinanzas;
        this.docsExp = docsExp;
        this.docsTotalExp = docsTotalExp;
        this.fechaTerminacionTitulacion = fechaTerminacionTitulacion;
    }
   
}
