package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaVocacional;
import mx.edu.utxj.pye.sgi.facade.Facade;

public class ComparadorEncuestaVocacional implements Comparador<EncuestaVocacional> {

    @Override
    public boolean isCompleto(EncuestaVocacional resultados){
        //System.out.println("Obligatorias" + obligatorias(resultados));
        return obligatorias(resultados);
    }

    /**
     * Compara si ya ha respondido las preguntas obligatorias
     * @param resultados
     * @return true/false
     */
    public boolean obligatorias(EncuestaVocacional resultados){
        boolean t= false;
        if(resultados!=null){
            if(compradorR1(resultados)==true){
                if(comparadoreTexto2(resultados.getR3())==false){
                    if(comparadoreTexto2(String.valueOf(resultados.getR4()))==false){
                        if(comparadoreTexto2(resultados.getR5())==false){
                            if(comparadoreTexto2(resultados.getR6())==false){
                                if(comparadoreTexto2(resultados.getR7())==false){
                                   if(compradorr8(resultados)==true){
                                       t=true;
                                   }else {t=false;}
                                }else {t=false;}
                            }else {t=false;}
                        }else {t=false;}
                    }else {t=false;}
                }else {t=false;}
            }else {t=false;}
        }else {t=false;}

        return t;
    }

    public boolean r3ar7(EncuestaVocacional resultados){
        return comparadoreTexto2(resultados.getR3())
                || comparadoreTexto2(String.valueOf(resultados.getR4()))
                || comparadoreTexto2(resultados.getR5())
                || comparadoreTexto2(resultados.getR6())
                || comparadoreTexto2(resultados.getR7());
    }




    public boolean compradorR1(EncuestaVocacional resultados){
        boolean t =false;
        if(comparadoreTexto2(resultados.getR1())==false){
           if(resultados.getR1().equals("Otros motivos")){
               if(comparadoreTexto2(resultados.getR2())==false){
                   t=true;
               }
               else {t=false;}
           }else {t=true;}
        }else {t=false;}
        //System.out.println("r1" + t);
        return t;
    }
    public boolean compradorr8(EncuestaVocacional resultados){
        boolean t =false;
        if(comparadoreTexto2(resultados.getR8())==false){
            if(resultados.getR8().equals("Si")){
                if(comparadoreTexto2(resultados.getR9())==false){
                    t=true;
                }else {t=false;}
            }else if(resultados.getR8().equals("No")){
                if(comparadoreTexto2(resultados.getR8())==false){
                    t=true;
                }else {t=false;}
            }
        }else {t=false;}
        //System.out.println("r8"+ t);
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
