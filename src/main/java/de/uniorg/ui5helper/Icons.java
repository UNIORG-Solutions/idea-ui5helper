package de.uniorg.ui5helper;

import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class Icons {
    public static final Icon OPENUI5 = IconLoader.getIcon("/de/uniorg/ui5helper/icons/ui5_logo.png");
    public static final Icons INSTANCE = new Icons();
    private Font font;

    private Icons() {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, ProjectComponent.class.getResourceAsStream("/de/uniorg/ui5helper/icons/SAP-icons.ttf")).deriveFont(12.0f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public Icon fromIconFont(char code) {
        return new FontIcon(font, code);
    }

    public class FontIcon implements Icon {

        private final Font font;
        private final char glyph;

        FontIcon(Font font, char glyph) {
            this.font = font;
            this.glyph = glyph;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setFont(font);
            g2d.setColor(JBColor.foreground());
            g2d.drawString("" + glyph, x, y + 12);
            g2d.dispose();

        }

        @Override
        public int getIconWidth() {
            return 16;
        }

        @Override
        public int getIconHeight() {
            return 16;
        }
    }
}
