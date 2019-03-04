package com.syswin.msgseal.routeprocessor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.syswin.msgseal.navigation.annotation.Page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class RouteProcessor extends AbstractProcessor {

    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Page.class);
        for(Element element:elements){
            TypeSpec spec = processElement(element);
            try {
                if (spec != null) {
                    JavaFile.builder("com.syswin.msgseal.routeprocessor", spec).build().writeTo(mFiler);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private TypeSpec processElement(Element element) {

        MethodSpec.Builder getClassMethodBuilder =
                MethodSpec.methodBuilder("getPageClass").addModifiers(Modifier.PUBLIC,
                        Modifier.STATIC).returns(Class.class);

        Page route = element.getAnnotation(Page.class);
        String url = route.url();
        if (null != url && !"".equals(url)) {
            getClassMethodBuilder.addStatement("return $T.class", ClassName.get
                    ((TypeElement) element));
        }
        TypeSpec.Builder builder = TypeSpec.classBuilder("Navigation_"
                +url.replace('.','_'))
                .addMethod(getClassMethodBuilder.build())
                .addModifiers(Modifier.PUBLIC);
        return builder.build();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(Page.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
