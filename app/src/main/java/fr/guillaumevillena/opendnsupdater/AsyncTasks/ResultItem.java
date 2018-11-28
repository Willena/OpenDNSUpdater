package fr.guillaumevillena.opendnsupdater.AsyncTasks;

import java.util.HashMap;

/**
 * Created by guill on 28/06/2018.
 */

public class ResultItem extends HashMap<String, Object> {

    public ResultItem() {
        super();

        this.putSucessState(false);
    }

    public ResultItem(Boolean successState) {
        super();
        putSucessState(successState);
    }

    private final String[] excludedKeys = {
            "successState",
            "resultValue"
    };

    @Override
    public Object put(String key, Object value) {

        for (String s : this.excludedKeys) {
            if (s.equals(key))
                return null;
        }

        return super.put(key, value);
    }

    public Object putResultValue(Object value) {
        return super.put(excludedKeys[1], value);
    }

    public Object putSucessState(Boolean state) {
        return super.put(excludedKeys[0], state);
    }

    public Object getResultValue() {
        return this.get(excludedKeys[1]);
    }

    public Boolean getState() {
        return (Boolean) this.get(excludedKeys[0]);
    }


}
