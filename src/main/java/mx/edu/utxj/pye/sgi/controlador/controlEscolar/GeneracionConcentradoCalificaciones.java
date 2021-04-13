package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPresentacionCalificacionesReporte;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoVistaCalificacionestitulosTabla;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaCalificacion;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ViewScoped
@Named
public class GeneracionConcentradoCalificaciones implements Serializable{
    
    @Getter @Setter String encabezado;
    @Getter @Setter PdfTemplate total;
    @Getter @Setter SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    @Getter @Setter Date fechaFinVeda = new Date(2021, 06, 06);
    @Getter @Setter StreamedContent contenioArchivo;
    @Getter @Setter DefaultStreamedContent download;
    @EJB EjbConsultaCalificacion ejb;
    @Inject ConcentradoCalificacionesTutor con;

    public void generarPdf(Grupo grupo, List<DtoVistaCalificacionestitulosTabla> titulos, PeriodosEscolares periodoSelect, List<DtoPresentacionCalificacionesReporte> calificaciones, AreasUniversidad areasUniversidad) throws IOException, DocumentException {
        //Se crean las variables a utilizar para la creación del pdf
        Personal director = ejb.obtenerDirector(grupo.getPlan()).getValor();
        //calificaciones.stream().forEach(System.out::println);
        List<DtoPresentacionCalificacionesReporte> dpcp = calificaciones
                .stream().filter(dto -> dto.getTipoEstudiante().equals(Short.parseShort("1"))).collect(Collectors.toList());
        Document document;
        /*if(calificaciones.size() > 25){
            document = new Document(PageSize.LETTER, 15, 15, 15, 15);
        }else{

        }*/
        document = new Document(PageSize.LETTER.rotate() , 15, 15, 15, 15);
        Paragraph parrafo, parrafo2, parrafo3, fechaImpresion, fecha, periodo, periodoEscolar, gradoGrupo, grado, carrera,
                programaEducativo;
        Integer celdas = 2 + titulos.size();
        String nombrePdf = "acta_final"+grupo.getLiteral()+"_"+grupo.getTutor()+".pdf";
        //String ruta = ServicioArchivos.carpetaRaiz.concat(File.separator).concat("acta_final").concat(File.separator).concat(nombrePdf);
        Image logoSep = Image.getInstance("C:\\archivos\\acta_final\\logoSep.jpg");
        Image logoUT = Image.getInstance("C:\\archivos\\acta_final\\logoUT.jpg");
        Font fontBold = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        Font fontNormal = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
        Font fontMateria = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
        Font fontMateria2 = new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL);
        Font fontMateria1 = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);
        Font fontBolder = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, Font.UNDERLINE);
        Font fontCursive = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
        String base = ServicioArchivos.carpetaRaiz.concat("acta_final").concat(File.separator);

        ServicioArchivos.addCarpetaRelativa(base);
        ServicioArchivos.eliminarArchivo(base.concat(nombrePdf));
        OutputStream out = new FileOutputStream(new File(base.concat(nombrePdf)));

        PdfWriter.getInstance(document, out);
        PdfWriter.getInstance(document, new FileOutputStream("C:\\archivos\\acta_final\\acta_final_"+grupo.getLiteral()+"_"+grupo.getTutor()+".pdf"));
        //PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\"+usuario+"\\Downloads\\acta_final_"+grupo.getLiteral()+"_"+grupo.getTutor()+".pdf"));
        PdfPTable table = new PdfPTable(2 + titulos.size());
        PdfPTable tableFirma = new PdfPTable(3);
        PdfPTable tableEscalas = new PdfPTable(5);
        Caster caster = new Caster();


        //Se abre el pdf para iniciar a darle formato
        document.open();
        //Se coloca los elementos necesarios que contendra el pdf
        logoSep.scaleToFit(80,100);
        logoSep.setAbsolutePosition(60f, 560f);
        logoSep.setAlignment(Element.ALIGN_LEFT);
        parrafo = new Paragraph("UNIVERSIDAD TECNÓLOGICA DE XICOTEPEC DE JUÁREZ", fontBold);
        parrafo.setAlignment(Element.ALIGN_CENTER);
        parrafo.setLeading(15,0);
        parrafo2 = new Paragraph("Organismo Público Descentralizado del Gobierno del Estado de Puebla", fontCursive);
        parrafo2.setLeading(11,0);
        parrafo2.setAlignment(Element.ALIGN_CENTER);
        parrafo3 = new Paragraph("ACTA DE RESULTADOS FINALES DE EVALUACIÓN", fontBold);
        parrafo3.setAlignment(Element.ALIGN_CENTER);
        parrafo3.setLeading(11,0);

        logoUT.scaleToFit(70,95);
        logoUT.setAbsolutePosition(655f, 556f);
        logoUT.setAlignment(Element.ALIGN_RIGHT);
        fechaImpresion = new Paragraph("Fecha de impresión: ", fontNormal);
        fechaImpresion.setLeading(16, 0);
        fechaImpresion.setIndentationLeft(625f);
        fecha = new Paragraph(sdf.format(new Date()), fontBolder);
        fecha.setLeading(0, 0);
        fecha.setIndentationLeft(700f);
        gradoGrupo = new Paragraph("Grado, Grupo:", fontNormal);
        gradoGrupo.setLeading(0, 0);
        gradoGrupo.setIndentationLeft(20f);
        grado = new Paragraph(String.valueOf(grupo.getGrado()+"°'"+grupo.getLiteral().toString()+"'"), fontBolder);
        grado.setLeading(0, 0);
        grado.setIndentationLeft(75f);
        carrera = new Paragraph("Carrera:", fontNormal);
        carrera.setLeading(0, 0);
        carrera.setIndentationLeft(100f);
        programaEducativo = new Paragraph(grupo.getPlan().getDescripcion(), fontBolder);
        programaEducativo.setLeading(0, 0);
        programaEducativo.setIndentationLeft(130f);
        periodo = new Paragraph("Periodo:", fontNormal);
        periodo.setLeading(0, 0);
        periodo.setIndentationLeft(460f);
        periodoEscolar = new Paragraph(caster.periodoToString(periodoSelect), fontBolder);
        periodoEscolar.setLeading(0, 0);
        periodoEscolar.setIndentationLeft(493f);

        //Tabla de calificaciones
        if(celdas.equals(8)){table.setWidths(new float[]{15f, 40f, 25f, 25f, 25f, 25f, 25f, 25f});}
        if(celdas.equals(9)){table.setWidths(new float[]{15f, 40f, 25f, 25f, 25f, 25f, 25f, 25f, 25f});}
        if(celdas.equals(10)){table.setWidths(new float[]{15f, 40f, 25f, 25f, 25f, 25f, 25f, 25f, 25f, 25f});}
        if(celdas.equals(11)){table.setWidths(new float[]{15f, 40f, 25f, 25f, 25f, 25f, 25f, 25f, 25f, 25f, 25f});}
        if(celdas.equals(12)){table.setWidths(new float[]{15f, 40f, 25f, 25f, 25f, 25f, 25f, 25f, 25f, 25f, 25f, 25f});}
        table.setSpacingBefore(9);
        table.setWidthPercentage(100);
        PdfPCell matricula = new PdfPCell(new Paragraph("Matricula", fontMateria));
        matricula.setHorizontalAlignment(Element.ALIGN_CENTER);
        matricula.setRowspan(2);
        table.addCell(matricula);
        PdfPCell nombre = new PdfPCell(new Paragraph("Nombre del alumno", fontMateria));
        nombre.setHorizontalAlignment(Element.ALIGN_CENTER);
        nombre.setRowspan(2);
        table.addCell(nombre);
        PdfPCell materias = new PdfPCell(new Paragraph("Asignaturas", fontMateria));
        materias.setHorizontalAlignment(Element.ALIGN_CENTER);
        materias.setColspan(titulos.size());
        table.addCell(materias);
        titulos.forEach(x -> {
            PdfPCell materia = new PdfPCell(new Paragraph(x.getUnidad(), fontMateria));
            materia.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(materia);
        });
        dpcp.forEach(x -> {
            PdfPCell mat = new PdfPCell(new Paragraph(x.getMatricula().toString(), fontMateria));
            mat.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(mat);
            table.addCell(new Paragraph(x.getNombre(), fontMateria));
            x.getMaterias().forEach(y -> {
                PdfPCell cal;
                if(y.getNivelacion().compareTo(BigDecimal.ZERO) == 0){
                    cal = new PdfPCell(new Paragraph(y.getPromedioFinalO().setScale(2, RoundingMode.HALF_UP).toString(), fontMateria));
                }else{
                    cal = new PdfPCell(new Paragraph(y.getPromedioFinalN().setScale(2, RoundingMode.HALF_UP).toString().concat(" *"), fontMateria));
                }                
                cal.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cal);
            });
        });
        PdfPCell docentes = new PdfPCell(new Paragraph("Docentes", fontMateria1));
        docentes.setHorizontalAlignment(Element.ALIGN_CENTER);
        docentes.setColspan(2);
        table.addCell(docentes);
        titulos.forEach(x -> {
            PdfPCell docente = new PdfPCell(new Paragraph(x.getDocente(), fontMateria));
            docente.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(docente);
        });
        final PdfPCell[] leyenda = new PdfPCell[1];
        dpcp.forEach(x -> {
            x.getMaterias().forEach(y -> {
                if(!y.getPromedioFinalN().equals(BigDecimal.ZERO)){
                    leyenda[0] = new PdfPCell(new Paragraph("* Resultado obtenido en nivelación final", fontMateria1));
                    leyenda[0].setHorizontalAlignment(Element.ALIGN_LEFT);
                    leyenda[0].setColspan(2 + titulos.size());
                    leyenda[0].setBorder(Rectangle.NO_BORDER);
                }
            });
        });
        table.addCell(leyenda[0]);


        if(dpcp.size() > 10 && dpcp.size() <= 14){
            tableFirma.setSpacingBefore(170);
        }
        if(dpcp.size() > 15 && dpcp.size() <= 20){
            tableFirma.setSpacingBefore(140);
        }
        if(dpcp.size() >= 21 && dpcp.size() <= 25){
            tableFirma.setSpacingBefore(70);
        }
        if(dpcp.size() >= 26 && dpcp.size() <= 30){
            tableFirma.setSpacingBefore(30);
        }
        if(dpcp.size() >= 31 && dpcp.size() <= 35){
            tableFirma.setSpacingBefore(42);
        }
        if(dpcp.size() >= 36 && dpcp.size() <= 40){
            tableFirma.setSpacingBefore(42);
        }

        tableFirma.setWidthPercentage(60);
        PdfPCell nombreTutor = new PdfPCell(new Paragraph(con.buscarPersonal(grupo.getTutor()), fontMateria));
        nombreTutor.setHorizontalAlignment(Element.ALIGN_CENTER);
        nombreTutor.setBorder(Rectangle.BOTTOM);
        tableFirma.addCell(nombreTutor);
        PdfPCell celdaVacia = new PdfPCell();
        celdaVacia.setBorder(0);
        tableFirma.addCell(celdaVacia);
        PdfPCell nombreDirector = new PdfPCell(new Paragraph(director.getNombre(), fontMateria));
        nombreDirector.setHorizontalAlignment(Element.ALIGN_CENTER);
        nombreDirector.setBorder(Rectangle.BOTTOM);
        tableFirma.addCell(nombreDirector);
        PdfPCell textoTutor = new PdfPCell(new Paragraph("Nombre y firma del tutor de grupo", fontMateria));
        textoTutor.setHorizontalAlignment(Element.ALIGN_CENTER);
        textoTutor.setBorder(0);
        tableFirma.addCell(textoTutor);
        PdfPCell celdaVacia1 = new PdfPCell();
        celdaVacia1.setBorder(0);
        tableFirma.addCell(celdaVacia1);
        PdfPCell textoDir = new PdfPCell(new Paragraph("Nombre y firma del director de la carrera", fontMateria));
        textoDir.setHorizontalAlignment(Element.ALIGN_CENTER);
        textoDir.setBorder(0);
        tableFirma.addCell(textoDir);

        tableEscalas.setSpacingBefore(10);
        tableEscalas.setWidthPercentage(70);
        PdfPCell descripcion = new PdfPCell(new Paragraph("DESCRIPCIÓN DE LA ESCALA", fontMateria2));
        descripcion.setColspan(5);
        descripcion.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableEscalas.addCell(descripcion);
        PdfPCell asigNoInt = new PdfPCell(new Paragraph("Asignatura No Integradora", fontMateria2));
        asigNoInt.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableEscalas.addCell(asigNoInt);
        PdfPCell autonomo = new PdfPCell(new Paragraph("AU = Autónomo 10", fontMateria2));
        autonomo.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableEscalas.addCell(autonomo);
        PdfPCell destacado = new PdfPCell(new Paragraph("DE = Destacado 9", fontMateria2));
        destacado.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableEscalas.addCell(destacado);
        PdfPCell satisfactorio = new PdfPCell(new Paragraph("DE = Satisfactorio 8", fontMateria2));
        satisfactorio.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableEscalas.addCell(satisfactorio);
        PdfPCell noAcreditado = new PdfPCell(new Paragraph("NA = No Acreditado menor a 8", fontMateria2));
        noAcreditado.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableEscalas.addCell(noAcreditado);

        PdfPCell asigInt = new PdfPCell(new Paragraph("Asignatura Integradora", fontMateria2));
        asigInt.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableEscalas.addCell(asigInt);
        PdfPCell competente = new PdfPCell(new Paragraph("CA = Competente Autónomo 10", fontMateria2));
        competente.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableEscalas.addCell(competente);
        PdfPCell cdestacado = new PdfPCell(new Paragraph("CD = Competente Destacado 9", fontMateria2));
        cdestacado.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableEscalas.addCell(cdestacado);
        PdfPCell comp = new PdfPCell(new Paragraph("CO = Competente 8", fontMateria2));
        comp.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableEscalas.addCell(comp);
        PdfPCell noAcreditadoA = new PdfPCell(new Paragraph("NA = No Acreditado menor a 8", fontMateria2));
        noAcreditadoA.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableEscalas.addCell(noAcreditadoA);


        //Tabla de firma por tutor y director

        //Se agregan los elementos creados
        document.add(parrafo);
        document.add(parrafo2);
        document.add(parrafo3);
        document.add(logoSep);
            document.add(logoUT);
        if(new Date().after(fechaFinVeda)){
            
            //System.out.println("La fecha de hoy es despues de la veda electoral");
        }
        document.add(fechaImpresion);
        document.add(fecha);
        document.add(gradoGrupo);
        document.add(grado);
        document.add(carrera);
        document.add(programaEducativo);
        document.add(periodo);
        document.add(periodoEscolar);
        document.add(table);
        document.add(tableFirma);
        document.add(tableEscalas);
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
            //String ruta = "/sii2/media".concat(file.toURI().toString().split("archivos")[1]);
            //Ajax.oncomplete("descargar('" + ruta + "');");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Path path = Paths.get(base.concat(nombrePdf));
        //Files.delete(path);

        //Llama a aplicacion de PDF reader
    }
}
