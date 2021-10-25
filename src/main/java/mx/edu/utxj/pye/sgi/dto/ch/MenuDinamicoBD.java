package mx.edu.utxj.pye.sgi.dto.ch;

import java.util.List;
import lombok.*;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class MenuDinamicoBD {    
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class EncabezadosMenu {
        @Getter        @Setter        private List<String> encabezado;
        @Getter        @Setter        private List<Nivel1> primerNivel;
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class Nivel1 {
        @Getter        @Setter        private List<String> titulo;
        @Getter        @Setter        private String icono;
        @Getter        @Setter        private String enlace;
        @Getter        @Setter        private String estaus;
        @Getter        @Setter        private String tipoenlace;
        @Getter        @Setter        private List<Nivel2> segundoNivel;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class Nivel2 {
        @Getter        @Setter        private List<String> titulo;
        @Getter        @Setter        private String icono;
        @Getter        @Setter        private String enlace;
        @Getter        @Setter        private String estaus;
        @Getter        @Setter        private String tipoenlace;
        @Getter        @Setter        private List<Nivel3> tercerNivel;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class Nivel3 {
        @Getter        @Setter        private List<String> titulo;
        @Getter        @Setter        private String icono;
        @Getter        @Setter        private String enlace;
        @Getter        @Setter        private String estaus;
        @Getter        @Setter        private String tipoenlace;
        @Getter        @Setter        private List<Nivel4> cuartoNivel;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class Nivel4 {
        @Getter        @Setter        private List<String> titulo;
        @Getter        @Setter        private String icono;
        @Getter        @Setter        private String enlace;
        @Getter        @Setter        private String estaus;
        @Getter        @Setter        private String tipoenlace;
        @Getter        @Setter        private List<Nivel5> quintoNivel;
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class Nivel5 {
        @Getter        @Setter        private List<String> titulo;
        @Getter        @Setter        private String icono;
        @Getter        @Setter        private String enlace;
        @Getter        @Setter        private String estaus;
        @Getter        @Setter        private String tipoenlace;
    }
}
