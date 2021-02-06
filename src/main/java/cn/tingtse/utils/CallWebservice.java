package cn.tingtse.utils;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

import javax.xml.namespace.QName;

/**
 * @author enting_xie
 */
public class CallWebservice {
    public static String callWebService(String url,Object[] params,String methodName){
        String result = null;
        try{
            RPCServiceClient serviceClient = new RPCServiceClient();
            Options option = serviceClient.getOptions();
            EndpointReference targetEPR = new EndpointReference(url);
            option.setTo(targetEPR);

            Class[] classes = new Class[]{String.class};

            QName opAddEntry = new QName("",methodName);

            result=serviceClient.invokeBlocking(opAddEntry,params,classes)[0].toString();

            System.out.println(result);
        }catch (AxisFault e){
            e.printStackTrace();
        }
        return result;
    }

    public static String callWebService(String url,Object[] params,String methodName,String QName){
        String result = null;
        try{
            RPCServiceClient serviceClient = new RPCServiceClient();
            Options option = serviceClient.getOptions();
            EndpointReference targetEPR = new EndpointReference(url);
            option.setTo(targetEPR);

            Class[] classes = new Class[]{String.class};

            QName opAddEntry = new QName(QName,methodName);

            result=serviceClient.invokeBlocking(opAddEntry,params,classes)[0].toString();

            System.out.println(result);
        }catch (AxisFault e){
            e.printStackTrace();
        }
        return result;
    }

}
