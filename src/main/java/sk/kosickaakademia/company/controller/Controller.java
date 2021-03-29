package sk.kosickaakademia.company.controller;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kosickaakademia.company.database.Database;
import sk.kosickaakademia.company.entity.User;
import sk.kosickaakademia.company.enumerator.Gender;
import sk.kosickaakademia.company.log.Log;
import sk.kosickaakademia.company.util.Util;

import java.util.List;

@RestController
public class Controller {
    Log log = new Log();
    @PostMapping("/user/new")
    public ResponseEntity<String> insertNewUser(@RequestBody String data){
        try {
            JSONObject object = (JSONObject) new JSONParser().parse(data);
            String fname = ((String)object.get("fname"));
            String lname = ((String)object.get("lname"));
            String gender = (String)object.get("gender");
            int age = Integer.parseInt(String.valueOf(object.get("age")));
            System.out.println(age);
            if(fname==null ||lname==null || lname.trim().length()==0 || fname.trim().length()==0 || age<1){
                log.error("Last name or First name is missing");
                JSONObject obj = new JSONObject();
                obj.put("error","Last name or First name is missing");
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).
                        body(obj.toJSONString());
            }
            Gender g;
            if(gender==null){
                g=Gender.OTHER;
            }else if(gender.equalsIgnoreCase("male")){
                g=Gender.MALE;
            }else if(gender.equalsIgnoreCase("female")){
                g=Gender.FEMALE;
            }else
                g=Gender.OTHER;

            User user = new User(fname,lname,age,g.getValue());
            new Database().insertNewUser(user);

        } catch(Exception e){
            log.error("Can't process input data");
            JSONObject obj = new JSONObject();
            obj.put("error","Can't process input data");
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).
                    body(obj.toJSONString());
        }
        return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).
                body(null);
    }
    @GetMapping("/users")
    public ResponseEntity<String> getAllUsers(){
        List<User> list = new Database().getAllUsers();
        String json = new Util().getJson(list);
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(json);
    }
    @GetMapping("/users/age")
    public ResponseEntity<String> getUsersByAge(@RequestParam(value="from") int from, @RequestParam(value="to") int to){
        if(from>to || from<1){
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("");
        }
        List<User> list = new Database().getUsersByAge(from, to);
        String json = new Util().getJson(list);
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(json);
    }
    @PutMapping ("/user/{id}")
    public ResponseEntity<String> changeAge(@PathVariable Integer id, @RequestBody String body){
        JSONObject object = null;
        try {
            object = (JSONObject) new JSONParser().parse(body);
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("{}");
        }
        String data = String.valueOf(object.get("newage"));
        System.out.println("data:"+data);
        if(data.equalsIgnoreCase("null")){
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("{}");
        }
        int newage = Integer.parseInt(data);
        if(newage<1)
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("{}");

        boolean result = new Database().changeAge(id,newage);
        int status;
        if(result) status= 204; else status = 404;
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body("");
    }
    @GetMapping("/")
    public ResponseEntity<String> overview(){
        List<User> list = new Database().getAllUsers();
        String jsonOverview = new Util().getOverview(list);
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(jsonOverview.toString());
    }
}