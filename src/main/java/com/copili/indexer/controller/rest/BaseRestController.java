package com.copili.indexer.controller.rest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BaseRestController {

    final static Logger log = LoggerFactory.getLogger( BaseRestController.class );

	private final static String REGEX_RFC = "^([A-ZÑ\\x26]{3,4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1]))([A-Z\\d]{3})?$";

    protected List<String> messages;

	protected EmailValidator emailValidator;

	@Autowired
	private MessageSource messageSource;

	public BaseRestController() {
		super();
		emailValidator = EmailValidator.getInstance();
	}

	@ModelAttribute("initializeMessages")
	public void initializeMessages() {
		initMessages();
	}

	protected List<String> messages( Errors errors ) {
		List<String> messages = new ArrayList<>();
		for ( ObjectError error : errors.getAllErrors() ) {
			String name = messageSource.getMessage( (String) error.getArguments()[0], null, Locale.getDefault() );
			String[] arguments = { name };
			String message = messageSource.getMessage( error.getCode(), arguments, Locale.getDefault() );
			messages.add( message );
		}
		return messages;
	}

	protected ResponseEntity<List<String>> messages( HttpStatus httpStatus ) {
		return new ResponseEntity<>( messages, httpStatus );
	}

	protected void initMessages() {
		if ( messages == null ) {
			messages = new ArrayList<>();
		}
		messages.clear();
	}

	protected void addMessage( String key, String[] parameters ) {
		String message = messageSource.getMessage( key, parameters, Locale.getDefault() );
		messages.add( message );
	}

	protected void addMessage( String key ) {
		addMessage( key, null );
	}

	protected boolean hasMessages() {
		return messages != null && messages.size() > 0;
	}

	protected String translate( String key ) {
		String translatedMessage = messageSource.getMessage( key, null, Locale.getDefault() );
        log.error( "No se pudo traducir el Mensaje para {}", key );
        return StringUtils.isBlank( translatedMessage ) ? key : translatedMessage;
	}

	// Validaciones

	protected void validateNull( Object object, String property ) {
		if ( null == getValue( object, property ) ) {
			addMessage( "error.null", new String[]{ translate( property ) } );
		}
	}

    protected void validateNull( Object object, String property, boolean translate ) {
        if ( null == getValue( object, property ) ) {
            String translated = translate ? translate( property ) : property;
            addMessage( "error.null", new String[]{ translated } );
        }
    }

	protected void validateEmpty( Object object, String property ) {
		if ( StringUtils.isBlank( getValueAsString( object, property ) ) ) {
			addMessage( "error.empty", new String[]{ translate( property ) } );
		}
	}

    protected void validateEmpty( Object object, String property, boolean translate ) {
        if ( StringUtils.isBlank( getValueAsString( object, property ) ) ) {
            String translated = translate ? translate( property ) : property;
            addMessage( "error.empty", new String[]{ translated } );
        }
    }

	protected void validateEmail( Object object, String property ) {
		if ( !emailValidator.isValid( getValueAsString( object, property ) ) ) {
			addMessage( "error.email", new String[]{ translate( property ) } );
		}
	}

	protected void validateRFC( Object object, String property ) {
		if ( null == getValue( object, property ) ) {
			addMessage( "error.null", new String[]{ translate( property ) } );
		} else {
			String rfc = getValueAsString( object, property );
			if( !rfc.matches( REGEX_RFC ) ) {
				addMessage( "error.rfc", new String[]{ translate( property ) } );
			}
		}
	}

	protected void validateDate( Object object, String property, Date leftLimit, Date rightLimit ) {
		if ( null == getValue( object, property ) ) {
			addMessage( "error.null.fecha", new String[]{ translate( property ) } );
		} else {
			Date date = (Date) getValue( object, property );
			if ( null != leftLimit && date.compareTo( leftLimit ) < 0 ) {

			}
			if ( null != rightLimit && date.compareTo( rightLimit ) > 0 ) {

			}
		}
	}

	private String getValueAsString( Object object, String property ) {
		BeanWrapper bw = new BeanWrapperImpl( object );
		return (String) bw.getPropertyValue( property );
	}

	private Object getValue( Object object, String property ) {
		BeanWrapper bw = new BeanWrapperImpl( object );
		return bw.getPropertyValue( property );
	}

}
