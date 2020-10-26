package cn.okclouder.krouter.build.adapter

import cn.okclouder.krouter.build.ScanConstants
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

/**
 * @author caoshen
 * @date 2020/10/26
 */
class RouterAdapter(cw: ClassWriter, var registerModule: ArrayList<String>) : ClassVisitor(Opcodes.ASM6, cw), Opcodes {
    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        interfaces?.find {
            it == ScanConstants.IMODULE
        }?.let {
            registerModule.add(name)
        }
    }
}