package mx.edu.utxj.pye.sgi.dto.ch;

import java.util.ArrayList;
import java.util.List;
import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.Articulosp;
import mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Congresos;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrolloSoftware;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrollosTecnologicos;
import mx.edu.utxj.pye.sgi.entity.ch.Distinciones;
import mx.edu.utxj.pye.sgi.entity.ch.Docencias;
import mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales;
import mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica;
import mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas;
import mx.edu.utxj.pye.sgi.entity.ch.Idiomas;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Innovaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Investigaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Lenguas;
import mx.edu.utxj.pye.sgi.entity.ch.LibrosPub;
import mx.edu.utxj.pye.sgi.entity.ch.Memoriaspub;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class CurriculumRIPPPA {    
    
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class CurriculumRIPPPAReportes {
        @Getter        @Setter        @NonNull        Personal personal;
        @Getter        @Setter        @NonNull        Boolean cv;
        @Getter        @Setter        @NonNull        Boolean personales;
        @Getter        @Setter        @NonNull        Boolean escolaridad;
        @Getter        @Setter        @NonNull        Boolean experiencia;  
        @Getter        @Setter        @NonNull        Boolean capacitacion; 
        @Getter        @Setter        @NonNull        String semaforo;        
    } 
   
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class Personales {
        @Getter        @Setter        @NonNull        String rutaA;
        @Getter        @Setter        @NonNull        Boolean cuentaA;
        @Getter        @Setter        @NonNull        String rutaC;  
        @Getter        @Setter        @NonNull        Boolean cuentaC; 
    } 
   
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class Escolaridad {
        @Getter        @Setter        @NonNull        String rutaTp;
        @Getter        @Setter        @NonNull        Boolean cuentaTp;
        @Getter        @Setter        @NonNull        String rutaCp;  
        @Getter        @Setter        @NonNull        Boolean cuentaCp; 
        
        @Getter        @Setter        @NonNull        String rutaTi;
        @Getter        @Setter        @NonNull        Boolean cuentaTi;
        @Getter        @Setter        @NonNull        String rutaCi;  
        @Getter        @Setter        @NonNull        Boolean cuentaCi; 
        
        @Getter        @Setter        @NonNull        String rutaTt;
        @Getter        @Setter        @NonNull        Boolean cuentaTt;
        @Getter        @Setter        @NonNull        String rutaCt;  
        @Getter        @Setter        @NonNull        Boolean cuentaCt; 
        
        @Getter        @Setter        @NonNull        String rutaTm;
        @Getter        @Setter        @NonNull        Boolean cuentaTm;
    } 
   
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class Experiencia {
        @Getter        @Setter        @NonNull        List<RegistrosRIPPPA> ruta41;
        @Getter        @Setter        @NonNull        Boolean cuenta41;        
        @Getter        @Setter        @NonNull        List<RegistrosRIPPPA> ruta42;  
        @Getter        @Setter        @NonNull        Boolean cuenta42;        
        @Getter        @Setter        @NonNull        List<RegistrosRIPPPA> ruta43;
        @Getter        @Setter        @NonNull        Boolean cuenta43;        
        @Getter        @Setter        @NonNull        List<RegistrosRIPPPA> ruta44;  
        @Getter        @Setter        @NonNull        Boolean cuenta44;        
        @Getter        @Setter        @NonNull        List<RegistrosRIPPPA> ruta45;
        @Getter        @Setter        @NonNull        Boolean cuenta45;        
        @Getter        @Setter        @NonNull        List<RegistrosRIPPPA> ruta46;  
        @Getter        @Setter        @NonNull        Boolean cuenta46;        
        @Getter        @Setter        @NonNull        List<RegistrosRIPPPA> ruta47;
        @Getter        @Setter        @NonNull        Boolean cuenta47;        
        @Getter        @Setter        @NonNull        List<RegistrosRIPPPA> ruta48;  
        @Getter        @Setter        @NonNull        Boolean cuenta48;        
        @Getter        @Setter        @NonNull        List<RegistrosRIPPPA> ruta49;  
        @Getter        @Setter        @NonNull        Boolean cuenta49; 
    } 
   
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class Capacitacion {
        @Getter        @Setter        @NonNull        List<RegistrosRIPPPA> rutasEv;
        @Getter        @Setter        @NonNull        Boolean cuentaEv;
    }
    
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class ResumenCV {
        @Getter        @Setter        @NonNull        List<Idiomas> listaIdiomas;
        @Getter        @Setter        @NonNull        List<Lenguas> listaLenguas;
        @Getter        @Setter        @NonNull        List<Congresos> listaCongresos;
        @Getter        @Setter        @NonNull        List<LibrosPub> listaLibrosPubs;
        @Getter        @Setter        @NonNull        List<Articulosp> listaArticulosp;
        @Getter        @Setter        @NonNull        List<Docencias> listaDocenciases;
        @Getter        @Setter        @NonNull        List<Memoriaspub> listaMemoriaspub;
        @Getter        @Setter        @NonNull        List<Innovaciones> listaInnovaciones;
        @Getter        @Setter        @NonNull        List<Distinciones> listaDistinciones;
        @Getter        @Setter        @NonNull        List<Investigaciones> listaInvestigacion;
        @Getter        @Setter        @NonNull        List<DesarrolloSoftware> listaDesarrolloSoftwar;
        @Getter        @Setter        @NonNull        List<FormacionAcademica> listaFormacionAcademica;
        @Getter        @Setter        @NonNull        List<ExperienciasLaborales> listaExperienciasLaborales;
        @Getter        @Setter        @NonNull        List<Capacitacionespersonal> listaCapacitacionespersonal;
        @Getter        @Setter        @NonNull        List<HabilidadesInformaticas> listaHabilidadesInformaticas;
        @Getter        @Setter        @NonNull        List<DesarrollosTecnologicos> listaDesarrollosTecnologicos;
        @Getter        @Setter        @NonNull        Boolean completo; 
    }
    
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class DatosPersonal {
        @Getter        @Setter        @NonNull        Personal personal;
        @Getter        @Setter        @NonNull        ListaPersonal listaPersonal;
        @Getter        @Setter        @NonNull        InformacionAdicionalPersonal adicionalPersonal;
    }
    
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class RegistrosRIPPPA {
        @Getter        @Setter        @NonNull        String idr;
        @Getter        @Setter        @NonNull        String tabla;
        @Getter        @Setter        @NonNull        String alineacion;
        @Getter        @Setter        @NonNull        String ruta;
    }
}
