package Nodes;

import Custom.CustomHashMap;

public abstract class SecurityNodes {
    String securityName;
    int skillCheck;

    public Boolean check(CustomHashMap<String, Integer> map) {
        return false;
    }
}
