package com.ironhack.ScreenManager.Text;


import com.ironhack.Constants.ColorFactory;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static com.ironhack.Constants.ColorFactory.*;
import static com.ironhack.Constants.ColorFactory.TextStyle.RESET;
import static com.ironhack.Constants.Constants.LIMIT_X;
import static com.ironhack.Constants.Constants.LIMIT_Y;
import static com.ironhack.ScreenManager.Screens.MenuScreen.SMART_RESET;

/**
 * TextObject class:
 * A textObject is mostly a sorted ArrayList of String, each as lines of the total text.
 * Thought as a kind of StringBuilder, lets you construct complicated text structure for a console printer
 * without constructing and deconstructing each text on each change and adding all necessary methods for this use case.
 * It also has a bunch of utilities to format its lines, change color,background,style,create boxes and titles,
 * merge into new textObjects as columns, fit in determinate sizes, etc...
 *
 * @author DidacLL
 * @since v0.1
 */
@Data
public class TextObject {

    public enum Scroll {NO, BLOCK, LINE, TYPEWRITER}
    public BgColors bgColor;
    public CColors txtColor;
    public TextStyle txtStyle;



    private final Scroll scroll;
    protected final ArrayList<String> text;
    public final int MAX_WIDTH;
    public final int MAX_HEIGHT;
    private float printSpeed;
    private int totalWidth, totalHeight;

    //-------------------------------------------------------------------------------------------------------CONSTRUCTOR

    public TextObject(){
        this.scroll = Scroll.BLOCK;
        MAX_WIDTH = LIMIT_X;
        MAX_HEIGHT = LIMIT_Y;
        this.text = new ArrayList<>();

    }
    public TextObject(String text){
        this.scroll = Scroll.BLOCK;
        MAX_WIDTH = LIMIT_X;
        MAX_HEIGHT = LIMIT_Y;
        this.text = new ArrayList<>();
        addText(text);

    };
    public TextObject( int maxWidth, int maxHeight) {
        this.scroll = Scroll.NO;
        MAX_WIDTH = maxWidth;
        MAX_HEIGHT = maxHeight;
        this.text = new ArrayList<>();
    }
    public TextObject(Scroll scroll, int maxWidth, int maxHeight) {
        this.scroll = scroll;
        MAX_WIDTH = maxWidth;
        MAX_HEIGHT = maxHeight;
        this.text = new ArrayList<>();
    }
    public TextObject(String text, int maxWidth, int maxHeight) {
        this.scroll = Scroll.NO;
        MAX_WIDTH = maxWidth;
        MAX_HEIGHT = maxHeight;
        this.text = new ArrayList<>();
        this.totalWidth = 0;
        this.setTotalHeight();
        addText(text);
    }
    public TextObject(String text, Scroll scroll, int maxWidth, int maxHeight) {
        this.scroll = scroll;
        MAX_WIDTH = maxWidth;
        MAX_HEIGHT = maxHeight;
        this.text = new ArrayList<>();
        this.totalWidth = 0;
        this.setTotalHeight();
        addText(text);
    }

    public TextObject(Scroll scroll, String[] textLines, int maxWidth, int maxHeight) {
        this.scroll = scroll;
        MAX_WIDTH = maxWidth;
        MAX_HEIGHT = maxHeight;
        this.text = new ArrayList<>(List.of(textLines));
        this.printSpeed = 2;
    }

    public TextObject(TextObject txtObject, Scroll scroll, int maxWidth, int maxHeight) {
        this.scroll = scroll;
        MAX_WIDTH = maxWidth;
        MAX_HEIGHT = maxHeight;
        this.text = new ArrayList<>();
        addText(txtObject);
    }
    //---------------------------------------------------------------------------------------------------Getters&Setters

    BgColors getBgColor() {
        return bgColor;
    }

