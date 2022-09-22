package xyz.vectlabs.adminsx.configs;

public enum ConfigPath {


    NO_PERM("NO_PERM_MSG", "No Permission!");



    private String path;
    private String value;
    private ConfigPath(String path, String defaultValue){
        this.path = path;
        this.value = defaultValue;
    }

    @Override
    public String toString(){
        return path;
    }

    public String getValue() {
        return value;
    }
}
