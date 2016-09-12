/**
 * FormatInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.ac.ebi.www.ws.services.WSDbfetchDoclit;

public class FormatInfo  implements java.io.Serializable {
    private java.lang.String[] aliases;

    private java.lang.String[] dataTerms;

    private java.lang.String name;

    private uk.ac.ebi.www.ws.services.WSDbfetchDoclit.StyleInfo[] styleInfoList;

    private java.lang.String[] syntaxTerms;

    public FormatInfo() {
    }

    public FormatInfo(
           java.lang.String[] aliases,
           java.lang.String[] dataTerms,
           java.lang.String name,
           uk.ac.ebi.www.ws.services.WSDbfetchDoclit.StyleInfo[] styleInfoList,
           java.lang.String[] syntaxTerms) {
           this.aliases = aliases;
           this.dataTerms = dataTerms;
           this.name = name;
           this.styleInfoList = styleInfoList;
           this.syntaxTerms = syntaxTerms;
    }


    /**
     * Gets the aliases value for this FormatInfo.
     * 
     * @return aliases
     */
    public java.lang.String[] getAliases() {
        return aliases;
    }


    /**
     * Sets the aliases value for this FormatInfo.
     * 
     * @param aliases
     */
    public void setAliases(java.lang.String[] aliases) {
        this.aliases = aliases;
    }


    /**
     * Gets the dataTerms value for this FormatInfo.
     * 
     * @return dataTerms
     */
    public java.lang.String[] getDataTerms() {
        return dataTerms;
    }


    /**
     * Sets the dataTerms value for this FormatInfo.
     * 
     * @param dataTerms
     */
    public void setDataTerms(java.lang.String[] dataTerms) {
        this.dataTerms = dataTerms;
    }


    /**
     * Gets the name value for this FormatInfo.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this FormatInfo.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the styleInfoList value for this FormatInfo.
     * 
     * @return styleInfoList
     */
    public uk.ac.ebi.www.ws.services.WSDbfetchDoclit.StyleInfo[] getStyleInfoList() {
        return styleInfoList;
    }


    /**
     * Sets the styleInfoList value for this FormatInfo.
     * 
     * @param styleInfoList
     */
    public void setStyleInfoList(uk.ac.ebi.www.ws.services.WSDbfetchDoclit.StyleInfo[] styleInfoList) {
        this.styleInfoList = styleInfoList;
    }


    /**
     * Gets the syntaxTerms value for this FormatInfo.
     * 
     * @return syntaxTerms
     */
    public java.lang.String[] getSyntaxTerms() {
        return syntaxTerms;
    }


    /**
     * Sets the syntaxTerms value for this FormatInfo.
     * 
     * @param syntaxTerms
     */
    public void setSyntaxTerms(java.lang.String[] syntaxTerms) {
        this.syntaxTerms = syntaxTerms;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FormatInfo)) return false;
        FormatInfo other = (FormatInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.aliases==null && other.getAliases()==null) || 
             (this.aliases!=null &&
              java.util.Arrays.equals(this.aliases, other.getAliases()))) &&
            ((this.dataTerms==null && other.getDataTerms()==null) || 
             (this.dataTerms!=null &&
              java.util.Arrays.equals(this.dataTerms, other.getDataTerms()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.styleInfoList==null && other.getStyleInfoList()==null) || 
             (this.styleInfoList!=null &&
              java.util.Arrays.equals(this.styleInfoList, other.getStyleInfoList()))) &&
            ((this.syntaxTerms==null && other.getSyntaxTerms()==null) || 
             (this.syntaxTerms!=null &&
              java.util.Arrays.equals(this.syntaxTerms, other.getSyntaxTerms())));
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
        if (getAliases() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAliases());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAliases(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDataTerms() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDataTerms());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDataTerms(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getStyleInfoList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getStyleInfoList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getStyleInfoList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSyntaxTerms() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSyntaxTerms());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSyntaxTerms(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FormatInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "FormatInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aliases");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "aliases"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "alias"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataTerms");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "dataTerms"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "dataTerm"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("styleInfoList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "styleInfoList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "StyleInfo"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "styleInfo"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("syntaxTerms");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "syntaxTerms"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "syntaxTerm"));
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
