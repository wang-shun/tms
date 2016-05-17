
package com.hoperun.framework.sms;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.hoperun.shinho.sms package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.hoperun.shinho.sms
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SendSMS }
     * 
     */
    public SendSMS createSendSMS() {
        return new SendSMS();
    }

    /**
     * Create an instance of {@link SendSMSResponse }
     * 
     */
    public SendSMSResponse createSendSMSResponse() {
        return new SendSMSResponse();
    }

    /**
     * Create an instance of {@link SendYXSMS }
     * 
     */
    public SendYXSMS createSendYXSMS() {
        return new SendYXSMS();
    }

    /**
     * Create an instance of {@link SendYXSMSResponse }
     * 
     */
    public SendYXSMSResponse createSendYXSMSResponse() {
        return new SendYXSMSResponse();
    }

}
