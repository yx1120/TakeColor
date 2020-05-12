package indi.xu.owercolor.version4.util;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * 文件过滤器
 *
 * @author a_apple
 * @create 2019-09-24 16:05
 */
public class MyFileFilter extends FileFilter {
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return false;
        }
        return f.getName().endsWith(".dat");
    }

    @Override
    public String getDescription() {
        return null;
    }
}
