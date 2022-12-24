import java.util.ArrayList;

public class ContextFreeGrammar {
    private Variable startVariable; //Because it is a special variable, it is also stored separately
    private ArrayList<Terminal> alphabet;

    private ArrayList<Rule> rules;


    public ContextFreeGrammar(){}

    public void addAlphabet(String line){
        //here, add the alphaber
    }

    public void addRule(String line){
        //Here, add the rule to the
    }

    public void addRuleWithStartVariable(String line){

    }

    public ContextFreeGrammar getChomskyNormalForm(){
        ContextFreeGrammar CFG = new ContextFreeGrammar();
        return CFG;
    }

}
