/**
 * DatabaseInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.ac.ebi.www.ws.services.WSDbfetchDoclit;

public class DatabaseInfo  implements java.io.Serializable {
    private java.lang.String[] aliasList;

    private java.lang.String[] databaseTerms;

    private uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DataResourceInfo[] dataResourceInfoList;

    private java.lang.String defaultFormat;

    private java.lang.String description;

    private java.lang.String displayName;

    private uk.ac.ebi.www.ws.services.WSDbfetchDoclit.ExampleIdentifiersInfo exampleIdentifiers;

    private uk.ac.ebi.www.ws.services.WSDbfetchDoclit.FormatInfo[] formatInfoList;

    private java.lang.String href;

    private java.lang.String name;

    public DatabaseInfo() {
    }

    public DatabaseInfo(
           java.lang.String[] aliasList,
           java.lang.String[] databaseTerms,
           uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DataResourceInfo[] dataResourceInfoList,
           java.lang.String defaultFormat,
           java.lang.String description,
           java.lang.String displayName,
           uk.ac.ebi.www.ws.services.WSDbfetchDoclit.ExampleIdentifiersInfo exampleIdentifiers,
           uk.ac.ebi.www.ws.services.WSDbfetchDoclit.FormatInfo[] formatInfoList,
           java.lang.String href,
           java.lang.String name) {
           this.aliasList = aliasList;
           this.databaseTerms = databaseTerms;
           this.dataResourceInfoList = dataResourceInfoList;
           this.defaultFormat = defaultFormat;
           this.description = description;
           this.displayName = displayName;
           this.exampleIdentifiers = exampleIdentifiers;
           this.formatInfoList = formatInfoList;
           this.href = href;
           this.name = name;
    }


    /**
     * Gets the aliasList value for this DatabaseInfo.
     * 
     * @return aliasList
     */
    public java.lang.String[] getAliasList() {
        return aliasList;
    }


    /**
     * Sets the aliasList value for this DatabaseInfo.
     * 
     * @param aliasList
     */
    public void setAliasList(java.lang.String[] aliasList) {
        this.aliasList = aliasList;
    }


    /**
     * Gets the databaseTerms value for this DatabaseInfo.
     * 
     * @return databaseTerms
     */
    public java.lang.String[] getDatabaseTerms() {
        return databaseTerms;
    }


    /**
     * Sets the databaseTerms value for this DatabaseInfo.
     * 
     * @param databaseTerms
     */
    public void setDatabaseTerms(java.lang.String[] databaseTerms) {
        this.databaseTerms = databaseTerms;
    }


    /**
     * Gets the dataResourceInfoList value for this DatabaseInfo.
     * 
     * @return dataResourceInfoList
     */
    public uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DataResourceInfo[] getDataResourceInfoList() {
        return dataResourceInfoList;
    }


    /**
     * Sets the dataResourceInfoList value for this DatabaseInfo.
     * 
     * @param dataResourceInfoList
     */
    public void setDataResourceInfoList(uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DataResourceInfo[] dataResourceInfoList) {
        this.dataResourceInfoList = dataResourceInfoList;
    }


    /**
     * Gets the defaultFormat value for this DatabaseInfo.
     * 
     * @return defaultFormat
     */
    public java.lang.String getDefaultFormat() {
        return defaultFormat;
    }


    /**
     * Sets the defaultFormat value for this DatabaseInfo.
     * 
     * @param defaultFormat
     */
    public void setDefaultFormat(java.lang.String defaultFormat) {
        this.defaultFormat = defaultFormat;
    }


    /**
     * Gets the description value for this DatabaseInfo.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this DatabaseInfo.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the displayName value for this DatabaseInfo.
     * 
     * @return displayName
     */
    public java.lang.String getDisplayName() {
        return displayName;
    }


    /**
     * Sets the displayName value for this DatabaseInfo.
     * 
     * @param displayName
     */
    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }


    /**
     * Gets the exampleIdentifiers value for this DatabaseInfo.
     * 
     * @return exampleIdentifiers
     */
    public uk.ac.ebi.www.ws.services.WSDbfetchDoclit.ExampleIdentifiersInfo getExampleIdentifiers() {
        return exampleIdentifiers;
    }


    /**
     * Sets the exampleIdentifiers value for this DatabaseInfo.
     * 
     * @param exampleIdentifiers
     */
    public void setExampleIdentifiers(uk.ac.ebi.www.ws.services.WSDbfetchDoclit.ExampleIdentifiersInfo exampleIdentifiers) {
        this.exampleIdentifiers = exampleIdentifiers;
    }


    /**
     * Gets the formatInfoList value for this DatabaseInfo.
     * 
     * @return formatInfoList
     */
    public uk.ac.ebi.www.ws.services.WSDbfetchDoclit.FormatInfo[] getFormatInfoList() {
        return formatInfoList;
    }


    /**
     * Sets the formatInfoList value for this DatabaseInfo.
     * 
     * @param formatInfoList
     */
    public void setFormatInfoList(uk.ac.ebi.www.ws.services.WSDbfetchDoclit.FormatInfo[] formatInfoList) {
        this.formatInfoList = formatInfoList;
    }


    /**
     * Gets the href value for this DatabaseInfo.
     * 
     * @return href
     */
    public java.lang.String getHref() {
        return href;
    }


    /**
     * Sets the href value for this DatabaseInfo.
     * 
     * @param href
     */
    public void setHref(java.lang.String href) {
        this.href = href;
    }


    /**
     * Gets the name value for this DatabaseInfo.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this DatabaseInfo.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatabaseInfo)) return false;
        DatabaseInfo other = (DatabaseInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.aliasList==null && other.getAliasList()==null) || 
             (this.aliasList!=null &&
              java.util.Arrays.equals(this.aliasList, other.getAliasList()))) &&
            ((this.databaseTerms==null && other.getDatabaseTerms()==null) || 
             (this.databaseTerms!=null &&
              java.util.Arrays.equals(this.databaseTerms, other.getDatabaseTerms()))) &&
            ((this.dataResourceInfoList==null && other.getDataResourceInfoList()==null) || 
             (this.dataResourceInfoList!=null &&
              java.util.Arrays.equals(this.dataResourceInfoList, other.getDataResourceInfoList()))) &&
            ((this.defaultFormat==null && other.getDefaultFormat()==null) || 
             (this.defaultFormat!=null &&
              this.defaultFormat.equals(other.getDefaultFormat()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.displayName==null && other.getDisplayName()==null) || 
             (this.displayName!=null &&
              this.displayName.equals(other.getDisplayName()))) &&
            ((this.exampleIdentifiers==null && other.getExampleIdentifiers()==null) || 
             (this.exampleIdentifiers!=null &&
              this.exampleIdentifiers.equals(other.getExampleIdentifiers()))) &&
            ((this.formatInfoList==null && other.getFormatInfoList()==null) || 
             (this.formatInfoList!=null &&
              java.util.Arrays.equals(this.formatInfoList, other.getFormatInfoList()))) &&
            ((this.href==null && other.getHref()==null) || 
             (this.href!=null &&
              this.href.equals(other.getHref()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getAliasList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAliasList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAliasList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDatabaseTerms() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDatabaseTerms());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDatabaseTerms(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDataResourceInfoList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDataResourceInfoList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDataResourceInfoList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDefaultFormat() != null) {
            _hashCode += getDefaultFormat().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getDisplayName() != null) {
            _hashCode += getDisplayName().hashCode();
        }
        if (getExampleIdentifiers() != null) {
            _hashCode += getExampleIdentifiers().hashCode();
        }
        if (getFormatInfoList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFormatInfoList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFormatInfoList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getHref() != null) {
            _hashCode += getHref().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatabaseInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "DatabaseInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aliasList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "aliasList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "alias"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("databaseTerms");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "databaseTerms"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "databaseTerm"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataResourceInfoList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "dataResourceInfoList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "DataResourceInfo"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "dataResourceInfo"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("defaultFormat");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "defaultFormat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("displayName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "displayName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exampleIdentifiers");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "exampleIdentifiers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "ExampleIdentifiersInfo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("formatInfoList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "formatInfoList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "FormatInfo"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "formatInfo"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("href");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "href"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
