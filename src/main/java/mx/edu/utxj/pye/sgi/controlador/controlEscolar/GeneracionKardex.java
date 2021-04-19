package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaCalificacion;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionCriterioEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoEstadiaEstudiante;

@ViewScoped
@Named
public class GeneracionKardex implements Serializable{
    
    @Getter @Setter String encabezado;
    @Getter @Setter PdfTemplate total;
    @Getter @Setter SimpleDateFormat sdf = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("ES", "MX"));
    @Getter @Setter Date fechaFinVeda = new Date(2021, 06, 06);
    @Getter @Setter StreamedContent contenioArchivo;
    @Getter @Setter DefaultStreamedContent download;
    @EJB EjbConsultaCalificacion ejb;
    @Inject ConsultaCalificacionesEstudiante controlador;
    
    public void controlPDF(DtoEstudiante estudiante) throws IOException, DocumentException {
        if(estudiante.getInscripcionActiva().getGrupo().getGrado() == 1){
            generarPdfPrimer(estudiante);
        }
        if(estudiante.getInscripcionActiva().getGrupo().getGrado() == 2){
            generarPdfSegundo(estudiante);
        }
        if(estudiante.getInscripcionActiva().getGrupo().getGrado() == 3){
            generarPdfTercero(estudiante);
        }
        if(estudiante.getInscripcionActiva().getGrupo().getGrado() == 4){
            generarPdfCuarto(estudiante);
        }
        if(estudiante.getInscripcionActiva().getGrupo().getGrado() == 5){
            generarPdfQuinto(estudiante);
        }
        if(estudiante.getInscripcionActiva().getGrupo().getGrado() == 6){
            generarPdfSexto(estudiante);
        }
        if(estudiante.getInscripcionActiva().getGrupo().getGrado() == 7){
            
        }
        if(estudiante.getInscripcionActiva().getGrupo().getGrado() == 8){
            
        }
        if(estudiante.getInscripcionActiva().getGrupo().getGrado() == 9){
            
        }
        if(estudiante.getInscripcionActiva().getGrupo().getGrado() == 10){
            
        }
        if(estudiante.getInscripcionActiva().getGrupo().getGrado() == 11){
            
        }
    }
    
    public void generarPdfPrimer(DtoEstudiante estudiante)throws IOException, DocumentException{
        Document document;

        document = new Document(PageSize.LETTER, 70, 70, 15, 15);
        Paragraph nombreUniversidad, departamento, textoPrincipal, texto1, texto2, texto3, texto4, texto5, texto6;

        String nombrePdf = "Kardex Calificaciones.pdf";
        //String ruta = ServicioArchivos.carpetaRaiz.concat(File.separator).concat("acta_final").concat(File.separator).concat(nombrePdf);
        Image logoSep = Image.getInstance("C:\\archivos\\acta_final\\logoSep.png");     Image logoUT = Image.getInstance("C:\\archivos\\acta_final\\logoUT.jpg");
        Font fontBold = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);              Font fontNormal = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
        Font fontMateria2 = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL);        Font fontMateria1 = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        Font fontBolder = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);           String base = ServicioArchivos.carpetaRaiz.concat("acta_final").concat(File.separator);

        ServicioArchivos.addCarpetaRelativa(base);  ServicioArchivos.eliminarArchivo(base.concat(nombrePdf)); OutputStream out = new FileOutputStream(new File(base.concat(nombrePdf)));

        PdfWriter.getInstance(document, out);    PdfWriter.getInstance(document, new FileOutputStream("C:\\archivos\\acta_final\\Kardex Calificaciones.pdf"));
        //PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\"+usuario+"\\Downloads\\acta_final_"+grupo.getLiteral()+"_"+grupo.getTutor()+".pdf"));
        PdfPTable tablaPrincipal = new PdfPTable(2);    PdfPTable table = new PdfPTable(2);           PdfPTable table3 = new PdfPTable(2);
        PdfPTable tablaPromedio2 = new PdfPTable(2);
        

        //Se abre el pdf para iniciar a darle formato
        document.open();
        //Se coloca los elementos necesarios que contendra el pdf
        logoSep.scaleToFit(80,280);      logoSep.setAbsolutePosition(80f, 696f);    logoSep.setAlignment(Element.ALIGN_LEFT);
        nombreUniversidad = new Paragraph("UNIVERSIDAD TECNÓLOGICA DE XICOTEPEC DE JUÁREZ", fontBold);  nombreUniversidad.setAlignment(Element.ALIGN_RIGHT);  nombreUniversidad.setLeading(57,0);
        departamento = new Paragraph("DEPARTAMENTO DE SERVICIOS ESCOLARES", fontBold);                  departamento.setLeading(16,0);        departamento.setAlignment(Element.ALIGN_RIGHT);

        logoUT.scaleToFit(65,100);        logoUT.setAbsolutePosition(180f, 692f);         logoUT.setAlignment(Element.ALIGN_RIGHT);

        textoPrincipal = new Paragraph("HISTORIAL ACADÉMICO", fontMateria1);   textoPrincipal.setAlignment(Element.ALIGN_CENTER);
        textoPrincipal.setLeading(30, 0);                                      textoPrincipal.setSpacingAfter(13);

        String carrera1 = ejb.obtenerProgramaEducativo(estudiante.getInscripcionActiva().getInscripcion().getCarrera()).getValor().getNombre();
        String carrera2 = ejb.obtenerProgramaEducativo(estudiante.getInscripcionActiva().getInscripcion().getCarrera()).getValor().getNombre();
        texto1 = new Paragraph(9);   texto1.setSpacingAfter(10);   texto2 = new Paragraph(9);


        texto1.setAlignment(Element.ALIGN_JUSTIFIED_ALL);   texto1.add(new Paragraph("La ", fontNormal));     texto1.add(new Paragraph("Universidad Tecnológica de Xicotepec de Juárez ", fontBolder));
        texto1.add(new Paragraph("hace constar que, según documentos existentes en el archivo escolar el (la) C. ", fontNormal));
        texto1.add(new Paragraph(
                estudiante.getAspirante().getIdPersona().getApellidoPaterno().concat(" ")
                        .concat(estudiante.getAspirante().getIdPersona().getApellidoMaterno()).concat(" ")
                        .concat(estudiante.getAspirante().getIdPersona().getNombre()), fontBolder));
        texto1.add(new Paragraph("con matricula ", fontNormal));     texto1.add(new Paragraph(String.valueOf(estudiante.getInscripcionActiva().getInscripcion().getMatricula()), fontBolder));
        texto1.add(new Paragraph(", cursó las siguientes asignaturas correspondientes a la carrera de: ", fontNormal));   texto1.add(new Paragraph("Técnico Superior Universtiario en ".concat(carrera1), fontBolder));
        texto1.add(new Paragraph(" y actualmente se encuentra cursando el ", fontNormal));     texto1.add(new Paragraph(String.valueOf(estudiante.getInscripciones().get(0).getGrupo().getGrado()).concat(" "), fontBolder));
        texto1.add(new Paragraph("cuatrimestre.", fontNormal));        Phrase parrafo = new Phrase(texto1);
        texto2.add(new Paragraph("A petición del interesado y para los fines legales, que a él (ella) convengan, se expide la presente en Xicotepec de Juárez, Puebla, el "
                    .concat(sdf.format(new Date())).concat("."), fontNormal));
        texto2.setSpacingAfter(20);
        
        table.setWidthPercentage(100);           table.setHorizontalAlignment(Element.ALIGN_LEFT);           table.setWidths(new float[] {40f, 7f});
        table3.setWidthPercentage(100);          table3.setHorizontalAlignment(Element.ALIGN_LEFT);          table3.setWidths(new float[] {40f, 7f});
        tablaPromedio2.setWidthPercentage(100);  tablaPromedio2.setHorizontalAlignment(Element.ALIGN_LEFT);  tablaPromedio2.setWidths(new float[] {40f, 7f});
        
        Collections.sort(estudiante.getInscripciones(), (x,y) -> Integer.compare(x.getGrupo().getGrado(),y.getGrupo().getGrado()));
        estudiante.getInscripciones().forEach(estudiante1 -> {
            PdfPCell celda = new PdfPCell();                         PdfPCell celda2 = new PdfPCell();     PdfPCell cuatrimestre,texto,avg;
            BigDecimal promedioCuatrimestral1 = BigDecimal.ZERO;     Integer materiastotal =0;
            
            if(estudiante1.getGrupo().getGrado() == 1){
                BigDecimal promedioA = controlador.getPromedioCuatrimestral(estudiante1.getInscripcion());
                cuatrimestre = new PdfPCell(new Paragraph("PRIMER", fontBold));      cuatrimestre.setColspan(3);       cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table3.addCell(cuatrimestre);
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 1).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    PdfPCell materias2 = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias2.setHorizontalAlignment(Element.ALIGN_LEFT);   PdfPCell promedios2;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios2 = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios2 = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table3.addCell(materias2);
                    table3.addCell(promedios2);
                });
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));   avg = new PdfPCell(new Paragraph(promedioA.setScale(2, RoundingMode.HALF_UP).toString(), fontBold));
                tablaPromedio2.addCell(texto);     tablaPromedio2.addCell(avg);
                celda.addElement(table3);          celda2.addElement(tablaPromedio2);
            }
            tablaPrincipal.addCell(celda);
            tablaPrincipal.addCell(celda2);
        });
        texto3 = new Paragraph("ATENTAMENTE", fontBold);
        texto3.setAlignment(Element.ALIGN_CENTER);
        texto4 = new Paragraph("'TECNOLOGIA Y CALIDAD, BASE DEL DESARROLLO'", fontBold);
        texto4.setAlignment(Element.ALIGN_CENTER);
        texto4.setSpacingAfter(15);
        texto5 = new Paragraph("Ing. Juan Alberto Sánchez González", fontBolder);
        texto5.setSpacingBefore(70);
        texto5.setAlignment(Element.ALIGN_CENTER);
        texto6 = new Paragraph("Jefe del Departamento de Servicios Escolares", fontBolder);
        texto6.setAlignment(Element.ALIGN_CENTER);

        //Se agregan los elementos creados
        document.add(nombreUniversidad);
        document.add(departamento);
        if(new Date().after(fechaFinVeda)){
            document.add(logoSep);
            document.add(logoUT);
            //System.out.println("La fecha de hoy es despues de la veda electoral");
        }
        document.add(textoPrincipal);
        document.add(parrafo);
        document.add(texto2);
        document.add(tablaPrincipal);
        document.add(texto3);
        document.add(texto4);
        document.add(texto5);
        document.add(texto6);
        //Se cierra el documento
        document.close();
        FacesContext.getCurrentInstance()
                .addMessage(null, new
                        FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Su documento se ha generado correctamente",
                        ""));
        File file = new File(base.concat(nombrePdf));

        try {
            InputStream input = new FileInputStream(file);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            setDownload(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
//            String ruta = "/sii2/media".concat(file.toURI().toString().split("archivos")[1]);
            //Ajax.oncomplete("descargar('" + ruta + "');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void generarPdfSegundo(DtoEstudiante estudiante)throws IOException, DocumentException{
        Document document;

        document = new Document(PageSize.LETTER, 70, 70, 15, 15);
        Paragraph nombreUniversidad, departamento, textoPrincipal, texto1, texto2, texto3, texto4, texto5, texto6;

        String nombrePdf = "Kardex Calificaciones.pdf";
        //String ruta = ServicioArchivos.carpetaRaiz.concat(File.separator).concat("acta_final").concat(File.separator).concat(nombrePdf);
        Image logoSep = Image.getInstance("C:\\archivos\\acta_final\\logoSep.png");     Image logoUT = Image.getInstance("C:\\archivos\\acta_final\\logoUT.jpg");
        Font fontBold = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);              Font fontNormal = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
        Font fontMateria2 = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL);        Font fontMateria1 = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        Font fontBolder = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);           String base = ServicioArchivos.carpetaRaiz.concat("acta_final").concat(File.separator);

        ServicioArchivos.addCarpetaRelativa(base);  ServicioArchivos.eliminarArchivo(base.concat(nombrePdf)); OutputStream out = new FileOutputStream(new File(base.concat(nombrePdf)));

        PdfWriter.getInstance(document, out);    PdfWriter.getInstance(document, new FileOutputStream("C:\\archivos\\acta_final\\Kardex Calificaciones.pdf"));
        //PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\"+usuario+"\\Downloads\\acta_final_"+grupo.getLiteral()+"_"+grupo.getTutor()+".pdf"));
        PdfPTable tablaPrincipal = new PdfPTable(2);    PdfPTable table = new PdfPTable(2);           PdfPTable table3 = new PdfPTable(2);
        PdfPTable tablaPromedio2 = new PdfPTable(2);    PdfPTable tablaPromedio = new PdfPTable(2);
        

        //Se abre el pdf para iniciar a darle formato
        document.open();
        //Se coloca los elementos necesarios que contendra el pdf
        logoSep.scaleToFit(80,280);      logoSep.setAbsolutePosition(80f, 696f);    logoSep.setAlignment(Element.ALIGN_LEFT);
        nombreUniversidad = new Paragraph("UNIVERSIDAD TECNÓLOGICA DE XICOTEPEC DE JUÁREZ", fontBold);  nombreUniversidad.setAlignment(Element.ALIGN_RIGHT);  nombreUniversidad.setLeading(57,0);
        departamento = new Paragraph("DEPARTAMENTO DE SERVICIOS ESCOLARES", fontBold);                  departamento.setLeading(16,0);        departamento.setAlignment(Element.ALIGN_RIGHT);

        logoUT.scaleToFit(65,100);        logoUT.setAbsolutePosition(180f, 692f);         logoUT.setAlignment(Element.ALIGN_RIGHT);

        textoPrincipal = new Paragraph("HISTORIAL ACADÉMICO", fontMateria1);   textoPrincipal.setAlignment(Element.ALIGN_CENTER);
        textoPrincipal.setLeading(30, 0);                                      textoPrincipal.setSpacingAfter(13);

        String carrera1 = ejb.obtenerProgramaEducativo(estudiante.getInscripcionActiva().getInscripcion().getCarrera()).getValor().getNombre();
        String carrera2 = ejb.obtenerProgramaEducativo(estudiante.getInscripcionActiva().getInscripcion().getCarrera()).getValor().getNombre();
        texto1 = new Paragraph(9);   texto1.setSpacingAfter(10);   texto2 = new Paragraph(9);


        texto1.setAlignment(Element.ALIGN_JUSTIFIED_ALL);   texto1.add(new Paragraph("La ", fontNormal));     texto1.add(new Paragraph("Universidad Tecnológica de Xicotepec de Juárez ", fontBolder));
        texto1.add(new Paragraph("hace constar que, según documentos existentes en el archivo escolar el (la) C. ", fontNormal));
        texto1.add(new Paragraph(
                estudiante.getAspirante().getIdPersona().getApellidoPaterno().concat(" ")
                        .concat(estudiante.getAspirante().getIdPersona().getApellidoMaterno()).concat(" ")
                        .concat(estudiante.getAspirante().getIdPersona().getNombre()), fontBolder));
        texto1.add(new Paragraph("con matricula ", fontNormal));     texto1.add(new Paragraph(String.valueOf(estudiante.getInscripcionActiva().getInscripcion().getMatricula()), fontBolder));
        texto1.add(new Paragraph(", cursó las siguientes asignaturas correspondientes a la carrera de: ", fontNormal));   texto1.add(new Paragraph("Técnico Superior Universtiario en ".concat(carrera1), fontBolder));
        texto1.add(new Paragraph(" y actualmente se encuentra cursando el ", fontNormal));     texto1.add(new Paragraph(String.valueOf(estudiante.getInscripciones().get(0).getGrupo().getGrado()).concat(" "), fontBolder));
        texto1.add(new Paragraph("cuatrimestre.", fontNormal));        Phrase parrafo = new Phrase(texto1);
        texto2.add(new Paragraph("A petición del interesado y para los fines legales, que a él (ella) convengan, se expide la presente en Xicotepec de Juárez, Puebla, el "
                    .concat(sdf.format(new Date())).concat("."), fontNormal));
        texto2.setSpacingAfter(20);
        
        table.setWidthPercentage(100);           table.setHorizontalAlignment(Element.ALIGN_LEFT);           table.setWidths(new float[] {40f, 7f});
        table3.setWidthPercentage(100);          table3.setHorizontalAlignment(Element.ALIGN_LEFT);          table3.setWidths(new float[] {40f, 7f});
        tablaPromedio2.setWidthPercentage(100);  tablaPromedio2.setHorizontalAlignment(Element.ALIGN_LEFT);  tablaPromedio2.setWidths(new float[] {40f, 7f});
        tablaPromedio.setWidthPercentage(100);  tablaPromedio.setHorizontalAlignment(Element.ALIGN_LEFT);  tablaPromedio.setWidths(new float[] {40f, 7f});
        
        Collections.sort(estudiante.getInscripciones(), (x,y) -> Integer.compare(x.getGrupo().getGrado(),y.getGrupo().getGrado()));
        estudiante.getInscripciones().forEach(estudiante1 -> {
            PdfPCell celda = new PdfPCell();                         PdfPCell celda2 = new PdfPCell();     PdfPCell cuatrimestre,texto,avg;
            BigDecimal promedioCuatrimestral1 = BigDecimal.ZERO;     Integer materiastotal =0;
            
            if(estudiante1.getGrupo().getGrado() == 1){
                Integer contador; 
                BigDecimal p = estudiante.getInscripciones()
                        .stream()
                        .filter(e -> e.getInscripcion().getGrupo().getGrado() == 2)
                        .map(dtoInscripcion -> controlador.getPromedioCuatrimestral(dtoInscripcion.getInscripcion()))
                        .map(BigDecimal::plus)
                        .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
                if(p.compareTo(new BigDecimal(8)) == -1){
                    contador = 1;
                    //System.out.println("Es "+ contador);
                }else{
                    contador = 2;
                    //System.out.println("Es "+ contador);
                }
                BigDecimal promedioA = estudiante.getInscripciones().stream()
                        .filter(x -> x.getGrupo().getGrado() <= 2)
                        .map(dtoInscripcion -> controlador.getPromedioCuatrimestral(dtoInscripcion.getInscripcion()))
                        .collect(Collectors.toList())
                        .stream()
                        .filter(pr -> pr.compareTo(new BigDecimal(7.9999)) == 1)
                        .map(BigDecimal::plus).reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(new BigDecimal(contador), 8, RoundingMode.HALF_UP);
                cuatrimestre = new PdfPCell(new Paragraph("PRIMER", fontBold));      cuatrimestre.setColspan(3);       cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table3.addCell(cuatrimestre);
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 1).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    PdfPCell materias2 = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias2.setHorizontalAlignment(Element.ALIGN_LEFT);   PdfPCell promedios2;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios2 = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios2 = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table3.addCell(materias2);
                    table3.addCell(promedios2);
                });
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));   avg = new PdfPCell(new Paragraph(promedioA.setScale(2, RoundingMode.HALF_UP).toString(), fontBold));
                tablaPromedio.addCell(texto);     tablaPromedio.addCell(avg);
                celda.addElement(table3);          celda2.addElement(tablaPromedio);
            }
            if(estudiante1.getGrupo().getGrado() == 2){
                Integer cargas = (int)estudiante1.getGrupo().getCargaAcademicaList().stream().mapToInt(CargaAcademica::getCarga).count();
                Integer calificaciones = (int)estudiante1.getInscripcion().getCalificacionPromedioList().stream().mapToInt((cp) -> cp.getCargaAcademica().getCarga()).count();
                BigDecimal promedioC = controlador.getPromedioCuatrimestral(estudiante1.getInscripcion());
                if(!cargas.equals(calificaciones) || promedioC.compareTo(new BigDecimal(8)) == -1) return;
                cuatrimestre = new PdfPCell(new Paragraph("SEGUNDO", fontBold));          cuatrimestre.setColspan(3);
                cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);            table.addCell(cuatrimestre);
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 2).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    if(promedio.compareTo(BigDecimal.ZERO) == 0) return;
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal promedioFinal = BigDecimal.ZERO;
                    PdfPCell materias = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias.setHorizontalAlignment(Element.ALIGN_LEFT);
                    PdfPCell promedios;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table.addCell(materias);
                    table.addCell(promedios);
                });
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));
                avg = new PdfPCell(new Paragraph("", fontBold));
                tablaPromedio.addCell(texto);
                tablaPromedio.addCell(avg);
                celda.addElement(table);
                celda2.addElement(new Paragraph(""));
            }
            tablaPrincipal.addCell(celda);
            tablaPrincipal.addCell(celda2);
        });
        texto3 = new Paragraph("ATENTAMENTE", fontBold);
        texto3.setAlignment(Element.ALIGN_CENTER);
        texto4 = new Paragraph("'TECNOLOGIA Y CALIDAD, BASE DEL DESARROLLO'", fontBold);
        texto4.setAlignment(Element.ALIGN_CENTER);
        texto4.setSpacingAfter(15);
        texto5 = new Paragraph("Ing. Juan Alberto Sánchez González", fontBolder);
        texto5.setSpacingBefore(70);
        texto5.setAlignment(Element.ALIGN_CENTER);
        texto6 = new Paragraph("Jefe del Departamento de Servicios Escolares", fontBolder);
        texto6.setAlignment(Element.ALIGN_CENTER);

        //Se agregan los elementos creados
        document.add(nombreUniversidad);
        document.add(departamento);
        if(new Date().after(fechaFinVeda)){
            document.add(logoSep);
            document.add(logoUT);
            //System.out.println("La fecha de hoy es despues de la veda electoral");
        }
        document.add(textoPrincipal);
        document.add(parrafo);
        document.add(texto2);
        document.add(tablaPrincipal);
        document.add(texto3);
        document.add(texto4);
        document.add(texto5);
        document.add(texto6);
        //Se cierra el documento
        document.close();
        FacesContext.getCurrentInstance()
                .addMessage(null, new
                        FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Su documento se ha generado correctamente",
                        ""));
        File file = new File(base.concat(nombrePdf));

        try {
            InputStream input = new FileInputStream(file);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            setDownload(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
//            String ruta = "/sii2/media".concat(file.toURI().toString().split("archivos")[1]);
            //Ajax.oncomplete("descargar('" + ruta + "');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void generarPdfTercero(DtoEstudiante estudiante)throws IOException, DocumentException{
        Document document;

        document = new Document(PageSize.LETTER, 70, 70, 15, 15);
        Paragraph nombreUniversidad, departamento, textoPrincipal, texto1, texto2, texto3, texto4, texto5, texto6;

        String nombrePdf = "Kardex Calificaciones.pdf";
        //String ruta = ServicioArchivos.carpetaRaiz.concat(File.separator).concat("acta_final").concat(File.separator).concat(nombrePdf);
        Image logoSep = Image.getInstance("C:\\archivos\\acta_final\\logoSep.png");     Image logoUT = Image.getInstance("C:\\archivos\\acta_final\\logoUT.jpg");
        Font fontBold = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);              Font fontNormal = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
        Font fontMateria2 = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL);        Font fontMateria1 = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        Font fontBolder = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);           String base = ServicioArchivos.carpetaRaiz.concat("acta_final").concat(File.separator);

        ServicioArchivos.addCarpetaRelativa(base);  ServicioArchivos.eliminarArchivo(base.concat(nombrePdf)); OutputStream out = new FileOutputStream(new File(base.concat(nombrePdf)));

        PdfWriter.getInstance(document, out);    PdfWriter.getInstance(document, new FileOutputStream("C:\\archivos\\acta_final\\Kardex Calificaciones.pdf"));
        //PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\"+usuario+"\\Downloads\\acta_final_"+grupo.getLiteral()+"_"+grupo.getTutor()+".pdf"));
        PdfPTable tablaPrincipal = new PdfPTable(2);    PdfPTable table = new PdfPTable(2);           PdfPTable table3 = new PdfPTable(2);
        PdfPTable tablaPromedio2 = new PdfPTable(2);    PdfPTable tablaPromedio = new PdfPTable(2);   PdfPTable table4 = new PdfPTable(2);
        PdfPTable tablaPromedio3 = new PdfPTable(2);
        

        //Se abre el pdf para iniciar a darle formato
        document.open();
        //Se coloca los elementos necesarios que contendra el pdf
        logoSep.scaleToFit(80,280);      logoSep.setAbsolutePosition(80f, 696f);    logoSep.setAlignment(Element.ALIGN_LEFT);
        nombreUniversidad = new Paragraph("UNIVERSIDAD TECNÓLOGICA DE XICOTEPEC DE JUÁREZ", fontBold);  nombreUniversidad.setAlignment(Element.ALIGN_RIGHT);  nombreUniversidad.setLeading(57,0);
        departamento = new Paragraph("DEPARTAMENTO DE SERVICIOS ESCOLARES", fontBold);                  departamento.setLeading(16,0);        departamento.setAlignment(Element.ALIGN_RIGHT);

        logoUT.scaleToFit(65,100);        logoUT.setAbsolutePosition(180f, 692f);         logoUT.setAlignment(Element.ALIGN_RIGHT);

        textoPrincipal = new Paragraph("HISTORIAL ACADÉMICO", fontMateria1);   textoPrincipal.setAlignment(Element.ALIGN_CENTER);
        textoPrincipal.setLeading(30, 0);                                      textoPrincipal.setSpacingAfter(13);

        String carrera1 = ejb.obtenerProgramaEducativo(estudiante.getInscripcionActiva().getInscripcion().getCarrera()).getValor().getNombre();
        String carrera2 = ejb.obtenerProgramaEducativo(estudiante.getInscripcionActiva().getInscripcion().getCarrera()).getValor().getNombre();
        texto1 = new Paragraph(9);   texto1.setSpacingAfter(10);   texto2 = new Paragraph(9);


        texto1.setAlignment(Element.ALIGN_JUSTIFIED_ALL);   texto1.add(new Paragraph("La ", fontNormal));     texto1.add(new Paragraph("Universidad Tecnológica de Xicotepec de Juárez ", fontBolder));
        texto1.add(new Paragraph("hace constar que, según documentos existentes en el archivo escolar el (la) C. ", fontNormal));
        texto1.add(new Paragraph(
                estudiante.getAspirante().getIdPersona().getApellidoPaterno().concat(" ")
                        .concat(estudiante.getAspirante().getIdPersona().getApellidoMaterno()).concat(" ")
                        .concat(estudiante.getAspirante().getIdPersona().getNombre()), fontBolder));
        texto1.add(new Paragraph("con matricula ", fontNormal));     texto1.add(new Paragraph(String.valueOf(estudiante.getInscripcionActiva().getInscripcion().getMatricula()), fontBolder));
        texto1.add(new Paragraph(", cursó las siguientes asignaturas correspondientes a la carrera de: ", fontNormal));   texto1.add(new Paragraph("Técnico Superior Universtiario en ".concat(carrera1), fontBolder));
        texto1.add(new Paragraph(" y actualmente se encuentra cursando el ", fontNormal));     texto1.add(new Paragraph(String.valueOf(estudiante.getInscripciones().get(0).getGrupo().getGrado()).concat(" "), fontBolder));
        texto1.add(new Paragraph("cuatrimestre.", fontNormal));        Phrase parrafo = new Phrase(texto1);
        texto2.add(new Paragraph("A petición del interesado y para los fines legales, que a él (ella) convengan, se expide la presente en Xicotepec de Juárez, Puebla, el "
                    .concat(sdf.format(new Date())).concat("."), fontNormal));
        texto2.setSpacingAfter(20);
        
        table.setWidthPercentage(100);           table.setHorizontalAlignment(Element.ALIGN_LEFT);           table.setWidths(new float[] {40f, 7f});
        table3.setWidthPercentage(100);          table3.setHorizontalAlignment(Element.ALIGN_LEFT);          table3.setWidths(new float[] {40f, 7f});
        table4.setWidthPercentage(100);          table4.setHorizontalAlignment(Element.ALIGN_LEFT);          table4.setWidths(new float[] {40f, 7f});
        tablaPromedio2.setWidthPercentage(100);  tablaPromedio2.setHorizontalAlignment(Element.ALIGN_LEFT);  tablaPromedio2.setWidths(new float[] {40f, 7f});
        tablaPromedio.setWidthPercentage(100);  tablaPromedio.setHorizontalAlignment(Element.ALIGN_LEFT);  tablaPromedio.setWidths(new float[] {40f, 7f});
        tablaPromedio3.setWidthPercentage(100);  tablaPromedio3.setHorizontalAlignment(Element.ALIGN_LEFT);  tablaPromedio3.setWidths(new float[] {40f, 7f});
        
        Collections.sort(estudiante.getInscripciones(), (x,y) -> Integer.compare(x.getGrupo().getGrado(),y.getGrupo().getGrado()));
        estudiante.getInscripciones().forEach(estudiante1 -> {
            PdfPCell celda = new PdfPCell();                         PdfPCell celda2 = new PdfPCell();     PdfPCell cuatrimestre,texto,avg;
            BigDecimal promedioCuatrimestral1 = BigDecimal.ZERO;     Integer materiastotal;
            
            if(estudiante1.getGrupo().getGrado() == 1){
                Integer contador;  List<BigDecimal> promedios3 = new ArrayList<>();
                BigDecimal p = estudiante.getInscripciones()
                        .stream()
                        .filter(e -> e.getInscripcion().getGrupo().getGrado() == 3)
                        .map(dtoInscripcion -> controlador.getPromedioCuatrimestral(dtoInscripcion.getInscripcion()))
                        .map(BigDecimal::plus)
                        .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
                if(p.compareTo(new BigDecimal(8)) == -1){
                    contador = 2;
                    //System.out.println("Es "+ contador);
                }else{
                    contador = 3;
                    //System.out.println("Es "+ contador);
                }
                BigDecimal promedioA = estudiante.getInscripciones().stream()
                        .filter(x -> x.getGrupo().getGrado() <= 3)
                        .map(dtoInscripcion -> controlador.getPromedioCuatrimestral(dtoInscripcion.getInscripcion()))
                        .collect(Collectors.toList())
                        .stream()
                        .filter(pr -> pr.compareTo(new BigDecimal(7.9999)) == 1)
                        .map(BigDecimal::plus).reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(new BigDecimal(contador), 8, RoundingMode.HALF_UP);
                cuatrimestre = new PdfPCell(new Paragraph("PRIMER", fontBold));      cuatrimestre.setColspan(3);       cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table3.addCell(cuatrimestre);
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 1).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    PdfPCell materias2 = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias2.setHorizontalAlignment(Element.ALIGN_LEFT);   PdfPCell promedios2;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios2 = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios2 = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table3.addCell(materias2);
                    table3.addCell(promedios2);
                });
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));   avg = new PdfPCell(new Paragraph(promedioA.setScale(2, RoundingMode.HALF_UP).toString(), fontBold));
                tablaPromedio.addCell(texto);     tablaPromedio.addCell(avg);
                celda.addElement(table3);          celda2.addElement(tablaPromedio);
            }
            if(estudiante1.getGrupo().getGrado() == 2){
                Integer cargas = estudiante1.getGrupo().getCargaAcademicaList().size();
                Integer calificaciones = estudiante1.getInscripcion().getCalificacionPromedioList().size();
                if(!cargas.equals(calificaciones)) return;
                cuatrimestre = new PdfPCell(new Paragraph("SEGUNDO", fontBold));          cuatrimestre.setColspan(3);
                cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);            table.addCell(cuatrimestre);
                
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 2).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    if(promedio.compareTo(BigDecimal.ZERO) == 0) return;
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal promedioFinal = BigDecimal.ZERO;
                    PdfPCell materias = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias.setHorizontalAlignment(Element.ALIGN_LEFT);
                    PdfPCell promedios;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table.addCell(materias);
                    table.addCell(promedios);
                });
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));
                avg = new PdfPCell(new Paragraph("", fontBold));
                tablaPromedio.addCell(texto);
                tablaPromedio.addCell(avg);
                celda.addElement(table);
                celda2.addElement(new Paragraph(""));
            }
            if(estudiante1.getGrupo().getGrado() == 3){
                Integer cargas = (int)estudiante1.getGrupo().getCargaAcademicaList().stream().mapToInt(CargaAcademica::getCarga).count();
                Integer calificaciones = (int)estudiante1.getInscripcion().getCalificacionPromedioList().stream().mapToInt((cp) -> cp.getCargaAcademica().getCarga()).count();
                BigDecimal promedioC = controlador.getPromedioCuatrimestral(estudiante1.getInscripcion());
                if(!cargas.equals(calificaciones) || promedioC.compareTo(new BigDecimal(8)) == -1) return;
                cuatrimestre = new PdfPCell(new Paragraph("TERCERO", fontBold));
                cuatrimestre.setColspan(3);
                cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table4.addCell(cuatrimestre);
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() <= 3).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal promedioFinal = BigDecimal.ZERO;
                    PdfPCell materias = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias.setHorizontalAlignment(Element.ALIGN_LEFT);
                    PdfPCell promedios;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table4.addCell(materias);
                    table4.addCell(promedios);
                });
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));
                avg = new PdfPCell(new Paragraph("", fontBold));
                tablaPromedio3.addCell(texto);
                tablaPromedio3.addCell(avg);
                celda.addElement(table4);
                celda2.addElement(new Paragraph(""));
            }
            tablaPrincipal.addCell(celda);
            tablaPrincipal.addCell(celda2);
        });
        texto3 = new Paragraph("ATENTAMENTE", fontBold);
        texto3.setAlignment(Element.ALIGN_CENTER);
        texto4 = new Paragraph("'TECNOLOGIA Y CALIDAD, BASE DEL DESARROLLO'", fontBold);
        texto4.setAlignment(Element.ALIGN_CENTER);
        texto4.setSpacingAfter(15);
        texto5 = new Paragraph("Ing. Juan Alberto Sánchez González", fontBolder);
        texto5.setSpacingBefore(70);
        texto5.setAlignment(Element.ALIGN_CENTER);
        texto6 = new Paragraph("Jefe del Departamento de Servicios Escolares", fontBolder);
        texto6.setAlignment(Element.ALIGN_CENTER);

        //Se agregan los elementos creados
        document.add(nombreUniversidad);
        document.add(departamento);
        if(new Date().after(fechaFinVeda)){
            document.add(logoSep);
            document.add(logoUT);
            //System.out.println("La fecha de hoy es despues de la veda electoral");
        }
        document.add(textoPrincipal);
        document.add(parrafo);
        document.add(texto2);
        document.add(tablaPrincipal);
        document.add(texto3);
        document.add(texto4);
        document.add(texto5);
        document.add(texto6);
        //Se cierra el documento
        document.close();
        FacesContext.getCurrentInstance()
                .addMessage(null, new
                        FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Su documento se ha generado correctamente",
                        ""));
        File file = new File(base.concat(nombrePdf));

        try {
            InputStream input = new FileInputStream(file);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            setDownload(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
//            String ruta = "/sii2/media".concat(file.toURI().toString().split("archivos")[1]);
            //Ajax.oncomplete("descargar('" + ruta + "');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void generarPdfCuarto(DtoEstudiante estudiante)throws IOException, DocumentException{
        Document document;

        document = new Document(PageSize.LETTER, 70, 70, 15, 15);
        Paragraph nombreUniversidad, departamento, textoPrincipal, texto1, texto2, texto3, texto4, texto5, texto6;

        String nombrePdf = "Kardex Calificaciones.pdf";
        //String ruta = ServicioArchivos.carpetaRaiz.concat(File.separator).concat("acta_final").concat(File.separator).concat(nombrePdf);
        Image logoSep = Image.getInstance("C:\\archivos\\acta_final\\logoSep.png");     Image logoUT = Image.getInstance("C:\\archivos\\acta_final\\logoUT.jpg");
        Font fontBold = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);              Font fontNormal = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
        Font fontMateria2 = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL);        Font fontMateria1 = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        Font fontBolder = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);           String base = ServicioArchivos.carpetaRaiz.concat("acta_final").concat(File.separator);

        ServicioArchivos.addCarpetaRelativa(base);  ServicioArchivos.eliminarArchivo(base.concat(nombrePdf)); OutputStream out = new FileOutputStream(new File(base.concat(nombrePdf)));

        PdfWriter.getInstance(document, out);    PdfWriter.getInstance(document, new FileOutputStream("C:\\archivos\\acta_final\\Kardex Calificaciones.pdf"));
        //PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\"+usuario+"\\Downloads\\acta_final_"+grupo.getLiteral()+"_"+grupo.getTutor()+".pdf"));
        PdfPTable tablaPrincipal = new PdfPTable(2);    PdfPTable table = new PdfPTable(2);           PdfPTable table3 = new PdfPTable(2);
        PdfPTable tablaPromedio2 = new PdfPTable(2);    PdfPTable tablaPromedio = new PdfPTable(2);   PdfPTable table4 = new PdfPTable(2);
        PdfPTable tablaPromedio3 = new PdfPTable(2);    PdfPTable tablaPromedio4 = new PdfPTable(2);  PdfPTable table5 = new PdfPTable(2);
        

        //Se abre el pdf para iniciar a darle formato
        document.open();
        //Se coloca los elementos necesarios que contendra el pdf
        logoSep.scaleToFit(80,280);      logoSep.setAbsolutePosition(80f, 696f);    logoSep.setAlignment(Element.ALIGN_LEFT);
        nombreUniversidad = new Paragraph("UNIVERSIDAD TECNÓLOGICA DE XICOTEPEC DE JUÁREZ", fontBold);  nombreUniversidad.setAlignment(Element.ALIGN_RIGHT);  nombreUniversidad.setLeading(57,0);
        departamento = new Paragraph("DEPARTAMENTO DE SERVICIOS ESCOLARES", fontBold);                  departamento.setLeading(16,0);        departamento.setAlignment(Element.ALIGN_RIGHT);

        logoUT.scaleToFit(65,100);        logoUT.setAbsolutePosition(180f, 692f);         logoUT.setAlignment(Element.ALIGN_RIGHT);

        textoPrincipal = new Paragraph("HISTORIAL ACADÉMICO", fontMateria1);   textoPrincipal.setAlignment(Element.ALIGN_CENTER);
        textoPrincipal.setLeading(30, 0);                                      textoPrincipal.setSpacingAfter(13);

        String carrera1 = ejb.obtenerProgramaEducativo(estudiante.getInscripcionActiva().getInscripcion().getCarrera()).getValor().getNombre();
        String carrera2 = ejb.obtenerProgramaEducativo(estudiante.getInscripcionActiva().getInscripcion().getCarrera()).getValor().getNombre();
        texto1 = new Paragraph(9);   texto1.setSpacingAfter(10);   texto2 = new Paragraph(9);


        texto1.setAlignment(Element.ALIGN_JUSTIFIED_ALL);   texto1.add(new Paragraph("La ", fontNormal));     texto1.add(new Paragraph("Universidad Tecnológica de Xicotepec de Juárez ", fontBolder));
        texto1.add(new Paragraph("hace constar que, según documentos existentes en el archivo escolar el (la) C. ", fontNormal));
        texto1.add(new Paragraph(
                estudiante.getAspirante().getIdPersona().getApellidoPaterno().concat(" ")
                        .concat(estudiante.getAspirante().getIdPersona().getApellidoMaterno()).concat(" ")
                        .concat(estudiante.getAspirante().getIdPersona().getNombre()), fontBolder));
        texto1.add(new Paragraph("con matricula ", fontNormal));     texto1.add(new Paragraph(String.valueOf(estudiante.getInscripcionActiva().getInscripcion().getMatricula()), fontBolder));
        texto1.add(new Paragraph(", cursó las siguientes asignaturas correspondientes a la carrera de: ", fontNormal));   texto1.add(new Paragraph("Técnico Superior Universtiario en ".concat(carrera1), fontBolder));
        texto1.add(new Paragraph(" y actualmente se encuentra cursando el ", fontNormal));     texto1.add(new Paragraph(String.valueOf(estudiante.getInscripciones().get(0).getGrupo().getGrado()).concat(" "), fontBolder));
        texto1.add(new Paragraph("cuatrimestre.", fontNormal));        Phrase parrafo = new Phrase(texto1);
        texto2.add(new Paragraph("A petición del interesado y para los fines legales, que a él (ella) convengan, se expide la presente en Xicotepec de Juárez, Puebla, el "
                    .concat(sdf.format(new Date())).concat("."), fontNormal));
        texto2.setSpacingAfter(20);
        
        table.setWidthPercentage(100);           table.setHorizontalAlignment(Element.ALIGN_LEFT);           table.setWidths(new float[] {40f, 7f});
        table3.setWidthPercentage(100);          table3.setHorizontalAlignment(Element.ALIGN_LEFT);          table3.setWidths(new float[] {40f, 7f});
        table4.setWidthPercentage(100);          table4.setHorizontalAlignment(Element.ALIGN_LEFT);          table4.setWidths(new float[] {40f, 7f});
        table5.setWidthPercentage(100);          table5.setHorizontalAlignment(Element.ALIGN_LEFT);          table5.setWidths(new float[] {40f, 7f});
        tablaPromedio2.setWidthPercentage(100);  tablaPromedio2.setHorizontalAlignment(Element.ALIGN_LEFT);  tablaPromedio2.setWidths(new float[] {40f, 7f});
        tablaPromedio.setWidthPercentage(100);  tablaPromedio.setHorizontalAlignment(Element.ALIGN_LEFT);  tablaPromedio.setWidths(new float[] {40f, 7f});
        tablaPromedio3.setWidthPercentage(100);  tablaPromedio3.setHorizontalAlignment(Element.ALIGN_LEFT);  tablaPromedio3.setWidths(new float[] {40f, 7f});
        tablaPromedio4.setWidthPercentage(100);  tablaPromedio4.setHorizontalAlignment(Element.ALIGN_LEFT);  tablaPromedio4.setWidths(new float[] {40f, 7f});
        
        Collections.sort(estudiante.getInscripciones(), (x,y) -> Integer.compare(x.getGrupo().getGrado(),y.getGrupo().getGrado()));
        estudiante.getInscripciones().forEach(estudiante1 -> {
            PdfPCell celda = new PdfPCell();                         PdfPCell celda2 = new PdfPCell();     PdfPCell cuatrimestre,texto,avg;
            BigDecimal promedioCuatrimestral1 = BigDecimal.ZERO;     Integer materiastotal;
            
            if(estudiante1.getGrupo().getGrado() == 1){
                Integer contador;  List<BigDecimal> promedios3 = new ArrayList<>();
                BigDecimal p = estudiante.getInscripciones()
                        .stream()
                        .filter(e -> e.getInscripcion().getGrupo().getGrado() == 4)
                        .map(dtoInscripcion -> controlador.getPromedioCuatrimestral(dtoInscripcion.getInscripcion()))
                        .map(BigDecimal::plus)
                        .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
                if(p.compareTo(new BigDecimal(8)) == -1){
                    contador = 3;
                    //System.out.println("Es "+ contador);
                }else{
                    contador = 4;
                    //System.out.println("Es "+ contador);
                }
                BigDecimal registro = new BigDecimal(contador);
                BigDecimal promedioA = estudiante.getInscripciones().stream()
                        .filter(x -> x.getGrupo().getGrado() <= 4)
                        .map(dtoInscripcion -> controlador.getPromedioCuatrimestral(dtoInscripcion.getInscripcion()))
                        .collect(Collectors.toList())
                        .stream()
                        .filter(pr -> pr.compareTo(new BigDecimal(7.9999)) == 1)
                        .map(BigDecimal::plus).reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(registro, 8, RoundingMode.HALF_UP);
                cuatrimestre = new PdfPCell(new Paragraph("PRIMER", fontBold));      cuatrimestre.setColspan(3);       cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table3.addCell(cuatrimestre);
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 1).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    PdfPCell materias2 = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias2.setHorizontalAlignment(Element.ALIGN_LEFT);   PdfPCell promedios2;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios2 = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios2 = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table3.addCell(materias2);
                    table3.addCell(promedios2);
                });
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));   avg = new PdfPCell(new Paragraph(promedioA.setScale(2, RoundingMode.HALF_UP).toString(), fontBold));
                tablaPromedio.addCell(texto);     tablaPromedio.addCell(avg);
                celda.addElement(table3);          celda2.addElement(tablaPromedio);
            }
            if(estudiante1.getGrupo().getGrado() == 2){
                cuatrimestre = new PdfPCell(new Paragraph("SEGUNDO", fontBold));          cuatrimestre.setColspan(3);
                cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);            table.addCell(cuatrimestre);
                
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 2).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    if(promedio.compareTo(BigDecimal.ZERO) == 0) return;
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal promedioFinal = BigDecimal.ZERO;
                    PdfPCell materias = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias.setHorizontalAlignment(Element.ALIGN_LEFT);
                    PdfPCell promedios;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table.addCell(materias);
                    table.addCell(promedios);
                });
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));
                avg = new PdfPCell(new Paragraph("", fontBold));
                tablaPromedio.addCell(texto);
                tablaPromedio.addCell(avg);
                celda.addElement(table);
                celda2.addElement(new Paragraph(""));
            }
            if(estudiante1.getGrupo().getGrado() == 3){
                cuatrimestre = new PdfPCell(new Paragraph("TERCERO", fontBold));
                cuatrimestre.setColspan(3);
                cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table4.addCell(cuatrimestre);
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() <= 3).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal promedioFinal = BigDecimal.ZERO;
                    PdfPCell materias = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias.setHorizontalAlignment(Element.ALIGN_LEFT);
                    PdfPCell promedios;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table4.addCell(materias);
                    table4.addCell(promedios);
                });
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));
                avg = new PdfPCell(new Paragraph("", fontBold));
                tablaPromedio3.addCell(texto);
                tablaPromedio3.addCell(avg);
                celda.addElement(table4);
                celda2.addElement(new Paragraph(""));
            }
            if(estudiante1.getGrupo().getGrado() == 4){
                Integer cargas = (int)estudiante1.getGrupo().getCargaAcademicaList().stream().mapToInt(CargaAcademica::getCarga).count();
                Integer calificaciones = (int)estudiante1.getInscripcion().getCalificacionPromedioList().stream().mapToInt((cp) -> cp.getCargaAcademica().getCarga()).count();
                BigDecimal promedioC = controlador.getPromedioCuatrimestral(estudiante1.getInscripcion());
                if(!cargas.equals(calificaciones) || promedioC.compareTo(new BigDecimal(8)) == -1) return;
                cuatrimestre = new PdfPCell(new Paragraph("CUARTO", fontBold));
                cuatrimestre.setColspan(3);
                cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table5.addCell(cuatrimestre);
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() <= 4).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal promedioFinal = BigDecimal.ZERO;
                    PdfPCell materias = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias.setHorizontalAlignment(Element.ALIGN_LEFT);
                    PdfPCell promedios;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table5.addCell(materias);
                    table5.addCell(promedios);
                });
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));
                avg = new PdfPCell(new Paragraph("", fontBold));
                tablaPromedio3.addCell(texto);
                tablaPromedio3.addCell(avg);
                celda.addElement(table5);
                celda2.addElement(new Paragraph(""));
            }
            tablaPrincipal.addCell(celda);
            tablaPrincipal.addCell(celda2);
        });
        texto3 = new Paragraph("ATENTAMENTE", fontBold);
        texto3.setAlignment(Element.ALIGN_CENTER);
        texto4 = new Paragraph("'TECNOLOGIA Y CALIDAD, BASE DEL DESARROLLO'", fontBold);
        texto4.setAlignment(Element.ALIGN_CENTER);
        texto4.setSpacingAfter(15);
        texto5 = new Paragraph("Ing. Juan Alberto Sánchez González", fontBolder);
        texto5.setSpacingBefore(70);
        texto5.setAlignment(Element.ALIGN_CENTER);
        texto6 = new Paragraph("Jefe del Departamento de Servicios Escolares", fontBolder);
        texto6.setAlignment(Element.ALIGN_CENTER);

        //Se agregan los elementos creados
        document.add(nombreUniversidad);
        document.add(departamento);
        if(new Date().after(fechaFinVeda)){
            document.add(logoSep);
            document.add(logoUT);
            //System.out.println("La fecha de hoy es despues de la veda electoral");
        }
        document.add(textoPrincipal);
        document.add(parrafo);
        document.add(texto2);
        document.add(tablaPrincipal);
        document.add(texto3);
        document.add(texto4);
        document.add(texto5);
        document.add(texto6);
        //Se cierra el documento
        document.close();
        FacesContext.getCurrentInstance()
                .addMessage(null, new
                        FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Su documento se ha generado correctamente",
                        ""));
        File file = new File(base.concat(nombrePdf));

        try {
            InputStream input = new FileInputStream(file);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            setDownload(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
//            String ruta = "/sii2/media".concat(file.toURI().toString().split("archivos")[1]);
            //Ajax.oncomplete("descargar('" + ruta + "');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void generarPdfQuinto(DtoEstudiante estudiante)throws IOException, DocumentException{
        Document document;

        document = new Document(PageSize.LETTER, 70, 70, 15, 15);
        Paragraph nombreUniversidad, departamento, textoPrincipal, texto1, texto2, texto3, texto4, texto5, texto6;

        String nombrePdf = "Kardex Calificaciones.pdf";
        //String ruta = ServicioArchivos.carpetaRaiz.concat(File.separator).concat("acta_final").concat(File.separator).concat(nombrePdf);
        Image logoSep = Image.getInstance("C:\\archivos\\acta_final\\logoSep.png");     Image logoUT = Image.getInstance("C:\\archivos\\acta_final\\logoUT.jpg");
        Font fontBold = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);              Font fontNormal = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
        Font fontMateria2 = new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL);        Font fontMateria1 = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        Font fontBolder = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);           String base = ServicioArchivos.carpetaRaiz.concat("acta_final").concat(File.separator);
        Font fontBold1 = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);

        ServicioArchivos.addCarpetaRelativa(base);  ServicioArchivos.eliminarArchivo(base.concat(nombrePdf)); OutputStream out = new FileOutputStream(new File(base.concat(nombrePdf)));

        PdfWriter.getInstance(document, out);    PdfWriter.getInstance(document, new FileOutputStream("C:\\archivos\\acta_final\\Kardex Calificaciones.pdf"));
        //PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\"+usuario+"\\Downloads\\acta_final_"+grupo.getLiteral()+"_"+grupo.getTutor()+".pdf"));
        PdfPTable tablaPrincipal = new PdfPTable(2);    PdfPTable table = new PdfPTable(2);           PdfPTable table3 = new PdfPTable(2);
        PdfPTable tablaPromedio = new PdfPTable(2);     PdfPTable table4 = new PdfPTable(2);          PdfPTable table5 = new PdfPTable(2);
        PdfPTable table6 = new PdfPTable(2);            PdfPTable table7 = new PdfPTable(2);

        //Se abre el pdf para iniciar a darle formato
        document.open();
        //Se coloca los elementos necesarios que contendra el pdf
        logoSep.scaleToFit(80,280);      logoSep.setAbsolutePosition(80f, 696f);    logoSep.setAlignment(Element.ALIGN_LEFT);
        nombreUniversidad = new Paragraph("UNIVERSIDAD TECNÓLOGICA DE XICOTEPEC DE JUÁREZ", fontBold);  nombreUniversidad.setAlignment(Element.ALIGN_RIGHT);  nombreUniversidad.setLeading(57,0);
        departamento = new Paragraph("DEPARTAMENTO DE SERVICIOS ESCOLARES", fontBold);                  departamento.setLeading(16,0);        departamento.setAlignment(Element.ALIGN_RIGHT);

        logoUT.scaleToFit(65,100);        logoUT.setAbsolutePosition(180f, 692f);         logoUT.setAlignment(Element.ALIGN_RIGHT);

        textoPrincipal = new Paragraph("HISTORIAL ACADÉMICO", fontMateria1);   textoPrincipal.setAlignment(Element.ALIGN_CENTER);
        textoPrincipal.setLeading(30, 0);                                      textoPrincipal.setSpacingAfter(13);

        String carrera1 = ejb.obtenerProgramaEducativo(estudiante.getInscripcionActiva().getInscripcion().getCarrera()).getValor().getNombre();
        String carrera2 = ejb.obtenerProgramaEducativo(estudiante.getInscripcionActiva().getInscripcion().getCarrera()).getValor().getNombre();
        texto1 = new Paragraph(9);   texto1.setSpacingAfter(7);   texto2 = new Paragraph(9);


        texto1.setAlignment(Element.ALIGN_JUSTIFIED_ALL);   texto1.add(new Paragraph("La ", fontNormal));     texto1.add(new Paragraph("Universidad Tecnológica de Xicotepec de Juárez ", fontBolder));
        texto1.add(new Paragraph("hace constar que, según documentos existentes en el archivo escolar el (la) C. ", fontNormal));
        texto1.add(new Paragraph(
                estudiante.getAspirante().getIdPersona().getApellidoPaterno().concat(" ")
                        .concat(estudiante.getAspirante().getIdPersona().getApellidoMaterno()).concat(" ")
                        .concat(estudiante.getAspirante().getIdPersona().getNombre()), fontBolder));
        texto1.add(new Paragraph("con matricula ", fontNormal));     texto1.add(new Paragraph(String.valueOf(estudiante.getInscripcionActiva().getInscripcion().getMatricula()), fontBolder));
        texto1.add(new Paragraph(", cursó las siguientes asignaturas correspondientes a la carrera de: ", fontNormal));   texto1.add(new Paragraph("Técnico Superior Universtiario en ".concat(carrera1), fontBolder));
        texto1.add(new Paragraph(" y actualmente se encuentra cursando el ", fontNormal));     texto1.add(new Paragraph(String.valueOf(estudiante.getInscripciones().get(0).getGrupo().getGrado()).concat(" "), fontBolder));
        texto1.add(new Paragraph("cuatrimestre.", fontNormal));        Phrase parrafo = new Phrase(texto1);
        texto2.add(new Paragraph("A petición del interesado y para los fines legales, que a él (ella) convengan, se expide la presente en Xicotepec de Juárez, Puebla, el "
                    .concat(sdf.format(new Date())).concat("."), fontNormal));
        texto2.setSpacingAfter(2);
        
        tablaPrincipal.setWidthPercentage(100);  tablaPrincipal.setHorizontalAlignment(Element.ALIGN_LEFT);  tablaPrincipal.setWidths(new float[] {50f, 50f});
        table.setWidthPercentage(100);           table.setHorizontalAlignment(Element.ALIGN_LEFT);           table.setWidths(new float[] {43f, 7f});
        table3.setWidthPercentage(100);          table3.setHorizontalAlignment(Element.ALIGN_LEFT);          table3.setWidths(new float[] {43f, 7f});
        table4.setWidthPercentage(100);          table4.setHorizontalAlignment(Element.ALIGN_LEFT);          table4.setWidths(new float[] {43f, 7f});
        table5.setWidthPercentage(100);          table5.setHorizontalAlignment(Element.ALIGN_LEFT);          table5.setWidths(new float[] {43f, 7f});
        table6.setWidthPercentage(100);          table6.setHorizontalAlignment(Element.ALIGN_LEFT);          table6.setWidths(new float[] {43f, 7f});
        table7.setWidthPercentage(100);          table7.setHorizontalAlignment(Element.ALIGN_LEFT);          table7.setWidths(new float[] {43f, 7f});
        tablaPromedio.setWidthPercentage(100);  tablaPromedio.setHorizontalAlignment(Element.ALIGN_LEFT);  tablaPromedio.setWidths(new float[] {43f, 7f});
        
        Collections.sort(estudiante.getInscripciones(), (x,y) -> Integer.compare(x.getGrupo().getGrado(),y.getGrupo().getGrado()));
        estudiante.getInscripciones().forEach(estudiante1 -> {
            PdfPCell celda = new PdfPCell();                         PdfPCell celda2 = new PdfPCell();     PdfPCell cuatrimestre,texto,avg;
            if(estudiante1.getGrupo().getGrado() == 1){
                Integer contador;  List<BigDecimal> promedios3 = new ArrayList<>();
                BigDecimal p = estudiante.getInscripciones()
                        .stream()
                        .filter(e -> e.getInscripcion().getGrupo().getGrado() == 5)
                        .map(dtoInscripcion -> controlador.getPromedioCuatrimestral(dtoInscripcion.getInscripcion()))
                        .map(BigDecimal::plus)
                        .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
                if(p.compareTo(new BigDecimal(8)) == -1){
                    contador = 4;
                    //System.out.println("Es "+ contador);
                }else{
                    contador = 5;
                    //System.out.println("Es "+ contador);
                }
                BigDecimal promedioA = estudiante.getInscripciones().stream()
                        .filter(x -> x.getGrupo().getGrado() <= 5)
                        .map(dtoInscripcion -> controlador.getPromedioCuatrimestral(dtoInscripcion.getInscripcion()))
                        .collect(Collectors.toList())
                        .stream()
                        .filter(pr -> pr.compareTo(new BigDecimal(7.9999)) == 1)
                        .map(BigDecimal::plus).reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(new BigDecimal(contador), 8, RoundingMode.HALF_UP);
                cuatrimestre = new PdfPCell(new Paragraph("PRIMER", fontBold1));      cuatrimestre.setColspan(3);       cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table3.addCell(cuatrimestre);
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 1).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    PdfPCell materias2 = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias2.setHorizontalAlignment(Element.ALIGN_LEFT);   PdfPCell promedios2;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios2 = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios2 = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table3.addCell(materias2);
                    table3.addCell(promedios2);
                });
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));   avg = new PdfPCell(new Paragraph(promedioA.setScale(2, RoundingMode.HALF_UP).toString(), fontBold));
                tablaPromedio.addCell(texto);     tablaPromedio.addCell(avg);
                celda.addElement(table3);          celda2.addElement(tablaPromedio);
            }
            if(estudiante1.getGrupo().getGrado() == 2){
                cuatrimestre = new PdfPCell(new Paragraph("SEGUNDO", fontBold1));          cuatrimestre.setColspan(3);
                cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);                table.addCell(cuatrimestre);
                
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 2).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    if(promedio.compareTo(BigDecimal.ZERO) == 0) return;
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal promedioFinal = BigDecimal.ZERO;
                    PdfPCell materias = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias.setHorizontalAlignment(Element.ALIGN_LEFT);
                    PdfPCell promedios;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table.addCell(materias);
                    table.addCell(promedios);
                });
                celda.addElement(table);
                celda2.addElement(new Paragraph(""));
            }
            if(estudiante1.getGrupo().getGrado() == 3){
                cuatrimestre = new PdfPCell(new Paragraph("TERCERO", fontBold1));
                cuatrimestre.setColspan(3);
                cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table4.addCell(cuatrimestre);
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 3).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal promedioFinal = BigDecimal.ZERO;
                    PdfPCell materias = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias.setHorizontalAlignment(Element.ALIGN_LEFT);
                    PdfPCell promedios;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table4.addCell(materias);
                    table4.addCell(promedios);
                });
                celda.addElement(table4);
                celda2.addElement(new Paragraph(""));
            }
            if(estudiante1.getGrupo().getGrado() == 4){
                cuatrimestre = new PdfPCell(new Paragraph("CUARTO", fontBold1));
                cuatrimestre.setColspan(3);
                cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table5.addCell(cuatrimestre);
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 4).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal promedioFinal = BigDecimal.ZERO;
                    PdfPCell materias = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias.setHorizontalAlignment(Element.ALIGN_LEFT);
                    PdfPCell promedios;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table5.addCell(materias);
                    table5.addCell(promedios);
                });
                celda.addElement(table5);
                celda2.addElement(new Paragraph(""));
            }
            if(estudiante1.getGrupo().getGrado() == 5){
                Integer cargas = (int)estudiante1.getGrupo().getCargaAcademicaList().stream().mapToInt(CargaAcademica::getCarga).count();
                Integer calificaciones = (int)estudiante1.getInscripcion().getCalificacionPromedioList().stream().mapToInt((cp) -> cp.getCargaAcademica().getCarga()).count();
                BigDecimal promedioC = controlador.getPromedioCuatrimestral(estudiante1.getInscripcion());
                if(!cargas.equals(calificaciones) || promedioC.compareTo(new BigDecimal(8)) == -1) return;
                cuatrimestre = new PdfPCell(new Paragraph("QUINTO", fontBold1));  cuatrimestre.setColspan(3);  cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table6.addCell(cuatrimestre);
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 5).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal promedioFinal = BigDecimal.ZERO;
                    PdfPCell materias = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias.setHorizontalAlignment(Element.ALIGN_LEFT);
                    PdfPCell promedios;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table6.addCell(materias);
                    table6.addCell(promedios);
                });
                celda.addElement(table6);
                celda2.addElement(new Paragraph(""));
            }
            tablaPrincipal.addCell(celda);
            tablaPrincipal.addCell(celda2);
        });
        texto3 = new Paragraph("ATENTAMENTE", fontBold1);                                       texto3.setAlignment(Element.ALIGN_CENTER);
        texto4 = new Paragraph("'TECNOLOGIA Y CALIDAD, BASE DEL DESARROLLO'", fontBold1);       texto4.setAlignment(Element.ALIGN_CENTER);       texto4.setLeading(7, 0);
        texto5 = new Paragraph("Ing. Juan Alberto Sánchez González", fontBolder);              texto5.setSpacingBefore(70);                     texto5.setAlignment(Element.ALIGN_CENTER);
        texto6 = new Paragraph("Jefe del Departamento de Servicios Escolares", fontBolder);    texto6.setAlignment(Element.ALIGN_CENTER);

        //Se agregan los elementos creados
        document.add(nombreUniversidad);
        document.add(departamento);
        if(new Date().after(fechaFinVeda)){
            document.add(logoSep);
            document.add(logoUT);
            //System.out.println("La fecha de hoy es despues de la veda electoral");
        }
        document.add(textoPrincipal);
        document.add(parrafo);
        document.add(texto2);
        document.add(tablaPrincipal);
        document.add(texto3);
        document.add(texto4);
        document.add(texto5);
        document.add(texto6);
        //Se cierra el documento
        document.close();
        FacesContext.getCurrentInstance()
                .addMessage(null, new
                        FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Su documento se ha generado correctamente",
                        ""));
        File file = new File(base.concat(nombrePdf));

        try {
            InputStream input = new FileInputStream(file);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            setDownload(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
//            String ruta = "/sii2/media".concat(file.toURI().toString().split("archivos")[1]);
            //Ajax.oncomplete("descargar('" + ruta + "');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void generarPdfSexto(DtoEstudiante estudiante)throws IOException, DocumentException{
        Document document;

        document = new Document(PageSize.LETTER, 70, 70, 15, 15);
        Paragraph nombreUniversidad, departamento, textoPrincipal, texto1, texto2, texto3, texto4, texto5, texto6;

        String nombrePdf = "Kardex Calificaciones.pdf";
        //String ruta = ServicioArchivos.carpetaRaiz.concat(File.separator).concat("acta_final").concat(File.separator).concat(nombrePdf);
        Image logoSep = Image.getInstance("C:\\archivos\\acta_final\\logoSep.png");     Image logoUT = Image.getInstance("C:\\archivos\\acta_final\\logoUT.jpg");
        Font fontBold = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);              Font fontNormal = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
        Font fontMateria2 = new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL);        Font fontMateria1 = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        Font fontBolder = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);           String base = ServicioArchivos.carpetaRaiz.concat("acta_final").concat(File.separator);
        Font fontBold1 = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);

        ServicioArchivos.addCarpetaRelativa(base);  ServicioArchivos.eliminarArchivo(base.concat(nombrePdf)); OutputStream out = new FileOutputStream(new File(base.concat(nombrePdf)));

        PdfWriter.getInstance(document, out);    PdfWriter.getInstance(document, new FileOutputStream("C:\\archivos\\acta_final\\Kardex Calificaciones.pdf"));
        //PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\"+usuario+"\\Downloads\\acta_final_"+grupo.getLiteral()+"_"+grupo.getTutor()+".pdf"));
        PdfPTable tablaPrincipal = new PdfPTable(2);    PdfPTable table = new PdfPTable(2);           PdfPTable table3 = new PdfPTable(2);
        PdfPTable tablaPromedio = new PdfPTable(2);     PdfPTable table4 = new PdfPTable(2);          PdfPTable table5 = new PdfPTable(2);
        PdfPTable table6 = new PdfPTable(2);            PdfPTable table7 = new PdfPTable(2);

        //Se abre el pdf para iniciar a darle formato
        document.open();
        //Se coloca los elementos necesarios que contendra el pdf
        logoSep.scaleToFit(80,280);      logoSep.setAbsolutePosition(80f, 696f);    logoSep.setAlignment(Element.ALIGN_LEFT);
        nombreUniversidad = new Paragraph("UNIVERSIDAD TECNÓLOGICA DE XICOTEPEC DE JUÁREZ", fontBold);  nombreUniversidad.setAlignment(Element.ALIGN_RIGHT);  nombreUniversidad.setLeading(57,0);
        departamento = new Paragraph("DEPARTAMENTO DE SERVICIOS ESCOLARES", fontBold);                  departamento.setLeading(16,0);        departamento.setAlignment(Element.ALIGN_RIGHT);

        logoUT.scaleToFit(65,100);        logoUT.setAbsolutePosition(180f, 692f);         logoUT.setAlignment(Element.ALIGN_RIGHT);

        textoPrincipal = new Paragraph("HISTORIAL ACADÉMICO", fontMateria1);   textoPrincipal.setAlignment(Element.ALIGN_CENTER);
        textoPrincipal.setLeading(30, 0);                                      textoPrincipal.setSpacingAfter(13);

        String carrera1 = ejb.obtenerProgramaEducativo(estudiante.getInscripcionActiva().getInscripcion().getCarrera()).getValor().getNombre();
        String carrera2 = ejb.obtenerProgramaEducativo(estudiante.getInscripcionActiva().getInscripcion().getCarrera()).getValor().getNombre();
        texto1 = new Paragraph(9);   texto1.setSpacingAfter(7);   texto2 = new Paragraph(9);


        texto1.setAlignment(Element.ALIGN_JUSTIFIED_ALL);   texto1.add(new Paragraph("La ", fontNormal));     texto1.add(new Paragraph("Universidad Tecnológica de Xicotepec de Juárez ", fontBolder));
        texto1.add(new Paragraph("hace constar que, según documentos existentes en el archivo escolar el (la) C. ", fontNormal));
        texto1.add(new Paragraph(
                estudiante.getAspirante().getIdPersona().getApellidoPaterno().concat(" ")
                        .concat(estudiante.getAspirante().getIdPersona().getApellidoMaterno()).concat(" ")
                        .concat(estudiante.getAspirante().getIdPersona().getNombre()), fontBolder));
        texto1.add(new Paragraph("con matricula ", fontNormal));     texto1.add(new Paragraph(String.valueOf(estudiante.getInscripcionActiva().getInscripcion().getMatricula()), fontBolder));
        texto1.add(new Paragraph(", cursó las siguientes asignaturas correspondientes a la carrera de: ", fontNormal));   texto1.add(new Paragraph("Técnico Superior Universtiario en ".concat(carrera1), fontBolder));
        texto1.add(new Paragraph(" y actualmente se encuentra cursando el ", fontNormal));     texto1.add(new Paragraph(String.valueOf(estudiante.getInscripciones().get(0).getGrupo().getGrado()).concat(" "), fontBolder));
        texto1.add(new Paragraph("cuatrimestre.", fontNormal));        Phrase parrafo = new Phrase(texto1);
        texto2.add(new Paragraph("A petición del interesado y para los fines legales, que a él (ella) convengan, se expide la presente en Xicotepec de Juárez, Puebla, el "
                    .concat(sdf.format(new Date())).concat("."), fontNormal));
        texto2.setSpacingAfter(2);
        
        tablaPrincipal.setWidthPercentage(100);  tablaPrincipal.setHorizontalAlignment(Element.ALIGN_LEFT);  tablaPrincipal.setWidths(new float[] {50f, 50f});
        table.setWidthPercentage(100);           table.setHorizontalAlignment(Element.ALIGN_LEFT);           table.setWidths(new float[] {43f, 7f});
        table3.setWidthPercentage(100);          table3.setHorizontalAlignment(Element.ALIGN_LEFT);          table3.setWidths(new float[] {43f, 7f});
        table4.setWidthPercentage(100);          table4.setHorizontalAlignment(Element.ALIGN_LEFT);          table4.setWidths(new float[] {43f, 7f});
        table5.setWidthPercentage(100);          table5.setHorizontalAlignment(Element.ALIGN_LEFT);          table5.setWidths(new float[] {43f, 7f});
        table6.setWidthPercentage(100);          table6.setHorizontalAlignment(Element.ALIGN_LEFT);          table6.setWidths(new float[] {43f, 7f});
        table7.setWidthPercentage(100);          table7.setHorizontalAlignment(Element.ALIGN_LEFT);          table7.setWidths(new float[] {43f, 7f});
        tablaPromedio.setWidthPercentage(100);  tablaPromedio.setHorizontalAlignment(Element.ALIGN_LEFT);  tablaPromedio.setWidths(new float[] {43f, 7f});
        
        Collections.sort(estudiante.getInscripciones(), (x,y) -> Integer.compare(x.getGrupo().getGrado(),y.getGrupo().getGrado()));
        estudiante.getInscripciones().forEach(estudiante1 -> {
            PdfPCell celda = new PdfPCell();                         PdfPCell celda2 = new PdfPCell();     PdfPCell cuatrimestre,texto,avg;
            if(estudiante1.getGrupo().getGrado() == 1){
                Integer contador;  List<BigDecimal> promedios3 = new ArrayList<>();
                BigDecimal promedioA = estudiante.getInscripciones().stream()
                        .filter(x -> x.getGrupo().getGrado() <= 5)
                        .map(dtoInscripcion -> controlador.getPromedioCuatrimestral(dtoInscripcion.getInscripcion()))
                        .collect(Collectors.toList())
                        .stream()
                        .filter(pr -> pr.compareTo(new BigDecimal(7.9999)) == 1)
                        .map(BigDecimal::plus).reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(new BigDecimal(5), 8, RoundingMode.HALF_UP);
                cuatrimestre = new PdfPCell(new Paragraph("PRIMER", fontBold1));      cuatrimestre.setColspan(3);       cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table3.addCell(cuatrimestre);
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 1).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    PdfPCell materias2 = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias2.setHorizontalAlignment(Element.ALIGN_LEFT);   PdfPCell promedios2;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios2 = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios2 = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table3.addCell(materias2);
                    table3.addCell(promedios2);
                });
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));   avg = new PdfPCell(new Paragraph(promedioA.setScale(2, RoundingMode.HALF_UP).toString(), fontBold));
                tablaPromedio.addCell(texto);     tablaPromedio.addCell(avg);
                celda.addElement(table3);          celda2.addElement(tablaPromedio);
            }
            if(estudiante1.getGrupo().getGrado() == 2){
                cuatrimestre = new PdfPCell(new Paragraph("SEGUNDO", fontBold1));          cuatrimestre.setColspan(3);
                cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);                table.addCell(cuatrimestre);
                
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 2).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    if(promedio.compareTo(BigDecimal.ZERO) == 0) return;
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal promedioFinal = BigDecimal.ZERO;
                    PdfPCell materias = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias.setHorizontalAlignment(Element.ALIGN_LEFT);
                    PdfPCell promedios;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table.addCell(materias);
                    table.addCell(promedios);
                });
                celda.addElement(table);
                celda2.addElement(new Paragraph(""));
            }
            if(estudiante1.getGrupo().getGrado() == 3){
                cuatrimestre = new PdfPCell(new Paragraph("TERCERO", fontBold1));
                cuatrimestre.setColspan(3);
                cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table4.addCell(cuatrimestre);
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 3).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal promedioFinal = BigDecimal.ZERO;
                    PdfPCell materias = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias.setHorizontalAlignment(Element.ALIGN_LEFT);
                    PdfPCell promedios;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table4.addCell(materias);
                    table4.addCell(promedios);
                });
                celda.addElement(table4);
                celda2.addElement(new Paragraph(""));
            }
            if(estudiante1.getGrupo().getGrado() == 4){
                cuatrimestre = new PdfPCell(new Paragraph("CUARTO", fontBold1));
                cuatrimestre.setColspan(3);
                cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table5.addCell(cuatrimestre);
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 4).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal promedioFinal = BigDecimal.ZERO;
                    PdfPCell materias = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias.setHorizontalAlignment(Element.ALIGN_LEFT);
                    PdfPCell promedios;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table5.addCell(materias);
                    table5.addCell(promedios);
                });
                celda.addElement(table5);
                celda2.addElement(new Paragraph(""));
            }
            if(estudiante1.getGrupo().getGrado() == 5){
                cuatrimestre = new PdfPCell(new Paragraph("QUINTO", fontBold1));  cuatrimestre.setColspan(3);  cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table6.addCell(cuatrimestre);
                controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 5).forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal promedioFinal = BigDecimal.ZERO;
                    PdfPCell materias = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                    materias.setHorizontalAlignment(Element.ALIGN_LEFT);
                    PdfPCell promedios;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                    table6.addCell(materias);
                    table6.addCell(promedios);
                });
                celda.addElement(table6);
                celda2.addElement(new Paragraph(""));
            }
            if(estudiante1.getGrupo().getGrado() == 6){
                SeguimientoEstadiaEstudiante see = ejb.verificarEstadiaEstudiante(estudiante1.getInscripcion()).getValor();
                CalificacionCriterioEstadia cce = ejb.verificarCalificacionEstadia(see).getValor();
                BigDecimal acreditado = new BigDecimal((double) cce.getCalificacion()).setScale(2, RoundingMode.HALF_UP);
                if(acreditado.compareTo(new BigDecimal(8)) == -1) return;
                cuatrimestre = new PdfPCell(new Paragraph("SEXTO", fontBold1));  cuatrimestre.setColspan(3);  cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell descEstadia = new PdfPCell(new Paragraph("Estadía en el Sector Productivo", fontMateria2)); descEstadia.setHorizontalAlignment(Element.ALIGN_LEFT);
                PdfPCell promEstadia = new PdfPCell(new Paragraph("Acreditado", fontMateria2));
                table7.addCell(cuatrimestre);
                table7.addCell(descEstadia);
                table7.addCell(promEstadia);
                celda.addElement(table7);
                celda2.addElement(new Paragraph(""));
            }
            tablaPrincipal.addCell(celda);
            tablaPrincipal.addCell(celda2);
        });
        texto3 = new Paragraph("ATENTAMENTE", fontBold1);                                       texto3.setAlignment(Element.ALIGN_CENTER);
        texto4 = new Paragraph("'TECNOLOGIA Y CALIDAD, BASE DEL DESARROLLO'", fontBold1);       texto4.setAlignment(Element.ALIGN_CENTER);       texto4.setLeading(7, 0);
        texto5 = new Paragraph("Ing. Juan Alberto Sánchez González", fontBolder);              texto5.setSpacingBefore(50);                     texto5.setAlignment(Element.ALIGN_CENTER);
        texto6 = new Paragraph("Jefe del Departamento de Servicios Escolares", fontBolder);    texto6.setAlignment(Element.ALIGN_CENTER);

        //Se agregan los elementos creados
        document.add(nombreUniversidad);
        document.add(departamento);
        if(new Date().after(fechaFinVeda)){
            document.add(logoSep);
            document.add(logoUT);
            //System.out.println("La fecha de hoy es despues de la veda electoral");
        }
        document.add(textoPrincipal);
        document.add(parrafo);
        document.add(texto2);
        document.add(tablaPrincipal);
        document.add(texto3);
        document.add(texto4);
        document.add(texto5);
        document.add(texto6);
        //Se cierra el documento
        document.close();
        FacesContext.getCurrentInstance()
                .addMessage(null, new
                        FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Su documento se ha generado correctamente",
                        ""));
        File file = new File(base.concat(nombrePdf));

        try {
            InputStream input = new FileInputStream(file);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            setDownload(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
//            String ruta = "/sii2/media".concat(file.toURI().toString().split("archivos")[1]);
            //Ajax.oncomplete("descargar('" + ruta + "');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void generarPdf(DtoEstudiante estudiante) throws IOException, DocumentException {
        //Se crean las variables a utilizar para la creación del pdf
        Document document;

        document = new Document(PageSize.LETTER, 70, 70, 15, 15);
        Paragraph nombreUniversidad, departamento, textoPrincipal, texto1, texto2, texto3, texto4, texto5, texto6;

        String nombrePdf = "Kardex Calificaciones.pdf";
        //String ruta = ServicioArchivos.carpetaRaiz.concat(File.separator).concat("acta_final").concat(File.separator).concat(nombrePdf);
        Image logoSep = Image.getInstance("C:\\archivos\\acta_final\\logoSep.png");
        Image logoUT = Image.getInstance("C:\\archivos\\acta_final\\logoUT.jpg");
        Font fontBold = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
        Font fontNormal = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
        Font fontMateria2 = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL);
        Font fontMateria1 = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        Font fontBolder = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);
        String base = ServicioArchivos.carpetaRaiz.concat("acta_final").concat(File.separator);

        ServicioArchivos.addCarpetaRelativa(base);
        ServicioArchivos.eliminarArchivo(base.concat(nombrePdf));
        OutputStream out = new FileOutputStream(new File(base.concat(nombrePdf)));

        PdfWriter.getInstance(document, out);
        PdfWriter.getInstance(document, new FileOutputStream("C:\\archivos\\acta_final\\Kardex Calificaciones.pdf"));
        //PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\"+usuario+"\\Downloads\\acta_final_"+grupo.getLiteral()+"_"+grupo.getTutor()+".pdf"));
        PdfPTable tablaPrincipal = new PdfPTable(2);
        PdfPTable table = new PdfPTable(2);        
        PdfPTable table3 = new PdfPTable(2);
        PdfPTable table2 = new PdfPTable(2);
        PdfPTable table4 = new PdfPTable(2);
        PdfPTable table5 = new PdfPTable(2);
        PdfPTable table6 = new PdfPTable(2);
        PdfPTable tablaPromedio = new PdfPTable(2);
        PdfPTable tablaPromedio2 = new PdfPTable(2);
        PdfPTable tablaPromedio3 = new PdfPTable(2);
        PdfPTable tablaPromedio4 = new PdfPTable(2);
        PdfPTable tablaPromedio5 = new PdfPTable(2);
        PdfPTable tablaPromedio6 = new PdfPTable(2);
        Caster caster = new Caster();

        //Se abre el pdf para iniciar a darle formato
        document.open();
        //Se coloca los elementos necesarios que contendra el pdf
        logoSep.scaleToFit(80,280);
        logoSep.setAbsolutePosition(80f, 696f);
        logoSep.setAlignment(Element.ALIGN_LEFT);
        nombreUniversidad = new Paragraph("UNIVERSIDAD TECNÓLOGICA DE XICOTEPEC DE JUÁREZ", fontBold);
        nombreUniversidad.setAlignment(Element.ALIGN_RIGHT);
        nombreUniversidad.setLeading(57,0);
        departamento = new Paragraph("DEPARTAMENTO DE SERVICIOS ESCOLARES", fontBold);
        departamento.setLeading(16,0);
        departamento.setAlignment(Element.ALIGN_RIGHT);

        logoUT.scaleToFit(65,100);
        logoUT.setAbsolutePosition(180f, 692f);
        logoUT.setAlignment(Element.ALIGN_RIGHT);

        textoPrincipal = new Paragraph("HISTORIAL ACADÉMICO", fontMateria1);
        textoPrincipal.setAlignment(Element.ALIGN_CENTER);
        textoPrincipal.setLeading(30, 0);
        textoPrincipal.setSpacingAfter(13);

        String carrera1 = ejb.obtenerProgramaEducativo(estudiante.getInscripcionActiva().getInscripcion().getCarrera()).getValor().getNombre();
        String carrera2 = ejb.obtenerProgramaEducativo(estudiante.getInscripcionActiva().getInscripcion().getCarrera()).getValor().getNombre();
        texto1 = new Paragraph(9);
        texto1.setSpacingAfter(10);
        texto2 = new Paragraph(9);


        texto1.setAlignment(Element.ALIGN_JUSTIFIED_ALL);
        texto1.add(new Paragraph("La ", fontNormal));
        texto1.add(new Paragraph("Universidad Tecnológica de Xicotepec de Juárez ", fontBolder));
        texto1.add(new Paragraph("hace constar que, según documentos existentes en el archivo escolar el (la) C. ", fontNormal));
        texto1.add(new Paragraph(
                estudiante.getAspirante().getIdPersona().getApellidoPaterno().concat(" ")
                        .concat(estudiante.getAspirante().getIdPersona().getApellidoMaterno()).concat(" ")
                        .concat(estudiante.getAspirante().getIdPersona().getNombre()), fontBolder));
        texto1.add(new Paragraph("con matricula ", fontNormal));
        texto1.add(new Paragraph(String.valueOf(estudiante.getInscripcionActiva().getInscripcion().getMatricula()), fontBolder));
        texto1.add(new Paragraph(", cursó las siguientes asignaturas correspondientes a la carrera de: ", fontNormal));
        texto1.add(new Paragraph("Técnico Superior Universtiario en ".concat(carrera1), fontBolder));
        texto1.add(new Paragraph(" y actualmente se encuentra cursando el ", fontNormal));
        texto1.add(new Paragraph(String.valueOf(estudiante.getInscripciones().get(0).getGrupo().getGrado()).concat(" "), fontBolder));
        texto1.add(new Paragraph("cuatrimestre.", fontNormal));
        Phrase parrafo = new Phrase(texto1);
        texto2.add(new Paragraph("A petición del interesado y para los fines legales, que a él (ella) convengan, se expide la presente en Xicotepec de Juárez, Puebla, el "
                    .concat(sdf.format(new Date())).concat("."), fontNormal));
        texto2.setSpacingAfter(20);


        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidths(new float[] {40f, 7f});
        table3.setWidthPercentage(100);
        table3.setHorizontalAlignment(Element.ALIGN_LEFT);
        table3.setWidths(new float[] {40f, 7f});
        table2.setWidthPercentage(100);
        table2.setHorizontalAlignment(Element.ALIGN_LEFT);
        table2.setWidths(new float[] {40f, 7f});
        table4.setWidthPercentage(100);
        table4.setHorizontalAlignment(Element.ALIGN_LEFT);
        table4.setWidths(new float[] {40f, 7f});
        table5.setWidthPercentage(100);
        table5.setHorizontalAlignment(Element.ALIGN_LEFT);
        table5.setWidths(new float[] {40f, 7f});
        tablaPromedio.setWidthPercentage(100);
        tablaPromedio.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablaPromedio.setWidths(new float[] {40f, 7f});
        tablaPromedio2.setWidthPercentage(100);
        tablaPromedio2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablaPromedio2.setWidths(new float[] {40f, 7f});
        tablaPromedio3.setWidthPercentage(100);
        tablaPromedio3.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablaPromedio3.setWidths(new float[] {40f, 7f});
        tablaPromedio4.setWidthPercentage(100);
        tablaPromedio4.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablaPromedio4.setWidths(new float[] {40f, 7f});
        tablaPrincipal.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        tablaPrincipal.setWidthPercentage(100);
        tablaPrincipal.setSpacingBefore(8);
        tablaPrincipal.setSpacingAfter(8);
        Collections.sort(estudiante.getInscripciones(), (x,y) -> Integer.compare(x.getGrupo().getGrado(),y.getGrupo().getGrado()));
        estudiante.getInscripciones().forEach(estudiante1 -> {
//            System.out.println(estudiante1.getInscripcion().getMatricula()+"-"+estudiante1.getGrupo().getGrado());
            PdfPCell celda = new PdfPCell();
            PdfPCell celda2 = new PdfPCell();
            
            PdfPCell cuatrimestre,texto,avg;
            Integer materiastotal =0;
            BigDecimal promedioCuatrimestral1 = BigDecimal.ZERO;
            switch (estudiante1.getInscripcion().getGrupo().getGrado()){
                case 1:
                    BigDecimal promedioAcumulativo ;
                    BigDecimal suma;
                    Integer contador = 0;
                    if(estudiante.getInscripcionActiva().getGrupo().getGrado() == 5){
                        contador = 4;
                    }
                    if(estudiante.getInscripcionActiva().getGrupo().getGrado() == 2){
                        contador = 1;
                    }
                    
                    BigDecimal registro = new BigDecimal(contador);
                    List<BigDecimal> promedios3 = new ArrayList<>();
        estudiante.getInscripciones().stream().filter(x -> x.getGrupo().getGrado() < 5).forEach(estudiante2 -> {
            promedios3.add(controlador.getPromedioCuatrimestral(estudiante2.getInscripcion()));
        });
        suma = promedios3.stream().map(BigDecimal::plus).reduce(BigDecimal.ZERO, BigDecimal::add);
        promedioAcumulativo = suma.divide(registro,8 ,RoundingMode.HALF_UP);
                    //System.out.println("Promedio:"+promedioAcumulativo);
                    cuatrimestre = new PdfPCell(new Paragraph("PRIMER", fontBold));
                    cuatrimestre.setColspan(3);
                    cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table3.addCell(cuatrimestre);
                     List<BigDecimal> listaPromedios1 = new ArrayList<>();
                    controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 1).forEach(dtoCargaAcademica -> {
                        BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor())
                                .setScale(2, RoundingMode.HALF_UP);
                        BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor())
                                .setScale(2, RoundingMode.HALF_UP);
                        BigDecimal promedioFinal2 = BigDecimal.ZERO;
                        PdfPCell materias2 = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                        materias2.setHorizontalAlignment(Element.ALIGN_LEFT);
                        PdfPCell promedios2;
                        if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                            promedioFinal2 = promedioFinal2.add(promedio);
                            promedios2 = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                        }else{
                            promedioFinal2 = promedioFinal2.add(nivelacion);
                            promedios2 = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                        }
                        listaPromedios1.add(promedioFinal2);
                        table3.addCell(materias2);
                        table3.addCell(promedios2);
                    });
                    materiastotal = (int) ejb.obtenerCargasAcademicas(estudiante1.getInscripcion()).getValor().stream().filter(grado -> grado.getGrupo().getGrado() == 1).count();
