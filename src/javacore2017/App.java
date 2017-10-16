package javacore2017;

public class App {
    public static void main(String[] args) {
        try {
            Spreadsheet spreadsheet = new Spreadsheet();
            SpreadsheetController controller = new SpreadsheetController(spreadsheet);
            controller.enterMainLoop();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
