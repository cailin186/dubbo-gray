package com.xlz.commons.utils;

import java.lang.reflect.Field;

public class ReflectUtils {

	public static Object getInstanceFieldValue(Object instance, String fieldName)
			{
		Field fields[] = instance.getClass().getDeclaredFields();// 获得对象所有属性
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);// 修改访问权限
			if (fieldName.equals(field.getName())) {
				try {
					return field.get(instance);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}// 读取属性值
			}
		}
		return null;
	}
}
