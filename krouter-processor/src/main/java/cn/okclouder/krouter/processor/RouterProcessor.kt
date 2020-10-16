package cn.okclouder.krouter.processor

import cn.okclouder.krouter.annotation.Router
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

@AutoService(Processor::class)
class RouterProcessor : AbstractProcessor() {

    companion object {
        private const val KEY_MODULE_NAME = "MODULE_NAME"

        private const val PACKAGE_NAME = "cn.okclouder.krouter"
    }

    private lateinit var elementUtils: Elements

    private lateinit var messager: Messager

    private var moduleName: String? = null

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        elementUtils = processingEnv.elementUtils
        messager = processingEnv.messager
        val options = processingEnv.options
        moduleName = options[KEY_MODULE_NAME]
        if (moduleName == null) {
            messager.printMessage(Diagnostic.Kind.ERROR, "$KEY_MODULE_NAME must not be null")
        }
    }

    override fun process(
        mutableSet: MutableSet<out TypeElement>,
        roundEnvironment: RoundEnvironment
    ): Boolean {
        val elements = roundEnvironment.getElementsAnnotatedWith(Router::class.java)
        if (elements.isNullOrEmpty()) {
            return true
        }

        val typeSpec = TypeSpec.classBuilder("$moduleName" + "Loader")
            .addModifiers(KModifier.PUBLIC)
            .build()

        val file = FileSpec.builder(PACKAGE_NAME, typeSpec.name.toString()).build()
        file.writeTo(processingEnv.filer)

        return true
    }

    override fun getSupportedOptions(): MutableSet<String> {
        return mutableSetOf(KEY_MODULE_NAME)
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val mutableSetOf = mutableSetOf<String>()
        getSupportedAnnotations().forEach {
            mutableSetOf.add(it.canonicalName)
        }
        return mutableSetOf
    }

    private fun getSupportedAnnotations(): MutableSet<Class<*>> {
        val mutableSet = mutableSetOf<Class<*>>()
        mutableSet.add(Router::class.java)
        return mutableSet
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }
}