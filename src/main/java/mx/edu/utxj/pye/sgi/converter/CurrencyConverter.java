package mx.edu.utxj.pye.sgi.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;

@FacesConverter("CurrencyConverter")
public class CurrencyConverter implements Converter<Double> {
    final DecimalFormat df = new DecimalFormat("$###,###,###.00");
    /**
     * <p><span class="changed_modified_2_3">Convert</span> the specified string value, which is associated with
     * the specified {@link UIComponent}, into a model data object that
     * is appropriate for being stored during the <em class="changed_modified_2_3">Process Validations</em>
     * phase of the request processing lifecycle.</p>
     *
     * @param context   {@link FacesContext} for the request being processed
     * @param component {@link UIComponent} with which this model object
     *                  value is associated
     * @param value     String value to be converted (may be <code>null</code>)
     * @return <code>null</code> if the value to convert is <code>null</code>,
     * otherwise the result of the conversion
     * @throws NullPointerException if <code>context</code> or
     *                              <code>component</code> is <code>null</code>
     */
    @Override
    public Double getAsObject(FacesContext context, UIComponent component, String value) {
        System.out.println("context = [" + context + "], component = [" + component + "], value = [" + value + "]");
        try {
            return df.parse(value).doubleValue();
        } catch (ParseException e) {
            return 0d;
        }
    }

    /**
     * <p>Convert the specified model object value, which is associated with
     * the specified {@link UIComponent}, into a String that is suitable
     * for being included in the response generated during the
     * <em>Render Response</em> phase of the request processing
     * lifeycle.</p>
     *
     * @param context   {@link FacesContext} for the request being processed
     * @param component {@link UIComponent} with which this model object
     *                  value is associated
     * @param value     Model object value to be converted
     *                  (may be <code>null</code>)
     * @return a zero-length String if value is <code>null</code>,
     * otherwise the result of the conversion
     * @throws NullPointerException if <code>context</code> or
     *                              <code>component</code> is <code>null</code>
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Double value) {
        System.out.println("context = [" + context + "], component = [" + component + "], value = [" + value + "]");
        return df.format(value);
    }
}
