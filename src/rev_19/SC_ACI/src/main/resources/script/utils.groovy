/*
 The integration developer needs to create the method processData 
 This method takes Message object of package com.sap.gateway.ip.core.customdev.util 
which includes helper methods useful for the content developer:
The methods available are:
    public java.lang.Object getBody()
	public void setBody(java.lang.Object exchangeBody)
    public java.util.Map<java.lang.String,java.lang.Object> getHeaders()
    public void setHeaders(java.util.Map<java.lang.String,java.lang.Object> exchangeHeaders)
    public void setHeader(java.lang.String name, java.lang.Object value)
    public java.util.Map<java.lang.String,java.lang.Object> getProperties()
    public void setProperties(java.util.Map<java.lang.String,java.lang.Object> exchangeProperties) 
    public void setProperty(java.lang.String name, java.lang.Object value)
    public java.util.List<com.sap.gateway.ip.core.customdev.util.SoapHeader> getSoapHeaders()
    public void setSoapHeaders(java.util.List<com.sap.gateway.ip.core.customdev.util.SoapHeader> soapHeaders) 
       public void clearSoapHeaders()
 */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.*;
import java.util.regex.*;

// Catch the exception and set it as body
def Message logException(Message message) {
    // get a map of iflow properties
    def map = message.getProperties();
    
    // Get SAPJMSRetries of the artifact
    def headers = message.getHeaders()
    def retryNumber = headers.get("SAPJMSRetries");;
    
    // get an exception java class instance
    def ex = map.get("CamelExceptionCaught");
    if (ex!=null) {
        
        if (retryNumber == 0 || retryNumber == null) {

            def exceptionBody = ex.toString();
            
            // save the error response as a message attachment 
            def messageLog = messageLogFactory.getMessageLog(message);
            messageLog.addAttachmentAsString(ex.getClass().getCanonicalName(), exceptionBody, "text/plain");

            // copy the error response to the message body
            message.setBody(exceptionBody);
        }     
    }

    return message;
}


//Log the payload as attachment if log level debug or trace
def Message logPayloadWithLogLevel(Message message, String payloadDesc, String isLogLevelrelevant, String isXML) {
    def body = message.getBody(java.lang.String) as String;
    
    // Get SAPJMSRetries of the artifact
    def headers = message.getHeaders()
    def retryNumber = headers.get("SAPJMSRetries");;
    
    def headersAsString ="\n";
	    headers.each{ it -> headersAsString = headersAsString + "${it}" + "\n" };

    def properties = message.getProperties() 
    def propertiesAsString ="\n";
	    properties.each{ it -> propertiesAsString = propertiesAsString + "${it}" + "\n" };
    
    
    // Get LogLevel of the artifact
    def map = message.getProperties();
	def logConfig = map.get("SAP_MessageProcessingLogConfiguration");
	def logLevel = (String) logConfig.logLevel;
	
    def messageLog = messageLogFactory.getMessageLog(message);
    if(messageLog != null){
        
        if (retryNumber == 0 || retryNumber == null) {

            if(isLogLevelrelevant.toUpperCase().equals("TRUE")) {

                // Only log when LogLevel of iFlow == Debug || Trace
                if(logLevel.equals("DEBUG") || logLevel.equals("TRACE")) {
                    
                    if(isXML.toUpperCase().equals("TRUE")) {
                        def bodyNice = XmlUtil.serialize(body); // Make XML fancy    
                        // messageLog.addAttachmentAsString(payloadDesc, bodyNice , "text/plain");
                        
                        messageLog.addAttachmentAsString(payloadDesc, 
                            "\n Properties \n ----------   \n" + propertiesAsString +
                            "\n Headers \n ----------   \n" + headersAsString +
                            "\n Body \n ----------  \n\n" + bodyNice, "text/plain");
                        
                        
                    } else {
                        // messageLog.addAttachmentAsString(payloadDesc, body , "text/plain");
                        
                        messageLog.addAttachmentAsString(payloadDesc, 
                            "\n Properties \n ----------   \n" + propertiesAsString +
                            "\n Headers \n ----------   \n" + headersAsString +
                            "\n Body \n ----------  \n\n" + body, "text/plain");

                    }

                } // Here it would be possible to add logging alternatives in case of other log levels
            
            } else {
                if(isXML.toUpperCase().equals("TRUE")) {
                    def bodyNice = XmlUtil.serialize(body); // Make XML fancy    
                    // messageLog.addAttachmentAsString(payloadDesc, bodyNice , "text/plain");
                    
                    messageLog.addAttachmentAsString(payloadDesc, 
                        //"\n Properties \n ----------   \n" + propertiesAsString +
                        //"\n Headers \n ----------   \n" + headersAsString +
                        //"\n Body \n ----------  \n\n" + 
                        bodyNice, "text/plain");
                    
                } else {
                    // messageLog.addAttachmentAsString(payloadDesc, body , "text/plain");
                    
                    messageLog.addAttachmentAsString(payloadDesc, 
                        //"\n Properties \n ----------   \n" + propertiesAsString +
                        //"\n Headers \n ----------   \n" + headersAsString +
                        //"\n Body \n ----------  \n\n" + 
                        body, "text/plain");
                }
            }
        }
    }

    return message;
}


//log Text Input if log level debug or trace
//def Message logPayloadWithLogLevel(Message message, String payloadDesc, String isLogLevelrelevant, String isXML)
def Message logXMLInput(Message message) {
    message = logPayloadWithLogLevel(message, "InboundMessageXML", "TRUE", "TRUE");
    return message;
}

