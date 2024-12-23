package my.edu.um.umpoint.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConvertUtils {
    private static Logger logger = LoggerFactory.getLogger(ConvertUtils.class);

    public static <T> T sourceToTarget(Object source, Class<T> target){
        if(source == null){
            return null;
        }
        T targetObject = null;
        try {
            targetObject = target.newInstance();
            BeanUtils.copyProperties(source, targetObject);
            for (Field field : getField(target)) {
                field.setAccessible(true);
                field.set(targetObject, sourceToTarget(getObject(source, field.getName()), field.getType()));
            }
            for (Field dtoList : getFieldList(target)) {
                ParameterizedType genericType = (ParameterizedType) dtoList.getGenericType();
                Class<?> clazz = (Class<?>) genericType.getActualTypeArguments()[0];
                dtoList.setAccessible(true);
                dtoList.set(targetObject, sourceToTarget((Collection<?>) getObject(source, dtoList.getName()), clazz));
            }
        } catch (Exception e) {
            logger.error("convert error ", e);
        }

        return targetObject;
    }

    public static <T> List<T> sourceToTarget(Collection<?> sourceList, Class<T> target){
        if(sourceList == null){
            return null;
        }

        List targetList = new ArrayList<>(sourceList.size());
        try {
            for(Object source : sourceList){
                T targetObject = sourceToTarget(source, target);
                targetList.add(targetObject);
            }
        }catch (Exception e){
            logger.error("convert error ", e);
        }

        return targetList;
    }

    private static List<Field> getField(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (!field.getType().isPrimitive() && (field.getName().endsWith("DTO") || field.getName().endsWith("Entity"))) {
                fieldList.add(field);
            }
        }

        return fieldList;
    }

    private static List<Field> getFieldList(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (!field.getType().isPrimitive() && (field.getName().endsWith("DTOList") || field.getName().endsWith("EntityList"))) {
                fieldList.add(field);
            }
        }

        return fieldList;
    }

    private static Object getObject(Object source, String name) {
        try {
            String fieldName;
            Class<?> sourceClass = source.getClass();
            if (name.endsWith("DTO") || name.endsWith("DTOList")) {
                fieldName = name.replace("DTO", "Entity");
            } else if (name.endsWith("Entity") || name.endsWith("EntityList")) {
                fieldName = name.replace("Entity", "DTO");
            } else {
                throw new RuntimeException("Did not find DTO or Entity in target class:" + sourceClass);
            }
            Field field = sourceClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(source);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("convert error ", e);
            return null;
        }
    }
}