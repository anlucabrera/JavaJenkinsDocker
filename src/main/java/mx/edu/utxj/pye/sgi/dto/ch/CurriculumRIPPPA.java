package mx.edu.utxj.pye.sgi.dto.ch;

import java.util.List;
import lombok.*;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class CurriculumRIPPPA {    
    
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class CurriculumRIPPPAReportes {
        @Getter        @Setter        @NonNull        Personales personales;
        @Getter        @Setter        @NonNull        Escolaridad escolaridad;
        @Getter        @Setter        @NonNull        Experiencia experiencia;  
        @Getter        @Setter        @NonNull        Capacitacion capacitacion; 
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
        @Getter        @Setter        @NonNull        List<String> ruta41;
        @Getter        @Setter        @NonNull        Boolean cuenta41;        
        @Getter        @Setter        @NonNull        List<String> ruta42;  
        @Getter        @Setter        @NonNull        Boolean cuenta42;        
        @Getter        @Setter        @NonNull        List<String> ruta43;
        @Getter        @Setter        @NonNull        Boolean cuenta43;        
        @Getter        @Setter        @NonNull        List<String> ruta44;  
        @Getter        @Setter        @NonNull        Boolean cuenta44;        
        @Getter        @Setter        @NonNull        List<String> ruta45;
        @Getter        @Setter        @NonNull        Boolean cuenta45;        
        @Getter        @Setter        @NonNull        List<String> ruta46;  
        @Getter        @Setter        @NonNull        Boolean cuenta46;        
        @Getter        @Setter        @NonNull        List<String> ruta47;
        @Getter        @Setter        @NonNull        Boolean cuenta47;        
        @Getter        @Setter        @NonNull        List<String> ruta48;  
        @Getter        @Setter        @NonNull        Boolean cuenta48;        
        @Getter        @Setter        @NonNull        List<String> ruta49;  
        @Getter        @Setter        @NonNull        Boolean cuenta49; 
    } 
   
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class Capacitacion {
        @Getter        @Setter        @NonNull        List<String> rutasEv;
        @Getter        @Setter        @NonNull        Boolean cuentaEv;
    }
}
