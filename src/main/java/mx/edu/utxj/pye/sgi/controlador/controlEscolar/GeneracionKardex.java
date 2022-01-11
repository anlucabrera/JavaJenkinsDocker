package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Getter;
import lombok.Setter;
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
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
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
            generarPdfSeptimo(estudiante);
        }
        if(estudiante.getInscripcionActiva().getGrupo().getGrado() == 8){
            generarPdfOctavo(estudiante);
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
        //Image logoSep = Image.getInstance("C:\\archivos\\acta_final\\logoSep.png");     Image logoUT = Image.getInstance("C:\\archivos\\acta_final\\logoUT.jpg");
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
        //logoSep.scaleToFit(80,280);      logoSep.setAbsolutePosition(80f, 696f);    logoSep.setAlignment(Element.ALIGN_LEFT);
        nombreUniversidad = new Paragraph("UNIVERSIDAD TECNÓLOGICA DE XICOTEPEC DE JUÁREZ", fontBold);  nombreUniversidad.setAlignment(Element.ALIGN_RIGHT);  nombreUniversidad.setLeading(57,0);
        departamento = new Paragraph("DEPARTAMENTO DE SERVICIOS ESCOLARES", fontBold);                  departamento.setLeading(16,0);        departamento.setAlignment(Element.ALIGN_RIGHT);

        //logoUT.scaleToFit(65,100);        logoUT.setAbsolutePosition(180f, 692f);         logoUT.setAlignment(Element.ALIGN_RIGHT);

        textoPrincipal = new Paragraph("HISTORIAL ACADÉMICO", fontMateria1);   textoPrincipal.setAlignment(Element.ALIGN_CENTER);
        textoPrincipal.setLeading(30, 0);                                      textoPrincipal.setSpacingAfter(13);

        String carrera1 = ejb.obtenerProgramaEducativo(estudiante.getInscripcionActiva().getInscripcion().getCarrera()).getValor().getNombre();
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
                List<DtoCargaAcademica> cargas = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 1).collect(Collectors.toList());
                cargas.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));   avg = new PdfPCell(new Paragraph(promedioA.setScale(2, RoundingMode.HALF_DOWN).toString(), fontBold));
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
        //Image logoSep = Image.getInstance("C:\\archivos\\acta_final\\logoSep.png");     Image logoUT = Image.getInstance("C:\\archivos\\acta_final\\logoUT.jpg");
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
        //logoSep.scaleToFit(80,280);      logoSep.setAbsolutePosition(80f, 696f);    logoSep.setAlignment(Element.ALIGN_LEFT);
        nombreUniversidad = new Paragraph("UNIVERSIDAD TECNÓLOGICA DE XICOTEPEC DE JUÁREZ", fontBold);  nombreUniversidad.setAlignment(Element.ALIGN_RIGHT);  nombreUniversidad.setLeading(57,0);
        departamento = new Paragraph("DEPARTAMENTO DE SERVICIOS ESCOLARES", fontBold);                  departamento.setLeading(16,0);        departamento.setAlignment(Element.ALIGN_RIGHT);

        //logoUT.scaleToFit(65,100);        logoUT.setAbsolutePosition(180f, 692f);         logoUT.setAlignment(Element.ALIGN_RIGHT);

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
                        .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_DOWN);
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
                        .divide(new BigDecimal(contador), 8, RoundingMode.HALF_DOWN);
                cuatrimestre = new PdfPCell(new Paragraph("PRIMER", fontBold));      cuatrimestre.setColspan(3);       cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table3.addCell(cuatrimestre);
                List<DtoCargaAcademica> cargas = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 1).collect(Collectors.toList());
                cargas.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));   avg = new PdfPCell(new Paragraph(promedioA.setScale(2, RoundingMode.HALF_DOWN).toString(), fontBold));
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
                List<DtoCargaAcademica> cargas2 = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 2).collect(Collectors.toList());
                cargas2.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas2.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    if(promedio.compareTo(BigDecimal.ZERO) == 0) return;
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
                        .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_DOWN);
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
                        .divide(new BigDecimal(contador), 8, RoundingMode.HALF_DOWN);
                cuatrimestre = new PdfPCell(new Paragraph("PRIMER", fontBold));      cuatrimestre.setColspan(3);       cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table3.addCell(cuatrimestre);
                List<DtoCargaAcademica> cargas = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 1).collect(Collectors.toList());
                cargas.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));   avg = new PdfPCell(new Paragraph(promedioA.setScale(2, RoundingMode.HALF_DOWN).toString(), fontBold));
                tablaPromedio.addCell(texto);     tablaPromedio.addCell(avg);
                celda.addElement(table3);          celda2.addElement(tablaPromedio);
            }
            if(estudiante1.getGrupo().getGrado() == 2){
                Integer cargas = estudiante1.getGrupo().getCargaAcademicaList().size();
                Integer calificaciones = estudiante1.getInscripcion().getCalificacionPromedioList().size();
                if(!cargas.equals(calificaciones)) return;
                cuatrimestre = new PdfPCell(new Paragraph("SEGUNDO", fontBold));          cuatrimestre.setColspan(3);
                cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);            table.addCell(cuatrimestre);
                
                List<DtoCargaAcademica> cargas2 = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 2).collect(Collectors.toList());
                cargas2.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas2.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    if(promedio.compareTo(BigDecimal.ZERO) == 0) return;
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
                List<DtoCargaAcademica> cargas3 = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 3).collect(Collectors.toList());
                cargas3.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas3.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
        //logoSep.scaleToFit(80,280);      logoSep.setAbsolutePosition(80f, 696f);    logoSep.setAlignment(Element.ALIGN_LEFT);
        nombreUniversidad = new Paragraph("UNIVERSIDAD TECNÓLOGICA DE XICOTEPEC DE JUÁREZ", fontBold);  nombreUniversidad.setAlignment(Element.ALIGN_RIGHT);  nombreUniversidad.setLeading(57,0);
        departamento = new Paragraph("DEPARTAMENTO DE SERVICIOS ESCOLARES", fontBold);                  departamento.setLeading(16,0);        departamento.setAlignment(Element.ALIGN_RIGHT);

        //logoUT.scaleToFit(65,100);        logoUT.setAbsolutePosition(180f, 692f);         logoUT.setAlignment(Element.ALIGN_RIGHT);

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
                        .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_DOWN);
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
                        .divide(registro, 8, RoundingMode.HALF_DOWN);
                cuatrimestre = new PdfPCell(new Paragraph("PRIMER", fontBold));      cuatrimestre.setColspan(3);       cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table3.addCell(cuatrimestre);
                List<DtoCargaAcademica> cargas = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 1).collect(Collectors.toList());
                cargas.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));   avg = new PdfPCell(new Paragraph(promedioA.setScale(2, RoundingMode.HALF_DOWN).toString(), fontBold));
                tablaPromedio.addCell(texto);     tablaPromedio.addCell(avg);
                celda.addElement(table3);          celda2.addElement(tablaPromedio);
            }
            if(estudiante1.getGrupo().getGrado() == 2){
                cuatrimestre = new PdfPCell(new Paragraph("SEGUNDO", fontBold));          cuatrimestre.setColspan(3);
                cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);            table.addCell(cuatrimestre);
                
                List<DtoCargaAcademica> cargas = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 2).collect(Collectors.toList());
                cargas.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    if(promedio.compareTo(BigDecimal.ZERO) == 0) return;
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
                List<DtoCargaAcademica> cargas = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 3).collect(Collectors.toList());
                cargas.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
                List<DtoCargaAcademica> cargas4 = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 4).collect(Collectors.toList());
                cargas4.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas4.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
        //Image logoSep = Image.getInstance("C:\\archivos\\acta_final\\logoSep.png");     Image logoUT = Image.getInstance("C:\\archivos\\acta_final\\logoUT.jpg");
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
        //logoSep.scaleToFit(80,280);      logoSep.setAbsolutePosition(80f, 696f);    logoSep.setAlignment(Element.ALIGN_LEFT);
        nombreUniversidad = new Paragraph("UNIVERSIDAD TECNÓLOGICA DE XICOTEPEC DE JUÁREZ", fontBold);  nombreUniversidad.setAlignment(Element.ALIGN_RIGHT);  nombreUniversidad.setLeading(57,0);
        departamento = new Paragraph("DEPARTAMENTO DE SERVICIOS ESCOLARES", fontBold);                  departamento.setLeading(16,0);        departamento.setAlignment(Element.ALIGN_RIGHT);

        //logoUT.scaleToFit(65,100);        logoUT.setAbsolutePosition(180f, 692f);         logoUT.setAlignment(Element.ALIGN_RIGHT);

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
                        .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_DOWN);
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
                        .divide(new BigDecimal(contador), 8, RoundingMode.HALF_DOWN);
                cuatrimestre = new PdfPCell(new Paragraph("PRIMER", fontBold1));      cuatrimestre.setColspan(3);       cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table3.addCell(cuatrimestre);
                List<DtoCargaAcademica> cargas = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 1).collect(Collectors.toList());
                cargas.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));   avg = new PdfPCell(new Paragraph(promedioA.setScale(2, RoundingMode.HALF_DOWN).toString(), fontBold));
                tablaPromedio.addCell(texto);     tablaPromedio.addCell(avg);
                celda.addElement(table3);          celda2.addElement(tablaPromedio);
            }
            if(estudiante1.getGrupo().getGrado() == 2){
                cuatrimestre = new PdfPCell(new Paragraph("SEGUNDO", fontBold1));          cuatrimestre.setColspan(3);
                cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);                table.addCell(cuatrimestre);
                
                List<DtoCargaAcademica> cargas2 = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 2).collect(Collectors.toList());
                cargas2.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas2.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    if(promedio.compareTo(BigDecimal.ZERO) == 0) return;
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
                List<DtoCargaAcademica> cargas3 = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 3).collect(Collectors.toList());
                cargas3.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas3.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
                List<DtoCargaAcademica> cargas4 = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 4).collect(Collectors.toList());
                cargas4.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas4.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
                List<DtoCargaAcademica> cargas6 = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 5).collect(Collectors.toList());
                cargas6.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas6.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
        //Image logoSep = Image.getInstance("C:\\archivos\\acta_final\\logoSep.png");     Image logoUT = Image.getInstance("C:\\archivos\\acta_final\\logoUT.jpg");
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
        //logoSep.scaleToFit(80,280);      logoSep.setAbsolutePosition(80f, 696f);    logoSep.setAlignment(Element.ALIGN_LEFT);
        nombreUniversidad = new Paragraph("UNIVERSIDAD TECNÓLOGICA DE XICOTEPEC DE JUÁREZ", fontBold);  nombreUniversidad.setAlignment(Element.ALIGN_RIGHT);  nombreUniversidad.setLeading(57,0);
        departamento = new Paragraph("DEPARTAMENTO DE SERVICIOS ESCOLARES", fontBold);                  departamento.setLeading(16,0);        departamento.setAlignment(Element.ALIGN_RIGHT);

        //logoUT.scaleToFit(65,100);        logoUT.setAbsolutePosition(180f, 692f);         logoUT.setAlignment(Element.ALIGN_RIGHT);

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
                        .divide(new BigDecimal(5), 8, RoundingMode.HALF_DOWN);
                cuatrimestre = new PdfPCell(new Paragraph("PRIMER", fontBold1));      cuatrimestre.setColspan(3);       cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);
                table3.addCell(cuatrimestre);
                List<DtoCargaAcademica> cargas = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 1).collect(Collectors.toList());
                cargas.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));   avg = new PdfPCell(new Paragraph(promedioA.setScale(2, RoundingMode.HALF_DOWN).toString(), fontBold));
                tablaPromedio.addCell(texto);     tablaPromedio.addCell(avg);
                celda.addElement(table3);          celda2.addElement(tablaPromedio);
            }
            if(estudiante1.getGrupo().getGrado() == 2){
                cuatrimestre = new PdfPCell(new Paragraph("SEGUNDO", fontBold1));          cuatrimestre.setColspan(3);
                cuatrimestre.setHorizontalAlignment(Element.ALIGN_CENTER);                table.addCell(cuatrimestre);
                
                List<DtoCargaAcademica> cargas = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 2).collect(Collectors.toList());
                cargas.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    if(promedio.compareTo(BigDecimal.ZERO) == 0) return;
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
                List<DtoCargaAcademica> cargas = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 3).collect(Collectors.toList());
                cargas.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
                List<DtoCargaAcademica> cargas = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 4).collect(Collectors.toList());
                cargas.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
                List<DtoCargaAcademica> cargas = controlador.getCargasAcademicas(estudiante1.getInscripcion()).stream().filter(grado -> grado.getGrupo().getGrado() == 5).collect(Collectors.toList());
                cargas.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
                cargas.forEach(dtoCargaAcademica -> {
                    BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1.getInscripcion()).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                    BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1.getInscripcion()).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
                if(!estudiante1.getInscripcion().getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("1")) && !estudiante1.getInscripcion().getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("4")) ) return;
                ResultadoEJB<SeguimientoEstadiaEstudiante> resSEE = ejb.verificarEstadiaEstudiante(estudiante1.getInscripcion());
                SeguimientoEstadiaEstudiante see = resSEE.getValor();
                if(see.getPromedioAsesorInterno() < 8 && see.getPromedioAsesorExterno() < 8) return;
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
    
    public void generarPdfSeptimo(DtoEstudiante estudiante)throws IOException, DocumentException{
        Document document;

        document = new Document(PageSize.LETTER, 70, 70, 15, 15);
        Paragraph nombreUniversidad, departamento, textoPrincipal, texto1, texto2, texto3, texto4, texto5, texto6;

        String nombrePdf = "Kardex Calificaciones.pdf";
        //String ruta = ServicioArchivos.carpetaRaiz.concat(File.separator).concat("acta_final").concat(File.separator).concat(nombrePdf);
        //Image logoSep = Image.getInstance("C:\\archivos\\acta_final\\logoSep.png");     Image logoUT = Image.getInstance("C:\\archivos\\acta_final\\logoUT.jpg");
        Font fontBold = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);              Font fontNormal = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
        Font fontMateria2 = new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL);        Font fontMateria1 = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        Font fontBolder = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);           String base = ServicioArchivos.carpetaRaiz.concat("acta_final").concat(File.separator);
        Font fontBold1 = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);

        ServicioArchivos.addCarpetaRelativa(base);  ServicioArchivos.eliminarArchivo(base.concat(nombrePdf)); OutputStream out = new FileOutputStream(new File(base.concat(nombrePdf)));

        PdfWriter.getInstance(document, out);    PdfWriter.getInstance(document, new FileOutputStream("C:\\archivos\\acta_final\\Kardex Calificaciones_"+estudiante.getInscripcionActiva().getInscripcion().getMatricula()+".pdf"));
        //PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\"+usuario+"\\Downloads\\acta_final_"+grupo.getLiteral()+"_"+grupo.getTutor()+".pdf"));
        PdfPTable tablaPrincipal = new PdfPTable(2);    PdfPTable table = new PdfPTable(2);           PdfPTable table3 = new PdfPTable(2);
        PdfPTable tablaPromedio = new PdfPTable(2);     PdfPTable table4 = new PdfPTable(2);          PdfPTable table5 = new PdfPTable(2);
        PdfPTable table6 = new PdfPTable(2);            PdfPTable table7 = new PdfPTable(2);          PdfPTable table8 = new PdfPTable(2);
        PdfPTable table2 = new PdfPTable(2);            PdfPTable table1 = new PdfPTable(2);
        
        PdfPCell c1 = new PdfPCell(), c2 = new PdfPCell(), c3 = new PdfPCell(), c4 = new PdfPCell(), c5 = new PdfPCell(), c6 = new PdfPCell(), c7 = new PdfPCell(),
                c8 = new PdfPCell(), c9 = new PdfPCell(), c10 = new PdfPCell(), c11 = new PdfPCell(), c12 = new PdfPCell(); 
        PdfPCell cuatri1, cuatri2, cuatri3, cuatri4, cuatri5, cuatri6, cuatri7;
        PdfPCell texto, avg;
        //Se abre el pdf para iniciar a darle formato
        document.open();
        //Se coloca los elementos necesarios que contendra el pdf
        //logoSep.scaleToFit(80,280);      logoSep.setAbsolutePosition(80f, 696f);    logoSep.setAlignment(Element.ALIGN_LEFT);
        nombreUniversidad = new Paragraph("UNIVERSIDAD TECNÓLOGICA DE XICOTEPEC DE JUÁREZ", fontBold);  nombreUniversidad.setAlignment(Element.ALIGN_RIGHT);  nombreUniversidad.setLeading(57,0);
        departamento = new Paragraph("DEPARTAMENTO DE SERVICIOS ESCOLARES", fontBold);                  departamento.setLeading(16,0);        departamento.setAlignment(Element.ALIGN_RIGHT);

        //logoUT.scaleToFit(65,100);        logoUT.setAbsolutePosition(180f, 692f);         logoUT.setAlignment(Element.ALIGN_RIGHT);

        textoPrincipal = new Paragraph("HISTORIAL ACADÉMICO", fontMateria1);   textoPrincipal.setAlignment(Element.ALIGN_CENTER);
        textoPrincipal.setLeading(30, 0);                                      textoPrincipal.setSpacingAfter(13);
        
        
        String carreraTSU = ejb.obtenerProgramaEducativo(estudiante.getInscripciones().stream().filter(x -> x.getGrupo().getGrado() == 6).findFirst().get().getInscripcion().getCarrera()).getValor().getNombre();
        String carreraING = ejb.obtenerProgramaEducativo(estudiante.getInscripcionActiva().getInscripcion().getCarrera()).getValor().getNombre();
        texto1 = new Paragraph(9);   texto1.setSpacingAfter(7);   texto2 = new Paragraph(9);


        texto1.setAlignment(Element.ALIGN_JUSTIFIED);   texto1.add(new Paragraph("La ", fontNormal));     texto1.add(new Paragraph("Universidad Tecnológica de Xicotepec de Juárez ", fontBolder));
        texto1.add(new Paragraph("hace constar que, según documentos existentes en el archivo escolar el (la) C. ", fontNormal));
        texto1.add(new Paragraph(
                estudiante.getAspirante().getIdPersona().getApellidoPaterno().concat(" ")
                        .concat(estudiante.getAspirante().getIdPersona().getApellidoMaterno()).concat(" ")
                        .concat(estudiante.getAspirante().getIdPersona().getNombre()), fontBolder));
        texto1.add(new Paragraph("con matricula ", fontNormal));     texto1.add(new Paragraph(String.valueOf(estudiante.getInscripcionActiva().getInscripcion().getMatricula()), fontBolder));
        texto1.add(new Paragraph(", cursó las siguientes asignaturas correspondientes a la carrera de: ", fontNormal));   
        texto1.add(new Paragraph("Técnico Superior Universtiario en ".concat(carreraTSU), fontBolder));
        texto1.add(new Paragraph(" y continuación a ", fontNormal));
        texto1.add(new Paragraph(carreraING, fontBolder));
        texto1.add(new Paragraph(" y actualmente se encuentra cursando el ", fontNormal));     texto1.add(new Paragraph(String.valueOf(estudiante.getInscripciones().get(0).getGrupo().getGrado()).concat(" "), fontBolder));
        texto1.add(new Paragraph("cuatrimestre.", fontNormal));        Phrase parrafo = new Phrase(texto1);
        texto2.add(new Paragraph("A petición del interesado y para los fines legales, que a él (ella) convengan, se expide la presente en Xicotepec de Juárez, Puebla, el "
                    .concat(sdf.format(new Date())).concat("."), fontNormal));
        texto2.setSpacingAfter(2);
        
        tablaPrincipal.setWidthPercentage(100);  tablaPrincipal.setHorizontalAlignment(Element.ALIGN_LEFT);  tablaPrincipal.setWidths(new float[] {50f, 50f});
        table.setWidthPercentage(100);           table.setHorizontalAlignment(Element.ALIGN_LEFT);           table.setWidths(new float[] {43f, 7f});
        table1.setWidthPercentage(100);          table1.setHorizontalAlignment(Element.ALIGN_LEFT);           table1.setWidths(new float[] {43f, 7f});
        table2.setWidthPercentage(100);          table2.setHorizontalAlignment(Element.ALIGN_LEFT);           table2.setWidths(new float[] {43f, 7f});
        table3.setWidthPercentage(100);          table3.setHorizontalAlignment(Element.ALIGN_LEFT);          table3.setWidths(new float[] {43f, 7f});
        table4.setWidthPercentage(100);          table4.setHorizontalAlignment(Element.ALIGN_LEFT);          table4.setWidths(new float[] {43f, 7f});
        table5.setWidthPercentage(100);          table5.setHorizontalAlignment(Element.ALIGN_LEFT);          table5.setWidths(new float[] {43f, 7f});
        table6.setWidthPercentage(100);          table6.setHorizontalAlignment(Element.ALIGN_LEFT);          table6.setWidths(new float[] {43f, 7f});
        table7.setWidthPercentage(100);          table7.setHorizontalAlignment(Element.ALIGN_LEFT);          table7.setWidths(new float[] {43f, 7f});
        table8.setWidthPercentage(100);          table8.setHorizontalAlignment(Element.ALIGN_LEFT);          table8.setWidths(new float[] {43f, 7f});
        tablaPromedio.setWidthPercentage(100);  tablaPromedio.setHorizontalAlignment(Element.ALIGN_LEFT);  tablaPromedio.setWidths(new float[] {43f, 7f});
        
        Estudiante estudiante1 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 1).findFirst().get().getInscripcion();
        Estudiante estudiante2 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 2).findFirst().get().getInscripcion();
        Estudiante estudiante3 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 3).findFirst().get().getInscripcion();
        Estudiante estudiante4 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 4).findFirst().get().getInscripcion();
        Estudiante estudiante5 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 5).findFirst().get().getInscripcion();
        Estudiante estudiante6 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 6).findFirst().get().getInscripcion();
        Estudiante estudiante7 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 7).findFirst().get().getInscripcion();
        
        
         List<DtoCargaAcademica> lista1 = controlador.getCargasAcademicas(estudiante1);
        lista1.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
        List<DtoCargaAcademica> lista2 = controlador.getCargasAcademicas(estudiante2);
        lista2.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
        List<DtoCargaAcademica> lista3 = controlador.getCargasAcademicas(estudiante3);
        lista3.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
        List<DtoCargaAcademica> lista4 = controlador.getCargasAcademicas(estudiante4);
        lista4.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
        List<DtoCargaAcademica> lista5 = controlador.getCargasAcademicas(estudiante5);
        lista5.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
        List<DtoCargaAcademica> lista7 = controlador.getCargasAcademicas(estudiante7);
        lista7.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
        
        cuatri1 = new PdfPCell(new Paragraph("PRIMER", fontBold1));      cuatri1.setColspan(3);       cuatri1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1.addCell(cuatri1);
        lista1.forEach(dtoCargaAcademica -> {
            BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            PdfPCell materias2 = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
            materias2.setHorizontalAlignment(Element.ALIGN_LEFT);   PdfPCell promedios2;
                if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                    promedios2 = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                }else{
                    promedios2 = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                }
            table1.addCell(materias2);
            table1.addCell(promedios2);
        });
        c1.addElement(table1);
        
        Integer cargas = (int)estudiante7.getGrupo().getCargaAcademicaList().stream().mapToInt(CargaAcademica::getCarga).count();
        Integer calificaciones = (int)estudiante7.getCalificacionPromedioList().stream().mapToInt((cp) -> cp.getCargaAcademica().getCarga()).count();
        BigDecimal promedioC = controlador.getPromedioCuatrimestral(estudiante7);
        if(!cargas.equals(calificaciones) || promedioC.compareTo(new BigDecimal(8)) == -1) {
            Integer contador;
                BigDecimal p = estudiante.getInscripciones().stream().filter(e -> e.getInscripcion().getGrupo().getGrado() == 7)
                        .map(dtoInscripcion -> controlador.getPromedioCuatrimestral(dtoInscripcion.getInscripcion())).map(BigDecimal::plus)
                        .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_DOWN);
                if(p.compareTo(new BigDecimal(8)) == -1){
                    contador = 5;
                    //System.out.println("Es "+ contador);
                }else{
                    contador = 6;
                    //System.out.println("Es "+ contador);
                }
                BigDecimal promedioA = estudiante.getInscripciones().stream().filter(x -> x.getGrupo().getGrado() != 6)
                        .map(dtoInscripcion -> controlador.getPromedioCuatrimestral(dtoInscripcion.getInscripcion())).collect(Collectors.toList())
                        .stream().filter(pr -> pr.compareTo(new BigDecimal(7.9999)) == 1).map(BigDecimal::plus).reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(new BigDecimal(contador), 8, RoundingMode.HALF_DOWN);
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));   avg = new PdfPCell(new Paragraph(promedioA.setScale(2, RoundingMode.HALF_DOWN).toString(), fontBold));
                tablaPromedio.addCell(texto);     tablaPromedio.addCell(avg);
                c2.addElement(tablaPromedio);
        }else{
            cuatri7 = new PdfPCell(new Paragraph("SEPTIMO", fontBold1));      cuatri7.setColspan(3);       cuatri7.setHorizontalAlignment(Element.ALIGN_CENTER);
            table7.addCell(cuatri7);
            lista7.forEach(dtoCargaAcademica -> {
                BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante7).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante7).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                PdfPCell materias2 = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                materias2.setHorizontalAlignment(Element.ALIGN_LEFT);   PdfPCell promedios2;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios2 = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios2 = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                table7.addCell(materias2);
                table7.addCell(promedios2);
            });
            c2.addElement(table7);
        }
        
        cuatri2 = new PdfPCell(new Paragraph("SEGUNDO", fontBold1));      cuatri2.setColspan(3);       cuatri2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2.addCell(cuatri2);
        lista2.forEach(dtoCargaAcademica -> {
            BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante2).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante2).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            PdfPCell materias2 = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
            materias2.setHorizontalAlignment(Element.ALIGN_LEFT);   PdfPCell promedios2;
                if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                    promedios2 = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                }else{
                    promedios2 = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                }
            table2.addCell(materias2);
            table2.addCell(promedios2);
        });
        c3.addElement(table2);
        if(!cargas.equals(calificaciones) || promedioC.compareTo(new BigDecimal(8)) == -1) {
            c4.addElement(new Paragraph(""));
        }else{
            Integer contador;
                BigDecimal p = estudiante.getInscripciones().stream().filter(e -> e.getInscripcion().getGrupo().getGrado() == 7)
                        .map(dtoInscripcion -> controlador.getPromedioCuatrimestral(dtoInscripcion.getInscripcion())).map(BigDecimal::plus)
                        .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_DOWN);
                if(p.compareTo(new BigDecimal(8)) == -1){
                    contador = 5;
                    //System.out.println("Es "+ contador);
                }else{
                    contador = 6;
                    //System.out.println("Es "+ contador);
                }
                BigDecimal promedioA = estudiante.getInscripciones().stream().filter(x -> x.getGrupo().getGrado() != 6)
                        .map(dtoInscripcion -> controlador.getPromedioCuatrimestral(dtoInscripcion.getInscripcion())).collect(Collectors.toList())
                        .stream().filter(pr -> pr.compareTo(new BigDecimal(7.9999)) == 1).map(BigDecimal::plus).reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(new BigDecimal(contador), 8, RoundingMode.HALF_DOWN);
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));   avg = new PdfPCell(new Paragraph(promedioA.setScale(2, RoundingMode.HALF_DOWN).toString(), fontBold));
                tablaPromedio.addCell(texto);     tablaPromedio.addCell(avg);
                c4.addElement(tablaPromedio);
        }
        
        cuatri3 = new PdfPCell(new Paragraph("TERCERO", fontBold1));      cuatri3.setColspan(3);       cuatri3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3.addCell(cuatri3);
        lista3.forEach(dtoCargaAcademica -> {
            BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante3).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante3).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
        c5.addElement(table3);
        c6.addElement(new Paragraph(""));
        
        cuatri4 = new PdfPCell(new Paragraph("CUARTO", fontBold1));      cuatri4.setColspan(3);       cuatri4.setHorizontalAlignment(Element.ALIGN_CENTER);
        table4.addCell(cuatri4);
        lista4.forEach(dtoCargaAcademica -> {
            BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante4).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante4).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            PdfPCell materias2 = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
            materias2.setHorizontalAlignment(Element.ALIGN_LEFT);   PdfPCell promedios2;
                if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                    promedios2 = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                }else{
                    promedios2 = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                }
            table4.addCell(materias2);
            table4.addCell(promedios2);
        });
        c7.addElement(table4);
        c8.addElement(new Paragraph(""));
        
        cuatri5 = new PdfPCell(new Paragraph("QUINTO", fontBold1));      cuatri5.setColspan(3);       cuatri5.setHorizontalAlignment(Element.ALIGN_CENTER);
        table5.addCell(cuatri5);
        lista5.forEach(dtoCargaAcademica -> {
            BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante5).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante5).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            PdfPCell materias2 = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
            materias2.setHorizontalAlignment(Element.ALIGN_LEFT);   PdfPCell promedios2;
                if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                    promedios2 = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                }else{
                    promedios2 = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                }
            table5.addCell(materias2);
            table5.addCell(promedios2);
        });
        c9.addElement(table5);
        c10.addElement(new Paragraph(""));
        
         cuatri6 = new PdfPCell(new Paragraph("SEXTO", fontBold1));      cuatri6.setColspan(3);       cuatri6.setHorizontalAlignment(Element.ALIGN_CENTER);
        table6.addCell(cuatri6);
        PdfPCell descEstadia = new PdfPCell(new Paragraph("Estadía en el Sector Productivo", fontMateria2)); descEstadia.setHorizontalAlignment(Element.ALIGN_LEFT);
        PdfPCell promEstadia = new PdfPCell(new Paragraph("Acreditado", fontMateria2));
        table6.addCell(descEstadia);
        table6.addCell(promEstadia);
        c11.addElement(table6);
        c12.addElement(new Paragraph(""));
        
        tablaPrincipal.addCell(c1);
        tablaPrincipal.addCell(c2);
        tablaPrincipal.addCell(c3);
        tablaPrincipal.addCell(c4);
        tablaPrincipal.addCell(c5);
        tablaPrincipal.addCell(c6);
        tablaPrincipal.addCell(c7);
        tablaPrincipal.addCell(c8);
        tablaPrincipal.addCell(c9);
        tablaPrincipal.addCell(c10);
        tablaPrincipal.addCell(c11);
        tablaPrincipal.addCell(c12);
        
        texto3 = new Paragraph("ATENTAMENTE", fontBold1);                                       texto3.setAlignment(Element.ALIGN_CENTER);
        texto4 = new Paragraph("'TECNOLOGIA Y CALIDAD, BASE DEL DESARROLLO'", fontBold1);       texto4.setAlignment(Element.ALIGN_CENTER);       texto4.setLeading(7, 0);
        texto5 = new Paragraph("Ing. Juan Alberto Sánchez González", fontBolder);              texto5.setSpacingBefore(50);                     texto5.setAlignment(Element.ALIGN_CENTER);
        texto6 = new Paragraph("Jefe del Departamento de Servicios Escolares", fontBolder);    texto6.setAlignment(Element.ALIGN_CENTER);

        //Se agregan los elementos creados
        document.add(nombreUniversidad);
        document.add(departamento);
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
    
    public void generarPdfOctavo(DtoEstudiante estudiante)throws IOException, DocumentException{
        Document document;

        document = new Document(PageSize.LETTER, 70, 70, 15, 15);
        Paragraph nombreUniversidad, departamento, textoPrincipal, texto1, texto2, texto3, texto4, texto5, texto6;

        String nombrePdf = "Kardex Calificaciones.pdf";
        //String ruta = ServicioArchivos.carpetaRaiz.concat(File.separator).concat("acta_final").concat(File.separator).concat(nombrePdf);
        //Image logoSep = Image.getInstance("C:\\archivos\\acta_final\\logoSep.png");     Image logoUT = Image.getInstance("C:\\archivos\\acta_final\\logoUT.jpg");
        Font fontBold = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);              Font fontNormal = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
        Font fontMateria2 = new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL);        Font fontMateria1 = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        Font fontBolder = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);           String base = ServicioArchivos.carpetaRaiz.concat("acta_final").concat(File.separator);
        Font fontBold1 = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);

        ServicioArchivos.addCarpetaRelativa(base);  ServicioArchivos.eliminarArchivo(base.concat(nombrePdf)); OutputStream out = new FileOutputStream(new File(base.concat(nombrePdf)));

        PdfWriter.getInstance(document, out);    PdfWriter.getInstance(document, new FileOutputStream("C:\\archivos\\acta_final\\Kardex Calificaciones_"+estudiante.getInscripcionActiva().getInscripcion().getMatricula()+".pdf"));
        //PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\"+usuario+"\\Downloads\\acta_final_"+grupo.getLiteral()+"_"+grupo.getTutor()+".pdf"));
        PdfPTable tablaPrincipal = new PdfPTable(2);    PdfPTable table = new PdfPTable(2);           PdfPTable table3 = new PdfPTable(2);
        PdfPTable tablaPromedio = new PdfPTable(2);     PdfPTable table4 = new PdfPTable(2);          PdfPTable table5 = new PdfPTable(2);
        PdfPTable table6 = new PdfPTable(2);            PdfPTable table7 = new PdfPTable(2);          PdfPTable table8 = new PdfPTable(2);
        PdfPTable table2 = new PdfPTable(2);            PdfPTable table1 = new PdfPTable(2);
        
        PdfPCell c1 = new PdfPCell(), c2 = new PdfPCell(), c3 = new PdfPCell(), c4 = new PdfPCell(), c5 = new PdfPCell(), c6 = new PdfPCell(), c7 = new PdfPCell(),
                c8 = new PdfPCell(), c9 = new PdfPCell(), c10 = new PdfPCell(), c11 = new PdfPCell(), c12 = new PdfPCell(); 
        PdfPCell cuatri1, cuatri2, cuatri3, cuatri4, cuatri5, cuatri6, cuatri7, cuatri8;
        PdfPCell texto, avg;
        //Se abre el pdf para iniciar a darle formato
        document.open();
        //Se coloca los elementos necesarios que contendra el pdf
        //logoSep.scaleToFit(80,280);      logoSep.setAbsolutePosition(80f, 696f);    logoSep.setAlignment(Element.ALIGN_LEFT);
        nombreUniversidad = new Paragraph("UNIVERSIDAD TECNÓLOGICA DE XICOTEPEC DE JUÁREZ", fontBold);  nombreUniversidad.setAlignment(Element.ALIGN_RIGHT);  nombreUniversidad.setLeading(57,0);
        departamento = new Paragraph("DEPARTAMENTO DE SERVICIOS ESCOLARES", fontBold);                  departamento.setLeading(16,0);        departamento.setAlignment(Element.ALIGN_RIGHT);

        //logoUT.scaleToFit(65,100);        logoUT.setAbsolutePosition(180f, 692f);         logoUT.setAlignment(Element.ALIGN_RIGHT);

        textoPrincipal = new Paragraph("HISTORIAL ACADÉMICO", fontMateria1);   textoPrincipal.setAlignment(Element.ALIGN_CENTER);
        textoPrincipal.setLeading(30, 0);                                      textoPrincipal.setSpacingAfter(13);
        
        
        String carreraTSU = ejb.obtenerProgramaEducativo(estudiante.getInscripciones().stream().filter(x -> x.getGrupo().getGrado() == 6).findFirst().get().getInscripcion().getCarrera()).getValor().getNombre();
        String carreraING = ejb.obtenerProgramaEducativo(estudiante.getInscripcionActiva().getInscripcion().getCarrera()).getValor().getNombre();
        texto1 = new Paragraph(9);   texto1.setSpacingAfter(7);   texto2 = new Paragraph(9);


        texto1.setAlignment(Element.ALIGN_JUSTIFIED);   texto1.add(new Paragraph("La ", fontNormal));     texto1.add(new Paragraph("Universidad Tecnológica de Xicotepec de Juárez ", fontBolder));
        texto1.add(new Paragraph("hace constar que, según documentos existentes en el archivo escolar el (la) C. ", fontNormal));
        texto1.add(new Paragraph(
                estudiante.getAspirante().getIdPersona().getApellidoPaterno().concat(" ")
                        .concat(estudiante.getAspirante().getIdPersona().getApellidoMaterno()).concat(" ")
                        .concat(estudiante.getAspirante().getIdPersona().getNombre()), fontBolder));
        texto1.add(new Paragraph("con matricula ", fontNormal));     texto1.add(new Paragraph(String.valueOf(estudiante.getInscripcionActiva().getInscripcion().getMatricula()), fontBolder));
        texto1.add(new Paragraph(", cursó las siguientes asignaturas correspondientes a la carrera de: ", fontNormal));   
        texto1.add(new Paragraph("Técnico Superior Universtiario en ".concat(carreraTSU), fontBolder));
        texto1.add(new Paragraph(" y continuación a ", fontNormal));
        texto1.add(new Paragraph(carreraING, fontBolder));
        texto1.add(new Paragraph(" y actualmente se encuentra cursando el ", fontNormal));     texto1.add(new Paragraph(String.valueOf(estudiante.getInscripciones().get(0).getGrupo().getGrado()).concat(" "), fontBolder));
        texto1.add(new Paragraph("cuatrimestre.", fontNormal));        Phrase parrafo = new Phrase(texto1);
        texto2.add(new Paragraph("A petición del interesado y para los fines legales, que a él (ella) convengan, se expide la presente en Xicotepec de Juárez, Puebla, el "
                    .concat(sdf.format(new Date())).concat("."), fontNormal));
        texto2.setSpacingAfter(2);
        
        tablaPrincipal.setWidthPercentage(100);  tablaPrincipal.setHorizontalAlignment(Element.ALIGN_LEFT);  tablaPrincipal.setWidths(new float[] {50f, 50f});
        table.setWidthPercentage(100);           table.setHorizontalAlignment(Element.ALIGN_LEFT);           table.setWidths(new float[] {43f, 7f});
        table1.setWidthPercentage(100);          table1.setHorizontalAlignment(Element.ALIGN_LEFT);           table1.setWidths(new float[] {43f, 7f});
        table2.setWidthPercentage(100);          table2.setHorizontalAlignment(Element.ALIGN_LEFT);           table2.setWidths(new float[] {43f, 7f});
        table3.setWidthPercentage(100);          table3.setHorizontalAlignment(Element.ALIGN_LEFT);          table3.setWidths(new float[] {43f, 7f});
        table4.setWidthPercentage(100);          table4.setHorizontalAlignment(Element.ALIGN_LEFT);          table4.setWidths(new float[] {43f, 7f});
        table5.setWidthPercentage(100);          table5.setHorizontalAlignment(Element.ALIGN_LEFT);          table5.setWidths(new float[] {43f, 7f});
        table6.setWidthPercentage(100);          table6.setHorizontalAlignment(Element.ALIGN_LEFT);          table6.setWidths(new float[] {43f, 7f});
        table7.setWidthPercentage(100);          table7.setHorizontalAlignment(Element.ALIGN_LEFT);          table7.setWidths(new float[] {43f, 7f});
        table8.setWidthPercentage(100);          table8.setHorizontalAlignment(Element.ALIGN_LEFT);          table8.setWidths(new float[] {43f, 7f});
        tablaPromedio.setWidthPercentage(100);  tablaPromedio.setHorizontalAlignment(Element.ALIGN_LEFT);  tablaPromedio.setWidths(new float[] {43f, 7f});
        
        Estudiante estudiante1 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 1).findFirst().get().getInscripcion();
        Estudiante estudiante2 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 2).findFirst().get().getInscripcion();
        Estudiante estudiante3 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 3).findFirst().get().getInscripcion();
        Estudiante estudiante4 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 4).findFirst().get().getInscripcion();
        Estudiante estudiante5 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 5).findFirst().get().getInscripcion();
        Estudiante estudiante6 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 6).findFirst().get().getInscripcion();
        Estudiante estudiante7 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 7).findFirst().get().getInscripcion();
        Estudiante estudiante8 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 8).findFirst().get().getInscripcion();
        
        
         List<DtoCargaAcademica> lista1 = controlador.getCargasAcademicas(estudiante1);
        lista1.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
        List<DtoCargaAcademica> lista2 = controlador.getCargasAcademicas(estudiante2);
        lista2.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
        List<DtoCargaAcademica> lista3 = controlador.getCargasAcademicas(estudiante3);
        lista3.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
        List<DtoCargaAcademica> lista4 = controlador.getCargasAcademicas(estudiante4);
        lista4.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
        List<DtoCargaAcademica> lista5 = controlador.getCargasAcademicas(estudiante5);
        lista5.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
        List<DtoCargaAcademica> lista7 = controlador.getCargasAcademicas(estudiante7);
        lista7.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
        List<DtoCargaAcademica> lista8 = controlador.getCargasAcademicas(estudiante8);
        lista8.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
        
        cuatri1 = new PdfPCell(new Paragraph("PRIMER", fontBold1));      cuatri1.setColspan(3);       cuatri1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1.addCell(cuatri1);
        lista1.forEach(dtoCargaAcademica -> {
            BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante1).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante1).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            PdfPCell materias2 = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
            materias2.setHorizontalAlignment(Element.ALIGN_LEFT);   PdfPCell promedios2;
                if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                    promedios2 = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                }else{
                    promedios2 = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                }
            table1.addCell(materias2);
            table1.addCell(promedios2);
        });
        c1.addElement(table1);
        cuatri7 = new PdfPCell(new Paragraph("SEPTIMO", fontBold1));      cuatri7.setColspan(3);       cuatri7.setHorizontalAlignment(Element.ALIGN_CENTER);
        table7.addCell(cuatri7);
        lista7.forEach(dtoCargaAcademica -> {
            BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante7).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante7).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            System.out.println("Cargas academicas" + dtoCargaAcademica.getMateria().getNombre());
            PdfPCell materias2 = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
            materias2.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell promedios2;
            if (nivelacion.compareTo(BigDecimal.ZERO) == 0) {
                promedios2 = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
            } else {
                promedios2 = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
            }
            table7.addCell(materias2);
            table7.addCell(promedios2);
        });
        c2.addElement(table7);
        
            Integer cargas = (int)estudiante8.getGrupo().getCargaAcademicaList().stream().mapToInt(CargaAcademica::getCarga).count();
        Integer calificaciones = (int)estudiante8.getCalificacionPromedioList().stream().mapToInt((cp) -> cp.getCargaAcademica().getCarga()).count();
        BigDecimal promedioC = controlador.getPromedioCuatrimestral(estudiante8);
        
        cuatri2 = new PdfPCell(new Paragraph("SEGUNDO", fontBold1));      cuatri2.setColspan(3);       cuatri2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2.addCell(cuatri2);
        lista2.forEach(dtoCargaAcademica -> {
            BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante2).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante2).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            PdfPCell materias2 = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
            materias2.setHorizontalAlignment(Element.ALIGN_LEFT);   PdfPCell promedios2;
                if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                    promedios2 = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                }else{
                    promedios2 = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                }
            table2.addCell(materias2);
            table2.addCell(promedios2);
        });
        c3.addElement(table2);
        
        if(!cargas.equals(calificaciones) || promedioC.compareTo(new BigDecimal(8)) == -1) {
            Integer contador;
                BigDecimal p = estudiante.getInscripciones().stream().filter(e -> e.getInscripcion().getGrupo().getGrado() == 8)
                        .map(dtoInscripcion -> controlador.getPromedioCuatrimestral(dtoInscripcion.getInscripcion())).map(BigDecimal::plus)
                        .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_DOWN);
                if(p.compareTo(new BigDecimal(8)) == -1){
                    contador = 6;
                    //System.out.println("Es "+ contador);
                }else{
                    contador = 7;
                    //System.out.println("Es "+ contador);
                }
                BigDecimal promedioA = estudiante.getInscripciones().stream().filter(x -> x.getGrupo().getGrado() != 6)
                        .map(dtoInscripcion -> controlador.getPromedioCuatrimestral(dtoInscripcion.getInscripcion())).collect(Collectors.toList())
                        .stream().filter(pr -> pr.compareTo(new BigDecimal(7.9999)) == 1).map(BigDecimal::plus).reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(new BigDecimal(contador), 8, RoundingMode.HALF_DOWN);
                texto = new PdfPCell(new Paragraph("Promedio", fontBold));   avg = new PdfPCell(new Paragraph(promedioA.setScale(2, RoundingMode.HALF_DOWN).toString(), fontBold));
                tablaPromedio.addCell(texto);     tablaPromedio.addCell(avg);
                c4.addElement(tablaPromedio);
        }else{
            cuatri8 = new PdfPCell(new Paragraph("OCTAVO", fontBold1));      cuatri8.setColspan(3);       cuatri8.setHorizontalAlignment(Element.ALIGN_CENTER);
            table8.addCell(cuatri8);
            lista8.forEach(dtoCargaAcademica -> {
                BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante8).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante8).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
                PdfPCell materias2 = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
                materias2.setHorizontalAlignment(Element.ALIGN_LEFT);   PdfPCell promedios2;
                    if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                        promedios2 = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                    }else{
                        promedios2 = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                    }
                table8.addCell(materias2);
                table8.addCell(promedios2);
            });
            c4.addElement(table8);
        }
        
        cuatri3 = new PdfPCell(new Paragraph("TERCERO", fontBold1));      cuatri3.setColspan(3);       cuatri3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3.addCell(cuatri3);
        lista3.forEach(dtoCargaAcademica -> {
            BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante3).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante3).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
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
        c5.addElement(table3);
        c6.addElement(new Paragraph(""));
        
        cuatri4 = new PdfPCell(new Paragraph("CUARTO", fontBold1));      cuatri4.setColspan(3);       cuatri4.setHorizontalAlignment(Element.ALIGN_CENTER);
        table4.addCell(cuatri4);
        lista4.forEach(dtoCargaAcademica -> {
            BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante4).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante4).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            PdfPCell materias2 = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
            materias2.setHorizontalAlignment(Element.ALIGN_LEFT);   PdfPCell promedios2;
                if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                    promedios2 = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                }else{
                    promedios2 = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                }
            table4.addCell(materias2);
            table4.addCell(promedios2);
        });
        c7.addElement(table4);
        c8.addElement(new Paragraph(""));
        
        cuatri5 = new PdfPCell(new Paragraph("QUINTO", fontBold1));      cuatri5.setColspan(3);       cuatri5.setHorizontalAlignment(Element.ALIGN_CENTER);
        table5.addCell(cuatri5);
        lista5.forEach(dtoCargaAcademica -> {
            BigDecimal promedio = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), estudiante5).getValor().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            BigDecimal nivelacion = new BigDecimal(controlador.getNivelacion(dtoCargaAcademica, estudiante5).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_DOWN);
            PdfPCell materias2 = new PdfPCell(new Paragraph(dtoCargaAcademica.getMateria().getNombre(), fontMateria2));
            materias2.setHorizontalAlignment(Element.ALIGN_LEFT);   PdfPCell promedios2;
                if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
                    promedios2 = new PdfPCell(new Paragraph(promedio.toString(), fontMateria2));
                }else{
                    promedios2 = new PdfPCell(new Paragraph(nivelacion.toString(), fontMateria2));
                }
            table5.addCell(materias2);
            table5.addCell(promedios2);
        });
        c9.addElement(table5);
        c10.addElement(new Paragraph(""));
        
         cuatri6 = new PdfPCell(new Paragraph("SEXTO", fontBold1));      cuatri6.setColspan(3);       cuatri6.setHorizontalAlignment(Element.ALIGN_CENTER);
        table6.addCell(cuatri6);
        PdfPCell descEstadia = new PdfPCell(new Paragraph("Estadía en el Sector Productivo", fontMateria2)); descEstadia.setHorizontalAlignment(Element.ALIGN_LEFT);
        PdfPCell promEstadia = new PdfPCell(new Paragraph("Acreditado", fontMateria2));
        table6.addCell(descEstadia);
        table6.addCell(promEstadia);
        c11.addElement(table6);
        c12.addElement(new Paragraph(""));
        
        tablaPrincipal.addCell(c1);
        tablaPrincipal.addCell(c2);
        tablaPrincipal.addCell(c3);
        tablaPrincipal.addCell(c4);
        tablaPrincipal.addCell(c5);
        tablaPrincipal.addCell(c6);
        tablaPrincipal.addCell(c7);
        tablaPrincipal.addCell(c8);
        tablaPrincipal.addCell(c9);
        tablaPrincipal.addCell(c10);
        tablaPrincipal.addCell(c11);
        tablaPrincipal.addCell(c12);
        
        texto3 = new Paragraph("ATENTAMENTE", fontBold1);                                       texto3.setAlignment(Element.ALIGN_CENTER);
        texto4 = new Paragraph("'TECNOLOGIA Y CALIDAD, BASE DEL DESARROLLO'", fontBold1);       texto4.setAlignment(Element.ALIGN_CENTER);       texto4.setLeading(7, 0);
        texto5 = new Paragraph("Ing. Juan Alberto Sánchez González", fontBolder);              texto5.setSpacingBefore(50);                     texto5.setAlignment(Element.ALIGN_CENTER);
        texto6 = new Paragraph("Jefe del Departamento de Servicios Escolares", fontBolder);    texto6.setAlignment(Element.ALIGN_CENTER);

        //Se agregan los elementos creados
        document.add(nombreUniversidad);
        document.add(departamento);
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
    
}
