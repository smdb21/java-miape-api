package uk.ac.ebi.www.ws.services.WSDbfetchDoclit;

public class WSDBFetchServerProxy implements uk.ac.ebi.www.ws.services.WSDbfetchDoclit.WSDBFetchServer {
  private String _endpoint = null;
  private uk.ac.ebi.www.ws.services.WSDbfetchDoclit.WSDBFetchServer wSDBFetchServer = null;
  
  public WSDBFetchServerProxy() {
    _initWSDBFetchServerProxy();
  }
  
  public WSDBFetchServerProxy(String endpoint) {
    _endpoint = endpoint;
    _initWSDBFetchServerProxy();
  }
  
  private void _initWSDBFetchServerProxy() {
    try {
      wSDBFetchServer = (new uk.ac.ebi.www.ws.services.WSDbfetchDoclit.WSDBFetchDoclitServerServiceLocator()).getWSDbfetchDoclit();
      if (wSDBFetchServer != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)wSDBFetchServer)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)wSDBFetchServer)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (wSDBFetchServer != null)
      ((javax.xml.rpc.Stub)wSDBFetchServer)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public uk.ac.ebi.www.ws.services.WSDbfetchDoclit.WSDBFetchServer getWSDBFetchServer() {
    if (wSDBFetchServer == null)
      _initWSDBFetchServerProxy();
    return wSDBFetchServer;
  }
  
  public java.lang.String[] getSupportedDBs() throws java.rmi.RemoteException{
    if (wSDBFetchServer == null)
      _initWSDBFetchServerProxy();
    return wSDBFetchServer.getSupportedDBs();
  }
  
  public java.lang.String[] getSupportedFormats() throws java.rmi.RemoteException{
    if (wSDBFetchServer == null)
      _initWSDBFetchServerProxy();
    return wSDBFetchServer.getSupportedFormats();
  }
  
  public java.lang.String[] getSupportedStyles() throws java.rmi.RemoteException{
    if (wSDBFetchServer == null)
      _initWSDBFetchServerProxy();
    return wSDBFetchServer.getSupportedStyles();
  }
  
  public uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DatabaseInfo[] getDatabaseInfoList() throws java.rmi.RemoteException{
    if (wSDBFetchServer == null)
      _initWSDBFetchServerProxy();
    return wSDBFetchServer.getDatabaseInfoList();
  }
  
  public uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DatabaseInfo getDatabaseInfo(java.lang.String db) throws java.rmi.RemoteException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfParamsException{
    if (wSDBFetchServer == null)
      _initWSDBFetchServerProxy();
    return wSDBFetchServer.getDatabaseInfo(db);
  }
  
  public java.lang.String[] getDbFormats(java.lang.String db) throws java.rmi.RemoteException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfParamsException{
    if (wSDBFetchServer == null)
      _initWSDBFetchServerProxy();
    return wSDBFetchServer.getDbFormats(db);
  }
  
  public uk.ac.ebi.www.ws.services.WSDbfetchDoclit.FormatInfo getFormatInfo(java.lang.String db, java.lang.String format) throws java.rmi.RemoteException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfParamsException{
    if (wSDBFetchServer == null)
      _initWSDBFetchServerProxy();
    return wSDBFetchServer.getFormatInfo(db, format);
  }
  
  public java.lang.String[] getFormatStyles(java.lang.String db, java.lang.String format) throws java.rmi.RemoteException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfParamsException{
    if (wSDBFetchServer == null)
      _initWSDBFetchServerProxy();
    return wSDBFetchServer.getFormatStyles(db, format);
  }
  
  public uk.ac.ebi.www.ws.services.WSDbfetchDoclit.StyleInfo getStyleInfo(java.lang.String db, java.lang.String format, java.lang.String style) throws java.rmi.RemoteException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfParamsException{
    if (wSDBFetchServer == null)
      _initWSDBFetchServerProxy();
    return wSDBFetchServer.getStyleInfo(db, format, style);
  }
  
  public java.lang.String fetchData(java.lang.String query, java.lang.String format, java.lang.String style) throws java.rmi.RemoteException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfConnException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfNoEntryFoundException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfParamsException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.InputException{
    if (wSDBFetchServer == null)
      _initWSDBFetchServerProxy();
    return wSDBFetchServer.fetchData(query, format, style);
  }
  
  public java.lang.String fetchBatch(java.lang.String db, java.lang.String ids, java.lang.String format, java.lang.String style) throws java.rmi.RemoteException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfConnException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfNoEntryFoundException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfParamsException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfException, uk.ac.ebi.www.ws.services.WSDbfetchDoclit.InputException{
    if (wSDBFetchServer == null)
      _initWSDBFetchServerProxy();
    return wSDBFetchServer.fetchBatch(db, ids, format, style);
  }
  
  
}