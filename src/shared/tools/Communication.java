package shared.tools;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import shared.model.*;

public class Communication {
	
	private static int intToByte(int num, byte[] data, int index) {
        data[index++] = (byte) ((num >> 24) & 0xFF);
        data[index++] = (byte) ((num >> 16) & 0xFF);
        data[index++] = (byte) ((num >> 8) & 0xFF);
        data[index++] = (byte) ((num) & 0xFF);
        return index;
    }
	
	
	private static int stringToByte(String word, byte[] data, int index) {
        for (byte part : word.getBytes()) {
            data[index++] = part;
        }
        return index;
    }

    private static int longToByte(long num, byte[] data, int index) {
        data[index++] = (byte) ((num >> 56) & 0xFF);
        data[index++] = (byte) ((num >> 48) & 0xFF);
        data[index++] = (byte) ((num >> 40) & 0xFF);
        data[index++] = (byte) ((num >> 32) & 0xFF);
        data[index++] = (byte) ((num >> 24) & 0xFF);
        data[index++] = (byte) ((num >> 16) & 0xFF);
        data[index++] = (byte) ((num >> 8) & 0xFF);
        data[index++] = (byte) ((num) & 0xFF);
        return index;
    }
    
    private static int byteToInt(byte[] data, int start) {
        try {
            int i = 0;
            i += ((int) data[start++] & 0xFF) << 24;
            i += ((int) data[start++] & 0xFF) << 16;
            i += ((int) data[start++] & 0xFF) << 8;
            i += ((int) data[start++] & 0xFF);
            return i;
        } catch (IndexOutOfBoundsException e) {
            return (Integer) null;
        }
    }
    
    private static String byteToString(byte[] data, int start, int length) {
        try {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
            	builder.append((char) data[start++]);
            }
            return builder.toString();
        } catch (IndexOutOfBoundsException e) {
            return (String)null;
        }
    }
    
	public static byte[] marshal(Request object){
		/*
		 * Need to discuss with team for implementation details 
		 */
		
		return null;
	}
	
	public static byte[] marshal(Reply object){
		/*
		 * Need to discuss with team for implementation details
		 */
		return null;
	
	}
	
	public static Object unMarshal(byte[] data, Class<?> type){
		/*
		 * Need to discuss with team for implementation details
		 */
		return null;
	}
	
	public static byte[] marshal(Object o) throws IllegalArgumentException, IllegalAccessException{
    	Field[] g = o.getClass().getDeclaredFields();
    	Arrays.sort(g, new Comparator<Field>(){
			public int compare(Field f1, Field f2){
				return f1.getName().compareTo(f2.getName());
			}
		});
    	int size = 0;
    	for (Field l: g){
			if (l.getType().isAssignableFrom(Integer.TYPE))
				size +=4;
			else if (l.getType().isAssignableFrom(String.class)){
				l.setAccessible(true);
				size += 4 + l.get(o).toString().length();
			}
		}
		byte[] buffer = new byte[size];
		int index = 0;
		
		for (Field l: g){
			if (l.getType().isAssignableFrom(Integer.TYPE)){
				l.setAccessible(true);
				index = intToByte(l.getInt(o), buffer, index);
			}
			else if (l.getType().isAssignableFrom(String.class)){
				l.setAccessible(true);
				index = intToByte(l.get(o).toString().length(), buffer, index);
				index = stringToByte(l.get(o).toString(), buffer, index);
			}
			else if (l.getType().isAssignableFrom(Date.class)){
				l.setAccessible(true);
				System.out.println(l.get(o).toString());
			}
		}
		
    	return buffer;
    }
    
	/*
    public static <T> Object unMarshal(byte[] buffer, Class<T> type) throws InstantiationException, IllegalAccessException{
    	Object o = type.newInstance();
    	Field[] g = o.getClass().getDeclaredFields();
    	Arrays.sort(g, new Comparator<Field>(){
			public int compare(Field f1, Field f2){
				return f1.getName().compareTo(f2.getName());
			}
		});
    	int index = 0;
		for (Field l: g){
			if (l.getType().isAssignableFrom(Integer.TYPE)){
				int h = byteToInt(buffer, index);
				l.setAccessible(true);
				l.setInt(o, h);
				index += 4;
			}
			else if (l.getType().isAssignableFrom(String.class)){
				int len = byteToInt(buffer,index);
				index += 4;
				String val = byteToString(buffer, index, len);
				index += len;
				l.setAccessible(true);
				l.set(o,val);
			}
			
		}
    	return o;
    }
    */
}
