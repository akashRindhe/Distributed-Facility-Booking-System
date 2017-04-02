package shared.service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import shared.webservice.*;
import shared.Marshallable;

@SuppressWarnings("unused")
public class MarshallingService {
	
	private static MarshallingService instance;
		
	private Field[] getSortedFields(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
    	Arrays.sort(fields, new Comparator<Field>(){
			public int compare(Field f1, Field f2){
				return f1.getName().compareTo(f2.getName());
			}
		});
    	return fields;
	}
	
	private Class<?> getListGenericType(Type listType) {
		return (Class<?>) ((ParameterizedType) listType).getActualTypeArguments()[0];
	}
	
	private int getSize(Marshallable obj) 
			throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = obj.getClass().getDeclaredFields();
		int size = 0;
		for (Field field : fields) {
			if (field.getType().isAssignableFrom(Integer.TYPE)) {
				size += 4;
			} else if (field.getType().isAssignableFrom(String.class)) {
				field.setAccessible(true);
				size += 4 + field.get(obj).toString().length();
			} else if (Marshallable.class.isAssignableFrom(field.getType())) {
				field.setAccessible(true);
				size += 4 + field.get(obj).getClass().getCanonicalName().length();
				size += getSize((Marshallable) field.get(obj));
			} else if (List.class.isAssignableFrom(field.getType())
					&& Marshallable.class.isAssignableFrom(getListGenericType(field.getGenericType()))) {
				size += 4;
				field.setAccessible(true);
				List<?> list = (List<?>) field.get(obj);
				for (Object item : list) {
					size += getSize((Marshallable) item);
				}
			} else if (List.class.isAssignableFrom(field.getType())
					&& getListGenericType(field.getGenericType()).isAssignableFrom(Timestamp.class)){
				size += 4;
				field.setAccessible(true);
				@SuppressWarnings("unchecked")
				List<Timestamp> list = (List<Timestamp>) field.get(obj);
				for (Timestamp item : list){
					size += 4 + item.toString().length();
				}
			} else if (List.class.isAssignableFrom(field.getType())
					&& getListGenericType(field.getGenericType()).isAssignableFrom(Integer.TYPE)){
				size += 4;
				field.setAccessible(true);
				@SuppressWarnings("unchecked")
				List<Integer> list = (List<Integer>)field.get(obj);
				size += 4*list.size();
			} else if (field.getType().isAssignableFrom(Timestamp.class)){
				field.setAccessible(true);
				size += 4 + field.get(obj).toString().length();
			}
		}
		return size;
	}
	
	public byte[] marshal(Marshallable obj) 
			throws IllegalArgumentException, IllegalAccessException {
		ByteBuffer buffer = ByteBuffer.allocate(getSize(obj));
		Field[] fields = getSortedFields(obj);
    	for (Field field : fields) {
    		field.setAccessible(true);
			if (field.getType().isAssignableFrom(Integer.TYPE)) {
				buffer.putInt(field.getInt(obj));
			} else if (field.getType().isAssignableFrom(String.class)) {
				String temp = field.get(obj).toString();
				buffer.putInt(temp.length());
				buffer.put(temp.getBytes());
			} else if (Marshallable.class.isAssignableFrom(field.getType())) {
				buffer.putInt(field.get(obj).getClass().getCanonicalName().length());
		    	buffer.put(field.get(obj).getClass().getCanonicalName().getBytes());
				buffer.put(marshal((Marshallable) field.get(obj)));
			} else if (List.class.isAssignableFrom(field.getType())
					&& Marshallable.class.isAssignableFrom(getListGenericType(field.getGenericType()))) {
				List<?> list = (List<?>) field.get(obj);
				buffer.putInt(list.size());
				for (Object item : list) {
					buffer.put(marshal((Marshallable) item));
				}
			}else if (List.class.isAssignableFrom(field.getType())
					&& getListGenericType(field.getGenericType()).isAssignableFrom(Timestamp.class)){
				@SuppressWarnings("unchecked")
				List<Timestamp> list = (List<Timestamp>) field.get(obj);
				buffer.putInt(list.size());
				for (Timestamp item : list){
					buffer.putInt(item.toString().length());
					buffer.put(item.toString().getBytes());
				}
			} else if (List.class.isAssignableFrom(field.getType())
					&& getListGenericType(field.getGenericType()).isAssignableFrom(Integer.TYPE)){
				@SuppressWarnings("unchecked")
				List<Integer> list = (List<Integer>)field.get(obj);
				buffer.putInt(list.size());
				for (Integer temp: list){
					buffer.putInt(temp);
				}
			} else if (field.getType().isAssignableFrom(Timestamp.class)){
				buffer.putInt(field.get(obj).toString().length());
				buffer.put(field.get(obj).toString().getBytes());
			}
		}
    	return buffer.array();
	}
	
	public <T extends Marshallable> T unmarshal(byte[] arr, Class<T> type) 
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		ByteBuffer buffer = ByteBuffer.wrap(arr);
		return unmarshal(buffer, type);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Marshallable> T unmarshal(ByteBuffer buffer, Class<T> type) 
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		T data = type.newInstance();
		Field[] fields = getSortedFields(data);
		for (Field field : fields) {
    		field.setAccessible(true);
			if (field.getType().isAssignableFrom(Integer.TYPE)) {
				int temp = buffer.getInt();
				field.setInt(data, temp);
			} else if (field.getType().isAssignableFrom(String.class)) {
				int len = buffer.getInt();
				byte[] temp = new byte[len];
				buffer.get(temp, 0, len);
				field.set(data, new String(temp));
			} else if (Marshallable.class.isAssignableFrom(field.getType())) {
				int len = buffer.getInt();
				byte[] temp = new byte[len];
				buffer.get(temp, 0, len);
				Class<T> marshClass = (Class<T>) Class.forName(new String(temp));
				field.set(data, unmarshal(buffer,marshClass));
			} else if (List.class.isAssignableFrom(field.getType())
					&& Marshallable.class.isAssignableFrom(getListGenericType(field.getGenericType()))) {
				int len = buffer.getInt();
				List<Marshallable> list = (List) field.get(data);
				list.clear();
				Class<?> genericType = getListGenericType(field.getGenericType());
				for (int i = 0; i < len; i++) {
					list.add(unmarshal(buffer, (Class<? extends Marshallable>)genericType));
				}
			}else if (List.class.isAssignableFrom(field.getType())
					&& getListGenericType(field.getGenericType()).isAssignableFrom(Timestamp.class)){
				int len = buffer.getInt();
				@SuppressWarnings("unchecked")
				List<Timestamp> list = (List<Timestamp>) field.get(data);
				list.clear();
				for (int i = 0; i < len; i++){
					int slen = buffer.getInt();
					byte[] temp = new byte[slen];
					buffer.get(temp, 0, slen);
					String s = new String(temp);
					Timestamp t = Timestamp.valueOf(s);
					list.add(t);
				}
			} else if (List.class.isAssignableFrom(field.getType())
					&& getListGenericType(field.getGenericType()).isAssignableFrom(Integer.TYPE)){
				@SuppressWarnings("unchecked")
				int len = buffer.getInt();
				List<Integer> list = (List<Integer>)field.get(data);
				list.clear();
				for (int i = 0; i < len; i++){
					list.add(buffer.getInt());
				}
			} else if (field.getType().isAssignableFrom(Timestamp.class)){
				int len = buffer.getInt();
				byte[] temp = new byte[len];
				buffer.get(temp,0,len);
				String s = new String(temp);
				Timestamp t = Timestamp.valueOf(s);
				field.set(data, t);
			}
		}
		return data;
	}

	// Singleton pattern
	public static MarshallingService getInstance() {
		if (instance == null) {
			instance = new MarshallingService();
		}
		return instance;
	}
}
