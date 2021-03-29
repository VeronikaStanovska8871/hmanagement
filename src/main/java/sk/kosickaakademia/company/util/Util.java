package sk.kosickaakademia.company.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sk.kosickaakademia.company.entity.User;
import sk.kosickaakademia.company.enumerator.Gender;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Util {
    public String getJson(List<User> list){
        if(list.isEmpty()) return "{}";
        JSONObject object = new JSONObject();
        object.put("datetime",getCurrentDateTime());
        object.put("size",list.size());
        JSONArray jsonArray=new JSONArray();
        for(User u : list ) {
            JSONObject userJson = new JSONObject();
            userJson.put("id", u.getId());
            userJson.put("fname", u.getFname());
            userJson.put("lname", u.getLname());
            userJson.put("age", u.getAge());
            userJson.put("gender", u.getGender().toString());
            jsonArray.add(userJson);
        }
        object.put("users",jsonArray);

        return object.toJSONString();
    }
    public String getJson(User user){
        if(user==null) return "{}";
        JSONObject object = new JSONObject();
        object.put("datetime",getCurrentDateTime());
        object.put("size",1);
        JSONArray jsonArray=new JSONArray();
        JSONObject userJson = new JSONObject();
        userJson.put("id",user.getId()) ;
        userJson.put("fname",user.getFname()) ;
        userJson.put("lname",user.getLname()) ;
        userJson.put("age",user.getAge()) ;
        userJson.put("gender",user.getGender().toString()) ;
        jsonArray.add(userJson);
        object.put("users",jsonArray);

        return object.toJSONString();
    }
    public String getCurrentDateTime(){
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        return date;
    }
    public String normalizeName(String name){
        if(name==null || name.equals(""))
            return "";
        name=name.trim();
        return Character.toUpperCase(name.charAt(0))+name.substring(1).toLowerCase();
    }
    public String getOverview(List<User> list) {
        int count = list.size();
        int male = 0;
        int female = 0;
        int sumage = 0;
        int min = count>0? list.get(0).getAge():0;
        int max = count>0? list.get(0).getAge():0;
        for(User u : list){
            if(u.getGender()== Gender.MALE) male++;
            else if(u.getGender()== Gender.FEMALE) female++;
            sumage+=u.getAge();
            if(min>u.getAge())
                min=u.getAge();
            if(max<u.getAge())
                max=u.getAge();
        }
        double avg=(double)sumage/count;
        JSONObject obj = new JSONObject();
        obj.put("count",count);
        obj.put("min",min);
        obj.put("max",max);
        obj.put("countMale",male);
        obj.put("countFemale",female);
        obj.put("averageAge",avg);
        return obj.toJSONString();
    }
    public String generateToken(){
        String token= "";
        Random rnd = new Random();
        for (int i=0; i<40; i++){
           int x= rnd.nextInt(3);
           switch (x){
               case 0: token = token+(char)(rnd.nextInt(26)+65); break;
               case 1: token = token+(char)(rnd.nextInt(26)+97); break;
               case 2: token = token+(char)(rnd.nextInt(26)+48); break;
           }
        }return token;
    }
}
