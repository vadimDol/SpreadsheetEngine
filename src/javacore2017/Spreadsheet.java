package javacore2017;

import java.util.TreeMap;

public class Spreadsheet {
    private static TreeMap<Character, TreeMap<Integer, String>> mColumn = new TreeMap<Character, TreeMap<Integer, String>>();

    public Spreadsheet() {
    }

    public
    public void setValue(Pair pair, String newValue) {
        if(mColumn.containsKey(pair.first)) {
            TreeMap<Integer, String> row = mColumn.get(pair.first);
            row.put(pair.second, newValue);
        } else {
            TreeMap<Integer, String> row = new TreeMap<Integer, String>();
            row.put(pair.second, newValue);
            mColumn.put(pair.first, row);
        }
    }

    public void setFormula(Pair pair, String newValue) {
        if(mColumn.containsKey(pair.first)) {
            TreeMap<Integer, String> row = mColumn.get(pair.first);
            row.put(pair.second, newValue);
        } else {
            TreeMap<Integer, String> row = new TreeMap<Integer, String>();
            row.put(pair.second, newValue);
            mColumn.put(pair.first, row);
        }
    }

    public void display() {
        System.out.println(mColumn);
        /*System.out.print("  ");
        for (Map.Entry entry : mColumn.entrySet()) {
            System.out.print(entry.getKey() + "    ");
        }

        for (Map.Entry entry : mColumn.entrySet()) {
            for(Map.Entry element : mColumn.entrySet()) {
                System.out.print(element.getKey() + " ");
                for() {

                }
            }
        }*/
    }

}
