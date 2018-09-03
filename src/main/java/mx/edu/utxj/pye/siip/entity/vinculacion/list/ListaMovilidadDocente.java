/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.entity.vinculacion.list;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOMovilidadDocente;

/**
 *
 * @author UTXJ
 */
public class ListaMovilidadDocente implements Serializable{

    private static final long serialVersionUID = 1625081131340260197L;
    @Getter @Setter private List<DTOMovilidadDocente> movilidadDocente;
}
