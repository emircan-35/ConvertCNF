import java.util.*;

public class ContextFreeGrammar {
    private Variable startVariable; //Because it is a special variable, it is also stored separately
    private ArrayList<Terminal> alphabet;

    private ArrayList<Rule> rules;

    private ArrayList<Character> usedVariableLetters;
    private ArrayList<String> leftSideUsedLetters;


    public ContextFreeGrammar(){
        this.alphabet=new ArrayList<Terminal>();
        this.rules=new ArrayList<Rule>();
        this.usedVariableLetters=new ArrayList<>();
        this.leftSideUsedLetters=new ArrayList<>();

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
        if (lineFirstSplit.length==1){
            String[] lineFirstSplitAddedEmpty=new String[2];
            lineFirstSplitAddedEmpty[0]=lineFirstSplit[0];
            lineFirstSplitAddedEmpty[1]="€";
            lineFirstSplit=lineFirstSplitAddedEmpty;
        }
        if (!leftSideUsedLetters.contains(lineFirstSplit[0])){
            leftSideUsedLetters.add(lineFirstSplit[0]);
        }
        String[] rightSideRulesSeparatedByOr=lineFirstSplit[1].split("\\|");
        for (int i = 0; i < rightSideRulesSeparatedByOr.length; i++) {
            String[] lineSplitWithOrSymbol=new String[2];
            lineSplitWithOrSymbol[0]=lineFirstSplit[0];
            lineSplitWithOrSymbol[1]=rightSideRulesSeparatedByOr[i];
            //add them to the above variableLetters, so that we are able to produce unique variable when needed
            char[] variablesUsed=rightSideRulesSeparatedByOr[i].toCharArray();
            for (int j = 0; j < variablesUsed.length; j++) usedVariableLetters.add(variablesUsed[j]);
            this.rules.add(new Rule(lineSplitWithOrSymbol,this.alphabet));
        }
    }
    private String produceUniqueVariable(){
        Random rd=new Random();
        while(true){
            int producedLetterDecimal=rd.nextInt(65,91);
            char producedLetter=(char)producedLetterDecimal;
            if (!usedVariableLetters.contains(producedLetter)){
                return String.valueOf(producedLetter);
            }
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
        handleEmptyRules();

        handleUnitRules();

        convertProperForm();
        writeContextFreeWithRulesORed();
    }
    private void handleUnitRules(){
        //Iterating over all the rules
        Object[] rulesLocal=this.rules.toArray();
        for (int i = 0; i < rulesLocal.length; i++) {
            Rule ruleToCheck= (Rule) rulesLocal[i];
            if (ruleToCheck.isUnit()){
                //Remove this rule
                if(!this.rules.remove(ruleToCheck)) System.out.println("problem when deleting");
                String rightJustRemoved=ruleToCheck.getRightUnit();
                ArrayList<String> newRightRules=getRightOfThis(rightJustRemoved);
                //Now adding the new rule
                for (int j = 0; j < newRightRules.size(); j++) {
                    this.addRule(ruleToCheck.getLeftSide().getVariable()+"-"+newRightRules.get(j));
                }
            }
        }
        //Last deletion for the ones coming again
        for (int i = 0; i < this.rules.size(); i++) {
            if (this.rules.get(i).isUnit()){
                this.rules.remove(i);
                i=0;
            }
        }

        //Delete duplicate rules
        ArrayList<Rule> newList=new ArrayList<>();
        for (int i = 0; i < this.rules.size(); i++) {
            boolean isContain=false;
            Rule ruleToCheck=this.rules.get(i);
            for (int j = 0; j <newList.size() ; j++) {
                if (newList.get(j).equals(ruleToCheck)){
                    isContain=true;
                    break;
                }
            }
            if (!isContain){
                newList.add(ruleToCheck);
            }
        }
        this.rules=newList;


    }

    private void convertProperForm(){
        int index=0;
        while(index<this.rules.size()){//Until converting all of them
            Rule ruleLocal=this.rules.get(index);
            if (ruleLocal.getRightSide().size()>2){
                String leftSide=ruleLocal.getLeftSide().getVariable();
                String rightSide=ruleLocal.getRightAsString();
                char[] rightSideAsChar=rightSide.toCharArray();
                String remainingRight="";
                String newVariable=produceUniqueVariable();
                for (int i = 1; i < rightSideAsChar.length; i++) remainingRight+=rightSideAsChar[i];
                this.addRule(newVariable+"-"+remainingRight);
                this.addRule(leftSide+"-"+rightSideAsChar[0]+newVariable);
                this.rules.remove(ruleLocal);
                index=0;
            }
            index++;
        }
    }
    private void writeContextFreeWithRulesORed(){
        String[] rightSideElements=new String[leftSideUsedLetters.size()];
        for (int i = 0; i < rightSideElements.length; i++) rightSideElements[i]="";
        for (int i = 0; i < leftSideUsedLetters.size(); i++) {
            for (int j = 0; j < this.rules.size(); j++) {
                Rule ruleLocal=this.rules.get(j);
                if (ruleLocal.getLeftSide().getVariable().equals(leftSideUsedLetters.get(i))){
                    rightSideElements[i]+=ruleLocal.getRightAsString()+" | ";
                }
            }
        }
        
        //Now writing
        for (int i = 0; i < leftSideUsedLetters.size(); i++) {
            System.out.println(leftSideUsedLetters.get(i)+" - "+rightSideElements[i]);
        }
    }
    private ArrayList<String> getRightOfThis(String leftSide){
        Object[] rulesLocal=this.rules.toArray();
        ArrayList<String> rightSide=new ArrayList<>();
        for (int i = 0; i < rulesLocal.length; i++) {
            Rule ruleToCheck= (Rule) rulesLocal[i];
            if (ruleToCheck.getLeftSide().getVariable().equals(leftSide)){
                rightSide.add(ruleToCheck.getRightAsString());
            }
        }
        return rightSide;
    }
    private void writeRules(){
        for (int i = 0; i < this.rules.size(); i++) {
            System.out.println(rules.get(i).getRuleAsString());
        }
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
                //Before deleting, copy the rule
                Variable leftSide=rule.getLeftSide();
                rules.remove(i);
                foundEmptyLeftSide(leftSide);
                i=0;
            }
        }

    }

    private void foundEmptyLeftSide(Variable leftSide) {
        //Step 1, iterate all the rules
        Object[] rules=  this.rules.toArray();
        for (int i = 0; i < rules.length; i++) {
            Rule rule = (Rule) rules[i];
            ArrayList<Rule.RightSideElement> rightSide = rule.getRightIfContains(leftSide);
            if (rightSide==null)continue;
            //Iterate over the right side
            //For every occurrence of deleted variable, insert a new one with its deleted form
            String ruleAsStringToAdd=rule.getRuleAsString();
            this.rules.remove(rule);
            String[] ruleSplit=ruleAsStringToAdd.split("-");
            String leftSideToAdd=ruleSplit[0];
            String rightSideToAdd=ruleSplit[1];
            ArrayList<String> allPossibilities=new ArrayList<>();
            ArrayList<Character> currentString=new ArrayList<>();
            for (char c : rightSideToAdd.toCharArray()) {
                currentString.add(c);
            }
            addAllPossibilities(allPossibilities,currentString,0,leftSide.getAsChar());
            //Now, add all the possibilities
            for (int j = 0; j < allPossibilities.size(); j++) {
                this.addRule(leftSideToAdd+"-"+allPossibilities.get(j));
            }
        }
    }
    private void addAllPossibilities(ArrayList<String> allPossibilities,ArrayList<Character> currentString,int startingIndex,Character deletedChar){
        for (int i = startingIndex; i <currentString.size() ; i++) {
            if (currentString.get(i)==deletedChar){
                ArrayList<Character> deletedForm=new ArrayList<>();
                deletedForm.addAll(currentString);
                deletedForm.remove(i);
                addAllPossibilities(allPossibilities,deletedForm,0,deletedChar);

                //Existing form
                addAllPossibilities(allPossibilities,currentString,i+1,deletedChar);
            }
        }
        String stringToAdd=currentString.toString();
        stringToAdd=stringToAdd.replace("[","");
        stringToAdd=stringToAdd.replace("]","");
        stringToAdd=stringToAdd.replace(",","");
        stringToAdd=stringToAdd.replace(" ","");


        //Check if it contains
        boolean isContain=false;
        for (int i = 0; i < allPossibilities.size(); i++) {
            if (allPossibilities.get(i).equals(stringToAdd)){
                isContain=true;
                break;
            }
        }
        if (!isContain){
            allPossibilities.add(stringToAdd);
        }
    }
}
