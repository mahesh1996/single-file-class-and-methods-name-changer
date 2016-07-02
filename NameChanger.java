import java.io.*;
import java.nio.file.*;
import java.util.regex.*;
import java.lang.reflect.*;
import java.util.*;
public class NameChanger{
	public static HashMap<Character,Integer> charInfo = new HashMap<>();
	public static LinkedHashMap<String,String> classInfo = new LinkedHashMap<String,String>();
	public static char currentChar = 'A';
	public static void main(String args[]){
		StringBuilder content = null;
		Class test = null;
		try{
			byte b[] = args[0].getBytes();
			test = Class.forName(new String(b,0,b.length - 5));
			content = new StringBuilder(new String(Files.readAllBytes(Paths.get(args[0]))));
			for(char i='A';i<='Z';i++)
			charInfo.put(i,0);
		
			classInfo.put(test.getName(),getNextToken(currentChar,charInfo.get(currentChar)));
			
			for (Method method : test.getDeclaredMethods()) {
				String name = method.getName();
				//System.out.println(name);
				if(name.equals("main"));
				else
					classInfo.put(name,getNextToken(currentChar,charInfo.get(currentChar)));
			}
			
			
			Set<Map.Entry<String,String>> x = classInfo.entrySet();
			for(Map.Entry y : x)
			{
				//System.out.println(y.getKey() + " = " + y.getValue());
				replaceAll(content,Pattern.compile("\\b" + (String)y.getKey() + "\\b"),(String)y.getValue());
			}
			try(  PrintWriter out = new PrintWriter( (String)classInfo.get(test.getName()) + ".java" )  ){
				out.println( content.toString() );
				System.out.println("File written with name = " + (String)classInfo.get(test.getName()) + ".java");
			}
		}
		catch(Exception e){
			System.out.println("Check file name and ensure file is complied and placed in same directory and class is public");
			System.out.println("Usage : java NameChanger filename.java");
		}
		
	}
	
	public static String getNextToken(char symbol,int no){
		StringBuilder nextPattern = new StringBuilder();
		for(int i=0;i<no + 1;i++)
			nextPattern.append(symbol);
		if(symbol == 'Z')
			currentChar = 'A';
		else
			currentChar = (char)((int)symbol + 1);
		charInfo.put(symbol,no+1);
		//System.out.println(currentChar);
		return new String(nextPattern);
	}
	
	public static void replaceAll(StringBuilder sb,Pattern pattern,String replacement){
		Matcher m = pattern.matcher(sb);
		int start = 0;
		while (m.find(start)) {
			//System.out.println("Pattern found = " + m.group());
			sb.replace(m.start(), m.end(), replacement);
			start = m.start() + replacement.length();
		}
	}
}