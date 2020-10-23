package SecAgent.SecAsm.Stub.Container.Pafa5;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

public class Pafa5Stub extends CommonStub {
    private final static String METHOD_NAME;

    static {
        METHOD_NAME = "com.pingan.pafa.papp.sar.SARContextBean.handleRequest(Lcom/paic/pafa/app/dto/ServiceRequest;)Lcom/paic/pafa/app/dto/ServiceResponse;";
    }

    public static String getStubName() {
        return METHOD_NAME;
    }


    /**
     * Constructs a new {@link AdviceAdapter}.
     *
     * @param api           the ASM API version implemented by this visitor. Must be one of {@link
     *                      Opcodes#ASM4}, {@link Opcodes#ASM5}, {@link Opcodes#ASM6} or {@link Opcodes#ASM7}.
     * @param methodVisitor the method visitor to which this adapter delegates calls.
     * @param access        the method's access flags (see {@link Opcodes}).
     * @param name          the method's name.
     * @param descriptor    the method's descriptor (see {@link Type Type}).
     * @param paramsInfo
     */
    public Pafa5Stub(int api, MethodVisitor methodVisitor, int access, String name, String descriptor, ParamsInfo paramsInfo) {
        super(api, methodVisitor, access, name, descriptor, paramsInfo);
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();

        debug_print_offline(String.format("[DEBUG] [%s]: %s", this.getClass().getName(), this.paramsInfo.toString()));
        debug_print_online(T_OBJECT, 0);
    }


    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
    }
}
