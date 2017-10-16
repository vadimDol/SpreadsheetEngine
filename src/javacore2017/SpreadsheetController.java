package javacore2017;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class SpreadsheetController extends ParserString {
    private static Spreadsheet mSpreadsheet;
    private static HashMap<String, ICommand> mCommands;

    public SpreadsheetController(Spreadsheet spreadsheet) {
        mSpreadsheet = spreadsheet;
        mCommands = new HashMap<String, ICommand>();
        mCommands.put("set", new ICommand() {
            public void runCommand(String[] token) throws Exception {
                setValue(token);
            };
        });

        mCommands.put("setformula", new ICommand() {
            public void runCommand(String[] token) throws Exception{
                setFormula(token);
            };
        });

        mCommands.put("display", new ICommand() {
            public void runCommand(String[] token) throws Exception{
                mSpreadsheet.display();
            };
        });
    }

    public void enterMainLoop() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()) != null ) {
            try {
                if (line.endsWith("stop")) {
                    break;
                }
                String[] token = getLineToken(line);
                if(token.length == 0) {
                    throw new Exception("Unknown command!");
                }

                if(mCommands.containsKey(token[0])) {
                    mCommands.get(token[0]).runCommand(token);
                } else {
                    throw new Exception("Unknown command!");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void setValue(String[] token) throws Exception{
        //System.out.print("Command: " + token[0] + " ");
        if(token.length == 1) {
            throw new Exception("Нет идентификатора!");
        }
        Pair pair = getVariableInfo(token[1]);
        //System.out.print(pair.first + " " + pair.second);

        if(token.length == 2) {
            throw new Exception("Не введено значение!");
        }
        mSpreadsheet.setValue(pair, token[2]);
        System.out.println("OK");
        //System.out.println(" " + token[2]);
    }

    private void setFormula(String[] token) throws Exception{
        //System.out.println("setFormula");
        if(token.length == 1) {
            throw new Exception("Нет идентификатора!");
        }
        Pair pair = getVariableInfo(token[1]);
        //System.out.print(pair.first + " " + pair.second);

        if(token.length == 2) {
            throw new Exception("Не задано формула!");
        }
        if(!isCorrectFormula(token)) {
            throw new Exception("Некорректная формула!");
        }
        mSpreadsheet.setFormula(pair, "formula " + token[2]);
        System.out.println("OK");
    }

    private static  boolean isInteger (String s)
    {
        try
        {
            Integer.parseInt(s);//converts the string into an integer
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    private boolean isCorrectFormula(String[] token) throws  Exception{
        String formula = token[2].replaceAll("\\(|\\)|\\[|\\]", " ");

        String[] prefixStrArray = formula.split(" ");
        Stack<String> stack = new Stack<String>();
        for(int i = prefixStrArray.length-1; i > -1; i--){
            String prefixStr = prefixStrArray[i];
            if(prefixStr.equals("")){
                continue;
            }
            if(prefixStr.equals("+") || prefixStr.equals("/") || prefixStr.equals("*") || prefixStr.equals("-")) {
                if(stack.size() < 2){
                    return false;
                }
                stack.push(stack.pop() + stack.pop());
            } else {
                if(!isInteger(prefixStr)) {
                    getVariableInfo(prefixStr);
                }
                stack.push(prefixStr);
            }
        }
        if((stack.pop() != null) && (stack.size() != 0)) {
            return false;
        }

        token[2] = formula;
        return true;
    }
}
