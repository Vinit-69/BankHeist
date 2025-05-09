package Nodes;

import Custom.CustomHashMap;

public class Lasers extends SecurityNodes {
    public Lasers() {
        this.securityName = "Lasers";
        this.skillCheck = 5;
    }

    @Override
    public Boolean check(CustomHashMap<String, Integer> map) {
        // Using get method from CustomHashMap, no default value needed as we're sure the keys exist
        return map.get("Speed") >= skillCheck || map.get("Stealth") >= skillCheck;
    }
}
