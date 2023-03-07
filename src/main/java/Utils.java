import com.github.tomaslanger.chalk.Chalk;

import javax.swing.*;

public class Utils {
    public static void error(String message, String title){

        System.out.println(Chalk.on(message).red().bold());

        if(!Config.usePopupDialogBoxesForErrors) return;

        JOptionPane.getRootFrame().setAlwaysOnTop(true);
//        JOptionPane.showMessageDialog(null, message, title != null ? title : "Error message", JOptionPane.ERROR_MESSAGE);
        JOptionPane.showConfirmDialog(null, message, title != null ? title : "Error message", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);


    }


}
