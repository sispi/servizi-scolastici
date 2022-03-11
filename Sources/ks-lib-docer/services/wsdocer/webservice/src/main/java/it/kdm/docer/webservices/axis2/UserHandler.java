package it.kdm.docer.webservices.axis2;

import it.kdm.docer.webservices.axis2.methods.MethodAdapter;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;

import java.util.HashMap;


/**
 * Created by Lorenzo Lucherini on 1/26/15.
 */
public class UserHandler extends AbstractHandler {

    private final static HashMap<String, MethodAdapter> classCache =
            new HashMap<String, MethodAdapter>();

    @Override
    public InvocationResponse invoke(MessageContext msgContext) throws AxisFault {

        String methodName = msgContext.getAxisOperation().getName().getLocalPart();
        StringBuilder builder = new StringBuilder();

        builder.append(Character.toUpperCase(methodName.charAt(0)));
        builder.append(methodName.substring(1));

        methodName = builder.toString();

        if (!classCache.containsKey(methodName)) {
            try {
                Class<?> klass = Class.forName("it.kdm.docer.webservices.axis2.methods." + methodName);
                classCache.put(methodName, (MethodAdapter) klass.newInstance());
            } catch (ClassNotFoundException e) {
                classCache.put(methodName, null);
            } catch (InstantiationException e) {
                //TODO: Handle error
            } catch (IllegalAccessException e) {
                //TODO: Handle error
            }
        }

        MethodAdapter method = classCache.get(methodName);
        if (method != null) {
            return method.invoke(msgContext);
        }

        return InvocationResponse.CONTINUE;
    }

}
