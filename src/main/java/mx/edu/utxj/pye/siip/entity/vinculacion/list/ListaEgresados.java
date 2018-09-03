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
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOActividadEconomicaEgresadoG;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOActividadEgresadoGeneracion;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTONivelIngresoEgresadosG;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTONivelOcupacionEgresadosG;

/**
 *
 * @author UTXJ
 */
public class ListaEgresados implements Serializable{
    private static final long serialVersionUID = -4975689546351049234L;
    @Getter @Setter private List<DTOActividadEgresadoGeneracion> dtoActividadEgresadosGeneracion;
    @Getter @Setter private List<DTOActividadEconomicaEgresadoG> dtoActividadEconomicaEgresadoG;
    @Getter @Setter private List<DTONivelOcupacionEgresadosG> dtoNivelOcupacionEgresadosG;
    @Getter @Setter private List<DTONivelIngresoEgresadosG> dtoNivelIngresoEgresadosG;
}
