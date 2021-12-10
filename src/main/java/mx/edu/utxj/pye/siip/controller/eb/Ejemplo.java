/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.eb;

import com.google.zxing.NotFoundException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import mx.edu.utxj.pye.sgi.ejb.ch.ServicioCarga;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SegurosFacultativosEstudiante;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoEstado;
import mx.edu.utxj.pye.sgi.enums.RolNotificacion;
import mx.edu.utxj.pye.sgi.enums.SeguroFacultativoValidacion;
import mx.edu.utxj.pye.sgi.enums.SegurosFacultativosEstatus;
import mx.edu.utxj.pye.siip.dto.eb.DTOArchivoRepositorio;
import mx.edu.utxj.pye.siip.dto.eb.DTORepositorio;
import nl.lcs.qrscan.core.QrPdf;

/**
 *
 * @author UTXJ
 */
public class Ejemplo {
    
    public static void main (String args[]){
//        Integer valor = 20;
//        if(valor > 20) System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() valor de la constante: " + valor);
//        
//        System.out.println("----------------------------------------------------------------------------------------------------------------");
//        
//        List<SegurosFacultativosEstudiante> listaSeguroFacultativo = new ArrayList<>();
//        SegurosFacultativosEstudiante seguroFacultativo = new SegurosFacultativosEstudiante(1, SeguroFacultativoValidacion.EN_ESPERA_DE_VALIDACION.getLabel(),new Date(), SegurosFacultativosEstatus.REGISTRADO.getLabel());
//        SegurosFacultativosEstudiante seguroFacultativo2 = new SegurosFacultativosEstudiante(2, SeguroFacultativoValidacion.BAJA.getLabel(), new Date(), SegurosFacultativosEstatus.VALIDADO.getLabel());
//        SegurosFacultativosEstudiante seguroFacultativo3 = new SegurosFacultativosEstudiante(3, SeguroFacultativoValidacion.BAJA.getLabel(), new Date(),SegurosFacultativosEstatus.VALIDADO.getLabel());
//        
//        listaSeguroFacultativo.add(seguroFacultativo);
//        listaSeguroFacultativo.add(seguroFacultativo2);
//        listaSeguroFacultativo.add(seguroFacultativo3);
//        
//        listaSeguroFacultativo.forEach(System.out::println);
//        System.err.println("Depues del filtrado");
//        
//        SegurosFacultativosEstudiante seguroFacultativoSeleccionado = listaSeguroFacultativo.stream().filter(sf -> sf.getValidacionEnfermeria().equals(SeguroFacultativoValidacion.BAJA.getLabel()) && sf.getSeguroFacultativoEstudiante() == 2).collect(Collectors.toList()).get(0);
//        listaSeguroFacultativo.forEach(System.out::println);
//        System.err.println("Seguro Seleccionado: " + seguroFacultativoSeleccionado);
//
////        listaSeguroFacultativo.removeIf(sf -> sf.getValidacionEnfermeria().equals(SeguroFacultativoValidacion.BAJA.getLabel()));
////        listaSeguroFacultativo.forEach(System.out::println);
//        
//        System.out.println("----------------------------------------------------------------------------------------------------------------");
//        
//        System.out.println("Fecha minima del mes: " + getFechaMinima(1, 2019));
//        System.out.println("Fecha maxima del mes: " + getFechaMaxima(1, 2019));
//        
//        System.out.println("----------------------------------------------------------------------------------------------------------------");
//        
//        List<CasoCriticoEstado> estadosAbiertos = Arrays.stream(CasoCriticoEstado.values())
//                        .filter(casoCriticoEstado -> casoCriticoEstado.getNivel() < 0D)
//                        .collect(Collectors.toList());
//        System.err.println("estadosAbiertos: " + estadosAbiertos);
//        
//        
//        System.out.println("----------------------------------------------------------------------------------------------------------------");
//        try {
//            List<String> archivosTarjetonesIMSS = new ArrayList<>();
//            if(Files.exists(Paths.get("C:\\archivos\\modulos_registro\\2018\\dpye\\calidad_academica\\diciembre\\actividades_varias\\"), LinkOption.NOFOLLOW_LINKS)){
//                System.err.println("Lectura de Codigo QR mediante tarjetón del IMSS");
////                Lista de archivos, solo PDF
//                Files.list(Paths.get("C:\\archivos\\modulos_registro\\2018\\dpye\\calidad_academica\\diciembre\\actividades_varias\\")).forEach((e) -> {
//                    if(e.toString().endsWith(".pdf")){
//                        archivosTarjetonesIMSS.add(e.getParent().toString() + File.separator + e.getFileName());
//                    }
//                });
//                
//                System.out.println("Path y Uri");
//                archivosTarjetonesIMSS.forEach((f) -> {
//                    try {
//                        Path path = Paths.get(f);
//                        URI uri = path.toUri();
//                        System.err.println("");
//                        System.err.println("Nombre: " + path.getFileName());
//                        System.err.println("Path: " + path.toString());
//                        System.err.println("Uri: " + uri.toString());
//                        System.err.println("");
//                        
//                        QrPdf pdf = new QrPdf(Paths.get(uri));
//                        String qrCode = pdf.getQRCode(1, true, true);
//                        String[] parts = qrCode.split("\\|");
//                        
//                        Arrays.stream(parts).forEach(System.out::println);
//                        
//                        String num_seg_social = parts[2].replaceAll("[^-\\d]+","").trim();
//                        System.err.println("num_seg_social: ");
//                        System.err.println(num_seg_social);
//                        
//                        String nombre_validacion = parts[3].replaceAll("Nombre: ", "").trim();
//                        System.err.println("nombre_modificado_directamente: ");
//                        System.err.println(nombre_validacion);
//                        
//                        String curp_modificado = parts[4].replaceAll("CURP: ","").trim();
//                        System.err.println("curp_modificado_directamente: ");
//                        System.err.println(curp_modificado);
//                        
//                        System.out.println("");
////                        [^-?0-9]+
//                        
////                        String str = "Thére {}{}}{´p´p´ee|° 0 are 1 some -2-34 -numbers 567 here 890 .";
////                        int[] ints = Arrays.stream(str.replaceAll("-", " -").split("[^-\\d]+"))
////                                         .filter(s -> !s.matches("-?"))
////                                         .mapToInt(Integer::parseInt).toArray();
////                        System.out.println(Arrays.toString(ints));
//                        
//                    } catch (IOException ex) {
//                        Logger.getLogger(Ejemplo.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (NotFoundException ex) {
//                        Logger.getLogger(Ejemplo.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    
//                });
//            }
//            
//            
//            System.out.println("----------------------------------------------------------------------------------------------------------------");
//            List<String> listarArchivos = new ArrayList<>();
//            if (Files.exists(Paths.get("C:\\archivos\\modulos_registro\\2018\\dpye\\calidad_academica\\diciembre\\actividades_varias\\"), LinkOption.NOFOLLOW_LINKS)) {
//                System.err.println("Archivos: ");
//                //Lista general
//                Files.list(Paths.get("C:\\archivos\\modulos_registro\\2018\\dpye\\calidad_academica\\diciembre\\actividades_varias\\")).forEach((t) -> {
//                    if (t.toString().endsWith(".xlsx")) {
//                        listarArchivos.add(t.getParent().toString() + File.separator + t.getFileName());
//                    }
//                });
//                
//                System.out.println("Path y Uri");
//                listarArchivos.forEach((t) -> {
//                    Path path = Paths.get(t);
//                    URI uri = path.toUri();
//                    System.err.println("Nombre: " + path.getFileName());
//                    System.err.println("Path: " + path.toString());
//                    System.err.println("Uri: " + uri.toString());
//                });
//
//            } else {
//                System.out.println("mx.edu.utxj.pye.siip.services.ca.Ejemplo.main() Aún no existen archivos en el directorio o bien el directorio no existe");
//            }
//
//            System.out.println("----------------------------------------------------------------------------------------------------------------");
//            List<String> listarArchivosImagenesDocumentos = new ArrayList<>();
//            if (Files.exists(Paths.get("C:\\archivos\\e.castillo\\documentos\\"), LinkOption.NOFOLLOW_LINKS)) {
//                Files.list(Paths.get("C:\\archivos\\e.castillo\\documentos\\")).forEach((t) -> {
////                    Condición para que sólo acepte archivos con la extensión especificada
//                    listarArchivosImagenesDocumentos.add(t.getParent().toString() + File.separator + t.getFileName());
//                });
//                listarArchivosImagenesDocumentos.forEach((t) -> {
//                    try {
//                        Path path = Paths.get(t);
//                        URI uri = path.toUri();
////                        System.err.println("Nombre: " + path.getFileName());
////                        System.err.println("Path: " + path.toString());
////                        System.err.println("Uri: " + uri.toString());
//                        
//                        File imagen = new File(uri);
//                        BufferedImage img = ImageIO.read(imagen);
//                        
//                        int width = img.getWidth();
//                        boolean widthF = false;
//                        
//                        int height = img.getHeight();
//                        boolean heightF = false;
//                        
//                        if (width >= 400 && width <= 450){widthF = true;}
//                        if (height >= 400 && height <= 450){heightF = true;}
//                        
////                        System.err.println("widthF: " + img.getWidth());
////                        System.err.println("heightF: " + img.getHeight());
//                        
//                        if (!widthF && !heightF) {
//                            System.err.println("El Ancho de la imagen y el alto tienen que ser de 400 x 450 px.");
//                            System.err.println("Path: " + path.toString() + " Medidas- Ancho:" + width + " Alto: " + height);
////                            FacesMessage message = new FacesMessage("Problemas", " El Ancho de la imagen y el alto tienen que ser de 400 x 450 px.");
////                            FacesContext.getCurrentInstance().addMessage(null, message);
////                            return;
//                        }else{
//                            System.err.println("El archivo es correcto");
//                            System.err.println("Path: " + path.toString() + " Medidas- Ancho:" + width + " Alto: " + height);
//                        }
//                    } catch (IOException ex) {
//                        Logger.getLogger(Ejemplo.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                });
//            } else {
//                System.out.println("mx.edu.utxj.pye.siip.services.ca.Ejemplo.main() Aún no existen archivos en el directorio o bien el directorio no existe");
//            }
//            System.out.println("----------------------------------------------------------------------------------------------------------------");
//        } catch (IOException ex) {
//            Logger.getLogger(Ejemplo.class.getName()).log(Level.SEVERE, null, ex);
//        }
////        System.out.println("--------------------------------------------------------");
////        
////        try (Stream<Path> stream = Files.list(Paths.get("C:\\archivos\\modulos_registro\\2018\\dpye\\calidad_academica\\diciembre\\actividades_varias\\"))) {
////            stream.map(String::valueOf)
////                    .filter(path -> path.endsWith(".xlsx"))
////                    .forEach(System.out::println);
////
////        } catch (IOException ex) {
////            Logger.getLogger(Ejemplo.class.getName()).log(Level.SEVERE, null, ex);
////        }
////        
//        System.out.println("----------------------------------------------------------------------------------------------------------------");
//        
//        Integer numero = 0;
//        Calendar calendario = new GregorianCalendar();
//        numero = calendario.get(Calendar.MONTH)+1;
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.Ejemplo.main() Mes: " + numero);
//        
//        Integer anio = calendario.get(Calendar.YEAR);
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.Ejemplo.main() Anio " + anio);
//        
//        String telefono = "(764) 764-7575";
//        String telefonoNuevo = quitarGuionesEspacios(telefono);
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.Ejemplo.main() " + telefonoNuevo);
//        
//        String espacios = "7 6 4 7 6 7 7 5 7 5";
//        String espacioNuevo = quitarGuionesEspacios(espacios);
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.Ejemplo.main() espacios: " + espacioNuevo);
//        
//        
//        Stream<Integer> registro1 = Stream.of(1, 2, 3, 4, 5);
//        Stream<Integer> registro2 = Stream.of(1, 2, 3, 4, 5);
//
//        List<Integer> total1 = registro1.collect(Collectors.toList());
//        List<Integer> total2 = registro2.collect(Collectors.toList());
//
////        total1.forEach(x -> System.out.println(x));
//        
//        List<Integer> claves = new ArrayList<>();
//        
//        total1.forEach((t) -> {
//            claves.add(t);
//        });
//        
//        total2.forEach((t) -> {
//            claves.add(t);
//        });
//        
//        claves.stream().forEach((t) -> {
//            System.out.println("mx.edu.utxj.pye.siip.services.ca.Ejemplo.main() Numero: " + t);
//        });
//
//        System.out.println("-------------------------------------------------------");
//        System.out.println("Ejecudado desde IntelliJ IDEA");
//        
//        
//        System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() Método de ejemplo de la clase LocalDate");
////        Fechas de la actividad x
//        Date fechaInicioActividadCorrecta = new Date(2019,02,20);
//        Date fechaInicioActividadIgual = new Date(2019, 02, 16);
//        Date fechaInicioActividadIncorrecta = new Date(2019, 01, 15);
//        
////        Fechas de evento
//        Date fechaInicioEventoBD = new Date(2019,02,16);
//        Date fechaFinEventoBD = new Date(2019,03,05);
////        
//////        Getting the default zone id
////        ZoneId defaultZoneId = ZoneId.systemDefault();
////        
//////        Converting the date to Instant
////        Instant ifiac = fechaInicioActividadCorrecta.toInstant();
////        Instant ifiai = fechaInicioActividadIgual.toInstant();
////        Instant ifiainc = fechaInicioActividadIncorrecta.toInstant();
////        
////        Instant ifechaInicioEvento = fechaInicioEventoBD.toInstant();
////        Instant ifechaFinEvento = fechaFinEventoBD.toInstant();
////        
//////        Converting the Date to LocalDate
////        LocalDate fechaInicioActividad = ifiac.atZone(defaultZoneId).toLocalDate();
////        LocalDate fechaInicioActividadI = ifiai.atZone(defaultZoneId).toLocalDate();
////        LocalDate fechaInicioActividadInc = ifiainc.atZone(defaultZoneId).toLocalDate();
////        
////        LocalDate fechaInicioEvento = ifechaInicioEvento.atZone(defaultZoneId).toLocalDate();
////        LocalDate fechaFinEvento = ifechaFinEvento.atZone(defaultZoneId).toLocalDate();
////        
////        
////        Segundo Método
//
//        LocalDate ejemplo = fechaInicioActividadCorrecta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//
//        LocalDate fechaInicioActividad = Instant.ofEpochMilli(fechaInicioActividadCorrecta.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
//        LocalDate fechaInicioActividadI = Instant.ofEpochMilli(fechaInicioActividadIgual.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
//        LocalDate fechaInicioActividadInc = Instant.ofEpochMilli(fechaInicioActividadIncorrecta.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
//        
//        LocalDate fechaInicioEvento = Instant.ofEpochMilli(fechaInicioEventoBD.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
//        LocalDate fechaFinEvento = Instant.ofEpochMilli(fechaFinEventoBD.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
//        
//        
////        LocalDate fechaFinEvento = fechaFinEventoBD.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        
////        Fechas correctas
//        System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() fechas correctas: ");
//        
//        System.out.println("--");
//        System.out.println("--");
//        System.out.println("--");
//        
//        System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main()Fecha de ejemplo: " + ejemplo.getYear());
//        System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main()Fecha Inicio Evento: " + fechaInicioEvento.getYear() + " mes: " + fechaInicioEvento.getMonth() + " día: " + fechaInicioEvento.getDayOfMonth());
//        System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main()Fecha Fin Evento: " + fechaFinEvento);
//        System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main()Fecha Actividad: " + fechaInicioActividad);
//        System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main()Fecha Actividad Igual: " + fechaInicioActividadI);
//        System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main()Fecha Actividad Inorrecta: " + fechaInicioActividadInc);
//        
//        System.out.println("--");
//        System.out.println("--");
//        System.out.println("--");
//        
//        System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main()Fecha igual a fechainicio: " + fechaInicioActividadI.isEqual(fechaInicioEvento));
//        System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main()Fecha igual a fechafin: " + fechaInicioActividadI.isEqual(fechaFinEvento));
//        System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main()Fecha antes de: " + fechaInicioActividadI.isBefore(fechaInicioEvento));
//        System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main()Fecha despues de: " + fechaInicioActividadI.isAfter(fechaFinEvento));
//        
//        System.out.println("--");
//        System.out.println("--");
//        System.out.println("--");
//        System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main()Caso 1: " + validaFechas(fechaInicioActividad, fechaInicioEvento, fechaFinEvento));
//         System.out.println("--");
//        System.out.println("--");
//        System.out.println("--");
//        System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main()Caso 2: " + validaFechas(fechaInicioActividadI, fechaInicioEvento, fechaFinEvento));
//         System.out.println("--");
//        System.out.println("--");
//        System.out.println("--");
//        System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main()Caso 3: " + validaFechas(fechaInicioActividadInc, fechaInicioEvento, fechaFinEvento));
//        
//        
//        
//        System.out.println("-------------------------------------------------------");
//        System.out.println("-------------------------------------------------------");
//        System.out.println("-------------------------------------------------------");
//        
//        System.out.println("--------------------------Validando fecha inicial-----------------------------");
//        Date fechaI1 = new Date(2019, 03, 21);
//        Date fechaF1 = new Date(2019, 03, 20);
//        if (fechaI1.before(fechaF1)) {
//            System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() true");
//        } else {
//            if (fechaF1.before(fechaI1)) {
//                System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() false");
//            } else {
//                System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() Las fechas son iguales");
//                System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() true");
//            }
//        }
//        
//        System.out.println("--------------------------Validando fecha final-----------------------------");
//        Date fechaI2 = new Date(2019, 03, 21);
//        Date fechaF2 = new Date(2019, 03, 22);
//        if (fechaF2.after(fechaI2)) {
//            System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() true");
//        } else {
//            if (fechaI2.after(fechaF2)) {
//                System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() false");
//            } else {
//                System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() Las fechas son iguales");
//                System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() true");
//            }
//        }
//        
//        System.out.println("-------------------------------------------------------");
//        System.out.println("-------------------------------------------------------");
//        System.out.println("-------------------------------------------------------");
//        
//        System.out.println("-------------------------- Ejes registros y tipos de registros -----------------------------");
//        
//        DTORepositorio dto = new DTORepositorio();
//        List<String> listaArchivosEncontrados = new ArrayList<>();
//        List<DTOArchivoRepositorio> listaRepositorio = new ArrayList<>();
//        String rutaPrincipal = ServicioCarga.carpetaRaiz + ServicioCarga.modulosRegistro + File.separator;
//        dto.getEjesRegistros().forEach((eje) -> {
//            dto.getTiposRegistros().forEach((registro) -> {
//                String rutaCompleta = rutaPrincipal + "2019" + File.separator + "vin" + File.separator + eje + File.separator + "enero" + File.separator + registro + File.separator;
//                if (Files.exists(Paths.get(rutaCompleta), LinkOption.NOFOLLOW_LINKS)) {
//                    try {
//                        Files.list(Paths.get(rutaCompleta)).forEach((t) -> {
//                            if (t.toString().endsWith(".xlsx")) {
//                                listaArchivosEncontrados.add(t.getParent().toString() + File.separator + t.getFileName());
//                            }
//                        });
//                        listaArchivosEncontrados.forEach((t) -> {
//                            Path path = Paths.get(t);
//                            URI uri = path.toUri();
//                            listaRepositorio.add(new DTOArchivoRepositorio(
//                                    (short)2019,
//                                    "vin",
//                                    eje,
//                                    convierteEjesRegistro(eje),
//                                    "enero",
//                                    IntStream.concat(IntStream.of("enero".codePointAt(0)).map(Character::toUpperCase), "enero".codePoints().skip(1) ).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString(),
//                                    registro,
//                                    convierteRegistrosTipo(registro),
//                                    path.toString(),
//                                    path
//                            ));
//                            listaRepositorio.forEach((lr) -> {
//                                System.err.println("Archivo: " + lr.toString());
//                            });
//                        });
//                    } catch (IOException ex) {
//                        Logger.getLogger(Ejemplo.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                } else {
//                    
//                }
//            });
//        });
//
//        System.out.println("-------------------------------------------------------");
//        System.out.println("-------------------------------------------------------");
//        System.out.println("-------------------------------------------------------");
//        
////        System.err.println("Mes Ingresado 0: " + obtenerNombreMesFecha(new Date(2020, 1, 1)));
//        Date fechaVacia = null;
//        System.err.println("Mes Ingresado 0: " + obtenerNombreMesFecha(fechaVacia));
//        System.err.println("Mes Ingresado 1: " + obtenerNombreMesFecha(new Date(2020, 0, 1)));
//        System.err.println("Mes Ingresado 2: " + obtenerNombreMesFecha(new Date(2020, 1, 2)));
//        System.err.println("Mes Ingresado 3: " + obtenerNombreMesFecha(new Date(2020, 2, 3)));
//        System.err.println("Mes Ingresado 4: " + obtenerNombreMesFecha(new Date(2020, 3, 4)));
//        System.err.println("Mes Ingresado 5: " + obtenerNombreMesFecha(new Date(2020, 4, 5)));
//        System.err.println("Mes Ingresado 6: " + obtenerNombreMesFecha(new Date(2020, 5, 6)));
//        System.err.println("Mes Ingresado 7: " + obtenerNombreMesFecha(new Date(2020, 6, 7)));
//        System.err.println("Mes Ingresado 8: " + obtenerNombreMesFecha(new Date(2020, 7, 8)));
//        System.err.println("Mes Ingresado 9: " + obtenerNombreMesFecha(new Date(2020, 8, 9)));
//        System.err.println("Mes Ingresado 10: " + obtenerNombreMesFecha(new Date(2020, 9, 10)));
//        System.err.println("Mes Ingresado 11: " + obtenerNombreMesFecha(new Date(2020, 10, 11)));
//        System.err.println("Mes Ingresado 12: " + obtenerNombreMesFecha(new Date(2020, 11, 12)));
//        
//        System.err.println("Ejercicio Ingresado 0: " + obtenerEjercicioFecha(new Date()));
//        
//        System.err.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------");
//        
//        System.err.println("Rol Notificaciones: " + RolNotificacion.ListaValoresLabel());
//        List<String> listaRoles = RolNotificacion.ListaValoresLabel();
//        List<String> listaAlcanceElegidaUsuario = new ArrayList<>(Arrays.asList("Jefe de Departamento","Rector","Coordinador de Tutores"));
//        System.err.println("Rol Usuarios: " + listaAlcanceElegidaUsuario);
//        
//        listaRoles.retainAll(listaAlcanceElegidaUsuario);
//        System.out.println(listaRoles);

    }
    
