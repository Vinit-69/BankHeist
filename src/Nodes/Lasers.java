package Nodes;

import Custom.CustomHashMap;

public class Lasers extends SecurityNodes {
    public Lasers() {
        this.securityName = "Lasers";
        this.skillCheck = 5;
    }

    @Override
    public Boolean check(CustomHashMap<String, Integer> map) {

        return map.get("Speed") >= skillCheck || map.get("Stealth") >= skillCheck;
    }
}
