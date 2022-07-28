package com.ironhack.Constants;

public class ColorFactory {

    public static final char DELETE_CURRENT_LINE='\r';
    public static final char NEW_LINE_CH = '\n';
    public static final String SMART_RESET = "$€€$";
    public static final String NEW_LINE = "\n";
    public static final char BLANK_SPACE_CH = ' ';
    public static final String BLANK_SPACE = " ";
    public static final String TAB_SPACE = "    ";


    public static final int COLOR_LABEL_CHAR_SIZE=6;
    public static final char COLOR_CHAR='\u001B';
    public enum TextStyle{

        BOLD("[001m"),
        UNDERLINE("[004m"),
        BLINK("[005m"),
        REVERSED("[007m"),
        RESET( "[000m");
        final String label;
        TextStyle(String label){
            this.label=label;
        }

        @Override
        public String toString() {
            return COLOR_CHAR+label;
        }

    }
    public enum BgColors{
        BLACK( "[040m"),

        RED( "[041m"),

        GREEN( "[042m"),

        YELLOW( "[043m"),

        BLUE( "[044m"),

        PURPLE( "[045m"),

        CYAN( "[046m"),

        WHITE( "[047m"),

        BRIGHT_BLACK( "[100m"),

        BRIGHT_RED( "[101m"),

        BRIGHT_GREEN( "[102m"),

        BRIGHT_YELLOW( "[103m"),

        BRIGHT_BLUE( "[104m"),

        BRIGHT_PURPLE( "[105m"),

        BRIGHT_CYAN( "[106m"),

        BRIGHT_WHITE( "[107m");
        final String label;
        BgColors(String label){
            this.label=label;
        }

        @Override
        public String toString() {
            return COLOR_CHAR+label;
        }

    }
    public enum CColors {
        BLACK( "[030m"),
        BRIGHT_BLACK( "[090m"),
        WHITE( "[037m"),
        BRIGHT_WHITE( "[097m"),
        RED( "[031m"),
        BRIGHT_RED( "[091m"),
        BRIGHT_YELLOW( "[093m"),
        YELLOW( "[033m"),
        GREEN( "[032m"),
        BRIGHT_GREEN( "[092m"),
        BRIGHT_CYAN( "[096m"),
        CYAN( "[036m"),
        BRIGHT_BLUE( "[094m"),
        BLUE( "[034m"),
        PURPLE( "[035m"),
        BRIGHT_PURPLE( "[095m");

         
        final String label;
            CColors(String label){
                this.label=label;
            }

        @Override
        public String toString() {
            return COLOR_CHAR+label;
        }
    }

    public static com.ironhack.Constants.ColorFactory.CColors getRandomColor(){
        int num;
            num = new java.util.Random().nextInt(4, com.ironhack.Constants.ColorFactory.CColors.values().length);
        return com.ironhack.Constants.ColorFactory.CColors.values()[num];
    }
    public static com.ironhack.Constants.ColorFactory.CColors getNextRainbowColor(com.ironhack.Constants.ColorFactory.CColors currentColor){
        int num= currentColor.ordinal()+1;
        if(num>= com.ironhack.Constants.ColorFactory.CColors.values().length||num<4)num=4;
        return com.ironhack.Constants.ColorFactory.CColors.values()[num];
    }

    public static String changeColors(String text, com.ironhack.Constants.ColorFactory.CColors...newColors){
        var charList= text.toCharArray();
        boolean isRandom = true;
        for (int i = 0; i < charList.length; i++) {
            if (i< newColors.length) isRandom=false;
            var currentChar=charList[i];
            if(charList[i]==COLOR_CHAR){
                var color = (isRandom?getRandomColor().toString():newColors[i].toString()).toCharArray();
                System.arraycopy(color, 0, charList, i, COLOR_LABEL_CHAR_SIZE - 1);
                i+=COLOR_LABEL_CHAR_SIZE-1;
            }
        }
        return new String(charList);

    }
    public static String rainbowCharacters(String line,int startVal){
        var color= com.ironhack.Constants.ColorFactory.CColors.values()[startVal];
        var charList=line.toCharArray();
        var sb= new StringBuilder();
        for (int i = 0; i < charList.length; i++) {
            char ch = charList[i];
            if (ch != BLANK_SPACE_CH) {
                if (ch==COLOR_CHAR)i+=COLOR_LABEL_CHAR_SIZE-1;
                else {
                    sb.append(color);
                    color = getNextRainbowColor(color);
                }
            }
            sb.append(ch);
        }
        return sb.append(com.ironhack.Constants.ColorFactory.TextStyle.RESET).toString();
    }

    public static boolean isASpecialCharacter(char ch){
        return ch==NEW_LINE_CH || ch==DELETE_CURRENT_LINE ||ch==COLOR_CHAR;
    }

    public static boolean containsSpecialCharacters(String str){
        return str.contains(NEW_LINE)||str.contains(DELETE_CURRENT_LINE+"")||str.contains(COLOR_CHAR+"[");
    }

}
