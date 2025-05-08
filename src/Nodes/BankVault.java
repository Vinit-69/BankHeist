package Nodes;

import java.util.Map;

public class BankVault extends SecurityNodes {
    public BankVault() {
        this.securityName = "Bank Vault";
        this.skillCheck = 8;
    }

    @Override
    public Boolean check(Map<String, Integer> map) {
        return map.getOrDefault("Stealth", 0) >= skillCheck || map.getOrDefault("Power", 0) >= skillCheck;
    }
}

