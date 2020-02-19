/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;


import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Baja;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroBajas;


import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoFormatoBaja;
import org.omnifaces.util.Ajax;
/**
 *
 * @author UTXJ
 */
@Named
@SessionScoped
public class GeneracionFormatoBaja implements Serializable{

    private static final long serialVersionUID = 1413874063554432050L;
    
    @EJB EjbRegistroBajas ejb;
    
    @Setter @Getter private Date fechaImpresion;
    @Setter @Getter private DtoFormatoBaja formatoBaja;

  
      /**
     * Permite generar el formato de baja del registro seleccionado
     * @param registro Registro de la baja
     */
    public void generarFormatoBaja(Baja registro){
       fechaImpresion = new Date();
       ResultadoEJB<DtoFormatoBaja> res = ejb.generarFormatoBaja(registro);
       if(res.getCorrecto()){
            formatoBaja = res.getValor();
            Ajax.update("frmFormatoBaja");
        }
    }
    
}
