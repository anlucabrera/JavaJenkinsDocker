/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;



import javax.ejb.Local;
import javax.faces.model.SelectItem;
import java.util.List;

import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbSelectItemCE {

    public List<SelectItem> itemTipoSangre();
    public List<SelectItem> itemDiscapcidad();
    public List<Ocupacion> itemOcupacion();
    public List<Escolaridad> itemEscolaridad();
    public List<SelectItem> itemIems(Integer estado, Integer municipio, Integer localidad);
    public List<EspecialidadCentro> itemEspecialidadCentro();
    public List<SelectItem> itemAreaAcademica();
    public List<SelectItem> itemProgramEducativoPorArea(Short area);
    public List<SelectItem> itemProgramaEducativoAll();
    public List<AreasUniversidad> itemPEAll();
    public List<Turno> itemTurno();
    public List<Sistema> itemSistema();
    public List<LenguaIndigena> itemLenguaIndigena();
    public List<MedioDifusion> itemMedioDifusion();
}
