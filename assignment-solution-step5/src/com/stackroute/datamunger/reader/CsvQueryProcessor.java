package com.stackroute.datamunger.reader;

import com.stackroute.datamunger.query.*;
import com.stackroute.datamunger.query.parser.QueryParameter;
import com.stackroute.datamunger.query.parser.Restriction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class CsvQueryProcessor implements QueryProcessingEngine {
	/*
	 * This method will take QueryParameter object as a parameter which contains the
	 * parsed query and will process and populate the ResultSet
	 */
	public DataSet getResultSet(QueryParameter queryParameter){

		/*
		 * initialize BufferedReader to read from the file which is mentioned in
		 * QueryParameter. Consider Handling Exception related to file reading.
		 */
        DataSet resultSet = new DataSet();

		try {

            BufferedReader Buff = new BufferedReader(new FileReader(queryParameter.getFileName()));

            /*
             * read the first line which contains the header. Please note that the headers
             * can contain spaces in between them. For eg: city, winner
             */

            String firstLine = Buff.readLine();
            String[] headerArray = firstLine.split(",");



            /*
             * read the next line which contains the first row of data. We are reading this
             * line so that we can determine the data types of all the fields. Please note
             * that ipl.csv file contains null value in the last column. If you do not
             * consider this while splitting, this might cause exceptions later
             */
            String secondLine = Files.readAllLines(Paths.get(queryParameter.getFileName())).get(2);
            String[] dataValuesArray = secondLine.trim().split(",");

            /*
             * populate the header Map object from the header array. header map is having
             * data type <String,Integer> to contain the header and it's index.
             */
            Header headerMap = new Header();
            headerMap.setHeader(headerArray);
            System.out.println(headerMap.getHeader());

            /*
             * We have read the first line of text already and kept it in an array. Now, we
             * can populate the RowDataTypeDefinition Map object. RowDataTypeDefinition map
             * is having data type <Integer,String> to contain the index of the field and
             * it's data type. To find the dataType by the field value, we will use
             * getDataType() method of DataTypeDefinitions class
             */

            DataTypeDefinitions definitions = new DataTypeDefinitions();


            RowDataTypeDefinitions rowData = new RowDataTypeDefinitions();

            for(int i =0 ;i< dataValuesArray.length;i++) {

                rowData.put(i,definitions.getDataType(dataValuesArray[i]));

            }

            /*
             * once we have the header and dataTypeDefinitions maps populated, we can start
             * reading from the first line. We will read one line at a time, then check
             * whether the field values satisfy the conditions mentioned in the query,if
             * yes, then we will add it to the resultSet. Otherwise, we will continue to
             * read the next line. We will continue this till we have read till the last
             * line of the CSV file.
             */

            /* reset the buffered reader so that it can start reading from the first line */

            Buff = null;


            /*
             * skip the first line as it is already read earlier which contained the header
             */
            Buff = new BufferedReader(new FileReader(queryParameter.getFileName()));

            Buff.readLine(); // this will read the first line

            String line1=null;

            boolean conditionPresent = false;




            List<String> fields = queryParameter.getFields();

            List<Restriction> restriction = queryParameter.getRestrictions();

            List<String> logicalOperators = queryParameter.getLogicalOperators();


            String queryString = queryParameter.getQueryString();

            for(String s: queryString.split(" ") )
            {
                if(s.equals("where")){
                    conditionPresent = true;
                }
            }
            String line = null;
            long rowIndex = 1;
            int count = 0;
            while ((line = Buff.readLine()) != null){


                /* read one line at a time from the CSV file till we have any lines left */


                /*
                 * once we have read one line, we will split it into a String Array. This array
                 * will continue all the fields of the row. Please note that fields might
                 * contain spaces in between. Also, few fields might be empty.
                 */
                String[] lineArray = line.split(",");
                String[] editedLineArray = new String[lineArray.length+1];

                for(int i=0;i<lineArray.length;i++){
                    editedLineArray[i]= lineArray[i];
                }
                editedLineArray[lineArray.length] = "";
                /*
                 * if there are where condition(s) in the query, test the row fields against
                 * those conditions to check whether the selected row satifies the conditions
                 */
                List<String> list = Arrays.asList(lineArray);



                /*
                 * from QueryParameter object, read one condition at a time and evaluate the
                 * same. For evaluating the conditions, we will use evaluateExpressions() method
                 * of Filter class. Please note that evaluation of expression will be done
                 * differently based on the data type of the field. In case the query is having
                 * multiple conditions, you need to evaluate the overall expression i.e. if we
                 * have OR operator between two conditions, then the row will be selected if any
                 * of the condition is satisfied. However, in case of AND operator, the row will
                 * be selected only if both of them are satisfied.
                 */

                /*
                 * check for multiple conditions in where clause for eg: where salary>20000 and
                 * city=Bangalore for eg: where salary>20000 or city=Bangalore and dept!=Sales
                 */

                /*
                 * if the overall condition expression evaluates to true, then we need to check
                 * if all columns are to be selected(select *) or few columns are to be
                 * selected(select col1,col2). In either of the cases, we will have to populate
                 * the row map object. Row Map object is having type <String,String> to contain
                 * field Index and field value for the selected fields. Once the row object is
                 * populated, add it to DataSet Map Object. DataSet Map object is having type
                 * <Long,Row> to hold the rowId (to be manually generated by incrementing a Long
                 * variable) and it's corresponding Row Object.
                 */
                Row row = new Row();

                if(fields.get(0).equals("*")){
                    count++;
                    for(int j=0;j<headerArray.length;j++){
                            row.put(headerArray[j],editedLineArray[j].trim());
                        }
                }
                else if(!conditionPresent){

                    for(int i=0;i<fields.size();i++){

                        row.put(fields.get(i),editedLineArray[headerMap.getHeader().get(fields.get(i))]);

                    }
                }

                else{

                    Filter filter = new Filter();
                    Boolean rowAddFlag = filter.evaluateExpression(headerMap, rowData, editedLineArray,
                            restriction, logicalOperators);
                    if (rowAddFlag) {
                        for (int i = 0; i < fields.size(); i++) {

                            row.put((String) fields.get(i), editedLineArray[headerMap.getHeader().get(fields.get(i))]);

                        }
                        resultSet.put(rowIndex, row);
                    }


                }

                resultSet.put(rowIndex,row);

                rowIndex++;

            }

        }

        catch(IOException e){

        }


        /* return dataset object */

		return resultSet;
	}

}
