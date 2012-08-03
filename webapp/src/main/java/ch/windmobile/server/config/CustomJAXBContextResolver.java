package ch.windmobile.server.config;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import ch.windmobile.server.datasourcemodel.xml.Chart;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

/**
 * Custom JAXB context resolver that enables the "natural" json generator. The natural generator has nicer features,
 * such as generating json arrays always properly, instead of only when the array has more than one element (makes the
 * parsing easier)
 */
@Provider
public class CustomJAXBContextResolver implements ContextResolver<JAXBContext> {

    private JAXBContext context;
    private Class<?>[] types = { Chart.class };

    public CustomJAXBContextResolver() throws Exception {
        this.context = new JSONJAXBContext(JSONConfiguration.natural().build(), types);
    }

    public JAXBContext getContext(Class<?> objectType) {
        for (int i = 0; i < this.types.length; i++)
            if (this.types[i].equals(objectType))
                return context;

        return null;
    }
}