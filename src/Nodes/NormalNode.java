package Nodes;

import Custom.CustomHashMap;

public class NormalNode extends SecurityNodes {
    public NormalNode() {
        this.securityName = "Normal Node";
        this.skillCheck = 0;
    }

    @Override
    public Boolean check(CustomHashMap<String, Integer> map) {
        return true;
    }
}
