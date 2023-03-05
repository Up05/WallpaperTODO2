import java.awt.*;

public class UFont extends Font {

    private Color color;

    protected UFont(Font font) {
        super(font);
    }
    protected UFont(Font font, Color color){
        super(font); this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
