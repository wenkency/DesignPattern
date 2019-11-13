package cn.lven.router.compiler;

import com.google.auto.service.AutoService;

import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import cn.lven.router.annotation.Router;
import cn.lven.router.compiler.utils.Consts;
import cn.lven.router.compiler.utils.Log;
import cn.lven.router.compiler.utils.Utils;

// 相当于Activity的注册
@AutoService(Process.class)
/**
 * 处理器接收的参数 替代 {@link AbstractProcessor#getSupportedOptions()} 函数
 */
@SupportedOptions(Consts.ARGUMENTS_NAME)
/**
 * 注册给哪些注解的  替代 {@link AbstractProcessor#getSupportedAnnotationTypes()} 函数
 */
@SupportedAnnotationTypes({Consts.ANN_TYPE_ROUTE})
/**
 * 指定使用的Java版本 替代 {@link AbstractProcessor#getSupportedSourceVersion()} 函数
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class RouterProcessor extends AbstractProcessor {

    private Elements mElementUtils;
    private Types mTypeUtils;
    private Filer mFiler;
    private String mModuleName;
    private Log mLog;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        //获得apt的日志输出
        mLog = Log.newLog(processingEnvironment.getMessager());
        mElementUtils = processingEnvironment.getElementUtils();
        mTypeUtils = processingEnvironment.getTypeUtils();
        mFiler = processingEnvironment.getFiler();
        //参数是模块名 为了防止多模块/组件化开发的时候 生成相同的 xx$$ROOT$$文件
        Map<String, String> options = processingEnvironment.getOptions();
        if (!Utils.isEmpty(options)) {
            mModuleName = options.get(Consts.ARGUMENTS_NAME);
        }
        mLog.i("RouteProcessor Parmaters:" + mModuleName);
        if (Utils.isEmpty(mModuleName)) {
            throw new RuntimeException("Not set Processor Parmaters.");
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!Utils.isEmpty(set)) {
            // 被Router注解 注解的节点集合(Router注解下面的Activity节点的集合)
            Set<? extends Element> routerElements = roundEnvironment.getElementsAnnotatedWith(Router.class);
            if (!Utils.isEmpty(routerElements)) {
                processRoute(routerElements);
                return true;
            }
        }
        return false;
    }

    /**
     * 处理被注解的节点
     */
    private void processRoute(Set<? extends Element> elements) {
        // 获得Activity这个类的 节点信息
        TypeElement activity = mElementUtils.getTypeElement(Consts.ACTIVITY);
        for (Element element : elements) {
            RouterMeta routerMeta;
            // 注解下Activity 或者 Service类信息
            TypeMirror typeMirror = element.asType();
            mLog.i("Route class:" + typeMirror.toString());

            // 只能在指定的类上面使用
            if (mTypeUtils.isSubtype(typeMirror, activity.asType())) {

            }

        }
    }
}
