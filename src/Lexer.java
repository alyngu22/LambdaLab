import javax.swing.*;
import java.lang.reflect.Array;
import java.util.*;

public class Lexer {
    public static ArrayList<String> cleanUp (String input) {
        ArrayList<String> strArrList = new ArrayList<>();
        int indexOfSemi = input.indexOf(';');
        if (indexOfSemi != -1) {
            input = input.substring(0, indexOfSemi);
        }


        input = input.trim();
        input = input.replaceAll("\\(", " ( ");
        input = input.replaceAll( "\\)", " ) ");
        input = input.replaceAll( "\\\\", " \\\\ ");
        input = input.replaceAll( "\\.", " . ");

        input = input.trim();

        StringTokenizer st = new StringTokenizer(input);
        while (st.hasMoreTokens()) {
            strArrList.add(st.nextToken());
        }
        return strArrList;
    }
}