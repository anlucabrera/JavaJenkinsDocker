/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.eb;

import java.io.Serializable;
import java.nio.file.Path;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DTOArchivoRepositorio implements Serializable{
    private static final long serialVersionUID = 9020976035903761062L;
    @Getter @Setter private Short   ejercicio;
    @Getter @Setter private String  siglas;
    @Getter @Setter private String  ejeRuta;
    @Getter @Setter private String  ejeOficial;
    @Getter @Setter private String  mes;
    @Getter @Setter private String  mesOficial;
    @Getter @Setter private String  nombreRegistroRuta;
    @Getter @Setter private String  nombreRegistroOficial;
    @Getter @Setter private String  rutaString;
    @Getter @Setter private Path    rutaPath;
}
