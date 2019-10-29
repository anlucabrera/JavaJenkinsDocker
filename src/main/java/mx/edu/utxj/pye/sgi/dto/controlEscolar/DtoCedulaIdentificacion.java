/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.finanzascarlos.Registro;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.*;

import java.util.List;

/**
 * Representa todos los datos necesarios para la cedula de idetificacion del estudiante buscado
 * @author UTXJ
 */

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoCedulaIdentificacion {
    //Datos generales
    @Getter @Setter private dtoCedulaIdentificacionDatosGenerales datosGenerales;
    @Getter @Setter private dtoCedulaIdentificacionDatosPersonales datosPersonales;
    //TODO: Datos socieconomicos
    @Getter @Setter private  DtoCedulaIdentificacionDatosSocioeconomicos datosSocioeconomicos;
    //TODO:Datos Escolares
    @Getter @Setter private  DtoCedulaIdentidicacionDatosEscolares datosEscolares;
    //TODO:Datos Familiares
    @Getter @Setter private  DtoCedulaIdentificacionDatosFamiliares datosFamiliares;
    //TODO: Datos de salud
    @Getter @Setter private DtoCedulaIdentifiacionDatosDeSalud datosDeSalud;
    //TODO: Cuestionario Psoipedagogico
    @Getter @Setter CuestionarioPsicopedagogicoResultados resultados;
    
    
   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
