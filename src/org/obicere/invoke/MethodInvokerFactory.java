package org.obicere.invoke;

import org.obicere.invoke.bootstrap.BootstrapMethod;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author Obicere
 */
public class MethodInvokerFactory {

    public static MethodInvoker buildInvoker(final String dynamicName, final Class<?> returnType, final Class<?>... argumentTypes) {
        return objects -> {
            try {
                // this emulates code similar to the bytecode
                /*
                // push first parameter onto stack
                aload objects
                bipush 0
                aaload
                // push second parameter onto stack
                aload objects
                bipush 1
                aaload
                ...
                // push last parameter onto stack
                aload objects
                bipush n - 1
                aaload

                // call the invoke dynamic and create the callsite
                invokedynamic {
                    invokestatic {
                        BootstrapMethod.resolve(MethodHandles.Lookup, String, MethodType, Object[])
                    }
                    dynamicMethodName
                    type
                }
                // call the parameter-less method
                invokevirtual
                // return the result
                areturn
                 */
                final MethodHandles.Lookup lookup = MethodHandles.lookup();
                final MethodType type = MethodType.methodType(returnType, argumentTypes);
                final CallSite callSite = BootstrapMethod.resolve(lookup, dynamicName, type, objects);

                return callSite.getTarget().invoke();
            } catch (final Throwable t) {
                t.printStackTrace();
                return null;
            }
        };
    }
}
