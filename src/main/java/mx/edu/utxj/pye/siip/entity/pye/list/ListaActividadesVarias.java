/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.entity.pye.list;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesVariasRegistro;

/**
 *
 * @author UTXJ
 */
public class ListaActividadesVarias implements Serializable {

    private static final long serialVersionUID = 8288108931077670757L;
    @Getter
    @Setter
    private List<ActividadesVariasRegistro> actividadesVarias;

}
