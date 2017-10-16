package javacore2017;

import java.text.DecimalFormat;
import java.util.*;


public class Spreadsheet {
    private static final Integer COUNT_SPACES = 15;
    private static TreeMap<Character, TreeMap<Integer, String>> mTable = new TreeMap<>();

    public Spreadsheet() {
    }

    public void setValue(Pair pair, String newValue) {
        if(mTable.containsKey(pair.first)) {
            TreeMap<Integer, String> row = mTable.get(pair.first);
            row.put(pair.second, newValue);
        } else {
            TreeMap<Integer, String> row = new TreeMap<Integer, String>();
            row.put(pair.second, newValue);
            mTable.put(pair.first, row);
        }
    }

    public void setFormula(Pair pair, String newValue) {
        if(mTable.containsKey(pair.first)) {
            TreeMap<Integer, String> row = mTable.get(pair.first);
            row.put(pair.second, newValue);
        } else {
            TreeMap<Integer, String> row = new TreeMap<Integer, String>();
            row.put(pair.second, newValue);
            mTable.put(pair.first, row);
        }
    }

    public void display() {
        System.out.println(mTable);
        System.out.print("  ");
        for (Map.Entry entry : mTable.entrySet()) {
            System.out.printf("%15s", entry.getKey());
        }
        System.out.println("");
        Set<Integer> setIndexColumn = new HashSet<Integer>();
        for (Map.Entry<Character, TreeMap<Integer, String>> entry : mTable.entrySet()) {
            TreeMap<Integer, String> row = entry.getValue();
            for(Map.Entry element : row.entrySet()) {
                if(!setIndexColumn.contains(element.getKey())) {
                    System.out.print(element.getKey() + " ");
                    printValue((Integer)element.getKey());
                    setIndexColumn.add((Integer)element.getKey());
                    System.out.println();

                }
            }
        }
    }

    private void printValue(Integer rowInteger) {
        int counter = 1;
        for (Map.Entry<Character, TreeMap<Integer, String>> entry : mTable.entrySet()) {
            TreeMap<Integer, String> row = entry.getValue();
            for(Map.Entry element : row.entrySet()) {
                if((Integer)element.getKey() == rowInteger) {
                    String value = (String)element.getValue();
                    String format = "%" + (COUNT_SPACES  * counter) + "s";
                    counter = 0;
                    if(value.split(" ")[0].equals("formula")) {
                        String calcResult = new DecimalFormat("#0.00")
                                .format(calculation(value.substring(value.split(" ")[0].length(), value.length())));
                        System.out.printf(format, calcResult);
                        continue;
                    }
                    System.out.printf(format, value);
                }
            }
            ++counter;
        }
    }


    private Double getValueVariable(String variable){
        Double value;
        if(mTable.containsKey(variable.charAt(0))) {
            TreeMap<Integer, String> col = mTable.get(variable.charAt(0));
            if(col.containsKey(Integer.parseInt("" + variable.charAt(1)))) {
                String valueStr = col.get(Integer.parseInt("" + variable.charAt(1)));
                if(valueStr.split(" ")[0].equals("formula")) {
                    return calculation(valueStr.substring(valueStr.split(" ")[0].length(), valueStr.length()));
                }
                value = (!isDouble(valueStr)) ? Double.NaN : Double.parseDouble(valueStr);
            }else {
                value = Double.NaN;
            }
        } else {
            value = Double.NaN;
        }
        return value;
    }

    private double calculation(String formula) {
        String[] prefixStrArray = formula.split(" "); //* + A1 5  A1
        Stack<Double> stack = new Stack<Double>();

        Map<String, ICalculation> mapOperations = new HashMap<String, ICalculation>();
        mapOperations.put("+", new ICalculation() {
            public double getResult(double operand1, double operand2) {
                return operand1 + operand2;
            }}
        );
        mapOperations.put("-", new ICalculation() {
            public double getResult(double operand1, double operand2) {
                return operand1 - operand2;
            }}
        );
        mapOperations.put("*", new ICalculation() {
            public double getResult(double operand1, double operand2) {
                return operand1 * operand2;
            }}
        );
        mapOperations.put("/", new ICalculation() {
            public double getResult(double operand1, double operand2) {
                return operand1 / operand2;
            }}
        );
        for(int i = prefixStrArray.length-1; i >- 1; i--) {
            String prefixStr = prefixStrArray[i];
            if (prefixStr.equals("")) {
                continue;
            }
            if (mapOperations.containsKey(prefixStr)) {
                stack.push(mapOperations.get(prefixStr).getResult(stack.pop(), stack.pop()));
            } else {
                //System.out.println( "{" + getValueVariable(prefixStr) + "}");
                if (isDouble(prefixStr)) {
                    stack.push(Double.parseDouble(prefixStr));
                    continue;
                }
                stack.push(getValueVariable(prefixStr));
            }
        }
        return stack.pop();
    }

    public static boolean isDouble (String s)
    {
        try
        {
            Double.parseDouble(s);//converts the string into an integer
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
}
