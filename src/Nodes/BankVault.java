package Nodes;

import Custom.CustomHashMap;

public class BankVault extends SecurityNodes {
    public BankVault() {
        this.securityName = "Bank Vault";
        this.skillCheck = 8;
    }

    @Override
    public Boolean check(CustomHashMap<String, Integer> map) {

        return map.get("Stealth") >= skillCheck || map.get("Power") >= skillCheck;
    }
}
