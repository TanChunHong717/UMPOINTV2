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
            for (Field dto : getDto(target)) {
                dto.setAccessible(true);
                dto.set(targetObject, sourceToTarget(getEntity(source, dto.getName()), dto.getDeclaringClass()));
            }
            for (Field dtoList : getDtoList(target)) {
                ParameterizedType genericType = (ParameterizedType) dtoList.getGenericType();
                Class<?> clazz = (Class<?>) genericType.getActualTypeArguments()[0];
                dtoList.setAccessible(true);
                dtoList.set(targetObject, sourceToTarget((Collection<?>) getEntity(source, dtoList.getName()), clazz));
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

    private static List<Field> getDto(Class<?> clazz) {
        List<Field> dtoFields = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (!field.getType().isPrimitive() && field.getName().endsWith("DTO")) {
                dtoFields.add(field);
            }
        }

        return dtoFields;
    }

    private static List<Field> getDtoList(Class<?> clazz) {
        List<Field> dtoFields = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (!field.getType().isPrimitive() && field.getName().endsWith("DTOList")) {
                dtoFields.add(field);
            }
        }

        return dtoFields;
    }


    private static Object getEntity(Object source, String name) {
        try {
            Class<?> sourceClass = source.getClass();
            Field field = sourceClass.getDeclaredField(name.replace("DTO", "Entity"));
            field.setAccessible(true);
            return field.get(source);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("convert error ", e);
            return null;
        }
    }
}