    private static String getFechaMinima(int mes, int anio) {
        Calendar calendar = Calendar.getInstance();
        // passing month-1 because 0-->jan, 1-->feb... 11-->dec
        calendar.set(anio, mes-1, 1);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
        Date date = calendar.getTime();
        DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
        return DATE_FORMAT.format(date);
    }
    
    private static String getFechaMaxima(int mes, int anio) {
        Calendar calendar = Calendar.getInstance();
        // passing month-1 because 0-->jan, 1-->feb... 11-->dec
        calendar.set(anio, mes-1, 1);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        Date date = calendar.getTime();
        DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
        return DATE_FORMAT.format(date);
    }
    
    public static String convierteEjesRegistro(String ejeRegistro) {
        String ejeRegistroOficial;
        switch (ejeRegistro) {
            case "estadistica_basica":
                ejeRegistroOficial = "Estadística Básica";
                break;
            case "calidad_academica":
                ejeRegistroOficial = "Asegurar la calidad académica";
                break;
            case "vinculacion":
                ejeRegistroOficial = "Consolidar la vinculación institucional";
                break;
            case "gestion_institucional":
                ejeRegistroOficial = "Fortalecer los procesos de gestión institucional";
                break;
            case "talento_humano":
                ejeRegistroOficial = "Desarrollar el talento humano";
                break;
            default:
                ejeRegistroOficial = "Eje Desconocido";
                break;
        }
        return ejeRegistroOficial;
    }
    