//                    System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.GeneracionKardex.generarPdf()"+materiastotal);
                    promedioCuatrimestral1 = listaPromedios1.stream().reduce(BigDecimal.ZERO, BigDecimal::add).plus()
                            .divide(new BigDecimal(materiastotal), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
                    texto = new PdfPCell(new Paragraph("Promedio", fontBold));
                    avg = new PdfPCell(new Paragraph(promedioAcumulativo.setScale(2, RoundingMode.HALF_UP).toString(), fontBold));
                    tablaPromedio2.addCell(texto);
                    tablaPromedio2.addCell(avg);
                    celda.addElement(table3);
                    celda2.addElement(tablaPromedio2);
                    break;
                    case 2:
                    if(estudiante.getInscripcionActiva().getInscripcion().getCalificacionList().contains(0) || estudiante.getInscripcionActiva().getInscripcion().getCalificacionList().isEmpty()) return;
                    cuatrimestre = new PdfPCell(new Paragraph("SEGUNDO", fontBold));
                    cuatrimestre.setColspan(3);
                    cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cuatrimestre);
                     List<BigDecimal> listaPromedios2 = new ArrayList<>();
                    controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 2).forEach(dtoCargaAcademica -> {
                        BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor())
                                .setScale(2, RoundingMode.HALF_UP);
                        if(promedio.compareTo(BigDecimal.ZERO) == 0) return;
                        BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor())
                                .setScale(2, RoundingMode.HALF_UP);
                        BigDecimal promedioFinal = BigDecimal.ZERO;
                        PdfPCell materias = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                        materias.setHorizontalAlignment(Element.ALIGN_LEFT);
                        PdfPCell promedios;
                        if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                            promedioFinal = promedioFinal.add(promedio);
                            promedios = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                        }else{
                            promedioFinal = promedioFinal.add(nivelacion);
                            promedios = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                        }
                        listaPromedios2.add(promedioFinal);
                        table.addCell(materias);
                        table.addCell(promedios);
                    });
                    materiastotal = (int) ejb.obtenerCargasAcademicas(estudiante1.getInscripcion()).getValor().stream().filter(grado -> grado.getGrupo().getGrado() == 2).count();
