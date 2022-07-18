package com.ironhack.MenuUtilities;


import java.util.Scanner;
import java.util.Objects;


public class Menu<T> {

        // Menu options
        private final MenuOption<T>[] options;
        // Display title
        private final String title;
        // Question before user input
        private final String question;

        public Menu(MenuOption<T>[] options, String title, String question) {
            this.options = options;
            this.title = title;
            this.question = question;
        }

        public T display() {

            // If there are no options null is returned
            if(options.length == 0) return null;

            // One based index (starts at 1 for user-friendly reasons)
            MenuOption<T> selected = null;

            do {
                final var scanner = new Scanner(System.in);
                try {
                    System.out.println("\n---[ " + title + " ]---");
                    for (int i = 0; i < options.length; i++) options[i].display(i);
                    System.out.print("\n" + question + ": ");
                    int selectedIndex = scanner.nextInt();
                    System.out.println();
                    if(selectedIndex <= 0 || selectedIndex > options.length) {
                        Errors.logError("Please, provide a valid number between 1 and " + options.length + ".");
                        continue;
                    }

                    selected = options[selectedIndex - 1];

                    if(!selected.isAvailable()) {
                        Errors.logError("This option is not available, please, select another one.");
                        selected = null;
                    }
                } catch (Exception ignored) {
                    Errors.logError("Please, provide a valid number.");
                }
            } while(selected == null);

            // Converts the selected value into a 0 based index value (starts at 0)
            return selected.getValue();
        }
    }

