/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.eb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;

/**
 *
 * @author UTXJ
 */
public final class DTORepositorio {
    
    /************************************* Consulta de información ***************************************/
    @Getter private List<String>    mesesConsulta   =   new ArrayList<>();
    @Getter private List<Short>     aniosConsulta   =   new ArrayList<>();
    
    @Getter @Setter private String          mesConsulta     =   null;
    @Getter @Setter private Short           anioConsulta    =   null;
    
    @Getter @Setter private AreasUniversidad    areaUniversidad;
    
    @Getter @Setter private List<String>        tiposRegistros              =   new ArrayList<>();
    @Getter @Setter private List<String>        ejesRegistros               =   new ArrayList<>();
    
    @Getter @Setter private List<DTOArchivoRepositorio>     listaDirectorioArchivos     =   new ArrayList<>();

    public DTORepositorio() {
        setTiposRegistros(new ArrayList<>(Arrays.asList(
                "acervo_bibliografico",
                "actividades_formacion_integral",
                "participantes_actformint",
                "actividades_varias",
                "becas",
                "cuerpos_academicos",
                "desercion_academica",
                "egetsu",
                "estadias_estudiante",
                "exani",
                "productos_academicos",
                "programas_PertCalidad",
                "servicios_enfermeria",
                "tutorias_asesorias",
                "comisiones_academicas",
                "programas_estimulos",
                "personal_capacitado",
                "reconocimientos_prodep",
                "distribucion_equipamiento",
                "distribucion_instalaciones",
                "ingresosPropiosCaptados",
                "presupuestos",
                "bolsa_trabajo",
                "difusion_iems",
                "egresados",
                "ferias_profesiográficas",
                "iems",
                "registro_movilidad",
                "organismos_vinculados",
                "servicios_tecnologicos",
                "visitas_industriales",
                "convenios",
                "eficiencia_terminal",
                "matricula_inicial"
        )));
        
        setEjesRegistros(new ArrayList<>(Arrays.asList(
                ServicioArchivos.EJES
        )));
        
    }
    
    public void setAniosConsulta(List<Short> aniosConsulta) {
        this.aniosConsulta = aniosConsulta;
        if(aniosConsulta.isEmpty())
            nulificarAnioConsulta();
    }
    
    public void setMesesConsulta(List<String> mesesConsulta) {
        this.mesesConsulta = mesesConsulta;
    }
    
    public void nulificarAnioConsulta(){
        mesesConsulta = Collections.EMPTY_LIST;
    }
    
    public void setAnioConsulta(Short anioConsulta) {
        this.anioConsulta = anioConsulta;
        if(anioConsulta == null)
            nulificarAnioConsulta();
    }
    
    public void setMesConsulta(String mesConsulta) {
        this.mesConsulta = mesConsulta;
    }
    
}
