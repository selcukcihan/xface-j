/*  This java class is distributed under the BSD license.
 *
 * Copyright 2005 Lilian Chamontin.
 * contact lilian.chamontin at f r e e . f r
 */

package com.selcukcihan.android.xface;

import java.awt.BorderLayout;
import java.net.URL;
import java.applet.AppletStub;

import javax.media.opengl.GLDrawableFactory;
import javax.swing.SwingUtilities;
import java.applet.AppletContext;
import java.awt.Color;
import java.applet.Applet;
import java.io.IOException;
import java.awt.Panel;
import java.net.MalformedURLException;
import java.awt.Label;
import java.io.File;
import java.net.URLConnection;
import java.awt.Graphics;
import java.util.jar.*;
import java.io.*;
//import net.java.games.jogl.*;
import java.security.cert.*;
import java.util.Enumeration;
import javax.swing.*;


/** Basic JOGL installer for Applets.
 *
 *<p> Sample applet code :
 * <pre>
 *
 * <applet code="net.java.games.jogl.applets.JOGLAppletInstaller"
 *      width=600
 *      height=400
 *      codebase="/lib"
 *      archive="jogl.jar,your_applet.jar"
 *    >
 *    <param name="subapplet.classname" VALUE="untrusted.JOGLApplet">
 *    </applet>
 *
 * </pre>
 *
 *  This version uses jar verification and allows the native jar to be excluded from
 * the archive list of the applet tag.
 *
 *
 * */
