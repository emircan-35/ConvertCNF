import java.util.ArrayList;

public class Rule {
    private Variable leftSide;
    private ArrayList<RightSideElement> rightSide; //Because right side can occur from both type elements


    public Rule(String[] lineSplit,ArrayList<Terminal> alphabet) {
        this.rightSide=new ArrayList<>();
        loadRule(lineSplit,alphabet);
    }


    private void loadRule(String[] line,ArrayList<Terminal> alphabet){
        this.leftSide=new Variable(line[0]);
        //for the right side, every char represents a variable or a terminal, so
        char[] rightSide=line[1].toCharArray();
        for(int i=0;i<rightSide.length;i++){
            //from char to str
            String element= String.valueOf(rightSide[i]);
            if (isVariable(element,alphabet)){
                this.rightSide.add(new RightSideElement(new Variable(element)));
            }else{
                this.rightSide.add(new RightSideElement(new Terminal(element)));
            }
        }

    }

    public boolean isVariable(String element,ArrayList<Terminal> alphabet){
        for (int i = 0; i < alphabet.size(); i++) {
            if (alphabet.get(i).isEqual(element)) return false;
        }
        return true;
    }
    public Variable getLeftSide() {
        return leftSide;
    }

    public void setLeftSide(Variable leftSide) {
        this.leftSide = leftSide;
    }

    public ArrayList<RightSideElement> getRightSide() {
        return rightSide;
    }

    public void setRightSide(ArrayList<RightSideElement> rightSide) {
        this.rightSide = rightSide;
    }

    private class RightSideElement {
        private Variable variable;
        private Terminal terminal;

        public RightSideElement(Variable variable) {
            if (this.terminal!=null)return;
            this.variable = variable;
        }

        public RightSideElement(Terminal terminal) {
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
