/**
 * Created by e080868 on 11/28/2018.
 */
public class AggregateFunction {

    private String field;
    private Object result;
    private String function;
    private int aggregateFuntcionIndex;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public int getAggregateFuntcionIndex() {
        return aggregateFuntcionIndex;
    }

    public void setAggregateFuntcionIndex(int aggregateFuntcionIndex) {
        this.aggregateFuntcionIndex = aggregateFuntcionIndex;
    }
}