public class JOGLAppletLauncher extends Applet {
  static {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ignore) {
    }
  }

  /** denotes an applet executed in a Windows System */
    private static final int WINDOWS = 0;

    /** denotes an applet executed on Mac OS*/
    private static final int MAC_OS = 1;

    /** denotes an applet executed on Linux*/
    private static final int LINUX = 2;


    /** native libraries location */
    private static final String [] nativeLibsJarNames = {
        "jogl-natives-win32.jar",
        "jogl-natives-macosx.jar",
        "jogl-natives-linux.jar",
    };

    /** native libraries file names */
    private static final String [] nativeLibsFileNames = {
        "jogl.dll",
        "libjogl.jnilib",
        "libjogl.so",
    };

    /** Current os the applet is running on */
    private int osType;

    /** The applet we have to start */
    private Applet subApplet;

    private String subAppletClassName; // from applet PARAM
    private String subAppletDisplayName; // from applet PARAM
    /** URL string to an image used while installing */
    private String subAppletImageName; // from applet PARAM

    private String installDirectory; // (defines a private directory for native libs)

    private JPanel loaderPanel = new JPanel(new BorderLayout());

    private JProgressBar progressBar = new JProgressBar(0,100);

    private boolean isInitOk = false;

    /** false once start() has been invoked */
    private boolean firstStart = true;

    /** true if joglStart() has passed successfully */
    private boolean joglStarted = false;


    public JOGLAppletLauncher() {
    }


    /** Applet initialization */
    public void init()  {

      this.subAppletClassName = getParameter("subapplet.classname");
      if (subAppletClassName == null){
        displayError("Init failed : Missing subapplet.classname argument");
        return;
      }
      this.subAppletDisplayName = getParameter("subapplet.displayname");
      if (subAppletDisplayName == null){
        subAppletDisplayName = "Applet";
      }

      this.subAppletImageName = getParameter("subapplet.image");

      initLoaderLayout();
      validate();

      String codeBase = getCodeBase().toExternalForm().substring(7); // minus http://

      this.installDirectory = codeBase.replace(':', '_')
          .replace('.', '_').replace('/', '_'); // clean up the name

      String osName = System.getProperty("os.name").toLowerCase();

      if (osName.startsWith("win")){
        osType = WINDOWS;
      } else if (osName.startsWith("mac")){
        osType = MAC_OS;
      } else if (osName.startsWith("linux")){
        osType = LINUX;
      } else {
        displayError("Init failed : Unsupported os ( " + osName + " )");
        return;
      }
      this.isInitOk = true;
    }

    private void displayMessage(String message){
      progressBar.setString(message);
    }
    private void displayError(String errorMessage){
      progressBar.setString("Error : " + errorMessage);
    }

    private void initLoaderLayout(){
      setLayout(new BorderLayout());
      progressBar.setBorderPainted(true);
      progressBar.setStringPainted(true);
      progressBar.setString("Loading...");
      boolean includeImage = false;
      ImageIcon image = null;
      if (subAppletImageName != null){
        try {
          image = new ImageIcon(new URL(subAppletImageName));
          includeImage = true;
        } catch (MalformedURLException ex) {
          ex.printStackTrace();
          // not blocking
        }
      }
      if (includeImage){
         add(loaderPanel, BorderLayout.SOUTH);
         loaderPanel.add(new JLabel(image), BorderLayout.CENTER);
         loaderPanel.add(progressBar, BorderLayout.SOUTH);
      } else {
        add(loaderPanel, BorderLayout.SOUTH);
        loaderPanel.add(progressBar, BorderLayout.CENTER);
      }
    }


    /** start asynchroneous loading of libraries if needed */
    public void start(){
      if (isInitOk){
        if (firstStart) {
          firstStart = false;
          String userHome = System.getProperty("user.home");
          String installDirName = userHome + File.separator + ".jogl_ext"
              + File.separator + installDirectory; // should manage jogl versions here

          final File installDir = new File(installDirName);

          Thread refresher = new Thread() {
            public void run() {
              refreshJOGL(installDir);
            }
          };
          refresher.setPriority(Thread.NORM_PRIORITY - 1);
          refresher.start();
        } else if (joglStarted) {
          // we have to start again the applet (start can be called multiple times,
          // e.g once per tabbed browsing
          subApplet.start();
        }
      }
    }

    public void stop(){
      if (subApplet != null){
        subApplet.stop();
      }
    }

    public void destroy(){
      if (subApplet != null){
        subApplet.destroy();
      }
    }


    /** This method is executed from outside the Event Dispatch Thread, and installs
     *  the required native libraries in the local folder.
     *  */
    private void refreshJOGL(final File installDir){
      try {
        Class subAppletClass = Class.forName(subAppletClassName);
        // this will block until the applet jar is downloaded
      } catch (ClassNotFoundException cnfe){
        displayError("Start failed : class not found : " + subAppletClassName);
      }

      if (!installDir.exists()){
        installDir.mkdirs();
      }

      String libURLName = nativeLibsJarNames[osType];
      // first, let's get the manifest file from jogl.jar

      URL nativeLibURL;
      URLConnection urlConnection;
      String path = getCodeBase().toExternalForm() + libURLName;
      try {
        nativeLibURL = new URL(path);
        urlConnection = nativeLibURL.openConnection();
      } catch (Exception e){
        e.printStackTrace();
        displayError("Couldn't access the native lib URL : " + path);
        return;
      }

      // the timestamp used to determine if we have to download the native again
      long lastModified = urlConnection.getLastModified();

      File localNativeFile = new File(installDir, nativeLibsFileNames[osType]);
      boolean needsRefresh = (!localNativeFile.exists()) || localNativeFile.lastModified() != lastModified;

      if (needsRefresh){
        displayMessage("Updating local version of the native libraries");
        // first download the full jar locally
        File localJarFile = new File(installDir, nativeLibsJarNames[osType]);
        try {
          saveNativesJarLocally(localJarFile, urlConnection);
        } catch (IOException ioe) {
          ioe.printStackTrace();
          displayError("Unable to install the native file locally");
          return;
        }

        InputStream is = null;
        BufferedOutputStream out = null;
        try {
          JarFile jf = new JarFile(localJarFile);

          JarEntry nativeLibEntry = findNativeEntry(jf);

          if (nativeLibEntry == null) {
            displayError("native library not found in jar file");
          } else {
            is = jf.getInputStream(nativeLibEntry);
            int totalLength = (int) nativeLibEntry.getSize();
            // now we install the file
            try {
              out = new BufferedOutputStream(new FileOutputStream(localNativeFile));
            } catch (FileNotFoundException ex) {
              ex.printStackTrace(); // shouldn't happen
              return;
            }

            byte[] buffer = new byte[1024];
            int sum = 0;
            int len;
            try {
              while ( (len = is.read(buffer)) > 0) {
                out.write(buffer, 0, len);
                sum += len;
                int percent = (100 * sum / totalLength);
                displayMessage("Installing native files");
                progressBar.setValue(percent);
              }
              displayMessage("Download complete");
            } catch (IOException ioe) {
              ioe.printStackTrace();
              displayMessage("An error has occured during native library download");
              return;
            } finally {
              if (out != null) {
                try {
                  out.close();
                } catch (IOException ignore) {
                }
              }
            }

            if (checkNativeCertificates(nativeLibEntry.getCertificates())) {
              // security check complete
              localNativeFile.setLastModified(lastModified); // same timestamp

              loadNativesAndStart(localNativeFile);

            } else {
              displayError("The native librairies aren't properly signed");
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          // close the files
          if (is != null){
            try {
              is.close();
            } catch (IOException ignore) {
            }
          }

        }
      } else {
        loadNativesAndStart(localNativeFile);
      }
      //System.out.println("classpath is : "  + classpath);

    }
    private void saveNativesJarLocally(File localJarFile,
        URLConnection urlConnection) throws IOException {
      BufferedOutputStream out = null;;
      InputStream in = null;
      displayMessage("Downloading native library");
      progressBar.setValue(0);
      try {
        out = new BufferedOutputStream(new
            FileOutputStream(localJarFile));
        int totalLength = urlConnection.getContentLength();
        in = urlConnection.getInputStream();
        byte[] buffer = new byte[1024];
        int len;
        int sum = 0;
        while ( (len = in.read(buffer)) > 0) {
          out.write(buffer, 0, len);
          sum += len;
          int percent = (100 * sum / totalLength);
          progressBar.setValue(percent);
        }
        out.close();
        in.close();
      } finally {
        // close the files
        if (out != null) {
          try {
            out.close();
          } catch (IOException ignore) {
          }
        }
        if (in != null) {
          try {
            in.close();
          } catch (IOException ignore) {
          }
        }
      }
    }

    private JarEntry findNativeEntry(JarFile jf){
      // next : find the native lib entry
      Enumeration e = jf.entries();
      while (e.hasMoreElements()) {
        JarEntry entry = (JarEntry) e.nextElement();
        if (entry.getName().equals(nativeLibsFileNames[osType])) {
          return entry;
        }
      }
      return null;
    }

    /** checking the native certificates with the jogl ones (all must match)*/
    private boolean checkNativeCertificates(Certificate[] nativeCerts){
      // locate the JOGL certificates
      Certificate[] joglCerts = GLDrawableFactory.class.getProtectionDomain().
      getCodeSource().getCertificates();

      if (nativeCerts == null || nativeCerts.length == 0){
        return false;
      }
      int checked = 0;
      for (int i = 0; i < joglCerts.length; i++) {
        for (int j = 0; j < nativeCerts.length; j++) {
          if (nativeCerts[j].equals(joglCerts[i])){
            checked++;
            break;
          }
        }
      }
      return  (checked == joglCerts.length);
    }

    /** last step before launch : System.load() the native and init()/start() the child applet  */
    private void loadNativesAndStart(final File nativeLib){
      displayMessage("Starting " + subAppletDisplayName);

      // back to the EDT
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          if (osType != MAC_OS) { // borrowed from NativeLibLoader
            try {
              System.loadLibrary("jawt");
            } catch (UnsatisfiedLinkError ex) {
              // Accessibility technologies load JAWT themselves; safe to continue
              // as long as JAWT is loaded by any loader
              if (ex.getMessage().indexOf("already loaded") == -1) {
                displayError("Unabled to load JAWT");
                throw ex;
              }
            }
          }
          // static linking
          try {
            System.load(nativeLib.getPath());
          } catch (UnsatisfiedLinkError ex) {
            // should be safe to continue as long as the native is loaded by any loader
            if (ex.getMessage().indexOf("already loaded") == -1) {
              displayError("Unable to load " + nativeLib.getName());
              throw ex;
            }
          }

          // disable JOGL loading form elsewhere
          //net.java.games.jogl.impl.NativeLibLoader.disableLoading();

          // start the subapplet
          startSubApplet();
        }
      });
    }

    /** The true start of the sub applet (invoked in the EDT) */
    private void startSubApplet(){
      try {
        subApplet = (Applet)Class.forName(subAppletClassName).newInstance();
        subApplet.setStub(new AppletStubProxy());
      } catch (ClassNotFoundException cnfe) {
        cnfe.printStackTrace();
        displayError("Class not found (" + subAppletClassName + ")");
        return;
      } catch (Exception ex) {
        ex.printStackTrace();
        displayError("Unable to start " + subAppletDisplayName);
        return;
      }

      add(subApplet, BorderLayout.CENTER);

      try {
        subApplet.init();
        remove(loaderPanel);
        validate();
        subApplet.start();
        joglStarted = true;
      } catch (Exception ex){
        ex.printStackTrace();
      }

    }

    /** a proxy to allow the subApplet to work like a real applet */
    class AppletStubProxy implements AppletStub{
      public boolean isActive() {
        return JOGLAppletLauncher.this.isActive();
      }

      public URL getDocumentBase() {
        return JOGLAppletLauncher.this.getDocumentBase();
      }

      public URL getCodeBase() {
        return JOGLAppletLauncher.this.getCodeBase();
      }

      public String getParameter(String name) {
        return JOGLAppletLauncher.this.getParameter(name);
      }

      public AppletContext getAppletContext() {
        return JOGLAppletLauncher.this.getAppletContext();
      }

      public void appletResize(int width, int height) {
        JOGLAppletLauncher.this.resize(width, height);
      }
    }


}

