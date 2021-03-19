package sk.kosickaakademia.company.controller;

import org.json.simple.parser.JSONParser;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kosickaakademia.company.log.Log;
import sk.kosickaakademia.company.util.Util;
import java.util.Map;

@RestController
public class SecretController {
    private final String PASSWORD = "Kosice2021!";
    Map<String, String>
    Log log = new Log();
    @GetMapping("/secret")
    public String secret(@RequestHeader("token") String header){
        System.out.println(header);
        String token = header.substring(7);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if(entry.getValue().equalsIgnoreCase(token)){
                return "secret";
            }
        }
        return "401";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody String auth) {
        JSONPObject object = null;
        try {
            object = (JSONPObject) new JSONParser().parse(auth);
            String login = ((String) object.get("login"));
            String password = ((String) object.get("password"));
            System.out.println(login + " " + password);
            if (login.equals("null") || password.equals("null")){
                log.error("Missing login or password");
                return ResponseEntity.status(400).body("");
            }
            if (password.equals(PASSWORD)) {
                String token = new Util().generateToken();
                map.put(login, token);
                JSONPObject obj = new JSONPObject();
                obj.put("login", login);
                obj.put("token", login);
            log.print("User logged");
            return ResponseEntity.status(200).body("");
            }else {
                log.print("Wrong password");
                return ResponseEntity.status(401).body("");
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
         return null;
    }
}

