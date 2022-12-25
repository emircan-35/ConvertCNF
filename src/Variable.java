public class Variable {
    private String variable;

    public Variable(String variable) {
        this.variable = variable;
    }

    public String getVariable() {
        return variable;
    }
    public Character getAsChar(){
        return variable.toCharArray()[0];
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }
}
