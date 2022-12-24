import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Test {

    private String fileName="CFG.txt";
    private ContextFreeGrammar CFG;
    public Test(){
        this.CFG=new ContextFreeGrammar();
        loadFromFile();
    }

    private void loadFromFile(){
        try {
            Scanner scanner = new Scanner(new File(fileName));
            boolean isFirstLine=true; //Because first line specified for the alphabet
            boolean isFirstRule=true;
            while (scanner.hasNextLine()) {
                String line=scanner.nextLine();
                if (isFirstLine){
                    this.CFG.addAlphabet(line);
                    isFirstLine=false;
                }else{//Otherwise it must be a rule
                    if (isFirstRule){//Because first rule contains start variable
                        isFirstRule=false;
                        CFG.addRuleWithStartVariable(line);
                    }else{//Otherwise, which is the last possible case, it is an ordinary rule
                        CFG.addRule(line);
                    }
                }
            }
            //at this step, construction of given CFG via txt is completed

            //now, finding its Chomsky Normal Form


            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
