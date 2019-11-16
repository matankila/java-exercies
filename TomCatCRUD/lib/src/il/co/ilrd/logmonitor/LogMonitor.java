package il.co.ilrd.logmonitor;

import il.co.ilrd.CrudFile;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.file.*;
import java.io.*;
import java.util.Objects;

/**
 * @author Matan Keler, Infinity Labs.
 * Log monitor, monitor changes in file and update all the observers.
 */
public class LogMonitor {
    private final Path syslogDir;
    private final File syslogFile;
    private final PropertyChangeSupport support;
    private WatchService watchService;

    /*********************************************** CTOR *************************************************************/
    public LogMonitor(String pathToFile) {
        Objects.requireNonNull(pathToFile, "path cannot be null");
        support = new PropertyChangeSupport(this);
        syslogFile = new File(pathToFile);
        if (!syslogFile.exists()) { throw new IllegalArgumentException("path not exist"); }

        syslogDir = Paths.get(syslogFile.getParent());
        try {
            watchService = FileSystems.getDefault().newWatchService();
            syslogDir.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
        }
        catch (IOException e) {
            e.getCause();
        }
    }

    /************************************** Property Listener Methods *************************************************/
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    /********************************************** API Methods *******************************************************/
    public void watch() throws InterruptedException {
       WatchKey key = null;

        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                if (((Path)event.context()).endsWith(syslogFile.getName())) {
                    sendEvent("<" + event.kind() + ">" + getLastLine(syslogFile));
                }
            }

            key.reset();
        }
    }

    public File getMonitoredFile() {
        return syslogFile;
    }

    public void sendEvent(String event) {
        support.firePropertyChange("event", null, event);
    }

    /******************************************** non API Methods *****************************************************/
    private static String getLastLine(File file) {
        String line = null;
        String nextLine = null;

        try {
            BufferedReader bis = new BufferedReader(new FileReader(file));
            while ((nextLine = bis.readLine()) != null) {
                line = nextLine;
            }

            bis.close();
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return line;
    }

    public static void main(String[] args) {
        LogMonitor l = new LogMonitor("/var/log/syslog");
        CrudFile cf = new CrudFile("/home/matan/Documents/pass.txt/", l.getMonitoredFile());
        l.addPropertyChangeListener((evt)-> {
            String s = (String) evt.getNewValue();
            cf.create(s);
        });

        try {
            l.watch();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}