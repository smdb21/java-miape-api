/**
 * WSDBFetchServer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.ac.ebi.www.ws.services.WSDbfetchDoclit;

public interface WSDBFetchServer extends java.rmi.Remote {

    /**
     * Get a list of available databases (see http://www.ebi.ac.uk/Tools/webservices/services/dbfetch#getsupporteddbs).
     */
    public java.lang.String[] getSupportedDBs() throws java.rmi.RemoteException;

    /**
     * Get a list of databases and formats (see http://www.ebi.ac.uk/Tools/webservices/services/dbfetch#getsupportedformats).
     */
    public java.lang.String[] getSupportedFormats() throws java.rmi.RemoteException;

    /**
     * Deprecated: use getFormatStyles() or getDatabaseInfo(). Get
     * a list of databases, formats and styles (see http://www.ebi.ac.uk/Tools/webservices/services/dbfetch#getsupportedstyles).
     */
    public java.lang.String[] getSupportedStyles() throws java.rmi.RemoteException;

    /**
     * Get detailed information about the available databases (see
     * http://www.ebi.ac.uk/Tools/webservices/services/dbfetch#getdatabaseinfolist).
     */
    public uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DatabaseInfo[] getDatabaseInfoList() throws java.rmi.RemoteException;

    /**
     * Get detailed information about a database (see http://www.ebi.ac.uk/Tools/webservices/services/dbfetch#getdatabaseinfo_db).
     */
    public uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DatabaseInfo getDatabaseInfo(java.lang.String db) throws java.rmi.RemoteException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfParamsException;

    /**
     * Get a list of formats for a given database (see http://www.ebi.ac.uk/Tools/webservices/services/dbfetch#getdbformats_db).
     */
    public java.lang.String[] getDbFormats(java.lang.String db) throws java.rmi.RemoteException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfParamsException;

    /**
     * Get detailed information about a format of a database (see
     * http://www.ebi.ac.uk/Tools/webservices/services/dbfetch#getformatinfo_db_format).
     */
    public uk.ac.ebi.www.ws.services.WSDbfetchDoclit.FormatInfo getFormatInfo(java.lang.String db, java.lang.String format) throws java.rmi.RemoteException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfParamsException;

    /**
     * Get a list of available styles for a given database and format
     * (see http://www.ebi.ac.uk/Tools/webservices/services/dbfetch#getformatstyles_db_format).
     */
    public java.lang.String[] getFormatStyles(java.lang.String db, java.lang.String format) throws java.rmi.RemoteException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfParamsException;

    /**
     * Get detailed information about a style of a format of a database
     * (see http://www.ebi.ac.uk/Tools/webservices/services/dbfetch#getstyleinfo_db_format_style).
     */
    public uk.ac.ebi.www.ws.services.WSDbfetchDoclit.StyleInfo getStyleInfo(java.lang.String db, java.lang.String format, java.lang.String style) throws java.rmi.RemoteException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfParamsException;

    /**
     * Get a database entry (see http://www.ebi.ac.uk/Tools/webservices/services/dbfetch#fetchdata_query_format_style).
     */
    public java.lang.String fetchData(java.lang.String query, java.lang.String format, java.lang.String style) throws java.rmi.RemoteException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfConnException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfNoEntryFoundException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfParamsException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.InputException;

    /**
     * Get a set of database entries (see http://www.ebi.ac.uk/Tools/webservices/services/dbfetch#fetchbatch_db_ids_format_style).
     */
    public java.lang.String fetchBatch(java.lang.String db, java.lang.String ids, java.lang.String format, java.lang.String style) throws java.rmi.RemoteException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfConnException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfNoEntryFoundException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfParamsException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.InputException;
}
