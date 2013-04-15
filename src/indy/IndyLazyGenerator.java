package indy;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class IndyLazyGenerator implements Opcodes {

	private static final MethodType bsmType = MethodType.methodType(CallSite.class, Lookup.class, String.class, MethodType.class);
	private static final Handle BOOTSTRAP_METHOD = new Handle(Opcodes.H_INVOKESTATIC, "indy/Util", "bootstrap", bsmType.toMethodDescriptorString());

	public static byte[] dump () throws Exception {


		ClassWriter cw = new ClassWriter(0);
		FieldVisitor fv;
		MethodVisitor mv;
		AnnotationVisitor av0;

		cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, "indy/IndyLazy", null, "java/lang/Object", new String[] { "indy/Lazy" });

		cw.visitSource("IndyLazy.java", null);

		{
			fv = cw.visitField(ACC_PRIVATE, "count", "I", null, null);
			fv.visitEnd();
		}
		{
			fv = cw.visitField(ACC_PRIVATE, "result", "Ljava/lang/String;", null, null);
			fv.visitEnd();
		}
		{
			fv = cw.visitField(ACC_PRIVATE + ACC_VOLATILE, "initialised", "Z", null, null);
			fv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(3, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(7, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ICONST_0);
			mv.visitFieldInsn(PUTFIELD, "indy/IndyLazy", "initialised", "Z");
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(3, l2);
			mv.visitInsn(RETURN);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLocalVariable("this", "Lindy/IndyLazy;", null, l0, l3, 0);
			mv.visitMaxs(2, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PRIVATE, "checkInit", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			Label l1 = new Label();
			Label l2 = new Label();
			mv.visitTryCatchBlock(l0, l1, l2, null);
			Label l3 = new Label();
			mv.visitTryCatchBlock(l2, l3, l2, null);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(10, l4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "indy/IndyLazy", "initialised", "Z");
			Label l5 = new Label();
			mv.visitJumpInsn(IFNE, l5);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(11, l6);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(DUP);
			mv.visitVarInsn(ASTORE, 1);
			mv.visitInsn(MONITORENTER);
			mv.visitLabel(l0);
			mv.visitLineNumber(12, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "indy/IndyLazy", "initialised", "Z");
			Label l7 = new Label();
			mv.visitJumpInsn(IFNE, l7);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(13, l8);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "indy/IndyLazy", "init", "()V");
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(14, l9);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ICONST_1);
			mv.visitFieldInsn(PUTFIELD, "indy/IndyLazy", "initialised", "Z");
			mv.visitLabel(l7);
			mv.visitLineNumber(11, l7);
			mv.visitFrame(Opcodes.F_APPEND,1, new Object[] {"indy/IndyLazy"}, 0, null);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitInsn(MONITOREXIT);
			mv.visitLabel(l1);
			mv.visitJumpInsn(GOTO, l5);
			mv.visitLabel(l2);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {"java/lang/Throwable"});
			mv.visitVarInsn(ALOAD, 1);
			mv.visitInsn(MONITOREXIT);
			mv.visitLabel(l3);
			mv.visitInsn(ATHROW);
			mv.visitLabel(l5);
			mv.visitLineNumber(18, l5);
			mv.visitFrame(Opcodes.F_CHOP,1, null, 0, null);
			mv.visitInsn(RETURN);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLocalVariable("this", "Lindy/IndyLazy;", null, l4, l10, 0);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PRIVATE, "init", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(21, l0);
			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			mv.visitLdcInsn("Initialisation");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(22, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "indy/Util", "veryExpensiveComputation1", "()I");
			mv.visitFieldInsn(PUTFIELD, "indy/IndyLazy", "count", "I");
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(23, l2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "indy/Util", "veryExpensiveComputation2", "()Ljava/lang/String;");
			mv.visitFieldInsn(PUTFIELD, "indy/IndyLazy", "result", "Ljava/lang/String;");
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(24, l3);
			mv.visitInsn(RETURN);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLocalVariable("this", "Lindy/IndyLazy;", null, l0, l4, 0);
			mv.visitMaxs(2, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "getCount", "()I", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(28, l0);
//			mv.visitVarInsn(ALOAD, 0);
//			mv.visitMethodInsn(INVOKESPECIAL, "indy/IndyLazy", "checkInit", "()V");
			mv.visitVarInsn(ALOAD, 0);
//			mv.visitMethodInsn(INVOKESPECIAL, "indy/LazyImpl", "checkInit", "(Lindy/Lazy;)V");
			mv.visitInvokeDynamicInsn("checkInit", "(Lindy/Lazy;)V", BOOTSTRAP_METHOD);

//			mv.visitInvokeDynamicInsn("checkInit", "()V", BOOTSTRAP_METHOD);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(29, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "indy/IndyLazy", "count", "I");
			mv.visitInsn(IRETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this", "Lindy/IndyLazy;", null, l0, l2, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "getResult", "()Ljava/lang/String;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(34, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "indy/IndyLazy", "checkInit", "()V");
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(35, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "indy/IndyLazy", "result", "Ljava/lang/String;");
			mv.visitInsn(ARETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this", "Lindy/IndyLazy;", null, l0, l2, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		cw.visitEnd();

		return cw.toByteArray();
	}

	public static void main(String[] args) throws Exception {
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File("lib/IndyLazy.jar")));
		zos.putNextEntry(new ZipEntry("indy/IndyLazy.class"));
		zos.write(dump());
		zos.flush();
		zos.close();
	}
}
