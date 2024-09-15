package com.czx.demoj.nio.utils;

import com.czx.demoj.nio.TestByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class BufferUtils {
    private static final Logger log = LoggerFactory.getLogger(BufferUtils.class);

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        ByteBuffer encode = StandardCharsets.UTF_8.encode("哈哈哈我学会 utf-8 的转换拉");
        encode.position(10);
        debugAll(encode);
    }

    public static void debugAll(ByteBuffer buffer) throws NoSuchFieldException, IllegalAccessException {
        debugAll(buffer, StandardCharsets.UTF_8);
    }

    public static void debugAll(ByteBuffer buffer, Charset charset) throws NoSuchFieldException, IllegalAccessException {
        Map<String, Object> convertResultMap = convertBufferToPropertyMap(buffer);
        List<Byte> bufferList = (List<Byte>) convertResultMap.get("list");
        byte[] hb = (byte[]) convertResultMap.get("hb");
        Integer position = (Integer) convertResultMap.get("position");
        Integer limit = (Integer) convertResultMap.get("limit");
        Integer capacity = (Integer) convertResultMap.get("capacity");

        final int size = bufferList.size();
        final int sizeHexStrLength = Integer.toHexString(size - 1).length();
        final int finalHexStrSize = Math.max(sizeHexStrLength, 2);

        Function<List<Byte>, String> convertByteListToString = (list) -> new String(hb, charset);

        Function<Integer, String> generateOutline = (s) -> {
            StringBuilder outline = new StringBuilder();
            outline.append("+");
            for (int i = 0; i < s; i++) {
                outline.append("-".repeat(finalHexStrSize));
                outline.append("---"); // padding
            }
            outline.append("-+" + "\n");

            return outline.toString();
        };


        BiFunction<List<Byte>, String, String> generateDataLine = (l, m) -> {
            StringBuilder result = new StringBuilder();
            result.append("| ");

            BiFunction<Integer, Integer, String> convertIntToHexStringWithCompletion = (number, finalLenth) -> {
                String hexString = Integer.toHexString(number);
                int completion = finalHexStrSize - hexString.length();
                return " ".repeat(completion) + hexString;
            };

            BiFunction<Integer, Integer, String> convertByteToHexStringWithCompletion = (number, finalLenth) -> {
                String hexString = Integer.toHexString(number & 0xFF);
                int completion = finalHexStrSize - hexString.length();
                return " ".repeat(completion) + hexString;
            };

            BiFunction<Integer, Integer, String> convertByteToCharStringwithCompletion = (number, finalLenth) -> {
                String hexString = escapeChar((char) (number & 0xff));
                int completion = finalHexStrSize - hexString.length();
                return " ".repeat(completion) + hexString;
            };

            for (int i = 0; i < l.size(); i++) {
                if (Objects.equals(m, "index")) {
                    String indexWithCompletion = convertIntToHexStringWithCompletion.apply(i, finalHexStrSize);
                    result.append(indexWithCompletion);
                } else if (Objects.equals(m, "data")) {
                    String dataWithCompletion = convertByteToHexStringWithCompletion.apply((int) l.get(i), finalHexStrSize);
                    result.append(dataWithCompletion);
                } else if (Objects.equals(m, "hex_data")) {
                    String dataWithCompletion = convertByteToCharStringwithCompletion.apply(l.get(i).intValue(), finalHexStrSize);
                    result.append(dataWithCompletion);
                } else {
                    throw new RuntimeException("No such mode: " + m);
                }
                result.append(" | "); // padding
            }

            result.append("|\n");
            return result.toString();
        };

        String startStr = " -< start >- ";
        System.out.print(generateOutline.apply(size).replaceAll("(?<=\\+-)(-{" + startStr.length() + "})", startStr));
        System.out.printf("  position: [%d], limit: [%d], capacity: [%d]\n", position, limit, capacity);
        System.out.print(generateOutline.apply(size));
        System.out.print(generateDataLine.apply(bufferList, "index"));
        System.out.print(generateOutline.apply(size));
        System.out.print(generateDataLine.apply(bufferList, "data"));
        System.out.print(generateOutline.apply(size));
        System.out.print(generateDataLine.apply(bufferList, "hex_data"));
        System.out.print(generateOutline.apply(size));
        System.out.print(" \"" + convertByteListToString.apply(bufferList) + "\"\n");
        String endStr = " -< end >- ";
        System.out.print(generateOutline.apply(size).replaceAll("(?<=\\+-)(-{" + endStr.length() + "})", endStr));
        System.out.println();
    }

    public static String escapeChar(char c) {
        Map<Character, String> escapeMap = new HashMap<>();
        escapeMap.put('\n', "\\n");
        escapeMap.put('\t', "\\t");
        escapeMap.put('\b', "\\b");
        escapeMap.put('\r', "\\r");
        escapeMap.put('\f', "\\f");
        escapeMap.put('\\', "\\\\");
        escapeMap.put('\'', "\\\'");
        escapeMap.put('\"', "\\\"");

        return escapeMap.getOrDefault(c, Character.toString(c));
    }

    public static Map<String, Object> convertBufferToPropertyMap(ByteBuffer buffer) throws NoSuchFieldException, IllegalAccessException {

        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);

        Field hbField = ByteBuffer.class.getDeclaredField("hb");
        Field positionField = Buffer.class.getDeclaredField("position");
        Field limitField = Buffer.class.getDeclaredField("limit");
        Field capacityField = Buffer.class.getDeclaredField("capacity");

        long hbFieldOffset = unsafe.objectFieldOffset(hbField);
        byte[] hb = (byte[]) unsafe.getObject(buffer, hbFieldOffset);

        long positionFieldOffset = unsafe.objectFieldOffset(positionField);
        int position = unsafe.getInt(buffer, positionFieldOffset);

        long limitFieldOffset = unsafe.objectFieldOffset(limitField);
        int limit = unsafe.getInt(buffer, limitFieldOffset);


        long capacityFieldOffset = unsafe.objectFieldOffset(capacityField);
        int capacity = unsafe.getInt(buffer, capacityFieldOffset);

        List<Byte> list = new ArrayList<>();
        for (byte b : hb) {
            list.add(b);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("hb", Arrays.copyOf(hb, hb.length));
        result.put("list", list);
        result.put("position", position);
        result.put("limit", limit);
        result.put("capacity", capacity);
        return result;
    }


    public static File getResourceFile(String path) {
        ClassLoader classLoader = TestByteBuffer.class.getClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            return null;
        }
        return new File(resource.getFile());
    }

}
