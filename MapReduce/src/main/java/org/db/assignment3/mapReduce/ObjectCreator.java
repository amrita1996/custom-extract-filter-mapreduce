package org.db.assignment3.mapReduce;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectCreator<T> {
    public static final String CONTENT = ".*";
    public static final String EMPTY = "";

    public void setFields(String content, List<String> fields, T object) {
        fields.forEach(field -> {
            String prefix = "\"" + field + "\":\\s?\"";
            String suffix = getSuffix(field, fields);
            String regex = prefix + CONTENT + suffix;
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);
            if(matcher.find()) {
                String group = matcher.group();
                String fieldContent = group.replaceAll(prefix, EMPTY).replaceAll(suffix, EMPTY);
                try {
                    Field declaredField = object.getClass().getDeclaredField(field);
                    declaredField.setAccessible(true);
                    declaredField.set(object, fieldContent);
                } catch (NoSuchFieldException | IllegalAccessException ignored) {
                }
            }
        });
    }

    private String getSuffix(String field, List<String> fields) {
        int index = fields.indexOf(field);
        return index == fields.size() - 1 ? "\"}" :
                "\",\"" + fields.get(index + 1) + "\"";
    }

}
