import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by e080868 on 11/29/2018.
 */
public class CsvQueryProcessor {

    private static CsvQueryProcessor instance;

    public static CsvQueryProcessor getInstance(){
        if(instance == null){
            instance = new CsvQueryProcessor();
        }
        return instance;
    }

    public String executeQuery(QueryParameter parameter){
        String jsonResult;
        List<CSVData> data = readCSVFile(parameter);
        jsonResult = convertDataToJSON(data, parameter);
        return jsonResult;
    }


    public List<CSVData> readCSVFile(QueryParameter parameter){
        BufferedReader br = null;
        int lineNumber = 0;
        String line;
        List<String> headerFields = new ArrayList<String>();
        List<String> dataFields;
        List<Field> fieldsAux;
        List<Field> fields;
        List<CSVData> dataList = new ArrayList<CSVData>();
        try {

            br = new BufferedReader(new FileReader(parameter.getFileName()));
            while ((line = br.readLine()) != null) {
                //Read first line of the file and gets the headers
                if (lineNumber == 0) {
                    headerFields = Arrays.asList(line.split(","));
                    System.out.println(headerFields);
                    //Read other lines and get the data
                } else {
                    dataFields = Arrays.asList(line.split(","));
                    fieldsAux = new ArrayList<Field>();
                    System.out.println(dataFields);
                    //Creates a list of fields with ALL fields
                    for (int i = 0; i < headerFields.size(); i++) {
                        addFieldToFieldList(fieldsAux, headerFields.get(i).trim(), dataFields.get(i).trim());
                    }
                    //Filter the fields selected
                    fields = filterFields(parameter, fieldsAux);
                    //Filter the data accordingly with the conditionals and return if it's supposed to add or not
                    boolean addItem = filterData(parameter, fieldsAux);
                    if (addItem) {
                        CSVData data = new CSVData();
                        data.setFields(fields);
                        dataList.add(data);
                    }
                    printFields(fields);
                }
                lineNumber++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataList;
    }

    private void orderDataBy(QueryParameter parameter, List<CSVData> dataList){
        if(parameter.getOrderBy() != null && !parameter.getOrderBy().equals("")){
            int indexOf = dataList.get(0).getFields().indexOf(parameter.getOrderBy());
            FieldComparator fieldComparator = new FieldComparator(indexOf);
            Collections.sort(dataList, (java.util.Comparator)fieldComparator);
        }

    }

    private void createAggregateFunctionFields(AggregateFunction aggregateFunction, Field field){
        if (aggregateFunction.getFunction().equals("avg")) {
            if(aggregateFunction.getResult() == null){
                Double value = 0.0;
                value = (Double)field.getValue();
                aggregateFunction.setResult(value);
            } else{
                aggregateFunction.setResult(((Double)aggregateFunction.getResult() + (Double)field.getValue())/2);
            }
        } else if (aggregateFunction.getFunction().equals("min")) {
            if(aggregateFunction.getResult() == null){
                Integer value = (Integer)field.getValue();
                aggregateFunction.setResult(value);
            } else{
                if((Integer)aggregateFunction.getResult() > (Integer) field.getValue()){
                    aggregateFunction.setResult(field.getValue());
                }
            }
        } else if (aggregateFunction.getFunction().equals("max")) {
            if(aggregateFunction.getResult() == null){
                Integer value = (Integer)field.getValue();
                aggregateFunction.setResult(value);
            } else{
                if((Integer)aggregateFunction.getResult() < (Integer) field.getValue()){
                    aggregateFunction.setResult(field.getValue());
                }
            }
        } else if (aggregateFunction.getFunction().equals("count")) {
            Integer value = 0;
            if(aggregateFunction.getResult() == null){
                value = 1;
            } else{
                value = (Integer)aggregateFunction.getResult()+1;
            }
            aggregateFunction.setResult(value);
        } else if (aggregateFunction.getFunction().equals("sum")) {
            if (aggregateFunction.getResult() == null) {
                if (field.getValue() instanceof Integer) {
                    Integer value = 0;
                    value = (Integer) field.getValue();
                    aggregateFunction.setResult(value);
                } else if (field.getValue() instanceof Double) {
                    Double value = 0.0;
                    value = (Double) field.getValue();
                    aggregateFunction.setResult(value);
                }

            } else {
                if (field.getValue() instanceof Integer) {
                    aggregateFunction.setResult((Integer) aggregateFunction.getResult() + (Integer) field.getValue());
                } else if (field.getValue() instanceof Double) {
                    aggregateFunction.setResult((Double) aggregateFunction.getResult() + (Double) field.getValue());
                }
            }
        }

    }

    private List<Field> filterFields(QueryParameter parameter, List<Field> fieldsAux){
        List<Field> fields = new ArrayList<Field>();
        for (Field field : fieldsAux) {
            if (!parameter.getFields().get(0).equals("*")) {
                for (String s : parameter.getFields()) {
                    if (field.getName().equals(s.trim())) {
                        fields.add(field);
                        break;
                    }
                }
                for(AggregateFunction aggregateFunction : parameter.getAggregateFunctions()){
                    if(field.getName().equals(aggregateFunction.getField().trim())){
                        createAggregateFunctionFields(aggregateFunction, field);
                    }
                }
            } else{
                fields.add(field);
            }
        }
        return fields;
    }

    private boolean filterData(QueryParameter parameter, List<Field> fields){
        boolean addItemFinal = false;
        boolean addItem = false;
        int addItemIndex=-1;
        if(parameter.getRestrictions() != null && parameter.getRestrictions().size() > 0) {
            for (Restriction restriction : parameter.getRestrictions()) {
                addItemIndex++;
                for (Field field : fields) {
                    if (restriction.getPropertyName().trim().equals(field.getName())) {
                        String propertyValue = restriction.getPropertyValue().trim();
                        if (restriction.getCondition().equals("<=")) {
                            if ((Integer) field.getValue() <= Integer.parseInt(propertyValue)) {
                                addItem = true;
                            } else {
                                addItem = false;
                            }
                            break;
                        } else if (restriction.getCondition().equals(">=")) {
                            if ((Integer) field.getValue() >= Integer.parseInt(propertyValue)) {
                                addItem = true;
                            } else {
                                addItem = false;
                            }
                            break;
                        } else if (restriction.getCondition().equals("<")) {
                            if ((Integer) field.getValue() < Integer.parseInt(propertyValue)) {
                                addItem = true;
                            } else {
                                addItem = false;
                            }
                            break;
                        } else if (restriction.getCondition().equals(">")) {
                            if ((Integer) field.getValue() > Integer.parseInt(propertyValue)) {
                                addItem = true;
                            } else {
                                addItem = false;
                            }
                            break;
                        } else if (restriction.getCondition().equals("=")) {
                            if (field.getValue() instanceof Integer) {
                                if (Integer.parseInt(propertyValue) == (Integer) field.getValue()) {
                                    addItem = true;
                                } else {
                                    addItem = false;
                                }
                            } else if (field.getValue() instanceof String) {
                                if (propertyValue.replace("'", "").equals(field.getValue())) {
                                    addItem = true;
                                } else {
                                    addItem = false;
                                }
                            }
                            break;
                        } else if (restriction.getCondition().equals("<>")) {
                            if (field.getValue() instanceof Integer) {
                                if (Integer.parseInt(propertyValue) != (Integer) field.getValue()) {
                                    addItem = true;
                                } else {
                                    addItem = false;
                                }
                            } else if (field.getValue() instanceof String) {
                                if (!propertyValue.equals(field.getValue())) {
                                    addItem = true;
                                } else {
                                    addItem = false;
                                }
                            }
                            break;
                        }
                    }
                }
                addItemFinal = verifyOperators(parameter, addItemIndex, addItemFinal, addItem);
            }
        }
        return addItemFinal;
    }

    private boolean verifyOperators(QueryParameter parameter, int addItemIndex, boolean addItemFinal, boolean addItemThisRound){
        if(addItemIndex == 0){
            addItemFinal = addItemThisRound;
        } else {
            for(String operator : parameter.getLogicalOperators()){
                if(operator.trim().equals("and")){
                    addItemFinal = addItemFinal && addItemThisRound;
                } else if(operator.trim().equals("or")) {
                    addItemFinal = addItemFinal || addItemThisRound;
                }
                addItemIndex++;
            }
        }

        return addItemFinal;
    }

    private void addFieldToFieldList(List<Field> fields, String name, String data){
        Field field = new Field();
        field.setName(name.trim());
        field.setValue(ValueCaster.cast(data));
        fields.add(field);
    }

    private String convertDataToJSON(List<CSVData> dataList, QueryParameter parameter){
        String result = "{";
        List<Field> fieldList;
        for (int i = 0; i < dataList.size(); i++) {
            result += "\n" + "\"" + (i+1) + ":{\n";
            fieldList = dataList.get(i).getFields();
            for (int j = 0; j < fieldList.size(); j++) {
                result += "\"" + fieldList.get(j).getName() + "\": " + "\"" + fieldList.get(j).getValue() + "\",\n";

            }
            result += "},";
        }
        if(parameter.getAggregateFunctions() != null && parameter.getAggregateFunctions().size() > 0){
            result += "\n{\n";
            for(AggregateFunction aggregateFunction : parameter.getAggregateFunctions()){
                result += "\""+aggregateFunction.getFunction()+"("+aggregateFunction.getField()+")\": " + aggregateFunction.getResult()+"\",\n";
            }
            result += "}\n";
        }

        result = result.substring(0, result.length()-1) + "\n";
        System.out.println(result);
        return result;
    }

    public static void printFields(List<Field> fields){
        for (Field f: fields) {
            System.out.println(f.getName() + ": " + f.getValue().getClass().getName());
        }
    }
}
