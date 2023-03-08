import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        String filepath = Main.DATA_PATH + "\\crash_reports\\crash " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log";
        System.out.println(filepath);
        File file = new File(filepath);

        file.getParentFile().mkdirs();

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                System.out.println("Unable to create a crash report file. LOL, L me");
                ex.printStackTrace();
            }
        }
        // new Date() + '\n' + e.getMessage() + '\n' + formattedStackTrace.toString() + "\n\n"
        StringBuilder formattedStackTrace = new StringBuilder(256);
        formattedStackTrace
                .append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append('\n')
                .append(e.getMessage()).append('\n');

        for(StackTraceElement line : e.getStackTrace())
            formattedStackTrace.append("    ").append(line.toString()).append('\n');

        formattedStackTrace.append("\n\n");

        System.err.println(formattedStackTrace);

        try {
            if(Files.size(file.toPath()) < 1_000_000) // just in case I do something stupid...
                Files.write(Paths.get(filepath), formattedStackTrace.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException ex) {
            System.out.println("Unable to write to crash report file. LOL, L me");
            ex.printStackTrace();
        }

        if(Objects.requireNonNull(file.getParentFile().listFiles()).length > 250){
            SystemTray tray = SystemTray.getSystemTray();

            Image image = Toolkit.getDefaultToolkit().createImage(Main.DATA_PATH + "\\warning_icon.png");
            TrayIcon trayIcon = new TrayIcon(image, "WallpaperTODO2");
            trayIcon.setImageAutoSize(true);
            trayIcon.setImage(image);
            trayIcon.setToolTip("%LocalAppData%\\Ult1\\WallpaperTODO2\\crash_reports directory has more than 50 files already!");
            try {
                tray.add(trayIcon);
            } catch (AWTException ex) {
                ex.printStackTrace();
            }
            trayIcon.displayMessage("WallpaperTODO2", "WallpaperTODO2 crash reports directory is getting quite large, you may want to delete some crash reports!", TrayIcon.MessageType.WARNING);

            // I dislike this, however, neither me, nor anyone else cares about it...
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.exit(-1);


        }


    }
}
