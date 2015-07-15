package com.masstersoft.checktv_2_0_2.app;

/**
 * Created by WismutPC on 20.02.15.
 */
public class KeyNames {

    public static enum Keys {

        // Numbers
        ONE, TWO, THREE,
        FOUR, FIVE, SIX,
        SEVEN, EIGHT, NINE,
        ZERO,
        // Dot
        DOT,
        // Mathematcs operations
        PLUS, MINUS, MULTIPLY, DIVISION,
        // Other function of calculator
        EQUAL, DEL, CLEAR, PLUS_MINUS,
        PERCENT,
        // Nothing was pressed
        NONE
    }

    public static boolean isOperation(Keys k) {
        switch (k) {
            case MINUS:
            case PLUS:
            case MULTIPLY:
            case DIVISION:
                return true;
            default:
                return false;
        }
    }

    public static boolean isNumber(Keys k) {
        switch (k) {
            case ONE:
            case TWO:
            case THREE:
            case FOUR:
            case FIVE:
            case SIX:
            case SEVEN:
            case EIGHT:
            case NINE:
            case ZERO:
                return true;
            default:
                return false;
        }
    }

    public static boolean isDot(Keys k) {
        return k == Keys.DOT;
    }
}
