package cn.lven.router.compiler;




import javax.lang.model.element.Element;

import cn.lven.router.annotation.Router;

/**
 * 路由信息封装
 */
public class RouterMeta {
    public enum Type {
        ACTIVITY,
        ISERVICE
    }

    private Type type;
    /**
     * 节点 (Activity)
     */
    private Element element;
    /**
     * 注解使用的类对象
     */
    private Class<?> destination;
    /**
     * 路由地址
     */
    private String path;
    /**
     * 路由组0
     */
    private String group;


    public static RouterMeta build(Type type, Class<?> destination, String path, String
            group) {
        return new RouterMeta(type, null, destination, path, group);
    }


    public RouterMeta() {
    }

    /**
     * Type
     *
     * @param route   route
     * @param element element
     */
    public RouterMeta(Type type, Router route, Element element) {
        this(type, element, null, route.path(), route.group());
    }


    public RouterMeta(Type type, Element element, Class<?> destination, String path, String
            group) {
        this.type = type;
        this.destination = destination;
        this.element = element;
        this.path = path;
        this.group = group;
    }

    public Type getType() {
        return type;
    }


    public void setType(Type type) {
        this.type = type;
    }

    public Element getElement() {
        return element;
    }

    public RouterMeta setElement(Element element) {
        this.element = element;
        return this;
    }

    public Class<?> getDestination() {
        return destination;
    }

    public RouterMeta setDestination(Class<?> destination) {
        this.destination = destination;
        return this;
    }

    public String getPath() {
        return path;
    }

    public RouterMeta setPath(String path) {
        this.path = path;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public RouterMeta setGroup(String group) {
        this.group = group;
        return this;
    }


}