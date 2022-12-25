import java.util.ArrayList;

public class Rule {
    private Variable leftSide;
    private ArrayList<RightSideElement> rightSide; //Because right side can occur from both type elements

    public ArrayList<RightSideElement>  getRightIfContains(Variable variable){
        for (int i = 0; i < rightSide.size(); i++) {
            if (rightSide.get(i).isContain(variable)){
                return rightSide;
            }
        }
        return null;
    }
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
    public String getRightAsString(){
        String right="";
        for (int i = 0; i < this.rightSide.size(); i++) {
            right+=this.rightSide.get(i).getString();
        }
        return right;
    }
    public String getRightUnit(){
        return rightSide.get(0).getVariable().getVariable();
    }
    public boolean isUnit(){
        if (rightSide.size()==1&&rightSide.get(0).getVariable()!=null){
            return true;
        }
        return false;
    }
    public String getRuleAsString(){
        String leftSide=this.leftSide.getVariable();
        String rightSideAsString="";
        for (int i = 0; i < this.rightSide.size(); i++) {
            rightSideAsString+=this.rightSide.get(i).getString();
        }
        return leftSide+"-"+rightSideAsString;
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
    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Rule)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Rule c = (Rule) o;

        // Compare the data members and return accordingly
        return (this.getLeftSide().getVariable().equals(c.getLeftSide().getVariable())&&this.getRightAsString().equals(c.getRightAsString()));
    }

    public class RightSideElement {
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
        public String getString(){
            if (this.variable!=null){
                return this.variable.getVariable();
            }else{
                return this.terminal.getTerminal();
            }
        }
        public boolean isEmpty(){
            if (this.variable!=null && this.variable.getVariable().equals("€"))return true;
            return false;
        }



        public boolean isContain(Variable variable){
            if (this.variable!=null && this.variable.getVariable().equals(variable.getVariable()))return true;
            return false;
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
