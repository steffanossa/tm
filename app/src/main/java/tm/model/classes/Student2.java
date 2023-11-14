// package tm.model.classes;

// public class Student2 {
//     public static void main(String[] args) {

//         String url = "app/src/main/resources/config.properties";
        
//         DynamicClass dynClass = new DynamicClass();
//         String[] properties = {"first_name", "surname", "matriculation_number", "fh_identifier"};
//         String propertyValue = null;
//         for (String property : properties) {
//             propertyValue = ConfigParser.parse(url, "properties." + property);
//             dynClass.setAttribute(CaseUtils.toCamelCase(propertyValue, false, new char[]{'_'}), propertyValue);
//         }
//     }
// }