import com.sun.xml.internal.ws.util.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;
//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

public class Main {
    public static void main(String[] args) {
        new Main().processJson();
    }

    /**
     1. Read File
     2. Extract all the fields
     3. Extract Master Data
     4. Generate new Json File
     **/

    public void processJson(){
        //json file path to be read
        String jsonString = readDirectory("D:\\Spring Boot\\JsonGenerator\\resources\\");

        //generate new Json File
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.setLength(0);
        strBuilder.append(extractKeyValuePair(jsonString));
        System.out.println(beautifyJson(strBuilder.toString()));

        //json file directory to be generated
        String writeDirectory = "D:\\Spring Boot\\test-file\\";

        //write file
        writeJsonToFile(strBuilder.toString(),  writeDirectory + "case_" + System.currentTimeMillis() + (new File(writeDirectory).listFiles().length + 1) + ".json");
    }

    private String readDirectory(String directoryPath){
        //String directoryPath = "/path/to/your/directory"; // Replace with your directory path
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            return extractFiles(directory);
        } else {
            System.out.println("The specified directory does not exist.");
            return null;
        }
    }

    private String extractFiles(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursively process subdirectories
                    extractFiles(file);
                } else {
                    // Process individual files
                    System.out.println("File: " + file.getAbsolutePath());
                    String jsonString = readFile(file.getAbsolutePath());
                    return jsonString;
                    // Add your logic to process or extract the file here
                }
            }
        }
        return null;
    }

    private String readFile(String filePath){
        StringBuilder strBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                strBuilder.append(line);
            }

            // Assuming the entire JSON content is now in the jsonContent StringBuilder


            // Parse the JSON manually as needed
            // For example, you can use regular expressions, StringTokenizer, or custom parsing logic

            // Example: Extracting values by key
//            String field1 = extractValueByKey(jsonString, "field1");
//            int field2 = extractValueByKeyAsInt(jsonString, "field2");
//
//            System.out.println("field1: " + field1);
//            System.out.println("field2: " + field2);
            return strBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private StringBuilder beautifyJson(String jsonString){
        //read line by line
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder tempStringBuilder = new StringBuilder();
        stringBuilder.append(jsonString.replaceFirst("\\{","{\n").replaceAll(",\t\"|,\\s*\"",",\n\""));

        tempStringBuilder.append(stringBuilder.toString());
        stringBuilder.setLength(0);
        stringBuilder.append(tempStringBuilder.toString().replaceAll("}","\n}"));

        tempStringBuilder.setLength(0);
        tempStringBuilder.append(stringBuilder.toString());
        stringBuilder.setLength(0);
        stringBuilder.append(tempStringBuilder.toString().replaceAll(":\\{|:\\s*\\{",": {\n"));

        tempStringBuilder.setLength(0);
        tempStringBuilder.append(stringBuilder.toString());

        return stringBuilder;
    }

    private String extractKeyValuePair(String jsonString) {
        StringBuilder stringBuilder = beautifyJson(jsonString);

        Stack<String> jsonStackCurlyBracket = new Stack<>();
        HashSet<String> keyValue = new HashSet<>();
//        System.out.println(stringBuilder.toString());
        String[] splitElem = stringBuilder.toString().split("\n");
        stringBuilder.setLength(0);
        for(String jsonElem : splitElem){
            String[] splitKeyPairs = jsonElem.split(":",2);
            if(splitKeyPairs[0].contains("body")){
                stringBuilder.append(jsonElem);
                continue;
            }
            else if(splitKeyPairs[0].contains("txnDateTime")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                String formattedDate = sdf.format(new Date());
                splitKeyPairs[1] = "\""+formattedDate+ "\",";
            }
            else if(splitKeyPairs[0].contains("txnId")){
                splitKeyPairs[1] = "\""+randomGenerateValue() + "\",";
            }
//            else if(splitKeyPairs[1].contains("\"")){ //have other data
//                if(splitKeyPairs[1].matches("\\d+")){ //money
//                    splitKeyPairs[1] = randomGenerateData();
//                }
//                else if(){ //check for master data
//
//                }
//            }
//            else if(splitKeyPairs[1].contains("{")){ //have inner body
//
//            }
//            else{ //numeric
//
//            }
            stringBuilder.append(String.join(":",splitKeyPairs));
        }
        return stringBuilder.toString();
    }

    private String randomGenerateMoney(){
        return null;
    }

    private String randomGenerateValue(){
        Random random = new Random();
        long value = (long) (random.nextDouble() * 9000000000L + 1000000000L);
        return String.valueOf(value);
    }

    // Example utility methods for extracting values by key
//    private String extractValueByKey(String jsonString, String key) {
//        int startIndex = jsonString.indexOf("\"" + key + "\":");
//        if (startIndex == -1) {
//            return null;
//        }
//
//        int valueStartIndex = jsonString.indexOf(":", startIndex) + 1;
//        int valueEndIndex = jsonString.indexOf(",", valueStartIndex);
//        if (valueEndIndex == -1) {
//            valueEndIndex = jsonString.indexOf("}", valueStartIndex);
//        }
//
//        return jsonString.substring(valueStartIndex, valueEndIndex).trim().replaceAll("\"", "");
//    }
//
//    private int extractValueByKeyAsInt(String jsonString, String key) {
//        String value = extractValueByKey(jsonString, key);
//        if (value == null) {
//            return 0; // or handle the case where the key is not found
//        }
//        return Integer.parseInt(value);
//    }



    private void writeJsonToFile(String jsonContent, String filePath){
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(jsonContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}