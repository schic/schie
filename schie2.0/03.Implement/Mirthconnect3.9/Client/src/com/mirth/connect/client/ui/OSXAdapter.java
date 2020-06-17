/*
 * 
 * File: OSXAdapter.java
 * 
 * Abstract: Hooks existing preferences/about/quit functionality from an existing Java app into
 * handlers for the Mac OS X application menu. Uses a Proxy object to dynamically implement the
 * com.apple.eawt.ApplicationListener interface and register it with the com.apple.eawt.Application
 * object. This allows the complete project to be both built and run on any platform without any
 * stubs or placeholders. Useful for developers looking to implement Mac OS X features while
 * supporting multiple platforms with minimal impact.
 * 
 * Version: 2.0
 * 
 * Disclaimer: IMPORTANT: This Apple software is supplied to you by Apple Inc. ("Apple") in
 * consideration of your agreement to the following terms, and your use, installation, modification
 * or redistribution of this Apple software constitutes acceptance of these terms. If you do not
 * agree with these terms, please do not use, install, modify or redistribute this Apple software.
 * 
 * In consideration of your agreement to abide by the following terms, and subject to these terms,
 * Apple grants you a personal, non-exclusive license, under Apple's copyrights in this original
 * Apple software (the "Apple Software"), to use, reproduce, modify and redistribute the Apple
 * Software, with or without modifications, in source and/or binary forms; provided that if you
 * redistribute the Apple Software in its entirety and without modifications, you must retain this
 * notice and the following text and disclaimers in all such redistributions of the Apple Software.
 * Neither the name, trademarks, service marks or logos of Apple Inc. may be used to endorse or
 * promote products derived from the Apple Software without specific prior written permission from
 * Apple. Except as expressly stated in this notice, no other rights or licenses, express or
 * implied, are granted by Apple herein, including but not limited to any patent rights that may be
 * infringed by your derivative works or by other works in which the Apple Software may be
 * incorporated.
 * 
 * The Apple Software is provided by Apple on an "AS IS" basis. APPLE MAKES NO WARRANTIES, EXPRESS
 * OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT,
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND
 * OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
 * 
 * IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION,
 * MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY
 * OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Copyright (c) 2003-2007 Apple, Inc., All Rights Reserved
 * 
 */

package com.mirth.connect.client.ui;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.glassfish.jersey.internal.util.ReflectionHelper;

public class OSXAdapter implements InvocationHandler {

    protected Object targetObject;
    protected Method targetMethod;
    protected String proxySignature;

    static Object macOSXApplication;

