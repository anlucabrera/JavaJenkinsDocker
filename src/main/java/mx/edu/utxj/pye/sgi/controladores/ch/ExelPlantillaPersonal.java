package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class ExelPlantillaPersonal implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("newexcel.ExcelOOXML");
    private static final long serialVersionUID = -4156885120913503886L;

    @Getter    @Setter    private List<ListaPersonal> nuevaListaListaPersonal = new ArrayList<>();
    @Getter    @Setter    private ListaPersonal nuevoOBJListaListaPersonal = new ListaPersonal();
    @Getter    @Setter    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    @Getter    @Setter    String sin = "", pp = "", direccionDescarga = "";

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbSelectec ejbSelectec;

    @PostConstruct
    public void init() {
        System.out.println("ExelPlantillaPersonal Inicio: " + System.currentTimeMillis());
        try {
            nuevaListaListaPersonal.clear();
            nuevoOBJListaListaPersonal = new ListaPersonal();

            nuevaListaListaPersonal = ejbSelectec.mostrarListaDeEmpleados();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("ExelPlantillaPersonal Fin: " + System.currentTimeMillis());
    }

    public void generarPlantillaPersoanl() {
        // Creamos el archivo donde almacenaremos la hoja
        // de calculo, recuerde usar la extension correcta,
        // en este caso .xlsx
        File archivo = new File("PlantillaPersonal.xlsx");

        // Creamos el libro de trabajo de Excel formato OOXML
        Workbook workbook = new XSSFWorkbook();

        // La hoja donde pondremos los datos
        Sheet pagina = workbook.createSheet("Personal Activo");

        // Creamos el estilo paga las celdas del encabezado
        CellStyle style = workbook.createCellStyle();
        // Indicamos que tendra un fondo azul aqua
        // con patron solido del color indicado
        style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        String[] titulos = {"Clave del trabajador", "Nombre", "Fecha Ingreso", "Estatus", "Género", "Área Superior de Directivos", "Área Operativa", "Puesto Operativo", "Actividad", "Área Oficial", "Puesto Oficial", "Grado máximo estudios", "Perfil Profesional ", " Experiencia docente", "Experiencia laboral", "Fecha Nacimiento", "Localidad ", "Municipio", "Estado", "País", "S N I", "Perfil PRODEP", "Correo Electrónico", "Correo Electrónico 2",};

        // Creamos una fila en la hoja en la posicion 0
        Row fila = pagina.createRow(0);

        // Creamos el encabezado
        for (int i = 0; i < titulos.length; i++) {
            // Creamos una celda en esa fila, en la posicion 
            // indicada por el contador del ciclo
            Cell celda = fila.createCell(i);

            // Indicamos el estilo que deseamos 
            // usar en la celda, en este caso el unico 
            // que hemos creado
            celda.setCellStyle(style);
            celda.setCellValue(titulos[i]);

        }
        for (int i = 0; i <= nuevaListaListaPersonal.size() - 1; i++) {
            nuevoOBJListaListaPersonal = nuevaListaListaPersonal.get(i);
            if (nuevoOBJListaListaPersonal.getSni() == false) {
                sin = "NO";
            } else {
                sin = "SI";
            }
            if (nuevoOBJListaListaPersonal.getPerfilProdep() == false) {
                pp = "NO";
            } else {
                pp = "SI";
            }
            String[] datos = {
                nuevoOBJListaListaPersonal.getClave().toString(),
                nuevoOBJListaListaPersonal.getNombre(),
                dateFormat.format(nuevoOBJListaListaPersonal.getFechaIngreso()),
                nuevoOBJListaListaPersonal.getStatus().toString(),
                nuevoOBJListaListaPersonal.getGeneroNombre(),
                nuevoOBJListaListaPersonal.getAreaSuperiorNombre(),
                nuevoOBJListaListaPersonal.getAreaOperativaNombre(),
                nuevoOBJListaListaPersonal.getCategoriaOperativaNombre(),
                nuevoOBJListaListaPersonal.getActividadNombre(),
                nuevoOBJListaListaPersonal.getAreaOficialNombre(),
                nuevoOBJListaListaPersonal.getCategoriaOficialNombre(),
                nuevoOBJListaListaPersonal.getGradoNombre(),
                nuevoOBJListaListaPersonal.getPerfilProfesional(),
                String.valueOf(nuevoOBJListaListaPersonal.getExperienciaDocente()) + "año(s)",
                String.valueOf(nuevoOBJListaListaPersonal.getExperienciaLaboral()) + "año(s)",
                dateFormat.format(nuevoOBJListaListaPersonal.getFechaNacimiento()),
                nuevoOBJListaListaPersonal.getLocalidad(),
                nuevoOBJListaListaPersonal.getMunicipio(),
                nuevoOBJListaListaPersonal.getEstado(),
                nuevoOBJListaListaPersonal.getPais(),
                sin,
                pp,
                nuevoOBJListaListaPersonal.getCorreoElectronico(),
                nuevoOBJListaListaPersonal.getCorreoElectronico2(),};

            // Ahora creamos una fila en la posicion 1
            fila = pagina.createRow(i + 1);

            // Y colocamos los datos en esa fila
            for (int j = 0; j < datos.length; j++) {
                // Creamos una celda en esa fila, en la
                // posicion indicada por el contador del ciclo
                Cell celda = fila.createCell(j);

                celda.setCellValue(datos[j]);
            }
            sin = "";
            pp = "";

        }

        // Ahora guardaremos el archivo
        try {
            // Creamos el flujo de salida de datos,
            // apuntando al archivo donde queremos 
            // almacenar el libro de Excel
            FileOutputStream salida = new FileOutputStream("C:\\archivos\\evidenciasCapitalHumano\\PlantillaPersonal\\" + archivo);

            // Almacenamos el libro de 
            // Excel via ese 
            // flujo de datos
            for (int i = 0; i <= 23; i++) {
                pagina.autoSizeColumn((short) i);
//                System.out.println("mx.edu.utxj.pye.sgi.ch.controladores.ExelPlantillaPersonal.generarPlantillaPersoanl() " + i);
            }

            workbook.write(salida);

            // Cerramos el libro para concluir operaciones
            workbook.close();

            LOGGER.log(Level.INFO, "Archivo creado existosamente en {0}", archivo.getAbsolutePath());
            direccionDescarga = convertirRuta(archivo);
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Archivo no localizable en sistema de archivos");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error de entrada/salida");
        }

        Ajax.oncomplete("PF('dlgPlantillaActiva').show();");
        Ajax.oncomplete("PF('dlgPlantillaActiva').show();");
    }

    public String convertirRuta(File file) {
//        System.out.println("evidencias2/evidenciasCapitalHumano/PlantillaPersonal/" + "evidencias2/evidanciasCapitalHuano/PlantillaPersonal/".concat(file.toURI().toString().split("config")[1]));
        return "evidencias2/evidenciasCapitalHumano/PlantillaPersonal".concat(file.toURI().toString().split("config")[1]);
    }
}