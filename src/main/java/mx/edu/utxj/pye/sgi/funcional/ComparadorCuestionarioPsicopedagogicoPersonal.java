package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.controlEscolar.CuestionarioPsicopedagogicoResultados;

import java.util.stream.IntStream;

public class ComparadorCuestionarioPsicopedagogicoPersonal implements Comparador<CuestionarioPsicopedagogicoResultados> {

    @Override
    public boolean isCompleto(CuestionarioPsicopedagogicoResultados resultados){
        Boolean t= false;
        //Verifica que los resultados no sean nulos
        if(resultados==null){ t=false; }
        else {
            //Verifica que las preguntas obligatorias hayan sido respondidas
            if(obligatorias(resultados)==true){t=true;}
            else {t=false;}
        }
        return t;
    }

    /**
     * Compara si ya ha respondido las preguntas obligatorias
     * @param resultados
     * @return true/false
     */
    public boolean obligatorias(CuestionarioPsicopedagogicoResultados resultados){
        boolean t= false;
        if(comparadoreTexto2(resultados.getR45())==false){
            if(resultados.getR45().equals("Si")){
                if(gruposVunerables(resultados)!=0){
                    if(comparadoreTexto2(resultados.getR53())==true){t=false;}
                    else {t=true;}
                }else {t=false;}
            }else if (resultados.getR45().equals("No")){
                if (comparadoreTexto2(resultados.getR53())==true){t=false;}
                else{t= true;}
            }
        }else {t= false;}
        return t;
    }
    public int gruposVunerables(CuestionarioPsicopedagogicoResultados resultados){
      int cont = 0;
      if(comparadoreTexto2(resultados.getR46())==false){cont++;}
      if(comparadoreTexto2(resultados.getR47())==false){cont++;}
      if(comparadoreTexto2(resultados.getR48())==false){cont++;}
      if(comparadoreTexto2(resultados.getR49())==false){cont++;}
      if(comparadoreTexto2(resultados.getR50())==false){cont++;}
      if(comparadoreTexto2(resultados.getR51())==false){cont++;}
      if(comparadoreTexto2(resultados.getR52())==false){cont++;}
      return cont;
    }
    /**
     * Compardor de las preguntas obligatorias para resultados del personal
     * @param resultados
     * @return true/false
     */
    public boolean compradorResultados(CuestionarioPsicopedagogicoResultados resultados){
        return comparadoreTexto2(resultados.getR45())
                || comparadoreTexto2(resultados.getR46())
                || comparadoreTexto2(resultados.getR47())
                || comparadoreTexto2(resultados.getR48())
                || comparadoreTexto2(resultados.getR49())
                || comparadoreTexto2(resultados.getR50())
                || comparadoreTexto2(resultados.getR51())
                || comparadoreTexto2(resultados.getR52())
                || comparadoreTexto2(resultados.getR53())
                ;
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
