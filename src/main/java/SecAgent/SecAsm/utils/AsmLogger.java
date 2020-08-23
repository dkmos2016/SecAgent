package SecAgent.SecAsm.utils;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AsmLogger {

  public static void info(MethodVisitor mv, int obj_idx) {
    mv.visitVarInsn(Opcodes.ALOAD, obj_idx);
    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "SecAgent/Logger/DefaultLogger", "info", "(Ljava/lang/Object;)V", false);
  }

  public static void debug(MethodVisitor mv, int obj_idx) {
    mv.visitVarInsn(Opcodes.ALOAD, obj_idx);
    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "SecAgent/Logger/DefaultLogger", "debug", "(Ljava/lang/Object;)V", false);
  }

  public static void warn(MethodVisitor mv, int obj_idx) {
    mv.visitVarInsn(Opcodes.ALOAD, obj_idx);
    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "SecAgent/Logger/DefaultLogger", "warn", "(Ljava/lang/Object;)V", false);
  }

  public static void error(MethodVisitor mv, int obj_idx) {
    mv.visitVarInsn(Opcodes.ALOAD, obj_idx);
    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "SecAgent/Logger/DefaultLogger", "error", "(Ljava/lang/Object;)V", false);
  }

}