
import org.yaml.snakeyaml.Yaml;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


public class Config {

    static HashMap<String, Object> config = new HashMap<>(20);
    static private Profile current;
    static boolean usePopupDialogBoxesForErrors;
    static boolean openNotepad;

    public static void init() {
        File file = new File(Main.DATA_PATH + "\\config.cfg");
        Yaml yaml = new Yaml();
        try {
            config = yaml.load(new FileReader(file));
        } catch(FileNotFoundException e) {
            Utils.error("The config.cfg was not found at " + Main.DATA_PATH + "\\config.cfg" + "!\n " +
                    "This is, likely, because the whole data folder is in the wrong directory or\n " +
                    "it doesn't exist.", "");
        }

        usePopupDialogBoxesForErrors = (boolean) config.get("pop_up_dialog_boxes");
        openNotepad = (boolean) config.get("open_notepad");

    }

    public static Profile getCurrentProfile() throws FileNotFoundException, ClassCastException {
        if(current != null) // cache for current profile
            return current;

        // only runs once \/

        HashMap<String, Object> profile;
        try {
            String name = (String) config.get("current_profile");
            File file = new File(Main.DATA_PATH + "\\profiles\\" + name + ".yml");
            Yaml yaml = new Yaml();
            profile = yaml.load(new FileReader(file));
        } catch(ClassCastException e) {
            Utils.error(
                    "The profile name (config.cfg#current_profile) must be a String. \n" +
                            "example profile name: `current_profile: \"1\"`!\n",
                    null
            );
            throw e;
        } catch(FileNotFoundException e){
            Utils.error(
                    "\"Do not include the file extension \\\".yml\\\" in your profile name, \n" +
                            "unless the file name for it is: \\\"myprofile.yml.yml\\\"\"",
                    null
            );
            throw e;
        }

        current = new Profile();

        {
            @SuppressWarnings("unchecked")
            HashMap<String, Object> section = (HashMap<String, Object>)     profile.get("wallpaper");

            current.useImage = (boolean) section.get("use_image"); // "wallpaper: " section
            if (current.useImage)
                current.imagePath = (String) section.get("image_path");
            else {
                current.background = decodeButBetter((String) section.get("bg_color"));
                current.width = (int) section.get("width");
                current.height = (int) section.get("height");
            }
        }

        {
            @SuppressWarnings("unchecked")
            HashMap<String, Object> section = (HashMap<String, Object>)     profile.get("text");

            if(!section.containsKey("font"))   section.put("font",  Font.MONOSPACED);
            if(!section.containsKey("style"))  section.put("style", Font.PLAIN);
            if(!section.containsKey("size"))   section.put("size",  16);
            if(!section.containsKey("weight")) section.put("weight", 4);

            current.baseFont = makeFont(
                    (String) section.get("font"),
                    (int)    section.get("style"),
                    (int)    section.get("weight"),
                    (int)    section.get("size"),
                    (String) section.get("color")
            );

            current.baseCharSize = (int) section.get("size");

            @SuppressWarnings("unchecked")
            ArrayList<HashMap<String, Object>> labels = (ArrayList<HashMap<String, Object>>) section.get("labels");

            for(HashMap<String, Object> label : labels){

                UFont font = makeFont(
                        label.containsKey("font")   ? (String) label.get("font")   : (String) section.get("font"),
                        label.containsKey("style")  ? (int)    label.get("style")  :    (int) section.get("style"),
                        label.containsKey("weight") ? (int)    label.get("weight") :    (int) section.get("weight"),
                        label.containsKey("size")   ? (int)    label.get("size")   :    (int) section.get("size"),
                        label.containsKey("color")  ? (String) label.get("color")  : (String) section.get("color")

                );


                @SuppressWarnings("unchecked")
                ArrayList<String> symbols = (ArrayList<String>) label.get("symbols");

                for(String symbol : symbols)
                    current.otherFonts.put(symbol, font); // e.g.: "HW" = MyFonts.HOMEWORK (MyFonts doesn't exist btw).
            }

        }

        {
            @SuppressWarnings("unchecked")
            HashMap<String, Object> section = (HashMap<String, Object>)     profile.get("text_box");

            String background = section.containsKey("background") ? ( (String) section.get("background") ) : "transparent";

            if(background.equals("transparent")) current.textBoxBackground = new Color(255, 0, 0, 0);
            else                                 current.textBoxBackground = decodeButBetter(background);

            if(section.containsKey("wrap_around_letters"))
                current.textBoxSolid = ! (Boolean) section.get("wrap_around_letters");
                // !                  ^^^

            if(section.containsKey("padding")) current.padding = (int) section.get("padding");
            if(section.containsKey("margin"))  current.margin  = (int) section.get("margin");
        }

        return current;
    }

    private static UFont makeFont(String fontFamily, int style, int weight, int size, String color){
        int styles = concatToBitWiseOR(style);

        Map<TextAttribute, Integer> fontAttributes2 = new HashMap<>();

        if((styles & 4) != 0)
            fontAttributes2.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        else fontAttributes2.put(TextAttribute.UNDERLINE, -1);

        UFont font = new UFont(new Font(fontFamily, styles & 3, size).deriveFont(fontAttributes2));

        if((styles & 1) == 0)
            font = new UFont(font.deriveFont(Collections.singletonMap(
                    TextAttribute.WEIGHT, 0.25f + (0.25f * weight)
            )), font.getColor());

        if(color == null) font.setColor(getContrastColor(current.background));
        else font.setColor(decodeButBetter(color));

        return font;
    }

    private static int concatToBitWiseOR(int number){
        int output = 0;
        if(number == 0)
            return output;

        for(int i = (int) Math.log10(number); i >= 0; i --)
            output |= (number / (int) Math.pow(10, i)) % 10;
        return output;
    }

    public static Color decodeButBetter(String hex){
        hex = hex.replace("#", "");
        String[] numbers = hex.split("(?<=\\G.{2})");
        for(int i = 0; i < numbers.length; i ++) numbers[i] = '#' + numbers[i];
        int r = 198, g = 0, b = 0, a = 255;
        if(numbers.length == 4) {
            a = Integer.decode(numbers[0]);
            r = Integer.decode(numbers[1]);
            g = Integer.decode(numbers[2]);
            b = Integer.decode(numbers[3]);
        } else if(numbers.length == 3){
            r = Integer.decode(numbers[0]);
            g = Integer.decode(numbers[1]);
            b = Integer.decode(numbers[2]);
        } else if(numbers.length == 2){
            a = Integer.decode(numbers[0]);
            r = Integer.decode(numbers[1]);
            g = Integer.decode(numbers[1]);
            b = Integer.decode(numbers[1]);
        } // I didn't specify if this function is any good, it's just better than Color#decode(String nm)...

        return new Color(r, g, b, a);
    }

    private static Color getContrastColor(Color color) {
        double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
        return y >= 128 ? Color.black : Color.white;
    } // from: https://stackoverflow.com/questions/4672271/reverse-opposing-colors
}




