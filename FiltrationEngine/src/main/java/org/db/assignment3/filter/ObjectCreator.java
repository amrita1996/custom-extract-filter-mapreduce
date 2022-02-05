package org.db.assignment3.filter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectCreator<T> {
    public static final String CONTENT = ".*";
    public static final String EMPTY = "";
    public static final String INVALID_CHARACTER_REGEX = "[^A-z0-9\\s]|\\\\+";
    public static final String SPACE = " ";

    public void setCleanFields(String content, List<String> fields, T object) {
        fields.forEach(field -> {
            String prefix = "\"" + field + "\":\\s?\"";
            String suffix = getSuffix(field, fields);
            String regex = prefix + CONTENT + suffix;
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);
            if(matcher.find()) {
                String group = matcher.group();
                String fieldContent = group.replaceAll(prefix, EMPTY).replaceAll(suffix, EMPTY);
                String cleanedData = fieldContent.replaceAll(INVALID_CHARACTER_REGEX, SPACE).trim();
                try {
                    Field declaredField = object.getClass().getDeclaredField(field);
                    declaredField.setAccessible(true);
                    declaredField.set(object, cleanedData);
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
