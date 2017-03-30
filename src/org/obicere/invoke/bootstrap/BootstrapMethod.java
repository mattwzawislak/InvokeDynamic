package org.obicere.invoke.bootstrap;

import org.obicere.invoke.impl.StaticMethods;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Obicere
 */
public class BootstrapMethod {

    private static final Map<String, String> securityMap = new HashMap<>();

    public static CallSite resolve(final MethodHandles.Lookup callerClass, final String dynamicMethodName, final MethodType dynamicMethodType, final Object... extraArguments) {
        // print out some basic information about the arguments

        System.out.println("Bootstrap method was called:");
        System.out.println("\tCalled from: " + callerClass.lookupClass());
        System.out.println("\tLooking for method with access: 0x" + Integer.toHexString(callerClass.lookupModes()));
        System.out.println("\tDynamic method name: " + dynamicMethodName);
        System.out.println("\tDynamic method type: " + dynamicMethodType.toMethodDescriptorString());
        System.out.println("\tExtra arguments: ");
        for (final Object object : extraArguments) {
            System.out.println("\t\t" + String.valueOf(object));
        }

        try {
            System.out.println("Providing caller");
            // some super duper secure decryption thing to hide methods
            final String encode = encode(dynamicMethodName, extraArguments);
            final String decrypted = decrypt(encode);

            // use the decrypted name as the resulting method name
            MethodHandle handle = callerClass.findStatic(StaticMethods.class, decrypted, dynamicMethodType);

            // make sure shit is gucci
            if (!handle.type().equals(dynamicMethodType)) {
                System.out.println("Type mismatch, casting caller type");
                handle = handle.asType(dynamicMethodType);
            }

            return new ConstantCallSite(handle);
        } catch (final Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    public static void addDecryption(final String a, final String b) {
        securityMap.put(a, b);
    }

    private static String decrypt(final String input) {
        return securityMap.get(input);
    }

    private static String encode(final String dynamicMethodName, final Object... extraArguments) {
        // using the dynamicMethodName and extraArguments to find the name
        // of the static method we want to invoke

        // for simplicity, we just appending them all together (as strings)

        if (extraArguments.length == 0) {
            return dynamicMethodName;
        }
        final StringBuilder builder = new StringBuilder(dynamicMethodName);
        for (final Object obj : extraArguments) {
            builder.append(String.valueOf(obj));
        }
        return builder.toString();
    }
}
