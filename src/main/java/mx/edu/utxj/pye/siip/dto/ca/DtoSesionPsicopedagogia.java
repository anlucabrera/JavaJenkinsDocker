/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.ca;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.AreasConflicto;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.OtrosTiposSesionesPsicopedagogia;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;

/**
 *
 * @author UTXJ
 */
public final class DtoSesionPsicopedagogia {
    
    /************************************* Agregación de un nuevo registro ************************************************/
    @Getter @Setter private String mensaje;
    @Getter @Setter private Boolean nuevoRegistro;
    @Getter @Setter private Boolean habilitaProgramaEducativo;
    
    /************************** Evidencias *************************************/
    @Getter @Setter private Boolean tieneEvidencia, forzarAperturaDialogo;
    
    /************************** Alineación POA  *************************************/
    
    /******************** Consulta de información *********************/
    @Getter @Setter private List<String> mesesConsulta = new ArrayList<>();
    @Getter @Setter private List<Short> aniosConsulta = new ArrayList<>();
    
    @Getter @Setter private String mesConsulta = null;
    @Getter @Setter private Short anioConsulta = null;
    
    @Getter @Setter private List<Short> registros;
    @Getter @Setter private RegistrosTipo registroTipo;
    @Getter @Setter private EjesRegistro ejesRegistro;
    
    @Getter @Setter private AreasUniversidad area;
    @Getter @Setter private AreasUniversidad programaEducativo;
    
    @Getter @Setter private List<AreasConflicto> lstAreasConflicto;
    @Getter @Setter private List<OtrosTiposSesionesPsicopedagogia> lstOtrosTiposSesionesPsicopedagogia;
    @Getter @Setter private List<AreasUniversidad> lstProgramasEducativos;
    
    @Getter @Setter private DTOSesionesPsicopedagogia dtoSesionPsicopedagogia;
    @Getter @Setter private List<DTOSesionesPsicopedagogia> lstSesionesPsicopedagogia;
    
    
    public DtoSesionPsicopedagogia(){
        setRegistroTipo(new RegistrosTipo((short)52));
        setEjesRegistro(new EjesRegistro(3));
        setRegistros(new ArrayList<>(Arrays.asList(registroTipo.getRegistroTipo())));
        forzarAperturaDialogo = false;
        nuevoRegistro = false;
    }
    
    public void setForzarAperturaDialogo(Boolean forzarAperturaDialogo){
        this.forzarAperturaDialogo = forzarAperturaDialogo;
    }
    
}
