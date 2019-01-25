package mx.edu.utxj.pye.sgi.ejb;

import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.finanzas.Tarifas;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.List;

@Stateless
public class EjbTarifasViajes {
    @EJB Facade f;

    /**
     * Genera una lista tarifas con destinos segun una pista
     * @param pista Pista del destino
     * @return Lista de destinos
     */
    public List<Tarifas> getDestinos(@NonNull String pista){
        return f.getEntityManager().createQuery("select t from Tarifas t inner join t.tarifasViajes tv where tv.destino like concat('%',:pista,'%') ", Tarifas.class)
                .setParameter("pista", pista.trim())
                .getResultList();
    }

    /**
     * Crea una nueva tarifa
     * @param tarifa Tarifa a persistir
     * @return Resultado del proceso
     */
    public ResultadoEJB<Tarifas> crearTarifa(@NonNull Tarifas tarifa){
        try{
            ResultadoEJB<Tarifas> existe = verificarTarifa(tarifa);
            switch (existe.getResultado()){
                case 0: //existe
                    Tarifas bdTarifa = existe.getValor();
                    bdTarifa.setFechaCancelacion(new Date());
                    f.edit(bdTarifa);
                    f.create(tarifa);
                    tarifa.getTarifasViajes().setTarifa(tarifa.getTarifa());
                    f.create(tarifa.getTarifasPorZona());
                    return ResultadoEJB.crearCorrecto(tarifa, "La tarifa se creó correctamente y reemplazó a una existente como vigente.");
                case 1: //no existe
                    f.create(tarifa);
                    tarifa.getTarifasViajes().setTarifa(tarifa.getTarifa());
                    f.create(tarifa.getTarifasPorZona());
                    return ResultadoEJB.crearCorrecto(tarifa, "Tarifa creada correctamente.");
                case 3: case 4: case 5: //error
                    return existe;
                default: return ResultadoEJB.crearErroneo(7, "Caso de reconomiento de existencia de tarifa no identificado", Tarifas.class);
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(6, "Ocurrió un error.", e, Tarifas.class);
        }
    }

    /**
     * Verifica si ya existe una tarifa en base a su origen y destino
     * @param tarifa
     * @return
     */
    public ResultadoEJB<Tarifas> verificarTarifa(@NonNull Tarifas tarifa){
        if(tarifa.getTarifasViajes() == null) return ResultadoEJB.crearErroneo(3, "La tarifa viaje es nula.", Tarifas.class);
        if(tarifa.getTarifasViajes().getOrigen() == null) return ResultadoEJB.crearErroneo(4, "El origen de la tarifa es nulo.", Tarifas.class);
        if(tarifa.getTarifasViajes().getDestino() == null) return ResultadoEJB.crearErroneo(5, "El destino de la tarifa es nulo.", Tarifas.class);

        try{
            return f.getEntityManager().createQuery("select t from Tarifas  t inner join  t.tarifasViajes tv where tv.origen=:origen and tv.destino = :destino", Tarifas.class)
                    .setParameter("origen", tarifa.getTarifasViajes().getOrigen().trim())
                    .setParameter("destino", tarifa.getTarifasViajes().getDestino().trim())
                    .getResultStream()
                    .map(t -> ResultadoEJB.crearCorrecto(t, "Tarifa encontrada."))
                    .findAny()
                    .orElse(ResultadoEJB.crearErroneo(1, "La tarifa no existe.", Tarifas.class));
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(2, "Ocurrió un error.", e, Tarifas.class);
        }
    }
}
