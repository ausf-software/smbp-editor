package io.github.ausf_software.smbp_editor.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Содержит статические методы, которые позволяют удобно работать с конфигурационными
 * файлами инструментов с высоким уровнем абстракции.
 * @since 1.0
 * @version 1.0
 * @author Daniil Scherbina
 */
public class ConfigureToolObjectUtil {
    private static final Logger log = LoggerFactory.getLogger(ConfigureToolObjectUtil.class);

    /**
     * Генерирует шаблон конфигурационного файла для инструмента
     * @param fields множество полей которые необходимо конфигурировать
     * @param file объект файла конфигурации инструмента
     */
    public static void generateConfigFile(Set<Field> fields, File file) {
        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            for (Field f : fields) {
                String cfg = f.getName() +"=\n";
                fileWriter.write(cfg);
                fileWriter.flush();
            }
            fileWriter.close();
        } catch (IOException e) {
            log.error("Ошибка создания шаблона конфигурационного файла по пути: " +
                    "{}", file.getPath());
        }
    }

    /**
     * Считывает файл конфигурации и устанавливает значение полей для
     * объекта инструмента.
     * @param toolObject объект инструмента
     * @param fields множество полей значение которых необходимо установить
     * @param clazz объект класса инструмента
     * @param file объект файла конфигурации
     * @return true если конфигурационный файл не содержит ошибок, false иначе
     */
    public static boolean configure(Object toolObject, Set<Field> fields, Class<?> clazz, File file) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
            for (Field f : fields) {
                String key = f.getName();
                Method m = getMethodByKey(key, clazz);
                if (m == null) {
                    log.error("В классе инструмента отсутствует метод {}", getMethodName(key));
                    return false;
                }
                if (!f.getType().getName().equals("[Ljava.lang.String;")) {
                    String value = properties.getProperty(key);
                    if ((value == null || value.equals(""))) {
                        log.error("В конфигурационном файле {} отсутствует поле {}. Инструмент будет " +
                                "проигнорирован.", file.getPath(), key);
                        return false;
                    }
                    setField(toolObject, m, value, f);
                } else {
                    Set<String> values = loadArray(properties, key);
                    setStringArrayField(toolObject, m, values.toArray(new String[values.size()]));
                }
            }
        } catch (IOException e) {
            log.error("Ошибка при чтении конфигурационного файла по пути: {}", file.getPath());
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * Читает из набора свойств массив заданного имени
     * @param properties набор свойств
     * @param key имя массива
     * @return считанное множество из значений массива
     */
    public static Set<String> loadArray(Properties properties, String key) {
        Set<String> values = new HashSet<>();
        String cur = "";
        int i = 0;
        while ((cur = properties.getProperty(key +"_" + i)) != null) {
            values.add(cur);
            i++;
        }
        return values;
    }

    /**
     * Устанавливает значение полю массива строк
     * @param object объект инструмента для которого устанавливается значение
     * @param m метод сеттера для поля
     * @param value значение которое необходимо установить
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static void setStringArrayField(Object object, Method m, String[] value)
            throws InvocationTargetException, IllegalAccessException {
        m.invoke(object, (Object) value);
    }

    /**
     * Устанавливает записанное в конфигурационном файле значение для полей
     * не являющимися массивом строк
     * @param object объект инструмента для которого устанавливается значение
     * @param m метод сеттера для поля
     * @param value значение которое необходимо установить
     * @param f объект поля для которого устанавливается значение
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static void setField(Object object, Method m, String value, Field f)
            throws InvocationTargetException, IllegalAccessException {
        String type = f.getType().getName();
        switch (type) {
            case "int":
                m.invoke(object, Integer.valueOf(value));
                break;
            case "long":
                m.invoke(object, Long.valueOf(value));
                break;
            case "short":
                m.invoke(object, Short.valueOf(value));
                break;
            case "byte":
                m.invoke(object, Byte.valueOf(value));
                break;
            case "java.lang.String":
                m.invoke(object, value);
                break;
            case "java.awt.Color":
                m.invoke(object, new Color(Integer.valueOf(value)));
                break;
            default:
                log.error("Неподдерживаемый тип данных для конфигурации: {}", type);
        }
    }

    /**
     * Возвращает ожидаемое имя сеттера для поля
     * @param key имя поля
     * @return ожидаемое имя сеттера для поля
     */
    private static String getMethodName(String key) {
        return "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
    }

    /**
     * Ищет и возвращает сеттер для поля
     * @param key строка с именем сеттера исходя из имени поля
     * @param clazz класс в котором происходит поиск
     * @return сеттер для поля
     */
    private static Method getMethodByKey(String key, Class<?> clazz) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(getMethodName(key)))
                return m;
        }
        return null;
    }
}
