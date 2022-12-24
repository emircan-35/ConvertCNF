import java.util.ArrayList;
import java.util.Random;

public class ContextFreeGrammar {
    private Variable startVariable; //Because it is a special variable, it is also stored separately
    private ArrayList<Terminal> alphabet;

    private ArrayList<Rule> rules;

    private ContextFreeGrammar chomskyNormalForm;
    public ContextFreeGrammar(){
        this.alphabet=new ArrayList<Terminal>();
        this.rules=new ArrayList<Rule>();

    }

    public void addAlphabet(String line){
        //here, add the alphabet
        //The alphaber line has a pattern like E=X,Y,Z ...
        //So, we can split it with = in first, then interested in , in it.
        String[] alphabetSplitFromText=((line.split("="))[1]).split(",");
        //We are interested with the right side, which has the pattern

        //Iterating over the alphabet, creating terminals and storing them in the CFG
        for (int i=0;i<alphabetSplitFromText.length;i++){
            Terminal terminal=new Terminal(alphabetSplitFromText[i]);
            this.alphabet.add(terminal);
        }
    }

    public void addRule(String line){
        //Here, add the rule to the
        String[] lineFirstSplit=line.split("-");
        //Create the rule and add it
        this.rules.add(new Rule(lineFirstSplit,this.alphabet));
    }

    public void addRuleWithStartVariable(String line){
        //First, splitting with '-'
        String[] lineFirstSplit=line.split("-");
        //The first element is the start variable, then first create and insert it
        this.startVariable= new Variable(lineFirstSplit[0]);

        //Create the rule and add it
        this.rules.add(new Rule(lineFirstSplit,this.alphabet));
    }

    public ContextFreeGrammar getChomskyNormalForm(){
        this.chomskyNormalForm= new ContextFreeGrammar();
        changeStartVariable();

        return this.chomskyNormalForm;
    }
    private void changeStartVariable(){
        //!assuming S0 is never used before!
        //add its rule
        this.chomskyNormalForm.addRuleWithStartVariable("S0-S");
    }


}
