package newApproach1.Bank;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Bank {

    Map<String, Map<String, Integer>> userPossessions = new HashMap<>(); //user => token, amount

    public void addAssets(String user, String token, int amount){
        init(user, token);
        Map<String, Integer> tk = userPossessions.get(user);
        int val = tk.get(token);
        val = val+amount;
        tk.put(token,val);
        userPossessions.put(user, tk);
    }
    public void subtractAssets(String user, String token, int amount){
        init(user, token);

        Map<String, Integer> tk = userPossessions.get(user);
        int val = tk.get(token);
        if(val>=amount) {
            val = val - amount;
            tk.put(token, val);
            userPossessions.put(user, tk);
        }
    }

    public int getAssets(String user, String token){
        if(userPossessions.containsKey(user) && userPossessions.get(user).containsKey(token))
            return userPossessions.get(user).get(token);
        return 0;
    }

    private void init(String user, String token) {
        if(!userPossessions.containsKey(user)){
            Map<String, Integer> usrMap = new HashMap<>();
            usrMap.put(token,0);
            userPossessions.put(user, usrMap);
        }
        if(!userPossessions.get(user).containsKey(token)){
            Map<String, Integer> usrMap = userPossessions.get(user);
            usrMap.put(token,0);
            userPossessions.put(user, usrMap);
        }
    }



}