//                    System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.GeneracionKardex.generarPdf()"+materiastotal);
                    texto = new PdfPCell(new Paragraph("Promedio", fontBold));
                    avg = new PdfPCell(new Paragraph("", fontBold));
                    tablaPromedio.addCell(texto);
                    tablaPromedio.addCell(avg);
                    celda.addElement(table);
                    celda2.addElement(new Paragraph(""));
                    break;
                    case 3:
                    cuatrimestre = new PdfPCell(new Paragraph("TERCERO", fontBold));
                    cuatrimestre.setColspan(3);
                    cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table4.addCell(cuatrimestre);
                     List<BigDecimal> listaPromedios3 = new ArrayList<>();
                    controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 3).forEach(dtoCargaAcademica -> {
                        BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor())
                                .setScale(2, RoundingMode.HALF_UP);
                        BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor())
                                .setScale(2, RoundingMode.HALF_UP);
                        BigDecimal promedioFinal = BigDecimal.ZERO;
                        PdfPCell materias = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                        materias.setHorizontalAlignment(Element.ALIGN_LEFT);
                        PdfPCell promedios;
                        if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                            promedioFinal = promedioFinal.add(promedio);
                            promedios = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                        }else{
                            promedioFinal = promedioFinal.add(nivelacion);
                            promedios = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                        }
                        listaPromedios3.add(promedioFinal);
                        table4.addCell(materias);
                        table4.addCell(promedios);
                    });
                    materiastotal = (int) ejb.obtenerCargasAcademicas(estudiante1.getInscripcion()).getValor().stream().filter(grado -> grado.getGrupo().getGrado() == 3).count();
