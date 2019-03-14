import it.unibo.bls.utils.Utils;

public class SegChainFramework {

    private final String cmdName;

    public static SegChainFramework createTheSystem(String cmdName) {
        return new SegChainFramework(cmdName);
    }


    public SegChainFramework(String cmdName) {
        Utils.showSystemInfo();
        this.cmdName = cmdName;
        createLogicalComponents();
        configureSystemArchitecture();
    }

    protected void createLogicalComponents() {
    }

    protected void configureSystemArchitecture() {
    }

}
