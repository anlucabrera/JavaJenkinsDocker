package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.controlador.controlEscolar.CuestionarioPsicopedagogicoEstudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CuestionarioPsicopedagogicoResultados;

public class ComparadorCuestionarioPsicopedagogicoEstudiante implements Comparador<CuestionarioPsicopedagogicoResultados> {

    @Override
    public boolean isCompleto(CuestionarioPsicopedagogicoResultados resultados){
        Boolean t= false;
        //Verifica que los resultados no sean nulos
        if(resultados==null){ t=false; }
        else {
            //Comprueba que todas las preguntas obligatorias generales esten contestadas
            if(obligatoriasGenerales(resultados)==false){
                t=true;
                //Verifica si en la pregunta número 54 respondio "Si", comprueba que haya respondido la pregunta 55
                if(resultados.getR54().equals("Si")){
                    if (obligatoriaP55(resultados)==false){ t= true; }
                    else{ t= false;}
                }
                //Verfica si en la pregunta 16 respondio "Finados", en caso de que si, comprueba que haya sido respondida la pregunta 17
                if(resultados.getR16().equals("Finados")){
                    if (obligatoriaP17(resultados)==false){ t= true; }
                    else{ t= false;}
                }
                //Verifica si en la pregunta 57 respondio "Si", en caso de que si, verifica que la pregunta 58 hayan sido respondidas
                if(resultados.getR57().equals("Si")){
                    if(obligatoriaP58(resultados)==false){ t=true; }
                    else { t=false; }
                }
                //Verifica si en la pregunta 19 respondio "Si", en caso de que si, verifica que la pregunta 20 y 21 hayan sido respondidas
                if(resultados.getR19().equals("Si")){
                    if(obligatoriaP20(resultados)==false){ t=true; }
                    else { t=false; }
                }
                // Verifica si en la pregunta 23 respondio "Si", en caso de que si, verifica que haya repondido la pregunta 24
                if(resultados.getR23().equals("Si")){
                    if(obligatoriaP24(resultados)==false){ t= true;
                    }else { t=false; }
                }
                //Verifica si en la pregunta 25 haya repondido "Si", en caso de que si, verifica que haya respondido las preguntas de la 26 -42
                if(resultados.getR25().equals("Si")){
                    if(obligatorias26(resultados)==false){ t= true;
                    }else { t=false; }
                }
                //Verifica si en la pregunta 43 haya repondido "Si", en caso de que si, verifica que haya respondido la pregunta 44
                if(resultados.getR43().equals("Si")){
                    if(obligatoriaP44(resultados)==false){ t= true; }
                    else { t=false; }
                }
            }else {
                t=false;
            }
        }
        return t;
    }
    public boolean obligatoriasGenerales(CuestionarioPsicopedagogicoResultados resultados){
       return   comparadoreTexto2(resultados.getR1())
                || comparadoreTexto(resultados.getR2(),25)
                || comparadoreTexto(resultados.getR3(),25)
                || comparadoreTexto(resultados.getR4(),25)
                || comparadoreTexto(resultados.getR5(),15)
                || comparadoreTexto(resultados.getR6(),25)
                || comparadoreTexto(resultados.getR7(),15)
                || comparadoreTexto2(resultados.getR8())
                || comparadoreTexto2(resultados.getR9())
                || comparadoreTexto(resultados.getR10(),20)
                || comparadoreTexto(resultados.getR11(),20)
                || comparadoreTexto(resultados.getR12(),20)
                || comparadoreTexto(resultados.getR13(),20)
                || comparadoreTexto(resultados.getR14(),20)
               || comparadoreTexto2(resultados.getR54())
                || comparadoreTexto(resultados.getR15(),20)
                || comparadoreTexto2(resultados.getR16())
                || comparadoreTexto(resultados.getR18(),20)
               || comparadoreTexto2(resultados.getR56())
               || comparadoreTexto2(resultados.getR57())
               || comparadoreTexto2(resultados.getR19())
               || comparadoreTexto2(resultados.getR21())
               || comparadoreTexto(resultados.getR59(),20)
               || comparadoreTexto(resultados.getR22(),10)
                || comparadoreTexto2(resultados.getR23())
                || comparadoreTexto2(resultados.getR25())
               || comparadoreTexto2(resultados.getR43());
    }

    /**
     * Comparador para preguntas de la 26 a 42
     * @param resultados
     * @return
     */
    public boolean obligatorias26(CuestionarioPsicopedagogicoResultados resultados){
        return comparadoreTexto2(resultados.getR26())
                || comparadoreTexto2(resultados.getR27())
                || comparadoreTexto2(resultados.getR28())
                || comparadoreTexto2(resultados.getR29())
                || comparadoreTexto2(resultados.getR30())
                || comparadoreTexto2(resultados.getR31())
                || comparadoreTexto2(resultados.getR32())
                || comparadoreTexto2(resultados.getR33())
                || comparadoreTexto2(resultados.getR34())
                || comparadoreTexto2(resultados.getR35())
                || comparadoreTexto2(resultados.getR36())
                || comparadoreTexto2(resultados.getR37())
                || comparadoreTexto2(resultados.getR38())
                || comparadoreTexto2(resultados.getR39())
                || comparadoreTexto2(resultados.getR40())
                || comparadoreTexto2(resultados.getR41())
                || comparadoreTexto2(resultados.getR42());

    }
    public boolean obligatoriaP17(CuestionarioPsicopedagogicoResultados resultados){
        return comparadoreTexto2(resultados.getR17());
    }
    public boolean obligatoriaP20(CuestionarioPsicopedagogicoResultados resultados){
        return  comparadoreTexto2(resultados.getR20());
    }
    public boolean obligatoriaP24(CuestionarioPsicopedagogicoResultados resultados){
        return comparadoreTexto(resultados.getR24(),10);
    }
    public boolean obligatoriaP44(CuestionarioPsicopedagogicoResultados resultados){
        return comparadoreTexto(resultados.getR44(),10);
    }
    public boolean obligatoriaP55(CuestionarioPsicopedagogicoResultados resultados){
        return comparadoreTexto2(resultados.getR55());
    }
    public boolean obligatoriaP58(CuestionarioPsicopedagogicoResultados resultados){
        return  comparadoreTexto2(resultados.getR58());
    }

    public boolean comparadoreTexto(String pregunta, int t) {
        if (pregunta == null) {
            return true;
        } else if (pregunta.trim().isEmpty()) {
            return true;
        }else if(pregunta.trim().length()< t){
          return true;
        } else {
            return false;
        }
    }
    public boolean comparadoreTexto2(String pregunta) {
        if (pregunta == null) {
            return true;
        } else if (pregunta.trim().isEmpty()) {
            return true;
        }else {
            return false;
        }
    }

}
