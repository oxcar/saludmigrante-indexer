package com.copili.indexer.controller.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuppressWarnings("rawtypes")
public class ApiResponse {

    @JsonProperty("_data")
    public Object data;

    @JsonProperty("_pagination")
    public Pagination pagination;

    @JsonProperty("_errorMessages")
    public List<String> errorMessages;

    //----------------------------------------------------------------------
    // Constructor
    //----------------------------------------------------------------------

    private ApiResponse( Object data ) {
        this.data = data;
    }

    private ApiResponse( String[] errorMessages ) {
        this.errorMessages = ImmutableList.copyOf( errorMessages );
    }

    private ApiResponse( String errorMessage ) {
        this.errorMessages = ImmutableList.copyOf( new String[]{ errorMessage } );
    }

	private ApiResponse( org.springframework.data.domain.Page page, Link link ) {
        this( page, link, null );
    }

    private ApiResponse( org.springframework.data.domain.Page page, Link link, String filter ) {
        data = page.getContent();
        pagination = new Pagination();
        pagination.page = page.getNumber();
        pagination.totalElements = page.getTotalElements();
        pagination.totalPages = page.getTotalPages();

        if( pagination.totalPages > 1 ) {
            Sort sort = page.getSort();
            pagination.links = new Links();
            pagination.links.first = new Page( 0, link.getHref() + "?page=0&size=" + page.getSize() + sort2UrlQuery( sort ) + filter( filter ) );
            pagination.links.last = new Page( pagination.totalPages - 1, link.getHref() + "?page=" + ( pagination.totalPages - 1 ) + "&size=" + page.getSize() + sort2UrlQuery( sort ) + filter( filter ) ) ;

            int numberOfLinks = 5;
            Integer firstLink = calculateFirstLinkIndex( page.getNumber(), numberOfLinks ) ;
            Integer lastLink = calculateLastLinkIndex( page.getNumber(), page.getTotalPages(), numberOfLinks ) ;

            List<Page> pages = new ArrayList<>();
            for( int i = firstLink; i <= lastLink; i++ ) {
                String url = link.getHref() + "?page=" + i + "&size=" + page.getSize() + sort2UrlQuery( sort ) + filter( filter );
                pages.add( new Page( i + 1, url ) );
            }
            pagination.links.pages = pages.toArray( new Page[pages.size()] );

            if( page.getNumber() - 10 > 0 ) {
                pagination.links.minus10 = new Page( page.getNumber() - 10, link.getHref() + "?page=" + ( page.getNumber() - 10 ) + "&size=" + page.getSize() + sort2UrlQuery( sort ) + filter( filter ) ) ;
            }
            if( page.getNumber() + 10 < pagination.totalPages ) {
                pagination.links.plus10 = new Page( page.getNumber() + 10, link.getHref() + "?page=" + ( page.getNumber() + 10 ) + "&size=" + page.getSize() + sort2UrlQuery( sort ) + filter( filter ) ) ;
            }
        }
    }

    private Integer calculateFirstLinkIndex( Integer currentPage, Integer numberOfLinks ) {
        int half = numberOfLinks / 2;
        if( currentPage - half < 0 ) {
            return 0;
        }
        return currentPage - half;
    }

    private Integer calculateLastLinkIndex( Integer currentPage, Integer totalPages, Integer numberOfLinks ) {
        int half = numberOfLinks / 2;
        if( currentPage + half > totalPages ) {
            return totalPages;
        }
        return currentPage + half;
    }

    public static ApiResponse ok( Object o ) {
        return new ApiResponse( o );
    }

    public static ApiResponse ok( org.springframework.data.domain.Page page, Link link ) {
        return new ApiResponse( page, link );
    }

    public static ApiResponse ok( org.springframework.data.domain.Page page, Link link, String filter ) {
        return new ApiResponse( page, link, filter );
    }

    public static ApiResponse withError( String errorMessage ) {
        return new ApiResponse( errorMessage );
    }

    public static ApiResponse withErrors( String[] errorMessages ) {
        return new ApiResponse( errorMessages );
    }

    public static ApiResponse withErrors( List<String> errorMessages ) {
        return new ApiResponse( errorMessages.toArray( new String[errorMessages.size()] ) );
    }

    private String sort2UrlQuery( Sort sort ) {
        if( null != sort ) {
            StringBuilder sb = new StringBuilder();
            for( Sort.Order order : sort ) {
                sb.append( "&sort=" ).append( order.getProperty() ).append( "," ).append( order.getDirection() );
            }
            return sb.toString();
        }
        return "";
    }

    private String filter( String filter ) {
        return filter != null ? "&" + filter : "";
    }

    //----------------------------------------------------------------------
    // Inner classes
    //----------------------------------------------------------------------

    public class Pagination {

        @JsonProperty("page")
        public Integer page;

        @JsonProperty("totalPages")
        public Integer totalPages;

        @JsonProperty("totalElements")
        public Long totalElements;

        @JsonProperty("links")
        public Links links;

    }

    public class Links {

        @JsonProperty("first")
        public Page first;

        @JsonProperty("last")
        public Page last;

        @JsonProperty("minus10")
        public Page minus10;

        @JsonProperty("plus10")
        public Page plus10;

        @JsonProperty("pages")
        public Page[] pages;

    }

    public class Page {

        public Page( Integer number, String url ) {
            this.number = number;
            this.url = url;
        }

        @JsonProperty( "number" )
        public Integer number;

        @JsonProperty( "url" )
        public String url;

    }

}
