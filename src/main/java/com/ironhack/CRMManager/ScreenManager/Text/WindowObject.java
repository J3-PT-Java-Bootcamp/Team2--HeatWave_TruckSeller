package com.ironhack.CRMManager.ScreenManager.Text;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.ironhack.Constants.ColorFactory.*;
import static com.ironhack.Constants.ColorFactory.CColors.BRIGHT_WHITE;
import static com.ironhack.Constants.ColorFactory.TextStyle.BOLD;

public class WindowObject extends TextObject {
    final java.awt.Point borderSize;
    final java.awt.Point margin;
    String[] pattern;
    String title;
    final int windowWidth;
    final int windowHeight;
    private BgColors frameColor;
    private CColors titleColor;

    public WindowObject(int maxWidth, int maxHeight, int borderSize, int margin, String... pattern){
        super(Scroll.BLOCK, maxWidth-((borderSize+margin)*2),
                maxHeight-(borderSize+margin));
        this.pattern=pattern;
        this.borderSize=new java.awt.Point(borderSize,borderSize);
        this.margin=new java.awt.Point(margin,margin);
        this.windowHeight=maxHeight;
        this.windowWidth=maxWidth;
        //generateBorder(pattern);
    }

    private String generateBorder(boolean isVertical, boolean starts) {
        adjustPattern();
        var sb = new StringBuilder();
        if(isVertical) {
            if(starts&&margin.x>0)sb.append(BLANK_SPACE.repeat(margin.x));
            if (!starts)sb.append(TextStyle.RESET);
            if(frameColor!=null)sb.append(frameColor);
            if(borderSize.x>0&&pattern.length>0){
                for (int i = 0; i < borderSize.x; i++) {
                    sb.append(pattern[starts?i:borderSize.x-i-1]);
                }
                sb.append(TextStyle.RESET);
                if(starts && this.bgColor!=null)sb.append(bgColor);
                if(starts&&txtColor!=null)sb.append(txtColor);
            }
            if(!starts&&margin.x>0)sb.append(BLANK_SPACE.repeat(margin.x));
            if(!starts)sb.append(TextStyle.RESET).append(NEW_LINE);
            return sb.toString();
        }
        if(starts){
            if(margin.y>0)sb.append((BLANK_SPACE.repeat(windowWidth-(margin.x*2))+NEW_LINE).repeat(margin.y));
        }
        if(borderSize.y>0&&pattern.length>0){
            for (int i = 0; i < borderSize.y; i++) {
                sb.append(BLANK_SPACE.repeat(margin.x));
                if(frameColor!=null)sb.append(frameColor);
                if (title!=null&&(title.length() > 0) && starts && (i == 0)){
                    int fillLeft= windowWidth-(margin.x*2)-title.length();
                    int fillRight= ((fillLeft % 2) == 0) ? (fillLeft / 2) : ((fillLeft / 2) + 1);
                    fillLeft/=2;
                    sb.append(pattern[i].repeat(fillLeft)).append(titleColor!=null?titleColor:"").append(BOLD).append(title).append(TextStyle.RESET)
                            .append(frameColor!=null?frameColor:"").append(pattern[i].repeat(fillRight));
                }else {
                    sb.append(pattern[starts ? i : borderSize.y - i-1]
                            .repeat(!starts&&i==borderSize.y-1?windowWidth-(margin.x*2)- borderSize.y-5:windowWidth-(margin.x*2)));
                    if(!starts&&i==borderSize.y-1){
                        sb.append(BOLD).append(BRIGHT_WHITE)
                                .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")))
                                .append(BLANK_SPACE.repeat(borderSize.y));
                    }
                }
                sb.append(TextStyle.RESET).append(BLANK_SPACE.repeat(margin.x)).append(NEW_LINE);
            }
        }
        return sb.toString();
    }

    private void adjustPattern() {
        int size= Math.max(borderSize.x, borderSize.y);
        int aux= pattern.length-size;
        if(aux<0){
            var newPattern= new String[size];
            for (int i = 0; i < size; i++) {
                newPattern[i] = (i < pattern.length) ? (pattern[i].equals("") ? BLANK_SPACE : pattern[i]) : " ";
            }
            pattern=newPattern.clone();
        }else{
            for (int i = 0; i < pattern.length; i++) {
                if (pattern[i].equals("")) pattern[i] += BLANK_SPACE;
            }
        }
    }
    public String getTextModifications(){
        return bgColor+txtColor.toString();
    }
    public WindowObject setTitle(String title) {
        this.title = title;
        return this;
    }

    public WindowObject setTitleColor(CColors color){
        this.titleColor=color;
        return this;
    }

    public WindowObject setFrameColor(BgColors frameColor) {
        this.frameColor = frameColor;
        return this;
    }
    @Override
    public String print(){
        var sb= new StringBuilder();
        sb.append(generateBorder(false,true));
        while (hasText()) {
            sb.append(generateBorder(true,true))
                    .append(fillLine(poll()+bgColor)).append(generateBorder(true,false));
        }
        sb.append(generateBorder(false,false));
        sb.append(BLANK_SPACE.repeat(windowWidth / 2));
        return sb.toString();
    }


}
