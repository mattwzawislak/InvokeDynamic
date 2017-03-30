package org.obicere.invoke;

import org.obicere.invoke.bootstrap.BootstrapMethod;

/**
 * @author Obicere
 */
public class Main {

    public static void main(final String[] args) {
        // add my "decryption" information
        BootstrapMethod.addDecryption("abc", "xyz");
        BootstrapMethod.addDecryption("abc1000", "a");

        // create the general method invoker
        final MethodInvoker method = MethodInvokerFactory.buildInvoker("a", Object.class);

        final Object result1 = method.invoke(new Object[]{"b", 'c'});
        System.out.println(result1);

        final Object result2 = method.invoke(new Object[]{"bc", 1000});
        System.out.println(result2);
    }

}
