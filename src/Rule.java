import java.util.ArrayList;

public class Rule {
    private Variable leftSide;
    private ArrayList<VariableTerminal> rightSide; //Because right side can occur from both type elements


    public Rule(String[] lineSplit,ArrayList<Terminal> alphabet) {
        loadRule(lineSplit,alphabet);
    }


    private void loadRule(String[] line,ArrayList<Terminal> alphabet){
        this.leftSide=new Variable(line[0]);

        //for the right side, every char represents a variable or a terminal, so
        char[] rightSide=line[1].toCharArray();
        for(int i=0;i<rightSide.length;i++){

        }

    }
    public Variable getLeftSide() {
        return leftSide;
    }

    public void setLeftSide(Variable leftSide) {
        this.leftSide = leftSide;
    }

    public ArrayList<VariableTerminal> getRightSide() {
        return rightSide;
    }

    public void setRightSide(ArrayList<VariableTerminal> rightSide) {
        this.rightSide = rightSide;
    }

    private class VariableTerminal{
        private Variable variable;
        private Terminal terminal;

        public VariableTerminal(Variable variable) {
            if (this.terminal!=null)return;
            this.variable = variable;
        }

        public VariableTerminal(Terminal terminal) {
            if (this.variable!=null)return;
            this.terminal = terminal;
        }

        public Variable getVariable() {
            return variable;
        }

        public void setVariable(Variable variable) {
            this.variable = variable;
        }

        public Terminal getTerminal() {
            return terminal;
        }

        public void setTerminal(Terminal terminal) {
            this.terminal = terminal;
        }
    }

}