//                    System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.GeneracionKardex.generarPdf()"+materiastotal);
                    texto = new PdfPCell(new Paragraph("Promedio", fontBold));
                    avg = new PdfPCell(new Paragraph("", fontBold));
                    tablaPromedio3.addCell(texto);
                    tablaPromedio3.addCell(avg);
                    celda.addElement(table4);
                    celda2.addElement(new Paragraph(""));
                    break;
                    case 4:
                    cuatrimestre = new PdfPCell(new Paragraph("CUARTO", fontBold));
                    cuatrimestre.setColspan(3);
                    cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table5.addCell(cuatrimestre);
                     List<BigDecimal> listaPromedios5 = new ArrayList<>();
                    controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 4).forEach(dtoCargaAcademica -> {
                        BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor())
                                .setScale(2, RoundingMode.HALF_UP);
                        BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor())
                                .setScale(2, RoundingMode.HALF_UP);
                        BigDecimal promedioFinal = BigDecimal.ZERO;
                        PdfPCell materias = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                        materias.setHorizontalAlignment(Element.ALIGN_LEFT);
                        PdfPCell promedios;
                        if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                            promedioFinal = promedioFinal.add(promedio);
                            promedios = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                        }else{
                            promedioFinal = promedioFinal.add(nivelacion);
                            promedios = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                        }
                        listaPromedios5.add(promedioFinal);
                        table5.addCell(materias);
                        table5.addCell(promedios);
                    });
                    materiastotal = (int) ejb.obtenerCargasAcademicas(estudiante1.getInscripcion()).getValor().stream().filter(grado -> grado.getGrupo().getGrado() == 4).count();
