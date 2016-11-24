package com.zzu.ehome.bean;

import com.zzu.ehome.application.MMloveConstants;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/1.
 */
public class AdressInterface implements Serializable {
    private String  SOAP_NAMESPACE;
    private String SOAP_ACTION;
    private  String SOAP_METHODNAMEA ;
    private  String SOAP_URL;

    public String getSOAP_NAMESPACE() {
        return SOAP_NAMESPACE;
    }

    public void setSOAP_NAMESPACE(String SOAP_NAMESPACE) {
        this.SOAP_NAMESPACE = SOAP_NAMESPACE;
    }

    public String getSOAP_ACTION() {
        return SOAP_ACTION;
    }

    public void setSOAP_ACTION(String SOAP_ACTION) {
        this.SOAP_ACTION = SOAP_ACTION;
    }

    public String getSOAP_METHODNAMEA() {
        return SOAP_METHODNAMEA;
    }

    public void setSOAP_METHODNAMEA(String SOAP_METHODNAMEA) {
        this.SOAP_METHODNAMEA = SOAP_METHODNAMEA;
    }

    public String getSOAP_URL() {
        return SOAP_URL;
    }

    public void setSOAP_URL(String SOAP_URL) {
        this.SOAP_URL = SOAP_URL;
    }
}
