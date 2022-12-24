public class Terminal {
    private String terminal;

    public Terminal(String terminal) {
        this.terminal = terminal;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public boolean isEqual(String element){
        if (this.terminal.equals(element)) return true;
        return false;
    }
}
