package Nodes;

import Custom.CustomHashMap;

public class Firewall extends SecurityNodes {
    public Firewall() {
        this.securityName = "Firewall";
        this.skillCheck = 3;
    }

    @Override
    public Boolean check(CustomHashMap<String, Integer> map) {

        return map.get("Power") >= skillCheck || map.get("Stealth") >= skillCheck;
    }
}
