/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.vin;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadEconomicaEgresadoGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadEgresadoGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelIngresosEgresadosGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelOcupacionEgresadosGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOActividadEconomicaEgresadoG;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOActividadEgresadoGeneracion;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTONivelIngresoEgresadosG;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTONivelOcupacionEgresadosG;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaEgresados;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbEgresados {
    public ListaEgresados getListaEgresados(String rutaArchivo) throws Throwable;
    public void guardaActividadEgresadoGeneracion(List<DTOActividadEgresadoGeneracion> listaActividadEgresadoGeneracion, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public void guardaActividadEcnomicaEgresadoG(List<DTOActividadEconomicaEgresadoG> listaActividadEconomicaEgresadoG, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public void guardaNivelOcupacionEgresadoG(List<DTONivelOcupacionEgresadosG> listaNivelOcupacionEgresadosG, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public void guardaNivelIngresoEgresadoG(List<DTONivelIngresoEgresadosG> listaNivelIngresoEgresadosG, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public ActividadEgresadoGeneracion getActividadEgresadoGeneracion(ActividadEgresadoGeneracion actividadEgresadoGeneracion);
    public ActividadEconomicaEgresadoGeneracion getActividadEconomicaEgresadoGeneracion(ActividadEconomicaEgresadoGeneracion actividadEconomicaEgresadoGeneracion);
    public NivelOcupacionEgresadosGeneracion getNivelOcupacionEgresadosGeneracion(NivelOcupacionEgresadosGeneracion nivelOcupacionEgresadosGeneracion);
    public NivelIngresosEgresadosGeneracion getNivelIngresosEgresadosGeneracion(NivelIngresosEgresadosGeneracion nivelIngresosEgresadosGeneracion);
}
