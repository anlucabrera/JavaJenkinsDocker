/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.vin;

import java.util.List;
import javax.faces.model.SelectItem;
import lombok.Getter;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.siip.entity.prontuario.list.ListaIemsPrevia;

/**
 *
 * @author Planeacion
 */
public class DtoIems {
    @Getter  private Iems iemsSeleccionada;
    @Getter  private Integer estado = 0, municipio = 0;
    @Getter  private List<SelectItem> selectEstados, SelectMunicipio;
    @Getter  private List<Iems> listaIems, listaIemsFiltro;    
    @Getter  private ListaIemsPrevia listaIemsPrevia = new ListaIemsPrevia();

    public void setIemsSeleccionada(Iems iemsSeleccionada) {
        this.iemsSeleccionada = iemsSeleccionada;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public void setMunicipio(Integer municipio) {
        this.municipio = municipio;
    }

    public void setSelectEstados(List<SelectItem> selectEstados) {
        this.selectEstados = selectEstados;
    }

    public void setSelectMunicipio(List<SelectItem> SelectMunicipio) {
        this.SelectMunicipio = SelectMunicipio;
    }

    public void setListaIems(List<Iems> listaIems) {
        this.listaIems = listaIems;
    }

    public void setListaIemsFiltro(List<Iems> listaIemsFiltro) {
        this.listaIemsFiltro = listaIemsFiltro;
    }

    public void setListaIemsPrevia(ListaIemsPrevia listaIemsPrevia) {
        this.listaIemsPrevia = listaIemsPrevia;
    }
       
}