    // Pass this method an Object and Method equipped to perform application shutdown logic
    // The method passed should return a boolean stating whether or not the quit should occur
    public static void setQuitHandler(final Object target, final Method quitHandler) {
        try {
            if (isJava9OrGreater()) {
                InvocationHandler invocationHandler = new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (method.getName().equals(Messages.getString("OSXAdapter.0"))) { //$NON-NLS-1$
                            boolean quit = (boolean) quitHandler.invoke(target);
                            Object quitResponse = args[1];
                            quitResponse.getClass().getMethod(quit ? Messages.getString("OSXAdapter.1") : Messages.getString("OSXAdapter.2")).invoke(quitResponse); //$NON-NLS-1$ //$NON-NLS-2$
                        }
                        return null;
                    }
                };

                setHandler(invocationHandler, Messages.getString("OSXAdapter.3"), Messages.getString("OSXAdapter.4")); //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                setHandler(new OSXAdapter(Messages.getString("OSXAdapter.5"), target, quitHandler)); //$NON-NLS-1$
            }
        } catch (Exception ex) {
            System.err.println(Messages.getString("OSXAdapter.6")); //$NON-NLS-1$
            ex.printStackTrace();
        }
    }

    // Pass this method an Object and Method equipped to display application info
    // They will be called when the About menu item is selected from the application menu
    public static void setAboutHandler(Object target, Method aboutHandler) {
        boolean enableAboutMenu = (target != null && aboutHandler != null);

        try {
            if (isJava9OrGreater()) {
                if (enableAboutMenu) {
                    setHandler(new OSXAdapter(Messages.getString("OSXAdapter.7"), target, aboutHandler), Messages.getString("OSXAdapter.8"), Messages.getString("OSXAdapter.9")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
            } else {
                if (enableAboutMenu) {
                    setHandler(new OSXAdapter(Messages.getString("OSXAdapter.10"), target, aboutHandler)); //$NON-NLS-1$
                }
                // If we're setting a handler, enable the About menu item by calling
                // com.apple.eawt.Application reflectively
                Method enableAboutMethod = macOSXApplication.getClass().getDeclaredMethod(Messages.getString("OSXAdapter.11"), new Class[] { //$NON-NLS-1$
                        boolean.class });
                enableAboutMethod.invoke(macOSXApplication, new Object[] {
                        Boolean.valueOf(enableAboutMenu) });
            }
        } catch (Exception ex) {
            System.err.println(Messages.getString("OSXAdapter.12")); //$NON-NLS-1$
            ex.printStackTrace();
        }
    }

    // Pass this method an Object and a Method equipped to display application options
    // They will be called when the Preferences menu item is selected from the application menu
    public static void setPreferencesHandler(Object target, Method prefsHandler) {
        boolean enablePrefsMenu = (target != null && prefsHandler != null);
        try {
            if (isJava9OrGreater()) {
                if (enablePrefsMenu) {
                    setHandler(new OSXAdapter(Messages.getString("OSXAdapter.13"), target, prefsHandler), Messages.getString("OSXAdapter.14"), Messages.getString("OSXAdapter.15")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
            } else {
                if (enablePrefsMenu) {
                    setHandler(new OSXAdapter(Messages.getString("OSXAdapter.16"), target, prefsHandler)); //$NON-NLS-1$
                }
                // If we're setting a handler, enable the Preferences menu item by calling
                // com.apple.eawt.Application reflectively
                Method enablePrefsMethod = macOSXApplication.getClass().getDeclaredMethod(Messages.getString("OSXAdapter.17"), new Class[] { //$NON-NLS-1$
                        boolean.class });
                enablePrefsMethod.invoke(macOSXApplication, new Object[] {
                        Boolean.valueOf(enablePrefsMenu) });
            }
        } catch (Exception ex) {
            System.err.println(Messages.getString("OSXAdapter.18")); //$NON-NLS-1$
            ex.printStackTrace();
        }
    }

    // Pass this method an Object and a Method equipped to handle document events from the Finder
    // Documents are registered with the Finder via the CFBundleDocumentTypes dictionary in the 
    // application bundle's Info.plist
    public static void setFileHandler(final Object target, final Method fileHandler) {
        try {
            if (isJava9OrGreater()) {
                InvocationHandler invocationHandler = new InvocationHandler() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (method.getName().equals(Messages.getString("OSXAdapter.19"))) { //$NON-NLS-1$
                            Object event = args[0];
                            List<File> files = (List<File>) event.getClass().getMethod(Messages.getString("OSXAdapter.20")).invoke(event); //$NON-NLS-1$
                            String searchTerm = (String) event.getClass().getMethod(Messages.getString("OSXAdapter.21")).invoke(event); //$NON-NLS-1$
                            fileHandler.invoke(target, files, searchTerm);
                        }
                        return null;
                    }
                };

                setHandler(invocationHandler, Messages.getString("OSXAdapter.22"), Messages.getString("OSXAdapter.23")); //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                setHandler(new OSXAdapter(Messages.getString("OSXAdapter.24"), target, fileHandler) { //$NON-NLS-1$
                    // Override OSXAdapter.callTarget to send information on the
                    // file to be opened
                    public boolean callTarget(Object appleEvent) {
                        if (appleEvent != null) {
                            try {
                                Method getFilenameMethod = appleEvent.getClass().getDeclaredMethod(Messages.getString("OSXAdapter.25"), (Class[]) null); //$NON-NLS-1$
                                String filename = (String) getFilenameMethod.invoke(appleEvent, (Object[]) null);
                                this.targetMethod.invoke(this.targetObject, new Object[] {
                                        filename });
                            } catch (Exception ex) {

                            }
                        }
                        return true;
                    }
                });
            }
        } catch (Exception ex) {
            System.err.println(Messages.getString("OSXAdapter.26")); //$NON-NLS-1$
            ex.printStackTrace();
        }
    }

    // setHandler creates a Proxy object from the passed OSXAdapter and adds it as an ApplicationListener
    public static void setHandler(OSXAdapter adapter) {
        try {
            Object application = getApplication();
            Class applicationListenerClass = Class.forName(Messages.getString("OSXAdapter.27")); //$NON-NLS-1$
            Method addListenerMethod = application.getClass().getDeclaredMethod(Messages.getString("OSXAdapter.28"), new Class[] { //$NON-NLS-1$
                    applicationListenerClass });
            // Create a proxy object around this handler that can be reflectively added as an Apple ApplicationListener
            Object osxAdapterProxy = Proxy.newProxyInstance(OSXAdapter.class.getClassLoader(), new Class[] {
                    applicationListenerClass }, adapter);
            addListenerMethod.invoke(application, new Object[] { osxAdapterProxy });
        } catch (ClassNotFoundException cnfe) {
            System.err.println(Messages.getString("OSXAdapter.29") + cnfe + Messages.getString("OSXAdapter.30")); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (Exception ex) { // Likely a NoSuchMethodException or an IllegalAccessException loading/invoking eawt.Application methods
            System.err.println(Messages.getString("OSXAdapter.31")); //$NON-NLS-1$
            ex.printStackTrace();
        }
    }

    // Each OSXAdapter has the name of the EAWT method it intends to listen for (handleAbout, for example),
    // the Object that will ultimately perform the task, and the Method to be called on that Object
    protected OSXAdapter(String proxySignature, Object target, Method handler) {
        this.proxySignature = proxySignature;
        this.targetObject = target;
        this.targetMethod = handler;
    }

    // Override this method to perform any operations on the event 
    // that comes with the various callbacks
    // See setFileHandler above for an example
    public boolean callTarget(Object appleEvent) throws InvocationTargetException, IllegalAccessException {
        Object result = targetMethod.invoke(targetObject, (Object[]) null);
        if (result == null) {
            return true;
        }
        return Boolean.valueOf(result.toString()).booleanValue();
    }

    // InvocationHandler implementation
    // This is the entry point for our proxy object; it is called every time an ApplicationListener method is invoked
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isCorrectMethod(method, args)) {
            boolean handled = callTarget(args[0]);
            setApplicationEventHandled(args[0], handled);
        }
        // All of the ApplicationListener methods are void; return null regardless of what happens
        return null;
    }

    // Compare the method that was called to the intended method when the OSXAdapter instance was created
    // (e.g. handleAbout, handleQuit, handleOpenFile, etc.)
    protected boolean isCorrectMethod(Method method, Object[] args) {
        return (targetMethod != null && proxySignature.equals(method.getName()) && args.length == 1);
    }

    // It is important to mark the ApplicationEvent as handled and cancel the default behavior
    // This method checks for a boolean result from the proxy method and sets the event accordingly
    protected void setApplicationEventHandled(Object event, boolean handled) {
        if (event != null && !isJava9OrGreater()) {
            try {
                Method setHandledMethod = event.getClass().getDeclaredMethod(Messages.getString("OSXAdapter.32"), new Class[] { //$NON-NLS-1$
                        boolean.class });
                // If the target method returns a boolean, use that as a hint
                setHandledMethod.invoke(event, new Object[] { Boolean.valueOf(handled) });
            } catch (Exception ex) {
                System.err.println(Messages.getString("OSXAdapter.33") + event); //$NON-NLS-1$
                ex.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected static Object getApplication() throws Exception {
        if (macOSXApplication == null) {
            Class applicationClass = Class.forName(Messages.getString("OSXAdapter.34")); //$NON-NLS-1$

            if (isJava9OrGreater()) {
                macOSXApplication = applicationClass.getMethod(Messages.getString("OSXAdapter.35")).invoke(null); //$NON-NLS-1$
            } else {
                macOSXApplication = applicationClass.getConstructor((Class[]) null).newInstance((Object[]) null);
            }
        }

        return macOSXApplication;
    }

    protected static void setHandler(InvocationHandler adapter, String interfaceName, String applicationSetter) throws Exception {
        Class<?> handlerInterface = Class.forName(interfaceName);
        Object handlerImpl = Proxy.newProxyInstance(AccessController.doPrivileged(ReflectionHelper.getClassLoaderPA(handlerInterface)), new Class[] {
                handlerInterface }, adapter);
        Object application = getApplication();
        application.getClass().getMethod(applicationSetter, handlerInterface).invoke(application, handlerImpl);
    }

    protected static boolean isJava9OrGreater() {
        String version = System.getProperty(Messages.getString("OSXAdapter.36")); //$NON-NLS-1$

        int index = version.indexOf('-');
        if (index > 0) {
            version = version.substring(0, index);
        }

        index = version.indexOf('.');
        if (index > 0) {
            version = version.substring(0, index);
        }

        return NumberUtils.toDouble(version) >= 9;
    }
}