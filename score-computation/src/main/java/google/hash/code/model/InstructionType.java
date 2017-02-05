package google.hash.code.model;

public enum InstructionType {

    LOAD('L'), DELIVER('D');

    private char letter;

    InstructionType(char letter) {
        this.letter = letter;
    }

    public char getLetter() {
        return letter;
    }
}
