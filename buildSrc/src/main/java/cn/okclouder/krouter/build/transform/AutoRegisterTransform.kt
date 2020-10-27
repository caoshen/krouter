package cn.okclouder.krouter.build.transform

import cn.okclouder.krouter.build.ScanConstants
import cn.okclouder.krouter.build.adapter.RouterAdapter
import cn.okclouder.krouter.build.utils.ScanCondition
import cn.okclouder.krouter.build.utils.ScanHelper
import com.android.SdkConstants
import com.android.build.api.transform.TransformInvocation
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile


class AutoRegisterTransform : DemoTransform() {
    val registerModule = arrayListOf<String>()

    var autoRegisterFile: File? = null

    override fun getScanConditions(): MutableList<ScanCondition> {
        return mutableListOf(object : ScanCondition {

            var hasRegisterInSingleClass = false

            var hasRegisterInJar = false

            override fun scan(jar: JarFile, scanEntry: JarEntry) {
                if (scanEntry.name.contains("cn")) {
                    println("scan:${scanEntry.name}")
                }

                if (scanEntry.name == ScanConstants.AUTO_REGISTER
                    || scanEntry.name == ScanConstants.AUTO_REGISTER_WINDOWS
                ) {
                    hasRegisterInJar = true
                }
                if (scanEntry.name.endsWith(SdkConstants.DOT_CLASS)) {
                    scanByte(jar.getInputStream(scanEntry))
                }
            }

            override fun scan(inputFile: File) {
                println("scan:${inputFile.name}, path:${inputFile.absolutePath}")

                if (inputFile.absolutePath.contains(ScanConstants.AUTO_REGISTER)
                    || inputFile.absolutePath.contains(ScanConstants.AUTO_REGISTER_WINDOWS)
                ) {
                    hasRegisterInSingleClass = true
                }

                if (inputFile.endsWith(SdkConstants.DOT_CLASS)) {
                    scanByte(inputFile.inputStream())
                }
            }

            override fun scanFileEnd(inputFile: File, destFile: File) {
                if (hasRegisterInSingleClass) {
                    println("注册类在 ${destFile.absolutePath}")
                    autoRegisterFile = destFile
                }
            }

            override fun scanJarEnd(inputJar: File, destJar: File) {
                if (hasRegisterInJar) {
                    println("注册类的 jar 在${destJar.absolutePath}")
                    autoRegisterFile = destJar
                }
            }
        })
    }

    private fun scanByte(inputStream: InputStream) {
        val classReader = ClassReader(inputStream)
        val classWriter = ClassWriter(0)
        val routerAdapter = RouterAdapter(classWriter, registerModule)
        classReader.accept(routerAdapter, 0)
    }

    override fun getName(): String = "AutoRegisterTransform"

    override fun isIncremental(): Boolean = false

    override fun transform(transformInvocation: TransformInvocation?) {
        println("开始父类 Transform:")
        super.transform(transformInvocation)
        autoRegisterFile?.let {
            println("转换后的类在" + it.absolutePath)

            if (it.name.endsWith(SdkConstants.DOT_CLASS)) {
                val rewrite = modifyRegisterByte(it.inputStream())
                FileOutputStream(it).write(rewrite)
            }

            if (it.name.endsWith(SdkConstants.DOT_JAR)) {
                ScanHelper.dealJarFile(it) { jarEntry, jos ->
                    val isRegisterClass = jarEntry.name == ScanConstants.AUTO_REGISTER
                    if (isRegisterClass) {
                        jos.putNextEntry(JarEntry(jarEntry.name))
                        jos.write(modifyRegisterByte(it.inputStream()))
                    }
                    isRegisterClass
                }
            }
        }
    }

    private fun modifyRegisterByte(inputStream: InputStream): ByteArray {
        val classWriter = ClassWriter(0)
        val classReader = ClassReader(inputStream)
        val classNode = ClassNode()
        classReader.accept(classNode, 0)

        classNode.methods.removeIf {
            it.name == "getAllRoutes" && it.desc == "()Ljava/util/List;"
        }
        val visitMethod = classNode.visitMethod(
            Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
            "getAllRoutes",
            "()Ljava/util/List;",
            "()Ljava/util/List<Ljava/lang/String;>;",
            null
        )

        with(visitMethod) {
            val labels = arrayListOf<Label>()

            visitCode()
            val l0 = Label()
            visitLabel(l0)
            visitTypeInsn(Opcodes.NEW, "java/util/ArrayList")
            visitInsn(Opcodes.DUP)
            visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false)
            visitVarInsn(Opcodes.ASTORE, 0)

            registerModule.forEach {
                val label = Label()
                labels.add(label)
                visitLabel(label)
                visitVarInsn(Opcodes.ALOAD, 0);
                visitTypeInsn(Opcodes.NEW, it);
                visitInsn(Opcodes.DUP);
                visitMethodInsn(Opcodes.INVOKESPECIAL, it, "<init>", "()V", false);
                visitMethodInsn(Opcodes.INVOKEVIRTUAL, it, "getRoute", "()Ljava/util/List;", false);
                visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "addAll", "(Ljava/util/Collection;)Z", true);
                visitInsn(Opcodes.POP);
            }

            val l1 = Label()
            visitLabel(l1)
            visitVarInsn(Opcodes.ALOAD, 0)
            visitInsn(Opcodes.ARETURN)
            val l2 = Label()
            visitLabel(l2)
            visitLocalVariable(
                "list",
                "Ljava/util/List;",
                "Ljava/util/List<Ljava/lang/String;>;",
                labels[0],
                l2,
                0
            )
            visitEnd()
        }

        classNode.accept(classWriter)
        return classWriter.toByteArray()
    }
}