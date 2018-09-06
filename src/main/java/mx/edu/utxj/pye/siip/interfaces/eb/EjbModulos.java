/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.eb;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistroUsuario;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbModulos {
    /**
     * Interfaz para mostrar a los usuarios los Ejes Habilitados
     * @param clavepersonal
     * @return 
     * @throws java.lang.Throwable 
     */
    public List<ModulosRegistroUsuario> getEjesRectores(Integer clavepersonal) throws Throwable;
    
    /**
     * Interfaz para mostrar a los usuario los registros habilitados
     * @param eje
     * @param clavepersonal
     * @return 
     * @throws java.lang.Throwable 
     */
    public List<ModulosRegistroUsuario> getModulosRegistroUsuario(Integer eje, Integer clavepersonal) throws  Throwable;
    
    public Registros getRegistro(RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
    
    /**
     * Obtiene el evento de registro según la fecha actual
     * @return Evento de registro actual
     * @throws EventoRegistroNoExistenteException Se lanza en caso que no exista un evento de registro para la fecha actual
     */
    public EventosRegistros getEventoRegistro() throws EventoRegistroNoExistenteException;
    
    public Boolean validaPeriodoRegistro(PeriodosEscolares periodoEscolar, Integer periodoRegistro);
}