//                    System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.GeneracionKardex.generarPdf()"+materiastotal);
                    texto = new PdfPCell(new Paragraph("Promedio", fontBold));
                    avg = new PdfPCell(new Paragraph("", fontBold));
                    tablaPromedio3.addCell(texto);
                    tablaPromedio3.addCell(avg);
                    celda.addElement(table5);
                    celda2.addElement(new Paragraph(""));
                    break;
            }
            tablaPrincipal.addCell(celda);
            tablaPrincipal.addCell(celda2);
        });

        texto3 = new Paragraph("ATENTAMENTE", fontBold);
        texto3.setAlignment(Element.ALIGN_CENTER);
        texto4 = new Paragraph("'TECNOLOGIA Y CALIDAD, BASE DEL DESARROLLO'", fontBold);
        texto4.setAlignment(Element.ALIGN_CENTER);
        texto4.setSpacingAfter(15);
        texto5 = new Paragraph("Ing. Juan Alberto Sánchez González", fontBolder);
        texto5.setSpacingBefore(70);
        texto5.setAlignment(Element.ALIGN_CENTER);
        texto6 = new Paragraph("Jefe del Departamento de Servicios Escolares", fontBolder);
        texto6.setAlignment(Element.ALIGN_CENTER);

        //Se agregan los elementos creados
        document.add(nombreUniversidad);
        document.add(departamento);
        document.add(logoSep);
        document.add(logoUT);
        document.add(textoPrincipal);
        document.add(parrafo);
        document.add(texto2);
        document.add(tablaPrincipal);
        document.add(texto3);
        document.add(texto4);
        document.add(texto5);
        document.add(texto6);
        //Se cierra el documento
        document.close();
        FacesContext.getCurrentInstance()
                .addMessage(null, new
                        FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Su documento se ha generado correctamente",
                        ""));
        File file = new File(base.concat(nombrePdf));

        try {
            InputStream input = new FileInputStream(file);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            setDownload(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
//            String ruta = "/sii2/media".concat(file.toURI().toString().split("archivos")[1]);
            //Ajax.oncomplete("descargar('" + ruta + "');");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Path path = Paths.get(base.concat(nombrePdf));
        //Files.delete(path);

        //Llama a aplicacion de PDF reader
    }
}
