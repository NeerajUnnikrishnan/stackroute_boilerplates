package com.stackroute.datamunger.query;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.List;

import com.stackroute.datamunger.query.parser.Restriction;

import java.util.ArrayList;

//this class contains methods to evaluate expressions
public class Filter {

    /*
     * the evaluateExpression() method of this class is responsible for
     * evaluating the expressions mentioned in the query. It has to be noted
     * that the process of evaluating expressions will be different for
     * different data types. there are 6 operators that can exist within a query
     * i.e. >=,<=,<,>,!=,= This method should be able to evaluate all of them.
     * Note: while evaluating string expressions, please handle uppercase and
     * lowercase
     *
     */

    public Boolean evaluateExpression(Header header, RowDataTypeDefinitions dataType, String[] data,
                                      List<Restriction> r, List<String> logicalOperators) {
        List<Boolean> list = new ArrayList<>();
        for (int i = 0; i < r.size(); i++) {
            if (r.get(i).getCondition().equals(">")) {
                int conditionValue = Integer.parseInt(r.get(i).getPropertyValue().trim());
                int rowDataValue = Integer.parseInt(data[header.getHeader().get(r.get(i).getPropertyName().trim())]);
                list.add(conditionIsGreaterthan(rowDataValue, conditionValue));
            }
            if (r.get(i).getCondition().equals("<")) {
                int conditionValue = Integer.parseInt(r.get(i).getPropertyValue().trim());
                int rowDataValue = Integer.parseInt(data[header.getHeader().get(r.get(i).getPropertyName().trim())]);
                list.add(conditionIsLessthan(rowDataValue, conditionValue));
            }
            if (r.get(i).getCondition().equals("<=")) {
                int conditionValue = Integer.parseInt(r.get(i).getPropertyValue().trim());
                int rowDataValue = Integer.parseInt(data[header.getHeader().get(r.get(i).getPropertyName().trim())]);
                list.add(conditionIsLessthanOrEqual(rowDataValue, conditionValue));
            }
            if (r.get(i).getCondition().equals(">=")) {
                int conditionValue = Integer.parseInt(r.get(i).getPropertyValue().trim());
                int rowDataValue = Integer.parseInt(data[header.getHeader().get(r.get(i).getPropertyName().trim())]);
                list.add(conditionIsGreaterthanOrEqual(rowDataValue, conditionValue));
            }
            if (r.get(i).getCondition().equals("!=")) {
                if (dataType.get(header.get(r.get(i).getPropertyName().trim())).equals("java.lang.String")) {
                    String conditionValue = r.get(i).getPropertyValue().trim();
                    String rowDataValue = data[header.getHeader().get(r.get(i).getPropertyName().trim())];
                    list.add(conditionIsNotEqualTo(rowDataValue, conditionValue));
                }
            }
            if (r.get(i).getCondition().equals("=")) {
                if (dataType.get(header.get(r.get(i).getPropertyName().trim())).equals("java.lang.String")) {
                    String conditionValue = r.get(i).getPropertyValue().trim();
                    String rowDataValue = data[header.getHeader().get(r.get(i).getPropertyName().trim())];
                    list.add(conditionIsNotEqualTo(rowDataValue, conditionValue));
                }
            }
        }
        String[] final_combine ;
//		System.out.println(r);
        if(logicalOperators!=null){
            final_combine = new String[(logicalOperators.size()
                    + list.size())];
        }
        else
        {
            final_combine = new String[list.size()];
        }
        int log_cnt = 0, value_cnt = 0;
        for (int final_check = 0; final_check < final_combine.length; final_check++) {
            if (final_check % 2 == 1) {
                final_combine[final_check] = logicalOperators.get(log_cnt);
                log_cnt++;
            } else {
                final_combine[final_check] = Boolean.toString((list.get(value_cnt)));
                value_cnt++;
            }
        }

        StringBuffer conditionalstring=new StringBuffer();
        for (int final_checker = 0; final_checker < final_combine.length;final_checker++) {
            conditionalstring.append(final_combine[final_checker]);
            if(!(final_checker+1==final_combine.length))
            {
                conditionalstring.append(",");
            }
        }

        String[] orspliter=conditionalstring.toString().split("or");
        int it_temp=0;
        Boolean indicator=false;
        for(int iterate=0;iterate<orspliter.length;iterate++)
        {
            int temp1=0;
            if(orspliter[iterate].contains("true") && orspliter[iterate].contains("false"))
            {
                temp1=0;
            }
            else if(orspliter[iterate].contains("true"))
            {
                temp1=1;
            }
            else if(orspliter[iterate].contains("false"))
            {
                temp1=0;
            }
            it_temp+=temp1;
        }
        if(it_temp>=1)
        {
            indicator=true;
        }
        else if(it_temp==0)
        {
            indicator=false;
        }
	/*	if (r.size() == 1) {
			return list.get(0);
		} else if (r.size() > 1) {
			if (logicalOperators.get(0).trim().equals("and")) {

				return list.get(0) && list.get(1);
			}
			if (logicalOperators.get(0).trim().equals("or")) {

				return list.get(0) || list.get(1);
			}
		}*/
        return indicator;
    }

    // method containing implementation of equalTo operator
    Boolean conditionIsEqualTo(String a, String b) {
        if (a.equals(b)) {
            return true;
        } else {
            return false;
        }

    }

    // method containing implementation of notEqualTo operator
    Boolean conditionIsNotEqualTo(String a, String b) {
        if (!a.equals(b)) {
            return true;
        } else {
            return false;
        }

    }

    // method containing implementation of greaterThanOrEqualTo operator
    Boolean conditionIsGreaterthanOrEqual(int a, int b) {
        if (a >= b) {
            return true;
        } else {
            return false;
        }

    }

    // method containing implementation of lessThanOrEqualTo operator
    Boolean conditionIsLessthanOrEqual(int a, int b) {
        if (a <= b) {
            return true;
        } else {
            return false;
        }

    }

    // method containing implementation of greaterThan operator
    Boolean conditionIsGreaterthan(int a, int b) {
        if (a > b) {
            return true;
        } else {
            return false;
        }

    }

    // method containing implementation of lessThan operator
    Boolean conditionIsLessthan(int a, int b) {
        if (a < b) {
            return true;
        } else {
            return false;
        }

    }
}