//log Text Output if log level debug or trace
//def Message logPayloadWithLogLevel(Message message, String payloadDesc, String isLogLevelrelevant, String isXML)
def Message logXMLOutput(Message message) {
    message = logPayloadWithLogLevel(message, "OutboundMessageXML", "TRUE", "TRUE");
    return message;
}


//log XML Request if log level debug or trace
//def Message logPayloadWithLogLevel(Message message, String payloadDesc, String isLogLevelrelevant, String isXML)
def Message logXMLRequest(Message message) {
    message = logPayloadWithLogLevel(message, "RequestMessageXML", "TRUE", "TRUE");
    return message;
}

//log XML Response if log level debug or trace
//def Message logPayloadWithLogLevel(Message message, String payloadDesc, String isLogLevelrelevant, String isXML)
def Message logXMLResponse(Message message) {
    message = logPayloadWithLogLevel(message, "ResponseMessageXML", "TRUE", "TRUE");
    return message;
}

//log Text Input if log level debug or trace
//def Message logPayloadWithLogLevel(Message message, String payloadDesc, String isLogLevelrelevant, String isXML)
def Message logTextInput(Message message) {
    message = logPayloadWithLogLevel(message, "InboundMessageText", "TRUE", "FALSE");
    return message;
}

//log Text Output if log level debug or trace
//def Message logPayloadWithLogLevel(Message message, String payloadDesc, String isLogLevelrelevant, String isXML)
def Message logTextOutput(Message message) {
    message = logPayloadWithLogLevel(message, "OutboundMessageText", "TRUE", "FALSE");
    return message;
}


//log Text Input in error case for any log level
def Message logTextInputErrorCase(Message message) {
    message = logPayloadWithLogLevel(message, "InboundMessageTextErrorCase", "FALSE", "FALSE");
    return message;
}


//log Text Input in error case for any log level
def Message logTextRequestErrorCase(Message message) {
    message = logPayloadWithLogLevel(message, "RequestMessageTextErrorCase", "FALSE", "FALSE");
    return message;
}

//log Text Input in error case for any log level
def Message logTextResponseErrorCase(Message message) {
    message = logPayloadWithLogLevel(message, "ResponseMessageTextErrorCase", "FALSE", "FALSE");
    return message;
}

//log XML Input in error case  any log level
//def Message logPayloadWithLogLevel(Message message, String payloadDesc, String isLogLevelrelevant, String isXML)
def Message logXMLInputErrorCase(Message message) {
    message = logPayloadWithLogLevel(message, "InboundMessageXMLErrorCase", "FALSE", "TRUE");
    return message;
}

//log XML Input in error case  any log level
//def Message logPayloadWithLogLevel(Message message, String payloadDesc, String isLogLevelrelevant, String isXML)
def Message logFailedRecordsErrorCase(Message message) {
    message = logPayloadWithLogLevel(message, "FailedRecordsErrorCase", "FALSE", "FALSE");
    return message;
}



def Message throwCustomException(Message message) {
	
	def messageLog = messageLogFactory.getMessageLog(message);
	
	throw new Exception("Custom Exception!");  	
	
	return message;
}


def Message saveProductCount(Message message) {
    String totalCount = message.getBody(java.lang.String);
    message.setProperty("totalCount",totalCount)
    
    if (totalCount == "0") {
        message.setProperty("exitFlag", "true");
    }
    else {
        message.setProperty("exitFlag", "false");
    }
    return message;
}

def Message updatePagination(Message message) {
    def map = message.getProperties();
    def exitFlag = "false"
    
    def itemsPerCall = map.get("itemsPerCall") 
    def skipVal = map.get("skipVal")
    def totalCount = map.get("totalCount")
    
    def tempSkipVal = skipVal.toInteger() + itemsPerCall.toInteger()
    if(totalCount.toInteger()/tempSkipVal <= 1) 
    exitFlag = "true"
    
    skipVal = tempSkipVal.toString()
    
    message.setProperty("exitFlag",exitFlag);
    message.setProperty("skipVal", skipVal);
    
    return message;
}

def Message saveLogIdAsCustomHeader(Message message) {
    
	def messageLog = messageLogFactory.getMessageLog(message);
	if(messageLog != null){

		def logID = message.getProperties().get("logID");		
		if(logID!=null){
			messageLog.addCustomHeaderProperty("logID", logID);		
        }
	}
	return message;
}

def Message setEndDate(Message message) {
    def map = message.getProperties();
     message.setProperty("endDateSCP", java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC)
      .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")));
    
    return message;
}

def Message throwRojoException(Message message){
    throw new Exception("SF Adapter Bulk Call contained failed requests");  
    return message
}

//def Message DeleteDeclaration(Message message) {
  //      def body = message.getBody();
    //    message.setBody(body.split('\n').drop(1).join('\n'))
      // return message;
//}

def Message DeleteNodes(Message message) {
    //Body 
       def body = message.getBody(java.lang.String);
       
        body = body.replace("<ns0:Messages xmlns:ns0=\"http://sap.com/xi/XI/SplitAndMerge\">", "")
        
        body = body.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "")
        
        body = body.replace("<ns0:Message1>", "")
        
        body = body.replace("</ns0:Message1>", "")
        
        body = body.replace("</ns0:Messages>", "")
        
        message.setBody(body)
        
       return message;
}