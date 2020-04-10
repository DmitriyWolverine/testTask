package by.sda.task.modules;


public abstract class AbstractModule implements Runnable{
    private long number;
    private String moduleId;

    public AbstractModule(String moduleId, long number) {
        this.number = number;
        this.moduleId = moduleId;
    }

    public long getNumber() {
        return number;
    }

    public String getModuleId() {
        return moduleId;
    }

    protected void printTaskIdAndNumber(){
        System.out.println(getModuleId()+" running task "+ getNumber());
    }

    public abstract void run();

    protected abstract double doTheJob();
}
