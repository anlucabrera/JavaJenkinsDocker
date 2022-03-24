/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controlador.controlEscolar.ConcentradoCalificacionesTutor;
import mx.edu.utxj.pye.sgi.dto.ch.CurriculumRIPPPA;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaCalificacion;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;


@Named
@ManagedBean
@ViewScoped
public class PdfUtilidades implements Serializable{
    
    @Getter @Setter String encabezado;
    @Getter @Setter PdfTemplate total;
    @Getter @Setter SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    @Getter @Setter Date fechaFinVeda = new Date(2021, 06, 06);
    @Getter @Setter StreamedContent contenioArchivo;
    @Getter @Setter DefaultStreamedContent download;
    @EJB EjbConsultaCalificacion ejb;
    @Inject ConcentradoCalificacionesTutor con;

    public void generarPdf(CurriculumRIPPPA.ResumenCV cV, CurriculumRIPPPA.DatosPersonal dp) throws IOException, DocumentException {
        //calificaciones.stream().forEach(System.out::println);
        Document document = new Document(PageSize.LETTER , 15, 15, 15, 15);
        
        Paragraph parrafo, parrafo2, parrafo3, fechaImpresion, fecha, periodo, periodoEscolar, gradoGrupo, grado, carrera,
                programaEducativo;
//        Integer celdas = 2 + titulos.size();

        String nombrePdf = "resumenCv"+dp.getPersonal().getClave()+"_"+dp.getPersonal().getNombre()+".pdf";
        //String ruta = ServicioArchivos.carpetaRaiz.concat(File.separator).concat("acta_final").concat(File.separator).concat(nombrePdf);
        Image logoSep = Image.getInstance("C:\\archivos\\evidenciasCapitalHumano\\RIPPPA\\logoSep.jpg");
        Image logoUT = Image.getInstance("C:\\archivos\\evidenciasCapitalHumano\\RIPPPA\\logoUT.jpg");
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
        PdfWriter.getInstance(document, new FileOutputStream("C:\\archivos\\evidenciasCapitalHumano\\RIPPPA\\resumenCv"+dp.getPersonal().getClave()+"_"+dp.getPersonal().getNombre()+".pdf"));
        //PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\"+usuario+"\\Downloads\\acta_final_"+grupo.getLiteral()+"_"+grupo.getTutor()+".pdf"));
//        PdfPTable table = new PdfPTable(2 + titulos.size());
        PdfPTable tableFirma = new PdfPTable(3);
        PdfPTable tableEscalas = new PdfPTable(5);
        Caster caster = new Caster();


        //Se abre el pdf para iniciar a darle formato
        document.open();
        //Se coloca los elementos necesarios que contendra el pdf
        logoSep.scaleToFit(80,100);
        logoSep.setAbsolutePosition(60f, 740f);
        logoSep.setAlignment(Element.ALIGN_LEFT);
        parrafo = new Paragraph("UNIVERSIDAD TECNÓLOGICA DE XICOTEPEC DE JUÁREZ", fontBold);
        parrafo.setAlignment(Element.ALIGN_CENTER);
        parrafo.setLeading(15,0);
        parrafo2 = new Paragraph("Organismo Público Descentralizado del Gobierno del Estado de Puebla", fontCursive);
        parrafo2.setLeading(11,0);
        parrafo2.setAlignment(Element.ALIGN_CENTER);
        parrafo3 = new Paragraph("Expedientes RIPPPA, Comisión Dictaminadora", fontBold);
        parrafo3.setAlignment(Element.ALIGN_CENTER);
        parrafo3.setLeading(11,0);

        logoUT.scaleToFit(70,95);
        logoUT.setAbsolutePosition(480f, 735);
        logoUT.setAlignment(Element.ALIGN_RIGHT);
//        fechaImpresion = new Paragraph("Fecha de impresión: ", fontNormal);
//        fechaImpresion.setLeading(16, 0);
//        fechaImpresion.setIndentationLeft(625f);
//        fecha = new Paragraph(sdf.format(new Date()), fontBolder);
//        fecha.setLeading(0, 0);
//        fecha.setIndentationLeft(700f);
//        gradoGrupo = new Paragraph("Grado, Grupo:", fontNormal);
//        gradoGrupo.setLeading(0, 0);
//        gradoGrupo.setIndentationLeft(20f);
//        grado = new Paragraph(String.valueOf(grupo.getGrado()+"°'"+grupo.getLiteral().toString()+"'"), fontBolder);
//        grado.setLeading(0, 0);
//        grado.setIndentationLeft(75f);
//        carrera = new Paragraph("Carrera:", fontNormal);
//        carrera.setLeading(0, 0);
//        carrera.setIndentationLeft(100f);
//        programaEducativo = new Paragraph(grupo.getPlan().getDescripcion(), fontBolder);
//        programaEducativo.setLeading(0, 0);
//        programaEducativo.setIndentationLeft(130f);
//        periodo = new Paragraph("Periodo:", fontNormal);
//        periodo.setLeading(0, 0);
//        periodo.setIndentationLeft(460f);
//        periodoEscolar = new Paragraph(caster.periodoToString(periodoSelect), fontBolder);
//        periodoEscolar.setLeading(0, 0);
//        periodoEscolar.setIndentationLeft(493f);

        //Tabla de calificaciones
//        if(celdas.equals(8)){table.setWidths(new float[]{15f, 40f, 25f, 25f, 25f, 25f, 25f, 25f});}
//        if(celdas.equals(9)){table.setWidths(new float[]{15f, 40f, 25f, 25f, 25f, 25f, 25f, 25f, 25f});}
//        if(celdas.equals(10)){table.setWidths(new float[]{15f, 40f, 25f, 25f, 25f, 25f, 25f, 25f, 25f, 25f});}
//        if(celdas.equals(11)){table.setWidths(new float[]{15f, 40f, 25f, 25f, 25f, 25f, 25f, 25f, 25f, 25f, 25f});}
//        if(celdas.equals(12)){table.setWidths(new float[]{15f, 40f, 25f, 25f, 25f, 25f, 25f, 25f, 25f, 25f, 25f, 25f});}
//        table.setSpacingBefore(9);
//        table.setWidthPercentage(100);
//        PdfPCell matricula = new PdfPCell(new Paragraph("Matricula", fontMateria));
//        matricula.setHorizontalAlignment(Element.ALIGN_CENTER);
//        matricula.setRowspan(2);
//        table.addCell(matricula);
//        PdfPCell nombre = new PdfPCell(new Paragraph("Nombre del alumno", fontMateria));
//        nombre.setHorizontalAlignment(Element.ALIGN_CENTER);
//        nombre.setRowspan(2);
//        table.addCell(nombre);
//        PdfPCell materias = new PdfPCell(new Paragraph("Asignaturas", fontMateria));
//        materias.setHorizontalAlignment(Element.ALIGN_CENTER);
//        materias.setColspan(titulos.size());
//        table.addCell(materias);
//        titulos.forEach(x -> {
//            PdfPCell materia = new PdfPCell(new Paragraph(x.getUnidad(), fontMateria));
//            materia.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(materia);
//        });
//        dpcp.forEach(x -> {
//            PdfPCell mat = new PdfPCell(new Paragraph(x.getMatricula().toString(), fontMateria));
//            mat.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(mat);
//            table.addCell(new Paragraph(x.getNombre(), fontMateria));
//            x.getMaterias().forEach(y -> {
//                PdfPCell cal;
//                if(y.getNivelacion().compareTo(BigDecimal.ZERO) == 0){
//                    cal = new PdfPCell(new Paragraph(y.getPromedioFinalO().setScale(2, RoundingMode.HALF_DOWN).toString(), fontMateria));
//                }else{
//                    cal = new PdfPCell(new Paragraph(y.getPromedioFinalN().setScale(2, RoundingMode.HALF_DOWN).toString().concat(" *"), fontMateria));
//                }                
//                cal.setHorizontalAlignment(Element.ALIGN_CENTER);
//                table.addCell(cal);
//            });
//        });
//        PdfPCell docentes = new PdfPCell(new Paragraph("Docentes", fontMateria1));
//        docentes.setHorizontalAlignment(Element.ALIGN_CENTER);
//        docentes.setColspan(2);
//        table.addCell(docentes);
//        titulos.forEach(x -> {
//            PdfPCell docente = new PdfPCell(new Paragraph(x.getDocente(), fontMateria));
//            docente.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(docente);
//        });
//        final PdfPCell[] leyenda = new PdfPCell[1];
//        dpcp.forEach(x -> {
//            x.getMaterias().forEach(y -> {
//                if(!y.getPromedioFinalN().equals(BigDecimal.ZERO)){
//                    leyenda[0] = new PdfPCell(new Paragraph("* Resultado obtenido en nivelación final", fontMateria1));
//                    leyenda[0].setHorizontalAlignment(Element.ALIGN_LEFT);
//                    leyenda[0].setColspan(2 + titulos.size());
//                    leyenda[0].setBorder(Rectangle.NO_BORDER);
//                }
//            });
//        });
//        table.addCell(leyenda[0]);
//
//
//        if(dpcp.size() < 10){
//            tableFirma.setSpacingBefore(190);
//        }
//        if(dpcp.size() >= 10 && dpcp.size() <= 14){
//            tableFirma.setSpacingBefore(170);
//        }
//        if(dpcp.size() >= 15 && dpcp.size() <= 20){
//            tableFirma.setSpacingBefore(140);
//        }
//        if(dpcp.size() >= 21 && dpcp.size() <= 25){
//            tableFirma.setSpacingBefore(70);
//        }
//        if(dpcp.size() >= 26 && dpcp.size() <= 30){
//            tableFirma.setSpacingBefore(30);
//        }
//        if(dpcp.size() >= 31 && dpcp.size() <= 35){
//            tableFirma.setSpacingBefore(42);
//        }
//        if(dpcp.size() >= 36 && dpcp.size() <= 40){
//            tableFirma.setSpacingBefore(42);
//        }

//        tableFirma.setWidthPercentage(60);
//        PdfPCell nombreTutor = new PdfPCell(new Paragraph(con.buscarPersonal(grupo.getTutor()), fontMateria));
//        nombreTutor.setHorizontalAlignment(Element.ALIGN_CENTER);
//        nombreTutor.setBorder(Rectangle.BOTTOM);
//        tableFirma.addCell(nombreTutor);
//        PdfPCell celdaVacia = new PdfPCell();
//        celdaVacia.setBorder(0);
//        tableFirma.addCell(celdaVacia);
//        PdfPCell nombreDirector = new PdfPCell(new Paragraph(director.getNombre(), fontMateria));
//        nombreDirector.setHorizontalAlignment(Element.ALIGN_CENTER);
//        nombreDirector.setBorder(Rectangle.BOTTOM);
//        tableFirma.addCell(nombreDirector);
//        PdfPCell textoTutor = new PdfPCell(new Paragraph("Nombre y firma del tutor de grupo", fontMateria));
//        textoTutor.setHorizontalAlignment(Element.ALIGN_CENTER);
//        textoTutor.setBorder(0);
//        tableFirma.addCell(textoTutor);
//        PdfPCell celdaVacia1 = new PdfPCell();
//        celdaVacia1.setBorder(0);
//        tableFirma.addCell(celdaVacia1);
//        PdfPCell textoDir = new PdfPCell(new Paragraph(description, fontMateria));
//        textoDir.setHorizontalAlignment(Element.ALIGN_CENTER);
//        textoDir.setBorder(0);
//        tableFirma.addCell(textoDir);

        tableEscalas.setSpacingBefore(10);
        tableEscalas.setWidthPercentage(90);
        PdfPCell descripcionDP = new PdfPCell(new Paragraph("DATOS PERSONALES", fontMateria2));
        descripcionDP.setColspan(15);
        descripcionDP.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableEscalas.addCell(descripcionDP);
        
        PdfPCell fotoPer = new PdfPCell(new PdfPCell(createImageCell(dp.getPersonal().getClave())));
        fotoPer.setHorizontalAlignment(Element.ALIGN_CENTER);
        fotoPer.setColspan(3);
        fotoPer.setRowspan(4);
        tableEscalas.addCell(fotoPer);
        
        PdfPCell nomb = new PdfPCell(new Paragraph("Nombre completo", fontMateria2));
        nomb.setHorizontalAlignment(Element.ALIGN_CENTER);
        nomb.setColspan(5);
        tableEscalas.addCell(nomb);        
        PdfPCell curp = new PdfPCell(new Paragraph("CURP", fontMateria2));
        curp.setHorizontalAlignment(Element.ALIGN_CENTER);
        curp.setColspan(4);
        tableEscalas.addCell(curp);        
        PdfPCell edad = new PdfPCell(new Paragraph("Edad", fontMateria2));
        edad.setHorizontalAlignment(Element.ALIGN_CENTER);
        edad.setColspan(1);
        tableEscalas.addCell(edad);        
        PdfPCell civil = new PdfPCell(new Paragraph("Estado civil", fontMateria2));
        civil.setHorizontalAlignment(Element.ALIGN_CENTER);
        civil.setColspan(2);
        tableEscalas.addCell(civil);       
        PdfPCell nombBD = new PdfPCell(new Paragraph(dp.getPersonal().getNombre(), fontMateria));
        nombBD.setHorizontalAlignment(Element.ALIGN_CENTER);
        nombBD.setColspan(5);
        tableEscalas.addCell(nombBD);        
        PdfPCell curpBD = new PdfPCell(new Paragraph(dp.getAdicionalPersonal().getCurp(), fontMateria));
        curpBD.setHorizontalAlignment(Element.ALIGN_CENTER);
        curpBD.setColspan(4);
        tableEscalas.addCell(curpBD);        
        PdfPCell edadBd = new PdfPCell(new Paragraph(dp.getAdicionalPersonal().getEdad().toString(), fontMateria));
        edadBd.setHorizontalAlignment(Element.ALIGN_CENTER);
        edadBd.setColspan(1);
        tableEscalas.addCell(edadBd);        
        PdfPCell civilBD = new PdfPCell(new Paragraph(dp.getAdicionalPersonal().getEstadoCivil(), fontMateria));
        civilBD.setHorizontalAlignment(Element.ALIGN_CENTER);
        civilBD.setColspan(2);
        tableEscalas.addCell(civilBD);

        PdfPCell nacimieno = new PdfPCell(new Paragraph("Lugar de nacimiento", fontMateria2));
        nacimieno.setHorizontalAlignment(Element.ALIGN_CENTER);
        nacimieno.setColspan(5);
        tableEscalas.addCell(nacimieno);        
        PdfPCell rfc = new PdfPCell(new Paragraph("RFC", fontMateria2));
        rfc.setHorizontalAlignment(Element.ALIGN_CENTER);
        rfc.setColspan(4);
        tableEscalas.addCell(rfc);        
        PdfPCell sexo = new PdfPCell(new Paragraph("Sexo", fontMateria2));
        sexo.setHorizontalAlignment(Element.ALIGN_CENTER);
        sexo.setColspan(1);
        tableEscalas.addCell(sexo);        
        PdfPCell fecham = new PdfPCell(new Paragraph("Fecha de nacimiento", fontMateria2));
        fecham.setHorizontalAlignment(Element.ALIGN_CENTER);
        fecham.setColspan(2);
        tableEscalas.addCell(fecham);    
        PdfPCell nacimienoBD = new PdfPCell(new Paragraph(dp.getAdicionalPersonal().getLugarProcedencia(), fontMateria));
        nacimienoBD.setHorizontalAlignment(Element.ALIGN_CENTER);
        nacimienoBD.setColspan(5);
        tableEscalas.addCell(nacimienoBD);        
        PdfPCell rfcBD = new PdfPCell(new Paragraph(dp.getAdicionalPersonal().getRfc(), fontMateria));
        rfcBD.setHorizontalAlignment(Element.ALIGN_CENTER);
        rfcBD.setColspan(4);
        tableEscalas.addCell(rfcBD);        
        PdfPCell sexoBD = new PdfPCell(new Paragraph(dp.getListaPersonal().getGeneroNombre(), fontMateria));
        sexoBD.setHorizontalAlignment(Element.ALIGN_CENTER);
        sexoBD.setColspan(1);
        tableEscalas.addCell(sexoBD);        
        PdfPCell fechamBD = new PdfPCell(new Paragraph(sdf.format(dp.getPersonal().getFechaNacimiento()), fontMateria));
        fechamBD.setHorizontalAlignment(Element.ALIGN_CENTER);
        fechamBD.setColspan(2);
        tableEscalas.addCell(fechamBD);

        PdfPCell direc = new PdfPCell(new Paragraph("Dirección", fontMateria2));
        direc.setHorizontalAlignment(Element.ALIGN_CENTER);
        direc.setColspan(7);
        direc.setRowspan(3);
        tableEscalas.addCell(direc);        
        PdfPCell emails = new PdfPCell(new Paragraph("Correo(s) electrónico(s)	", fontMateria2));
        emails.setHorizontalAlignment(Element.ALIGN_CENTER);
        emails.setColspan(5);
        tableEscalas.addCell(emails);        
        PdfPCell numCel = new PdfPCell(new Paragraph("Número de teléfono", fontMateria2));
        numCel.setHorizontalAlignment(Element.ALIGN_CENTER);
        numCel.setColspan(3);
        tableEscalas.addCell(numCel);     
        
        PdfPCell direcBD = new PdfPCell(new Paragraph(dp.getAdicionalPersonal().getDireccion(), fontMateria));
        direcBD.setHorizontalAlignment(Element.ALIGN_CENTER);
        direcBD.setColspan(7);
        direcBD.setRowspan(3);
        tableEscalas.addCell(direcBD);        
        PdfPCell emailsBD1 = new PdfPCell(new Paragraph(dp.getPersonal().getCorreoElectronico(), fontMateria));
        emailsBD1.setHorizontalAlignment(Element.ALIGN_CENTER);
        emailsBD1.setColspan(5);
        tableEscalas.addCell(emailsBD1);        
        PdfPCell numCelBD1 = new PdfPCell(new Paragraph(dp.getAdicionalPersonal().getNumTelFijo(), fontMateria));
        numCelBD1.setHorizontalAlignment(Element.ALIGN_CENTER);
        numCelBD1.setColspan(3);
        tableEscalas.addCell(numCelBD1);  
        
        PdfPCell emailsBD2 = new PdfPCell(new Paragraph(dp.getPersonal().getCorreoElectronico2(), fontMateria));
        emailsBD2.setHorizontalAlignment(Element.ALIGN_CENTER);
        emailsBD2.setColspan(5);
        tableEscalas.addCell(emailsBD2);        
        PdfPCell numCelBD2 = new PdfPCell(new Paragraph(dp.getAdicionalPersonal().getNumTelMovil(), fontMateria));
        numCelBD2.setHorizontalAlignment(Element.ALIGN_CENTER);
        numCelBD2.setColspan(3);
        tableEscalas.addCell(numCelBD2);  

        //Tabla de firma por tutor y director

        //Se agregan los elementos creados
        document.add(parrafo);
        document.add(parrafo2);
        document.add(parrafo3);
        document.add(logoSep);
        document.add(logoUT);
        //System.out.println("La fecha de hoy es despues de la veda electoral");

//        document.add(fechaImpresion);
//        document.add(fecha);
//        document.add(gradoGrupo);
//        document.add(grado);
//        document.add(carrera);
//        document.add(programaEducativo);
//        document.add(periodo);
//        document.add(periodoEscolar);
//        document.add(table);
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
    
    public static PdfPCell createImageCell(Integer cvper) throws DocumentException, IOException {
        String path="C:\\archivos\\personal\\"+cvper+".jpg";
        Image img = Image.getInstance(path);
        img.scaleToFit(100, 100);
        PdfPCell cell = new PdfPCell(img, true);
        return cell;
    }
    
}
