package security;

import java.util.Map;

public abstract class SecurityNodes{
    String securityName;
    int skillCheck;
    Boolean check(Map<String, Integer> map){
        return false;
    }
}
