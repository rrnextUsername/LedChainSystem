package segments;

import it.unibo.chain.segment7.LedSegment;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;


public class LedSegmentAdapter extends LedSegment {
    private final JFrame frame;

    public LedSegmentAdapter(@NotNull String name, int width, int height, int x, int y) {
        super(name, width, height);

        frame = new JFrame();
        frame.setName(name);
        frame.setSize(width, height * 10);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation(x, y);

        frame.setLayout(new GridLayout(1, 1));

        frame.add(this);
        frame.setVisible(true);
    }

    public LedSegmentAdapter(@NotNull String name, int width, int height) {
        this(name, width, height, 0, 0);
    }

    @Override
    protected void setLedRep() {
        getLedRep().addPoint(getX() + 10, getY() + 8);
        getLedRep().addPoint(getX() + 100, getY() + 8);
        getLedRep().addPoint(getX() + 110, getY() + 15);
        getLedRep().addPoint(getX() + 100, getY() + 22);
        getLedRep().addPoint(getX() + 10, getY() + 22);
        getLedRep().addPoint(getX() + 2, getY() + 15);
    }
}
