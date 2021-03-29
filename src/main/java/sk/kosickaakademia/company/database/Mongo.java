package sk.kosickaakademia.company.database;
import com.mongodb.*;
import sk.kosickaakademia.company.log.Log;

public class Mongo {
    Log log = new Log ();
   public static MongoClient mongoClient;
   public static DB database;
   public static DBCollection test;

    public static void main(String[] args) {
        try{
         mongoClient = new MongoClient("localhost", 27017);
         database = mongoClient.getDB("test");
         test = database.getCollection("users");
        }catch(Exception e){
           e.printStackTrace();
        }


    }

}
