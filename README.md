# Converting given Context-Free Grammar (CFG) to Chomsky's Normal Form (CNF)







By Emircan Tepe





# Contents
[A.	DESCRIPTION	3](#_Toc123073862)

[B.	PSEUDO CODE	4](#_Toc123073863)

[I.	Eliminate Empty (€) Rules	4](#_Toc123073864)

[II.	Eliminate Unit Production	7](#_Toc123073865)

[III.	Eliminate terminals	9](#_Toc123073866)

[IV.	Break Variable Strings longer than 2	11](#_Toc123073867)

[C.	ONE SAMPLE SCREENSHOT OF THE PROGRAM	14](#_Toc123073868)

[D.	REFERENCES	15](#_Toc123073869)





















1. # DESCRIPTION
![](Aspose.Words.9550ed75-a6a3-48ae-89fb-21f14b59403f.004.png)	The program is based on the converting any given Context-Free Grammar (CFG) to Chomsky Normal Form (CNF). Any CFG has a CNF which makes the working on the grammar easier than the first given form. CNF is described as follows[1]:

A context-free grammar is in Chomsky Normal Form if every rule is of the form
A *à* BC
A*à* a
where a is any terminal and A, B, and C are any variables—except that B and 
C may not be the start variable. In addition, we permit the rule S ***à*** ε, where S is the start variable. 

`	`The conversion algorithm consists of many steps, basically can be divided to 4, as follows;

1. Eliminate Empty (€) Rules
1. Eliminate Unit Production
1. Eliminate Terminals
1. Break variable strings longer than 2

`	`In the development of this program, the only resource taken help from is the course’s text book[1], also referenced in the above formula. It should also be noted that benefits of Object Oriented Programming (OOP) is highly used instead of only working with functions and strings. Although the program has many phases that can be potentially reported in detail, only the four steps given above and their pseudo codes are detailed in the following section for the sake of simplicity, but one can still see the development

in the codes easily because it is written as possible as clear by following OOP design paradigms.



















1. # PSEUDO CODE
1. ## Eliminate Empty (€) Rules
Three different functions are used for this task. Pseudo codes of them are also provedid below with their brief descriptions.

1. **Function 1,** *addAllPossibilities*

`	`This function calculates the all posibilities for the given string based on the changing variable given to the function. It works recursively. It is **fully** designed by me without taking any help from outside, as all the program.

![](Aspose.Words.9550ed75-a6a3-48ae-89fb-21f14b59403f.005.png)

`	`It takes four input values: "allPossibilities", which is an array list to store the combinations; "currentString", which is the string to generate combinations from; "startingIndex", which is the index in the string to start generating combinations from; and "deletedChar", which is the character to potentially delete at each step.

`	`The function first iterates through the "currentString" and checks if the current character is equal to "deletedChar". If it is, it creates a copy of "currentString" called "deletedForm" and removes the current character from "deletedForm". It then calls itself recursively with "deletedForm" as the new "currentString" and a starting index of 0. It also calls itself recursively with the original "currentString" and an incremented starting index so that it will cal itself all the possibilities for a given string.

1. **Function 2,** *handleEmptyRules*

`	`The **handleEmptyRules** function is the method that handles rules in a grammar that have an empty right side. Below is the pseudo code and explanation of it.

![](Aspose.Words.9550ed75-a6a3-48ae-89fb-21f14b59403f.006.png)

- It initializes an array list called **justHandledLeftSideLetter** to keep track of the left sides of rules that have been handled to prevent infinite loops.
- It then iterates through the list of rules, represented by the variable **rules**.
- For each rule, it checks if the following conditions are met:
  - The left side of the rule is not the same as the start variable of the grammar.
  - The right side of the rule has a length of 1.
  - The right side of the rule is empty.
- If all of these conditions are met, the function checks if the left side of the rule is present in the **justHandledLeftSideLetter** list. If it is, the function removes the rule from the list and sets the loop counter **i** to 0 to start the loop over again.
- If the left side of the rule is not present in the **justHandledLeftSideLetter** list, the function stores the left side in the **justHandledLeftSideLetter** list and calls the **foundEmptyLeftSide** function with the left side as input. It then sets the loop counter **i** to 0 to start the loop over again.

`	`Overall, the **handleEmptyRules** function is designed to remove rules from a grammar that have an empty right side and to prevent infinite loops by keeping track of the left sides of rules that have been handled. It does this by iterating through the list of rules and checking for certain conditions, then removing the rule and starting the loop over again if necessary.

1. **Function 3,** *foundEmpryLeftSide*

`	`The **foundEmptyLeftSide** functionis the method that performs a manipulation on the rules of a grammar when a rule with an empty right side is found. Below is pseudo code and an explanation of what the function does.

![](Aspose.Words.9550ed75-a6a3-48ae-89fb-21f14b59403f.007.png)

- It converts the list of rules to an array called **rulesArray** and iterates through the array.
- For each rule in the array, it calls the **getRightIfContains** method on the rule with the input **leftSide** and stores the result in a variable called **rightSide**.
- If **rightSide** is not null, the function stores the string representation of the rule in a variable called **ruleAsStringToAdd** and removes the rule from the list of rules.
- It then splits **ruleAsStringToAdd** into its left and right sides, stored in variables called **leftSideToAdd** and **rightSideToAdd** respectively.
- It initializes an array list called **allPossibilities** and an array list called **currentString** to store the characters of **rightSideToAdd**.
- It calls the **addAllPossibilities** function with **allPossibilities**, **currentString**, an index of 0, and **leftSide.getAsChar()** as input. This generates a list of all possible combinations of **rightSideToAdd**, with the option to delete the character stored in **leftSide** at each step.
- It then iterates through **allPossibilities** and adds each combination as a new rule to the list of rules.
1. ## Eliminate Unit Production
`	`The deleteUnits function is the method that removes unit rules from a grammar and eliminates duplicate rules. Below is the pseudo code and an explanation of what the function does:

![](Aspose.Words.9550ed75-a6a3-48ae-89fb-21f14b59403f.008.png)

- It converts the list of rules to an array called rulesLocal and iterates through the array.
- For each rule in the array, it checks if the rule is a unit rule (a rule with a single nonterminal symbol on the right side). If it is, the function removes the rule from the list of rules and stores the right side of the rule in a variable called rightJustRemoved.
- It then calls the getRightOfThis function with rightJustRemoved as input and stores the result in a variable called newRightRules.
- It iterates through newRightRules and adds each element as a new rule to the list of rules, using the left side of the original rule and the element from newRightRules as the left and right sides of the new rule respectively.
- It then iterates through the list of rules and removes any remaining unit rules.
- Finally, it iterates through the list of rules and removes any duplicates by creating a new list called newList and adding only unique rules to it. It then assigns newList to the list of rules.

1. ## Eliminate terminals
`	`The **eliminateTerminals** function is a method that eliminates terminal symbols from the right sides of rules in a grammar. Below is the pseudo code and explanation.

![](Aspose.Words.9550ed75-a6a3-48ae-89fb-21f14b59403f.009.png)

`	`It does this by iterating through the list of rules and searching for a terminal symbol on the right side of each rule. If it finds a terminal symbol, it creates a new nonterminal symbol, adds a new rule with the new symbol and the terminal symbol as the left and right sides respectively, and replaces the terminal symbol with the new symbol in the right side of the original rule. It then adds the modified original rule back to the list of rules and starts the loop over again.

Here is a detailed explanation of how the function works:

1. It initializes a variable called **rulePointer** to 0 and enters a **while** loop that continues as long as **rulePointer** is less than the length of the list of rules.
1. It assigns the value of the **rulePointer**-th element of the list of rules to a variable called **ruleLocal**.
1. It calls the **getRightSide** method on **ruleLocal** and assigns the result to a variable called **rightSide**.
1. If the length of **rightSide** is greater than 1, it enters a **for** loop that iterates through **rightSide**.
1. Inside the loop, it checks if the **i**-th element of **rightSide** has a terminal symbol. If it does, it stores the terminal symbol in a variable called **deletedTerminal** and calls the **produceUniqueVariable** function to generate a new variable letter. It then assigns the value of the **getRightAsString** method called on **ruleLocal** to a variable called **rightSideAsString** and replaces all occurrences of **deletedTerminal** in **rightSideAsString** with the new variable letter using the **replaceAll** method. It also stores the left side of **ruleLocal** in a variable called **leftSide**.
1. It then removes **ruleLocal** from the list of rules, adds the new rule with the new variable letter and **deletedTerminal** as the left and right sides respectively, and adds the modified original rule with the new variable letter in the right side to the list of rules. It sets **rulePointer** to -1 and breaks out of the **for** loop.
1. After the **for** loop, it increments **rulePointer** by 1.
1. The **while** loop continues until **rulePointer** is no longer less than the length of the list of rules.

`	`Overall, the **eliminateTerminals** function is designed to eliminate terminal symbols from the right sides of rules in a grammar by creating new nonterminal symbols in their place and modifying the original rules to use the new symbols. It does this by iterating through the list of rules, searching for terminal
1. ## Break Variable Strings longer than 2
`	`The convertProperForm function is a method that breaks the rules in a grammar to a proper form, where the right side of each rule has at most two elements. Below is the pseudo code and explanation.

![](Aspose.Words.9550ed75-a6a3-48ae-89fb-21f14b59403f.010.png)

`	`It achieves this by iterating through the list of rules and checking if the right side of each rule has more than two elements. If it does, it creates two new rules: one with a new variable letter and the remaining elements of the right side as the left and right sides respectively, and one with the original left side and the first element of the right side concatenated with the new variable letter as the left and right sides respectively. It then removes the original rule from the list and starts the loop over again.

Here is a detailed explanation of how the function works:

1. It initializes a variable called **index** to 0 and enters a **while** loop that continues as long as **index** is less than the length of the list of rules.
1. It assigns the value of the **index**-th element of the list of rules to a variable called **ruleLocal**.
1. It calls the **getRightSide** method on **ruleLocal** and checks if the length of the result is greater than 2. If it is, it assigns the value of the **getLeftSide** method called on **ruleLocal** to a variable called **leftSide** and the value of the **getRightAsString** method called on **ruleLocal** to a variable called **rightSide**. It then converts **rightSide** to an array of characters and stores the result in a variable called **rightSideAsChar**. It also initializes a variable called **remainingRight** to an empty string.
1. It enters a for loop that iterates through the elements of rightSideAsChar starting from the second element. For each element, it concatenates the element to the end of remainingRight.
1. After the for loop completes, it calls the produceUniqueVariable method and assigns the result to a variable called newVariable.
1. It calls the addRule method on this with newVariable and remainingRight as arguments to add a new rule to the list of rules with newVariable as the left side and remainingRight as the right side.
1. It calls the addRule method on this with leftSide and the concatenation of the first element of rightSideAsChar and newVariable as arguments to add a new rule to the list of rules with leftSide as the left side and the concatenation of the first element of rightSideAsChar and newVariable as the right side.
1. It calls the remove method on this.rules with ruleLocal as an argument to remove ruleLocal from the list of rules.
1. It sets index to 0 to start the loop over again.
1. If the if statement in step 3 is not satisfied, it increments index by 1.
1. The while loop ends when index is no longer less than the length of the list of rules.
















1. # ONE SAMPLE SCREENSHOT OF THE PROGRAM
`	`Below is an example screenshot of the overall program.

![Text

Description automatically generated with medium confidence](Aspose.Words.9550ed75-a6a3-48ae-89fb-21f14b59403f.011.png)

**Figure 1:** *Example Screenshot of the program*

1. # REFERENCES
**[1]** Sipser, M. (2013), *Introduction to the Theory of Computation*, *pg-109*, Course Technology, Boston, MA. 
