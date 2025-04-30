package security;

import java.util.Map;

public class Lasers extends SecurityNodes{
    Lasers(){
        this.securityName = "Lasers";
        this.skillCheck = 5;
    }
    @Override
    Boolean check(Map<String, Integer> map){
        return map.get("Speed") >= skillCheck || map.get("Stealth") >= skillCheck;
    }
}
