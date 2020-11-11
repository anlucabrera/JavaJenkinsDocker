package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.ch.CuestionarioComplementarioInformacionPersonal;

public class ComparadorCuestionarioComplementarioInfPersonal implements Comparador<CuestionarioComplementarioInformacionPersonal> {

    @Override
    public boolean isCompleto(CuestionarioComplementarioInformacionPersonal resultados){
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
    public boolean obligatorias(CuestionarioComplementarioInformacionPersonal resultados){
        boolean t= false;
       if(resultados!=null){
          if (compradorTipoLengua(resultados)==true){
              if(compradorDiscapacidad(resultados)==true){
                 if(resultados.getR5tipoSangre()==null
                    || resultados.getR6EstadoCivil()==null
                    || resultados.getR7noHijos()==null
                    || resultados.getR8TelefonoCel()==null
                 ){t=false;}
                 else {t=true;}
              }else {t=false;}
          }else {t=false;}
       }else {t=true;}
        return t;
    }

    public boolean compararGenerales(CuestionarioComplementarioInformacionPersonal resultados){
        return comparadoreTexto2(Short.toString(resultados.getR5tipoSangre()))
                || comparadoreTexto2(resultados.getR6EstadoCivil())
                || comparadoreTexto2(resultados.getR7noHijos())
                || comparadoreTexto2(resultados.getR8TelefonoCel());
    }
    public boolean compradorTipoLengua(CuestionarioComplementarioInformacionPersonal resultados){
        boolean t =false;
        if(comparadoreTexto2(resultados.getR1lenguaIndigina())==false){
            if(resultados.getR1lenguaIndigina().equals("Si")){
                if(resultados.getR2tipoLengua()!=null){
                    t=true;
                }else {t=false;}
            }else if(resultados.getR1lenguaIndigina().equals("No")){
                if(comparadoreTexto2(resultados.getR1lenguaIndigina())==false){
                    t=true;
                }else {t=false;}
            }
        }else {t=false;}

        return t;
    }
    public boolean compradorDiscapacidad(CuestionarioComplementarioInformacionPersonal resultados){
        boolean t =false;
        if(comparadoreTexto2(resultados.getR3Discapacidad())==false){
            if(resultados.getR3Discapacidad().equals("Si")){
                if(resultados.getR4tipoDiscapacidad()!=null){
                    t=true;
                }else {t=false;}
            }else if(resultados.getR3Discapacidad().equals("No")){
                if(comparadoreTexto2(resultados.getR1lenguaIndigina())==false){
                    t=true;
                }else {t=false;}
            }
        }else {t=false;}
        return t;
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
