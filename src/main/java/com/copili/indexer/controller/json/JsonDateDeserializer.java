package com.copili.indexer.controller.json;

import com.copili.indexer.util.Formats;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonDateDeserializer extends JsonDeserializer<Date> {

	private final static Logger log = LoggerFactory.getLogger( JsonDateDeserializer.class );

	@Override
	public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		String date = jsonParser.getText();
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat( Formats.FORM_DATE_FORMAT );
			dateFormat.setLenient( false );
			return dateFormat.parse( date );
		} catch( Exception e ) {
			log.error( "Error al parsear fecha json: {} : {}", date, e.getMessage() );
		}
		return null;
	}
}
