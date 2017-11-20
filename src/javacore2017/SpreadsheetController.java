package javacore2017;

import java.io.BufferedReader;
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
            }

            ;
        });

        mCommands.put("setformula", new ICommand() {
            public void runCommand(String[] token) throws Exception {
                setFormula(token);
            }

            ;
        });

        mCommands.put("display", new ICommand() {
            public void runCommand(String[] token) throws Exception {
                mSpreadsheet.display();
            }

            ;
        });
    }

    public void enterMainLoop() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()) != null) {
            try {
                if (line.endsWith("stop")) {
                    break;
                }
                String[] token = getLineToken(line);
                if (token.length == 0) {
                    throw new Exception("Unknown command!");
                }

                if (mCommands.containsKey(token[0])) {
                    mCommands.get(token[0]).runCommand(token);
                } else {
                    throw new Exception("Unknown command!");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void setValue(String[] token) throws Exception {
        //System.out.print("Command: " + token[0] + " ");
        if (token.length == 1) {
            throw new Exception("No identifier!");
        }
        Pair pair = getVariableInfo(token[1]);
        //System.out.print(pair.first + " " + pair.second);

        if (token.length == 2) {
            throw new Exception("Value not entered!");
        }
        mSpreadsheet.setValue(pair, token[2]);
        System.out.println("OK");
        //System.out.println(" " + token[2]);
    }

    private void setFormula(String[] token) throws Exception {
        //System.out.println("setFormula");
        if (token.length == 1) {
            throw new Exception("No identifier!");
        }
        Pair pair = getVariableInfo(token[1]);
        //System.out.print(pair.first + " " + pair.second);

        if (token.length == 2) {
            throw new Exception("Не задано формула!");
        }
        List<String> items = Arrays.asList(token);
        items = items.subList(1, 3);
        if (!isCorrectFormula(items)) {
            throw new Exception("Invalid formula!");
        }
        mSpreadsheet.setFormula(pair, "formula " + items.get(1));
        System.out.println("OK");
    }

    private static boolean isDouble(String s) {
        try {
            Double.parseDouble(s); //converts the string into an integer
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isCorrectFormula(List<String> token) throws Exception {
        String formula = token.get(1).replaceAll("\\(|\\)|\\[|\\]", " ");
        String[] prefixStrArray = formula.split(" ");
        Stack<String> stack = new Stack<String>();
        for (int i = prefixStrArray.length - 1; i > -1; i--) {
            String prefixStr = prefixStrArray[i];
            if (prefixStr.equals("")) {
                continue;
            }
            if (prefixStr.equals("+") || prefixStr.equals("/") || prefixStr.equals("*") || prefixStr.equals("-")) {
                if (stack.size() < 2) {
                    return false;
                }
                stack.push(stack.pop() + stack.pop());
            } else {
                if (token.get(0).equals(prefixStr)) {
                    return false;
                }
                if (!isDouble(prefixStr)) {
                    Pair cell = getVariableInfo(prefixStr);
                    if (mSpreadsheet.isFormula(cell)) {
                        List<String> items = new ArrayList<String>();
                        items.add(token.get(0));
                        items.add(mSpreadsheet.getFormula(cell));
                        return isCorrectFormula(items);
                    }
                }
                stack.push(prefixStr);
            }
        }
        if ((stack.pop() != null) && (stack.size() != 0)) {
            return false;
        }

        token.set(1, formula);
        return true;
    }
}