    public static String convierteRegistrosTipo(String tipoRegistro) {
        String tipoRegistroOficial;
        switch (tipoRegistro) {
            case "acervo_bibliografico":
                tipoRegistroOficial = "Acervo Bibliográfico";
                break;
            case "actividades_formacion_integral":
                tipoRegistroOficial = "Actividades de Formación Integral";
                break;
            case "participantes_actformint":
                tipoRegistroOficial = "Actividades de Formación Integral";
                break;
            case "actividades_varias":
                tipoRegistroOficial = "Actividades Varias";
                break;
            case "becas":
                tipoRegistroOficial = "Becas";
                break;
            case "cuerpos_academicos":
                tipoRegistroOficial = "Cuerpos Académicos";
                break;
            case "desercion_academica":
                tipoRegistroOficial = "Deserción Académica";
                break;
            case "egetsu":
                tipoRegistroOficial = "EGETSU";
                break;
            case "estadias_estudiante":
                tipoRegistroOficial = "Estadías";
                break;
            case "exani":
                tipoRegistroOficial = "EXANI";
                break;
            case "productos_academicos":
                tipoRegistroOficial = "Productos Académicos";
                break;
            case "programas_PertCalidad":
                tipoRegistroOficial = "Programas Educativos Pertinentes y de Calidad";
                break;
            case "servicios_enfermeria":
                tipoRegistroOficial = "Servicios Enfermeria";
                break;
            case "tutorias_asesorias":
                tipoRegistroOficial = "Asesorías y Tutorías";
                break;
            case "comisiones_academicas":
                tipoRegistroOficial = "Comisiones Académicas";
                break;
            case "programas_estimulos":
                tipoRegistroOficial = "Programa de Estímulos";
                break;
            case "personal_capacitado":
                tipoRegistroOficial = "Personal Capacitado";
                break;
            case "reconocimientos_prodep":
                tipoRegistroOficial = "Reconocimiento PRODEP";
                break;
            case "distribucion_equipamiento":
                tipoRegistroOficial = "Distribución de Equipamiento";
                break;
            case "distribucion_instalaciones":
                tipoRegistroOficial = "Distribución de Instalaciones";
                break;
            case "ingresosPropiosCaptados":
                tipoRegistroOficial = "Ingresos Propios";
                break;
            case "presupuestos":
                tipoRegistroOficial = "Presupuesto";
                break;
            case "bolsa_trabajo":
                tipoRegistroOficial = "Bolsa de Trabajo";
                break;
            case "difusion_iems":
                tipoRegistroOficial = "Difusión IEMS";
                break;
            case "egresados":
                tipoRegistroOficial = "Egresados/as";
                break;
            case "ferias_profesiográficas":
                tipoRegistroOficial = " Ferías Profesiográficas";
                break;
            case "iems":
                tipoRegistroOficial = "IEMS";
                break;
            case "registro_movilidad":
                tipoRegistroOficial = "Movilidad";
                break;
            case "organismos_vinculados":
                tipoRegistroOficial = "Organismos Vinculados";
                break;
            case "servicios_tecnologicos":
                tipoRegistroOficial = "Servicios Tecnológicos";
                break;
            case "visitas_industriales":
                tipoRegistroOficial = "Visitas Industriales";
                break;
            case "convenios":
                tipoRegistroOficial = "Convenios";
                break;
            case "eficiencia_terminal":
                tipoRegistroOficial = "Eficiencia Terminal";
                break;
            case "matricula_inicial":
                tipoRegistroOficial = "Matricula Inicial";
                break;
            default:
                tipoRegistroOficial = "Registro Desconocido";
                break;
        }
        return tipoRegistroOficial;
    }
    
