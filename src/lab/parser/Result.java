package lab.parser;

import java.util.List;

public class Result {
	private List<String> stringValue;
    private boolean booleanValue;

    public Result(List<String> stringValue, boolean booleanValue) {
        this.stringValue = stringValue;
        this.booleanValue = booleanValue;
    }

    public List<String> getStringValue() {
        return stringValue;
    }

    public boolean getBooleanValue() {
        return booleanValue;
    }
}
