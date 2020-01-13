/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class dtoCedulaIdentificacionDatosPersonales {
    //TODO: Datos personales
  @Getter @Setter private String p7;//Fecha de nacimiento
  @Getter @Setter private String p8;//Edad
  @Getter @Setter private String p9; //Sexo
  @Getter @Setter private String p10; //CURP
  @Getter @Setter private String p11;//Estado civil
  @Getter @Setter private String p12;// Hijos;
  @Getter @Setter private String p13 ;//Correo electronico
  @Getter @Setter private String p14 ;//Telefono fijo
  @Getter @Setter private String p15;//Celular
  
  //TODO:Domicilio residencia
  @Getter @Setter private String p16; //Calle
  @Getter @Setter private String p17; //Número
  @Getter @Setter private String p18; //Colonia
  @Getter @Setter private String p19; //Localidad
  @Getter @Setter private String p20; //Municipio
  @Getter @Setter private String p21; //Estado
  @Getter @Setter private String p22; //Código Postal
  @Getter @Setter private String p23; //Pais
  @Getter @Setter private String p24; //Tiempo de residencia

  //TODO:Domicilio pocedencia
  @Getter @Setter private String p25; //Calle
  @Getter @Setter private String p26; //Número
  @Getter @Setter private String p27; //Colonia
  @Getter @Setter private String p28; //Localidad
  @Getter @Setter private String p29; //Municipio
  @Getter @Setter private String p30; //Estado
  @Getter @Setter private String p31; //Código Postal
  @Getter @Setter private  String p32; //Pais
  
  //TODO: Lugar de nacimiento
  @Getter @Setter private String p33; //Localidad
  @Getter @Setter private String p34; //Municipio
  @Getter @Setter private String p35; //Estado
  @Getter @Setter private String p36; //Pais
  @Getter @Setter private String p37; //Actualmente vive con
}
