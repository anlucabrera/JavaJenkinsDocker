/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.eb;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author UTXJ
 */
public class Ejemplo {
    
    public static void main (String args[]){
        System.out.println("Lectura de archivo en una carpeta: ");
        try {
            List<String> listarArchivos = new ArrayList<>();
            if (Files.exists(Paths.get("C:\\archivos\\"), LinkOption.NOFOLLOW_LINKS)) {
                //Prueba de solo lectura de carpetas
                Files.list(Paths.get("C:\\archivos\\modulos_registro\\")).forEach(path -> {
                    System.out.println("SOLO CARPETAS: " + path.getParent().toString()+File.separator+path.getRoot().toString());
                });

                //Lista general
                Files.list(Paths.get("C:\\archivos\\modulos_registro\\2018\\dpye\\calidad_academica\\diciembre\\actividades_varias\\")).forEach((t) -> {
                    if (t.toString().endsWith(".xlsx")) {
                        listarArchivos.add(t.getParent().toString());
                    }
                });

                System.out.println("mx.edu.utxj.pye.siip.services.ca.Ejemplo.main() Solo nombres");
                listarArchivos.forEach((t) -> {
                    System.out.println(Paths.get(t).getFileName());
                });
                
                System.out.println("mx.edu.utxj.pye.siip.services.ca.Ejemplo.main() Lista de archivos");
                listarArchivos.forEach(System.out::println);

            } else {
                System.out.println("mx.edu.utxj.pye.siip.services.ca.Ejemplo.main() Aún no existen archivos en el directorio o bien el directorio no existe");
            }

        } catch (IOException ex) {
            Logger.getLogger(Ejemplo.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("--------------------------------------------------------");
//        
//        try (Stream<Path> stream = Files.list(Paths.get("C:\\archivos\\modulos_registro\\2018\\dpye\\calidad_academica\\diciembre\\actividades_varias\\"))) {
//            stream.map(String::valueOf)
//                    .filter(path -> path.endsWith(".xlsx"))
//                    .forEach(System.out::println);
//
//        } catch (IOException ex) {
//            Logger.getLogger(Ejemplo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
        System.out.println("--------------------------------------------------------");
        
        Integer numero = 0;
        Calendar calendario = new GregorianCalendar();
        numero = calendario.get(Calendar.MONTH)+1;
        System.out.println("mx.edu.utxj.pye.siip.services.ca.Ejemplo.main() Mes: " + numero);
        
        Integer anio = calendario.get(Calendar.YEAR);
        System.out.println("mx.edu.utxj.pye.siip.services.ca.Ejemplo.main() Anio " + anio);
        
        String telefono = "(764) 764-7575";
        String telefonoNuevo = quitarGuionesEspacios(telefono);
        System.out.println("mx.edu.utxj.pye.siip.services.ca.Ejemplo.main() " + telefonoNuevo);
        
        String espacios = "7 6 4 7 6 7 7 5 7 5";
        String espacioNuevo = quitarGuionesEspacios(espacios);
        System.out.println("mx.edu.utxj.pye.siip.services.ca.Ejemplo.main() espacios: " + espacioNuevo);
        
        
        Stream<Integer> registro1 = Stream.of(1, 2, 3, 4, 5);
        Stream<Integer> registro2 = Stream.of(1, 2, 3, 4, 5);

        List<Integer> total1 = registro1.collect(Collectors.toList());
        List<Integer> total2 = registro2.collect(Collectors.toList());

//        total1.forEach(x -> System.out.println(x));
        
        List<Integer> claves = new ArrayList<>();
        
        total1.forEach((t) -> {
            claves.add(t);
        });
        
        total2.forEach((t) -> {
            claves.add(t);
        });
        
        claves.stream().forEach((t) -> {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.Ejemplo.main() Numero: " + t);
        });

        System.out.println("-------------------------------------------------------");
        System.out.println("Ejecudado desde IntelliJ IDEA");
        
        
        System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() Método de ejemplo de la clase LocalDate");
        Date fechaInicioActividadCorrecta = new Date(2019, 02, 23);
        Date fechaInicioActividadIncorrecta = new Date(2019, 01, 15);
        
//        Fechas de evento
        Date fechaInicioEventoBD = new Date(2019-02-16);
        Date fechaFinEventoBD = new Date(2019,03,05);
        
        LocalDate fechaInicioActividad = fechaInicioActividadCorrecta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaInicioActividadInc = fechaInicioActividadIncorrecta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaInicioEvento = fechaInicioEventoBD.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaFinEvento = fechaFinEventoBD.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
//        Fechas correctas
        System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() fechas correctas: ");
        System.out.println("Fecha InicioEvento: " + fechaInicioActividad.compareTo(fechaInicioEvento));
        System.out.println("Fecha FinEvento:    " + fechaInicioActividad.compareTo(fechaFinEvento));
        
        System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() fechas incorrectas: ");
        System.out.println("Fecha InicioEvento:     " + fechaInicioActividadInc.compareTo(fechaInicioEvento));
        System.out.println("Fecha FinEvento;        " + fechaInicioActividadInc.compareTo(fechaFinEvento));
    }

    public static String quitarEspaciosTelefono(String cadena) {
        if (cadena == null) {
            return "";
        }
        return cadena.replace(" ", "").trim();
    }
    public static String quitarGuionesEspacios(String string){
        if (string == null) {
            return null;
        }

        return Normalizer.normalize(string.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                /*.replaceAll("[^\\p{Alnum}]+", ".")*/
                .replaceAll("[\\D]+", "")
                .replaceAll(" ", "");
    }
}
