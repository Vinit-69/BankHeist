package Nodes;

import java.util.Map;

public class NormalNode extends SecurityNodes {
    public NormalNode() {
        this.securityName = "Normal Node";
        this.skillCheck = 0;
    }

    @Override
    public Boolean check(Map<String, Integer> map) {
        return true;
    }
}

