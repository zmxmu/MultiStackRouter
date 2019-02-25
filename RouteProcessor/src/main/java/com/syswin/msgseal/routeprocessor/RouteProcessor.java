package com.syswin.msgseal.routeprocessor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;


@AutoService(Processor.class)
public class RouteProcessor extends AbstractProcessor {

    private Messager mMessager;

    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(MixStackRoute.class);
        TypeSpec spec = processElements(elements);
        try {
            if (spec != null) {
                JavaFile.builder("com.syswin.msgseal.routeprocessor", spec).build().writeTo(mFiler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private TypeSpec processElements(Set<? extends Element> elements) {
        if (elements == null || elements.size() == 0) {
            return null;
        }

        // 1. 构造参数，参数为routerMap
        // 参数类型为:HashMap<String,Class<?>>
        ParameterizedTypeName mapTypeName =
                ParameterizedTypeName.get(ClassName.get(HashMap.class), ClassName.get(String
                        .class), ClassName.get(Class.class));
        ParameterSpec mapSpec = ParameterSpec.builder(mapTypeName, "routerMap").build();
        MethodSpec.Builder initMethodBuilder =
                MethodSpec.methodBuilder("initPageMap").addModifiers(Modifier.PUBLIC,
                        Modifier.STATIC).addParameter(mapSpec);

        for (Element element : elements) {
            MixStackRoute route = element.getAnnotation(MixStackRoute.class);
            String url = route.url();
            if (null != url && !"".equals(url)) {
                initMethodBuilder.addStatement("routerMap.put($S,$T.class)", url, ClassName.get
                        ((TypeElement) element));
            }
        }

        return TypeSpec.classBuilder("RouteMap").addMethod(initMethodBuilder.build())
                .addModifiers(Modifier.PUBLIC).build();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(MixStackRoute.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
