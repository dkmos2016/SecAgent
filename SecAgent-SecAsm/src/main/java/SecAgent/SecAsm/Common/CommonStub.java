package SecAgent.SecAsm.Common;

import SecAgent.Utils.utils.ParamsInfo;
import SecAgent.Utils.utils.ReqInfo;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public abstract class CommonStub extends AdviceAdapter implements Opcodes {
    protected static final int T_OBJECT = 1111111;
    // new Throwable()
    protected final int tmp_sb = newLocal(Type.getType(StringBuilder.class));
    protected final int tmp_obj = newLocal(Type.getType(Object.class));
    protected final int bak_obj = newLocal(Type.getType(Object.class));
    protected final int flag_idx = newLocal(Type.getType(int.class));
    protected final int res_idx = newLocal(Type.getType(Object.class));
    protected final int reqinfo_idx = newLocal(Type.getType(ReqInfo.class));
    protected final ParamsInfo paramsInfo;
    /**
     * for invoke (nonestatic)
     */
    protected final int inst_idx = newLocal(Type.getType(Object.class));
    /**
     * for mybatis
     */
    protected final int params_idx = newLocal(Type.getType(Object[].class));
    protected final int null_idx = newLocal(Type.getType(Object[].class));
    private final int stk_idx = newLocal(Type.getType(Throwable.class));
    private final int cls_idx = newLocal(Type.getType(Class.class));
    private final int method_idx = newLocal(Type.getType(Method.class));
    /**
     * for invoke
     */
    private final int param4Invoke_idx = newLocal(Type.getType(Object[].class));

    /**
     * Constructs a new {@link AdviceAdapter}.
     *
     * @param api           the ASM API version implemented by this visitor. Must be one of {@link
     *                      Opcodes#ASM4}, {@link Opcodes#ASM5}, {@link Opcodes#ASM6} or {@link Opcodes#ASM7}.
     * @param methodVisitor the method visitor to which this adapter delegates calls.
     * @param access        the method's access flags (see {@link Opcodes}).
     * @param name          the method's name.
     * @param descriptor    the method's descriptor (see {@link Type Type}).
     */
    public CommonStub(
            int api,
            MethodVisitor methodVisitor,
            int access,
            String name,
            String descriptor,
            ParamsInfo paramsInfo) {
        super(api, methodVisitor, access, name, descriptor);
        this.paramsInfo = paramsInfo;

        initExtras();
    }

    /**
     * initialize all local
     */
    private void initExtras() {
        setNull(this.tmp_obj);
        setNull(this.stk_idx);
        setNull(this.res_idx);
        setNull(this.reqinfo_idx);
        setNull(this.inst_idx);
        setNull(this.cls_idx);
        setNull(this.method_idx);
        setNull(this.param4Invoke_idx);
        setNull(this.null_idx);
    }

    /**
     * Object o = null;
     *
     * @param obj_idx
     */
    protected void setNull(int obj_idx) {
        mv.visitInsn(ACONST_NULL);
        mv.visitVarInsn(ASTORE, obj_idx);
    }

    protected void debug_print_tid() {
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitMethodInsn(
                INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Thread", "getId", "()J", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(J)V", false);
    }

    protected void debug_print_offline(String msg) {
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn(msg);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }

    protected void debug_print_online(int type, int obj_idx) {
        String desc;

        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        switch (type) {
            case T_BYTE:
            case T_SHORT:
            case T_INT:
                desc = "(I)V";
                mv.visitVarInsn(ILOAD, obj_idx);
                break;
            case T_BOOLEAN:
                desc = "(Z)V";
                mv.visitVarInsn(ILOAD, obj_idx);
                break;

            case T_CHAR:
                desc = "(C)V";
                mv.visitVarInsn(ILOAD, obj_idx);
                break;

            case T_LONG:
                desc = "(J)V";
                mv.visitVarInsn(LLOAD, obj_idx);
                break;

            case T_DOUBLE:
                desc = "(J)V";
                mv.visitVarInsn(DLOAD, obj_idx);
                break;

            case T_FLOAT:
                desc = "(J)V";
                mv.visitVarInsn(FLOAD, obj_idx);
                break;

            case T_OBJECT:
            default:
                desc = "(Ljava/lang/Object;)V";
                mv.visitVarInsn(ALOAD, obj_idx);
                break;
        }
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", desc, false);
    }

    /**
     * generate new instance of cls, and saved to target
     *
     * @param cls:    classname, ex: java/lang/StringBuilder
     * @param target: index of instance, ex: tmp_sb
     */
    protected void newInstance(String cls, int target) {
        mv.visitTypeInsn(NEW, cls);
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, cls, "<init>", "()V", false);
        mv.visitVarInsn(ASTORE, target);
    }

    /**
     * generate new instance of cls, and saved to target
     *
     * @param cls:    classname, ex: java/lang/StringBuilder
     * @param target: index of instance, ex: tmp_sb
     */
    protected void newInstance(Class cls, int target) {
        String classname = cls.getName().replace('.', '/');
//    mv.visitTypeInsn(NEW, name);
//    mv.visitInsn(DUP);
//    mv.visitMethodInsn(INVOKESPECIAL, name, "<init>", "()V", false);
//    mv.visitVarInsn(ASTORE, target);

        newInstance(classname, target);
    }

    /**
     * new StringBuilder()
     *
     * @param sb_idx
     */
    protected void newStringBuilder(int sb_idx) {
        this.newInstance("java/lang/StringBuilder", sb_idx);
    }

    //

    /**
     * new ArrayList()
     *
     * @param dst_idx
     */
    protected void newArrayList(int dst_idx) {
        this.newInstance("java/util/ArrayList", dst_idx);
    }

    /**
     * sb.append(obj.toString());
     *
     * @param sb_idx:  index of StringBuilder's instance
     * @param obj_idx: index of target
     */
    protected void append(int sb_idx, int obj_idx) {
        mv.visitVarInsn(ALOAD, sb_idx);
        mv.visitVarInsn(ALOAD, obj_idx);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/StringBuilder",
                "append",
                "(Ljava/lang/Object;)Ljava/lang/StringBuilder;",
                false);
        mv.visitInsn(POP);
    }

    /**
     * sb.append(str);
     *
     * @param sb_idx: index of StringBuilder's instance
     * @param sep:    index of target
     */
    protected void append(int sb_idx, String sep) {
        mv.visitVarInsn(ALOAD, sb_idx);
        mv.visitLdcInsn(sep);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/StringBuilder",
                "append",
                "(Ljava/lang/Object;)Ljava/lang/StringBuilder;",
                false);
        mv.visitInsn(POP);
    }

    private Type getType(int type, Class cls) {
        switch (type) {
            case T_INT:
                return Type.INT_TYPE;

            case T_DOUBLE:
                return Type.DOUBLE_TYPE;

            case T_FLOAT:
                return Type.FLOAT_TYPE;

            case T_LONG:
                return Type.LONG_TYPE;

            case T_BOOLEAN:
                return Type.BOOLEAN_TYPE;

            case T_SHORT:
                return Type.SHORT_TYPE;

            case T_CHAR:
                return Type.CHAR_TYPE;

            case T_BYTE:
                return Type.BYTE_TYPE;

            case T_OBJECT:
            default:
                return Type.getType(cls.getClass());
        }
    }

    /**
     * for basic type, not implement Array
     *
     * @param type
     * @return
     */
    private int getOpCode(int type) {
        switch (type) {
            case T_BYTE:
            case T_SHORT:
            case T_INT:
            case T_BOOLEAN:
            case T_CHAR:
                return ILOAD;

            case T_LONG:
                return LLOAD;

            case T_DOUBLE:
                return DLOAD;

            case T_FLOAT:
                return FLOAD;

            case T_OBJECT:
            default:
                return ALOAD;
        }
    }

    /**
     * map.put(xx)
     *
     * @param map_idx HashMap's index
     * @param key     value's key
     * @param type    value's type
     * @param obj_idx value's index
     */
    protected void put(int map_idx, String key, int type, int obj_idx) {
        mv.visitVarInsn(ALOAD, map_idx);
        mv.visitLdcInsn(key);
        mv.visitVarInsn(getOpCode(type), obj_idx);
//    mv.visitInsn(POP);

//    System.out.println(getOpCode(type));


        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", Type.getMethodDescriptor(Type.getType(Object.class), Type.getType(Object.class), Type.getType(Object.class)), true);
        mv.visitInsn(POP);
    }

    /**
     * sb.appent(o)
     *
     * @param type
     * @param obj_idx
     */
    @Deprecated
    protected void append(int sb_idx, int type, int obj_idx) {
        mv.visitVarInsn(ALOAD, sb_idx);

        String desc = "(Ljava/lang/Object;)Ljava/lang/StringBuilder;";
        switch (type) {
            case T_BYTE:
            case T_SHORT:
            case T_INT:
                desc = "(I)Ljava/lang/StringBuilder;";
                mv.visitVarInsn(ILOAD, obj_idx);
                break;

            case T_BOOLEAN:
                desc = "(Z)Ljava/lang/StringBuilder;";
                mv.visitVarInsn(ILOAD, obj_idx);
                break;

            case T_CHAR:
                desc = "(C)Ljava/lang/StringBuilder;";
                mv.visitVarInsn(ILOAD, obj_idx);
                break;

            case T_LONG:
                desc = "(J)Ljava/lang/StringBuilder;";
                mv.visitVarInsn(LLOAD, obj_idx);
                break;

            case T_DOUBLE:
                desc = "(J)Ljava/lang/StringBuilder;";
                mv.visitVarInsn(DLOAD, obj_idx);
                break;

            case T_FLOAT:
                desc = "(J)Ljava/lang/StringBuilder;";
                mv.visitVarInsn(FLOAD, obj_idx);
                break;

            case T_OBJECT:
            default:
                desc = "(Ljava/lang/Object;)Ljava/lang/StringBuilder;";
                mv.visitVarInsn(ALOAD, obj_idx);
                break;
        }

        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", desc, false);
        mv.visitInsn(POP);
    }

    /**
     * sb.appent(o)
     *
     * @param type
     * @param obj_idx
     */
    @Deprecated
    protected void setValue(int tar_idx, int type, Object obj_idx) {
        mv.visitLdcInsn(obj_idx);
        switch (type) {
            case T_BYTE:
            case T_SHORT:
            case T_INT:
            case T_BOOLEAN:
            case T_CHAR:
                mv.visitVarInsn(ISTORE, tar_idx);
                break;

            case T_LONG:
                mv.visitVarInsn(LSTORE, tar_idx);
                break;

            case T_DOUBLE:
                mv.visitVarInsn(DSTORE, tar_idx);
                break;

            case T_FLOAT:
                mv.visitVarInsn(FSTORE, tar_idx);
                break;

            case T_OBJECT:
            default:
                mv.visitVarInsn(ASTORE, tar_idx);
                break;
        }
    }

    /**
     * dst = sb.toString();
     *
     * @param sb_idx
     * @param dst_idx
     */
    protected void toStr(int sb_idx, int dst_idx) {
        mv.visitVarInsn(ALOAD, sb_idx);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitVarInsn(ASTORE, dst_idx);
    }

    protected void Exchange(int src_idx, int dst_idx) {
        mv.visitVarInsn(ALOAD, src_idx);
        mv.visitVarInsn(ASTORE, bak_obj);
        mv.visitVarInsn(ALOAD, dst_idx);
        mv.visitVarInsn(ASTORE, src_idx);
        mv.visitVarInsn(ALOAD, bak_obj);
        mv.visitVarInsn(ASTORE, dst_idx);
    }

    /**
     * use thread to dump current classloader
     */
    //  @Deprecated
    protected void classLoaderInfo() {
        // classloader info
        Label try_start = new Label();
        Label try_target = new Label();
        Label try_excep = new Label();

        mv.visitTryCatchBlock(try_start, try_target, try_excep, "java/lang/ClassNotFoundException");
        mv.visitLabel(try_start);

        mv.visitMethodInsn(
                INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/Thread",
                "getContextClassLoader",
                "()Ljava/lang/ClassLoader;",
                false);
        mv.visitVarInsn(ASTORE, tmp_obj);

        mv.visitJumpInsn(GOTO, try_target);
        mv.visitLabel(try_excep);
        mv.visitFrame(F_SAME1, 0, null, 1, new Object[]{"java/lang/ClassNotFoundException"});
        //    mv.visitVarInsn(ASTORE, tmp_obj);
        mv.visitMethodInsn(
                INVOKESTATIC,
                "SecAgent/Utils/Logger/ExceptionLogger",
                "doExpLogger",
                "(Ljava/lang/Exception;)V",
                false);

        //    mv.visitVarInsn(ALOAD, tmp_obj);
        //    mv.visitMethodInsn(
        //        INVOKEVIRTUAL, "java/lang/ClassNotFoundException", "printStackTrace", "()V", false);
        mv.visitLabel(try_target);

        mv.visitFrame(F_SAME, 0, null, 0, null);
    }

    /**
     * get ReqInfo from ThreadLocal
     *
     * @param reqinfo_idx
     */
    protected void getGlobalReqInfo(int reqinfo_idx) {
        //    AsmReqLocalOp.getReqInfo(mv, reqinfo_idx);
        //    debug_print_offline("hello");

        findAndExecute("SecAgent.Utils.utils.ReqLocal", "getReqInfo", null, null_idx, null_idx, reqinfo_idx);
    }

    /**
     * save log data to ReqInfo
     *
     * @param type
     * @param src_idx
     */
    protected void putStubData(String type, int src_type, int src_idx) {
        Label if_null = new Label();
        //    if type == null
        mv.visitVarInsn(ALOAD, src_idx);
        mv.visitJumpInsn(IFNULL, if_null);

        // if reqinfo_idx == null
        //    mv.visitVarInsn(ALOAD, reqinfo_idx);
        //    mv.visitJumpInsn(IFNULL, if_null);
        //
        //    newArrayList(param4Invoke_idx);
        //
        //    findAndExecute(
        //        "SecAgent.utils.ReqInfo",
        //        "isALLOWED_PUT_STUB",
        //        new Class[] {},
        //        reqinfo_idx,
        //        param4Invoke_idx,
        //        tmp_obj);
        //    mv.visitVarInsn(ALOAD, tmp_obj);
        //    mv.visitJumpInsn(IFNULL, if_null);
        //    mv.visitVarInsn(ALOAD, tmp_obj);
        //    mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
        //    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
        //    mv.visitJumpInsn(IFEQ, if_null);
        newArrayList(param4Invoke_idx);
        mv.visitLdcInsn(type);
        mv.visitVarInsn(ASTORE, tmp_obj);
        addListElement(param4Invoke_idx, T_OBJECT, tmp_obj);

        newInstance("java/lang/Throwable", stk_idx);
        addListElement(param4Invoke_idx, T_OBJECT, stk_idx);
        addListElement(param4Invoke_idx, T_OBJECT, src_idx);

        //    mv.visitVarInsn(ALOAD, src_idx);
        //    mv.visitMethodInsn(
        //      INVOKEVIRTUAL, "java/util/ArrayList", "toArray", "()[Ljava/lang/Object;", false);
        //    mv.visitVarInsn(ASTORE, tmp_obj);
        //    addListElement(param4Invoke_idx, src_type, tmp_obj);

        findAndExecute(
                "SecAgent.Utils.utils.ReqInfo",
                "putStubData",
                new Class[]{String.class, Throwable.class, Object.class},
                reqinfo_idx,
                param4Invoke_idx,
                res_idx);

        mv.visitLabel(if_null);
    }

    private void hasInterface(int obj_idx, int dst_idx) {
        Label if_null = new Label();
        Label if_end = new Label();
        mv.visitInsn(ICONST_0);
        mv.visitVarInsn(ISTORE, dst_idx);

        mv.visitVarInsn(ALOAD, obj_idx);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "java/lang/Class", "getInterfaces", "()[Ljava/lang/Class;", false);
        mv.visitInsn(ARRAYLENGTH);

        mv.visitJumpInsn(IFLE, if_null);

        mv.visitInsn(ICONST_1);
        mv.visitVarInsn(ISTORE, dst_idx);
        //    debug_print_offline("has interface");
        mv.visitJumpInsn(GOTO, if_end);
        mv.visitLabel(if_null);
        //    debug_print_offline("does not have interface");
        mv.visitLabel(if_end);
    }

    private Object getInnerNameforClass(Type type) {

        return type.getInternalName();
    }

    /**
     * find method with paramTypes, and execute
     *
     * @param classname:        class'name, ex: java.lang.Math
     * @param methodname:       method name
     * @param paramTypes:       method parameters' type, int.class, Object.class...
     * @param inst_idx:         instance of object
     * @param param4Invoke_idx: params index of real stack, ArrayList
     * @param dst_idx:          save result to dst_idx
     */
    protected void findAndExecute(
            String classname,
            String methodname,
            Class[] paramTypes,
            int inst_idx,
            int param4Invoke_idx,
            int dst_idx) {
        //    debug_print_offline("findAndExecute 1 " + classname + "." + methodname);

        Label try_end_all = new Label();

        Label try_start0 = new Label();
        Label try_end0 = new Label();
        Label try_excep0 = new Label();

        mv.visitTryCatchBlock(try_start0, try_end0, try_excep0, "java/lang/Exception");
        mv.visitLabel(try_start0);

        //        debug_print_offline("to load: " + classname);
        loadClass(classname, cls_idx);
        //    debug_print_online(T_OBJECT, cls_idx);
        //    debug_print_offline("find: " + methodname);
        getDeclaredMethod(cls_idx, methodname, paramTypes, method_idx);
        //        debug_print_online(T_OBJECT, method_idx);
        invoke(method_idx, inst_idx, param4Invoke_idx, dst_idx);
        //        debug_print_offline("invoke done. " );

        mv.visitLabel(try_end0);

        mv.visitJumpInsn(GOTO, try_end_all);

        mv.visitLabel(try_excep0);
        mv.visitFrame(F_SAME1, 0, null, 1, new Object[]{"java/lang/Exception"});
        mv.visitVarInsn(ASTORE, tmp_obj);

        //   ExceptionLogger.doExpLog(Exception e)
        Label try_start1 = new Label();
        Label try_excep1 = new Label();
        Label try_end1 = new Label();

        mv.visitTryCatchBlock(try_start1, try_end1, try_excep1, "java/lang/Exception");
        mv.visitLabel(try_start1);

        newArrayList(param4Invoke_idx);
        addListElement(param4Invoke_idx, T_OBJECT, tmp_obj);
        loadClass("SecAgent.Utils.Logger.ExceptionLogger", cls_idx);
        getDeclaredMethod(cls_idx, "doExpLog", new Class[]{Exception.class}, method_idx);
        invoke(method_idx, null_idx, param4Invoke_idx, dst_idx);
        mv.visitLabel(try_end1);

        mv.visitJumpInsn(GOTO, try_end_all);

        int size = this.paramsInfo.getIn_types().length + 1;
        ArrayList tmp_list;

        if (Modifier.isStatic(this.paramsInfo.getAccess())) {

            tmp_list = new ArrayList(size);
        } else {

            size += 1;
            tmp_list = new ArrayList(size);
            tmp_list.add(this.paramsInfo.getClazz().replace('.', '/'));
        }

        for (Type type : this.paramsInfo.getIn_types()) {
            tmp_list.add(getInnerNameforClass(type));
        }
        tmp_list.add("java/lang/Exception");

        mv.visitLabel(try_excep1);
        mv.visitFrame(F_FULL, size, tmp_list.toArray(), 1, new Object[]{"java/lang/Exception"});
        //    mv.visitVarInsn(ASTORE, tmp_obj);
        ////        mv.visitVarInsn(ALOAD, tmp_obj);
        //
        //    mv.visitVarInsn(ALOAD, tmp_obj);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "printStackTrace", "()V", false);

        mv.visitLabel(try_end_all);

        mv.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
    }

    /**
     *
     * @param obj_idx
     * @param dst_idx
     * @param proxyFactory
     */
    protected void findAndGetSecProxyInstance(int obj_idx, int dst_idx, int proxyFactory) {
        Label try_end_all = new Label();
        Label try_start0 = new Label();
        Label try_end0 = new Label();
        Label try_excep0 = new Label();
        Label if_false = new Label();

        mv.visitTryCatchBlock(try_start0, try_end0, try_excep0, "java/lang/Exception");
        mv.visitLabel(try_start0);

        hasInterface(obj_idx, flag_idx);
        mv.visitVarInsn(ILOAD, flag_idx);
        mv.visitJumpInsn(IFEQ, if_false);

        loadClassFromJar(proxyFactory, cls_idx);

        Class[] paramTypes = new Class[]{Object.class};
        getConstructor(cls_idx, paramTypes, method_idx);

        newArrayList(param4Invoke_idx);
        addListElement(param4Invoke_idx, T_OBJECT, obj_idx);

        invokeConstructor(method_idx, param4Invoke_idx, inst_idx);
        getDeclaredMethod(cls_idx, "getProxyInstance", new Class[]{}, method_idx);

        newArrayList(param4Invoke_idx);
        invoke(method_idx, inst_idx, param4Invoke_idx, dst_idx);

        mv.visitLabel(try_end0);

        mv.visitJumpInsn(GOTO, try_end_all);

        mv.visitLabel(try_excep0);
        mv.visitFrame(F_SAME1, 0, null, 1, new Object[]{"java/lang/Exception"});
        mv.visitVarInsn(ASTORE, tmp_obj);

        //   ExceptionLogger.doExpLog(Exception e)
        Label try_start1 = new Label();
        Label try_excep1 = new Label();
        Label try_end1 = new Label();

        mv.visitTryCatchBlock(try_start1, try_end1, try_excep1, "java/lang/Exception");
        mv.visitLabel(try_start1);

        newArrayList(param4Invoke_idx);
        addListElement(param4Invoke_idx, T_OBJECT, tmp_obj);
        loadClass("SecAgent.Utils.Logger.ExceptionLogger", cls_idx);
        getDeclaredMethod(cls_idx, "doExpLog", new Class[]{Exception.class}, method_idx);
        invoke(method_idx, null_idx, param4Invoke_idx, dst_idx);
        mv.visitLabel(try_end1);

        mv.visitJumpInsn(GOTO, try_end_all);

        int size = this.paramsInfo.getIn_types().length + 1;
        ArrayList tmp_list;

        if (Modifier.isStatic(this.paramsInfo.getAccess())) {

            tmp_list = new ArrayList(size);
        } else {

            size += 1;
            tmp_list = new ArrayList(size);
            tmp_list.add(this.paramsInfo.getClazz().replace('.', '/'));
        }

        for (Type type : this.paramsInfo.getIn_types()) {
            tmp_list.add(getInnerNameforClass(type));
        }
        tmp_list.add("java/lang/Exception");

        mv.visitLabel(try_excep1);
        mv.visitFrame(F_FULL, size, tmp_list.toArray(), 1, new Object[]{"java/lang/Exception"});
        //    mv.visitVarInsn(ASTORE, tmp_obj);
        ////        mv.visitVarInsn(ALOAD, tmp_obj);
        //
        //    mv.visitVarInsn(ALOAD, tmp_obj);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "printStackTrace", "()V", false);

        mv.visitLabel(if_false);
        mv.visitLabel(try_end_all);

        mv.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
    }
    /**
     * for [new SecInstanceProxyFactory(Object).getProxyInstance()]
     * @param obj_idx
     * @param dst_idx
     * @param proxyFactory like "SecAgent.Container.Tomcat.Filter.SecInstanceProxyFactory"
     */
    protected void findAndGetSecProxyInstance(int obj_idx, int dst_idx, String proxyFactory) {
        if (proxyFactory == null || proxyFactory.isEmpty()) return;
        Label try_end_all = new Label();
        Label try_start0 = new Label();
        Label try_end0 = new Label();
        Label try_excep0 = new Label();
        Label if_false = new Label();

        mv.visitTryCatchBlock(try_start0, try_end0, try_excep0, "java/lang/Exception");
        mv.visitLabel(try_start0);

        hasInterface(obj_idx, flag_idx);
        mv.visitVarInsn(ILOAD, flag_idx);
        mv.visitJumpInsn(IFEQ, if_false);

        //    debug_print_offline("to load: " + classname);
        //    debug_print_offline("getConstructor done");
//        loadClass(proxyFactory, cls_idx);
        loadClassFromJar(proxyFactory, cls_idx);

        Class[] paramTypes = new Class[]{Object.class};
        getConstructor(cls_idx, paramTypes, method_idx);

        newArrayList(param4Invoke_idx);
        addListElement(param4Invoke_idx, T_OBJECT, obj_idx);

        invokeConstructor(method_idx, param4Invoke_idx, inst_idx);
        getDeclaredMethod(cls_idx, "getProxyInstance", new Class[]{}, method_idx);

        newArrayList(param4Invoke_idx);
        invoke(method_idx, inst_idx, param4Invoke_idx, dst_idx);

        mv.visitLabel(try_end0);

        mv.visitJumpInsn(GOTO, try_end_all);

        mv.visitLabel(try_excep0);
        mv.visitFrame(F_SAME1, 0, null, 1, new Object[]{"java/lang/Exception"});
        mv.visitVarInsn(ASTORE, tmp_obj);

        //   ExceptionLogger.doExpLog(Exception e)
        Label try_start1 = new Label();
        Label try_excep1 = new Label();
        Label try_end1 = new Label();

        mv.visitTryCatchBlock(try_start1, try_end1, try_excep1, "java/lang/Exception");
        mv.visitLabel(try_start1);

        newArrayList(param4Invoke_idx);
        addListElement(param4Invoke_idx, T_OBJECT, tmp_obj);
        loadClass("SecAgent.Utils.Logger.ExceptionLogger", cls_idx);
        getDeclaredMethod(cls_idx, "doExpLog", new Class[]{Exception.class}, method_idx);
        invoke(method_idx, null_idx, param4Invoke_idx, dst_idx);
        mv.visitLabel(try_end1);

        mv.visitJumpInsn(GOTO, try_end_all);

        int size = this.paramsInfo.getIn_types().length + 1;
        ArrayList tmp_list;

        if (Modifier.isStatic(this.paramsInfo.getAccess())) {

            tmp_list = new ArrayList(size);
        } else {

            size += 1;
            tmp_list = new ArrayList(size);
            tmp_list.add(this.paramsInfo.getClazz().replace('.', '/'));
        }

        for (Type type : this.paramsInfo.getIn_types()) {
            tmp_list.add(getInnerNameforClass(type));
        }
        tmp_list.add("java/lang/Exception");

        mv.visitLabel(try_excep1);
        mv.visitFrame(F_FULL, size, tmp_list.toArray(), 1, new Object[]{"java/lang/Exception"});
        //    mv.visitVarInsn(ASTORE, tmp_obj);
        ////        mv.visitVarInsn(ALOAD, tmp_obj);
        //
        //    mv.visitVarInsn(ALOAD, tmp_obj);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "printStackTrace", "()V", false);

        mv.visitLabel(if_false);
        mv.visitLabel(try_end_all);

        mv.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
    }

    //  @Deprecated
    protected void findAndGetInstance(
            String classname, Class[] paramTypes, int param4Invoke_idx, int dst_idx) {
        //    debug_print_offline("findAndExecute 1 " + classname + "." + methodname);

        Label try_end_all = new Label();

        Label try_start0 = new Label();
        Label try_end0 = new Label();
        Label try_excep0 = new Label();

        mv.visitTryCatchBlock(try_start0, try_end0, try_excep0, "java/lang/Exception");
        mv.visitLabel(try_start0);

        //    debug_print_offline("to load: " + classname);
        loadClass(classname, cls_idx);
        //    debug_print_online(T_OBJECT, cls_idx);
        //    debug_print_offline("find: " + methodname);
        getConstructor(cls_idx, paramTypes, method_idx);
        //    debug_print_online(T_OBJECT, method_idx);
        invokeConstructor(method_idx, param4Invoke_idx, dst_idx);

        mv.visitLabel(try_end0);

        mv.visitJumpInsn(GOTO, try_end_all);

        mv.visitLabel(try_excep0);
        mv.visitFrame(F_SAME1, 0, null, 1, new Object[]{"java/lang/Exception"});
        mv.visitVarInsn(ASTORE, tmp_obj);

        //   ExceptionLogger.doExpLog(Exception e)
        Label try_start1 = new Label();
        Label try_excep1 = new Label();
        Label try_end1 = new Label();

        mv.visitTryCatchBlock(try_start1, try_end1, try_excep1, "java/lang/Exception");
        mv.visitLabel(try_start1);

        newArrayList(param4Invoke_idx);
        addListElement(param4Invoke_idx, T_OBJECT, tmp_obj);
        loadClass("SecAgent.Utils.Logger.ExceptionLogger", cls_idx);
        getDeclaredMethod(cls_idx, "doExpLog", new Class[]{Exception.class}, method_idx);
        invoke(method_idx, null_idx, param4Invoke_idx, dst_idx);
        mv.visitLabel(try_end1);

        mv.visitJumpInsn(GOTO, try_end_all);

        int size = this.paramsInfo.getIn_types().length + 1;
        ArrayList tmp_list;

        if (Modifier.isStatic(this.paramsInfo.getAccess())) {

            tmp_list = new ArrayList(size);
        } else {

            size += 1;
            tmp_list = new ArrayList(size);
            tmp_list.add(this.paramsInfo.getClazz().replace('.', '/'));
        }

        for (Type type : this.paramsInfo.getIn_types()) {
            tmp_list.add(getInnerNameforClass(type));
        }
        tmp_list.add("java/lang/Exception");

        mv.visitLabel(try_excep1);
        mv.visitFrame(F_FULL, size, tmp_list.toArray(), 1, new Object[]{"java/lang/Exception"});
        //    mv.visitVarInsn(ASTORE, tmp_obj);
        ////        mv.visitVarInsn(ALOAD, tmp_obj);
        //
        //    mv.visitVarInsn(ALOAD, tmp_obj);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "printStackTrace", "()V", false);

        mv.visitLabel(try_end_all);

        mv.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
    }

    /**
     * load class
     *
     * @param classname
     * @param dst_idx
     */
    @Deprecated
    private void loadClass(String classname, int dst_idx) {
        mv.visitMethodInsn(
                INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/Thread",
                "getContextClassLoader",
                "()Ljava/lang/ClassLoader;",
                false);
        mv.visitLdcInsn(classname);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/ClassLoader",
                "loadClass",
                "(Ljava/lang/String;)Ljava/lang/Class;",
                false);
        mv.visitVarInsn(ASTORE, dst_idx);
    }

    /**
     *
     * @param classname
     * @param dst_idx
     */
    private void loadClassFromJar(String classname, int dst_idx) {
        mv.visitFieldInsn(GETSTATIC, "SecAgent/Utils/Conf/Config", "jarLoader", "LSecAgent/Utils/utils/JarClassLoader;");
        mv.visitLdcInsn(classname);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/ClassLoader",
                "loadClass",
                "(Ljava/lang/String;)Ljava/lang/Class;",
                false);
        mv.visitVarInsn(ASTORE, dst_idx);
    }

    /**
     *
     * @param classname_idx
     * @param dst_idx
     */
    private void loadClassFromJar(int classname_idx, int dst_idx) {
        mv.visitFieldInsn(GETSTATIC, "SecAgent/Utils/Conf/Config", "jarLoader", "LSecAgent/Utils/utils/JarClassLoader;");
        mv.visitVarInsn(ALOAD, classname_idx);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/ClassLoader",
                "loadClass",
                "(Ljava/lang/String;)Ljava/lang/Class;",
                false);
        mv.visitVarInsn(ASTORE, dst_idx);
    }

    /**
     * find Constructor
     *
     * @param cls_idx
     * @param paramTypes
     * @param dst_idx
     */
    private void getConstructor(int cls_idx, Class[] paramTypes, int dst_idx) {
        if (paramTypes == null || paramTypes.length == 0) {
            //      debug_print_offline("no paramTypes");
            mv.visitInsn(ACONST_NULL);
        } else {
            //      debug_print_offline("more paramTypes");
            mv.visitIntInsn(BIPUSH, paramTypes.length);
            mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");

            for (int i = 0; i < paramTypes.length; i++) {
                mv.visitInsn(DUP);
                mv.visitIntInsn(BIPUSH, i);
                //        debug_print_offline(paramTypes[i].toString());
                switch (paramTypes[i].getName()) {
                    case "byte":
                        mv.visitFieldInsn(GETSTATIC, "java/lang/Byte", "TYPE", "Ljava/lang/Class;");
                        break;

                    case "short":
                        mv.visitFieldInsn(GETSTATIC, "java/lang/Short", "TYPE", "Ljava/lang/Class;");
                        break;

                    case "int":
                        mv.visitFieldInsn(GETSTATIC, "java/lang/Integer", "TYPE", "Ljava/lang/Class;");
                        break;

                    case "boolean":
                        mv.visitFieldInsn(GETSTATIC, "java/lang/Boolean", "TYPE", "Ljava/lang/Class;");
                        break;

                    case "char":
                        mv.visitFieldInsn(GETSTATIC, "java/lang/Character", "TYPE", "Ljava/lang/Class;");
                        break;

                    case "long":
                        mv.visitFieldInsn(GETSTATIC, "java/lang/Long", "TYPE", "Ljava/lang/Class;");
                        break;

                    case "double":
                        mv.visitFieldInsn(GETSTATIC, "java/lang/Double", "TYPE", "Ljava/lang/Class;");
                        break;

                    case "float":
                        mv.visitFieldInsn(GETSTATIC, "java/lang/Float", "TYPE", "Ljava/lang/Class;");
                        break;

                    default:
                        mv.visitLdcInsn(Type.getType(paramTypes[i]));
                        break;
                }
                mv.visitInsn(AASTORE);
            }
        }
        mv.visitVarInsn(ASTORE, tmp_obj);

        mv.visitVarInsn(ALOAD, cls_idx);
        mv.visitVarInsn(ALOAD, tmp_obj);

        //    debug_print_offline("to getMethod...");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/Class",
                "getConstructor",
                "([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;",
                false);

        mv.visitVarInsn(ASTORE, dst_idx);
    }

    /**
     * getDeclaredMethod
     *
     * @param cls_idx
     * @param methodname
     * @param paramTypes
     * @param dst_idx
     */
    private void getDeclaredMethod(int cls_idx, String methodname, Class[] paramTypes, int dst_idx) {
        //    debug_print_offline("getDeclaredMethod");

        if (paramTypes == null || paramTypes.length == 0) {
            //      debug_print_offline("no paramTypes");
            mv.visitInsn(ACONST_NULL);
        } else {
            //      debug_print_offline("more paramTypes");
            mv.visitIntInsn(BIPUSH, paramTypes.length);
            mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");

            for (int i = 0; i < paramTypes.length; i++) {
                mv.visitInsn(DUP);
                mv.visitIntInsn(BIPUSH, i);
                //        debug_print_offline(paramTypes[i].toString());
                switch (paramTypes[i].getName()) {
                    case "byte":
                        mv.visitFieldInsn(GETSTATIC, "java/lang/Byte", "TYPE", "Ljava/lang/Class;");
                        break;

                    case "short":
                        mv.visitFieldInsn(GETSTATIC, "java/lang/Short", "TYPE", "Ljava/lang/Class;");
                        break;

                    case "int":
                        mv.visitFieldInsn(GETSTATIC, "java/lang/Integer", "TYPE", "Ljava/lang/Class;");
                        break;

                    case "boolean":
                        mv.visitFieldInsn(GETSTATIC, "java/lang/Boolean", "TYPE", "Ljava/lang/Class;");
                        break;

                    case "char":
                        mv.visitFieldInsn(GETSTATIC, "java/lang/Character", "TYPE", "Ljava/lang/Class;");
                        break;

                    case "long":
                        mv.visitFieldInsn(GETSTATIC, "java/lang/Long", "TYPE", "Ljava/lang/Class;");
                        break;

                    case "double":
                        mv.visitFieldInsn(GETSTATIC, "java/lang/Double", "TYPE", "Ljava/lang/Class;");
                        break;

                    case "float":
                        mv.visitFieldInsn(GETSTATIC, "java/lang/Float", "TYPE", "Ljava/lang/Class;");
                        break;

                    default:
                        mv.visitLdcInsn(Type.getType(paramTypes[i]));
                        break;
                }
                mv.visitInsn(AASTORE);
            }
        }
        mv.visitVarInsn(ASTORE, tmp_obj);

        //    debug_print_offline("prepare parameter types done");
        mv.visitVarInsn(ALOAD, cls_idx);
        mv.visitLdcInsn(methodname);
        mv.visitVarInsn(ALOAD, tmp_obj);

        //    debug_print_offline("to getMethod...");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/Class",
                "getDeclaredMethod",
                "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;",
                false);
        mv.visitVarInsn(ASTORE, dst_idx);

        //    debug_print_offline("getDeclaredMethod done");
    }

    /**
     * invoke method
     *
     * @param method_idx
     * @param inst_idx
     * @param param4Invoke_idx
     * @param dst_idx
     */
    private void invoke(int method_idx, int inst_idx, int param4Invoke_idx, int dst_idx) {
        Label if_null = new Label();
        Label if_body = new Label();
        Label if_end = new Label();

        // if params == null
        mv.visitVarInsn(ALOAD, param4Invoke_idx);
        mv.visitJumpInsn(IFNULL, if_null);
        mv.visitJumpInsn(GOTO, if_body);

        // if params.length == 0
        mv.visitVarInsn(ALOAD, param4Invoke_idx);
        mv.visitInsn(ARRAYLENGTH);
        mv.visitJumpInsn(IFEQ, if_null);

        mv.visitLabel(if_body);
        mv.visitVarInsn(ALOAD, param4Invoke_idx);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "java/util/ArrayList", "toArray", "()[Ljava/lang/Object;", false);
        mv.visitVarInsn(ASTORE, tmp_obj);
        mv.visitJumpInsn(GOTO, if_end);

        mv.visitLabel(if_null);
        mv.visitInsn(ACONST_NULL);
        mv.visitVarInsn(ASTORE, tmp_obj);

        mv.visitLabel(if_end);
        //    mv.visitVarInsn(ALOAD, method_idx);
        //    mv.visitVarInsn(ALOAD, inst_idx);

        mv.visitVarInsn(ALOAD, method_idx);
        mv.visitVarInsn(ALOAD, inst_idx);
        mv.visitVarInsn(ALOAD, tmp_obj);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/reflect/Method",
                "invoke",
                "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;",
                false);
        mv.visitVarInsn(ASTORE, dst_idx);
    }

    private void invokeConstructor(int method_idx, int param4Invoke_idx, int dst_idx) {
        Label if_null = new Label();
        Label if_body = new Label();
        Label if_end = new Label();

        // if params == null
        mv.visitVarInsn(ALOAD, param4Invoke_idx);
        mv.visitJumpInsn(IFNULL, if_null);
        mv.visitJumpInsn(GOTO, if_body);

        // if params.length == 0
        mv.visitVarInsn(ALOAD, param4Invoke_idx);
        mv.visitInsn(ARRAYLENGTH);
        mv.visitJumpInsn(IFEQ, if_null);

        mv.visitLabel(if_body);
        mv.visitVarInsn(ALOAD, param4Invoke_idx);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "java/util/ArrayList", "toArray", "()[Ljava/lang/Object;", false);
        mv.visitVarInsn(ASTORE, tmp_obj);
        mv.visitJumpInsn(GOTO, if_end);

        mv.visitLabel(if_null);
        mv.visitInsn(ACONST_NULL);
        mv.visitVarInsn(ASTORE, tmp_obj);

        mv.visitLabel(if_end);
        //    mv.visitVarInsn(ALOAD, method_idx);
        //    mv.visitVarInsn(ALOAD, inst_idx);

        mv.visitVarInsn(ALOAD, method_idx);
        mv.visitVarInsn(ALOAD, tmp_obj);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/reflect/Constructor",
                "newInstance",
                "([Ljava/lang/Object;)Ljava/lang/Object;",
                false);
        mv.visitVarInsn(ASTORE, dst_idx);
    }

    protected void addListElement(int list_idx, int type, int obj_idx) {
        mv.visitVarInsn(ALOAD, list_idx);

        switch (type) {
            case T_BYTE:
                mv.visitVarInsn(ILOAD, obj_idx);
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
                break;

            case T_SHORT:
                mv.visitVarInsn(ILOAD, obj_idx);
                mv.visitMethodInsn(
                        INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
                break;

            case T_INT:
                mv.visitVarInsn(ILOAD, obj_idx);
                mv.visitMethodInsn(
                        INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
                break;

            case T_BOOLEAN:
                mv.visitVarInsn(ILOAD, obj_idx);
                mv.visitMethodInsn(
                        INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
                break;

            case T_CHAR:
                mv.visitVarInsn(ILOAD, obj_idx);
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Char", "valueOf", "(C)Ljava/lang/Char;", false);
                break;

            case T_LONG:
                mv.visitVarInsn(LLOAD, obj_idx);
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
                break;

            case T_DOUBLE:
                mv.visitVarInsn(DLOAD, obj_idx);
                mv.visitMethodInsn(
                        INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
                break;

            case T_FLOAT:
                mv.visitVarInsn(FLOAD, obj_idx);
                mv.visitMethodInsn(
                        INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
                break;

            case T_OBJECT:
            default:
                mv.visitVarInsn(ALOAD, obj_idx);
                break;
        }

        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "add", "(Ljava/lang/Object;)Z", false);
        mv.visitInsn(POP);
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();

        getGlobalReqInfo(reqinfo_idx);
    }
}
