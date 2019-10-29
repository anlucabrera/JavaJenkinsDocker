package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.controlEscolar.CuestionarioPsicopedagogicoResultados;

public class ComparadorCuestionarioPsicopedagogicoPersonal implements Comparador<CuestionarioPsicopedagogicoResultados> {

    @Override
    public boolean isCompleto(CuestionarioPsicopedagogicoResultados resultados){
        Boolean t= false;
        //Todo: Verifica que los resultados no sean nulos
        if(resultados==null){ t=false; }
        else {
            //TODO: Verifica que las preguntas obligatorias hayan sido respondidas
            if(compradorResultados(resultados)==true){t=false;}
            else {t=true;}
        }
        return t;
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
