package mx.edu.utxj.pye.sgi.util;

import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.consulta.DtoSatisfaccionServiciosEstudiante;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;

import java.io.*;

public class Serializador implements Serializable {
    public static ResultadoEJB<String> serializarMatriculaPeriodoEscolar(MatriculaPeriodosEscolares matricula){
        try{
            String ruta = generarCarpetaContenedora(MatriculaPeriodosEscolares.class.getName(), matricula.getPeriodo());
            ResultadoEJB<String> serializar = serializar(matricula, ruta, matricula.getMatricula());
            if(serializar.getCorrecto()) return ResultadoEJB.crearCorrecto(serializar.getValor(), "Objeto serializado");
            else return ResultadoEJB.crearErroneo(serializar.getResultado(), serializar.getMensaje(), String.class);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al serializar el objeto.", e, String.class);
        }
    }

    public static ResultadoEJB<MatriculaPeriodosEscolares> deserializarMatriculaPeriodoEscolar(String matricula, Integer periodo){
        try{
            String ruta = generarCarpetaContenedora(MatriculaPeriodosEscolares.class.getName(), periodo);
            ResultadoEJB<Object> deserializar = deserializar(matricula, ruta);
            if(deserializar.getCorrecto()) return ResultadoEJB.crearCorrecto((MatriculaPeriodosEscolares) deserializar.getValor(), "Objeto deserializado");
            else return ResultadoEJB.crearErroneo(deserializar.getResultado(), deserializar.getMensaje(), MatriculaPeriodosEscolares.class);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al serializar el objeto.", e, MatriculaPeriodosEscolares.class);
        }
    }

    public static ResultadoEJB<String> serializarDtoSatisfaccionServiciosEstudiante(DtoSatisfaccionServiciosEstudiante estudiante){
        try{
            String ruta = generarCarpetaContenedora(DtoSatisfaccionServiciosEstudiante.class.getName(), estudiante.getDtoEstudiantePeriodo().getPeriodo().getPeriodo());
            ResultadoEJB<String> serializar = serializar(estudiante, ruta, estudiante.getDtoEstudiantePeriodo().getMatriculaPeriodosEscolares().getMatricula());
            if(serializar.getCorrecto()) return ResultadoEJB.crearCorrecto(serializar.getValor(), "Objeto serializado");
            else return ResultadoEJB.crearErroneo(serializar.getResultado(), serializar.getMensaje(), String.class);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al serializar el objeto.", e, String.class);
        }
    }

    public static ResultadoEJB<DtoSatisfaccionServiciosEstudiante> deserializarDtoSatisfaccionServiciosEstudiante(String matricula, Integer periodo){
        try{
            String ruta = generarCarpetaContenedora(DtoSatisfaccionServiciosEstudiante.class.getName(), periodo);
            ResultadoEJB<Object> deserializar = deserializar(matricula, ruta);
            if(deserializar.getCorrecto()) return ResultadoEJB.crearCorrecto((DtoSatisfaccionServiciosEstudiante) deserializar.getValor(), "Objeto deserializado");
            else return ResultadoEJB.crearErroneo(deserializar.getResultado(), deserializar.getMensaje(), DtoSatisfaccionServiciosEstudiante.class);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al serializar el objeto.", e, DtoSatisfaccionServiciosEstudiante.class);
        }
    }

    public static ResultadoEJB<MatriculaPeriodosEscolares> deserializarMatriculaPeriodoEscolar(Integer matricula, Integer periodo){
        return deserializarMatriculaPeriodoEscolar(matricula.toString(), periodo);
    }

    public static ResultadoEJB<DtoSatisfaccionServiciosEstudiante> deserializarDtoSatisfaccionServiciosEstudiante(Integer matricula, Integer periodo){
        return deserializarDtoSatisfaccionServiciosEstudiante(matricula.toString(), periodo);
    }

    public static String generarCarpetaContenedora(String clase, Integer periodo){
        String rutaRelativa = ServicioArchivos.genRutaRelativa(clase, periodo);
        String ruta = ServicioArchivos.carpetaRaiz.concat("serializados").concat(File.separator).concat(rutaRelativa);

        return ruta;
    }

    public static ResultadoEJB<String> serializar(Object object, String ruta, String fileName){
        try{
            ServicioArchivos.addCarpetaRelativa(ruta);
            String rutaArchivo = ruta.concat(fileName).concat(".ser");
            FileOutputStream fileOutputStream = new FileOutputStream(rutaArchivo);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
            fileOutputStream.close();
            return ResultadoEJB.crearCorrecto(rutaArchivo, "Objeto serializado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al serializar el objeto.", e, String.class);
        }
    }

    public static ResultadoEJB<Object> deserializar(String fileName, String ruta){
        try{
            String rutaArchivo = ruta.concat(fileName).concat(".ser");

            FileInputStream fileInputStream = new FileInputStream(rutaArchivo);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            return ResultadoEJB.crearCorrecto(object, "Objeto desserializado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al serializar el objeto.", e, Object.class);
        }
    }
}
