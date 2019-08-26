/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.eb;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javax.ejb.Stateless;
import javax.servlet.annotation.MultipartConfig;
import mx.edu.utxj.pye.sgi.ejb.ch.ServicioCarga;
import mx.edu.utxj.pye.siip.dto.eb.DTOArchivoRepositorio;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbRepositorio;

/**
 *
 * @author UTXJ
 */
@Stateless
@MultipartConfig
public class ServicioRepositorio implements EjbRepositorio{

    @Override
    public List<DTOArchivoRepositorio> getListaArchivosPorAreaEjeRegistroEjercicioMes(Short ejercicio, String siglas, List<String> ejes, String mes, List<String> tiposRegistros) {
        List<DTOArchivoRepositorio> listaRepositorio = new ArrayList<>();
        String rutaPrincipal = ServicioCarga.carpetaRaiz + ServicioCarga.modulosRegistro + File.separator;
        ejes.forEach((eje) -> {
            tiposRegistros.forEach((registro) -> {
                String rutaCompleta = rutaPrincipal + ejercicio + File.separator + siglas + File.separator + eje + File.separator + mes + File.separator + registro + File.separator;
                if (Files.exists(Paths.get(rutaCompleta), LinkOption.NOFOLLOW_LINKS)) {
                    try {
                        Files.list(Paths.get(rutaCompleta)).forEach((archivo) -> {
                            if (archivo.toString().endsWith(".xlsx")) {
                                Path path = Paths.get(archivo.getParent().toString() + File.separator + archivo.getFileName());
                                listaRepositorio.add(new DTOArchivoRepositorio(
                                        ejercicio,
                                        siglas,
                                        eje,
                                        convierteEjesRegistro(eje),
                                        mes,
                                        IntStream.concat(IntStream.of(mes.codePointAt(0)).map(Character::toUpperCase), mes.codePoints().skip(1)).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString(),
                                        registro,
                                        convierteRegistrosTipo(registro),
                                        path.toString(),
                                        path
                                ));
                            }
                        });

                        listaRepositorio.forEach((t) -> {
//                            System.err.println("Archivo: " + t.toString());
                        });
                    } catch (IOException ex) {
                        System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioRepositorio.getListaArchivosPorAreaEjeRegistroEjercicioMes() -- Ha ocurrido un error durante la lectura de archivos o bien la ruta es incorrecta");
                        Logger.getLogger(ServicioRepositorio.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception e){
                        System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioRepositorio.getListaArchivosPorAreaEjeRegistroEjercicioMes() -- Ha ocurrido un error durante la lectura de archivos");
                        Logger.getLogger(ServicioRepositorio.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });
        });
        if(!listaRepositorio.isEmpty()){
            return listaRepositorio;
        }else{
            return Collections.EMPTY_LIST;
        }
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
                tipoRegistroOficial = "Ferías Profesiográficas";
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

}
