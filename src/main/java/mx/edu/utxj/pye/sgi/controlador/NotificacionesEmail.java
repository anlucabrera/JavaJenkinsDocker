/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import java.io.Serializable;
import java.util.List;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author Planeacion
 */
@Named
@ViewScoped
public class NotificacionesEmail implements Serializable{
    @Getter @Setter private Integer nivelFriltro ;
    @Getter @Setter private String area, encabezado, mensaje;
    @Getter @Setter private List<String> destinatarios;
    
    public void enviarMensaje(){
        
    }
}
