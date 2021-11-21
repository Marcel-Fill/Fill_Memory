package com.example.memory;

public class Card {
    private boolean visible = false;
    private int value = -1;

    @Override
    public String toString() {
        return "Card{" +
                "visible=" + visible +
                ", value=" + value +
                '}';
    }

    public boolean isVisible() {
        return visible;
    }

    public int getValue() {
        return value;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
