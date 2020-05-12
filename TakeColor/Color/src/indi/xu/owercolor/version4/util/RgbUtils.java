package indi.xu.owercolor.version4.util;

/**
 * 将rgb值装换为16进制值，
 * 将16进制转为rgb
 */
public class RgbUtils {

    private RgbUtils() {
    }

    /**
     * 将整数型rgb值，转换为16进制值
     *
     * @param r red
     * @param g green
     * @param b blue
     * @return 16进制数值字符串
     */
    public static String toHex(int r, int g, int b) {
        return "#" + toBrowserHexValue(r) + toBrowserHexValue(g)
                + toBrowserHexValue(b);
    }

    /**
     * 将整数型rgb值，转换为16进制值2
     *
     * @param rgb rgb数值
     * @return rgb字符串
     */
    private static String toBrowserHexValue(int rgb) {
        StringBuilder sb = new StringBuilder(
                Integer.toHexString(rgb & 0xff));
        //为什么要和0xff做&运算？保持二进制补码的一致性
        while (sb.length() < 2) {
            sb.append("0");
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 16进制值转为整数型rgb值
     *
     * @param hex 16进制值
     * @return rgb字符串
     */
    public static String toInt(String hex) {
        //要头不要尾
        String r = rgbToNumber(hex.substring(0, 2));
        String g = rgbToNumber(hex.substring(2, 4));
        String b = rgbToNumber(hex.substring(4, 6));

        return r + "," + g + "," + b;
    }

    private static String rgbToNumber(String str) {
        return "" + Integer.parseUnsignedInt(str, 16);
    }

    public static void main(String[] args) {
        System.out.println(toHex(255, 0, 0));
        System.out.println(Integer.parseUnsignedInt("a8", 16));

        System.out.println(toInt("FFFFF4"));
    }
}

