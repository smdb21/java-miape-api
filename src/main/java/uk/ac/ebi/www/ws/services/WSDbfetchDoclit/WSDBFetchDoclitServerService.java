/**
 * WSDBFetchDoclitServerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.ac.ebi.www.ws.services.WSDbfetchDoclit;

public interface WSDBFetchDoclitServerService extends javax.xml.rpc.Service {

/**
 * WSDbfetch: entry retrieval using entry identifiers or accession
 * numbers for 
 * various biological databases, including EMBL-Bank, InterPro, MEDLINE,
 * Patent 
 * Proteins, PDB, RefSeq, UniParc, UniProtKB and UniRef.
 * 
 * Note: this is a document/literal wrapped SOAP interface to the WSDbfetch
 * service, for 
 * other interfaces please see the documentation 
 * (http://www.ebi.ac.uk/Tools/webservices/services/dbfetch).
 */
    public java.lang.String getWSDbfetchDoclitAddress();

    public uk.ac.ebi.www.ws.services.WSDbfetchDoclit.WSDBFetchServer getWSDbfetchDoclit() throws javax.xml.rpc.ServiceException;

    public uk.ac.ebi.www.ws.services.WSDbfetchDoclit.WSDBFetchServer getWSDbfetchDoclit(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
