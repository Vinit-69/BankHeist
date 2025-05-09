package Nodes;

import Custom.CustomHashMap;

public abstract class SecurityNodes {
    String securityName;
    int skillCheck;

    // Updated to use CustomHashMap
    public Boolean check(CustomHashMap<String, Integer> map) {
        return false; // Default behavior if not overridden by subclass
    }
}
