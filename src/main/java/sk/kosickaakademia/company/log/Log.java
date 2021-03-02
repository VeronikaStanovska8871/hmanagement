package sk.kosickaakademia.company.log;

public class Log {
    public void error(String msg){
        System.out.println("Error: "+msg);
    }
    public void print(String msg){
        System.out.println("OK: "+msg);
    }
}
