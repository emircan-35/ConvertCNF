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
        //Create the rule and add it by also taking care of OR (|) symbol

        String[] rightSideRulesSeparatedByOr=lineFirstSplit[1].split("\\|");
        for (int i = 0; i < rightSideRulesSeparatedByOr.length; i++) {
            String[] lineSplitWithOrSymbol=new String[2];
            lineSplitWithOrSymbol[0]=lineFirstSplit[0];
            lineSplitWithOrSymbol[1]=rightSideRulesSeparatedByOr[i];
            this.rules.add(new Rule(lineSplitWithOrSymbol,this.alphabet));
        }
    }

    public void addRuleWithStartVariable(String line){
        //First, splitting with '-'
        String[] lineFirstSplit=line.split("-");
        //The first element is the start variable, then first create and insert it
        this.startVariable= new Variable(lineFirstSplit[0]);
        addRule(line);
    }

    public ContextFreeGrammar getChomskyNormalForm(){
        this.chomskyNormalForm= new ContextFreeGrammar();
        //Step 1, changing step variable
        changeStartVariable();

        //Step 2, taking care of all € rules


        return this.chomskyNormalForm;
    }
    private void changeStartVariable(){
        //!assuming S0 is never used before!
        //add its rule
        this.chomskyNormalForm.addRuleWithStartVariable("S0-S");
    }


}
