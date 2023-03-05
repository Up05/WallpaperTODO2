import com.github.tomaslanger.chalk.Chalk;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;

public class Main {

    public static String DATA_PATH;

    public static void main(String[] args) throws Exception {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception ignored){}

        AppDirs appDirs = AppDirsFactory.getInstance();
        DATA_PATH = appDirs.getUserDataDir("WallpaperTODO2", null, "Ult1");
//        System.out.println(Chalk.on("Data path: " + DATA_PATH).blue().underline());
        Config.init();

        final String todoText;
        {
            File file = new File(DATA_PATH + "\\todo.txt");

            if(Config.openNotepad) {
                Process p = new ProcessBuilder("notepad.exe", quote(file.getAbsolutePath())).start();
                p.waitFor();
            }

            FileReader reader = new FileReader(file.getAbsolutePath());
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            todoText = new String(chars);
            reader.close();
        }

        Profile current = Config.getCurrentProfile();

        BufferedImage wallpaper;
        if(current.useImage) {
            File imageFile = new File(current.imagePath.replace("$DATA_DIR", DATA_PATH));
            if(!imageFile.exists()) {
                System.out.println(
                        Chalk.on("No image found at path: \"").red().bold() + "" +
                                Chalk.on(imageFile.getAbsolutePath()).blue().underline() +
                                Chalk.on("\"").red().bold()
                );
                return;
            }
            wallpaper = ImageIO.read(imageFile);
        }
        else {
            wallpaper = new BufferedImage(current.width, current.height, BufferedImage.TYPE_INT_ARGB);
        }

        {
            Graphics2D graphics = wallpaper.createGraphics();

            graphics.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );



                graphics.setColor(current.background);
            if(!current.useImage)
                graphics.fillRect(0, 0, current.width, current.height);

            String[] lines = todoText.split("\n");
            int commentLineCount = 0;

            if(current.textBoxSolid && ( !current.textBoxBackground.equals(new Color(255, 0, 0, 0)) )) {
                int maxWidth = 0;
                for(int i = 0; i < lines.length; i ++) {

                    for (String labelKey : current.otherFonts.keySet()) {
                        if(firstCharsAre(lines[i], '#')) {
                            commentLineCount ++;
                            continue;
                        }
                        if (firstCharsAre(lines[i], labelKey.toCharArray()))
                            graphics.setFont(current.otherFonts.get(labelKey));
                    }

                    int _width = graphics.getFontMetrics().stringWidth(lines[i]);
                    if (maxWidth < _width) maxWidth = _width;
                }

                int avgCharHeight = 0;
                for(UFont font : current.otherFonts.values()) {
                    graphics.setFont(font);
                    avgCharHeight += (graphics.getFontMetrics().getHeight() + 2) / (current.otherFonts.values().size());
                }

                graphics.setColor(current.textBoxBackground);
                int width = (int) (maxWidth * 1.75) + current.padding;
                int height = (lines.length - commentLineCount) * avgCharHeight + current.padding;

                graphics.fillRect(current.margin - current.padding / 2, current.margin - current.padding / 2, width, height);
            }


            int previousLineWidth = 0;
            int y = current.margin;
            main_loop:
            for(int i = 0; i < lines.length; i ++){

                Color currentColor = current.baseFont.getColor();
                graphics.setFont(current.baseFont);

                for(String labelKey : current.otherFonts.keySet()){
                    if(firstCharsAre(lines[i], '#')) {

                        continue main_loop;
                    }
                    if(firstCharsAre(lines[i], labelKey.toCharArray())){
                        graphics.setFont(current.otherFonts.get(labelKey));
                        currentColor = current.otherFonts.get(labelKey).getColor();
                    }
                }

                if(!current.textBoxSolid && !current.textBoxBackground.equals(new Color(255, 0, 0, 0))){ // !, NOT  transparent
                    graphics.setColor(current.textBoxBackground);
                    int width = graphics.getFontMetrics().stringWidth(lines[i]);
                    int temp = previousLineWidth;
                    previousLineWidth = width;
                    if(temp > width)
                        width = temp;
                    int lastLinePadding = 0;
                    if(i == lines.length - 1)
                        lastLinePadding = current.padding;
                    graphics.fillRect(
                            current.margin - current.padding / 2,
                            y,
                            width + current.baseCharSize + current.padding,
                            graphics.getFontMetrics().getHeight() + 2 + lastLinePadding
                    );
                }

                graphics.setColor(currentColor);

                y += graphics.getFontMetrics().getHeight() + 2;
                graphics.drawString(lines[i], current.margin, y);

            }

            ImageIO.write(wallpaper, "png", new File(DATA_PATH + "/out.png"));

            new SetWallpaperNative().setWallpaper(DATA_PATH + "/out.png");

            // Todo: maybe add more styles e.g.: unimportant, future...


        }



    }

    private static String quote(String string){
        return '"' + string + '"';
    }

    private static boolean firstCharsAre(String str, char... chars){
        if(str.length() >= chars.length) {
            for (int i = 0; i < chars.length; i++)
                if (str.charAt(i) != chars[i] && (i + 1 >= str.length() || str.charAt(i + 1) != chars[i])) return false;
        } else return false;
        return true;
    } // this somehow completely works and everything else doesn't... I do not know how.
}