    public static Boolean validaFechas(LocalDate fechaInicioActividad, LocalDate fechaInicioEvento, LocalDate fechaFinEvento) {
        Boolean r1 = false;
        Boolean r2 = false;

        if (fechaInicioActividad.isEqual(fechaInicioEvento)) {
            System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.validaFechas() Es igual a la fecha de inicio: " + fechaInicioActividad.isEqual(fechaInicioEvento));
            r1 = true;
            return true;
        } else {
            if (fechaInicioActividad.isEqual(fechaFinEvento)) {
                System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.validaFechas() Es igual a la fecha fin: " + fechaInicioActividad.isEqual(fechaFinEvento));
                r2 = true;
                return true;
            } else {
                return validaEntreFechas(fechaInicioActividad, fechaInicioEvento, fechaFinEvento);
            }
        }
//        if (r1 == true || r2 == true) {
//            System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.validaFechas() Resultado Final: true");
//            return true;
//        } else {
//            return validaEntreFechas(fechaInicioActividad, fechaInicioEvento, fechaFinEvento);
//        }
    }
    
    public static Boolean validaEntreFechas(LocalDate fechaInicioActividad, LocalDate fechaInicioEvento, LocalDate fechaFinEvento){
        Boolean r1 = false;
        Boolean r2 = false;
        
        if (fechaInicioActividad.isAfter(fechaInicioEvento)) {
            System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.validaEntreFechas() Es mayor que la fecha de inicio: " + fechaInicioActividad.isAfter(fechaInicioEvento));
            r1 = true;
        }
        if (fechaInicioActividad.isBefore(fechaFinEvento)) {
            System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.validaEntreFechas() Es menor que la fecha fin: " + fechaInicioActividad.isBefore(fechaFinEvento));
            r2 = true;
        }
        
        return r1 && r2;
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
    
    private static String obtenerNombreMesFecha(Date mes) {
        if (mes == null) {
            return "nulo";
        }
        DateFormat formatoMes = new SimpleDateFormat("MM");
        String horaCadena = formatoMes.format(mes);
        Integer convertido = Integer.valueOf(horaCadena);

        String mesNombre = "nulo";
        Integer[] meses = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        String[] mesesNombre = {"nulo", "enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};
        for (int i = 0; i < meses.length; i++) {
//            System.err.println("Comparativo:  " + meses[i] + " HoraCadena: " + convertido);
            if (Objects.equals(meses[i], convertido)) {
                mesNombre = mesesNombre[i];
            }
        }
        return mesNombre;
    }

    private static String obtenerEjercicioFecha(Date ejercicio) {
        if (ejercicio == null) {
            return "nulo";
        }
        DateFormat formatoEjercicio = new SimpleDateFormat("yyyy");
        String ejercicioCadena = formatoEjercicio.format(ejercicio);
        return ejercicioCadena;
    }

}
