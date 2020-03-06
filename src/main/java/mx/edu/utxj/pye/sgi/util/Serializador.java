package mx.edu.utxj.pye.sgi.util;

import mx.edu.utxj.pye.sgi.dto.DtoAreaAcademica;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.consulta.DtoSatisfaccionServiciosEncuesta;
import mx.edu.utxj.pye.sgi.dto.consulta.DtoSatisfaccionServiciosEstudiante;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static ResultadoEJB<String> serializarEncuestaServiciosResultados(EncuestaServiciosResultados encuestaServiciosResultados){
        try{
            String ruta = generarCarpetaContenedora(EncuestaServiciosResultados.class.getName(), encuestaServiciosResultados.getEncuestaServiciosResultadosPK().getEvaluacion());
            ResultadoEJB<String> serializar = serializar(encuestaServiciosResultados, ruta, String.valueOf(encuestaServiciosResultados.getEncuestaServiciosResultadosPK().getEvaluador()));
            if(serializar.getCorrecto()) return ResultadoEJB.crearCorrecto(serializar.getValor(), "Objeto serializado");
            else return ResultadoEJB.crearErroneo(serializar.getResultado(), serializar.getMensaje(), String.class);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al serializar el objeto.", e, String.class);
        }
    }

    public static ResultadoEJB<String> serializarDtoSatisfaccionServiciosEncuesta(DtoSatisfaccionServiciosEncuesta dtoSatisfaccionServiciosEncuesta){
        try{
            String ruta = generarCarpetaContenedora(DtoSatisfaccionServiciosEncuesta.class.getName(), dtoSatisfaccionServiciosEncuesta.getEvaluacion().getEvaluacion());
            ResultadoEJB<String> serializar = serializar(dtoSatisfaccionServiciosEncuesta, ruta, String.valueOf(dtoSatisfaccionServiciosEncuesta.getEvaluacion().getTipo()));
            if(serializar.getCorrecto()) return ResultadoEJB.crearCorrecto(serializar.getValor(), "Objeto serializado");
            else return ResultadoEJB.crearErroneo(serializar.getResultado(), serializar.getMensaje(), String.class);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al serializar el objeto.", e, String.class);
        }
    }

    public static ResultadoEJB<String> serializarDtoAreaAcademica(DtoAreaAcademica dtoAreaAcademica){
        try{
//            System.out.println("Serializador.serializarDtoAreaAcademica");
            String ruta = generarCarpetaContenedora(DtoAreaAcademica.class.getName(), dtoAreaAcademica.getPeriodoDeConsulta());
//            System.out.println("iniciando serialización de  dtoAreaAcademica");
            ResultadoEJB<String> serializar = serializar(dtoAreaAcademica, ruta, dtoAreaAcademica.getAreaAcademica().getSiglas());
//            System.out.println("serializar = " + serializar);
//            System.out.println("terminando serialización de  dtoAreaAcademica");
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

    public static ResultadoEJB<DtoAreaAcademica> deserializarDtoAreaAcademica(String siglas, Integer periodoDeConsulta){
        try{
            String ruta = generarCarpetaContenedora(DtoAreaAcademica.class.getName(), periodoDeConsulta);
            ResultadoEJB<Object> deserializar = deserializar(siglas, ruta);
            if(deserializar.getCorrecto()) return ResultadoEJB.crearCorrecto((DtoAreaAcademica) deserializar.getValor(), "Objeto deserializado");
            else return ResultadoEJB.crearErroneo(deserializar.getResultado(), deserializar.getMensaje(), DtoAreaAcademica.class);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al serializar el objeto.", e, DtoAreaAcademica.class);
        }
    }

    public static ResultadoEJB<DtoSatisfaccionServiciosEncuesta> deserializarDtoSatisfaccionServiciosEncuesta(String tipo, Integer evaluacion){
        try{
            String ruta = generarCarpetaContenedora(DtoSatisfaccionServiciosEncuesta.class.getName(), evaluacion);
            ResultadoEJB<Object> deserializar = deserializar(tipo, ruta);
            if(deserializar.getCorrecto()) return ResultadoEJB.crearCorrecto((DtoSatisfaccionServiciosEncuesta) deserializar.getValor(), "Objeto deserializado");
            else return ResultadoEJB.crearErroneo(deserializar.getResultado(), deserializar.getMensaje(), DtoSatisfaccionServiciosEncuesta.class);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al serializar el objeto.", e, DtoSatisfaccionServiciosEncuesta.class);
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

    public static <T> ResultadoEJB<List<T>> deserializarMasivo(String className, Integer periodo, Class<T> clase){
        try{
            String ruta = generarCarpetaContenedora(className, periodo);
            ServicioArchivos.addCarpetaRelativa(ruta);
            List<T> objects = new Vector<>();
            try(Stream<Path> walk = Files.walk(Paths.get(ruta))){
                List<String> rutasArchivo = walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList());
                rutasArchivo.parallelStream().forEach(rutaArchivo -> {
                    try{
                        FileInputStream fileInputStream = new FileInputStream(rutaArchivo);
                        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                        T t = (T)objectInputStream.readObject();
                        objects.add(t);
                        objectInputStream.close();
                        fileInputStream.close();
                    } catch (IOException|ClassNotFoundException e){e.printStackTrace();}
                });
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
            if(objects.isEmpty()) return ResultadoEJB.crearErroneo(2, null, "No se encontraron archivos");
            return ResultadoEJB.crearCorrecto(objects, "Objeto desserializado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al serializar los objetos.", e, null);
        }
    }
}
