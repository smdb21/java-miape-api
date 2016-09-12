/**
 * WSDBFetchDoclitServerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.ac.ebi.www.ws.services.WSDbfetchDoclit;

public class WSDBFetchDoclitServerServiceLocator extends org.apache.axis.client.Service implements uk.ac.ebi.www.ws.services.WSDbfetchDoclit.WSDBFetchDoclitServerService {

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

    public WSDBFetchDoclitServerServiceLocator() {
    }


    public WSDBFetchDoclitServerServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSDBFetchDoclitServerServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSDbfetchDoclit
    private java.lang.String WSDbfetchDoclit_address = "http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit";

    public java.lang.String getWSDbfetchDoclitAddress() {
        return WSDbfetchDoclit_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSDbfetchDoclitWSDDServiceName = "WSDbfetchDoclit";

    public java.lang.String getWSDbfetchDoclitWSDDServiceName() {
        return WSDbfetchDoclitWSDDServiceName;
    }

    public void setWSDbfetchDoclitWSDDServiceName(java.lang.String name) {
        WSDbfetchDoclitWSDDServiceName = name;
    }

    public uk.ac.ebi.www.ws.services.WSDbfetchDoclit.WSDBFetchServer getWSDbfetchDoclit() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSDbfetchDoclit_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSDbfetchDoclit(endpoint);
    }

    public uk.ac.ebi.www.ws.services.WSDbfetchDoclit.WSDBFetchServer getWSDbfetchDoclit(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            uk.ac.ebi.www.ws.services.WSDbfetchDoclit.WSDbfetchDoclitSoapBindingStub _stub = new uk.ac.ebi.www.ws.services.WSDbfetchDoclit.WSDbfetchDoclitSoapBindingStub(portAddress, this);
            _stub.setPortName(getWSDbfetchDoclitWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSDbfetchDoclitEndpointAddress(java.lang.String address) {
        WSDbfetchDoclit_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (uk.ac.ebi.www.ws.services.WSDbfetchDoclit.WSDBFetchServer.class.isAssignableFrom(serviceEndpointInterface)) {
                uk.ac.ebi.www.ws.services.WSDbfetchDoclit.WSDbfetchDoclitSoapBindingStub _stub = new uk.ac.ebi.www.ws.services.WSDbfetchDoclit.WSDbfetchDoclitSoapBindingStub(new java.net.URL(WSDbfetchDoclit_address), this);
                _stub.setPortName(getWSDbfetchDoclitWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("WSDbfetchDoclit".equals(inputPortName)) {
            return getWSDbfetchDoclit();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "WSDBFetchDoclitServerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "WSDbfetchDoclit"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WSDbfetchDoclit".equals(portName)) {
            setWSDbfetchDoclitEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
