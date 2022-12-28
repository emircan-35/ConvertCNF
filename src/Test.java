import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Test {

    private String path;
    private ContextFreeGrammar CFG;
    public Test(String path){
        this.path=path;
        this.CFG=new ContextFreeGrammar();
        loadFromFile();
    }

    private void loadFromFile(){
        try {
            Scanner scanner = new Scanner(new File(path));
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
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //now, finding its Chomsky Normal Form
        CFG.convertChomskyNormalForm();

        //Done :)
    }

}
