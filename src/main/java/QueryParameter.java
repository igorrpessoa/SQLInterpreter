import java.util.List;

/**
 * Created by e080868 on 11/28/2018.
 */
public class QueryParameter {

    private String QUERY_TYPE;
    private String query;
    private String base;
    private String fileName;
    private String filter;
    private String orderBy;
    private String groupBy;
    private List<String> filterList;
    private List<String> logicalOperators;
    private List<String> fields;
    private List<AggregateFunction> aggregateFunctions;
    private List<Restriction> restrictions;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List<String> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<String> filterList) {
        this.filterList = filterList;
    }

    public List<String> getLogicalOperators() {
        return logicalOperators;
    }

    public void setLogicalOperators(List<String> logicalOperators) {
        this.logicalOperators = logicalOperators;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getQUERY_TYPE() {
        return QUERY_TYPE;
    }

    public void setQUERY_TYPE(String QUERY_TYPE) {
        this.QUERY_TYPE = QUERY_TYPE;
    }

    public List<AggregateFunction> getAggregateFunctions() {
        return aggregateFunctions;
    }

    public void setAggregateFunctions(List<AggregateFunction> aggregateFunctions) {
        this.aggregateFunctions = aggregateFunctions;
    }

    public List<Restriction> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(List<Restriction> restrictions) {
        this.restrictions = restrictions;
    }
}
