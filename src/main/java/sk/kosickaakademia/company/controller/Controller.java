package sk.kosickaakademia.company.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.kosickaakademia.company.database.Database;
import sk.kosickaakademia.company.entity.User;
import sk.kosickaakademia.company.enumerator.Gender;
import sk.kosickaakademia.company.util.Util;

import java.util.List;

@RestController
public class Controller {
@PostMapping("/user/new")
    public ResponseEntity<String> insertNewUser(){
    try{
        JSONPObject object = (JSONPObject) new JSONParser().parse(data);

        String fname = (String)object.get("fname").trim();
        String lname = (String)object.get("lname").trim();
        String gender = (String)object.get("gender");
        System.out.println(age);
        if(fname==null ||lname==null || lname.trim().length()==0 || fname.trim().length()==0 || age<1){
            log.error("Missing lname or fname.");
            JSONObject obj = new JSONObject();
            obj.put("error","missing lname or fname.");
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
            g= Gender.OTHER;
        User user = new User(fname,lname,age,g.getValue());
        new Database().insertNewUser(user);

    }catch (Exception e){
        log.error("Cannot process input data.");
        JSONObject obj = new JSONObject();
        obj.put("error","cannot process input data");
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
}
