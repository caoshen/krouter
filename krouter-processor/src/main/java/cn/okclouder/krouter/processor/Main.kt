package cn.okclouder.krouter.processor

import cn.okclouder.krouter.annotation.RouterBean
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

/**
 * @author caoshen
 * @date 2020/10/16
 */

fun main() {
    val companion = TypeSpec.companionObjectBuilder()
        .addProperty(
            PropertySpec.builder("routerLoader", String::class)
                .initializer("%S", "buzz")
                .build()
        )
        .addProperty(
            PropertySpec.builder("routerMap", HashMap::class.parameterizedBy(
                String::class,
                RouterBean::class
            ))
                .initializer("%T()", HashMap::class)
                .build()
        )
        .addInitializerBlock(CodeBlock.of("%N[%S] = %T(%T, %S, %S)", "routerMap", "path", RouterBean::class, RouterProcessor::class.asClassName(), "path", "group"))
        .addFunction(
            FunSpec.builder("beep")
                .addStatement("println(%S)", "Beep!")
                .build()
        )
        .build()

    val helloWorld = TypeSpec.classBuilder("HelloWorld")
        .addType(companion)
        .build()

    val file = FileSpec.builder("", "HelloWorld")
        .addType(helloWorld)
        .build()
    file.writeTo(System.out)
}