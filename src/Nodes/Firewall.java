package Nodes;

import java.util.Map;

public class Firewall extends SecurityNodes{
    public Firewall(){
        this.securityName = "Firewall";
        this.skillCheck = 3;
    }
    @Override
    public Boolean check(Map<String, Integer> map){
        return map.get("Power") >= skillCheck || map.get("Stealth") >= skillCheck;
    }
}
