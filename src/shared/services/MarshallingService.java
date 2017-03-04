package shared.services;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import shared.Marshallable;

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
				size += 4 + field.get(obj).toString().length();
			} else if (Marshallable.class.isAssignableFrom(field.getType())) {
				size += getSize((Marshallable) field.get(obj));
			} else if (List.class.isAssignableFrom(field.getType())
					&& Marshallable.class.isAssignableFrom(getListGenericType(field.getGenericType()))) {
				size += 4;
				List<?> list = (List<?>) field.get(obj);
				for (Object item : list) {
					size += getSize((Marshallable) item);
				}
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
				buffer.put(marshal((Marshallable) field.get(obj)));
			} else if (List.class.isAssignableFrom(field.getType())
					&& Marshallable.class.isAssignableFrom(getListGenericType(field.getGenericType()))) {
				List<?> list = (List<?>) field.get(obj);
				buffer.putInt(list.size());
				for (Object item : list) {
					buffer.put(marshal((Marshallable) item));
				}
			}
		}
		return buffer.array();
	}
	
	public <T extends Marshallable> T unmarshal(byte[] arr, Class<T> type) 
			throws InstantiationException, IllegalAccessException {
		ByteBuffer buffer = ByteBuffer.wrap(arr);
		return unmarshal(buffer, type);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Marshallable> T unmarshal(ByteBuffer buffer, Class<T> type) 
			throws InstantiationException, IllegalAccessException {
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
				field.set(data, unmarshal(buffer, (Class<? extends Marshallable>)field.getType()));
			} else if (List.class.isAssignableFrom(field.getType())
					&& Marshallable.class.isAssignableFrom(getListGenericType(field.getGenericType()))) {
				int len = buffer.getInt();
				List<Marshallable> list = (List) field.get(data);
				list.clear();
				Class<?> genericType = getListGenericType(field.getGenericType());
				for (int i = 0; i < len; i++) {
					list.add(unmarshal(buffer, (Class<? extends Marshallable>)genericType));
				}
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
