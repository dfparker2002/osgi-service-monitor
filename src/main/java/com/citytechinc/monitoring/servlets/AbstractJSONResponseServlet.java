package com.citytechinc.monitoring.servlets;

import com.citytechinc.monitoring.constants.Constants;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
public abstract class AbstractJSONResponseServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractJSONResponseServlet.class);
    private static final JsonFactory FACTORY = new JsonFactory().disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Write an object to the response as JSON.
     *
     * @param response sling response
     * @param object object to be written as JSON
     */
    protected final void writeJsonResponse(final SlingHttpServletResponse response, final Object object) {
        writeJsonResponseWithEnums(response, object, false, Constants.ABSTRACT_JSON_RESPONSE_SERVLET_DEFAULT_DATE_FORMAT);
    }

    private void writeJsonResponseWithEnums(final SlingHttpServletResponse response, final Object object,
                                            final boolean useStrings, final String dateFormat) {
        response.setContentType(Constants.ABSTRACT_JSON_RESPONSE_SERVLET_CONTENT_TYPE);
        response.setCharacterEncoding(Constants.ABSTRACT_JSON_RESPONSE_SERVLET_CHARACTER_ENCODING);

        try {

            final JsonGenerator generator = FACTORY.createJsonGenerator(response.getWriter());
            final SerializationConfig config = MAPPER.getSerializationConfig().withDateFormat(new SimpleDateFormat(dateFormat));

            if (useStrings) {
                MAPPER.setSerializationConfig(config.with(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING));
            } else {
                MAPPER.setSerializationConfig(config.without(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING));
            }

            MAPPER.writeValue(generator, object);

        } catch (final Exception exception) {
            LOG.error("error writing JSON response", exception);
        }
    }
}
