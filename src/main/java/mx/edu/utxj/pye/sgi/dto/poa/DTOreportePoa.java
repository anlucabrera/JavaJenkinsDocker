/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.poa;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.ch.Permisosevaluacionpoaex;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CapitulosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.Partidas;
import mx.edu.utxj.pye.sgi.entity.pye2.PretechoFinanciero;
import mx.edu.utxj.pye.sgi.entity.pye2.Productos;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAreas;
import mx.edu.utxj.pye.sgi.entity.pye2.RecursosActividad;
import mx.edu.utxj.pye.sgi.entity.pye2.UnidadMedidas;

/**
 *
 * @author Desarrollo
 */
@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DTOreportePoa {

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class General {
        @Getter @Setter @NonNull AreasUniversidad areasUniversidad;
        @Getter @Setter @NonNull Personal personal;
        @Getter @Setter @NonNull EjesRegistro ejesRegistro;
        @Getter @Setter @NonNull Estrategias estrategias;
        @Getter @Setter @NonNull LineasAccion lineasAccion;
        @Getter @Setter @NonNull ActividadesPoa actividadesPoa;
        @Getter @Setter @NonNull UnidadMedidas medidas;
        @Getter @Setter @NonNull RecursosActividad recursosActividad;
        @Getter @Setter @NonNull ProductosAreas productosAreas;
        @Getter @Setter @NonNull Productos productos;
        @Getter @Setter @NonNull Partidas partidas;
        @Getter @Setter @NonNull CapitulosTipos capitulosTipos;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class CuadroMI {
        @Getter @Setter @NonNull EjesRegistro ejesRegistro;
        @Getter @Setter @NonNull List<Estrategias> estrategiases;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class ProgramacionActividades {
        @Getter @Setter @NonNull EjesRegistro ejesRegistro;
        @Getter @Setter @NonNull List<EstrategiaLineas> estrategiaLineas;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class EstrategiaLineas {
        @Getter @Setter @NonNull Estrategias estrategias;
        @Getter @Setter @NonNull List<ActividadRecurso> actividadeRecurso;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class ActividadRecurso {
        @Getter @Setter @NonNull ActividadesPoa actividadesPoa;
        @Getter @Setter @NonNull UnidadMedidas medidas;
        @Getter @Setter @NonNull List<RecursoActividad> recursosActividad;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class RecursoActividad {
        @Getter @Setter @NonNull RecursosActividad recursosActividad;
        @Getter @Setter @NonNull ProductosAreas productosAreas;
        @Getter @Setter @NonNull Productos productos;
        @Getter @Setter @NonNull Partidas partidas;
        @Getter @Setter @NonNull CapitulosTipos capitulosTipos;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class CapitulosLista {
        @Getter @Setter @NonNull Partidas partidas1;
        @Getter @Setter @NonNull Double total;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class ListaEjeEstrategia {
        @Getter @Setter @NonNull private EjesRegistro ejess;
        @Getter @Setter @NonNull private List<Estrategias> estrategiases;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class ListaEjesEsLaAp {
        @Getter @Setter @NonNull private EjesRegistro ejeA;
        @Getter @Setter @NonNull private List<ListaEstrategiaActividades> listalistaEstrategiaLaAp;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class ListaEstrategiaActividades {
        @Getter @Setter @NonNull private Estrategias estrategias;
        @Getter @Setter @NonNull private List<Actividad> actividadesPoas;
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class ListaEjesEsLaApPOA {
        @Getter @Setter @NonNull private EjesRegistro ejeA;
        @Getter @Setter @NonNull private List<ListaEstrategiaActividadesPOA> listalistaEstrategiaLaAp;
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class ListaEstrategiaActividadesPOA {
        @Getter @Setter @NonNull private Estrategias estrategias;
        @Getter @Setter @NonNull private List<ActividadesPoa> actividadesPoas;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class Actividad {
        @Getter @Setter @NonNull private ActividadesPoa actividadesPoa;
        @Getter @Setter @NonNull private Double totalPCuatrimestre;
        @Getter @Setter @NonNull private Double totalACuatrimestre;
        @Getter @Setter @NonNull private Double totalPCorte;
        @Getter @Setter @NonNull private Double totalACorte;
        @Getter @Setter @NonNull private Double porcentajeCuatrimestre, porcentejeAlCorte;
        @Getter @Setter @NonNull private String semaforoC;
        @Getter @Setter @NonNull private String semaforoG;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class PresupuestoPOA {
        @Getter @Setter @NonNull private Short areaID;
        @Getter @Setter @NonNull private String area;
        @Getter @Setter @NonNull private String responsable;
        @Getter @Setter @NonNull private PretechoFinanciero cap2000;
        @Getter @Setter @NonNull private PretechoFinanciero cap3000;        
        @Getter @Setter @NonNull private PretechoFinanciero cap4000;
        @Getter @Setter @NonNull private PretechoFinanciero capdder;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class ProcesoDetallePoa {
        @Getter @Setter @NonNull private AreasUniversidad universidad;
        @Getter @Setter @NonNull private ListaPersonal personal;
        @Getter @Setter @NonNull private Procesopoa procesopoa;        
        @Getter @Setter @NonNull private List<Permisosevaluacionpoaex> permisosevaluacionpoaexs;
        @Getter @Setter @NonNull private Boolean ract;
        @Getter @Setter @NonNull private Boolean rrec;
        @Getter @Setter @NonNull private Boolean rjus;
        @Getter @Setter @NonNull private Boolean vact;
        @Getter @Setter @NonNull private Boolean vrec;
        @Getter @Setter @NonNull private Boolean vjus;        
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class VistaRecurso {
        @Getter @Setter @NonNull private Double totalPretecho;
        @Getter @Setter @NonNull private Double totalProgramado;
        @Getter @Setter @NonNull private Double totalDisponible;
        @Getter @Setter @NonNull private String colorClase;
        @Getter @Setter @NonNull private Boolean rendered;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class EjeListaEstrategias {
        @Getter @Setter @NonNull private EjesRegistro ejeA;
        @Getter @Setter @NonNull private List<EstrategiasListaLineasAccion> listaLineasAccions;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class EstrategiasListaLineasAccion {
        @Getter @Setter @NonNull private Estrategias etra;
        @Getter @Setter @NonNull private List<LineasAccionListaActividad> listalistaEstrategiaLaAp;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class LineasAccionListaActividad {
        @Getter @Setter @NonNull private LineasAccion lineasAccion;
        @Getter @Setter @NonNull private List<ActividadListaRecursoActividades> listaRecursoActividadeses;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class ActividadListaRecursoActividades {
        @Getter @Setter @NonNull private ActividadesPoa actividadesPoa1;
        @Getter @Setter @NonNull private List<RecursosActividad> recacts;
    }
}
