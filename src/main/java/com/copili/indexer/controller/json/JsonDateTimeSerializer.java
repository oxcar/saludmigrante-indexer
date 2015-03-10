package com.copili.indexer.controller.json;

import com.copili.indexer.util.Formats;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonDateTimeSerializer extends JsonSerializer<Date> {

    private final static Logger log = LoggerFactory.getLogger( JsonDateTimeSerializer.class );

    @Override
    public void serialize( Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider ) throws IOException, JsonProcessingException {
        try {
            if( null != date ) {
                SimpleDateFormat dateFormat = new SimpleDateFormat( Formats.FORM_DATETIME_FORMAT );
                dateFormat.setLenient( false );
                jsonGenerator.writeString( dateFormat.format( date ) );
            }
        } catch( Exception e ) {
            log.error( "Error al serializar fecha json: {} : {}", date, e.getMessage() );
        }
    }

}
