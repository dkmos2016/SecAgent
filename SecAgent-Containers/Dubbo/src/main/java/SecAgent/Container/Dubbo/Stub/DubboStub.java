package SecAgent.Container.Dubbo.Stub;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.Utils.utils.ParamsInfo;
import SecAgent.Utils.utils.ReqInfo;
import org.objectweb.asm.MethodVisitor;

import java.util.HashMap;
import java.util.Map;

public class DubboStub extends CommonStub {

    public DubboStub(int api, MethodVisitor methodVisitor, int access, String name, String descriptor, ParamsInfo paramsInfo) {
        super(api, methodVisitor, access, name, descriptor, paramsInfo);
    }

    private void process() {
        newInstance(HashMap.class, res_idx);
        put(res_idx, "channel", T_OBJECT, 1);
        put(res_idx, "message", T_OBJECT, 2);
//        put(res_idx, "arguments", T_OBJECT, 3);
//        put(res_idx, "attachments", T_OBJECT, 4);
//        put(res_idx, "invoker", T_OBJECT, 5);

        newArrayList(params_idx);
        addListElement(params_idx, T_OBJECT, res_idx);


//        findAndExecute("SecAgent.utils.ReqInfo", "setDubboInfo", new Class[]{Map.class}, reqinfo_idx, res_idx, tmp_obj);


        findAndExecute(
                "SecAgent.Utils.utils.ReqInfo",
                "setDubboInfo",
                new Class[] {Map.class},
                reqinfo_idx,
                params_idx,
                tmp_obj);
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        debug_print_offline(String.format("[DEBUG] [%s]: %s", this.getClass().getName(), this.paramsInfo.toString()));
        process();
    }

    @Override
    protected void onMethodExit(int opcode) {
        newArrayList(params_idx);

        mv.visitLdcInsn(ReqInfo.Protocol.DUBBO.getName());
        mv.visitVarInsn(ASTORE, tmp_obj);

        addListElement(params_idx, T_OBJECT, tmp_obj);
        findAndExecute(
                "SecAgent.Utils.utils.ReqInfo", "doJob", new Class[] {String.class}, reqinfo_idx, params_idx, tmp_obj);


        newArrayList(params_idx);
        findAndExecute(
                "SecAgent.Utils.utils.ReqLocal", "clear", new Class[] {}, reqinfo_idx, params_idx, tmp_obj);


        super.onMethodExit(opcode);

    }
}
