package Nodes;

import Custom.CustomHashMap;

public class Firewall extends SecurityNodes {
    public Firewall() {
        this.securityName = "Firewall";
        this.skillCheck = 3;
    }

    @Override
    public Boolean check(CustomHashMap<String, Integer> map) {
        // Using get method from CustomHashMap, no default value needed as we're sure the keys exist
        return map.get("Power") >= skillCheck || map.get("Stealth") >= skillCheck;
    }
}
