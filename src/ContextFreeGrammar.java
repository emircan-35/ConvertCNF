import java.util.*;

public class ContextFreeGrammar {
    private Variable startVariable; //Because it is a special variable, it is also stored separately
    private ArrayList<Terminal> alphabet;

    private ArrayList<Rule> rules;

    private ArrayList<String> leftSideUsedLetters;


    public ContextFreeGrammar(){
        this.alphabet=new ArrayList<Terminal>();
        this.rules=new ArrayList<Rule>();
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
            this.rules.add(new Rule(lineSplitWithOrSymbol,this.alphabet));
        }
    }
    private String produceUniqueVariable(){
        Random rd=new Random();
        while(true){
            int producedLetterDecimal=rd.nextInt(65,91);
            char producedLetter=(char)producedLetterDecimal;
            if (!leftSideUsedLetters.contains(String.valueOf(producedLetter))){
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
        System.out.println("CFG FORM\n");
        writeContextFreeWithRulesORed(true);

        System.out.println("\nELIMINATING € RULES\n");
        //Step 2, taking care of all € rules
        handleEmptyRules();
        writeContextFreeWithRulesORed(true);

        System.out.println("\nELIMINATING UNIT PRODUCTIONS\n");
        handleUnitRules();
        writeContextFreeWithRulesORed(true);

        System.out.println("\nELIMINATING TERMINALS\n");
        eliminateTerminals();
        writeContextFreeWithRulesORed(true);

        System.out.println("\nCONVERTING PROPER FORM AND SIMPLIFYING\n");
        convertProperForm();
        writeContextFreeWithRulesORed(true);
    }

    private void eliminateTerminals(){
        int rulePointer=0;
        while(rulePointer<this.rules.size()){
            //Get the rule
            Rule ruleLocal=this.rules.get(rulePointer);

            //Get the right side
            ArrayList<Rule.RightSideElement> rightSide=ruleLocal.getRightSide();

            //Search for a terminal located on the right side
            if (rightSide.size()>1) {
                for (int i = 0; i < rightSide.size(); i++) {
                    if (rightSide.get(i).getTerminal()!=null){
                        String deletedTerminal=rightSide.get(i).getTerminal().getTerminal();
                        String newVariableLetter=produceUniqueVariable();
                        if (newVariableLetter.equals("S")){
                            System.out.println("problem HERE");
                        }
                        //Get the right side as string
                        String rightSideAsString=ruleLocal.getRightAsString();
                        //Get left side
                        String letfSide=ruleLocal.getLeftSide().getVariable();
                        //Delete this rule
                        this.rules.remove(rulePointer);

                        //Create new variable
                        this.addRule(newVariableLetter+"-"+deletedTerminal);

                        //Replace all the terminal values with newly created variable
                        rightSideAsString=rightSideAsString.replaceAll(deletedTerminal,newVariableLetter);

                        //Add new rule without this terminal
                        this.addRule(letfSide+"-"+rightSideAsString);

                        //Look from start
                        rulePointer=-1;
                        break;
                    }
                }
            }
            rulePointer++;
        }
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
        //To prevent needless duplications, holding newly created rules

        while(index<this.rules.size()){//Until converting all of them
            Rule ruleLocal=this.rules.get(index);
            if (ruleLocal.getRightSide().size()>2){
                String leftSide=ruleLocal.getLeftSide().getVariable();
                String rightSide=ruleLocal.getRightAsString();
                char[] rightSideAsChar=rightSide.toCharArray();
                String remainingRight="";
                for (int i = 1; i < rightSideAsChar.length; i++) remainingRight+=rightSideAsChar[i];

                String newVariable=produceUniqueVariable();
                this.addRule(newVariable+"-"+remainingRight);
                this.addRule(leftSide+"-"+rightSideAsChar[0]+newVariable);
                this.rules.remove(ruleLocal);
                index=0;
            }
            index++;
        }
    }


    private void writeContextFreeWithRulesORed(boolean willSimplified){
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
        //Using simplification
        if (willSimplified){
            for (int i = 0; i < rightSideElements.length; i++) {
                for (int j = i+1; j < rightSideElements.length; j++) {
                    if (rightSideElements[i]!=null&&rightSideElements[i].equals(rightSideElements[j])){
                        //Then no need to duplicate i, deleting j
                        String letterDeletedLeft=leftSideUsedLetters.get(j);
                        leftSideUsedLetters.set(j,null);
                        rightSideElements[j]=null;
                        for (int k = 0; k < rightSideElements.length; k++) {
                            if (rightSideElements[k]!=null&&leftSideUsedLetters.get(i)!=null){
                                rightSideElements[k]=rightSideElements[k].replaceAll(letterDeletedLeft,leftSideUsedLetters.get(i));
                            }
                        }
                        i=0;
                    }
                }
            }
        }

        for (int i = 0; i < leftSideUsedLetters.size(); i++) {
            if (leftSideUsedLetters.get(i)!=null) {
                System.out.println(leftSideUsedLetters.get(i) + " - " + (rightSideElements[i].substring(0, rightSideElements[i].length() - 2)));
            }
        }
        //To prevent future errors, construct CFG with these new rules
        for (int i = 0; i < rightSideElements.length; i++) {
            if (rightSideElements[i]!=null)rightSideElements[i]=rightSideElements[i].replaceAll("\\s+","");
        }

        Object[] leftSideLettersCopy= leftSideUsedLetters.toArray();
        rules=new ArrayList<>();
        leftSideUsedLetters=new ArrayList<>();
        for (int i = 0; i < leftSideLettersCopy.length; i++) {
            if (leftSideLettersCopy[i]!=null&&((String) leftSideLettersCopy[i]!="")&&rightSideElements[i]!=null&&rightSideElements[i]!=""){
                String[] rightSideSeparated=rightSideElements[i].split("\\|");
                for (int j = 0; j < rightSideSeparated.length; j++) {
                    this.addRule(((String) leftSideLettersCopy[i])+"-"+rightSideSeparated[j]);
                }
            }
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
