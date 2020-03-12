/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.NotificacionesCe;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.NotificacionesCeImagenes;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.NotificacionesEnlaces;

/**
 *
 * @author UTXJ
 */
public class RegistroNotificacionRolGeneral extends AbstractRol{
    @Getter             @NonNull                private                                     PersonalActivo                              personal;
    
    @Getter             @Setter                 private                                     Date                                        fechaInicio;
    @Getter             @Setter                 private                                     Date                                        fechaFin;
    
    @Getter             @Setter                 private                                     Date                                        fechaInicioFiltro;
    @Getter             @Setter                 private                                     Date                                        fechaFinFiltro;
    
    @Getter             @Setter                 private                                     List<String>                                alcance;
    
    @Getter             private                 String                                      pistaTituloPrincipal; //Considerar en el resultado mostrar el tipo y subtítulo de la notificación
    
    @Getter             private                 NotificacionesCe                            notificacionCe;
    @Getter             private                 List<NotificacionesCe>                      listaNotificacionesCe;
    
    @Getter             private                 NotificacionesEnlaces                       notificacionEnlace;
    @Getter             private                 List<NotificacionesEnlaces>                 listaNotificacionesEnlaces;
    
    @Getter             private                 NotificacionesCeImagenes                    notificacionCeImagen;
    @Getter             private                 List<NotificacionesCeImagenes>              listaNotificacionesCeImagenes;
    @Getter             private                 List<Part>                                  archivos;
    
    @Getter             private                 List<DtoNotificacionesAreas>                listaDtoNotificacionesAreas = new ArrayList<>();

    public RegistroNotificacionRolGeneral(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);
        this.personal = filtro.getEntity();
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setPistaTituloPrincipal(String pistaTituloPrincipal) {
        this.pistaTituloPrincipal = pistaTituloPrincipal;
    }

    public void setNotificacionCe(NotificacionesCe notificacionCe) {
        this.notificacionCe = notificacionCe;
    }

    public void setListaNotificacionesCe(List<NotificacionesCe> listaNotificacionesCe) {
        this.listaNotificacionesCe = listaNotificacionesCe;
    }

    public void setNotificacionEnlace(NotificacionesEnlaces notificacionEnlace) {
        this.notificacionEnlace = notificacionEnlace;
    }

    public void setListaNotificacionesEnlaces(List<NotificacionesEnlaces> listaNotificacionesEnlaces) {
        this.listaNotificacionesEnlaces = listaNotificacionesEnlaces;
    }

    public void setNotificacionCeImagen(NotificacionesCeImagenes notificacionCeImagen) {
        this.notificacionCeImagen = notificacionCeImagen;
    }

    public void setListaNotificacionesCeImagenes(List<NotificacionesCeImagenes> listaNotificacionesCeImagenes) {
        this.listaNotificacionesCeImagenes = listaNotificacionesCeImagenes;
    }

    public void setArchivos(List<Part> archivos) {
        this.archivos = archivos;
    }

    public void setListaDtoNotificacionesAreas(List<DtoNotificacionesAreas> listaDtoNotificacionesAreas) {
        this.listaDtoNotificacionesAreas = listaDtoNotificacionesAreas;
    }
    
    
    
}