    public TextObject setBgcolor(BgColors bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    CColors getTxtColor() {
        return txtColor;
    }

    public TextObject setTxtColor(CColors txtColor) {
        this.txtColor = txtColor;
        return this;
    }

    TextStyle getTxtStyle() {
        return txtStyle;
    }

    public TextObject setTxtStyle(TextStyle txtStyle) {
        this.txtStyle = txtStyle;
        return this;
    }
    public float getPrintSpeed() {
        if (printSpeed > 0) return printSpeed;
        setPrintSpeed(1);
        return printSpeed;
    }

    public TextObject setPrintSpeed(float printSpeed) {
        this.printSpeed = printSpeed;
        return this;
    }

    public Scroll getScroll() {
        return scroll;
    }

    public ArrayList<String> getText() {
        return text;
    }

    private int getTotalWidth() {
        return totalWidth;
    }

    private void setTotalWidth(int totalWidth) {
        this.totalWidth = Math.max(totalWidth, getTotalWidth());
    }

    public int getTotalHeight() {
        setTotalHeight();
        return this.totalHeight;
    }

    private void setTotalHeight() {
        this.totalHeight = getText().size();
    }

    public boolean hasText() {
        return getTotalHeight() > 0;
    }

    public String get(int index) throws Exception {
        if (index < getTotalHeight()) return text.get(index);
        throw new Exception();
    }

    public String poll() {
        return text.remove(0);
    }
    //------------------------------------------------------------------------------------------------------------ADDERS

    /**
     * Adds new lines to text by calling addText(String[])-->addSimpleLine()
     *
     * @param text String to be added, always splits it by "\n" to add line by line
     *
     * @return this TextObject itself to allow chained calls
     */
    public TextObject addText(String text) {
        return addText(splitTextInLines(text));
    }

    /**
     * Adds new lines to text by calling addSimpleLine()
     *
     * @param lines String[] to be added
     *
     * @return this TextObject itself to allow chained calls
     */
    TextObject addText(String[] lines) {
        for (String line : lines) {
            addSimpleLine(line);
        }
        return this;
    }

    /**
     * Appends new lines to text by calling addSimpleLine()
     *
     * @param txtObject txtObject to be added at the end of this one
     *
     * @return this TextObject itself to allow chained calls
     */
    public TextObject addText(TextObject txtObject) {
        for (int i = 0; i < txtObject.getTotalHeight(); i++) {
            try {
                addSimpleLine(txtObject.get(i));
            } catch (Exception e) {
                return this;
            }
        }
        return this;
    }

    /**
     * Append to this text new lines by merging various textObjects in diferent justified columns
     *
     * @param numberOfColumns number of desired columns in our pattern
     * @param totalSize       total available space to be divided in columns (count in chars)
     * @param columnsContent  array of TextObjects[] to be merged (it does a resize and wrap logic)
     *
     * @return this textObject to allow chain calls.
     */
    public TextObject addGroupInColumns(int numberOfColumns, int totalSize, TextObject[] columnsContent) {
        int charLimit = (numberOfColumns > 1 ? (totalSize / numberOfColumns) : totalSize);
        int rest= totalSize%numberOfColumns;
        int totalLines = 0;
        for (TextObject textColumn : columnsContent) {
            trimAllText();
            totalLines = Math.max(totalLines, textColumn.getTotalHeight());
        }
        for (int j = 0; j < totalLines; j++) {
            var strBuilder = new StringBuilder();
            for (int i = 0; i < numberOfColumns; i++) {
                var currentContent=columnsContent[i];
                String currentVal;
                try {
                    currentVal = getTextColorsModifiers()+currentContent.getTextColorsModifiers()
                            +currentContent.centerLine(currentContent.get(j),charLimit)
                            +RESET+getTextColorsModifiers();
                } catch (Exception e) {
                    currentVal = getTextColorsModifiers()+currentContent.getTextColorsModifiers()
                            +BLANK_SPACE.repeat(charLimit)+getTextColorsModifiers();
                }
                strBuilder.append(centerLine(currentVal,charLimit));
            }
            if (rest>0) strBuilder.append(BLANK_SPACE.repeat(rest));
            var val=strBuilder.toString();
            addText(val);
        }
        return this;
    }


    private String createTableCell(int charLimit, String currentField,boolean isLast) {
        int availableSpace= charLimit -1;
        return (TextStyle.BOLD+"|"+ RESET
                +centerLine(currentField,availableSpace)
                +(isLast?TextStyle.BOLD+"|"+ RESET: RESET));
    }

    //-----------------------------------------------------------------------------------------------------INNER_METHODS

    //===================   LINE_MANIPULATION   ===================\\
    //REAL ADD_LINE METHOD, IT WRAPS IN 2 LINES IF IT EXCEEDS MAX_WIDTH

    /**
     * com.ironhack.soutbattle.Main Add Line Method, It Checks That Size Fits On Specified Width
     * if not it splits line by wrap() method. After check adds line/s by addSafe()
     *
     * @param line text to be added, not necessary an oneliner string
     */
    private void addSimpleLine(String line) {
        int sizeCounter;
        sizeCounter = countValidCharacters(line);
        if (sizeCounter > MAX_WIDTH) {
            var group = wrapLine(line, MAX_WIDTH);
            for (String wrapLine : group) addSafe(wrapLine);
        } else {
            setTotalWidth(sizeCounter);
            addSafe(line);
        }
    }

    /**
     * Only method that can add a new element to text attribute
     *
     * @param line oneliner String to be added
     */
    private void addSafe(String line) {
        this.text.add(line.replaceAll(NEW_LINE, ""));
        setTotalHeight();
    }

    /**
     * Only method that can add a new element to text attribute
     *
     * @param index position where to add it (doesn't overwrite)
     * @param line  oneliner String to be added
     */
    private void addSafe(int index, String line) {
        this.text.add(index, line.replaceAll(NEW_LINE, ""));
        setTotalHeight();
    }

    /**
     * Method to count characters that will be printed, it doesn't count scape characters nor colors or styles tags.
     *
     * @param line line to count
     *
     * @return integer value of char count
     */
    public int countValidCharacters(String line) {
        int colourCount = 0;
        int charCount = 0;
        var chArray = line.toCharArray();
        for (char ch : chArray) {
            charCount++;
            if (ch == COLOR_CHAR) colourCount++;
            if (ch == NEW_LINE_CH || ch == DELETE_CURRENT_LINE) charCount--;
        }
        return charCount - (colourCount * COLOR_LABEL_CHAR_SIZE);
    }

    /**
     * Recursive method that checks line size. If its bigger than limit it splits the String recursively
     * until all resulting lines fits on specified limit char size
     *
     * @param line  text to analyze
     * @param limit maximum chars width
     *
     * @return String[] array with all resulting strings
     *
     */
    private String[] wrapLine(String line, int limit) {
        String[] result;

        var wordList = line.replace(BLANK_SPACE + BLANK_SPACE, "€€").split(BLANK_SPACE);
        StringBuilder line1 = new StringBuilder();
        StringBuilder line2 = new StringBuilder();
        int charCounter = 0;
        for (String word : wordList) {
            charCounter += countValidCharacters(word) + (java.util.Objects.equals(word, "€€") ? 0 : 1);
            if (charCounter <= limit) line1.append(" ").append(word.replace("€€", "  "));
            else line2.append(" ").append(word.replace("€€", "  "));
        }
        if (charCounter > limit * 2) {
            var auxList = wrapLine(line2.toString(), limit);
            var resVal = new String[auxList.length + 1];
            resVal[0] = line1.toString();
            System.arraycopy(auxList, 0, resVal, 1, auxList.length);
            result = resVal;
        } else {
            result = new String[]{line1.toString(), line2.toString()};
        }
        return result;
    }

    /**
     * Method to fill available right space of a string with blank spaces
     *
     * @param line  oneliner text String to be modified
     * @param width desired width
     *
     * @return resulting String
     */
    public String fillLine(String line, int width) {
        return applyTextColorsToLine(line) + (BLANK_SPACE.repeat(Math.max(width - countValidCharacters(line), 0)));
    }

    String fillLine(String line) {
        return fillLine(line, MAX_WIDTH);
    }

    /**
     * Method to align text on right by adding blank spaces at start of the string
     *
     * @param line  oneliner text String to be modified
     * @param width desired width
     *
     * @return resulting String
     */
    private String lineToRight(String line, int width) {
        int count = width - countValidCharacters(line);
        return applyTextColorsToLine((count > 0 ? BLANK_SPACE.repeat(count) : "") + line);
    }

    private String lineToRight(String line) {
        return lineToRight(line, MAX_WIDTH);
    }

    /**
     * Method to center string by adding blank spaces both sides
     *
     * @param line  oneliner text String to be modified
     * @param width desired width
     *
     * @return resulting String
     */
    public String centerLine(String line, int width) {
        int leftSpace, rightSpace, remainSpace;
        remainSpace = width - countValidCharacters(line);
        leftSpace = remainSpace / 2;
        rightSpace = (remainSpace % 2 == 0) ? leftSpace : leftSpace + 1;
        return getTextColorsModifiers()+(BLANK_SPACE.repeat(leftSpace)) + line + (BLANK_SPACE.repeat(rightSpace)+getTextColorsModifiers());
    }
    public String centerLineWithoutColors(String line, int width){
        int leftSpace, rightSpace, remainSpace;
        remainSpace = width - countValidCharacters(line);
        leftSpace = remainSpace / 2;
        rightSpace = (remainSpace % 2 == 0) ? leftSpace : leftSpace + 1;
        return (BLANK_SPACE.repeat(leftSpace)) + line + (BLANK_SPACE.repeat(rightSpace));
    }

    public String centerLine(String line) {
        return centerLine(line, MAX_WIDTH);
    }

    /**
     * Adds color and reset labels on this line
     *
     * @param s     String line to modify
     * @param color CColors enum value
     *
     * @return modified String
     */
    private String colorizeLine(String s, CColors color) {
        return color + s + RESET;
    }

    /**
     * Adds Styles and reset labels on this line
     *
     * @param s     String line to modify
     * @param style TextStyle enum value
     *
     * @return modified String
     */
    private String stylizeLine(String s, TextStyle style) {
        return style + s + RESET;
    }

    /**
     * Sets a Background color to full line and resets style at end
     *
     * @param s       String text line to modify
     * @param bgColor desired Background color from BgColors enum
     *
     * @return modified String
     */
    private String setLineBackground(String s, BgColors bgColor) {
        return bgColor + s + RESET;
    }

    /**
     * Quit all Color, Style and BG modifiers
     *
     * @param line text to modify
     *
     * @return text without those characters
     */
    private String removeStyleAndColorLine(String line) {
        var textParts = line.split(String.valueOf(COLOR_CHAR));
        for (int i = 1; i < textParts.length; i++) {
            textParts[i] = textParts[i].substring(COLOR_LABEL_CHAR_SIZE - 1);
        }
        var sb = new StringBuilder();
        for (String part : textParts) sb.append(part);
        return sb.toString();
    }
    //===================   TEXT_MANIPULATION   ===================

    /**
     * Splits a multi line string into array of String lines
     *
     * @param text multi line text
     *
     * @return String Lines Array
     */
    private String[] splitTextInLines(String text) {
        return text.split(NEW_LINE);
    }

    //----------------------------------------------------------------------------------------------------PUBLIC_METHODS

    /**
     * Method that creates a new TextObject with the current content of this one but with a diferent sizes
     *
     * @param newWidth  new desired width in characters
     * @param newHeight new desired height in lines
     *
     * @return new TextObject
     *
     */
    public TextObject getResizedText(int newWidth, int newHeight) {
        return new TextObject(this, scroll, newWidth, newHeight);
    }

    /**
     * Method to align current text at center vertically by adding necessary blank space lines at top and bottom.
     * It fills all MAX_HEIGHT
     *
     * @return this TextObject to allow chain calls.
     */
    public TextObject alignTextMiddle() {
        int remainingLines = MAX_HEIGHT - getTotalHeight();
        int num;
        if (remainingLines > 1) {
            num = Math.floorDiv(remainingLines, 2);
            for (int i = 0; i < num; i++) {
                addSafe(0, BLANK_SPACE);
                addSafe(BLANK_SPACE);
            }
        }
        return this;
    }

    /**
     * Method to align current text at top vertically by adding necessary blank space lines at bottom.
     * It fills all MAX_HEIGHT
     *
     * @return this TextObject to allow chain calls.
     */
    public TextObject alignTextTop() {
        if (getTotalHeight() < MAX_HEIGHT) {
                addSafe(BLANK_SPACE);
                alignTextTop();
        }
        return this;
    }
    public TextObject alignTextTop(int size) {
        if (getTotalHeight() < size) {
            addSafe(BLANK_SPACE);
            alignTextTop(size);
        }
        return this;
    }

    /**
     * Method to align current text at right horizontally by adding necessary blank space chars at left.
     * It fills all MAX_WIDTH
     *
     * @return this TextObject to allow chain calls.
     */
    public TextObject alignTextRight() {
        for (int i = 0; i < totalHeight; i++) {
            text.set(i, lineToRight(text.get(i)));
        }
        return this;
    }

    /**
     * Method to align current text at center horizontally by adding necessary blank space chars at left and right.
     * It fills all MAX_WIDTH
     *
     * @return this TextObject to allow chain calls.
     */
    public TextObject alignTextCenter() {
        for (int i = 0; i < totalHeight; i++) {
            text.set(i, centerLine(text.get(i)));
        }
        return this;
    }

    /**
     * Method that sets a color/s for all current text
     *
     * @param colors optional parameter,
     *               if present it assigns the only CColor enum value to all text and a RESET character at the end.
     *               if there are more than one CColor value,
     *               it applies color each line by repeating the pattern (colorA,colorB,colorC --> A,B,C,A,B,C,A..)
     *               Otherwise it peeks a random color for each line.
     *
     * @return this TextObject allow chain call.
     *
     * @see ColorFactory
     */
    public TextObject colorizeAllText(CColors... colors) {
        switch (colors.length) {
            case 0 -> {
                for (int i = 0; i < totalHeight; i++) {
                    text.set(i, colorizeLine(text.get(i), getRandomColor()));
                }
            }
            case 1 -> {
                int lastIndex = text.size() - 1;
                text.set(0, colors[0] + text.get(0));
                text.set(lastIndex, text.get(lastIndex) + RESET);
            }
            default -> {
                int colorCount = 0;
                for (int i = 0; i < totalHeight; i++) {
                    text.set(i, colorizeLine(text.get(i), colors[colorCount]));
                    colorCount++;
                    if (colorCount >= colors.length) colorCount = 0;
                }

            }
        }
        return this;
    }

    /**
     * Method that sets a style for all current text
     *
     * @param style sets same style tag for all text and a RESET character at end of last line in text
     *
     * @return this TextObject allow chain call.
     *
     * @see ColorFactory
     */
    public TextObject stylizeAllText(TextStyle style) {
        this.txtStyle=style;
        for (int i = 0; i <getTotalHeight(); i++) {
            this.text.set(i,style+text.get(i)+ RESET);
        }
        return this;
    }

    /**
     * Method that sets a background for all current text
     *
     * @param bg sets same Background tag for all text
     *           and a RESET character at end of each line in text to avoid printing more than MAX_WIDTH
     *
     * @return this TextObject allow chain call.
     *
     * @see ColorFactory
     */
    public TextObject setAllTextBackground(BgColors bg) {
        this.bgColor =bg;
        for (int i = 0; i < totalHeight; i++) {
            text.set(i, setLineBackground(text.get(i), bg));
        }
        return this;
    }
    public TextObject setAllTextColor(CColors color) {
        this.txtColor=color;
        for (int i = 0; i < totalHeight; i++) {
            text.set(i, color+text.get(i)+ RESET);
        }
        return this;
    }

    public TextObject trimAllText(){
        for (int i = 0; i <getTotalHeight(); i++) {
            text.set(i,removeStyleAndColorLine(text.get(i)).trim());
        }
        return this;
    }
    public TextObject applyAllTextColors(Boolean mustFill){
        for (int i = 0; i <getTotalHeight(); i++) {
            text.set(i,
                    (bgColor !=null? bgColor.toString():"")
                            +(txtColor!=null?txtColor.toString():"")
                            +(txtStyle!=null?txtStyle.toString():"")
                            +text.get(i)
                            +(mustFill?BLANK_SPACE.repeat(MAX_WIDTH-countValidCharacters(text.get(i))):"")
                            + RESET);
        }
        return this;
    }
    public String applyTextColorsToLine(String line){
            return (bgColor !=null? bgColor.toString():"")
                    +(txtColor!=null?txtColor.toString():"")
                    +(txtStyle!=null?txtStyle.toString():"")
                    +line
                    + RESET;

    }
    public String getTextColorsModifiers(){
        return (bgColor !=null? bgColor.toString():"")
                +(txtColor!=null?txtColor.toString():"")
                +(txtStyle!=null?txtStyle.toString():"");
    }

    @Override
    public String toString() {
        final var sb = new StringBuilder();
        for (int i = 0; i < getTotalHeight(); i++) {
            String line = text.get(i);
            sb.append(NEW_LINE).append(line);
        }
        return sb.toString();
    }

    public TextObject fillAllLines() {
        for (String line : text) {
            fillLine(line);
        }
        return this;
    }

    public String print() {
        return toString();
    }

    public String printLine(int index) {
        if (hasText()) return text.remove(index);
        return "";
    }
    public TextObject smartReplaceReset() {

        for (int i = 0; i < text.size(); i++) {
            String line = text.get(i);
            if (line.contains(SMART_RESET)){
                text.set(i,line.replace(SMART_RESET, RESET+getTextColorsModifiers()));
            }
        }
        return this;
    }

}
