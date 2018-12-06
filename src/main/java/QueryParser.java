import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by e080868 on 11/28/2018.
 */
public class QueryParser {
    public static QueryParameter query = new QueryParameter();


    public QueryParameter parseQuery(String queryString){
        if(queryString.startsWith("select")) {
            query.setQuery(queryString);
            query.setFileName(query.getQuery().split(" from ")[1].split(" ")[0]);
            query.setBase(query.getQuery().split(" where ")[0].trim());
            if (query.getQuery().contains(" where ")) {
                query.setFilter(query.getQuery().split(" where ")[1]);
                query.setFilterList(getFiltersList(query.getFilter()));
                query.setRestrictions(mapFilterToRestriction(query.getFilterList()));
                query.setLogicalOperators(getListAndOrList(query.getFilter()));
            }
            query.setFields(getFieldsList(query.getBase()));
            query.setAggregateFunctions(getAgregateFunctionsList(query.getFields()));
            if (query.getQuery().contains("order by")) {
                query.setOrderBy(query.getQuery().split("order by ")[1].split(" ")[0]);
            }
            if (query.getQuery().contains("group by")) {
                query.setGroupBy(query.getQuery().split("group by ")[1].split(" ")[0]);
            }

            printQuery();
        }
        return query;
    }

    public static void printQuery(){

        System.out.println("File Name: " + query.getFileName());
        System.out.println("Base Part: " + query.getBase());
        System.out.println("Filter Part: " + query.getFilter());
        System.out.println("Filters List: " + query.getFilterList());
        System.out.println("Logical Operators: " + query.getLogicalOperators());
        System.out.println("Fields: " + query.getFields());
        System.out.println("Agregate Functions: " + query.getAggregateFunctions());
        System.out.println("Order By: " + query.getOrderBy());
        System.out.println("Group By: " + query.getGroupBy());

    }
    public List<String> getFiltersList(String filter){
        if(filter.contains("order by")){
            filter = filter.split("order by")[0].trim();
        }
        if(filter.contains("group by")){
            filter = filter.split("group by")[0].trim();
        }
        List<String> andOrList = Arrays.asList(filter.split("( and )|( or )"));

        return andOrList;
    }

    public List<String> getListAndOrList(String filter){
        List<String> andOrList = new ArrayList<String>();
        for (String aux : filter.split(" ")) {
            if ("or".equals(aux)){
                andOrList.add(aux);
            }else if("and".equals(aux)){
                andOrList.add(aux);
            }
        }
        return andOrList;
    }

    public List<String> getFieldsList(String base){
        List<String> fieldsList = new ArrayList<String>();
        List<String> filteredQueryList = Arrays.asList(base.substring(7).split(","));
        for (String aux : filteredQueryList) {
            if (aux.contains("from")){
                fieldsList.add(aux.trim().split(" ")[0]);
                break;
            }
            fieldsList.add(aux);
        }
        return fieldsList;
    }

    public List<AggregateFunction> getAgregateFunctionsList(List<String> fieldList){
        List<AggregateFunction> agregateFunctionsList = new ArrayList<AggregateFunction>();
        AggregateFunction aggregateFunction = null;
        for (String aux : fieldList) {
            if (aux.contains("avg(")){
                aggregateFunction = new AggregateFunction();
                aggregateFunction.setFunction("avg");
                aggregateFunction.setField(aux.replace("avg(","").replace(")",""));
                agregateFunctionsList.add(aggregateFunction);
            } else if (aux.contains("min(")){
                aggregateFunction = new AggregateFunction();
                aggregateFunction.setFunction("min");
                aggregateFunction.setField(aux.replace("min(","").replace(")",""));
                agregateFunctionsList.add(aggregateFunction);
            } else if (aux.contains("max(")){
                aggregateFunction = new AggregateFunction();
                aggregateFunction.setFunction("max");
                aggregateFunction.setField(aux.replace("max(","").replace(")",""));
                agregateFunctionsList.add(aggregateFunction);
            } else if (aux.contains("count(")){
                aggregateFunction = new AggregateFunction();
                aggregateFunction.setFunction("count");
                aggregateFunction.setField(aux.replace("count(","").replace(")",""));
                agregateFunctionsList.add(aggregateFunction);
            } else if (aux.contains("sum(")){
                aggregateFunction = new AggregateFunction();
                aggregateFunction.setFunction("sum");
                aggregateFunction.setField(aux.replace("sum(","").replace(")",""));
                agregateFunctionsList.add(aggregateFunction);
            }
        }

        return agregateFunctionsList;
    }

    private List<Restriction> mapFilterToRestriction(List<String> filterList){
        List<Restriction> restrictionList = new ArrayList<Restriction>();
        for (String filter: filterList) {
            Restriction r = new Restriction();
            if(filter.contains("<=")){
                r.setPropertyName(filter.split("<=")[0]);
                r.setPropertyValue(filter.split("<=")[1]);
                r.setCondition("<=");
            } else if(filter.contains(">=")){
                r.setPropertyName(filter.split(">=")[0]);
                r.setPropertyValue(filter.split(">=")[1]);
                r.setCondition(">=");
            } else if(filter.contains("<")){
                r.setPropertyName(filter.split("<")[0]);
                r.setPropertyValue(filter.split("<")[1]);
                r.setCondition("<");
            } else if(filter.contains(">")){
                r.setPropertyName(filter.split(">")[0]);
                r.setPropertyValue(filter.split(">")[1]);
                r.setCondition(">");
            } else if(filter.contains("=")){
                r.setPropertyName(filter.split("=")[0]);
                r.setPropertyValue(filter.split("=")[1]);
                r.setCondition("=");
            } else if(filter.contains("<>")){
                r.setPropertyName(filter.split("<>")[0]);
                r.setPropertyValue(filter.split("<>")[1]);
                r.setCondition("<>");
            }
            restrictionList.add(r);
        }
        return restrictionList;
    }
}
