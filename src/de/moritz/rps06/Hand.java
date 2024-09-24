package de.moritz.rps06;

public enum Hand {

    ROCK,
    PAPER,
    SCISSOR,
    EVEN;

    public Hand battle(Hand hand2) {
        return battle(this, hand2);
    }

    public static Hand battle(Hand hand1, Hand hand2) {
        switch (hand1) {
            case ROCK: {
                switch (hand2) {
                    case PAPER: {
                        return PAPER;

                    }
                    case SCISSOR: {
                        return ROCK;
                    }
                    case ROCK: {
                        return EVEN;
                    }
                }
                break;
            }
            case PAPER: {
                switch (hand2) {
                    case ROCK: {
                        return PAPER;
                    }
                    case SCISSOR: {
                        return SCISSOR;
                    }
                    case PAPER: {
                        return EVEN;
                    }
                }
                break;
            }
            case SCISSOR: {
                switch (hand2) {
                    case ROCK: {
                        return ROCK;
                    }
                    case PAPER: {
                        return SCISSOR;
                    }
                    case SCISSOR: {
                        return EVEN;
                    }

                }
                break;
            }
        }
        return null;
    }

}
