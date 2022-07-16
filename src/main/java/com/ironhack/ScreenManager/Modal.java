package com.ironhack.ScreenManager;

public  enum Modal {CANCEL("Cancel"),OK("Confirm");
        private final String label;

        Modal(String label){
            this.label=label;
        }

        @Override
        public String toString() {
            return this.label;
        }
}
