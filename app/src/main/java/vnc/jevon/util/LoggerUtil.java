package vnc.jevon.util;

import org.slf4j.Logger;

/**
 * User: fujinjun
 * Date: 13-4-9
 * Time: 下午4:19
 */
public class LoggerUtil {
    public static void print(Exception e, Logger logger) {
        logger.error(e.toString());
        for (StackTraceElement elem : e.getStackTrace()) {
            logger.error(elem.toString());
        }
    }
}
