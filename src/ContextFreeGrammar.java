import java.util.ArrayList;
import java.util.Random;

public class ContextFreeGrammar {
    private Variable startVariable; //Because it is a special variable, it is also stored separately
    private ArrayList<Terminal> alphabet;

    private ArrayList<Rule> rules;





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

    public void convertChomskyNormalForm(){
        //Step 1, changing step variable
        changeStartVariable();

        //Step 2, taking care of all € rules
        System.out.println("asd");
        handleEmptyRules();
    }
    private void changeStartVariable(){
        //!assuming S0 is never used before!
        //add its rule
        this.addRuleWithStartVariable("S0-S");
    }
    private void handleEmptyRules(){
        //IMPORTANT! WE only handle empty string which does not relate with start variable !
        //Step 1, iterate all the rules
        for (int i = 0; i < rules.size(); i++) {
            Rule rule=rules.get(i);
            if ((!rule.getLeftSide().getVariable().equals(this.startVariable.getVariable())) &&rule.getRightSide().size()==1 && (rule.getRightSide().get(0)).isEmpty()){
                //here means this rule should be deleted
                //Before deleting, copy its left side
                Variable leftSide=rule.getLeftSide();
                rules.remove(i);
                foundEmptyLeftSide(leftSide);
            }
        }

    }

    private void foundEmptyLeftSide(Variable leftSide) {
        //Step 1, iterate all the rules
        for (int i = 0; i < rules.size(); i++) {
            Rule rule = rules.get(i);
            ArrayList<Rule.RightSideElement> rightSide = rule.getRightIfContains(leftSide);
            if (rightSide==null)continue;
            //Iterate over the right side
            //For every occurrence of deleted variable, insert a new one with its deleted form
            //TODO char üzerinden yap!
        }
    }
}
