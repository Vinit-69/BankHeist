package Nodes;

import java.util.Map;

public abstract class SecurityNodes{
    String securityName;
    int skillCheck;
    public Boolean check(Map<String, Integer> map){
        return false;
    }
}
