package vnc.jevon.util;

import java.io.File;

/**
 * User: fujinjun
 * Date: 13-4-17
 * Time: 上午9:08
 */
public class FileUtil {
    private static final String root = "/sdcard/ds.sinodevice/";

    /**
     * 判断文件是否存在
     *
     * @param folder
     * @param filename
     * @return
     */
    public static boolean check(String folder, String filename) {
        File file = new File(root + folder + "/" + filename);
        return file.exists();
    }
}
