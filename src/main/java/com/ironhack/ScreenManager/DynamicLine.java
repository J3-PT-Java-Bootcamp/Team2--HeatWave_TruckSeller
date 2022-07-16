package com.ironhack.ScreenManager;



import static com.ironhack.ScreenManager.ColorFactory.DELETE_CURRENT_LINE;

public class DynamicLine extends TextObject {

    int delay;//milliseconds
    int currentFrame;
//    final AnimationObject[] animations;

    public DynamicLine(int maxWidth, int maxHeight, int delay) {
        super(Scroll.LINE, maxWidth, maxHeight);
        setDelay(delay);
        this.currentFrame = 0;
    }

    private void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public String poll() {
        currentFrame++;
        return DELETE_CURRENT_LINE + text.remove(0);
    }

    @Override
    public String print() {
        return DELETE_CURRENT_LINE + poll();
    }
}
