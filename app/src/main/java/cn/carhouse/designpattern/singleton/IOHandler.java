package cn.carhouse.designpattern.singleton;

/**
 * 单例模式，静态内部类
 */
public class IOHandler {

    private IOHandler() {
    }

    public static IOHandler getInstance() {
        return IOHandlerHolder.mInstance;
    }

    private static class IOHandlerHolder {
        private static final IOHandler mInstance = new IOHandler();
    }
}

