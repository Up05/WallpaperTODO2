import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Profile {

    public boolean
            useImage = false,
            textBoxSolid = false;

    public String
            imagePath = "";

    public Color
            background = null,
            textBoxBackground = null;

    public int
            width = -1,
            height = -1,
            padding = 0,
            margin = 0,
            baseCharSize;

    public UFont baseFont;
    public HashMap<String, UFont> otherFonts = new HashMap<>(5);
    // I would be such a bad father...

}